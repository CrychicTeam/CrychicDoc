package com.mojang.realmsclient.gui.screens;

import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.RealmsMainScreen;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.util.task.GetServerDetailsTask;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.realms.RealmsScreen;
import org.slf4j.Logger;

public class RealmsTermsScreen extends RealmsScreen {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Component TITLE = Component.translatable("mco.terms.title");

    private static final Component TERMS_STATIC_TEXT = Component.translatable("mco.terms.sentence.1");

    private static final Component TERMS_LINK_TEXT = CommonComponents.space().append(Component.translatable("mco.terms.sentence.2").withStyle(Style.EMPTY.withUnderlined(true)));

    private final Screen lastScreen;

    private final RealmsMainScreen mainScreen;

    private final RealmsServer realmsServer;

    private boolean onLink;

    public RealmsTermsScreen(Screen screen0, RealmsMainScreen realmsMainScreen1, RealmsServer realmsServer2) {
        super(TITLE);
        this.lastScreen = screen0;
        this.mainScreen = realmsMainScreen1;
        this.realmsServer = realmsServer2;
    }

    @Override
    public void init() {
        int $$0 = this.f_96543_ / 4 - 2;
        this.m_142416_(Button.builder(Component.translatable("mco.terms.buttons.agree"), p_90054_ -> this.agreedToTos()).bounds(this.f_96543_ / 4, m_120774_(12), $$0, 20).build());
        this.m_142416_(Button.builder(Component.translatable("mco.terms.buttons.disagree"), p_280762_ -> this.f_96541_.setScreen(this.lastScreen)).bounds(this.f_96543_ / 2 + 4, m_120774_(12), $$0, 20).build());
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

    private void agreedToTos() {
        RealmsClient $$0 = RealmsClient.create();
        try {
            $$0.agreeToTos();
            this.f_96541_.setScreen(new RealmsLongRunningMcoTaskScreen(this.lastScreen, new GetServerDetailsTask(this.mainScreen, this.lastScreen, this.realmsServer, new ReentrantLock())));
        } catch (RealmsServiceException var3) {
            LOGGER.error("Couldn't agree to TOS");
        }
    }

    @Override
    public boolean mouseClicked(double double0, double double1, int int2) {
        if (this.onLink) {
            this.f_96541_.keyboardHandler.setClipboard("https://aka.ms/MinecraftRealmsTerms");
            Util.getPlatform().openUri("https://aka.ms/MinecraftRealmsTerms");
            return true;
        } else {
            return super.m_6375_(double0, double1, int2);
        }
    }

    @Override
    public Component getNarrationMessage() {
        return CommonComponents.joinForNarration(super.m_142562_(), TERMS_STATIC_TEXT).append(CommonComponents.SPACE).append(TERMS_LINK_TEXT);
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        guiGraphics0.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 17, 16777215);
        guiGraphics0.drawString(this.f_96547_, TERMS_STATIC_TEXT, this.f_96543_ / 2 - 120, m_120774_(5), 16777215, false);
        int $$4 = this.f_96547_.width(TERMS_STATIC_TEXT);
        int $$5 = this.f_96543_ / 2 - 121 + $$4;
        int $$6 = m_120774_(5);
        int $$7 = $$5 + this.f_96547_.width(TERMS_LINK_TEXT) + 1;
        int $$8 = $$6 + 1 + 9;
        this.onLink = $$5 <= int1 && int1 <= $$7 && $$6 <= int2 && int2 <= $$8;
        guiGraphics0.drawString(this.f_96547_, TERMS_LINK_TEXT, this.f_96543_ / 2 - 120 + $$4, m_120774_(5), this.onLink ? 7107012 : 3368635, false);
        super.m_88315_(guiGraphics0, int1, int2, float3);
    }
}