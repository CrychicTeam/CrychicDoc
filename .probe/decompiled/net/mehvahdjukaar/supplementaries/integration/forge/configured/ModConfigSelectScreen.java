package net.mehvahdjukaar.supplementaries.integration.forge.configured;

import net.mehvahdjukaar.moonlight.api.client.gui.LinkButton;
import net.mehvahdjukaar.moonlight.api.integration.configured.CustomConfigSelectScreen;
import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModTextures;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;

public class ModConfigSelectScreen extends CustomConfigSelectScreen {

    public ModConfigSelectScreen(Screen parent) {
        super("supplementaries", ((net.minecraft.world.item.Item) ModRegistry.GLOBE_ITEM.get()).getDefaultInstance(), "§6Supplementaries Configured", ModTextures.CONFIG_BACKGROUND, parent, ModConfigScreen::new, ClientConfigs.SPEC, CommonConfigs.SPEC);
    }

    @Override
    protected void init() {
        super.m_7856_();
        Button found = null;
        for (GuiEventListener c : this.m_6702_()) {
            if (c instanceof Button) {
                Button button = (Button) c;
                if (button.m_5711_() == 150) {
                    found = button;
                }
            }
        }
        if (found != null) {
            this.m_169411_(found);
        }
        int y = this.f_96544_ - 29;
        int centerX = this.f_96543_ / 2;
        this.m_142416_(Button.builder(CommonComponents.GUI_BACK, buttonx -> this.f_96541_.setScreen(this.parent)).bounds(centerX - 45, y, 90, 20).build());
        this.m_142416_(LinkButton.create(this, centerX - 45 - 22, y, 3, 1, "https://www.patreon.com/user?u=53696377", "Support me on Patreon :D"));
        this.m_142416_(LinkButton.create(this, centerX - 45 - 44, y, 2, 2, "https://ko-fi.com/mehvahdjukaar", "Donate a Coffee"));
        this.m_142416_(LinkButton.create(this, centerX - 45 - 66, y, 1, 2, "https://www.curseforge.com/minecraft/mc-mods/supplementaries", "CurseForge Page"));
        this.m_142416_(LinkButton.create(this, centerX - 45 - 88, y, 0, 2, "https://github.com/MehVahdJukaar/Supplementaries/wiki", "Mod Wiki"));
        this.m_142416_(LinkButton.create(this, centerX + 45 + 2, y, 1, 1, "https://discord.com/invite/qdKRTDf8Cv", "Mod Discord"));
        this.m_142416_(LinkButton.create(this, centerX + 45 + 2 + 22, y, 0, 1, "https://www.youtube.com/watch?v=LSPNAtAEn28&t=1s", "Youtube Channel"));
        this.m_142416_(LinkButton.create(this, centerX + 45 + 2 + 44, y, 2, 1, "https://twitter.com/Supplementariez?s=09", "Twitter Page"));
        this.m_142416_(LinkButton.create(this, centerX + 45 + 2 + 66, y, 3, 2, "https://www.akliz.net/supplementaries", "Need a server? Get one with Akliz"));
    }
}