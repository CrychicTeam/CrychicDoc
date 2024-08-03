package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class IafTabRegistry {

    public static final DeferredRegister<CreativeModeTab> TAB_REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "iceandfire");

    public static final List<Supplier<? extends Block>> TAB_BLOCKS_LIST = new ArrayList();

    public static final List<Supplier<? extends Item>> TAB_ITEMS_LIST = new ArrayList();

    public static final RegistryObject<CreativeModeTab> TAB_BLOCKS = TAB_REGISTER.register("blocks", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.iceandfire.blocks")).icon(() -> new ItemStack(IafBlockRegistry.DRAGON_SCALE_RED.get())).withTabsBefore(new ResourceKey[] { CreativeModeTabs.SPAWN_EGGS }).displayItems((params, output) -> TAB_BLOCKS_LIST.forEach(block -> output.accept((ItemLike) block.get()))).build());

    public static final RegistryObject<CreativeModeTab> TAB_ITEMS = TAB_REGISTER.register("items", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.iceandfire.items")).icon(() -> new ItemStack(IafItemRegistry.DRAGON_SKULL_FIRE.get())).withTabsBefore(new ResourceKey[] { TAB_BLOCKS.getKey() }).displayItems((params, output) -> TAB_ITEMS_LIST.forEach(block -> output.accept((ItemLike) block.get()))).build());
}