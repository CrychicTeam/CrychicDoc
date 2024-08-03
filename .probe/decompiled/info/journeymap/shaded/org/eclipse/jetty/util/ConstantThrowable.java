package info.journeymap.shaded.org.eclipse.jetty.util;

public class ConstantThrowable extends Throwable {

    public ConstantThrowable() {
        this(null);
    }

    public ConstantThrowable(String name) {
        super(name, null, false, false);
    }

    public String toString() {
        return String.valueOf(this.getMessage());
    }
}