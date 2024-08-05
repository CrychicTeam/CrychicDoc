package team.lodestar.lodestone.systems.datagen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class LodestoneDatagenBlockData {

    public static final LodestoneDatagenBlockData EMPTY = new LodestoneDatagenBlockData();

    private final List<TagKey<Block>> tags = new ArrayList();

    public boolean hasInheritedLootTable = false;

    public LodestoneDatagenBlockData addTag(TagKey<Block> blockTagKey) {
        this.tags.add(blockTagKey);
        return this;
    }

    @SafeVarargs
    public final LodestoneDatagenBlockData addTags(TagKey<Block>... blockTagKeys) {
        this.tags.addAll(Arrays.asList(blockTagKeys));
        return this;
    }

    public List<TagKey<Block>> getTags() {
        return this.tags;
    }

    public LodestoneDatagenBlockData hasInheritedLoot() {
        this.hasInheritedLootTable = true;
        return this;
    }

    public LodestoneDatagenBlockData needsPickaxe() {
        return this.addTag(BlockTags.MINEABLE_WITH_PICKAXE);
    }

    public LodestoneDatagenBlockData needsAxe() {
        return this.addTag(BlockTags.MINEABLE_WITH_AXE);
    }

    public LodestoneDatagenBlockData needsShovel() {
        return this.addTag(BlockTags.MINEABLE_WITH_SHOVEL);
    }

    public LodestoneDatagenBlockData needsHoe() {
        return this.addTag(BlockTags.MINEABLE_WITH_HOE);
    }

    public LodestoneDatagenBlockData needsStone() {
        return this.addTag(BlockTags.NEEDS_STONE_TOOL);
    }

    public LodestoneDatagenBlockData needsIron() {
        return this.addTag(BlockTags.NEEDS_IRON_TOOL);
    }

    public LodestoneDatagenBlockData needsDiamond() {
        return this.addTag(BlockTags.NEEDS_DIAMOND_TOOL);
    }
}