package harmonised.pmmo.storage;

import com.mojang.serialization.Codec;
import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.api.events.XpEvent;
import harmonised.pmmo.config.Config;
import harmonised.pmmo.config.SkillsConfig;
import harmonised.pmmo.config.codecs.CodecTypes;
import harmonised.pmmo.config.codecs.SkillData;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.core.IDataStorage;
import harmonised.pmmo.features.loot_modifiers.SkillUpTrigger;
import harmonised.pmmo.network.Networking;
import harmonised.pmmo.network.clientpackets.CP_UpdateExperience;
import harmonised.pmmo.network.clientpackets.CP_UpdateLevelCache;
import harmonised.pmmo.util.MsLoggy;
import harmonised.pmmo.util.TagBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.server.ServerLifecycleHooks;

public class PmmoSavedData extends SavedData implements IDataStorage {

    private static String NAME = "pmmo";

    private Map<UUID, Map<String, Long>> xp = new HashMap();

    private Map<UUID, Map<String, Long>> scheduledXP = new HashMap();

    private static final Codec<Map<UUID, Map<String, Long>>> XP_CODEC = Codec.unboundedMap(CodecTypes.UUID_CODEC, Codec.unboundedMap(Codec.STRING, Codec.LONG).xmap(HashMap::new, HashMap::new));

    private static final String XP_KEY = "xp_data";

    private static final String SCHEDULED_KEY = "scheduled_xp";

    private List<Long> levelCache = new ArrayList();

    @Override
    public long getXpRaw(UUID playerID, String skillName) {
        return (Long) ((Map) this.xp.getOrDefault(playerID, new HashMap())).getOrDefault(skillName, 0L);
    }

    @Override
    public boolean setXpDiff(UUID playerID, String skillName, long change) {
        long oldValue = this.getXpRaw(playerID, skillName);
        ServerPlayer player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(playerID);
        if (player == null) {
            ((Map) this.scheduledXP.computeIfAbsent(playerID, id -> new HashMap())).merge(skillName, change, (o, n) -> o + n);
            return true;
        } else {
            XpEvent gainXpEvent = new XpEvent(player, skillName, oldValue, change, TagBuilder.start().build());
            if (MinecraftForge.EVENT_BUS.post(gainXpEvent)) {
                return false;
            } else {
                this.setXpRaw(playerID, gainXpEvent.skill, oldValue + gainXpEvent.amountAwarded);
                return true;
            }
        }
    }

