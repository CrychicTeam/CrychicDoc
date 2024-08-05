package info.journeymap.shaded.kotlin.spark.embeddedserver;

public class NotSupportedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public static void raise(String clazz, String feature) {
        throw new NotSupportedException(clazz, feature);
    }

    private NotSupportedException(String clazz, String feature) {
        super("'" + clazz + "' doesn't support '" + feature + "'");
    }
}