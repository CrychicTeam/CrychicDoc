package dev.latvian.mods.kubejs.block.state;

import com.google.common.collect.UnmodifiableIterator;
import com.mojang.serialization.DataResult;
import dev.latvian.mods.kubejs.level.gen.ruletest.AllMatchRuleTest;
import dev.latvian.mods.kubejs.level.gen.ruletest.AlwaysFalseRuleTest;
import dev.latvian.mods.kubejs.level.gen.ruletest.AnyMatchRuleTest;
import dev.latvian.mods.kubejs.level.gen.ruletest.InvertRuleTest;
import dev.latvian.mods.kubejs.recipe.ReplacementMatch;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.util.ListJS;
import dev.latvian.mods.kubejs.util.MapJS;
import dev.latvian.mods.kubejs.util.Tags;
import dev.latvian.mods.kubejs.util.UtilsJS;
import dev.latvian.mods.rhino.mod.util.NBTUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.AlwaysTrueTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockStateMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import org.jetbrains.annotations.Nullable;

public sealed interface BlockStatePredicate extends Predicate<BlockState>, ReplacementMatch permits BlockStatePredicate.Simple, BlockStatePredicate.BlockMatch, BlockStatePredicate.StateMatch, BlockStatePredicate.TagMatch, BlockStatePredicate.RegexMatch, BlockStatePredicate.OrMatch, BlockStatePredicate.NotMatch, BlockStatePredicate.AndMatch {

    ResourceLocation AIR_ID = new ResourceLocation("minecraft:air");

    boolean test(BlockState var1);

    default boolean testBlock(Block block) {
        return this.test(block.defaultBlockState());
    }

    @Nullable
    default RuleTest asRuleTest() {
        return null;
    }

    static BlockStatePredicate fromString(String s) {
        if (s.equals("*")) {
            return BlockStatePredicate.Simple.ALL;
        } else if (s.equals("-")) {
            return BlockStatePredicate.Simple.NONE;
        } else if (s.startsWith("#")) {
            return new BlockStatePredicate.TagMatch(Tags.block(new ResourceLocation(s.substring(1))));
        } else {
            if (s.indexOf(91) != -1) {
                BlockState state = UtilsJS.parseBlockState(s);
                if (state != Blocks.AIR.defaultBlockState()) {
                    return new BlockStatePredicate.StateMatch(state);
                }
            } else {
                Block block = RegistryInfo.BLOCK.getValue(new ResourceLocation(s));
                if (block != Blocks.AIR) {
                    return new BlockStatePredicate.BlockMatch(block);
                }
            }
            return BlockStatePredicate.Simple.NONE;
        }
    }

    static BlockStatePredicate of(Object o) {
        if (o == null || o == BlockStatePredicate.Simple.ALL) {
            return BlockStatePredicate.Simple.ALL;
        } else if (o == BlockStatePredicate.Simple.NONE) {
            return BlockStatePredicate.Simple.NONE;
        } else {
            List<?> list = ListJS.orSelf(o);
            if (list.isEmpty()) {
                return BlockStatePredicate.Simple.NONE;
            } else if (list.size() > 1) {
                ArrayList<BlockStatePredicate> predicates = new ArrayList();
                for (Object o1 : list) {
                    BlockStatePredicate p = of(o1);
                    if (p == BlockStatePredicate.Simple.ALL) {
                        return BlockStatePredicate.Simple.ALL;
                    }
                    if (p != BlockStatePredicate.Simple.NONE) {
                        predicates.add(p);
                    }
                }
                return (BlockStatePredicate) (predicates.isEmpty() ? BlockStatePredicate.Simple.NONE : (predicates.size() == 1 ? (BlockStatePredicate) predicates.get(0) : new BlockStatePredicate.OrMatch(predicates)));
            } else {
                Map<?, ?> map = MapJS.of(o);
                if (map == null) {
                    return ofSingle(list.get(0));
                } else if (map.isEmpty()) {
                    return BlockStatePredicate.Simple.ALL;
                } else {
                    ArrayList<BlockStatePredicate> predicates = new ArrayList();
                    if (map.get("or") != null) {
                        predicates.add(of(map.get("or")));
                    }
                    if (map.get("not") != null) {
                        predicates.add(new BlockStatePredicate.NotMatch(of(map.get("not"))));
                    }
                    return new BlockStatePredicate.AndMatch(predicates);
                }
            }
        }
    }

