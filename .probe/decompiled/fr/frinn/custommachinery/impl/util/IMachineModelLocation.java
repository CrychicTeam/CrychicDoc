package fr.frinn.custommachinery.impl.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface IMachineModelLocation {

    @Nullable
    BlockState getState();

    @Nullable
    Item getItem();

    @Nullable
    ResourceLocation getLoc();

    @Nullable
    String getProperties();

    String toString();
}