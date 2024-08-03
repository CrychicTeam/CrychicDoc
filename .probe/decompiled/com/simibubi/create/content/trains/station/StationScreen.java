package com.simibubi.create.content.trains.station;

import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPackets;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.decoration.slidingDoor.DoorControl;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.entity.TrainIconType;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.UIRenderHelper;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.gui.widget.Label;
import com.simibubi.create.foundation.gui.widget.ScrollInput;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.Pair;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;

public class StationScreen extends AbstractStationScreen {

    private EditBox nameBox;

    private EditBox trainNameBox;

    private IconButton newTrainButton;

    private IconButton disassembleTrainButton;

    private IconButton dropScheduleButton;

    private int leavingAnimation;

    private LerpedFloat trainPosition;

    private DoorControl doorControl;

    private boolean switchingToAssemblyMode;

    public StationScreen(StationBlockEntity be, GlobalStation station) {
        super(be, station);
        this.background = AllGuiTextures.STATION;
        this.leavingAnimation = 0;
        this.trainPosition = LerpedFloat.linear().startWithValue(0.0);
        this.switchingToAssemblyMode = false;
        this.doorControl = be.doorControls.mode;
    }

    @Override
    protected void init() {
        super.init();
        int x = this.guiLeft;
        int y = this.guiTop;
        Consumer<String> onTextChanged = s -> this.nameBox.m_252865_(this.nameBoxX(s, this.nameBox));
        this.nameBox = new EditBox(new NoShadowFontWrapper(this.f_96547_), x + 23, y + 4, this.background.width - 20, 10, Components.literal(this.station.name));
        this.nameBox.setBordered(false);
        this.nameBox.setMaxLength(25);
        this.nameBox.setTextColor(5841956);
        this.nameBox.setValue(this.station.name);
        this.nameBox.setFocused(false);
        this.nameBox.m_6375_(0.0, 0.0, 0);
        this.nameBox.setResponder(onTextChanged);
        this.nameBox.m_252865_(this.nameBoxX(this.nameBox.getValue(), this.nameBox));
        this.m_142416_(this.nameBox);
        Runnable assemblyCallback = () -> {
            this.switchingToAssemblyMode = true;
            this.f_96541_.setScreen(new AssemblyScreen(this.blockEntity, this.station));
        };
        this.newTrainButton = new WideIconButton(x + 84, y + 65, AllGuiTextures.I_NEW_TRAIN);
        this.newTrainButton.withCallback(assemblyCallback);
        this.m_142416_(this.newTrainButton);
        this.disassembleTrainButton = new WideIconButton(x + 94, y + 65, AllGuiTextures.I_DISASSEMBLE_TRAIN);
        this.disassembleTrainButton.f_93623_ = false;
        this.disassembleTrainButton.f_93624_ = false;
        this.disassembleTrainButton.withCallback(assemblyCallback);
        this.m_142416_(this.disassembleTrainButton);
        this.dropScheduleButton = new IconButton(x + 73, y + 65, AllIcons.I_VIEW_SCHEDULE);
        this.dropScheduleButton.f_93623_ = false;
        this.dropScheduleButton.f_93624_ = false;
        this.dropScheduleButton.withCallback(() -> AllPackets.getChannel().sendToServer(StationEditPacket.dropSchedule(this.blockEntity.m_58899_())));
        this.m_142416_(this.dropScheduleButton);
        onTextChanged = s -> this.trainNameBox.m_252865_(this.nameBoxX(s, this.trainNameBox));
        this.trainNameBox = new EditBox(this.f_96547_, x + 23, y + 47, this.background.width - 75, 10, Components.immutableEmpty());
        this.trainNameBox.setBordered(false);
        this.trainNameBox.setMaxLength(35);
        this.trainNameBox.setTextColor(13027014);
        this.trainNameBox.setFocused(false);
        this.trainNameBox.m_6375_(0.0, 0.0, 0);
        this.trainNameBox.setResponder(onTextChanged);
        this.trainNameBox.f_93623_ = false;
        this.tickTrainDisplay();
        Pair<ScrollInput, Label> doorControlWidgets = DoorControl.createWidget(x + 35, y + 102, mode -> this.doorControl = mode, this.doorControl);
        this.m_142416_(doorControlWidgets.getFirst());
        this.m_142416_(doorControlWidgets.getSecond());
    }

