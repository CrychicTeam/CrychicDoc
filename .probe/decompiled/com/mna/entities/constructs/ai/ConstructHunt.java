package com.mna.entities.constructs.ai;

import com.mna.ManaAndArtifice;
import com.mna.Registries;
import com.mna.api.affinity.Affinity;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.entities.IFactionEnemy;
import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.IConstructConstruction;
import com.mna.api.entities.construct.ai.ConstructAITask;
import com.mna.api.entities.construct.ai.parameter.ConstructAITaskParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskBooleanParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskFilterParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskIntegerParameter;
import com.mna.api.faction.IFaction;
import com.mna.api.items.DynamicItemFilter;
import com.mna.api.tools.MATags;
import com.mna.api.tools.RLoc;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.entities.constructs.ai.base.ConstructTasks;
import com.mna.items.sorcery.ItemCrystalPhylactery;
import com.mna.tools.InventoryUtilities;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.IForgeRegistry;

public class ConstructHunt extends ConstructAITask<ConstructHunt> {

    private static final ConstructCapability[] required_capabilities = new ConstructCapability[] { ConstructCapability.CARRY };

    private static final int REQUIRED_LIGHTS_UNDERGROUND = 20;

    private static final int REQUIRED_PERCEPTION_FOR_NO_LIGHTS = 20;

    private static final float REQUIRED_BUOYANCY = 1.0F;

    protected BlockPos blockPos;

    protected Vec3 targetLook;

    private static final int WANDER_DIST = 5;

    protected int waitCount = 0;

    protected boolean isWaiting = false;

    protected int waitTime = 100;

    protected DynamicItemFilter itemFilter = new DynamicItemFilter();

    protected boolean surface;

    protected boolean flying;

    protected boolean swimming;

    protected boolean underground;

    protected int dangerLevel;

    protected List<Pair<EntityType<?>, ConstructHunt.EntitySource>> aggregatedEntityTypes;

    protected int damageToDeal = 0;

