package net.minecraft.client.gui.screens;

import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.commands.PublishCommand;
import net.minecraft.util.HttpUtil;
import net.minecraft.world.level.GameType;

public class ShareToLanScreen extends Screen {

    private static final int PORT_LOWER_BOUND = 1024;

    private static final int PORT_HIGHER_BOUND = 65535;

    private static final Component ALLOW_COMMANDS_LABEL = Component.translatable("selectWorld.allowCommands");

    private static final Component GAME_MODE_LABEL = Component.translatable("selectWorld.gameMode");

    private static final Component INFO_TEXT = Component.translatable("lanServer.otherPlayers");

    private static final Component PORT_INFO_TEXT = Component.translatable("lanServer.port");

    private static final Component PORT_UNAVAILABLE = Component.translatable("lanServer.port.unavailable.new", 1024, 65535);

    private static final Component INVALID_PORT = Component.translatable("lanServer.port.invalid.new", 1024, 65535);

    private static final int INVALID_PORT_COLOR = 16733525;

    private final Screen lastScreen;

    private GameType gameMode = GameType.SURVIVAL;

    private boolean commands;

    private int port = HttpUtil.getAvailablePort();

    @Nullable
    private EditBox portEdit;

    public ShareToLanScreen(Screen screen0) {
        super(Component.translatable("lanServer.title"));
        this.lastScreen = screen0;
    }

    @Override
    protected void init() {
        IntegratedServer $$0 = this.f_96541_.getSingleplayerServer();
        this.gameMode = $$0.m_130008_();
        this.commands = $$0.m_129910_().getAllowCommands();
        this.m_142416_(CycleButton.<GameType>builder(GameType::m_151500_).withValues(GameType.SURVIVAL, GameType.SPECTATOR, GameType.CREATIVE, GameType.ADVENTURE).withInitialValue(this.gameMode).create(this.f_96543_ / 2 - 155, 100, 150, 20, GAME_MODE_LABEL, (p_169429_, p_169430_) -> this.gameMode = p_169430_));
        this.m_142416_(CycleButton.onOffBuilder(this.commands).create(this.f_96543_ / 2 + 5, 100, 150, 20, ALLOW_COMMANDS_LABEL, (p_169432_, p_169433_) -> this.commands = p_169433_));
        Button $$1 = Button.builder(Component.translatable("lanServer.start"), p_280826_ -> {
            this.f_96541_.setScreen(null);
            Component $$2;
            if ($$0.publishServer(this.gameMode, this.commands, this.port)) {
                $$2 = PublishCommand.getSuccessMessage(this.port);
            } else {
                $$2 = Component.translatable("commands.publish.failed");
            }
            this.f_96541_.gui.getChat().addMessage($$2);
            this.f_96541_.updateTitle();
        }).bounds(this.f_96543_ / 2 - 155, this.f_96544_ - 28, 150, 20).build();
        this.portEdit = new EditBox(this.f_96547_, this.f_96543_ / 2 - 75, 160, 150, 20, Component.translatable("lanServer.port"));
        this.portEdit.setResponder(p_258130_ -> {
            Component $$2 = this.tryParsePort(p_258130_);
            this.portEdit.setHint(Component.literal(this.port + "").withStyle(ChatFormatting.DARK_GRAY));
            if ($$2 == null) {
                this.portEdit.setTextColor(14737632);
                this.portEdit.m_257544_(null);
                $$1.f_93623_ = true;
            } else {
                this.portEdit.setTextColor(16733525);
                this.portEdit.m_257544_(Tooltip.create($$2));
                $$1.f_93623_ = false;
            }
        });
        this.portEdit.setHint(Component.literal(this.port + "").withStyle(ChatFormatting.DARK_GRAY));
        this.m_142416_(this.portEdit);
        this.m_142416_($$1);
        this.m_142416_(Button.builder(CommonComponents.GUI_CANCEL, p_280824_ -> this.f_96541_.setScreen(this.lastScreen)).bounds(this.f_96543_ / 2 + 5, this.f_96544_ - 28, 150, 20).build());
    }

    @Override
    public void tick() {
        super.tick();
        if (this.portEdit != null) {
            this.portEdit.tick();
        }
    }

    @Nullable
    private Component tryParsePort(String string0) {
        if (string0.isBlank()) {
            this.port = HttpUtil.getAvailablePort();
            return null;
        } else {
            try {
                this.port = Integer.parseInt(string0);
                if (this.port < 1024 || this.port > 65535) {
                    return INVALID_PORT;
                } else {
                    return !HttpUtil.isPortAvailable(this.port) ? PORT_UNAVAILABLE : null;
                }
            } catch (NumberFormatException var3) {
                this.port = HttpUtil.getAvailablePort();
                return INVALID_PORT;
            }
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        guiGraphics0.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 50, 16777215);
        guiGraphics0.drawCenteredString(this.f_96547_, INFO_TEXT, this.f_96543_ / 2, 82, 16777215);
        guiGraphics0.drawCenteredString(this.f_96547_, PORT_INFO_TEXT, this.f_96543_ / 2, 142, 16777215);
        super.render(guiGraphics0, int1, int2, float3);
    }
}