    @Override
    public void tick() {
        this.tickTrainDisplay();
        if (this.m_7222_() != this.nameBox) {
            this.nameBox.setCursorPosition(this.nameBox.getValue().length());
            this.nameBox.setHighlightPos(this.nameBox.getCursorPosition());
        }
        if (this.m_7222_() != this.trainNameBox || !this.trainNameBox.f_93623_) {
            this.trainNameBox.setCursorPosition(this.trainNameBox.getValue().length());
            this.trainNameBox.setHighlightPos(this.trainNameBox.getCursorPosition());
        }
        super.tick();
        this.updateAssemblyTooltip(this.blockEntity.edgePoint.isOnCurve() ? "no_assembly_curve" : (!this.blockEntity.edgePoint.isOrthogonal() ? "no_assembly_diagonal" : (this.trainPresent() && !this.blockEntity.trainCanDisassemble ? "train_not_aligned" : null)));
    }

    private void tickTrainDisplay() {
        Train train = (Train) this.displayedTrain.get();
        if (train == null) {
            if (this.trainNameBox.f_93623_) {
                this.trainNameBox.f_93623_ = false;
                this.m_169411_(this.trainNameBox);
            }
            this.leavingAnimation = 0;
            this.newTrainButton.f_93623_ = this.blockEntity.edgePoint.isOrthogonal();
            this.newTrainButton.f_93624_ = true;
            Train imminentTrain = this.getImminent();
            if (imminentTrain != null) {
                this.displayedTrain = new WeakReference(imminentTrain);
                this.newTrainButton.f_93623_ = false;
                this.newTrainButton.f_93624_ = false;
                this.disassembleTrainButton.f_93623_ = false;
                this.disassembleTrainButton.f_93624_ = true;
                this.dropScheduleButton.f_93623_ = this.blockEntity.trainHasSchedule;
                this.dropScheduleButton.f_93624_ = true;
                this.trainNameBox.f_93623_ = true;
                this.trainNameBox.setValue(imminentTrain.name.getString());
                this.trainNameBox.m_252865_(this.nameBoxX(this.trainNameBox.getValue(), this.trainNameBox));
                this.m_142416_(this.trainNameBox);
                int trainIconWidth = this.getTrainIconWidth(imminentTrain);
                int targetPos = this.background.width / 2 - trainIconWidth / 2;
                if (trainIconWidth > 130) {
                    targetPos -= trainIconWidth - 130;
                }
                float f = (float) (imminentTrain.navigation.distanceToDestination / 15.0);
                if (this.trainPresent()) {
                    f = 0.0F;
                }
                this.trainPosition.startWithValue((double) ((float) targetPos - (float) (targetPos + 5) * f));
            }
        } else {
            int trainIconWidthx = this.getTrainIconWidth(train);
            int targetPosx = this.background.width / 2 - trainIconWidthx / 2;
            if (trainIconWidthx > 130) {
                targetPosx -= trainIconWidthx - 130;
            }
            if (this.leavingAnimation > 0) {
                this.disassembleTrainButton.f_93623_ = false;
                float f = 1.0F - (float) this.leavingAnimation / 80.0F;
                this.trainPosition.setValue((double) ((float) targetPosx + f * f * f * (float) (this.background.width - targetPosx + 5)));
                this.leavingAnimation--;
                if (this.leavingAnimation <= 0) {
                    this.displayedTrain = new WeakReference(null);
                    this.disassembleTrainButton.f_93624_ = false;
                    this.dropScheduleButton.f_93623_ = false;
                    this.dropScheduleButton.f_93624_ = false;
                }
            } else if (this.getImminent() != train) {
                this.leavingAnimation = 80;
            } else {
                boolean trainAtStation = this.trainPresent();
                this.disassembleTrainButton.f_93623_ = trainAtStation && this.blockEntity.trainCanDisassemble && this.blockEntity.edgePoint.isOrthogonal();
                this.dropScheduleButton.f_93623_ = this.blockEntity.trainHasSchedule;
                if (this.blockEntity.trainHasSchedule) {
                    this.dropScheduleButton.setToolTip(Lang.translateDirect(this.blockEntity.trainHasAutoSchedule ? "station.remove_auto_schedule" : "station.remove_schedule"));
                } else {
                    this.dropScheduleButton.getToolTip().clear();
                }
                float f = trainAtStation ? 0.0F : (float) (train.navigation.distanceToDestination / 30.0);
                this.trainPosition.setValue((double) ((float) targetPosx - (float) (targetPosx + trainIconWidthx) * f));
            }
        }
    }

