package io.github.apace100.origins;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.apace100.apoli.ApoliClient;
import io.github.apace100.origins.registry.ModEntities;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@OnlyIn(Dist.CLIENT)
public class OriginsClient {

    public static KeyMapping usePrimaryActivePowerKeybind;

    public static KeyMapping useSecondaryActivePowerKeybind;

    public static KeyMapping viewCurrentOriginKeybind;

    public static void initialize() {
        usePrimaryActivePowerKeybind = new KeyMapping("key.origins.primary_active", InputConstants.Type.KEYSYM, 71, "category.origins");
        useSecondaryActivePowerKeybind = new KeyMapping("key.origins.secondary_active", InputConstants.Type.KEYSYM, -1, "category.origins");
        viewCurrentOriginKeybind = new KeyMapping("key.origins.view_origin", InputConstants.Type.KEYSYM, 79, "category.origins");
        ApoliClient.registerPowerKeybinding("key.origins.primary_active", usePrimaryActivePowerKeybind);
        ApoliClient.registerPowerKeybinding("key.origins.secondary_active", useSecondaryActivePowerKeybind);
        ApoliClient.registerPowerKeybinding("primary", usePrimaryActivePowerKeybind);
        ApoliClient.registerPowerKeybinding("secondary", useSecondaryActivePowerKeybind);
        ApoliClient.registerPowerKeybinding("none", usePrimaryActivePowerKeybind);
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(OriginsClient::clientSetup);
        bus.addListener(OriginsClient::entityRenderers);
        bus.addListener(OriginsClient::registerKeyBindings);
    }

    public static void clientSetup(FMLClientSetupEvent event) {
    }

    private static void registerKeyBindings(RegisterKeyMappingsEvent event) {
        event.register(usePrimaryActivePowerKeybind);
        event.register(useSecondaryActivePowerKeybind);
        event.register(viewCurrentOriginKeybind);
    }

    public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.ENDERIAN_PEARL.get(), ThrownItemRenderer::new);
    }
}