package com.simibubi.create.compat;

import com.simibubi.create.foundation.utility.Lang;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

public enum Mods {

    AETHER,
    COMPUTERCRAFT,
    CONNECTIVITY,
    CURIOS,
    DYNAMICTREES,
    OCCULTISM,
    PACKETFIXER,
    STORAGEDRAWERS,
    TCONSTRUCT,
    XLPACKETS;

    private final String id = Lang.asId(this.name());

    public String id() {
        return this.id;
    }

    public ResourceLocation rl(String path) {
        return new ResourceLocation(this.id, path);
    }

    public Block getBlock(String id) {
        return ForgeRegistries.BLOCKS.getValue(this.rl(id));
    }

    public boolean isLoaded() {
        return ModList.get().isLoaded(this.id);
    }

    public <T> Optional<T> runIfInstalled(Supplier<Supplier<T>> toRun) {
        return this.isLoaded() ? Optional.of(((Supplier) toRun.get()).get()) : Optional.empty();
    }

    public void executeIfInstalled(Supplier<Runnable> toExecute) {
        if (this.isLoaded()) {
            ((Runnable) toExecute.get()).run();
        }
    }
}