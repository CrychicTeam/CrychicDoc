package fr.frinn.custommachinery.client.integration.jei.experience;

import fr.frinn.custommachinery.api.integration.jei.JEIIngredientRenderer;
import fr.frinn.custommachinery.common.guielement.ExperienceGuiElement;
import fr.frinn.custommachinery.common.util.Color;
import fr.frinn.custommachinery.common.util.ExperienceUtils;
import fr.frinn.custommachinery.common.util.Utils;
import fr.frinn.custommachinery.impl.integration.jei.CustomIngredientTypes;
import fr.frinn.custommachinery.impl.integration.jei.Experience;
import java.util.ArrayList;
import java.util.List;
import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

public class ExperienceJEIIngredientRenderer extends JEIIngredientRenderer<Experience, ExperienceGuiElement> {

    public ExperienceJEIIngredientRenderer(ExperienceGuiElement element) {
        super(element);
    }

    @Override
    public IIngredientType<Experience> getType() {
        return CustomIngredientTypes.EXPERIENCE;
    }

    @Override
    public int getWidth() {
        return this.element.getWidth() - 2;
    }

    @Override
    public int getHeight() {
        return this.element.getHeight() - 2;
    }

    public void render(GuiGraphics graphics, @Nullable Experience ingredient) {
        int width = this.getWidth();
        int height = this.getHeight();
        if (this.element.getMode().isDisplayBar()) {
            String levels = (ingredient != null ? (ingredient.isLevels() ? ingredient.getXp() : ExperienceUtils.getLevelFromXp((long) ingredient.getXp())) : 0) + "";
            int xPos = width / 2 - Minecraft.getInstance().font.width(levels) / 2;
            graphics.drawString(Minecraft.getInstance().font, levels, xPos, 0, 8453920, true);
            graphics.fill(0, height - 3, width, height, -16777216);
            if (ingredient != null && ingredient.isPoints()) {
                int level = ExperienceUtils.getLevelFromXp((long) ingredient.getXp());
                int xpDiff = ingredient.getXp() - ExperienceUtils.getXpFromLevel(level);
                if (xpDiff > 0) {
                    double percent = (double) xpDiff / (double) ExperienceUtils.getXpNeededForNextLevel(level);
                    graphics.fill(1, height - 2, 1 + Math.max((int) Math.ceil((double) width * percent) - 2, 0), height - 1, -8323296);
                }
            }
        } else {
            graphics.fill(this.element.getX(), this.element.getY(), this.element.getX() + width, this.element.getY() + height, Color.fromColors(1, 7, 186, 7).getARGB());
        }
    }

    public List<Component> getTooltip(Experience ingredient, TooltipFlag iTooltipFlag) {
        List<Component> tooltips = new ArrayList();
        String amount = Utils.format(ingredient.getXp());
        if (ingredient.isPoints()) {
            if (ingredient.isPerTick()) {
                tooltips.add(Component.translatable("custommachinery.jei.ingredient.xp.point.pertick", amount));
            } else {
                tooltips.add(Component.translatable("custommachinery.jei.ingredient.xp.point", amount));
            }
        } else if (ingredient.isLevels()) {
            if (ingredient.isPerTick()) {
                tooltips.add(Component.translatable("custommachinery.jei.ingredient.xp.level.pertick", amount));
            } else {
                tooltips.add(Component.translatable("custommachinery.jei.ingredient.xp.level", amount));
            }
        }
        if (ingredient.getChance() == 0.0) {
            tooltips.add(Component.translatable("custommachinery.jei.ingredient.chance.0").withStyle(ChatFormatting.DARK_RED));
        }
        if (ingredient.getChance() < 1.0 && ingredient.getChance() > 0.0) {
            tooltips.add(Component.translatable("custommachinery.jei.ingredient.chance", (int) (ingredient.getChance() * 100.0)));
        }
        return tooltips;
    }
}