package net.minecraft.util.valueproviders;

import java.util.Arrays;
import net.minecraft.util.RandomSource;

public class MultipliedFloats implements SampledFloat {

    private final SampledFloat[] values;

    public MultipliedFloats(SampledFloat... sampledFloat0) {
        this.values = sampledFloat0;
    }

    @Override
    public float sample(RandomSource randomSource0) {
        float $$1 = 1.0F;
        for (int $$2 = 0; $$2 < this.values.length; $$2++) {
            $$1 *= this.values[$$2].sample(randomSource0);
        }
        return $$1;
    }

    public String toString() {
        return "MultipliedFloats" + Arrays.toString(this.values);
    }
}