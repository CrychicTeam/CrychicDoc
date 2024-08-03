package org.violetmoon.quark.content.experimental.config;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.config.ConfigFlagManager;
import org.violetmoon.zeta.config.type.IConfigType;
import org.violetmoon.zeta.module.ZetaModule;

public class VariantsConfig implements IConfigType {

    private static final VariantsConfig.VariantMap EMPTY_VARIANT_MAP = new VariantsConfig.VariantMap(new HashMap());

    @Config(description = "The list of all variant types available for players to use. Values are treated as suffixes to block IDs for scanning.\nPrefix any variant type with ! to make it show up for Manual Variants but not be automatically scanned for. (e.g. '!polish')")
    private List<String> variantTypes = Arrays.asList("slab", "stairs", "wall", "fence", "fence_gate", "vertical_slab");

    @Config(description = "By default, only a mod's namespace is scanned for variants for its items (e.g. if coolmod adds coolmod:fun_block, it'll search only for coolmod:fun_block_stairs).\n Mods in this list are also scanned for variants if none are found in itself (e.g. if quark is in the list and coolmod:fun_block_stairs doesn't exist, it'll try to look for quark:fun_block_stairs next)")
    private List<String> testedMods = Arrays.asList("quark");

    @Config
    private boolean printVariantMapToLog = false;

    @Config(description = "Format is 'alias=original' in each value (e.g. 'wall=fence' means that a failed search for, minecraft:cobblestone_fence will try cobblestone_wall next)")
    private List<String> aliases = Arrays.asList("carpet=slab", "pane=fence");

    @Config(description = "Ends of block IDs to try and remove when looking for variants. (e.g. minecraft:oak_planks goes into minecraft:oak_stairs, so we have to include '_planks' in this list for it to find them or else it'll only look for minecraft:oak_planks_stairs)")
    private List<String> stripCandidates = Arrays.asList("_planks", "_wool", "_block", "s");

    @Config(description = "Add manual variant overrides here, the format is 'type,block,output' (e.g. polish,minecraft:stone_bricks,minecraft:chiseled_stone_bricks). The type must be listed in Variant Types")
    private List<String> manualVariants = new ArrayList();

    @Config(description = " A list of block IDs and mappings to be excluded from variant selection.\nTo exclude a block from being turned into other blocks, just include the block ID (e.g. minecraft:cobblestone).\nTo exclude a block from having other blocks turned into it, suffix it with = (e.g. =minecraft:cobblestone_stairs)\nTo exclude a specific block->variant combination, put = between the two (e.g. minecraft:cobblestone=minecraft:cobblestone_stairs)")
    private List<String> blacklist = Arrays.asList("minecraft:snow", "minecraft:bamboo", "minecraft:bamboo_block");

    private final Map<Block, VariantsConfig.VariantMap> blockVariants = new HashMap();

    private final Map<Block, Block> originals = new HashMap();

    private final Multimap<String, String> aliasMap = HashMultimap.create();

    private final Multimap<Block, VariantsConfig.ManualVariant> manualVariantMap = HashMultimap.create();

    private final List<String> visibleVariants = new ArrayList();

    private final List<String> sortedSuffixes = new ArrayList();

    @Override
    public void onReload(ZetaModule module, ConfigFlagManager flagManager) {
        this.blockVariants.clear();
        this.visibleVariants.clear();
        this.originals.clear();
        this.aliasMap.clear();
        this.manualVariantMap.clear();
        this.sortedSuffixes.clear();
        if (module == null || module.enabled) {
            for (String s : this.variantTypes) {
                this.visibleVariants.add(s.replaceAll("!", ""));
            }
            this.sortedSuffixes.addAll(this.visibleVariants);
            this.sortedSuffixes.sort((s1, s2) -> {
                int ct1 = s1.replaceAll("[^_]", "").length();
                int ct2 = s2.replaceAll("[^_]", "").length();
                return ct2 - ct1;
            });
            for (String s : this.aliases) {
                String[] toks = s.split("=");
                this.aliasMap.put(toks[1], toks[0]);
            }
            for (String s : this.manualVariants) {
                String[] toks = s.split(",");
                Block block = BuiltInRegistries.BLOCK.get(new ResourceLocation(toks[1]));
                Block out = BuiltInRegistries.BLOCK.get(new ResourceLocation(toks[2]));
                this.manualVariantMap.put(block, new VariantsConfig.ManualVariant(toks[0], out));
            }
            BuiltInRegistries.BLOCK.forEach(this::getVariants);
            if (this.printVariantMapToLog) {
                this.logVariantMap();
            }
        }
    }

    @Nullable
    public String findVariantForBlock(Block variantBlock) {
        String name = BuiltInRegistries.BLOCK.getKey(variantBlock).getPath();
        for (String suffix : this.sortedSuffixes) {
            if (name.endsWith(String.format("_%s", suffix))) {
                return suffix;
            }
            if (this.aliasMap.containsKey(suffix)) {
                for (String alias : this.aliasMap.get(suffix)) {
                    if (name.endsWith(String.format("_%s", alias))) {
                        return suffix;
                    }
                }
            }
        }
        return null;
    }

