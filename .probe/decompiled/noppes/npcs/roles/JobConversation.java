package noppes.npcs.roles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import noppes.npcs.controllers.PlayerQuestController;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.controllers.data.Availability;
import noppes.npcs.controllers.data.Line;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.entity.EntityNPCInterface;

public class JobConversation extends JobInterface {

    public Availability availability = new Availability();

    private ArrayList<String> names = new ArrayList();

    private HashMap<String, EntityNPCInterface> npcs = new HashMap();

    public HashMap<Integer, JobConversation.ConversationLine> lines = new HashMap();

    public int quest = -1;

    public String questTitle = "";

    public int generalDelay = 400;

    public int ticks = 100;

    public int range = 20;

    private JobConversation.ConversationLine nextLine;

    private boolean hasStarted = false;

    private int startedTicks = 20;

    public int mode = 0;

    public JobConversation(EntityNPCInterface npc) {
        super(npc);
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        compound.put("ConversationAvailability", this.availability.save(new CompoundTag()));
        compound.putInt("ConversationQuest", this.quest);
        compound.putInt("ConversationDelay", this.generalDelay);
        compound.putInt("ConversationRange", this.range);
        compound.putInt("ConversationMode", this.mode);
        ListTag nbttaglist = new ListTag();
        for (int slot : this.lines.keySet()) {
            JobConversation.ConversationLine line = (JobConversation.ConversationLine) this.lines.get(slot);
            CompoundTag nbttagcompound = new CompoundTag();
            nbttagcompound.putInt("Slot", slot);
            line.addAdditionalSaveData(nbttagcompound);
            nbttaglist.add(nbttagcompound);
        }
        compound.put("ConversationLines", nbttaglist);
        if (this.hasQuest()) {
            compound.putString("ConversationQuestTitle", this.getQuest().title);
        }
        return compound;
    }

    @Override
    public void load(CompoundTag compound) {
        this.names.clear();
        this.availability.load(compound.getCompound("ConversationAvailability"));
        this.quest = compound.getInt("ConversationQuest");
        this.generalDelay = compound.getInt("ConversationDelay");
        this.questTitle = compound.getString("ConversationQuestTitle");
        this.range = compound.getInt("ConversationRange");
        this.mode = compound.getInt("ConversationMode");
        ListTag nbttaglist = compound.getList("ConversationLines", 10);
        HashMap<Integer, JobConversation.ConversationLine> map = new HashMap();
        for (int i = 0; i < nbttaglist.size(); i++) {
            CompoundTag nbttagcompound = nbttaglist.getCompound(i);
            JobConversation.ConversationLine line = new JobConversation.ConversationLine();
            line.readAdditionalSaveData(nbttagcompound);
            if (!line.npc.isEmpty() && !this.names.contains(line.npc.toLowerCase())) {
                this.names.add(line.npc.toLowerCase());
            }
            map.put(nbttagcompound.getInt("Slot"), line);
        }
        this.lines = map;
        this.ticks = this.generalDelay;
    }

    public boolean hasQuest() {
        return this.getQuest() != null;
    }

    public Quest getQuest() {
        return this.npc.isClientSide() ? null : (Quest) QuestController.instance.quests.get(this.quest);
    }

    @Override
    public void aiUpdateTask() {
        this.ticks--;
        if (this.ticks <= 0 && this.nextLine != null) {
            this.say(this.nextLine);
            boolean seenNext = false;
            JobConversation.ConversationLine compare = this.nextLine;
            this.nextLine = null;
            for (JobConversation.ConversationLine line : this.lines.values()) {
                if (!line.isEmpty()) {
                    if (seenNext) {
                        this.nextLine = line;
                        break;
                    }
                    if (line == compare) {
                        seenNext = true;
                    }
                }
            }
            if (this.nextLine != null) {
                this.ticks = this.nextLine.delay;
            } else if (this.hasQuest()) {
                for (Player player : this.npc.m_9236_().m_45976_(Player.class, this.npc.m_20191_().inflate((double) this.range, (double) this.range, (double) this.range))) {
                    if (this.availability.isAvailable(player)) {
                        PlayerQuestController.addActiveQuest(this.getQuest(), player);
                    }
                }
            }
        }
    }

