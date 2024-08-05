package info.journeymap.shaded.org.eclipse.jetty.websocket.common;

import info.journeymap.shaded.org.eclipse.jetty.util.Callback;
import info.journeymap.shaded.org.eclipse.jetty.util.SharedBlockingCallback;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.Invocable;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.WriteCallback;
import java.io.IOException;

public class BlockingWriteCallback extends SharedBlockingCallback {

    public BlockingWriteCallback.WriteBlocker acquireWriteBlocker() throws IOException {
        return new BlockingWriteCallback.WriteBlocker(this.acquire());
    }

    public static class WriteBlocker implements WriteCallback, Callback, AutoCloseable {

        private final SharedBlockingCallback.Blocker blocker;

        protected WriteBlocker(SharedBlockingCallback.Blocker blocker) {
            this.blocker = blocker;
        }

        @Override
        public Invocable.InvocationType getInvocationType() {
            return Invocable.InvocationType.NON_BLOCKING;
        }

        @Override
        public void writeFailed(Throwable x) {
            this.blocker.failed(x);
        }

        @Override
        public void writeSuccess() {
            this.blocker.succeeded();
        }

        @Override
        public void succeeded() {
            this.blocker.succeeded();
        }

        @Override
        public void failed(Throwable x) {
            this.blocker.failed(x);
        }

        public void close() {
            this.blocker.close();
        }

        public void block() throws IOException {
            this.blocker.block();
        }
    }
}