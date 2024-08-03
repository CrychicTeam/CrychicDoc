package org.violetmoon.zeta.world.generator.multichunk;

import com.google.common.collect.ImmutableList;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
import net.minecraft.world.phys.Vec3;
import org.violetmoon.zeta.config.ZetaGeneralConfig;
import org.violetmoon.zeta.config.type.ClusterSizeConfig;

public record ClusterShape(BlockPos src, Vec3 radius, PerlinSimplexNoise noiseGenerator) {

    public boolean isInside(BlockPos pos) {
        double dx = (double) (pos.m_123341_() - this.src.m_123341_()) / this.radius.x;
        double dy = (double) (pos.m_123342_() - this.src.m_123342_()) / this.radius.y;
        double dz = (double) (pos.m_123343_() - this.src.m_123343_()) / this.radius.z;
        double r = dx * dx + dy * dy + dz * dz;
        if (r > 1.0) {
            return false;
        } else if (ZetaGeneralConfig.useFastWorldgen) {
            return true;
        } else {
            r = Math.sqrt(r);
            double phi = Math.atan2(dz, dx);
            double theta = r == 0.0 ? 0.0 : Math.acos(dy / r);
            double xn = phi + (double) this.src.m_123341_();
            double yn = theta + (double) this.src.m_123343_();
            double noise = this.noiseGenerator.getValue(xn, yn, false);
            double cutoff = Math.PI * 3.0 / 4.0;
            if (phi > cutoff) {
                double noise0 = this.noiseGenerator.getValue(-Math.PI + (double) this.src.m_123341_(), yn, false);
                noise = Mth.lerp((phi - cutoff) / (Math.PI - cutoff), noise, noise0);
            }
            double maxR = noise + 0.5;
            return maxR - r > 0.0;
        }
    }

    public int getUpperBound() {
        return (int) Math.ceil((double) this.src.m_123342_() + this.radius.y());
    }

    public int getLowerBound() {
        return (int) Math.floor((double) this.src.m_123342_() - this.radius.y());
    }

    public static class Provider {

        private final ClusterSizeConfig config;

        private final PerlinSimplexNoise noiseGenerator;

        public Provider(ClusterSizeConfig config, long seed) {
            this.config = config;
            this.noiseGenerator = new PerlinSimplexNoise(new LegacyRandomSource(seed), ImmutableList.of(-4, -3, -2, -1, 0, 1, 2, 3, 4));
        }

        public ClusterShape around(BlockPos src) {
            Random rand = this.randAroundBlockPos(src);
            int radiusX = this.config.horizontalSize + rand.nextInt(this.config.horizontalVariation);
            int radiusY = this.config.verticalSize + rand.nextInt(this.config.verticalVariation);
            int radiusZ = this.config.horizontalSize + rand.nextInt(this.config.horizontalVariation);
            return new ClusterShape(src, new Vec3((double) radiusX, (double) radiusY, (double) radiusZ), this.noiseGenerator);
        }

        public int getRadius() {
            return this.config.horizontalSize + this.config.horizontalVariation;
        }

        public int getRarity() {
            return this.config.rarity;
        }

        public Random randAroundBlockPos(BlockPos pos) {
            return new Random(31L * (31L * (long) (31 + pos.m_123341_()) + (long) pos.m_123342_()) + (long) pos.m_123343_());
        }
    }
}