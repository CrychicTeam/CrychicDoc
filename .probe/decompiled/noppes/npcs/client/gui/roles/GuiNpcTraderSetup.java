package noppes.npcs.client.gui.roles;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import noppes.npcs.client.CustomNpcResourceListener;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface2;
import noppes.npcs.containers.ContainerNPCTraderSetup;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketNpcMarketSet;
import noppes.npcs.packets.server.SPacketNpcRoleSave;
import noppes.npcs.roles.RoleTrader;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiButtonYesNo;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;
import noppes.npcs.shared.client.gui.listeners.ITextfieldListener;

public class GuiNpcTraderSetup extends GuiContainerNPCInterface2<ContainerNPCTraderSetup> implements ITextfieldListener {

    private final ResourceLocation slot = new ResourceLocation("customnpcs", "textures/gui/slot.png");

    private RoleTrader role;

    public GuiNpcTraderSetup(ContainerNPCTraderSetup container, Inventory inv, Component titleIn) {
        super(NoppesUtil.getLastNpc(), container, inv, titleIn);
        this.f_97727_ = 220;
        this.menuYOffset = 10;
        this.role = container.role;
    }

    @Override
    public void init() {
        super.init();
        this.f_169369_.clear();
        this.setBackground("tradersetup.png");
        this.addLabel(new GuiLabel(0, "role.marketname", this.guiLeft + 214, this.guiTop + 150));
        this.addTextField(new GuiTextFieldNop(0, this, this.guiLeft + 214, this.guiTop + 160, 180, 20, this.role.marketName));
        this.addLabel(new GuiLabel(1, "gui.ignoreDamage", this.guiLeft + 260, this.guiTop + 29));
        this.addButton(new GuiButtonYesNo(this, 1, this.guiLeft + 340, this.guiTop + 24, this.role.ignoreDamage));
        this.addLabel(new GuiLabel(2, "gui.ignoreNBT", this.guiLeft + 260, this.guiTop + 51));
        this.addButton(new GuiButtonYesNo(this, 2, this.guiLeft + 340, this.guiTop + 46, this.role.ignoreNBT));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.guiTop += 10;
        super.m_88315_(graphics, mouseX, mouseY, partialTicks);
        this.guiTop -= 10;
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        if (guibutton.id == 1) {
            this.role.ignoreDamage = ((GuiButtonYesNo) guibutton).getBoolean();
        }
        if (guibutton.id == 2) {
            this.role.ignoreNBT = ((GuiButtonYesNo) guibutton).getBoolean();
        }
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int xMouse, int yMouse) {
        super.renderBg(graphics, partialTicks, xMouse, yMouse);
        for (int slot = 0; slot < 18; slot++) {
            int x = this.guiLeft + slot % 3 * 94 + 7;
            int y = this.guiTop + slot / 3 * 22 + 4;
            RenderSystem.setShader(GameRenderer::m_172817_);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, this.slot);
            graphics.blit(this.slot, x - 1, y, 0, 0, 18, 18);
            graphics.blit(this.slot, x + 17, y, 0, 0, 18, 18);
            graphics.drawString(this.f_96547_, "=", x + 36, y + 5, CustomNpcResourceListener.DefaultTextColor);
            RenderSystem.setShader(GameRenderer::m_172817_);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, this.slot);
            graphics.blit(this.slot, x + 42, y, 0, 0, 18, 18);
        }
    }

    @Override
    public void save() {
        Packets.sendServer(new SPacketNpcMarketSet(this.role.marketName, true));
        Packets.sendServer(new SPacketNpcRoleSave(this.role.save(new CompoundTag())));
    }

    @Override
    public void unFocused(GuiTextFieldNop guiNpcTextField) {
        String name = guiNpcTextField.m_94155_();
        if (!name.equalsIgnoreCase(this.role.marketName)) {
            this.role.marketName = name;
            Packets.sendServer(new SPacketNpcMarketSet(this.role.marketName, false));
        }
    }
}