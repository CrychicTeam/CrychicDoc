package software.bernie.geckolib.core.molang.functions;

import com.eliotlash.mclib.math.IValue;
import com.eliotlash.mclib.math.functions.Function;

public class CosDegrees extends Function {

    public CosDegrees(IValue[] values, String name) throws Exception {
        super(values, name);
    }

    public int getRequiredArguments() {
        return 1;
    }

    public double get() {
        return Math.cos(this.getArg(0) / 180.0 * Math.PI);
    }
}