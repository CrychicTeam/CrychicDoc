package dev.xkmc.l2hostility.content.capability.mob;

import dev.xkmc.l2hostility.content.capability.chunk.ChunkDifficulty;
import dev.xkmc.l2hostility.content.capability.chunk.RegionalDifficultyModifier;
import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.content.traits.legendary.MasterTrait;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

@SerialClass
public class MasterData {

    @SerialField(toClient = true)
    public ArrayList<MasterData.Minion> data = new ArrayList();

    @SerialField
    private LinkedHashMap<EntityType<?>, MasterData.Data> map = new LinkedHashMap();

    public boolean tick(MobTraitCap cap, Mob mob) {
        EntityConfig.MasterConfig config = MasterTrait.getConfig(mob.m_6095_());
        if (config == null) {
            return false;
        } else {
            for (EntityConfig.Minion e : config.minions()) {
                ((MasterData.Data) this.map.computeIfAbsent(e.type(), k -> new MasterData.Data())).setup(e);
            }
            boolean updated = this.data.removeIf(ex -> {
                ex.tick(mob);
                if (ex.minion == null) {
                    return !mob.m_9236_().isClientSide();
                } else {
                    MasterData.Data ent = (MasterData.Data) this.map.get(ex.minion.m_6095_());
                    if (ent != null) {
                        ent.count++;
                    }
                    return false;
                }
            });
            if (!mob.m_9236_().isClientSide()) {
                for (MasterData.Minion e : this.data) {
                    if (e.minion != null) {
                        if (e.minion.getTarget() == null && mob.getTarget() != null) {
                            e.minion.setTarget(mob.getTarget());
                        }
                        if (e.id != e.minion.m_19879_()) {
                            e.id = e.minion.m_19879_();
                            updated = true;
                        }
                    }
                }
            }
            for (MasterData.Data ex : this.map.values()) {
                if (ex.count < ex.config.maxCount() && ex.cooldown > 0) {
                    ex.cooldown--;
                }
            }
            if (mob.m_9236_() instanceof ServerLevel sl && mob.getTarget() != null && mob.getTarget().isAlive() && this.data.size() < config.maxTotalCount() && mob.f_19797_ % config.spawnInterval() == 0) {
                for (MasterData.Data exx : this.map.values()) {
                    if (exx.cooldown <= 0 && this.data.size() < config.maxTotalCount() && exx.count < exx.config.maxCount() && cap.getLevel() >= exx.config.minLevel()) {
                        MasterData.Minion nd = exx.spawn(cap, sl, mob);
                        if (nd != null) {
                            this.data.add(nd);
                            exx.cooldown = exx.config.cooldown();
                            return true;
                        }
                    }
                }
            }
            return updated;
        }
    }

    @SerialClass
    public static class Data {

        private EntityConfig.Minion config;

        private int count;

        @SerialField
        public int cooldown;

        public void setup(EntityConfig.Minion e) {
            this.config = e;
            this.count = 0;
        }

        @Nullable
        public MasterData.Minion spawn(MobTraitCap parent, ServerLevel sl, Mob mob) {
            int r = this.config.spawnRange();
            RandomSource rand = mob.m_217043_();
            Vec3 eye = mob.m_146892_();
            BlockPos pos = mob.m_20183_();
            BlockPos target = null;
            for (int i = 0; i < 16; i++) {
                BlockPos p = pos.offset(rand.nextInt(0, r * 2 + 1) - r, rand.nextInt(0, 3), rand.nextInt(0, r * 2 + 1) - r);
                if (sl.m_45772_(this.config.type().getAABB((double) p.m_123341_(), (double) p.m_123342_(), (double) p.m_123343_()))) {
                    Vec3 e = Vec3.atBottomCenterOf(p).add(0.0, (double) (this.config.type().getHeight() / 2.0F), 0.0);
                    BlockHitResult bhit = sl.m_45547_(new ClipContext(eye, e, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, null));
                    if (bhit.getType() == HitResult.Type.MISS) {
                        target = p;
                        break;
                    }
                }
            }
            if (target == null) {
                return null;
            } else if (this.config.type().create(sl, null, null, target, MobSpawnType.MOB_SUMMONED, false, false) instanceof Mob m) {
                MobTraitCap var16 = (MobTraitCap) MobTraitCap.HOLDER.get(m);
                RegionalDifficultyModifier var17 = (p, c) -> {
                    if (this.config.copyLevel()) {
                        c.base = parent.getLevel();
                    } else {
                        ChunkDifficulty.at(sl, p).ifPresent(x -> x.modifyInstance(p, c));
                    }
                    if (this.config.copyTrait()) {
                        var16.traits.putAll(parent.traits);
                        c.delegateTrait();
                    }
                };
                var16.minion = true;
                var16.asMinion = new MinionData().init(mob, this.config);
                var16.init(sl, m, var17);
                m.setTarget(mob.getTarget());
                sl.addFreshEntity(m);
                MasterData.Minion ans = new MasterData.Minion();
                ans.minion = m;
                ans.uuid = m.m_20148_();
                ans.id = m.m_19879_();
                return ans;
            } else {
                return null;
            }
        }
    }

    @SerialClass
    public static class Minion {

        @SerialField(toClient = true)
        public UUID uuid;

        @SerialField(toClient = true)
        public int id;

        public Mob minion;

        public void tick(Mob mob) {
            if (mob.m_9236_() instanceof ServerLevel sl) {
                if (this.minion == null && sl.getEntity(this.uuid) instanceof Mob m) {
                    this.minion = m;
                }
                if (this.minion != null && !this.minion.m_6084_()) {
                    this.minion = null;
                }
            } else if (this.minion == null && mob.m_9236_().getEntity(this.id) instanceof Mob m && m.m_20148_().equals(this.uuid)) {
                this.minion = m;
            }
        }
    }
}