package dev.xkmc.l2hostility.content.capability.mob;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2hostility.content.capability.chunk.ChunkDifficulty;
import dev.xkmc.l2hostility.content.capability.chunk.RegionalDifficultyModifier;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.content.item.spawner.TraitSpawnerBlockEntity;
import dev.xkmc.l2hostility.content.logic.InheritContext;
import dev.xkmc.l2hostility.content.logic.ItemPopulator;
import dev.xkmc.l2hostility.content.logic.MobDifficultyCollector;
import dev.xkmc.l2hostility.content.logic.PlayerFinder;
import dev.xkmc.l2hostility.content.logic.TraitManager;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.advancements.HostilityTriggers;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LHTagGen;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2library.capability.entity.GeneralCapabilityHolder;
import dev.xkmc.l2library.capability.entity.GeneralCapabilityTemplate;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import dev.xkmc.l2serial.util.Wrappers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

@SerialClass
public class MobTraitCap extends GeneralCapabilityTemplate<LivingEntity, MobTraitCap> {

    public static final Capability<MobTraitCap> CAPABILITY = CapabilityManager.get(new CapabilityToken<MobTraitCap>() {
    });

    public static final GeneralCapabilityHolder<LivingEntity, MobTraitCap> HOLDER = new GeneralCapabilityHolder(new ResourceLocation("l2hostility", "traits"), CAPABILITY, MobTraitCap.class, MobTraitCap::new, LivingEntity.class, e -> e.m_6095_().is(LHTagGen.WHITELIST) || e instanceof Enemy && !e.m_6095_().is(LHTagGen.BLACKLIST));

    @SerialField(toClient = true)
    public final LinkedHashMap<MobTrait, Integer> traits = new LinkedHashMap();

    @SerialField(toClient = true)
    private MobTraitCap.Stage stage = MobTraitCap.Stage.PRE_INIT;

    @SerialField(toClient = true)
    public int lv;

    @SerialField
    private final HashMap<ResourceLocation, CapStorageData> data = new HashMap();

    @SerialField(toClient = true)
    public boolean summoned = false;

    @SerialField(toClient = true)
    public boolean minion = false;

    @SerialField(toClient = true)
    public boolean noDrop = false;

    @SerialField(toClient = true)
    public boolean fullDrop = false;

    @SerialField
    public double dropRate = 1.0;

    @Nullable
    @SerialField
    public BlockPos pos = null;

    @Nullable
    @SerialField(toClient = true)
    public MinionData asMinion = null;

    @Nullable
    @SerialField(toClient = true)
    public MasterData asMaster = null;

    @Nullable
    private TraitSpawnerBlockEntity summoner = null;

    private boolean inherited = false;

    private boolean ticking = false;

    private EntityConfig.Config configCache = null;

    private final ArrayList<Pair<MobTrait, Integer>> pending = new ArrayList();

    public void syncToClient(LivingEntity entity) {
        L2Hostility.HANDLER.toTrackingPlayers(new MobCapSyncToClient(entity, this), entity);
    }

    public void syncToPlayer(LivingEntity entity, ServerPlayer player) {
        L2Hostility.HANDLER.toClientPlayer(new MobCapSyncToClient(entity, this), player);
    }

    public static void register() {
    }

    public void deinit() {
        this.traits.clear();
        this.lv = 0;
        this.stage = MobTraitCap.Stage.PRE_INIT;
    }

    public boolean reinit(LivingEntity mob, int level, boolean max) {
        this.deinit();
        this.init(mob.m_9236_(), mob, (pos, ins) -> {
            ins.base = level;
            if (max) {
                ins.setFullChance();
            }
        });
        return true;
    }

    @Nullable
    public EntityConfig.Config getConfigCache(LivingEntity le) {
        if (this.configCache == null) {
            this.configCache = L2Hostility.ENTITY.getMerged().get(le.m_6095_());
        }
        return this.configCache;
    }

    public void setConfigCache(EntityConfig.Config config) {
        this.configCache = config;
    }

    public void init(Level level, LivingEntity le, RegionalDifficultyModifier difficulty) {
        boolean var10000;
        label33: {
            if (!LHConfig.COMMON.allowNoAI.get() && le instanceof Mob mob && mob.isNoAi()) {
                var10000 = true;
                break label33;
            }
            var10000 = false;
        }
        boolean skip = var10000;
        MobDifficultyCollector instance = new MobDifficultyCollector();
        EntityConfig.Config diff = this.getConfigCache(le);
        if (diff != null) {
            instance.acceptConfig(diff.difficulty());
        }
        difficulty.modifyInstance(le.m_20183_(), instance);
        Player player = PlayerFinder.getNearestPlayer(level, le);
        if (player != null && PlayerDifficulty.HOLDER.isProper(player)) {
            PlayerDifficulty playerDiff = PlayerDifficulty.HOLDER.get(player);
            playerDiff.apply(instance);
            if (!LHConfig.COMMON.allowPlayerAllies.get() && le.m_7307_(player)) {
                skip = true;
            }
        }
        this.lv = skip ? 0 : TraitManager.fill(this, le, this.traits, instance);
        this.fullDrop = instance.isFullDrop();
        this.stage = MobTraitCap.Stage.INIT;
        this.syncToClient(le);
    }

