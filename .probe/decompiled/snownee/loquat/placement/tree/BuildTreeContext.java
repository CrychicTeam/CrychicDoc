package snownee.loquat.placement.tree;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class BuildTreeContext {

    public final RandomSource random;

    public final Structure.GenerationContext generationContext;

    public final Registry<StructureTemplatePool> pools;

    public final List<String> globalTags = Lists.newArrayList();

    public TreeNode root;

    public BuildTreeContext(TreeNode root, RandomSource random, Structure.GenerationContext generationContext, Registry<StructureTemplatePool> pools) {
        this.root = root;
        this.random = random;
        this.generationContext = generationContext;
        this.pools = pools;
    }

    public StructureTemplateManager getTemplateManager() {
        return this.generationContext.structureTemplateManager();
    }

    public ChunkGenerator getChunkGenerator() {
        return this.generationContext.chunkGenerator();
    }

    public BiomeSource getBiomeSource() {
        return this.generationContext.biomeSource();
    }

    public LevelHeightAccessor getHeightAccessor() {
        return this.generationContext.heightAccessor();
    }

    public Predicate<Holder<Biome>> getValidBiome() {
        return this.generationContext.validBiome();
    }

    public RegistryAccess getRegistryAccess() {
        return this.generationContext.registryAccess();
    }

    public WorldgenRandom getWorldgenRandom() {
        return this.generationContext.random();
    }
}