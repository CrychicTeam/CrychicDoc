package com.mna.gui.block;

import com.mna.ManaAndArtifice;
import com.mna.Registries;
import com.mna.api.entities.construct.ai.ConstructTask;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskIntegerParameter;
import com.mna.gui.GuiTextures;
import com.mna.gui.base.GuiJEIDisable;
import com.mna.gui.containers.block.ContainerLodestar;
import com.mna.gui.widgets.lodestar.AreaParameterInput;
import com.mna.gui.widgets.lodestar.BooleanParameterInput;
import com.mna.gui.widgets.lodestar.FilterParameterInput;
import com.mna.gui.widgets.lodestar.IntegerParameterInput;
import com.mna.gui.widgets.lodestar.ItemStackParameterInput;
import com.mna.gui.widgets.lodestar.LodestarGroup;
import com.mna.gui.widgets.lodestar.LodestarNode;
import com.mna.gui.widgets.lodestar.LodestarParameter;
import com.mna.gui.widgets.lodestar.PointParameterInput;
import com.mna.tools.math.MathUtils;
import com.mna.tools.render.GuiRenderUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.mutable.MutableInt;

public class GuiLodestarV2 extends GuiJEIDisable<ContainerLodestar> {

    public static final int MAIN_WIDTH = 256;

    public static final int MAIN_HEIGHT = 165;

    public static final int BACKGROUND_SIZE = 512;

    public static final int BACKGROUND_OFFSET = 10;

    public static final int COMMANDS_WIDTH = 56;

    public static final int COMMANDS_HEIGHT = 164;

    public static final int PARAMS_WIDTH = 68;

    public static final int PARAMS_HEIGHT = 164;

    public static final int PARAMS_START_X = 5;

    public static final int PARAMS_START_Y = 21;

    public static final int PARAMS_INCREMENT_Y = 22;

    public static final int START_BUTTON_X = 4;

    public static final int START_BUTTON_Y = 10;

    public static final int START_BUTTON_SIZE = 8;

    private GuiLodestarV2.CommandScrollList actionsList;

    private EditBox groupBox;

    private EditBox filterBox;

    private Component currentGroupBoxInput;

    private Component currentFilterBoxInput;

    private Component currentTooltip;

    public static float Zoom = 1.0F;

    private int scroll_pos_x = 0;

    private int scroll_pos_y = 0;

    private double scroll_accum_x = 0.0;

    private double scroll_accum_y = 0.0;

    private boolean canBackgroundScroll = false;

    private List<LodestarNode> nodes;

    private List<LodestarGroup> nodeGroups;

    private LodestarNode selectedNode;

    private boolean drawingGroup = false;

    private List<LodestarParameter<?>> parameterWidgets;

    private LodestarNode lineConnectionOrigin;

    private int lineConnectionOriginNodeIndex = -1;

    private boolean draggingStartNode = false;

    private ImageButton startNodeButton = null;

    private LodestarNode draggingNode;

    private LodestarGroup draggingGroup;

    private List<LodestarNode> draggingGroupNodes;

    private LodestarGroup creatingGroup;

    private LodestarGroup selectedGroup;

    private ConstructTask draggingOutTask;

    public GuiLodestarV2(ContainerLodestar screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.f_97726_ = 256;
        this.f_97727_ = 256;
        this.nodes = new ArrayList();
        this.draggingGroupNodes = new ArrayList();
        this.nodeGroups = new ArrayList();
        this.parameterWidgets = new ArrayList();
    }

    @Override
    protected void init() {
        super.m_7856_();
        this.nodes.clear();
        this.nodeGroups.clear();
        this.selectedNode = null;
        this.draggingNode = null;
        this.draggingOutTask = null;
        this.parameterWidgets.clear();
        this.m_169413_();
        this.actionsList = new GuiLodestarV2.CommandScrollList(((ContainerLodestar) this.f_97732_).isLowTier());
        this.m_142416_(this.actionsList);
        this.groupBox = new EditBox(this.f_96541_.font, this.f_97735_ + 4, this.f_97736_ - 12, this.getXSize() - 8, 12, this.currentGroupBoxInput);
        this.groupBox.setMaxLength(100);
        this.groupBox.setResponder(this::onGroupTextChanged);
        this.groupBox.f_93623_ = false;
        this.groupBox.f_93624_ = false;
        this.m_142416_(this.groupBox);
        this.filterBox = new EditBox(this.f_96541_.font, this.f_97735_ - 51, this.f_97736_ - (((ContainerLodestar) this.f_97732_).isLowTier() ? 7 : 5), 50, 12, this.currentFilterBoxInput);
        this.filterBox.setMaxLength(20);
        this.filterBox.setResponder(this::onFilterTextChanged);
        this.filterBox.f_93623_ = true;
        this.filterBox.f_93624_ = true;
        this.m_142416_(this.filterBox);
        this.startNodeButton = new ImageButton(this.f_97735_ + 4, this.f_97736_ + 10, 8, 8, 0, 216, 0, this.getMainTexture(), 256, 256, b -> this.draggingStartNode = true);
        this.m_142416_(this.startNodeButton);
        ImageButton recenterButton = new ImageButton(this.f_97735_ + 14, this.f_97736_ + 165, 14, 14, 140, 242, 0, this.getMainTexture(), 256, 256, b -> this.recenterView());
        recenterButton.m_257544_(Tooltip.create(Component.translatable("gui.mna.lodestar.recenter")));
        this.m_142416_(recenterButton);
        ImageButton drawGroupButton = new ImageButton(this.f_97735_ + this.getXSize() - 28, this.f_97736_ + 165, 14, 14, 140, 228, 0, this.getMainTexture(), 256, 256, b -> this.drawingGroup = !this.drawingGroup);
        drawGroupButton.m_257544_(Tooltip.create(Component.translatable("gui.mna.lodestar.draw_group_toggle")));
        this.m_142416_(drawGroupButton);
        this.loadLogic(((ContainerLodestar) this.f_97732_).getLogic());
        this.recenterView();
    }

