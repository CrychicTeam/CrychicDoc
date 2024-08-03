package com.github.alexthe666.alexsmobs.tileentity;

import com.github.alexthe666.alexsmobs.block.BlockVoidWormBeak;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class TileEntityVoidWormBeak extends BlockEntity {

    private float chompProgress;

    private float prevChompProgress;

    public int ticksExisted;

    public TileEntityVoidWormBeak(BlockPos pos, BlockState state) {
        super(AMTileEntityRegistry.VOID_WORM_BEAK.get(), pos, state);
    }

    public static void commonTick(Level level, BlockPos pos, BlockState state, TileEntityVoidWormBeak entity) {
        entity.tick();
    }

    public void tick() {
        this.prevChompProgress = this.chompProgress;
        boolean powered = false;
        if (this.m_58900_().m_60734_() instanceof BlockVoidWormBeak) {
            powered = (Boolean) this.m_58900_().m_61143_(BlockVoidWormBeak.POWERED);
        }
        if (powered && this.chompProgress < 5.0F) {
            this.chompProgress++;
        }
        if (!powered && this.chompProgress > 0.0F) {
            this.chompProgress--;
        }
        if (this.chompProgress >= 5.0F && !this.f_58857_.isClientSide && this.ticksExisted % 5 == 0) {
            float i = (float) this.m_58899_().m_123341_() + 0.5F;
            float j = (float) this.m_58899_().m_123342_() + 0.5F;
            float k = (float) this.m_58899_().m_123343_() + 0.5F;
            float d0 = 0.5F;
            for (LivingEntity entity : this.f_58857_.m_45976_(LivingEntity.class, new AABB((double) i - (double) d0, (double) j - (double) d0, (double) k - (double) d0, (double) i + (double) d0, (double) j + (double) d0, (double) k + (double) d0))) {
                entity.hurt(entity.m_269291_().generic(), 5.0F);
            }
        }
        this.ticksExisted++;
    }

    public float getChompProgress(float partialTick) {
        return this.prevChompProgress + (this.chompProgress - this.prevChompProgress) * partialTick;
    }
}