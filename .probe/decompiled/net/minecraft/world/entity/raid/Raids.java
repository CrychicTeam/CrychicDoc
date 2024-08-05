package net.minecraft.world.entity.raid;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.Vec3;

public class Raids extends SavedData {

    private static final String RAID_FILE_ID = "raids";

    private final Map<Integer, Raid> raidMap = Maps.newHashMap();

    private final ServerLevel level;

    private int nextAvailableID;

    private int tick;

    public Raids(ServerLevel serverLevel0) {
        this.level = serverLevel0;
        this.nextAvailableID = 1;
        this.m_77762_();
    }

    public Raid get(int int0) {
        return (Raid) this.raidMap.get(int0);
    }

    public void tick() {
        this.tick++;
        Iterator<Raid> $$0 = this.raidMap.values().iterator();
        while ($$0.hasNext()) {
            Raid $$1 = (Raid) $$0.next();
            if (this.level.m_46469_().getBoolean(GameRules.RULE_DISABLE_RAIDS)) {
                $$1.stop();
            }
            if ($$1.isStopped()) {
                $$0.remove();
                this.m_77762_();
            } else {
                $$1.tick();
            }
        }
        if (this.tick % 200 == 0) {
            this.m_77762_();
        }
        DebugPackets.sendRaids(this.level, this.raidMap.values());
    }

    public static boolean canJoinRaid(Raider raider0, Raid raid1) {
        return raider0 != null && raid1 != null && raid1.getLevel() != null ? raider0.m_6084_() && raider0.canJoinRaid() && raider0.m_21216_() <= 2400 && raider0.m_9236_().dimensionType() == raid1.getLevel().dimensionType() : false;
    }

    @Nullable
    public Raid createOrExtendRaid(ServerPlayer serverPlayer0) {
        if (serverPlayer0.isSpectator()) {
            return null;
        } else if (this.level.m_46469_().getBoolean(GameRules.RULE_DISABLE_RAIDS)) {
            return null;
        } else {
            DimensionType $$1 = serverPlayer0.m_9236_().dimensionType();
            if (!$$1.hasRaids()) {
                return null;
            } else {
                BlockPos $$2 = serverPlayer0.m_20183_();
                List<PoiRecord> $$3 = this.level.getPoiManager().getInRange(p_219845_ -> p_219845_.is(PoiTypeTags.VILLAGE), $$2, 64, PoiManager.Occupancy.IS_OCCUPIED).toList();
                int $$4 = 0;
                Vec3 $$5 = Vec3.ZERO;
                for (PoiRecord $$6 : $$3) {
                    BlockPos $$7 = $$6.getPos();
                    $$5 = $$5.add((double) $$7.m_123341_(), (double) $$7.m_123342_(), (double) $$7.m_123343_());
                    $$4++;
                }
                BlockPos $$8;
                if ($$4 > 0) {
                    $$5 = $$5.scale(1.0 / (double) $$4);
                    $$8 = BlockPos.containing($$5);
                } else {
                    $$8 = $$2;
                }
                Raid $$10 = this.getOrCreateRaid(serverPlayer0.serverLevel(), $$8);
                boolean $$11 = false;
                if (!$$10.isStarted()) {
                    if (!this.raidMap.containsKey($$10.getId())) {
                        this.raidMap.put($$10.getId(), $$10);
                    }
                    $$11 = true;
                } else if ($$10.getBadOmenLevel() < $$10.getMaxBadOmenLevel()) {
                    $$11 = true;
                } else {
                    serverPlayer0.m_21195_(MobEffects.BAD_OMEN);
                    serverPlayer0.connection.send(new ClientboundEntityEventPacket(serverPlayer0, (byte) 43));
                }
                if ($$11) {
                    $$10.absorbBadOmen(serverPlayer0);
                    serverPlayer0.connection.send(new ClientboundEntityEventPacket(serverPlayer0, (byte) 43));
                    if (!$$10.hasFirstWaveSpawned()) {
                        serverPlayer0.m_36220_(Stats.RAID_TRIGGER);
                        CriteriaTriggers.BAD_OMEN.trigger(serverPlayer0);
                    }
                }
                this.m_77762_();
                return $$10;
            }
        }
    }

    private Raid getOrCreateRaid(ServerLevel serverLevel0, BlockPos blockPos1) {
        Raid $$2 = serverLevel0.getRaidAt(blockPos1);
        return $$2 != null ? $$2 : new Raid(this.getUniqueId(), serverLevel0, blockPos1);
    }

    public static Raids load(ServerLevel serverLevel0, CompoundTag compoundTag1) {
        Raids $$2 = new Raids(serverLevel0);
        $$2.nextAvailableID = compoundTag1.getInt("NextAvailableID");
        $$2.tick = compoundTag1.getInt("Tick");
        ListTag $$3 = compoundTag1.getList("Raids", 10);
        for (int $$4 = 0; $$4 < $$3.size(); $$4++) {
            CompoundTag $$5 = $$3.getCompound($$4);
            Raid $$6 = new Raid(serverLevel0, $$5);
            $$2.raidMap.put($$6.getId(), $$6);
        }
        return $$2;
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag0) {
        compoundTag0.putInt("NextAvailableID", this.nextAvailableID);
        compoundTag0.putInt("Tick", this.tick);
        ListTag $$1 = new ListTag();
        for (Raid $$2 : this.raidMap.values()) {
            CompoundTag $$3 = new CompoundTag();
            $$2.save($$3);
            $$1.add($$3);
        }
        compoundTag0.put("Raids", $$1);
        return compoundTag0;
    }

    public static String getFileId(Holder<DimensionType> holderDimensionType0) {
        return holderDimensionType0.is(BuiltinDimensionTypes.END) ? "raids_end" : "raids";
    }

    private int getUniqueId() {
        return ++this.nextAvailableID;
    }

    @Nullable
    public Raid getNearbyRaid(BlockPos blockPos0, int int1) {
        Raid $$2 = null;
        double $$3 = (double) int1;
        for (Raid $$4 : this.raidMap.values()) {
            double $$5 = $$4.getCenter().m_123331_(blockPos0);
            if ($$4.isActive() && $$5 < $$3) {
                $$2 = $$4;
                $$3 = $$5;
            }
        }
        return $$2;
    }
}