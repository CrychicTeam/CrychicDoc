package info.journeymap.shaded.org.eclipse.jetty.websocket.common.extensions;

import info.journeymap.shaded.org.eclipse.jetty.io.ByteBufferPool;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedAttribute;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedObject;
import info.journeymap.shaded.org.eclipse.jetty.util.component.AbstractLifeCycle;
import info.journeymap.shaded.org.eclipse.jetty.util.component.ContainerLifeCycle;
import info.journeymap.shaded.org.eclipse.jetty.util.component.Dumpable;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.BatchMode;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.WebSocketPolicy;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.WriteCallback;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.Extension;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.ExtensionConfig;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.Frame;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.IncomingFrames;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.OutgoingFrames;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.LogicalConnection;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.scopes.WebSocketContainerScope;
import java.io.IOException;

@ManagedObject("Abstract Extension")
public abstract class AbstractExtension extends AbstractLifeCycle implements Dumpable, Extension {

    private final Logger log = Log.getLogger(this.getClass());

    private WebSocketPolicy policy;

    private ByteBufferPool bufferPool;

    private ExtensionConfig config;

    private LogicalConnection connection;

    private OutgoingFrames nextOutgoing;

    private IncomingFrames nextIncoming;

    @Override
    public String dump() {
        return ContainerLifeCycle.dump(this);
    }

    @Override
    public void dump(Appendable out, String indent) throws IOException {
        this.dumpWithHeading(out, indent, "incoming", this.nextIncoming);
        this.dumpWithHeading(out, indent, "outgoing", this.nextOutgoing);
    }

    protected void dumpWithHeading(Appendable out, String indent, String heading, Object bean) throws IOException {
        out.append(indent).append(" +- ");
        out.append(heading).append(" : ");
        out.append(bean.toString());
    }

    @Deprecated
    public void init(WebSocketContainerScope container) {
        this.init(container.getPolicy(), container.getBufferPool());
    }

    public void init(WebSocketPolicy policy, ByteBufferPool bufferPool) {
        this.policy = policy;
        this.bufferPool = bufferPool;
    }

    public ByteBufferPool getBufferPool() {
        return this.bufferPool;
    }

    @Override
    public ExtensionConfig getConfig() {
        return this.config;
    }

    public LogicalConnection getConnection() {
        return this.connection;
    }

    @Override
    public String getName() {
        return this.config.getName();
    }

    @ManagedAttribute(name = "Next Incoming Frame Handler", readonly = true)
    public IncomingFrames getNextIncoming() {
        return this.nextIncoming;
    }

    @ManagedAttribute(name = "Next Outgoing Frame Handler", readonly = true)
    public OutgoingFrames getNextOutgoing() {
        return this.nextOutgoing;
    }

    public WebSocketPolicy getPolicy() {
        return this.policy;
    }

    @Override
    public void incomingError(Throwable e) {
        this.nextIncomingError(e);
    }

    @Override
    public boolean isRsv1User() {
        return false;
    }

    @Override
    public boolean isRsv2User() {
        return false;
    }

    @Override
    public boolean isRsv3User() {
        return false;
    }

    protected void nextIncomingError(Throwable e) {
        this.nextIncoming.incomingError(e);
    }

    protected void nextIncomingFrame(Frame frame) {
        this.log.debug("nextIncomingFrame({})", frame);
        this.nextIncoming.incomingFrame(frame);
    }

    protected void nextOutgoingFrame(Frame frame, WriteCallback callback, BatchMode batchMode) {
        this.log.debug("nextOutgoingFrame({})", frame);
        this.nextOutgoing.outgoingFrame(frame, callback, batchMode);
    }

    public void setBufferPool(ByteBufferPool bufferPool) {
        this.bufferPool = bufferPool;
    }

    public void setConfig(ExtensionConfig config) {
        this.config = config;
    }

    public void setConnection(LogicalConnection connection) {
        this.connection = connection;
    }

    @Override
    public void setNextIncomingFrames(IncomingFrames nextIncoming) {
        this.nextIncoming = nextIncoming;
    }

    @Override
    public void setNextOutgoingFrames(OutgoingFrames nextOutgoing) {
        this.nextOutgoing = nextOutgoing;
    }

    public void setPolicy(WebSocketPolicy policy) {
        this.policy = policy;
    }

    public String toString() {
        return String.format("%s[%s]", this.getClass().getSimpleName(), this.config.getParameterizedName());
    }
}