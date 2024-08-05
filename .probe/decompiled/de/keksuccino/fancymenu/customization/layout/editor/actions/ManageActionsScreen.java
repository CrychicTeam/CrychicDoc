package de.keksuccino.fancymenu.customization.layout.editor.actions;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.action.ActionInstance;
import de.keksuccino.fancymenu.customization.action.Executable;
import de.keksuccino.fancymenu.customization.action.blocks.AbstractExecutableBlock;
import de.keksuccino.fancymenu.customization.action.blocks.GenericExecutableBlock;
import de.keksuccino.fancymenu.customization.action.blocks.statements.ElseExecutableBlock;
import de.keksuccino.fancymenu.customization.action.blocks.statements.ElseIfExecutableBlock;
import de.keksuccino.fancymenu.customization.action.blocks.statements.IfExecutableBlock;
import de.keksuccino.fancymenu.customization.layout.editor.loadingrequirements.ManageRequirementsScreen;
import de.keksuccino.fancymenu.customization.loadingrequirement.internal.LoadingRequirementContainer;
import de.keksuccino.fancymenu.customization.loadingrequirement.internal.LoadingRequirementGroup;
import de.keksuccino.fancymenu.customization.loadingrequirement.internal.LoadingRequirementInstance;
import de.keksuccino.fancymenu.util.LocalizationUtils;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.fancymenu.util.rendering.ui.UIBase;
import de.keksuccino.fancymenu.util.rendering.ui.screen.ConfirmationScreen;
import de.keksuccino.fancymenu.util.rendering.ui.scroll.v1.scrollarea.ScrollArea;
import de.keksuccino.fancymenu.util.rendering.ui.scroll.v1.scrollarea.entry.ScrollAreaEntry;
import de.keksuccino.fancymenu.util.rendering.ui.tooltip.Tooltip;
import de.keksuccino.fancymenu.util.rendering.ui.widget.button.ExtendedButton;
import de.keksuccino.konkrete.input.MouseInput;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ManageActionsScreen extends Screen {

    private static final Logger LOGGER = LogManager.getLogger();

    protected GenericExecutableBlock executableBlock;

    protected Consumer<GenericExecutableBlock> callback;

    protected ScrollArea actionsScrollArea = new ScrollArea(0, 0, 0, 0);

    protected ExtendedButton addActionButton;

    protected ExtendedButton moveUpButton;

    protected ExtendedButton moveDownButton;

    protected ExtendedButton editButton;

    protected ExtendedButton removeButton;

    protected ExtendedButton doneButton;

    protected ExtendedButton cancelButton;

    protected ExtendedButton addIfButton;

    protected ExtendedButton appendElseIfButton;

    protected ExtendedButton appendElseButton;

    @Nullable
    protected ManageActionsScreen.ExecutableEntry renderTickDragHoveredEntry = null;

    @Nullable
    protected ManageActionsScreen.ExecutableEntry renderTickDraggedEntry = null;

    private final ManageActionsScreen.ExecutableEntry BEFORE_FIRST = new ManageActionsScreen.ExecutableEntry(this.actionsScrollArea, new GenericExecutableBlock(), 1, 0);

    private final ManageActionsScreen.ExecutableEntry AFTER_LAST = new ManageActionsScreen.ExecutableEntry(this.actionsScrollArea, new GenericExecutableBlock(), 1, 0);

    protected int lastWidth = 0;

    protected int lastHeight = 0;

    public ManageActionsScreen(@NotNull GenericExecutableBlock executableBlock, @NotNull Consumer<GenericExecutableBlock> callback) {
        super(Component.translatable("fancymenu.editor.action.screens.manage_screen.manage"));
        this.executableBlock = executableBlock.copy(false);
        this.callback = callback;
        this.updateActionInstanceScrollArea(false);
    }

    @Override
    protected void init() {
        this.addIfButton = new ExtendedButton(0, 0, 150, 20, Component.translatable("fancymenu.editor.actions.blocks.add.if"), button -> {
            ManageRequirementsScreen s = new ManageRequirementsScreen(new LoadingRequirementContainer(), container -> {
                if (container != null) {
                    this.executableBlock.addExecutable(new IfExecutableBlock(container));
                    this.updateActionInstanceScrollArea(false);
                    this.actionsScrollArea.verticalScrollBar.setScroll(1.0F);
                }
                Minecraft.getInstance().setScreen(this);
            });
            Minecraft.getInstance().setScreen(s);
        });
        this.m_7787_(this.addIfButton);
        UIBase.applyDefaultWidgetSkinTo(this.addIfButton);
        this.appendElseIfButton = new ExtendedButton(0, 0, 150, 20, Component.translatable("fancymenu.editor.actions.blocks.add.else_if"), button -> {
            ManageActionsScreen.ExecutableEntry selected = this.getSelectedEntry();
            if (selected != null && (selected.executable instanceof IfExecutableBlock || selected.executable instanceof ElseIfExecutableBlock)) {
                ManageRequirementsScreen s = new ManageRequirementsScreen(new LoadingRequirementContainer(), container -> {
                    if (container != null) {
                        ElseIfExecutableBlock b = new ElseIfExecutableBlock(container);
                        b.setAppendedBlock(((AbstractExecutableBlock) selected.executable).getAppendedBlock());
                        ((AbstractExecutableBlock) selected.executable).setAppendedBlock(b);
                        this.updateActionInstanceScrollArea(true);
                    }
                    Minecraft.getInstance().setScreen(this);
                });
                Minecraft.getInstance().setScreen(s);
            }
        }).setIsActiveSupplier(consumes -> {
            ManageActionsScreen.ExecutableEntry selected = this.getSelectedEntry();
            return selected == null ? false : selected.executable instanceof IfExecutableBlock || selected.executable instanceof ElseIfExecutableBlock;
        });
        this.m_7787_(this.appendElseIfButton);
        UIBase.applyDefaultWidgetSkinTo(this.appendElseIfButton);
        this.appendElseButton = new ExtendedButton(0, 0, 150, 20, Component.translatable("fancymenu.editor.actions.blocks.add.else"), button -> {
            ManageActionsScreen.ExecutableEntry selected = this.getSelectedEntry();
            if (selected != null && (selected.executable instanceof IfExecutableBlock || selected.executable instanceof ElseIfExecutableBlock)) {
                ElseExecutableBlock b = new ElseExecutableBlock();
                b.setAppendedBlock(((AbstractExecutableBlock) selected.executable).getAppendedBlock());
                ((AbstractExecutableBlock) selected.executable).setAppendedBlock(b);
                this.updateActionInstanceScrollArea(true);
            }
        }).setIsActiveSupplier(consumes -> {
            ManageActionsScreen.ExecutableEntry selected = this.getSelectedEntry();
            return selected == null ? false : selected.executable instanceof IfExecutableBlock || selected.executable instanceof ElseIfExecutableBlock;
        });
        this.m_7787_(this.appendElseButton);
        UIBase.applyDefaultWidgetSkinTo(this.appendElseButton);
        this.addActionButton = new ExtendedButton(0, 0, 150, 20, I18n.get("fancymenu.editor.action.screens.add_action"), button -> {
            BuildActionScreen s = new BuildActionScreen(null, call -> {
                if (call != null) {
                    this.executableBlock.addExecutable(call);
                    this.updateActionInstanceScrollArea(false);
                }
                Minecraft.getInstance().setScreen(this);
            });
            Minecraft.getInstance().setScreen(s);
        });
        this.m_7787_(this.addActionButton);
        this.addActionButton.setTooltip(Tooltip.of(LocalizationUtils.splitLocalizedStringLines("fancymenu.editor.action.screens.add_action.desc")));
        UIBase.applyDefaultWidgetSkinTo(this.addActionButton);
        this.moveUpButton = new ExtendedButton(0, 0, 150, 20, I18n.get("fancymenu.editor.action.screens.move_action_up"), button -> this.moveUp(this.getSelectedEntry())) {

            @Override
            public void render(@NotNull GuiGraphics graphics, int p_93658_, int p_93659_, float p_93660_) {
                ManageActionsScreen s = ManageActionsScreen.this;
                if (!s.isAnyExecutableSelected()) {
                    this.setTooltip(Tooltip.of(LocalizationUtils.splitLocalizedStringLines("fancymenu.editor.action.screens.finish.no_action_selected")));
                    this.f_93623_ = false;
                } else {
                    this.setTooltip(Tooltip.of(LocalizationUtils.splitLocalizedStringLines("fancymenu.editor.action.screens.move_action_up.desc")));
                    this.f_93623_ = true;
                }
                super.render(graphics, p_93658_, p_93659_, p_93660_);
            }
        };
        this.m_7787_(this.moveUpButton);
        UIBase.applyDefaultWidgetSkinTo(this.moveUpButton);
        this.moveDownButton = new ExtendedButton(0, 0, 150, 20, I18n.get("fancymenu.editor.action.screens.move_action_down"), button -> this.moveDown(this.getSelectedEntry())) {

            @Override
            public void render(@NotNull GuiGraphics graphics, int p_93658_, int p_93659_, float p_93660_) {
                ManageActionsScreen s = ManageActionsScreen.this;
                if (!s.isAnyExecutableSelected()) {
                    this.setTooltip(Tooltip.of(LocalizationUtils.splitLocalizedStringLines("fancymenu.editor.action.screens.finish.no_action_selected")));
                    this.f_93623_ = false;
                } else {
                    this.setTooltip(Tooltip.of(LocalizationUtils.splitLocalizedStringLines("fancymenu.editor.action.screens.move_action_down.desc")));
                    this.f_93623_ = true;
                }
                super.render(graphics, p_93658_, p_93659_, p_93660_);
            }
        };
        this.m_7787_(this.moveDownButton);
        UIBase.applyDefaultWidgetSkinTo(this.moveDownButton);
        this.editButton = new ExtendedButton(0, 0, 150, 20, I18n.get("fancymenu.editor.action.screens.edit_action"), button -> {
            ManageActionsScreen.ExecutableEntry selected = this.getSelectedEntry();
            if (selected != null) {
                AbstractExecutableBlock block = selected.getParentBlock();
                if (selected.executable instanceof ActionInstance i) {
                    BuildActionScreen s = new BuildActionScreen(i.copy(false), call -> {
                        if (call != null) {
                            int index = block.getExecutables().indexOf(selected.executable);
                            block.getExecutables().remove(selected.executable);
                            if (index != -1) {
                                block.getExecutables().add(index, call);
                            } else {
                                block.getExecutables().add(call);
                            }
                            this.updateActionInstanceScrollArea(false);
                        }
                        Minecraft.getInstance().setScreen(this);
                    });
                    Minecraft.getInstance().setScreen(s);
                } else if (selected.executable instanceof IfExecutableBlock b) {
                    ManageRequirementsScreen s = new ManageRequirementsScreen(b.condition.copy(false), container -> {
                        if (container != null) {
                            b.condition = container;
                            this.updateActionInstanceScrollArea(true);
                        }
                        Minecraft.getInstance().setScreen(this);
                    });
                    Minecraft.getInstance().setScreen(s);
                } else if (selected.executable instanceof ElseIfExecutableBlock b) {
                    ManageRequirementsScreen s = new ManageRequirementsScreen(b.condition.copy(false), container -> {
                        if (container != null) {
                            b.condition = container;
                            this.updateActionInstanceScrollArea(true);
                        }
                        Minecraft.getInstance().setScreen(this);
                    });
                    Minecraft.getInstance().setScreen(s);
                }
            }
        }).setIsActiveSupplier(consumes -> {
            ManageActionsScreen.ExecutableEntry selected = this.getSelectedEntry();
            if (selected != null && !(selected.executable instanceof ElseExecutableBlock)) {
                consumes.setTooltip(Tooltip.of(LocalizationUtils.splitLocalizedStringLines("fancymenu.editor.action.screens.edit_action.desc")));
                return true;
            } else {
                consumes.setTooltip(Tooltip.of(LocalizationUtils.splitLocalizedStringLines("fancymenu.editor.action.screens.finish.no_action_selected")));
                return false;
            }
        });
        this.m_7787_(this.editButton);
        UIBase.applyDefaultWidgetSkinTo(this.editButton);
        this.removeButton = new ExtendedButton(0, 0, 150, 20, I18n.get("fancymenu.editor.action.screens.remove_action"), button -> {
            ManageActionsScreen.ExecutableEntry selected = this.getSelectedEntry();
            if (selected != null) {
                Minecraft.getInstance().setScreen(ConfirmationScreen.ofStrings(call -> {
                    if (call) {
                        if (selected.appendParent != null) {
                            selected.appendParent.setAppendedBlock(null);
                        }
                        selected.getParentBlock().getExecutables().remove(selected.executable);
                        this.updateActionInstanceScrollArea(true);
                    }
                    Minecraft.getInstance().setScreen(this);
                }, LocalizationUtils.splitLocalizedStringLines("fancymenu.editor.action.screens.remove_action.confirm")));
            }
        }) {

            @Override
            public void render(@NotNull GuiGraphics graphics, int p_93658_, int p_93659_, float p_93660_) {
                ManageActionsScreen s = ManageActionsScreen.this;
                if (!s.isAnyExecutableSelected()) {
                    this.setTooltip(Tooltip.of(LocalizationUtils.splitLocalizedStringLines("fancymenu.editor.action.screens.finish.no_action_selected")));
                    this.f_93623_ = false;
                } else {
                    this.setTooltip(Tooltip.of(LocalizationUtils.splitLocalizedStringLines("fancymenu.editor.action.screens.remove_action.desc")));
                    this.f_93623_ = true;
                }
                super.render(graphics, p_93658_, p_93659_, p_93660_);
            }
        };
        this.m_7787_(this.removeButton);
        UIBase.applyDefaultWidgetSkinTo(this.removeButton);
        this.doneButton = new ExtendedButton(0, 0, 150, 20, I18n.get("fancymenu.guicomponents.done"), button -> this.callback.accept(this.executableBlock));
        this.m_7787_(this.doneButton);
        UIBase.applyDefaultWidgetSkinTo(this.doneButton);
        this.cancelButton = new ExtendedButton(0, 0, 150, 20, Component.translatable("fancymenu.guicomponents.cancel"), button -> this.callback.accept(null));
        this.m_7787_(this.cancelButton);
        UIBase.applyDefaultWidgetSkinTo(this.cancelButton);
        this.doneButton.m_252865_(this.f_96543_ - 20 - this.doneButton.m_5711_());
        this.doneButton.m_253211_(this.f_96544_ - 20 - 20);
        this.cancelButton.m_252865_(this.f_96543_ - 20 - this.cancelButton.m_5711_());
        this.cancelButton.m_253211_(this.doneButton.m_252907_() - 5 - 20);
        this.removeButton.m_252865_(this.f_96543_ - 20 - this.removeButton.m_5711_());
        this.removeButton.m_253211_(this.cancelButton.m_252907_() - 15 - 20);
        this.editButton.m_252865_(this.f_96543_ - 20 - this.editButton.m_5711_());
        this.editButton.m_253211_(this.removeButton.m_252907_() - 5 - 20);
        this.moveDownButton.m_252865_(this.f_96543_ - 20 - this.moveDownButton.m_5711_());
        this.moveDownButton.m_253211_(this.editButton.m_252907_() - 5 - 20);
        this.moveUpButton.m_252865_(this.f_96543_ - 20 - this.moveUpButton.m_5711_());
        this.moveUpButton.m_253211_(this.moveDownButton.m_252907_() - 5 - 20);
        this.appendElseButton.m_252865_(this.f_96543_ - 20 - this.appendElseButton.m_5711_());
        this.appendElseButton.m_253211_(this.moveUpButton.m_252907_() - 15 - 20);
        this.appendElseIfButton.m_252865_(this.f_96543_ - 20 - this.appendElseIfButton.m_5711_());
        this.appendElseIfButton.m_253211_(this.appendElseButton.m_252907_() - 5 - 20);
        this.addIfButton.m_252865_(this.f_96543_ - 20 - this.addIfButton.m_5711_());
        this.addIfButton.m_253211_(this.appendElseIfButton.m_252907_() - 5 - 20);
        this.addActionButton.m_252865_(this.f_96543_ - 20 - this.addActionButton.m_5711_());
        this.addActionButton.m_253211_(this.addIfButton.m_252907_() - 5 - 20);
        AbstractWidget topRightSideWidget = this.addActionButton;
        Window window = Minecraft.getInstance().getWindow();
        boolean resized = window.getScreenWidth() != this.lastWidth || window.getScreenHeight() != this.lastHeight;
        this.lastWidth = window.getScreenWidth();
        this.lastHeight = window.getScreenHeight();
        if (topRightSideWidget.getY() < 20 && window.getGuiScale() > 1.0) {
            double newScale = window.getGuiScale();
            if (--newScale < 1.0) {
                newScale = 1.0;
            }
            window.setGuiScale(newScale);
            this.m_6574_(Minecraft.getInstance(), window.getGuiScaledWidth(), window.getGuiScaledHeight());
        } else if (topRightSideWidget.getY() >= 20 && resized) {
            RenderingUtils.resetGuiScale();
            this.m_6574_(Minecraft.getInstance(), window.getGuiScaledWidth(), window.getGuiScaledHeight());
        }
    }

    @Override
    public void onClose() {
        this.callback.accept(null);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        this.renderTickDragHoveredEntry = this.getDragHoveredEntry();
        this.renderTickDraggedEntry = this.getDraggedEntry();
        if (this.renderTickDraggedEntry != null) {
            float scrollOffset = 0.1F * this.actionsScrollArea.verticalScrollBar.getWheelScrollSpeed();
            if (MouseInput.getMouseY() <= this.actionsScrollArea.getInnerY()) {
                this.actionsScrollArea.verticalScrollBar.setScroll(this.actionsScrollArea.verticalScrollBar.getScroll() - scrollOffset);
            }
            if (MouseInput.getMouseY() >= this.actionsScrollArea.getInnerY() + this.actionsScrollArea.getInnerHeight()) {
                this.actionsScrollArea.verticalScrollBar.setScroll(this.actionsScrollArea.verticalScrollBar.getScroll() + scrollOffset);
            }
        }
        graphics.fill(0, 0, this.f_96543_, this.f_96544_, UIBase.getUIColorTheme().screen_background_color.getColorInt());
        Component titleComp = this.f_96539_.copy().withStyle(Style.EMPTY.withBold(true));
        graphics.drawString(this.f_96547_, titleComp, 20, 20, UIBase.getUIColorTheme().generic_text_base_color.getColorInt(), false);
        graphics.drawString(this.f_96547_, I18n.get("fancymenu.editor.action.screens.manage_screen.actions"), 20, 50, UIBase.getUIColorTheme().generic_text_base_color.getColorInt(), false);
        this.actionsScrollArea.setWidth(this.f_96543_ - 20 - 150 - 20 - 20, true);
        this.actionsScrollArea.setHeight(this.f_96544_ - 85, true);
        this.actionsScrollArea.setX(20, true);
        this.actionsScrollArea.setY(65, true);
        this.actionsScrollArea.render(graphics, mouseX, mouseY, partial);
        if (this.renderTickDragHoveredEntry != null) {
            int dY = this.renderTickDragHoveredEntry.getY();
            int dH = this.renderTickDragHoveredEntry.getHeight();
            if (this.renderTickDragHoveredEntry == this.BEFORE_FIRST) {
                dY = this.actionsScrollArea.getInnerY();
                dH = 1;
            }
            if (this.renderTickDragHoveredEntry == this.AFTER_LAST) {
                dY = this.actionsScrollArea.getInnerY() + this.actionsScrollArea.getInnerHeight() - 1;
                dH = 1;
            }
            graphics.fill(this.actionsScrollArea.getInnerX(), dY + dH - 1, this.actionsScrollArea.getInnerX() + this.actionsScrollArea.getInnerWidth(), dY + dH, UIBase.getUIColorTheme().description_area_text_color.getColorInt());
        }
        this.doneButton.render(graphics, mouseX, mouseY, partial);
        this.cancelButton.render(graphics, mouseX, mouseY, partial);
        this.removeButton.render(graphics, mouseX, mouseY, partial);
        this.editButton.render(graphics, mouseX, mouseY, partial);
        this.moveDownButton.render(graphics, mouseX, mouseY, partial);
        this.moveUpButton.render(graphics, mouseX, mouseY, partial);
        this.appendElseButton.render(graphics, mouseX, mouseY, partial);
        this.appendElseIfButton.render(graphics, mouseX, mouseY, partial);
        this.addIfButton.render(graphics, mouseX, mouseY, partial);
        this.addActionButton.render(graphics, mouseX, mouseY, partial);
        super.render(graphics, mouseX, mouseY, partial);
    }

    protected boolean isContentOfStatementChain(@NotNull ManageActionsScreen.ExecutableEntry entry, @NotNull List<ManageActionsScreen.ExecutableEntry> statementChain) {
        for (ManageActionsScreen.ExecutableEntry parentBlock : this.getParentBlockHierarchyOf(entry)) {
            if (statementChain.contains(parentBlock)) {
                return true;
            }
        }
        return false;
    }

    @NotNull
    protected List<ManageActionsScreen.ExecutableEntry> getParentBlockHierarchyOf(@NotNull ManageActionsScreen.ExecutableEntry entry) {
        List<ManageActionsScreen.ExecutableEntry> blocks = new ArrayList();
        ManageActionsScreen.ExecutableEntry e = entry;
        while (e != null) {
            e = e.parentBlock != null ? this.findEntryForExecutable(e.parentBlock) : null;
            if (e != null) {
                blocks.add(0, e);
            }
        }
        return blocks;
    }

    @NotNull
    protected List<ManageActionsScreen.ExecutableEntry> getStatementChainOf(@NotNull ManageActionsScreen.ExecutableEntry entry) {
        List<ManageActionsScreen.ExecutableEntry> entries = new ArrayList();
        if (entry.executable instanceof AbstractExecutableBlock) {
            List<ManageActionsScreen.ExecutableEntry> beforeEntry = new ArrayList();
            ManageActionsScreen.ExecutableEntry e1 = entry;
            while (e1 != null) {
                e1 = e1.appendParent != null ? this.findEntryForExecutable(e1.appendParent) : null;
                if (e1 != null) {
                    beforeEntry.add(0, e1);
                }
            }
            List<ManageActionsScreen.ExecutableEntry> afterEntry = new ArrayList();
            ManageActionsScreen.ExecutableEntry e2 = entry;
            while (e2 != null) {
                Executable appendChild = e2.executable;
                if (appendChild instanceof AbstractExecutableBlock) {
                    AbstractExecutableBlock b = (AbstractExecutableBlock) appendChild;
                    AbstractExecutableBlock appendChildx = b.getAppendedBlock();
                    e2 = appendChildx != null ? this.findEntryForExecutable(appendChildx) : null;
                    if (e2 != null) {
                        afterEntry.add(e2);
                    }
                }
            }
            entries.addAll(beforeEntry);
            entries.add(entry);
            entries.addAll(afterEntry);
        }
        return entries;
    }

    @Nullable
    protected ManageActionsScreen.ExecutableEntry getDragHoveredEntry() {
        ManageActionsScreen.ExecutableEntry draggedEntry = this.getDraggedEntry();
        if (draggedEntry != null) {
            if (MouseInput.getMouseY() <= this.actionsScrollArea.getInnerY() && this.actionsScrollArea.verticalScrollBar.getScroll() == 0.0F) {
                return this.BEFORE_FIRST;
            }
            if (MouseInput.getMouseY() >= this.actionsScrollArea.getInnerY() + this.actionsScrollArea.getInnerHeight() && this.actionsScrollArea.verticalScrollBar.getScroll() == 1.0F) {
                return this.AFTER_LAST;
            }
            for (ScrollAreaEntry e : this.actionsScrollArea.getEntries()) {
                if (e instanceof ManageActionsScreen.ExecutableEntry ee && e.getY() + e.getHeight() <= this.actionsScrollArea.getInnerY() + this.actionsScrollArea.getInnerHeight() && ee != draggedEntry && UIBase.isXYInArea(MouseInput.getMouseX(), MouseInput.getMouseY(), ee.getX(), ee.getY(), ee.getWidth(), ee.getHeight()) && this.actionsScrollArea.isMouseInsideArea()) {
                    List<ManageActionsScreen.ExecutableEntry> statementChain = new ArrayList();
                    if (draggedEntry.executable instanceof AbstractExecutableBlock) {
                        statementChain = this.getStatementChainOf(draggedEntry);
                    }
                    if (draggedEntry.executable instanceof AbstractExecutableBlock && statementChain.contains(ee)) {
                        return null;
                    }
                    if (ee.parentBlock != null && ee.parentBlock != this.executableBlock) {
                        ManageActionsScreen.ExecutableEntry pb = this.findEntryForExecutable(ee.parentBlock);
                        if (pb != null && statementChain.contains(pb)) {
                            return null;
                        }
                    }
                    if (draggedEntry.executable instanceof AbstractExecutableBlock && this.isContentOfStatementChain(ee, statementChain)) {
                        return null;
                    }
                    return ee;
                }
            }
        }
        return null;
    }

    @Nullable
    protected ManageActionsScreen.ExecutableEntry getDraggedEntry() {
        for (ScrollAreaEntry e : this.actionsScrollArea.getEntries()) {
            if (e instanceof ManageActionsScreen.ExecutableEntry ee && ee.dragging) {
                return ee;
            }
        }
        return null;
    }

    @Nullable
    protected ManageActionsScreen.ExecutableEntry findEntryForExecutable(Executable executable) {
        for (ScrollAreaEntry e : this.actionsScrollArea.getEntries()) {
            if (e instanceof ManageActionsScreen.ExecutableEntry ee && ee.executable == executable) {
                return ee;
            }
        }
        return null;
    }

    @Nullable
    protected ManageActionsScreen.ExecutableEntry getSelectedEntry() {
        ScrollAreaEntry e = this.actionsScrollArea.getFocusedEntry();
        return e instanceof ManageActionsScreen.ExecutableEntry ? (ManageActionsScreen.ExecutableEntry) e : null;
    }

    protected boolean isAnyExecutableSelected() {
        return this.getSelectedEntry() != null;
    }

    @Nullable
    protected ManageActionsScreen.ExecutableEntry getValidMoveToEntryBefore(@NotNull ManageActionsScreen.ExecutableEntry entry, boolean ignoreValidityChecks) {
        if (entry == this.BEFORE_FIRST) {
            return this.BEFORE_FIRST;
        } else {
            int index = this.actionsScrollArea.getEntries().size();
            boolean foundEntry = false;
            boolean foundValidMoveTo = false;
            for (ScrollAreaEntry e : Lists.reverse(this.actionsScrollArea.getEntries())) {
                if (e instanceof ManageActionsScreen.ExecutableEntry) {
                    ManageActionsScreen.ExecutableEntry ee = (ManageActionsScreen.ExecutableEntry) e;
                    index--;
                    if (e == entry) {
                        foundEntry = true;
                    } else {
                        if (!ignoreValidityChecks) {
                            List<ManageActionsScreen.ExecutableEntry> statementChain = new ArrayList();
                            if (entry.executable instanceof AbstractExecutableBlock) {
                                statementChain = this.getStatementChainOf(entry);
                            }
                            if (entry.executable instanceof AbstractExecutableBlock && statementChain.contains(ee)) {
                                continue;
                            }
                            if (ee.parentBlock != null && ee.parentBlock != this.executableBlock) {
                                ManageActionsScreen.ExecutableEntry pb = this.findEntryForExecutable(ee.parentBlock);
                                if (pb != null && statementChain.contains(pb)) {
                                    continue;
                                }
                            }
                            if (entry.executable instanceof AbstractExecutableBlock && this.isContentOfStatementChain(ee, statementChain)) {
                                continue;
                            }
                        }
                        if (foundEntry) {
                            foundValidMoveTo = true;
                            break;
                        }
                    }
                }
            }
            if (!foundValidMoveTo) {
                return null;
            } else {
                ScrollAreaEntry ex = this.actionsScrollArea.getEntry(index);
                return ex instanceof ManageActionsScreen.ExecutableEntry ? (ManageActionsScreen.ExecutableEntry) ex : null;
            }
        }
    }

    @Nullable
    protected ManageActionsScreen.ExecutableEntry getValidMoveToEntryAfter(@NotNull ManageActionsScreen.ExecutableEntry entry, boolean ignoreValidityChecks) {
        if (entry == this.AFTER_LAST) {
            return this.AFTER_LAST;
        } else {
            int index = -1;
            boolean foundEntry = false;
            boolean foundValidMoveTo = false;
            for (ScrollAreaEntry e : this.actionsScrollArea.getEntries()) {
                if (e instanceof ManageActionsScreen.ExecutableEntry) {
                    ManageActionsScreen.ExecutableEntry ee = (ManageActionsScreen.ExecutableEntry) e;
                    index++;
                    if (e == entry) {
                        foundEntry = true;
                    } else {
                        if (!ignoreValidityChecks) {
                            List<ManageActionsScreen.ExecutableEntry> statementChain = new ArrayList();
                            if (entry.executable instanceof AbstractExecutableBlock) {
                                statementChain = this.getStatementChainOf(entry);
                            }
                            if (entry.executable instanceof AbstractExecutableBlock && statementChain.contains(ee)) {
                                continue;
                            }
                            if (ee.parentBlock != null && ee.parentBlock != this.executableBlock) {
                                ManageActionsScreen.ExecutableEntry pb = this.findEntryForExecutable(ee.parentBlock);
                                if (pb != null && statementChain.contains(pb)) {
                                    continue;
                                }
                            }
                            if (entry.executable instanceof AbstractExecutableBlock && this.isContentOfStatementChain(ee, statementChain)) {
                                continue;
                            }
                        }
                        if (foundEntry) {
                            foundValidMoveTo = true;
                            break;
                        }
                    }
                }
            }
            if (!foundValidMoveTo) {
                return null;
            } else {
                ScrollAreaEntry ex = this.actionsScrollArea.getEntry(index);
                return ex instanceof ManageActionsScreen.ExecutableEntry ? (ManageActionsScreen.ExecutableEntry) ex : null;
            }
        }
    }

    protected void moveAfter(@NotNull ManageActionsScreen.ExecutableEntry entry, @NotNull ManageActionsScreen.ExecutableEntry moveAfter) {
        entry.getParentBlock().getExecutables().remove(entry.executable);
        int moveAfterIndex = Math.max(0, moveAfter.getParentBlock().getExecutables().indexOf(moveAfter.executable));
        if (moveAfter == this.BEFORE_FIRST) {
            this.executableBlock.getExecutables().add(0, entry.executable);
        } else if (moveAfter == this.AFTER_LAST) {
            this.executableBlock.getExecutables().add(entry.executable);
        } else if (moveAfter.executable instanceof AbstractExecutableBlock b) {
            b.getExecutables().add(0, entry.executable);
        } else {
            moveAfter.getParentBlock().getExecutables().add(moveAfterIndex + 1, entry.executable);
        }
        this.updateActionInstanceScrollArea(true);
        ManageActionsScreen.ExecutableEntry newEntry = this.findEntryForExecutable(entry.executable);
        if (newEntry != null) {
            newEntry.setSelected(true);
        }
    }

    protected void moveUp(ManageActionsScreen.ExecutableEntry entry) {
        if (entry != null) {
            if (entry.executable instanceof ActionInstance || entry.executable instanceof IfExecutableBlock) {
                boolean manualUpdate = false;
                if (this.actionsScrollArea.getEntries().indexOf(entry) == 1) {
                    this.moveAfter(entry, this.BEFORE_FIRST);
                } else if (entry.getParentBlock() != this.executableBlock && (entry.getParentBlock() instanceof ElseIfExecutableBlock || entry.getParentBlock() instanceof ElseExecutableBlock) && entry.getParentBlock().getExecutables().indexOf(entry.executable) == 0) {
                    ManageActionsScreen.ExecutableEntry parentBlock = this.findEntryForExecutable(entry.getParentBlock());
                    if (parentBlock != null) {
                        entry.getParentBlock().getExecutables().remove(entry.executable);
                        if (parentBlock.appendParent != null) {
                            parentBlock.appendParent.getExecutables().add(entry.executable);
                            manualUpdate = true;
                        }
                    }
                } else if (entry.getParentBlock() != this.executableBlock && entry.getParentBlock() instanceof IfExecutableBlock && entry.getParentBlock().getExecutables().indexOf(entry.executable) == 0) {
                    ManageActionsScreen.ExecutableEntry parentBlock = this.findEntryForExecutable(entry.getParentBlock());
                    if (parentBlock != null) {
                        int parentIndex = Math.max(0, parentBlock.getParentBlock().getExecutables().indexOf(parentBlock.executable));
                        entry.getParentBlock().getExecutables().remove(entry.executable);
                        parentBlock.getParentBlock().getExecutables().add(parentIndex, entry.executable);
                        manualUpdate = true;
                    }
                } else {
                    ManageActionsScreen.ExecutableEntry before = this.getValidMoveToEntryBefore(entry, false);
                    if (before != null) {
                        boolean isMovable = entry.executable instanceof IfExecutableBlock || entry.executable instanceof ActionInstance;
                        if (isMovable && before.executable instanceof AbstractExecutableBlock b && entry.getParentBlock() != b) {
                            this.moveAfter(entry, before);
                        } else if (isMovable && !(before.executable instanceof AbstractExecutableBlock) && before.getParentBlock().getExecutables().indexOf(before.executable) == before.getParentBlock().getExecutables().size() - 1) {
                            this.moveAfter(entry, before);
                        } else {
                            ManageActionsScreen.ExecutableEntry beforeBefore = this.getValidMoveToEntryBefore(before, true);
                            if (beforeBefore != null) {
                                this.moveAfter(entry, beforeBefore);
                            }
                        }
                    }
                }
                if (manualUpdate) {
                    this.updateActionInstanceScrollArea(true);
                    ManageActionsScreen.ExecutableEntry newEntry = this.findEntryForExecutable(entry.executable);
                    if (newEntry != null) {
                        newEntry.setSelected(true);
                    }
                }
                return;
            }
            if (entry.executable instanceof ElseIfExecutableBlock ei) {
                AbstractExecutableBlock entryAppendParent = entry.appendParent;
                if (entryAppendParent != null) {
                    ManageActionsScreen.ExecutableEntry appendParentEntry = this.findEntryForExecutable(entryAppendParent);
                    if (appendParentEntry != null) {
                        AbstractExecutableBlock parentOfParent = appendParentEntry.appendParent;
                        if (parentOfParent != null) {
                            entryAppendParent.setAppendedBlock(ei.getAppendedBlock());
                            ei.setAppendedBlock(entryAppendParent);
                            parentOfParent.setAppendedBlock(ei);
                        }
                    }
                }
            }
            this.updateActionInstanceScrollArea(true);
            ManageActionsScreen.ExecutableEntry newEntry = this.findEntryForExecutable(entry.executable);
            if (newEntry != null) {
                newEntry.setSelected(true);
            }
        }
    }

    protected void moveDown(ManageActionsScreen.ExecutableEntry entry) {
        if (entry != null) {
            if (entry.executable instanceof ActionInstance || entry.executable instanceof IfExecutableBlock) {
                boolean manualUpdate = false;
                if (entry.getParentBlock() != this.executableBlock && entry.getParentBlock().getAppendedBlock() == null && entry.getParentBlock().getExecutables().indexOf(entry.executable) == entry.getParentBlock().getExecutables().size() - 1) {
                    ManageActionsScreen.ExecutableEntry parentBlock = this.findEntryForExecutable(entry.getParentBlock());
                    if (parentBlock != null) {
                        int parentIndex = -1;
                        if (parentBlock.executable instanceof IfExecutableBlock) {
                            parentIndex = Math.max(0, parentBlock.getParentBlock().getExecutables().indexOf(parentBlock.executable));
                        } else {
                            List<ManageActionsScreen.ExecutableEntry> chain = this.getStatementChainOf(parentBlock);
                            if (!chain.isEmpty()) {
                                parentIndex = ((ManageActionsScreen.ExecutableEntry) chain.get(0)).getParentBlock().getExecutables().indexOf(((ManageActionsScreen.ExecutableEntry) chain.get(0)).executable);
                            }
                        }
                        if (parentIndex != -1) {
                            entry.getParentBlock().getExecutables().remove(entry.executable);
                            parentBlock.getParentBlock().getExecutables().add(parentIndex + 1, entry.executable);
                            manualUpdate = true;
                        }
                    }
                } else {
                    ManageActionsScreen.ExecutableEntry after = this.getValidMoveToEntryAfter(entry, false);
                    if (after != null) {
                        this.moveAfter(entry, after);
                    }
                }
                if (manualUpdate) {
                    this.updateActionInstanceScrollArea(true);
                    ManageActionsScreen.ExecutableEntry newEntry = this.findEntryForExecutable(entry.executable);
                    if (newEntry != null) {
                        newEntry.setSelected(true);
                    }
                }
                return;
            }
            if (entry.executable instanceof ElseIfExecutableBlock ei) {
                AbstractExecutableBlock entryAppendChild = ei.getAppendedBlock();
                AbstractExecutableBlock entryAppendParent = entry.appendParent;
                if (entryAppendChild instanceof ElseIfExecutableBlock && entryAppendParent != null) {
                    ei.setAppendedBlock(entryAppendChild.getAppendedBlock());
                    entryAppendChild.setAppendedBlock(ei);
                    entryAppendParent.setAppendedBlock(entryAppendChild);
                }
            }
            this.updateActionInstanceScrollArea(true);
            ManageActionsScreen.ExecutableEntry newEntry = this.findEntryForExecutable(entry.executable);
            if (newEntry != null) {
                newEntry.setSelected(true);
            }
        }
    }

    protected void updateActionInstanceScrollArea(boolean keepScroll) {
        for (ScrollAreaEntry e : this.actionsScrollArea.getEntries()) {
            if (e instanceof ManageActionsScreen.ExecutableEntry ee) {
                ee.leftMouseDownDragging = false;
                ee.dragging = false;
            }
        }
        float oldScrollVertical = this.actionsScrollArea.verticalScrollBar.getScroll();
        float oldScrollHorizontal = this.actionsScrollArea.horizontalScrollBar.getScroll();
        this.actionsScrollArea.clearEntries();
        this.addExecutableToEntries(-1, this.executableBlock, null, null);
        if (keepScroll) {
            this.actionsScrollArea.verticalScrollBar.setScroll(oldScrollVertical);
            this.actionsScrollArea.horizontalScrollBar.setScroll(oldScrollHorizontal);
        }
    }

    protected void addExecutableToEntries(int level, Executable executable, @Nullable AbstractExecutableBlock appendParent, @Nullable AbstractExecutableBlock parentBlock) {
        if (level >= 0) {
            ManageActionsScreen.ExecutableEntry entry = new ManageActionsScreen.ExecutableEntry(this.actionsScrollArea, executable, 14, level);
            entry.appendParent = appendParent;
            entry.parentBlock = parentBlock;
            this.actionsScrollArea.addEntry(entry);
        }
        if (executable instanceof AbstractExecutableBlock b) {
            for (Executable e : b.getExecutables()) {
                this.addExecutableToEntries(level + 1, e, null, b);
            }
            if (b.getAppendedBlock() != null) {
                this.addExecutableToEntries(level, b.getAppendedBlock(), b, parentBlock);
            }
        }
    }

    public class ExecutableEntry extends ScrollAreaEntry {

        public static final int HEADER_FOOTER_HEIGHT = 3;

        public static final int INDENT_X_OFFSET = 20;

        @NotNull
        public Executable executable;

        @Nullable
        public AbstractExecutableBlock parentBlock;

        @Nullable
        public AbstractExecutableBlock appendParent;

        public final int lineHeight;

        public Font font;

        public int indentLevel;

        public boolean leftMouseDownDragging;

        public double leftMouseDownDraggingPosX;

        public double leftMouseDownDraggingPosY;

        public boolean dragging;

        private final MutableComponent displayNameComponent;

        private final MutableComponent valueComponent;

        public ExecutableEntry(@NotNull ScrollArea parentScrollArea, @NotNull Executable executable, int lineHeight, int indentLevel) {
            super(parentScrollArea, 100, 30);
            this.font = Minecraft.getInstance().font;
            this.leftMouseDownDragging = false;
            this.leftMouseDownDraggingPosX = 0.0;
            this.leftMouseDownDraggingPosY = 0.0;
            this.dragging = false;
            this.executable = executable;
            this.lineHeight = lineHeight;
            this.indentLevel = indentLevel;
            if (this.executable instanceof ActionInstance i) {
                this.displayNameComponent = i.action.getActionDisplayName().copy().setStyle(Style.EMPTY.withColor(UIBase.getUIColorTheme().description_area_text_color.getColorInt()));
                String cachedValue = i.value;
                String valueString = cachedValue != null && i.action.hasValue() ? cachedValue : I18n.get("fancymenu.editor.action.screens.manage_screen.info.value.none");
                this.valueComponent = Component.literal(I18n.get("fancymenu.editor.action.screens.manage_screen.info.value") + " ").setStyle(Style.EMPTY.withColor(UIBase.getUIColorTheme().description_area_text_color.getColorInt())).append(Component.literal(valueString).setStyle(Style.EMPTY.withColor(UIBase.getUIColorTheme().element_label_color_normal.getColorInt())));
            } else if (this.executable instanceof IfExecutableBlock b) {
                String requirements = "";
                for (LoadingRequirementGroup g : b.condition.getGroups()) {
                    if (!requirements.isEmpty()) {
                        requirements = requirements + ", ";
                    }
                    requirements = requirements + g.identifier;
                }
                for (LoadingRequirementInstance i : b.condition.getInstances()) {
                    if (!requirements.isEmpty()) {
                        requirements = requirements + ", ";
                    }
                    requirements = requirements + i.requirement.getDisplayName();
                }
                this.displayNameComponent = Component.translatable("fancymenu.editor.actions.blocks.if", Component.literal(requirements)).setStyle(Style.EMPTY.withColor(UIBase.getUIColorTheme().description_area_text_color.getColorInt()));
                this.valueComponent = Component.empty();
            } else if (this.executable instanceof ElseIfExecutableBlock b) {
                String requirements = "";
                for (LoadingRequirementGroup g : b.condition.getGroups()) {
                    if (!requirements.isEmpty()) {
                        requirements = requirements + ", ";
                    }
                    requirements = requirements + g.identifier;
                }
                for (LoadingRequirementInstance i : b.condition.getInstances()) {
                    if (!requirements.isEmpty()) {
                        requirements = requirements + ", ";
                    }
                    requirements = requirements + i.requirement.getDisplayName();
                }
                this.displayNameComponent = Component.translatable("fancymenu.editor.actions.blocks.else_if", Component.literal(requirements)).setStyle(Style.EMPTY.withColor(UIBase.getUIColorTheme().description_area_text_color.getColorInt()));
                this.valueComponent = Component.empty();
            } else if (this.executable instanceof ElseExecutableBlock b) {
                this.displayNameComponent = Component.translatable("fancymenu.editor.actions.blocks.else").setStyle(Style.EMPTY.withColor(UIBase.getUIColorTheme().description_area_text_color.getColorInt()));
                this.valueComponent = Component.empty();
            } else {
                this.displayNameComponent = Component.literal("[UNKNOWN EXECUTABLE]").withStyle(ChatFormatting.RED);
                this.valueComponent = Component.empty();
            }
            this.setWidth(this.calculateWidth());
            if (this.executable instanceof AbstractExecutableBlock) {
                this.setHeight(lineHeight + 6);
            } else {
                this.setHeight(lineHeight * 2 + 6);
            }
        }

        @Override
        public void render(GuiGraphics graphics, int mouseX, int mouseY, float partial) {
            this.handleDragging();
            super.render(graphics, mouseX, mouseY, partial);
            int centerYLine1 = this.getY() + 3 + this.lineHeight / 2;
            int centerYLine2 = this.getY() + 3 + this.lineHeight / 2 * 3;
            RenderSystem.enableBlend();
            int renderX = this.getX() + 20 * this.indentLevel;
            if (this.executable instanceof ActionInstance) {
                renderListingDot(graphics, renderX + 5, centerYLine1 - 2, UIBase.getUIColorTheme().listing_dot_color_2.getColor());
                graphics.drawString(this.font, this.displayNameComponent, renderX + 5 + 4 + 3, centerYLine1 - 9 / 2, -1, false);
                renderListingDot(graphics, renderX + 5 + 4 + 3, centerYLine2 - 2, UIBase.getUIColorTheme().listing_dot_color_1.getColor());
                graphics.drawString(this.font, this.valueComponent, renderX + 5 + 4 + 3 + 4 + 3, centerYLine2 - 9 / 2, -1, false);
            } else {
                renderListingDot(graphics, renderX + 5, centerYLine1 - 2, UIBase.getUIColorTheme().warning_text_color.getColor());
                graphics.drawString(this.font, this.displayNameComponent, renderX + 5 + 4 + 3, centerYLine1 - 9 / 2, -1, false);
            }
        }

        protected void handleDragging() {
            if (!MouseInput.isLeftMouseDown()) {
                if (this.dragging) {
                    ManageActionsScreen.ExecutableEntry hover = ManageActionsScreen.this.renderTickDragHoveredEntry;
                    if (hover != null && ManageActionsScreen.this.renderTickDraggedEntry == this) {
                        ManageActionsScreen.this.moveAfter(this, hover);
                    }
                }
                this.leftMouseDownDragging = false;
                this.dragging = false;
            }
            if (this.leftMouseDownDragging && (this.leftMouseDownDraggingPosX != (double) MouseInput.getMouseX() || this.leftMouseDownDraggingPosY != (double) MouseInput.getMouseY()) && (!(this.executable instanceof AbstractExecutableBlock) || this.executable instanceof IfExecutableBlock)) {
                this.dragging = true;
            }
        }

        @NotNull
        public AbstractExecutableBlock getParentBlock() {
            return (AbstractExecutableBlock) (this.parentBlock == null ? ManageActionsScreen.this.executableBlock : this.parentBlock);
        }

        private int calculateWidth() {
            int w = 12 + this.font.width(this.displayNameComponent) + 5;
            int w2 = 19 + this.font.width(this.valueComponent) + 5;
            if (w2 > w) {
                w = w2;
            }
            return w + 20 * this.indentLevel;
        }

        @Override
        public void onClick(ScrollAreaEntry entry) {
            if (this.parent.getEntries().contains(this)) {
                this.leftMouseDownDragging = true;
                this.leftMouseDownDraggingPosX = (double) MouseInput.getMouseX();
                this.leftMouseDownDraggingPosY = (double) MouseInput.getMouseY();
            }
        }
    }
}