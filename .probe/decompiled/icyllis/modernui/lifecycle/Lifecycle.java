package icyllis.modernui.lifecycle;

import icyllis.modernui.annotation.UiThread;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class Lifecycle {

    @UiThread
    public abstract void addObserver(@Nonnull LifecycleObserver var1);

    @UiThread
    public abstract void removeObserver(@Nonnull LifecycleObserver var1);

    @Nonnull
    @UiThread
    public abstract Lifecycle.State getCurrentState();

    public static enum Event {

        ON_CREATE,
        ON_START,
        ON_RESUME,
        ON_PAUSE,
        ON_STOP,
        ON_DESTROY;

        @Nullable
        public static Lifecycle.Event downFrom(@Nonnull Lifecycle.State state) {
            return switch(state) {
                case CREATED ->
                    ON_DESTROY;
                case STARTED ->
                    ON_STOP;
                case RESUMED ->
                    ON_PAUSE;
                default ->
                    null;
            };
        }

        @Nullable
        public static Lifecycle.Event downTo(@Nonnull Lifecycle.State state) {
            return switch(state) {
                case CREATED ->
                    ON_STOP;
                case STARTED ->
                    ON_PAUSE;
                default ->
                    null;
                case DESTROYED ->
                    ON_DESTROY;
            };
        }

        @Nullable
        public static Lifecycle.Event upFrom(@Nonnull Lifecycle.State state) {
            return switch(state) {
                case CREATED ->
                    ON_START;
                case STARTED ->
                    ON_RESUME;
                default ->
                    null;
                case INITIALIZED ->
                    ON_CREATE;
            };
        }

        @Nullable
        public static Lifecycle.Event upTo(@Nonnull Lifecycle.State state) {
            return switch(state) {
                case CREATED ->
                    ON_CREATE;
                case STARTED ->
                    ON_START;
                case RESUMED ->
                    ON_RESUME;
                default ->
                    null;
            };
        }

        @Nonnull
        public Lifecycle.State getTargetState() {
            return switch(this) {
                case ON_CREATE, ON_STOP ->
                    Lifecycle.State.CREATED;
                case ON_START, ON_PAUSE ->
                    Lifecycle.State.STARTED;
                case ON_RESUME ->
                    Lifecycle.State.RESUMED;
                case ON_DESTROY ->
                    Lifecycle.State.DESTROYED;
            };
        }
    }

    public static enum State {

        DESTROYED, INITIALIZED, CREATED, STARTED, RESUMED;

        public boolean isAtLeast(@Nonnull Lifecycle.State state) {
            return this.compareTo(state) >= 0;
        }
    }
}