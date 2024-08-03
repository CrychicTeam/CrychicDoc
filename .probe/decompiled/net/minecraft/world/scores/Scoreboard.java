package net.minecraft.world.scores;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.logging.LogUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.slf4j.Logger;

public class Scoreboard {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final int DISPLAY_SLOT_LIST = 0;

    public static final int DISPLAY_SLOT_SIDEBAR = 1;

    public static final int DISPLAY_SLOT_BELOW_NAME = 2;

    public static final int DISPLAY_SLOT_TEAMS_SIDEBAR_START = 3;

    public static final int DISPLAY_SLOT_TEAMS_SIDEBAR_END = 18;

    public static final int DISPLAY_SLOTS = 19;

    private final Map<String, Objective> objectivesByName = Maps.newHashMap();

    private final Map<ObjectiveCriteria, List<Objective>> objectivesByCriteria = Maps.newHashMap();

    private final Map<String, Map<Objective, Score>> playerScores = Maps.newHashMap();

    private final Objective[] displayObjectives = new Objective[19];

    private final Map<String, PlayerTeam> teamsByName = Maps.newHashMap();

    private final Map<String, PlayerTeam> teamsByPlayer = Maps.newHashMap();

    @Nullable
    private static String[] displaySlotNames;

    public boolean hasObjective(String string0) {
        return this.objectivesByName.containsKey(string0);
    }

    public Objective getOrCreateObjective(String string0) {
        return (Objective) this.objectivesByName.get(string0);
    }

    @Nullable
    public Objective getObjective(@Nullable String string0) {
        return (Objective) this.objectivesByName.get(string0);
    }

    public Objective addObjective(String string0, ObjectiveCriteria objectiveCriteria1, Component component2, ObjectiveCriteria.RenderType objectiveCriteriaRenderType3) {
        if (this.objectivesByName.containsKey(string0)) {
            throw new IllegalArgumentException("An objective with the name '" + string0 + "' already exists!");
        } else {
            Objective $$4 = new Objective(this, string0, objectiveCriteria1, component2, objectiveCriteriaRenderType3);
            ((List) this.objectivesByCriteria.computeIfAbsent(objectiveCriteria1, p_83426_ -> Lists.newArrayList())).add($$4);
            this.objectivesByName.put(string0, $$4);
            this.onObjectiveAdded($$4);
            return $$4;
        }
    }

    public final void forAllObjectives(ObjectiveCriteria objectiveCriteria0, String string1, Consumer<Score> consumerScore2) {
        ((List) this.objectivesByCriteria.getOrDefault(objectiveCriteria0, Collections.emptyList())).forEach(p_83444_ -> consumerScore2.accept(this.getOrCreatePlayerScore(string1, p_83444_)));
    }

    public boolean hasPlayerScore(String string0, Objective objective1) {
        Map<Objective, Score> $$2 = (Map<Objective, Score>) this.playerScores.get(string0);
        if ($$2 == null) {
            return false;
        } else {
            Score $$3 = (Score) $$2.get(objective1);
            return $$3 != null;
        }
    }

    public Score getOrCreatePlayerScore(String string0, Objective objective1) {
        Map<Objective, Score> $$2 = (Map<Objective, Score>) this.playerScores.computeIfAbsent(string0, p_83507_ -> Maps.newHashMap());
        return (Score) $$2.computeIfAbsent(objective1, p_83487_ -> {
            Score $$2x = new Score(this, p_83487_, string0);
            $$2x.setScore(0);
            return $$2x;
        });
    }

    public Collection<Score> getPlayerScores(Objective objective0) {
        List<Score> $$1 = Lists.newArrayList();
        for (Map<Objective, Score> $$2 : this.playerScores.values()) {
            Score $$3 = (Score) $$2.get(objective0);
            if ($$3 != null) {
                $$1.add($$3);
            }
        }
        $$1.sort(Score.SCORE_COMPARATOR);
        return $$1;
    }

    public Collection<Objective> getObjectives() {
        return this.objectivesByName.values();
    }

