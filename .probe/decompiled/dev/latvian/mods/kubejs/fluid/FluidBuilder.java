package dev.latvian.mods.kubejs.fluid;

import dev.architectury.core.fluid.ArchitecturyFlowingFluid;
import dev.architectury.core.fluid.ArchitecturyFluidAttributes;
import dev.architectury.core.fluid.SimpleArchitecturyFluidAttributes;
import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.util.UtilsJS;
import dev.latvian.mods.rhino.mod.util.color.Color;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.material.FlowingFluid;

public class FluidBuilder extends BuilderBase<FlowingFluid> {

    public transient ResourceLocation stillTexture;

    public transient ResourceLocation flowingTexture;

    public transient int color = -1;

    public transient int bucketColor = -1;

    public transient int luminosity = 0;

    public transient int density = 1000;

    public transient int temperature = 300;

    public transient int viscosity = 1000;

    public transient boolean isGaseous;

    public transient Rarity rarity = Rarity.COMMON;

    public transient String renderType = "solid";

    public ArchitecturyFluidAttributes attributes;

    public FlowingFluidBuilder flowingFluid;

    public FluidBlockBuilder block;

    public FluidBucketItemBuilder bucketItem;

    public FluidBuilder(ResourceLocation i) {
        super(i);
        this.stillTexture = this.newID("block/", "_still");
        this.flowingTexture = this.newID("block/", "_flow");
        this.flowingFluid = new FlowingFluidBuilder(this);
        this.block = new FluidBlockBuilder(this);
        this.bucketItem = new FluidBucketItemBuilder(this);
    }

    @Override
    public BuilderBase<FlowingFluid> displayName(Component name) {
        if (this.block != null) {
            this.block.displayName(name);
        }
        if (this.bucketItem != null) {
            this.bucketItem.displayName(Component.literal("").append(name).append(" Bucket"));
        }
        return super.displayName(name);
    }

    @Override
    public final RegistryInfo getRegistryType() {
        return RegistryInfo.FLUID;
    }

    public FlowingFluid createObject() {
        return new ArchitecturyFlowingFluid.Source(this.createAttributes());
    }

    public ArchitecturyFluidAttributes createAttributes() {
        if (this.attributes != null) {
            return this.attributes;
        } else {
            SimpleArchitecturyFluidAttributes attributes = SimpleArchitecturyFluidAttributes.of(this.flowingFluid, this).flowingTexture(this.flowingTexture).sourceTexture(this.stillTexture).color(this.color).rarity(this.rarity).density(this.density).viscosity(this.viscosity).luminosity(this.luminosity).temperature(this.temperature).lighterThanAir(this.isGaseous).bucketItem(() -> Optional.ofNullable(this.bucketItem).map(Supplier::get)).block(() -> Optional.ofNullable(this.block).map(Supplier::get).map(UtilsJS::cast));
            this.attributes = attributes;
            return attributes;
        }
    }

    @Override
    public void createAdditionalObjects() {
        RegistryInfo.FLUID.addBuilder(this.flowingFluid);
        if (this.block != null) {
            RegistryInfo.BLOCK.addBuilder(this.block);
        }
        if (this.bucketItem != null) {
            RegistryInfo.ITEM.addBuilder(this.bucketItem);
        }
    }

    @Override
    public BuilderBase<FlowingFluid> tag(ResourceLocation tag) {
        this.flowingFluid.tag(tag);
        return super.tag(tag);
    }

    public FluidBuilder color(Color c) {
        this.color = this.bucketColor = c.getArgbJS();
        return this;
    }

    public FluidBuilder bucketColor(Color c) {
        this.bucketColor = c.getArgbJS();
        return this;
    }

    public FluidBuilder builtinTextures() {
        this.stillTexture(KubeJS.id("fluid/fluid_thin_still"));
        this.flowingTexture(KubeJS.id("fluid/fluid_thin_flow"));
        return this;
    }

    public FluidBuilder stillTexture(ResourceLocation id) {
        this.stillTexture = id;
        return this;
    }

    public FluidBuilder flowingTexture(ResourceLocation id) {
        this.flowingTexture = id;
        return this;
    }

    public FluidBuilder renderType(String l) {
        this.renderType = l;
        return this;
    }

    public FluidBuilder translucent() {
        return this.renderType("translucent");
    }

    public FluidBuilder thickTexture(Color color) {
        return this.stillTexture(KubeJS.id("block/thick_fluid_still")).flowingTexture(KubeJS.id("block/thick_fluid_flow")).color(color);
    }

    public FluidBuilder thinTexture(Color color) {
        return this.stillTexture(KubeJS.id("block/thin_fluid_still")).flowingTexture(KubeJS.id("block/thin_fluid_flow")).color(color);
    }

    public FluidBuilder luminosity(int luminosity) {
        this.luminosity = luminosity;
        return this;
    }

    public FluidBuilder density(int density) {
        this.density = density;
        return this;
    }

    public FluidBuilder temperature(int temperature) {
        this.temperature = temperature;
        return this;
    }

    public FluidBuilder viscosity(int viscosity) {
        this.viscosity = viscosity;
        return this;
    }

    public FluidBuilder gaseous() {
        this.isGaseous = true;
        return this;
    }

    public FluidBuilder rarity(Rarity rarity) {
        this.rarity = rarity;
        return this;
    }

    public FluidBuilder noBucket() {
        this.bucketItem = null;
        return this;
    }

    public FluidBuilder noBlock() {
        this.block = null;
        return this;
    }
}