    @Nullable
    public Block getBlockOfVariant(Block baseBlock, @NotNull String variant) {
        if (!this.sortedSuffixes.contains(variant)) {
            return null;
        } else {
            VariantsConfig.VariantMap map = this.getVariants(baseBlock);
            return (Block) map.variants.get(variant);
        }
    }

    @Nullable
    public String getVariantOfBlock(Block baseBlock, Block possibleVariant) {
        VariantsConfig.VariantMap map = this.getVariants(baseBlock);
        if (map != null) {
            for (Entry<String, Block> entry : map.variants.entrySet()) {
                if (((Block) entry.getValue()).equals(possibleVariant)) {
                    return (String) entry.getKey();
                }
            }
        }
        return null;
    }

    public boolean hasVariants(Block block) {
        return !this.getVariants(block).isEmpty();
    }

    public Collection<Block> getAllVariants(Block block) {
        Map<String, Block> map = this.getVariants(block).variants;
        List<Block> blocks = new ArrayList();
        for (String s : this.variantTypes) {
            if (s.startsWith("!")) {
                s = s.substring(1);
            }
            if (map.containsKey(s)) {
                blocks.add((Block) map.get(s));
            }
        }
        return blocks;
    }

    public Block getOriginalBlock(Block block) {
        return (Block) this.originals.getOrDefault(block, block);
    }

    public boolean isOriginal(Block block) {
        return this.originals.containsValue(block);
    }

    public boolean isVariant(Block block) {
        return this.originals.containsKey(block);
    }

    private VariantsConfig.VariantMap getVariants(Block block) {
        if (this.blockVariants.containsKey(block)) {
            return (VariantsConfig.VariantMap) this.blockVariants.get(block);
        } else {
            Map<String, Block> newVariants = new HashMap();
            if (!this.isBlacklisted(block, null)) {
                for (String s : this.sortedSuffixes) {
                    if (this.variantTypes.contains(s)) {
                        Block suffixed = this.getSuffixedBlock(block, s);
                        if (suffixed != null && !this.isBlacklisted(null, suffixed) && !this.isBlacklisted(block, suffixed)) {
                            newVariants.put(s, suffixed);
                            this.originals.put(suffixed, block);
                        }
                    }
                }
            }
            if (this.manualVariantMap.containsKey(block)) {
                for (VariantsConfig.ManualVariant mv : this.manualVariantMap.get(block)) {
                    newVariants.put(mv.type, mv.out);
                    this.originals.put(mv.out, block);
                }
            }
            if (newVariants.isEmpty()) {
                this.blockVariants.put(block, EMPTY_VARIANT_MAP);
            } else {
                this.blockVariants.put(block, new VariantsConfig.VariantMap(newVariants));
            }
            return this.getVariants(block);
        }
    }

    private Block getSuffixedBlock(Block ogBlock, String suffix) {
        ResourceLocation resloc = BuiltInRegistries.BLOCK.getKey(ogBlock);
        String namespace = resloc.getNamespace();
        String name = resloc.getPath();
        Block ret = this.getSuffixedBlock(namespace, name, suffix);
        if (ret != null) {
            return ret;
        } else {
            for (String mod : this.testedMods) {
                ret = this.getSuffixedBlock(mod, name, suffix);
                if (ret != null) {
                    return ret;
                }
            }
            return null;
        }
    }

    private Block getSuffixedBlock(String namespace, String name, String suffix) {
        for (String strip : this.stripCandidates) {
            if (name.endsWith(strip)) {
                String stripped = name.substring(0, name.length() - strip.length());
                Block strippedAttempt = this.getSuffixedBlock(namespace, stripped, suffix);
                if (strippedAttempt != null) {
                    return strippedAttempt;
                }
            }
        }
        String targetStr = String.format("%s:%s_%s", namespace, name, suffix);
        ResourceLocation target = new ResourceLocation(targetStr);
        Block ret = BuiltInRegistries.BLOCK.get(target);
        if (ret != Blocks.AIR) {
            return ret;
        } else {
            if (this.aliasMap.containsKey(suffix)) {
                for (String alias : this.aliasMap.get(suffix)) {
                    Block aliasAttempt = this.getSuffixedBlock(namespace, name, alias);
                    if (aliasAttempt != null) {
                        return aliasAttempt;
                    }
                }
            }
            return null;
        }
    }

    private boolean isBlacklisted(Block block, Block result) {
        if (this.blacklist.isEmpty()) {
            return false;
        } else {
            String search = "";
            if (block != null) {
                search = search + BuiltInRegistries.BLOCK.getKey(block).toString();
            }
            if (result != null) {
                search = search + "=" + BuiltInRegistries.BLOCK.getKey(result).toString();
            }
            return !search.isEmpty() && this.blacklist.contains(search);
        }
    }

    public boolean isKnownVariant(String variant) {
        return this.visibleVariants.contains(variant);
    }

    public List<String> getVisibleVariants() {
        return this.visibleVariants;
    }

    private void logVariantMap() {
        for (Entry<Block, Block> entry : this.originals.entrySet()) {
            Quark.LOG.info("{} is variant of {}", entry.getKey(), entry.getValue());
        }
    }

    private static record ManualVariant(String type, Block out) {
    }

    private static record VariantMap(Map<String, Block> variants) {

        private boolean isEmpty() {
            return this.variants.isEmpty();
        }
    }
}