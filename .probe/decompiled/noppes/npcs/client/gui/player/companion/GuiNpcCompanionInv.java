package noppes.npcs.client.gui.player.companion;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import noppes.npcs.CustomNpcs;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.constants.EnumCompanionTalent;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.containers.ContainerNPCCompanion;
import noppes.npcs.roles.RoleCompanion;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;

public class GuiNpcCompanionInv extends GuiContainerNPCInterface<ContainerNPCCompanion> {

    private final ResourceLocation resource = new ResourceLocation("customnpcs", "textures/gui/companioninv.png");

    private final ResourceLocation slot = new ResourceLocation("customnpcs", "textures/gui/slot.png");

    private RoleCompanion role = (RoleCompanion) this.npc.role;

    public GuiNpcCompanionInv(ContainerNPCCompanion container, Inventory inv, Component titleIn) {
        super(NoppesUtil.getLastNpc(), container, inv, titleIn);
        this.f_97726_ = 171;
        this.f_97727_ = 166;
    }

    @Override
    public void init() {
        super.m_7856_();
        GuiNpcCompanionStats.addTopMenu(this.role, this, 3);
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        int id = guibutton.id;
        if (id == 1) {
            CustomNpcs.proxy.openGui(this.npc, EnumGuiType.Companion);
        }
        if (id == 2) {
            CustomNpcs.proxy.openGui(this.npc, EnumGuiType.CompanionTalent);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int par1, int limbSwingAmount) {
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float f, int xMouse, int yMouse) {
        super.m_280273_(graphics);
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, this.resource);
        graphics.blit(this.resource, this.guiLeft, this.guiTop, 0, 0, this.f_97726_, this.f_97727_);
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, this.slot);
        if (this.role.getTalentLevel(EnumCompanionTalent.ARMOR) > 0) {
            for (int i = 0; i < 4; i++) {
                graphics.blit(this.resource, this.guiLeft + 5, this.guiTop + 7 + i * 18, 0, 0, 18, 18);
            }
        }
        if (this.role.getTalentLevel(EnumCompanionTalent.SWORD) > 0) {
            graphics.blit(this.resource, this.guiLeft + 78, this.guiTop + 16, 0, this.npc.inventory.weapons.get(0) == null ? 18 : 0, 18, 18);
        }
        if (this.role.getTalentLevel(EnumCompanionTalent.RANGED) > 0) {
        }
        if (this.role.talents.containsKey(EnumCompanionTalent.INVENTORY)) {
            int size = (this.role.getTalentLevel(EnumCompanionTalent.INVENTORY) + 1) * 2;
            for (int i = 0; i < size; i++) {
                graphics.blit(this.resource, this.guiLeft + 113 + i % 3 * 18, this.guiTop + 7 + i / 3 * 18, 0, 0, 18, 18);
            }
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.m_88315_(graphics, mouseX, mouseY, partialTicks);
        super.drawNpc(graphics, 52, 70);
    }

    @Override
    public void save() {
    }
}