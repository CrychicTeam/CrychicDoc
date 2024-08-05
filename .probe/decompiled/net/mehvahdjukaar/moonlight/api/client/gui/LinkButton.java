package net.mehvahdjukaar.moonlight.api.client.gui;

import java.util.Calendar;
import net.mehvahdjukaar.moonlight.core.Moonlight;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.TextAndImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

public class LinkButton {

    public static final ResourceLocation MISC_ICONS = Moonlight.res("textures/gui/misc_icons.png");

    private static final boolean LOL;

    public static TextAndImageButton create(Screen parent, int x, int y, int uInd, int vInd, String url, String tooltip) {
        return create(MISC_ICONS, 64, 64, 14, 14, parent, x, y, uInd, vInd, url, tooltip);
    }

    public static TextAndImageButton create(ResourceLocation texture, int textureW, int textureH, int iconW, int iconH, Screen parent, int x, int y, int uInd, int vInd, String url, String tooltip) {
        String finalUrl = getLink(url);
        Button.OnPress onPress = op -> {
            Style style = Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, finalUrl));
            parent.handleComponentClicked(style);
        };
        TextAndImageButton button = TextAndImageButton.builder(CommonComponents.EMPTY, texture, onPress).textureSize(iconW, iconH).usedTextureSize(iconW, iconH).textureSize(textureW, textureH).offset(0, 3).texStart(uInd * iconW, vInd * iconH).build();
        button.m_264152_(x, y);
        button.m_93674_(iconW + 6);
        button.f_93619_ = iconH + 6;
        button.m_257544_(Tooltip.create(Component.literal(tooltip)));
        return button;
    }

    private static String getLink(String original) {
        return LOL ? "https://www.youtube.com/watch?v=dQw4w9WgXcQ" : original;
    }

    static {
        Calendar calendar = Calendar.getInstance();
        LOL = calendar.get(2) == 3 && calendar.get(5) == 1;
    }
}