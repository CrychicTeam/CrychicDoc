package dev.ftb.mods.ftbteams.data;

import java.util.UUID;
import java.util.function.BiFunction;
import net.minecraft.util.StringRepresentable;

public enum TeamType implements StringRepresentable {

    PLAYER("player", PlayerTeam::new), PARTY("party", PartyTeam::new), SERVER("server", ServerTeam::new);

    private final String name;

    private final BiFunction<TeamManagerImpl, UUID, AbstractTeam> factory;

    private TeamType(String n, BiFunction<TeamManagerImpl, UUID, AbstractTeam> f) {
        this.name = n;
        this.factory = f;
    }

    public AbstractTeam createTeam(TeamManagerImpl manager, UUID id) {
        return (AbstractTeam) this.factory.apply(manager, id);
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public boolean isPlayer() {
        return this == PLAYER;
    }

    public boolean isParty() {
        return this == PARTY;
    }

    public boolean isServer() {
        return this == SERVER;
    }
}