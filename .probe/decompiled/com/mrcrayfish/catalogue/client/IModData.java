package com.mrcrayfish.catalogue.client;

import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;

public interface IModData {

    IModData.Type getType();

    String getModId();

    String getDisplayName();

    String getVersion();

    String getDescription();

    @Nullable
    String getItemIcon();

    @Nullable
    String getImageIcon();

    String getLicense();

    @Nullable
    String getCredits();

    @Nullable
    String getAuthors();

    @Nullable
    String getHomepage();

    @Nullable
    String getIssueTracker();

    @Nullable
    String getBanner();

    @Nullable
    String getBackground();

    IModData.Update getUpdate();

    boolean hasConfig();

    boolean isLogoSmooth();

    void openConfigScreen(Screen var1);

    public static enum Type {

        DEFAULT(ChatFormatting.RESET), LIBRARY(ChatFormatting.DARK_GRAY), GENERATED(ChatFormatting.AQUA);

        private final ChatFormatting style;

        private Type(ChatFormatting style) {
            this.style = style;
        }

        public ChatFormatting getStyle() {
            return this.style;
        }
    }

    public static record Update(boolean animated, String url, int texOffset) {
    }
}