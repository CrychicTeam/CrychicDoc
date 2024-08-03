package org.violetmoon.quark.integration.lootr;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import noobanidus.mods.lootr.LootrTags;
import noobanidus.mods.lootr.block.entities.LootrChestBlockEntity;
import noobanidus.mods.lootr.config.ConfigManager;
import noobanidus.mods.lootr.util.ChestUtil;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.content.building.block.VariantChestBlock;
import org.violetmoon.zeta.item.ZetaBlockItem;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.IZetaBlockItemProvider;

public class LootrVariantChestBlock extends VariantChestBlock implements IZetaBlockItemProvider {

    public LootrVariantChestBlock(String type, ZetaModule module, Supplier<BlockEntityType<? extends ChestBlockEntity>> supplier, BlockBehaviour.Properties properties) {
        super("lootr", type, module, supplier, properties.strength(2.5F));
    }

    @Override
    public float getExplosionResistance() {
        if (ConfigManager.BLAST_IMMUNE.get()) {
            return Float.MAX_VALUE;
        } else {
            return ConfigManager.BLAST_RESISTANT.get() ? 16.0F : super.m_7325_();
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult trace) {
        if (player.m_6144_()) {
            ChestUtil.handleLootSneak(this, world, pos, player);
        } else if (!ChestBlock.isChestBlockedAt(world, pos)) {
            ChestUtil.handleLootChest(this, world, pos, player);
        }
        return InteractionResult.sidedSuccess(world.isClientSide);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LootrVariantChestBlockEntity(pos, state);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if ((Boolean) stateIn.m_61143_(f_51480_)) {
            worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.m_6718_(worldIn));
        }
        return stateIn;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return f_51485_;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.m_8125_().getOpposite();
        FluidState fluidstate = context.m_43725_().getFluidState(context.getClickedPos());
        return (BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(f_51478_, direction)).m_61124_(f_51479_, ChestType.SINGLE)).m_61124_(f_51480_, fluidstate.getType() == Fluids.WATER);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(f_51480_) ? Fluids.WATER.getSource(false) : super.m_5888_(state);
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState state, Level worldIn, BlockPos pos) {
        return null;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState pState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState pBlockState, Level pLevel, BlockPos pPos) {
        return ConfigManager.POWER_COMPARATORS.get() ? 1 : 0;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? LootrChestBlockEntity::lootrLidAnimateTick : null;
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        BlockEntity blockentity = pLevel.m_7702_(pPos);
        if (blockentity instanceof LootrChestBlockEntity) {
            ((LootrChestBlockEntity) blockentity).recheckOpen();
        }
    }

    @Override
    public BlockItem provideItemBlock(Block block, net.minecraft.world.item.Item.Properties props) {
        return new LootrVariantChestBlock.Item(block, props, false);
    }

    public static class Item extends ZetaBlockItem {

        private final boolean trap;

        public Item(Block block, net.minecraft.world.item.Item.Properties props, boolean trap) {
            super(block, props);
            this.trap = trap;
        }

        @Override
        public InteractionResult onItemUseFirstZeta(ItemStack stack, UseOnContext context) {
            if (!context.isSecondaryUseActive()) {
                Player player = context.getPlayer();
                Level level = context.getLevel();
                BlockPos pos = context.getClickedPos();
                Block block = this.m_40614_();
                if (player != null && player.isCreative()) {
                    BlockState state = level.getBlockState(pos);
                    TagKey<Block> key = this.trap ? LootrTags.Blocks.TRAPPED_CHESTS : LootrTags.Blocks.CHESTS;
                    if (state.m_204336_(key) && !state.m_60713_(block)) {
                        BlockEntity entity = level.getBlockEntity(pos);
                        CompoundTag nbt = entity == null ? null : entity.serializeNBT();
                        level.setBlock(pos, block.withPropertiesOf(state), 18);
                        level.m_46796_(2001, pos, Block.getId(state));
                        BlockEntity newEntity = level.getBlockEntity(pos);
                        if (newEntity != null && nbt != null) {
                            newEntity.load(nbt);
                        }
                        return InteractionResult.sidedSuccess(level.isClientSide);
                    }
                }
            }
            return super.onItemUseFirstZeta(stack, context);
        }
    }
}