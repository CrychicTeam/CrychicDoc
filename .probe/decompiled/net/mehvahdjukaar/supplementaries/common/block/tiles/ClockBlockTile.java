package net.mehvahdjukaar.supplementaries.common.block.tiles;

import net.mehvahdjukaar.moonlight.api.util.math.MthUtils;
import net.mehvahdjukaar.supplementaries.common.block.blocks.ClockBlock;
import net.mehvahdjukaar.supplementaries.common.utils.MiscUtils;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ClockBlockTile extends BlockEntity {

    private int power = 0;

    private float roll = 0.0F;

    private float prevRoll = 0.0F;

    private float targetRoll = 0.0F;

    private float sRoll = 0.0F;

    private float sPrevRoll = 0.0F;

    private float sTargetRoll = 0.0F;

    private float rota;

    private float sRota;

    public ClockBlockTile(BlockPos pos, BlockState state) {
        super((BlockEntityType<?>) ModRegistry.CLOCK_BLOCK_TILE.get(), pos, state);
    }

    public int getPower() {
        return this.power;
    }

    @Override
    public void load(@NotNull CompoundTag compound) {
        super.load(compound);
        this.roll = compound.getFloat("MinRoll");
        this.prevRoll = this.roll;
        this.targetRoll = this.roll;
        this.sRoll = compound.getFloat("SecRoll");
        this.sPrevRoll = this.sRoll;
        this.sTargetRoll = this.sRoll;
        this.power = compound.getInt("Power");
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putFloat("MinRoll", this.targetRoll);
        tag.putFloat("SecRoll", this.sTargetRoll);
        tag.putInt("Power", this.power);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }

    public void updateInitialTime(Level level, BlockState state, BlockPos pos) {
        int time = (int) (level.getDayTime() % 24000L);
        this.updateTime(time, level, state, pos);
        this.roll = this.targetRoll;
        this.prevRoll = this.targetRoll;
        this.sRoll = this.sTargetRoll;
        this.sPrevRoll = this.sTargetRoll;
    }

    public void updateTime(int time, Level level, BlockState state, BlockPos pos) {
        int minute = Mth.clamp(time % 1000 / 20, 0, 50);
        int hour = Mth.clamp(time / 1000, 0, 24);
        if (!level.isClientSide && level.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)) {
            if (hour != (Integer) state.m_61143_(ClockBlock.HOUR)) {
                level.setBlock(pos, (BlockState) state.m_61124_(ClockBlock.HOUR, hour), 3);
            }
            int p = Mth.clamp(time / 1500, 0, 15);
            if (p != this.power) {
                this.power = p;
                level.updateNeighbourForOutputSignal(pos, this.m_58900_().m_60734_());
            }
            this.f_58857_.playSound(null, this.f_58858_, (SoundEvent) (minute % 2 == 0 ? ModSounds.CLOCK_TICK_1 : ModSounds.CLOCK_TICK_2).get(), SoundSource.BLOCKS, 0.08F, MthUtils.nextWeighted(level.random, 0.1F) + 0.95F);
        }
        this.targetRoll = (float) (hour * 30 % 360);
        this.sTargetRoll = ((float) minute * 7.2F + 180.0F) % 360.0F;
    }

    public static boolean canReadTime(Level level) {
        return level.dimensionType().natural() && !MiscUtils.FESTIVITY.isAprilsFool();
    }

    public static void tick(Level level, BlockPos pPos, BlockState pState, ClockBlockTile tile) {
        int dayTime = (int) (level.getDayTime() % 24000L);
        int time = level.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT) ? dayTime : (int) (level.getGameTime() % 24000L);
        if (canReadTime(level)) {
            if (time % 20 == 0) {
                tile.updateTime(dayTime, level, pState, pPos);
            }
            tile.prevRoll = tile.roll;
            if (tile.roll != tile.targetRoll) {
                float r = (tile.roll + 8.0F) % 360.0F;
                if (r >= tile.targetRoll && r <= tile.targetRoll + 8.0F) {
                    r = tile.targetRoll;
                }
                tile.roll = r;
            }
            tile.sPrevRoll = tile.sRoll;
            if (tile.sRoll != tile.sTargetRoll) {
                float r = (tile.sRoll + 8.0F) % 360.0F;
                if (r >= tile.sTargetRoll && r <= tile.sTargetRoll + 8.0F) {
                    r = tile.sTargetRoll;
                }
                tile.sRoll = r;
            }
        } else {
            tile.prevRoll = tile.roll;
            if (time % 5 == 0) {
                float d = level.random.nextFloat() * 360.0F;
                float d0 = d - tile.roll;
                d0 = Mth.positiveModulo(d0 + 180.0F, 360.0F) - 180.0F;
                tile.targetRoll = d0;
                d = level.random.nextFloat() * 360.0F;
                d0 = d - tile.sRoll;
                d0 = Mth.positiveModulo(d0 + 180.0F, 360.0F) - 180.0F;
                tile.sTargetRoll = d0;
            }
            tile.rota = (float) ((double) tile.rota + (double) tile.targetRoll * 0.1);
            tile.rota = (float) ((double) tile.rota * 0.8);
            tile.roll = Mth.positiveModulo(tile.roll + tile.rota, 360.0F);
            tile.sPrevRoll = tile.sRoll;
            tile.sRota = (float) ((double) tile.sRota + (double) tile.sTargetRoll * 0.1);
            tile.sRota = (float) ((double) tile.sRota * 0.8);
            tile.sRoll = Mth.positiveModulo(tile.sRoll + tile.sRota, 360.0F);
        }
    }

    public Direction getDirection() {
        return (Direction) this.m_58900_().m_61143_(ClockBlock.FACING);
    }

    public float getRollS(float partialTicks) {
        return Mth.rotLerp(partialTicks, this.sPrevRoll, this.sRoll);
    }

    public float getRoll(float partialTicks) {
        return Mth.rotLerp(partialTicks, this.prevRoll, this.roll);
    }
}