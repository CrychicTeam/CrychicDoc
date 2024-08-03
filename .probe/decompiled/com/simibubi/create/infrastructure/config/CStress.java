package com.simibubi.create.infrastructure.config;

import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.content.kinetics.BlockStressValues;
import com.simibubi.create.foundation.config.ConfigBase;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeConfigSpec;

public class CStress extends ConfigBase implements BlockStressValues.IStressValueProvider {

    private final Map<ResourceLocation, ForgeConfigSpec.ConfigValue<Double>> capacities = new HashMap();

    private final Map<ResourceLocation, ForgeConfigSpec.ConfigValue<Double>> impacts = new HashMap();

    @Override
    public void registerAll(ForgeConfigSpec.Builder builder) {
        builder.comment(".", CStress.Comments.su, CStress.Comments.impact).push("impact");
        BlockStressDefaults.DEFAULT_IMPACTS.forEach((r, i) -> {
            if (r.getNamespace().equals("create")) {
                this.getImpacts().put(r, builder.define(r.getPath(), i));
            }
        });
        builder.pop();
        builder.comment(".", CStress.Comments.su, CStress.Comments.capacity).push("capacity");
        BlockStressDefaults.DEFAULT_CAPACITIES.forEach((r, i) -> {
            if (r.getNamespace().equals("create")) {
                this.getCapacities().put(r, builder.define(r.getPath(), i));
            }
        });
        builder.pop();
    }

    @Override
    public double getImpact(Block block) {
        block = this.redirectValues(block);
        ResourceLocation key = RegisteredObjects.getKeyOrThrow(block);
        ForgeConfigSpec.ConfigValue<Double> value = (ForgeConfigSpec.ConfigValue<Double>) this.getImpacts().get(key);
        return value != null ? value.get() : 0.0;
    }

    @Override
    public double getCapacity(Block block) {
        block = this.redirectValues(block);
        ResourceLocation key = RegisteredObjects.getKeyOrThrow(block);
        ForgeConfigSpec.ConfigValue<Double> value = (ForgeConfigSpec.ConfigValue<Double>) this.getCapacities().get(key);
        return value != null ? value.get() : 0.0;
    }

    @Override
    public Couple<Integer> getGeneratedRPM(Block block) {
        block = this.redirectValues(block);
        ResourceLocation key = RegisteredObjects.getKeyOrThrow(block);
        Supplier<Couple<Integer>> supplier = (Supplier<Couple<Integer>>) BlockStressDefaults.GENERATOR_SPEEDS.get(key);
        return supplier == null ? null : (Couple) supplier.get();
    }

    @Override
    public boolean hasImpact(Block block) {
        block = this.redirectValues(block);
        ResourceLocation key = RegisteredObjects.getKeyOrThrow(block);
        return this.getImpacts().containsKey(key);
    }

    @Override
    public boolean hasCapacity(Block block) {
        block = this.redirectValues(block);
        ResourceLocation key = RegisteredObjects.getKeyOrThrow(block);
        return this.getCapacities().containsKey(key);
    }

    protected Block redirectValues(Block block) {
        return block;
    }

    @Override
    public String getName() {
        return "stressValues.v2";
    }

    public Map<ResourceLocation, ForgeConfigSpec.ConfigValue<Double>> getImpacts() {
        return this.impacts;
    }

    public Map<ResourceLocation, ForgeConfigSpec.ConfigValue<Double>> getCapacities() {
        return this.capacities;
    }

    private static class Comments {

        static String su = "[in Stress Units]";

        static String impact = "Configure the individual stress impact of mechanical blocks. Note that this cost is doubled for every speed increase it receives.";

        static String capacity = "Configure how much stress a source can accommodate for.";
    }
}