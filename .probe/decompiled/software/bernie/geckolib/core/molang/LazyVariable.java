package software.bernie.geckolib.core.molang;

import com.eliotlash.mclib.math.Variable;
import java.util.function.DoubleSupplier;

public class LazyVariable extends Variable {

    private DoubleSupplier valueSupplier;

    public LazyVariable(String name, double value) {
        this(name, () -> value);
    }

    public LazyVariable(String name, DoubleSupplier valueSupplier) {
        super(name, 0.0);
        this.valueSupplier = valueSupplier;
    }

    public void set(double value) {
        this.valueSupplier = () -> value;
    }

    public void set(DoubleSupplier valueSupplier) {
        this.valueSupplier = valueSupplier;
    }

    public double get() {
        return this.valueSupplier.getAsDouble();
    }

    public static LazyVariable from(Variable variable) {
        return new LazyVariable(variable.getName(), variable.get());
    }
}