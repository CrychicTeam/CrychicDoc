package se.mickelus.tetra.blocks.forged.extractor;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;
import se.mickelus.mutil.util.TileEntityOptional;
import se.mickelus.tetra.TetraMod;

@ParametersAreNonnullByDefault
public class CoreExtractorPistonBlockEntity extends BlockEntity {

    static final long activationDuration = 105L;

    private static final int fillAmount = 40;

    public static RegistryObject<BlockEntityType<CoreExtractorPistonBlockEntity>> type;

    private long endTime = Long.MAX_VALUE;

    public CoreExtractorPistonBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(type.get(), blockPos, blockState);
    }

    public void activate() {
        if (!this.isActive()) {
            this.endTime = this.f_58857_.getGameTime() + 105L;
            if (!this.f_58857_.isClientSide) {
                TetraMod.packetHandler.sendToAllPlayersNear(new CoreExtractorPistonUpdatePacket(this.f_58858_, this.endTime), this.f_58858_, 64.0, this.f_58857_.dimension());
            }
        }
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public boolean isActive() {
        return this.endTime != Long.MAX_VALUE;
    }

    public float getProgress(float partialTicks) {
        return this.isActive() ? Math.min(1.0F, Math.max(0.0F, ((float) (this.f_58857_.getGameTime() + 105L - this.endTime) + partialTicks) / 105.0F)) : 0.0F;
    }

    private void runEndEffects() {
        if (this.f_58857_ instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, (double) this.f_58858_.m_123341_() + 0.5, (double) this.f_58858_.m_123342_() + 0.1, (double) this.f_58858_.m_123343_() + 0.5, 5, 0.0, 0.0, 0.0, 0.02F);
        }
        this.f_58857_.playSound(null, this.f_58858_, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.1F, 1.0F);
        this.f_58857_.playSound(null, this.f_58858_, SoundEvents.METAL_FALL, SoundSource.BLOCKS, 0.2F, 0.5F);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (this.endTime < level.getGameTime()) {
            this.endTime = Long.MAX_VALUE;
            if (!level.isClientSide) {
                TileEntityOptional.from(level, pos.relative(Direction.DOWN), CoreExtractorBaseBlockEntity.class).ifPresent(base -> base.fill(40));
                this.runEndEffects();
                this.m_6596_();
            }
        }
    }
}