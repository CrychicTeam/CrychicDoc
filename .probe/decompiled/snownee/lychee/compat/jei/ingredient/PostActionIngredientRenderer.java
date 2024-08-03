package snownee.lychee.compat.jei.ingredient;

import java.util.List;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import snownee.lychee.client.core.post.PostActionRenderer;
import snownee.lychee.core.post.PostAction;

public enum PostActionIngredientRenderer implements IIngredientRenderer<PostAction> {

    INSTANCE;

    public List<Component> getTooltip(PostAction action, TooltipFlag flag) {
        return PostActionRenderer.<PostAction>of(action).getTooltips(action);
    }

    public void render(GuiGraphics graphics, PostAction action) {
        PostActionRenderer.<PostAction>of(action).render(action, graphics, 0, 0);
    }
}