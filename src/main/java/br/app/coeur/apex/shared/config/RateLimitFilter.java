package br.app.coeur.apex.shared.config;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Mirrors Orion's "auth" rate-limit policy: 10 requests/minute per client IP,
 * applied to account creation and every authentication endpoint.
 */
@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private static final int CAPACITY = 10;
    private static final Duration WINDOW = Duration.ofMinutes(1);

    private final ConcurrentHashMap<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (isRateLimited(request)) {
            String clientIp = resolveClientIp(request);
            Bucket bucket = buckets.computeIfAbsent(clientIp, ip -> newBucket());
            if (!bucket.tryConsume(1)) {
                response.setStatus(429);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isRateLimited(HttpServletRequest request) {
        String path = request.getRequestURI();
        boolean isCreateUser = "POST".equalsIgnoreCase(request.getMethod()) && "/api/users".equals(path);
        boolean isAuthEndpoint = path.startsWith("/api/auth/");
        return isCreateUser || isAuthEndpoint;
    }

    private String resolveClientIp(HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        return remoteAddr == null ? "unknown" : remoteAddr;
    }

    private Bucket newBucket() {
        return Bucket.builder()
                .addLimit(Bandwidth.builder().capacity(CAPACITY).refillIntervally(CAPACITY, WINDOW).build())
                .build();
    }
}
