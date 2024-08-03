package com.almostreliable.summoningrituals.compat.viewer.common;

import com.almostreliable.summoningrituals.Registration;
import com.almostreliable.summoningrituals.recipe.AltarRecipe;
import com.almostreliable.summoningrituals.recipe.component.IngredientStack;
import com.almostreliable.summoningrituals.recipe.component.RecipeOutputs;
import com.almostreliable.summoningrituals.recipe.component.RecipeSacrifices;
import com.almostreliable.summoningrituals.util.GameUtils;
import com.almostreliable.summoningrituals.util.MathUtils;
import com.almostreliable.summoningrituals.util.TextUtils;
import com.almostreliable.summoningrituals.util.Utils;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;

public class AltarCategory<I, R> {

    protected static final ResourceLocation TEXTURE = Utils.getRL(TextUtils.f("textures/{}/{}.png", "recipe_viewer", "altar"));

    protected static final int TEXTURE_WIDTH = 188;

    protected static final int TEXTURE_HEIGHT = 148;

    protected static final int ITEM_SLOT_SIZE = 18;

    protected static final int ITEM_SIZE = 16;

    protected static final int BLOCK_SLOT_SIZE = 22;

    protected static final int BLOCK_SIZE = 20;

    protected static final int SPRITE_SLOT_SIZE = 16;

    protected static final int CENTER_X = 87;

    protected static final int RENDER_Y = 64;

    private static final int INPUT_RADIUS = 47;

    protected final ItemStack altar;

    private final I logo;

    protected final R altarRenderer;

    protected final R catalystRenderer;

    protected final List<AltarCategory.SpriteWidget> conditionSpriteWidgets = List.of(new AltarCategory.SpriteWidget(0, altarRecipe -> altarRecipe.getDayTime() == AltarRecipe.DAY_TIME.DAY), new AltarCategory.SpriteWidget(1, altarRecipe -> altarRecipe.getDayTime() == AltarRecipe.DAY_TIME.NIGHT), new AltarCategory.SpriteWidget(2, altarRecipe -> altarRecipe.getWeather() == AltarRecipe.WEATHER.CLEAR), new AltarCategory.SpriteWidget(3, altarRecipe -> altarRecipe.getWeather() == AltarRecipe.WEATHER.RAIN), new AltarCategory.SpriteWidget(4, altarRecipe -> altarRecipe.getWeather() == AltarRecipe.WEATHER.THUNDER));

    protected AltarCategory(I logo, R altarRenderer, R catalystRenderer) {
        this.altar = Registration.ALTAR_ITEM.get().getDefaultInstance();
        this.logo = logo;
        this.altarRenderer = altarRenderer;
        this.catalystRenderer = catalystRenderer;
    }

    public I getIcon() {
        return this.logo;
    }

    public Component getTitle() {
        return TextUtils.translate("block", "altar");
    }

    protected List<Component> getTooltip(AltarRecipe recipe, int x, int y, double mX, double mY) {
        List<Component> tooltip = new ArrayList();
        if (!recipe.getSacrifices().isEmpty() && MathUtils.isWithinBounds(mX, mY, x + 1, y + 1, 30, 20)) {
            tooltip.add(TextUtils.translate("tooltip", "region", ChatFormatting.WHITE));
        }
        if (this.isSpriteHovered(mX, mY, x, y + 1)) {
            if (recipe.getDayTime() != AltarRecipe.DAY_TIME.ANY) {
                tooltip.add(this.createConditionTooltip("day_time", recipe.getDayTime().name()));
            } else if (recipe.getWeather() != AltarRecipe.WEATHER.ANY) {
                tooltip.add(this.createConditionTooltip("weather", recipe.getWeather().name()));
            }
        }
        if (this.isSpriteHovered(mX, mY, x, y + 16 + 2) && recipe.getDayTime() != AltarRecipe.DAY_TIME.ANY && recipe.getWeather() != AltarRecipe.WEATHER.ANY) {
            tooltip.add(this.createConditionTooltip("weather", recipe.getWeather().name()));
        }
        return tooltip;
    }

    private Component createConditionTooltip(String translationKey, String value) {
        return TextUtils.translate("tooltip", translationKey, ChatFormatting.AQUA).append(": ").append(TextUtils.translate(translationKey, value.toLowerCase(), ChatFormatting.WHITE));
    }

    private boolean isSpriteHovered(double mX, double mY, int x, int y) {
        return MathUtils.isWithinBounds(mX, mY, x + 188 - 32 - 1, y, 16, 16);
    }

