package com.mna.blocks.ritual;

import com.mna.api.blocks.interfaces.IDontCreateBlockItem;
import com.mna.api.blocks.interfaces.ITranslucentBlock;
import com.mna.api.rituals.IRitualReagent;
import com.mna.api.tools.MATags;
import com.mna.blocks.WaterloggableBlock;
import com.mna.blocks.tileentities.ChalkRuneTile;
import com.mna.entities.EntityInit;
import com.mna.entities.rituals.Ritual;
import com.mna.items.filters.ItemFilterGroup;
import com.mna.tools.DidYouKnowHelper;
import com.mna.tools.InventoryUtilities;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ChalkRuneBlock extends WaterloggableBlock implements ITranslucentBlock, IDontCreateBlockItem, EntityBlock {

    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 0.05, 16.0);

    public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");

    public static final BooleanProperty METAL = BooleanProperty.create("metal");

    public static final IntegerProperty RUNEINDEX = IntegerProperty.create("rune", 0, 15);

    private int heldItemActivateCount = 0;

    private long lastActivateTime = 0L;

    public ChalkRuneBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.WOOL).strength(0.1F).noCollission().noOcclusion().sound(SoundType.WOOL), false);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(ACTIVATED, false)).m_61124_(METAL, false)).m_61124_(RUNEINDEX, (int) Math.random() * 15));
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ACTIVATED, RUNEINDEX, METAL);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ChalkRuneTile(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult p_225533_6_) {
        ItemStack activeStack = player.m_21120_(handIn);
        if (ItemFilterGroup.GROUND_RUNE_NON_PLACEABLE.anyMatchesFilter(activeStack)) {
            return InteractionResult.PASS;
        } else {
            if ((Boolean) state.m_61143_(ACTIVATED)) {
                List<Ritual> rituals = worldIn.getEntities((EntityTypeTest<Entity, Ritual>) EntityInit.RITUAL_ENTITY.get(), new AABB(pos).inflate(16.0), e -> true);
                if (rituals.size() > 0 && rituals.stream().anyMatch(r -> r.getState() != Ritual.RitualState.GUIDING_REAGENT_PLACEMENT)) {
                    return InteractionResult.PASS;
                }
            }
            if (worldIn.isClientSide) {
                if (!activeStack.isEmpty() && (Boolean) state.m_61143_(ACTIVATED)) {
                    if (worldIn.getGameTime() - this.lastActivateTime < 200L) {
                        this.heldItemActivateCount++;
                        if (this.heldItemActivateCount >= 4) {
                            DidYouKnowHelper.CheckAndShowDidYouKnow(player, "helptip.mna.ritual_empty_hand");
                        }
                    } else {
                        this.heldItemActivateCount = 0;
                    }
                    this.lastActivateTime = worldIn.getGameTime();
                }
                return InteractionResult.SUCCESS;
            } else {
                BlockEntity tileEntity = worldIn.getBlockEntity(pos);
                if (tileEntity != null && tileEntity instanceof ChalkRuneTile tecr) {
                    if (!tecr.m_7983_()) {
                        ItemStack stack = tecr.removeItemNoUpdate(0);
                        if (!player.addItem(stack)) {
                            player.drop(stack, true);
                        }
                        worldIn.sendBlockUpdated(pos, state, state, 2);
                        worldIn.updateNeighbourForOutputSignal(pos, state.m_60734_());
                    } else {
                        if (activeStack.isEmpty()) {
                            IRitualReagent reagent = this.getReagentFromNearbyRitual(worldIn, pos);
                            if (reagent != null) {
                                if (!player.isCreative()) {
                                    activeStack = InventoryUtilities.removeSingleItemFromInventory(reagent.getResourceLocation(), player.getInventory());
                                } else {
                                    List<Item> possibilities = MATags.smartLookupItem(reagent.getResourceLocation());
                                    if (possibilities != null && possibilities.size() > 0) {
                                        activeStack = new ItemStack((ItemLike) possibilities.get((int) (Math.random() * (double) possibilities.size())));
                                    }
                                }
                            }
                        }
                        if (!activeStack.isEmpty() && tecr.m_7983_()) {
                            ItemStack single = activeStack.copy();
                            single.setCount(1);
                            tecr.setItem(0, single);
                            worldIn.sendBlockUpdated(pos, state, state, 2);
                            worldIn.updateNeighbourForOutputSignal(pos, state.m_60734_());
                            if (!player.isCreative()) {
                                activeStack.shrink(1);
                            }
                        }
                    }
                }
                return InteractionResult.SUCCESS;
            }
        }
    }

    @Nullable
    private IRitualReagent getReagentFromNearbyRitual(Level world, BlockPos pos) {
        AABB bb = new AABB(pos).inflate(5.0);
        List<Ritual> rituals = world.m_45976_(Ritual.class, bb);
        return rituals != null && rituals.size() == 1 ? ((Ritual) rituals.get(0)).getReagentForPosition(pos) : null;
    }

    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.IGNORE;
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.m_60734_() != newState.m_60734_()) {
            this.notifyNearbyRitualsOfRuneDestruction(state, worldIn, pos);
            this.dropInventory(worldIn, pos);
        }
        super.m_6810_(state, worldIn, pos, newState, isMoving);
    }

    private void dropInventory(Level world, BlockPos pos) {
        if (!world.isClientSide) {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity != null && tileEntity instanceof ChalkRuneTile && !((ChalkRuneTile) tileEntity).isGhostItem()) {
                Containers.dropContents(world, pos, (Container) tileEntity);
            }
        }
    }

    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        return state.m_61143_(ACTIVATED) ? 6 : 0;
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isClientSide && fromPos.equals(pos.below()) && !worldIn.getBlockState(fromPos).m_60804_(worldIn, fromPos)) {
            worldIn.m_46961_(pos, true);
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
        BlockEntity te = worldIn.getBlockEntity(pos);
        return te != null && te instanceof ChalkRuneTile && !((ChalkRuneTile) te).getDisplayedItem().isEmpty() ? 15 : 0;
    }

    public void notifyNearbyRitualsOfRuneDestruction(BlockState blockState, Level world, BlockPos pos) {
        if (!world.isClientSide) {
            for (Ritual ritual : world.getEntities((EntityTypeTest<Entity, Ritual>) EntityInit.RITUAL_ENTITY.get(), new AABB(pos).inflate(16.0), e -> true)) {
                ritual.checkForExplosion(false);
            }
        }
    }

    @Override
    public boolean canPlaceLiquid(BlockGetter worldIn, BlockPos pos, BlockState state, Fluid fluidIn) {
        return state.m_61138_(METAL) && state.m_61143_(METAL) ? super.canPlaceLiquid(worldIn, pos, state, fluidIn) : true;
    }

    @Override
    public boolean placeLiquid(LevelAccessor worldIn, BlockPos pos, BlockState state, FluidState fluidStateIn) {
        if (state.m_61138_(METAL) && !(Boolean) state.m_61143_(METAL)) {
            worldIn.m_7731_(pos, fluidStateIn.createLegacyBlock(), 3);
            return false;
        } else {
            return super.placeLiquid(worldIn, pos, state, fluidStateIn);
        }
    }
}