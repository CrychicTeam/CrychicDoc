package noppes.npcs.client.gui.global;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.containers.ContainerNpcQuestReward;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;
import noppes.npcs.shared.client.gui.listeners.ITextfieldListener;

public class GuiNpcQuestReward extends GuiContainerNPCInterface<ContainerNpcQuestReward> implements ITextfieldListener {

    private Quest quest = NoppesUtilServer.getEditingQuest(this.player);

    private ResourceLocation resource = this.getResource("questreward.png");

    public GuiNpcQuestReward(ContainerNpcQuestReward container, Inventory inv, Component titleIn) {
        super(NoppesUtil.getLastNpc(), container, inv, titleIn);
    }

    @Override
    public void init() {
        super.m_7856_();
        this.addLabel(new GuiLabel(0, "quest.randomitem", this.guiLeft + 4, this.guiTop + 4));
        this.addButton(new GuiButtonNop(this, 0, this.guiLeft + 4, this.guiTop + 14, 60, 20, new String[] { "gui.no", "gui.yes" }, this.quest.randomReward ? 1 : 0));
        this.addButton(new GuiButtonNop(this, 5, this.guiLeft, this.guiTop + this.f_97727_, 98, 20, "gui.back"));
        this.addLabel(new GuiLabel(1, "quest.exp", this.guiLeft + 4, this.guiTop + 45));
        this.addTextField(new GuiTextFieldNop(0, this, this.guiLeft + 4, this.guiTop + 55, 60, 20, this.quest.rewardExp + ""));
        this.getTextField(0).numbersOnly = true;
        this.getTextField(0).setMinMaxDefault(0, 99999, 0);
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        int id = guibutton.id;
        if (id == 5) {
            NoppesUtil.openGUI(this.player, GuiNPCManageQuest.Instance);
        }
        if (id == 0) {
            this.quest.randomReward = guibutton.getValue() == 1;
        }
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
        super.m_7286_(graphics, partialTicks, x, y);
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, this.resource);
        int l = (this.f_96543_ - this.f_97726_) / 2;
        int i1 = (this.f_96544_ - this.f_97727_) / 2;
        graphics.blit(this.resource, l, i1, 0, 0, this.f_97726_, this.f_97727_);
    }

    @Override
    public void save() {
    }

    @Override
    public void unFocused(GuiTextFieldNop textfield) {
        this.quest.rewardExp = textfield.getInteger();
    }
}