    public void copyFrom(LivingEntity par, LivingEntity child, MobTraitCap parent) {
        InheritContext ctx = new InheritContext(par, parent, child, this, !parent.inherited);
        parent.inherited = true;
        this.lv = parent.lv;
        this.summoned = parent.summoned;
        this.minion = parent.minion;
        this.noDrop = parent.noDrop;
        this.dropRate = parent.dropRate * LHConfig.COMMON.splitDropRateFactor.get();
        for (Entry<MobTrait, Integer> ent : parent.traits.entrySet()) {
            int rank = ((MobTrait) ent.getKey()).inherited(this, (Integer) ent.getValue(), ctx);
            if (rank > 0) {
                this.traits.put((MobTrait) ent.getKey(), rank);
            }
        }
        TraitManager.fill(this, child, this.traits, MobDifficultyCollector.noTrait(this.lv));
        this.stage = MobTraitCap.Stage.INIT;
    }

    public int getEnchantBonus() {
        return (int) ((double) this.lv * LHConfig.COMMON.enchantmentFactor.get());
    }

    public int getLevel() {
        return this.lv;
    }

    public void setLevel(LivingEntity le, int level) {
        this.lv = this.clampLevel(le, level);
        TraitManager.scale(le, this.lv);
    }

    public int clampLevel(LivingEntity le, int lv) {
        int cap = LHConfig.COMMON.maxMobLevel.get();
        EntityConfig.Config config = this.getConfigCache(le);
        if (config != null && config.maxLevel > 0) {
            cap = Math.min(config.maxLevel, cap);
        }
        return Math.min(cap, lv);
    }

    public boolean isInitialized() {
        return this.stage != MobTraitCap.Stage.PRE_INIT;
    }

    public int getTraitLevel(MobTrait trait) {
        return (Integer) this.traits.getOrDefault(trait, 0);
    }

    public boolean hasTrait(MobTrait trait) {
        return this.getTraitLevel(trait) > 0;
    }

    public void traitEvent(BiConsumer<MobTrait, Integer> cons) {
        this.traits.forEach(cons);
    }

    public void setTrait(String id, int lv) {
        MobTrait trait = LHTraits.TRAITS.get().getValue(new ResourceLocation(id));
        if (trait != null) {
            this.setTrait(trait, lv);
        }
    }

    public void setTrait(MobTrait trait, int lv) {
        this.pending.add(Pair.of(trait, lv));
    }

    public void removeTrait(MobTrait trait) {
        if (this.traits.containsKey(trait)) {
            if (this.ticking) {
                this.setTrait(trait, 0);
            } else {
                this.traits.remove(trait);
            }
        }
    }

    private boolean clearPending(LivingEntity mob) {
        if (this.pending.isEmpty()) {
            return false;
        } else {
            while (!this.pending.isEmpty()) {
                ArrayList<Pair<MobTrait, Integer>> temp = new ArrayList(this.pending);
                for (Pair<MobTrait, Integer> pair : this.pending) {
                    this.traits.put((MobTrait) pair.getFirst(), (Integer) pair.getSecond());
                }
                this.pending.clear();
                for (Pair<MobTrait, Integer> pair : temp) {
                    ((MobTrait) pair.getFirst()).initialize(mob, (Integer) pair.getSecond());
                    ((MobTrait) pair.getFirst()).postInit(mob, (Integer) pair.getSecond());
                }
                for (Pair<MobTrait, Integer> pair : temp) {
                    if ((Integer) pair.getSecond() == 0) {
                        this.traits.remove(pair.getFirst());
                    }
                }
            }
            return true;
        }
    }

