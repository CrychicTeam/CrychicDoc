package com.simibubi.create.infrastructure.gui;

import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.gui.ScreenOpener;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.apache.commons.lang3.mutable.MutableObject;

public class OpenCreateMenuButton extends Button {

    public static final ItemStack ICON = AllItems.GOGGLES.asStack();

    public OpenCreateMenuButton(int x, int y) {
        super(x, y, 20, 20, Components.immutableEmpty(), OpenCreateMenuButton::click, f_252438_);
    }

    @Override
    public void renderString(GuiGraphics graphics, Font pFont, int pColor) {
        graphics.renderItem(ICON, this.m_252754_() + 2, this.m_252907_() + 2);
    }

    public static void click(Button b) {
        ScreenOpener.open(new CreateMainMenuScreen(Minecraft.getInstance().screen));
    }

    public static class MenuRows {

        public static final OpenCreateMenuButton.MenuRows MAIN_MENU = new OpenCreateMenuButton.MenuRows(Arrays.asList(new OpenCreateMenuButton.SingleMenuRow("menu.singleplayer"), new OpenCreateMenuButton.SingleMenuRow("menu.multiplayer"), new OpenCreateMenuButton.SingleMenuRow("fml.menu.mods", "menu.online"), new OpenCreateMenuButton.SingleMenuRow("narrator.button.language", "narrator.button.accessibility")));

        public static final OpenCreateMenuButton.MenuRows INGAME_MENU = new OpenCreateMenuButton.MenuRows(Arrays.asList(new OpenCreateMenuButton.SingleMenuRow("menu.returnToGame"), new OpenCreateMenuButton.SingleMenuRow("gui.advancements", "gui.stats"), new OpenCreateMenuButton.SingleMenuRow("menu.sendFeedback", "menu.reportBugs"), new OpenCreateMenuButton.SingleMenuRow("menu.options", "menu.shareToLan"), new OpenCreateMenuButton.SingleMenuRow("menu.returnToMenu")));

        protected final List<String> leftButtons;

        protected final List<String> rightButtons;

        public MenuRows(List<OpenCreateMenuButton.SingleMenuRow> variants) {
            this.leftButtons = (List<String>) variants.stream().map(r -> r.left).collect(Collectors.toList());
            this.rightButtons = (List<String>) variants.stream().map(r -> r.right).collect(Collectors.toList());
        }
    }

    @EventBusSubscriber({ Dist.CLIENT })
    public static class OpenConfigButtonHandler {

        @SubscribeEvent
        public static void onGuiInit(ScreenEvent.Init event) {
            Screen gui = event.getScreen();
            OpenCreateMenuButton.MenuRows menu = null;
            int rowIdx = 0;
            int offsetX = 0;
            if (gui instanceof TitleScreen) {
                menu = OpenCreateMenuButton.MenuRows.MAIN_MENU;
                rowIdx = AllConfigs.client().mainMenuConfigButtonRow.get();
                offsetX = AllConfigs.client().mainMenuConfigButtonOffsetX.get();
            } else if (gui instanceof PauseScreen) {
                menu = OpenCreateMenuButton.MenuRows.INGAME_MENU;
                rowIdx = AllConfigs.client().ingameMenuConfigButtonRow.get();
                offsetX = AllConfigs.client().ingameMenuConfigButtonOffsetX.get();
            }
            if (rowIdx != 0 && menu != null) {
                boolean onLeft = offsetX < 0;
                String target = (String) (onLeft ? menu.leftButtons : menu.rightButtons).get(rowIdx - 1);
                int offsetX_ = offsetX;
                MutableObject<GuiEventListener> toAdd = new MutableObject(null);
                event.getListenersList().stream().filter(w -> w instanceof AbstractWidget).map(w -> (AbstractWidget) w).filter(w -> w.getMessage().getString().equals(target)).findFirst().ifPresent(w -> toAdd.setValue(new OpenCreateMenuButton(w.getX() + offsetX_ + (onLeft ? -20 : w.getWidth()), w.getY())));
                if (toAdd.getValue() != null) {
                    event.addListener((GuiEventListener) toAdd.getValue());
                }
            }
        }
    }

    public static class SingleMenuRow {

        public final String left;

        public final String right;

        public SingleMenuRow(String left, String right) {
            this.left = I18n.get(left);
            this.right = I18n.get(right);
        }

        public SingleMenuRow(String center) {
            this(center, center);
        }
    }
}