package icyllis.modernui.widget;

import icyllis.modernui.ModernUI;
import icyllis.modernui.core.Handler;
import icyllis.modernui.core.HandlerThread;
import icyllis.modernui.core.Looper;
import icyllis.modernui.core.Message;
import javax.annotation.Nonnull;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.ApiStatus.Internal;

public abstract class Filter {

    private static final Marker MARKER = MarkerManager.getMarker("Filter");

    private static final String THREAD_NAME = "Filter";

    private static final int FILTER_TOKEN = -791613427;

    private static final int FINISH_TOKEN = -559038737;

    private Handler mThreadHandler;

    private HandlerThread mHandlerThread;

    private final Handler mResultHandler;

    private Filter.Delayer mDelayer;

    private final Object mLock = new Object();

    public Filter() {
        this.mResultHandler = new Filter.ResultsHandler();
    }

    public void setDelayer(Filter.Delayer delayer) {
        synchronized (this.mLock) {
            this.mDelayer = delayer;
        }
    }

    public final void filter(CharSequence constraint) {
        this.filter(constraint, null);
    }

    public final void filter(CharSequence constraint, Filter.FilterListener listener) {
        synchronized (this.mLock) {
            if (this.mThreadHandler == null) {
                this.mHandlerThread = new HandlerThread("Filter");
                this.mHandlerThread.start();
                this.mThreadHandler = new Filter.RequestHandler(this.mHandlerThread.getLooper());
            }
            long delay = this.mDelayer == null ? 0L : this.mDelayer.getPostingDelay(constraint);
            Message message = this.mThreadHandler.obtainMessage(-791613427);
            Filter.RequestArguments args = new Filter.RequestArguments();
            args.constraint = constraint != null ? constraint.toString() : null;
            args.listener = listener;
            message.obj = args;
            this.mThreadHandler.removeMessages(-791613427);
            this.mThreadHandler.removeMessages(-559038737);
            this.mThreadHandler.sendMessageDelayed(message, delay);
        }
    }

    protected abstract Filter.FilterResults performFiltering(CharSequence var1);

    protected abstract void publishResults(CharSequence var1, Filter.FilterResults var2);

    public CharSequence convertResultToString(Object resultValue) {
        return resultValue == null ? "" : resultValue.toString();
    }

    @Internal
    public interface Delayer {

        long getPostingDelay(CharSequence var1);
    }

    @FunctionalInterface
    public interface FilterListener {

        void onFilterComplete(int var1);
    }

    protected static class FilterResults {

        public Object values;

        public int count;

        public FilterResults() {
        }
    }

    private static class RequestArguments {

        CharSequence constraint;

        Filter.FilterListener listener;

        Filter.FilterResults results;
    }

    private class RequestHandler extends Handler {

        public RequestHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@Nonnull Message msg) {
            int what = msg.what;
            switch(what) {
                case -791613427:
                    Filter.RequestArguments args = (Filter.RequestArguments) msg.obj;
                    try {
                        args.results = Filter.this.performFiltering(args.constraint);
                    } catch (Exception var15) {
                        args.results = new Filter.FilterResults();
                        ModernUI.LOGGER.warn(Filter.MARKER, "An exception occurred during performFiltering()!", var15);
                    } finally {
                        Message message = Filter.this.mResultHandler.obtainMessage(what);
                        message.obj = args;
                        message.sendToTarget();
                    }
                    synchronized (Filter.this.mLock) {
                        if (Filter.this.mThreadHandler != null) {
                            Message finishMessage = Filter.this.mThreadHandler.obtainMessage(-559038737);
                            Filter.this.mThreadHandler.sendMessageDelayed(finishMessage, 3000L);
                        }
                        break;
                    }
                case -559038737:
                    synchronized (Filter.this.mLock) {
                        if (Filter.this.mThreadHandler != null) {
                            Filter.this.mHandlerThread.quit();
                            Filter.this.mThreadHandler = null;
                            Filter.this.mHandlerThread = null;
                        }
                    }
            }
        }
    }

    private class ResultsHandler extends Handler {

        public ResultsHandler() {
            super(Looper.myLooper());
        }

        @Override
        public void handleMessage(@Nonnull Message msg) {
            Filter.RequestArguments args = (Filter.RequestArguments) msg.obj;
            Filter.this.publishResults(args.constraint, args.results);
            if (args.listener != null) {
                int count = args.results != null ? args.results.count : -1;
                args.listener.onFilterComplete(count);
            }
        }
    }
}