    private void onGroupTextChanged(String newText) {
        if (this.selectedGroup != null) {
            this.selectedGroup.setLabel(newText);
        }
    }

    private void onFilterTextChanged(String newText) {
        this.actionsList.setFilter(newText);
    }

    private void addParameter(LodestarParameter<?> btn) {
        this.parameterWidgets.add(btn);
        this.m_142416_(btn);
    }

    private void addNode(int x, int y, ConstructTask task) {
        LodestarNode added = new LodestarNode(this.f_97735_ + x, this.f_97736_ + y, ((ContainerLodestar) this.f_97732_).isLowTier(), task, b -> this.nodeSubClick((LodestarNode) b), this::getNodeById);
        this.insertNode(added);
    }

    private void insertNode(LodestarNode node) {
        this.nodes.add(node);
        this.m_7787_(node);
    }

    private void deleteNode(LodestarNode node) {
        if (this.selectedNode == node) {
            this.deselectNode();
        }
        this.nodes.remove(node);
        this.m_169411_(node);
    }

    private void groupClicked(LodestarGroup group, boolean forDelete) {
        if (forDelete) {
            this.nodeGroups.remove(group);
        } else {
            double clickX = group.getClickX();
            double clickY = group.getClickY();
            if (this.isMouseWithinStage(clickX * (double) Zoom, clickY * (double) Zoom)) {
                this.f_96541_.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                this.draggingGroup = group;
                this.draggingGroupNodes = this.getNodesInGroup(group);
                this.selectedGroup = group;
                this.groupBox.f_93623_ = true;
                this.groupBox.f_93624_ = true;
                this.groupBox.setValue(group.getLabel());
            }
        }
    }

    private void nodeSubClick(LodestarNode node) {
        double clickX = node.getClickX();
        double clickY = node.getClickY();
        if (this.isMouseWithinStage(clickX * (double) Zoom, clickY * (double) Zoom)) {
            int widgetIndex = node.getSubClickIndex();
            this.f_96541_.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            switch(widgetIndex) {
                case 97:
                    this.deleteNode(node);
                    ((ContainerLodestar) this.f_97732_).updateTileLogic(this.saveLogic(), false);
                    return;
                case 98:
                    this.nodes.forEach(n -> n.disconnectFromNode(node));
                    ((ContainerLodestar) this.f_97732_).updateTileLogic(this.saveLogic(), false);
                    return;
                case 99:
                    this.draggingNode = node;
                    this.selectNode(node);
                    return;
                default:
                    this.lineConnectionOrigin = node;
                    this.lineConnectionOriginNodeIndex = widgetIndex;
            }
        }
    }

    private void deselectNode() {
        if (this.selectedNode != null) {
            this.selectedNode.saveParameters(this.parameterWidgets);
            this.parameterWidgets.forEach(w -> this.m_169411_(w));
            this.parameterWidgets.clear();
            this.selectedNode.deselect();
            this.selectedNode = null;
        }
    }

    private void selectNode(LodestarNode node) {
        if (this.selectedNode != null) {
            this.deselectNode();
        }
        MutableInt paramIndex = new MutableInt(0);
        node.getParameters().forEach(param -> {
            switch(param.getTypeID()) {
                case AREA:
                    this.addParameter(new AreaParameterInput(((ContainerLodestar) this.f_97732_).isLowTier(), this.getParameterX(), this.getParameterY(paramIndex.getValue()), b -> {
                    }, param.getTooltip()));
                    break;
                case BOOLEAN:
                    this.addParameter(new BooleanParameterInput(((ContainerLodestar) this.f_97732_).isLowTier(), this.getParameterX(), this.getParameterY(paramIndex.getValue()), b -> {
                    }, param.getTooltip()));
                    break;
                case FILTER:
                    this.addParameter(new FilterParameterInput(((ContainerLodestar) this.f_97732_).isLowTier(), this.getParameterX(), this.getParameterY(paramIndex.getValue()), b -> {
                    }, param.getTooltip()));
                    break;
                case INTEGER:
                    this.addParameter(new IntegerParameterInput(((ContainerLodestar) this.f_97732_).isLowTier(), this.getParameterX(), this.getParameterY(paramIndex.getValue()), ((ConstructTaskIntegerParameter) param).getMinimum(), ((ConstructTaskIntegerParameter) param).getMinimum(), ((ConstructTaskIntegerParameter) param).getMaximum(), b -> {
                    }, param.getTooltip()));
                    break;
                case POINT:
                    this.addParameter(new PointParameterInput(((ContainerLodestar) this.f_97732_).isLowTier(), this.getParameterX(), this.getParameterY(paramIndex.getValue()), b -> {
                    }, param.getTooltip()));
                    break;
                case ITEMSTACK:
                    this.addParameter(new ItemStackParameterInput(((ContainerLodestar) this.f_97732_).isLowTier(), this.getParameterX(), this.getParameterY(paramIndex.getValue()), b -> {
                    }, param.getTooltip()));
                    break;
                default:
                    ManaAndArtifice.LOGGER.error("Unknown parameter type for construct task!  No known parameter widget.  Skipping.");
            }
            paramIndex.increment();
        });
        this.selectedNode = node;
        this.selectedNode.select();
        this.selectedNode.loadParameters(this.parameterWidgets);
    }

