package software.bernie.geckolib.core.molang.expressions;

import com.eliotlash.mclib.math.Constant;
import com.eliotlash.mclib.math.IValue;

public class MolangValue implements IValue {

    private final IValue value;

    private final boolean returns;

    public MolangValue(IValue value) {
        this(value, false);
    }

    public MolangValue(IValue value, boolean isReturn) {
        this.value = value;
        this.returns = isReturn;
    }

    public double get() {
        return this.value.get();
    }

    public IValue getValueHolder() {
        return this.value;
    }

    public boolean isReturnValue() {
        return this.returns;
    }

    public boolean isConstant() {
        return this.getClass() == MolangValue.class && this.value instanceof Constant;
    }

    public String toString() {
        return (this.returns ? "return " : "") + this.value.toString();
    }
}