    public void tick(LivingEntity mob) {
        boolean sync = false;
        this.ticking = true;
        if (!mob.m_9236_().isClientSide()) {
            if (!this.isInitialized()) {
                Optional<ChunkDifficulty> opt = ChunkDifficulty.at(mob.m_9236_(), mob.m_20183_());
                opt.ifPresent(chunkDifficulty -> this.init(mob.m_9236_(), mob, chunkDifficulty));
            }
            if (this.stage == MobTraitCap.Stage.INIT) {
                this.stage = MobTraitCap.Stage.POST_INIT;
                ItemPopulator.postFill(this, mob);
                this.traits.forEach((k, v) -> k.postInit(mob, v));
                this.clearPending(mob);
                mob.setHealth(mob.getMaxHealth());
                sync = true;
            }
            if (!this.traits.isEmpty() && !LHConfig.COMMON.allowTraitOnOwnable.get() && mob instanceof OwnableEntity own && own.getOwner() instanceof Player) {
                this.traits.clear();
                sync = true;
            }
        }
        if (this.isInitialized()) {
            if (!this.traits.isEmpty()) {
                if (mob.f_19797_ % 10 == 0) {
                    sync |= this.traits.keySet().removeIf(Objects::isNull);
                    sync |= this.traits.keySet().removeIf(MobTrait::isBanned);
                }
                this.traits.forEach((k, v) -> k.tick(mob, v));
            }
            sync |= this.clearPending(mob);
        }
        if (!mob.m_9236_().isClientSide() && this.pos != null) {
            if (this.summoner == null && mob.m_9236_().getBlockEntity(this.pos) instanceof TraitSpawnerBlockEntity be) {
                this.summoner = be;
            }
            if (this.summoner == null || this.summoner.m_58901_()) {
                mob.m_146870_();
            }
        }
        if (this.asMinion != null) {
            sync |= this.asMinion.tick(mob);
        }
        if (this.hasTrait((MobTrait) LHTraits.MASTER.get())) {
            if (!mob.m_9236_().isClientSide() && this.asMaster == null) {
                this.asMaster = new MasterData();
                sync = true;
            }
            if (mob instanceof Mob master && this.asMaster != null) {
                sync |= this.asMaster.tick(this, master);
            }
        }
        if (!mob.m_9236_().isClientSide() && sync && !mob.m_213877_()) {
            this.syncToClient(mob);
        }
        this.ticking = false;
    }

    public boolean shouldDiscard(LivingEntity mob) {
        EntityConfig.Config config = this.getConfigCache(mob);
        return config != null && config.minSpawnLevel > 0 ? this.lv < config.minSpawnLevel : false;
    }

    public void onKilled(LivingEntity mob, @Nullable Player player) {
        if (this.summoner != null && !this.summoner.m_58901_()) {
            this.summoner.data.onDeath(mob);
        }
        if (player instanceof ServerPlayer sp) {
            HostilityTriggers.TRAIT_LEVEL.trigger(sp, this);
            HostilityTriggers.TRAIT_COUNT.trigger(sp, this);
            HostilityTriggers.KILL_TRAITS.trigger(sp, this);
            HostilityTriggers.TRAIT_FLAME.trigger(sp, mob, this);
            HostilityTriggers.TRAIT_EFFECT.trigger(sp, mob, this);
        }
    }

    public boolean isSummoned() {
        return this.summoned || this.minion;
    }

    public boolean isMasterProtected() {
        if (this.asMaster != null) {
            for (MasterData.Minion e : this.asMaster.data) {
                if (e.minion != null && HOLDER.isProper(e.minion)) {
                    MobTraitCap scap = (MobTraitCap) HOLDER.get(e.minion);
                    if (scap.asMinion != null && scap.asMinion.protectMaster) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Nullable
    public <T extends CapStorageData> T getData(ResourceLocation id) {
        return (T) Wrappers.cast((CapStorageData) this.data.get(id));
    }

    public <T extends CapStorageData> T getOrCreateData(ResourceLocation id, Supplier<T> sup) {
        return (T) Wrappers.cast((CapStorageData) this.data.computeIfAbsent(id, e -> (CapStorageData) sup.get()));
    }

    public List<Component> getTitle(boolean showLevel, boolean showTrait) {
        List<Component> ans = new ArrayList();
        if (showLevel && this.lv > 0) {
            ans.add(LangData.LV.get(this.lv).withStyle(Style.EMPTY.withColor(this.fullDrop ? LHConfig.CLIENT.overHeadLevelColorAbyss.get() : LHConfig.CLIENT.overHeadLevelColor.get())));
        }
        if (!showTrait) {
            return ans;
        } else {
            MutableComponent temp = null;
            int count = 0;
            for (Entry<MobTrait, Integer> e : this.traits.entrySet()) {
                MutableComponent comp = ((MobTrait) e.getKey()).getFullDesc((Integer) e.getValue());
                if (temp == null) {
                    temp = comp;
                    count = 1;
                } else {
                    temp.append(Component.literal(" / ").withStyle(ChatFormatting.WHITE)).append(comp);
                    if (++count >= 3) {
                        ans.add(temp);
                        count = 0;
                        temp = null;
                    }
                }
            }
            if (count > 0) {
                ans.add(temp);
            }
            return ans;
        }
    }

    public static enum Stage {

        PRE_INIT, INIT, POST_INIT
    }
}