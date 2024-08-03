package net.minecraft.realms;

import com.google.common.util.concurrent.RateLimiter;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.client.GameNarrator;
import net.minecraft.network.chat.Component;

public class RepeatedNarrator {

    private final float permitsPerSecond;

    private final AtomicReference<RepeatedNarrator.Params> params = new AtomicReference();

    public RepeatedNarrator(Duration duration0) {
        this.permitsPerSecond = 1000.0F / (float) duration0.toMillis();
    }

    public void narrate(GameNarrator gameNarrator0, Component component1) {
        RepeatedNarrator.Params $$2 = (RepeatedNarrator.Params) this.params.updateAndGet(p_175080_ -> p_175080_ != null && component1.equals(p_175080_.narration) ? p_175080_ : new RepeatedNarrator.Params(component1, RateLimiter.create((double) this.permitsPerSecond)));
        if ($$2.rateLimiter.tryAcquire(1)) {
            gameNarrator0.sayNow(component1);
        }
    }

    static class Params {

        final Component narration;

        final RateLimiter rateLimiter;

        Params(Component component0, RateLimiter rateLimiter1) {
            this.narration = component0;
            this.rateLimiter = rateLimiter1;
        }
    }
}