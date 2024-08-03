package com.simibubi.create.foundation.ponder.instruction;

import com.simibubi.create.foundation.ponder.PonderScene;
import java.util.function.Consumer;

public abstract class PonderInstruction {

    public boolean isBlocking() {
        return false;
    }

    public void reset(PonderScene scene) {
    }

    public abstract boolean isComplete();

    public void onScheduled(PonderScene scene) {
    }

    public abstract void tick(PonderScene var1);

    public static PonderInstruction simple(Consumer<PonderScene> callback) {
        return new PonderInstruction.Simple(callback);
    }

    private static class Simple extends PonderInstruction {

        private Consumer<PonderScene> callback;

        public Simple(Consumer<PonderScene> callback) {
            this.callback = callback;
        }

        @Override
        public boolean isComplete() {
            return true;
        }

        @Override
        public void tick(PonderScene scene) {
            this.callback.accept(scene);
        }
    }
}