    @Override
    public void setXpRaw(UUID playerID, String skillName, long value) {
        long formerRaw = (long) this.getLevelFromXP(this.getXpRaw(playerID, skillName));
        ((Map) this.xp.computeIfAbsent(playerID, s -> new HashMap())).put(skillName, value);
        this.m_77762_();
        ServerPlayer player;
        if ((player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(playerID)) != null) {
            Networking.sendToClient(new CP_UpdateExperience(skillName, value), player);
            MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.XP, "Skill Update Packet sent to Client" + playerID.toString());
            if (formerRaw != (long) this.getLevelFromXP(value)) {
                SkillUpTrigger.SKILL_UP.trigger(player);
                Core.get(LogicalSide.SERVER).getPerkRegistry().executePerk(EventType.SKILL_UP, player, TagBuilder.start().withString("firework_skill", skillName).build());
            }
        }
    }

    @Override
    public Map<String, Long> getXpMap(UUID playerID) {
        return (Map<String, Long>) this.xp.getOrDefault(playerID, new HashMap());
    }

    @Override
    public void setXpMap(UUID playerID, Map<String, Long> map) {
        this.xp.put(playerID, map != null ? map : new HashMap());
        this.m_77762_();
    }

    @Override
    public int getPlayerSkillLevel(String skill, UUID player) {
        int rawLevel = Core.get(LogicalSide.SERVER).getLevelProvider().process(skill, this.getLevelFromXP(this.getXpRaw(player, skill)));
        int skillMaxLevel = ((SkillData) SkillsConfig.SKILLS.get().getOrDefault(skill, SkillData.Builder.getDefault())).getMaxLevel();
        return Math.min(rawLevel, skillMaxLevel);
    }

    @Override
    public void setPlayerSkillLevel(String skill, UUID player, int level) {
        long xpRaw = level > 0 ? (Long) this.levelCache.get(level - 1) : 0L;
        this.setXpRaw(player, skill, xpRaw);
    }

    @Override
    public boolean changePlayerSkillLevel(String skill, UUID playerID, int change) {
        int currentLevel = this.getPlayerSkillLevel(skill, playerID);
        long oldXp = this.getXpRaw(playerID, skill);
        long newXp = currentLevel - 1 + change >= 0 ? (Long) this.levelCache.get(currentLevel + change - 1) : 0L;
        ServerPlayer player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(playerID);
        if (player != null) {
            XpEvent gainXpEvent = new XpEvent(player, skill, oldXp, newXp - oldXp, TagBuilder.start().build());
            if (MinecraftForge.EVENT_BUS.post(gainXpEvent)) {
                return false;
            }
            if (gainXpEvent.isLevelUp()) {
                Core.get(LogicalSide.SERVER).getPerkRegistry().executePerk(EventType.SKILL_UP, player, TagBuilder.start().withString("firework_skill", skill).build());
            }
            this.setPlayerSkillLevel(gainXpEvent.skill, playerID, gainXpEvent.endLevel());
        } else {
            this.setPlayerSkillLevel(skill, playerID, currentLevel + change);
        }
        return true;
    }

    @Override
    public long getBaseXpForLevel(int level) {
        return level > 0 && level - 1 < this.levelCache.size() ? (Long) this.levelCache.get(level - 1) : 0L;
    }

    public PmmoSavedData() {
    }

    public PmmoSavedData(CompoundTag nbt) {
        this.xp = new HashMap((Map) XP_CODEC.parse(NbtOps.INSTANCE, nbt.getCompound("xp_data")).result().orElse(new HashMap()));
        this.scheduledXP = new HashMap((Map) XP_CODEC.parse(NbtOps.INSTANCE, nbt.getCompound("scheduled_xp")).result().orElse(new HashMap()));
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        Map<UUID, Map<String, Long>> cleanXP = (Map<UUID, Map<String, Long>>) this.xp.entrySet().stream().filter(entry -> !((Map) entry.getValue()).isEmpty()).collect(Collectors.toMap(Entry::getKey, Entry::getValue));
        nbt.put("xp_data", (CompoundTag) XP_CODEC.encodeStart(NbtOps.INSTANCE, cleanXP).result().orElse(new CompoundTag()));
        nbt.put("scheduled_xp", (CompoundTag) XP_CODEC.encodeStart(NbtOps.INSTANCE, this.scheduledXP).result().orElse(new CompoundTag()));
        return nbt;
    }

    @Override
    public IDataStorage get() {
        return (IDataStorage) (ServerLifecycleHooks.getCurrentServer() != null ? ServerLifecycleHooks.getCurrentServer().overworld().getDataStorage().computeIfAbsent(PmmoSavedData::new, PmmoSavedData::new, NAME) : new PmmoSavedData());
    }

    @Override
    public int getLevelFromXP(long xp) {
        for (int i = 0; i < this.levelCache.size(); i++) {
            if ((Long) this.levelCache.get(i) > xp) {
                return Core.get(LogicalSide.SERVER).getLevelProvider().process("", i);
            }
        }
        return Config.MAX_LEVEL.get();
    }

    public List<Long> getLevelCache() {
        return this.levelCache;
    }

    @Override
    public void computeLevelsForCache() {
        if (Config.STATIC_LEVELS.get().size() > 0 && (Long) Config.STATIC_LEVELS.get().get(0) != -1L) {
            List<Long> values = new ArrayList((Collection) Config.STATIC_LEVELS.get());
            boolean validList = true;
            for (int i = 1; i < values.size(); i++) {
                if ((Long) values.get(i) <= (Long) values.get(i - 1)) {
                    validList = false;
                    break;
                }
            }
            if (validList) {
                Config.MAX_LEVEL.set(values.size());
                this.levelCache = values;
                return;
            }
        }
        boolean exponential = Config.USE_EXPONENTIAL_FORMULA.get();
        long linearBase = Config.LINEAR_BASE_XP.get();
        double linearPer = Config.LINEAR_PER_LEVEL.get();
        int exponentialBase = Config.EXPONENTIAL_BASE_XP.get();
        double exponentialRoot = Config.EXPONENTIAL_POWER_BASE.get();
        double exponentialRate = Config.EXPONENTIAL_LEVEL_MOD.get();
        long current = 0L;
        for (int ix = 1; ix <= Config.MAX_LEVEL.get(); ix++) {
            current = (long) ((double) current + (exponential ? (double) exponentialBase * Math.pow(exponentialRoot, exponentialRate * (double) ix) : (double) linearBase + (double) ix * linearPer));
            if (current >= Long.MAX_VALUE) {
                Config.MAX_LEVEL.set(ix - 1);
                break;
            }
            this.levelCache.add(current);
        }
        for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
            Networking.sendToClient(new CP_UpdateLevelCache(this.levelCache), player);
        }
    }

    public void awardScheduledXP(UUID playerID) {
        Map<String, Long> queue = new HashMap((Map) this.scheduledXP.getOrDefault(playerID, new HashMap()));
        this.scheduledXP.remove(playerID);
        for (Entry<String, Long> scheduledValue : queue.entrySet()) {
            this.setXpDiff(playerID, (String) scheduledValue.getKey(), (Long) scheduledValue.getValue());
        }
    }
}