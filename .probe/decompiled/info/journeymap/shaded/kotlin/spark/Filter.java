package info.journeymap.shaded.kotlin.spark;

@FunctionalInterface
public interface Filter {

    void handle(Request var1, Response var2) throws Exception;
}