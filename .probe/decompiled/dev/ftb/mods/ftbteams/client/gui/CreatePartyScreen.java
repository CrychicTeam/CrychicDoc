package dev.ftb.mods.ftbteams.client.gui;

import com.mojang.authlib.GameProfile;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.FaceIcon;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftblibrary.ui.GuiHelper;
import dev.ftb.mods.ftblibrary.ui.NordButton;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.SimpleButton;
import dev.ftb.mods.ftblibrary.ui.TextBox;
import dev.ftb.mods.ftblibrary.ui.TextField;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.VerticalSpaceWidget;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.ui.WidgetLayout;
import dev.ftb.mods.ftblibrary.ui.input.Key;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.ui.misc.NordColors;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.client.ClientTeamManager;
import dev.ftb.mods.ftbteams.api.property.TeamProperties;
import dev.ftb.mods.ftbteams.data.FTBTUtils;
import dev.ftb.mods.ftbteams.net.CreatePartyMessage;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

public class CreatePartyScreen extends BaseScreen implements NordColors, InvitationSetup {

    private final ClientTeamManager manager;

    private final Set<GameProfile> invitedMembers;

    private Panel invitePanel;

    private Panel settingsPanel;

    private Button createTeamButton;

    private Color4I teamColor;

    private TextBox nameTextBox;

    private TextBox descriptionTextBox;

    public CreatePartyScreen() {
        this.setSize(300, 200);
        this.manager = FTBTeamsAPI.api().getClientManager();
        this.invitedMembers = new HashSet();
        this.teamColor = this.manager.selfTeam().getProperty(TeamProperties.COLOR);
    }

