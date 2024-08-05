package info.journeymap.shaded.org.eclipse.jetty.io;

import info.journeymap.shaded.org.eclipse.jetty.util.BufferUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.Callback;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.Invocable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.WritePendingException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public abstract class WriteFlusher {

    private static final Logger LOG = Log.getLogger(WriteFlusher.class);

    private static final boolean DEBUG = LOG.isDebugEnabled();

    private static final ByteBuffer[] EMPTY_BUFFERS = new ByteBuffer[] { BufferUtil.EMPTY_BUFFER };

    private static final EnumMap<WriteFlusher.StateType, Set<WriteFlusher.StateType>> __stateTransitions = new EnumMap(WriteFlusher.StateType.class);

    private static final WriteFlusher.State __IDLE = new WriteFlusher.IdleState();

    private static final WriteFlusher.State __WRITING = new WriteFlusher.WritingState();

    private static final WriteFlusher.State __COMPLETING = new WriteFlusher.CompletingState();

    private final EndPoint _endPoint;

    private final AtomicReference<WriteFlusher.State> _state = new AtomicReference();

    protected WriteFlusher(EndPoint endPoint) {
        this._state.set(__IDLE);
        this._endPoint = endPoint;
    }

    private boolean updateState(WriteFlusher.State previous, WriteFlusher.State next) {
        if (!this.isTransitionAllowed(previous, next)) {
            throw new IllegalStateException();
        } else {
            boolean updated = this._state.compareAndSet(previous, next);
            if (DEBUG) {
                LOG.debug("update {}:{}{}{}", this, previous, updated ? "-->" : "!->", next);
            }
            return updated;
        }
    }

    private void fail(WriteFlusher.PendingState pending) {
        WriteFlusher.State current = (WriteFlusher.State) this._state.get();
        if (current.getType() == WriteFlusher.StateType.FAILED) {
            WriteFlusher.FailedState failed = (WriteFlusher.FailedState) current;
            if (this.updateState(failed, __IDLE)) {
                pending.fail(failed.getCause());
                return;
            }
        }
        throw new IllegalStateException();
    }

    private void ignoreFail() {
        for (WriteFlusher.State current = (WriteFlusher.State) this._state.get(); current.getType() == WriteFlusher.StateType.FAILED; current = (WriteFlusher.State) this._state.get()) {
            if (this.updateState(current, __IDLE)) {
                return;
            }
        }
    }

    private boolean isTransitionAllowed(WriteFlusher.State currentState, WriteFlusher.State newState) {
        Set<WriteFlusher.StateType> allowedNewStateTypes = (Set<WriteFlusher.StateType>) __stateTransitions.get(currentState.getType());
        if (!allowedNewStateTypes.contains(newState.getType())) {
            LOG.warn("{}: {} -> {} not allowed", this, currentState, newState);
            return false;
        } else {
            return true;
        }
    }

    public Invocable.InvocationType getCallbackInvocationType() {
        WriteFlusher.State s = (WriteFlusher.State) this._state.get();
        return s instanceof WriteFlusher.PendingState ? ((WriteFlusher.PendingState) s).getCallbackInvocationType() : Invocable.InvocationType.BLOCKING;
    }

    protected abstract void onIncompleteFlush();

    public void write(Callback callback, ByteBuffer... buffers) throws WritePendingException {
        if (DEBUG) {
            LOG.debug("write: {} {}", this, BufferUtil.toDetailString(buffers));
        }
        if (!this.updateState(__IDLE, __WRITING)) {
            throw new WritePendingException();
        } else {
            try {
                buffers = this.flush(buffers);
                if (buffers != null) {
                    if (DEBUG) {
                        LOG.debug("flushed incomplete");
                    }
                    WriteFlusher.PendingState pending = new WriteFlusher.PendingState(buffers, callback);
                    if (this.updateState(__WRITING, pending)) {
                        this.onIncompleteFlush();
                    } else {
                        this.fail(pending);
                    }
                    return;
                }
                if (!this.updateState(__WRITING, __IDLE)) {
                    this.ignoreFail();
                }
                if (callback != null) {
                    callback.succeeded();
                }
            } catch (IOException var4) {
                if (DEBUG) {
                    LOG.debug("write exception", var4);
                }
                if (this.updateState(__WRITING, __IDLE)) {
                    if (callback != null) {
                        callback.failed(var4);
                    }
                } else {
                    this.fail(new WriteFlusher.PendingState(buffers, callback));
                }
            }
        }
    }

    public void completeWrite() {
        if (DEBUG) {
            LOG.debug("completeWrite: {}", this);
        }
        WriteFlusher.State previous = (WriteFlusher.State) this._state.get();
        if (previous.getType() == WriteFlusher.StateType.PENDING) {
            WriteFlusher.PendingState pending = (WriteFlusher.PendingState) previous;
            if (this.updateState(pending, __COMPLETING)) {
                try {
                    ByteBuffer[] buffers = pending.getBuffers();
                    buffers = this.flush(buffers);
                    if (buffers != null) {
                        if (DEBUG) {
                            LOG.debug("flushed incomplete {}", BufferUtil.toDetailString(buffers));
                        }
                        if (buffers != pending.getBuffers()) {
                            pending = new WriteFlusher.PendingState(buffers, pending._callback);
                        }
                        if (this.updateState(__COMPLETING, pending)) {
                            this.onIncompleteFlush();
                        } else {
                            this.fail(pending);
                        }
                        return;
                    }
                    if (!this.updateState(__COMPLETING, __IDLE)) {
                        this.ignoreFail();
                    }
                    pending.complete();
                } catch (IOException var4) {
                    if (DEBUG) {
                        LOG.debug("completeWrite exception", var4);
                    }
                    if (this.updateState(__COMPLETING, __IDLE)) {
                        pending.fail(var4);
                    } else {
                        this.fail(pending);
                    }
                }
            }
        }
    }

    protected ByteBuffer[] flush(ByteBuffer[] buffers) throws IOException {
        boolean progress = true;
        while (progress && buffers != null) {
            int before = buffers.length == 0 ? 0 : buffers[0].remaining();
            boolean flushed = this._endPoint.flush(buffers);
            int r = buffers.length == 0 ? 0 : buffers[0].remaining();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Flushed={} {}/{}+{} {}", flushed, before - r, before, buffers.length - 1, this);
            }
            if (flushed) {
                return null;
            }
            progress = before != r;
            int not_empty = 0;
            while (true) {
                if (r == 0) {
                    if (++not_empty != buffers.length) {
                        progress = true;
                        r = buffers[not_empty].remaining();
                        continue;
                    }
                    buffers = null;
                    not_empty = 0;
                }
                if (not_empty > 0) {
                    buffers = (ByteBuffer[]) Arrays.copyOfRange(buffers, not_empty, buffers.length);
                }
                break;
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("!fully flushed {}", this);
        }
        return buffers == null ? EMPTY_BUFFERS : buffers;
    }

    public boolean onFail(Throwable cause) {
        while (true) {
            WriteFlusher.State current = (WriteFlusher.State) this._state.get();
            switch(current.getType()) {
                case IDLE:
                case FAILED:
                    if (DEBUG) {
                        LOG.debug("ignored: {} {}", this, cause);
                    }
                    return false;
                case PENDING:
                    if (DEBUG) {
                        LOG.debug("failed: {} {}", this, cause);
                    }
                    WriteFlusher.PendingState pending = (WriteFlusher.PendingState) current;
                    if (!this.updateState(pending, __IDLE)) {
                        break;
                    }
                    return pending.fail(cause);
                default:
                    if (DEBUG) {
                        LOG.debug("failed: {} {}", this, cause);
                    }
                    if (this.updateState(current, new WriteFlusher.FailedState(cause))) {
                        return false;
                    }
            }
        }
    }

    public void onClose() {
        this.onFail(new ClosedChannelException());
    }

    boolean isIdle() {
        return ((WriteFlusher.State) this._state.get()).getType() == WriteFlusher.StateType.IDLE;
    }

    public boolean isInProgress() {
        switch(((WriteFlusher.State) this._state.get()).getType()) {
            case PENDING:
            case WRITING:
            case COMPLETING:
                return true;
            default:
                return false;
        }
    }

    public String toString() {
        WriteFlusher.State s = (WriteFlusher.State) this._state.get();
        return String.format("WriteFlusher@%x{%s}->%s", this.hashCode(), s, s instanceof WriteFlusher.PendingState ? ((WriteFlusher.PendingState) s).getCallback() : null);
    }

    public String toStateString() {
        switch(((WriteFlusher.State) this._state.get()).getType()) {
            case IDLE:
                return "-";
            case FAILED:
                return "F";
            case PENDING:
                return "P";
            case WRITING:
                return "W";
            case COMPLETING:
                return "C";
            default:
                return "?";
        }
    }

    static {
        __stateTransitions.put(WriteFlusher.StateType.IDLE, EnumSet.of(WriteFlusher.StateType.WRITING));
        __stateTransitions.put(WriteFlusher.StateType.WRITING, EnumSet.of(WriteFlusher.StateType.IDLE, WriteFlusher.StateType.PENDING, WriteFlusher.StateType.FAILED));
        __stateTransitions.put(WriteFlusher.StateType.PENDING, EnumSet.of(WriteFlusher.StateType.COMPLETING, WriteFlusher.StateType.IDLE));
        __stateTransitions.put(WriteFlusher.StateType.COMPLETING, EnumSet.of(WriteFlusher.StateType.IDLE, WriteFlusher.StateType.PENDING, WriteFlusher.StateType.FAILED));
        __stateTransitions.put(WriteFlusher.StateType.FAILED, EnumSet.of(WriteFlusher.StateType.IDLE));
    }

    private static class CompletingState extends WriteFlusher.State {

        private CompletingState() {
            super(WriteFlusher.StateType.COMPLETING);
        }
    }

    private static class FailedState extends WriteFlusher.State {

        private final Throwable _cause;

        private FailedState(Throwable cause) {
            super(WriteFlusher.StateType.FAILED);
            this._cause = cause;
        }

        public Throwable getCause() {
            return this._cause;
        }
    }

    private static class IdleState extends WriteFlusher.State {

        private IdleState() {
            super(WriteFlusher.StateType.IDLE);
        }
    }

    private class PendingState extends WriteFlusher.State {

        private final Callback _callback;

        private final ByteBuffer[] _buffers;

        private PendingState(ByteBuffer[] buffers, Callback callback) {
            super(WriteFlusher.StateType.PENDING);
            this._buffers = buffers;
            this._callback = callback;
        }

        public ByteBuffer[] getBuffers() {
            return this._buffers;
        }

        protected boolean fail(Throwable cause) {
            if (this._callback != null) {
                this._callback.failed(cause);
                return true;
            } else {
                return false;
            }
        }

        protected void complete() {
            if (this._callback != null) {
                this._callback.succeeded();
            }
        }

        Invocable.InvocationType getCallbackInvocationType() {
            return Invocable.getInvocationType(this._callback);
        }

        public Object getCallback() {
            return this._callback;
        }
    }

    private static class State {

        private final WriteFlusher.StateType _type;

        private State(WriteFlusher.StateType stateType) {
            this._type = stateType;
        }

        public WriteFlusher.StateType getType() {
            return this._type;
        }

        public String toString() {
            return String.format("%s", this._type);
        }
    }

    private static enum StateType {

        IDLE, WRITING, PENDING, COMPLETING, FAILED
    }

    private static class WritingState extends WriteFlusher.State {

        private WritingState() {
            super(WriteFlusher.StateType.WRITING);
        }
    }
}