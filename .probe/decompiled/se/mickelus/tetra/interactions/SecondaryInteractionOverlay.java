package se.mickelus.tetra.interactions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiRoot;
import se.mickelus.tetra.client.keymap.TetraKeyMappings;

public class SecondaryInteractionOverlay extends GuiRoot implements IGuiOverlay {

    SecondaryInteraction currentInteraction;

    SecondaryInteractionGui currentDisplay;

    boolean wasKeyDown = false;

    public SecondaryInteractionOverlay(Minecraft minecraft) {
        super(minecraft);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.Key event) {
        if (!TetraKeyMappings.secondaryUseBinding.isDown() && this.wasKeyDown) {
            BlockPos blockPos = this.mc.hitResult.getType() == HitResult.Type.BLOCK ? ((BlockHitResult) this.mc.hitResult).getBlockPos() : null;
            Entity entity = this.mc.hitResult.getType() == HitResult.Type.ENTITY ? ((EntityHitResult) this.mc.hitResult).getEntity() : null;
            this.updateCurrentInteraction(blockPos, entity);
            if (this.currentInteraction != null) {
                SecondaryInteractionHandler.dispatchInteraction(this.currentInteraction, this.mc.player, blockPos, entity);
            }
        }
        this.wasKeyDown = TetraKeyMappings.secondaryUseBinding.isDown();
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        this.draw(guiGraphics);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (TickEvent.Phase.END == event.phase && this.mc.level != null && this.mc.level.m_46467_() % 10L == 0L) {
            this.updateCurrentInteraction(this.mc.hitResult.getType() == HitResult.Type.BLOCK ? ((BlockHitResult) this.mc.hitResult).getBlockPos() : null, this.mc.hitResult.getType() == HitResult.Type.ENTITY ? ((EntityHitResult) this.mc.hitResult).getEntity() : null);
        }
    }

    private void updateCurrentInteraction(BlockPos pos, Entity target) {
        SecondaryInteraction newInteraction = SecondaryInteractionHandler.findRelevantAction(this.mc.player, pos, target);
        boolean changed = newInteraction != this.currentInteraction;
        if (this.currentInteraction != null && changed) {
            if (this.currentDisplay != null) {
                this.currentDisplay.hide();
            }
            this.currentInteraction = null;
        }
        if (newInteraction != null && changed) {
            this.currentInteraction = newInteraction;
            int offset = Math.min((int) ((double) (-this.mc.getWindow().getGuiScaledWidth()) * 0.3), -120);
            this.currentDisplay = new SecondaryInteractionGui(offset, -1, this.currentInteraction);
            this.currentDisplay.setAttachmentPoint(GuiAttachment.middleLeft);
            this.currentDisplay.setAttachmentAnchor(GuiAttachment.middleRight);
            this.currentDisplay.show();
            this.addChild(this.currentDisplay);
        }
    }
}