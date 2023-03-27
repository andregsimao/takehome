package com.example.takehome.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


@Component
public class RequestThrottleFilter implements Filter {

    private final int MAX_REQUESTS_PER_SECOND_UNAUTHENTICATED = 5;
    private final int MAX_REQUESTS_PER_SECOND_AUTHENTICATED = 20;

    private LoadingCache<String, Integer> requestCountsPerIpAddress;

    public RequestThrottleFilter(){
        super();
        requestCountsPerIpAddress = Caffeine.newBuilder().
                expireAfterWrite(1, TimeUnit.SECONDS).build(key -> 0);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String clientIpAddress = getClientIP((HttpServletRequest) servletRequest);
        String authorizationHeader = ((HttpServletRequest) servletRequest).getHeader(HttpHeaders.AUTHORIZATION);
        boolean isNotAuthenticated = authorizationHeader == null || authorizationHeader.isBlank();
        if(isMaximumRequestsPerSecondExceeded(isNotAuthenticated, clientIpAddress)){
            httpServletResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            httpServletResponse.getWriter().write("Too many requests");
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean isMaximumRequestsPerSecondExceeded(boolean isNotAuthenticated, String clientIpAddress){
        Integer requests = requestCountsPerIpAddress.get(clientIpAddress);
        int maxRequestsPerSecound = isNotAuthenticated ?
                MAX_REQUESTS_PER_SECOND_UNAUTHENTICATED : MAX_REQUESTS_PER_SECOND_AUTHENTICATED;
        if(requests == null) {
            requests = 0;
        } else if (requests > maxRequestsPerSecound) {
            requestCountsPerIpAddress.asMap().remove(clientIpAddress);
            requestCountsPerIpAddress.put(clientIpAddress, requests);
            return true;
        }
        requests++;
        requestCountsPerIpAddress.put(clientIpAddress, requests);
        return false;
    }

    public String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null){
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    @Override
    public void destroy() {

    }
}