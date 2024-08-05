package dev.xkmc.l2library.compat.curios;

import java.util.ArrayList;
import net.minecraft.resources.ResourceLocation;

public record CurioEntityBuilder(ArrayList<ResourceLocation> entities, ArrayList<String> slots, ArrayList<SlotCondition> conditions) {
}