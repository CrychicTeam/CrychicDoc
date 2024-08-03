package dev.ftb.mods.ftbteams.api;

import dev.ftb.mods.ftblibrary.config.NameMap;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.Icons;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;

public enum TeamRank implements StringRepresentable {

    ENEMY("enemy", -100),
    NONE("none", 0),
    ALLY("ally", 50, Icons.FRIENDS),
    INVITED("invited", 75),
    MEMBER("member", 100, Icons.ACCEPT_GRAY),
    OFFICER("officer", 500, Icons.SHIELD),
    OWNER("owner", 1000, Icons.DIAMOND);

    public static final NameMap<TeamRank> NAME_MAP = NameMap.of(NONE, values()).create();

    private final String name;

    private final int power;

    private final Icon icon;

    private TeamRank(String name, int power, Icon icon) {
        this.name = name;
        this.power = power;
        this.icon = icon;
    }

    private TeamRank(String name, int power) {
        this(name, power, null);
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public int getPower() {
        return this.power;
    }

    public boolean isAtLeast(TeamRank rank) {
        return rank.power >= 0 ? this.power >= rank.power : this.power <= rank.power;
    }

    public boolean isEnemyOrWorse() {
        return this.isAtLeast(ENEMY);
    }

    public boolean isNoneOrBetter() {
        return this.isAtLeast(NONE);
    }

    public boolean isAllyOrBetter() {
        return this.isAtLeast(ALLY);
    }

    public boolean isInvitedOrBetter() {
        return this.isAtLeast(INVITED);
    }

    public boolean isMemberOrBetter() {
        return this.isAtLeast(MEMBER);
    }

    public boolean isOfficerOrBetter() {
        return this.isAtLeast(OFFICER);
    }

    public boolean isOwner() {
        return this.isAtLeast(OWNER);
    }

    public Optional<Icon> getIcon() {
        return Optional.ofNullable(this.icon);
    }

    public Component getDisplayName() {
        return Component.translatable("ftbteams.ranks." + this.name);
    }
}