package com.simibubi.create.content.trains.schedule;

import com.google.common.collect.ImmutableList;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPackets;
import com.simibubi.create.Create;
import com.simibubi.create.content.trains.GlobalRailwayManager;
import com.simibubi.create.content.trains.graph.EdgePointType;
import com.simibubi.create.content.trains.graph.TrackGraph;
import com.simibubi.create.content.trains.schedule.condition.ScheduleWaitCondition;
import com.simibubi.create.content.trains.schedule.condition.ScheduledDelay;
import com.simibubi.create.content.trains.schedule.destination.DestinationInstruction;
import com.simibubi.create.content.trains.schedule.destination.ScheduleInstruction;
import com.simibubi.create.content.trains.station.GlobalStation;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.ModularGuiLine;
import com.simibubi.create.foundation.gui.ModularGuiLineBuilder;
import com.simibubi.create.foundation.gui.UIRenderHelper;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.element.RenderElement;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.menu.GhostItemSubmitPacket;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.gui.widget.Indicator;
import com.simibubi.create.foundation.gui.widget.Label;
import com.simibubi.create.foundation.gui.widget.SelectionScrollInput;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.IntAttached;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.Pair;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.opengl.GL11;

public class ScheduleScreen extends AbstractSimiContainerScreen<ScheduleMenu> {

    private static final int CARD_HEADER = 22;

    private static final int CARD_WIDTH = 195;

    private List<Rect2i> extraAreas = Collections.emptyList();

    private List<LerpedFloat> horizontalScrolls = new ArrayList();

    private LerpedFloat scroll = LerpedFloat.linear().startWithValue(0.0);

    private Schedule schedule;

    private IconButton confirmButton;

    private IconButton cyclicButton;

    private Indicator cyclicIndicator;

    private IconButton resetProgress;

    private IconButton skipProgress;

    private ScheduleInstruction editingDestination;

    private ScheduleWaitCondition editingCondition;

    private SelectionScrollInput scrollInput;

    private Label scrollInputLabel;

    private IconButton editorConfirm;

    private IconButton editorDelete;

    private ModularGuiLine editorSubWidgets;

    private Consumer<Boolean> onEditorClose;

    private DestinationSuggestions destinationSuggestions;

