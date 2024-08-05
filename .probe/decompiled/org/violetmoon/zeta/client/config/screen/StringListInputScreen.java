package org.violetmoon.zeta.client.config.screen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.zeta.client.ZetaClient;
import org.violetmoon.zeta.client.config.widget.ScrollableWidgetList;
import org.violetmoon.zeta.config.ChangeSet;
import org.violetmoon.zeta.config.ValueDefinition;

public class StringListInputScreen extends AbstractInputScreen<List<String>> {

    protected ScrollableWidgetList<StringListInputScreen, StringListInputScreen.Entry> list;

    public StringListInputScreen(ZetaClient zc, Screen parent, ChangeSet changes, ValueDefinition<List<String>> def) {
        super(zc, parent, changes, def);
    }

    @Override
    protected void init() {
        super.init();
        this.list = new ScrollableWidgetList<>(this);
        this.m_7787_(this.list);
        this.forceUpdateWidgetsTo(this.get());
    }

    protected void forceUpdateWidgetsTo(List<String> value) {
        this.list.removeChildWidgets(x$0 -> this.m_169411_(x$0));
        this.list.replaceEntries(IntStream.range(0, value.size() + 1).mapToObj(x$0 -> new StringListInputScreen.Entry(x$0)).toList());
        this.list.addChildWidgets(x$0 -> {
            AbstractWidget var10000 = (AbstractWidget) this.m_142416_(x$0);
        }, x$0 -> {
            AbstractWidget var10000 = (AbstractWidget) this.m_7787_(x$0);
        });
        this.list.m_93410_(this.list.m_93517_());
        this.updateButtonStatus(this.def.validate(value));
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(guiGraphics);
        this.list.render(guiGraphics, mouseX, mouseY, partialTicks);
        super.m_88315_(guiGraphics, mouseX, mouseY, partialTicks);
        this.list.reenableVisibleWidgets();
        guiGraphics.drawCenteredString(this.f_96547_, Component.literal(this.def.getTranslatedDisplayName(x$0 -> I18n.get(x$0))).withStyle(ChatFormatting.BOLD), this.f_96543_ / 2, 20, 16777215);
    }

    protected String getString(int index) {
        List<String> list = this.get();
        return index < list.size() ? (String) list.get(index) : null;
    }

    protected void setString(int index, String s) {
        List<String> copy = new ArrayList((Collection) this.get());
        copy.set(index, s);
        this.set(copy);
        this.updateButtonStatus(this.def.validate(copy));
    }

    protected void add() {
        List<String> copy = new ArrayList((Collection) this.get());
        copy.add("");
        this.set(copy);
        this.forceUpdateWidgetsTo(copy);
        this.updateButtonStatus(this.def.validate(copy));
        this.list.ensureVisible2(copy.size() + 1);
    }

    protected void remove(int idx) {
        List<String> copy = new ArrayList((Collection) this.get());
        copy.remove(idx);
        this.set(copy);
        this.forceUpdateWidgetsTo(copy);
        this.updateButtonStatus(this.def.validate(copy));
    }

    protected class Entry extends ScrollableWidgetList.Entry<StringListInputScreen.Entry> {

        private final int index;

        public Entry(int index) {
            this.index = index;
            String here = StringListInputScreen.this.getString(index);
            if (StringListInputScreen.this.getString(index) != null) {
                Minecraft mc = Minecraft.getInstance();
                EditBox field = new EditBox(mc.font, 10, 3, 210, 20, Component.literal(""));
                field.setMaxLength(256);
                field.setValue(here);
                field.moveCursorTo(0);
                field.setResponder(str -> StringListInputScreen.this.setString(index, str));
                this.addScrollingWidget(field);
                this.addScrollingWidget(new Button.Builder(Component.literal("-").withStyle(ChatFormatting.RED), b -> StringListInputScreen.this.remove(index)).size(20, 20).pos(230, 3).build());
            } else {
                this.addScrollingWidget(new Button.Builder(Component.literal("+").withStyle(ChatFormatting.GREEN), b -> StringListInputScreen.this.add()).size(20, 20).pos(10, 3).build());
            }
        }

        @NotNull
        @Override
        public Component getNarration() {
            return Component.literal((String) Optional.ofNullable(StringListInputScreen.this.getString(this.index)).orElse(""));
        }
    }
}