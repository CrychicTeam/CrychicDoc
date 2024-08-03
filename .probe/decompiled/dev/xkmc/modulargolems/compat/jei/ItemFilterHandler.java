package dev.xkmc.modulargolems.compat.jei;

import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.modulargolems.content.menu.filter.ItemConfigScreen;
import dev.xkmc.modulargolems.content.menu.ghost.ItemTarget;
import java.util.List;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import mezz.jei.api.ingredients.ITypedIngredient;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.item.ItemStack;

public class ItemFilterHandler implements IGhostIngredientHandler<ItemConfigScreen> {

    public <I> List<IGhostIngredientHandler.Target<I>> getTargetsTyped(ItemConfigScreen gui, ITypedIngredient<I> ingredient, boolean doStart) {
        return ingredient.getIngredient() instanceof ItemStack ? (List) Wrappers.cast(gui.getTargets().stream().map(ItemFilterHandler.RTarget::new).toList()) : List.of();
    }

    @Override
    public void onComplete() {
    }

    public static record RTarget(ItemTarget target) implements IGhostIngredientHandler.Target<ItemStack> {

        @Override
        public Rect2i getArea() {
            return new Rect2i(this.target.x(), this.target.y(), this.target.w(), this.target.h());
        }

        public void accept(ItemStack ingredient) {
            this.target.con().accept(ingredient);
        }
    }
}