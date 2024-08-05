package net.minecraftforge.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.loading.ClientModLoader;
import net.minecraftforge.fml.VersionChecker.Status;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLConfig.ConfigValue;

@OnlyIn(Dist.CLIENT)
public class TitleScreenModUpdateIndicator extends Screen {

    private static final ResourceLocation VERSION_CHECK_ICONS = new ResourceLocation("forge", "textures/gui/version_check_icons.png");

    private final Button modButton;

    private Status showNotification = null;

    private boolean hasCheckedForUpdates = false;

    public TitleScreenModUpdateIndicator(Button modButton) {
        super(Component.translatable("forge.menu.updatescreen.title"));
        this.modButton = modButton;
    }

    @Override
    public void init() {
        if (!this.hasCheckedForUpdates) {
            if (this.modButton != null) {
                this.showNotification = ClientModLoader.checkForUpdates();
            }
            this.hasCheckedForUpdates = true;
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (this.showNotification != null && this.showNotification.shouldDraw() && FMLConfig.getBoolConfigValue(ConfigValue.VERSION_CHECK)) {
            int x = this.modButton.m_252754_();
            int y = this.modButton.m_252907_();
            int w = this.modButton.m_5711_();
            int h = this.modButton.m_93694_();
            guiGraphics.blit(VERSION_CHECK_ICONS, x + w - (h / 2 + 4), y + (h / 2 - 4), (float) (this.showNotification.getSheetOffset() * 8), this.showNotification.isAnimated() && (System.currentTimeMillis() / 800L & 1L) == 1L ? 8.0F : 0.0F, 8, 8, 64, 16);
        }
    }

    public static TitleScreenModUpdateIndicator init(TitleScreen guiMainMenu, Button modButton) {
        TitleScreenModUpdateIndicator titleScreenModUpdateIndicator = new TitleScreenModUpdateIndicator(modButton);
        titleScreenModUpdateIndicator.m_6574_(guiMainMenu.getMinecraft(), guiMainMenu.f_96543_, guiMainMenu.f_96544_);
        titleScreenModUpdateIndicator.init();
        return titleScreenModUpdateIndicator;
    }
}