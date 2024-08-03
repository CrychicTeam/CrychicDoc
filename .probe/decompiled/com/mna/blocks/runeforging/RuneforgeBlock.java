package com.mna.blocks.runeforging;

import com.mna.advancements.CustomAdvancementTriggers;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.blocks.tileentities.RuneForgeTile;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.blocks.utility.WaterloggableBlockWithOffset;
import com.mna.items.ItemInit;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RuneforgeBlock extends WaterloggableBlockWithOffset implements EntityBlock {

    public static final int MATERIAL_STONE = 0;

    public static final int MATERIAL_BRIMSTONE = 1;

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public static final BooleanProperty ORE_DOUBLING = BooleanProperty.create("doubling");

    public static final BooleanProperty REPAIR = BooleanProperty.create("repair");

    public static final BooleanProperty SPEED = BooleanProperty.create("speed");

    public static final IntegerProperty MATERIAL = IntegerProperty.create("material", 0, 1);

    private static final VoxelShape SHAPE = Shapes.or(Block.box(2.0, 0.0, 2.0, 14.0, 1.0, 14.0), Block.box(4.0, 1.0, 4.0, 12.0, 2.0, 12.0), Block.box(5.0, 2.0, 5.0, 11.0, 13.5, 11.0), Block.box(1.0, 13.5, 1.0, 15.0, 16.5, 15.0));

    public RuneforgeBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F).noOcclusion().requiresCorrectToolForDrops(), false);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(ACTIVE, false)).m_61124_(REPAIR, false)).m_61124_(MATERIAL, 0));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RuneForgeTile(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return SHAPE;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == TileEntityInit.RUNEFORGE.get() ? (lvl, pos, state1, be) -> RuneForgeTile.Tick(lvl, pos, state1, (RuneForgeTile) be) : null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.m_7926_(builder);
        builder.add(FACING, ACTIVE, REPAIR, SPEED, MATERIAL, ORE_DOUBLING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) this.m_49966_().m_61124_(FACING, context.m_8125_().getOpposite());
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult p_225533_6_) {
        ItemStack activeStack = player.m_21120_(handIn);
        BlockEntity tileEntity = worldIn.getBlockEntity(pos);
        if (tileEntity != null && tileEntity instanceof RuneForgeTile te) {
            if (activeStack.getItem() == ItemInit.BRIMSTONE_CHARM.get()) {
                if ((Integer) state.m_61143_(MATERIAL) == 0) {
                    if (!worldIn.isClientSide) {
                        if (player instanceof ServerPlayer) {
                            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player, pos, activeStack);
                        }
                        worldIn.setBlock(pos, (BlockState) state.m_61124_(MATERIAL, 1), 3);
                        worldIn.playSound(null, pos, SFX.Spell.Buff.FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
                        worldIn.playSound(null, pos, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 1.0F, 1.0F);
                        if (!player.isCreative()) {
                            activeStack.shrink(1);
                        }
                    } else {
                        for (int i = 0; i < 150; i++) {
                            worldIn.addParticle(new MAParticleType(ParticleInit.FLAME.get()), (double) pos.m_123341_() + Math.random(), (double) pos.m_123342_() + Math.random() * 2.0, (double) pos.m_123343_() + Math.random(), 0.0, Math.random() * 0.01 + 0.01, 0.0);
                        }
                    }
                }
            } else if (!te.m_7983_()) {
                if (!worldIn.isClientSide) {
                    ItemStack stack = te.removeItemNoUpdate(0);
                    if (player instanceof ServerPlayer) {
                        CustomAdvancementTriggers.ARCANE_FURNACE_SMELT.trigger((ServerPlayer) player, stack);
                    }
                    if (!player.addItem(stack)) {
                        player.drop(stack, true);
                    }
                    worldIn.sendBlockUpdated(pos, state, state, 2);
                }
            } else if (!worldIn.isClientSide && !activeStack.isEmpty() && te.m_7983_()) {
                ItemStack insert = activeStack.copy();
                int quantity = Math.min(16, insert.getCount());
                insert.setCount(quantity);
                te.setItem(0, insert);
                worldIn.sendBlockUpdated(pos, state, state, 2);
                if (!player.isCreative()) {
                    activeStack.shrink(quantity);
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        return state.m_61143_(ACTIVE) ? 15 : 0;
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.m_60734_() != newState.m_60734_()) {
            this.dropInventory(worldIn, pos, state);
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    private void dropInventory(Level world, BlockPos pos, BlockState state) {
        if (!world.isClientSide) {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity != null && tileEntity instanceof RuneForgeTile) {
                Containers.dropContents(world, pos, (Container) tileEntity);
            }
            if (state.m_60734_() == this && (Integer) state.m_61143_(MATERIAL) == 1) {
                Containers.dropContents(world, pos, NonNullList.of(ItemStack.EMPTY, new ItemStack(ItemInit.BRIMSTONE_CHARM.get())));
            }
        }
    }
}