    private void panBackground(double deltaX, double deltaY) {
        if (this.canBackgroundScroll) {
            this.scroll_accum_x = this.scroll_accum_x + deltaX / (double) Zoom;
            this.scroll_accum_y = this.scroll_accum_y + deltaY / (double) Zoom;
            if (Math.abs(this.scroll_accum_x) > 1.0 || Math.abs(this.scroll_accum_y) > 1.0) {
                int dX = (int) (Math.floor(Math.abs(this.scroll_accum_x)) * Math.signum(this.scroll_accum_x));
                int dY = (int) (Math.floor(Math.abs(this.scroll_accum_y)) * Math.signum(this.scroll_accum_y));
                this.scroll_accum_x -= (double) dX;
                this.scroll_accum_y -= (double) dY;
                int scroll_pos_x_o = this.scroll_pos_x;
                int scroll_pos_y_o = this.scroll_pos_y;
                this.scroll_pos_x -= dX;
                this.scroll_pos_y -= dY;
                int adX = scroll_pos_x_o - this.scroll_pos_x;
                int adY = scroll_pos_y_o - this.scroll_pos_y;
                for (int i = 0; i < this.nodes.size(); i++) {
                    LodestarNode node = (LodestarNode) this.nodes.get(i);
                    node.m_252865_(node.m_252754_() + adX);
                    node.m_253211_(node.m_252907_() + adY);
                }
                for (int i = 0; i < this.nodeGroups.size(); i++) {
                    LodestarGroup node = (LodestarGroup) this.nodeGroups.get(i);
                    node.m_252865_(node.m_252754_() + adX);
                    node.m_253211_(node.m_252907_() + adY);
                }
            }
        }
    }

    private void startDraggingCommandFromList(ConstructTask action) {
        this.draggingOutTask = action;
    }

    @Override
    protected boolean hasClickedOutside(double mouseX, double mouseY, int leftPos, int topPos, int button) {
        if (mouseX >= (double) (this.f_97735_ - 56) && mouseX < (double) (this.f_97735_ + 256 + 68) && mouseY >= (double) this.f_97736_ && mouseY <= (double) (this.f_97736_ + 165)) {
            return false;
        } else {
            int halfPlayerInvWidth = 88;
            return !(mouseX >= (double) (this.f_97735_ + this.f_97726_ / 2 - halfPlayerInvWidth)) || !(mouseX <= (double) (this.f_97735_ + this.f_97726_ / 2 + halfPlayerInvWidth)) || !(mouseY >= (double) (this.f_97736_ + 165)) || !(mouseY <= (double) (this.f_97736_ + this.f_97727_));
        }
    }

    private ResourceLocation getMainTexture() {
        return ((ContainerLodestar) this.f_97732_).isLowTier() ? GuiTextures.Blocks.LODESTAR_LESSER_MAIN : GuiTextures.Blocks.LODESTAR_MAIN;
    }

    private ResourceLocation getBackgroundTexture() {
        return ((ContainerLodestar) this.f_97732_).isLowTier() ? GuiTextures.Blocks.LODESTAR_LESSER_BACKGROUND : GuiTextures.Blocks.LODESTAR_BACKGROUND;
    }

    private ResourceLocation getExtensionTexture() {
        return ((ContainerLodestar) this.f_97732_).isLowTier() ? GuiTextures.Blocks.LODESTAR_LESSER_EXTENSION : GuiTextures.Blocks.LODESTAR_EXTENSION;
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float partialTick, int mouseX, int mouseY) {
        this.m_280273_(pGuiGraphics);
        RenderSystem.setShader(GameRenderer::m_172817_);
        pGuiGraphics.blit(this.getMainTexture(), this.f_97735_ - 45, this.f_97736_ - 45, 166, 166, 90, 90);
        pGuiGraphics.blit(this.getMainTexture(), this.f_97735_ + 211, this.f_97736_ + 121, 166, 166, 90, 90);
        this.renderNodes(pGuiGraphics, partialTick, mouseX, mouseY);
        pGuiGraphics.blit(this.getMainTexture(), this.f_97735_, this.f_97736_, 0, 0, 256, 165);
        pGuiGraphics.blit(this.getMainTexture(), this.f_97735_, this.f_97736_ + 7, 0, 187, 8, 14);
        pGuiGraphics.blit(this.getExtensionTexture(), this.f_97735_ - 56 + 2, this.f_97736_ - 2, 0, 0, 56, 164);
        pGuiGraphics.blit(this.getExtensionTexture(), this.f_97735_ + 256 - 2, this.f_97736_ - 2, 56, 0, 68, 164);
        GuiRenderUtils.renderStandardPlayerInventory(pGuiGraphics, this.f_97735_ + this.f_97726_ / 2, this.f_97736_ + 165 + 45);
        if (this.draggingOutTask != null) {
            pGuiGraphics.blit(this.draggingOutTask.getIconTexture(), mouseX - 8, mouseY - 8, 0, 0.0F, 0.0F, 16, 16, 16, 16);
        }
        if (this.selectedNode != null) {
            pGuiGraphics.blit(this.selectedNode.getTask().getIconTexture(), this.f_97735_ + 256 + 24, this.f_97736_, 0.0F, 0.0F, 16, 16, 16, 16);
        }
    }

