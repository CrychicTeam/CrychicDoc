package dev.xkmc.l2weaponry.init;

import dev.xkmc.l2weaponry.content.client.ClawItemDecorationRenderer;
import dev.xkmc.l2weaponry.content.client.ShieldItemDecorationRenderer;
import dev.xkmc.l2weaponry.content.item.types.NunchakuItem;
import dev.xkmc.l2weaponry.init.registrate.LWItems;
import java.util.function.UnaryOperator;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterItemDecorationsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

@EventBusSubscriber(value = { Dist.CLIENT }, modid = "l2weaponry", bus = Bus.MOD)
public class L2WeaponryClient {

    @SubscribeEvent
    public static void onParticleRegistryEvent(RegisterParticleProvidersEvent event) {
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ClampedItemPropertyFunction func = (stack, level, entity, layer) -> entity != null && entity.isBlocking() && entity.getUseItem() == stack ? 1.0F : 0.0F;
            for (Item i : LWItems.BLOCK_DECO) {
                ItemProperties.register(i, new ResourceLocation("l2weaponry", "blocking"), func);
            }
            func = (stack, level, entity, layer) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
            for (Item i : LWItems.THROW_DECO) {
                ItemProperties.register(i, new ResourceLocation("l2weaponry", "throwing"), func);
            }
            func = (stack, level, entity, layer) -> NunchakuItem.check(entity, stack) ? 1.0F : 0.0F;
            for (Item i : LWItems.NUNCHAKU_DECO) {
                ItemProperties.register(i, new ResourceLocation("l2weaponry", "spinning"), func);
            }
        });
    }

    @SubscribeEvent
    public static void registerItemDecoration(RegisterItemDecorationsEvent event) {
        ShieldItemDecorationRenderer shield = new ShieldItemDecorationRenderer();
        ClawItemDecorationRenderer claw = new ClawItemDecorationRenderer();
        for (Item i : LWItems.BLOCK_DECO) {
            event.register(i, shield);
        }
        for (Item i : LWItems.CLAW_DECO) {
            event.register(i, claw);
        }
    }

    @SubscribeEvent
    public static void onResourceReload(RegisterClientReloadListenersEvent event) {
    }

    @SubscribeEvent
    public static void onModelLoad(ModelEvent.RegisterAdditional event) {
        for (Item item : LWItems.NUNCHAKU_DECO) {
            event.register(ForgeRegistries.ITEMS.getKey(item).withPath((UnaryOperator<String>) (e -> "item/" + e + "_roll")));
            event.register(ForgeRegistries.ITEMS.getKey(item).withPath((UnaryOperator<String>) (e -> "item/" + e + "_unroll")));
        }
    }
}