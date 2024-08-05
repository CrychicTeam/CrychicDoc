package dev.ftb.mods.ftbteams.client.gui;

import com.mojang.authlib.GameProfile;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.FaceIcon;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.ui.ContextMenuItem;
import dev.ftb.mods.ftblibrary.ui.NordButton;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftbteams.api.TeamRank;
import dev.ftb.mods.ftbteams.api.client.KnownClientPlayer;
import dev.ftb.mods.ftbteams.data.ClientTeam;
import dev.ftb.mods.ftbteams.data.ClientTeamManagerImpl;
import dev.ftb.mods.ftbteams.data.TeamType;
import dev.ftb.mods.ftbteams.net.PlayerGUIOperationMessage;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class MemberButton extends NordButton {

    private final KnownClientPlayer player;

    MemberButton(Panel panel, KnownClientPlayer p) {
        super(panel, Component.literal(p.name()), FaceIcon.getFace(p.profile()));
        this.setWidth(this.width + 18);
        this.player = p;
    }

    @Override
    public void drawIcon(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        super.drawIcon(graphics, theme, x, y, w, h);
        if (this.player.online()) {
            graphics.pose().pushPose();
            graphics.pose().translate((double) (x + w) - 1.5, (double) y - 0.5, 0.0);
            Color4I.GREEN.draw(graphics, 0, 0, 2, 2);
            graphics.pose().popPose();
        }
        ClientTeam selfTeam = ClientTeamManagerImpl.getInstance().selfTeam();
        if (selfTeam.getType() == TeamType.PARTY) {
            TeamRank tr = selfTeam.getRankForPlayer(this.player.id());
            tr.getIcon().ifPresent(icon -> icon.draw(graphics, this.getX() + this.width - 14, this.getY() + 2, 12, 12));
        }
    }

    @Override
    public void onClicked(MouseButton button) {
        KnownClientPlayer self = ClientTeamManagerImpl.getInstance().self();
        ClientTeam selfTeam = ClientTeamManagerImpl.getInstance().selfTeam();
        if (selfTeam != null && self != null) {
            TeamRank selfRank = selfTeam.getRankForPlayer(self.id());
            TeamRank playerRank = selfTeam.getRankForPlayer(this.player.id());
            if (selfTeam.getType() == TeamType.PARTY) {
                List<ContextMenuItem> items0 = new ArrayList();
                if (this.player.id().equals(self.id())) {
                    if (selfRank.isAtLeast(TeamRank.OWNER)) {
                        if (selfTeam.getMembers().size() == 1) {
                            items0.add(new ContextMenuItem(Component.translatable("ftbteams.gui.disband"), Icons.CLOSE, b -> PlayerGUIOperationMessage.Operation.LEAVE.sendMessage(this.player)).setYesNoText(Component.translatable("ftbteams.gui.disband.confirm")));
                        }
                    } else {
                        items0.add(new ContextMenuItem(Component.translatable("ftbteams.gui.leave"), Icons.CLOSE, b -> PlayerGUIOperationMessage.Operation.LEAVE.sendMessage(this.player)).setYesNoText(Component.translatable("ftbteams.gui.leave.confirm")));
                    }
                } else if (selfRank.isAtLeast(TeamRank.OWNER)) {
                    if (playerRank == TeamRank.MEMBER) {
                        items0.add(new ContextMenuItem(Component.translatable("ftbteams.gui.promote", this.player.name()), Icons.SHIELD, b -> PlayerGUIOperationMessage.Operation.PROMOTE.sendMessage(this.player)).setYesNoText(Component.translatable("ftbteams.gui.promote.confirm", this.player.name())));
                    } else if (playerRank == TeamRank.OFFICER) {
                        items0.add(new ContextMenuItem(Component.translatable("ftbteams.gui.demote", this.player.name()), Icons.ACCEPT_GRAY, b -> PlayerGUIOperationMessage.Operation.DEMOTE.sendMessage(this.player)).setYesNoText(Component.translatable("ftbteams.gui.demote.confirm", this.player.name())));
                    }
                    if (playerRank.isMemberOrBetter()) {
                        items0.add(new ContextMenuItem(Component.translatable("ftbteams.gui.transfer_ownership", this.player.name()), Icons.DIAMOND, b -> PlayerGUIOperationMessage.Operation.TRANSFER_OWNER.sendMessage(this.player)).setYesNoText(Component.translatable("ftbteams.gui.transfer_ownership.confirm", this.player.name())));
                    }
                }
                if (selfRank.getPower() > playerRank.getPower()) {
                    if (playerRank.isMemberOrBetter()) {
                        items0.add(new ContextMenuItem(Component.translatable("ftbteams.gui.kick", this.player.name()), Icons.CLOSE, b -> PlayerGUIOperationMessage.Operation.KICK.sendMessage(this.player)).setYesNoText(Component.translatable("ftbteams.gui.kick.confirm", this.player.name())));
                    } else if (selfRank.isOfficerOrBetter() && playerRank.isAllyOrBetter()) {
                        items0.add(new ContextMenuItem(Component.translatable("ftbteams.gui.remove_ally", this.player.name()), Icons.CANCEL, b -> PlayerGUIOperationMessage.Operation.REMOVE_ALLY.sendMessage(this.player)).setYesNoText(Component.translatable("ftbteams.gui.remove_ally.confirm", this.player.name())));
                    }
                }
                if (!items0.isEmpty()) {
                    List<ContextMenuItem> items = new ArrayList(List.of(new ContextMenuItem(playerRank.getDisplayName(), FaceIcon.getFace(new GameProfile(this.player.id(), null)), null).setCloseMenu(false), ContextMenuItem.SEPARATOR));
                    items.addAll(items0);
                    this.getGui().openContextMenu(items);
                }
            }
        }
    }
}