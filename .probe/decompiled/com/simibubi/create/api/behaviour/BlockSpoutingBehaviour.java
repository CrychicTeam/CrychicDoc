package com.simibubi.create.api.behaviour;

import com.simibubi.create.Create;
import com.simibubi.create.compat.tconstruct.SpoutCasting;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

public abstract class BlockSpoutingBehaviour {

    private static final Map<ResourceLocation, BlockSpoutingBehaviour> BLOCK_SPOUTING_BEHAVIOURS = new HashMap();

    public static void addCustomSpoutInteraction(ResourceLocation resourceLocation, BlockSpoutingBehaviour movementBehaviour) {
        BLOCK_SPOUTING_BEHAVIOURS.put(resourceLocation, movementBehaviour);
    }

    public static void forEach(Consumer<? super BlockSpoutingBehaviour> accept) {
        BLOCK_SPOUTING_BEHAVIOURS.values().forEach(accept);
    }

    public abstract int fillBlock(Level var1, BlockPos var2, SpoutBlockEntity var3, FluidStack var4, boolean var5);

    public static void registerDefaults() {
        addCustomSpoutInteraction(Create.asResource("ticon_casting"), new SpoutCasting());
    }
}