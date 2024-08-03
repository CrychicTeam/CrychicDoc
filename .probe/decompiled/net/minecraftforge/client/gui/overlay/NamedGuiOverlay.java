package net.minecraftforge.client.gui.overlay;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus.Internal;

public record NamedGuiOverlay(ResourceLocation id, IGuiOverlay overlay) {

    @Internal
    public NamedGuiOverlay(ResourceLocation id, IGuiOverlay overlay) {
        this.id = id;
        this.overlay = overlay;
    }
}