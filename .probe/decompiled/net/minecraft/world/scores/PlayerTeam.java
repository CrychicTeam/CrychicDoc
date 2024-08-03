package net.minecraft.world.scores;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

public class PlayerTeam extends Team {

    private static final int BIT_FRIENDLY_FIRE = 0;

    private static final int BIT_SEE_INVISIBLES = 1;

    private final Scoreboard scoreboard;

    private final String name;

    private final Set<String> players = Sets.newHashSet();

    private Component displayName;

    private Component playerPrefix = CommonComponents.EMPTY;

    private Component playerSuffix = CommonComponents.EMPTY;

    private boolean allowFriendlyFire = true;

    private boolean seeFriendlyInvisibles = true;

    private Team.Visibility nameTagVisibility = Team.Visibility.ALWAYS;

    private Team.Visibility deathMessageVisibility = Team.Visibility.ALWAYS;

    private ChatFormatting color = ChatFormatting.RESET;

    private Team.CollisionRule collisionRule = Team.CollisionRule.ALWAYS;

    private final Style displayNameStyle;

    public PlayerTeam(Scoreboard scoreboard0, String string1) {
        this.scoreboard = scoreboard0;
        this.name = string1;
        this.displayName = Component.literal(string1);
        this.displayNameStyle = Style.EMPTY.withInsertion(string1).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal(string1)));
    }

    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public Component getDisplayName() {
        return this.displayName;
    }

    public MutableComponent getFormattedDisplayName() {
        MutableComponent $$0 = ComponentUtils.wrapInSquareBrackets(this.displayName.copy().withStyle(this.displayNameStyle));
        ChatFormatting $$1 = this.getColor();
        if ($$1 != ChatFormatting.RESET) {
            $$0.withStyle($$1);
        }
        return $$0;
    }

    public void setDisplayName(Component component0) {
        if (component0 == null) {
            throw new IllegalArgumentException("Name cannot be null");
        } else {
            this.displayName = component0;
            this.scoreboard.onTeamChanged(this);
        }
    }

    public void setPlayerPrefix(@Nullable Component component0) {
        this.playerPrefix = component0 == null ? CommonComponents.EMPTY : component0;
        this.scoreboard.onTeamChanged(this);
    }

    public Component getPlayerPrefix() {
        return this.playerPrefix;
    }

    public void setPlayerSuffix(@Nullable Component component0) {
        this.playerSuffix = component0 == null ? CommonComponents.EMPTY : component0;
        this.scoreboard.onTeamChanged(this);
    }

    public Component getPlayerSuffix() {
        return this.playerSuffix;
    }

    @Override
    public Collection<String> getPlayers() {
        return this.players;
    }

    @Override
    public MutableComponent getFormattedName(Component component0) {
        MutableComponent $$1 = Component.empty().append(this.playerPrefix).append(component0).append(this.playerSuffix);
        ChatFormatting $$2 = this.getColor();
        if ($$2 != ChatFormatting.RESET) {
            $$1.withStyle($$2);
        }
        return $$1;
    }

    public static MutableComponent formatNameForTeam(@Nullable Team team0, Component component1) {
        return team0 == null ? component1.copy() : team0.getFormattedName(component1);
    }

    @Override
    public boolean isAllowFriendlyFire() {
        return this.allowFriendlyFire;
    }

    public void setAllowFriendlyFire(boolean boolean0) {
        this.allowFriendlyFire = boolean0;
        this.scoreboard.onTeamChanged(this);
    }

    @Override
    public boolean canSeeFriendlyInvisibles() {
        return this.seeFriendlyInvisibles;
    }

    public void setSeeFriendlyInvisibles(boolean boolean0) {
        this.seeFriendlyInvisibles = boolean0;
        this.scoreboard.onTeamChanged(this);
    }

    @Override
    public Team.Visibility getNameTagVisibility() {
        return this.nameTagVisibility;
    }

    @Override
    public Team.Visibility getDeathMessageVisibility() {
        return this.deathMessageVisibility;
    }

    public void setNameTagVisibility(Team.Visibility teamVisibility0) {
        this.nameTagVisibility = teamVisibility0;
        this.scoreboard.onTeamChanged(this);
    }

    public void setDeathMessageVisibility(Team.Visibility teamVisibility0) {
        this.deathMessageVisibility = teamVisibility0;
        this.scoreboard.onTeamChanged(this);
    }

    @Override
    public Team.CollisionRule getCollisionRule() {
        return this.collisionRule;
    }

    public void setCollisionRule(Team.CollisionRule teamCollisionRule0) {
        this.collisionRule = teamCollisionRule0;
        this.scoreboard.onTeamChanged(this);
    }

    public int packOptions() {
        int $$0 = 0;
        if (this.isAllowFriendlyFire()) {
            $$0 |= 1;
        }
        if (this.canSeeFriendlyInvisibles()) {
            $$0 |= 2;
        }
        return $$0;
    }

    public void unpackOptions(int int0) {
        this.setAllowFriendlyFire((int0 & 1) > 0);
        this.setSeeFriendlyInvisibles((int0 & 2) > 0);
    }

    public void setColor(ChatFormatting chatFormatting0) {
        this.color = chatFormatting0;
        this.scoreboard.onTeamChanged(this);
    }

    @Override
    public ChatFormatting getColor() {
        return this.color;
    }
}