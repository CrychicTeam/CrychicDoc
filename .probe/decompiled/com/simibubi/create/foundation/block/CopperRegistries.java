package com.simibubi.create.foundation.block;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableBiMap.Builder;
import com.simibubi.create.Create;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;

public class CopperRegistries {

    private static final BiMap<Supplier<Block>, Supplier<Block>> WEATHERING = HashBiMap.create();

    private static final BiMap<Supplier<Block>, Supplier<Block>> WAXABLE = HashBiMap.create();

    private static boolean injected;

    private static boolean weatheringMemoized;

    private static boolean waxableMemoized;

    public static synchronized void addWeathering(Supplier<Block> original, Supplier<Block> weathered) {
        if (weatheringMemoized) {
            throw new IllegalStateException("Cannot add weathering entry to CopperRegistries after memoization!");
        } else {
            WEATHERING.put(original, weathered);
        }
    }

    public static synchronized void addWaxable(Supplier<Block> original, Supplier<Block> waxed) {
        if (waxableMemoized) {
            throw new IllegalStateException("Cannot add waxable entry to CopperRegistries after memoization!");
        } else {
            WAXABLE.put(original, waxed);
        }
    }

    public static void inject() {
        if (injected) {
            throw new IllegalStateException("Cannot inject CopperRegistries twice!");
        } else {
            injected = true;
            try {
                Field delegateField = WeatheringCopper.NEXT_BY_BLOCK.getClass().getDeclaredField("delegate");
                delegateField.setAccessible(true);
                Supplier<BiMap<Block, Block>> originalWeatheringMapDelegate = (Supplier<BiMap<Block, Block>>) delegateField.get(WeatheringCopper.NEXT_BY_BLOCK);
                com.google.common.base.Supplier<BiMap<Block, Block>> weatheringMapDelegate = () -> {
                    weatheringMemoized = true;
                    Builder<Block, Block> builder = ImmutableBiMap.builder();
                    builder.putAll((Map) originalWeatheringMapDelegate.get());
                    WEATHERING.forEach((original, weathered) -> builder.put((Block) original.get(), (Block) weathered.get()));
                    return builder.build();
                };
                delegateField.set(WeatheringCopper.NEXT_BY_BLOCK, weatheringMapDelegate);
            } catch (Exception var3) {
                Create.LOGGER.error("Failed to inject weathering copper from CopperRegistries", var3);
            }
            Supplier<BiMap<Block, Block>> originalWaxableMapSupplier = HoneycombItem.WAXABLES;
            Supplier<BiMap<Block, Block>> waxableMapSupplier = Suppliers.memoize(() -> {
                waxableMemoized = true;
                Builder<Block, Block> builder = ImmutableBiMap.builder();
                builder.putAll((Map) originalWaxableMapSupplier.get());
                WAXABLE.forEach((original, waxed) -> builder.put((Block) original.get(), (Block) waxed.get()));
                return builder.build();
            });
            HoneycombItem.WAXABLES = waxableMapSupplier;
        }
    }
}