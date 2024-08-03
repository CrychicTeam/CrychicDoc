package noppes.npcs.controllers.data;

import java.util.HashSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.Objective;
import noppes.npcs.CustomNpcs;
import noppes.npcs.ICompatibilty;
import noppes.npcs.VersionCompatibility;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.handler.data.IAvailability;
import noppes.npcs.constants.EnumAvailabilityDialog;
import noppes.npcs.constants.EnumAvailabilityFaction;
import noppes.npcs.constants.EnumAvailabilityFactionType;
import noppes.npcs.constants.EnumAvailabilityQuest;
import noppes.npcs.constants.EnumAvailabilityScoreboard;
import noppes.npcs.constants.EnumDayTime;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.controllers.PlayerQuestController;

public class Availability implements ICompatibilty, IAvailability {

    public static HashSet<String> scores = new HashSet();

    public int version = VersionCompatibility.ModRev;

    public EnumAvailabilityDialog dialogAvailable = EnumAvailabilityDialog.Always;

    public EnumAvailabilityDialog dialog2Available = EnumAvailabilityDialog.Always;

    public EnumAvailabilityDialog dialog3Available = EnumAvailabilityDialog.Always;

    public EnumAvailabilityDialog dialog4Available = EnumAvailabilityDialog.Always;

    public int dialogId = -1;

    public int dialog2Id = -1;

    public int dialog3Id = -1;

    public int dialog4Id = -1;

    public EnumAvailabilityQuest questAvailable = EnumAvailabilityQuest.Always;

    public EnumAvailabilityQuest quest2Available = EnumAvailabilityQuest.Always;

    public EnumAvailabilityQuest quest3Available = EnumAvailabilityQuest.Always;

    public EnumAvailabilityQuest quest4Available = EnumAvailabilityQuest.Always;

    public int questId = -1;

    public int quest2Id = -1;

    public int quest3Id = -1;

    public int quest4Id = -1;

    public EnumDayTime daytime = EnumDayTime.Always;

    public int factionId = -1;

    public int faction2Id = -1;

    public EnumAvailabilityFactionType factionAvailable = EnumAvailabilityFactionType.Always;

    public EnumAvailabilityFactionType faction2Available = EnumAvailabilityFactionType.Always;

    public EnumAvailabilityFaction factionStance = EnumAvailabilityFaction.Friendly;

    public EnumAvailabilityFaction faction2Stance = EnumAvailabilityFaction.Friendly;

    public EnumAvailabilityScoreboard scoreboardType = EnumAvailabilityScoreboard.EQUAL;

    public EnumAvailabilityScoreboard scoreboard2Type = EnumAvailabilityScoreboard.EQUAL;

    public String scoreboardObjective = "";

    public String scoreboard2Objective = "";

    public int scoreboardValue = 1;

    public int scoreboard2Value = 1;

    public int minPlayerLevel = 0;

    private boolean hasOptions = false;

