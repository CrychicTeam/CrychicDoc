package noppes.npcs.client.gui.questtypes;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.global.GuiNPCManageQuest;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.containers.ContainerNpcQuestTypeItem;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.quests.QuestItem;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiButtonYesNo;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;
import noppes.npcs.shared.client.gui.listeners.ITextfieldListener;

public class GuiNpcQuestTypeItem extends GuiContainerNPCInterface<ContainerNpcQuestTypeItem> implements ITextfieldListener {

    private Quest quest = NoppesUtilServer.getEditingQuest(this.player);

    private static final ResourceLocation field_110422_t = new ResourceLocation("customnpcs", "textures/gui/followersetup.png");

    public GuiNpcQuestTypeItem(ContainerNpcQuestTypeItem container, Inventory inv, Component titleIn) {
        super(NoppesUtil.getLastNpc(), container, inv, titleIn);
        this.title = "";
        this.f_97727_ = 202;
        this.closeOnEsc = false;
    }

    @Override
    public void init() {
        super.m_7856_();
        this.addLabel(new GuiLabel(0, "quest.takeitems", this.guiLeft + 4, this.guiTop + 8));
        this.addButton(new GuiButtonNop(this, 0, this.guiLeft + 90, this.guiTop + 3, 60, 20, new String[] { "gui.yes", "gui.no" }, ((QuestItem) this.quest.questInterface).leaveItems ? 1 : 0));
        this.addLabel(new GuiLabel(1, "gui.ignoreDamage", this.guiLeft + 4, this.guiTop + 29));
        this.addButton(new GuiButtonYesNo(this, 1, this.guiLeft + 90, this.guiTop + 24, 50, 20, ((QuestItem) this.quest.questInterface).ignoreDamage));
        this.addLabel(new GuiLabel(2, "gui.ignoreNBT", this.guiLeft + 62, this.guiTop + 51));
        this.addButton(new GuiButtonYesNo(this, 2, this.guiLeft + 120, this.guiTop + 46, 50, 20, ((QuestItem) this.quest.questInterface).ignoreNBT));
        this.addButton(new GuiButtonNop(this, 5, this.guiLeft, this.guiTop + this.f_97727_, 98, 20, "gui.back"));
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        if (guibutton.id == 0) {
            ((QuestItem) this.quest.questInterface).leaveItems = guibutton.getValue() == 1;
        }
        if (guibutton.id == 1) {
            ((QuestItem) this.quest.questInterface).ignoreDamage = ((GuiButtonYesNo) guibutton).getBoolean();
        }
        if (guibutton.id == 2) {
            ((QuestItem) this.quest.questInterface).ignoreNBT = ((GuiButtonYesNo) guibutton).getBoolean();
        }
        if (guibutton.id == 5) {
            NoppesUtil.openGUI(this.player, GuiNPCManageQuest.Instance);
        }
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
        super.m_280273_(graphics);
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, field_110422_t);
        int l = (this.f_96543_ - this.f_97726_) / 2;
        int i1 = (this.f_96544_ - this.f_97727_) / 2;
        graphics.blit(field_110422_t, l, i1, 0, 0, this.f_97726_, this.f_97727_);
    }

    @Override
    public void save() {
    }

    @Override
    public void unFocused(GuiTextFieldNop textfield) {
        this.quest.rewardExp = textfield.getInteger();
    }
}