package info.journeymap.shaded.kotlin.spark;

@FunctionalInterface
public interface Route {

    Object handle(Request var1, Response var2) throws Exception;
}