package studios.tkoh.portfolio.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;

class InMemoryRateLimitServiceTest {

    @Test
    void blocksAfterConfiguredAttemptsInsideWindow() {
        MutableClock clock = new MutableClock(Instant.parse("2026-01-01T00:00:00Z"));
        InMemoryRateLimitService service = new InMemoryRateLimitService(clock);

        assertThat(service.tryConsume("login:127.0.0.1", 2, Duration.ofMinutes(1))).isTrue();
        assertThat(service.tryConsume("login:127.0.0.1", 2, Duration.ofMinutes(1))).isTrue();
        assertThat(service.tryConsume("login:127.0.0.1", 2, Duration.ofMinutes(1))).isFalse();
    }

    @Test
    void resetsAfterWindowExpires() {
        MutableClock clock = new MutableClock(Instant.parse("2026-01-01T00:00:00Z"));
        InMemoryRateLimitService service = new InMemoryRateLimitService(clock);

        assertThat(service.tryConsume("contact:127.0.0.1", 1, Duration.ofSeconds(10))).isTrue();
        assertThat(service.tryConsume("contact:127.0.0.1", 1, Duration.ofSeconds(10))).isFalse();

        clock.advance(Duration.ofSeconds(11));

        assertThat(service.tryConsume("contact:127.0.0.1", 1, Duration.ofSeconds(10))).isTrue();
    }

    private static final class MutableClock extends Clock {
        private Instant instant;

        private MutableClock(Instant instant) {
            this.instant = instant;
        }

        private void advance(Duration duration) {
            instant = instant.plus(duration);
        }

        @Override
        public ZoneOffset getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(java.time.ZoneId zone) {
            return this;
        }

        @Override
        public Instant instant() {
            return instant;
        }
    }
}
