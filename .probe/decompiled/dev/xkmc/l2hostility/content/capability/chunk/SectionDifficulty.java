package dev.xkmc.l2hostility.content.capability.chunk;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.config.WorldDifficultyConfig;
import dev.xkmc.l2hostility.content.logic.DifficultyLevel;
import dev.xkmc.l2hostility.content.logic.LevelEditor;
import dev.xkmc.l2hostility.content.logic.MobDifficultyCollector;
import dev.xkmc.l2hostility.events.CapabilityEvents;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.LevelChunkSection;

@SerialClass
public class SectionDifficulty {

    @SerialField
    int index;

    @SerialField
    public BlockPos activePos = null;

    @SerialField(toClient = true)
    private final DifficultyLevel difficulty = new DifficultyLevel();

    @SerialField(toClient = true)
    private SectionDifficulty.SectionStage stage = SectionDifficulty.SectionStage.INIT;

    LevelChunkSection section;

    public static Optional<SectionDifficulty> sectionAt(Level level, BlockPos pos) {
        return ChunkDifficulty.at(level, pos).map(e -> e.getSection(pos.m_123342_()));
    }

    public void modifyInstance(Level level, BlockPos pos, MobDifficultyCollector instance) {
        this.modifyInstanceInternal(level, pos, instance);
        if (LHConfig.COMMON.allowSectionDifficulty.get()) {
            instance.acceptBonusLevel(this.difficulty.getLevel());
        }
        if (this.stage == SectionDifficulty.SectionStage.CLEARED) {
            instance.setCap(0);
        }
    }

    private void modifyInstanceInternal(Level level, BlockPos pos, MobDifficultyCollector instance) {
        WorldDifficultyConfig.DifficultyConfig levelDiff = (WorldDifficultyConfig.DifficultyConfig) L2Hostility.DIFFICULTY.getMerged().levelMap.get(level.dimension().location());
        if (levelDiff == null) {
            levelDiff = WorldDifficultyConfig.defaultLevel();
        }
        instance.acceptConfig(levelDiff);
        Holder<Biome> biome = level.m_204166_(pos);
        biome.unwrapKey().map(e -> (WorldDifficultyConfig.DifficultyConfig) L2Hostility.DIFFICULTY.getMerged().biomeMap.get(e.location())).ifPresent(instance::acceptConfig);
        instance.acceptBonusLevel((int) Math.round(LHConfig.COMMON.distanceFactor.get() * Math.sqrt(1.0 * (double) pos.m_123341_() * (double) pos.m_123341_() + 1.0 * (double) pos.m_123343_() * (double) pos.m_123343_())));
    }

    public List<Component> getSectionDifficultyDetail(Player player) {
        if (this.isCleared()) {
            return List.of();
        } else {
            WorldDifficultyConfig.DifficultyConfig levelDiff = (WorldDifficultyConfig.DifficultyConfig) L2Hostility.DIFFICULTY.getMerged().levelMap.get(player.m_9236_().dimension().location());
            int dim = levelDiff == null ? WorldDifficultyConfig.defaultLevel().base() : levelDiff.base();
            BlockPos pos = player.m_20183_();
            Holder<Biome> biome = player.m_9236_().m_204166_(pos);
            int bio = (Integer) biome.unwrapKey().map(e -> (WorldDifficultyConfig.DifficultyConfig) L2Hostility.DIFFICULTY.getMerged().biomeMap.get(e.location())).map(WorldDifficultyConfig.DifficultyConfig::base).orElse(0);
            int dist = (int) Math.round(LHConfig.COMMON.distanceFactor.get() * Math.sqrt((double) (pos.m_123341_() * pos.m_123341_() + pos.m_123343_() * pos.m_123343_())));
            int adaptive = this.difficulty.getLevel();
            return List.of(LangData.INFO_SECTION_DIM_LEVEL.get(dim).withStyle(ChatFormatting.GRAY), LangData.INFO_SECTION_BIOME_LEVEL.get(bio).withStyle(ChatFormatting.GRAY), LangData.INFO_SECTION_DISTANCE_LEVEL.get(dist).withStyle(ChatFormatting.GRAY), LangData.INFO_SECTION_ADAPTIVE_LEVEL.get(adaptive).withStyle(ChatFormatting.GRAY));
        }
    }

    public boolean isCleared() {
        return this.stage == SectionDifficulty.SectionStage.CLEARED;
    }

    public boolean setClear(ChunkDifficulty chunk, BlockPos pos) {
        if (this.stage == SectionDifficulty.SectionStage.CLEARED) {
            return false;
        } else {
            this.stage = SectionDifficulty.SectionStage.CLEARED;
            CapabilityEvents.markDirty(chunk);
            chunk.chunk.m_8092_(true);
            return true;
        }
    }

    public boolean setUnclear(ChunkDifficulty chunk, BlockPos pos) {
        if (this.stage == SectionDifficulty.SectionStage.INIT) {
            return false;
        } else {
            this.stage = SectionDifficulty.SectionStage.INIT;
            CapabilityEvents.markDirty(chunk);
            chunk.chunk.m_8092_(true);
            return true;
        }
    }

    public void addKillHistory(ChunkDifficulty chunk, Player player, LivingEntity mob, MobTraitCap cap) {
        this.difficulty.grow(1.0, cap);
        CapabilityEvents.markDirty(chunk);
        chunk.chunk.m_8092_(true);
    }

    public LevelEditor getLevelEditor(Level level, BlockPos pos) {
        MobDifficultyCollector col = new MobDifficultyCollector();
        this.modifyInstanceInternal(level, pos, col);
        DifficultyLevel diff = LHConfig.COMMON.allowSectionDifficulty.get() ? this.difficulty : new DifficultyLevel();
        return new LevelEditor(diff, col.getBase());
    }

    public double getScale(Level level, BlockPos pos) {
        MobDifficultyCollector col = new MobDifficultyCollector();
        this.modifyInstanceInternal(level, pos, col);
        return col.scale;
    }

    public static enum SectionStage {

        INIT, CLEARED
    }
}