package info.journeymap.shaded.org.eclipse.jetty.util.thread.strategy;

import info.journeymap.shaded.org.eclipse.jetty.util.component.AbstractLifeCycle;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.ExecutionStrategy;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.Invocable;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.Locker;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.Condition;

public class EatWhatYouKill extends AbstractLifeCycle implements ExecutionStrategy, Runnable {

    private static final Logger LOG = Log.getLogger(EatWhatYouKill.class);

    private final Locker _locker = new Locker();

    private EatWhatYouKill.State _state = EatWhatYouKill.State.IDLE;

    private final Runnable _runProduce = new EatWhatYouKill.RunProduce();

    private final ExecutionStrategy.Producer _producer;

    private final Invocable.InvocableExecutor _executor;

    private int _pendingProducersMax;

    private int _pendingProducers;

    private int _pendingProducersDispatched;

    private int _pendingProducersSignalled;

    private Condition _produce = this._locker.newCondition();

    public EatWhatYouKill(ExecutionStrategy.Producer producer, Executor executor) {
        this(producer, executor, Invocable.InvocationType.NON_BLOCKING, Invocable.InvocationType.BLOCKING);
    }

    public EatWhatYouKill(ExecutionStrategy.Producer producer, Executor executor, int maxProducersPending) {
        this(producer, executor, Invocable.InvocationType.NON_BLOCKING, Invocable.InvocationType.BLOCKING);
    }

    public EatWhatYouKill(ExecutionStrategy.Producer producer, Executor executor, Invocable.InvocationType preferredInvocationPEC, Invocable.InvocationType preferredInvocationEPC) {
        this(producer, executor, preferredInvocationPEC, preferredInvocationEPC, Integer.getInteger("info.journeymap.shaded.org.eclipse.jetty.util.thread.strategy.EatWhatYouKill.maxProducersPending", 1));
    }

    public EatWhatYouKill(ExecutionStrategy.Producer producer, Executor executor, Invocable.InvocationType preferredInvocationPEC, Invocable.InvocationType preferredInvocationEPC, int maxProducersPending) {
        this._producer = producer;
        this._pendingProducersMax = maxProducersPending;
        this._executor = new Invocable.InvocableExecutor(executor, preferredInvocationPEC, preferredInvocationEPC);
    }

    @Override
    public void produce() {
        boolean produce;
        try (Locker.Lock locked = this._locker.lock()) {
            switch(this._state) {
                case IDLE:
                    this._state = EatWhatYouKill.State.PRODUCING;
                    produce = true;
                    break;
                case PRODUCING:
                    this._state = EatWhatYouKill.State.REPRODUCING;
                    produce = false;
                    break;
                default:
                    produce = false;
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("{} execute {}", this, produce);
        }
        if (produce) {
            this.doProduce();
        }
    }

    @Override
    public void dispatch() {
        boolean dispatch = false;
        try (Locker.Lock locked = this._locker.lock()) {
            switch(this._state) {
                case IDLE:
                    dispatch = true;
                    break;
                case PRODUCING:
                    this._state = EatWhatYouKill.State.REPRODUCING;
                    dispatch = false;
                    break;
                default:
                    dispatch = false;
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("{} dispatch {}", this, dispatch);
        }
        if (dispatch) {
            this._executor.execute(this._runProduce, Invocable.InvocationType.BLOCKING);
        }
    }

    public void run() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("{} run", this);
        }
        if (this.isRunning()) {
            boolean producing = false;
            try (Locker.Lock locked = this._locker.lock()) {
                this._pendingProducersDispatched--;
                this._pendingProducers++;
                while (this.isRunning()) {
                    try {
                        this._produce.await();
                        if (this._pendingProducersSignalled != 0) {
                            this._pendingProducersSignalled--;
                            if (this._state == EatWhatYouKill.State.IDLE) {
                                this._state = EatWhatYouKill.State.PRODUCING;
                                producing = true;
                            }
                            break;
                        }
                    } catch (InterruptedException var13) {
                        LOG.debug(var13);
                        this._pendingProducers--;
                        break;
                    }
                }
            }
            if (producing) {
                this.doProduce();
            }
        }
    }

