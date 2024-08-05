package info.journeymap.shaded.org.eclipse.jetty.websocket.common.extensions;

import info.journeymap.shaded.org.eclipse.jetty.util.IteratingCallback;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedAttribute;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedObject;
import info.journeymap.shaded.org.eclipse.jetty.util.component.ContainerLifeCycle;
import info.journeymap.shaded.org.eclipse.jetty.util.component.LifeCycle;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.BatchMode;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.WebSocketPolicy;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.WriteCallback;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.Extension;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.ExtensionConfig;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.ExtensionFactory;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.Frame;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.IncomingFrames;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.OutgoingFrames;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.Generator;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.Parser;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;

@ManagedObject("Extension Stack")
public class ExtensionStack extends ContainerLifeCycle implements IncomingFrames, OutgoingFrames {

    private static final Logger LOG = Log.getLogger(ExtensionStack.class);

    private final Queue<ExtensionStack.FrameEntry> entries = new ArrayDeque();

    private final IteratingCallback flusher = new ExtensionStack.Flusher();

    private final ExtensionFactory factory;

    private List<Extension> extensions;

    private IncomingFrames nextIncoming;

    private OutgoingFrames nextOutgoing;

    public ExtensionStack(ExtensionFactory factory) {
        this.factory = factory;
    }

    public void configure(Generator generator) {
        generator.configureFromExtensions(this.extensions);
    }

    public void configure(Parser parser) {
        parser.configureFromExtensions(this.extensions);
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
        if (this.extensions != null && this.extensions.size() > 0) {
            ListIterator<Extension> exts = this.extensions.listIterator();
            while (exts.hasNext()) {
                Extension ext = (Extension) exts.next();
                ext.setNextOutgoingFrames(this.nextOutgoing);
                this.nextOutgoing = ext;
                if (ext instanceof LifeCycle) {
                    this.addBean(ext, true);
                }
            }
            while (exts.hasPrevious()) {
                Extension ext = (Extension) exts.previous();
                ext.setNextIncomingFrames(this.nextIncoming);
                this.nextIncoming = ext;
            }
        }
    }

    @Override
    public void dump(Appendable out, String indent) throws IOException {
        super.dump(out, indent);
        IncomingFrames websocket = this.getLastIncoming();
        OutgoingFrames network = this.getLastOutgoing();
        out.append(indent).append(" +- Stack").append(System.lineSeparator());
        out.append(indent).append("     +- Network  : ").append(network.toString()).append(System.lineSeparator());
        for (Extension ext : this.extensions) {
            out.append(indent).append("     +- Extension: ").append(ext.toString()).append(System.lineSeparator());
        }
        out.append(indent).append("     +- Websocket: ").append(websocket.toString()).append(System.lineSeparator());
    }

    @ManagedAttribute(name = "Extension List", readonly = true)
    public List<Extension> getExtensions() {
        return this.extensions;
    }

    private IncomingFrames getLastIncoming() {
        IncomingFrames last = this.nextIncoming;
        boolean done = false;
        while (!done) {
            if (last instanceof AbstractExtension) {
                last = ((AbstractExtension) last).getNextIncoming();
            } else {
                done = true;
            }
        }
        return last;
    }

    private OutgoingFrames getLastOutgoing() {
        OutgoingFrames last = this.nextOutgoing;
        boolean done = false;
        while (!done) {
            if (last instanceof AbstractExtension) {
                last = ((AbstractExtension) last).getNextOutgoing();
            } else {
                done = true;
            }
        }
        return last;
    }

    public List<ExtensionConfig> getNegotiatedExtensions() {
        List<ExtensionConfig> ret = new ArrayList();
        if (this.extensions == null) {
            return ret;
        } else {
            for (Extension ext : this.extensions) {
                if (ext.getName().charAt(0) != '@') {
                    ret.add(ext.getConfig());
                }
            }
            return ret;
        }
    }

    @ManagedAttribute(name = "Next Incoming Frames Handler", readonly = true)
    public IncomingFrames getNextIncoming() {
        return this.nextIncoming;
    }

    @ManagedAttribute(name = "Next Outgoing Frames Handler", readonly = true)
    public OutgoingFrames getNextOutgoing() {
        return this.nextOutgoing;
    }

    public boolean hasNegotiatedExtensions() {
        return this.extensions != null && this.extensions.size() > 0;
    }

    @Override
    public void incomingError(Throwable e) {
        this.nextIncoming.incomingError(e);
    }

    @Override
    public void incomingFrame(Frame frame) {
        this.nextIncoming.incomingFrame(frame);
    }

