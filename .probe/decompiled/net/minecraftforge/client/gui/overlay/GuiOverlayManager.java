package net.minecraftforge.client.gui.overlay;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.fml.ModLoader;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public final class GuiOverlayManager {

    private static ImmutableList<NamedGuiOverlay> OVERLAYS;

    private static ImmutableMap<ResourceLocation, NamedGuiOverlay> OVERLAYS_BY_NAME;

    public static ImmutableList<NamedGuiOverlay> getOverlays() {
        return OVERLAYS;
    }

    @Nullable
    public static NamedGuiOverlay findOverlay(ResourceLocation id) {
        return (NamedGuiOverlay) OVERLAYS_BY_NAME.get(id);
    }

    @Internal
    public static void init() {
        HashMap<ResourceLocation, IGuiOverlay> overlays = new HashMap();
        ArrayList<ResourceLocation> orderedOverlays = new ArrayList();
        preRegisterVanillaOverlays(overlays, orderedOverlays);
        RegisterGuiOverlaysEvent event = new RegisterGuiOverlaysEvent(overlays, orderedOverlays);
        ModLoader.get().postEventWrapContainerInModOrder(event);
        OVERLAYS = (ImmutableList<NamedGuiOverlay>) orderedOverlays.stream().map(id -> new NamedGuiOverlay(id, (IGuiOverlay) overlays.get(id))).collect(ImmutableList.toImmutableList());
        OVERLAYS_BY_NAME = (ImmutableMap<ResourceLocation, NamedGuiOverlay>) OVERLAYS.stream().collect(ImmutableMap.toImmutableMap(NamedGuiOverlay::id, Function.identity()));
        assignVanillaOverlayTypes();
    }

    private static void preRegisterVanillaOverlays(HashMap<ResourceLocation, IGuiOverlay> overlays, ArrayList<ResourceLocation> orderedOverlays) {
        for (VanillaGuiOverlay entry : VanillaGuiOverlay.values()) {
            overlays.put(entry.id(), entry.overlay);
            orderedOverlays.add(entry.id());
        }
    }

    private static void assignVanillaOverlayTypes() {
        for (VanillaGuiOverlay entry : VanillaGuiOverlay.values()) {
            entry.type = (NamedGuiOverlay) OVERLAYS_BY_NAME.get(entry.id());
        }
    }

    private GuiOverlayManager() {
    }
}