    private int nameBoxX(String s, EditBox nameBox) {
        return this.guiLeft + this.background.width / 2 - (Math.min(this.f_96547_.width(s), nameBox.m_5711_()) + 10) / 2;
    }

    private void updateAssemblyTooltip(String key) {
        if (key == null) {
            this.disassembleTrainButton.setToolTip(Lang.translateDirect("station.disassemble_train"));
            this.newTrainButton.setToolTip(Lang.translateDirect("station.create_train"));
        } else {
            for (IconButton ib : new IconButton[] { this.disassembleTrainButton, this.newTrainButton }) {
                List<Component> toolTip = ib.getToolTip();
                toolTip.clear();
                toolTip.add(Lang.translateDirect("station." + key).withStyle(ChatFormatting.GRAY));
                toolTip.add(Lang.translateDirect("station." + key + "_1").withStyle(ChatFormatting.GRAY));
            }
        }
    }

    @Override
    protected void renderWindow(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.renderWindow(graphics, mouseX, mouseY, partialTicks);
        int x = this.guiLeft;
        int y = this.guiTop;
        String text = this.nameBox.getValue();
        if (!this.nameBox.m_93696_()) {
            AllGuiTextures.STATION_EDIT_NAME.render(graphics, this.nameBoxX(text, this.nameBox) + this.f_96547_.width(text) + 5, y + 1);
        }
        graphics.renderItem(AllBlocks.TRAIN_DOOR.asStack(), x + 14, y + 103);
        Train train = (Train) this.displayedTrain.get();
        if (train == null) {
            MutableComponent header = Lang.translateDirect("station.idle");
            graphics.drawString(this.f_96547_, header, x + 97 - this.f_96547_.width(header) / 2, y + 47, 8026746, false);
        } else {
            float position = this.trainPosition.getValue(partialTicks);
            PoseStack ms = graphics.pose();
            ms.pushPose();
            RenderSystem.enableBlend();
            ms.translate(position, 0.0F, 0.0F);
            TrainIconType icon = train.icon;
            int offset = 0;
            List<Carriage> carriages = train.carriages;
            for (int i = carriages.size() - 1; i > 0; i--) {
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, Math.min(1.0F, Math.min((position + (float) offset - 10.0F) / 30.0F, ((float) (this.background.width - 40) - position - (float) offset) / 30.0F)));
                Carriage carriage = (Carriage) carriages.get(this.blockEntity.trainBackwards ? carriages.size() - i - 1 : i);
                offset += icon.render(carriage.bogeySpacing, graphics, x + offset, y + 20) + 1;
            }
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, Math.min(1.0F, Math.min((position + (float) offset - 10.0F) / 30.0F, ((float) (this.background.width - 40) - position - (float) offset) / 30.0F)));
            offset += icon.render(-1, graphics, x + offset, y + 20);
            RenderSystem.disableBlend();
            ms.popPose();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            AllGuiTextures.STATION_TEXTBOX_TOP.render(graphics, x + 21, y + 42);
            UIRenderHelper.drawStretched(graphics, x + 21, y + 60, 150, 26, 0, AllGuiTextures.STATION_TEXTBOX_MIDDLE);
            AllGuiTextures.STATION_TEXTBOX_BOTTOM.render(graphics, x + 21, y + 86);
            ms.pushPose();
            ms.translate(Mth.clamp(position + (float) offset - 13.0F, 25.0F, 159.0F), 0.0F, 0.0F);
            AllGuiTextures.STATION_TEXTBOX_SPEECH.render(graphics, x, y + 38);
            ms.popPose();
            text = this.trainNameBox.getValue();
            if (!this.trainNameBox.m_93696_()) {
                int buttonX = this.nameBoxX(text, this.trainNameBox) + this.f_96547_.width(text) + 5;
                AllGuiTextures.STATION_EDIT_TRAIN_NAME.render(graphics, Math.min(buttonX, this.guiLeft + 156), y + 44);
                if (this.f_96547_.width(text) > this.trainNameBox.m_5711_()) {
                    graphics.drawString(this.f_96547_, "...", this.guiLeft + 26, this.guiTop + 47, 10921638);
                }
            }
        }
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (!this.nameBox.m_93696_() && pMouseY > (double) this.guiTop && pMouseY < (double) (this.guiTop + 14) && pMouseX > (double) this.guiLeft && pMouseX < (double) (this.guiLeft + this.background.width)) {
            this.nameBox.setFocused(true);
            this.nameBox.setHighlightPos(0);
            this.m_7522_(this.nameBox);
            return true;
        } else if (this.trainNameBox.f_93623_ && !this.trainNameBox.m_93696_() && pMouseY > (double) (this.guiTop + 45) && pMouseY < (double) (this.guiTop + 58) && pMouseX > (double) (this.guiLeft + 25) && pMouseX < (double) (this.guiLeft + 168)) {
            this.trainNameBox.setFocused(true);
            this.trainNameBox.setHighlightPos(0);
            this.m_7522_(this.trainNameBox);
            return true;
        } else {
            return super.m_6375_(pMouseX, pMouseY, pButton);
        }
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        boolean hitEnter = this.m_7222_() instanceof EditBox && (pKeyCode == 257 || pKeyCode == 335);
        if (hitEnter && this.nameBox.m_93696_()) {
            this.nameBox.setFocused(false);
            this.syncStationName();
            return true;
        } else if (hitEnter && this.trainNameBox.m_93696_()) {
            this.trainNameBox.setFocused(false);
            this.syncTrainName();
            return true;
        } else {
            return super.m_7933_(pKeyCode, pScanCode, pModifiers);
        }
    }

    private void syncTrainName() {
        Train train = (Train) this.displayedTrain.get();
        if (train != null && !this.trainNameBox.getValue().equals(train.name.getString())) {
            AllPackets.getChannel().sendToServer(new TrainEditPacket(train.id, this.trainNameBox.getValue(), train.icon.getId()));
        }
    }

    private void syncStationName() {
        if (!this.nameBox.getValue().equals(this.station.name)) {
            AllPackets.getChannel().sendToServer(StationEditPacket.configure(this.blockEntity.m_58899_(), false, this.nameBox.getValue(), this.doorControl));
        }
    }

    @Override
    public void removed() {
        super.m_7861_();
        if (this.nameBox != null && this.trainNameBox != null) {
            AllPackets.getChannel().sendToServer(StationEditPacket.configure(this.blockEntity.m_58899_(), this.switchingToAssemblyMode, this.nameBox.getValue(), this.doorControl));
            Train train = (Train) this.displayedTrain.get();
            if (train != null) {
                if (!this.switchingToAssemblyMode) {
                    AllPackets.getChannel().sendToServer(new TrainEditPacket(train.id, this.trainNameBox.getValue(), train.icon.getId()));
                } else {
                    this.blockEntity.imminentTrain = null;
                }
            }
        }
    }

    @Override
    protected PartialModel getFlag(float partialTicks) {
        return this.blockEntity.flag.getValue(partialTicks) > 0.75F ? AllPartialModels.STATION_ON : AllPartialModels.STATION_OFF;
    }
}