    public void negotiate(List<ExtensionConfig> configs) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Extension Configs={}", configs);
        }
        this.extensions = new ArrayList();
        String[] rsvClaims = new String[3];
        for (ExtensionConfig config : configs) {
            Extension ext = this.factory.newInstance(config);
            if (ext != null) {
                if (ext.isRsv1User() && rsvClaims[0] != null) {
                    LOG.debug("Not adding extension {}. Extension {} already claimed RSV1", config, rsvClaims[0]);
                } else if (ext.isRsv2User() && rsvClaims[1] != null) {
                    LOG.debug("Not adding extension {}. Extension {} already claimed RSV2", config, rsvClaims[1]);
                } else if (ext.isRsv3User() && rsvClaims[2] != null) {
                    LOG.debug("Not adding extension {}. Extension {} already claimed RSV3", config, rsvClaims[2]);
                } else {
                    this.extensions.add(ext);
                    this.addBean(ext);
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Adding Extension: {}", config);
                    }
                    if (ext.isRsv1User()) {
                        rsvClaims[0] = ext.getName();
                    }
                    if (ext.isRsv2User()) {
                        rsvClaims[1] = ext.getName();
                    }
                    if (ext.isRsv3User()) {
                        rsvClaims[2] = ext.getName();
                    }
                }
            }
        }
    }

    @Override
    public void outgoingFrame(Frame frame, WriteCallback callback, BatchMode batchMode) {
        ExtensionStack.FrameEntry entry = new ExtensionStack.FrameEntry(frame, callback, batchMode);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Queuing {}", entry);
        }
        this.offerEntry(entry);
        this.flusher.iterate();
    }

    public void setNextIncoming(IncomingFrames nextIncoming) {
        this.nextIncoming = nextIncoming;
    }

    public void setNextOutgoing(OutgoingFrames nextOutgoing) {
        this.nextOutgoing = nextOutgoing;
    }

    public void setPolicy(WebSocketPolicy policy) {
        for (Extension extension : this.extensions) {
            if (extension instanceof AbstractExtension) {
                ((AbstractExtension) extension).setPolicy(policy);
            }
        }
    }

    private void offerEntry(ExtensionStack.FrameEntry entry) {
        synchronized (this) {
            this.entries.offer(entry);
        }
    }

    private ExtensionStack.FrameEntry pollEntry() {
        synchronized (this) {
            return (ExtensionStack.FrameEntry) this.entries.poll();
        }
    }

    private int getQueueSize() {
        synchronized (this) {
            return this.entries.size();
        }
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("ExtensionStack[");
        s.append("queueSize=").append(this.getQueueSize());
        s.append(",extensions=");
        if (this.extensions == null) {
            s.append("<null>");
        } else {
            s.append('[');
            boolean delim = false;
            for (Extension ext : this.extensions) {
                if (delim) {
                    s.append(',');
                }
                if (ext == null) {
                    s.append("<null>");
                } else {
                    s.append(ext.getName());
                }
                delim = true;
            }
            s.append(']');
        }
        s.append(",incoming=").append(this.nextIncoming == null ? "<null>" : this.nextIncoming.getClass().getName());
        s.append(",outgoing=").append(this.nextOutgoing == null ? "<null>" : this.nextOutgoing.getClass().getName());
        s.append("]");
        return s.toString();
    }

    private class Flusher extends IteratingCallback implements WriteCallback {

        private ExtensionStack.FrameEntry current;

        private Flusher() {
        }

        @Override
        protected IteratingCallback.Action process() throws Exception {
            this.current = ExtensionStack.this.pollEntry();
            if (this.current == null) {
                if (ExtensionStack.LOG.isDebugEnabled()) {
                    ExtensionStack.LOG.debug("Entering IDLE");
                }
                return IteratingCallback.Action.IDLE;
            } else {
                if (ExtensionStack.LOG.isDebugEnabled()) {
                    ExtensionStack.LOG.debug("Processing {}", this.current);
                }
                ExtensionStack.this.nextOutgoing.outgoingFrame(this.current.frame, this, this.current.batchMode);
                return IteratingCallback.Action.SCHEDULED;
            }
        }

        @Override
        protected void onCompleteSuccess() {
        }

        @Override
        protected void onCompleteFailure(Throwable x) {
        }

        @Override
        public void writeSuccess() {
            this.notifyCallbackSuccess(this.current.callback);
            this.succeeded();
        }

        @Override
        public void writeFailed(Throwable x) {
            this.notifyCallbackFailure(this.current.callback, x);
            this.succeeded();
        }

        private void notifyCallbackSuccess(WriteCallback callback) {
            try {
                if (callback != null) {
                    callback.writeSuccess();
                }
            } catch (Throwable var3) {
                ExtensionStack.LOG.debug("Exception while notifying success of callback " + callback, var3);
            }
        }

        private void notifyCallbackFailure(WriteCallback callback, Throwable failure) {
            try {
                if (callback != null) {
                    callback.writeFailed(failure);
                }
            } catch (Throwable var4) {
                ExtensionStack.LOG.debug("Exception while notifying failure of callback " + callback, var4);
            }
        }
    }

    private static class FrameEntry {

        private final Frame frame;

        private final WriteCallback callback;

        private final BatchMode batchMode;

        private FrameEntry(Frame frame, WriteCallback callback, BatchMode batchMode) {
            this.frame = frame;
            this.callback = callback;
            this.batchMode = batchMode;
        }

        public String toString() {
            return this.frame.toString();
        }
    }
}