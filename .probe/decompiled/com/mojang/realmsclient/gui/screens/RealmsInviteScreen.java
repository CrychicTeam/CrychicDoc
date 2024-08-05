package com.mojang.realmsclient.gui.screens;

import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.RealmsServer;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.realms.RealmsScreen;
import org.slf4j.Logger;

public class RealmsInviteScreen extends RealmsScreen {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Component NAME_LABEL = Component.translatable("mco.configure.world.invite.profile.name").withStyle(p_289621_ -> p_289621_.withColor(-6250336));

    private static final Component INVITING_PLAYER_TEXT = Component.translatable("mco.configure.world.players.inviting").withStyle(p_289617_ -> p_289617_.withColor(-6250336));

    private static final Component NO_SUCH_PLAYER_ERROR_TEXT = Component.translatable("mco.configure.world.players.error").withStyle(p_289622_ -> p_289622_.withColor(-65536));

    private EditBox profileName;

    private Button inviteButton;

    private final RealmsServer serverData;

    private final RealmsConfigureWorldScreen configureScreen;

    private final Screen lastScreen;

    @Nullable
    private Component message;

    public RealmsInviteScreen(RealmsConfigureWorldScreen realmsConfigureWorldScreen0, Screen screen1, RealmsServer realmsServer2) {
        super(GameNarrator.NO_TITLE);
        this.configureScreen = realmsConfigureWorldScreen0;
        this.lastScreen = screen1;
        this.serverData = realmsServer2;
    }

    @Override
    public void tick() {
        this.profileName.tick();
    }

    @Override
    public void init() {
        this.profileName = new EditBox(this.f_96541_.font, this.f_96543_ / 2 - 100, m_120774_(2), 200, 20, null, Component.translatable("mco.configure.world.invite.profile.name"));
        this.m_7787_(this.profileName);
        this.m_264313_(this.profileName);
        this.inviteButton = (Button) this.m_142416_(Button.builder(Component.translatable("mco.configure.world.buttons.invite"), p_88721_ -> this.onInvite()).bounds(this.f_96543_ / 2 - 100, m_120774_(10), 200, 20).build());
        this.m_142416_(Button.builder(CommonComponents.GUI_CANCEL, p_280729_ -> this.f_96541_.setScreen(this.lastScreen)).bounds(this.f_96543_ / 2 - 100, m_120774_(12), 200, 20).build());
    }

    private void onInvite() {
        if (Util.isBlank(this.profileName.getValue())) {
            this.showMessage(NO_SUCH_PLAYER_ERROR_TEXT);
        } else {
            long $$0 = this.serverData.id;
            String $$1 = this.profileName.getValue().trim();
            this.inviteButton.f_93623_ = false;
            this.profileName.setEditable(false);
            this.showMessage(INVITING_PLAYER_TEXT);
            CompletableFuture.supplyAsync(() -> {
                try {
                    return RealmsClient.create().invite($$0, $$1);
                } catch (Exception var4) {
                    LOGGER.error("Couldn't invite user");
                    return null;
                }
            }, Util.ioPool()).thenAcceptAsync(p_289618_ -> {
                if (p_289618_ != null) {
                    this.serverData.players = p_289618_.players;
                    this.f_96541_.setScreen(new RealmsPlayerScreen(this.configureScreen, this.serverData));
                } else {
                    this.showMessage(NO_SUCH_PLAYER_ERROR_TEXT);
                }
                this.profileName.setEditable(true);
                this.inviteButton.f_93623_ = true;
            }, this.f_289574_);
        }
    }

    private void showMessage(Component component0) {
        this.message = component0;
        this.f_96541_.getNarrator().sayNow(component0);
    }

    @Override
    public boolean keyPressed(int int0, int int1, int int2) {
        if (int0 == 256) {
            this.f_96541_.setScreen(this.lastScreen);
            return true;
        } else {
            return super.m_7933_(int0, int1, int2);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        guiGraphics0.drawString(this.f_96547_, NAME_LABEL, this.f_96543_ / 2 - 100, m_120774_(1), -1, false);
        if (this.message != null) {
            guiGraphics0.drawCenteredString(this.f_96547_, this.message, this.f_96543_ / 2, m_120774_(5), -1);
        }
        this.profileName.m_88315_(guiGraphics0, int1, int2, float3);
        super.m_88315_(guiGraphics0, int1, int2, float3);
    }
}