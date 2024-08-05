package io.github.lightman314.lightmanscurrency.client.gui.screen.team;

import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.client.gui.easy.EasyTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.TeamManagerScreen;
import io.github.lightman314.lightmanscurrency.common.teams.Team;
import io.github.lightman314.lightmanscurrency.network.message.teams.CPacketEditTeam;
import javax.annotation.Nonnull;
import net.minecraft.world.entity.player.Player;

public abstract class TeamTab extends EasyTab {

    protected final TeamManagerScreen screen;

    @Override
    public int getColor() {
        return 16777215;
    }

    protected final Player getPlayer() {
        return this.screen.getPlayer();
    }

    protected final Team getActiveTeam() {
        return this.screen.getActiveTeam();
    }

    protected TeamTab(TeamManagerScreen screen) {
        super(screen);
        this.screen = screen;
    }

    public abstract boolean allowViewing(Player var1, Team var2);

    protected final void RequestChange(@Nonnull LazyPacketData.Builder request) {
        Team team = this.getActiveTeam();
        if (team != null) {
            new CPacketEditTeam(team.getID(), request.build()).send();
        }
    }
}