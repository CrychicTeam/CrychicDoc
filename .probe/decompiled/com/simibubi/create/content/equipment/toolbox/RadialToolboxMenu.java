package com.simibubi.create.content.equipment.toolbox;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllKeys;
import com.simibubi.create.AllPackets;
import com.simibubi.create.foundation.gui.AbstractSimiScreen;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.element.RenderElement;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.Lang;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

public class RadialToolboxMenu extends AbstractSimiScreen {

    private RadialToolboxMenu.State state;

    private int ticksOpen;

    private int hoveredSlot;

    private boolean scrollMode;

    private int scrollSlot = 0;

    private List<ToolboxBlockEntity> toolboxes;

    private ToolboxBlockEntity selectedBox;

    private static final int DEPOSIT = -7;

    private static final int UNEQUIP = -5;

    public RadialToolboxMenu(List<ToolboxBlockEntity> toolboxes, RadialToolboxMenu.State state, @Nullable ToolboxBlockEntity selectedBox) {
        this.toolboxes = toolboxes;
        this.state = state;
        this.hoveredSlot = -1;
        if (selectedBox != null) {
            this.selectedBox = selectedBox;
        }
    }

    public void prevSlot(int slot) {
        this.scrollSlot = slot;
    }

    @Override
    protected void renderWindow(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        float fade = Mth.clamp(((float) this.ticksOpen + AnimationTickHolder.getPartialTicks()) / 10.0F, 0.001953125F, 1.0F);
        this.hoveredSlot = -1;
        Window window = this.getMinecraft().getWindow();
        float hoveredX = (float) (mouseX - window.getGuiScaledWidth() / 2);
        float hoveredY = (float) (mouseY - window.getGuiScaledHeight() / 2);
        float distance = hoveredX * hoveredX + hoveredY * hoveredY;
        if (distance > 25.0F && distance < 10000.0F) {
            this.hoveredSlot = Mth.floor(AngleHelper.deg(Mth.atan2((double) hoveredY, (double) hoveredX)) + 360.0F + 180.0F - 22.5F) % 360 / 45;
        }
        boolean renderCenterSlot = this.state == RadialToolboxMenu.State.SELECT_ITEM_UNEQUIP;
        if (this.scrollMode && distance > 150.0F) {
            this.scrollMode = false;
        }
        if (renderCenterSlot && distance <= 150.0F) {
            this.hoveredSlot = -5;
        }
        PoseStack ms = graphics.pose();
        ms.pushPose();
        ms.translate((float) (this.f_96543_ / 2), (float) (this.f_96544_ / 2), 0.0F);
        Component tip = null;
        if (this.state == RadialToolboxMenu.State.DETACH) {
            tip = Lang.translateDirect("toolbox.outOfRange");
            if (hoveredX > -20.0F && hoveredX < 20.0F && hoveredY > -80.0F && hoveredY < -20.0F) {
                this.hoveredSlot = -5;
            }
            ms.pushPose();
            AllGuiTextures.TOOLBELT_INACTIVE_SLOT.render(graphics, -12, -12);
            GuiGameElement.of(AllBlocks.TOOLBOXES.get(DyeColor.BROWN).asStack()).<RenderElement>at(-9.0F, -9.0F).render(graphics);
            ms.translate(0.0F, -40.0F + 10.0F * (1.0F - fade) * (1.0F - fade), 0.0F);
            AllGuiTextures.TOOLBELT_SLOT.render(graphics, -12, -12);
            ms.translate(-0.5, 0.5, 0.0);
            AllIcons.I_DISABLE.render(graphics, -9, -9);
            ms.translate(0.5, -0.5, 0.0);
            if (!this.scrollMode && this.hoveredSlot == -5) {
                AllGuiTextures.TOOLBELT_SLOT_HIGHLIGHT.render(graphics, -13, -13);
                tip = Lang.translateDirect("toolbox.detach").withStyle(ChatFormatting.GOLD);
            }
            ms.popPose();
        } else {
            if (hoveredX > 60.0F && hoveredX < 100.0F && hoveredY > -20.0F && hoveredY < 20.0F) {
                this.hoveredSlot = -7;
            }
            ms.pushPose();
            ms.translate(80.0F + -5.0F * (1.0F - fade) * (1.0F - fade), 0.0F, 0.0F);
            AllGuiTextures.TOOLBELT_SLOT.render(graphics, -12, -12);
            ms.translate(-0.5, 0.5, 0.0);
            AllIcons.I_TOOLBOX.render(graphics, -9, -9);
            ms.translate(0.5, -0.5, 0.0);
            if (!this.scrollMode && this.hoveredSlot == -7) {
                AllGuiTextures.TOOLBELT_SLOT_HIGHLIGHT.render(graphics, -13, -13);
                tip = Lang.translateDirect(this.state == RadialToolboxMenu.State.SELECT_BOX ? "toolbox.depositAll" : "toolbox.depositBox").withStyle(ChatFormatting.GOLD);
            }
            ms.popPose();
            for (int slot = 0; slot < 8; slot++) {
                ms.pushPose();
                ((TransformStack) ((TransformStack) TransformStack.cast(ms).rotateZ((double) (slot * 45 - 45))).translate(0.0, (double) (-40.0F + 10.0F * (1.0F - fade) * (1.0F - fade)), 0.0)).rotateZ((double) (-slot * 45 + 45));
                ms.translate(-12.0F, -12.0F, 0.0F);
                if (this.state == RadialToolboxMenu.State.SELECT_ITEM || this.state == RadialToolboxMenu.State.SELECT_ITEM_UNEQUIP) {
                    ToolboxInventory inv = this.selectedBox.inventory;
                    ItemStack stackInSlot = (ItemStack) inv.filters.get(slot);
                    if (!stackInSlot.isEmpty()) {
                        boolean empty = inv.getStackInSlot(slot * 4).isEmpty();
                        (empty ? AllGuiTextures.TOOLBELT_INACTIVE_SLOT : AllGuiTextures.TOOLBELT_SLOT).render(graphics, 0, 0);
                        GuiGameElement.of(stackInSlot).<RenderElement>at(3.0F, 3.0F).render(graphics);
                        if (slot == (this.scrollMode ? this.scrollSlot : this.hoveredSlot) && !empty) {
                            AllGuiTextures.TOOLBELT_SLOT_HIGHLIGHT.render(graphics, -1, -1);
                            tip = stackInSlot.getHoverName();
                        }
                    } else {
                        AllGuiTextures.TOOLBELT_EMPTY_SLOT.render(graphics, 0, 0);
                    }
                } else if (this.state == RadialToolboxMenu.State.SELECT_BOX) {
                    if (slot < this.toolboxes.size()) {
                        AllGuiTextures.TOOLBELT_SLOT.render(graphics, 0, 0);
                        ToolboxBlockEntity toolboxBlockEntity = (ToolboxBlockEntity) this.toolboxes.get(slot);
                        GuiGameElement.of(AllBlocks.TOOLBOXES.get(toolboxBlockEntity.getColor()).asStack()).<RenderElement>at(3.0F, 3.0F).render(graphics);
                        if (slot == (this.scrollMode ? this.scrollSlot : this.hoveredSlot)) {
                            AllGuiTextures.TOOLBELT_SLOT_HIGHLIGHT.render(graphics, -1, -1);
                            tip = toolboxBlockEntity.getDisplayName();
                        }
                    } else {
                        AllGuiTextures.TOOLBELT_EMPTY_SLOT.render(graphics, 0, 0);
                    }
                }
                ms.popPose();
            }
            if (renderCenterSlot) {
                ms.pushPose();
                AllGuiTextures.TOOLBELT_SLOT.render(graphics, -12, -12);
                (this.scrollMode ? AllIcons.I_REFRESH : AllIcons.I_FLIP).render(graphics, -9, -9);
                if (!this.scrollMode && -5 == this.hoveredSlot) {
                    AllGuiTextures.TOOLBELT_SLOT_HIGHLIGHT.render(graphics, -13, -13);
                    tip = Lang.translateDirect("toolbox.unequip", this.f_96541_.player.m_21205_().getHoverName()).withStyle(ChatFormatting.GOLD);
                }
                ms.popPose();
            }
        }
        ms.popPose();
        if (tip != null) {
            int i1 = (int) (fade * 255.0F);
            if (i1 > 255) {
                i1 = 255;
            }
            if (i1 > 8) {
                ms.pushPose();
                ms.translate((float) (this.f_96543_ / 2), (float) (this.f_96544_ - 68), 0.0F);
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                int k1 = 16777215;
                int k = i1 << 24 & 0xFF000000;
                int l = this.f_96547_.width(tip);
                graphics.drawString(this.f_96547_, tip, Math.round((float) (-l) / 2.0F), -4, k1 | k, false);
                RenderSystem.disableBlend();
                ms.popPose();
            }
        }
    }

