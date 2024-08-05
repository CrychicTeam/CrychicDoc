package net.minecraft.world.item.armortrim;

import java.util.Optional;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class TrimPatterns {

    public static final ResourceKey<TrimPattern> SENTRY = registryKey("sentry");

    public static final ResourceKey<TrimPattern> DUNE = registryKey("dune");

    public static final ResourceKey<TrimPattern> COAST = registryKey("coast");

    public static final ResourceKey<TrimPattern> WILD = registryKey("wild");

    public static final ResourceKey<TrimPattern> WARD = registryKey("ward");

    public static final ResourceKey<TrimPattern> EYE = registryKey("eye");

    public static final ResourceKey<TrimPattern> VEX = registryKey("vex");

    public static final ResourceKey<TrimPattern> TIDE = registryKey("tide");

    public static final ResourceKey<TrimPattern> SNOUT = registryKey("snout");

    public static final ResourceKey<TrimPattern> RIB = registryKey("rib");

    public static final ResourceKey<TrimPattern> SPIRE = registryKey("spire");

    public static final ResourceKey<TrimPattern> WAYFINDER = registryKey("wayfinder");

    public static final ResourceKey<TrimPattern> SHAPER = registryKey("shaper");

    public static final ResourceKey<TrimPattern> SILENCE = registryKey("silence");

    public static final ResourceKey<TrimPattern> RAISER = registryKey("raiser");

    public static final ResourceKey<TrimPattern> HOST = registryKey("host");

    public static void bootstrap(BootstapContext<TrimPattern> bootstapContextTrimPattern0) {
        register(bootstapContextTrimPattern0, Items.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE, SENTRY);
        register(bootstapContextTrimPattern0, Items.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE, DUNE);
        register(bootstapContextTrimPattern0, Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE, COAST);
        register(bootstapContextTrimPattern0, Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE, WILD);
        register(bootstapContextTrimPattern0, Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE, WARD);
        register(bootstapContextTrimPattern0, Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE, EYE);
        register(bootstapContextTrimPattern0, Items.VEX_ARMOR_TRIM_SMITHING_TEMPLATE, VEX);
        register(bootstapContextTrimPattern0, Items.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE, TIDE);
        register(bootstapContextTrimPattern0, Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE, SNOUT);
        register(bootstapContextTrimPattern0, Items.RIB_ARMOR_TRIM_SMITHING_TEMPLATE, RIB);
        register(bootstapContextTrimPattern0, Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE, SPIRE);
        register(bootstapContextTrimPattern0, Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE, WAYFINDER);
        register(bootstapContextTrimPattern0, Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE, SHAPER);
        register(bootstapContextTrimPattern0, Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, SILENCE);
        register(bootstapContextTrimPattern0, Items.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE, RAISER);
        register(bootstapContextTrimPattern0, Items.HOST_ARMOR_TRIM_SMITHING_TEMPLATE, HOST);
    }

    public static Optional<Holder.Reference<TrimPattern>> getFromTemplate(RegistryAccess registryAccess0, ItemStack itemStack1) {
        return registryAccess0.registryOrThrow(Registries.TRIM_PATTERN).holders().filter(p_266833_ -> itemStack1.is(((TrimPattern) p_266833_.value()).templateItem())).findFirst();
    }

    private static void register(BootstapContext<TrimPattern> bootstapContextTrimPattern0, Item item1, ResourceKey<TrimPattern> resourceKeyTrimPattern2) {
        TrimPattern $$3 = new TrimPattern(resourceKeyTrimPattern2.location(), BuiltInRegistries.ITEM.m_263177_(item1), Component.translatable(Util.makeDescriptionId("trim_pattern", resourceKeyTrimPattern2.location())));
        bootstapContextTrimPattern0.register(resourceKeyTrimPattern2, $$3);
    }

    private static ResourceKey<TrimPattern> registryKey(String string0) {
        return ResourceKey.create(Registries.TRIM_PATTERN, new ResourceLocation(string0));
    }
}