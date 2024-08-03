package dev.ftb.mods.ftbteams.data;

import dev.ftb.mods.ftbteams.api.event.TeamEvent;
import java.util.UUID;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class ServerTeam extends AbstractTeam {

    public ServerTeam(TeamManagerImpl manager, UUID id) {
        super(manager, id);
    }

    @Override
    public TeamType getType() {
        return TeamType.SERVER;
    }

    @Override
    public boolean isServerTeam() {
        return true;
    }

    public int delete(CommandSourceStack source) {
        this.markDirty();
        this.invalidateTeam();
        this.manager.deleteTeam(this);
        this.manager.saveNow();
        this.manager.tryDeleteTeamFile(this.getId() + ".snbt", "server");
        this.manager.syncToAll(this);
        source.sendSuccess(() -> Component.translatable("ftbteams.message.deleted_server_team", this.getShortName()), true);
        TeamEvent.DELETED.invoker().accept(new TeamEvent(this));
        return 1;
    }
}