package noppes.npcs.client.gui.player;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.client.CustomNpcResourceListener;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.containers.ContainerNPCFollowerHire;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketFollowerHire;
import noppes.npcs.roles.RoleFollower;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;

public class GuiNpcFollowerHire extends GuiContainerNPCInterface<ContainerNPCFollowerHire> {

    private final ResourceLocation resource = new ResourceLocation("customnpcs", "textures/gui/followerhire.png");

    private ContainerNPCFollowerHire container;

    private RoleFollower role;

    public GuiNpcFollowerHire(ContainerNPCFollowerHire container, Inventory inv, Component titleIn) {
        super(NoppesUtil.getLastNpc(), container, inv, titleIn);
        this.container = container;
        this.role = (RoleFollower) this.npc.role;
    }

    @Override
    public void init() {
        super.m_7856_();
        this.addButton(new GuiButtonNop(this, 5, this.guiLeft + 26, this.guiTop + 60, 50, 20, I18n.get("follower.hire")));
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        if (guibutton.id == 5) {
            Packets.sendServer(new SPacketFollowerHire());
            this.close();
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics0, int int1, int int2) {
        super.m_280003_(guiGraphics0, int1, int2);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float f, int i, int j) {
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, this.resource);
        int l = (this.f_96543_ - this.f_97726_) / 2;
        int i1 = (this.f_96544_ - this.f_97727_) / 2;
        graphics.blit(this.resource, l, i1, 0, 0, this.f_97726_, this.f_97727_);
        int index = 0;
        for (int slot = 0; slot < this.role.inventory.items.size(); slot++) {
            ItemStack itemstack = this.role.inventory.items.get(slot);
            if (!NoppesUtilServer.IsItemStackNull(itemstack)) {
                int days = 1;
                if (this.role.rates.containsKey(slot)) {
                    days = (Integer) this.role.rates.get(slot);
                }
                int yOffset = index * 26;
                int x = this.guiLeft + 78;
                int y = this.guiTop + yOffset + 10;
                graphics.renderItem(itemstack, x + 11, y);
                graphics.renderItemDecorations(this.f_96547_, itemstack, x + 11, y);
                String daysS = days + " " + (days == 1 ? I18n.get("follower.day") : I18n.get("follower.days"));
                graphics.drawString(this.f_96547_, " = " + daysS, x + 27, y + 4, CustomNpcResourceListener.DefaultTextColor);
                if (this.m_6774_(x - this.guiLeft + 11, y - this.guiTop, 16, 16, (double) this.mouseX, (double) this.mouseY)) {
                    graphics.renderTooltip(this.f_96547_, itemstack, this.mouseX, this.mouseY);
                }
                index++;
            }
        }
    }

    @Override
    public void save() {
    }
}