    private void doProduce() {
        boolean may_block_caller = !Invocable.isNonBlockingInvocation();
        if (LOG.isDebugEnabled()) {
            LOG.debug("{} produce {}", this, may_block_caller ? "non-blocking" : "blocking");
        }
        while (this.isRunning()) {
            Runnable task = this._producer.produce();
            StringBuilder state = null;
            boolean produce;
            boolean consume;
            boolean execute_producer;
            try (Locker.Lock locked = this._locker.lock()) {
                if (LOG.isDebugEnabled()) {
                    state = new StringBuilder();
                    this.getString(state);
                    this.getState(state);
                    state.append("->");
                }
                if (task == null) {
                    if (this._state == EatWhatYouKill.State.REPRODUCING) {
                        this._state = EatWhatYouKill.State.PRODUCING;
                        continue;
                    }
                    this._state = EatWhatYouKill.State.IDLE;
                    break;
                }
                if (Invocable.getInvocationType(task) == Invocable.InvocationType.NON_BLOCKING) {
                    produce = true;
                    consume = true;
                    execute_producer = false;
                } else if (!may_block_caller || this._pendingProducers <= 0 && this._pendingProducersMax != 0) {
                    produce = true;
                    consume = false;
                    execute_producer = this._pendingProducersDispatched + this._pendingProducers < this._pendingProducersMax;
                    if (execute_producer) {
                        this._pendingProducersDispatched++;
                    }
                } else {
                    produce = false;
                    consume = true;
                    execute_producer = true;
                    this._pendingProducersDispatched++;
                    this._state = EatWhatYouKill.State.IDLE;
                    this._pendingProducers--;
                    this._pendingProducersSignalled++;
                    this._produce.signal();
                }
                if (LOG.isDebugEnabled()) {
                    this.getState(state);
                }
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("{} {} {}", state, consume ? (execute_producer ? "EPC!" : "PC") : "PEC", task);
            }
            if (execute_producer) {
                this._executor.execute(this);
            }
            if (consume) {
                this._executor.invoke(task);
            } else {
                this._executor.execute(task);
            }
            if (!produce) {
                try (Locker.Lock locked = this._locker.lock()) {
                    if (this._state != EatWhatYouKill.State.IDLE) {
                        break;
                    }
                    this._state = EatWhatYouKill.State.PRODUCING;
                }
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("{} produce exit", this);
        }
    }

    public Boolean isIdle() {
        Boolean var3;
        try (Locker.Lock locked = this._locker.lock()) {
            var3 = this._state == EatWhatYouKill.State.IDLE;
        }
        return var3;
    }

    @Override
    protected void doStop() throws Exception {
        try (Locker.Lock locked = this._locker.lock()) {
            this._pendingProducersSignalled = this._pendingProducers + this._pendingProducersDispatched;
            this._pendingProducers = 0;
            this._produce.signalAll();
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        this.getString(builder);
        try (Locker.Lock locked = this._locker.lock()) {
            this.getState(builder);
        }
        return builder.toString();
    }

    private void getString(StringBuilder builder) {
        builder.append(this.getClass().getSimpleName());
        builder.append('@');
        builder.append(Integer.toHexString(this.hashCode()));
        builder.append('/');
        builder.append(this._producer);
        builder.append('/');
    }

    private void getState(StringBuilder builder) {
        builder.append(this._state);
        builder.append('/');
        builder.append(this._pendingProducers);
        builder.append('/');
        builder.append(this._pendingProducersMax);
    }

    private class RunProduce implements Runnable {

        private RunProduce() {
        }

        public void run() {
            EatWhatYouKill.this.produce();
        }
    }

    static enum State {

        IDLE, PRODUCING, REPRODUCING
    }
}