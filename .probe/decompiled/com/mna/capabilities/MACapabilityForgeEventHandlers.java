package com.mna.capabilities;

import com.mna.Registries;
import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.capabilities.IWorldMagic;
import com.mna.api.config.GeneralConfigValues;
import com.mna.api.faction.IFaction;
import com.mna.api.tools.RLoc;
import com.mna.blocks.artifice.WardingCandleBlock;
import com.mna.capabilities.chunkdata.ChunkMagicProvider;
import com.mna.capabilities.entity.MAPFXProvider;
import com.mna.capabilities.particles.ParticleAuraProvider;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.capabilities.playerdata.rote.PlayerRoteSpellsProvider;
import com.mna.capabilities.worlddata.WorldMagicProvider;
import com.mna.entities.EntityInit;
import com.mna.entities.faction.util.FactionRaid;
import com.mna.items.artifice.charms.ItemContingencyCharm;
import com.mna.network.ServerMessageDispatcher;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber(modid = "mna", bus = Bus.FORGE)
public class MACapabilityForgeEventHandlers {

    public static final ResourceLocation PLAYERMAGIC_CAP = RLoc.create("magic");

    public static final ResourceLocation WORLDMAGIC_CAP = RLoc.create("worldmagic");

    public static final ResourceLocation CHUNKMAGIC_CAP = RLoc.create("chunkmagic");

    public static final ResourceLocation PROGRESSION_CAP = RLoc.create("progression");

    public static final ResourceLocation ROTE_SPELLS_CAP = RLoc.create("rote_spells");

    public static final ResourceLocation AURAS_CAP = RLoc.create("auras");

    public static final ResourceLocation PFX_CAP = RLoc.create("pfx_capability");