    public void load(CompoundTag compound) {
        this.version = compound.getInt("ModRev");
        VersionCompatibility.CheckAvailabilityCompatibility(this, compound);
        this.dialogAvailable = EnumAvailabilityDialog.values()[compound.getInt("AvailabilityDialog")];
        this.dialog2Available = EnumAvailabilityDialog.values()[compound.getInt("AvailabilityDialog2")];
        this.dialog3Available = EnumAvailabilityDialog.values()[compound.getInt("AvailabilityDialog3")];
        this.dialog4Available = EnumAvailabilityDialog.values()[compound.getInt("AvailabilityDialog4")];
        this.dialogId = compound.getInt("AvailabilityDialogId");
        this.dialog2Id = compound.getInt("AvailabilityDialog2Id");
        this.dialog3Id = compound.getInt("AvailabilityDialog3Id");
        this.dialog4Id = compound.getInt("AvailabilityDialog4Id");
        this.questAvailable = EnumAvailabilityQuest.values()[compound.getInt("AvailabilityQuest")];
        this.quest2Available = EnumAvailabilityQuest.values()[compound.getInt("AvailabilityQuest2")];
        this.quest3Available = EnumAvailabilityQuest.values()[compound.getInt("AvailabilityQuest3")];
        this.quest4Available = EnumAvailabilityQuest.values()[compound.getInt("AvailabilityQuest4")];
        this.questId = compound.getInt("AvailabilityQuestId");
        this.quest2Id = compound.getInt("AvailabilityQuest2Id");
        this.quest3Id = compound.getInt("AvailabilityQuest3Id");
        this.quest4Id = compound.getInt("AvailabilityQuest4Id");
        this.setFactionAvailability(compound.getInt("AvailabilityFaction"));
        this.setFactionAvailabilityStance(compound.getInt("AvailabilityFactionStance"));
        this.setFaction2Availability(compound.getInt("AvailabilityFaction2"));
        this.setFaction2AvailabilityStance(compound.getInt("AvailabilityFaction2Stance"));
        this.factionId = compound.getInt("AvailabilityFactionId");
        this.faction2Id = compound.getInt("AvailabilityFaction2Id");
        this.scoreboardObjective = compound.getString("AvailabilityScoreboardObjective");
        this.scoreboard2Objective = compound.getString("AvailabilityScoreboard2Objective");
        this.initScore(this.scoreboardObjective);
        this.initScore(this.scoreboard2Objective);
        this.scoreboardType = EnumAvailabilityScoreboard.values()[compound.getInt("AvailabilityScoreboardType")];
        this.scoreboard2Type = EnumAvailabilityScoreboard.values()[compound.getInt("AvailabilityScoreboard2Type")];
        this.scoreboardValue = compound.getInt("AvailabilityScoreboardValue");
        this.scoreboard2Value = compound.getInt("AvailabilityScoreboard2Value");
        this.daytime = EnumDayTime.values()[compound.getInt("AvailabilityDayTime")];
        this.minPlayerLevel = compound.getInt("AvailabilityMinPlayerLevel");
        this.hasOptions = this.checkHasOptions();
    }

    private void initScore(String objective) {
        if (!objective.isEmpty() && !scores.contains(objective)) {
            scores.add(objective);
            if (CustomNpcs.Server != null) {
                for (ServerLevel level : CustomNpcs.Server.getAllLevels()) {
                    ServerScoreboard board = level.getScoreboard();
                    Objective so = board.m_83477_(objective);
                    if (so != null) {
                        board.startTrackingObjective(so);
                    }
                }
            }
        }
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        compound.putInt("ModRev", this.version);
        compound.putInt("AvailabilityDialog", this.dialogAvailable.ordinal());
        compound.putInt("AvailabilityDialog2", this.dialog2Available.ordinal());
        compound.putInt("AvailabilityDialog3", this.dialog3Available.ordinal());
        compound.putInt("AvailabilityDialog4", this.dialog4Available.ordinal());
        compound.putInt("AvailabilityDialogId", this.dialogId);
        compound.putInt("AvailabilityDialog2Id", this.dialog2Id);
        compound.putInt("AvailabilityDialog3Id", this.dialog3Id);
        compound.putInt("AvailabilityDialog4Id", this.dialog4Id);
        compound.putInt("AvailabilityQuest", this.questAvailable.ordinal());
        compound.putInt("AvailabilityQuest2", this.quest2Available.ordinal());
        compound.putInt("AvailabilityQuest3", this.quest3Available.ordinal());
        compound.putInt("AvailabilityQuest4", this.quest4Available.ordinal());
        compound.putInt("AvailabilityQuestId", this.questId);
        compound.putInt("AvailabilityQuest2Id", this.quest2Id);
        compound.putInt("AvailabilityQuest3Id", this.quest3Id);
        compound.putInt("AvailabilityQuest4Id", this.quest4Id);
        compound.putInt("AvailabilityFaction", this.factionAvailable.ordinal());
        compound.putInt("AvailabilityFaction2", this.faction2Available.ordinal());
        compound.putInt("AvailabilityFactionStance", this.factionStance.ordinal());
        compound.putInt("AvailabilityFaction2Stance", this.faction2Stance.ordinal());
        compound.putInt("AvailabilityFactionId", this.factionId);
        compound.putInt("AvailabilityFaction2Id", this.faction2Id);
        compound.putString("AvailabilityScoreboardObjective", this.scoreboardObjective);
        compound.putString("AvailabilityScoreboard2Objective", this.scoreboard2Objective);
        compound.putInt("AvailabilityScoreboardType", this.scoreboardType.ordinal());
        compound.putInt("AvailabilityScoreboard2Type", this.scoreboard2Type.ordinal());
        compound.putInt("AvailabilityScoreboardValue", this.scoreboardValue);
        compound.putInt("AvailabilityScoreboard2Value", this.scoreboard2Value);
        compound.putInt("AvailabilityDayTime", this.daytime.ordinal());
        compound.putInt("AvailabilityMinPlayerLevel", this.minPlayerLevel);
        return compound;
    }

