package net.minecraft.world.level;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.core.BlockPos;

public class PotentialCalculator {

    private final List<PotentialCalculator.PointCharge> charges = Lists.newArrayList();

    public void addCharge(BlockPos blockPos0, double double1) {
        if (double1 != 0.0) {
            this.charges.add(new PotentialCalculator.PointCharge(blockPos0, double1));
        }
    }

    public double getPotentialEnergyChange(BlockPos blockPos0, double double1) {
        if (double1 == 0.0) {
            return 0.0;
        } else {
            double $$2 = 0.0;
            for (PotentialCalculator.PointCharge $$3 : this.charges) {
                $$2 += $$3.getPotentialChange(blockPos0);
            }
            return $$2 * double1;
        }
    }

    static class PointCharge {

        private final BlockPos pos;

        private final double charge;

        public PointCharge(BlockPos blockPos0, double double1) {
            this.pos = blockPos0;
            this.charge = double1;
        }

        public double getPotentialChange(BlockPos blockPos0) {
            double $$1 = this.pos.m_123331_(blockPos0);
            return $$1 == 0.0 ? Double.POSITIVE_INFINITY : this.charge / Math.sqrt($$1);
        }
    }
}