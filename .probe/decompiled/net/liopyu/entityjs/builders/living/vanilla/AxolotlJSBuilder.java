package net.liopyu.entityjs.builders.living.vanilla;

import dev.latvian.mods.kubejs.typings.Info;
import java.util.function.Function;
import net.liopyu.entityjs.builders.living.entityjs.AnimalEntityBuilder;
import net.liopyu.entityjs.entities.living.vanilla.AxolotlEntityJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

public class AxolotlJSBuilder extends AnimalEntityBuilder<AxolotlEntityJS> {

    public transient Function<LivingEntity, Object> bucketItemStack;

    public AxolotlJSBuilder(ResourceLocation i) {
        super(i);
    }

    @Info("@param bucketItemStack Function returning the itemstack to receive when bucketed\nDefaults to Axolotl Bucket\nExample usage:\n```javascript\nbuilder.bucketItemStack(entity => {\n    // Use information about the entity to return an ItemStack.\n    return Item.of('minecraft:diamond')\n})\n```\n")
    public AxolotlJSBuilder bucketItemStack(Function<LivingEntity, Object> function) {
        this.bucketItemStack = function;
        return this;
    }

    @Override
    public EntityType.EntityFactory<AxolotlEntityJS> factory() {
        return (type, level) -> new AxolotlEntityJS(this, type, level);
    }

    @Override
    public AttributeSupplier.Builder getAttributeBuilder() {
        return AxolotlEntityJS.m_149176_();
    }
}