    public void setFactionAvailability(int value) {
        this.factionAvailable = EnumAvailabilityFactionType.values()[value];
        this.hasOptions = this.checkHasOptions();
    }

    public void setFaction2Availability(int value) {
        this.faction2Available = EnumAvailabilityFactionType.values()[value];
        this.hasOptions = this.checkHasOptions();
    }

    public void setFactionAvailabilityStance(int integer) {
        this.factionStance = EnumAvailabilityFaction.values()[integer];
    }

    public void setFaction2AvailabilityStance(int integer) {
        this.faction2Stance = EnumAvailabilityFaction.values()[integer];
    }

    public boolean isAvailable(Player player) {
        if (this.daytime == EnumDayTime.Day) {
            long time = player.m_9236_().getDayTime() % 24000L;
            if (time > 12000L) {
                return false;
            }
        }
        if (this.daytime == EnumDayTime.Night) {
            long time = player.m_9236_().getDayTime() % 24000L;
            if (time < 12000L) {
                return false;
            }
        }
        if (!this.dialogAvailable(this.dialogId, this.dialogAvailable, player)) {
            return false;
        } else if (!this.dialogAvailable(this.dialog2Id, this.dialog2Available, player)) {
            return false;
        } else if (!this.dialogAvailable(this.dialog3Id, this.dialog3Available, player)) {
            return false;
        } else if (!this.dialogAvailable(this.dialog4Id, this.dialog4Available, player)) {
            return false;
        } else if (!this.questAvailable(this.questId, this.questAvailable, player)) {
            return false;
        } else if (!this.questAvailable(this.quest2Id, this.quest2Available, player)) {
            return false;
        } else if (!this.questAvailable(this.quest3Id, this.quest3Available, player)) {
            return false;
        } else if (!this.questAvailable(this.quest4Id, this.quest4Available, player)) {
            return false;
        } else if (!this.factionAvailable(this.factionId, this.factionStance, this.factionAvailable, player)) {
            return false;
        } else if (!this.factionAvailable(this.faction2Id, this.faction2Stance, this.faction2Available, player)) {
            return false;
        } else if (!this.scoreboardAvailable(player, this.scoreboardObjective, this.scoreboardType, this.scoreboardValue)) {
            return false;
        } else {
            return !this.scoreboardAvailable(player, this.scoreboard2Objective, this.scoreboard2Type, this.scoreboard2Value) ? false : player.experienceLevel >= this.minPlayerLevel;
        }
    }

    private boolean scoreboardAvailable(Player player, String objective, EnumAvailabilityScoreboard type, int value) {
        if (objective.isEmpty()) {
            return true;
        } else {
            Objective sbObjective = player.getScoreboard().getObjective(objective);
            if (sbObjective == null) {
                return false;
            } else if (!player.getScoreboard().hasPlayerScore(player.getName().getString(), sbObjective)) {
                return false;
            } else {
                int i = player.getScoreboard().getOrCreatePlayerScore(player.getName().getString(), sbObjective).getScore();
                if (type == EnumAvailabilityScoreboard.EQUAL) {
                    return i == value;
                } else {
                    return type == EnumAvailabilityScoreboard.BIGGER ? i > value : i < value;
                }
            }
        }
    }

    private boolean factionAvailable(int id, EnumAvailabilityFaction stance, EnumAvailabilityFactionType available, Player player) {
        if (available == EnumAvailabilityFactionType.Always) {
            return true;
        } else {
            Faction faction = FactionController.instance.getFaction(id);
            if (faction == null) {
                return true;
            } else {
                PlayerFactionData data = PlayerData.get(player).factionData;
                int points = data.getFactionPoints(player, id);
                EnumAvailabilityFaction current = EnumAvailabilityFaction.Neutral;
                if (points < faction.neutralPoints) {
                    current = EnumAvailabilityFaction.Hostile;
                }
                if (points >= faction.friendlyPoints) {
                    current = EnumAvailabilityFaction.Friendly;
                }
                return available == EnumAvailabilityFactionType.Is && stance == current ? true : available == EnumAvailabilityFactionType.IsNot && stance != current;
            }
        }
    }

