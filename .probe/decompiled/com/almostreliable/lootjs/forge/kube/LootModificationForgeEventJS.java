package com.almostreliable.lootjs.forge.kube;

import com.almostreliable.lootjs.kube.LootJSPlugin;
import com.almostreliable.lootjs.kube.LootModificationEventJS;
import dev.latvian.mods.kubejs.event.EventResult;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.loot.IGlobalLootModifier;

public class LootModificationForgeEventJS extends LootModificationEventJS {

    private final Map<ResourceLocation, IGlobalLootModifier> modifiers;

    private final List<ResourceLocation> removed = new ArrayList();

    public LootModificationForgeEventJS(Map<ResourceLocation, IGlobalLootModifier> modifiers) {
        this.modifiers = modifiers;
    }

    public List<String> getGlobalModifiers() {
        return (List<String>) this.modifiers.keySet().stream().map(ResourceLocation::toString).collect(Collectors.toList());
    }

    public void removeGlobalModifier(String... locationOrModIds) {
        Set<String> modIds = new HashSet();
        Set<ResourceLocation> locations = new HashSet();
        for (String locationOrModId : locationOrModIds) {
            if (locationOrModId.startsWith("@")) {
                modIds.add(locationOrModId.substring(1));
            } else {
                locations.add(new ResourceLocation(locationOrModId));
            }
        }
        Set<ResourceLocation> collectedByModIds = (Set<ResourceLocation>) this.modifiers.keySet().stream().filter(rl -> modIds.contains(rl.getNamespace())).collect(Collectors.toSet());
        Set<ResourceLocation> collectedByLocations = (Set<ResourceLocation>) this.modifiers.keySet().stream().filter(locations::contains).collect(Collectors.toSet());
        this.remove(collectedByModIds);
        this.remove(collectedByLocations);
    }

    private void remove(Set<ResourceLocation> locations) {
        locations.forEach(this.modifiers::remove);
        this.removed.addAll(locations);
    }

    @Override
    protected void afterPosted(EventResult result) {
        super.afterPosted(result);
        if (!LootJSPlugin.eventsAreDisabled()) {
            if (!this.removed.isEmpty()) {
                ConsoleJS.SERVER.info("[LootJS] Removed " + this.removed.size() + " global loot modifiers: " + (String) this.removed.stream().map(ResourceLocation::toString).collect(Collectors.joining(", ")));
            }
        }
    }
}