    private void renderNodes(GuiGraphics pGuiGraphics, float partialTick, int mouseX, int mouseY) {
        Pair<Integer, Integer> mouseScaled = this.getScaledMouseCoordinates((double) mouseX, (double) mouseY);
        pGuiGraphics.enableScissor(this.f_97735_, this.f_97736_, this.f_97735_ + 256, this.f_97736_ + 165);
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(Zoom, Zoom, Zoom);
        pGuiGraphics.blit(this.getBackgroundTexture(), (int) ((float) (this.f_97735_ + 5) / Zoom), (int) ((float) (this.f_97736_ + 5) / Zoom), (float) Math.round((float) this.scroll_pos_x), (float) Math.round((float) this.scroll_pos_y), (int) (246.0F / Zoom), (int) (155.0F / Zoom), 512, 512);
        this.nodeGroups.forEach(group -> group.m_88315_(pGuiGraphics, mouseX, mouseY, partialTick));
        this.nodes.forEach(nodex -> nodex.renderConnections(pGuiGraphics, this.f_97735_, this.f_97736_, partialTick));
        this.nodes.forEach(nodex -> {
            nodex.m_88315_(pGuiGraphics, (Integer) mouseScaled.getFirst(), (Integer) mouseScaled.getSecond(), partialTick);
            nodex.renderMisconfigured(pGuiGraphics, ((ContainerLodestar) this.f_97732_).getMisconfiguredNodeIDs());
        });
        if (this.lineConnectionOrigin != null || this.draggingStartNode) {
            int lineColor = LodestarNode.COLOR_RED;
            if (this.isMouseWithinStage((double) mouseX, (double) mouseY)) {
                Optional<GuiEventListener> node = this.m_94729_((double) mouseX, (double) mouseY);
                if (node.isPresent() && node.get() instanceof LodestarNode) {
                    int idx = ((LodestarNode) node.get()).getSubWidgetAt((double) mouseX, (double) mouseY);
                    if (idx == 98) {
                        lineColor = this.draggingStartNode ? LodestarNode.COLOR_BLUE : LodestarNode.COLOR_GREEN;
                    }
                }
            }
            if (this.lineConnectionOrigin != null) {
                this.lineConnectionOrigin.renderConnectingLine(pGuiGraphics, this.lineConnectionOriginNodeIndex, (float) ((Integer) mouseScaled.getFirst()).intValue(), (float) ((Integer) mouseScaled.getSecond()).intValue(), lineColor);
            } else {
                GuiRenderUtils.bezierLineBetween(pGuiGraphics, (float) ((int) ((float) (this.f_97735_ + 4 + 4 + 3) / Zoom)), (float) ((int) (((float) (this.f_97736_ + 10 + 4) + 0.5F) / Zoom)), (float) ((Integer) mouseScaled.getFirst()).intValue(), (float) ((Integer) mouseScaled.getSecond()).intValue(), 50.0F, 3.0F, lineColor, lineColor);
            }
        }
        pGuiGraphics.pose().popPose();
        pGuiGraphics.disableScissor();
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.currentTooltip = null;
        super.m_88315_(pGuiGraphics, mouseX, mouseY, partialTicks);
        if (this.currentTooltip != null) {
            pGuiGraphics.renderTooltip(this.f_96547_, this.currentTooltip, mouseX, mouseY);
        } else {
            this.m_280072_(pGuiGraphics, mouseX, mouseY);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int mouse_x, int mouse_y) {
        String text = "";
        List<Component> tooltipLines = null;
        if (this.drawingGroup) {
            pGuiGraphics.blit(this.getMainTexture(), 40, -14, 154, 220, 12, 12);
            text = Component.translatable("gui.mna.lodestar.draw_group_toggle.prompt").getString();
            pGuiGraphics.drawString(this.f_96547_, text, 56, -12, 16777215, false);
        } else {
            int y = -14;
            if (this.selectedGroup != null) {
                y = -26;
            }
            if (((ContainerLodestar) this.f_97732_).getErrors().size() > 0) {
                pGuiGraphics.blit(this.getMainTexture(), 40, y, 154, 244, 12, 12);
                text = Component.translatable("gui.mna.lodestar.error").getString();
                pGuiGraphics.drawString(this.f_96547_, text, 56, y + 2, 16777215, false);
                if (this.isMousedOverWarnings(mouse_x, mouse_y, text)) {
                    tooltipLines = ((ContainerLodestar) this.f_97732_).getErrors();
                }
            } else if (((ContainerLodestar) this.f_97732_).getWarnings().size() > 0) {
                pGuiGraphics.blit(this.getMainTexture(), 40, y, 154, 232, 12, 12);
                text = Component.translatable("gui.mna.lodestar.warning").getString();
                pGuiGraphics.drawString(this.f_96547_, text, 56, y + 2, 16777215, false);
                if (this.isMousedOverWarnings(mouse_x, mouse_y, text)) {
                    tooltipLines = ((ContainerLodestar) this.f_97732_).getWarnings();
                }
            }
            if (tooltipLines != null) {
                ArrayList<FormattedCharSequence> split = new ArrayList();
                tooltipLines.forEach(c -> split.addAll(this.f_96547_.split(c, this.f_96541_.screen.width / 4)));
                pGuiGraphics.renderTooltip(this.f_96547_, split, mouse_x - this.f_97735_, mouse_y - this.f_97736_);
            }
        }
    }

    private boolean isMousedOverWarnings(int mouse_x, int mouse_y, String promptText) {
        return mouse_x >= this.f_97735_ + 40 && mouse_x <= this.f_97735_ + 40 + this.f_96547_.width(promptText) ? mouse_y >= this.f_97736_ - (this.selectedGroup != null ? 34 : 14) && mouse_y <= this.f_97736_ - (this.selectedGroup != null ? 20 : 0) : false;
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        if (button == 1) {
            if (this.groupBox.isMouseOver(x, y)) {
                this.groupBox.setValue("");
            }
            if (this.filterBox.isMouseOver(x, y)) {
                this.filterBox.setValue("");
            }
            return true;
        } else {
            this.canBackgroundScroll = this.isMouseWithinStage(x, y);
            if (this.groupBox.m_6375_(x, y, button)) {
                this.groupBox.setFocused(true);
                return true;
            } else {
                this.selectedGroup = null;
                this.groupBox.setFocused(false);
                this.groupBox.f_93623_ = false;
                this.groupBox.f_93624_ = false;
                if (this.filterBox.m_6375_(x, y, button)) {
                    this.filterBox.setFocused(true);
                    return true;
                } else {
                    this.filterBox.setFocused(false);
                    if (this.drawingGroup && this.canBackgroundScroll) {
                        Pair<Integer, Integer> scaled = this.getScaledMouseCoordinates(x, y);
                        this.creatingGroup = new LodestarGroup((Integer) scaled.getFirst(), (Integer) scaled.getSecond(), 50, 50, ((ContainerLodestar) this.f_97732_).isLowTier(), this::groupClicked);
                        this.nodeGroups.add(this.creatingGroup);
                        return true;
                    } else {
                        for (LodestarParameter<?> widget : this.parameterWidgets) {
                            if (widget.m_6375_(x, y, button)) {
                                if (this.selectedNode != null) {
                                    this.selectedNode.saveParameters(this.parameterWidgets);
                                    ((ContainerLodestar) this.f_97732_).updateTileLogic(this.saveLogic(), false);
                                }
                                this.m_7522_(widget);
                                return true;
                            }
                        }
                        if (this.canBackgroundScroll) {
                            Pair<Integer, Integer> mouseScaled = this.getScaledMouseCoordinates(x, y);
                            for (LodestarNode node : this.nodes) {
                                if (node.m_6375_((double) ((Integer) mouseScaled.getFirst()).intValue(), (double) ((Integer) mouseScaled.getSecond()).intValue(), button)) {
                                    return true;
                                }
                            }
                            for (LodestarGroup nodex : this.nodeGroups) {
                                if (nodex.m_6375_((double) ((Integer) mouseScaled.getFirst()).intValue(), (double) ((Integer) mouseScaled.getSecond()).intValue(), button)) {
                                    return true;
                                }
                            }
                        }
                        for (LodestarNode nodexx : this.nodes) {
                            nodexx.f_93623_ = false;
                        }
                        for (LodestarGroup nodexx : this.nodeGroups) {
                            nodexx.f_93623_ = false;
                        }
                        boolean superClick = super.m_6375_(x, y, button);
                        for (LodestarNode nodexx : this.nodes) {
                            nodexx.f_93623_ = true;
                        }
                        for (LodestarGroup nodexx : this.nodeGroups) {
                            nodexx.f_93623_ = true;
                        }
                        return superClick;
                    }
                }
            }
        }
    }

    @Override
    public boolean mouseReleased(double mouse_x, double mouse_y, int button) {
        this.canBackgroundScroll = false;
        if (this.drawingGroup) {
            if (this.creatingGroup != null) {
                this.creatingGroup = null;
                this.drawingGroup = false;
            }
            return true;
        } else {
            this.m_7522_(null);
            if (this.draggingNode != null) {
                this.draggingNode = null;
            }
            if (this.draggingGroup != null) {
                this.draggingGroup.scaling = false;
                this.draggingGroup = null;
                this.draggingGroupNodes.clear();
            }
            Pair<Integer, Integer> mouseScaled = this.getScaledMouseCoordinates(mouse_x, mouse_y);
            if (this.lineConnectionOrigin != null || this.draggingStartNode) {
                this.getNodeAt((double) ((Integer) mouseScaled.getFirst()).intValue(), (double) ((Integer) mouseScaled.getSecond()).intValue()).ifPresent(g -> {
                    if (g instanceof LodestarNode) {
                        int idx = ((LodestarNode) g).getSubWidgetAt((double) ((Integer) mouseScaled.getFirst()).intValue(), (double) ((Integer) mouseScaled.getSecond()).intValue());
                        if (idx == 98) {
                            if (this.draggingStartNode) {
                                this.nodes.forEach(node -> node.setStart(false));
                                ((LodestarNode) g).setStart(true);
                                ((ContainerLodestar) this.f_97732_).updateTileLogic(this.saveLogic(), false);
                            } else {
                                this.lineConnectionOrigin.connectToNode((LodestarNode) g, this.lineConnectionOriginNodeIndex);
                                ((ContainerLodestar) this.f_97732_).updateTileLogic(this.saveLogic(), false);
                            }
                        }
                    }
                });
                this.lineConnectionOrigin = null;
                this.draggingStartNode = false;
            }
            if (this.draggingOutTask != null) {
                if (this.isMouseWithinStage(mouse_x, mouse_y)) {
                    this.addNode((Integer) mouseScaled.getFirst() - this.f_97735_ - 24, (Integer) mouseScaled.getSecond() - this.f_97736_ - 20, this.draggingOutTask);
                    ((ContainerLodestar) this.f_97732_).updateTileLogic(this.saveLogic(), false);
                }
                this.draggingOutTask = null;
            }
            return super.m_6348_(mouse_x, mouse_y, button);
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.actionsList._scrolling) {
            return this.actionsList.m_7979_(mouseX, mouseY, button, deltaX, deltaY);
        } else if (this.drawingGroup) {
            if (this.creatingGroup != null) {
                Pair<Integer, Integer> mouseScaled = this.getScaledMouseCoordinates(mouseX, mouseY);
                int width = (Integer) mouseScaled.getFirst() - this.creatingGroup.m_252754_();
                int height = (Integer) mouseScaled.getSecond() - this.creatingGroup.m_252907_();
                this.creatingGroup.resize(Math.abs(width), Math.abs(height));
            }
            return true;
        } else if (this.draggingGroup != null) {
            Pair<Integer, Integer> mouseScaled = this.getScaledMouseCoordinates(mouseX, mouseY);
            if (this.draggingGroup.scaling) {
                int width = (Integer) mouseScaled.getFirst() - this.draggingGroup.m_252754_();
                int height = (Integer) mouseScaled.getSecond() - this.draggingGroup.m_252907_();
                this.draggingGroup.resize(Math.abs(width), Math.abs(height));
            } else {
                int newX = (int) ((double) ((Integer) mouseScaled.getFirst()).intValue() - this.draggingGroup.getClickXOffset());
                int newY = (int) ((double) ((Integer) mouseScaled.getSecond()).intValue() - this.draggingGroup.getClickYOffset());
                int scaledDeltaX = this.draggingGroup.m_252754_() - newX;
                int scaledDeltaY = this.draggingGroup.m_252907_() - newY;
                this.draggingGroupNodes.forEach(node -> {
                    node.m_252865_(node.m_252754_() - scaledDeltaX);
                    node.m_253211_(node.m_252907_() - scaledDeltaY);
                });
                this.draggingGroup.m_252865_(newX);
                this.draggingGroup.m_253211_(newY);
            }
            return true;
        } else if (this.draggingNode != null) {
            Pair<Integer, Integer> mouseScaled = this.getScaledMouseCoordinates(mouseX, mouseY);
            int newX = (int) ((double) ((Integer) mouseScaled.getFirst()).intValue() - this.draggingNode.getClickXOffset());
            int newY = (int) ((double) ((Integer) mouseScaled.getSecond()).intValue() - this.draggingNode.getClickYOffset());
            this.draggingNode.m_252865_(newX);
            this.draggingNode.m_253211_(newY);
            return true;
        } else if (this.lineConnectionOrigin == null && this.draggingOutTask == null && !this.draggingStartNode) {
            this.panBackground(deltaX, deltaY);
            return this.m_7222_() != null && button == 0 && this.m_7222_().mouseDragged(mouseX, mouseY, button, deltaX, deltaY) ? true : super.m_7979_(mouseX, mouseY, button, deltaX, deltaY);
        } else {
            return true;
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (this.actionsList.m_5953_(mouseX, mouseY)) {
            return this.actionsList.mouseScrolled(mouseX, mouseY, delta);
        } else {
            if (this.isMouseWithinStage(mouseX, mouseY)) {
                Pair<Integer, Integer> scaledMousePre = this.getScaledMouseCoordinates(mouseX, mouseY);
                float zoomAmt = (float) (0.2 * Math.signum(delta));
                Zoom += zoomAmt;
                Zoom = MathUtils.clamp(Zoom, 0.2F, 1.2F);
                Pair<Integer, Integer> scaledMousePost = this.getScaledMouseCoordinates(mouseX, mouseY);
                int adX = (Integer) scaledMousePost.getFirst() - (Integer) scaledMousePre.getFirst();
                int adY = (Integer) scaledMousePost.getSecond() - (Integer) scaledMousePre.getSecond();
                this.scroll_pos_x -= adX / 2;
                this.scroll_pos_y -= adY / 2;
                for (int i = 0; i < this.nodes.size(); i++) {
                    LodestarNode node = (LodestarNode) this.nodes.get(i);
                    node.m_252865_(node.m_252754_() + adX);
                    node.m_253211_(node.m_252907_() + adY);
                }
                for (int i = 0; i < this.nodeGroups.size(); i++) {
                    LodestarGroup node = (LodestarGroup) this.nodeGroups.get(i);
                    node.m_252865_(node.m_252754_() + adX);
                    node.m_253211_(node.m_252907_() + adY);
                }
            }
            return true;
        }
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (pKeyCode != 256) {
            if (this.selectedGroup != null && this.groupBox.keyPressed(pKeyCode, pScanCode, pModifiers)) {
                return true;
            }
            if (this.filterBox.keyPressed(pKeyCode, pScanCode, pModifiers)) {
                return true;
            }
            if (this.groupBox.m_93696_() && this.groupBox.isVisible()) {
                return true;
            }
            if (this.filterBox.m_93696_() && this.filterBox.isVisible()) {
                return true;
            }
        }
        return super.m_7933_(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public boolean keyReleased(int pKeyCode, int pScanCode, int pModifiers) {
        if (pKeyCode != 256) {
            if (this.selectedGroup != null && this.groupBox.m_7920_(pKeyCode, pScanCode, pModifiers)) {
                return true;
            }
            if (this.filterBox.m_7920_(pKeyCode, pScanCode, pModifiers)) {
                return true;
            }
            if (this.groupBox.m_93696_() && this.groupBox.isVisible()) {
                return true;
            }
            if (this.filterBox.m_93696_() && this.filterBox.isVisible()) {
                return true;
            }
        }
        return super.m_7920_(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public boolean charTyped(char pCodePoint, int pModifiers) {
        if (this.selectedGroup != null && this.groupBox.isVisible() && this.groupBox.m_93696_()) {
            return this.groupBox.charTyped(pCodePoint, pModifiers);
        } else {
            return this.filterBox.isVisible() && this.filterBox.m_93696_() ? this.filterBox.charTyped(pCodePoint, pModifiers) : super.m_5534_(pCodePoint, pModifiers);
        }
    }

    @Override
    public void onClose() {
        if (this.selectedNode != null) {
            this.deselectNode();
        }
        ((ContainerLodestar) this.f_97732_).updateTileLogic(this.saveLogic(), true);
        super.m_7379_();
    }

    private void recenterView() {
        if (this.nodes.size() != 0 || this.nodeGroups.size() != 0) {
            int minX = Math.min(this.nodes.size() > 0 ? this.nodes.stream().flatMapToInt(n -> IntStream.of(n.m_252754_())).min().getAsInt() : Integer.MAX_VALUE, this.nodeGroups.size() > 0 ? this.nodeGroups.stream().flatMapToInt(n -> IntStream.of(n.m_252754_())).min().getAsInt() : Integer.MAX_VALUE);
            int maxX = Math.max(this.nodes.size() > 0 ? this.nodes.stream().flatMapToInt(n -> IntStream.of(n.m_252754_() + n.m_5711_())).max().getAsInt() : Integer.MIN_VALUE, this.nodeGroups.size() > 0 ? this.nodeGroups.stream().flatMapToInt(n -> IntStream.of(n.m_252754_() + n.m_5711_())).max().getAsInt() : Integer.MIN_VALUE);
            int minY = Math.min(this.nodes.size() > 0 ? this.nodes.stream().flatMapToInt(n -> IntStream.of(n.m_252907_())).min().getAsInt() : Integer.MAX_VALUE, this.nodeGroups.size() > 0 ? this.nodeGroups.stream().flatMapToInt(n -> IntStream.of(n.m_252907_())).min().getAsInt() : Integer.MAX_VALUE);
            int maxY = Math.max(this.nodes.size() > 0 ? this.nodes.stream().flatMapToInt(n -> IntStream.of(n.m_252907_() + n.m_93694_())).max().getAsInt() : Integer.MIN_VALUE, this.nodeGroups.size() > 0 ? this.nodeGroups.stream().flatMapToInt(n -> IntStream.of(n.m_252907_() + n.m_93694_())).max().getAsInt() : Integer.MIN_VALUE);
            int cX = (minX + maxX) / 2;
            int cY = (minY + maxY) / 2;
            int vpCx = (int) (256.0F / Zoom);
            int vpCy = (int) (165.0F / Zoom);
            int deltaX = vpCx - cX + 24;
            int deltaY = vpCy - cY + 20;
            for (LodestarNode node : this.nodes) {
                node.m_252865_(node.m_252754_() + deltaX);
                node.m_253211_(node.m_252907_() + deltaY);
            }
            for (LodestarGroup node : this.nodeGroups) {
                node.m_252865_(node.m_252754_() + deltaX);
                node.m_253211_(node.m_252907_() + deltaY);
            }
        }
    }

    private CompoundTag saveLogic() {
        ListTag data = new ListTag();
        this.nodes.forEach(node -> data.add(node.toCompoundTag(this.f_97736_ - this.scroll_pos_y, this.f_97735_ - this.scroll_pos_x)));
        ListTag groups = new ListTag();
        this.nodeGroups.forEach(group -> groups.add(group.toCompoundTag(this.f_97736_ - this.scroll_pos_y, this.f_97735_ - this.scroll_pos_x)));
        CompoundTag output = new CompoundTag();
        output.put("commands", data);
        output.put("groups", groups);
        return output;
    }

    private void loadLogic(CompoundTag input) {
        if (input != null && input.contains("commands")) {
            ListTag data = input.getList("commands", 10);
            data.forEach(tag -> {
                LodestarNode node = LodestarNode.fromCompoundTag((CompoundTag) tag, this.f_97736_, this.f_97735_, ((ContainerLodestar) this.f_97732_).isLowTier(), b -> this.nodeSubClick((LodestarNode) b), this::getNodeById);
                if (node != null) {
                    this.insertNode(node);
                }
            });
            if (input.contains("groups")) {
                ListTag groupData = input.getList("groups", 10);
                groupData.forEach(tag -> {
                    LodestarGroup group = LodestarGroup.fromCompound((CompoundTag) tag, this.f_97736_, this.f_97735_, ((ContainerLodestar) this.f_97732_).isLowTier(), this::groupClicked);
                    if (group != null) {
                        this.nodeGroups.add(group);
                    }
                });
            }
        }
    }

    private boolean isMouseWithinStage(double mouse_x, double mouse_y) {
        return mouse_x >= (double) this.f_97735_ && mouse_x <= (double) (this.f_97735_ + 256) && mouse_y >= (double) this.f_97736_ && mouse_y <= (double) (this.f_97736_ + 165);
    }

    private Pair<Integer, Integer> getScaledMouseCoordinates(double mouse_x, double mouse_y) {
        return new Pair((int) (mouse_x / (double) Zoom), (int) (mouse_y / (double) Zoom));
    }

    public LodestarNode getNodeById(String id) {
        Optional<LodestarNode> found = this.nodes.stream().filter(n -> n.getId().equals(id)).findFirst();
        return !found.isPresent() ? null : (LodestarNode) found.get();
    }

    Optional<GuiEventListener> getNodeAt(double pMouseX, double pMouseY) {
        for (LodestarNode node : this.nodes) {
            if (node.m_5953_(pMouseX, pMouseY)) {
                return Optional.of(node);
            }
        }
        return Optional.empty();
    }

    private List<LodestarNode> getNodesInGroup(LodestarGroup group) {
        ScreenRectangle groupRect = group.m_264198_();
        return (List<LodestarNode>) this.nodes.stream().filter(n -> groupRect.overlaps(n.m_264198_())).collect(Collectors.toList());
    }

    private int getParameterX() {
        return this.f_97735_ + this.f_97726_ + 5;
    }

    private int getParameterY(int index) {
        return this.f_97736_ + 21 + index * 22;
    }

    @OnlyIn(Dist.CLIENT)
    class CommandScrollList extends ObjectSelectionList<GuiLodestarV2.CommandScrollList.Command> {

        private boolean _scrolling = false;

        private String _filter = "";

        private boolean low_tier;

        public CommandScrollList(boolean low_tier) {
            super(GuiLodestarV2.this.f_96541_, 46, 135, GuiLodestarV2.this.f_97736_ + 10, GuiLodestarV2.this.f_97736_ + 153, 18);
            this.m_93471_(false);
            this.m_93473_(false, 24);
            this.f_93393_ = GuiLodestarV2.this.f_97735_ - 56 + 1;
            this.f_93392_ = this.f_93393_ + 46;
            this.low_tier = low_tier;
            this.setFilter("");
        }

        public void clear() {
            this.m_93516_();
        }

        private void addIconsForAll(Collection<ConstructTask> parts, Consumer<ConstructTask> clickHandler) {
            ArrayList<ConstructTask> segment = new ArrayList();
            for (ConstructTask part : parts) {
                segment.add(part);
                if (segment.size() == 2) {
                    this.m_7085_(new GuiLodestarV2.CommandScrollList.Command(segment, clickHandler, this.f_93390_, this.f_93391_));
                    segment.clear();
                }
            }
            if (segment.size() > 0) {
                this.m_7085_(new GuiLodestarV2.CommandScrollList.Command(segment, clickHandler, this.f_93390_, this.f_93391_));
            }
        }

        @Override
        public void render(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
            int scrollBarStartX = this.getScrollbarPosition();
            pGuiGraphics.enableScissor(this.f_93393_, this.f_93390_, this.f_93392_, this.f_93391_);
            this.m_239227_(pGuiGraphics, mouseX, mouseY, partialTicks);
            pGuiGraphics.disableScissor();
            if (this.getMaxScroll() > 0) {
                int scrollBarTop = this.f_93390_ + 2;
                int scrollBarBottom = this.f_93391_;
                int top = (int) this.m_93517_() * (scrollBarBottom - scrollBarTop) / this.getMaxScroll() + this.f_93390_;
                if (top < scrollBarTop) {
                    top = scrollBarTop;
                }
                pGuiGraphics.blit(GuiLodestarV2.this.getExtensionTexture(), scrollBarStartX, top, 250.0F, 244.0F, 6, 6, 256, 256);
            }
        }

        @Nullable
        protected final GuiLodestarV2.CommandScrollList.Command getEntryAtPos(double mouseX, double mouseY) {
            int lowerXBound = this.getRowLeft();
            int upperXBound = lowerXBound + this.getRowWidth();
            int adjustedY = Mth.floor(mouseY - (double) this.f_93390_) + (int) this.m_93517_();
            int index = adjustedY / this.f_93387_;
            return index >= 0 && adjustedY >= 0 && index < this.m_5773_() && mouseX < (double) this.getScrollbarPosition() && mouseX >= (double) lowerXBound && mouseX <= (double) upperXBound ? (GuiLodestarV2.CommandScrollList.Command) this.m_6702_().get(index) : null;
        }

        @Override
        public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
            this.m_93410_(this.m_93517_() - delta * (double) this.f_93387_);
            return true;
        }

        public void setFilter(String filter) {
            this._filter = filter.trim().toLowerCase();
            this.clear();
            this.addIconsForAll((Collection<ConstructTask>) ((IForgeRegistry) Registries.ConstructTasks.get()).getValues().stream().filter(c -> this._filter != null && this._filter != "" ? Component.translatable("construct." + ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(c).toString()).getString().toLowerCase().contains(this._filter) : true).filter(c -> c.isLodestarAssignable() && (c.isLowTierAssignable() || !this.low_tier)).collect(Collectors.toList()), action -> GuiLodestarV2.this.startDraggingCommandFromList(action));
        }

        @Override
        public int getRowLeft() {
            return this.f_93393_ + 2;
        }

        @Override
        protected int getScrollbarPosition() {
            return this.getRowLeft() + this.getRowWidth() + 6;
        }

        @Override
        protected int getRowTop(int p_getRowTop_1_) {
            return this.f_93390_ - (int) this.m_93517_() + p_getRowTop_1_ * this.f_93387_ - 2;
        }

        @Override
        public int getRowWidth() {
            return 36;
        }

        @Override
        protected void updateScrollingState(double p_updateScrollingState_1_, double p_updateScrollingState_3_, int p_updateScrollingState_5_) {
            super.m_93481_(p_updateScrollingState_1_, p_updateScrollingState_3_, p_updateScrollingState_5_);
            this._scrolling = p_updateScrollingState_5_ == 0 && p_updateScrollingState_1_ >= (double) this.getScrollbarPosition() && p_updateScrollingState_1_ < (double) (this.getScrollbarPosition() + 6);
        }

        @Override
        public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
            this.updateScrollingState(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
            if (!this.m_5953_(p_mouseClicked_1_, p_mouseClicked_3_)) {
                return false;
            } else {
                GuiLodestarV2.CommandScrollList.Command e = this.getEntryAtPos(p_mouseClicked_1_, p_mouseClicked_3_);
                if (e != null) {
                    if (e.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_)) {
                        e.m_93692_(true);
                        return true;
                    }
                } else if (p_mouseClicked_5_ == 0) {
                    this.m_7897_(true);
                    return true;
                }
                return this._scrolling;
            }
        }

        @Override
        public int getMaxScroll() {
            return Math.max(0, this.m_5775_() - (this.f_93391_ - this.f_93390_ + this.f_93387_ / 2)) + 6;
        }

        @OnlyIn(Dist.CLIENT)
        public class Command extends ObjectSelectionList.Entry<GuiLodestarV2.CommandScrollList.Command> {

            private Collection<ConstructTask> parts;

            private int spacing = 18;

            private ConstructTask _hoveredComponent;

            private Consumer<ConstructTask> _clickHandler;

            private final int y0;

            private final int y1;

            public Command(Collection<ConstructTask> parts, Consumer<ConstructTask> clickHandler, int y0, int y1) {
                this.parts = new ArrayList(parts);
                this._hoveredComponent = null;
                this._clickHandler = clickHandler;
                this.y0 = y0;
                this.y1 = y1;
            }

            @Override
            public boolean isMouseOver(double x, double y) {
                return y >= (double) this.y0 && y <= (double) this.y1 ? super.isMouseOver(x, y) : false;
            }

            @Override
            public void render(GuiGraphics pGuiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isHovered, float p_render_9_) {
                int i = 0;
                for (ConstructTask action : this.parts) {
                    if (action != null) {
                        int x = 6 + left + i++ * this.spacing;
                        int y = top + 4;
                        pGuiGraphics.blit(action.getIconTexture(), x, y, 0, 0.0F, 0.0F, 16, 16, 16, 16);
                        if (this.isMouseOver((double) mouseX, (double) mouseY) && mouseX >= x && mouseX <= x + this.spacing) {
                            GuiLodestarV2.this.currentTooltip = action.getAIClass() == null ? Component.translatable("gui.mna.command.coming-soon").withStyle(ChatFormatting.GOLD) : Component.translatable("construct." + ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(action).toString());
                            this._hoveredComponent = action;
                        }
                    }
                }
            }

            @Override
            public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
                if (this._clickHandler != null && this._hoveredComponent != null) {
                    this._clickHandler.accept(this._hoveredComponent);
                }
                return true;
            }

            @Override
            public Component getNarration() {
                return Component.translatable("gui.mna.lodestar.component");
            }
        }
    }
}