    protected void drawLabel(GuiGraphics guiGraphics, String text, GameUtils.ANCHOR anchor, int x, int y, int color) {
        GameUtils.renderText(guiGraphics, text, anchor, x, y, 1.0F, color);
    }

    protected static void handleInputs(int offsetX, int offsetY, AltarRecipe recipe, AltarCategory.ItemInputConsumer itemConsumer, AltarCategory.MobInputConsumer mobConsumer) {
        NonNullList<IngredientStack> itemInputs = recipe.getInputs();
        RecipeSacrifices mobInputs = recipe.getSacrifices();
        int inputSlots = itemInputs.size() + mobInputs.size();
        for (int i = 0; i < inputSlots; i++) {
            int x = offsetX + 87 + (int) (Math.cos((double) (i * 2) * Math.PI / (double) inputSlots) * 47.0) - 9;
            int y = offsetY + 64 + (int) (Math.sin((double) (i * 2) * Math.PI / (double) inputSlots) * 47.0) - 9;
            if (i >= itemInputs.size()) {
                RecipeSacrifices.Sacrifice mobInput = mobInputs.get(i - itemInputs.size());
                MobIngredient mobIngredient = new MobIngredient(mobInput.mob(), mobInput.count());
                SpawnEggItem egg = mobIngredient.getEgg();
                mobConsumer.accept(x, y, mobIngredient, egg);
            } else {
                List<ItemStack> inputStacks = new ArrayList();
                for (ItemStack stack : itemInputs.get(i).ingredient().getItems()) {
                    stack.setCount(itemInputs.get(i).count());
                    inputStacks.add(stack);
                }
                itemConsumer.accept(x, y, inputStacks);
            }
        }
    }

    protected static void handleOutputs(int offsetX, int offsetY, AltarRecipe recipe, AltarCategory.ItemOutputConsumer itemConsumer, AltarCategory.MobOutputConsumer mobConsumer) {
        recipe.getOutputs().forEach((type, output, i) -> {
            int x = offsetX + 2 + i * 17;
            int y = offsetY + 130;
            if (type == RecipeOutputs.OutputType.ITEM) {
                itemConsumer.accept(x, y, (ItemStack) output.getOutput());
            } else if (type == RecipeOutputs.OutputType.MOB) {
                MobIngredient entityIngredient = new MobIngredient((EntityType<?>) output.getOutput(), output.getCount(), output.getData());
                SpawnEggItem egg = entityIngredient.getEgg();
                mobConsumer.accept(x, y, entityIngredient, egg);
            }
        });
    }

    @FunctionalInterface
    protected interface ItemInputConsumer {

        void accept(int var1, int var2, List<ItemStack> var3);
    }

    @FunctionalInterface
    protected interface ItemOutputConsumer {

        void accept(int var1, int var2, ItemStack var3);
    }

    @FunctionalInterface
    protected interface MobInputConsumer {

        void accept(int var1, int var2, MobIngredient var3, @Nullable SpawnEggItem var4);
    }

    @FunctionalInterface
    protected interface MobOutputConsumer {

        void accept(int var1, int var2, MobIngredient var3, @Nullable SpawnEggItem var4);
    }

    public static final class SpriteWidget implements Renderable, Predicate<AltarRecipe> {

        private static final int SPRITE_SIZE = 14;

        private final int offset;

        private final Predicate<AltarRecipe> renderPredicate;

        private SpriteWidget(int offset, Predicate<AltarRecipe> renderPredicate) {
            this.offset = offset;
            this.renderPredicate = renderPredicate;
        }

        public void render(GuiGraphics guiGraphics, int x, int y) {
            PoseStack stack = guiGraphics.pose();
            stack.pushPose();
            stack.translate((float) (x + 188 - 32 - 1), (float) y, 0.0F);
            this.render(guiGraphics, 0, 0, 0.0F);
            stack.popPose();
        }

        @Override
        public void render(GuiGraphics guiGraphics, int mX, int mY, float partial) {
            guiGraphics.blit(AltarCategory.TEXTURE, 0, 0, 172.0F, 0.0F, 16, 16, 188, 148);
            guiGraphics.blit(AltarCategory.TEXTURE, 1, 1, 172.0F, (float) (16 + this.offset * 14), 14, 14, 188, 148);
        }

        public boolean test(AltarRecipe altarRecipe) {
            return this.renderPredicate.test(altarRecipe);
        }
    }
}