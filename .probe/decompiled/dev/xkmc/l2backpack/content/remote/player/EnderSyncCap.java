package dev.xkmc.l2backpack.content.remote.player;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2backpack.content.common.BaseBagItem;
import dev.xkmc.l2backpack.content.quickswap.common.IQuickSwapItem;
import dev.xkmc.l2backpack.content.quickswap.common.IQuickSwapToken;
import dev.xkmc.l2backpack.content.quickswap.entry.ISwapEntry;
import dev.xkmc.l2backpack.content.quickswap.type.QuickSwapType;
import dev.xkmc.l2backpack.init.L2Backpack;
import dev.xkmc.l2library.capability.player.PlayerCapabilityHolder;
import dev.xkmc.l2library.capability.player.PlayerCapabilityNetworkHandler;
import dev.xkmc.l2library.capability.player.PlayerCapabilityTemplate;
import dev.xkmc.l2serial.serialization.SerialClass;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class EnderSyncCap extends PlayerCapabilityTemplate<EnderSyncCap> {

    public static final Capability<EnderSyncCap> CAPABILITY = CapabilityManager.get(new CapabilityToken<EnderSyncCap>() {
    });

    public static final PlayerCapabilityHolder<EnderSyncCap> HOLDER = new PlayerCapabilityHolder<>(new ResourceLocation("l2backpack", "player_ender"), CAPABILITY, EnderSyncCap.class, EnderSyncCap::new, PlayerCapabilityNetworkHandler::new);

    public NonNullList<ItemStack> clientEnderInv = NonNullList.withSize(27, ItemStack.EMPTY);

    @Override
    public void tick() {
        if (!this.world.isClientSide()) {
            List<Pair<Integer, ItemStack>> changes = new ArrayList();
            for (int i = 0; i < 27; i++) {
                ItemStack stack = this.player.getEnderChestInventory().m_8020_(i);
                if (!ItemStack.isSameItemSameTags(stack, this.clientEnderInv.get(i))) {
                    this.clientEnderInv.set(i, stack.copy());
                    changes.add(Pair.of(i, stack));
                }
            }
            if (changes.size() > 0) {
                L2Backpack.HANDLER.toClientPlayer(new EnderSyncPacket(changes), (ServerPlayer) this.player);
            }
        }
    }

    public static void register() {
    }

    public List<ItemStack> getItems() {
        if (this.world.isClientSide()) {
            return this.clientEnderInv;
        } else {
            List<ItemStack> ans = new ArrayList();
            for (int i = 0; i < 27; i++) {
                ans.add(this.player.getEnderChestInventory().m_8020_(i));
            }
            return ans;
        }
    }

    public void setItem(int slot, ItemStack stack) {
        this.clientEnderInv.set(slot, stack);
    }

    @Nullable
    public IQuickSwapToken getToken(QuickSwapType type) {
        for (ItemStack stack : this.getItems()) {
            Item token = stack.getItem();
            if (token instanceof IQuickSwapItem) {
                IQuickSwapItem item = (IQuickSwapItem) token;
                if (item instanceof BaseBagItem) {
                    IQuickSwapToken<? extends ISwapEntry<?>> tokenx = (IQuickSwapToken<? extends ISwapEntry<?>>) item.getTokenOfType(stack, this.player, type);
                    if (tokenx != null) {
                        return tokenx;
                    }
                }
            }
        }
        return null;
    }
}