package fr.frinn.custommachinery.common.util;

import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class TagUtil {

    public static Stream<Item> getItems(TagKey<Item> tag) {
        return (Stream<Item>) BuiltInRegistries.ITEM.m_203431_(tag).map(named -> named.m_203614_().map(Holder::m_203334_)).orElse(Stream.empty());
    }

    public static Stream<Block> getBlocks(TagKey<Block> tag) {
        return (Stream<Block>) BuiltInRegistries.BLOCK.m_203431_(tag).map(named -> named.m_203614_().map(Holder::m_203334_)).orElse(Stream.empty());
    }

    public static Stream<Fluid> getFluids(TagKey<Fluid> tag) {
        return (Stream<Fluid>) BuiltInRegistries.FLUID.m_203431_(tag).map(named -> named.m_203614_().map(Holder::m_203334_)).orElse(Stream.empty());
    }
}