    public boolean dialogAvailable(int id, EnumAvailabilityDialog en, Player player) {
        if (en == EnumAvailabilityDialog.Always) {
            return true;
        } else {
            boolean hasRead = PlayerData.get(player).dialogData.dialogsRead.contains(id);
            return hasRead && en == EnumAvailabilityDialog.After ? true : !hasRead && en == EnumAvailabilityDialog.Before;
        }
    }

    public boolean questAvailable(int id, EnumAvailabilityQuest en, Player player) {
        if (en == EnumAvailabilityQuest.Always) {
            return true;
        } else if (en == EnumAvailabilityQuest.After && PlayerQuestController.isQuestFinished(player, id)) {
            return true;
        } else if (en == EnumAvailabilityQuest.Before && !PlayerQuestController.isQuestFinished(player, id)) {
            return true;
        } else if (en == EnumAvailabilityQuest.Active && PlayerQuestController.isQuestActive(player, id)) {
            return true;
        } else if (en == EnumAvailabilityQuest.NotActive && !PlayerQuestController.isQuestActive(player, id)) {
            return true;
        } else {
            return en == EnumAvailabilityQuest.Completed && PlayerQuestController.isQuestCompleted(player, id) ? true : en == EnumAvailabilityQuest.CanStart && PlayerQuestController.canQuestBeAccepted(player, id);
        }
    }

    @Override
    public int getVersion() {
        return this.version;
    }

    @Override
    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public boolean isAvailable(IPlayer player) {
        return this.isAvailable(player.getMCEntity());
    }

    @Override
    public int getDaytime() {
        return this.daytime.ordinal();
    }

    @Override
    public void setDaytime(int type) {
        this.daytime = EnumDayTime.values()[Mth.clamp(type, 0, 2)];
        this.hasOptions = this.checkHasOptions();
    }

    @Override
    public int getMinPlayerLevel() {
        return this.minPlayerLevel;
    }

    @Override
    public void setMinPlayerLevel(int level) {
        this.minPlayerLevel = level;
        this.hasOptions = this.checkHasOptions();
    }

    @Override
    public int getDialog(int i) {
        if (i < 0 && i > 3) {
            throw new CustomNPCsException(i + " isnt between 0 and 3");
        } else if (i == 0) {
            return this.dialogId;
        } else if (i == 1) {
            return this.dialog2Id;
        } else {
            return i == 2 ? this.dialog3Id : this.dialog4Id;
        }
    }

    @Override
    public void setDialog(int i, int id, int type) {
        if (i < 0 && i > 3) {
            throw new CustomNPCsException(i + " isnt between 0 and 3");
        } else {
            EnumAvailabilityDialog e = EnumAvailabilityDialog.values()[Mth.clamp(type, 0, 2)];
            if (i == 0) {
                this.dialogId = id;
                this.dialogAvailable = e;
            } else if (i == 1) {
                this.dialog2Id = id;
                this.dialog2Available = e;
            } else if (i == 2) {
                this.dialog3Id = id;
                this.dialog3Available = e;
            } else if (i == 3) {
                this.dialog4Id = id;
                this.dialog4Available = e;
            }
            this.hasOptions = this.checkHasOptions();
        }
    }

    @Override
    public void removeDialog(int i) {
        if (i < 0 && i > 3) {
            throw new CustomNPCsException(i + " isnt between 0 and 3");
        } else {
            if (i == 0) {
                this.dialogId = -1;
                this.dialogAvailable = EnumAvailabilityDialog.Always;
            } else if (i == 1) {
                this.dialog2Id = -1;
                this.dialog2Available = EnumAvailabilityDialog.Always;
            } else if (i == 2) {
                this.dialog3Id = -1;
                this.dialog3Available = EnumAvailabilityDialog.Always;
            } else if (i == 3) {
                this.dialog4Id = -1;
                this.dialog4Available = EnumAvailabilityDialog.Always;
            }
            this.hasOptions = this.checkHasOptions();
        }
    }

    @Override
    public int getQuest(int i) {
        if (i < 0 && i > 3) {
            throw new CustomNPCsException(i + " isnt between 0 and 3");
        } else if (i == 0) {
            return this.questId;
        } else if (i == 1) {
            return this.quest2Id;
        } else {
            return i == 2 ? this.quest3Id : this.quest4Id;
        }
    }

