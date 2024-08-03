package net.minecraft.client.gui.components.toasts;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

public class RecipeToast implements Toast {

    private static final long DISPLAY_TIME = 5000L;

    private static final Component TITLE_TEXT = Component.translatable("recipe.toast.title");

    private static final Component DESCRIPTION_TEXT = Component.translatable("recipe.toast.description");

    private final List<Recipe<?>> recipes = Lists.newArrayList();

    private long lastChanged;

    private boolean changed;

    public RecipeToast(Recipe<?> recipe0) {
        this.recipes.add(recipe0);
    }

    @Override
    public Toast.Visibility render(GuiGraphics guiGraphics0, ToastComponent toastComponent1, long long2) {
        if (this.changed) {
            this.lastChanged = long2;
            this.changed = false;
        }
        if (this.recipes.isEmpty()) {
            return Toast.Visibility.HIDE;
        } else {
            guiGraphics0.blit(f_94893_, 0, 0, 0, 32, this.m_7828_(), this.m_94899_());
            guiGraphics0.drawString(toastComponent1.getMinecraft().font, TITLE_TEXT, 30, 7, -11534256, false);
            guiGraphics0.drawString(toastComponent1.getMinecraft().font, DESCRIPTION_TEXT, 30, 18, -16777216, false);
            Recipe<?> $$3 = (Recipe<?>) this.recipes.get((int) ((double) long2 / Math.max(1.0, 5000.0 * toastComponent1.getNotificationDisplayTimeMultiplier() / (double) this.recipes.size()) % (double) this.recipes.size()));
            ItemStack $$4 = $$3.getToastSymbol();
            guiGraphics0.pose().pushPose();
            guiGraphics0.pose().scale(0.6F, 0.6F, 1.0F);
            guiGraphics0.renderFakeItem($$4, 3, 3);
            guiGraphics0.pose().popPose();
            guiGraphics0.renderFakeItem($$3.getResultItem(toastComponent1.getMinecraft().level.m_9598_()), 8, 8);
            return (double) (long2 - this.lastChanged) >= 5000.0 * toastComponent1.getNotificationDisplayTimeMultiplier() ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
        }
    }

    private void addItem(Recipe<?> recipe0) {
        this.recipes.add(recipe0);
        this.changed = true;
    }

    public static void addOrUpdate(ToastComponent toastComponent0, Recipe<?> recipe1) {
        RecipeToast $$2 = toastComponent0.getToast(RecipeToast.class, f_94894_);
        if ($$2 == null) {
            toastComponent0.addToast(new RecipeToast(recipe1));
        } else {
            $$2.addItem(recipe1);
        }
    }
}