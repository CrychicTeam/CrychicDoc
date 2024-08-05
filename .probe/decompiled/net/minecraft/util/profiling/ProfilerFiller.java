package net.minecraft.util.profiling;

import java.util.function.Supplier;
import net.minecraft.util.profiling.metrics.MetricCategory;

public interface ProfilerFiller {

    String ROOT = "root";

    void startTick();

    void endTick();

    void push(String var1);

    void push(Supplier<String> var1);

    void pop();

    void popPush(String var1);

    void popPush(Supplier<String> var1);

    void markForCharting(MetricCategory var1);

    default void incrementCounter(String string0) {
        this.incrementCounter(string0, 1);
    }

    void incrementCounter(String var1, int var2);

    default void incrementCounter(Supplier<String> supplierString0) {
        this.incrementCounter(supplierString0, 1);
    }

    void incrementCounter(Supplier<String> var1, int var2);

    static ProfilerFiller tee(final ProfilerFiller profilerFiller0, final ProfilerFiller profilerFiller1) {
        if (profilerFiller0 == InactiveProfiler.INSTANCE) {
            return profilerFiller1;
        } else {
            return profilerFiller1 == InactiveProfiler.INSTANCE ? profilerFiller0 : new ProfilerFiller() {

                @Override
                public void startTick() {
                    profilerFiller0.startTick();
                    profilerFiller1.startTick();
                }

                @Override
                public void endTick() {
                    profilerFiller0.endTick();
                    profilerFiller1.endTick();
                }

                @Override
                public void push(String p_18594_) {
                    profilerFiller0.push(p_18594_);
                    profilerFiller1.push(p_18594_);
                }

                @Override
                public void push(Supplier<String> p_18596_) {
                    profilerFiller0.push(p_18596_);
                    profilerFiller1.push(p_18596_);
                }

                @Override
                public void markForCharting(MetricCategory p_145961_) {
                    profilerFiller0.markForCharting(p_145961_);
                    profilerFiller1.markForCharting(p_145961_);
                }

                @Override
                public void pop() {
                    profilerFiller0.pop();
                    profilerFiller1.pop();
                }

                @Override
                public void popPush(String p_18599_) {
                    profilerFiller0.popPush(p_18599_);
                    profilerFiller1.popPush(p_18599_);
                }

                @Override
                public void popPush(Supplier<String> p_18601_) {
                    profilerFiller0.popPush(p_18601_);
                    profilerFiller1.popPush(p_18601_);
                }

                @Override
                public void incrementCounter(String p_185263_, int p_185264_) {
                    profilerFiller0.incrementCounter(p_185263_, p_185264_);
                    profilerFiller1.incrementCounter(p_185263_, p_185264_);
                }

                @Override
                public void incrementCounter(Supplier<String> p_185266_, int p_185267_) {
                    profilerFiller0.incrementCounter(p_185266_, p_185267_);
                    profilerFiller1.incrementCounter(p_185266_, p_185267_);
                }
            };
        }
    }
}