package com.rekindled.embers.blockentity;

import com.rekindled.embers.EmbersEvents;
import com.rekindled.embers.compat.curios.CuriosCompat;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.particle.GlowParticleOptions;
import com.rekindled.embers.particle.SmokeParticleOptions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ExplosionPedestalBlockEntity extends BlockEntity {

    public ExplosionPedestalBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CuriosCompat.EXPLOSION_PEDESTAL_ENTITY.get(), pPos, pBlockState);
    }

    public void onLoad() {
        super.onLoad();
        EmbersEvents.putExplosionCharm(this.f_58857_, this.f_58858_);
    }

    public void absorb(Explosion explosion) {
        Vec3 explosionPos = explosion.getPosition();
        if (this.f_58857_ instanceof ServerLevel server) {
            this.f_58857_.playSound(null, explosionPos.x, explosionPos.y, explosionPos.z, EmbersSounds.EXPLOSION_CHARM_ABSORB.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            server.sendParticles(GlowParticleOptions.EMBER, explosionPos.x, explosionPos.y, explosionPos.z, 3, 0.0, 0.0, 0.0, 0.1);
            server.sendParticles(SmokeParticleOptions.BIG_SMOKE, explosionPos.x, explosionPos.y, explosionPos.z, 20, 0.25, 0.25, 0.25, 1.0);
        }
    }
}