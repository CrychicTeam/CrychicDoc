package net.blay09.mods.balm.forge.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.balm.forge.DeferredRegisters;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

public class ForgeBalmItems implements BalmItems {

    private final Map<String, ForgeBalmItems.Registrations> registrations = new ConcurrentHashMap();

    @Override
    public Item.Properties itemProperties() {
        return new Item.Properties();
    }

    @Override
    public DeferredObject<Item> registerItem(Supplier<Item> supplier, ResourceLocation identifier, @Nullable ResourceLocation creativeTab) {
        DeferredRegister<Item> register = DeferredRegisters.get(ForgeRegistries.ITEMS, identifier.getNamespace());
        RegistryObject<Item> registryObject = register.register(identifier.getPath(), supplier);
        if (creativeTab != null) {
            this.getActiveRegistrations().creativeTabContents.put(creativeTab, (Supplier) () -> new ItemLike[] { registryObject.get() });
        }
        return new DeferredObject<>(identifier, registryObject, registryObject::isPresent);
    }

    @Override
    public DeferredObject<CreativeModeTab> registerCreativeModeTab(Supplier<ItemStack> iconSupplier, ResourceLocation identifier) {
        DeferredRegister<CreativeModeTab> register = DeferredRegisters.get(Registries.CREATIVE_MODE_TAB, identifier.getNamespace());
        RegistryObject<CreativeModeTab> registryObject = register.register(identifier.getPath(), () -> {
            Component displayName = Component.translatable("itemGroup." + identifier.toString().replace(':', '.'));
            ForgeBalmItems.Registrations registrations = this.getActiveRegistrations();
            CreativeModeTab creativeModeTab = CreativeModeTab.builder().title(displayName).icon(iconSupplier).displayItems((enabledFeatures, entries) -> registrations.buildCreativeTabContents(identifier, entries)).build();
            return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, identifier, creativeModeTab);
        });
        return new DeferredObject<>(identifier, registryObject, registryObject::isPresent);
    }

    @Override
    public void addToCreativeModeTab(ResourceLocation tabIdentifier, Supplier<ItemLike[]> itemsSupplier) {
        this.getActiveRegistrations().creativeTabContents.put(tabIdentifier, itemsSupplier);
    }

    public void register() {
        FMLJavaModLoadingContext.get().getModEventBus().register(this.getActiveRegistrations());
    }

    private ForgeBalmItems.Registrations getActiveRegistrations() {
        return (ForgeBalmItems.Registrations) this.registrations.computeIfAbsent(ModLoadingContext.get().getActiveNamespace(), it -> new ForgeBalmItems.Registrations());
    }

    private static class Registrations {

        public final Multimap<ResourceLocation, Supplier<ItemLike[]>> creativeTabContents = ArrayListMultimap.create();

        public void buildCreativeTabContents(ResourceLocation tabIdentifier, CreativeModeTab.Output entries) {
            Collection<Supplier<ItemLike[]>> itemStacks = this.creativeTabContents.get(tabIdentifier);
            if (!itemStacks.isEmpty()) {
                itemStacks.forEach(it -> {
                    for (ItemLike itemStack : (ItemLike[]) it.get()) {
                        entries.accept(itemStack);
                    }
                });
            }
        }
    }
}