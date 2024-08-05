package info.journeymap.shaded.kotlin.spark;

@FunctionalInterface
public interface ExceptionHandler<T extends Exception> {

    void handle(T var1, Request var2, Response var3);
}