    @Override
    public void renderBackground(GuiGraphics graphics) {
        int a = (int) (80.0F * Math.min(1.0F, ((float) this.ticksOpen + AnimationTickHolder.getPartialTicks()) / 20.0F)) << 24;
        graphics.fillGradient(0, 0, this.f_96543_, this.f_96544_, 1052688 | a, 1052688 | a);
    }

    @Override
    public void tick() {
        this.ticksOpen++;
        super.tick();
    }

    @Override
    public void removed() {
        super.m_7861_();
        int selected = this.scrollMode ? this.scrollSlot : this.hoveredSlot;
        if (selected == -7) {
            if (this.state != RadialToolboxMenu.State.DETACH) {
                if (this.state == RadialToolboxMenu.State.SELECT_BOX) {
                    this.toolboxes.forEach(be -> AllPackets.getChannel().sendToServer(new ToolboxDisposeAllPacket(be.m_58899_())));
                } else {
                    AllPackets.getChannel().sendToServer(new ToolboxDisposeAllPacket(this.selectedBox.m_58899_()));
                }
            }
        } else if (this.state != RadialToolboxMenu.State.SELECT_BOX) {
            if (this.state == RadialToolboxMenu.State.DETACH) {
                if (selected == -5) {
                    AllPackets.getChannel().sendToServer(new ToolboxEquipPacket(null, selected, this.f_96541_.player.m_150109_().selected));
                }
            } else {
                if (selected == -5) {
                    AllPackets.getChannel().sendToServer(new ToolboxEquipPacket(this.selectedBox.m_58899_(), selected, this.f_96541_.player.m_150109_().selected));
                }
                if (selected >= 0) {
                    ToolboxInventory inv = this.selectedBox.inventory;
                    ItemStack stackInSlot = (ItemStack) inv.filters.get(selected);
                    if (!stackInSlot.isEmpty()) {
                        if (!inv.getStackInSlot(selected * 4).isEmpty()) {
                            AllPackets.getChannel().sendToServer(new ToolboxEquipPacket(this.selectedBox.m_58899_(), selected, this.f_96541_.player.m_150109_().selected));
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        Window window = this.getMinecraft().getWindow();
        double hoveredX = mouseX - (double) (window.getGuiScaledWidth() / 2);
        double hoveredY = mouseY - (double) (window.getGuiScaledHeight() / 2);
        double distance = hoveredX * hoveredX + hoveredY * hoveredY;
        if (!(distance <= 150.0)) {
            return super.m_6050_(mouseX, mouseY, delta);
        } else {
            this.scrollMode = true;
            this.scrollSlot = ((int) ((double) this.scrollSlot - delta) + 8) % 8;
            for (int i = 0; i < 10; i++) {
                if (this.state == RadialToolboxMenu.State.SELECT_ITEM || this.state == RadialToolboxMenu.State.SELECT_ITEM_UNEQUIP) {
                    ToolboxInventory inv = this.selectedBox.inventory;
                    ItemStack stackInSlot = (ItemStack) inv.filters.get(this.scrollSlot);
                    if (!stackInSlot.isEmpty() && !inv.getStackInSlot(this.scrollSlot * 4).isEmpty()) {
                        break;
                    }
                }
                if (this.state == RadialToolboxMenu.State.SELECT_BOX && this.scrollSlot < this.toolboxes.size() || this.state == RadialToolboxMenu.State.DETACH) {
                    break;
                }
                this.scrollSlot = this.scrollSlot - Mth.sign(delta);
                this.scrollSlot = (this.scrollSlot + 8) % 8;
            }
            return true;
        }
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        int selected = this.scrollMode ? this.scrollSlot : this.hoveredSlot;
        if (button == 0) {
            if (selected == -7) {
                this.m_7379_();
                ToolboxHandlerClient.COOLDOWN = 2;
                return true;
            }
            if (this.state == RadialToolboxMenu.State.SELECT_BOX && selected >= 0 && selected < this.toolboxes.size()) {
                this.state = RadialToolboxMenu.State.SELECT_ITEM;
                this.selectedBox = (ToolboxBlockEntity) this.toolboxes.get(selected);
                return true;
            }
            if ((this.state == RadialToolboxMenu.State.DETACH || this.state == RadialToolboxMenu.State.SELECT_ITEM || this.state == RadialToolboxMenu.State.SELECT_ITEM_UNEQUIP) && (selected == -5 || selected >= 0)) {
                this.m_7379_();
                ToolboxHandlerClient.COOLDOWN = 2;
                return true;
            }
        }
        if (button == 1) {
            if (this.state == RadialToolboxMenu.State.SELECT_ITEM && this.toolboxes.size() > 1) {
                this.state = RadialToolboxMenu.State.SELECT_BOX;
                return true;
            }
            if (this.state == RadialToolboxMenu.State.SELECT_ITEM_UNEQUIP && selected == -5) {
                if (this.toolboxes.size() > 1) {
                    AllPackets.getChannel().sendToServer(new ToolboxEquipPacket(this.selectedBox.m_58899_(), selected, this.f_96541_.player.m_150109_().selected));
                    this.state = RadialToolboxMenu.State.SELECT_BOX;
                    return true;
                }
                this.m_7379_();
                ToolboxHandlerClient.COOLDOWN = 2;
                return true;
            }
        }
        return super.mouseClicked(x, y, button);
    }

    @Override
    public boolean keyPressed(int code, int scanCode, int modifiers) {
        KeyMapping[] hotbarBinds = this.f_96541_.options.keyHotbarSlots;
        for (int i = 0; i < hotbarBinds.length && i < 8; i++) {
            if (hotbarBinds[i].matches(code, scanCode)) {
                if (this.state == RadialToolboxMenu.State.SELECT_ITEM || this.state == RadialToolboxMenu.State.SELECT_ITEM_UNEQUIP) {
                    ToolboxInventory inv = this.selectedBox.inventory;
                    ItemStack stackInSlot = (ItemStack) inv.filters.get(i);
                    if (stackInSlot.isEmpty() || inv.getStackInSlot(i * 4).isEmpty()) {
                        return false;
                    }
                }
                if (this.state == RadialToolboxMenu.State.SELECT_BOX && i >= this.toolboxes.size()) {
                    return false;
                }
                this.scrollMode = true;
                this.scrollSlot = i;
                this.mouseClicked(0.0, 0.0, 0);
                return true;
            }
        }
        return super.keyPressed(code, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int code, int scanCode, int modifiers) {
        InputConstants.Key mouseKey = InputConstants.getKey(code, scanCode);
        if (AllKeys.TOOLBELT.getKeybind().isActiveAndMatches(mouseKey)) {
            this.m_7379_();
            return true;
        } else {
            return super.m_7920_(code, scanCode, modifiers);
        }
    }

    public static enum State {

        SELECT_BOX, SELECT_ITEM, SELECT_ITEM_UNEQUIP, DETACH
    }
}