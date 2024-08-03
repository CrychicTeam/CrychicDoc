package noppes.npcs.controllers.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import noppes.npcs.ICompatibilty;
import noppes.npcs.NpcMiscInventory;
import noppes.npcs.VersionCompatibility;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.IContainer;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.handler.data.IQuest;
import noppes.npcs.api.handler.data.IQuestCategory;
import noppes.npcs.api.handler.data.IQuestObjective;
import noppes.npcs.constants.EnumQuestCompletion;
import noppes.npcs.constants.EnumQuestRepeat;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.db.DatabaseColumn;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketQuestCompletion;
import noppes.npcs.quests.QuestDialog;
import noppes.npcs.quests.QuestInterface;
import noppes.npcs.quests.QuestItem;
import noppes.npcs.quests.QuestKill;
import noppes.npcs.quests.QuestLocation;
import noppes.npcs.quests.QuestManual;

public class Quest implements ICompatibilty, IQuest {

    public int version = VersionCompatibility.ModRev;

    @DatabaseColumn(name = "id", type = DatabaseColumn.Type.INT)
    public int id = -1;

    @DatabaseColumn(name = "title", type = DatabaseColumn.Type.VARCHAR)
    public String title = "default";

    @DatabaseColumn(name = "type", type = DatabaseColumn.Type.SMALLINT)
    public int type = 0;

    @DatabaseColumn(name = "repeat_type", type = DatabaseColumn.Type.ENUM)
    public EnumQuestRepeat repeat = EnumQuestRepeat.NONE;

    @DatabaseColumn(name = "completion_type", type = DatabaseColumn.Type.ENUM)
    public EnumQuestCompletion completion = EnumQuestCompletion.Npc;

    @DatabaseColumn(name = "category", type = DatabaseColumn.Type.VARCHAR)
    public String categoryName;

    public QuestCategory category;

    @DatabaseColumn(name = "log_text", type = DatabaseColumn.Type.TEXT)
    public String logText = "";

    @DatabaseColumn(name = "complete_text", type = DatabaseColumn.Type.TEXT)
    public String completeText = "";

    @DatabaseColumn(name = "complete_npc", type = DatabaseColumn.Type.VARCHAR)
    public String completerNpc = "";

    @DatabaseColumn(name = "next_quest", type = DatabaseColumn.Type.INT)
    public int nextQuestid = -1;

    @DatabaseColumn(name = "command", type = DatabaseColumn.Type.TEXT)
    public String command = "";

    @DatabaseColumn(name = "mail_data", type = DatabaseColumn.Type.JSON)
    public CompoundTag mailData = new CompoundTag();

    public PlayerMail mail = new PlayerMail();

    @DatabaseColumn(name = "quest_data", type = DatabaseColumn.Type.JSON)
    public CompoundTag questData = new CompoundTag();

    public QuestInterface questInterface = new QuestItem();

    @DatabaseColumn(name = "reward_exp", type = DatabaseColumn.Type.INT)
    public int rewardExp = 0;

    @DatabaseColumn(name = "reward_items", type = DatabaseColumn.Type.JSON)
    public CompoundTag rewardItemsData = new CompoundTag();

    public NpcMiscInventory rewardItems = new NpcMiscInventory(9);

    @DatabaseColumn(name = "reward_randomized", type = DatabaseColumn.Type.BOOLEAN)
    public boolean randomReward = false;

    @DatabaseColumn(name = "faction_options", type = DatabaseColumn.Type.JSON)
    public FactionOptions factionOptions = new FactionOptions();

    public Quest(QuestCategory category) {
        this.category = category;
    }

    public void readNBT(CompoundTag compound) {
        this.id = compound.getInt("Id");
        this.readNBTPartial(compound);
    }

    public void readNBTPartial(CompoundTag compound) {
        this.version = compound.getInt("ModRev");
        VersionCompatibility.CheckAvailabilityCompatibility(this, compound);
        this.setType(compound.getInt("Type"));
        this.title = compound.getString("Title");
        this.logText = compound.getString("Text");
        this.completeText = compound.getString("CompleteText");
        this.completerNpc = compound.getString("CompleterNpc");
        this.command = compound.getString("QuestCommand");
        this.nextQuestid = compound.getInt("NextQuestId");
        this.randomReward = compound.getBoolean("RandomReward");
        this.rewardExp = compound.getInt("RewardExp");
        this.rewardItems.setFromNBT(compound.getCompound("Rewards"));
        this.completion = EnumQuestCompletion.values()[compound.getInt("QuestCompletion")];
        this.repeat = EnumQuestRepeat.values()[compound.getInt("QuestRepeat")];
        this.questInterface.readAdditionalSaveData(compound);
        this.factionOptions.load(compound.getCompound("QuestFactionPoints"));
        this.mail.readNBT(compound.getCompound("QuestMail"));
    }

