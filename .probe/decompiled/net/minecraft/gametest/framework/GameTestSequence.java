package net.minecraft.gametest.framework;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

public class GameTestSequence {

    final GameTestInfo parent;

    private final List<GameTestEvent> events = Lists.newArrayList();

    private long lastTick;

    GameTestSequence(GameTestInfo gameTestInfo0) {
        this.parent = gameTestInfo0;
        this.lastTick = gameTestInfo0.getTick();
    }

    public GameTestSequence thenWaitUntil(Runnable runnable0) {
        this.events.add(GameTestEvent.create(runnable0));
        return this;
    }

    public GameTestSequence thenWaitUntil(long long0, Runnable runnable1) {
        this.events.add(GameTestEvent.create(long0, runnable1));
        return this;
    }

    public GameTestSequence thenIdle(int int0) {
        return this.thenExecuteAfter(int0, () -> {
        });
    }

    public GameTestSequence thenExecute(Runnable runnable0) {
        this.events.add(GameTestEvent.create(() -> this.executeWithoutFail(runnable0)));
        return this;
    }

    public GameTestSequence thenExecuteAfter(int int0, Runnable runnable1) {
        this.events.add(GameTestEvent.create(() -> {
            if (this.parent.getTick() < this.lastTick + (long) int0) {
                throw new GameTestAssertException("Waiting");
            } else {
                this.executeWithoutFail(runnable1);
            }
        }));
        return this;
    }

    public GameTestSequence thenExecuteFor(int int0, Runnable runnable1) {
        this.events.add(GameTestEvent.create(() -> {
            if (this.parent.getTick() < this.lastTick + (long) int0) {
                this.executeWithoutFail(runnable1);
                throw new GameTestAssertException("Waiting");
            }
        }));
        return this;
    }

    public void thenSucceed() {
        this.events.add(GameTestEvent.create(this.parent::m_177486_));
    }

    public void thenFail(Supplier<Exception> supplierException0) {
        this.events.add(GameTestEvent.create(() -> this.parent.fail((Throwable) supplierException0.get())));
    }

    public GameTestSequence.Condition thenTrigger() {
        GameTestSequence.Condition $$0 = new GameTestSequence.Condition();
        this.events.add(GameTestEvent.create(() -> $$0.trigger(this.parent.getTick())));
        return $$0;
    }

    public void tickAndContinue(long long0) {
        try {
            this.tick(long0);
        } catch (GameTestAssertException var4) {
        }
    }

    public void tickAndFailIfNotComplete(long long0) {
        try {
            this.tick(long0);
        } catch (GameTestAssertException var4) {
            this.parent.fail(var4);
        }
    }

    private void executeWithoutFail(Runnable runnable0) {
        try {
            runnable0.run();
        } catch (GameTestAssertException var3) {
            this.parent.fail(var3);
        }
    }

    private void tick(long long0) {
        Iterator<GameTestEvent> $$1 = this.events.iterator();
        while ($$1.hasNext()) {
            GameTestEvent $$2 = (GameTestEvent) $$1.next();
            $$2.assertion.run();
            $$1.remove();
            long $$3 = long0 - this.lastTick;
            long $$4 = this.lastTick;
            this.lastTick = long0;
            if ($$2.expectedDelay != null && $$2.expectedDelay != $$3) {
                this.parent.fail(new GameTestAssertException("Succeeded in invalid tick: expected " + ($$4 + $$2.expectedDelay) + ", but current tick is " + long0));
                break;
            }
        }
    }

    public class Condition {

        private static final long NOT_TRIGGERED = -1L;

        private long triggerTime = -1L;

        void trigger(long long0) {
            if (this.triggerTime != -1L) {
                throw new IllegalStateException("Condition already triggered at " + this.triggerTime);
            } else {
                this.triggerTime = long0;
            }
        }

        public void assertTriggeredThisTick() {
            long $$0 = GameTestSequence.this.parent.getTick();
            if (this.triggerTime != $$0) {
                if (this.triggerTime == -1L) {
                    throw new GameTestAssertException("Condition not triggered (t=" + $$0 + ")");
                } else {
                    throw new GameTestAssertException("Condition triggered at " + this.triggerTime + ", (t=" + $$0 + ")");
                }
            }
        }
    }
}