package com.my.sample.kafka.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.annotation.Resource;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@WebFilter(urlPatterns = "/*", initParams = {@WebInitParam(name = "includeQueryString", value = "true"),
        @WebInitParam(name = "includeClientInfo", value = "true"),
        @WebInitParam(name = "includeHeaders", value = "true"), @WebInitParam(name = "includePayload", value = "true"),
        @WebInitParam(name = "maxPayloadLength", value = "1024")})
@Slf4j
public class RequestLoggingFilter extends CommonsRequestLoggingFilter {

    @Resource
    ObjectMapper objectMapper;

    @Override
    protected void initFilterBean() throws ServletException {
        super.initFilterBean();
        FilterConfig filterConfig = getFilterConfig();
        if (filterConfig != null) {
            this.setIncludeQueryString(Boolean.valueOf(filterConfig.getInitParameter("includeQueryString")));
            this.setIncludeClientInfo(Boolean.valueOf(filterConfig.getInitParameter("includeClientInfo")));
            this.setIncludeHeaders(Boolean.valueOf(filterConfig.getInitParameter("includeHeaders")));
            this.setIncludePayload(Boolean.valueOf(filterConfig.getInitParameter("includePayload")));
            String maxPayloadLength = filterConfig.getInitParameter("maxPayloadLength");
            if (maxPayloadLength != null) {
                this.setMaxPayloadLength(Integer.valueOf(filterConfig.getInitParameter("maxPayloadLength")));
            }
        }
    }

    @Override
    protected boolean shouldLog(HttpServletRequest request) {
        return this.logger.isInfoEnabled();
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        MDC.clear();
        MDC.put("uri", request.getRequestURI());
        MDC.put("localAddr", request.getLocalAddr());
        MDC.put("reqSeq", RandomStringUtils.randomAlphabetic(10));
        request.setAttribute("timestamp", System.currentTimeMillis());
        this.logger.info(message);
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        Object start = request.getAttribute("timestamp");
        Long requestTime = System.currentTimeMillis() - Long.valueOf(Objects.toString(start, "0"));
        MDC.put("duration", requestTime.toString());
        this.logger.info(message);
    }

}
