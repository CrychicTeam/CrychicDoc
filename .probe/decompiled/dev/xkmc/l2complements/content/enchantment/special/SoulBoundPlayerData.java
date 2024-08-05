package dev.xkmc.l2complements.content.enchantment.special;

import dev.xkmc.l2library.capability.player.PlayerCapabilityHolder;
import dev.xkmc.l2library.capability.player.PlayerCapabilityNetworkHandler;
import dev.xkmc.l2library.capability.player.PlayerCapabilityTemplate;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

@SerialClass
public class SoulBoundPlayerData extends PlayerCapabilityTemplate<SoulBoundPlayerData> {

    public static final Capability<SoulBoundPlayerData> CAPABILITY = CapabilityManager.get(new CapabilityToken<SoulBoundPlayerData>() {
    });

    public static final PlayerCapabilityHolder<SoulBoundPlayerData> HOLDER = new PlayerCapabilityHolder<>(new ResourceLocation("l2complements", "soulbound"), CAPABILITY, SoulBoundPlayerData.class, SoulBoundPlayerData::new, PlayerCapabilityNetworkHandler::new);

    @SerialField
    public final List<ItemStack> list = new ArrayList();

    public static void register() {
    }

    public static boolean addToPlayer(ServerPlayer player, ItemStack item) {
        HOLDER.get(player).list.add(item.copy());
        return true;
    }

    @Override
    public void onClone(boolean isWasDeath) {
        if (isWasDeath) {
            for (ItemStack stack : this.list) {
                this.player.getInventory().placeItemBackInInventory(stack);
            }
            this.list.clear();
        }
    }
}