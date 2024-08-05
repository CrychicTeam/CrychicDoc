package com.mna.rituals;

import com.mna.api.rituals.IRitualReagent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

public class RitualReagent implements IRitualReagent {

    private ResourceLocation rLoc;

    private boolean isOptional;

    private boolean shouldConsume;

    private boolean manualReturn;

    private boolean isDynamic;

    private boolean isDynamicSource;

    public RitualReagent(ResourceLocation rLoc) {
        this.rLoc = rLoc;
        this.isOptional = false;
        this.shouldConsume = true;
        this.manualReturn = false;
    }

    public RitualReagent(String namespaceID, String path) {
        this(new ResourceLocation(namespaceID, path));
    }

    public RitualReagent(String resourceName) {
        this(new ResourceLocation(resourceName));
    }

    public RitualReagent asOptional() {
        if (!this.isDynamicSource) {
            this.isOptional = true;
        }
        return this;
    }

    public RitualReagent noConsume() {
        this.shouldConsume = false;
        return this;
    }

    public RitualReagent manualReturn() {
        this.manualReturn = true;
        return this;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return this.rLoc;
    }

    @Override
    public boolean isOptional() {
        return this.isOptional;
    }

    @Override
    public boolean shouldConsumeReagent() {
        return this.shouldConsume;
    }

    @Override
    public boolean isManualReturn() {
        return this.manualReturn;
    }

    @Override
    public boolean isEmpty() {
        return this.rLoc == null || this.rLoc.toString().equals(ForgeRegistries.ITEMS.getKey(Items.AIR).toString());
    }

    @Override
    public boolean isDynamic() {
        return this.isDynamic;
    }

    @Override
    public boolean isDynamicSource() {
        return this.isDynamicSource;
    }

    public RitualReagent asDynamic() {
        this.isDynamic = true;
        return this;
    }

    public RitualReagent asDynamicSource() {
        this.isOptional = false;
        this.isDynamicSource = true;
        return this;
    }

    @Override
    public void setResourceLocation(ResourceLocation rLoc) {
        if (this.isDynamic()) {
            this.rLoc = rLoc;
            this.isDynamic = false;
            this.isOptional = false;
        }
    }

    public void writeToNBT(CompoundTag nbt) {
        if (this.rLoc != null) {
            nbt.putString("ritual_reagent_resource", this.rLoc.toString());
            nbt.putBoolean("ritual_reagent_optional", this.isOptional);
            nbt.putBoolean("ritual_reagent_consume", this.shouldConsume);
            nbt.putBoolean("ritual_reagent_manual_return", this.manualReturn);
            nbt.putBoolean("ritual_reagent_dynamic", this.isDynamic);
            nbt.putBoolean("ritual_reagent_dynamic_source", this.isDynamicSource);
        }
    }

    public static RitualReagent fromNBT(CompoundTag nbt) {
        String key = "";
        if (nbt.contains("ritual_reagent_resource")) {
            key = nbt.getString("ritual_reagent_resource");
        }
        RitualReagent rr = key.isEmpty() ? null : new RitualReagent(new ResourceLocation(key));
        if (nbt.contains("ritual_reagent_optional")) {
            rr.isOptional = nbt.getBoolean("ritual_reagent_optional");
        }
        if (nbt.contains("ritual_reagent_consume")) {
            rr.shouldConsume = nbt.getBoolean("ritual_reagent_consume");
        }
        if (nbt.contains("ritual_reagent_manual_return")) {
            rr.manualReturn = nbt.getBoolean("ritual_reagent_manual_return");
        }
        if (nbt.contains("ritual_reagent_dynamic")) {
            rr.isDynamic = nbt.getBoolean("ritual_reagent_dynamic");
        }
        if (nbt.contains("ritual_reagent_dynamic_source")) {
            rr.isDynamicSource = nbt.getBoolean("ritual_reagent_dynamic_source");
        }
        return rr;
    }
}