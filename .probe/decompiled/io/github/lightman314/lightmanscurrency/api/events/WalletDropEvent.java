package io.github.lightman314.lightmanscurrency.api.events;

import io.github.lightman314.lightmanscurrency.common.capability.wallet.IWalletHandler;
import io.github.lightman314.lightmanscurrency.common.items.WalletItem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.world.Container;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class WalletDropEvent extends PlayerEvent {

    private Container walletInventory;

    private ItemStack walletStack;

    public final DamageSource source;

    private List<ItemStack> walletDrops = new ArrayList();

    public final boolean keepWallet;

    public final int coinDropPercent;

    @Nonnull
    public Container getWalletInventory() {
        return this.walletInventory;
    }

    @Nonnull
    public ItemStack getWalletStack() {
        ItemStack result = this.walletStack.copy();
        WalletItem.putWalletInventory(result, this.walletInventory);
        return result;
    }

    public void setWalletStack(@Nonnull ItemStack wallet) {
        this.walletStack = wallet.copy();
        this.walletInventory = WalletItem.getWalletInventory(this.walletStack);
    }

    @Nonnull
    public List<ItemStack> getDrops() {
        return this.walletDrops;
    }

    public void setDrops(@Nonnull List<ItemStack> drops) {
        this.walletDrops = new ArrayList(drops);
    }

    public void addDrop(@Nonnull ItemStack drop) {
        this.walletDrops.add(drop);
    }

    public void addDrops(@Nonnull Collection<ItemStack> drop) {
        this.walletDrops.addAll(drop);
    }

    public WalletDropEvent(@Nonnull Player player, @Nonnull IWalletHandler walletHandler, @Nonnull Container walletInventory, @Nonnull DamageSource source, boolean keepWallet, int coinDropPercent) {
        super(player);
        this.walletStack = walletHandler.getWallet().copy();
        this.walletInventory = walletInventory;
        this.source = source;
        this.keepWallet = keepWallet;
        this.coinDropPercent = coinDropPercent;
    }
}