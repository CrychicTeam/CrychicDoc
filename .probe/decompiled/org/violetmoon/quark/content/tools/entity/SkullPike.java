package org.violetmoon.quark.content.tools.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.content.tools.module.SkullPikesModule;

public class SkullPike extends Entity {

    public SkullPike(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.m_9236_() instanceof ServerLevel sworld) {
            boolean good = false;
            BlockPos pos = this.m_20183_();
            BlockState state = this.m_9236_().getBlockState(pos);
            if (state.m_204336_(SkullPikesModule.pikeTrophiesTag)) {
                BlockPos down = pos.below();
                BlockState downState = this.m_9236_().getBlockState(down);
                if (downState.m_204336_(BlockTags.FENCES)) {
                    good = true;
                }
            }
            if (!good) {
                this.m_6089_();
            }
            if (Math.random() < 0.4) {
                sworld.sendParticles(Math.random() < 0.05 ? ParticleTypes.WARPED_SPORE : ParticleTypes.ASH, (double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.25, (double) pos.m_123343_() + 0.5, 1, 0.25, 0.25, 0.25, 0.0);
            }
        }
    }

    public boolean isVisible(Entity entityIn) {
        Vec3 vector3d = new Vec3(this.m_20185_(), this.m_20186_() + 1.0, this.m_20189_());
        Vec3 vector3d1 = new Vec3(entityIn.getX(), entityIn.getEyeY(), entityIn.getZ());
        return this.m_9236_().m_45547_(new ClipContext(vector3d, vector3d1, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() == HitResult.Type.MISS;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag nbt) {
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag nbt) {
    }

    @NotNull
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}