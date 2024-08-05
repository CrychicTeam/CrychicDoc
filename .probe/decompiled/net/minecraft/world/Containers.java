package net.minecraft.world;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Containers {

    public static void dropContents(Level level0, BlockPos blockPos1, Container container2) {
        dropContents(level0, (double) blockPos1.m_123341_(), (double) blockPos1.m_123342_(), (double) blockPos1.m_123343_(), container2);
    }

    public static void dropContents(Level level0, Entity entity1, Container container2) {
        dropContents(level0, entity1.getX(), entity1.getY(), entity1.getZ(), container2);
    }

    private static void dropContents(Level level0, double double1, double double2, double double3, Container container4) {
        for (int $$5 = 0; $$5 < container4.getContainerSize(); $$5++) {
            dropItemStack(level0, double1, double2, double3, container4.getItem($$5));
        }
    }

    public static void dropContents(Level level0, BlockPos blockPos1, NonNullList<ItemStack> nonNullListItemStack2) {
        nonNullListItemStack2.forEach(p_19009_ -> dropItemStack(level0, (double) blockPos1.m_123341_(), (double) blockPos1.m_123342_(), (double) blockPos1.m_123343_(), p_19009_));
    }

    public static void dropItemStack(Level level0, double double1, double double2, double double3, ItemStack itemStack4) {
        double $$5 = (double) EntityType.ITEM.getWidth();
        double $$6 = 1.0 - $$5;
        double $$7 = $$5 / 2.0;
        double $$8 = Math.floor(double1) + level0.random.nextDouble() * $$6 + $$7;
        double $$9 = Math.floor(double2) + level0.random.nextDouble() * $$6;
        double $$10 = Math.floor(double3) + level0.random.nextDouble() * $$6 + $$7;
        while (!itemStack4.isEmpty()) {
            ItemEntity $$11 = new ItemEntity(level0, $$8, $$9, $$10, itemStack4.split(level0.random.nextInt(21) + 10));
            float $$12 = 0.05F;
            $$11.m_20334_(level0.random.triangle(0.0, 0.11485000171139836), level0.random.triangle(0.2, 0.11485000171139836), level0.random.triangle(0.0, 0.11485000171139836));
            level0.m_7967_($$11);
        }
    }
}