    static RuleTest ruleTestOf(Object o) {
        if (o instanceof RuleTest) {
            return (RuleTest) o;
        } else {
            if (o instanceof BlockStatePredicate bsp && bsp.asRuleTest() != null) {
                return bsp.asRuleTest();
            }
            return (RuleTest) Optional.ofNullable(NBTUtils.toTagCompound(o)).map(tag -> RuleTest.CODEC.parse(NbtOps.INSTANCE, tag)).flatMap(DataResult::result).or(() -> Optional.ofNullable(of(o).asRuleTest())).orElseThrow(() -> new IllegalArgumentException("Could not parse valid rule test from " + o + "!"));
        }
    }

    private static BlockStatePredicate ofSingle(Object o) {
        if (o instanceof BlockStatePredicate) {
            return (BlockStatePredicate) o;
        } else if (o instanceof Block block) {
            return new BlockStatePredicate.BlockMatch(block);
        } else if (o instanceof BlockState state) {
            return new BlockStatePredicate.StateMatch(state);
        } else if (o instanceof TagKey tag) {
            return new BlockStatePredicate.TagMatch(tag);
        } else {
            Pattern pattern = UtilsJS.parseRegex(o);
            return (BlockStatePredicate) (pattern == null ? fromString(o.toString()) : new BlockStatePredicate.RegexMatch(pattern));
        }
    }

    default Collection<BlockState> getBlockStates() {
        HashSet<BlockState> states = new HashSet();
        for (BlockState state : UtilsJS.getAllBlockStates()) {
            if (this.test(state)) {
                states.add(state);
            }
        }
        return states;
    }

    default Collection<Block> getBlocks() {
        HashSet<Block> blocks = new HashSet();
        for (BlockState state : this.getBlockStates()) {
            blocks.add(state.m_60734_());
        }
        return blocks;
    }

    default Set<ResourceLocation> getBlockIds() {
        Set<ResourceLocation> set = new LinkedHashSet();
        for (Block block : this.getBlocks()) {
            ResourceLocation blockId = RegistryInfo.BLOCK.getId(block);
            if (blockId != null) {
                set.add(blockId);
            }
        }
        return set;
    }

    default boolean check(List<OreConfiguration.TargetBlockState> targetStates) {
        for (OreConfiguration.TargetBlockState state : targetStates) {
            if (this.test(state.state)) {
                return true;
            }
        }
        return false;
    }

    public static final class AndMatch implements BlockStatePredicate {

        private final List<BlockStatePredicate> list;

        private final Collection<BlockState> cachedStates;

        public AndMatch(List<BlockStatePredicate> list) {
            this.list = list;
            this.cachedStates = new LinkedHashSet();
            for (Entry<ResourceKey<Block>, Block> entry : RegistryInfo.BLOCK.entrySet()) {
                UnmodifiableIterator var4 = ((Block) entry.getValue()).getStateDefinition().getPossibleStates().iterator();
                while (var4.hasNext()) {
                    BlockState state = (BlockState) var4.next();
                    boolean match = true;
                    for (BlockStatePredicate predicate : list) {
                        if (!predicate.test(state)) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        this.cachedStates.add(state);
                    }
                }
            }
        }

