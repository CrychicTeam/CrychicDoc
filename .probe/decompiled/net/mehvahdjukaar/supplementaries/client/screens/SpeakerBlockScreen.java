package net.mehvahdjukaar.supplementaries.client.screens;

import net.mehvahdjukaar.supplementaries.SuppClientPlatformStuff;
import net.mehvahdjukaar.supplementaries.client.screens.widgets.ISlider;
import net.mehvahdjukaar.supplementaries.common.block.tiles.SpeakerBlockTile;
import net.mehvahdjukaar.supplementaries.common.network.ModNetwork;
import net.mehvahdjukaar.supplementaries.common.network.ServerBoundSetSpeakerBlockPacket;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class SpeakerBlockScreen extends Screen {

    private static final Component CHAT_TEXT = Component.translatable("gui.supplementaries.speaker_block.chat_message");

    private static final Component NARRATOR_TEXT = Component.translatable("gui.supplementaries.speaker_block.narrator_message");

    private static final Component ACTION_BAR_TEXT = Component.translatable("gui.supplementaries.speaker_block.action_bar_message");

    private static final Component TITLE_TEXT = Component.translatable("gui.supplementaries.speaker_block.title_message");

    private static final Component DISTANCE_BLOCKS = Component.translatable("gui.supplementaries.speaker_block.blocks");

    private static final Component VOLUME_TEXT = Component.translatable("gui.supplementaries.speaker_block.volume");

    private static final Component EDIT = Component.translatable("gui.supplementaries.speaker_block.edit");

    private EditBox editBox;

    private final SpeakerBlockTile tileSpeaker;

    private SpeakerBlockTile.Mode mode;

    private Button modeBtn;

    private ISlider volumeSlider;

    public SpeakerBlockScreen(SpeakerBlockTile te) {
        super(EDIT);
        this.tileSpeaker = te;
    }

    public static void open(SpeakerBlockTile te) {
        Minecraft.getInstance().setScreen(new SpeakerBlockScreen(te));
    }

    @Override
    public void tick() {
        this.editBox.tick();
    }

    private void updateMode() {
        switch(this.mode) {
            case NARRATOR:
                this.modeBtn.m_93666_(NARRATOR_TEXT);
                break;
            case STATUS_MESSAGE:
                this.modeBtn.m_93666_(ACTION_BAR_TEXT);
                break;
            case TITLE:
                this.modeBtn.m_93666_(TITLE_TEXT);
                break;
            default:
                this.modeBtn.m_93666_(CHAT_TEXT);
        }
    }

    private void toggleMode() {
        this.mode = SpeakerBlockTile.Mode.values()[(this.mode.ordinal() + 1) % SpeakerBlockTile.Mode.values().length];
        if (!(Boolean) CommonConfigs.Redstone.SPEAKER_NARRATOR.get() && this.mode == SpeakerBlockTile.Mode.NARRATOR) {
            this.mode = SpeakerBlockTile.Mode.CHAT;
        }
    }

    @Override
    public void init() {
        assert this.f_96541_ != null;
        int range = (Integer) CommonConfigs.Redstone.SPEAKER_RANGE.get();
        this.mode = this.tileSpeaker.getMode();
        String message = this.tileSpeaker.getMessage(Minecraft.getInstance().isTextFilteringEnabled()).getString();
        double initialVolume = this.tileSpeaker.getVolume();
        this.volumeSlider = SuppClientPlatformStuff.createSlider(this.f_96543_ / 2 - 75, this.f_96544_ / 4 + 80, 150, 20, VOLUME_TEXT, DISTANCE_BLOCKS, 1.0, (double) range, initialVolume, 1.0, 1, true);
        this.m_142416_(this.volumeSlider);
        this.m_142416_(Button.builder(CommonComponents.GUI_DONE, button -> this.onDone()).bounds(this.f_96543_ / 2 - 100, this.f_96544_ / 4 + 120, 200, 20).build());
        this.modeBtn = (Button) this.m_142416_(Button.builder(CHAT_TEXT, button -> {
            this.toggleMode();
            this.updateMode();
        }).bounds(this.f_96543_ / 2 - 75, this.f_96544_ / 4 + 50, 150, 20).build());
        if (!(Boolean) CommonConfigs.Redstone.SPEAKER_NARRATOR.get()) {
            this.modeBtn.f_93623_ = false;
        }
        this.updateMode();
        this.editBox = new EditBox(this.f_96547_, this.f_96543_ / 2 - 100, this.f_96544_ / 4 + 10, 200, 20, this.f_96539_) {

            @Override
            protected MutableComponent createNarrationMessage() {
                return super.createNarrationMessage();
            }
        };
        this.editBox.setValue(message);
        this.editBox.setMaxLength((Integer) CommonConfigs.Redstone.MAX_TEXT.get());
        this.m_142416_(this.editBox);
        this.m_264313_(this.editBox);
        this.editBox.setFocused(true);
    }

    @Override
    public void removed() {
        ModNetwork.CHANNEL.sendToServer(new ServerBoundSetSpeakerBlockPacket(this.tileSpeaker.m_58899_(), this.editBox.getValue(), this.mode, this.volumeSlider.getValue()));
    }

    private void onDone() {
        this.tileSpeaker.m_6596_();
        this.f_96541_.setScreen(null);
    }

    @Override
    public void onClose() {
        this.onDone();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else if (keyCode != 257 && keyCode != 335) {
            return false;
        } else {
            this.onDone();
            return true;
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (this.volumeSlider == this.m_7222_() && button == 0) {
            this.volumeSlider.onReleased(mouseX, mouseY);
            this.m_7522_(this.editBox);
        }
        return true;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(graphics);
        graphics.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 40, 16777215);
        super.render(graphics, mouseX, mouseY, partialTicks);
    }
}