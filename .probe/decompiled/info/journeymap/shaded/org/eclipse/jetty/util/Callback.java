package info.journeymap.shaded.org.eclipse.jetty.util;

import info.journeymap.shaded.org.eclipse.jetty.util.thread.Invocable;
import java.util.concurrent.CompletableFuture;

public interface Callback extends Invocable {

    Callback NOOP = new Callback() {
    };

    default void succeeded() {
    }

    default void failed(Throwable x) {
    }

    static Callback from(CompletableFuture<?> completable) {
        return from(completable, Invocable.InvocationType.NON_BLOCKING);
    }

    static Callback from(final CompletableFuture<?> completable, final Invocable.InvocationType invocation) {
        return completable instanceof Callback ? (Callback) completable : new Callback() {

            @Override
            public void succeeded() {
                completable.complete(null);
            }

            @Override
            public void failed(Throwable x) {
                completable.completeExceptionally(x);
            }

            @Override
            public Invocable.InvocationType getInvocationType() {
                return invocation;
            }
        };
    }

    public static class Completable extends CompletableFuture<Void> implements Callback {

        private final Invocable.InvocationType invocation;

        public Completable() {
            this(Invocable.InvocationType.NON_BLOCKING);
        }

        public Completable(Invocable.InvocationType invocation) {
            this.invocation = invocation;
        }

        @Override
        public void succeeded() {
            this.complete(null);
        }

        @Override
        public void failed(Throwable x) {
            this.completeExceptionally(x);
        }

        @Override
        public Invocable.InvocationType getInvocationType() {
            return this.invocation;
        }
    }

    public static class Nested implements Callback {

        private final Callback callback;

        public Nested(Callback callback) {
            this.callback = callback;
        }

        public Nested(Callback.Nested nested) {
            this.callback = nested.callback;
        }

        public Callback getCallback() {
            return this.callback;
        }

        @Override
        public void succeeded() {
            this.callback.succeeded();
        }

        @Override
        public void failed(Throwable x) {
            this.callback.failed(x);
        }

        @Override
        public Invocable.InvocationType getInvocationType() {
            return this.callback.getInvocationType();
        }
    }
}