package software.bernie.geckolib.core.molang.functions;

import com.eliotlash.mclib.math.IValue;
import com.eliotlash.mclib.math.functions.Function;

public class SinDegrees extends Function {

    public SinDegrees(IValue[] values, String name) throws Exception {
        super(values, name);
    }

    public int getRequiredArguments() {
        return 1;
    }

    public double get() {
        return Math.sin(this.getArg(0) / 180.0 * Math.PI);
    }
}