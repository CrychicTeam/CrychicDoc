package com.mna.entities.rituals;

import com.mna.ManaAndArtifice;
import com.mna.Registries;
import com.mna.advancements.CustomAdvancementTriggers;
import com.mna.api.config.GeneralConfigValues;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.recipes.IManaweavePattern;
import com.mna.api.recipes.IRitualRecipe;
import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.IRitualReagent;
import com.mna.api.rituals.RitualBlockPos;
import com.mna.api.rituals.RitualEffect;
import com.mna.api.sound.SFX;
import com.mna.blocks.BlockInit;
import com.mna.blocks.ritual.ChalkRuneBlock;
import com.mna.blocks.tileentities.ChalkRuneTile;
import com.mna.capabilities.chunkdata.ChunkMagicProvider;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.entities.manaweaving.Manaweave;
import com.mna.events.EventDispatcher;
import com.mna.network.ServerMessageDispatcher;
import com.mna.recipes.manaweaving.ManaweavingPattern;
import com.mna.recipes.manaweaving.ManaweavingPatternHelper;
import com.mna.recipes.rituals.RitualRecipe;
import com.mna.rituals.RitualBlockPosComparator;
import com.mna.rituals.RitualReagent;
import com.mna.rituals.contexts.RitualContext;
import com.mna.rituals.contexts.RitualReagentReplaceContext;
import com.mna.sound.EntityAliveLoopingSound;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.IForgeRegistry;

public class Ritual extends Entity {

    private static final EntityDataAccessor<CompoundTag> BLOCK_LOCATIONS = SynchedEntityData.defineId(Ritual.class, EntityDataSerializers.COMPOUND_TAG);

    private static final EntityDataAccessor<CompoundTag> VALID_LOCATIONS = SynchedEntityData.defineId(Ritual.class, EntityDataSerializers.COMPOUND_TAG);

    private static final EntityDataAccessor<CompoundTag> COLLECTED_REAGENTS = SynchedEntityData.defineId(Ritual.class, EntityDataSerializers.COMPOUND_TAG);

    private static final EntityDataAccessor<Byte> RITUAL_STATE = SynchedEntityData.defineId(Ritual.class, EntityDataSerializers.BYTE);

