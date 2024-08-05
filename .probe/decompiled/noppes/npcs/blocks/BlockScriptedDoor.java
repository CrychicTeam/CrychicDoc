package noppes.npcs.blocks;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import noppes.npcs.CustomBlocks;
import noppes.npcs.CustomItems;
import noppes.npcs.EventHooks;
import noppes.npcs.blocks.tiles.TileScriptedDoor;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.packets.server.SPacketGuiOpen;

public class BlockScriptedDoor extends BlockNpcDoorInterface {

    public BlockScriptedDoor() {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_DOOR).strength(5.0F, 10.0F));
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter worldIn, BlockPos pos, BlockState state) {
        return new ItemStack(CustomBlocks.scripted_door_item);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileScriptedDoor(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockPos blockpos1 = state.m_61143_(f_52730_) == DoubleBlockHalf.LOWER ? pos : pos.below();
            BlockState iblockstate1 = pos.equals(blockpos1) ? state : level.getBlockState(blockpos1);
            if (iblockstate1.m_60734_() != this) {
                return InteractionResult.FAIL;
            } else {
                ItemStack currentItem = player.getInventory().getSelected();
                if (currentItem == null || currentItem.getItem() != CustomItems.wand && currentItem.getItem() != CustomItems.scripter && currentItem.getItem() != CustomBlocks.scripted_door_item) {
                    TileScriptedDoor tile = (TileScriptedDoor) level.getBlockEntity(blockpos1);
                    Vec3 vec = ray.m_82450_();
                    float x = (float) (vec.x - (double) pos.m_123341_());
                    float y = (float) (vec.y - (double) pos.m_123342_());
                    float z = (float) (vec.z - (double) pos.m_123343_());
                    if (EventHooks.onScriptBlockInteract(tile, player, ray.getDirection().get3DDataValue(), x, y, z)) {
                        return InteractionResult.FAIL;
                    } else {
                        this.setOpen(player, level, iblockstate1, blockpos1, ((Boolean) iblockstate1.m_61143_(DoorBlock.OPEN)).equals(false));
                        return InteractionResult.SUCCESS;
                    }
                } else {
                    PlayerData data = PlayerData.get(player);
                    data.scriptBlockPos = blockpos1;
                    SPacketGuiOpen.sendOpenGui(player, EnumGuiType.ScriptDoor, null, blockpos1);
                    return InteractionResult.SUCCESS;
                }
            }
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block neighborBlock, BlockPos pos2, boolean isMoving) {
        if (state.m_61143_(f_52730_) == DoubleBlockHalf.UPPER) {
            BlockPos blockpos1 = pos.below();
            BlockState iblockstate1 = worldIn.getBlockState(blockpos1);
            if (iblockstate1.m_60734_() != this) {
                worldIn.removeBlock(pos, false);
            } else if (neighborBlock != this) {
                this.neighborChanged(iblockstate1, worldIn, blockpos1, neighborBlock, blockpos1, isMoving);
            }
        } else {
            BlockPos blockpos2 = pos.above();
            BlockState iblockstate2 = worldIn.getBlockState(blockpos2);
            if (iblockstate2.m_60734_() != this) {
                worldIn.removeBlock(pos, false);
            } else {
                TileScriptedDoor tile = (TileScriptedDoor) worldIn.getBlockEntity(pos);
                if (!worldIn.isClientSide) {
                    EventHooks.onScriptBlockNeighborChanged(tile, pos2);
                }
                boolean flag = worldIn.m_276867_(pos) || worldIn.m_276867_(blockpos2);
                if ((flag || neighborBlock.defaultBlockState().m_60803_()) && neighborBlock != this && flag != (Boolean) iblockstate2.m_61143_(f_52729_)) {
                    worldIn.setBlock(blockpos2, (BlockState) iblockstate2.m_61124_(f_52729_, flag), 2);
                    if (flag != (Boolean) state.m_61143_(f_52727_)) {
                        this.setOpen(null, worldIn, state, pos, flag);
                    }
                }
                int power = 0;
                for (Direction enumfacing : Direction.values()) {
                    int p = worldIn.m_277185_(pos.relative(enumfacing), enumfacing);
                    if (p > power) {
                        power = p;
                    }
                }
                tile.newPower = power;
            }
        }
    }

    @Override
    public void setOpen(Entity entity, Level worldIn, BlockState state, BlockPos pos, boolean open) {
        TileScriptedDoor tile = (TileScriptedDoor) worldIn.getBlockEntity(pos);
        if (!EventHooks.onScriptBlockDoorToggle(tile)) {
            super.m_153165_(entity, worldIn, state, pos, open);
        }
    }

    @Override
    public void attack(BlockState state, Level level, BlockPos pos, Player playerIn) {
        if (!level.isClientSide) {
            BlockPos blockpos1 = state.m_61143_(f_52730_) == DoubleBlockHalf.LOWER ? pos : pos.below();
            BlockState iblockstate1 = pos.equals(blockpos1) ? state : level.getBlockState(blockpos1);
            if (iblockstate1.m_60734_() == this) {
                TileScriptedDoor tile = (TileScriptedDoor) level.getBlockEntity(blockpos1);
                EventHooks.onScriptBlockClicked(tile, playerIn);
            }
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.m_60734_() != newState.m_60734_()) {
            BlockPos blockpos1 = state.m_61143_(f_52730_) == DoubleBlockHalf.LOWER ? pos : pos.below();
            BlockState iblockstate1 = pos.equals(blockpos1) ? state : level.getBlockState(blockpos1);
            if (!level.isClientSide && iblockstate1.m_60734_() == this) {
                TileScriptedDoor tile = (TileScriptedDoor) level.getBlockEntity(pos);
                EventHooks.onScriptBlockBreak(tile);
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (!level.isClientSide) {
            TileScriptedDoor tile = (TileScriptedDoor) level.getBlockEntity(pos);
            if (EventHooks.onScriptBlockHarvest(tile, player)) {
                return false;
            }
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entityIn) {
        if (!level.isClientSide) {
            TileScriptedDoor tile = (TileScriptedDoor) level.getBlockEntity(pos);
            EventHooks.onScriptBlockCollide(tile, entityIn);
        }
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BlockPos blockpos1 = state.m_61143_(f_52730_) == DoubleBlockHalf.LOWER ? pos : pos.below();
        BlockState iblockstate1 = pos.equals(blockpos1) ? state : level.getBlockState(blockpos1);
        if (player.getAbilities().instabuild && iblockstate1.m_61143_(f_52730_) == DoubleBlockHalf.LOWER && iblockstate1.m_60734_() == this) {
            level.removeBlock(blockpos1, false);
        }
    }

    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        float f = ((TileScriptedDoor) level.getBlockEntity(pos)).blockHardness;
        if (f == -1.0F) {
            return 0.0F;
        } else {
            int i = ForgeHooks.isCorrectToolForDrops(state, player) ? 30 : 100;
            return player.getDigSpeed(state, pos) / f / (float) i;
        }
    }

    public float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        return ((TileScriptedDoor) level.getBlockEntity(pos)).blockResistance;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, CustomBlocks.tile_scripteddoor, TileScriptedDoor::tick);
    }

    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> blockEntityTypeA0, BlockEntityType<E> blockEntityTypeE1, BlockEntityTicker<? super E> blockEntityTickerSuperE2) {
        return blockEntityTypeE1 == blockEntityTypeA0 ? blockEntityTickerSuperE2 : null;
    }
}