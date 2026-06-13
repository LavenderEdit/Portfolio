package studios.tkoh.portfolio.security;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class InMemoryRateLimitService {

    private final Clock clock;
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public InMemoryRateLimitService(Clock clock) {
        this.clock = clock;
    }

    public boolean tryConsume(String key, int maxAttempts, Duration window) {
        Instant now = clock.instant();
        Bucket bucket = buckets.compute(key, (ignored, current) -> {
            if (current == null || !now.isBefore(current.resetAt())) {
                return new Bucket(1, now.plus(window));
            }
            return new Bucket(current.attempts() + 1, current.resetAt());
        });
        return bucket.attempts() <= maxAttempts;
    }

    private record Bucket(int attempts, Instant resetAt) {
    }
}
