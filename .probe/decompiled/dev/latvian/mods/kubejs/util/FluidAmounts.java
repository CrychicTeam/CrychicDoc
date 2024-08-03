package dev.latvian.mods.kubejs.util;

import dev.architectury.hooks.fluid.FluidStackHooks;
import dev.latvian.mods.kubejs.platform.MiscPlatformHelper;

public interface FluidAmounts {

    long BUCKET = FluidStackHooks.bucketAmount();

    long MILLIBUCKET = BUCKET / 1000L;

    long B = BUCKET;

    long MB = MILLIBUCKET;

    long INGOT = MiscPlatformHelper.get().ingotFluidAmount();

    long NUGGET = INGOT / 9L;

    long METAL_BLOCK = INGOT * 9L;

    long BOTTLE = MiscPlatformHelper.get().bottleFluidAmount();
}