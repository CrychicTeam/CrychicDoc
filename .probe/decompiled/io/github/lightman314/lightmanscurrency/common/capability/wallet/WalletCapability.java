package io.github.lightman314.lightmanscurrency.common.capability.wallet;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.capability.money.CapabilityMoneyViewer;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyView;
import io.github.lightman314.lightmanscurrency.api.money.value.holder.IMoneyViewer;
import io.github.lightman314.lightmanscurrency.common.capability.CurrencyCapabilities;
import io.github.lightman314.lightmanscurrency.common.menus.containers.SuppliedItemContainer;
import io.github.lightman314.lightmanscurrency.integration.curios.wallet.CuriosWalletHandler;
import io.github.lightman314.lightmanscurrency.network.message.walletslot.CPacketCreativeWalletEdit;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class WalletCapability {

    @Deprecated
    public static LazyOptional<IWalletHandler> getWalletHandler(@Nonnull Entity entity) {
        return entity.getCapability(CurrencyCapabilities.WALLET);
    }

    @Nullable
    public static IWalletHandler lazyGetWalletHandler(@Nonnull Entity entity) {
        LazyOptional<IWalletHandler> optional = entity.getCapability(CurrencyCapabilities.WALLET);
        return optional.isPresent() ? optional.orElseThrow(() -> new RuntimeException("Unexpected error occurred!")) : null;
    }

    @Nonnull
    public static Container getWalletContainer(@Nonnull Entity entity) {
        return new SuppliedItemContainer(() -> {
            IWalletHandler handler = lazyGetWalletHandler(entity);
            return handler != null ? new WalletCapability.WalletInteractable(handler) : null;
        });
    }

    @Nullable
    public static IWalletHandler getRenderWalletHandler(@Nonnull Entity entity) {
        return (IWalletHandler) (LightmansCurrency.isCuriosLoaded() && entity instanceof LivingEntity le ? new CuriosWalletHandler(le) : lazyGetWalletHandler(entity));
    }

    @Nonnull
    public static MoneyView getWalletMoney(@Nonnull Entity entity) {
        IWalletHandler walletHandler = lazyGetWalletHandler(entity);
        if (walletHandler != null) {
            ItemStack wallet = walletHandler.getWallet();
            IMoneyViewer moneyViewer = CapabilityMoneyViewer.getCapability(wallet);
            return moneyViewer == null ? MoneyView.empty() : moneyViewer.getStoredMoney();
        } else {
            return MoneyView.builder().build();
        }
    }

    public static ICapabilityProvider createProvider(Player playerEntity) {
        return new WalletCapability.Provider(playerEntity);
    }

    private static class Provider implements ICapabilitySerializable<Tag> {

        final LazyOptional<IWalletHandler> optional;

        final IWalletHandler handler;

        Provider(Player playerEntity) {
            this.handler = new WalletHandler(playerEntity);
            this.optional = LazyOptional.of(() -> this.handler);
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nullable Capability<T> capability, Direction facing) {
            return CurrencyCapabilities.WALLET.orEmpty(capability, this.optional);
        }

        @Override
        public Tag serializeNBT() {
            return this.handler.save();
        }

        @Override
        public void deserializeNBT(Tag tag) {
            if (tag instanceof CompoundTag compound) {
                this.handler.load(compound);
            }
        }
    }

    private static class WalletInteractable implements SuppliedItemContainer.IItemInteractable {

        private final IWalletHandler walletHandler;

        WalletInteractable(@Nonnull IWalletHandler handler) {
            this.walletHandler = handler;
        }

        @Nonnull
        @Override
        public ItemStack getItem() {
            return this.walletHandler.getWallet();
        }

        @Override
        public void setItem(@Nonnull ItemStack item) {
            this.walletHandler.setWallet(item);
            if (this.walletHandler.entity().m_9236_().isClientSide && this.walletHandler.entity() instanceof Player player && player.isCreative()) {
                new CPacketCreativeWalletEdit(this.getItem().copy()).send();
            }
        }
    }
}