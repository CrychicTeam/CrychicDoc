package info.journeymap.shaded.org.eclipse.jetty.io;

import info.journeymap.shaded.org.eclipse.jetty.util.component.ContainerLifeCycle;
import info.journeymap.shaded.org.eclipse.jetty.util.component.Dumpable;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.ExecutionStrategy;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.Invocable;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.Locker;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.Scheduler;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.strategy.EatWhatYouKill;
import java.io.Closeable;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ManagedSelector extends ContainerLifeCycle implements Dumpable {

    private static final Logger LOG = Log.getLogger(ManagedSelector.class);

    private final Locker _locker = new Locker();

    private boolean _selecting = false;

    private final Queue<Runnable> _actions = new ArrayDeque();

    private final SelectorManager _selectorManager;

    private final int _id;

    private final ExecutionStrategy _strategy;

    private Selector _selector;

    public ManagedSelector(SelectorManager selectorManager, int id) {
        this._selectorManager = selectorManager;
        this._id = id;
        ManagedSelector.SelectorProducer producer = new ManagedSelector.SelectorProducer();
        Executor executor = selectorManager.getExecutor();
        this._strategy = new EatWhatYouKill(producer, executor);
        this.addBean(this._strategy);
        this.setStopTimeout(5000L);
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
        this._selector = this._selectorManager.newSelector();
        this._selectorManager.execute(this._strategy::produce);
    }

    public int size() {
        Selector s = this._selector;
        return s == null ? 0 : s.keys().size();
    }

    @Override
    protected void doStop() throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Stopping {}", this);
        }
        ManagedSelector.CloseEndPoints close_endps = new ManagedSelector.CloseEndPoints();
        this.submit(close_endps);
        close_endps.await(this.getStopTimeout());
        ManagedSelector.CloseSelector close_selector = new ManagedSelector.CloseSelector();
        this.submit(close_selector);
        close_selector.await(this.getStopTimeout());
        super.doStop();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Stopped {}", this);
        }
    }

    public void submit(Runnable change) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Queued change {} on {}", change, this);
        }
        Selector selector = null;
        try (Locker.Lock lock = this._locker.lock()) {
            this._actions.offer(change);
            if (this._selecting) {
                selector = this._selector;
                this._selecting = false;
            }
        }
        if (selector != null) {
            selector.wakeup();
        }
    }

    private Runnable processConnect(SelectionKey key, final ManagedSelector.Connect connect) {
        SelectableChannel channel = key.channel();
        try {
            key.attach(connect.attachment);
            boolean connected = this._selectorManager.doFinishConnect(channel);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Connected {} {}", connected, channel);
            }
            if (connected) {
                if (connect.timeout.cancel()) {
                    key.interestOps(0);
                    return new ManagedSelector.CreateEndPoint(channel, key) {

                        @Override
                        protected void failed(Throwable failure) {
                            super.failed(failure);
                            connect.failed(failure);
                        }
                    };
                } else {
                    throw new SocketTimeoutException("Concurrent Connect Timeout");
                }
            } else {
                throw new ConnectException();
            }
        } catch (Throwable var5) {
            connect.failed(var5);
            return null;
        }
    }

    private void processAccept(SelectionKey key) {
        SelectableChannel server = key.channel();
        SelectableChannel channel = null;
        try {
            channel = this._selectorManager.doAccept(server);
            if (channel != null) {
                this._selectorManager.accepted(channel);
            }
        } catch (Throwable var5) {
            this.closeNoExceptions(channel);
            LOG.warn("Accept failed for channel " + channel, var5);
        }
    }

    private void closeNoExceptions(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Throwable var3) {
            LOG.ignore(var3);
        }
    }

    private EndPoint createEndPoint(SelectableChannel channel, SelectionKey selectionKey) throws IOException {
        EndPoint endPoint = this._selectorManager.newEndPoint(channel, this, selectionKey);
        endPoint.onOpen();
        this._selectorManager.endPointOpened(endPoint);
        Connection connection = this._selectorManager.newConnection(channel, endPoint, selectionKey.attachment());
        endPoint.setConnection(connection);
        selectionKey.attach(endPoint);
        this._selectorManager.connectionOpened(connection);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Created {}", endPoint);
        }
        return endPoint;
    }

    public void destroyEndPoint(EndPoint endPoint) {
        Connection connection = endPoint.getConnection();
        this.submit(() -> {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Destroyed {}", endPoint);
            }
            if (connection != null) {
                this._selectorManager.connectionClosed(connection);
            }
            this._selectorManager.endPointClosed(endPoint);
        });
    }

    @Override
    public String dump() {
        return ContainerLifeCycle.dump(this);
    }

    @Override
    public void dump(Appendable out, String indent) throws IOException {
        out.append(String.valueOf(this)).append(" id=").append(String.valueOf(this._id)).append(System.lineSeparator());
        Selector selector = this._selector;
        if (selector != null && selector.isOpen()) {
            ArrayList<Object> dump = new ArrayList(selector.keys().size() * 2);
            ManagedSelector.DumpKeys dumpKeys = new ManagedSelector.DumpKeys(dump);
            this.submit(dumpKeys);
            dumpKeys.await(5L, TimeUnit.SECONDS);
            ContainerLifeCycle.dump(out, indent, dump);
        }
    }

    public String toString() {
        Selector selector = this._selector;
        return String.format("%s id=%s keys=%d selected=%d", super.toString(), this._id, selector != null && selector.isOpen() ? selector.keys().size() : -1, selector != null && selector.isOpen() ? selector.selectedKeys().size() : -1);
    }

    public Selector getSelector() {
        return this._selector;
    }

    class Accept extends ManagedSelector.NonBlockingAction implements Closeable {

        private final SelectableChannel channel;

        private final Object attachment;

        Accept(SelectableChannel channel, Object attachment) {
            this.channel = channel;
            this.attachment = attachment;
        }

        public void close() {
            ManagedSelector.LOG.debug("closed accept of {}", this.channel);
            ManagedSelector.this.closeNoExceptions(this.channel);
        }

        public void run() {
            try {
                SelectionKey key = this.channel.register(ManagedSelector.this._selector, 0, this.attachment);
                ManagedSelector.this.submit(ManagedSelector.this.new CreateEndPoint(this.channel, key));
            } catch (Throwable var2) {
                ManagedSelector.this.closeNoExceptions(this.channel);
                ManagedSelector.LOG.debug(var2);
            }
        }
    }

    class Acceptor extends ManagedSelector.NonBlockingAction {

        private final SelectableChannel _channel;

        public Acceptor(SelectableChannel channel) {
            this._channel = channel;
        }

        public void run() {
            try {
                SelectionKey key = this._channel.register(ManagedSelector.this._selector, 16, null);
                if (ManagedSelector.LOG.isDebugEnabled()) {
                    ManagedSelector.LOG.debug("{} acceptor={}", this, key);
                }
            } catch (Throwable var2) {
                ManagedSelector.this.closeNoExceptions(this._channel);
                ManagedSelector.LOG.warn(var2);
            }
        }
    }

    private class CloseEndPoints extends ManagedSelector.NonBlockingAction {

        private final CountDownLatch _latch = new CountDownLatch(1);

        private CountDownLatch _allClosed;

        private CloseEndPoints() {
        }

        public void run() {
            List<EndPoint> end_points = new ArrayList();
            for (SelectionKey key : ManagedSelector.this._selector.keys()) {
                if (key.isValid()) {
                    Object attachment = key.attachment();
                    if (attachment instanceof EndPoint) {
                        end_points.add((EndPoint) attachment);
                    }
                }
            }
            int size = end_points.size();
            if (ManagedSelector.LOG.isDebugEnabled()) {
                ManagedSelector.LOG.debug("Closing {} endPoints on {}", size, ManagedSelector.this);
            }
            this._allClosed = new CountDownLatch(size);
            this._latch.countDown();
            for (EndPoint endp : end_points) {
                ManagedSelector.this.submit(ManagedSelector.this.new EndPointCloser(endp, this._allClosed));
            }
            if (ManagedSelector.LOG.isDebugEnabled()) {
                ManagedSelector.LOG.debug("Closed {} endPoints on {}", size, ManagedSelector.this);
            }
        }

        public boolean await(long timeout) {
            try {
                return this._latch.await(timeout, TimeUnit.MILLISECONDS) && this._allClosed.await(timeout, TimeUnit.MILLISECONDS);
            } catch (InterruptedException var4) {
                return false;
            }
        }
    }

    private class CloseSelector extends ManagedSelector.NonBlockingAction {

        private CountDownLatch _latch = new CountDownLatch(1);

        private CloseSelector() {
        }

        public void run() {
            Selector selector = ManagedSelector.this._selector;
            ManagedSelector.this._selector = null;
            ManagedSelector.this.closeNoExceptions(selector);
            this._latch.countDown();
        }

        public boolean await(long timeout) {
            try {
                return this._latch.await(timeout, TimeUnit.MILLISECONDS);
            } catch (InterruptedException var4) {
                return false;
            }
        }
    }

    class Connect extends ManagedSelector.NonBlockingAction {

        private final AtomicBoolean failed = new AtomicBoolean();

        private final SelectableChannel channel;

        private final Object attachment;

        private final Scheduler.Task timeout;

        Connect(SelectableChannel channel, Object attachment) {
            this.channel = channel;
            this.attachment = attachment;
            this.timeout = ManagedSelector.this._selectorManager.getScheduler().schedule(ManagedSelector.this.new ConnectTimeout(this), ManagedSelector.this._selectorManager.getConnectTimeout(), TimeUnit.MILLISECONDS);
        }

        public void run() {
            try {
                this.channel.register(ManagedSelector.this._selector, 8, this);
            } catch (Throwable var2) {
                this.failed(var2);
            }
        }

        private void failed(Throwable failure) {
            if (this.failed.compareAndSet(false, true)) {
                this.timeout.cancel();
                ManagedSelector.this.closeNoExceptions(this.channel);
                ManagedSelector.this._selectorManager.connectionFailed(this.channel, failure, this.attachment);
            }
        }
    }

    private class ConnectTimeout extends ManagedSelector.NonBlockingAction {

        private final ManagedSelector.Connect connect;

        private ConnectTimeout(ManagedSelector.Connect connect) {
            this.connect = connect;
        }

        public void run() {
            SelectableChannel channel = this.connect.channel;
            if (ManagedSelector.this._selectorManager.isConnectionPending(channel)) {
                if (ManagedSelector.LOG.isDebugEnabled()) {
                    ManagedSelector.LOG.debug("Channel {} timed out while connecting, closing it", channel);
                }
                this.connect.failed(new SocketTimeoutException("Connect Timeout"));
            }
        }
    }

    private class CreateEndPoint implements Runnable, Closeable {

        private final SelectableChannel channel;

        private final SelectionKey key;

        public CreateEndPoint(SelectableChannel channel, SelectionKey key) {
            this.channel = channel;
            this.key = key;
        }

        public void run() {
            try {
                ManagedSelector.this.createEndPoint(this.channel, this.key);
            } catch (Throwable var2) {
                ManagedSelector.LOG.debug(var2);
                this.failed(var2);
            }
        }

        public void close() {
            ManagedSelector.LOG.debug("closed creation of {}", this.channel);
            ManagedSelector.this.closeNoExceptions(this.channel);
        }

        protected void failed(Throwable failure) {
            ManagedSelector.this.closeNoExceptions(this.channel);
            ManagedSelector.LOG.debug(failure);
        }
    }

    private class DumpKeys implements Runnable {

        private final CountDownLatch latch = new CountDownLatch(1);

        private final List<Object> _dumps;

        private DumpKeys(List<Object> dumps) {
            this._dumps = dumps;
        }

        public void run() {
            Selector selector = ManagedSelector.this._selector;
            if (selector != null && selector.isOpen()) {
                Set<SelectionKey> keys = selector.keys();
                this._dumps.add(selector + " keys=" + keys.size());
                for (SelectionKey key : keys) {
                    try {
                        this._dumps.add(String.format("SelectionKey@%x{i=%d}->%s", key.hashCode(), key.interestOps(), key.attachment()));
                    } catch (Throwable var6) {
                        ManagedSelector.LOG.ignore(var6);
                    }
                }
            }
            this.latch.countDown();
        }

        public boolean await(long timeout, TimeUnit unit) {
            try {
                return this.latch.await(timeout, unit);
            } catch (InterruptedException var5) {
                return false;
            }
        }
    }

    private class EndPointCloser implements Runnable {

        private final EndPoint _endPoint;

        private final CountDownLatch _latch;

        private EndPointCloser(EndPoint endPoint, CountDownLatch latch) {
            this._endPoint = endPoint;
            this._latch = latch;
        }

        public void run() {
            ManagedSelector.this.closeNoExceptions(this._endPoint.getConnection());
            this._latch.countDown();
        }
    }

    private abstract static class NonBlockingAction implements Runnable, Invocable {

        private NonBlockingAction() {
        }

        @Override
        public final Invocable.InvocationType getInvocationType() {
            return Invocable.InvocationType.NON_BLOCKING;
        }
    }

    public interface Selectable {

        Runnable onSelected();

        void updateKey();
    }

    private class SelectorProducer implements ExecutionStrategy.Producer {

        private Set<SelectionKey> _keys = Collections.emptySet();

        private Iterator<SelectionKey> _cursor = Collections.emptyIterator();

        private SelectorProducer() {
        }

        @Override
        public Runnable produce() {
            do {
                Runnable task = this.processSelected();
                if (task != null) {
                    return task;
                }
                Runnable action = this.nextAction();
                if (action != null) {
                    return action;
                }
                this.update();
            } while (this.select());
            return null;
        }

        private Runnable nextAction() {
            while (true) {
                Runnable action;
                try (Locker.Lock lock = ManagedSelector.this._locker.lock()) {
                    action = (Runnable) ManagedSelector.this._actions.poll();
                    if (action == null) {
                        ManagedSelector.this._selecting = true;
                        return null;
                    }
                }
                if (Invocable.getInvocationType(action) == Invocable.InvocationType.BLOCKING) {
                    return action;
                }
                try {
                    if (ManagedSelector.LOG.isDebugEnabled()) {
                        ManagedSelector.LOG.debug("Running action {}", action);
                    }
                    action.run();
                } catch (Throwable var15) {
                    ManagedSelector.LOG.debug("Could not run action " + action, var15);
                }
            }
        }

        private boolean select() {
            try {
                Selector selector = ManagedSelector.this._selector;
                if (selector != null && selector.isOpen()) {
                    if (ManagedSelector.LOG.isDebugEnabled()) {
                        ManagedSelector.LOG.debug("Selector loop waiting on select");
                    }
                    int selected = selector.select();
                    if (ManagedSelector.LOG.isDebugEnabled()) {
                        ManagedSelector.LOG.debug("Selector loop woken up from select, {}/{} selected", selected, selector.keys().size());
                    }
                    try (Locker.Lock lock = ManagedSelector.this._locker.lock()) {
                        ManagedSelector.this._selecting = false;
                    }
                    this._keys = selector.selectedKeys();
                    this._cursor = this._keys.iterator();
                    return true;
                }
            } catch (Throwable var16) {
                ManagedSelector.this.closeNoExceptions(ManagedSelector.this._selector);
                if (ManagedSelector.this.isRunning()) {
                    ManagedSelector.LOG.warn(var16);
                } else {
                    ManagedSelector.LOG.debug(var16);
                }
            }
            return false;
        }

        private Runnable processSelected() {
            while (this._cursor.hasNext()) {
                SelectionKey key = (SelectionKey) this._cursor.next();
                if (key.isValid()) {
                    Object attachment = key.attachment();
                    if (ManagedSelector.LOG.isDebugEnabled()) {
                        ManagedSelector.LOG.debug("selected {} {} ", key, attachment);
                    }
                    try {
                        if (attachment instanceof ManagedSelector.Selectable) {
                            Runnable task = ((ManagedSelector.Selectable) attachment).onSelected();
                            if (task != null) {
                                return task;
                            }
                        } else if (key.isConnectable()) {
                            Runnable task = ManagedSelector.this.processConnect(key, (ManagedSelector.Connect) attachment);
                            if (task != null) {
                                return task;
                            }
                        } else {
                            if (!key.isAcceptable()) {
                                throw new IllegalStateException("key=" + key + ", att=" + attachment + ", iOps=" + key.interestOps() + ", rOps=" + key.readyOps());
                            }
                            ManagedSelector.this.processAccept(key);
                        }
                    } catch (CancelledKeyException var4) {
                        ManagedSelector.LOG.debug("Ignoring cancelled key for channel {}", key.channel());
                        if (attachment instanceof EndPoint) {
                            ManagedSelector.this.closeNoExceptions((EndPoint) attachment);
                        }
                    } catch (Throwable var5) {
                        ManagedSelector.LOG.warn("Could not process key for channel " + key.channel(), var5);
                        if (attachment instanceof EndPoint) {
                            ManagedSelector.this.closeNoExceptions((EndPoint) attachment);
                        }
                    }
                } else {
                    if (ManagedSelector.LOG.isDebugEnabled()) {
                        ManagedSelector.LOG.debug("Selector loop ignoring invalid key for channel {}", key.channel());
                    }
                    Object attachment = key.attachment();
                    if (attachment instanceof EndPoint) {
                        ManagedSelector.this.closeNoExceptions((EndPoint) attachment);
                    }
                }
            }
            return null;
        }

        private void update() {
            for (SelectionKey key : this._keys) {
                this.updateKey(key);
            }
            this._keys.clear();
        }

        private void updateKey(SelectionKey key) {
            Object attachment = key.attachment();
            if (attachment instanceof ManagedSelector.Selectable) {
                ((ManagedSelector.Selectable) attachment).updateKey();
            }
        }
    }
}