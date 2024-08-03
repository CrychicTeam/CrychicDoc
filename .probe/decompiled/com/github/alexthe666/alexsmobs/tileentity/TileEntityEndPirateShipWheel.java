package com.github.alexthe666.alexsmobs.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TileEntityEndPirateShipWheel extends BlockEntity {

    private float wheelRot;

    private float prevWheelRot;

    private float targetWheelRot;

    public int ticksExisted;

    public TileEntityEndPirateShipWheel(BlockPos pos, BlockState state) {
        super(AMTileEntityRegistry.END_PIRATE_SHIP_WHEEL.get(), pos, state);
    }

    public static void commonTick(Level level, BlockPos pos, BlockState state, TileEntityEndPirateShipWheel entity) {
        entity.tick();
    }

    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox() {
        return new AABB(this.f_58858_.offset(-2, -2, -2), this.f_58858_.offset(2, 2, 2));
    }

    public void tick() {
        this.prevWheelRot = this.wheelRot;
        float scale = Math.abs(Math.abs(this.targetWheelRot - this.wheelRot) / 180.0F);
        float progress = Mth.clamp(10.0F * scale, 1.0F, 10.0F);
        if (this.wheelRot < this.targetWheelRot) {
            this.wheelRot = Math.min(this.targetWheelRot, this.wheelRot + progress);
        }
        if (this.wheelRot > this.targetWheelRot) {
            this.wheelRot = Math.max(this.targetWheelRot, this.wheelRot - progress);
        }
        this.ticksExisted++;
    }

    public void rotate(boolean clockwise) {
        if (Math.abs(this.wheelRot - this.targetWheelRot) < 90.0F) {
            if (clockwise) {
                this.targetWheelRot += 180.0F;
            } else {
                this.targetWheelRot -= 180.0F;
            }
        }
    }

    public float getWheelRot(float partialTick) {
        return this.prevWheelRot + (this.wheelRot - this.prevWheelRot) * partialTick;
    }
}