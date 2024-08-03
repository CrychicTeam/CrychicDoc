package info.journeymap.shaded.org.eclipse.jetty.util;

import info.journeymap.shaded.org.eclipse.jetty.util.thread.Locker;
import java.nio.channels.ClosedChannelException;

public abstract class IteratingCallback implements Callback {

    private Locker _locker = new Locker();

    private IteratingCallback.State _state;

    private boolean _iterate;

    protected IteratingCallback() {
        this._state = IteratingCallback.State.IDLE;
    }

    protected IteratingCallback(boolean needReset) {
        this._state = needReset ? IteratingCallback.State.SUCCEEDED : IteratingCallback.State.IDLE;
    }

    protected abstract IteratingCallback.Action process() throws Throwable;

    protected void onCompleteSuccess() {
    }

    protected void onCompleteFailure(Throwable cause) {
    }

    public void iterate() {
        boolean process = false;
        try (Locker.Lock lock = this._locker.lock()) {
            switch(this._state) {
                case PENDING:
                case CALLED:
                case FAILED:
                case SUCCEEDED:
                    break;
                case IDLE:
                    this._state = IteratingCallback.State.PROCESSING;
                    process = true;
                    break;
                case PROCESSING:
                    this._iterate = true;
                    break;
                case CLOSED:
                default:
                    throw new IllegalStateException(this.toString());
            }
        }
        if (process) {
            this.processing();
        }
    }

    private void processing() {
        boolean on_complete_success = false;
        label178: while (true) {
            IteratingCallback.Action action;
            try {
                action = this.process();
            } catch (Throwable var21) {
                this.failed(var21);
                break;
            }
            try (Locker.Lock lock = this._locker.lock()) {
                switch(this._state) {
                    case PENDING:
                    case IDLE:
                    default:
                        throw new IllegalStateException(String.format("%s[action=%s]", this, action));
                    case CALLED:
                        switch(action) {
                            case SCHEDULED:
                                this._state = IteratingCallback.State.PROCESSING;
                                continue;
                            default:
                                throw new IllegalStateException(String.format("%s[action=%s]", this, action));
                        }
                    case PROCESSING:
                        switch(action) {
                            case IDLE:
                                if (this._iterate) {
                                    this._iterate = false;
                                    this._state = IteratingCallback.State.PROCESSING;
                                    continue;
                                } else {
                                    this._state = IteratingCallback.State.IDLE;
                                    break label178;
                                }
                            case SCHEDULED:
                                this._state = IteratingCallback.State.PENDING;
                                break label178;
                            case SUCCEEDED:
                                this._iterate = false;
                                this._state = IteratingCallback.State.SUCCEEDED;
                                on_complete_success = true;
                                break label178;
                            default:
                                throw new IllegalStateException(String.format("%s[action=%s]", this, action));
                        }
                    case FAILED:
                    case SUCCEEDED:
                    case CLOSED:
                        break label178;
                }
            }
        }
        if (on_complete_success) {
            this.onCompleteSuccess();
        }
    }

    @Override
    public void succeeded() {
        boolean process = false;
        try (Locker.Lock lock = this._locker.lock()) {
            switch(this._state) {
                case PENDING:
                    this._state = IteratingCallback.State.PROCESSING;
                    process = true;
                    break;
                case CALLED:
                case IDLE:
                case SUCCEEDED:
                default:
                    throw new IllegalStateException(this.toString());
                case PROCESSING:
                    this._state = IteratingCallback.State.CALLED;
                case FAILED:
                case CLOSED:
            }
        }
        if (process) {
            this.processing();
        }
    }

    @Override
    public void failed(Throwable x) {
        boolean failure = false;
        try (Locker.Lock lock = this._locker.lock()) {
            switch(this._state) {
                case PENDING:
                case PROCESSING:
                    this._state = IteratingCallback.State.FAILED;
                    failure = true;
                case CALLED:
                case IDLE:
                case FAILED:
                case SUCCEEDED:
                case CLOSED:
                    break;
                default:
                    throw new IllegalStateException(this.toString());
            }
        }
        if (failure) {
            this.onCompleteFailure(x);
        }
    }

    public void close() {
        boolean failure = false;
        try (Locker.Lock lock = this._locker.lock()) {
            switch(this._state) {
                case IDLE:
                case FAILED:
                case SUCCEEDED:
                    this._state = IteratingCallback.State.CLOSED;
                    break;
                case PROCESSING:
                default:
                    this._state = IteratingCallback.State.CLOSED;
                    failure = true;
                case CLOSED:
            }
        }
        if (failure) {
            this.onCompleteFailure(new ClosedChannelException());
        }
    }

    boolean isIdle() {
        boolean var3;
        try (Locker.Lock lock = this._locker.lock()) {
            var3 = this._state == IteratingCallback.State.IDLE;
        }
        return var3;
    }

    public boolean isClosed() {
        boolean var3;
        try (Locker.Lock lock = this._locker.lock()) {
            var3 = this._state == IteratingCallback.State.CLOSED;
        }
        return var3;
    }

    public boolean isFailed() {
        boolean var3;
        try (Locker.Lock lock = this._locker.lock()) {
            var3 = this._state == IteratingCallback.State.FAILED;
        }
        return var3;
    }

    public boolean isSucceeded() {
        boolean var3;
        try (Locker.Lock lock = this._locker.lock()) {
            var3 = this._state == IteratingCallback.State.SUCCEEDED;
        }
        return var3;
    }

    public boolean reset() {
        boolean var3;
        try (Locker.Lock lock = this._locker.lock()) {
            switch(this._state) {
                case IDLE:
                    return true;
                case PROCESSING:
                default:
                    return false;
                case FAILED:
                case SUCCEEDED:
            }
            this._iterate = false;
            this._state = IteratingCallback.State.IDLE;
            var3 = true;
        }
        return var3;
    }

    public String toString() {
        return String.format("%s[%s]", super.toString(), this._state);
    }

    protected static enum Action {

        IDLE, SCHEDULED, SUCCEEDED
    }

    private static enum State {

        IDLE,
        PROCESSING,
        PENDING,
        CALLED,
        SUCCEEDED,
        FAILED,
        CLOSED
    }
}