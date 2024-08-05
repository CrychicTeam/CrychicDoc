package com.simibubi.create.foundation.blockEntity.behaviour;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPackets;
import com.simibubi.create.foundation.gui.ScreenOpener;
import com.simibubi.create.foundation.utility.Color;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class ValueSettingsClient implements IGuiOverlay {

    private Minecraft mc;

    public int interactHeldTicks = -1;

    public BlockPos interactHeldPos = null;

    public BehaviourType<?> interactHeldBehaviour = null;

    public InteractionHand interactHeldHand = null;

    public Direction interactHeldFace = null;

    public List<MutableComponent> lastHoverTip;

    public int hoverTicks;

    public int hoverWarmup;

    public ValueSettingsClient() {
        this.mc = Minecraft.getInstance();
    }

    public void cancelIfWarmupAlreadyStarted(PlayerInteractEvent.RightClickBlock event) {
        if (this.interactHeldTicks != -1 && event.getPos().equals(this.interactHeldPos)) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.FAIL);
        }
    }

    public void startInteractionWith(BlockPos pos, BehaviourType<?> behaviourType, InteractionHand hand, Direction side) {
        this.interactHeldTicks = 0;
        this.interactHeldPos = pos;
        this.interactHeldBehaviour = behaviourType;
        this.interactHeldHand = hand;
        this.interactHeldFace = side;
    }

    public void cancelInteraction() {
        this.interactHeldTicks = -1;
    }

    public void tick() {
        if (this.hoverWarmup > 0) {
            this.hoverWarmup--;
        }
        if (this.hoverTicks > 0) {
            this.hoverTicks--;
        }
        if (this.interactHeldTicks != -1) {
            Player player = this.mc.player;
            if (ValueSettingsInputHandler.canInteract(player) && !AllBlocks.CLIPBOARD.isIn(player.m_21205_())) {
                if (!(this.mc.hitResult instanceof BlockHitResult blockHitResult) || !blockHitResult.getBlockPos().equals(this.interactHeldPos)) {
                    this.cancelInteraction();
                    return;
                }
                if (!(BlockEntityBehaviour.get(this.mc.level, this.interactHeldPos, this.interactHeldBehaviour) instanceof ValueSettingsBehaviour valueSettingBehaviour) || !valueSettingBehaviour.testHit(blockHitResult.m_82450_())) {
                    this.cancelInteraction();
                    return;
                }
                if (!this.mc.options.keyUse.isDown()) {
                    AllPackets.getChannel().sendToServer(new ValueSettingsPacket(this.interactHeldPos, 0, 0, this.interactHeldHand, this.interactHeldFace, false));
                    this.cancelInteraction();
                } else {
                    if (this.interactHeldTicks > 3) {
                        player.f_20911_ = false;
                    }
                    if (this.interactHeldTicks++ >= 5) {
                        ScreenOpener.open(new ValueSettingsScreen(this.interactHeldPos, valueSettingBehaviour.createBoard(player, blockHitResult), valueSettingBehaviour.getValueSettings(), valueSettingBehaviour::newSettingHovered));
                        this.interactHeldTicks = -1;
                    }
                }
            } else {
                this.cancelInteraction();
            }
        }
    }

    public void showHoverTip(List<MutableComponent> tip) {
        if (this.mc.screen == null) {
            if (this.hoverWarmup < 6) {
                this.hoverWarmup += 2;
            } else {
                this.hoverWarmup++;
                this.hoverTicks = this.hoverTicks == 0 ? 11 : Math.max(this.hoverTicks, 6);
                this.lastHoverTip = tip;
            }
        }
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics graphics, float partialTicks, int width, int height) {
        Minecraft mc = Minecraft.getInstance();
        if (!mc.options.hideGui && ValueSettingsInputHandler.canInteract(mc.player)) {
            if (this.hoverTicks != 0 && this.lastHoverTip != null) {
                int x = width / 2;
                int y = height - 75 - this.lastHoverTip.size() * 12;
                float alpha = this.hoverTicks > 5 ? (float) (11 - this.hoverTicks) / 5.0F : Math.min(1.0F, (float) this.hoverTicks / 5.0F);
                Color color = new Color(16777215);
                Color titleColor = new Color(16505981);
                color.setAlpha(alpha);
                titleColor.setAlpha(alpha);
                for (int i = 0; i < this.lastHoverTip.size(); i++) {
                    MutableComponent mutableComponent = (MutableComponent) this.lastHoverTip.get(i);
                    graphics.drawString(mc.font, mutableComponent, x - mc.font.width(mutableComponent) / 2, y, (i == 0 ? titleColor : color).getRGB());
                    y += 12;
                }
            }
        }
    }
}