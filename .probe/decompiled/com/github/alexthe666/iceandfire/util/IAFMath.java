package com.github.alexthe666.iceandfire.util;

import java.util.Collections;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;

public class IAFMath {

    public static final List<Player> emptyPlayerEntityList = Collections.emptyList();

    public static final List<ItemEntity> emptyItemEntityList = Collections.emptyList();

    public static final List<AbstractHorse> emptyAbstractHorseEntityList = Collections.emptyList();

    public static final List<Entity> emptyEntityList = Collections.emptyList();

    public static final List<LivingEntity> emptyLivingEntityList = Collections.emptyList();

    private static final double coeff_1 = Math.PI / 4;

    private static final double coeff_2 = Math.PI * 3.0 / 4.0;

    public static double atan2_accurate(double y, double x) {
        if (y < 0.0) {
            y = -y;
            if (x > 0.0) {
                double r = (x - y) / (x + y);
                return -(0.1963 * r * r * r - 0.9817 * r + (Math.PI / 4));
            } else {
                double r = (x + y) / (y - x);
                return -(0.1963 * r * r * r - 0.9817 * r + (Math.PI * 3.0 / 4.0));
            }
        } else {
            if (y == 0.0) {
                y = 1.0E-25;
            }
            if (x > 0.0) {
                double r = (x - y) / (x + y);
                return 0.1963 * r * r * r - 0.9817 * r + (Math.PI / 4);
            } else {
                double r = (x + y) / (y - x);
                return 0.1963 * r * r * r - 0.9817 * r + (Math.PI * 3.0 / 4.0);
            }
        }
    }
}