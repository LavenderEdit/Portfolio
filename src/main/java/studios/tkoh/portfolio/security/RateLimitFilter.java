package studios.tkoh.portfolio.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

    private final InMemoryRateLimitService rateLimitService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Policy policy = policyFor(request);
        if (policy == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String key = policy.name() + ":" + clientIp(request);
        if (!rateLimitService.tryConsume(key, policy.maxAttempts(), policy.window())) {
            response.setStatus(429);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            String requestId = (String) request.getAttribute(RequestIdFilter.REQUEST_ID_ATTRIBUTE);
            response.getWriter().write(errorJson("RATE_LIMITED", "Demasiadas solicitudes. Intenta nuevamente mas tarde.", request.getRequestURI(), requestId));
            return;
        }

        filterChain.doFilter(request, response);
    }

    private Policy policyFor(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        if (path.startsWith("/api/auth/") && "POST".equals(method)) {
            return new Policy("auth", 10, Duration.ofMinutes(1));
        }
        if (path.matches("^/api/portfolios/[^/]+/contact$") && "POST".equals(method)) {
            return new Policy("contact", 5, Duration.ofMinutes(5));
        }
        if (path.startsWith("/api/me/upload/") && "POST".equals(method)) {
            return new Policy("upload", 20, Duration.ofMinutes(10));
        }
        return null;
    }

    private String clientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private record Policy(String name, int maxAttempts, Duration window) {
    }

    private String errorJson(String code, String message, String path, String requestId) {
        return """
                {"success":false,"message":"%s","data":null,"errorCode":"%s","path":"%s","requestId":"%s"}
                """.formatted(escape(message), escape(code), escape(path), escape(requestId));
    }

    private String escape(String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
