package org.violetmoon.quark.content.world.block;

import java.awt.Color;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;

public class MyaliteColorLogic {

    protected static final float s = 0.7F;

    protected static final float b = 0.8F;

    protected static final PerlinSimplexNoise noise = new PerlinSimplexNoise(new LegacyRandomSource(4543543L), List.of(-4, -3, -2, -1, 0, 1, 2, 3, 4));

    public static int getColor(BlockPos pos) {
        float sp = 0.15F;
        double range = 0.3;
        double shift = 0.05;
        if (pos == null) {
            pos = BlockPos.ZERO;
        }
        float x = (float) pos.m_123341_() * 0.15F;
        float y = (float) pos.m_123342_() * 0.15F;
        float z = (float) pos.m_123343_() * 0.15F;
        double xv = (double) (x + Mth.sin(z) * 2.0F);
        double zv = (double) (z + Mth.cos(x) * 2.0F);
        double yv = (double) (y + Mth.sin(y + (float) (Math.PI / 4)) * 2.0F);
        double noiseVal = noise.getValue(xv + yv, zv + yv * 2.0, false);
        double h = noiseVal * 0.15 - 0.3 + 0.05;
        return Color.HSBtoRGB((float) h, 0.7F, 0.8F);
    }
}