package net.darkhax.attributefix;

import java.util.Collection;
import net.darkhax.attributefix.temp.RegistryHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.registries.ForgeRegistries;

public class AttributeRegistryHelper implements RegistryHelper<Attribute> {

    public Attribute get(ResourceLocation id) {
        return ForgeRegistries.ATTRIBUTES.getValue(id);
    }

    public ResourceLocation getId(Attribute value) {
        return ForgeRegistries.ATTRIBUTES.getKey(value);
    }

    public boolean isRegistered(Attribute value) {
        return ForgeRegistries.ATTRIBUTES.containsValue(value);
    }

    @Override
    public boolean exists(ResourceLocation id) {
        return ForgeRegistries.ATTRIBUTES.containsKey(id);
    }

    @Override
    public Collection<Attribute> getValues() {
        return ForgeRegistries.ATTRIBUTES.getValues();
    }
}