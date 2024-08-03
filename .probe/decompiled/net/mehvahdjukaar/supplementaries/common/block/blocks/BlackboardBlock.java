package net.mehvahdjukaar.supplementaries.common.block.blocks;

import net.mehvahdjukaar.moonlight.api.block.IWashable;
import net.mehvahdjukaar.moonlight.api.block.WaterBlock;
import net.mehvahdjukaar.moonlight.api.util.math.MthUtils;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.common.block.tiles.BlackboardBlockTile;
import net.mehvahdjukaar.supplementaries.common.utils.BlockUtil;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

public class BlackboardBlock extends WaterBlock implements EntityBlock, IWashable {

    protected static final VoxelShape SHAPE_NORTH = Block.box(0.0, 0.0, 11.0, 16.0, 16.0, 16.0);

    protected static final VoxelShape SHAPE_SOUTH = MthUtils.rotateVoxelShape(SHAPE_NORTH, Direction.SOUTH);

    protected static final VoxelShape SHAPE_EAST = MthUtils.rotateVoxelShape(SHAPE_NORTH, Direction.EAST);

    protected static final VoxelShape SHAPE_WEST = MthUtils.rotateVoxelShape(SHAPE_NORTH, Direction.WEST);

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static final BooleanProperty GLOWING = ModBlockProperties.GLOWING;

    public BlackboardBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.NORTH)).m_61124_(WATERLOGGED, false)).m_61124_(GLOWING, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, GLOWING);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.m_6402_(world, pos, state, placer, stack);
        if (world.getBlockEntity(pos) instanceof BlackboardBlockTile tile) {
            BlockUtil.addOptionalOwnership(placer, tile);
        }
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return (BlockState) state.m_61124_(FACING, rot.rotate((Direction) state.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.m_60717_(mirrorIn.getRotation((Direction) state.m_61143_(FACING)));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch((Direction) state.m_61143_(FACING)) {
            case SOUTH ->
                SHAPE_SOUTH;
            case EAST ->
                SHAPE_EAST;
            case WEST ->
                SHAPE_WEST;
            default ->
                SHAPE_NORTH;
        };
    }

    public static byte colorToByte(DyeColor color) {
        return switch(color) {
            case BLACK ->
                0;
            case WHITE ->
                1;
            case ORANGE ->
                15;
            default ->
                (byte) color.getId();
        };
    }

    public static int colorFromByte(byte b) {
        return switch(b) {
            case 0, 1 ->
                16777215;
            case 15 ->
                DyeColor.ORANGE.getMapColor().col;
            default ->
                DyeColor.byId(b).getMapColor().col;
        };
    }

    public static Vector2i getHitSubPixel(BlockHitResult hit) {
        Vec3 pos = hit.m_82450_();
        Vec3 v = pos.yRot(hit.getDirection().toYRot() * (float) (Math.PI / 180.0));
        double fx = v.x % 1.0 * 16.0;
        if (fx < 0.0) {
            fx += 16.0;
        }
        int x = Mth.clamp((int) fx, -15, 15);
        int y = 15 - (int) Mth.clamp(Math.abs(v.y % 1.0 * 16.0), 0.0, 15.0);
        if (pos.y < 0.0) {
            y = 15 - y;
        }
        return new Vector2i(x, y);
    }

    @Nullable
    public static DyeColor getStackChalkColor(ItemStack stack) {
        boolean hasColor = (Boolean) CommonConfigs.Building.BLACKBOARD_COLOR.get();
        for (DyeColor dyeColor : DyeColor.values()) {
            if ((hasColor || dyeColor == DyeColor.WHITE || dyeColor == DyeColor.BLACK) && stack.is((TagKey<Item>) ModTags.BLACKBOARD_TAGS.get(dyeColor))) {
                return dyeColor;
            }
        }
        return null;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof BlackboardBlockTile te && te.isAccessibleBy(player)) {
            ItemStack stack = player.m_21120_(handIn);
            InteractionResult result = te.tryWaxing(level, pos, player, handIn);
            if (result.consumesAction()) {
                level.m_220407_(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, level.getBlockState(pos)));
                te.setChanged();
            }
            if (result != InteractionResult.PASS) {
                return result;
            }
            boolean glowChanged = false;
            if (stack.is(Items.GLOW_INK_SAC) && !(Boolean) state.m_61143_(GLOWING)) {
                level.playSound(null, pos, SoundEvents.GLOW_INK_SAC_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.setBlockAndUpdate(pos, (BlockState) state.m_61124_(GLOWING, true));
                glowChanged = true;
            } else if (stack.is(Items.INK_SAC) && (Boolean) state.m_61143_(GLOWING)) {
                level.playSound(null, pos, SoundEvents.INK_SAC_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.setBlockAndUpdate(pos, (BlockState) state.m_61124_(GLOWING, false));
                glowChanged = true;
            }
            if (glowChanged) {
                level.m_220407_(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, level.getBlockState(pos)));
                if (!player.isCreative()) {
                    stack.shrink(1);
                }
                if (player instanceof ServerPlayer serverPlayer) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
                    player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            BlackboardBlock.UseMode mode = (BlackboardBlock.UseMode) CommonConfigs.Building.BLACKBOARD_MODE.get();
            if (hit.getDirection() == state.m_61143_(FACING) && mode.canManualDraw()) {
                DyeColor color = getStackChalkColor(stack);
                if (color != null) {
                    if (!level.isClientSide) {
                        Vector2i pair = getHitSubPixel(hit);
                        int x = pair.x();
                        int y = pair.y();
                        byte newColor = colorToByte(color);
                        if (te.getPixel(x, y) != newColor) {
                            te.setPixel(x, y, newColor);
                            te.setChanged();
                        }
                    }
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
            if (mode.canOpenGui()) {
                if (player instanceof ServerPlayer serverPlayer) {
                    te.tryOpeningEditGui(serverPlayer, pos);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean flag = context.m_43725_().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        return (BlockState) ((BlockState) this.m_49966_().m_61124_(FACING, context.m_8125_().getOpposite())).m_61124_(WATERLOGGED, flag);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BlackboardBlockTile(pPos, pState);
    }

    public ItemStack getBlackboardItem(BlackboardBlockTile te) {
        ItemStack itemstack = new ItemStack(this);
        if (!te.isEmpty()) {
            CompoundTag tag = te.savePixels(new CompoundTag());
            if (!tag.isEmpty()) {
                itemstack.addTagElement("BlockEntityTag", tag);
            }
        }
        return itemstack;
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return level.getBlockEntity(pos) instanceof BlackboardBlockTile te ? this.getBlackboardItem(te) : super.m_7397_(level, pos, state);
    }

    @Override
    public boolean tryWash(Level level, BlockPos pos, BlockState state) {
        if (level.getBlockEntity(pos) instanceof BlackboardBlockTile te) {
            if ((Boolean) state.m_61143_(GLOWING)) {
                level.setBlockAndUpdate(pos, (BlockState) state.m_61124_(GLOWING, false));
            }
            if (te.isWaxed()) {
                te.setWaxed(false);
                te.setChanged();
                return true;
            }
            if (!te.isEmpty()) {
                te.clear();
                te.setChanged();
                return true;
            }
        }
        return false;
    }

    public static enum UseMode {

        BOTH, GUI, MANUAL;

        public boolean canOpenGui() {
            return this != MANUAL;
        }

        public boolean canManualDraw() {
            return this != GUI;
        }
    }
}