    @Override
    public boolean aiShouldExecute() {
        if (!this.lines.isEmpty() && !this.npc.isKilled() && !this.npc.isAttacking() && this.shouldRun()) {
            if (!this.hasStarted && this.mode == 1) {
                if (this.startedTicks-- > 0) {
                    return false;
                }
                this.startedTicks = 10;
                if (this.npc.m_9236_().m_45976_(Player.class, this.npc.m_20191_().inflate((double) this.range, (double) this.range, (double) this.range)).isEmpty()) {
                    return false;
                }
            }
            for (JobConversation.ConversationLine line : this.lines.values()) {
                if (line != null && !line.isEmpty()) {
                    this.nextLine = line;
                    break;
                }
            }
            return this.nextLine != null;
        } else {
            return false;
        }
    }

    private boolean shouldRun() {
        this.ticks--;
        if (this.ticks > 0) {
            return false;
        } else {
            this.npcs.clear();
            for (EntityNPCInterface npc : this.npc.m_9236_().m_45976_(EntityNPCInterface.class, this.npc.m_20191_().inflate(10.0, 10.0, 10.0))) {
                String name = npc.getName().getString().toLowerCase();
                if (!npc.isKilled() && !npc.isAttacking() && this.names.contains(name)) {
                    this.npcs.put(name, npc);
                }
            }
            boolean bo = this.names.size() == this.npcs.size();
            if (!bo) {
                this.ticks = 20;
            }
            return bo;
        }
    }

    @Override
    public boolean aiContinueExecute() {
        for (EntityNPCInterface npc : this.npcs.values()) {
            if (npc.isKilled() || npc.isAttacking()) {
                return false;
            }
        }
        return this.nextLine != null;
    }

    @Override
    public void stop() {
        this.nextLine = null;
        this.ticks = this.generalDelay;
        this.hasStarted = false;
    }

    @Override
    public void aiStartExecuting() {
        this.startedTicks = 20;
        this.hasStarted = true;
    }

    private void say(JobConversation.ConversationLine line) {
        List<Player> inRange = this.npc.m_9236_().m_45976_(Player.class, this.npc.m_20191_().inflate((double) this.range, (double) this.range, (double) this.range));
        EntityNPCInterface npc = (EntityNPCInterface) this.npcs.get(line.npc.toLowerCase());
        if (npc != null) {
            for (Player player : inRange) {
                if (this.availability.isAvailable(player)) {
                    npc.say(player, line);
                }
            }
        }
    }

    @Override
    public void reset() {
        this.hasStarted = false;
        this.stop();
        this.ticks = 60;
    }

    @Override
    public void killed() {
        this.reset();
    }

    public JobConversation.ConversationLine getLine(int slot) {
        if (this.lines.containsKey(slot)) {
            return (JobConversation.ConversationLine) this.lines.get(slot);
        } else {
            JobConversation.ConversationLine line = new JobConversation.ConversationLine();
            this.lines.put(slot, line);
            return line;
        }
    }

    @Override
    public int getType() {
        return 7;
    }

    public class ConversationLine extends Line {

        public String npc = "";

        public int delay = 40;

        public void addAdditionalSaveData(CompoundTag compound) {
            compound.putString("Line", this.text);
            compound.putString("Npc", this.npc);
            compound.putString("Sound", this.sound);
            compound.putInt("Delay", this.delay);
        }

        public void readAdditionalSaveData(CompoundTag compound) {
            this.text = compound.getString("Line");
            this.npc = compound.getString("Npc");
            this.sound = compound.getString("Sound");
            this.delay = compound.getInt("Delay");
        }

        public boolean isEmpty() {
            return this.npc.isEmpty() || this.text.isEmpty();
        }
    }
}