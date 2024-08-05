package net.liopyu.entityjs.builders.living.entityjs;

import dev.latvian.mods.kubejs.typings.Info;
import java.util.function.Function;
import net.liopyu.entityjs.entities.living.entityjs.WaterEntityJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class WaterEntityJSBuilder extends PathfinderMobBuilder<WaterEntityJS> {

    public transient Function<LivingEntity, Object> bucketItemStack;

    public WaterEntityJSBuilder(ResourceLocation i) {
        super(i);
    }

    @Info("@param bucketItemStack Function returning the itemstack to receive when bucketed\nDefaults to Axolotl Bucket\nExample usage:\n```javascript\nbuilder.bucketItemStack(entity => {\n    // Use information about the entity to return an ItemStack.\n    return Item.of('minecraft:diamond')\n})\n```\n")
    public WaterEntityJSBuilder bucketItemStack(Function<LivingEntity, Object> function) {
        this.bucketItemStack = function;
        return this;
    }

    @Override
    public EntityType.EntityFactory<WaterEntityJS> factory() {
        return (type, level) -> new WaterEntityJS(this, type, level);
    }

    @Override
    public AttributeSupplier.Builder getAttributeBuilder() {
        return WaterEntityJS.m_27495_().add(Attributes.MAX_HEALTH, 3.0).add(Attributes.FOLLOW_RANGE).add(Attributes.ATTACK_DAMAGE).add(Attributes.ARMOR).add(Attributes.ARMOR_TOUGHNESS).add(Attributes.ATTACK_SPEED).add(Attributes.ATTACK_KNOCKBACK).add(Attributes.LUCK).add(Attributes.MOVEMENT_SPEED);
    }
}