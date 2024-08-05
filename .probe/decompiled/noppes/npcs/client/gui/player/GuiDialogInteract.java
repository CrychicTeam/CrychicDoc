package noppes.npcs.client.gui.player;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import noppes.npcs.client.ClientProxy;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.TextBlockClient;
import noppes.npcs.client.controllers.MusicController;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.controllers.data.DialogOption;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.mixin.MouseHelperMixin;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketDialogSelected;
import noppes.npcs.packets.server.SPacketQuestCompletionCheckAll;
import noppes.npcs.shared.client.gui.listeners.IGuiClose;
import noppes.npcs.shared.client.util.NoppesStringUtils;

public class GuiDialogInteract extends GuiNPCInterface implements IGuiClose {

    private Dialog dialog;

    private int selected = 0;

    private List<TextBlockClient> lines = new ArrayList();

    private List<Integer> options = new ArrayList();

    private int rowStart = 0;

    private int rowTotal = 0;

    private int dialogHeight = 180;

    private ResourceLocation wheel;

    private ResourceLocation[] wheelparts;

    private ResourceLocation indicator;

    private boolean isGrabbed = false;

    private double selectedX = 0.0;

    private double selectedY = 0.0;

    public GuiDialogInteract(EntityNPCInterface npc, Dialog dialog) {
        super(npc);
        this.dialog = dialog;
        this.appendDialog(dialog);
        this.imageHeight = 238;
        this.wheel = this.getResource("wheel.png");
        this.indicator = this.getResource("indicator.png");
        this.wheelparts = new ResourceLocation[] { this.getResource("wheel1.png"), this.getResource("wheel2.png"), this.getResource("wheel3.png"), this.getResource("wheel4.png"), this.getResource("wheel5.png"), this.getResource("wheel6.png") };
    }

    @Override
    public void init() {
        super.m_7856_();
        this.isGrabbed = false;
        this.grabMouse(this.dialog.showWheel);
        this.guiTop = this.f_96544_ - this.imageHeight;
        this.calculateRowHeight();
    }

