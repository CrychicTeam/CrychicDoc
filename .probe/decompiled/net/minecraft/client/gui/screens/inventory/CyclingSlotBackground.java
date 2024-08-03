package net.minecraft.client.gui.screens.inventory;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;

public class CyclingSlotBackground {

    private static final int ICON_CHANGE_TICK_RATE = 30;

    private static final int ICON_SIZE = 16;

    private static final int ICON_TRANSITION_TICK_DURATION = 4;

    private final int slotIndex;

    private List<ResourceLocation> icons = List.of();

    private int tick;

    private int iconIndex;

    public CyclingSlotBackground(int int0) {
        this.slotIndex = int0;
    }

    public void tick(List<ResourceLocation> listResourceLocation0) {
        if (!this.icons.equals(listResourceLocation0)) {
            this.icons = listResourceLocation0;
            this.iconIndex = 0;
        }
        if (!this.icons.isEmpty() && ++this.tick % 30 == 0) {
            this.iconIndex = (this.iconIndex + 1) % this.icons.size();
        }
    }

    public void render(AbstractContainerMenu abstractContainerMenu0, GuiGraphics guiGraphics1, float float2, int int3, int int4) {
        Slot $$5 = abstractContainerMenu0.getSlot(this.slotIndex);
        if (!this.icons.isEmpty() && !$$5.hasItem()) {
            boolean $$6 = this.icons.size() > 1 && this.tick >= 30;
            float $$7 = $$6 ? this.getIconTransitionTransparency(float2) : 1.0F;
            if ($$7 < 1.0F) {
                int $$8 = Math.floorMod(this.iconIndex - 1, this.icons.size());
                this.renderIcon($$5, (ResourceLocation) this.icons.get($$8), 1.0F - $$7, guiGraphics1, int3, int4);
            }
            this.renderIcon($$5, (ResourceLocation) this.icons.get(this.iconIndex), $$7, guiGraphics1, int3, int4);
        }
    }

    private void renderIcon(Slot slot0, ResourceLocation resourceLocation1, float float2, GuiGraphics guiGraphics3, int int4, int int5) {
        TextureAtlasSprite $$6 = (TextureAtlasSprite) Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(resourceLocation1);
        guiGraphics3.blit(int4 + slot0.x, int5 + slot0.y, 0, 16, 16, $$6, 1.0F, 1.0F, 1.0F, float2);
    }

    private float getIconTransitionTransparency(float float0) {
        float $$1 = (float) (this.tick % 30) + float0;
        return Math.min($$1, 4.0F) / 4.0F;
    }
}