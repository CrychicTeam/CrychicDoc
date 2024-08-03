package de.keksuccino.fancymenu.customization.layout.editor.loadingrequirements;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.loadingrequirement.internal.LoadingRequirementContainer;
import de.keksuccino.fancymenu.customization.loadingrequirement.internal.LoadingRequirementGroup;
import de.keksuccino.fancymenu.customization.loadingrequirement.internal.LoadingRequirementInstance;
import de.keksuccino.fancymenu.util.LocalizationUtils;
import de.keksuccino.fancymenu.util.rendering.ui.UIBase;
import de.keksuccino.fancymenu.util.rendering.ui.screen.ConfirmationScreen;
import de.keksuccino.fancymenu.util.rendering.ui.scroll.v1.scrollarea.ScrollArea;
import de.keksuccino.fancymenu.util.rendering.ui.scroll.v1.scrollarea.entry.ScrollAreaEntry;
import de.keksuccino.fancymenu.util.rendering.ui.scroll.v1.scrollarea.entry.TextListScrollAreaEntry;
import de.keksuccino.fancymenu.util.rendering.ui.tooltip.Tooltip;
import de.keksuccino.fancymenu.util.rendering.ui.widget.button.ExtendedButton;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ManageRequirementsScreen extends Screen {

    protected LoadingRequirementContainer container;

    protected Consumer<LoadingRequirementContainer> callback;

    protected ScrollArea requirementsScrollArea = new ScrollArea(0, 0, 0, 0);

    protected ExtendedButton addRequirementButton;

    protected ExtendedButton addGroupButton;

    protected ExtendedButton editButton;

    protected ExtendedButton removeButton;

    protected ExtendedButton doneButton;

    protected ExtendedButton cancelButton;

    public ManageRequirementsScreen(@NotNull LoadingRequirementContainer container, @NotNull Consumer<LoadingRequirementContainer> callback) {
        super(Component.literal(I18n.get("fancymenu.editor.loading_requirement.screens.manage_screen.manage")));
        this.container = container;
        this.callback = callback;
        this.updateRequirementsScrollArea();
    }

    @Override
    protected void init() {
        this.addRequirementButton = new ExtendedButton(0, 0, 150, 20, I18n.get("fancymenu.editor.loading_requirement.screens.add_requirement"), button -> {
            BuildRequirementScreen s = new BuildRequirementScreen(this, this.container, null, call -> {
                if (call != null) {
                    this.container.addInstance(call);
                    this.updateRequirementsScrollArea();
                }
            });
            Minecraft.getInstance().setScreen(s);
        });
        this.m_7787_(this.addRequirementButton);
        this.addRequirementButton.setTooltip(Tooltip.of(LocalizationUtils.splitLocalizedStringLines("fancymenu.editor.loading_requirement.screens.manage_screen.add_requirement.desc")).setDefaultStyle());
        UIBase.applyDefaultWidgetSkinTo(this.addRequirementButton);
        this.addGroupButton = new ExtendedButton(0, 0, 150, 20, I18n.get("fancymenu.editor.loading_requirement.screens.add_group"), button -> {
            BuildRequirementGroupScreen s = new BuildRequirementGroupScreen(this, this.container, null, call -> {
                if (call != null) {
                    this.container.addGroup(call);
                    this.updateRequirementsScrollArea();
                }
            });
            Minecraft.getInstance().setScreen(s);
        });
        this.m_7787_(this.addGroupButton);
        this.addGroupButton.setTooltip(Tooltip.of(LocalizationUtils.splitLocalizedStringLines("fancymenu.editor.loading_requirement.screens.manage_screen.add_group.desc")).setDefaultStyle());
        UIBase.applyDefaultWidgetSkinTo(this.addGroupButton);
        this.editButton = new ExtendedButton(0, 0, 150, 20, "", button -> {
            Screen s = null;
            if (this.isInstanceSelected()) {
                s = new BuildRequirementScreen(this, this.container, this.getSelectedInstance(), call -> {
                    if (call != null) {
                        this.updateRequirementsScrollArea();
                    }
                });
            } else if (this.isGroupSelected()) {
                s = new BuildRequirementGroupScreen(this, this.container, this.getSelectedGroup(), call -> {
                    if (call != null) {
                        this.updateRequirementsScrollArea();
                    }
                });
            }
            if (s != null) {
                Minecraft.getInstance().setScreen(s);
            }
        }) {

            @Override
            public void render(@NotNull GuiGraphics graphics, int p_93658_, int p_93659_, float p_93660_) {
                ManageRequirementsScreen s = ManageRequirementsScreen.this;
                if (!s.isInstanceSelected() && !s.isGroupSelected()) {
                    this.setLabel(I18n.get("fancymenu.editor.loading_requirement.screens.manage_screen.edit.generic"));
                    this.setTooltip(Tooltip.of(LocalizationUtils.splitLocalizedStringLines("fancymenu.editor.loading_requirement.screens.manage_screen.no_entry_selected")).setDefaultStyle());
                    this.f_93623_ = false;
                } else {
                    if (s.isInstanceSelected()) {
                        this.setLabel(I18n.get("fancymenu.editor.loading_requirement.screens.edit_requirement"));
                    } else {
                        this.setLabel(I18n.get("fancymenu.editor.loading_requirement.screens.edit_group"));
                    }
                    this.setTooltip(Tooltip.of(LocalizationUtils.splitLocalizedStringLines("fancymenu.editor.loading_requirement.screens.manage_screen.edit.desc")).setDefaultStyle());
                    this.f_93623_ = true;
                }
                super.render(graphics, p_93658_, p_93659_, p_93660_);
            }
        };
        this.m_7787_(this.editButton);
        UIBase.applyDefaultWidgetSkinTo(this.editButton);
        this.removeButton = new ExtendedButton(0, 0, 150, 20, "", button -> {
            Screen s = null;
            if (this.isInstanceSelected()) {
                LoadingRequirementInstance i = this.getSelectedInstance();
                s = ConfirmationScreen.ofStrings(call -> {
                    if (call) {
                        this.container.removeInstance(i);
                        this.updateRequirementsScrollArea();
                    }
                    Minecraft.getInstance().setScreen(this);
                }, LocalizationUtils.splitLocalizedStringLines("fancymenu.editor.loading_requirement.screens.remove_requirement.confirm"));
            } else if (this.isGroupSelected()) {
                LoadingRequirementGroup g = this.getSelectedGroup();
                s = ConfirmationScreen.ofStrings(call -> {
                    if (call) {
                        this.container.removeGroup(g);
                        this.updateRequirementsScrollArea();
                    }
                    Minecraft.getInstance().setScreen(this);
                }, LocalizationUtils.splitLocalizedStringLines("fancymenu.editor.loading_requirement.screens.remove_group.confirm"));
            }
            if (s != null) {
                Minecraft.getInstance().setScreen(s);
            }
        }) {

            @Override
            public void render(@NotNull GuiGraphics graphics, int p_93658_, int p_93659_, float p_93660_) {
                ManageRequirementsScreen s = ManageRequirementsScreen.this;
                if (!s.isInstanceSelected() && !s.isGroupSelected()) {
                    this.setLabel(I18n.get("fancymenu.editor.loading_requirement.screens.manage_screen.remove.generic"));
                    this.setTooltip(Tooltip.of(LocalizationUtils.splitLocalizedStringLines("fancymenu.editor.loading_requirement.screens.manage_screen.no_entry_selected")).setDefaultStyle());
                    this.f_93623_ = false;
                } else {
                    if (s.isInstanceSelected()) {
                        this.setLabel(I18n.get("fancymenu.editor.loading_requirement.screens.remove_requirement"));
                    } else {
                        this.setLabel(I18n.get("fancymenu.editor.loading_requirement.screens.remove_group"));
                    }
                    this.setTooltip(Tooltip.of(LocalizationUtils.splitLocalizedStringLines("fancymenu.editor.loading_requirement.screens.manage_screen.remove.desc")).setDefaultStyle());
                    this.f_93623_ = true;
                }
                super.render(graphics, p_93658_, p_93659_, p_93660_);
            }
        };
        this.m_7787_(this.removeButton);
        UIBase.applyDefaultWidgetSkinTo(this.removeButton);
        this.cancelButton = new ExtendedButton(0, 0, 150, 20, I18n.get("fancymenu.guicomponents.cancel"), button -> this.callback.accept(null));
        this.m_7787_(this.cancelButton);
        UIBase.applyDefaultWidgetSkinTo(this.cancelButton);
        this.doneButton = new ExtendedButton(0, 0, 150, 20, I18n.get("fancymenu.guicomponents.done"), button -> this.callback.accept(this.container));
        this.m_7787_(this.doneButton);
        UIBase.applyDefaultWidgetSkinTo(this.doneButton);
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
        graphics.drawString(this.f_96547_, I18n.get("fancymenu.editor.loading_requirement.screens.manage_screen.requirements_and_groups"), 20, 50, UIBase.getUIColorTheme().generic_text_base_color.getColorInt(), false);
        this.requirementsScrollArea.setWidth(this.f_96543_ - 20 - 150 - 20 - 20, true);
        this.requirementsScrollArea.setHeight(this.f_96544_ - 85, true);
        this.requirementsScrollArea.setX(20, true);
        this.requirementsScrollArea.setY(65, true);
        this.requirementsScrollArea.render(graphics, mouseX, mouseY, partial);
        this.doneButton.m_252865_(this.f_96543_ - 20 - this.doneButton.m_5711_());
        this.doneButton.m_253211_(this.f_96544_ - 20 - 20);
        this.doneButton.render(graphics, mouseX, mouseY, partial);
        this.cancelButton.m_252865_(this.f_96543_ - 20 - this.cancelButton.m_5711_());
        this.cancelButton.m_253211_(this.doneButton.m_252907_() - 5 - 20);
        this.cancelButton.render(graphics, mouseX, mouseY, partial);
        this.removeButton.m_252865_(this.f_96543_ - 20 - this.removeButton.m_5711_());
        this.removeButton.m_253211_(this.cancelButton.m_252907_() - 15 - 20);
        this.removeButton.render(graphics, mouseX, mouseY, partial);
        this.editButton.m_252865_(this.f_96543_ - 20 - this.editButton.m_5711_());
        this.editButton.m_253211_(this.removeButton.m_252907_() - 5 - 20);
        this.editButton.render(graphics, mouseX, mouseY, partial);
        this.addGroupButton.m_252865_(this.f_96543_ - 20 - this.addGroupButton.m_5711_());
        this.addGroupButton.m_253211_(this.editButton.m_252907_() - 5 - 20);
        this.addGroupButton.render(graphics, mouseX, mouseY, partial);
        this.addRequirementButton.m_252865_(this.f_96543_ - 20 - this.addRequirementButton.m_5711_());
        this.addRequirementButton.m_253211_(this.addGroupButton.m_252907_() - 5 - 20);
        this.addRequirementButton.render(graphics, mouseX, mouseY, partial);
        super.render(graphics, mouseX, mouseY, partial);
    }

    @Nullable
    protected LoadingRequirementInstance getSelectedInstance() {
        ScrollAreaEntry e = this.requirementsScrollArea.getFocusedEntry();
        return e instanceof ManageRequirementsScreen.RequirementInstanceEntry ? ((ManageRequirementsScreen.RequirementInstanceEntry) e).instance : null;
    }

    protected boolean isInstanceSelected() {
        return this.getSelectedInstance() != null;
    }

    @Nullable
    protected LoadingRequirementGroup getSelectedGroup() {
        ScrollAreaEntry e = this.requirementsScrollArea.getFocusedEntry();
        return e instanceof ManageRequirementsScreen.RequirementGroupEntry ? ((ManageRequirementsScreen.RequirementGroupEntry) e).group : null;
    }

    protected boolean isGroupSelected() {
        return this.getSelectedGroup() != null;
    }

    protected void updateRequirementsScrollArea() {
        this.requirementsScrollArea.clearEntries();
        for (LoadingRequirementGroup g : this.container.getGroups()) {
            ManageRequirementsScreen.RequirementGroupEntry e = new ManageRequirementsScreen.RequirementGroupEntry(this.requirementsScrollArea, g);
            this.requirementsScrollArea.addEntry(e);
        }
        for (LoadingRequirementInstance i : this.container.getInstances()) {
            ManageRequirementsScreen.RequirementInstanceEntry e = new ManageRequirementsScreen.RequirementInstanceEntry(this.requirementsScrollArea, i, 14);
            this.requirementsScrollArea.addEntry(e);
        }
    }

    public static class RequirementGroupEntry extends TextListScrollAreaEntry {

        public static final int HEADER_FOOTER_HEIGHT = 3;

        public LoadingRequirementGroup group;

        public RequirementGroupEntry(ScrollArea parent, LoadingRequirementGroup group) {
            super(parent, Component.literal(group.identifier).setStyle(Style.EMPTY.withColor(UIBase.getUIColorTheme().description_area_text_color.getColorInt())).append(Component.literal(" (" + I18n.get("fancymenu.editor.loading_requirement.screens.manage_screen.group.info", group.getInstances().size() + "") + ")").setStyle(Style.EMPTY.withColor(UIBase.getUIColorTheme().element_label_color_normal.getColorInt()))), UIBase.getUIColorTheme().listing_dot_color_3.getColor(), entry -> {
            });
            this.group = group;
            this.setHeight(this.getHeight() + 6);
        }
    }

    public static class RequirementInstanceEntry extends ScrollAreaEntry {

        public static final int HEADER_FOOTER_HEIGHT = 3;

        public LoadingRequirementInstance instance;

        public final int lineHeight;

        public Font font;

        private final MutableComponent displayNameComponent;

        private final MutableComponent modeComponent;

        private final MutableComponent valueComponent;

        public RequirementInstanceEntry(ScrollArea parent, LoadingRequirementInstance instance, int lineHeight) {
            super(parent, 100, 30);
            this.font = Minecraft.getInstance().font;
            this.instance = instance;
            this.lineHeight = lineHeight;
            this.displayNameComponent = Component.literal(this.instance.requirement.getDisplayName()).setStyle(Style.EMPTY.withColor(UIBase.getUIColorTheme().description_area_text_color.getColorInt()));
            String modeString = this.instance.mode == LoadingRequirementInstance.RequirementMode.IF ? I18n.get("fancymenu.editor.loading_requirement.screens.requirement.info.mode.normal") : I18n.get("fancymenu.editor.loading_requirement.screens.requirement.info.mode.opposite");
            this.modeComponent = Component.literal(I18n.get("fancymenu.editor.loading_requirement.screens.requirement.info.mode") + " ").setStyle(Style.EMPTY.withColor(UIBase.getUIColorTheme().description_area_text_color.getColorInt())).append(Component.literal(modeString).setStyle(Style.EMPTY.withColor(UIBase.getUIColorTheme().element_label_color_normal.getColorInt())));
            String valueString = this.instance.value != null ? this.instance.value : I18n.get("fancymenu.editor.loading_requirement.screens.requirement.info.value.none");
            this.valueComponent = Component.literal(I18n.get("fancymenu.editor.loading_requirement.screens.requirement.info.value") + " ").setStyle(Style.EMPTY.withColor(UIBase.getUIColorTheme().description_area_text_color.getColorInt())).append(Component.literal(valueString).setStyle(Style.EMPTY.withColor(UIBase.getUIColorTheme().element_label_color_normal.getColorInt())));
            this.setWidth(this.calculateWidth());
            this.setHeight(lineHeight * 3 + 6);
        }

        @Override
        public void render(GuiGraphics graphics, int mouseX, int mouseY, float partial) {
            super.render(graphics, mouseX, mouseY, partial);
            int centerYLine1 = this.getY() + 3 + this.lineHeight / 2;
            int centerYLine2 = this.getY() + 3 + this.lineHeight / 2 * 3;
            int centerYLine3 = this.getY() + 3 + this.lineHeight / 2 * 5;
            RenderSystem.enableBlend();
            renderListingDot(graphics, this.getX() + 5, centerYLine1 - 2, UIBase.getUIColorTheme().listing_dot_color_2.getColor());
            graphics.drawString(this.font, this.displayNameComponent, this.getX() + 5 + 4 + 3, centerYLine1 - 9 / 2, -1, false);
            renderListingDot(graphics, this.getX() + 5 + 4 + 3, centerYLine2 - 2, UIBase.getUIColorTheme().listing_dot_color_1.getColor());
            graphics.drawString(this.font, this.modeComponent, this.getX() + 5 + 4 + 3 + 4 + 3, centerYLine2 - 9 / 2, -1, false);
            renderListingDot(graphics, this.getX() + 5 + 4 + 3, centerYLine3 - 2, UIBase.getUIColorTheme().listing_dot_color_1.getColor());
            graphics.drawString(this.font, this.valueComponent, this.getX() + 5 + 4 + 3 + 4 + 3, centerYLine3 - 9 / 2, -1, false);
        }

        private int calculateWidth() {
            int w = 12 + this.font.width(this.displayNameComponent) + 5;
            int w2 = 19 + this.font.width(this.modeComponent) + 5;
            int w3 = 19 + this.font.width(this.valueComponent) + 5;
            if (w2 > w) {
                w = w2;
            }
            if (w3 > w) {
                w = w3;
            }
            return w;
        }

        @Override
        public void onClick(ScrollAreaEntry entry) {
        }
    }
}