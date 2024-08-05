package com.mna.capabilities.playerdata.progression;

import com.mna.Registries;
import com.mna.advancements.CustomAdvancementTriggers;
import com.mna.api.capabilities.CodexBreadcrumb;
import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.capabilities.resource.CastingResourceIDs;
import com.mna.api.config.GeneralConfigValues;
import com.mna.api.entities.FactionRaidRegistry;
import com.mna.api.entities.IFactionEnemy;
import com.mna.api.faction.FactionDifficultyStats;
import com.mna.api.faction.IFaction;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.rote.PlayerRoteSpellsProvider;
import com.mna.interop.CuriosInterop;
import com.mna.progression.ProgressionEventHandler;
import com.mna.recipes.progression.ProgressionCondition;
import com.mna.tools.SummonUtils;
import com.mna.tools.math.MathUtils;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.IForgeRegistry;
import top.theillusivec4.curios.api.SlotTypePreset;

public class PlayerProgression implements IPlayerProgression {

    public static final int MAX_TIERS = 5;

    public static final double RAID_IRE = 1.0;

    private int tier;

    private IFaction allied_faction = null;

    private int faction_standing;

    private List<ResourceLocation> metTierProgressions;

    private int ally_cooldown;

    private HashMap<IFaction, Double> raidChances = new HashMap();

    private ArrayList<IFaction> forcedRaids;

    private boolean dirty;

    private LinkedList<CodexBreadcrumb> _breadcrumbs;

    private HashMap<IFaction, FactionDifficultyStats> factionDifficultyStats = new HashMap();

    public PlayerProgression() {
        this.tier = 1;
        this.metTierProgressions = new ArrayList();
        this._breadcrumbs = new LinkedList();
        this.forcedRaids = new ArrayList();
        this.setDirty();
    }

    @Override
    public int getTier() {
        return this.tier;
    }

    @Override
    public void setTier(int tier, @Nullable Player player, boolean notifyTierRote) {
        if (this.tier != tier) {
            if (player != null) {
                AttributeInstance inst = player.m_21051_(Attributes.MAX_HEALTH);
                for (int t = 1; t <= 5; t++) {
                    int index = t - 1;
                    int modifierAmount = GeneralConfigValues.TierHealthBoosts.size() >= index ? (Integer) GeneralConfigValues.TierHealthBoosts.get(index) : 0;
                    AttributeModifier modifier = new AttributeModifier(UUID.fromString(Tier_Health_Boost_IDs[index]), "Tier Health Boost " + t, (double) modifierAmount, AttributeModifier.Operation.ADDITION);
                    if (inst.hasModifier(modifier)) {
                        inst.removeModifier(modifier.getId());
                    }
                }
                for (int tx = 1; tx <= 5; tx++) {
                    int index = tx - 1;
                    int modifierAmount = GeneralConfigValues.TierHealthBoosts.size() >= index ? (Integer) GeneralConfigValues.TierHealthBoosts.get(index) : 0;
                    AttributeModifier modifier = new AttributeModifier(UUID.fromString(Tier_Health_Boost_IDs[index]), "Tier Health Boost " + tx, (double) modifierAmount, AttributeModifier.Operation.ADDITION);
                    if (modifierAmount > 0 && tier >= tx && !inst.hasModifier(modifier)) {
                        inst.addPermanentModifier(modifier);
                    }
                }
                if (player.m_21223_() > player.m_21233_()) {
                    player.m_21153_(player.m_21233_());
                }
            }
            this.tier = MathUtils.clamp(tier, 1, 5);
            if (this.tier == 3) {
                CuriosInterop.IMCModifyCurioSlot(SlotTypePreset.RING, 3);
            } else if (this.tier == 5) {
                CuriosInterop.IMCModifyCurioSlot(SlotTypePreset.RING, 4);
            }
            this.metTierProgressions.clear();
            if (player != null && player instanceof ServerPlayer) {
                CustomAdvancementTriggers.TIER_CHANGE.trigger((ServerPlayer) player, tier);
            }
            int belowTier = GeneralConfigValues.AutoRoteLowTier;
            int roteTier = this.tier - belowTier;
            if (player != null && roteTier > 0) {
                player.getCapability(PlayerRoteSpellsProvider.ROTE).ifPresent(r -> {
                    ((IForgeRegistry) Registries.Shape.get()).getValues().forEach(s -> {
                        if (s.getTier(player.m_9236_()) <= roteTier) {
                            r.addRoteXP(null, s, (float) s.requiredXPForRote());
                        }
                    });
                    ((IForgeRegistry) Registries.SpellEffect.get()).getValues().forEach(s -> {
                        if (s.getTier(player.m_9236_()) <= roteTier) {
                            r.addRoteXP(null, s, (float) s.requiredXPForRote());
                        }
                    });
                    ((IForgeRegistry) Registries.Modifier.get()).getValues().forEach(s -> {
                        if (s.getTier(player.m_9236_()) <= roteTier) {
                            r.addRoteXP(null, s, (float) s.requiredXPForRote());
                        }
                    });
                    if (!player.m_9236_().isClientSide() && notifyTierRote) {
                        player.m_213846_(Component.translatable("mna:progresscondition.tier_rote", roteTier));
                    }
                });
            }
            if (player != null) {
                ProgressionEventHandler.confirmExistingAdvancements(player);
            }
            this.setDirty();
        }
    }