    public ConstructHunt(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon);
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return super.canUse();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.isWaiting) {
            this.setMoveTarget(this.blockPos);
            if (this.doMove()) {
                this.construct.setHunting(true);
                this.waitCount = 0;
                this.targetLook = this.construct.asEntity().m_20156_().normalize().scale(-10.0);
                this.isWaiting = true;
            }
        } else {
            this.waitCount++;
            if (this.waitCount > 40) {
                this.faceBlockPos(this.blockPos.offset((int) this.targetLook.x, (int) this.targetLook.y, (int) this.targetLook.z));
            }
            if (this.waitCount >= this.getWaitTime()) {
                this.construct.resetActions();
                if ((float) this.damageToDeal > this.construct.asEntity().m_21223_()) {
                    this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.hunt_death", new Object[0]));
                } else {
                    this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.hunt_success", new Object[0]));
                }
                ManaAndArtifice.LOGGER.error("Dealing Damage: " + this.damageToDeal);
                if (this.damageToDeal >= 1 && !this.construct.asEntity().m_6469_(this.construct.asEntity().m_269291_().fellOutOfWorld(), (float) this.damageToDeal)) {
                    this.construct.asEntity().m_142687_(Entity.RemovalReason.KILLED);
                }
                this.exitCode = 0;
                return;
            }
            if (this.waitCount == this.getWaitTime() - 30) {
                this.construct.setHunting(false);
                this.rollLoot();
                if ((float) this.damageToDeal > this.construct.asEntity().m_21223_()) {
                    this.construct.setConcerned(100);
                } else {
                    this.construct.setHappy(100);
                }
            }
        }
    }

    @Override
    public void onTaskSet() {
        super.onTaskSet();
        this.waitCount = 0;
        this.isWaiting = false;
        AbstractGolem c = this.getConstructAsEntity();
        int rX = (int) c.m_20208_(5.0);
        int rY = (int) c.m_20187_();
        int rZ = (int) c.m_20262_(5.0);
        this.blockPos = new BlockPos(rX, rY, rZ);
        BlockState state = c.m_9236_().getBlockState(this.blockPos);
        for (int count = 0; !state.m_60795_() && count < 10; count++) {
            this.blockPos = this.blockPos.offset(0, 1, 0);
            state = c.m_9236_().getBlockState(this.blockPos);
        }
        this.aggregatedEntityTypes = this.aggregateEntityTypes();
        this.damageToDeal = 0;
    }

    private void rollLoot() {
        if (this.aggregatedEntityTypes.size() == 0) {
            this.forceFail();
            this.construct.setConfused(100);
            this.construct.pushDiagnosticMessage(this.translate("mna.constructs.feedback.hunt_no_mobs", new Object[0]), null);
        } else {
            IConstructConstruction caps = this.construct.getConstructData();
            int constructIntelligence = caps.calculateIntelligence();
            int constructPerception = caps.calculatePerception();
            int constructArmor = caps.calculateArmor();
            int constructToughness = caps.calculateToughness();
            float constructHealth = this.construct.asEntity().m_21223_();
            float constructMaxHealth = this.construct.asEntity().m_21233_();
            float constructDamage = Math.max(caps.calculateDamage(), caps.calculateRangedDamage());
            int num_encounters = constructPerception / 4;
            ServerLevel world = (ServerLevel) this.construct.asEntity().m_9236_();
            for (int i = 0; i < num_encounters; i++) {
                Pair<EntityType<?>, ConstructHunt.EntitySource> encounterMob = (Pair<EntityType<?>, ConstructHunt.EntitySource>) this.aggregatedEntityTypes.get((int) (Math.random() * (double) this.aggregatedEntityTypes.size()));
                Entity e = ((EntityType) encounterMob.getFirst()).create(world);
                if (e instanceof IFactionEnemy<?> factionMob && this.construct.getOwner() != null) {
                    IPlayerProgression progression = (IPlayerProgression) this.construct.getOwner().getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
                    if (progression != null) {
                        IFaction alliedFaction = progression.getAlliedFaction();
                        if (alliedFaction != null && alliedFaction == factionMob.getFaction()) {
                            float healing = constructMaxHealth * 0.33F;
                            this.damageToDeal = (int) ((float) this.damageToDeal - healing);
                            constructHealth += healing;
                            continue;
                        }
                    }
                }
                if (e instanceof Mob mob) {
                    ItemCrystalPhylactery.addToPhylactery(this.construct, mob.m_6095_(), 1.0F, world, false);
                }
                List<ItemStack> loot = this.rollMonsterLoot(e, world, constructIntelligence, constructPerception);
                for (int l = 0; l < loot.size(); l++) {
                    ItemStack stack = (ItemStack) loot.get(l);
                    if (this.itemFilter.matches(stack)) {
                        this.insertOrDiscardItem(stack);
                    }
                }
                float damage = this.calculateRiskDamage((EntityType<?>) encounterMob.getFirst(), (ConstructHunt.EntitySource) encounterMob.getSecond(), constructArmor, constructToughness, constructDamage);
                constructHealth -= damage;
                this.damageToDeal = (int) ((float) this.damageToDeal + damage);
                if (constructHealth <= 0.0F || constructIntelligence > 16 && constructHealth <= constructMaxHealth * 0.1F * (float) (this.dangerLevel + 1)) {
                    break;
                }
            }
        }
    }

    private List<ItemStack> rollMonsterLoot(Entity e, ServerLevel world, int constructIntelligence, int constructPerception) {
        AbstractGolem c = this.construct.asEntity();
        if (e instanceof LivingEntity living) {
            ResourceLocation resourcelocation = living.getLootTable();
            LootParams.Builder lootparams$builder = new LootParams.Builder(world);
            if ((float) constructIntelligence >= 36.0F && (float) constructPerception >= 28.0F) {
                lootparams$builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, this.createFakePlayer());
            }
            lootparams$builder.withParameter(LootContextParams.DAMAGE_SOURCE, living.m_269291_().magic());
            lootparams$builder.withOptionalParameter(LootContextParams.DIRECT_KILLER_ENTITY, c);
            lootparams$builder.withOptionalParameter(LootContextParams.KILLER_ENTITY, c);
            lootparams$builder.withParameter(LootContextParams.THIS_ENTITY, living);
            lootparams$builder.withParameter(LootContextParams.ORIGIN, c.m_20182_());
            LootParams lootparams = lootparams$builder.create(LootContextParamSets.ENTITY);
            LootTable loottable = world.getServer().getLootData().m_278676_(resourcelocation);
            return loottable.getRandomItems(lootparams);
        } else {
            return new ArrayList();
        }
    }

    private float calculateRiskDamage(EntityType<?> entity, ConstructHunt.EntitySource source, int armor, int toughness, float constructDamage) {
        int difficulty = 0;
        while (difficulty < MATags.EntityTypes.Constructs.HUNT_DANGER_LEVELS.length && !MATags.getEntitiesOnTag(MATags.EntityTypes.Constructs.HUNT_DANGER_LEVELS[difficulty]).contains(entity)) {
            difficulty++;
        }
        float riskDamage = (float) (Math.random() * 5.0 * (double) (difficulty + 1));
        switch(source) {
            case Flying:
            case Swimming:
                riskDamage *= 1.5F;
                break;
            case Underground:
                riskDamage *= 1.5F;
        }
        float armorFactor = (float) armor / 5.0F;
        float toughnessFactor = (float) armor - riskDamage * 4.0F / (float) (toughness + 8);
        float mitigationFactor = 1.0F - Math.min(20.0F, Math.max(armorFactor, toughnessFactor)) / 25.0F;
        riskDamage *= mitigationFactor;
        float constructDamageOutputMitigation = 1.0F - Math.min(constructDamage, 40.0F) / 50.0F;
        riskDamage *= constructDamageOutputMitigation;
        switch(source) {
            case Flying:
                riskDamage -= (float) this.construct.getConstructData().getAffinityScore(Affinity.WIND);
                riskDamage -= (float) this.construct.getConstructData().getAffinityScore(Affinity.ARCANE);
                break;
            case Swimming:
                riskDamage -= (float) this.construct.getConstructData().getAffinityScore(Affinity.WATER);
                break;
            case Underground:
                riskDamage -= (float) this.construct.getConstructData().getAffinityScore(Affinity.EARTH);
                riskDamage -= (float) this.construct.getConstructData().getAffinityScore(Affinity.FIRE);
                riskDamage -= (float) this.construct.getConstructData().getAffinityScore(Affinity.ENDER);
                break;
            case Surface:
                riskDamage -= (float) this.construct.getConstructData().getAffinityScore(Affinity.EARTH);
        }
        return Math.max(riskDamage, 0.0F);
    }

    private int getWaitTime() {
        return !this.construct.getConstructData().areCapabilitiesEnabled(ConstructCapability.ITEM_STORAGE) ? this.getInteractTime(ConstructCapability.ADVENTURE, 4800) : this.getInteractTime(ConstructCapability.ADVENTURE, 9600);
    }

    private ArrayList<Pair<EntityType<?>, ConstructHunt.EntitySource>> aggregateEntityTypes() {
        ArrayList<Pair<EntityType<?>, ConstructHunt.EntitySource>> types = new ArrayList();
        ServerLevel serverWorld = (ServerLevel) this.construct.asEntity().m_9236_();
        IConstructConstruction caps = this.construct.getConstructData();
        if (this.surface) {
            this.getTagForDimensionAndFlag(serverWorld, "surface").forEach(et -> types.add(new Pair(et, ConstructHunt.EntitySource.Surface)));
        }
        if (this.flying && (caps.areCapabilitiesEnabled(ConstructCapability.FLY) || caps.areCapabilitiesEnabled(ConstructCapability.RANGED_ATTACK) || caps.areCapabilitiesEnabled(ConstructCapability.TELEPORT))) {
            this.getTagForDimensionAndFlag(serverWorld, "flying").forEach(et -> types.add(new Pair(et, ConstructHunt.EntitySource.Flying)));
        }
        if (this.swimming && (caps.areCapabilitiesEnabled(ConstructCapability.FISH) || caps.calculateBuoyancy() >= 1.0F)) {
            this.getTagForDimensionAndFlag(serverWorld, "swimming").forEach(et -> types.add(new Pair(et, ConstructHunt.EntitySource.Flying)));
        }
        if (this.underground && (caps.calculatePerception() >= 20 || this.construct.hasItem(MATags.Items.Constructs.UNDERGROUND_LIGHTS, 20))) {
            this.getTagForDimensionAndFlag(serverWorld, "underground").forEach(et -> types.add(new Pair(et, ConstructHunt.EntitySource.Flying)));
        }
        ArrayList<Pair<EntityType<?>, ConstructHunt.EntitySource>> typesFiltered = this.filterTagToRiskLevel(types);
        if (typesFiltered.size() == 0) {
            this.construct.setConfused(100);
            this.construct.pushDiagnosticMessage(this.translate("mna.constructs.feedback.hunt_no_mobs", new Object[0]), null);
            this.forceFail();
        }
        if (this.underground && caps.calculatePerception() < 20) {
            InventoryUtilities.consumeByTag(this.construct, MATags.Items.Constructs.UNDERGROUND_LIGHTS, 20);
        }
        return typesFiltered;
    }

    private List<EntityType<?>> getTagForDimensionAndFlag(ServerLevel level, String flag) {
        ResourceLocation mobsForFlagTag = RLoc.create("constructs/hunt/" + flag + "/" + level.m_46472_().location().toString().replace(':', '_'));
        if (MATags.doesEntityTagExist(mobsForFlagTag)) {
            return MATags.getEntitiesOnTag(mobsForFlagTag);
        } else {
            ResourceLocation overworldFallback = RLoc.create("constructs/hunt/" + flag + "/overworld");
            return (List<EntityType<?>>) (MATags.doesEntityTagExist(overworldFallback) ? MATags.getEntitiesOnTag(overworldFallback) : new ArrayList());
        }
    }

    private ArrayList<Pair<EntityType<?>, ConstructHunt.EntitySource>> filterTagToRiskLevel(ArrayList<Pair<EntityType<?>, ConstructHunt.EntitySource>> originalTag) {
        ArrayList<EntityType<?>> mobsByDangerLevel = new ArrayList();
        for (int i = 0; i < this.dangerLevel; i++) {
            mobsByDangerLevel.addAll(MATags.getEntitiesOnTag(MATags.EntityTypes.Constructs.HUNT_DANGER_LEVELS[i]));
        }
        return (ArrayList<Pair<EntityType<?>, ConstructHunt.EntitySource>>) originalTag.stream().filter(et -> mobsByDangerLevel.contains(et.getFirst())).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public ResourceLocation getType() {
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.HUNT);
    }

    @Override
    public CompoundTag writeInternal(CompoundTag nbt) {
        nbt.putBoolean("surface", this.surface);
        nbt.putBoolean("flying", this.flying);
        nbt.putBoolean("swimming", this.swimming);
        nbt.putBoolean("underground", this.underground);
        return nbt;
    }

    @Override
    public void readNBT(CompoundTag nbt) {
        this.surface = nbt.getBoolean("surface");
        this.flying = nbt.getBoolean("flying");
        this.swimming = nbt.getBoolean("swimming");
        this.underground = nbt.getBoolean("underground");
    }

    @Override
    public boolean isFullyConfigured() {
        return this.surface || this.flying || this.swimming || this.underground;
    }

    @Override
    protected List<ConstructAITaskParameter> instantiateParameters() {
        List<ConstructAITaskParameter> parameters = super.instantiateParameters();
        parameters.add(new ConstructTaskIntegerParameter("hunt.danger", 1, 5));
        parameters.add(new ConstructTaskBooleanParameter("hunt.surface", true));
        parameters.add(new ConstructTaskBooleanParameter("hunt.underground", false));
        parameters.add(new ConstructTaskBooleanParameter("hunt.swimming", false));
        parameters.add(new ConstructTaskBooleanParameter("hunt.flying", false));
        parameters.add(new ConstructTaskFilterParameter("hunt.items"));
        return parameters;
    }

    @Override
    public void inflateParameters() {
        this.getParameter("hunt.danger").ifPresent(param -> {
            if (param instanceof ConstructTaskIntegerParameter intParam) {
                this.dangerLevel = intParam.getValue();
            }
        });
        this.getParameter("hunt.surface").ifPresent(param -> {
            if (param instanceof ConstructTaskBooleanParameter boolParam) {
                this.surface = boolParam.getValue();
            }
        });
        this.getParameter("hunt.underground").ifPresent(param -> {
            if (param instanceof ConstructTaskBooleanParameter boolParam) {
                this.underground = boolParam.getValue();
            }
        });
        this.getParameter("hunt.swimming").ifPresent(param -> {
            if (param instanceof ConstructTaskBooleanParameter boolParam) {
                this.swimming = boolParam.getValue();
            }
        });
        this.getParameter("hunt.flying").ifPresent(param -> {
            if (param instanceof ConstructTaskBooleanParameter boolParam) {
                this.flying = boolParam.getValue();
            }
        });
        this.getParameter("hunt.items").ifPresent(param -> {
            if (param instanceof ConstructTaskFilterParameter filterParam) {
                this.itemFilter.copyFrom(filterParam.getValue());
            }
        });
    }

    public ConstructHunt copyFrom(ConstructAITask<?> other) {
        if (other instanceof ConstructHunt hunt) {
            this.surface = hunt.surface;
            this.flying = hunt.flying;
            this.swimming = hunt.swimming;
            this.underground = hunt.underground;
            this.dangerLevel = hunt.dangerLevel;
            this.itemFilter.copyFrom(hunt.itemFilter);
        }
        return this;
    }

    public ConstructHunt duplicate() {
        return new ConstructHunt(this.construct, this.guiIcon).copyFrom(this);
    }

    @Override
    public ConstructCapability[] requiredCapabilities() {
        return required_capabilities;
    }

    @Override
    public int getRequiredIntelligence() {
        return 16;
    }

    @Override
    public boolean areCapabilitiesMet() {
        return this.construct.getConstructData().areCapabilitiesEnabled(ConstructCapability.CARRY) ? true : this.construct.getConstructData().areCapabilitiesEnabled(ConstructCapability.ITEM_STORAGE);
    }

    private static enum EntitySource {

        Surface, Underground, Flying, Swimming
    }
}