    @SubscribeEvent
    public static void attachEntityCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof LivingEntity) {
            event.addCapability(PFX_CAP, new MAPFXProvider());
        }
        if (event.getObject() instanceof Player) {
            event.addCapability(PLAYERMAGIC_CAP, new PlayerMagicProvider());
            event.addCapability(PROGRESSION_CAP, new PlayerProgressionProvider());
            event.addCapability(ROTE_SPELLS_CAP, new PlayerRoteSpellsProvider());
            event.addCapability(AURAS_CAP, new ParticleAuraProvider());
        }
    }

    @SubscribeEvent
    public static void attachWorldCapability(AttachCapabilitiesEvent<Level> event) {
        event.addCapability(WORLDMAGIC_CAP, new WorldMagicProvider(event.getObject().dimension() == Level.OVERWORLD));
    }

    @SubscribeEvent
    public static void attachChunkCapability(AttachCapabilitiesEvent<LevelChunk> event) {
        if (event.getObject() instanceof LevelChunk) {
            event.addCapability(CHUNKMAGIC_CAP, new ChunkMagicProvider());
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        Player player = event.getEntity();
        Player original = event.getOriginal();
        original.reviveCaps();
        player.getCapability(PlayerMagicProvider.MAGIC).ifPresent(mana -> {
            original.getCapability(PlayerMagicProvider.MAGIC).ifPresent(oldMana -> mana.copyFrom(oldMana));
            mana.getCastingResource().setAmount(mana.getCastingResource().getMaxAmount());
        });
        player.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(progression -> original.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(oldProgression -> {
            progression.setAlliedFaction(oldProgression.getAlliedFaction(), null);
            progression.setFactionStanding(oldProgression.getFactionStanding());
            progression.setTier(oldProgression.getTier(), player, false);
            progression.setTierProgression(oldProgression.getCompletedProgressionSteps());
        }));
        player.getCapability(PlayerRoteSpellsProvider.ROTE).ifPresent(rote -> original.getCapability(PlayerRoteSpellsProvider.ROTE).ifPresent(oldRote -> rote.copyFrom(oldRote)));
        player.getCapability(ParticleAuraProvider.AURA).ifPresent(aura -> original.getCapability(ParticleAuraProvider.AURA).ifPresent(oldAura -> aura.load(oldAura.save())));
        event.getOriginal().invalidateCaps();
    }

    @SubscribeEvent
    public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        event.getEntity().getCapability(PlayerMagicProvider.MAGIC).ifPresent(magic -> magic.forceSync());
        event.getEntity().getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(progression -> progression.setDirty());
        event.getEntity().getCapability(PlayerRoteSpellsProvider.ROTE).ifPresent(rote -> rote.setDirty());
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START) {
            tickMagic(event);
            tickRaids(event);
        }
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        event.getEntity().getCapability(MAPFXProvider.MAPFX).ifPresent(pfx -> pfx.sync(event.getEntity()));
    }

    private static void tickMagic(TickEvent.PlayerTickEvent event) {
        IPlayerMagic magic = (IPlayerMagic) event.player.getCapability(PlayerMagicProvider.MAGIC).orElse(null);
        if (magic != null) {
            magic.tick(event.player);
        }
        if (event.side == LogicalSide.SERVER) {
            if (magic != null && magic.needsSync()) {
                ServerMessageDispatcher.sendMagicSyncMessage((ServerPlayer) event.player);
                magic.clearSyncFlags();
            }
            event.player.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> {
                if (p.needsSync()) {
                    ServerMessageDispatcher.sendProgressionSyncMessage((ServerPlayer) event.player);
                    p.clearSyncFlag();
                }
            });
            event.player.getCapability(PlayerRoteSpellsProvider.ROTE).ifPresent(r -> {
                if (r.isDirty()) {
                    ServerMessageDispatcher.sendRoteSyncMessage((ServerPlayer) event.player);
                    r.clearDirty();
                }
            });
        }
    }

    private static void tickRaids(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER) {
            if (event.player.m_9236_().getServer().getWorldData().getDifficulty() == Difficulty.PEACEFUL) {
                return;
            }
            if (event.player.m_9236_().getGameTime() % 100L != 0L) {
                return;
            }
            event.player.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> {
                p.tickClassicRaids(event.player);
                if (p.canBeRaided(event.player)) {
                    int hRadius = 20;
                    int vRadius = 3;
                    BlockPos baseline = event.player.m_20183_();
                    boolean force = p.hasForceRaid();
                    int loops = force ? 100 : GeneralConfigValues.RaidSpawnAttempts;
                    for (int i = 0; i < loops; i++) {
                        BlockPos attemptedLocation = baseline.offset((int) ((double) (-hRadius) + Math.random() * (double) hRadius * 2.0), (int) ((double) (-vRadius) + Math.random() * (double) vRadius * 2.0), (int) ((double) (-hRadius) + Math.random() * (double) hRadius * 2.0));
                        while (attemptedLocation.m_123342_() > 5 && !event.player.m_9236_().getBlockState(attemptedLocation).m_60815_()) {
                            attemptedLocation = attemptedLocation.below();
                        }
                        while (attemptedLocation.m_123342_() < event.player.m_9236_().m_151558_() && !event.player.m_9236_().m_46859_(attemptedLocation)) {
                            attemptedLocation = attemptedLocation.above();
                        }
                        BlockPos attemptedLocUp = attemptedLocation.above();
                        if ((force || !(event.player.m_20238_(new Vec3((double) attemptedLocation.m_123341_(), (double) attemptedLocation.m_123342_(), (double) attemptedLocation.m_123343_())) < 25.0) && !((IWorldMagic) event.player.m_9236_().getCapability(WorldMagicProvider.MAGIC).resolve().get()).isWithinWardingCandle(attemptedLocation)) && event.player.m_9236_().m_46859_(attemptedLocation) && event.player.m_9236_().m_46859_(attemptedLocUp) && !event.player.m_9236_().m_46859_(attemptedLocation.below())) {
                            ClipContext context = new ClipContext(event.player.m_20299_(0.0F), new Vec3((double) attemptedLocUp.m_123341_() + 0.5, (double) attemptedLocUp.m_123342_(), (double) attemptedLocUp.m_123343_() + 0.5), ClipContext.Block.COLLIDER, ClipContext.Fluid.SOURCE_ONLY, null);
                            BlockHitResult result = event.player.m_9236_().m_45547_(context);
                            if (result.getType() == HitResult.Type.MISS) {
                                BlockState spawnBlock = event.player.m_9236_().getBlockState(attemptedLocation.below());
                                if (!force && !spawnBlock.m_60643_(event.player.m_9236_(), attemptedLocation.below(), EntityInit.FACTION_RAID_ENTITY.get())) {
                                    return;
                                }
                                List<IFaction> potentialRaiders = force ? Arrays.asList(p.getForceRaid()) : (List) ((IForgeRegistry) Registries.Factions.get()).getValues().stream().filter(f -> p.canBeRaided(f, event.player)).collect(Collectors.toList());
                                if (potentialRaiders.size() == 0) {
                                    return;
                                }
                                IFaction raidingFaction = (IFaction) potentialRaiders.get(event.player.m_9236_().getRandom().nextInt(potentialRaiders.size()));
                                if (raidingFaction == null) {
                                    return;
                                }
                                if (!force) {
                                    int light = event.player.m_9236_().isThundering() ? event.player.m_9236_().m_46849_(attemptedLocation, 10) : event.player.m_9236_().m_46803_(attemptedLocation);
                                    int anger = (int) Math.floor(p.getRaidChance(raidingFaction));
                                    if (light > 7 + anger) {
                                        return;
                                    }
                                }
                                if (event.player instanceof ServerPlayer) {
                                    ItemContingencyCharm.CheckAndConsumeCharmCharge((ServerPlayer) event.player, ItemContingencyCharm.ContingencyEvent.FACTION_RAID);
                                }
                                FactionRaid fre = new FactionRaid(event.player.m_9236_(), event.player, p.getRelativeRaidStrength(raidingFaction, event.player));
                                fre.setFaction(raidingFaction);
                                fre.m_6034_((double) attemptedLocUp.m_123341_() + 0.5, (double) attemptedLocUp.m_123342_(), (double) attemptedLocUp.m_123343_() + 0.5);
                                event.player.m_9236_().m_7967_(fre);
                                p.setRaidChance(raidingFaction, 0.0);
                                if (force) {
                                    p.clearForceRaid();
                                }
                                return;
                            }
                        }
                    }
                }
            });
        }
    }

    @SubscribeEvent
    public static void onCheckSpawnEvent(MobSpawnEvent.SpawnPlacementCheck event) {
        List<MobSpawnType> nonBlockedSpawnTypes = Arrays.asList(MobSpawnType.BREEDING, MobSpawnType.BUCKET, MobSpawnType.CHUNK_GENERATION, MobSpawnType.COMMAND, MobSpawnType.CONVERSION, MobSpawnType.DISPENSER, MobSpawnType.MOB_SUMMONED, MobSpawnType.SPAWN_EGG, MobSpawnType.TRIGGERED);
        if (!nonBlockedSpawnTypes.contains(event.getSpawnType())) {
            if (event.getLevel() instanceof Level) {
                ((Level) event.getLevel()).getCapability(WorldMagicProvider.MAGIC).ifPresent(w -> {
                    if (WardingCandleBlock.shouldEntityBeBlocked(event.getEntityType()) && w.isWithinWardingCandle(event.getPos())) {
                        event.setResult(Result.DENY);
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public static void onMobGriefing(EntityMobGriefingEvent event) {
        if (event != null && event.getEntity() != null && event.getEntity().level() != null) {
            if (event.getEntity().canChangeDimensions()) {
                event.getEntity().level().getCapability(WorldMagicProvider.MAGIC).ifPresent(w -> {
                    if (w.isWithinWardingCandle(event.getEntity().blockPosition()) && !GeneralConfigValues.WardingCandleWhitelist.contains(ForgeRegistries.ENTITY_TYPES.getKey(event.getEntity().getType()).toString())) {
                        event.setResult(Result.DENY);
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel) {
            ServerLevel sw = (ServerLevel) event.getLevel();
            sw.getCapability(WorldMagicProvider.MAGIC).ifPresent(m -> {
                BlockPos chunkCenter = new BlockPos((event.getChunk().getPos().getMinBlockX() + event.getChunk().getPos().getMaxBlockX()) / 2, 0, (event.getChunk().getPos().getMinBlockZ() + event.getChunk().getPos().getMaxBlockZ()) / 2);
                m.getWellspringRegistry().addRandomNode(sw, chunkCenter);
            });
        }
    }
}