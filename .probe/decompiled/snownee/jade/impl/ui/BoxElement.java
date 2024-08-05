package snownee.jade.impl.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.ui.Element;
import snownee.jade.api.ui.IBoxElement;
import snownee.jade.api.ui.IBoxStyle;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.ITooltipRenderer;
import snownee.jade.impl.Tooltip;
import snownee.jade.overlay.TooltipRenderer;

public class BoxElement extends Element implements IBoxElement {

    private final TooltipRenderer tooltip;

    private final IBoxStyle box;

    public BoxElement(Tooltip tooltip, IBoxStyle box) {
        this.tooltip = new TooltipRenderer(tooltip, false);
        this.tooltip.recalculateSize();
        this.box = box;
    }

    @Override
    public Vec2 getSize() {
        if (this.tooltip.getTooltip().isEmpty()) {
            return Vec2.ZERO;
        } else {
            Vec2 size = this.tooltip.getSize();
            return new Vec2(size.x, size.y + 1.0F);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, float x, float y, float maxX, float maxY) {
        if (!this.tooltip.getTooltip().isEmpty()) {
            RenderSystem.enableBlend();
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(x, y, 0.0F);
            this.box.render(guiGraphics, 0.0F, 0.0F, maxX - x, maxY - y - 2.0F);
            this.tooltip.setSize(new Vec2(maxX - x, this.tooltip.getSize().y));
            this.tooltip.draw(guiGraphics);
            guiGraphics.pose().popPose();
        }
    }

    @Override
    public IElement tag(ResourceLocation tag) {
        this.box.tag(tag);
        return super.tag(tag);
    }

    @Nullable
    @Override
    public String getMessage() {
        return this.tooltip.getTooltip().isEmpty() ? null : this.tooltip.getTooltip().getMessage();
    }

    @Override
    public ITooltipRenderer getTooltipRenderer() {
        return this.tooltip;
    }
}