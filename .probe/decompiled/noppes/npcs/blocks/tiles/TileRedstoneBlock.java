package noppes.npcs.blocks.tiles;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import noppes.npcs.CustomBlocks;
import noppes.npcs.CustomNpcs;
import noppes.npcs.blocks.BlockNpcRedstone;
import noppes.npcs.controllers.data.Availability;

public class TileRedstoneBlock extends TileNpcEntity {

    public int onRange = 12;

    public int offRange = 20;

    public int onRangeX = 12;

    public int onRangeY = 12;

    public int onRangeZ = 12;

    public int offRangeX = 20;

    public int offRangeY = 20;

    public int offRangeZ = 20;

    public boolean isDetailed = false;

    public Availability availability = new Availability();

    public boolean isActivated = false;

    private int ticks = 10;

    public TileRedstoneBlock(BlockPos pos, BlockState state) {
        super(CustomBlocks.tile_redstoneblock, pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, TileRedstoneBlock tile) {
        if (!tile.f_58857_.isClientSide) {
            tile.ticks--;
            if (tile.ticks <= 0) {
                tile.ticks = tile.onRange > 10 ? 20 : 10;
                Block block = state.m_60734_();
                if (block != null && block instanceof BlockNpcRedstone) {
                    if (CustomNpcs.FreezeNPCs) {
                        if (tile.isActivated) {
                            tile.setActive(block, false);
                        }
                    } else {
                        if (!tile.isActivated) {
                            int x = tile.isDetailed ? tile.onRangeX : tile.onRange;
                            int y = tile.isDetailed ? tile.onRangeY : tile.onRange;
                            int z = tile.isDetailed ? tile.onRangeZ : tile.onRange;
                            List<Player> list = tile.getPlayerList(x, y, z);
                            if (list.isEmpty()) {
                                return;
                            }
                            for (Player player : list) {
                                if (tile.availability.isAvailable(player)) {
                                    tile.setActive(block, true);
                                    return;
                                }
                            }
                        } else {
                            int x = tile.isDetailed ? tile.offRangeX : tile.offRange;
                            int y = tile.isDetailed ? tile.offRangeY : tile.offRange;
                            int z = tile.isDetailed ? tile.offRangeZ : tile.offRange;
                            for (Player playerx : tile.getPlayerList(x, y, z)) {
                                if (tile.availability.isAvailable(playerx)) {
                                    return;
                                }
                            }
                            tile.setActive(block, false);
                        }
                    }
                }
            }
        }
    }

    private void setActive(Block block, boolean bo) {
        this.isActivated = bo;
        BlockState state = (BlockState) block.defaultBlockState().m_61124_(BlockNpcRedstone.ACTIVE, this.isActivated);
        this.f_58857_.setBlock(this.f_58858_, state, 2);
        this.m_6596_();
        this.f_58857_.sendBlockUpdated(this.f_58858_, state, state, 3);
        block.m_6807_(state, this.f_58857_, this.f_58858_, state, false);
    }

    private List<Player> getPlayerList(int x, int y, int z) {
        return this.f_58857_.m_45976_(Player.class, new AABB((double) this.f_58858_.m_123341_(), (double) this.f_58858_.m_123342_(), (double) this.f_58858_.m_123343_(), (double) (this.f_58858_.m_123341_() + 1), (double) (this.f_58858_.m_123342_() + 1), (double) (this.f_58858_.m_123343_() + 1)).inflate((double) x, (double) y, (double) z));
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.onRange = compound.getInt("BlockOnRange");
        this.offRange = compound.getInt("BlockOffRange");
        this.isDetailed = compound.getBoolean("BlockIsDetailed");
        if (compound.contains("BlockOnRangeX")) {
            this.isDetailed = true;
            this.onRangeX = compound.getInt("BlockOnRangeX");
            this.onRangeY = compound.getInt("BlockOnRangeY");
            this.onRangeZ = compound.getInt("BlockOnRangeZ");
            this.offRangeX = compound.getInt("BlockOffRangeX");
            this.offRangeY = compound.getInt("BlockOffRangeY");
            this.offRangeZ = compound.getInt("BlockOffRangeZ");
        }
        if (compound.contains("BlockActivated")) {
            this.isActivated = compound.getBoolean("BlockActivated");
        }
        this.availability.load(compound);
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        compound.putInt("BlockOnRange", this.onRange);
        compound.putInt("BlockOffRange", this.offRange);
        compound.putBoolean("BlockActivated", this.isActivated);
        compound.putBoolean("BlockIsDetailed", this.isDetailed);
        if (this.isDetailed) {
            compound.putInt("BlockOnRangeX", this.onRangeX);
            compound.putInt("BlockOnRangeY", this.onRangeY);
            compound.putInt("BlockOnRangeZ", this.onRangeZ);
            compound.putInt("BlockOffRangeX", this.offRangeX);
            compound.putInt("BlockOffRangeY", this.offRangeY);
            compound.putInt("BlockOffRangeZ", this.offRangeZ);
        }
        this.availability.save(compound);
        super.saveAdditional(compound);
    }
}