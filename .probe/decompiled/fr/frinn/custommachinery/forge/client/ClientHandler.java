package fr.frinn.custommachinery.forge.client;

import dev.architectury.platform.Platform;
import fr.frinn.custommachinery.common.integration.config.CMConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(value = { Dist.CLIENT }, modid = "custommachinery", bus = Bus.MOD)
public class ClientHandler {

    @SubscribeEvent
    public static void registerModelLoader(ModelEvent.RegisterGeometryLoaders event) {
        event.register("custom_machine", CustomMachineModelLoader.INSTANCE);
    }

    @SubscribeEvent
    public static void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
        event.register(new ResourceLocation("custommachinery", "block/nope"));
        event.register(new ResourceLocation("custommachinery", "default/custom_machine_default"));
        for (String folder : CMConfig.get().modelFolders) {
            Minecraft.getInstance().getResourceManager().listResources("models/" + folder, s -> s.getPath().endsWith(".json")).forEach((rl, resource) -> {
                ResourceLocation modelRL = new ResourceLocation(rl.getNamespace(), rl.getPath().substring(7).replace(".json", ""));
                event.register(modelRL);
            });
        }
    }

    public static void setupConfig() {
        if (Platform.isModLoaded("cloth_config")) {
            ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> (Screen) AutoConfig.getConfigScreen(CMConfig.class, parent).get()));
        }
    }
}