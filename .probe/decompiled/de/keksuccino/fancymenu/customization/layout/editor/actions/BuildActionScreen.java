package de.keksuccino.fancymenu.customization.layout.editor.actions;

import de.keksuccino.fancymenu.customization.action.Action;
import de.keksuccino.fancymenu.customization.action.ActionInstance;
import de.keksuccino.fancymenu.customization.action.ActionRegistry;
import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import de.keksuccino.fancymenu.util.LocalizationUtils;
import de.keksuccino.fancymenu.util.rendering.ui.UIBase;
import de.keksuccino.fancymenu.util.rendering.ui.scroll.v1.scrollarea.ScrollArea;
import de.keksuccino.fancymenu.util.rendering.ui.scroll.v1.scrollarea.entry.ScrollAreaEntry;
import de.keksuccino.fancymenu.util.rendering.ui.scroll.v1.scrollarea.entry.TextListScrollAreaEntry;
import de.keksuccino.fancymenu.util.rendering.ui.scroll.v1.scrollarea.entry.TextScrollAreaEntry;
import de.keksuccino.fancymenu.util.rendering.ui.tooltip.Tooltip;
import de.keksuccino.fancymenu.util.rendering.ui.widget.button.ExtendedButton;
import java.util.function.Consumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BuildActionScreen extends Screen {

    protected final ActionInstance instance;

    protected Consumer<ActionInstance> callback;

    protected Action originalAction = null;

    protected String originalActionValue = null;

    protected ScrollArea actionsListScrollArea = new ScrollArea(0, 0, 0, 0);

    protected ScrollArea actionDescriptionScrollArea = new ScrollArea(0, 0, 0, 0);

    protected ExtendedButton editValueButton;

    protected ExtendedButton doneButton;

    protected ExtendedButton cancelButton;

    public BuildActionScreen(@Nullable ActionInstance instanceToEdit, @NotNull Consumer<ActionInstance> callback) {
        super(instanceToEdit != null ? Component.translatable("fancymenu.editor.action.screens.edit_action") : Component.translatable("fancymenu.editor.action.screens.add_action"));
        if (instanceToEdit != null) {
            this.originalAction = instanceToEdit.action;
            this.originalActionValue = instanceToEdit.value;
        }
        this.instance = instanceToEdit != null ? instanceToEdit : new ActionInstance(Action.EMPTY, null);
        this.callback = callback;
        this.setContentOfActionsList();
        if (this.instance.action != Action.EMPTY) {
            for (ScrollAreaEntry e : this.actionsListScrollArea.getEntries()) {
                if (e instanceof BuildActionScreen.ActionScrollEntry && ((BuildActionScreen.ActionScrollEntry) e).action == this.instance.action) {
                    e.setSelected(true);
                    break;
                }
            }
        }
    }

    @Override
    protected void init() {
        this.editValueButton = new ExtendedButton(0, 0, 150, 20, Component.translatable("fancymenu.editor.action.screens.build_screen.edit_value"), button -> {
            if (this.instance.action != Action.EMPTY) {
                this.originalAction = null;
                this.originalActionValue = null;
                this.instance.action.editValue(this, this.instance);
            }
        }) {

            @Override
            public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
                Action b = BuildActionScreen.this.instance.action;
                if (b != Action.EMPTY && !b.hasValue()) {
                    this.setTooltip(Tooltip.of(LocalizationUtils.splitLocalizedStringLines("fancymenu.editor.action.screens.build_screen.edit_value.desc.no_value")));
                } else {
                    this.setTooltip(Tooltip.of(LocalizationUtils.splitLocalizedStringLines("fancymenu.editor.action.screens.build_screen.edit_value.desc.normal")));
                }
                this.f_93623_ = b != Action.EMPTY && b.hasValue();
                super.render(graphics, mouseX, mouseY, partial);
            }
        };
        this.m_7787_(this.editValueButton);
        UIBase.applyDefaultWidgetSkinTo(this.editValueButton);
        this.doneButton = new ExtendedButton(0, 0, 150, 20, Component.translatable("fancymenu.guicomponents.done"), button -> this.callback.accept(this.instance.action != Action.EMPTY ? this.instance : null)) {

            @Override
            public void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
                if (BuildActionScreen.this.instance.action == Action.EMPTY) {
                    this.setTooltip(Tooltip.of(LocalizationUtils.splitLocalizedStringLines("fancymenu.editor.action.screens.finish.no_action_selected")));
                    this.f_93623_ = false;
                } else if (BuildActionScreen.this.instance.value == null && BuildActionScreen.this.instance.action.hasValue()) {
                    this.setTooltip(Tooltip.of(LocalizationUtils.splitLocalizedStringLines("fancymenu.editor.action.screens.build_screen.finish.no_value_set")));
                    this.f_93623_ = false;
                } else {
                    this.setTooltip((Tooltip) null);
                    this.f_93623_ = true;
                }
                super.renderWidget(graphics, mouseX, mouseY, partial);
            }
        };
        this.m_7787_(this.doneButton);
        UIBase.applyDefaultWidgetSkinTo(this.doneButton);
        this.cancelButton = new ExtendedButton(0, 0, 150, 20, Component.translatable("fancymenu.guicomponents.cancel"), button -> this.callback.accept(null));
        this.m_7787_(this.cancelButton);
        UIBase.applyDefaultWidgetSkinTo(this.cancelButton);
        this.setDescription(this.instance.action);
    }

    @Override
    public void onClose() {
        this.callback.accept(null);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        graphics.fill(0, 0, this.f_96543_, this.f_96544_, UIBase.getUIColorTheme().screen_background_color.getColorInt());
        Component titleComp = this.f_96539_.copy().withStyle(Style.EMPTY.withBold(true));
        graphics.drawString(this.f_96547_, titleComp, 20, 20, UIBase.getUIColorTheme().generic_text_base_color.getColorInt(), false);
        graphics.drawString(this.f_96547_, I18n.get("fancymenu.editor.action.screens.build_screen.available_actions"), 20, 50, UIBase.getUIColorTheme().generic_text_base_color.getColorInt(), false);
        this.actionsListScrollArea.setWidth(this.f_96543_ / 2 - 40, true);
        this.actionsListScrollArea.setHeight(this.f_96544_ - 85, true);
        this.actionsListScrollArea.setX(20, true);
        this.actionsListScrollArea.setY(65, true);
        this.actionsListScrollArea.render(graphics, mouseX, mouseY, partial);
        String descLabelString = I18n.get("fancymenu.editor.action.screens.build_screen.action_description");
        int descLabelWidth = this.f_96547_.width(descLabelString);
        graphics.drawString(this.f_96547_, descLabelString, this.f_96543_ - 20 - descLabelWidth, 50, UIBase.getUIColorTheme().generic_text_base_color.getColorInt(), false);
        this.actionDescriptionScrollArea.setWidth(this.f_96543_ / 2 - 40, true);
        this.actionDescriptionScrollArea.setHeight(Math.max(40, this.f_96544_ / 2 - 50 - 25), true);
        this.actionDescriptionScrollArea.setX(this.f_96543_ - 20 - this.actionDescriptionScrollArea.getWidthWithBorder(), true);
        this.actionDescriptionScrollArea.setY(65, true);
        this.actionDescriptionScrollArea.render(graphics, mouseX, mouseY, partial);
        this.doneButton.m_252865_(this.f_96543_ - 20 - this.doneButton.m_5711_());
        this.doneButton.m_253211_(this.f_96544_ - 20 - 20);
        this.doneButton.render(graphics, mouseX, mouseY, partial);
        this.cancelButton.m_252865_(this.f_96543_ - 20 - this.cancelButton.m_5711_());
        this.cancelButton.m_253211_(this.doneButton.m_252907_() - 5 - 20);
        this.cancelButton.render(graphics, mouseX, mouseY, partial);
        this.editValueButton.m_252865_(this.f_96543_ - 20 - this.editValueButton.m_5711_());
        this.editValueButton.m_253211_(this.cancelButton.m_252907_() - 15 - 20);
        this.editValueButton.render(graphics, mouseX, mouseY, partial);
        super.render(graphics, mouseX, mouseY, partial);
    }

    protected void setDescription(@Nullable Action action) {
        this.actionDescriptionScrollArea.clearEntries();
        if (action != null && action.getActionDescription() != null) {
            for (Component c : action.getActionDescription()) {
                TextScrollAreaEntry e = new TextScrollAreaEntry(this.actionDescriptionScrollArea, c, entry -> {
                });
                e.setSelectable(false);
                e.setBackgroundColorHover(e.getBackgroundColorIdle());
                e.setPlayClickSound(false);
                this.actionDescriptionScrollArea.addEntry(e);
            }
        }
    }

    protected void setContentOfActionsList() {
        this.actionsListScrollArea.clearEntries();
        for (Action c : ActionRegistry.getActions()) {
            if (LayoutEditorScreen.getCurrentInstance() == null || c.shouldShowUpInEditorActionMenu(LayoutEditorScreen.getCurrentInstance())) {
                BuildActionScreen.ActionScrollEntry e = new BuildActionScreen.ActionScrollEntry(this.actionsListScrollArea, c, entry -> {
                    this.instance.action = c;
                    if (this.originalAction == c) {
                        this.instance.value = this.originalActionValue;
                    } else {
                        this.instance.value = null;
                    }
                    this.setDescription(c);
                });
                this.actionsListScrollArea.addEntry(e);
            }
        }
    }

    public static class ActionScrollEntry extends TextListScrollAreaEntry {

        public Action action;

        public ActionScrollEntry(ScrollArea parent, @NotNull Action action, @NotNull Consumer<TextListScrollAreaEntry> onClick) {
            super(parent, buildLabel(action), UIBase.getUIColorTheme().listing_dot_color_1.getColor(), onClick);
            this.action = action;
        }

        @NotNull
        private static Component buildLabel(@NotNull Action action) {
            MutableComponent c = action.getActionDisplayName().copy().setStyle(Style.EMPTY.withColor(UIBase.getUIColorTheme().description_area_text_color.getColorInt()));
            if (action.isDeprecated()) {
                c = c.withStyle(Style.EMPTY.withStrikethrough(true));
                c = c.append(Component.literal(" ").setStyle(Style.EMPTY.withStrikethrough(false)));
                c = c.append(Component.translatable("fancymenu.editor.actions.deprecated").setStyle(Style.EMPTY.withColor(UIBase.getUIColorTheme().error_text_color.getColorInt()).withStrikethrough(false)));
            }
            return c;
        }
    }
}