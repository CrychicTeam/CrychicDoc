package com.simibubi.create.content.fluids.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import org.joml.Quaternionf;

public class BasinFluidParticle extends FluidStackParticle {

    BlockPos basinPos;

    Vec3 targetPos;

    Vec3 centerOfBasin;

    float yOffset;

    public BasinFluidParticle(ClientLevel world, FluidStack fluid, double x, double y, double z, double vx, double vy, double vz) {
        super(world, fluid, x, y, z, vx, vy, vz);
        this.f_107226_ = 0.0F;
        this.f_107215_ = 0.0;
        this.f_107216_ = 0.0;
        this.f_107217_ = 0.0;
        this.yOffset = world.f_46441_.nextFloat() * 1.0F / 32.0F;
        y += (double) this.yOffset;
        this.f_107663_ = 0.0F;
        this.f_107225_ = 60;
        Vec3 currentPos = new Vec3(x, y, z);
        this.basinPos = BlockPos.containing(currentPos);
        this.centerOfBasin = VecHelper.getCenterOf(this.basinPos);
        if (vx != 0.0) {
            this.f_107225_ = 20;
            Vec3 centerOf = VecHelper.getCenterOf(this.basinPos);
            Vec3 diff = currentPos.subtract(centerOf).multiply(1.0, 0.0, 1.0).normalize().scale(0.375);
            this.targetPos = centerOf.add(diff);
            x = this.centerOfBasin.x;
            this.f_107209_ = this.centerOfBasin.x;
            z = this.centerOfBasin.z;
            this.f_107211_ = this.centerOfBasin.z;
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.f_107663_ = this.targetPos != null ? Math.max(0.03125F, 1.0F * (float) this.f_107224_ / (float) this.f_107225_ / 8.0F) : 0.125F * (1.0F - (float) Math.abs(this.f_107224_ - this.f_107225_ / 2) / (1.0F * (float) this.f_107225_));
        if (this.f_107224_ % 2 == 0) {
            if (!AllBlocks.BASIN.has(this.f_107208_.m_8055_(this.basinPos))) {
                this.m_107274_();
                return;
            }
            BlockEntity blockEntity = this.f_107208_.m_7702_(this.basinPos);
            if (blockEntity instanceof BasinBlockEntity) {
                float totalUnits = ((BasinBlockEntity) blockEntity).getTotalFluidUnits(0.0F);
                if (totalUnits < 1.0F) {
                    totalUnits = 0.0F;
                }
                float fluidLevel = Mth.clamp(totalUnits / 2000.0F, 0.0F, 1.0F);
                this.f_107213_ = (double) (0.125F + (float) this.basinPos.m_123342_() + 0.75F * fluidLevel + this.yOffset);
            }
        }
        if (this.targetPos != null) {
            float progess = 1.0F * (float) this.f_107224_ / (float) this.f_107225_;
            Vec3 currentPos = this.centerOfBasin.add(this.targetPos.subtract(this.centerOfBasin).scale((double) progess));
            this.f_107212_ = currentPos.x;
            this.f_107214_ = currentPos.z;
        }
    }

    @Override
    public void render(VertexConsumer vb, Camera info, float pt) {
        Quaternionf rotation = info.rotation();
        Quaternionf prevRotation = new Quaternionf(rotation);
        rotation.set(1.0F, 0.0F, 0.0F, 1.0F);
        rotation.normalize();
        super.m_5744_(vb, info, pt);
        rotation.set(0.0F, 0.0F, 0.0F, 1.0F);
        rotation.mul(prevRotation);
    }

    @Override
    protected boolean canEvaporate() {
        return false;
    }
}