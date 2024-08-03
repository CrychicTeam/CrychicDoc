package com.mna.entities.attributes;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AttributeInit {

    public static DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, "mna");

    public static RegistryObject<Attribute> PERCEPTION_DISTANCE = ATTRIBUTES.register("perception_distance", () -> new AttributePerceptionDistance("perception_distance", 8.0));

    public static RegistryObject<Attribute> INTELLIGENCE = ATTRIBUTES.register("intelligence", () -> new AttributeIntelligence("intelligence", 8.0));

    public static RegistryObject<Attribute> RANGED_DAMAGE = ATTRIBUTES.register("ranged_damage", () -> new AttributeRangedDamage("ranged_damage", 0.0));
}