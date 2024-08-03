package net.mehvahdjukaar.moonlight.api.events;

import java.util.List;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;

public record EarlyPackReloadEvent(List<PackResources> packs, ResourceManager manager, PackType type) implements SimpleEvent {
}