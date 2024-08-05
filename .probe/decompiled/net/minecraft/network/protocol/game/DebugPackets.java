package net.minecraft.network.protocol.game;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringUtil;
import net.minecraft.world.Container;
import net.minecraft.world.Nameable;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.gossip.GossipType;
import net.minecraft.world.entity.ai.memory.ExpirableValue;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

public class DebugPackets {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static void sendGameTestAddMarker(ServerLevel serverLevel0, BlockPos blockPos1, String string2, int int3, int int4) {
        FriendlyByteBuf $$5 = new FriendlyByteBuf(Unpooled.buffer());
        $$5.writeBlockPos(blockPos1);
        $$5.writeInt(int3);
        $$5.writeUtf(string2);
        $$5.writeInt(int4);
        sendPacketToAllPlayers(serverLevel0, $$5, ClientboundCustomPayloadPacket.DEBUG_GAME_TEST_ADD_MARKER);
    }

    public static void sendGameTestClearPacket(ServerLevel serverLevel0) {
        FriendlyByteBuf $$1 = new FriendlyByteBuf(Unpooled.buffer());
        sendPacketToAllPlayers(serverLevel0, $$1, ClientboundCustomPayloadPacket.DEBUG_GAME_TEST_CLEAR);
    }

    public static void sendPoiPacketsForChunk(ServerLevel serverLevel0, ChunkPos chunkPos1) {
    }

    public static void sendPoiAddedPacket(ServerLevel serverLevel0, BlockPos blockPos1) {
        sendVillageSectionsPacket(serverLevel0, blockPos1);
    }

    public static void sendPoiRemovedPacket(ServerLevel serverLevel0, BlockPos blockPos1) {
        sendVillageSectionsPacket(serverLevel0, blockPos1);
    }

    public static void sendPoiTicketCountPacket(ServerLevel serverLevel0, BlockPos blockPos1) {
        sendVillageSectionsPacket(serverLevel0, blockPos1);
    }

    private static void sendVillageSectionsPacket(ServerLevel serverLevel0, BlockPos blockPos1) {
    }

    public static void sendPathFindingPacket(Level level0, Mob mob1, @Nullable Path path2, float float3) {
    }

    public static void sendNeighborsUpdatePacket(Level level0, BlockPos blockPos1) {
    }

    public static void sendStructurePacket(WorldGenLevel worldGenLevel0, StructureStart structureStart1) {
    }

    public static void sendGoalSelector(Level level0, Mob mob1, GoalSelector goalSelector2) {
        if (level0 instanceof ServerLevel) {
            ;
        }
    }

    public static void sendRaids(ServerLevel serverLevel0, Collection<Raid> collectionRaid1) {
    }

    public static void sendEntityBrain(LivingEntity livingEntity0) {
    }

    public static void sendBeeInfo(Bee bee0) {
    }

    public static void sendGameEventInfo(Level level0, GameEvent gameEvent1, Vec3 vec2) {
    }

    public static void sendGameEventListenerInfo(Level level0, GameEventListener gameEventListener1) {
    }

    public static void sendHiveInfo(Level level0, BlockPos blockPos1, BlockState blockState2, BeehiveBlockEntity beehiveBlockEntity3) {
    }

    private static void writeBrain(LivingEntity livingEntity0, FriendlyByteBuf friendlyByteBuf1) {
        Brain<?> $$2 = livingEntity0.getBrain();
        long $$3 = livingEntity0.m_9236_().getGameTime();
        if (livingEntity0 instanceof InventoryCarrier) {
            Container $$4 = ((InventoryCarrier) livingEntity0).getInventory();
            friendlyByteBuf1.writeUtf($$4.isEmpty() ? "" : $$4.toString());
        } else {
            friendlyByteBuf1.writeUtf("");
        }
        friendlyByteBuf1.writeOptional($$2.hasMemoryValue(MemoryModuleType.PATH) ? $$2.getMemory(MemoryModuleType.PATH) : Optional.empty(), (p_237912_, p_237913_) -> p_237913_.writeToStream(p_237912_));
        if (livingEntity0 instanceof Villager $$5) {
            boolean $$6 = $$5.wantsToSpawnGolem($$3);
            friendlyByteBuf1.writeBoolean($$6);
        } else {
            friendlyByteBuf1.writeBoolean(false);
        }
        if (livingEntity0.m_6095_() == EntityType.WARDEN) {
            Warden $$7 = (Warden) livingEntity0;
            friendlyByteBuf1.writeInt($$7.getClientAngerLevel());
        } else {
            friendlyByteBuf1.writeInt(-1);
        }
        friendlyByteBuf1.writeCollection($$2.getActiveActivities(), (p_237909_, p_237910_) -> p_237909_.writeUtf(p_237910_.getName()));
        Set<String> $$8 = (Set<String>) $$2.getRunningBehaviors().stream().map(BehaviorControl::m_22566_).collect(Collectors.toSet());
        friendlyByteBuf1.writeCollection($$8, FriendlyByteBuf::m_130070_);
        friendlyByteBuf1.writeCollection(getMemoryDescriptions(livingEntity0, $$3), (p_237915_, p_237916_) -> {
            String $$2x = StringUtil.truncateStringIfNecessary(p_237916_, 255, true);
            p_237915_.writeUtf($$2x);
        });
        if (livingEntity0 instanceof Villager) {
            Set<BlockPos> $$9 = (Set<BlockPos>) Stream.of(MemoryModuleType.JOB_SITE, MemoryModuleType.HOME, MemoryModuleType.MEETING_POINT).map($$2::m_21952_).flatMap(Optional::stream).map(GlobalPos::m_122646_).collect(Collectors.toSet());
            friendlyByteBuf1.writeCollection($$9, FriendlyByteBuf::m_130064_);
        } else {
            friendlyByteBuf1.writeVarInt(0);
        }
        if (livingEntity0 instanceof Villager) {
            Set<BlockPos> $$10 = (Set<BlockPos>) Stream.of(MemoryModuleType.POTENTIAL_JOB_SITE).map($$2::m_21952_).flatMap(Optional::stream).map(GlobalPos::m_122646_).collect(Collectors.toSet());
            friendlyByteBuf1.writeCollection($$10, FriendlyByteBuf::m_130064_);
        } else {
            friendlyByteBuf1.writeVarInt(0);
        }
        if (livingEntity0 instanceof Villager) {
            Map<UUID, Object2IntMap<GossipType>> $$11 = ((Villager) livingEntity0).getGossips().getGossipEntries();
            List<String> $$12 = Lists.newArrayList();
            $$11.forEach((p_237900_, p_237901_) -> {
                String $$3x = DebugEntityNameGenerator.getEntityName(p_237900_);
                p_237901_.forEach((p_237896_, p_237897_) -> $$12.add($$3x + ": " + p_237896_ + ": " + p_237897_));
            });
            friendlyByteBuf1.writeCollection($$12, FriendlyByteBuf::m_130070_);
        } else {
            friendlyByteBuf1.writeVarInt(0);
        }
    }

