package info.journeymap.shaded.kotlin.spark;

public abstract class ExceptionHandlerImpl<T extends Exception> implements ExceptionHandler<T> {

    protected Class<? extends T> exceptionClass;

    public ExceptionHandlerImpl(Class<T> exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    public Class<? extends T> exceptionClass() {
        return this.exceptionClass;
    }

    public void exceptionClass(Class<? extends T> exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    @Override
    public abstract void handle(T var1, Request var2, Response var3);
}