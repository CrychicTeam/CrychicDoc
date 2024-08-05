package com.simibubi.create.foundation.ponder;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.element.ScreenElement;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class PonderChapter implements ScreenElement {

    private final ResourceLocation id;

    private final ResourceLocation icon;

    private PonderChapter(ResourceLocation id) {
        this.id = id;
        this.icon = new ResourceLocation(id.getNamespace(), "textures/ponder/chapter/" + id.getPath() + ".png");
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public String getTitle() {
        return PonderLocalization.getChapter(this.id);
    }

    public PonderChapter addTagsToChapter(PonderTag... tags) {
        for (PonderTag t : tags) {
            PonderRegistry.TAGS.add(t, this);
        }
        return this;
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y) {
        PoseStack ms = graphics.pose();
        ms.pushPose();
        ms.scale(0.25F, 0.25F, 1.0F);
        graphics.blit(this.icon, x, y, 0, 0.0F, 0.0F, 64, 64, 64, 64);
        ms.popPose();
    }

    @Nonnull
    public static PonderChapter of(ResourceLocation id) {
        PonderChapter chapter = PonderRegistry.CHAPTERS.getChapter(id);
        if (chapter == null) {
            chapter = PonderRegistry.CHAPTERS.addChapter(new PonderChapter(id));
        }
        return chapter;
    }
}