    public Collection<String> getObjectiveNames() {
        return this.objectivesByName.keySet();
    }

    public Collection<String> getTrackedPlayers() {
        return Lists.newArrayList(this.playerScores.keySet());
    }

    public void resetPlayerScore(String string0, @Nullable Objective objective1) {
        if (objective1 == null) {
            Map<Objective, Score> $$2 = (Map<Objective, Score>) this.playerScores.remove(string0);
            if ($$2 != null) {
                this.onPlayerRemoved(string0);
            }
        } else {
            Map<Objective, Score> $$3 = (Map<Objective, Score>) this.playerScores.get(string0);
            if ($$3 != null) {
                Score $$4 = (Score) $$3.remove(objective1);
                if ($$3.size() < 1) {
                    Map<Objective, Score> $$5 = (Map<Objective, Score>) this.playerScores.remove(string0);
                    if ($$5 != null) {
                        this.onPlayerRemoved(string0);
                    }
                } else if ($$4 != null) {
                    this.onPlayerScoreRemoved(string0, objective1);
                }
            }
        }
    }

    public Map<Objective, Score> getPlayerScores(String string0) {
        Map<Objective, Score> $$1 = (Map<Objective, Score>) this.playerScores.get(string0);
        if ($$1 == null) {
            $$1 = Maps.newHashMap();
        }
        return $$1;
    }

    public void removeObjective(Objective objective0) {
        this.objectivesByName.remove(objective0.getName());
        for (int $$1 = 0; $$1 < 19; $$1++) {
            if (this.getDisplayObjective($$1) == objective0) {
                this.setDisplayObjective($$1, null);
            }
        }
        List<Objective> $$2 = (List<Objective>) this.objectivesByCriteria.get(objective0.getCriteria());
        if ($$2 != null) {
            $$2.remove(objective0);
        }
        for (Map<Objective, Score> $$3 : this.playerScores.values()) {
            $$3.remove(objective0);
        }
        this.onObjectiveRemoved(objective0);
    }

    public void setDisplayObjective(int int0, @Nullable Objective objective1) {
        this.displayObjectives[int0] = objective1;
    }

    @Nullable
    public Objective getDisplayObjective(int int0) {
        return this.displayObjectives[int0];
    }

    @Nullable
    public PlayerTeam getPlayerTeam(String string0) {
        return (PlayerTeam) this.teamsByName.get(string0);
    }

    public PlayerTeam addPlayerTeam(String string0) {
        PlayerTeam $$1 = this.getPlayerTeam(string0);
        if ($$1 != null) {
            LOGGER.warn("Requested creation of existing team '{}'", string0);
            return $$1;
        } else {
            $$1 = new PlayerTeam(this, string0);
            this.teamsByName.put(string0, $$1);
            this.onTeamAdded($$1);
            return $$1;
        }
    }

    public void removePlayerTeam(PlayerTeam playerTeam0) {
        this.teamsByName.remove(playerTeam0.getName());
        for (String $$1 : playerTeam0.getPlayers()) {
            this.teamsByPlayer.remove($$1);
        }
        this.onTeamRemoved(playerTeam0);
    }

    public boolean addPlayerToTeam(String string0, PlayerTeam playerTeam1) {
        if (this.getPlayersTeam(string0) != null) {
            this.removePlayerFromTeam(string0);
        }
        this.teamsByPlayer.put(string0, playerTeam1);
        return playerTeam1.getPlayers().add(string0);
    }

    public boolean removePlayerFromTeam(String string0) {
        PlayerTeam $$1 = this.getPlayersTeam(string0);
        if ($$1 != null) {
            this.removePlayerFromTeam(string0, $$1);
            return true;
        } else {
            return false;
        }
    }

    public void removePlayerFromTeam(String string0, PlayerTeam playerTeam1) {
        if (this.getPlayersTeam(string0) != playerTeam1) {
            throw new IllegalStateException("Player is either on another team or not on any team. Cannot remove from team '" + playerTeam1.getName() + "'.");
        } else {
            this.teamsByPlayer.remove(string0);
            playerTeam1.getPlayers().remove(string0);
        }
    }

