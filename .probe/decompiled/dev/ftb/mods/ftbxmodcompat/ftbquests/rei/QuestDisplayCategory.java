package dev.ftb.mods.ftbxmodcompat.ftbquests.rei;

import com.google.common.collect.Lists;
import dev.ftb.mods.ftbquests.item.FTBQuestsItems;
import java.util.List;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class QuestDisplayCategory implements DisplayCategory<QuestDisplay> {

    public CategoryIdentifier<? extends QuestDisplay> getCategoryIdentifier() {
        return REICategories.QUEST;
    }

    public Component getTitle() {
        return Component.translatable("ftbquests.quests");
    }

    public Renderer getIcon() {
        return EntryStack.of(VanillaEntryTypes.ITEM, new ItemStack((ItemLike) FTBQuestsItems.BOOK.get()));
    }

    public List<Widget> setupDisplay(QuestDisplay display, Rectangle bounds) {
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));
        int inputSize = Math.min(9, display.getInputEntries().size());
        for (int i = 0; i < 9; i++) {
            EntryIngredient ingredient = i < inputSize ? (EntryIngredient) display.getInputEntries().get(i) : EntryIngredient.empty();
            widgets.add(Widgets.createSlot(new Point(bounds.x + i % 3 * 18 + 8, bounds.y + i / 3 * 18 + 20)).entries(ingredient).markInput());
        }
        int outputSize = Math.min(9, display.getOutputEntries().size());
        for (int i = 0; i < 9; i++) {
            EntryIngredient ingredient = i < outputSize ? (EntryIngredient) display.getOutputEntries().get(i) : EntryIngredient.empty();
            widgets.add(Widgets.createSlot(new Point(bounds.x + i % 3 * 18 + 90, bounds.y + i / 3 * 18 + 20)).entries(ingredient).markOutput());
        }
        widgets.add(Widgets.createArrow(new Point(bounds.x + 60, bounds.y + 40)));
        Component text = display.getWrappedQuest().quest.getTitle().copy().withStyle(ChatFormatting.UNDERLINE);
        float x = (float) bounds.x + (float) bounds.width / 2.0F;
        float y = (float) bounds.y + 5.0F;
        widgets.add(Widgets.createClickableLabel(new Point((double) x, (double) y), text, c -> {
            Minecraft.getInstance().setScreen(null);
            display.getWrappedQuest().openQuestGui();
        }));
        return widgets;
    }

    public int getDisplayHeight() {
        return 80;
    }
}