package net.mehvahdjukaar.supplementaries.common.block.tiles;

import net.mehvahdjukaar.supplementaries.common.block.blocks.TurnTableBlock;
import net.mehvahdjukaar.supplementaries.common.utils.BlockUtil;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class TurnTableBlockTile extends BlockEntity {

    private int cooldown = 5;

    private boolean canRotate = false;

    private int catTimer = 0;

    public TurnTableBlockTile(BlockPos pos, BlockState state) {
        super((BlockEntityType<?>) ModRegistry.TURN_TABLE_TILE.get(), pos, state);
    }

    public void tryRotate() {
        this.canRotate = true;
        this.cooldown = TurnTableBlock.getPeriod(this.m_58900_());
    }

    public int getCatTimer() {
        return this.catTimer;
    }

    public void setCat() {
        this.catTimer = 400;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, TurnTableBlockTile tile) {
        tile.catTimer = Math.max(tile.catTimer - 1, 0);
        if (tile.cooldown == 0) {
            Direction dir = (Direction) state.m_61143_(TurnTableBlock.FACING);
            boolean ccw = (Boolean) state.m_61143_(TurnTableBlock.INVERTED) ^ state.m_61143_(TurnTableBlock.FACING) == Direction.DOWN;
            BlockPos targetPos = pos.relative(dir);
            boolean success = BlockUtil.tryRotatingBlock(dir, ccw, targetPos, level, null).isPresent();
            if (success) {
                level.blockEvent(pos, state.m_60734_(), 0, 0);
                level.m_142346_(null, GameEvent.BLOCK_CHANGE, targetPos);
                level.playSound(null, targetPos, SoundEvents.ITEM_FRAME_ROTATE_ITEM, SoundSource.BLOCKS, 1.0F, 0.6F);
            }
            tile.cooldown = TurnTableBlock.getPeriod(state);
            int power = (Integer) state.m_61143_(TurnTableBlock.POWER);
            tile.canRotate = success && power != 0;
            if (power == 0) {
                level.setBlock(pos, (BlockState) state.m_61124_(TurnTableBlock.ROTATING, false), 3);
            }
        } else if (tile.canRotate) {
            tile.cooldown--;
        }
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.cooldown = compound.getInt("Cooldown");
        this.canRotate = compound.getBoolean("CanRotate");
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("Cooldown", this.cooldown);
        compound.putBoolean("CanRotate", this.canRotate);
    }
}