package net.darkhax.attributefix;

import net.darkhax.attributefix.config.AttributeConfig;
import net.darkhax.attributefix.temp.RegistryHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod("attributefix")
@EventBusSubscriber(modid = "attributefix", bus = Bus.MOD)
public class AttributeFixForge {

    @SubscribeEvent
    public static void onLoadComplete(FMLLoadCompleteEvent event) {
        RegistryHelper<Attribute> registry = new AttributeRegistryHelper();
        AttributeConfig.load(FMLPaths.CONFIGDIR.get().resolve("attributefix.json").toFile(), registry).applyChanges(registry);
    }
}