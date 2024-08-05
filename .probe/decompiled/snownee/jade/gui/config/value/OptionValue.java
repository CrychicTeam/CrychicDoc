package snownee.jade.gui.config.value;

import com.google.common.base.Strings;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import snownee.jade.gui.config.OptionsList;

public abstract class OptionValue<T> extends OptionsList.Entry {

    private static final Component SERVER_FEATURE = Component.literal("*").withStyle(ChatFormatting.GRAY);

    protected final Consumer<T> setter;

    private final Component title;

    public boolean serverFeature;

    protected T value;

    protected int indent;

    private int x;

    public OptionValue(String optionName, Consumer<T> setter) {
        this.title = makeTitle(optionName);
        this.setter = setter;
        this.addMessage(this.title.getString());
        this.addMessageKey(optionName);
        String key = makeKey(optionName + "_desc");
        if (I18n.exists(key)) {
            this.appendDescription(I18n.get(key));
        }
    }

    @Override
    public final void render(GuiGraphics guiGraphics, int index, int rowTop, int rowLeft, int width, int height, int mouseX, int mouseY, boolean hovered, float deltaTime) {
        AbstractWidget widget = this.getFirstWidget();
        Component title0 = (Component) (widget.active ? this.title : this.title.copy().withStyle(ChatFormatting.STRIKETHROUGH, ChatFormatting.GRAY));
        int left = rowLeft + this.indent + 10;
        int top = rowTop + height / 2 - 9 / 2;
        guiGraphics.drawString(this.client.font, title0, left, top, 16777215);
        if (this.serverFeature) {
            guiGraphics.drawString(this.client.font, SERVER_FEATURE, left + this.getTextWidth() + 1, top, 16777215);
        }
        super.render(guiGraphics, index, rowTop, rowLeft, width, height, mouseX, mouseY, hovered, deltaTime);
        this.x = rowLeft;
    }

    public void save() {
        this.setter.accept(this.value);
    }

    public Component getTitle() {
        return this.title;
    }

    public void appendDescription(String description) {
        if (this.description == null) {
            this.description = description;
        } else {
            this.description = this.description + "\n" + description;
        }
        this.addMessage(description);
    }

    public int getX() {
        return this.x;
    }

    @Override
    public int getTextX(int width) {
        return this.getX() + this.indent + 10;
    }

    @Override
    public int getTextWidth() {
        return this.client.font.width(this.getTitle());
    }

    @Override
    public void updateNarration(NarrationElementOutput output) {
        super.m_168854_(output);
        if (!Strings.isNullOrEmpty(this.getDescription())) {
            output.add(NarratedElementType.HINT, Component.translatable(this.getDescription()));
        }
    }

    public boolean isValidValue() {
        return true;
    }

    @Override
    public OptionsList.Entry parent(OptionsList.Entry parent) {
        super.parent(parent);
        if (parent instanceof OptionValue) {
            this.indent = ((OptionValue) parent).indent + 12;
        }
        return this;
    }

    public abstract void setValue(T var1);
}