package net.minecraftforge.client.event;

import com.google.common.base.Preconditions;
import java.util.List;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.IModBusEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public class RegisterGuiOverlaysEvent extends Event implements IModBusEvent {

    private final Map<ResourceLocation, IGuiOverlay> overlays;

    private final List<ResourceLocation> orderedOverlays;

    @Internal
    public RegisterGuiOverlaysEvent(Map<ResourceLocation, IGuiOverlay> overlays, List<ResourceLocation> orderedOverlays) {
        this.overlays = overlays;
        this.orderedOverlays = orderedOverlays;
    }

    public void registerBelowAll(@NotNull String id, @NotNull IGuiOverlay overlay) {
        this.register(RegisterGuiOverlaysEvent.Ordering.BEFORE, null, id, overlay);
    }

    public void registerBelow(@NotNull ResourceLocation other, @NotNull String id, @NotNull IGuiOverlay overlay) {
        this.register(RegisterGuiOverlaysEvent.Ordering.BEFORE, other, id, overlay);
    }

    public void registerAbove(@NotNull ResourceLocation other, @NotNull String id, @NotNull IGuiOverlay overlay) {
        this.register(RegisterGuiOverlaysEvent.Ordering.AFTER, other, id, overlay);
    }

    public void registerAboveAll(@NotNull String id, @NotNull IGuiOverlay overlay) {
        this.register(RegisterGuiOverlaysEvent.Ordering.AFTER, null, id, overlay);
    }

    private void register(@NotNull RegisterGuiOverlaysEvent.Ordering ordering, @Nullable ResourceLocation other, @NotNull String id, @NotNull IGuiOverlay overlay) {
        ResourceLocation key = new ResourceLocation(ModLoadingContext.get().getActiveNamespace(), id);
        Preconditions.checkArgument(!this.overlays.containsKey(key), "Overlay already registered: " + key);
        int insertPosition;
        if (other == null) {
            insertPosition = ordering == RegisterGuiOverlaysEvent.Ordering.BEFORE ? 0 : this.overlays.size();
        } else {
            int otherIndex = this.orderedOverlays.indexOf(other);
            Preconditions.checkState(otherIndex >= 0, "Attempted to order against an unregistered overlay. Only order against vanilla's and your own.");
            insertPosition = otherIndex + (ordering == RegisterGuiOverlaysEvent.Ordering.BEFORE ? 0 : 1);
        }
        this.overlays.put(key, overlay);
        this.orderedOverlays.add(insertPosition, key);
    }

    private static enum Ordering {

        BEFORE, AFTER
    }
}