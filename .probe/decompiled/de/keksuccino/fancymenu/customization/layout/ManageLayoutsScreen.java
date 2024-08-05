package de.keksuccino.fancymenu.customization.layout;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.util.LocalizationUtils;
import de.keksuccino.fancymenu.util.cycle.ValueCycle;
import de.keksuccino.fancymenu.util.enums.LocalizedCycleEnum;
import de.keksuccino.fancymenu.util.file.FileUtils;
import de.keksuccino.fancymenu.util.rendering.ui.UIBase;
import de.keksuccino.fancymenu.util.rendering.ui.screen.ConfirmationScreen;
import de.keksuccino.fancymenu.util.rendering.ui.scroll.v1.scrollarea.ScrollArea;
import de.keksuccino.fancymenu.util.rendering.ui.scroll.v1.scrollarea.entry.ScrollAreaEntry;
import de.keksuccino.fancymenu.util.rendering.ui.scroll.v1.scrollarea.entry.TextListScrollAreaEntry;
import de.keksuccino.fancymenu.util.rendering.ui.scroll.v1.scrollarea.entry.TextScrollAreaEntry;
import de.keksuccino.fancymenu.util.rendering.ui.widget.button.ExtendedButton;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ManageLayoutsScreen extends Screen {

    protected Consumer<List<Layout>> callback;

    protected List<Layout> layouts;

    @Nullable
    protected Screen layoutTargetScreen;

    protected ValueCycle<ManageLayoutsScreen.Sorting> sortBy = ValueCycle.fromArray(ManageLayoutsScreen.Sorting.LAST_EDITED, ManageLayoutsScreen.Sorting.NAME, ManageLayoutsScreen.Sorting.STATUS);

    protected ScrollArea layoutListScrollArea = new ScrollArea(0, 0, 0, 0);

    protected ExtendedButton sortingButton;

    protected ExtendedButton doneButton;

    protected ExtendedButton editButton;

    protected ExtendedButton deleteButton;

    protected ExtendedButton openInTextEditorButton;

    protected ExtendedButton toggleStatusButton;

    public ManageLayoutsScreen(@NotNull List<Layout> layouts, @Nullable Screen layoutTargetScreen, @NotNull Consumer<List<Layout>> callback) {
        super(Component.translatable("fancymenu.layout.manage"));
        this.layouts = layouts;
        this.layoutTargetScreen = layoutTargetScreen;
        this.callback = callback;
        this.updateLayoutScrollArea();
    }

    @Override
    protected void init() {
        super.init();
        this.sortingButton = new ExtendedButton(0, 0, 150, 9 + 4, Component.literal(""), button -> {
            this.sortBy.next();
            this.updateLayoutScrollArea();
        }).setLabelSupplier(consumes -> this.sortBy.current().getCycleComponent());
        this.m_7787_(this.sortingButton);
        UIBase.applyDefaultWidgetSkinTo(this.sortingButton);
        this.doneButton = new ExtendedButton(0, 0, 150, 20, Component.translatable("fancymenu.guicomponents.done"), button -> this.callback.accept(this.layouts));
        this.m_7787_(this.doneButton);
        UIBase.applyDefaultWidgetSkinTo(this.doneButton);
        this.editButton = new ExtendedButton(0, 0, 150, 20, Component.translatable("fancymenu.layout.manage.edit"), button -> {
            ManageLayoutsScreen.LayoutScrollEntry e = this.getSelectedEntry();
            if (e != null) {
                LayoutHandler.openLayoutEditor(e.layout, e.layout.isUniversalLayout() ? null : this.layoutTargetScreen);
            }
        }).setIsActiveSupplier(consumes -> this.getSelectedEntry() != null);
        this.m_7787_(this.editButton);
        UIBase.applyDefaultWidgetSkinTo(this.editButton);
        this.deleteButton = new ExtendedButton(0, 0, 150, 20, Component.translatable("fancymenu.layout.manage.delete"), button -> {
            ManageLayoutsScreen.LayoutScrollEntry e = this.getSelectedEntry();
            if (e != null) {
                Minecraft.getInstance().setScreen(ConfirmationScreen.ofStrings(call -> {
                    if (call) {
                        e.layout.delete(false);
                        this.layouts.remove(e.layout);
                        this.updateLayoutScrollArea();
                    }
                    Minecraft.getInstance().setScreen(this);
                }, LocalizationUtils.splitLocalizedStringLines("fancymenu.layout.manage.delete.confirm")));
            }
        }).setIsActiveSupplier(consumes -> this.getSelectedEntry() != null);
        this.m_7787_(this.deleteButton);
        UIBase.applyDefaultWidgetSkinTo(this.deleteButton);
        this.openInTextEditorButton = new ExtendedButton(0, 0, 150, 20, Component.translatable("fancymenu.layout.manage.open_in_text_editor"), button -> {
            ManageLayoutsScreen.LayoutScrollEntry e = this.getSelectedEntry();
            if (e != null && e.layout.layoutFile != null) {
                FileUtils.openFile(e.layout.layoutFile);
            }
        }).setIsActiveSupplier(consumes -> this.getSelectedEntry() != null);
        this.m_7787_(this.openInTextEditorButton);
        UIBase.applyDefaultWidgetSkinTo(this.openInTextEditorButton);
        this.toggleStatusButton = new ExtendedButton(0, 0, 150, 20, Component.literal(""), button -> {
            ManageLayoutsScreen.LayoutScrollEntry e = this.getSelectedEntry();
            if (e != null) {
                e.layout.setEnabled(!e.layout.isEnabled(), false);
                e.updateName();
            }
        }).setIsActiveSupplier(consumes -> this.getSelectedEntry() != null).setLabelSupplier(consumes -> {
            ManageLayoutsScreen.LayoutScrollEntry e = this.getSelectedEntry();
            return e != null ? e.layout.getStatus().getCycleComponent() : Layout.LayoutStatus.DISABLED.getCycleComponent();
        });
        this.m_7787_(this.toggleStatusButton);
        UIBase.applyDefaultWidgetSkinTo(this.toggleStatusButton);
    }

    @Override
    public void onClose() {
        this.callback.accept(this.layouts);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        RenderSystem.enableBlend();
        graphics.fill(0, 0, this.f_96543_, this.f_96544_, UIBase.getUIColorTheme().screen_background_color.getColorInt());
        Component titleComp = this.f_96539_.copy().withStyle(Style.EMPTY.withBold(true));
        graphics.drawString(this.f_96547_, titleComp, 20, 20, UIBase.getUIColorTheme().generic_text_base_color.getColorInt(), false);
        graphics.drawString(this.f_96547_, Component.translatable("fancymenu.layout.manage.layouts"), 20, 50, UIBase.getUIColorTheme().generic_text_base_color.getColorInt(), false);
        this.layoutListScrollArea.setWidth(this.f_96543_ / 2 - 40, true);
        this.layoutListScrollArea.setHeight(this.f_96544_ - 85, true);
        this.layoutListScrollArea.setX(20, true);
        this.layoutListScrollArea.setY(65, true);
        this.layoutListScrollArea.render(graphics, mouseX, mouseY, partial);
        this.sortingButton.m_93674_(this.f_96547_.width(this.sortingButton.getMessage()) + 10);
        this.sortingButton.m_252865_(this.layoutListScrollArea.getXWithBorder() + this.layoutListScrollArea.getWidthWithBorder() - this.sortingButton.m_5711_());
        this.sortingButton.m_253211_(this.layoutListScrollArea.getYWithBorder() - 5 - this.sortingButton.m_93694_());
        this.sortingButton.render(graphics, mouseX, mouseY, partial);
        this.doneButton.m_252865_(this.f_96543_ - 20 - this.doneButton.m_5711_());
        this.doneButton.m_253211_(this.f_96544_ - 20 - 20);
        this.doneButton.render(graphics, mouseX, mouseY, partial);
        this.openInTextEditorButton.m_252865_(this.f_96543_ - 20 - this.openInTextEditorButton.m_5711_());
        this.openInTextEditorButton.m_253211_(this.doneButton.m_252907_() - 15 - 20);
        this.openInTextEditorButton.render(graphics, mouseX, mouseY, partial);
        this.deleteButton.m_252865_(this.f_96543_ - 20 - this.deleteButton.m_5711_());
        this.deleteButton.m_253211_(this.openInTextEditorButton.m_252907_() - 5 - 20);
        this.deleteButton.render(graphics, mouseX, mouseY, partial);
        this.editButton.m_252865_(this.f_96543_ - 20 - this.editButton.m_5711_());
        this.editButton.m_253211_(this.deleteButton.m_252907_() - 5 - 20);
        this.editButton.render(graphics, mouseX, mouseY, partial);
        this.toggleStatusButton.m_252865_(this.f_96543_ - 20 - this.toggleStatusButton.m_5711_());
        this.toggleStatusButton.m_253211_(this.editButton.m_252907_() - 5 - 20);
        this.toggleStatusButton.render(graphics, mouseX, mouseY, partial);
        super.render(graphics, mouseX, mouseY, partial);
    }

    @Nullable
    protected ManageLayoutsScreen.LayoutScrollEntry getSelectedEntry() {
        for (ScrollAreaEntry e : this.layoutListScrollArea.getEntries()) {
            if (e instanceof ManageLayoutsScreen.LayoutScrollEntry s && s.isSelected()) {
                return s;
            }
        }
        return null;
    }

    protected void updateLayoutScrollArea() {
        this.layoutListScrollArea.clearEntries();
        if (this.sortBy.current() == ManageLayoutsScreen.Sorting.STATUS) {
            LayoutHandler.sortLayoutListByStatus(this.layouts, false);
        } else if (this.sortBy.current() == ManageLayoutsScreen.Sorting.NAME) {
            LayoutHandler.sortLayoutListByName(this.layouts);
        } else if (this.sortBy.current() == ManageLayoutsScreen.Sorting.LAST_EDITED) {
            LayoutHandler.sortLayoutListByLastEdited(this.layouts, false);
        }
        for (Layout l : this.layouts) {
            ManageLayoutsScreen.LayoutScrollEntry e = new ManageLayoutsScreen.LayoutScrollEntry(this.layoutListScrollArea, l, entry -> {
            });
            this.layoutListScrollArea.addEntry(e);
        }
        if (this.layoutListScrollArea.getEntries().isEmpty()) {
            this.layoutListScrollArea.addEntry(new TextScrollAreaEntry(this.layoutListScrollArea, Component.translatable("fancymenu.layout.manage.no_layouts_found").setStyle(Style.EMPTY.withColor(UIBase.getUIColorTheme().error_text_color.getColorInt())), entry -> {
            }));
        }
    }

    public static class LayoutScrollEntry extends TextListScrollAreaEntry {

        public Layout layout;

        public LayoutScrollEntry(ScrollArea parent, @NotNull Layout layout, @NotNull Consumer<TextListScrollAreaEntry> onClick) {
            super(parent, Component.literal(""), UIBase.getUIColorTheme().listing_dot_color_1.getColor(), onClick);
            this.layout = layout;
            this.updateName();
        }

        protected void updateName() {
            Style style = this.layout.getStatus().getValueComponentStyle();
            MutableComponent c = Component.literal(this.layout.getLayoutName()).setStyle(Style.EMPTY.withColor(UIBase.getUIColorTheme().description_area_text_color.getColorInt()));
            c.append(Component.literal(" (").setStyle(style));
            c.append(this.layout.getStatus().getValueComponent());
            c.append(Component.literal(")").setStyle(style));
            this.setText(c);
        }
    }

    protected static enum Sorting implements LocalizedCycleEnum<ManageLayoutsScreen.Sorting> {

        NAME("name"), LAST_EDITED("last_edited"), STATUS("status");

        final String name;

        private Sorting(String name) {
            this.name = name;
        }

        @NotNull
        @Override
        public String getLocalizationKeyBase() {
            return "fancymenu.layout.manage.layouts.sort_by";
        }

        @NotNull
        @Override
        public String getName() {
            return this.name;
        }

        @NotNull
        public ManageLayoutsScreen.Sorting[] getValues() {
            return values();
        }

        @Nullable
        public ManageLayoutsScreen.Sorting getByNameInternal(@NotNull String name) {
            return getByName(name);
        }

        @Nullable
        public static ManageLayoutsScreen.Sorting getByName(@NotNull String name) {
            for (ManageLayoutsScreen.Sorting e : values()) {
                if (e.getName().equals(name)) {
                    return e;
                }
            }
            return null;
        }
    }
}