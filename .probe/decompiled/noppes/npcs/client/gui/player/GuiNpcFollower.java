package noppes.npcs.client.gui.player;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.client.CustomNpcResourceListener;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.containers.ContainerNPCFollower;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketFollowerExtend;
import noppes.npcs.packets.server.SPacketFollowerState;
import noppes.npcs.packets.server.SPacketNpcRoleGet;
import noppes.npcs.roles.RoleFollower;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.listeners.IGuiData;

public class GuiNpcFollower extends GuiContainerNPCInterface<ContainerNPCFollower> implements IGuiData {

    private final ResourceLocation resource = new ResourceLocation("customnpcs", "textures/gui/follower.png");

    private RoleFollower role = (RoleFollower) this.npc.role;

    public GuiNpcFollower(ContainerNPCFollower container, Inventory inv, Component titleIn) {
        super(NoppesUtil.getLastNpc(), container, inv, titleIn);
        Packets.sendServer(new SPacketNpcRoleGet());
    }

    @Override
    public void init() {
        super.m_7856_();
        this.f_169369_.clear();
        this.addButton(new GuiButtonNop(this, 4, this.guiLeft + 100, this.guiTop + 110, 50, 20, new String[] { I18n.get("follower.waiting"), I18n.get("follower.following") }, this.role.isFollowing ? 1 : 0));
        if (!this.role.infiniteDays) {
            this.addButton(new GuiButtonNop(this, 5, this.guiLeft + 8, this.guiTop + 30, 50, 20, I18n.get("follower.hire")));
        }
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        int id = guibutton.id;
        if (id == 4) {
            Packets.sendServer(new SPacketFollowerState());
        }
        if (id == 5) {
            Packets.sendServer(new SPacketFollowerExtend());
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int x, int y) {
        graphics.drawString(this.f_96547_, I18n.get("follower.health") + ": " + this.npc.m_21223_() + "/" + this.npc.m_21233_(), 62, 70, CustomNpcResourceListener.DefaultTextColor);
        if (!this.role.infiniteDays) {
            if (this.role.getDays() <= 1) {
                graphics.drawString(this.f_96547_, I18n.get("follower.daysleft") + ": " + I18n.get("follower.lastday"), 62, 94, CustomNpcResourceListener.DefaultTextColor);
            } else {
                graphics.drawString(this.f_96547_, I18n.get("follower.daysleft") + ": " + (this.role.getDays() - 1), 62, 94, CustomNpcResourceListener.DefaultTextColor);
            }
        }
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, this.resource);
        int l = this.guiLeft;
        int i1 = this.guiTop;
        graphics.blit(this.resource, l, i1, 0, 0, this.f_97726_, this.f_97727_);
        int index = 0;
        if (!this.role.infiniteDays) {
            for (int slot = 0; slot < this.role.inventory.items.size(); slot++) {
                ItemStack itemstack = this.role.inventory.items.get(slot);
                if (!NoppesUtilServer.IsItemStackNull(itemstack)) {
                    int days = 1;
                    if (this.role.rates.containsKey(slot)) {
                        days = (Integer) this.role.rates.get(slot);
                    }
                    int yOffset = index * 20;
                    int i = this.guiLeft + 68;
                    int j = this.guiTop + yOffset + 4;
                    graphics.renderItem(itemstack, x + 11, y);
                    graphics.renderItemDecorations(this.f_96547_, itemstack, x + 11, y);
                    String daysS = days + " " + (days == 1 ? I18n.get("follower.day") : I18n.get("follower.days"));
                    graphics.drawString(this.f_96547_, " = " + daysS, i + 27, j + 4, CustomNpcResourceListener.DefaultTextColor);
                    if (this.m_6774_(i - this.guiLeft + 11, j - this.guiTop, 16, 16, (double) this.mouseX, (double) this.mouseY)) {
                        graphics.renderTooltip(this.f_96547_, itemstack, this.mouseX, this.mouseY);
                    }
                    index++;
                }
            }
        }
        this.drawNpc(graphics, 33, 131);
    }

    @Override
    public void save() {
    }

    @Override
    public void setGuiData(CompoundTag compound) {
        this.npc.role.load(compound);
        this.init();
    }
}