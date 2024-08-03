package net.mehvahdjukaar.supplementaries.client.screens;

import java.util.Arrays;
import java.util.stream.IntStream;
import net.mehvahdjukaar.supplementaries.common.block.ITextHolderProvider;
import net.mehvahdjukaar.supplementaries.common.network.ModNetwork;
import net.mehvahdjukaar.supplementaries.common.network.ServerBoundSetTextHolderPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class TextHolderEditScreen<T extends BlockEntity & ITextHolderProvider> extends Screen {

    protected final T tile;

    protected final String[][] messages;

    protected final int totalLines;

    protected TextFieldHelper textInputUtil;

    protected int lineIndex = 0;

    protected int textHolderIndex = 0;

    protected int updateCounter;

    protected TextHolderEditScreen(T tile, Component title) {
        super(title);
        this.tile = tile;
        boolean filtering = Minecraft.getInstance().isTextFilteringEnabled();
        this.messages = (String[][]) IntStream.range(0, tile.textHoldersCount()).mapToObj(i -> (String[]) IntStream.range(0, tile.getTextHolder(i).size()).mapToObj(j -> tile.getTextHolder(i).getMessage(j, filtering)).map(Component::getString).toArray(String[]::new)).toArray(String[][]::new);
        this.totalLines = Arrays.stream(this.messages).mapToInt(innerArray -> innerArray.length).sum();
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        this.textInputUtil.charTyped(codePoint);
        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        this.scrollText((int) delta);
        return true;
    }

    protected boolean canScroll() {
        return true;
    }

    protected void scrollText(int amount) {
        if (this.canScroll()) {
            this.lineIndex -= amount;
            while (this.lineIndex < 0) {
                this.lineIndex = this.lineIndex + this.totalLines;
            }
            while (this.lineIndex >= this.messages[this.textHolderIndex].length) {
                this.lineIndex = this.lineIndex - this.messages[this.textHolderIndex].length;
                this.textHolderIndex++;
                this.textHolderIndex = this.textHolderIndex % this.messages.length;
            }
            this.textInputUtil.setCursorToEnd();
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 265) {
            this.scrollText(1);
            return true;
        } else if (keyCode != 264 && keyCode != 257 && keyCode != 335) {
            return this.textInputUtil.keyPressed(keyCode) || super.keyPressed(keyCode, scanCode, modifiers);
        } else {
            this.scrollText(-1);
            return true;
        }
    }

    @Override
    public void tick() {
        this.updateCounter++;
        if (!this.isValid()) {
            this.onClose();
        }
    }

    private boolean isValid() {
        return this.f_96541_ != null && this.f_96541_.player != null && !this.tile.isRemoved() && !this.tile.playerIsTooFarAwayToEdit(this.tile.getLevel(), this.tile.getBlockPos(), this.f_96541_.player.m_20148_());
    }

    @Override
    public void onClose() {
        this.tile.setChanged();
        super.onClose();
    }

    @Override
    public void removed() {
        ModNetwork.CHANNEL.sendToServer(new ServerBoundSetTextHolderPacket(this.tile.getBlockPos(), this.messages));
    }

    @Override
    protected void init() {
        this.m_142416_(Button.builder(CommonComponents.GUI_DONE, button -> this.onClose()).bounds(this.f_96543_ / 2 - 100, this.f_96544_ / 4 + 120, 200, 20).build());
        this.textInputUtil = new TextFieldHelper(() -> this.messages[this.textHolderIndex][this.lineIndex], h -> {
            this.messages[this.textHolderIndex][this.lineIndex] = h;
            this.tile.getTextHolder(this.textHolderIndex).setMessage(this.lineIndex, Component.literal(h));
        }, TextFieldHelper.createClipboardGetter(this.f_96541_), TextFieldHelper.createClipboardSetter(this.f_96541_), s -> this.f_96541_.font.width(s) <= this.tile.getTextHolder(this.textHolderIndex).getMaxLineVisualWidth());
    }
}