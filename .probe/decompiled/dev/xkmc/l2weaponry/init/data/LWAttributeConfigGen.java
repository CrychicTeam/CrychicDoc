package dev.xkmc.l2weaponry.init.data;

import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import dev.xkmc.l2tabs.init.L2Tabs;
import dev.xkmc.l2tabs.init.data.AttributeDisplayConfig;
import dev.xkmc.l2weaponry.init.registrate.LWItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class LWAttributeConfigGen extends ConfigDataProvider {

    public LWAttributeConfigGen(DataGenerator generator) {
        super(generator, "L2Weaponry Attribute Config Gen");
    }

    @Override
    public void add(ConfigDataProvider.Collector collector) {
        collector.add(L2Tabs.ATTRIBUTE_ENTRY, new ResourceLocation("l2weaponry", "l2weaponry"), new AttributeDisplayConfig().add((Attribute) LWItems.REFLECT_TIME.get(), false, 14000, 0.0).add((Attribute) LWItems.SHIELD_DEFENSE.get(), false, 15000, 0.0));
    }
}