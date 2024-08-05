package snownee.lychee.compat.rei.ingredient;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.entry.renderer.EntryRenderer;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.api.client.gui.widgets.TooltipContext;
import me.shedaniel.rei.api.common.entry.EntryStack;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.Nullable;
import snownee.lychee.client.core.post.PostActionRenderer;
import snownee.lychee.core.post.PostAction;

public enum PostActionIngredientRenderer implements EntryRenderer<PostAction> {

    INSTANCE;

    public void render(EntryStack<PostAction> entry, GuiGraphics graphics, Rectangle bounds, int mx, int my, float delta) {
        if (!entry.isEmpty()) {
            PostActionRenderer.of((PostAction) entry.getValue()).render((PostAction) entry.getValue(), graphics, bounds.x, bounds.y);
        }
    }

    @Nullable
    public Tooltip getTooltip(EntryStack<PostAction> entry, TooltipContext context) {
        return Tooltip.create(PostActionRenderer.of((PostAction) entry.getValue()).getTooltips((PostAction) entry.getValue()));
    }
}