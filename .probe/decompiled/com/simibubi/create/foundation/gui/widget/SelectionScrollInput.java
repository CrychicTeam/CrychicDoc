package com.simibubi.create.foundation.gui.widget;

import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class SelectionScrollInput extends ScrollInput {

    private final MutableComponent scrollToSelect = Lang.translateDirect("gui.scrollInput.scrollToSelect");

    protected List<? extends Component> options = new ArrayList();

    public SelectionScrollInput(int xIn, int yIn, int widthIn, int heightIn) {
        super(xIn, yIn, widthIn, heightIn);
        this.inverted();
    }

    public ScrollInput forOptions(List<? extends Component> options) {
        this.options = options;
        this.max = options.size();
        this.format(options::get);
        this.updateTooltip();
        return this;
    }

    @Override
    protected void updateTooltip() {
        this.toolTip.clear();
        if (this.title != null) {
            this.toolTip.add(this.title.plainCopy().withStyle(s -> s.withColor(5476833)));
            int min = Math.min(this.max - 16, this.state - 7);
            int max = Math.max(this.min + 16, this.state + 8);
            min = Math.max(min, this.min);
            max = Math.min(max, this.max);
            if (this.min + 1 == min) {
                min--;
            }
            if (min > this.min) {
                this.toolTip.add(Components.literal("> ...").withStyle(ChatFormatting.GRAY));
            }
            if (this.max - 1 == max) {
                max++;
            }
            for (int i = min; i < max; i++) {
                if (i == this.state) {
                    this.toolTip.add(Components.empty().append("-> ").append((Component) this.options.get(i)).withStyle(ChatFormatting.WHITE));
                } else {
                    this.toolTip.add(Components.empty().append("> ").append((Component) this.options.get(i)).withStyle(ChatFormatting.GRAY));
                }
            }
            if (max < this.max) {
                this.toolTip.add(Components.literal("> ...").withStyle(ChatFormatting.GRAY));
            }
            if (this.hint != null) {
                this.toolTip.add(this.hint.plainCopy().withStyle(s -> s.withColor(9877472)));
            }
            this.toolTip.add(this.scrollToSelect.m_6879_().withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
        }
    }
}