package dev.xkmc.l2artifacts.init.data.slot;

import java.util.ArrayList;
import net.minecraft.resources.ResourceLocation;

public record CurioEntityBuilder(ArrayList<ResourceLocation> entities, ArrayList<String> slots) {
}