    private Component clickToEdit = Lang.translateDirect("gui.schedule.lmb_edit").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC);

    private Component rClickToDelete = Lang.translateDirect("gui.schedule.rmb_remove").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC);

    public ScheduleScreen(ScheduleMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.schedule = new Schedule();
        CompoundTag tag = menu.contentHolder.getOrCreateTag().getCompound("Schedule");
        if (!tag.isEmpty()) {
            this.schedule = Schedule.fromTag(tag);
        }
        menu.slotsActive = false;
        this.editorSubWidgets = new ModularGuiLine();
    }

    @Override
    protected void init() {
        AllGuiTextures bg = AllGuiTextures.SCHEDULE;
        this.setWindowSize(bg.width, bg.height);
        super.init();
        this.m_169413_();
        this.confirmButton = new IconButton(this.f_97735_ + bg.width - 42, this.f_97736_ + bg.height - 24, AllIcons.I_CONFIRM);
        this.confirmButton.withCallback(() -> this.f_96541_.player.closeContainer());
        this.m_142416_(this.confirmButton);
        this.cyclicIndicator = new Indicator(this.f_97735_ + 21, this.f_97736_ + 196, Components.immutableEmpty());
        this.cyclicIndicator.state = this.schedule.cyclic ? Indicator.State.ON : Indicator.State.OFF;
        this.m_142416_(this.cyclicIndicator);
        this.cyclicButton = new IconButton(this.f_97735_ + 21, this.f_97736_ + 202, AllIcons.I_REFRESH);
        this.cyclicButton.withCallback(() -> {
            this.schedule.cyclic = !this.schedule.cyclic;
            this.cyclicIndicator.state = this.schedule.cyclic ? Indicator.State.ON : Indicator.State.OFF;
        });
        List<Component> tip = this.cyclicButton.getToolTip();
        tip.add(Lang.translateDirect("schedule.loop"));
        tip.add(Lang.translateDirect("schedule.loop1").withStyle(ChatFormatting.GRAY));
        tip.add(Lang.translateDirect("schedule.loop2").withStyle(ChatFormatting.GRAY));
        this.m_142416_(this.cyclicButton);
        this.resetProgress = new IconButton(this.f_97735_ + 45, this.f_97736_ + 202, AllIcons.I_PRIORITY_VERY_HIGH);
        this.resetProgress.withCallback(() -> {
            this.schedule.savedProgress = 0;
            this.resetProgress.f_93623_ = false;
        });
        this.resetProgress.f_93623_ = this.schedule.savedProgress > 0 && !this.schedule.entries.isEmpty();
        this.resetProgress.setToolTip(Lang.translateDirect("schedule.reset"));
        this.m_142416_(this.resetProgress);
        this.skipProgress = new IconButton(this.f_97735_ + 63, this.f_97736_ + 202, AllIcons.I_PRIORITY_LOW);
        this.skipProgress.withCallback(() -> {
            this.schedule.savedProgress++;
            this.schedule.savedProgress = this.schedule.savedProgress % this.schedule.entries.size();
            this.resetProgress.f_93623_ = this.schedule.savedProgress > 0;
        });
        this.skipProgress.f_93623_ = this.schedule.entries.size() > 1;
        this.skipProgress.setToolTip(Lang.translateDirect("schedule.skip"));
        this.m_142416_(this.skipProgress);
        this.stopEditing();
        this.extraAreas = ImmutableList.of(new Rect2i(this.f_97735_ + bg.width, this.f_97736_ + bg.height - 56, 48, 48));
        this.horizontalScrolls.clear();
        for (int i = 0; i < this.schedule.entries.size(); i++) {
            this.horizontalScrolls.add(LerpedFloat.linear().startWithValue(0.0));
        }
    }

    protected void startEditing(IScheduleInput field, Consumer<Boolean> onClose, boolean allowDeletion) {
        this.onEditorClose = onClose;
        this.confirmButton.f_93624_ = false;
        this.cyclicButton.f_93624_ = false;
        this.cyclicIndicator.f_93624_ = false;
        this.skipProgress.f_93624_ = false;
        this.resetProgress.f_93624_ = false;
        this.scrollInput = new SelectionScrollInput(this.f_97735_ + 56, this.f_97736_ + 65, 143, 16);
        this.scrollInputLabel = new Label(this.f_97735_ + 59, this.f_97736_ + 69, Components.immutableEmpty()).withShadow();
        this.editorConfirm = new IconButton(this.f_97735_ + 56 + 168, this.f_97736_ + 65 + 22, AllIcons.I_CONFIRM);
        if (allowDeletion) {
            this.editorDelete = new IconButton(this.f_97735_ + 56 - 45, this.f_97736_ + 65 + 22, AllIcons.I_TRASH);
        }
        ((ScheduleMenu) this.f_97732_).slotsActive = true;
        ((ScheduleMenu) this.f_97732_).targetSlotsActive = field.slotsTargeted();
        for (int i = 0; i < field.slotsTargeted(); i++) {
            ItemStack item = field.getItem(i);
            ((ScheduleMenu) this.f_97732_).ghostInventory.setStackInSlot(i, item);
            AllPackets.getChannel().sendToServer(new GhostItemSubmitPacket(item, i));
        }
        if (field instanceof ScheduleInstruction instruction) {
            int startIndex = 0;
            for (int i = 0; i < Schedule.INSTRUCTION_TYPES.size(); i++) {
                if (((ResourceLocation) ((Pair) Schedule.INSTRUCTION_TYPES.get(i)).getFirst()).equals(instruction.getId())) {
                    startIndex = i;
                }
            }
            this.editingDestination = instruction;
            this.updateEditorSubwidgets(this.editingDestination);
            this.scrollInput.forOptions(Schedule.getTypeOptions(Schedule.INSTRUCTION_TYPES)).titled(Lang.translateDirect("schedule.instruction_type")).writingTo(this.scrollInputLabel).calling(index -> {
                ScheduleInstruction newlyCreated = (ScheduleInstruction) ((Supplier) ((Pair) Schedule.INSTRUCTION_TYPES.get(index)).getSecond()).get();
                if (!this.editingDestination.getId().equals(newlyCreated.getId())) {
                    this.editingDestination = newlyCreated;
                    this.updateEditorSubwidgets(this.editingDestination);
                }
            }).setState(startIndex);
        }
        if (field instanceof ScheduleWaitCondition cond) {
            int startIndex = 0;
            for (int ix = 0; ix < Schedule.CONDITION_TYPES.size(); ix++) {
                if (((ResourceLocation) ((Pair) Schedule.CONDITION_TYPES.get(ix)).getFirst()).equals(cond.getId())) {
                    startIndex = ix;
                }
            }
            this.editingCondition = cond;
            this.updateEditorSubwidgets(this.editingCondition);
            this.scrollInput.forOptions(Schedule.getTypeOptions(Schedule.CONDITION_TYPES)).titled(Lang.translateDirect("schedule.condition_type")).writingTo(this.scrollInputLabel).calling(index -> {
                ScheduleWaitCondition newlyCreated = (ScheduleWaitCondition) ((Supplier) ((Pair) Schedule.CONDITION_TYPES.get(index)).getSecond()).get();
                if (!this.editingCondition.getId().equals(newlyCreated.getId())) {
                    this.editingCondition = newlyCreated;
                    this.updateEditorSubwidgets(this.editingCondition);
                }
            }).setState(startIndex);
        }
        this.m_142416_(this.scrollInput);
        this.m_142416_(this.scrollInputLabel);
        this.m_142416_(this.editorConfirm);
        if (allowDeletion) {
            this.m_142416_(this.editorDelete);
        }
    }

    private void onDestinationEdited(String text) {
        if (this.destinationSuggestions != null) {
            this.destinationSuggestions.updateCommandInfo();
        }
    }

    protected void stopEditing() {
        this.confirmButton.f_93624_ = true;
        this.cyclicButton.f_93624_ = true;
        this.cyclicIndicator.f_93624_ = true;
        this.skipProgress.f_93624_ = true;
        this.resetProgress.f_93624_ = true;
        if (this.editingCondition != null || this.editingDestination != null) {
            this.destinationSuggestions = null;
            this.m_169411_(this.scrollInput);
            this.m_169411_(this.scrollInputLabel);
            this.m_169411_(this.editorConfirm);
            this.m_169411_(this.editorDelete);
            IScheduleInput editing = (IScheduleInput) (this.editingCondition == null ? this.editingDestination : this.editingCondition);
            for (int i = 0; i < editing.slotsTargeted(); i++) {
                editing.setItem(i, ((ScheduleMenu) this.f_97732_).ghostInventory.getStackInSlot(i));
                AllPackets.getChannel().sendToServer(new GhostItemSubmitPacket(ItemStack.EMPTY, i));
            }
            this.editorSubWidgets.saveValues(editing.getData());
            this.editorSubWidgets.forEach(x$0 -> this.m_169411_(x$0));
            this.editorSubWidgets.clear();
            this.editingCondition = null;
            this.editingDestination = null;
            this.editorConfirm = null;
            this.editorDelete = null;
            ((ScheduleMenu) this.f_97732_).slotsActive = false;
            this.init();
        }
    }

    protected void updateEditorSubwidgets(IScheduleInput field) {
        this.destinationSuggestions = null;
        ((ScheduleMenu) this.f_97732_).targetSlotsActive = field.slotsTargeted();
        this.editorSubWidgets.forEach(x$0 -> this.m_169411_(x$0));
        this.editorSubWidgets.clear();
        field.initConfigurationWidgets(new ModularGuiLineBuilder(this.f_96547_, this.editorSubWidgets, this.getGuiLeft() + 77, this.getGuiTop() + 92).speechBubble());
        this.editorSubWidgets.loadValues(field.getData(), x$0 -> {
            NarratableEntry var10000 = (NarratableEntry) this.m_142416_((GuiEventListener) x$0);
        }, x$0 -> {
            NarratableEntry var10000 = (NarratableEntry) this.m_169394_((Renderable) x$0);
        });
        if (field instanceof DestinationInstruction) {
            this.editorSubWidgets.forEach(e -> {
                if (e instanceof EditBox destinationBox) {
                    this.destinationSuggestions = new DestinationSuggestions(this.f_96541_, this, destinationBox, this.f_96547_, this.getViableStations(field), this.f_97736_ + 33);
                    this.destinationSuggestions.m_93922_(true);
                    this.destinationSuggestions.updateCommandInfo();
                    destinationBox.setResponder(this::onDestinationEdited);
                }
            });
        }
    }

    private List<IntAttached<String>> getViableStations(IScheduleInput field) {
        GlobalRailwayManager railwayManager = Create.RAILWAYS.sided(null);
        Set<TrackGraph> viableGraphs = new HashSet(railwayManager.trackNetworks.values());
        for (ScheduleEntry entry : this.schedule.entries) {
            ScheduleInstruction filter = entry.instruction;
            if (filter instanceof DestinationInstruction) {
                DestinationInstruction destination = (DestinationInstruction) filter;
                if (destination != field) {
                    String filterx = destination.getFilterForRegex();
                    if (!filterx.isBlank()) {
                        Iterator<TrackGraph> iterator = viableGraphs.iterator();
                        label50: while (iterator.hasNext()) {
                            TrackGraph trackGraph = (TrackGraph) iterator.next();
                            for (GlobalStation station : trackGraph.getPoints(EdgePointType.STATION)) {
                                if (station.name.matches(filterx)) {
                                    continue label50;
                                }
                            }
                            iterator.remove();
                        }
                    }
                }
            }
        }
        if (viableGraphs.isEmpty()) {
            viableGraphs = new HashSet(railwayManager.trackNetworks.values());
        }
        Vec3 position = this.f_96541_.player.m_20182_();
        Set<String> visited = new HashSet();
        return viableGraphs.stream().flatMap(g -> g.getPoints(EdgePointType.STATION).stream()).filter(stationx -> stationx.blockEntityPos != null).filter(stationx -> visited.add(stationx.name)).map(stationx -> IntAttached.with((int) Vec3.atBottomCenterOf(stationx.blockEntityPos).distanceTo(position), stationx.name)).toList();
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.scroll.tickChaser();
        for (LerpedFloat lerpedFloat : this.horizontalScrolls) {
            lerpedFloat.tickChaser();
        }
        if (this.destinationSuggestions != null) {
            this.destinationSuggestions.tick();
        }
        this.schedule.savedProgress = this.schedule.entries.isEmpty() ? 0 : Mth.clamp(this.schedule.savedProgress, 0, this.schedule.entries.size() - 1);
        this.resetProgress.f_93623_ = this.schedule.savedProgress > 0;
        this.skipProgress.f_93623_ = this.schedule.entries.size() > 1;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        partialTicks = this.f_96541_.getFrameTime();
        if (((ScheduleMenu) this.f_97732_).slotsActive) {
            super.render(graphics, mouseX, mouseY, partialTicks);
        } else {
            this.m_280273_(graphics);
            this.renderBg(graphics, partialTicks, mouseX, mouseY);
            for (Renderable widget : this.f_169369_) {
                widget.render(graphics, mouseX, mouseY, partialTicks);
            }
            this.renderForeground(graphics, mouseX, mouseY, partialTicks);
        }
    }

    protected void renderSchedule(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        PoseStack matrixStack = graphics.pose();
        UIRenderHelper.swapAndBlitColor(this.f_96541_.getMainRenderTarget(), UIRenderHelper.framebuffer);
        UIRenderHelper.drawStretched(graphics, this.f_97735_ + 33, this.f_97736_ + 16, 3, 173, 200, AllGuiTextures.SCHEDULE_STRIP_DARK);
        int yOffset = 25;
        List<ScheduleEntry> entries = this.schedule.entries;
        float scrollOffset = -this.scroll.getValue(partialTicks);
        for (int i = 0; i <= entries.size(); i++) {
            if (this.schedule.savedProgress == i && !this.schedule.entries.isEmpty()) {
                matrixStack.pushPose();
                float expectedY = scrollOffset + (float) this.f_97736_ + (float) yOffset + 4.0F;
                float actualY = Mth.clamp(expectedY, (float) (this.f_97736_ + 18), (float) (this.f_97736_ + 170));
                matrixStack.translate(0.0F, actualY, 0.0F);
                (expectedY == actualY ? AllGuiTextures.SCHEDULE_POINTER : AllGuiTextures.SCHEDULE_POINTER_OFFSCREEN).render(graphics, this.f_97735_, 0);
                matrixStack.popPose();
            }
            this.startStencil(graphics, (float) (this.f_97735_ + 16), (float) (this.f_97736_ + 16), 220.0F, 173.0F);
            matrixStack.pushPose();
            matrixStack.translate(0.0F, scrollOffset, 0.0F);
            if (i == 0 || entries.size() == 0) {
                UIRenderHelper.drawStretched(graphics, this.f_97735_ + 33, this.f_97736_ + 16, 3, 10, -100, AllGuiTextures.SCHEDULE_STRIP_LIGHT);
            }
            if (i == entries.size()) {
                if (i > 0) {
                    yOffset += 9;
                }
                AllGuiTextures.SCHEDULE_STRIP_END.render(graphics, this.f_97735_ + 29, this.f_97736_ + yOffset);
                AllGuiTextures.SCHEDULE_CARD_NEW.render(graphics, this.f_97735_ + 43, this.f_97736_ + yOffset);
                matrixStack.popPose();
                this.endStencil();
                break;
            }
            ScheduleEntry scheduleEntry = (ScheduleEntry) entries.get(i);
            int cardY = yOffset;
            int cardHeight = this.renderScheduleEntry(graphics, scheduleEntry, yOffset, mouseX, mouseY, partialTicks);
            yOffset += cardHeight;
            if (i + 1 < entries.size()) {
                AllGuiTextures.SCHEDULE_STRIP_DOTTED.render(graphics, this.f_97735_ + 29, this.f_97736_ + yOffset - 3);
                yOffset += 10;
            }
            matrixStack.popPose();
            this.endStencil();
            if (scheduleEntry.instruction.supportsConditions()) {
                float h = (float) (cardHeight - 26);
                float y1 = (float) (cardY + 24) + scrollOffset;
                float y2 = y1 + h;
                if (y2 > 189.0F) {
                    h -= y2 - 189.0F;
                }
                if (y1 < 16.0F) {
                    float correction = 16.0F - y1;
                    y1 += correction;
                    h -= correction;
                }
                if (!(h <= 0.0F)) {
                    this.startStencil(graphics, (float) (this.f_97735_ + 43), (float) this.f_97736_ + y1, 161.0F, h);
                    matrixStack.pushPose();
                    matrixStack.translate(0.0F, scrollOffset, 0.0F);
                    this.renderScheduleConditions(graphics, scheduleEntry, cardY, mouseX, mouseY, partialTicks, cardHeight, i);
                    matrixStack.popPose();
                    this.endStencil();
                    if (this.isConditionAreaScrollable(scheduleEntry)) {
                        this.startStencil(graphics, (float) (this.f_97735_ + 16), (float) (this.f_97736_ + 16), 220.0F, 173.0F);
                        matrixStack.pushPose();
                        matrixStack.translate(0.0F, scrollOffset, 0.0F);
                        int center = (cardHeight - 8 + 22) / 2;
                        float chaseTarget = ((LerpedFloat) this.horizontalScrolls.get(i)).getChaseTarget();
                        if (!Mth.equal(chaseTarget, 0.0F)) {
                            AllGuiTextures.SCHEDULE_SCROLL_LEFT.render(graphics, this.f_97735_ + 40, this.f_97736_ + cardY + center);
                        }
                        if (!Mth.equal(chaseTarget, (float) (scheduleEntry.conditions.size() - 1))) {
                            AllGuiTextures.SCHEDULE_SCROLL_RIGHT.render(graphics, this.f_97735_ + 203, this.f_97736_ + cardY + center);
                        }
                        matrixStack.popPose();
                        this.endStencil();
                    }
                }
            }
        }
        int zLevel = 200;
        graphics.fillGradient(this.f_97735_ + 16, this.f_97736_ + 16, this.f_97735_ + 16 + 220, this.f_97736_ + 16 + 10, zLevel, 1996488704, 0);
        graphics.fillGradient(this.f_97735_ + 16, this.f_97736_ + 179, this.f_97735_ + 16 + 220, this.f_97736_ + 179 + 10, zLevel, 0, 1996488704);
        UIRenderHelper.swapAndBlitColor(UIRenderHelper.framebuffer, this.f_96541_.getMainRenderTarget());
    }

    public int renderScheduleEntry(GuiGraphics graphics, ScheduleEntry entry, int yOffset, int mouseX, int mouseY, float partialTicks) {
        int zLevel = -100;
        AllGuiTextures light = AllGuiTextures.SCHEDULE_CARD_LIGHT;
        AllGuiTextures medium = AllGuiTextures.SCHEDULE_CARD_MEDIUM;
        AllGuiTextures dark = AllGuiTextures.SCHEDULE_CARD_DARK;
        int cardWidth = 195;
        int cardHeader = 22;
        int maxRows = 0;
        for (List<ScheduleWaitCondition> list : entry.conditions) {
            maxRows = Math.max(maxRows, list.size());
        }
        boolean supportsConditions = entry.instruction.supportsConditions();
        int cardHeight = cardHeader + (supportsConditions ? 24 + maxRows * 18 : 4);
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate((float) (this.f_97735_ + 25), (float) (this.f_97736_ + yOffset), 0.0F);
        UIRenderHelper.drawStretched(graphics, 0, 1, cardWidth, cardHeight - 2, zLevel, light);
        UIRenderHelper.drawStretched(graphics, 1, 0, cardWidth - 2, cardHeight, zLevel, light);
        UIRenderHelper.drawStretched(graphics, 1, 1, cardWidth - 2, cardHeight - 2, zLevel, dark);
        UIRenderHelper.drawStretched(graphics, 2, 2, cardWidth - 4, cardHeight - 4, zLevel, medium);
        UIRenderHelper.drawStretched(graphics, 2, 2, cardWidth - 4, cardHeader, zLevel, supportsConditions ? light : medium);
        AllGuiTextures.SCHEDULE_CARD_REMOVE.render(graphics, cardWidth - 14, 2);
        AllGuiTextures.SCHEDULE_CARD_DUPLICATE.render(graphics, cardWidth - 14, cardHeight - 14);
        int i = this.schedule.entries.indexOf(entry);
        if (i > 0) {
            AllGuiTextures.SCHEDULE_CARD_MOVE_UP.render(graphics, cardWidth, cardHeader - 14);
        }
        if (i < this.schedule.entries.size() - 1) {
            AllGuiTextures.SCHEDULE_CARD_MOVE_DOWN.render(graphics, cardWidth, cardHeader);
        }
        UIRenderHelper.drawStretched(graphics, 8, 0, 3, cardHeight + 10, zLevel, AllGuiTextures.SCHEDULE_STRIP_LIGHT);
        (supportsConditions ? AllGuiTextures.SCHEDULE_STRIP_TRAVEL : AllGuiTextures.SCHEDULE_STRIP_ACTION).render(graphics, 4, 6);
        if (supportsConditions) {
            AllGuiTextures.SCHEDULE_STRIP_WAIT.render(graphics, 4, 28);
        }
        Pair<ItemStack, Component> destination = entry.instruction.getSummary();
        this.renderInput(graphics, destination, 26, 5, false, 100);
        entry.instruction.renderSpecialIcon(graphics, 30, 5);
        matrixStack.popPose();
        return cardHeight;
    }

    public void renderScheduleConditions(GuiGraphics graphics, ScheduleEntry entry, int yOffset, int mouseX, int mouseY, float partialTicks, int cardHeight, int entryIndex) {
        int cardWidth = 195;
        int cardHeader = 22;
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate((float) (this.f_97735_ + 25), (float) (this.f_97736_ + yOffset), 0.0F);
        int xOffset = 26;
        float scrollOffset = this.getConditionScroll(entry, partialTicks, entryIndex);
        matrixStack.pushPose();
        matrixStack.translate(-scrollOffset, 0.0F, 0.0F);
        for (List<ScheduleWaitCondition> list : entry.conditions) {
            int maxWidth = this.getConditionColumnWidth(list);
            for (int i = 0; i < list.size(); i++) {
                ScheduleWaitCondition scheduleWaitCondition = (ScheduleWaitCondition) list.get(i);
                Math.max(maxWidth, this.renderInput(graphics, scheduleWaitCondition.getSummary(), xOffset, 29 + i * 18, i != 0, maxWidth));
                scheduleWaitCondition.renderSpecialIcon(graphics, xOffset + 4, 29 + i * 18);
            }
            AllGuiTextures.SCHEDULE_CONDITION_APPEND.render(graphics, xOffset + (maxWidth - 10) / 2, 29 + list.size() * 18);
            xOffset += maxWidth + 10;
        }
        AllGuiTextures.SCHEDULE_CONDITION_NEW.render(graphics, xOffset - 3, 29);
        matrixStack.popPose();
        if (xOffset + 16 > cardWidth - 26) {
            TransformStack.cast(matrixStack).rotateZ(-90.0);
            int zLevel = 200;
            graphics.fillGradient(-cardHeight + 2, 18, -2 - cardHeader, 28, zLevel, 1140850688, 0);
            graphics.fillGradient(-cardHeight + 2, cardWidth - 26, -2 - cardHeader, cardWidth - 16, zLevel, 0, 1140850688);
        }
        matrixStack.popPose();
    }

    private boolean isConditionAreaScrollable(ScheduleEntry entry) {
        int xOffset = 26;
        for (List<ScheduleWaitCondition> list : entry.conditions) {
            xOffset += this.getConditionColumnWidth(list) + 10;
        }
        return xOffset + 16 > 169;
    }

    private float getConditionScroll(ScheduleEntry entry, float partialTicks, int entryIndex) {
        float scrollOffset = 0.0F;
        float scrollIndex = ((LerpedFloat) this.horizontalScrolls.get(entryIndex)).getValue(partialTicks);
        for (List<ScheduleWaitCondition> list : entry.conditions) {
            int maxWidth = this.getConditionColumnWidth(list);
            float partialOfThisColumn = Math.min(1.0F, scrollIndex);
            scrollOffset += (float) (maxWidth + 10) * partialOfThisColumn;
            scrollIndex -= partialOfThisColumn;
        }
        return scrollOffset;
    }

    private int getConditionColumnWidth(List<ScheduleWaitCondition> list) {
        int maxWidth = 0;
        for (ScheduleWaitCondition scheduleWaitCondition : list) {
            maxWidth = Math.max(maxWidth, this.getFieldSize(32, scheduleWaitCondition.getSummary()));
        }
        return maxWidth;
    }

    protected int renderInput(GuiGraphics graphics, Pair<ItemStack, Component> pair, int x, int y, boolean clean, int minSize) {
        ItemStack stack = pair.getFirst();
        Component text = pair.getSecond();
        boolean hasItem = !stack.isEmpty();
        int fieldSize = Math.min(this.getFieldSize(minSize, pair), 150);
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        AllGuiTextures left = clean ? AllGuiTextures.SCHEDULE_CONDITION_LEFT_CLEAN : AllGuiTextures.SCHEDULE_CONDITION_LEFT;
        AllGuiTextures middle = AllGuiTextures.SCHEDULE_CONDITION_MIDDLE;
        AllGuiTextures item = AllGuiTextures.SCHEDULE_CONDITION_ITEM;
        AllGuiTextures right = AllGuiTextures.SCHEDULE_CONDITION_RIGHT;
        matrixStack.translate((float) x, (float) y, 0.0F);
        UIRenderHelper.drawStretched(graphics, 0, 0, fieldSize, 16, -100, middle);
        left.render(graphics, clean ? 0 : -3, 0);
        right.render(graphics, fieldSize - 2, 0);
        if (hasItem) {
            item.render(graphics, 3, 0);
        }
        if (hasItem) {
            item.render(graphics, 3, 0);
            if (stack.getItem() != Items.STRUCTURE_VOID) {
                GuiGameElement.of(stack).<RenderElement>at(4.0F, 0.0F).render(graphics);
            }
        }
        if (text != null) {
            graphics.drawString(this.f_96547_, this.f_96547_.substrByWidth(text, 120).getString(), hasItem ? 28 : 8, 4, -855314);
        }
        matrixStack.popPose();
        return fieldSize;
    }

    public boolean action(@Nullable GuiGraphics graphics, double mouseX, double mouseY, int click) {
        if (this.editingCondition == null && this.editingDestination == null) {
            Component empty = Components.immutableEmpty();
            int mx = (int) mouseX;
            int my = (int) mouseY;
            int x = mx - this.f_97735_ - 25;
            int y = my - this.f_97736_ - 25;
            if (x >= 0 && x < 205) {
                if (y >= 0 && y < 173) {
                    y = (int) ((float) y + this.scroll.getValue(0.0F));
                    List<ScheduleEntry> entries = this.schedule.entries;
                    for (int i = 0; i < entries.size(); i++) {
                        ScheduleEntry entry = (ScheduleEntry) entries.get(i);
                        int maxRows = 0;
                        for (List<ScheduleWaitCondition> list : entry.conditions) {
                            maxRows = Math.max(maxRows, list.size());
                        }
                        int cardHeight = 22 + (entry.instruction.supportsConditions() ? 24 + maxRows * 18 : 4);
                        if (y < cardHeight + 5) {
                            int fieldSize = this.getFieldSize(100, entry.instruction.getSummary());
                            if (x > 25 && x <= 25 + fieldSize && y > 4 && y <= 20) {
                                List<Component> components = new ArrayList();
                                components.addAll(entry.instruction.getTitleAs("instruction"));
                                components.add(empty);
                                components.add(this.clickToEdit);
                                this.renderActionTooltip(graphics, components, mx, my);
                                if (click == 0) {
                                    this.startEditing(entry.instruction, confirmed -> {
                                        if (confirmed) {
                                            entry.instruction = this.editingDestination;
                                        }
                                    }, false);
                                }
                                return true;
                            }
                            if (x > 180 && x <= 192) {
                                if (y > 0 && y <= 14) {
                                    this.renderActionTooltip(graphics, ImmutableList.of(Lang.translateDirect("gui.schedule.remove_entry")), mx, my);
                                    if (click == 0) {
                                        entries.remove(entry);
                                        this.init();
                                    }
                                    return true;
                                }
                                if (y > cardHeight - 14) {
                                    this.renderActionTooltip(graphics, ImmutableList.of(Lang.translateDirect("gui.schedule.duplicate")), mx, my);
                                    if (click == 0) {
                                        entries.add(entries.indexOf(entry), entry.clone());
                                        this.init();
                                    }
                                    return true;
                                }
                            }
                            if (x > 194) {
                                if (y > 7 && y <= 20 && i > 0) {
                                    this.renderActionTooltip(graphics, ImmutableList.of(Lang.translateDirect("gui.schedule.move_up")), mx, my);
                                    if (click == 0) {
                                        entries.remove(entry);
                                        entries.add(i - 1, entry);
                                        this.init();
                                    }
                                    return true;
                                }
                                if (y > 20 && y <= 33 && i < entries.size() - 1) {
                                    this.renderActionTooltip(graphics, ImmutableList.of(Lang.translateDirect("gui.schedule.move_down")), mx, my);
                                    if (click == 0) {
                                        entries.remove(entry);
                                        entries.add(i + 1, entry);
                                        this.init();
                                    }
                                    return true;
                                }
                            }
                            int center = (cardHeight - 8 + 22) / 2;
                            if (y > center - 1 && y <= center + 7 && this.isConditionAreaScrollable(entry)) {
                                float chaseTarget = ((LerpedFloat) this.horizontalScrolls.get(i)).getChaseTarget();
                                if (x > 12 && x <= 19 && !Mth.equal(chaseTarget, 0.0F)) {
                                    if (click == 0) {
                                        ((LerpedFloat) this.horizontalScrolls.get(i)).chase((double) (chaseTarget - 1.0F), 0.5, LerpedFloat.Chaser.EXP);
                                    }
                                    return true;
                                }
                                if (x > 177 && x <= 184 && !Mth.equal(chaseTarget, (float) (entry.conditions.size() - 1))) {
                                    if (click == 0) {
                                        ((LerpedFloat) this.horizontalScrolls.get(i)).chase((double) (chaseTarget + 1.0F), 0.5, LerpedFloat.Chaser.EXP);
                                    }
                                    return true;
                                }
                            }
                            x -= 18;
                            y -= 28;
                            if (x >= 0 && y >= 0 && x <= 160) {
                                x = (int) ((float) x + (this.getConditionScroll(entry, 0.0F, i) - 8.0F));
                                List<List<ScheduleWaitCondition>> columns = entry.conditions;
                                for (int j = 0; j < columns.size(); j++) {
                                    List<ScheduleWaitCondition> conditions = (List<ScheduleWaitCondition>) columns.get(j);
                                    if (x < 0) {
                                        return false;
                                    }
                                    int w = this.getConditionColumnWidth(conditions);
                                    if (x < w) {
                                        int row = y / 18;
                                        if (row < conditions.size() && row >= 0) {
                                            boolean canRemove = conditions.size() > 1 || columns.size() > 1;
                                            List<Component> components = new ArrayList();
                                            components.add(Lang.translateDirect("schedule.condition_type").withStyle(ChatFormatting.GRAY));
                                            ScheduleWaitCondition condition = (ScheduleWaitCondition) conditions.get(row);
                                            components.addAll(condition.getTitleAs("condition"));
                                            components.add(empty);
                                            components.add(this.clickToEdit);
                                            if (canRemove) {
                                                components.add(this.rClickToDelete);
                                            }
                                            this.renderActionTooltip(graphics, components, mx, my);
                                            if (canRemove && click == 1) {
                                                conditions.remove(row);
                                                if (conditions.isEmpty()) {
                                                    columns.remove(conditions);
                                                }
                                            }
                                            if (click == 0) {
                                                this.startEditing(condition, confirmed -> {
                                                    conditions.remove(row);
                                                    if (confirmed) {
                                                        conditions.add(row, this.editingCondition);
                                                    } else {
                                                        if (conditions.isEmpty()) {
                                                            columns.remove(conditions);
                                                        }
                                                    }
                                                }, canRemove);
                                            }
                                            return true;
                                        }
                                        if (y > 18 * conditions.size() && y <= 18 * conditions.size() + 10 && x >= w / 2 - 5 && x < w / 2 + 5) {
                                            this.renderActionTooltip(graphics, ImmutableList.of(Lang.translateDirect("gui.schedule.add_condition")), mx, my);
                                            if (click == 0) {
                                                this.startEditing(new ScheduledDelay(), confirmed -> {
                                                    if (confirmed) {
                                                        conditions.add(this.editingCondition);
                                                    }
                                                }, true);
                                            }
                                            return true;
                                        }
                                        return false;
                                    }
                                    x -= w + 10;
                                }
                                if (x >= 0 && x <= 15 && y <= 20) {
                                    this.renderActionTooltip(graphics, ImmutableList.of(Lang.translateDirect("gui.schedule.alternative_condition")), mx, my);
                                    if (click == 0) {
                                        this.startEditing(new ScheduledDelay(), confirmed -> {
                                            if (confirmed) {
                                                ArrayList<ScheduleWaitCondition> conditionsx = new ArrayList();
                                                conditionsx.add(this.editingCondition);
                                                columns.add(conditionsx);
                                            }
                                        }, true);
                                    }
                                    return true;
                                }
                                return false;
                            }
                            return false;
                        }
                        y -= cardHeight + 10;
                        if (y < 0) {
                            return false;
                        }
                    }
                    if (x >= 18 && x <= 33 && y <= 14) {
                        this.renderActionTooltip(graphics, ImmutableList.of(Lang.translateDirect("gui.schedule.add_entry")), mx, my);
                        if (click == 0) {
                            this.startEditing(new DestinationInstruction(), confirmed -> {
                                if (confirmed) {
                                    ScheduleEntry entryx = new ScheduleEntry();
                                    ScheduledDelay delay = new ScheduledDelay();
                                    ArrayList<ScheduleWaitCondition> initialConditions = new ArrayList();
                                    initialConditions.add(delay);
                                    entryx.instruction = this.editingDestination;
                                    entryx.conditions.add(initialConditions);
                                    this.schedule.entries.add(entryx);
                                }
                            }, true);
                        }
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void renderActionTooltip(@Nullable GuiGraphics graphics, List<Component> tooltip, int mx, int my) {
        if (graphics != null) {
            graphics.renderTooltip(this.f_96547_, tooltip, Optional.empty(), mx, my);
        }
    }

    private int getFieldSize(int minSize, Pair<ItemStack, Component> pair) {
        ItemStack stack = pair.getFirst();
        Component text = pair.getSecond();
        boolean hasItem = !stack.isEmpty();
        return Math.max((text == null ? 0 : this.f_96547_.width(text)) + (hasItem ? 20 : 0) + 16, minSize);
    }

    protected void startStencil(GuiGraphics graphics, float x, float y, float w, float h) {
        RenderSystem.clear(1280, Minecraft.ON_OSX);
        GL11.glDisable(2960);
        RenderSystem.stencilMask(-1);
        RenderSystem.clear(1024, Minecraft.ON_OSX);
        GL11.glEnable(2960);
        RenderSystem.stencilOp(7681, 7680, 7680);
        RenderSystem.stencilMask(255);
        RenderSystem.stencilFunc(512, 1, 255);
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(x, y, 0.0F);
        matrixStack.scale(w, h, 1.0F);
        graphics.fillGradient(0, 0, 1, 1, -100, -16777216, -16777216);
        matrixStack.popPose();
        GL11.glEnable(2960);
        RenderSystem.stencilOp(7680, 7680, 7680);
        RenderSystem.stencilFunc(514, 1, 255);
    }

    protected void endStencil() {
        GL11.glDisable(2960);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (this.destinationSuggestions != null && this.destinationSuggestions.m_93884_((double) ((int) pMouseX), (double) ((int) pMouseY), pButton)) {
            return true;
        } else if (this.editorConfirm != null && this.editorConfirm.m_5953_(pMouseX, pMouseY) && this.onEditorClose != null) {
            this.onEditorClose.accept(true);
            this.stopEditing();
            return true;
        } else if (this.editorDelete != null && this.editorDelete.m_5953_(pMouseX, pMouseY) && this.onEditorClose != null) {
            this.onEditorClose.accept(false);
            this.stopEditing();
            return true;
        } else {
            return this.action(null, pMouseX, pMouseY, pButton) ? true : super.mouseClicked(pMouseX, pMouseY, pButton);
        }
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (this.destinationSuggestions != null && this.destinationSuggestions.m_93888_(pKeyCode, pScanCode, pModifiers)) {
            return true;
        } else if (this.editingCondition == null && this.editingDestination == null) {
            return super.keyPressed(pKeyCode, pScanCode, pModifiers);
        } else {
            InputConstants.Key mouseKey = InputConstants.getKey(pKeyCode, pScanCode);
            boolean hitEnter = this.m_7222_() instanceof EditBox && (pKeyCode == 257 || pKeyCode == 335);
            boolean hitE = this.m_7222_() == null && this.f_96541_.options.keyInventory.isActiveAndMatches(mouseKey);
            if (!hitE && !hitEnter) {
                return super.keyPressed(pKeyCode, pScanCode, pModifiers);
            } else {
                this.onEditorClose.accept(true);
                this.stopEditing();
                return true;
            }
        }
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        if (this.destinationSuggestions != null && this.destinationSuggestions.m_93882_(Mth.clamp(pDelta, -1.0, 1.0))) {
            return true;
        } else if (this.editingCondition == null && this.editingDestination == null) {
            if (m_96638_()) {
                List<ScheduleEntry> entries = this.schedule.entries;
                int y = (int) (pMouseY - (double) this.f_97736_ - 25.0 + (double) this.scroll.getValue());
                for (int i = 0; i < entries.size(); i++) {
                    ScheduleEntry entry = (ScheduleEntry) entries.get(i);
                    int maxRows = 0;
                    for (List<ScheduleWaitCondition> list : entry.conditions) {
                        maxRows = Math.max(maxRows, list.size());
                    }
                    int cardHeight = 46 + maxRows * 18;
                    if (y < cardHeight) {
                        if (this.isConditionAreaScrollable(entry) && y >= 24 && !(pMouseX < (double) (this.f_97735_ + 25)) && !(pMouseX > (double) (this.f_97735_ + 205))) {
                            float chaseTarget = ((LerpedFloat) this.horizontalScrolls.get(i)).getChaseTarget();
                            if (pDelta > 0.0 && !Mth.equal(chaseTarget, 0.0F)) {
                                ((LerpedFloat) this.horizontalScrolls.get(i)).chase((double) (chaseTarget - 1.0F), 0.5, LerpedFloat.Chaser.EXP);
                                return true;
                            }
                            if (pDelta < 0.0 && !Mth.equal(chaseTarget, (float) (entry.conditions.size() - 1))) {
                                ((LerpedFloat) this.horizontalScrolls.get(i)).chase((double) (chaseTarget + 1.0F), 0.5, LerpedFloat.Chaser.EXP);
                                return true;
                            }
                            return false;
                        }
                        break;
                    }
                    y -= cardHeight + 9;
                    if (y < 0) {
                        break;
                    }
                }
            }
            float chaseTargetx = this.scroll.getChaseTarget();
            float max = -133.0F;
            for (ScheduleEntry scheduleEntry : this.schedule.entries) {
                int maxRows = 0;
                for (List<ScheduleWaitCondition> list : scheduleEntry.conditions) {
                    maxRows = Math.max(maxRows, list.size());
                }
                max += (float) (46 + maxRows * 18 + 10);
            }
            if (max > 0.0F) {
                chaseTargetx = (float) ((double) chaseTargetx - pDelta * 12.0);
                chaseTargetx = Mth.clamp(chaseTargetx, 0.0F, max);
                this.scroll.chase((double) ((int) chaseTargetx), 0.7F, LerpedFloat.Chaser.EXP);
            } else {
                this.scroll.chase(0.0, 0.7F, LerpedFloat.Chaser.EXP);
            }
            return super.m_6050_(pMouseX, pMouseY, pDelta);
        } else {
            return super.m_6050_(pMouseX, pMouseY, pDelta);
        }
    }

    @Override
    protected void renderForeground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        PoseStack matrixStack = graphics.pose();
        if (this.destinationSuggestions != null) {
            matrixStack.pushPose();
            matrixStack.translate(0.0F, 0.0F, 500.0F);
            this.destinationSuggestions.m_280540_(graphics, mouseX, mouseY);
            matrixStack.popPose();
        }
        super.renderForeground(graphics, mouseX, mouseY, partialTicks);
        GuiGameElement.of(((ScheduleMenu) this.f_97732_).contentHolder).<GuiGameElement.GuiRenderBuilder>at((float) (this.f_97735_ + AllGuiTextures.SCHEDULE.width), (float) (this.f_97736_ + AllGuiTextures.SCHEDULE.height - 56), -200.0F).scale(3.0).render(graphics);
        this.action(graphics, (double) mouseX, (double) mouseY, -1);
        if (this.editingCondition != null || this.editingDestination != null) {
            int x = this.f_97735_ + 53;
            int y = this.f_97736_ + 87;
            if (mouseX >= x && mouseY >= y && mouseX < x + 120 && mouseY < y + 18) {
                IScheduleInput rendered = (IScheduleInput) (this.editingCondition == null ? this.editingDestination : this.editingCondition);
                for (int i = 0; i < Math.max(1, rendered.slotsTargeted()); i++) {
                    List<Component> secondLineTooltip = rendered.getSecondLineTooltip(i);
                    if (secondLineTooltip != null && this.f_97734_ == ((ScheduleMenu) this.f_97732_).m_38853_(36 + i) && this.f_97734_.getItem().isEmpty()) {
                        this.renderActionTooltip(graphics, secondLineTooltip, mouseX, mouseY);
                    }
                }
            }
        }
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY) {
        AllGuiTextures.SCHEDULE.render(graphics, this.f_97735_, this.f_97736_);
        FormattedCharSequence formattedcharsequence = this.f_96539_.getVisualOrderText();
        int center = this.f_97735_ + (AllGuiTextures.SCHEDULE.width - 8) / 2;
        graphics.drawString(this.f_96547_, formattedcharsequence, (float) (center - this.f_96547_.width(formattedcharsequence) / 2), (float) this.f_97736_ + 4.0F, 5263440, false);
        this.renderSchedule(graphics, pMouseX, pMouseY, pPartialTick);
        if (this.editingCondition != null || this.editingDestination != null) {
            graphics.fillGradient(0, 0, this.f_96543_, this.f_96544_, -1072689136, -804253680);
            AllGuiTextures.SCHEDULE_EDITOR.render(graphics, this.f_97735_ - 2, this.f_97736_ + 40);
            AllGuiTextures.PLAYER_INVENTORY.render(graphics, this.f_97735_ + 38, this.f_97736_ + 122);
            graphics.drawString(this.f_96547_, this.f_169604_, this.f_97735_ + 46, this.f_97736_ + 128, 5263440, false);
            formattedcharsequence = this.editingCondition == null ? Lang.translateDirect("schedule.instruction.editor").getVisualOrderText() : Lang.translateDirect("schedule.condition.editor").getVisualOrderText();
            graphics.drawString(this.f_96547_, formattedcharsequence, (float) (center - this.f_96547_.width(formattedcharsequence) / 2), (float) this.f_97736_ + 44.0F, 5263440, false);
            IScheduleInput rendered = (IScheduleInput) (this.editingCondition == null ? this.editingDestination : this.editingCondition);
            for (int i = 0; i < rendered.slotsTargeted(); i++) {
                AllGuiTextures.SCHEDULE_EDITOR_ADDITIONAL_SLOT.render(graphics, this.f_97735_ + 53 + 20 * i, this.f_97736_ + 87);
            }
            if (rendered.slotsTargeted() == 0 && !rendered.renderSpecialIcon(graphics, this.f_97735_ + 54, this.f_97736_ + 88)) {
                Pair<ItemStack, Component> summary = rendered.getSummary();
                ItemStack icon = summary.getFirst();
                if (icon.isEmpty()) {
                    icon = rendered.getSecondLineIcon();
                }
                if (icon.isEmpty()) {
                    AllGuiTextures.SCHEDULE_EDITOR_INACTIVE_SLOT.render(graphics, this.f_97735_ + 53, this.f_97736_ + 87);
                } else {
                    GuiGameElement.of(icon).<RenderElement>at((float) (this.f_97735_ + 54), (float) (this.f_97736_ + 88)).render(graphics);
                }
            }
            PoseStack pPoseStack = graphics.pose();
            pPoseStack.pushPose();
            pPoseStack.translate(0.0F, (float) (this.getGuiTop() + 87), 0.0F);
            this.editorSubWidgets.renderWidgetBG(this.getGuiLeft() + 77, graphics);
            pPoseStack.popPose();
        }
    }

    @Override
    public void removed() {
        super.m_7861_();
        AllPackets.getChannel().sendToServer(new ScheduleEditPacket(this.schedule));
    }

    @Override
    public List<Rect2i> getExtraAreas() {
        return this.extraAreas;
    }

    public Font getFont() {
        return this.f_96547_;
    }
}