package noppes.npcs.api.wrapper;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.IScoreboardTeam;

public class ScoreboardTeamWrapper implements IScoreboardTeam {

    private PlayerTeam team;

    private Scoreboard board;

    protected ScoreboardTeamWrapper(PlayerTeam team, Scoreboard board) {
        this.team = team;
        this.board = board;
    }

    @Override
    public String getName() {
        return this.team.getName();
    }

    @Override
    public String getDisplayName() {
        return this.team.getDisplayName().getString();
    }

    @Override
    public void setDisplayName(String name) {
        if (name.length() > 0 && name.length() <= 32) {
            this.team.setDisplayName(Component.translatable(name));
        } else {
            throw new CustomNPCsException("Score team display name must be between 1-32 characters: %s", name);
        }
    }

    @Override
    public void addPlayer(String player) {
        this.board.addPlayerToTeam(player, this.team);
    }

    @Override
    public void removePlayer(String player) {
        this.board.removePlayerFromTeam(player, this.team);
    }

    @Override
    public String[] getPlayers() {
        List<String> list = new ArrayList(this.team.getPlayers());
        return (String[]) list.toArray(new String[list.size()]);
    }

    @Override
    public void clearPlayers() {
        for (String player : new ArrayList(this.team.getPlayers())) {
            this.board.removePlayerFromTeam(player, this.team);
        }
    }

    @Override
    public boolean getFriendlyFire() {
        return this.team.isAllowFriendlyFire();
    }

    @Override
    public void setFriendlyFire(boolean bo) {
        this.team.setAllowFriendlyFire(bo);
    }

    @Override
    public void setColor(String color) {
        ChatFormatting enumchatformatting = ChatFormatting.getByName(color);
        if (enumchatformatting != null && !enumchatformatting.isFormat()) {
            this.team.setPlayerPrefix(Component.literal(enumchatformatting.toString()));
            this.team.setPlayerSuffix(Component.literal(ChatFormatting.RESET.toString()));
        } else {
            throw new CustomNPCsException("Not a proper color name: %s", color);
        }
    }

    @Override
    public String getColor() {
        Component prefix = this.team.getPlayerPrefix();
        if (prefix != null && !prefix.getString().isEmpty()) {
            for (ChatFormatting format : ChatFormatting.values()) {
                if (prefix.getString().equals(format.toString()) && format != ChatFormatting.RESET) {
                    return format.getName();
                }
            }
            return null;
        } else {
            return null;
        }
    }

    @Override
    public void setSeeInvisibleTeamPlayers(boolean bo) {
        this.team.setSeeFriendlyInvisibles(bo);
    }

    @Override
    public boolean getSeeInvisibleTeamPlayers() {
        return this.team.canSeeFriendlyInvisibles();
    }

    @Override
    public boolean hasPlayer(String player) {
        return this.board.getPlayersTeam(player) != null;
    }
}