    @Override
    public void setType(int questType) {
        this.type = questType;
        if (this.type == 0) {
            this.questInterface = new QuestItem();
        } else if (this.type == 1) {
            this.questInterface = new QuestDialog();
        } else if (this.type == 2 || this.type == 4) {
            this.questInterface = new QuestKill();
        } else if (this.type == 3) {
            this.questInterface = new QuestLocation();
        } else if (this.type == 5) {
            this.questInterface = new QuestManual();
        }
        if (this.questInterface != null) {
            this.questInterface.questId = this.id;
        }
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        compound.putInt("Id", this.id);
        return this.writeToNBTPartial(compound);
    }

    public CompoundTag writeToNBTPartial(CompoundTag compound) {
        compound.putInt("ModRev", this.version);
        compound.putInt("Type", this.type);
        compound.putString("Title", this.title);
        compound.putString("Text", this.logText);
        compound.putString("CompleteText", this.completeText);
        compound.putString("CompleterNpc", this.completerNpc);
        compound.putInt("NextQuestId", this.nextQuestid);
        compound.putInt("RewardExp", this.rewardExp);
        compound.put("Rewards", this.rewardItems.getToNBT());
        compound.putString("QuestCommand", this.command);
        compound.putBoolean("RandomReward", this.randomReward);
        compound.putInt("QuestCompletion", this.completion.ordinal());
        compound.putInt("QuestRepeat", this.repeat.ordinal());
        this.questInterface.addAdditionalSaveData(compound);
        compound.put("QuestFactionPoints", this.factionOptions.save(new CompoundTag()));
        compound.put("QuestMail", this.mail.writeNBT());
        return compound;
    }

    public boolean hasNewQuest() {
        return this.getNextQuest() != null;
    }

    public Quest getNextQuest() {
        return QuestController.instance == null ? null : (Quest) QuestController.instance.quests.get(this.nextQuestid);
    }

    public boolean complete(Player player, QuestData data) {
        if (this.completion == EnumQuestCompletion.Instant) {
            Packets.send((ServerPlayer) player, new PacketQuestCompletion(data.quest.id));
            return true;
        } else {
            return false;
        }
    }

    public Quest copy() {
        Quest quest = new Quest(this.category);
        quest.readNBT(this.save(new CompoundTag()));
        return quest;
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
    public int getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.title;
    }

    @Override
    public int getType() {
        return this.type;
    }

    @Override
    public IQuestCategory getCategory() {
        return this.category;
    }

    @Override
    public void save() {
        QuestController.instance.saveQuest(this.category, this);
    }

    @Override
    public void setName(String name) {
        this.title = name;
    }

    @Override
    public String getLogText() {
        return this.logText;
    }

    @Override
    public void setLogText(String text) {
        this.logText = text;
    }

    @Override
    public String getCompleteText() {
        return this.completeText;
    }

    @Override
    public void setCompleteText(String text) {
        this.completeText = text;
    }

    @Override
    public void setNextQuest(IQuest quest) {
        if (quest == null) {
            this.nextQuestid = -1;
        } else {
            if (quest.getId() < 0) {
                throw new CustomNPCsException("Quest id is lower than 0");
            }
            this.nextQuestid = quest.getId();
        }
    }

    @Override
    public String getNpcName() {
        return this.completerNpc;
    }

    @Override
    public void setNpcName(String name) {
        this.completerNpc = name;
    }

    @Override
    public IQuestObjective[] getObjectives(IPlayer player) {
        if (!player.hasActiveQuest(this.id)) {
            throw new CustomNPCsException("Player doesnt have this quest active.");
        } else {
            return this.questInterface.getObjectives(player.getMCEntity());
        }
    }

    @Override
    public boolean getIsRepeatable() {
        return this.repeat != EnumQuestRepeat.NONE;
    }

    @Override
    public IContainer getRewards() {
        return NpcAPI.Instance().getIContainer(this.rewardItems);
    }
}