package icyllis.arc3d.engine;

public final class FlushInfo {

    public BackendSemaphore[] mSignalSemaphores = null;

    public FlushInfo.FinishedCallback mFinishedCallback = null;

    public FlushInfo.SubmittedCallback mSubmittedCallback = null;

    @FunctionalInterface
    public interface FinishedCallback {

        void onFinished();
    }

    @FunctionalInterface
    public interface SubmittedCallback {

        void onSubmitted(boolean var1);
    }
}