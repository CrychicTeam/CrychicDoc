package com.simibubi.create.content.trains;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPackets;
import com.simibubi.create.content.contraptions.actors.trainControls.ControlsBlock;
import com.simibubi.create.content.contraptions.actors.trainControls.ControlsHandler;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.ControlsUtil;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class TrainHUD {

    public static final IGuiOverlay OVERLAY = TrainHUD::renderOverlay;

    static LerpedFloat displayedSpeed = LerpedFloat.linear();

    static LerpedFloat displayedThrottle = LerpedFloat.linear();

    static LerpedFloat displayedPromptSize = LerpedFloat.linear();

    static Double editedThrottle = null;

    static int hudPacketCooldown = 5;

    static int honkPacketCooldown = 5;

    public static Component currentPrompt;

    public static boolean currentPromptShadow;

    public static int promptKeepAlive = 0;

    static boolean usedToHonk;

    public static void tick() {
        if (promptKeepAlive > 0) {
            promptKeepAlive--;
        } else {
            currentPrompt = null;
        }
        Minecraft mc = Minecraft.getInstance();
        displayedPromptSize.chase(currentPrompt != null ? (double) (mc.font.width(currentPrompt) + 17) : 0.0, 0.5, LerpedFloat.Chaser.EXP);
        displayedPromptSize.tickChaser();
        Carriage carriage = getCarriage();
        if (carriage != null) {
            Train train = carriage.train;
            double value = Math.abs(train.speed) / (double) (train.maxSpeed() * AllConfigs.server().trains.manualTrainSpeedModifier.getF());
            value = Mth.clamp(value + 0.05F, 0.0, 1.0);
            displayedSpeed.chase((double) ((float) ((int) (value * 18.0)) / 18.0F), 0.5, LerpedFloat.Chaser.EXP);
            displayedSpeed.tickChaser();
            displayedThrottle.chase(editedThrottle != null ? editedThrottle : train.throttle, 0.75, LerpedFloat.Chaser.EXP);
            displayedThrottle.tickChaser();
            boolean isSprintKeyPressed = ControlsUtil.isActuallyPressed(mc.options.keySprint);
            if (isSprintKeyPressed && honkPacketCooldown-- <= 0) {
                train.determineHonk(mc.level);
                if (train.lowHonk != null) {
                    AllPackets.getChannel().sendToServer(new HonkPacket.Serverbound(train, true));
                    honkPacketCooldown = 5;
                    usedToHonk = true;
                }
            }
            if (!isSprintKeyPressed && usedToHonk) {
                AllPackets.getChannel().sendToServer(new HonkPacket.Serverbound(train, false));
                honkPacketCooldown = 0;
                usedToHonk = false;
            }
            if (editedThrottle != null) {
                if (Mth.equal(editedThrottle, train.throttle)) {
                    editedThrottle = null;
                    hudPacketCooldown = 5;
                } else {
                    if (hudPacketCooldown-- <= 0) {
                        AllPackets.getChannel().sendToServer(new TrainHUDUpdatePacket.Serverbound(train, editedThrottle));
                        hudPacketCooldown = 5;
                    }
                }
            }
        }
    }

    private static Carriage getCarriage() {
        return ControlsHandler.getContraption() instanceof CarriageContraptionEntity cce ? cce.getCarriage() : null;
    }

    public static void renderOverlay(ForgeGui gui, GuiGraphics graphics, float partialTicks, int width, int height) {
        Minecraft mc = Minecraft.getInstance();
        if (!mc.options.hideGui && mc.gameMode.getPlayerMode() != GameType.SPECTATOR) {
            if (ControlsHandler.getContraption() instanceof CarriageContraptionEntity cce) {
                Carriage carriage = cce.getCarriage();
                if (carriage != null) {
                    Entity cameraEntity = Minecraft.getInstance().getCameraEntity();
                    if (cameraEntity != null) {
                        BlockPos localPos = ControlsHandler.getControlsPos();
                        if (localPos != null) {
                            PoseStack poseStack = graphics.pose();
                            poseStack.pushPose();
                            poseStack.translate((float) (width / 2 - 91), (float) (height - 29), 0.0F);
                            AllGuiTextures.TRAIN_HUD_FRAME.render(graphics, -2, 1);
                            AllGuiTextures.TRAIN_HUD_SPEED_BG.render(graphics, 0, 0);
                            int w = (int) ((float) AllGuiTextures.TRAIN_HUD_SPEED.width * displayedSpeed.getValue(partialTicks));
                            int h = AllGuiTextures.TRAIN_HUD_SPEED.height;
                            graphics.blit(AllGuiTextures.TRAIN_HUD_SPEED.location, 0, 0, 0, (float) AllGuiTextures.TRAIN_HUD_SPEED.startX, (float) AllGuiTextures.TRAIN_HUD_SPEED.startY, w, h, 256, 256);
                            int promptSize = (int) displayedPromptSize.getValue(partialTicks);
                            if (promptSize > 1) {
                                poseStack.pushPose();
                                poseStack.translate((float) promptSize / -2.0F + 91.0F, -27.0F, 100.0F);
                                AllGuiTextures.TRAIN_PROMPT_L.render(graphics, -3, 0);
                                AllGuiTextures.TRAIN_PROMPT_R.render(graphics, promptSize, 0);
                                graphics.blit(AllGuiTextures.TRAIN_PROMPT.location, 0, 0, 0, (float) AllGuiTextures.TRAIN_PROMPT.startX + (128.0F - (float) promptSize / 2.0F), (float) AllGuiTextures.TRAIN_PROMPT.startY, promptSize, AllGuiTextures.TRAIN_PROMPT.height, 256, 256);
                                poseStack.popPose();
                                Font font = mc.font;
                                if (currentPrompt != null && font.width(currentPrompt) < promptSize - 10) {
                                    poseStack.pushPose();
                                    poseStack.translate((float) font.width(currentPrompt) / -2.0F + 82.0F, -27.0F, 100.0F);
                                    if (currentPromptShadow) {
                                        graphics.drawString(font, currentPrompt, 9, 4, 5524805);
                                    } else {
                                        graphics.drawString(font, currentPrompt, 9, 4, 5524805, false);
                                    }
                                    poseStack.popPose();
                                }
                            }
                            AllGuiTextures.TRAIN_HUD_DIRECTION.render(graphics, 77, -20);
                            w = (int) ((float) AllGuiTextures.TRAIN_HUD_THROTTLE.width * (1.0F - displayedThrottle.getValue(partialTicks)));
                            int invW = AllGuiTextures.TRAIN_HUD_THROTTLE.width - w;
                            graphics.blit(AllGuiTextures.TRAIN_HUD_THROTTLE.location, invW, 0, 0, (float) (AllGuiTextures.TRAIN_HUD_THROTTLE.startX + invW), (float) AllGuiTextures.TRAIN_HUD_THROTTLE.startY, w, h, 256, 256);
                            AllGuiTextures.TRAIN_HUD_THROTTLE_POINTER.render(graphics, Math.max(1, AllGuiTextures.TRAIN_HUD_THROTTLE.width - w) - 3, -2);
                            StructureTemplate.StructureBlockInfo info = (StructureTemplate.StructureBlockInfo) cce.getContraption().getBlocks().get(localPos);
                            Direction initialOrientation = cce.getInitialOrientation().getCounterClockWise();
                            boolean inverted = false;
                            if (info != null && info.state().m_61138_(ControlsBlock.f_54117_)) {
                                inverted = !((Direction) info.state().m_61143_(ControlsBlock.f_54117_)).equals(initialOrientation);
                            }
                            boolean reversing = ControlsHandler.currentlyPressed.contains(1);
                            inverted ^= reversing;
                            int angleOffset = (ControlsHandler.currentlyPressed.contains(2) ? -45 : 0) + (ControlsHandler.currentlyPressed.contains(3) ? 45 : 0);
                            if (reversing) {
                                angleOffset *= -1;
                            }
                            float snapSize = 22.5F;
                            float diff = AngleHelper.getShortestAngleDiff((double) cameraEntity.getYRot(), (double) cce.yaw) + (float) (inverted ? -90 : 90);
                            if (Math.abs(diff) < 60.0F) {
                                diff = 0.0F;
                            }
                            float angle = diff + (float) angleOffset;
                            float snappedAngle = snapSize * (float) Math.round(angle / snapSize) % 360.0F;
                            poseStack.translate(91.0F, -9.0F, 0.0F);
                            poseStack.scale(0.925F, 0.925F, 1.0F);
                            PlacementHelpers.textured(poseStack, 0.0F, 0.0F, 1.0F, snappedAngle);
                            poseStack.popPose();
                        }
                    }
                }
            }
        }
    }

    public static boolean onScroll(double delta) {
        Carriage carriage = getCarriage();
        if (carriage == null) {
            return false;
        } else {
            double prevThrottle = editedThrottle == null ? carriage.train.throttle : editedThrottle;
            editedThrottle = Mth.clamp(prevThrottle + (double) ((float) (delta > 0.0 ? 1 : -1) / 18.0F), 0.055555556F, 1.0);
            return true;
        }
    }
}