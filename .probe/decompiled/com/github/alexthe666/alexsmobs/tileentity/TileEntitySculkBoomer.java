package com.github.alexthe666.alexsmobs.tileentity;

import com.github.alexthe666.alexsmobs.block.BlockSculkBoomer;
import com.github.alexthe666.alexsmobs.client.particle.AMParticleRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.VibrationParticleOption;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class TileEntitySculkBoomer extends BlockEntity implements GameEventListener {

    private final BlockPositionSource blockPosSource = new BlockPositionSource(this.f_58858_);

    private boolean prevOpen = false;

    private int screamTime = 0;

    public TileEntitySculkBoomer(BlockPos pos, BlockState state) {
        super(AMTileEntityRegistry.SCULK_BOOMER.get(), pos, state);
    }

    public static void commonTick(Level level, BlockPos pos, BlockState state, TileEntitySculkBoomer tileEntity) {
        boolean hasPower = false;
        if (state.m_60734_() instanceof BlockSculkBoomer && !tileEntity.m_58901_()) {
            if (tileEntity.screamTime < 0 && !(Boolean) state.m_61143_(BlockSculkBoomer.POWERED)) {
                AABB screamBox = new AABB((double) (pos.m_123341_() - 4), (double) ((float) pos.m_123342_() - 0.25F), (double) (pos.m_123343_() - 4), (double) (pos.m_123341_() + 4), (double) ((float) pos.m_123342_() + 0.25F), (double) ((float) pos.m_123343_() + 4.0F));
                level.setBlockAndUpdate(pos, (BlockState) state.m_61124_(BlockSculkBoomer.OPEN, true));
                tileEntity.screamTime++;
                if (tileEntity.screamTime >= 0) {
                    tileEntity.screamTime = 100;
                    level.setBlockAndUpdate(pos, (BlockState) state.m_61124_(BlockSculkBoomer.OPEN, false));
                }
                float screamProgress = 1.0F - (float) tileEntity.screamTime / -20.0F;
                Vec3 center = screamBox.getCenter();
                for (LivingEntity entity : level.m_45976_(LivingEntity.class, screamBox)) {
                    double distance = 0.5 + entity.m_20182_().subtract(center).horizontalDistance();
                    if (distance < (double) (4.0F * screamProgress) && distance > (double) (3.5F * screamProgress) && !isOccluded(level, Vec3.atCenterOf(pos), entity.m_20182_())) {
                        entity.hurt(entity.m_269291_().magic(), (float) (6 + entity.getRandom().nextInt(3)));
                        entity.knockback(0.4F, center.x - entity.m_20185_(), center.z - entity.m_20189_());
                    }
                }
            }
            if (tileEntity.screamTime > 0) {
                tileEntity.screamTime--;
            }
            boolean openNow = (Boolean) state.m_61143_(BlockSculkBoomer.OPEN);
            if (!tileEntity.prevOpen && openNow) {
                SoundEvent sound = AMSoundRegistry.SCULK_BOOMER.get();
                if (level.getRandom().nextInt(100) == 0) {
                    sound = AMSoundRegistry.SCULK_BOOMER_FART.get();
                }
                level.playSound((Player) null, pos, sound, SoundSource.BLOCKS, 4.0F, level.random.nextFloat() * 0.2F + 0.9F);
                level.addParticle(AMParticleRegistry.SKULK_BOOM.get(), (double) ((float) pos.m_123341_() + 0.5F), (double) ((float) pos.m_123342_() + 0.5F), (double) ((float) pos.m_123343_() + 0.5F), 0.0, 0.0, 0.0);
            }
            tileEntity.prevOpen = openNow;
        }
    }

    public void tick() {
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("ScreamCooldown", 99)) {
            this.screamTime = tag.getInt("ScreamCooldown");
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("ScreamCooldown", this.screamTime);
    }

    @Override
    public PositionSource getListenerSource() {
        return this.blockPosSource;
    }

    @Override
    public int getListenerRadius() {
        return 8;
    }

    @Override
    public boolean handleGameEvent(ServerLevel serverLevel, GameEvent event, GameEvent.Context message, Vec3 from) {
        if (event == GameEvent.SCULK_SENSOR_TENDRILS_CLICKING && !isOccluded(serverLevel, Vec3.atCenterOf(this.m_58899_()), from)) {
            double distance = from.distanceTo(Vec3.atCenterOf(this.m_58899_()));
            serverLevel.sendParticles(new VibrationParticleOption(new BlockPositionSource(this.m_58899_()), Mth.floor(distance)), from.x, from.y, from.z, 1, 0.0, 0.0, 0.0, 0.0);
            if (this.screamTime == 0) {
                this.screamTime = -20;
            }
        }
        return false;
    }

    private static boolean isOccluded(Level level, Vec3 vec1, Vec3 vec2) {
        Vec3 vec3 = new Vec3((double) Mth.floor(vec1.x) + 0.5, (double) Mth.floor(vec1.y) + 0.5, (double) Mth.floor(vec1.z) + 0.5);
        Vec3 vec31 = new Vec3((double) Mth.floor(vec2.x) + 0.5, (double) Mth.floor(vec2.y) + 0.5, (double) Mth.floor(vec2.z) + 0.5);
        for (Direction direction : Direction.values()) {
            Vec3 vec32 = vec3.relative(direction, 1.0E-5F);
            if (level.m_151353_(new ClipBlockStateContext(vec32, vec31, p_223780_ -> p_223780_.m_204336_(BlockTags.OCCLUDES_VIBRATION_SIGNALS))).getType() != HitResult.Type.BLOCK) {
                return false;
            }
        }
        return true;
    }
}