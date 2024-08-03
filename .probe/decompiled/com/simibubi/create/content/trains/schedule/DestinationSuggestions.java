package com.simibubi.create.content.trains.schedule;

import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.suggestion.Suggestion;
import com.simibubi.create.foundation.utility.IntAttached;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.Mth;

public class DestinationSuggestions extends CommandSuggestions {

    private EditBox textBox;

    private List<IntAttached<String>> viableStations;

    private String previous = "<>";

    private Font font;

    private boolean active;

    List<Suggestion> currentSuggestions;

    private int yOffset;

    public DestinationSuggestions(Minecraft pMinecraft, Screen pScreen, EditBox pInput, Font pFont, List<IntAttached<String>> viableStations, int yOffset) {
        super(pMinecraft, pScreen, pInput, pFont, true, true, 0, 7, false, -298831824);
        this.textBox = pInput;
        this.font = pFont;
        this.viableStations = viableStations;
        this.yOffset = yOffset;
        this.currentSuggestions = new ArrayList();
        this.active = false;
    }

    public void tick() {
        if (this.f_93866_ == null) {
            this.textBox.setSuggestion("");
        }
        if (this.active != this.textBox.m_93696_()) {
            this.active = this.textBox.m_93696_();
            this.updateCommandInfo();
        }
    }

    @Override
    public void updateCommandInfo() {
        String value = this.textBox.getValue();
        if (!value.equals(this.previous)) {
            if (!this.active) {
                this.f_93866_ = null;
            } else {
                this.previous = value;
                this.currentSuggestions = this.viableStations.stream().filter(ia -> !((String) ia.getValue()).equals(value) && ((String) ia.getValue()).toLowerCase().startsWith(value.toLowerCase())).sorted((ia1, ia2) -> Integer.compare(ia1.getFirst(), ia2.getFirst())).map(IntAttached::getValue).map(s -> new Suggestion(new StringRange(0, s.length()), s)).toList();
                this.showSuggestions(false);
            }
        }
    }

    @Override
    public void showSuggestions(boolean pNarrateFirstSuggestion) {
        if (this.currentSuggestions.isEmpty()) {
            this.f_93866_ = null;
        } else {
            int width = 0;
            for (Suggestion suggestion : this.currentSuggestions) {
                width = Math.max(width, this.font.width(suggestion.getText()));
            }
            int x = Mth.clamp(this.textBox.getScreenX(0), 0, this.textBox.getScreenX(0) + this.textBox.getInnerWidth() - width);
            this.f_93866_ = new CommandSuggestions.SuggestionsList(x, 72 + this.yOffset, width, this.currentSuggestions, false);
        }
    }
}