    private static final EntityDataAccessor<Integer> SHUTDOWN_AGE = SynchedEntityData.defineId(Ritual.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Float> SPEED = SynchedEntityData.defineId(Ritual.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<String> RITUAL_NAME = SynchedEntityData.defineId(Ritual.class, EntityDataSerializers.STRING);

    private static final EntityDataAccessor<String> CASTER_UUID = SynchedEntityData.defineId(Ritual.class, EntityDataSerializers.STRING);

    private static final EntityDataAccessor<String> REQUESTED_PATTERN = SynchedEntityData.defineId(Ritual.class, EntityDataSerializers.STRING);

    private static final EntityDataAccessor<Integer> CURRENT_ENTITY = SynchedEntityData.defineId(Ritual.class, EntityDataSerializers.INT);

    public static final byte MODE_ALL = 0;

    public static final byte MODE_INDEXED = 1;

    public static final byte MODE_INDEXED_DISPLAY = 2;

    public static final int REAGENT_ADVANCE_RATE = 20;

    public static final int RADIANT_ADVANCE_RATE = 10;

    public static final int BEAM_ADVANCE_RATE = 3;

    public static final float SPEED_NORMAL = 1.0F;

    public static float SPEED_FAST = 0.1F;

    private int age = 0;

    private int stageTicks = 0;

    final RitualBlockPosComparator comp;

    private NonNullList<Pair<BlockPos, ItemStack>> collectedReagents;

    private NonNullList<ResourceLocation> collectedPatterns;

    private NonNullList<ResourceLocation> requiredPatterns;

    private Ritual.RitualState prevState;

    private Ritual.RitualState curState;

    private boolean forceCollectAllReagents = false;

    private Entity __patternEntity;

    private int patternConsumeTicks = 0;

    private int dispatchTries = 0;

    private NonNullList<RitualEffect> __cachedRitualHandlers;

    private RitualRecipe __cachedCurrentRitual;

    private boolean __dynamicReagentsCached = false;

    public long worldTimeAtReagentCollectStart = 0L;

    private ItemStack cachedDynamicStack = ItemStack.EMPTY;

    private boolean dynamicItemChanged = false;

    public Ritual(EntityType<? extends Ritual> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
        this.m_20331_(true);
        this.comp = new RitualBlockPosComparator();
        this.collectedReagents = NonNullList.create();
        this.collectedPatterns = NonNullList.create();
        this.requiredPatterns = NonNullList.create();
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void runStateLogic() {
        try {
            switch(this.curState) {
                case POWERING_UP:
                    this.logic_statePoweringUp();
                    break;
                case COLLECTING_REAGENTS:
                    this.logic_stateGatheringReagents();
                    break;
                case COLLECTING_PATTERNS:
                    this.logic_stateGatheringPatterns();
                    break;
                case COLLAPSING:
                    this.logic_collapsing();
                    break;
                case PROCESSING_RITUAL:
                    this.logic_processRitual();
                    break;
                case COMPLETING:
                    this.logic_complete();
            }
        } catch (Throwable var4) {
            try {
                Player p = this.getCaster();
                if (p != null) {
                    p.m_213846_(Component.translatable("ritual.mna.fatal_error"));
                }
            } catch (Throwable var3) {
                ManaAndArtifice.LOGGER.error("Tried to send a message to the player performing the ritual about the following error, but it failed.");
                ManaAndArtifice.LOGGER.error(var3);
            }
            ManaAndArtifice.LOGGER.error("A critical error has been caught during a ritual.  The ritual was terminated to preserve stability.");
            ManaAndArtifice.LOGGER.error(var4);
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    private void logic_statePoweringUp() {
        if (this.getAge() % 5 == 0) {
            this.checkForExplosion(true);
        }
        if (this.getStageTicks() >= this.getPowerupTime()) {
            this.setState(Ritual.RitualState.COLLECTING_REAGENTS);
        }
    }

    private void logic_stateGatheringReagents() {
        if (this.getAge() % 5 == 0) {
            this.checkForExplosion(false);
        }
        if (this.getStageTicks() == 10) {
            for (RitualBlockPos rbp : this.getRitualData((byte) 1)) {
                BlockEntity te = this.m_9236_().getBlockEntity(rbp.getBlockPos());
                if (te != null && te instanceof ChalkRuneTile) {
                    ChalkRuneTile teCr = (ChalkRuneTile) te;
                    if (rbp.isPresent() && !rbp.getReagent().isEmpty()) {
                        if (teCr.MatchesReagent(rbp.getReagent().getResourceLocation())) {
                            if (!rbp.getReagent().shouldConsumeReagent() && !rbp.getReagent().isManualReturn() && !this.forceCollectAllReagents) {
                                this.collectedReagents.add(new Pair(rbp.getBlockPos(), teCr.m_8020_(0).copy()));
                            } else {
                                ItemStack stack = teCr.m_8020_(0).copy();
                                teCr.clearStack();
                                this.collectedReagents.add(new Pair(rbp.getBlockPos(), stack));
                                BlockState state = this.m_9236_().getBlockState(rbp.getBlockPos());
                                this.m_9236_().sendBlockUpdated(rbp.getBlockPos(), state, state, 2);
                            }
                        } else {
                            this.collectedReagents.add(new Pair(rbp.getBlockPos(), new ItemStack(Items.AIR)));
                        }
                    }
                    teCr.setReadOnly(true);
                }
            }
            if (!this.m_9236_().isClientSide()) {
                ListTag list = this.writeCollectedReagents();
                CompoundTag compound = new CompoundTag();
                compound.put("data", list);
                this.f_19804_.set(COLLECTED_REAGENTS, compound);
                ServerMessageDispatcher.sendRitualReagentData(compound, this.m_19879_(), (ServerLevel) this.m_9236_(), this.m_20183_());
            }
        }
        if (this.getStageTicks() >= 40) {
            this.setState(Ritual.RitualState.COLLECTING_PATTERNS);
        }
    }

    private void logic_stateGatheringPatterns() {
        if (this.getAge() % 5 == 0) {
            this.checkForExplosion(false);
        }
        if (this.requiredPatterns.size() == 0) {
            this.setState(Ritual.RitualState.COLLAPSING);
        } else {
            ManaweavingPattern pattern = this.getRequestedPattern();
            if (pattern == null) {
                this.setRequestedPattern(this.requiredPatterns.get(0).toString());
            } else {
                if (this.getCurrentEntityTarget() == null) {
                    if (this.getAge() % 10 == 0) {
                        this.findPatternEntity();
                    }
                } else {
                    this.patternConsumeTicks++;
                    if (this.patternConsumeTicks >= 20) {
                        this.getCurrentEntityTarget().remove(Entity.RemovalReason.DISCARDED);
                        this.setRequestedPattern("");
                        this.patternConsumeTicks = 0;
                        this.collectedPatterns.add(this.requiredPatterns.get(0));
                        this.requiredPatterns.remove(0);
                    }
                }
            }
        }
    }

    private void logic_collapsing() {
        if (this.getStageTicks() >= 20) {
            this.setState(Ritual.RitualState.PROCESSING_RITUAL);
        }
    }

    private void logic_processRitual() {
        Player ritualCaster = this.getCaster();
        if (ritualCaster == null && this.dispatchTries < 10) {
            this.dispatchTries++;
        } else {
            if (ritualCaster == null) {
                ManaAndArtifice.LOGGER.error("Failed to parse UUID for ritual caster.  Giving up and completing the ritual anyway, but some effects may not apply!");
            }
            for (RitualEffect eff : this.getHandlers()) {
                eff.onRitualCompleted(new RitualContext(this.getCaster(), this));
            }
            int shutdownTime = 0;
            for (RitualEffect eff : this.getHandlers()) {
                int time = eff.getRitualCompleteDelay(new RitualContext(this.getCaster(), this));
                if (time > shutdownTime) {
                    shutdownTime = time;
                }
            }
            if (shutdownTime < 0) {
                shutdownTime = 0;
            }
            this.setShutdownAge(shutdownTime);
            this.setState(Ritual.RitualState.COMPLETING);
        }
    }

    private void logic_complete() {
        if (this.m_6084_() && this.getStageTicks() > this.getShutdownAge()) {
            NonNullList<RitualBlockPos> positions = this.getRitualData((byte) 0);
            for (int i = 0; i < positions.size(); i++) {
                BlockState runeState = this.m_9236_().getBlockState(positions.get(i).getBlockPos());
                if (runeState.m_60734_() == BlockInit.CHALK_RUNE.get()) {
                    ChalkRuneTile tecr = (ChalkRuneTile) this.m_9236_().getBlockEntity(positions.get(i).getBlockPos());
                    if (tecr != null) {
                        if ((Boolean) runeState.m_61143_(ChalkRuneBlock.METAL)) {
                            tecr.setReadOnly(false);
                            this.m_9236_().setBlockAndUpdate(positions.get(i).getBlockPos(), (BlockState) runeState.m_61124_(ChalkRuneBlock.ACTIVATED, false));
                        } else {
                            if (!tecr.isGhostItem()) {
                                Containers.dropContents(this.m_9236_(), positions.get(i).getBlockPos(), tecr);
                            }
                            this.m_9236_().setBlockAndUpdate(positions.get(i).getBlockPos(), Blocks.AIR.defaultBlockState());
                        }
                    }
                }
            }
            if (this.getCurrentRitual() != null && this.getCaster() != null) {
                EventDispatcher.DispatchRitualComplete(this.getCurrentRitual(), this.getHandlers(), this.m_20183_(), (List<ItemStack>) this.collectedReagents.stream().map(e -> (ItemStack) e.getSecond()).collect(Collectors.toList()), this.getCaster());
                this.getCaster().getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> this.getCaster().getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> m.addMagicXP(10, this.getCaster(), p)));
                this.executeCommand(this.getCurrentRitual());
                if (this.getCaster() instanceof ServerPlayer) {
                    CustomAdvancementTriggers.PERFORM_RITUAL.trigger((ServerPlayer) this.getCaster(), this.getCurrentRitual().m_6423_(), this.getCurrentRitual().getTier());
                }
            }
            this.m_9236_().getChunkAt(this.m_20183_()).getCapability(ChunkMagicProvider.MAGIC).ifPresent(cm -> cm.addResidualMagic(this.__cachedCurrentRitual != null ? (float) (this.__cachedCurrentRitual.getLowerBound() * 50) : 50.0F));
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    @Override
    public void tick() {
        this.prevState = this.curState;
        this.curState = this.getState();
        this.setStageTicks(this.getStageTicks() + 1);
        if (this.curState == Ritual.RitualState.GUIDING_REAGENT_PLACEMENT) {
            if (!this.m_9236_().isClientSide() && !this.__dynamicReagentsCached && this.stageTicks % 20 == 0) {
                this.checkRitualIntegrity(true);
            }
        } else {
            this.setAge(this.getAge() + 1);
            if (this.prevState != this.curState) {
                this.setStageTicks(0);
            }
            if (this.m_9236_().isClientSide()) {
                this.SpawnParticles();
                this.PlaySounds();
            } else {
                this.runStateLogic();
            }
        }
    }

    public boolean confirmRitualReagents() {
        if (this.getState() != Ritual.RitualState.GUIDING_REAGENT_PLACEMENT) {
            return false;
        } else {
            boolean canStart = true;
            ArrayList<BlockPos> validLocations = new ArrayList();
            NonNullList<RitualBlockPos> ritualData = this.getRitualData((byte) 1, true);
            if (this.dynamicItemChanged) {
                this.cancelRitual(true);
                return false;
            } else {
                for (RitualBlockPos pos : ritualData) {
                    if (pos.isPresent() && !pos.getReagent().isOptional() && this.m_9236_().isLoaded(pos.getBlockPos())) {
                        ChalkRuneTile te = (ChalkRuneTile) this.m_9236_().getBlockEntity(pos.getBlockPos());
                        if (te == null) {
                            return false;
                        }
                        boolean match = te.MatchesReagent(pos.getReagent().getResourceLocation());
                        if (match) {
                            validLocations.add(pos.getBlockPos());
                        }
                        canStart &= match;
                    }
                }
                this.setValidReagentLocations(validLocations);
                if (canStart) {
                    this.setState(Ritual.RitualState.POWERING_UP);
                }
                return canStart;
            }
        }
    }

    @Nullable
    public IRitualReagent getReagentForPosition(BlockPos pos) {
        if (this.getState() != Ritual.RitualState.GUIDING_REAGENT_PLACEMENT) {
            return null;
        } else {
            Optional<RitualBlockPos> dataPos = this.getRitualData((byte) 1).stream().filter(rbp -> rbp.getBlockPos().equals(pos)).findFirst();
            if (dataPos.isPresent() && this.m_9236_().isLoaded(pos)) {
                ChalkRuneTile te = (ChalkRuneTile) this.m_9236_().getBlockEntity(((RitualBlockPos) dataPos.get()).getBlockPos());
                return te == null ? null : ((RitualBlockPos) dataPos.get()).getReagent();
            } else {
                return null;
            }
        }
    }

    public boolean checkForExplosion(boolean checkReagents) {
        if (this.getState() == Ritual.RitualState.COMPLETING || this.getState() == Ritual.RitualState.PROCESSING_RITUAL) {
            return false;
        } else if (!this.m_6084_()) {
            return false;
        } else {
            boolean integrityCheck = this.checkRitualIntegrity(checkReagents);
            if (!integrityCheck) {
                this.cancelRitual(true);
                if (this.getState() != Ritual.RitualState.GUIDING_REAGENT_PLACEMENT) {
                    float radius = this.__cachedCurrentRitual == null ? 2.5F : (float) this.__cachedCurrentRitual.getLowerBound();
                    this.m_9236_().explode(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), radius, this.m_9236_().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) ? Level.ExplosionInteraction.MOB : Level.ExplosionInteraction.NONE);
                    this.setState(Ritual.RitualState.DEAD);
                }
                return false;
            } else {
                return true;
            }
        }
    }

    private boolean checkRitualIntegrity(boolean checkReagents) {
        NonNullList<RitualBlockPos> locations = this.getRitualData((byte) 0);
        for (int i = 0; i < locations.size(); i++) {
            if (!this.m_9236_().isLoaded(locations.get(i).getBlockPos()) || this.m_9236_().getBlockState(locations.get(i).getBlockPos()).m_60734_() != BlockInit.CHALK_RUNE.get().defaultBlockState().m_60734_()) {
                return false;
            }
            if (checkReagents && locations.get(i).getIndex() >= 0 && locations.get(i).isPresent() && !locations.get(i).getReagent().isOptional()) {
                ChalkRuneTile te = (ChalkRuneTile) this.m_9236_().getBlockEntity(locations.get(i).getBlockPos());
                if (!te.MatchesReagent(locations.get(i).getReagent().getResourceLocation())) {
                    return false;
                }
            }
        }
        return true;
    }

    private void findPatternEntity() {
        Optional<Manaweave> patternEntity = this.m_9236_().getEntities(this, this.m_20191_().inflate(10.0), entity -> {
            if (!(entity instanceof Manaweave mw)) {
                return false;
            } else {
                ArrayList<IManaweavePattern> p = mw.getPatterns();
                return p.size() != 1 ? false : ((IManaweavePattern) p.get(0)).getRegistryId().toString().equals(this.getRequestedPattern().m_6423_().toString());
            }
        }).stream().map(e -> (Manaweave) e).findFirst();
        if (patternEntity != null && patternEntity.isPresent()) {
            this.setCurrentEntityTarget((Entity) patternEntity.get());
            ((Manaweave) patternEntity.get()).setMerging(true);
        }
    }

    public boolean cancelRitual() {
        return this.cancelRitual(false);
    }

    private boolean cancelRitual(boolean ignoreState) {
        if (!this.m_9236_().isClientSide() && (ignoreState || this.getState() == Ritual.RitualState.GUIDING_REAGENT_PLACEMENT)) {
            for (RitualBlockPos pos : this.getRitualData((byte) 0)) {
                BlockEntity te = this.m_9236_().getBlockEntity(pos.getBlockPos());
                if (te != null && te instanceof ChalkRuneTile) {
                    if (this.m_9236_().getBlockState(pos.getBlockPos()).m_60734_() == BlockInit.CHALK_RUNE.get()) {
                        ((ChalkRuneTile) te).setReadOnly(false);
                        this.m_9236_().setBlockAndUpdate(pos.getBlockPos(), (BlockState) this.m_9236_().getBlockState(pos.getBlockPos()).m_61124_(ChalkRuneBlock.ACTIVATED, false));
                    } else {
                        this.m_9236_().setBlockAndUpdate(pos.getBlockPos(), Blocks.AIR.defaultBlockState());
                    }
                }
            }
            this.m_142687_(Entity.RemovalReason.DISCARDED);
            return true;
        } else {
            return false;
        }
    }

    public void executeCommand(IRitualRecipe recipe) {
        if (recipe.hasCommand()) {
            MinecraftServer minecraftserver = this.m_9236_().getServer();
            if (minecraftserver.isCommandBlockEnabled()) {
                try {
                    CommandSourceStack css = new CommandSourceStack(this, this.m_20182_(), this.m_20155_(), this.m_9236_() instanceof ServerLevel ? (ServerLevel) this.m_9236_() : null, GeneralConfigValues.RitualPermissionLevel, this.m_7755_().getString(), this.m_5446_(), this.m_9236_().getServer(), this.getCaster());
                    minecraftserver.getCommands().performPrefixedCommand(css, recipe.getCommand());
                } catch (Throwable var6) {
                    CrashReport crashreport = CrashReport.forThrowable(var6, "Executing ritual command");
                    CrashReportCategory crashreportcategory = crashreport.addCategory("Command to be executed");
                    crashreportcategory.setDetail("Command", recipe.getCommand());
                    throw new ReportedException(crashreport);
                }
            }
        }
    }

    private void PlaySounds() {
        if (this.curState == Ritual.RitualState.POWERING_UP && this.stageTicks == 0) {
            this.PlaySound(SFX.Spell.Cast.ARCANE);
            if (this.getHandlers().size() > 0) {
                RitualEffect effect = this.getHandlers().get((int) (Math.random() * (double) this.getHandlers().size()));
                this.PlayLoopingSound(effect.getLoopSound(new RitualContext(this.getCaster(), this)));
            } else {
                this.PlayLoopingSound(SFX.Loops.ENDER);
            }
        }
        if (this.curState.ordinal() >= Ritual.RitualState.POWERING_UP.ordinal() && this.curState != Ritual.RitualState.COMPLETING) {
            int radiant_ticks = (int) Math.ceil((double) (10.0F * this.getSpeed()));
            int maxPos = this.age / radiant_ticks;
            int points = this.getRitualData((byte) 2).size();
            if (maxPos < points + 1 && this.age % radiant_ticks == 0) {
                this.PlaySound(SFX.Ritual.Effects.RITUAL_POINT_APPEAR);
            }
            int beamAge = this.age - radiant_ticks * points;
            if (beamAge == 5) {
                this.PlaySound(SFX.Spell.Cast.ENDER);
            }
        }
    }

    private void PlaySound(SoundEvent soundID) {
        this.m_9236_().playLocalSound(this.m_20185_(), this.m_20186_(), this.m_20189_(), soundID, SoundSource.PLAYERS, 1.0F, 1.0F, false);
    }

    @OnlyIn(Dist.CLIENT)
    private void PlayLoopingSound(SoundEvent soundID) {
        Minecraft.getInstance().getSoundManager().play(new EntityAliveLoopingSound(soundID, this));
    }

    private void SpawnParticles() {
        boolean useDefault = false;
        IRitualContext context = new RitualContext(this.getCaster(), this);
        for (RitualEffect effect : this.getHandlers()) {
            useDefault |= !effect.spawnRitualParticles(context);
        }
        Ritual.RitualState curState = this.getState();
        NonNullList<RitualBlockPos> positions = this.getRitualData((byte) 1);
        for (int i = 0; i < positions.size(); i++) {
            BlockPos cur = positions.get(i).getBlockPos();
            if (useDefault && curState.ordinal() > Ritual.RitualState.POWERING_UP.ordinal()) {
                this.m_9236_().addParticle(new MAParticleType(ParticleInit.SPARKLE_VELOCITY.get()), (double) cur.m_123341_() + Math.random(), (double) cur.m_123342_(), (double) cur.m_123343_() + Math.random(), 0.0, Math.random() * 0.05F + 0.05F, 0.0);
            }
            if (curState == Ritual.RitualState.COLLECTING_PATTERNS && this.getCurrentEntityTarget() != null) {
                this.m_9236_().addParticle(new MAParticleType(ParticleInit.SPARKLE_LERP_POINT.get()), -0.5 + this.getCurrentEntityTarget().getX() + Math.random(), -0.5 + this.getCurrentEntityTarget().getY() + Math.random(), -0.5 + this.getCurrentEntityTarget().getZ() + Math.random(), this.m_20185_(), this.m_20186_() + 1.0, this.m_20189_());
            }
            if (this.getStageTicks() == this.getShutdownAge()) {
                for (int j = 0; j < 10; j++) {
                    this.m_9236_().addParticle(new MAParticleType(ParticleInit.SPARKLE_RANDOM.get()), (double) cur.m_123341_() + Math.random(), (double) cur.m_123342_(), (double) cur.m_123343_() + Math.random(), 0.1F, 0.1F, 0.1F);
                }
            }
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(BLOCK_LOCATIONS, new CompoundTag());
        this.f_19804_.define(RITUAL_STATE, (byte) Ritual.RitualState.GUIDING_REAGENT_PLACEMENT.ordinal());
        this.f_19804_.define(RITUAL_NAME, "");
        this.f_19804_.define(SHUTDOWN_AGE, -1);
        this.f_19804_.define(SPEED, 1.0F);
        this.f_19804_.define(CASTER_UUID, "");
        this.f_19804_.define(REQUESTED_PATTERN, "");
        this.f_19804_.define(CURRENT_ENTITY, -1);
        this.f_19804_.define(VALID_LOCATIONS, new CompoundTag());
        this.f_19804_.define(COLLECTED_REAGENTS, new CompoundTag());
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (this.m_9236_().isClientSide() && key == COLLECTED_REAGENTS) {
            ListTag data = this.f_19804_.get(COLLECTED_REAGENTS).getList("data", 10);
            this.readCollectedReagents(data);
        } else if (this.m_9236_().isClientSide() && key == RITUAL_STATE && this.getState() == Ritual.RitualState.COLLECTING_REAGENTS) {
            this.worldTimeAtReagentCollectStart = this.m_9236_().getGameTime();
        }
        super.onSyncedDataUpdated(key);
    }

    private void WriteBlockPosToNBT(String key, RitualBlockPos pos, CompoundTag nbt) {
        CompoundTag subNBT = new CompoundTag();
        subNBT.putInt("idx", pos.getIndex());
        subNBT.putInt("d_idx", pos.getDisplayIndex());
        subNBT.putInt("x", pos.getBlockPos().m_123341_());
        subNBT.putInt("y", pos.getBlockPos().m_123342_());
        subNBT.putInt("z", pos.getBlockPos().m_123343_());
        if (pos.isPresent()) {
            ((RitualReagent) pos.getReagent()).writeToNBT(subNBT);
        }
        nbt.put(key, subNBT);
    }

    private RitualBlockPos ReadBlockPosFromNBT(String key, CompoundTag nbt) {
        if (!nbt.contains(key)) {
            return null;
        } else {
            CompoundTag subNBT = nbt.getCompound(key);
            return subNBT.contains("x") && subNBT.contains("y") && subNBT.contains("z") && subNBT.contains("idx") && subNBT.contains("d_idx") ? new RitualBlockPos(subNBT.getInt("idx"), subNBT.getInt("d_idx"), new BlockPos(subNBT.getInt("x"), subNBT.getInt("y"), subNBT.getInt("z")), RitualReagent.fromNBT(subNBT)) : null;
        }
    }

    public void readCollectedReagents(ListTag data) {
        this.collectedReagents.clear();
        for (int i = 0; i < data.size(); i++) {
            CompoundTag entry = data.getCompound(i);
            if (entry != null && entry.contains("pos") && entry.contains("stack")) {
                BlockPos pos = BlockPos.of(entry.getLong("pos"));
                ItemStack stack = ItemStack.of(entry.getCompound("stack"));
                if (pos != null && stack != null) {
                    this.collectedReagents.add(new Pair(pos, stack));
                }
            }
        }
        this.worldTimeAtReagentCollectStart = this.m_9236_().getGameTime();
        if (!this.m_9236_().isClientSide()) {
            ListTag list = this.writeCollectedReagents();
            CompoundTag compound = new CompoundTag();
            compound.put("data", list);
            this.f_19804_.set(COLLECTED_REAGENTS, compound);
        }
    }

    private ListTag writeCollectedReagents() {
        ListTag collected_reagents = new ListTag();
        for (Pair<BlockPos, ItemStack> e : this.collectedReagents) {
            CompoundTag entry = new CompoundTag();
            entry.putLong("pos", ((BlockPos) e.getFirst()).asLong());
            entry.put("stack", ((ItemStack) e.getSecond()).save(new CompoundTag()));
            collected_reagents.add(entry);
        }
        return collected_reagents;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("ritual_data")) {
            CompoundTag ritualData = compound.getCompound("ritual_data");
            if (ritualData.contains("shutdown_age")) {
                this.f_19804_.set(SHUTDOWN_AGE, ritualData.getInt("shutdown_age"));
            }
            if (ritualData.contains("ritual_name")) {
                this.f_19804_.set(RITUAL_NAME, ritualData.getString("ritual_name"));
            }
            if (ritualData.contains("state")) {
                this.f_19804_.set(RITUAL_STATE, ritualData.getByte("state"));
            }
            if (ritualData.contains("speed")) {
                this.f_19804_.set(SPEED, ritualData.getFloat("speed"));
            }
            if (ritualData.contains("caster")) {
                this.f_19804_.set(CASTER_UUID, ritualData.getString("caster"));
            }
            if (ritualData.contains("block_locations")) {
                this.f_19804_.set(BLOCK_LOCATIONS, (CompoundTag) ritualData.get("block_locations"));
            }
            if (ritualData.contains("valid_locations")) {
                this.f_19804_.set(VALID_LOCATIONS, (CompoundTag) ritualData.get("valid_locations"));
            }
            if (ritualData.contains("__dynamicReagentsCached")) {
                this.__dynamicReagentsCached = ritualData.getBoolean("__dynamicReagentsCached");
            }
            if (ritualData.contains("collected_reagents", 9)) {
                this.readCollectedReagents(ritualData.getList("collected_reagents", 10));
            } else {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
            if (ritualData.contains("forceCollectAllReagents")) {
                this.forceCollectAllReagents = ritualData.getBoolean("forceCollectAllReagents");
            }
            if (ritualData.contains("dynamicSourceCache")) {
                this.cachedDynamicStack = ItemStack.of((CompoundTag) ritualData.get("dynamicSourceCache"));
            }
            if (ritualData.contains("collected_patterns")) {
                CompoundTag collected_patterns = ritualData.getCompound("collected_patterns");
                int count = collected_patterns.getInt("count");
                for (int i = 0; i < count; i++) {
                    this.collectedPatterns.add(new ResourceLocation(collected_patterns.getString("pattern_" + i)));
                }
            }
            if (ritualData.contains("required_patterns")) {
                CompoundTag required_patterns = ritualData.getCompound("required_patterns");
                int count = required_patterns.getInt("count");
                for (int i = 0; i < count; i++) {
                    this.requiredPatterns.add(new ResourceLocation(required_patterns.getString("pattern_" + i)));
                }
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        CompoundTag ritualData = new CompoundTag();
        ritualData.putInt("shutdown_age", this.f_19804_.get(SHUTDOWN_AGE));
        ritualData.putString("ritual_name", this.f_19804_.get(RITUAL_NAME));
        ritualData.putString("caster", this.f_19804_.get(CASTER_UUID));
        ritualData.putByte("state", this.f_19804_.get(RITUAL_STATE));
        ritualData.putFloat("speed", this.f_19804_.get(SPEED));
        ritualData.put("block_locations", this.f_19804_.get(BLOCK_LOCATIONS));
        ritualData.put("valid_locations", this.f_19804_.get(VALID_LOCATIONS));
        ritualData.putBoolean("forceCollectAllReagents", this.forceCollectAllReagents);
        ritualData.putBoolean("__dynamicReagentsCached", this.__dynamicReagentsCached);
        ritualData.put("dynamicSourceCache", this.cachedDynamicStack.serializeNBT());
        ritualData.put("collected_reagents", this.writeCollectedReagents());
        CompoundTag collected_patterns = new CompoundTag();
        collected_patterns.putInt("count", this.collectedPatterns.size());
        for (int i = 0; i < this.collectedPatterns.size(); i++) {
            collected_patterns.putString("pattern_" + i, this.collectedPatterns.get(i).toString());
        }
        ritualData.put("collected_patterns", collected_patterns);
        CompoundTag required_patterns = new CompoundTag();
        required_patterns.putInt("count", this.requiredPatterns.size());
        for (int i = 0; i < this.requiredPatterns.size(); i++) {
            required_patterns.putString("pattern_" + i, this.requiredPatterns.get(i).toString());
        }
        ritualData.put("required_patterns", required_patterns);
        compound.put("ritual_data", ritualData);
    }

    public ArrayList<BlockPos> getValidReagentLocations() {
        ArrayList<BlockPos> positions = new ArrayList();
        CompoundTag nbt = this.f_19804_.get(VALID_LOCATIONS);
        if (!nbt.contains("count")) {
            return positions;
        } else {
            int count = nbt.getInt("count");
            for (int i = 0; i < count; i++) {
                if (nbt.contains("block_pos_" + i)) {
                    CompoundTag blockPos = nbt.getCompound("block_pos_" + i);
                    BlockPos pos = new BlockPos(blockPos.getInt("x"), blockPos.getInt("y"), blockPos.getInt("z"));
                    positions.add(pos);
                } else {
                    ManaAndArtifice.LOGGER.error("Missing key block_pos_" + i + " in valid reagent locations sync data.");
                }
            }
            return positions;
        }
    }

    private void setValidReagentLocations(ArrayList<BlockPos> locations) {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("count", locations.size());
        for (int i = 0; i < locations.size(); i++) {
            CompoundTag blockPos = new CompoundTag();
            BlockPos pos = (BlockPos) locations.get(i);
            blockPos.putInt("x", pos.m_123341_());
            blockPos.putInt("y", pos.m_123342_());
            blockPos.putInt("z", pos.m_123343_());
            nbt.put("block_pos_" + i, blockPos);
        }
        this.f_19804_.set(VALID_LOCATIONS, nbt);
    }

    private void setCurrentEntityTarget(Entity e) {
        this.f_19804_.set(CURRENT_ENTITY, e.getId());
    }

    private Entity getCurrentEntityTarget() {
        if (this.__patternEntity == null || !this.__patternEntity.isAlive()) {
            this.patternConsumeTicks = 0;
            this.__patternEntity = this.m_9236_().getEntity(this.f_19804_.get(CURRENT_ENTITY));
        }
        return this.__patternEntity;
    }

    public void setRequiredPatterns(Collection<ResourceLocation> patterns) {
        if (patterns != this.requiredPatterns) {
            this.requiredPatterns.addAll(patterns);
        }
    }

    public void setRitualBlockLocations(NonNullList<RitualBlockPos> locations) {
        CompoundTag blockLocations = new CompoundTag();
        blockLocations.putInt("count", locations.size());
        for (int i = 0; i < locations.size(); i++) {
            this.WriteBlockPosToNBT("blockPos" + i, locations.get(i), blockLocations);
        }
        this.f_19804_.set(BLOCK_LOCATIONS, blockLocations);
    }

    public NonNullList<RitualBlockPos> getRitualData(byte mode) {
        return this.getRitualData(mode, false);
    }

    public NonNullList<RitualBlockPos> getRitualData(byte mode, boolean checkDynamicChange) {
        NonNullList<RitualBlockPos> positions = NonNullList.create();
        CompoundTag blockLocations = this.f_19804_.get(BLOCK_LOCATIONS);
        if (!blockLocations.contains("count")) {
            return positions;
        } else {
            RitualBlockPos dynamicSupplier = null;
            boolean foundDynamics = false;
            int count = blockLocations.getInt("count");
            for (int i = 0; i < count; i++) {
                RitualBlockPos pos = this.ReadBlockPosFromNBT("blockPos" + i, blockLocations);
                if (pos == null) {
                    return NonNullList.create();
                }
                if ((mode != 1 || pos.getIndex() >= 0) && (mode != 2 || pos.getDisplayIndex() >= 0)) {
                    if (pos.getReagent() != null) {
                        if (pos.getReagent().isDynamic()) {
                            foundDynamics = true;
                        }
                        if (pos.getReagent().isDynamicSource()) {
                            dynamicSupplier = pos;
                        }
                    }
                    positions.add(pos);
                }
            }
            if (mode == 1 || mode == 0) {
                this.comp.setCompareIndex();
            } else if (mode == 2) {
                this.comp.setCompareDisplay();
            }
            positions.sort(this.comp);
            if (!this.m_9236_().isClientSide()) {
                if (!this.__dynamicReagentsCached) {
                    if (foundDynamics) {
                        if (dynamicSupplier == null) {
                            ManaAndArtifice.LOGGER.error("Ritual specifies dynamic reagents/patterns but no supplier is found!");
                            positions.clear();
                        } else {
                            BlockEntity te = this.m_9236_().getBlockEntity(dynamicSupplier.getBlockPos());
                            if (te != null && te instanceof ChalkRuneTile) {
                                boolean modified = false;
                                ItemStack dynamicStack = ((ChalkRuneTile) te).getDisplayedItem().copy();
                                this.cachedDynamicStack = dynamicStack;
                                for (RitualEffect eff : this.getHandlers()) {
                                    modified |= eff.getDynamicReagents(dynamicStack, new RitualReagentReplaceContext(this.getCaster(), this, positions));
                                }
                                if (modified) {
                                    this.setRitualBlockLocations(positions);
                                    this.__dynamicReagentsCached = true;
                                }
                            }
                        }
                    } else {
                        this.__dynamicReagentsCached = true;
                    }
                } else if (foundDynamics && checkDynamicChange) {
                    BlockEntity te = this.m_9236_().getBlockEntity(dynamicSupplier.getBlockPos());
                    if (te != null && te instanceof ChalkRuneTile) {
                        ItemStack dynamicStack = ((ChalkRuneTile) te).getDisplayedItem().copy();
                        if (!ItemStack.matches(this.cachedDynamicStack, dynamicStack)) {
                            this.dynamicItemChanged = true;
                        }
                    }
                }
            }
            return positions;
        }
    }

    private ResourceLocation getRitualName() {
        return new ResourceLocation(this.f_19804_.get(RITUAL_NAME));
    }

    public void setRitualName(ResourceLocation name) {
        this.f_19804_.set(RITUAL_NAME, name.toString());
        this.__cachedCurrentRitual = RitualRecipe.find(this.m_9236_(), name);
        if (this.__cachedCurrentRitual != null) {
            ArrayList<ResourceLocation> patterns = new ArrayList();
            for (String s : this.__cachedCurrentRitual.getManaweavePatterns()) {
                patterns.add(new ResourceLocation(s));
            }
            this.setRequiredPatterns(patterns);
        }
    }

    public RitualRecipe getCurrentRitual() {
        if (this.__cachedCurrentRitual == null) {
            this.__cachedCurrentRitual = RitualRecipe.find(this.m_9236_(), this.getRitualName());
        }
        return this.__cachedCurrentRitual;
    }

    public Ritual.RitualState getState() {
        return Ritual.RitualState.values()[this.f_19804_.get(RITUAL_STATE)];
    }

    private void setState(Ritual.RitualState state) {
        this.f_19804_.set(RITUAL_STATE, (byte) state.ordinal());
        if (state == Ritual.RitualState.COLLECTING_REAGENTS) {
            this.worldTimeAtReagentCollectStart = this.m_9236_().getGameTime();
        }
    }

    private int getShutdownAge() {
        return this.f_19804_.get(SHUTDOWN_AGE);
    }

    private void setShutdownAge(int shutdown_age) {
        this.f_19804_.set(SHUTDOWN_AGE, shutdown_age);
    }

    public float getSpeed() {
        return this.f_19804_.get(SPEED);
    }

    public void setSpeed(float speed) {
        speed = Mth.clamp(speed, 0.1F, 2.0F);
        this.f_19804_.set(SPEED, speed);
    }

    public void preConsumePatterns(List<ResourceLocation> patterns) {
        this.requiredPatterns.removeAll(patterns);
    }

    public void setCasterUUID(UUID casterUUID) {
        if (casterUUID != null) {
            this.f_19804_.set(CASTER_UUID, casterUUID.toString());
        } else {
            ManaAndArtifice.LOGGER.error("Received null UUID for ritual caster.  Some effects may not apply!");
        }
    }

    public UUID getCasterUUID() {
        try {
            return UUID.fromString(this.f_19804_.get(CASTER_UUID));
        } catch (Exception var5) {
            return null;
        } finally {
            ;
        }
    }

    @Nullable
    private Player getCaster() {
        return this.getCasterUUID() == null ? null : this.m_9236_().m_46003_(this.getCasterUUID());
    }

    private int getPowerupTime() {
        return (int) Math.ceil((double) ((float) (this.getRitualData((byte) 2).size() * 13 + 20) * this.getSpeed()));
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return this.m_20191_().inflate(3.0);
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getStageTicks() {
        return this.stageTicks;
    }

    public void setStageTicks(int stageTicks) {
        this.stageTicks = stageTicks;
    }

    public void setRequestedPattern(String pattern) {
        this.f_19804_.set(REQUESTED_PATTERN, pattern);
    }

    public ManaweavingPattern getRequestedPattern() {
        String patternID = this.f_19804_.get(REQUESTED_PATTERN);
        return ManaweavingPatternHelper.GetManaweavingRecipe(this.m_9236_(), new ResourceLocation(patternID));
    }

    public void setForceConsumeReagents(boolean force) {
        this.forceCollectAllReagents = force;
    }

    private NonNullList<RitualEffect> getHandlers() {
        if (this.__cachedRitualHandlers == null) {
            this.__cachedRitualHandlers = NonNullList.create();
            ((IForgeRegistry) Registries.RitualEffect.get()).getValues().forEach(eff -> {
                if (eff.handlesRitual(this.getRitualName())) {
                    this.__cachedRitualHandlers.add(eff);
                }
            });
        }
        return this.__cachedRitualHandlers;
    }

    public NonNullList<ItemStack> getCollectedReagents() {
        NonNullList<ItemStack> reagents = NonNullList.create();
        reagents.addAll((Collection) this.collectedReagents.stream().map(e -> (ItemStack) e.getSecond()).collect(Collectors.toList()));
        return reagents;
    }

    public NonNullList<Pair<BlockPos, ItemStack>> getCollectedReagentsByLocation() {
        return this.collectedReagents;
    }

    public NonNullList<ResourceLocation> getCollectedPatterns() {
        NonNullList<ResourceLocation> patterns = NonNullList.create();
        patterns.addAll(this.collectedPatterns);
        return patterns;
    }

    public NonNullList<ResourceLocation> getRequiredPatterns() {
        return this.requiredPatterns;
    }

    @Override
    public void baseTick() {
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void checkDespawn() {
    }

    public boolean canUpdate() {
        return true;
    }

    public static enum RitualState {

        GUIDING_REAGENT_PLACEMENT,
        POWERING_UP,
        COLLECTING_REAGENTS,
        COLLECTING_PATTERNS,
        COLLAPSING,
        PROCESSING_RITUAL,
        COMPLETING,
        DEAD
    }
}