    @Override
    public void setQuest(int i, int id, int type) {
        if (i < 0 && i > 3) {
            throw new CustomNPCsException(i + " isnt between 0 and 3");
        } else {
            EnumAvailabilityQuest e = EnumAvailabilityQuest.values()[Mth.clamp(type, 0, 5)];
            if (i == 0) {
                this.questId = id;
                this.questAvailable = e;
            } else if (i == 1) {
                this.quest2Id = id;
                this.quest2Available = e;
            } else if (i == 2) {
                this.quest3Id = id;
                this.quest3Available = e;
            } else if (i == 3) {
                this.quest4Id = id;
                this.quest4Available = e;
            }
            this.hasOptions = this.checkHasOptions();
        }
    }

    @Override
    public void removeQuest(int i) {
        if (i < 0 && i > 3) {
            throw new CustomNPCsException(i + " isnt between 0 and 3");
        } else {
            if (i == 0) {
                this.questId = -1;
                this.questAvailable = EnumAvailabilityQuest.Always;
            } else if (i == 1) {
                this.quest2Id = -1;
                this.quest2Available = EnumAvailabilityQuest.Always;
            } else if (i == 2) {
                this.quest3Id = -1;
                this.quest3Available = EnumAvailabilityQuest.Always;
            } else if (i == 3) {
                this.quest4Id = -1;
                this.quest4Available = EnumAvailabilityQuest.Always;
            }
            this.hasOptions = this.checkHasOptions();
        }
    }

    @Override
    public void setFaction(int i, int id, int type, int stance) {
        if (i < 0 && i > 1) {
            throw new CustomNPCsException(i + " isnt between 0 and 1");
        } else {
            EnumAvailabilityFactionType e = EnumAvailabilityFactionType.values()[Mth.clamp(type, 0, 2)];
            EnumAvailabilityFaction ee = EnumAvailabilityFaction.values()[Mth.clamp(stance, 0, 2)];
            if (i == 0) {
                this.factionId = id;
                this.factionAvailable = e;
                this.factionStance = ee;
            } else if (i == 1) {
                this.faction2Id = id;
                this.faction2Available = e;
                this.faction2Stance = ee;
            }
            this.hasOptions = this.checkHasOptions();
        }
    }

    @Override
    public void setScoreboard(int i, String objective, int type, int value) {
        if (i < 0 && i > 1) {
            throw new CustomNPCsException(i + " isnt between 0 and 1");
        } else {
            if (objective == null) {
                objective = "";
            }
            EnumAvailabilityScoreboard e = EnumAvailabilityScoreboard.values()[Mth.clamp(type, 0, 2)];
            if (i == 0) {
                this.scoreboardObjective = objective;
                this.scoreboardType = e;
                this.scoreboardValue = value;
            } else if (i == 1) {
                this.scoreboard2Objective = objective;
                this.scoreboard2Type = e;
                this.scoreboard2Value = value;
            }
            this.hasOptions = this.checkHasOptions();
        }
    }

    @Override
    public void removeFaction(int i) {
        if (i < 0 && i > 1) {
            throw new CustomNPCsException(i + " isnt between 0 and 1");
        } else {
            if (i == 0) {
                this.factionId = -1;
                this.factionAvailable = EnumAvailabilityFactionType.Always;
                this.factionStance = EnumAvailabilityFaction.Friendly;
            } else if (i == 1) {
                this.faction2Id = -1;
                this.faction2Available = EnumAvailabilityFactionType.Always;
                this.faction2Stance = EnumAvailabilityFaction.Friendly;
            }
            this.hasOptions = this.checkHasOptions();
        }
    }

    private boolean checkHasOptions() {
        if (this.dialogAvailable != EnumAvailabilityDialog.Always || this.dialog2Available != EnumAvailabilityDialog.Always || this.dialog3Available != EnumAvailabilityDialog.Always || this.dialog4Available != EnumAvailabilityDialog.Always) {
            return true;
        } else if (this.questAvailable != EnumAvailabilityQuest.Always || this.quest2Available != EnumAvailabilityQuest.Always || this.quest3Available != EnumAvailabilityQuest.Always || this.quest4Available != EnumAvailabilityQuest.Always) {
            return true;
        } else if (this.daytime != EnumDayTime.Always || this.minPlayerLevel > 0) {
            return true;
        } else {
            return this.factionAvailable != EnumAvailabilityFactionType.Always || this.faction2Available != EnumAvailabilityFactionType.Always ? true : !this.scoreboardObjective.isEmpty() || !this.scoreboard2Objective.isEmpty();
        }
    }

    public boolean hasOptions() {
        return this.hasOptions;
    }
}