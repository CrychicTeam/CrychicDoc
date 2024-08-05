package snownee.kiwi.customization.block.family;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import snownee.kiwi.AbstractModule;
import snownee.kiwi.util.KHolder;

public class BlockFamilyInferrer {

    public static final TagKey<Block> IGNORE = AbstractModule.blockTag("kswitch", "ignore");

    private final List<KHolder<BlockFamily>> families = Lists.newArrayList();

    private final Set<Block> capturedBlocks = Sets.newHashSet();

    private final List<String> colorPrefixed = Lists.newArrayList();

    private final List<String> colorSuffixed = Lists.newArrayList();

    private final List<String> logs = List.of("%s_log", "%s_wood", "stripped_%s_log", "stripped_%s_wood");

    private final List<String> netherLogs = List.of("%s_stem", "%s_hyphae", "stripped_%s_stem", "stripped_%s_hyphae");

    private final List<String> general = List.of("%s", "%s_block", "%s_stairs", "%s_slab", "%s_wall", "%s_fence", "%s_fence_gate", "%s_door", "%s_trapdoor", "%s_button", "%s_pressure_plate");

    private final List<String> variants = List.of("%s", "chiseled_%s", "polished_%s", "cut_%s", "smooth_%s", "cracked_%s", "%s_bricks", "%s_pillar");

    public BlockFamilyInferrer() {
        for (DyeColor color : DyeColor.values()) {
            this.colorPrefixed.add(color.getName() + "_%s");
            this.colorSuffixed.add("%s_" + color.getName());
        }
    }

    public Collection<KHolder<BlockFamily>> generate() {
        List<Holder<Block>> sorted = Lists.newArrayList();
        for (Holder<Block> holder : BuiltInRegistries.BLOCK.m_206115_()) {
            String path = ((ResourceKey) holder.unwrapKey().orElseThrow()).location().getPath();
            if ((path.startsWith("pink_") || path.endsWith("_pink") || path.endsWith("_log") || path.endsWith("_stem") || path.endsWith("_stairs") || path.endsWith("_slab") || path.startsWith("smooth_")) && !holder.is(IGNORE)) {
                sorted.add(holder);
            }
        }
        sorted.sort((a, b) -> {
            String aPath = ((ResourceKey) a.unwrapKey().orElseThrow()).location().getPath();
            String bPath = ((ResourceKey) b.unwrapKey().orElseThrow()).location().getPath();
            boolean aIsStairs = aPath.endsWith("_stairs");
            boolean bIsStairs = bPath.endsWith("_stairs");
            return Boolean.compare(bIsStairs, aIsStairs);
        });
        for (Holder<Block> holderx : sorted) {
            Block block = holderx.value();
            if (!this.capturedBlocks.contains(block) && BlockFamilies.findQuickSwitch(block.asItem(), true).isEmpty()) {
                ResourceLocation key = ((ResourceKey) holderx.unwrapKey().orElseThrow()).location();
                String path = key.getPath();
                boolean captured = false;
                if (path.startsWith("pink_")) {
                    ResourceLocation id = key.withPath(path.substring(5));
                    this.generateColored(id, this.colorPrefixed);
                    captured = true;
                } else if (path.endsWith("_pink")) {
                    ResourceLocation id = key.withPath(path.substring(0, path.length() - 5));
                    this.generateColored(id, this.colorSuffixed);
                    captured = true;
                }
                if (path.endsWith("_log")) {
                    if (holderx.is(BlockTags.LOGS)) {
                        ResourceLocation id = key.withPath(path.substring(0, path.length() - 4));
                        this.fromTemplates(id, "logs", this.logs, true);
                    }
                } else if (path.endsWith("_stem")) {
                    if (holderx.is(BlockTags.LOGS)) {
                        ResourceLocation id = key.withPath(path.substring(0, path.length() - 5));
                        this.fromTemplates(id, "logs", this.netherLogs, true);
                    }
                } else if (path.endsWith("_stairs")) {
                    if (block instanceof StairBlock) {
                        StairBlock stairBlock = (StairBlock) block;
                        if (!stairBlock.baseState.m_60795_()) {
                            ResourceLocation id = key.withPath(path.substring(0, path.length() - 7));
                            List<Holder.Reference<Block>> blocks = this.collectBlocks(id, this.general);
                            blocks.addAll(this.collectBlocks(id, this.variants));
                            this.family(id, "variants", blocks.stream().distinct().toList(), true);
                        }
                    }
                } else {
                    if (path.endsWith("_slab")) {
                        ResourceLocation id = key.withPath(path.substring(0, path.length() - 5));
                        this.fromTemplates(id, "general", this.general, true);
                        captured = true;
                    } else if (path.startsWith("smooth_")) {
                        ResourceLocation id = key.withPath(path.substring(7));
                        this.fromTemplates(id, "variants", this.variants, true);
                        captured = true;
                    }
                    if (!captured) {
                        throw new IllegalStateException("Unrecognized block: " + holderx.value());
                    }
                }
            }
        }
        List<String> normalCopperTemplate = List.of("%s_block", "cut_%s", "chiseled_%s", "%s_grate");
        List<String> otherCopperTemplate = List.of("%s", "cut_%s", "chiseled_%s", "%s_grate");
        ResourceLocation copperId = new ResourceLocation("copper");
        for (String waxed : List.of("", "waxed_")) {
            for (String variant : List.of("", "exposed_", "weathered_", "oxidized_")) {
                List<String> template = variant.isEmpty() ? normalCopperTemplate : otherCopperTemplate;
                template = template.stream().map($ -> waxed + variant + $).toList();
                this.fromTemplates(copperId, waxed + variant + "copper", template, true);
            }
        }
        return this.families;
    }

    private List<Holder.Reference<Block>> collectBlocks(ResourceLocation id, List<String> templates) {
        List<Holder.Reference<Block>> blocks = Lists.newArrayList();
        for (String template : templates) {
            ResourceLocation blockId = id.withPath(String.format(template, id.getPath()));
            Optional<Holder.Reference<Block>> holder = BuiltInRegistries.BLOCK.m_203636_(ResourceKey.create(Registries.BLOCK, blockId));
            holder.ifPresent(blocks::add);
        }
        return blocks;
    }

    private void fromTemplates(ResourceLocation id, String desc, List<String> templates, boolean cascading) {
        List<Holder.Reference<Block>> blocks = this.collectBlocks(id, templates);
        if (blocks.size() >= 2) {
            this.family(id, desc, blocks, cascading);
        }
    }

    private void generateColored(ResourceLocation id, List<String> templates) {
        List<Holder.Reference<Block>> blocks = this.collectBlocks(id, templates);
        if (blocks.size() == templates.size()) {
            this.family(id, "colored", blocks, false);
        }
    }

    private void family(ResourceLocation id, String desc, List<Holder.Reference<Block>> blocks, boolean cascading) {
        List<ResourceKey<Block>> blockKeys = blocks.stream().filter($ -> !$.is(IGNORE)).map(Holder.Reference::m_205785_).toList();
        KHolder<BlockFamily> family = new KHolder<>(id.withPrefix("auto/%s/".formatted(desc)), new BlockFamily(false, blockKeys, List.of(), List.of(), false, Optional.empty(), 1, BlockFamily.SwitchAttrs.create(true, cascading, false)));
        this.families.add(family);
        family.value().blocks().forEach(this.capturedBlocks::add);
    }
}