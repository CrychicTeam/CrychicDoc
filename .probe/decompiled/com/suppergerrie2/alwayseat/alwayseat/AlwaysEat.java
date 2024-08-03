package com.suppergerrie2.alwayseat.alwayseat;

import java.util.Objects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.ForgeRegistries;

@Mod("salwayseat")
public class AlwaysEat {

    public static final String MOD_ID = "salwayseat";

    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation("salwayseat", "main"), () -> "1", "1"::equals, "1"::equals);

    public AlwaysEat() {
        DistExecutor.safeRunWhenOn(Dist.DEDICATED_SERVER, () -> ServerEvents::new);
        ModLoadingContext.get().registerConfig(Type.SERVER, Config.SERVER_CONFIG);
        INSTANCE.registerMessage(0, SyncSettings.class, SyncSettings::encode, SyncSettings::decode, SyncSettings::handle);
        MinecraftForge.EVENT_BUS.addListener(this::rightClickItemEvent);
    }

    public void rightClickItemEvent(PlayerInteractEvent.RightClickItem event) {
        ItemStack itemstack = event.getItemStack();
        if (itemstack.isEdible()) {
            Player player = event.getEntity();
            if (player.canEat(canEatItemWhenFull(itemstack, player))) {
                player.m_6672_(event.getHand());
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.CONSUME);
            } else {
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.FAIL);
            }
        }
    }

    public static boolean canEatItemWhenFull(ItemStack item, LivingEntity livingEntity) {
        String registryName = ((ResourceLocation) Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item.getItem()))).toString();
        if (!item.isEdible() || Config.UNEATABLE_ITEMS.get().contains(registryName)) {
            return false;
        } else if (Config.MODE.get() == Config.Mode.BLACKLIST) {
            return !Config.ITEM_LIST.get().contains(registryName) ? true : item.getFoodProperties(livingEntity).canAlwaysEat();
        } else {
            return Config.ITEM_LIST.get().contains(registryName) ? true : item.getFoodProperties(livingEntity).canAlwaysEat();
        }
    }
}