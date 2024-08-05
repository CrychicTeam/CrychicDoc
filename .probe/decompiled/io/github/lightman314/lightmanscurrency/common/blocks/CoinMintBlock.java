package io.github.lightman314.lightmanscurrency.common.blocks;

import com.google.common.collect.ImmutableList;
import io.github.lightman314.lightmanscurrency.LCConfig;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.blocks.IEasyEntityBlock;
import io.github.lightman314.lightmanscurrency.api.misc.blocks.RotatableBlock;
import io.github.lightman314.lightmanscurrency.common.blockentity.CoinMintBlockEntity;
import io.github.lightman314.lightmanscurrency.common.core.ModBlockEntities;
import io.github.lightman314.lightmanscurrency.common.items.TooltipItem;
import io.github.lightman314.lightmanscurrency.common.menus.MintMenu;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public class CoinMintBlock extends RotatableBlock implements IEasyEntityBlock {

    public CoinMintBlock(BlockBehaviour.Properties properties) {
        super(properties, m_49796_(1.0, 0.0, 1.0, 15.0, 16.0, 15.0));
    }

    @Nonnull
    @Override
    public Collection<BlockEntityType<?>> getAllowedTypes() {
        return ImmutableList.of(ModBlockEntities.COIN_MINT.get());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new CoinMintBlockEntity(pos, state);
    }

    @Nonnull
    @Override
    public InteractionResult use(@Nonnull BlockState state, Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult result) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof CoinMintBlockEntity mint) {
            NetworkHooks.openScreen((ServerPlayer) player, new CoinMintBlock.CoinMintMenuProvider(mint), pos);
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public void onRemove(@Nonnull BlockState state, Level level, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        if (level.getBlockEntity(pos) instanceof CoinMintBlockEntity mintEntity) {
            mintEntity.dumpContents(level, pos);
        }
        super.m_6810_(state, level, pos, newState, isMoving);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable BlockGetter level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flagIn) {
        TooltipItem.addTooltip(tooltip, () -> {
            List<Component> t = new ArrayList();
            if (LCConfig.SERVER.coinMintCanMint.get()) {
                t.add(LCText.TOOLTIP_COIN_MINT_MINTABLE.get());
            }
            if (LCConfig.SERVER.coinMintCanMelt.get()) {
                t.add(LCText.TOOLTIP_COIN_MINT_MELTABLE.get());
            }
            return t;
        });
        super.m_5871_(stack, level, tooltip, flagIn);
    }

    private static record CoinMintMenuProvider(CoinMintBlockEntity blockEntity) implements MenuProvider {

        @Override
        public AbstractContainerMenu createMenu(int id, @Nonnull Inventory inventory, @Nonnull Player player) {
            return new MintMenu(id, inventory, this.blockEntity);
        }

        @Nonnull
        @Override
        public Component getDisplayName() {
            return LCText.GUI_COIN_MINT_TITLE.get();
        }
    }
}