    @Override
    public IFaction getAlliedFaction() {
        return this.allied_faction;
    }

    @Override
    public boolean hasAlliedFaction() {
        return this.allied_faction != null;
    }

    @Override
    public void setAlliedFaction(IFaction faction, @Nullable Player player) {
        this.allied_faction = faction;
        if (player != null) {
            player.getCapability(PlayerMagicProvider.MAGIC).ifPresent(magic -> {
                if (faction != null) {
                    boolean curResourceIsForFaction = Arrays.asList(faction.getCastingResources()).contains(magic.getCastingResource().getRegistryName());
                    if (magic.getCastingResource() == null || !curResourceIsForFaction) {
                        magic.setCastingResourceType(faction.getCastingResource(player));
                    }
                } else {
                    magic.setCastingResourceType(CastingResourceIDs.MANA);
                }
            });
            if (player instanceof ServerPlayer) {
                CustomAdvancementTriggers.FACTION_JOIN.trigger((ServerPlayer) player, faction == null ? null : ((IForgeRegistry) Registries.Factions.get()).getKey(faction));
            }
        }
        this.setDirty();
    }

    @Override
    public int getFactionStanding() {
        return this.faction_standing;
    }

    @Override
    public void setFactionStanding(int standing) {
        this.faction_standing = standing;
        this.setDirty();
    }

    @Override
    public void increaseFactionStanding(int amount) {
        this.faction_standing += amount;
        this.setDirty();
    }

    @Override
    public boolean needsSync() {
        return this.dirty;
    }

    @Override
    public void clearSyncFlag() {
        this.dirty = false;
    }

    @Override
    public void addTierProgressionComplete(ResourceLocation complededCondition) {
        if (!this.metTierProgressions.contains(complededCondition)) {
            this.metTierProgressions.add(complededCondition);
            this.setDirty();
        }
    }

    @Override
    public float getTierProgress(Level world) {
        int req = ProgressionCondition.getCompletionRequirementForTier(world, this.getTier());
        return MathUtils.clamp01((float) this.metTierProgressions.size() / (float) req);
    }

    @Override
    public List<ResourceLocation> getCompletedProgressionSteps() {
        return this.metTierProgressions;
    }

    @Override
    public void setTierProgression(List<ResourceLocation> complededCondition) {
        this.metTierProgressions.clear();
        this.metTierProgressions.addAll(complededCondition);
        this.setDirty();
    }

    @Override
    public void setDirty() {
        this.dirty = true;
    }

    @Override
    public double getRaidChance(IFaction faction) {
        return this.forcedRaids.contains(faction) ? 1.0 : (Double) this.raidChances.getOrDefault(faction, 0.0);
    }

    @Override
    public void setRaidChance(IFaction faction, double chance) {
        this.raidChances.put(faction, chance);
    }

    @Override
    public int getRelativeRaidStrength(IFaction faction, Player player) {
        if (this.hasForceRaid()) {
            return GeneralConfigValues.ForcedRaidStrength;
        } else {
            int strength = 0;
            IPlayerMagic magic = (IPlayerMagic) player.getCapability(PlayerMagicProvider.MAGIC).orElse(null);
            if (magic != null) {
                strength = (int) ((double) magic.getMagicLevel() * 1.5);
            }
            return strength * this.tier;
        }
    }

    @Override
    public boolean canBeRaided(Player player) {
        return this.hasForceRaid() ? true : !player.isCreative() && this.getAlliedFaction() != null && ((IForgeRegistry) Registries.Factions.get()).getValues().stream().anyMatch(f -> this.canBeRaided(f, player));
    }

