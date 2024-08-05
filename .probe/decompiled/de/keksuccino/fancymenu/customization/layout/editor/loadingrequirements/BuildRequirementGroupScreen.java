package de.keksuccino.fancymenu.customization.layout.editor.loadingrequirements;

import de.keksuccino.fancymenu.customization.loadingrequirement.internal.LoadingRequirementContainer;
import de.keksuccino.fancymenu.customization.loadingrequirement.internal.LoadingRequirementGroup;
import de.keksuccino.fancymenu.customization.loadingrequirement.internal.LoadingRequirementInstance;
import de.keksuccino.fancymenu.util.LocalizationUtils;
import de.keksuccino.fancymenu.util.rendering.ui.UIBase;
import de.keksuccino.fancymenu.util.rendering.ui.screen.ConfirmationScreen;
import de.keksuccino.fancymenu.util.rendering.ui.scroll.v1.scrollarea.ScrollArea;
import de.keksuccino.fancymenu.util.rendering.ui.scroll.v1.scrollarea.entry.ScrollAreaEntry;
import de.keksuccino.fancymenu.util.rendering.ui.tooltip.Tooltip;
import de.keksuccino.fancymenu.util.rendering.ui.widget.button.ExtendedButton;
import de.keksuccino.konkrete.gui.content.AdvancedTextField;
import de.keksuccino.konkrete.input.CharacterFilter;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BuildRequirementGroupScreen extends Screen {

    protected Screen parentScreen;

    protected LoadingRequirementContainer parent;

    protected LoadingRequirementGroup group;

    protected boolean isEdit;

    protected Consumer<LoadingRequirementGroup> callback;

    protected ScrollArea requirementsScrollArea = new ScrollArea(0, 0, 0, 0);

    protected ExtendedButton groupModeButton;

    protected ExtendedButton addRequirementButton;

    protected ExtendedButton removeRequirementButton;

    protected ExtendedButton editRequirementButton;

    protected ExtendedButton doneButton;

    protected ExtendedButton cancelButton;

    protected AdvancedTextField groupIdentifierTextField;

    public BuildRequirementGroupScreen(@Nullable Screen parentScreen, @NotNull LoadingRequirementContainer parent, @Nullable LoadingRequirementGroup groupToEdit, @NotNull Consumer<LoadingRequirementGroup> callback) {
        super(groupToEdit != null ? Component.literal(I18n.get("fancymenu.editor.loading_requirement.screens.edit_group")) : Component.literal(I18n.get("fancymenu.editor.loading_requirement.screens.add_group")));
        this.parentScreen = parentScreen;
        this.parent = parent;
        this.group = groupToEdit != null ? groupToEdit : new LoadingRequirementGroup("group_" + System.currentTimeMillis(), LoadingRequirementGroup.GroupMode.AND, parent);
        this.callback = callback;
        this.isEdit = groupToEdit != null;
        this.updateRequirementsScrollArea();
        this.groupIdentifierTextField = new AdvancedTextField(Minecraft.getInstance().font, 0, 0, 150, 20, true, CharacterFilter.getBasicFilenameCharacterFilter()) {

            @Override
            public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
                super.m_88315_(graphics, mouseX, mouseY, partial);
                BuildRequirementGroupScreen.this.group.identifier = this.m_94155_();
            }
        };
        if (this.group.identifier != null) {
            this.groupIdentifierTextField.m_94144_(this.group.identifier);
        }
    }

    @Override
    protected void init() {
        Minecraft.getInstance().getWindow().setGuiScale((double) Minecraft.getInstance().getWindow().calculateScale(Minecraft.getInstance().options.guiScale().get(), Minecraft.getInstance().isEnforceUnicode()));
        this.f_96544_ = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        this.f_96543_ = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        super.init();
        this.groupModeButton = new ExtendedButton(0, 0, 150, 20, "", button -> {
            if (this.group.mode == LoadingRequirementGroup.GroupMode.AND) {
                this.group.mode = LoadingRequirementGroup.GroupMode.OR;
            } else {
                this.group.mode = LoadingRequirementGroup.GroupMode.AND;
            }
        }) {

            @Override
            public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
                if (BuildRequirementGroupScreen.this.group.mode == LoadingRequirementGroup.GroupMode.AND) {
                    this.setLabel(I18n.get("fancymenu.editor.loading_requirement.screens.build_group_screen.mode.and"));
                } else {
                    this.setLabel(I18n.get("fancymenu.editor.loading_requirement.screens.build_group_screen.mode.or"));
                }
                super.render(graphics, mouseX, mouseY, partial);
            }
        };
        this.m_7787_(this.groupModeButton);
        this.groupModeButton.setTooltip(Tooltip.of(LocalizationUtils.splitLocalizedStringLines("fancymenu.editor.loading_requirement.screens.build_group_screen.mode.desc")));
        UIBase.applyDefaultWidgetSkinTo(this.groupModeButton);
        this.addRequirementButton = new ExtendedButton(0, 0, 150, 20, I18n.get("fancymenu.editor.loading_requirement.screens.add_requirement"), button -> {
            BuildRequirementScreen s = new BuildRequirementScreen(this, this.parent, null, call -> {
                if (call != null) {
                    this.group.addInstance(call);
                    this.updateRequirementsScrollArea();
                }
            });
            Minecraft.getInstance().setScreen(s);
        });
        this.m_7787_(this.addRequirementButton);
        this.addRequirementButton.setTooltip(Tooltip.of(LocalizationUtils.splitLocalizedStringLines("fancymenu.editor.loading_requirement.screens.build_group_screen.add_requirement.desc")));
        UIBase.applyDefaultWidgetSkinTo(this.addRequirementButton);
        this.editRequirementButton = new ExtendedButton(0, 0, 150, 20, I18n.get("fancymenu.editor.loading_requirement.screens.edit_requirement"), button -> {
            LoadingRequirementInstance i = this.getSelectedInstance();
            if (i != null) {
                BuildRequirementScreen s = new BuildRequirementScreen(this, this.parent, i, call -> {
                    if (call != null) {
                        this.updateRequirementsScrollArea();
                    }
                });
                Minecraft.getInstance().setScreen(s);
            }
        }) {

            @Override
            public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
                if (BuildRequirementGroupScreen.this.getSelectedInstance() == null) {
                    this.setTooltip(Tooltip.of(LocalizationUtils.splitLocalizedStringLines("fancymenu.editor.loading_requirement.screens.build_group_screen.no_requirement_selected")));
                    this.f_93623_ = false;
                } else {
                    this.setTooltip((Tooltip) null);
                    this.f_93623_ = true;
                }
                super.render(graphics, mouseX, mouseY, partial);
            }
        };
        this.m_7787_(this.editRequirementButton);
        this.editRequirementButton.setTooltip(Tooltip.of(LocalizationUtils.splitLocalizedStringLines("fancymenu.editor.loading_requirement.screens.build_group_screen.edit_requirement.desc")));
        UIBase.applyDefaultWidgetSkinTo(this.editRequirementButton);
        this.removeRequirementButton = new ExtendedButton(0, 0, 150, 20, I18n.get("fancymenu.editor.loading_requirement.screens.remove_requirement"), button -> {
            LoadingRequirementInstance i = this.getSelectedInstance();
            if (i != null) {
                Minecraft.getInstance().setScreen(ConfirmationScreen.ofStrings(call -> {
                    if (call) {
                        this.group.removeInstance(i);
                        this.updateRequirementsScrollArea();
                    }
                    Minecraft.getInstance().setScreen(this);
                }, LocalizationUtils.splitLocalizedStringLines("fancymenu.editor.loading_requirement.screens.remove_requirement.confirm")));
            }
        }) {

            @Override
            public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
                if (BuildRequirementGroupScreen.this.getSelectedInstance() == null) {
                    this.setTooltip(Tooltip.of(LocalizationUtils.splitLocalizedStringLines("fancymenu.editor.loading_requirement.screens.build_group_screen.no_requirement_selected")));
                    this.f_93623_ = false;
                } else {
                    this.setTooltip((Tooltip) null);
                    this.f_93623_ = true;
                }
                super.render(graphics, mouseX, mouseY, partial);
            }
        };
        this.m_7787_(this.removeRequirementButton);
        this.removeRequirementButton.setTooltip(Tooltip.of(LocalizationUtils.splitLocalizedStringLines("fancymenu.editor.loading_requirement.screens.build_group_screen.remove_requirement.desc")));
        UIBase.applyDefaultWidgetSkinTo(this.removeRequirementButton);
        this.doneButton = new ExtendedButton(0, 0, 150, 20, I18n.get("fancymenu.guicomponents.done"), button -> {
            Minecraft.getInstance().setScreen(this.parentScreen);
            this.callback.accept(this.group);
        }) {

            @Override
            public void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
                BuildRequirementGroupScreen s = BuildRequirementGroupScreen.this;
                if (s.group.getInstances().isEmpty()) {
                    this.setTooltip(Tooltip.of(LocalizationUtils.splitLocalizedStringLines("fancymenu.editor.loading_requirement.screens.build_group_screen.finish.no_requirements_added")));
                    this.f_93623_ = false;
                } else if (s.parent.getGroup(s.group.identifier) != null && s.parent.getGroup(s.group.identifier) != s.group) {
                    this.setTooltip(Tooltip.of(LocalizationUtils.splitLocalizedStringLines("fancymenu.editor.loading_requirement.screens.build_group_screen.finish.identifier_already_used")));
                    this.f_93623_ = false;
                } else if (s.group.identifier != null && s.group.identifier.replace(" ", "").length() != 0) {
                    this.setTooltip((Tooltip) null);
                    this.f_93623_ = true;
                } else {
                    this.setTooltip(Tooltip.of(LocalizationUtils.splitLocalizedStringLines("fancymenu.editor.loading_requirement.screens.build_group_screen.finish.identifier_too_short")));
                    this.f_93623_ = false;
                }
                super.renderWidget(graphics, mouseX, mouseY, partialTicks);
            }
        };
        this.m_7787_(this.doneButton);
        UIBase.applyDefaultWidgetSkinTo(this.doneButton);
        this.cancelButton = new ExtendedButton(0, 0, 150, 20, I18n.get("fancymenu.guicomponents.cancel"), button -> {
            Minecraft.getInstance().setScreen(this.parentScreen);
            if (this.isEdit) {
                this.callback.accept(this.group);
            } else {
                this.callback.accept(null);
            }
        });
        this.m_7787_(this.cancelButton);
        UIBase.applyDefaultWidgetSkinTo(this.cancelButton);
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(this.parentScreen);
        if (this.isEdit) {
            this.callback.accept(this.group);
        } else {
            this.callback.accept(null);
        }
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        graphics.fill(0, 0, this.f_96543_, this.f_96544_, UIBase.getUIColorTheme().screen_background_color.getColorInt());
        Component titleComp = this.f_96539_.copy().withStyle(Style.EMPTY.withBold(true));
        graphics.drawString(this.f_96547_, titleComp, 20, 20, UIBase.getUIColorTheme().generic_text_base_color.getColorInt(), false);
        graphics.drawString(this.f_96547_, I18n.get("fancymenu.editor.loading_requirement.screens.build_group_screen.group_requirements"), 20, 50, UIBase.getUIColorTheme().generic_text_base_color.getColorInt(), false);
        this.requirementsScrollArea.setWidth(this.f_96543_ - 20 - 150 - 20 - 20, true);
        this.requirementsScrollArea.setHeight(this.f_96544_ - 85, true);
        this.requirementsScrollArea.setX(20, true);
        this.requirementsScrollArea.setY(65, true);
        this.requirementsScrollArea.render(graphics, mouseX, mouseY, partial);
        this.doneButton.m_252865_(this.f_96543_ - 20 - this.doneButton.m_5711_());
        this.doneButton.m_253211_(this.f_96544_ - 20 - 20);
        this.doneButton.render(graphics, mouseX, mouseY, partial);
        if (!this.isEdit) {
            this.cancelButton.m_252865_(this.f_96543_ - 20 - this.cancelButton.m_5711_());
            this.cancelButton.m_253211_(this.doneButton.m_252907_() - 5 - 20);
            this.cancelButton.render(graphics, mouseX, mouseY, partial);
        } else {
            this.cancelButton.f_93623_ = false;
        }
        this.removeRequirementButton.m_252865_(this.f_96543_ - 20 - this.removeRequirementButton.m_5711_());
        this.removeRequirementButton.m_253211_((this.isEdit ? this.doneButton.m_252907_() : this.cancelButton.m_252907_()) - 15 - 20);
        this.removeRequirementButton.render(graphics, mouseX, mouseY, partial);
        this.editRequirementButton.m_252865_(this.f_96543_ - 20 - this.editRequirementButton.m_5711_());
        this.editRequirementButton.m_253211_(this.removeRequirementButton.m_252907_() - 5 - 20);
        this.editRequirementButton.render(graphics, mouseX, mouseY, partial);
        this.addRequirementButton.m_252865_(this.f_96543_ - 20 - this.addRequirementButton.m_5711_());
        this.addRequirementButton.m_253211_(this.editRequirementButton.m_252907_() - 5 - 20);
        this.addRequirementButton.render(graphics, mouseX, mouseY, partial);
        this.groupModeButton.m_252865_(this.f_96543_ - 20 - this.groupModeButton.m_5711_());
        this.groupModeButton.m_253211_(this.addRequirementButton.m_252907_() - 5 - 20);
        this.groupModeButton.render(graphics, mouseX, mouseY, partial);
        this.groupIdentifierTextField.m_252865_(this.f_96543_ - 20 - this.groupIdentifierTextField.m_5711_());
        this.groupIdentifierTextField.m_253211_(this.groupModeButton.m_252907_() - 15 - 20);
        this.groupIdentifierTextField.m_88315_(graphics, mouseX, mouseY, partial);
        String idLabel = I18n.get("fancymenu.editor.loading_requirement.screens.build_group_screen.group_identifier");
        int idLabelWidth = this.f_96547_.width(idLabel);
        graphics.drawString(this.f_96547_, idLabel, this.f_96543_ - 20 - idLabelWidth, this.groupIdentifierTextField.m_252907_() - 15, UIBase.getUIColorTheme().generic_text_base_color.getColorInt(), false);
        super.render(graphics, mouseX, mouseY, partial);
    }

    @Nullable
    protected LoadingRequirementInstance getSelectedInstance() {
        ScrollAreaEntry e = this.requirementsScrollArea.getFocusedEntry();
        return e instanceof ManageRequirementsScreen.RequirementInstanceEntry ? ((ManageRequirementsScreen.RequirementInstanceEntry) e).instance : null;
    }

    protected void updateRequirementsScrollArea() {
        this.requirementsScrollArea.clearEntries();
        for (LoadingRequirementInstance i : this.group.getInstances()) {
            ManageRequirementsScreen.RequirementInstanceEntry e = new ManageRequirementsScreen.RequirementInstanceEntry(this.requirementsScrollArea, i, 14);
            this.requirementsScrollArea.addEntry(e);
        }
    }
}