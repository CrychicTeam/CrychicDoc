package net.liopyu.entityjs.builders.living.vanilla;

import net.liopyu.entityjs.builders.living.entityjs.PathfinderMobBuilder;
import net.liopyu.entityjs.entities.living.vanilla.AllayEntityJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

public class AllayJSBuilder extends PathfinderMobBuilder<AllayEntityJS> {

    public AllayJSBuilder(ResourceLocation i) {
        super(i);
    }

    @Override
    public EntityType.EntityFactory<AllayEntityJS> factory() {
        return (type, level) -> new AllayEntityJS(this, type, level);
    }

    @Override
    public AttributeSupplier.Builder getAttributeBuilder() {
        return AllayEntityJS.m_218388_();
    }
}