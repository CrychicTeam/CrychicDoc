package info.journeymap.shaded.org.eclipse.jetty.websocket.common.io;

import info.journeymap.shaded.org.eclipse.jetty.util.StringUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.CloseInfo;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.ConnectionState;
import java.io.EOFException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class IOState {

    private static final Logger LOG = Log.getLogger(IOState.class);

    private ConnectionState state;

    private final List<IOState.ConnectionStateListener> listeners = new CopyOnWriteArrayList();

    private boolean inputAvailable;

    private boolean outputAvailable;

    private IOState.CloseHandshakeSource closeHandshakeSource;

    private CloseInfo closeInfo;

    private AtomicReference<CloseInfo> finalClose = new AtomicReference();

    private boolean cleanClose;

    public IOState() {
        this.state = ConnectionState.CONNECTING;
        this.inputAvailable = false;
        this.outputAvailable = false;
        this.closeHandshakeSource = IOState.CloseHandshakeSource.NONE;
        this.closeInfo = null;
        this.cleanClose = false;
    }

    public void addListener(IOState.ConnectionStateListener listener) {
        this.listeners.add(listener);
    }

    public void assertInputOpen() throws IOException {
        if (!this.isInputAvailable()) {
            throw new IOException("Connection input is closed");
        }
    }

    public void assertOutputOpen() throws IOException {
        if (!this.isOutputAvailable()) {
            throw new IOException("Connection output is closed");
        }
    }

    public CloseInfo getCloseInfo() {
        CloseInfo ci = (CloseInfo) this.finalClose.get();
        return ci != null ? ci : this.closeInfo;
    }

    public ConnectionState getConnectionState() {
        return this.state;
    }

    public boolean isClosed() {
        synchronized (this) {
            return this.state == ConnectionState.CLOSED;
        }
    }

    public boolean isInputAvailable() {
        return this.inputAvailable;
    }

    public boolean isOpen() {
        return !this.isClosed();
    }

    public boolean isOutputAvailable() {
        return this.outputAvailable;
    }

    private void notifyStateListeners(ConnectionState state) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Notify State Listeners: {}", state);
        }
        for (IOState.ConnectionStateListener listener : this.listeners) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("{}.onConnectionStateChange({})", listener.getClass().getSimpleName(), state.name());
            }
            listener.onConnectionStateChange(state);
        }
    }

    public void onAbnormalClose(CloseInfo close) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("onAbnormalClose({})", close);
        }
        ConnectionState event = null;
        synchronized (this) {
            if (this.state == ConnectionState.CLOSED) {
                return;
            }
            if (this.state == ConnectionState.OPEN) {
                this.cleanClose = false;
            }
            this.state = ConnectionState.CLOSED;
            this.finalClose.compareAndSet(null, close);
            this.inputAvailable = false;
            this.outputAvailable = false;
            this.closeHandshakeSource = IOState.CloseHandshakeSource.ABNORMAL;
            event = this.state;
        }
        this.notifyStateListeners(event);
    }

    public void onCloseLocal(CloseInfo closeInfo) {
        boolean open = false;
        synchronized (this) {
            ConnectionState initialState = this.state;
            if (LOG.isDebugEnabled()) {
                LOG.debug("onCloseLocal({}) : {}", closeInfo, initialState);
            }
            if (initialState == ConnectionState.CLOSED) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("already closed");
                }
                return;
            }
            if (initialState == ConnectionState.CONNECTED) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("FastClose in CONNECTED detected");
                }
                open = true;
            }
        }
        if (open) {
            this.openAndCloseLocal(closeInfo);
        } else {
            this.closeLocal(closeInfo);
        }
    }

    private void openAndCloseLocal(CloseInfo closeInfo) {
        this.onOpened();
        if (LOG.isDebugEnabled()) {
            LOG.debug("FastClose continuing with Closure");
        }
        this.closeLocal(closeInfo);
    }

    private void closeLocal(CloseInfo closeInfo) {
        ConnectionState event = null;
        ConnectionState abnormalEvent = null;
        synchronized (this) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("onCloseLocal(), input={}, output={}", this.inputAvailable, this.outputAvailable);
            }
            this.closeInfo = closeInfo;
            this.outputAvailable = false;
            if (this.closeHandshakeSource == IOState.CloseHandshakeSource.NONE) {
                this.closeHandshakeSource = IOState.CloseHandshakeSource.LOCAL;
            }
            if (!this.inputAvailable) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Close Handshake satisfied, disconnecting");
                }
                this.cleanClose = true;
                this.state = ConnectionState.CLOSED;
                this.finalClose.compareAndSet(null, closeInfo);
                event = this.state;
            } else if (this.state == ConnectionState.OPEN) {
                this.state = ConnectionState.CLOSING;
                event = this.state;
                if (closeInfo.isAbnormal()) {
                    abnormalEvent = ConnectionState.CLOSED;
                    this.finalClose.compareAndSet(null, closeInfo);
                    this.cleanClose = false;
                    this.outputAvailable = false;
                    this.inputAvailable = false;
                    this.closeHandshakeSource = IOState.CloseHandshakeSource.ABNORMAL;
                }
            }
        }
        if (event != null) {
            this.notifyStateListeners(event);
            if (abnormalEvent != null) {
                this.notifyStateListeners(abnormalEvent);
            }
        }
    }

    public void onCloseRemote(CloseInfo closeInfo) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("onCloseRemote({})", closeInfo);
        }
        ConnectionState event = null;
        synchronized (this) {
            if (this.state == ConnectionState.CLOSED) {
                return;
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("onCloseRemote(), input={}, output={}", this.inputAvailable, this.outputAvailable);
            }
            this.closeInfo = closeInfo;
            this.inputAvailable = false;
            if (this.closeHandshakeSource == IOState.CloseHandshakeSource.NONE) {
                this.closeHandshakeSource = IOState.CloseHandshakeSource.REMOTE;
            }
            if (!this.outputAvailable) {
                LOG.debug("Close Handshake satisfied, disconnecting");
                this.cleanClose = true;
                this.state = ConnectionState.CLOSED;
                this.finalClose.compareAndSet(null, closeInfo);
                event = this.state;
            } else if (this.state == ConnectionState.OPEN) {
                this.state = ConnectionState.CLOSING;
                event = this.state;
            }
        }
        if (event != null) {
            this.notifyStateListeners(event);
        }
    }

    public void onConnected() {
        ConnectionState event = null;
        synchronized (this) {
            if (this.state != ConnectionState.CONNECTING) {
                LOG.debug("Unable to set to connected, not in CONNECTING state: {}", this.state);
                return;
            }
            this.state = ConnectionState.CONNECTED;
            this.inputAvailable = false;
            this.outputAvailable = true;
            event = this.state;
        }
        this.notifyStateListeners(event);
    }

    public void onFailedUpgrade() {
        assert this.state == ConnectionState.CONNECTING;
        ConnectionState event = null;
        synchronized (this) {
            this.state = ConnectionState.CLOSED;
            this.cleanClose = false;
            this.inputAvailable = false;
            this.outputAvailable = false;
            event = this.state;
        }
        this.notifyStateListeners(event);
    }

    public void onOpened() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("onOpened()");
        }
        ConnectionState event = null;
        synchronized (this) {
            if (this.state == ConnectionState.OPEN) {
                return;
            }
            if (this.state != ConnectionState.CONNECTED) {
                LOG.debug("Unable to open, not in CONNECTED state: {}", this.state);
                return;
            }
            this.state = ConnectionState.OPEN;
            this.inputAvailable = true;
            this.outputAvailable = true;
            event = this.state;
        }
        this.notifyStateListeners(event);
    }

    public void onReadFailure(Throwable t) {
        ConnectionState event = null;
        synchronized (this) {
            if (this.state == ConnectionState.CLOSED) {
                return;
            }
            String reason = "WebSocket Read Failure";
            if (t instanceof EOFException) {
                reason = "WebSocket Read EOF";
                Throwable cause = t.getCause();
                if (cause != null && StringUtil.isNotBlank(cause.getMessage())) {
                    reason = "EOF: " + cause.getMessage();
                }
            } else if (StringUtil.isNotBlank(t.getMessage())) {
                reason = t.getMessage();
            }
            CloseInfo close = new CloseInfo(1006, reason);
            this.finalClose.compareAndSet(null, close);
            this.cleanClose = false;
            this.state = ConnectionState.CLOSED;
            this.closeInfo = close;
            this.inputAvailable = false;
            this.outputAvailable = false;
            this.closeHandshakeSource = IOState.CloseHandshakeSource.ABNORMAL;
            event = this.state;
        }
        this.notifyStateListeners(event);
    }

    public void onWriteFailure(Throwable t) {
        ConnectionState event = null;
        synchronized (this) {
            if (this.state == ConnectionState.CLOSED) {
                return;
            }
            String reason = "WebSocket Write Failure";
            if (t instanceof EOFException) {
                reason = "WebSocket Write EOF";
                Throwable cause = t.getCause();
                if (cause != null && StringUtil.isNotBlank(cause.getMessage())) {
                    reason = "EOF: " + cause.getMessage();
                }
            } else if (StringUtil.isNotBlank(t.getMessage())) {
                reason = t.getMessage();
            }
            CloseInfo close = new CloseInfo(1006, reason);
            this.finalClose.compareAndSet(null, close);
            this.cleanClose = false;
            this.state = ConnectionState.CLOSED;
            this.inputAvailable = false;
            this.outputAvailable = false;
            this.closeHandshakeSource = IOState.CloseHandshakeSource.ABNORMAL;
            event = this.state;
        }
        this.notifyStateListeners(event);
    }

    public void onDisconnected() {
        ConnectionState event = null;
        synchronized (this) {
            if (this.state == ConnectionState.CLOSED) {
                return;
            }
            CloseInfo close = new CloseInfo(1006, "Disconnected");
            this.cleanClose = false;
            this.state = ConnectionState.CLOSED;
            this.closeInfo = close;
            this.inputAvailable = false;
            this.outputAvailable = false;
            this.closeHandshakeSource = IOState.CloseHandshakeSource.ABNORMAL;
            event = this.state;
        }
        this.notifyStateListeners(event);
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(this.getClass().getSimpleName());
        str.append("@").append(Integer.toHexString(this.hashCode()));
        str.append("[").append(this.state);
        str.append(',');
        if (!this.inputAvailable) {
            str.append('!');
        }
        str.append("in,");
        if (!this.outputAvailable) {
            str.append('!');
        }
        str.append("out");
        if (this.state == ConnectionState.CLOSED || this.state == ConnectionState.CLOSING) {
            CloseInfo ci = (CloseInfo) this.finalClose.get();
            if (ci != null) {
                str.append(",finalClose=").append(ci);
            } else {
                str.append(",close=").append(this.closeInfo);
            }
            str.append(",clean=").append(this.cleanClose);
            str.append(",closeSource=").append(this.closeHandshakeSource);
        }
        str.append(']');
        return str.toString();
    }

    public boolean wasAbnormalClose() {
        return this.closeHandshakeSource == IOState.CloseHandshakeSource.ABNORMAL;
    }

    public boolean wasCleanClose() {
        return this.cleanClose;
    }

    public boolean wasLocalCloseInitiated() {
        return this.closeHandshakeSource == IOState.CloseHandshakeSource.LOCAL;
    }

    public boolean wasRemoteCloseInitiated() {
        return this.closeHandshakeSource == IOState.CloseHandshakeSource.REMOTE;
    }

    private static enum CloseHandshakeSource {

        NONE, LOCAL, REMOTE, ABNORMAL
    }

    public interface ConnectionStateListener {

        void onConnectionStateChange(ConnectionState var1);
    }
}