    private static List<String> getMemoryDescriptions(LivingEntity livingEntity0, long long1) {
        Map<MemoryModuleType<?>, Optional<? extends ExpirableValue<?>>> $$2 = livingEntity0.getBrain().getMemories();
        List<String> $$3 = Lists.newArrayList();
        for (Entry<MemoryModuleType<?>, Optional<? extends ExpirableValue<?>>> $$4 : $$2.entrySet()) {
            MemoryModuleType<?> $$5 = (MemoryModuleType<?>) $$4.getKey();
            Optional<? extends ExpirableValue<?>> $$6 = (Optional<? extends ExpirableValue<?>>) $$4.getValue();
            String $$10;
            if ($$6.isPresent()) {
                ExpirableValue<?> $$7 = (ExpirableValue<?>) $$6.get();
                Object $$8 = $$7.getValue();
                if ($$5 == MemoryModuleType.HEARD_BELL_TIME) {
                    long $$9 = long1 - (Long) $$8;
                    $$10 = $$9 + " ticks ago";
                } else if ($$7.canExpire()) {
                    $$10 = getShortDescription((ServerLevel) livingEntity0.m_9236_(), $$8) + " (ttl: " + $$7.getTimeToLive() + ")";
                } else {
                    $$10 = getShortDescription((ServerLevel) livingEntity0.m_9236_(), $$8);
                }
            } else {
                $$10 = "-";
            }
            $$3.add(BuiltInRegistries.MEMORY_MODULE_TYPE.getKey($$5).getPath() + ": " + $$10);
        }
        $$3.sort(String::compareTo);
        return $$3;
    }

    private static String getShortDescription(ServerLevel serverLevel0, @Nullable Object object1) {
        if (object1 == null) {
            return "-";
        } else if (object1 instanceof UUID) {
            return getShortDescription(serverLevel0, serverLevel0.getEntity((UUID) object1));
        } else if (object1 instanceof LivingEntity) {
            Entity $$2 = (Entity) object1;
            return DebugEntityNameGenerator.getEntityName($$2);
        } else if (object1 instanceof Nameable) {
            return ((Nameable) object1).getName().getString();
        } else if (object1 instanceof WalkTarget) {
            return getShortDescription(serverLevel0, ((WalkTarget) object1).getTarget());
        } else if (object1 instanceof EntityTracker) {
            return getShortDescription(serverLevel0, ((EntityTracker) object1).getEntity());
        } else if (object1 instanceof GlobalPos) {
            return getShortDescription(serverLevel0, ((GlobalPos) object1).pos());
        } else if (object1 instanceof BlockPosTracker) {
            return getShortDescription(serverLevel0, ((BlockPosTracker) object1).currentBlockPosition());
        } else if (object1 instanceof DamageSource) {
            Entity $$3 = ((DamageSource) object1).getEntity();
            return $$3 == null ? object1.toString() : getShortDescription(serverLevel0, $$3);
        } else if (!(object1 instanceof Collection)) {
            return object1.toString();
        } else {
            List<String> $$4 = Lists.newArrayList();
            for (Object $$5 : (Iterable) object1) {
                $$4.add(getShortDescription(serverLevel0, $$5));
            }
            return $$4.toString();
        }
    }

    private static void sendPacketToAllPlayers(ServerLevel serverLevel0, FriendlyByteBuf friendlyByteBuf1, ResourceLocation resourceLocation2) {
        Packet<?> $$3 = new ClientboundCustomPayloadPacket(resourceLocation2, friendlyByteBuf1);
        for (ServerPlayer $$4 : serverLevel0.players()) {
            $$4.connection.send($$3);
        }
    }
}