        @Override
        public boolean test(BlockState state) {
            for (BlockStatePredicate predicate : this.list) {
                if (!predicate.test(state)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public boolean testBlock(Block block) {
            for (BlockStatePredicate predicate : this.list) {
                if (!predicate.testBlock(block)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public Collection<Block> getBlocks() {
            Set<Block> set = new HashSet();
            for (BlockState blockState : this.getBlockStates()) {
                set.add(blockState.m_60734_());
            }
            return set;
        }

        @Override
        public Collection<BlockState> getBlockStates() {
            return this.cachedStates;
        }

        @Override
        public RuleTest asRuleTest() {
            AllMatchRuleTest test = new AllMatchRuleTest();
            for (BlockStatePredicate predicate : this.list) {
                test.rules.add(predicate.asRuleTest());
            }
            return test;
        }
    }

    public static record BlockMatch(Block block) implements BlockStatePredicate {

        @Override
        public boolean test(BlockState state) {
            return state.m_60713_(this.block);
        }

        @Override
        public boolean testBlock(Block block) {
            return this.block == block;
        }

        @Override
        public Collection<Block> getBlocks() {
            return Collections.singleton(this.block);
        }

        @Override
        public Collection<BlockState> getBlockStates() {
            return this.block.getStateDefinition().getPossibleStates();
        }

        @Override
        public Set<ResourceLocation> getBlockIds() {
            ResourceLocation blockId = RegistryInfo.BLOCK.getId(this.block);
            return blockId == null ? Collections.emptySet() : Collections.singleton(blockId);
        }

        @Override
        public RuleTest asRuleTest() {
            return new BlockMatchTest(this.block);
        }
    }

    public static final class NotMatch implements BlockStatePredicate {

        private final BlockStatePredicate predicate;

        private final Collection<BlockState> cachedStates;

        public NotMatch(BlockStatePredicate predicate) {
            this.predicate = predicate;
            this.cachedStates = new LinkedHashSet();
            for (Entry<ResourceKey<Block>, Block> entry : RegistryInfo.BLOCK.entrySet()) {
                UnmodifiableIterator var4 = ((Block) entry.getValue()).getStateDefinition().getPossibleStates().iterator();
                while (var4.hasNext()) {
                    BlockState state = (BlockState) var4.next();
                    if (!predicate.test(state)) {
                        this.cachedStates.add(state);
                    }
                }
            }
        }

        @Override
        public boolean test(BlockState state) {
            return !this.predicate.test(state);
        }

        @Override
        public boolean testBlock(Block block) {
            return !this.predicate.testBlock(block);
        }

        @Override
        public Collection<Block> getBlocks() {
            Set<Block> set = new HashSet();
            for (BlockState blockState : this.getBlockStates()) {
                set.add(blockState.m_60734_());
            }
            return set;
        }

        @Override
        public Collection<BlockState> getBlockStates() {
            return this.cachedStates;
        }

        @Override
        public Set<ResourceLocation> getBlockIds() {
            HashSet<ResourceLocation> set = new HashSet();
            for (Block block : this.getBlocks()) {
                set.add(RegistryInfo.BLOCK.getId(block));
            }
            return set;
        }

        @Override
        public RuleTest asRuleTest() {
            return new InvertRuleTest(this.predicate.asRuleTest());
        }
    }

    public static record OrMatch(List<BlockStatePredicate> list) implements BlockStatePredicate {

        @Override
        public boolean test(BlockState state) {
            for (BlockStatePredicate predicate : this.list) {
                if (predicate.test(state)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean testBlock(Block block) {
            for (BlockStatePredicate predicate : this.list) {
                if (predicate.testBlock(block)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public Collection<Block> getBlocks() {
            HashSet<Block> set = new HashSet();
            for (BlockStatePredicate predicate : this.list) {
                set.addAll(predicate.getBlocks());
            }
            return set;
        }

        @Override
        public Collection<BlockState> getBlockStates() {
            HashSet<BlockState> set = new HashSet();
            for (BlockStatePredicate predicate : this.list) {
                set.addAll(predicate.getBlockStates());
            }
            return set;
        }

        @Override
        public Set<ResourceLocation> getBlockIds() {
            Set<ResourceLocation> set = new LinkedHashSet();
            for (BlockStatePredicate predicate : this.list) {
                set.addAll(predicate.getBlockIds());
            }
            return set;
        }

        @Override
        public RuleTest asRuleTest() {
            AnyMatchRuleTest test = new AnyMatchRuleTest();
            for (BlockStatePredicate predicate : this.list) {
                test.rules.add(predicate.asRuleTest());
            }
            return test;
        }
    }

    public static final class RegexMatch implements BlockStatePredicate {

        public final Pattern pattern;

        private final LinkedHashSet<Block> matchedBlocks;

        public RegexMatch(Pattern p) {
            this.pattern = p;
            this.matchedBlocks = new LinkedHashSet();
            for (BlockState state : UtilsJS.getAllBlockStates()) {
                Block block = state.m_60734_();
                if (!this.matchedBlocks.contains(block) && this.pattern.matcher(RegistryInfo.BLOCK.getId(block).toString()).find()) {
                    this.matchedBlocks.add(state.m_60734_());
                }
            }
        }

        @Override
        public boolean test(BlockState state) {
            return this.matchedBlocks.contains(state.m_60734_());
        }

        @Override
        public boolean testBlock(Block block) {
            return this.matchedBlocks.contains(block);
        }

        @Override
        public Collection<Block> getBlocks() {
            return this.matchedBlocks;
        }

        @Override
        public RuleTest asRuleTest() {
            AnyMatchRuleTest test = new AnyMatchRuleTest();
            for (Block block : this.matchedBlocks) {
                test.rules.add(new BlockMatchTest(block));
            }
            return test;
        }
    }

    public static enum Simple implements BlockStatePredicate {

        ALL(true), NONE(false);

        private final boolean match;

        private Simple(boolean match) {
            this.match = match;
        }

        @Override
        public boolean test(BlockState state) {
            return this.match;
        }

        @Override
        public boolean testBlock(Block block) {
            return this.match;
        }

        @Override
        public RuleTest asRuleTest() {
            return (RuleTest) (this.match ? AlwaysTrueTest.INSTANCE : AlwaysFalseRuleTest.INSTANCE);
        }

        @Override
        public Collection<BlockState> getBlockStates() {
            return (Collection<BlockState>) (this.match ? UtilsJS.getAllBlockStates() : List.of());
        }
    }

    public static record StateMatch(BlockState state) implements BlockStatePredicate {

        @Override
        public boolean test(BlockState s) {
            return this.state == s;
        }

        @Override
        public boolean testBlock(Block block) {
            return this.state.m_60734_() == block;
        }

        @Override
        public Collection<Block> getBlocks() {
            return Collections.singleton(this.state.m_60734_());
        }

        @Override
        public Collection<BlockState> getBlockStates() {
            return Collections.singleton(this.state);
        }

        @Override
        public Set<ResourceLocation> getBlockIds() {
            ResourceLocation blockId = RegistryInfo.BLOCK.getId(this.state.m_60734_());
            return blockId == null ? Collections.emptySet() : Collections.singleton(blockId);
        }

        @Override
        public RuleTest asRuleTest() {
            return new BlockStateMatchTest(this.state);
        }
    }

    public static record TagMatch(TagKey<Block> tag) implements BlockStatePredicate {

        @Override
        public boolean test(BlockState state) {
            return state.m_204336_(this.tag);
        }

        @Override
        public boolean testBlock(Block block) {
            return block.builtInRegistryHolder().is(this.tag);
        }

        @Override
        public Collection<Block> getBlocks() {
            return Util.make(new LinkedHashSet(), set -> {
                for (Holder<Block> holder : BuiltInRegistries.BLOCK.m_206058_(this.tag)) {
                    set.add(holder.value());
                }
            });
        }

        @Override
        public RuleTest asRuleTest() {
            return new TagMatchTest(this.tag);
        }
    }
}