    @Override
    public boolean canBeRaided(IFaction faction, Player player) {
        return this.hasForceRaid() ? true : (Double) this.raidChances.getOrDefault(faction, 0.0) >= 1.0;
    }

    @Override
    public boolean hasForceRaid() {
        return this.forcedRaids.size() > 0;
    }

    @Override
    public void raidImmediate(IFaction faction) {
        if (((IForgeRegistry) Registries.Factions.get()).containsValue(faction)) {
            this.forcedRaids.add(faction);
        }
    }

    @Nullable
    @Override
    public IFaction getForceRaid() {
        return this.forcedRaids.size() > 0 ? (IFaction) this.forcedRaids.get(0) : null;
    }

    @Override
    public void clearForceRaid() {
        if (this.forcedRaids.size() > 0) {
            this.forcedRaids.remove(0);
        }
    }

    @Override
    public void incrementFactionAggro(IFaction faction, float min, float max) {
        double chance = (Double) this.raidChances.getOrDefault(faction, 0.0) + (double) min + Math.random() * (double) (max - min);
        this.raidChances.put(faction, chance);
    }

    @Nullable
    @Override
    public CodexBreadcrumb popCodexBreadcrumb() {
        return (CodexBreadcrumb) this._breadcrumbs.pollLast();
    }

    @Nullable
    @Override
    public CodexBreadcrumb peekCodexBreadcrumb() {
        return (CodexBreadcrumb) this._breadcrumbs.peekLast();
    }

    @Override
    public int breadcrumbLength() {
        return this._breadcrumbs.size();
    }

    @Override
    public void clearCodexEntryHistory() {
        this._breadcrumbs.clear();
    }

    @Override
    public void pushCodexBreadcrumb(CodexBreadcrumb.Type type, String key, int page, String... metadata) {
        if (!this._breadcrumbs.stream().anyMatch(c -> c.Type == type && c.Key == key && c.Page == page)) {
            this._breadcrumbs.add(new CodexBreadcrumb(type, key, page, metadata));
        }
    }

    @Override
    public int getTierMaxComplexity() {
        return this.getTier() == 5 ? GeneralConfigValues.Tier5ComplexityLimit : 20 * this.getTier();
    }

    @Override
    public FactionDifficultyStats getFactionDifficultyStats(IFaction faction) {
        if (!this.factionDifficultyStats.containsKey(faction)) {
            this.factionDifficultyStats.put(faction, new FactionDifficultyStats());
        }
        return (FactionDifficultyStats) this.factionDifficultyStats.get(faction);
    }

    @Override
    public void tickClassicRaids(Player player) {
        if (GeneralConfigValues.ClassicRaids && this.getAlliedFaction() != null && this.getTier() >= 3) {
            if (player.m_9236_().getGameTime() % 12000L == 0L) {
                double base_increase = GeneralConfigValues.RaidChanceBase;
                double tier_increase = GeneralConfigValues.RaidChanceTier * (double) (this.getTier() - 3);
                List<IFaction> allFactions = ((IForgeRegistry) Registries.Factions.get()).getValues().stream().toList();
                IFaction anger = (IFaction) allFactions.get((int) Math.random() * allFactions.size());
                if (anger == null || anger == this.getAlliedFaction()) {
                    return;
                }
                float aggro_amount = (float) (base_increase + tier_increase);
                this.incrementFactionAggro(anger, aggro_amount, aggro_amount);
            }
        }
    }

    @Override
    public int getAllyCooldown() {
        return this.ally_cooldown;
    }

    @Override
    public void setAllyCooldown(int cooldown) {
        this.ally_cooldown = cooldown;
    }

    @Override
    public void summonRandomAlly(Player ally) {
        Pair<EntityType<? extends IFactionEnemy<? extends Mob>>, Integer> soldier = FactionRaidRegistry.getSoldier(this.getAlliedFaction(), 20 * this.getTier());
        if (soldier != null) {
            IFactionEnemy<? extends Mob> entity = (IFactionEnemy<? extends Mob>) ((EntityType) soldier.getFirst()).create(ally.m_9236_());
            SummonUtils.setSummon((Mob) entity, ally, 1200);
            entity.setTier((Integer) soldier.getSecond());
            ((LivingEntity) entity).m_6034_(ally.m_20185_(), ally.m_20186_(), ally.m_20189_());
            ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 40, 3));
            ally.m_9236_().m_7967_((Entity) entity);
            this.setAllyCooldown(6000);
        }
    }
}