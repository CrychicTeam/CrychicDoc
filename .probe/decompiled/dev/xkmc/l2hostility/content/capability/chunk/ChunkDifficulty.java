package dev.xkmc.l2hostility.content.capability.chunk;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.logic.MobDifficultyCollector;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.OnInject;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.ImposterProtoChunk;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

@SerialClass
public class ChunkDifficulty implements RegionalDifficultyModifier {

    public static Capability<ChunkDifficulty> CAPABILITY = CapabilityManager.get(new CapabilityToken<ChunkDifficulty>() {
    });

    public final LevelChunk chunk;

    @SerialField
    private ChunkDifficulty.ChunkStage stage = ChunkDifficulty.ChunkStage.PRE_INIT;

    @SerialField(toClient = true)
    private SectionDifficulty[] sections;

    public static Optional<ChunkDifficulty> at(Level level, BlockPos pos) {
        return at(level, pos.m_123341_() >> 4, pos.m_123343_() >> 4);
    }

    public static Optional<ChunkDifficulty> at(Level level, int x, int z) {
        ChunkAccess chunk = level.getChunk(x, z, ChunkStatus.CARVERS, false);
        if (chunk instanceof ImposterProtoChunk im) {
            chunk = im.getWrapped();
        }
        return chunk instanceof LevelChunk c ? c.getCapability(CAPABILITY).resolve() : Optional.empty();
    }

    protected ChunkDifficulty(LevelChunk chunk) {
        this.chunk = chunk;
    }

    private void check() {
        if (this.stage == ChunkDifficulty.ChunkStage.PRE_INIT) {
            this.stage = ChunkDifficulty.ChunkStage.INIT;
        }
    }

    public SectionDifficulty getSection(int y) {
        int index = (y >> 4) - this.chunk.m_151560_();
        index = Mth.clamp(index, 0, this.sections.length - 1);
        return this.sections[index];
    }

    @Override
    public void modifyInstance(BlockPos pos, MobDifficultyCollector instance) {
        this.check();
        this.getSection(pos.m_123342_()).modifyInstance(this.chunk.getLevel(), pos, instance);
    }

    public void addKillHistory(Player player, LivingEntity mob, MobTraitCap cap) {
        BlockPos pos = mob.m_20183_();
        int index = -this.chunk.m_151560_() + (pos.m_123342_() >> 4);
        if (index >= 0 && index < this.sections.length) {
            this.sections[index].addKillHistory(this, player, mob, cap);
        }
    }

    @OnInject
    public void init() {
        int size = this.chunk.getLevel().m_151559_();
        if (this.sections == null || this.sections.length != size) {
            this.sections = new SectionDifficulty[size];
            for (int i = 0; i < size; i++) {
                this.sections[i] = new SectionDifficulty();
                this.sections[i].index = this.chunk.m_151560_() + i;
            }
        }
        for (int i = 0; i < size; i++) {
            this.sections[i].section = this.chunk.m_183278_(i);
        }
    }

    public static void register() {
    }

    public static enum ChunkStage {

        PRE_INIT, INIT
    }
}