package com.corosus.coroutil.util;

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class CoroUtilParticle {

    public static Vec3[] rainPositions = new Vec3[CoroUtilParticle.maxRainDrops];

    public static int maxRainDrops = 2000;

    public static Random rand = new Random();

    public static Level getWorldParticle(Object obj) {
        return Minecraft.getInstance().level;
    }

    static {
        float range = 10.0F;
        for (int i = 0; i < maxRainDrops; i++) {
            rainPositions[i] = new Vec3((double) (rand.nextFloat() * range - range / 2.0F), (double) (rand.nextFloat() * range / 1.0F - range / 2.0F), (double) (rand.nextFloat() * range - range / 2.0F));
        }
    }
}