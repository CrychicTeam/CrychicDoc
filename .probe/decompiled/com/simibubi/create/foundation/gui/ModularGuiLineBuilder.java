package com.simibubi.create.foundation.gui;

import com.simibubi.create.foundation.gui.widget.Label;
import com.simibubi.create.foundation.gui.widget.ScrollInput;
import com.simibubi.create.foundation.gui.widget.SelectionScrollInput;
import com.simibubi.create.foundation.gui.widget.TooltipArea;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Pair;
import java.util.function.BiConsumer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;

public class ModularGuiLineBuilder {

    private ModularGuiLine target;

    private Font font;

    private int x;

    private int y;

    public ModularGuiLineBuilder(Font font, ModularGuiLine target, int x, int y) {
        this.font = font;
        this.target = target;
        this.x = x;
        this.y = y;
    }

    public ModularGuiLineBuilder addScrollInput(int x, int width, BiConsumer<ScrollInput, Label> inputTransform, String dataKey) {
        ScrollInput input = new ScrollInput(x + this.x, this.y - 4, width, 18);
        this.addScrollInput(input, inputTransform, dataKey);
        return this;
    }

    public ModularGuiLineBuilder addSelectionScrollInput(int x, int width, BiConsumer<SelectionScrollInput, Label> inputTransform, String dataKey) {
        SelectionScrollInput input = new SelectionScrollInput(x + this.x, this.y - 4, width, 18);
        this.addScrollInput(input, inputTransform, dataKey);
        return this;
    }

    public ModularGuiLineBuilder customArea(int x, int width) {
        this.target.customBoxes.add(Couple.create(x, width));
        return this;
    }

    public ModularGuiLineBuilder speechBubble() {
        this.target.speechBubble = true;
        return this;
    }

    private <T extends ScrollInput> void addScrollInput(T input, BiConsumer<T, Label> inputTransform, String dataKey) {
        Label label = new Label(input.m_252754_() + 5, this.y, Components.immutableEmpty());
        label.withShadow();
        inputTransform.accept(input, label);
        input.writingTo(label);
        this.target.add(Pair.of(label, "Dummy"));
        this.target.add(Pair.of(input, dataKey));
    }

    public ModularGuiLineBuilder addIntegerTextInput(int x, int width, BiConsumer<EditBox, TooltipArea> inputTransform, String dataKey) {
        return this.addTextInput(x, width, inputTransform.andThen((editBox, $) -> editBox.setFilter(s -> {
            if (s.isEmpty()) {
                return true;
            } else {
                try {
                    Integer.parseInt(s);
                    return true;
                } catch (NumberFormatException var2x) {
                    return false;
                }
            }
        })), dataKey);
    }

    public ModularGuiLineBuilder addTextInput(int x, int width, BiConsumer<EditBox, TooltipArea> inputTransform, String dataKey) {
        EditBox input = new EditBox(this.font, x + this.x + 5, this.y, width - 9, 8, Components.immutableEmpty());
        input.setBordered(false);
        input.setTextColor(16777215);
        input.setFocused(false);
        input.m_6375_(0.0, 0.0, 0);
        TooltipArea tooltipArea = new TooltipArea(this.x + x, this.y - 4, width, 18);
        inputTransform.accept(input, tooltipArea);
        this.target.add(Pair.of(input, dataKey));
        this.target.add(Pair.of(tooltipArea, "Dummy"));
        return this;
    }
}