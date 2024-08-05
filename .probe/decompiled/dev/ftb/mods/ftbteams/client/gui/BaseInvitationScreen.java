package dev.ftb.mods.ftbteams.client.gui;

import com.mojang.authlib.GameProfile;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftblibrary.ui.GuiHelper;
import dev.ftb.mods.ftblibrary.ui.NordButton;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.SimpleButton;
import dev.ftb.mods.ftblibrary.ui.TextField;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.WidgetLayout;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.ui.misc.NordColors;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.client.KnownClientPlayer;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public abstract class BaseInvitationScreen extends BaseScreen implements InvitationSetup {

    protected final Set<KnownClientPlayer> available;

    protected final Set<GameProfile> invites = new HashSet();

    private Panel playerPanel;

    private Button executeButton;

    private Button closeButton;

    private final Component title;

    public BaseInvitationScreen(Component title) {
        this.title = title;
        this.available = (Set<KnownClientPlayer>) FTBTeamsAPI.api().getClientManager().knownClientPlayers().stream().filter(this::shouldIncludePlayer).collect(Collectors.toSet());
    }

    protected abstract boolean shouldIncludePlayer(KnownClientPlayer var1);

    protected abstract BaseInvitationScreen.ExecuteButton makeExecuteButton();

    @Override
    public boolean onInit() {
        this.setWidth(200);
        this.setHeight(this.getScreen().getGuiScaledHeight() * 3 / 4);
        return true;
    }

    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        GuiHelper.drawHollowRect(graphics, x, y, w, h, NordColors.POLAR_NIGHT_0, true);
        NordColors.POLAR_NIGHT_0.draw(graphics, x + 1, y + 1, w - 2, h - 2);
        NordColors.POLAR_NIGHT_1.draw(graphics, x + this.playerPanel.posX, y + this.playerPanel.posY, this.playerPanel.width, this.playerPanel.height);
        NordColors.POLAR_NIGHT_0.draw(graphics, x + 1, y + h - 20, w - 2, 18);
    }

    @Override
    public void drawForeground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        theme.drawString(graphics, this.title, x + w / 2, y + 5, NordColors.SNOW_STORM_1, 4);
    }

    @Override
    public void addWidgets() {
        this.add(this.closeButton = new SimpleButton(this, Component.translatable("gui.cancel"), Icons.CANCEL.withTint(NordColors.SNOW_STORM_2), (simpleButton, mouseButton) -> this.closeGui()) {

            @Override
            public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
                this.drawIcon(graphics, theme, x, y, w, h);
            }
        });
        this.add(this.playerPanel = new BaseInvitationScreen.PlayerButtonPanel());
        this.add(this.executeButton = this.makeExecuteButton());
    }

    @Override
    public void alignWidgets() {
        this.closeButton.setPosAndSize(this.width - 20, 2, 16, 16);
        this.playerPanel.setPosAndSize(2, 20, this.width - 4, this.height - 40);
        this.executeButton.setPosAndSize(60, this.height - 18, 80, 16);
    }

    @Override
    public boolean isInvited(GameProfile profile) {
        return this.invites.contains(profile);
    }

    @Override
    public void setInvited(GameProfile profile, boolean invited) {
        if (invited) {
            this.invites.add(profile);
        } else {
            this.invites.remove(profile);
        }
    }

    protected abstract class ExecuteButton extends NordButton {

        private final Component titleDark = this.title.copy().withStyle(Style.EMPTY.withColor(NordColors.POLAR_NIGHT_0.rgb()));

        private final Runnable callback;

        public ExecuteButton(Component txt, Icon icon, Runnable callback) {
            super(BaseInvitationScreen.this, txt, icon);
            this.callback = callback;
        }

        @Override
        public void onClicked(MouseButton button) {
            if (this.isEnabled()) {
                this.callback.run();
            }
        }

        @Override
        public Component getTitle() {
            return this.isEnabled() ? this.title : this.titleDark;
        }

        @Override
        public boolean renderTitleInCenter() {
            return true;
        }
    }

    private class PlayerButtonPanel extends Panel {

        public PlayerButtonPanel() {
            super(BaseInvitationScreen.this);
        }

        @Override
        public void addWidgets() {
            if (BaseInvitationScreen.this.available.isEmpty()) {
                this.add(new TextField(this).setText(Component.translatable("ftbteams.gui.no_players").withStyle(ChatFormatting.ITALIC)).addFlags(4));
            } else {
                BaseInvitationScreen.this.available.forEach(player -> this.add(new InvitedButton(this, BaseInvitationScreen.this, player)));
            }
        }

        @Override
        public void alignWidgets() {
            this.align(new WidgetLayout.Vertical(2, 1, 2));
            this.widgets.forEach(w -> w.setX(4));
        }

        @Override
        public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            NordColors.POLAR_NIGHT_2.draw(graphics, x, y, w, h);
        }
    }
}