    public Collection<String> getTeamNames() {
        return this.teamsByName.keySet();
    }

    public Collection<PlayerTeam> getPlayerTeams() {
        return this.teamsByName.values();
    }

    @Nullable
    public PlayerTeam getPlayersTeam(String string0) {
        return (PlayerTeam) this.teamsByPlayer.get(string0);
    }

    public void onObjectiveAdded(Objective objective0) {
    }

    public void onObjectiveChanged(Objective objective0) {
    }

    public void onObjectiveRemoved(Objective objective0) {
    }

    public void onScoreChanged(Score score0) {
    }

    public void onPlayerRemoved(String string0) {
    }

    public void onPlayerScoreRemoved(String string0, Objective objective1) {
    }

    public void onTeamAdded(PlayerTeam playerTeam0) {
    }

    public void onTeamChanged(PlayerTeam playerTeam0) {
    }

    public void onTeamRemoved(PlayerTeam playerTeam0) {
    }

    public static String getDisplaySlotName(int int0) {
        switch(int0) {
            case 0:
                return "list";
            case 1:
                return "sidebar";
            case 2:
                return "belowName";
            default:
                if (int0 >= 3 && int0 <= 18) {
                    ChatFormatting $$1 = ChatFormatting.getById(int0 - 3);
                    if ($$1 != null && $$1 != ChatFormatting.RESET) {
                        return "sidebar.team." + $$1.getName();
                    }
                }
                return null;
        }
    }

    public static int getDisplaySlotByName(String string0) {
        if ("list".equalsIgnoreCase(string0)) {
            return 0;
        } else if ("sidebar".equalsIgnoreCase(string0)) {
            return 1;
        } else if ("belowName".equalsIgnoreCase(string0)) {
            return 2;
        } else {
            if (string0.startsWith("sidebar.team.")) {
                String $$1 = string0.substring("sidebar.team.".length());
                ChatFormatting $$2 = ChatFormatting.getByName($$1);
                if ($$2 != null && $$2.getId() >= 0) {
                    return $$2.getId() + 3;
                }
            }
            return -1;
        }
    }

    public static String[] getDisplaySlotNames() {
        if (displaySlotNames == null) {
            displaySlotNames = new String[19];
            for (int $$0 = 0; $$0 < 19; $$0++) {
                displaySlotNames[$$0] = getDisplaySlotName($$0);
            }
        }
        return displaySlotNames;
    }

    public void entityRemoved(Entity entity0) {
        if (entity0 != null && !(entity0 instanceof Player) && !entity0.isAlive()) {
            String $$1 = entity0.getStringUUID();
            this.resetPlayerScore($$1, null);
            this.removePlayerFromTeam($$1);
        }
    }

    protected ListTag savePlayerScores() {
        ListTag $$0 = new ListTag();
        this.playerScores.values().stream().map(Map::values).forEach(p_83452_ -> p_83452_.stream().filter(p_166098_ -> p_166098_.getObjective() != null).forEach(p_166096_ -> {
            CompoundTag $$2 = new CompoundTag();
            $$2.putString("Name", p_166096_.getOwner());
            $$2.putString("Objective", p_166096_.getObjective().getName());
            $$2.putInt("Score", p_166096_.getScore());
            $$2.putBoolean("Locked", p_166096_.isLocked());
            $$0.add($$2);
        }));
        return $$0;
    }

    protected void loadPlayerScores(ListTag listTag0) {
        for (int $$1 = 0; $$1 < listTag0.size(); $$1++) {
            CompoundTag $$2 = listTag0.getCompound($$1);
            Objective $$3 = this.getOrCreateObjective($$2.getString("Objective"));
            String $$4 = $$2.getString("Name");
            Score $$5 = this.getOrCreatePlayerScore($$4, $$3);
            $$5.setScore($$2.getInt("Score"));
            if ($$2.contains("Locked")) {
                $$5.setLocked($$2.getBoolean("Locked"));
            }
        }
    }
}