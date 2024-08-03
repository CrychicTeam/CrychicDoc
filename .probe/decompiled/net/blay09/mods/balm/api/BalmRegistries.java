package net.blay09.mods.balm.api;

import java.util.Collection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public interface BalmRegistries {

    ResourceLocation getKey(Item var1);

    ResourceLocation getKey(Block var1);

    ResourceLocation getKey(Fluid var1);

    ResourceLocation getKey(EntityType<?> var1);

    ResourceLocation getKey(MenuType<?> var1);

    Collection<ResourceLocation> getItemKeys();

    Item getItem(ResourceLocation var1);

    Block getBlock(ResourceLocation var1);

    Fluid getFluid(ResourceLocation var1);

    MobEffect getMobEffect(ResourceLocation var1);

    TagKey<Item> getItemTag(ResourceLocation var1);

    void enableMilkFluid();

    Fluid getMilkFluid();

    Attribute getAttribute(ResourceLocation var1);
}