package com.github.alexmodguy.alexscaves.server.block.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class AmbersolBlockEntity extends BlockEntity {

    private final RandomSource randomSource;

    private final int lights;

    private final float rotSpeed;

    private final float rotOffset;

    private final Vec3 randomOffset;

    public AmbersolBlockEntity(BlockPos pos, BlockState state) {
        super(ACBlockEntityRegistry.AMBERSOL.get(), pos, state);
        this.randomSource = RandomSource.create(pos.asLong());
        this.lights = this.randomSource.nextInt(5) + 4;
        this.rotSpeed = (float) ((double) (this.randomSource.nextFloat() * 0.5F + 1.0F) * this.randomSource.nextGaussian());
        this.rotOffset = this.randomSource.nextFloat() * 360.0F;
        this.randomOffset = new Vec3((double) (this.randomSource.nextFloat() - 0.5F), (double) (this.randomSource.nextFloat() - 0.5F), (double) (this.randomSource.nextFloat() - 0.5F));
    }

    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox() {
        BlockPos pos = this.m_58899_();
        return new AABB(pos.offset(-4, -4, -4), pos.offset(5, 5, 5));
    }

    public RandomSource getRandom() {
        return this.randomSource;
    }

    public int getLights() {
        return this.lights;
    }

    public float getRotOffset() {
        return this.rotOffset;
    }

    public float getRotSpeed() {
        return this.rotSpeed;
    }

    public Vec3 getRandomOffset() {
        return this.randomOffset;
    }

    public float calculateShineScale(Vec3 from) {
        double maxDist = 200.0;
        double dist = Math.min(from.distanceTo(Vec3.atCenterOf(this.m_58899_())), maxDist);
        float f = (float) Math.pow(Math.sin(dist / maxDist * Math.PI), 0.5);
        return f * 3.0F;
    }
}