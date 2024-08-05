package com.simibubi.create.content.kinetics;

import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class BlockStressValues {

    private static final Map<String, BlockStressValues.IStressValueProvider> PROVIDERS = new HashMap();

    public static void registerProvider(String namespace, BlockStressValues.IStressValueProvider provider) {
        PROVIDERS.put(namespace, provider);
    }

    @Nullable
    public static BlockStressValues.IStressValueProvider getProvider(String namespace) {
        return (BlockStressValues.IStressValueProvider) PROVIDERS.get(namespace);
    }

    @Nullable
    public static BlockStressValues.IStressValueProvider getProvider(Block block) {
        return getProvider(RegisteredObjects.getKeyOrThrow(block).getNamespace());
    }

    public static double getImpact(Block block) {
        ResourceLocation blockId = RegisteredObjects.getKeyOrThrow(block);
        BlockStressValues.IStressValueProvider provider = getProvider(blockId.getNamespace());
        if (provider != null) {
            return provider.getImpact(block);
        } else {
            Double defaultImpact = (Double) BlockStressDefaults.DEFAULT_IMPACTS.get(blockId);
            return defaultImpact != null ? defaultImpact : 0.0;
        }
    }

    public static double getCapacity(Block block) {
        ResourceLocation blockId = RegisteredObjects.getKeyOrThrow(block);
        BlockStressValues.IStressValueProvider provider = getProvider(blockId.getNamespace());
        if (provider != null) {
            return provider.getCapacity(block);
        } else {
            Double defaultCapacity = (Double) BlockStressDefaults.DEFAULT_CAPACITIES.get(blockId);
            return defaultCapacity != null ? defaultCapacity : 0.0;
        }
    }

    public static boolean hasImpact(Block block) {
        ResourceLocation blockId = RegisteredObjects.getKeyOrThrow(block);
        BlockStressValues.IStressValueProvider provider = getProvider(blockId.getNamespace());
        return provider != null ? provider.hasImpact(block) : BlockStressDefaults.DEFAULT_IMPACTS.containsKey(blockId);
    }

    public static boolean hasCapacity(Block block) {
        ResourceLocation blockId = RegisteredObjects.getKeyOrThrow(block);
        BlockStressValues.IStressValueProvider provider = getProvider(blockId.getNamespace());
        return provider != null ? provider.hasCapacity(block) : BlockStressDefaults.DEFAULT_CAPACITIES.containsKey(blockId);
    }

    @Nullable
    public static Couple<Integer> getGeneratedRPM(Block block) {
        ResourceLocation blockId = RegisteredObjects.getKeyOrThrow(block);
        BlockStressValues.IStressValueProvider provider = getProvider(blockId.getNamespace());
        return provider != null ? provider.getGeneratedRPM(block) : null;
    }

    public interface IStressValueProvider {

        double getImpact(Block var1);

        double getCapacity(Block var1);

        boolean hasImpact(Block var1);

        boolean hasCapacity(Block var1);

        @Nullable
        Couple<Integer> getGeneratedRPM(Block var1);
    }
}