package dev.xkmc.l2hostility.content.item.spawner;

import dev.xkmc.l2hostility.content.capability.chunk.ChunkDifficulty;
import dev.xkmc.l2hostility.content.capability.chunk.SectionDifficulty;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LHTagGen;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2serial.serialization.SerialClass;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.bossevents.CustomBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

@SerialClass
public class BurstSpawnerBlockEntity extends TraitSpawnerBlockEntity {

    private static WeightedRandomList<MobSpawnSettings.SpawnerData> mobsAt(ServerLevel level, MobCategory category, BlockPos pos) {
        StructureManager structure = level.structureManager();
        ChunkGenerator chunkGen = level.getChunkSource().getGenerator();
        Holder<Biome> biome = level.m_204166_(pos);
        return ForgeEventFactory.getPotentialSpawns(level, category, pos, chunkGen.getMobsAt(biome, structure, category, pos));
    }

    public static int getSpawnGroup() {
        return LHConfig.COMMON.hostilitySpawnCount.get();
    }

    public static double getBonusFactor() {
        return (double) LHConfig.COMMON.hostilitySpawnLevelFactor.get().intValue();
    }

    public static int getMaxTrials() {
        return 4;
    }

    public BurstSpawnerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected void generate(TraitSpawnerData data) {
        if (this.f_58857_ instanceof ServerLevel sl) {
            Optional<ChunkDifficulty> cdcap = ChunkDifficulty.at(this.f_58857_, this.m_58899_());
            if (!cdcap.isEmpty()) {
                SectionDifficulty sec = ((ChunkDifficulty) cdcap.get()).getSection(this.m_58899_().m_123342_());
                if (sec.activePos != null && this.f_58857_.isLoaded(sec.activePos) && this.f_58857_.getBlockEntity(sec.activePos) instanceof BurstSpawnerBlockEntity other && other != this) {
                    other.stop();
                }
                sec.activePos = this.m_58899_();
                int count = 0;
                for (int i = 0; i < getSpawnGroup() * getMaxTrials(); i++) {
                    int x = this.f_58857_.getRandom().nextInt(16);
                    int y = this.f_58857_.getRandom().nextInt(16);
                    int z = this.f_58857_.getRandom().nextInt(16);
                    BlockPos pos = new BlockPos(this.m_58899_().m_123341_() & -8 | x, this.m_58899_().m_123342_() & -8 | y, this.m_58899_().m_123343_() & -8 | z);
                    Optional<MobSpawnSettings.SpawnerData> e = mobsAt(sl, MobCategory.MONSTER, pos).getRandom(this.f_58857_.getRandom());
                    if (e.isPresent() && !((MobSpawnSettings.SpawnerData) e.get()).type.is(LHTagGen.NO_SCALING) && !((MobSpawnSettings.SpawnerData) e.get()).type.is(LHTagGen.NO_TRAIT)) {
                        Entity entity = ((MobSpawnSettings.SpawnerData) e.get()).type.create(sl);
                        if (entity != null && !(entity instanceof Creeper)) {
                            entity.setPos(Vec3.atCenterOf(this.m_58899_()));
                            if (entity instanceof LivingEntity) {
                                LivingEntity le = (LivingEntity) entity;
                                if (MobTraitCap.HOLDER.isProper(le)) {
                                    MobTraitCap cap = (MobTraitCap) MobTraitCap.HOLDER.get(le);
                                    cap.summoned = true;
                                    cap.noDrop = true;
                                    cap.pos = this.m_58899_();
                                    cap.init(this.f_58857_, le, (a, b) -> {
                                        ((ChunkDifficulty) cdcap.get()).modifyInstance(a, b);
                                        b.acceptBonusFactor(getBonusFactor());
                                        b.setFullChance();
                                    });
                                    entity.setPos(Vec3.atCenterOf(this.m_58899_().above()));
                                    data.add(le);
                                    this.f_58857_.m_7967_(entity);
                                    if (++count >= getSpawnGroup()) {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void clearStage() {
        assert this.f_58857_ != null;
        Optional<ChunkDifficulty> cdcap = ChunkDifficulty.at(this.f_58857_, this.m_58899_());
        if (cdcap.isPresent()) {
            SectionDifficulty section = ((ChunkDifficulty) cdcap.get()).getSection(this.m_58899_().m_123342_());
            section.setClear((ChunkDifficulty) cdcap.get(), this.m_58899_());
            section.activePos = null;
        }
    }

    @Override
    protected CustomBossEvent createBossEvent() {
        CustomBossEvent ans = new CustomBossEvent(new ResourceLocation("l2hostility", "hostility_spawner"), LangData.BOSS_EVENT.get(0, getSpawnGroup()).withStyle(ChatFormatting.GOLD));
        ans.m_6451_(BossEvent.BossBarColor.PURPLE);
        ans.m_5648_(BossEvent.BossBarOverlay.NOTCHED_10);
        return ans;
    }
}