package io.github.lightman314.lightmanscurrency.common.blocks.traderblocks.reference;

import com.google.common.collect.Lists;
import io.github.lightman314.lightmanscurrency.api.misc.blocks.IEasyEntityBlock;
import io.github.lightman314.lightmanscurrency.api.misc.blocks.LazyShapes;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.common.blockentity.AuctionStandBlockEntity;
import io.github.lightman314.lightmanscurrency.common.blocks.EasyBlock;
import io.github.lightman314.lightmanscurrency.common.core.ModBlockEntities;
import io.github.lightman314.lightmanscurrency.common.menus.validation.types.BlockValidator;
import io.github.lightman314.lightmanscurrency.common.traders.TraderSaveData;
import io.github.lightman314.lightmanscurrency.common.traders.auction.AuctionHouseTrader;
import io.github.lightman314.lightmanscurrency.util.InventoryUtil;
import java.util.Collection;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class AuctionStandBlock extends EasyBlock implements IEasyEntityBlock {

    public AuctionStandBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected boolean isBlockOpaque() {
        return false;
    }

    @Nonnull
    @Override
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return LazyShapes.BOX;
    }

    @Nonnull
    @Override
    public InteractionResult use(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult result) {
        if (!level.isClientSide && AuctionHouseTrader.isEnabled()) {
            TraderData ah = TraderSaveData.GetAuctionHouse(false);
            if (ah != null) {
                ah.openTraderMenu(player, BlockValidator.of(pos, this));
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Nonnull
    @Override
    public Collection<BlockEntityType<?>> getAllowedTypes() {
        return Lists.newArrayList(new BlockEntityType[] { ModBlockEntities.AUCTION_STAND.get() });
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new AuctionStandBlockEntity(pos, state);
    }

    @Override
    public void playerWillDestroy(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull Player player) {
        super.m_5707_(level, pos, state, player);
        if (player.isCreative() && level.getBlockEntity(pos) instanceof AuctionStandBlockEntity be) {
            be.dropItem = false;
        }
    }

    @Override
    public void onRemove(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, BlockState newState, boolean flag) {
        if (state.m_60713_(newState.m_60734_())) {
            super.m_6810_(state, level, pos, newState, flag);
        } else {
            if (level.getBlockEntity(pos) instanceof AuctionStandBlockEntity be && be.dropItem) {
                InventoryUtil.dumpContents(level, pos, new ItemStack(this));
            }
        }
    }
}