    public void grabMouse(boolean grab) {
        if (grab && !this.isGrabbed) {
            MouseHelperMixin mouse = (MouseHelperMixin) Minecraft.getInstance().mouseHandler;
            mouse.setGrabbed(false);
            double xpos = 0.0;
            double ypos = 0.0;
            mouse.setX(xpos);
            mouse.setY(ypos);
            InputConstants.grabOrReleaseMouse(this.f_96541_.getWindow().getWindow(), 212995, xpos, ypos);
            this.isGrabbed = true;
        } else if (!grab && this.isGrabbed) {
            Minecraft.getInstance().mouseHandler.releaseMouse();
            this.isGrabbed = false;
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.fillGradient(0, 0, this.f_96543_, this.f_96544_, -587202560, -587202560);
        if (!this.dialog.hideNPC) {
            int l = -70;
            int i1 = this.imageHeight;
            this.drawNpc(graphics, this.npc, l, i1, 1.4F, 0);
        }
        super.m_88315_(graphics, mouseX, mouseY, partialTicks);
        graphics.pose().pushPose();
        graphics.pose().translate(0.0F, 0.5F, 100.065F);
        int count = 0;
        for (TextBlockClient block : new ArrayList(this.lines)) {
            int size = ClientProxy.Font.width(block.getName() + ": ");
            this.drawString(graphics.pose(), block.getName() + ": ", -4 - size, block.color, count);
            for (Component line : block.lines) {
                this.drawString(graphics.pose(), line.getString(), 0, block.color, count);
                count++;
            }
            count++;
        }
        if (!this.options.isEmpty()) {
            if (!this.dialog.showWheel) {
                this.drawLinedOptions(graphics, mouseY);
            } else {
                this.drawWheel(graphics);
            }
        }
        graphics.pose().popPose();
    }

    private void drawWheel(GuiGraphics graphics) {
        int yoffset = this.guiTop + this.dialogHeight + 14;
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, this.wheel);
        graphics.blit(this.wheel, this.f_96543_ / 2 - 31, yoffset, 0, 0, 63, 40);
        this.selectedX = this.f_96541_.mouseHandler.xpos() * 0.5;
        this.selectedY = -this.f_96541_.mouseHandler.ypos() * 0.5;
        int limit = 80;
        if (this.selectedX > (double) limit) {
            this.selectedX = (double) limit;
        }
        if (this.selectedX < (double) (-limit)) {
            this.selectedX = (double) (-limit);
        }
        if (this.selectedY > (double) limit) {
            this.selectedY = (double) limit;
        }
        if (this.selectedY < (double) (-limit)) {
            this.selectedY = (double) (-limit);
        }
        this.selected = 1;
        if (this.selectedY < -20.0) {
            this.selected++;
        }
        if (this.selectedY > 54.0) {
            this.selected--;
        }
        if (this.selectedX < 0.0) {
            this.selected += 3;
        }
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, this.wheelparts[this.selected]);
        graphics.blit(this.wheelparts[this.selected], this.f_96543_ / 2 - 31, yoffset, 0, 0, 85, 55);
        for (int slot : this.dialog.options.keySet()) {
            DialogOption option = (DialogOption) this.dialog.options.get(slot);
            if (option != null && option.optionType != 2 && (!option.hasDialog() || option.getDialog().availability.isAvailable(this.player))) {
                int color = option.optionColor;
                if (slot == this.selected) {
                    color = 8622040;
                }
                int height = ClientProxy.Font.height(option.title);
                if (slot == 0) {
                    graphics.drawString(this.f_96547_, option.title, this.f_96543_ / 2 + 13, yoffset - height, color);
                }
                if (slot == 1) {
                    graphics.drawString(this.f_96547_, option.title, this.f_96543_ / 2 + 33, yoffset - height / 2 + 14, color);
                }
                if (slot == 2) {
                    graphics.drawString(this.f_96547_, option.title, this.f_96543_ / 2 + 27, yoffset + 27, color);
                }
                if (slot == 3) {
                    graphics.drawString(this.f_96547_, option.title, this.f_96543_ / 2 - 13 - ClientProxy.Font.width(option.title), yoffset - height, color);
                }
                if (slot == 4) {
                    graphics.drawString(this.f_96547_, option.title, this.f_96543_ / 2 - 33 - ClientProxy.Font.width(option.title), yoffset - height / 2 + 14, color);
                }
                if (slot == 5) {
                    graphics.drawString(this.f_96547_, option.title, this.f_96543_ / 2 - 27 - ClientProxy.Font.width(option.title), yoffset + 27, color);
                }
            }
        }
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, this.indicator);
        graphics.blit(this.indicator, this.f_96543_ / 2 + (int) this.selectedX / 4 - 2, yoffset + 16 - (int) this.selectedY / 6, 0, 0, 8, 8);
    }

    private void drawLinedOptions(GuiGraphics graphics, int j) {
        graphics.hLine(this.guiLeft - 60, this.guiLeft + this.imageWidth + 120, this.guiTop + this.dialogHeight - ClientProxy.Font.height(null) / 3, -1);
        int offset = this.dialogHeight;
        if (j >= this.guiTop + offset) {
            int selected = (j - (this.guiTop + offset)) / ClientProxy.Font.height(null);
            if (selected < this.options.size()) {
                this.selected = selected;
            }
        }
        if (this.selected >= this.options.size()) {
            this.selected = 0;
        }
        if (this.selected < 0) {
            this.selected = 0;
        }
        for (int k = 0; k < this.options.size(); k++) {
            int id = (Integer) this.options.get(k);
            DialogOption option = (DialogOption) this.dialog.options.get(id);
            int y = this.guiTop + offset + k * ClientProxy.Font.height(null);
            if (this.selected == k) {
                graphics.drawString(this.f_96547_, ">", this.guiLeft - 60, y, 14737632);
            }
            graphics.drawString(this.f_96547_, NoppesStringUtils.formatText(option.title, this.player, this.npc), this.guiLeft - 30, y, option.optionColor);
        }
    }

    private void drawString(PoseStack matrixStack, String text, int left, int color, int count) {
        int height = count - this.rowStart;
        ClientProxy.Font.draw(matrixStack, text, this.guiLeft + left, this.guiTop + height * ClientProxy.Font.height(null), color);
    }

    private int getSelected() {
        if (this.selected <= 0) {
            return 0;
        } else {
            return this.selected < this.options.size() ? this.selected : this.options.size() - 1;
        }
    }

    @Override
    public boolean keyPressed(int key, int p_keyPressed_2_, int p_keyPressed_3_) {
        if (key == this.f_96541_.options.keyUp.getKey().getValue() || key == InputConstants.getKey("key.keyboard.up").getValue()) {
            this.selected--;
        }
        if (key == this.f_96541_.options.keyDown.getKey().getValue() || key == InputConstants.getKey("key.keyboard.down").getValue()) {
            this.selected++;
        }
        if (key == InputConstants.getKey("key.keyboard.enter").getValue() || key == InputConstants.getKey("key.keyboard.keypad.enter").getValue()) {
            this.handleDialogSelection();
        }
        if (this.closeOnEsc && (key == InputConstants.getKey("key.keyboard.escape").getValue() || this.isInventoryKey(key))) {
            Packets.sendServer(new SPacketDialogSelected(this.dialog.id, -1));
            this.closed();
            this.m_7379_();
        }
        return true;
    }

    @Override
    public boolean mouseClicked(double i, double j, int k) {
        if ((this.selected == -1 && this.options.isEmpty() || this.selected >= 0) && k == 0) {
            this.handleDialogSelection();
        }
        return true;
    }

    private void handleDialogSelection() {
        int optionId = -1;
        if (this.dialog.showWheel) {
            optionId = this.selected;
        } else if (!this.options.isEmpty()) {
            optionId = (Integer) this.options.get(this.selected);
        }
        Packets.sendServer(new SPacketDialogSelected(this.dialog.id, optionId));
        if (this.dialog != null && this.dialog.hasOtherOptions() && !this.options.isEmpty()) {
            DialogOption option = (DialogOption) this.dialog.options.get(optionId);
            if (option != null && option.optionType == 1) {
                this.lines.add(new TextBlockClient(this.player.m_5446_().getString(), option.title, 280, option.optionColor, this.player, this.npc));
                this.calculateRowHeight();
                NoppesUtil.clickSound();
            } else {
                if (this.closeOnEsc) {
                    this.closed();
                    this.m_7379_();
                }
            }
        } else {
            if (this.closeOnEsc) {
                this.closed();
                this.m_7379_();
            }
        }
    }

    private void closed() {
        this.grabMouse(false);
        Packets.sendServer(new SPacketQuestCompletionCheckAll());
    }

    public void appendDialog(Dialog dialog) {
        this.closeOnEsc = !dialog.disableEsc;
        this.dialog = dialog;
        this.options = new ArrayList();
        if (dialog.sound != null && !dialog.sound.isEmpty()) {
            MusicController.Instance.stopMusic();
            BlockPos pos = this.npc.m_20183_();
            MusicController.Instance.playSound(SoundSource.VOICE, dialog.sound, pos, 1.0F, 1.0F);
        }
        this.lines.add(new TextBlockClient(this.npc.createCommandSourceStack(), dialog.text, 280, 14737632, this.player, this.npc));
        for (int slot : dialog.options.keySet()) {
            DialogOption option = (DialogOption) dialog.options.get(slot);
            if (option != null && option.isAvailable(this.player)) {
                this.options.add(slot);
            }
        }
        this.calculateRowHeight();
        this.grabMouse(dialog.showWheel);
    }

    private void calculateRowHeight() {
        if (this.dialog.showWheel) {
            this.dialogHeight = this.imageHeight - 58;
        } else {
            this.dialogHeight = this.imageHeight - 3 * ClientProxy.Font.height(null) - 4;
            if (this.dialog.options.size() > 3) {
                this.dialogHeight = this.dialogHeight - (this.dialog.options.size() - 3) * ClientProxy.Font.height(null);
            }
        }
        this.rowTotal = 0;
        for (TextBlockClient block : this.lines) {
            this.rowTotal = this.rowTotal + block.lines.size() + 1;
        }
        int max = this.dialogHeight / ClientProxy.Font.height(null);
        this.rowStart = this.rowTotal - max;
        if (this.rowStart < 0) {
            this.rowStart = 0;
        }
    }

    @Override
    public void setClose(CompoundTag data) {
        this.grabMouse(false);
    }

    @Override
    public void save() {
    }
}