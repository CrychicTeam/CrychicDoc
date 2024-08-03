package com.yungnickyoung.minecraft.yungsapi.api.autoregister;

import com.yungnickyoung.minecraft.yungsapi.autoregister.AutoRegisterEntry;
import java.util.function.Supplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.jetbrains.annotations.ApiStatus.Internal;

public class AutoRegisterBlock extends AutoRegisterEntry<Block> {

    private Supplier<Item.Properties> itemProperties;

    private WoodType fenceGateWoodType;

    private boolean hasStairs;

    private boolean hasSlab;

    private boolean hasFence;

    private boolean hasFenceGate;

    private boolean hasWall;

    private Block stairs;

    private Block slab;

    private Block fence;

    private Block fenceGate;

    private Block wall;

    public static AutoRegisterBlock of(Supplier<Block> blockSupplier) {
        return new AutoRegisterBlock(blockSupplier);
    }

    private AutoRegisterBlock(Supplier<Block> blockSupplier) {
        super(blockSupplier);
    }

    public AutoRegisterBlock withItem(Supplier<Item.Properties> itemProperties) {
        this.itemProperties = itemProperties;
        return this;
    }

    public Supplier<Item.Properties> getItemProperties() {
        return this.itemProperties;
    }

    public AutoRegisterBlock withStairs() {
        this.hasStairs = true;
        return this;
    }

    public AutoRegisterBlock withSlab() {
        this.hasSlab = true;
        return this;
    }

    public AutoRegisterBlock withFence() {
        this.hasFence = true;
        return this;
    }

    public AutoRegisterBlock withFenceGate(WoodType woodType) {
        this.hasFenceGate = true;
        this.fenceGateWoodType = woodType;
        return this;
    }

    public Block getStairs() {
        return this.stairs;
    }

    public Block getSlab() {
        return this.slab;
    }

    public Block getFence() {
        return this.fence;
    }

    public Block getFenceGate() {
        return this.fenceGate;
    }

    public Block getWall() {
        return this.wall;
    }

    public AutoRegisterBlock withWall() {
        this.hasWall = true;
        return this;
    }

    @Internal
    public boolean hasItemProperties() {
        return this.itemProperties != null;
    }

    @Internal
    public boolean hasStairs() {
        return this.hasStairs;
    }

    @Internal
    public boolean hasSlab() {
        return this.hasSlab;
    }

    @Internal
    public boolean hasFence() {
        return this.hasFence;
    }

    @Internal
    public boolean hasFenceGate() {
        return this.hasFenceGate;
    }

    @Internal
    public boolean hasWall() {
        return this.hasWall;
    }

    @Internal
    public WoodType getFenceGateWoodType() {
        return this.fenceGateWoodType;
    }

    @Internal
    public void setStairs(Block stairs) {
        this.stairs = stairs;
    }

    @Internal
    public void setSlab(Block slab) {
        this.slab = slab;
    }

    @Internal
    public void setFence(Block fence) {
        this.fence = fence;
    }

    @Internal
    public void setFenceGate(Block fenceGate) {
        this.fenceGate = fenceGate;
    }

    @Internal
    public void setWall(Block wall) {
        this.wall = wall;
    }
}