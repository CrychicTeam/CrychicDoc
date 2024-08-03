package org.violetmoon.quark.integration.lootr;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
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
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import noobanidus.mods.lootr.block.entities.LootrChestBlockEntity;
import noobanidus.mods.lootr.config.ConfigManager;
import noobanidus.mods.lootr.util.ChestUtil;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.content.building.block.VariantTrappedChestBlock;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.IZetaBlockItemProvider;

public class LootrVariantTrappedChestBlock extends VariantTrappedChestBlock implements IZetaBlockItemProvider {

    public LootrVariantTrappedChestBlock(String type, ZetaModule module, Supplier<BlockEntityType<? extends ChestBlockEntity>> supplier, BlockBehaviour.Properties properties) {
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
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState state) {
        return new LootrVariantTrappedChestBlockEntity(pPos, state);
    }

    @Override
    public boolean isSignalSource(BlockState pState) {
        return true;
    }

    @Override
    public int getSignal(BlockState pBlockState, BlockGetter pBlockAccess, BlockPos pPos, Direction pSide) {
        return Mth.clamp(LootrChestBlockEntity.m_59086_(pBlockAccess, pPos), 0, 15);
    }

    @Override
    public int getDirectSignal(BlockState pBlockState, BlockGetter pBlockAccess, BlockPos pPos, Direction pSide) {
        return pSide == Direction.UP ? pBlockState.m_60746_(pBlockAccess, pPos, pSide) : 0;
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

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState state, Level worldIn, BlockPos pos) {
        return null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? LootrChestBlockEntity::lootrLidAnimateTick : null;
    }

    @Override
    public BlockItem provideItemBlock(Block block, Item.Properties props) {
        return new LootrVariantChestBlock.Item(block, props, true);
    }
}