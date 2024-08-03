package com.almostreliable.summoningrituals.compat.viewer.rei;

import com.almostreliable.summoningrituals.Registration;
import com.almostreliable.summoningrituals.compat.viewer.common.AltarCategory;
import com.almostreliable.summoningrituals.compat.viewer.rei.ingredient.item.REIAltarRenderer;
import com.almostreliable.summoningrituals.compat.viewer.rei.ingredient.item.REICatalystRenderer;
import com.almostreliable.summoningrituals.recipe.AltarRecipe;
import com.almostreliable.summoningrituals.util.GameUtils;
import com.almostreliable.summoningrituals.util.TextUtils;
import com.almostreliable.summoningrituals.util.Utils;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.entry.renderer.EntryRenderer;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.client.util.ClientEntryStacks;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class AltarCategoryREI extends AltarCategory<Renderer, EntryRenderer<ItemStack>> implements DisplayCategory<AltarCategoryREI.AltarDisplay> {

    static final CategoryIdentifier<AltarCategoryREI.AltarDisplay> ID = CategoryIdentifier.of(Utils.getRL("altar"));

    private static final int BOUNDS_PADDING = 8;

    AltarCategoryREI() {
        super(EntryStacks.of(Registration.ALTAR_ITEM.get()), new REIAltarRenderer(20), new REICatalystRenderer(16));
    }

    public CategoryIdentifier<? extends AltarCategoryREI.AltarDisplay> getCategoryIdentifier() {
        return ID;
    }

    public List<Widget> setupDisplay(AltarCategoryREI.AltarDisplay display, Rectangle bounds) {
        ArrayList<Widget> widgets = new ArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));
        int offsetX = bounds.x + 4;
        int offsetY = bounds.y + 4;
        widgets.add(Widgets.createTexturedWidget(TEXTURE, offsetX, offsetY, 0.0F, 0.0F, 172, 148, 188, 148));
        AltarRecipe recipe = display.recipe;
        if (recipe.getBlockBelow() != null) {
            int blockBelowX = offsetX + 87 - 11;
            int blockBelowY = offsetY + 64 - 3;
            widgets.add(Widgets.createSlot(new Rectangle(blockBelowX, blockBelowY, 20, 20)).entry(EntryStack.of(AlmostREI.BLOCK_REFERENCE, recipe.getBlockBelow())).markInput().disableBackground());
        }
        ClientEntryStacks.setRenderer(EntryStacks.of(this.altar), this.altarRenderer);
        int altarX = offsetX + 87 - 11;
        int altarY = offsetY + 64 - 11 - (recipe.getBlockBelow() == null ? 0 : 4);
        widgets.add(Widgets.createSlot(new Rectangle(altarX, altarY, 20, 20)).entry(EntryStacks.of(this.altar)).disableBackground().disableHighlight().disableTooltips());
        widgets.add(this.labelWidget(TextUtils.f("{}:", TextUtils.translateAsString("label", "outputs")), GameUtils.ANCHOR.BOTTOM_LEFT, offsetX + 2, offsetY + 128, 3580928));
        if (!recipe.getSacrifices().isEmpty()) {
            widgets.add(this.labelWidget(TextUtils.f("{}:", TextUtils.translateAsString("label", "region")), GameUtils.ANCHOR.TOP_LEFT, offsetX + 1, offsetY + 1, 41727));
            widgets.add(this.labelWidget(recipe.getSacrifices().getDisplayRegion(), GameUtils.ANCHOR.TOP_LEFT, offsetX + 1, offsetY + 11, 16777215));
        }
        List<AltarCategory.SpriteWidget> sprites = this.conditionSpriteWidgets.stream().filter(s -> s.test(recipe)).toList();
        int spriteOffset = 0;
        for (AltarCategory.SpriteWidget sprite : sprites) {
            int spriteOffsetF = spriteOffset;
            widgets.add(Widgets.createDrawableWidget((guiGraphics, mX, mY, partial) -> sprite.render(guiGraphics, offsetX, offsetY + spriteOffsetF)));
            spriteOffset += 17;
        }
        EntryIngredient catalystEntry = EntryIngredients.ofIngredient(recipe.getCatalyst());
        catalystEntry.forEach(e -> ClientEntryStacks.setRenderer(e, this.catalystRenderer));
        int catalystX = offsetX + 87 - 9;
        int catalystY = offsetY + 64 - 32;
        widgets.add(Widgets.createSlot(new Point(catalystX, catalystY)).entries(catalystEntry).markInput().disableBackground());
        handleInputs(offsetX, offsetY, recipe, (x, y, inputs) -> widgets.add(Widgets.createSlot(new Point(x, y)).entries(inputs.stream().map(EntryStacks::of).toList()).markInput().disableBackground()), (x, y, mob, egg) -> widgets.add(Widgets.createSlot(new Point(x, y)).entry(EntryStack.of(AlmostREI.MOB, mob)).markInput().disableBackground()));
        handleOutputs(offsetX, offsetY, recipe, (x, y, output) -> widgets.add(Widgets.createSlot(new Point(x, y)).entry(EntryStacks.of(output)).markOutput().disableBackground()), (x, y, mob, egg) -> widgets.add(Widgets.createSlot(new Point(x, y)).entry(EntryStack.of(AlmostREI.MOB, mob)).markOutput().disableBackground()));
        widgets.add(Widgets.createDrawableWidget((guiGraphics, mX, mY, partial) -> {
            List<Component> tooltip = this.getTooltip(recipe, offsetX, offsetY, (double) mX, (double) mY);
            if (!tooltip.isEmpty()) {
                Tooltip.create(tooltip).queue();
            }
        }));
        return widgets;
    }

    public int getDisplayHeight() {
        return 156;
    }

    public int getDisplayWidth(AltarCategoryREI.AltarDisplay display) {
        return 180;
    }

    private Widget labelWidget(String text, GameUtils.ANCHOR anchor, int x, int y, int color) {
        return Widgets.createDrawableWidget((guiGraphics, mX, mY, partial) -> this.drawLabel(guiGraphics, text, anchor, x, y, color));
    }

    public static class AltarDisplay implements Display {

        private final AltarRecipe recipe;

        private final List<EntryIngredient> inputs;

        private final List<EntryIngredient> outputs;

        AltarDisplay(AltarRecipe recipe) {
            this.recipe = recipe;
            this.inputs = createInputs(recipe);
            this.outputs = createOutputs(recipe);
        }

        private static List<EntryIngredient> createInputs(AltarRecipe recipe) {
            List<EntryIngredient> inputIngredients = new ArrayList();
            AltarCategoryREI.handleInputs(0, 0, recipe, (x, y, inputs) -> inputIngredients.add(EntryIngredients.ofItemStacks(inputs)), (x, y, mob, egg) -> {
                inputIngredients.add(EntryIngredient.of(EntryStack.of(AlmostREI.MOB, mob)));
                if (egg != null) {
                    inputIngredients.add(EntryIngredients.of(egg));
                }
            });
            inputIngredients.add(EntryIngredients.ofIngredient(recipe.getCatalyst()));
            return ImmutableList.copyOf(inputIngredients);
        }

        private static List<EntryIngredient> createOutputs(AltarRecipe recipe) {
            List<EntryIngredient> outputIngredients = new ArrayList();
            AltarCategoryREI.handleOutputs(0, 0, recipe, (x, y, output) -> outputIngredients.add(EntryIngredients.of(output)), (x, y, mob, egg) -> {
                outputIngredients.add(EntryIngredient.of(EntryStack.of(AlmostREI.MOB, mob)));
                if (egg != null) {
                    outputIngredients.add(EntryIngredients.of(egg));
                }
            });
            return ImmutableList.copyOf(outputIngredients);
        }

        public CategoryIdentifier<?> getCategoryIdentifier() {
            return AltarCategoryREI.ID;
        }

        public Optional<ResourceLocation> getDisplayLocation() {
            return Optional.of(this.recipe.getId());
        }

        public List<EntryIngredient> getInputEntries() {
            return this.inputs;
        }

        public List<EntryIngredient> getOutputEntries() {
            return this.outputs;
        }
    }
}