    @Override
    public void addWidgets() {
        Button closeButton;
        this.add(closeButton = new SimpleButton(this, Component.translatable("gui.cancel"), Icons.CANCEL.withTint(SNOW_STORM_2), (simpleButton, mouseButton) -> this.closeGui()) {

            @Override
            public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
                this.drawIcon(graphics, theme, x, y, w, h);
            }
        });
        Button colorButton;
        this.add(colorButton = new SimpleButton(this, Component.translatable("gui.color"), this.teamColor.withBorder(POLAR_NIGHT_0, false), (simpleButton, mouseButton) -> {
            this.teamColor = FTBTUtils.randomColor();
            simpleButton.setIcon(this.teamColor.withBorder(POLAR_NIGHT_0, false));
        }) {

            @Override
            public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
                this.icon.draw(graphics, x, y, w, h);
            }
        });
        this.add(this.invitePanel = new CreatePartyScreen.InvitePanel());
        this.add(this.settingsPanel = new CreatePartyScreen.SettingsPanel());
        this.add(this.createTeamButton = new NordButton(this, Component.translatable("ftbteams.create_party").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(NordColors.GREEN.rgb()))), Icons.ACCEPT) {

            @Override
            public boolean renderTitleInCenter() {
                return true;
            }

            @Override
            public void onClicked(MouseButton mouseButton) {
                this.closeGui(false);
                new CreatePartyMessage(CreatePartyScreen.this.nameTextBox.getText(), CreatePartyScreen.this.descriptionTextBox.getText(), CreatePartyScreen.this.teamColor.rgb(), CreatePartyScreen.this.invitedMembers).sendToServer();
            }
        });
        closeButton.setPosAndSize(this.width - 18, 5, 12, 12);
        colorButton.setPosAndSize(5, 5, 12, 12);
        this.invitePanel.setPosAndSize(1, 22, 89, this.height - 23);
    }

    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        GuiHelper.drawHollowRect(graphics, x, y, w, h, POLAR_NIGHT_0, true);
        POLAR_NIGHT_1.draw(graphics, x + 1, y + 1, w - 2, h - 2);
        POLAR_NIGHT_0.draw(graphics, x + 1, y + 21, w - 2, 1);
        POLAR_NIGHT_0.draw(graphics, x + this.invitePanel.width + 1, y + this.invitePanel.posY, 1, this.invitePanel.height);
    }

    @Override
    public void drawForeground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        super.drawForeground(graphics, theme, x, y, w, h);
        theme.drawString(graphics, Component.translatable("ftbteams.create_party"), x + w / 2, y + 7, SNOW_STORM_1, 4);
    }

    @Override
    public boolean keyPressed(Key key) {
        return super.keyPressed(key);
    }

    @Override
    public boolean isInvited(GameProfile profile) {
        return this.invitedMembers.contains(profile);
    }

    @Override
    public void setInvited(GameProfile profile, boolean invited) {
        if (invited) {
            this.invitedMembers.add(profile);
        } else {
            this.invitedMembers.remove(profile);
        }
    }

    private class InvitePanel extends Panel {

        public InvitePanel() {
            super(CreatePartyScreen.this);
        }

        @Override
        public void addWidgets() {
            this.add(new TextField(this).addFlags(4).setText(Component.translatable("ftbteams.gui.add_members")));
            this.add(new VerticalSpaceWidget(this, 2));
            User self = Minecraft.getInstance().getUser();
            this.add(new NordButton(this, Component.literal("✦ ").withStyle(ChatFormatting.GOLD).append(self.getName()), FaceIcon.getFace(self.getGameProfile())) {

                @Override
                public void onClicked(MouseButton mouseButton) {
                }
            });
            CreatePartyScreen.this.manager.knownClientPlayers().stream().filter(kcp -> kcp.isOnlineAndNotInParty() && !kcp.equals(CreatePartyScreen.this.manager.self())).sorted().forEach(kcp -> this.add(new InvitedButton(this, CreatePartyScreen.this, kcp)));
        }

        @Override
        public void alignWidgets() {
            this.align(new WidgetLayout.Vertical(4, 2, 1));
            this.width = 80;
            for (Widget widget : this.widgets) {
                this.width = Math.max(this.width, widget.width);
            }
            for (Widget widget : this.widgets) {
                widget.setX(1);
                widget.setWidth(this.width - 2);
            }
            CreatePartyScreen.this.settingsPanel.setPosAndSize(this.width + 3, 23, CreatePartyScreen.this.width - CreatePartyScreen.this.invitePanel.width - 5, CreatePartyScreen.this.height - 40);
            CreatePartyScreen.this.createTeamButton.setPosAndSize(CreatePartyScreen.this.settingsPanel.posX, CreatePartyScreen.this.height - 15, CreatePartyScreen.this.settingsPanel.width, 13);
        }
    }

    private class SettingsPanel extends Panel {

        public SettingsPanel() {
            super(CreatePartyScreen.this);
        }

        @Override
        public void addWidgets() {
            this.add(new TextField(this).setMaxWidth(this.width - 6).setText(Component.translatable("ftbteams.gui.party_name")));
            this.add(CreatePartyScreen.this.nameTextBox = new TextBox(this) {

                @Override
                public void drawTextBox(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
                    NordColors.POLAR_NIGHT_0.draw(graphics, x, y, w, h);
                }
            });
            this.add(new VerticalSpaceWidget(this, 4));
            this.add(new TextField(this).setMaxWidth(this.width - 6).setText(Component.translatable("ftbteams.gui.party_description")));
            this.add(CreatePartyScreen.this.descriptionTextBox = new TextBox(this) {

                @Override
                public void drawTextBox(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
                    NordColors.POLAR_NIGHT_0.draw(graphics, x, y, w, h);
                }
            });
            this.add(new VerticalSpaceWidget(this, 4));
            CreatePartyScreen.this.nameTextBox.setHeight(14);
            CreatePartyScreen.this.nameTextBox.ghostText = Minecraft.getInstance().getUser().getName() + "'s Team";
            CreatePartyScreen.this.descriptionTextBox.setHeight(14);
            CreatePartyScreen.this.descriptionTextBox.ghostText = "<None>";
        }

        @Override
        public void alignWidgets() {
            for (Widget w : this.widgets) {
                w.setX(3);
                w.setWidth(this.width - 6);
            }
            this.align(new WidgetLayout.Vertical(3, 3, 10));
        }

        @Override
        public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            NordColors.POLAR_NIGHT_2.draw(graphics, x, y, w, h);
        }
    }
}