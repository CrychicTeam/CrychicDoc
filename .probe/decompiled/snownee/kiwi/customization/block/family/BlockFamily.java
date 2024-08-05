package snownee.kiwi.customization.block.family;

import com.google.common.base.Preconditions;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import snownee.kiwi.util.codec.CustomizationCodecs;

public class BlockFamily {

    public static final Codec<BlockFamily> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.BOOL.optionalFieldOf("strict", false).forGetter($ -> true), ResourceKey.codec(Registries.BLOCK).listOf().optionalFieldOf("blocks", List.of()).forGetter($ -> $.blockHolders().stream().map(Holder.Reference::m_205785_).toList()), ResourceKey.codec(Registries.ITEM).listOf().optionalFieldOf("items", List.of()).forGetter($ -> $.itemHolders().stream().map(Holder.Reference::m_205785_).toList()), CustomizationCodecs.compactList(ResourceKey.codec(Registries.ITEM)).optionalFieldOf("exchange_inputs_in_viewer", List.of()).forGetter($ -> $.exchangeInputsInViewer().stream().map(Holder.Reference::m_205785_).toList()), Codec.BOOL.optionalFieldOf("stonecutter_exchange", false).forGetter(BlockFamily::stonecutterExchange), ResourceKey.codec(Registries.ITEM).optionalFieldOf("stonecutter_from").forGetter($ -> $.stonecutterSource().map(Holder.Reference::m_205785_)), Codec.intRange(1, 64).optionalFieldOf("stonecutter_from_multiplier", 1).forGetter(BlockFamily::stonecutterSourceMultiplier), BlockFamily.SwitchAttrs.CODEC.optionalFieldOf("switch", BlockFamily.SwitchAttrs.DISABLED).forGetter(BlockFamily::switchAttrs)).apply(instance, BlockFamily::new));

    private final List<Holder.Reference<Block>> blocks;

    private final List<Holder.Reference<Item>> items;

    private final List<Holder.Reference<Item>> exchangeInputsInViewer;

    private final boolean stonecutterExchange;

    private final Optional<Holder.Reference<Item>> stonecutterFrom;

    private final int stonecutterFromMultiplier;

    private final BlockFamily.SwitchAttrs switchAttrs;

    private Ingredient ingredient;

    private Ingredient ingredientInViewer;

    public BlockFamily(boolean strict, List<ResourceKey<Block>> blocks, List<ResourceKey<Item>> items, List<ResourceKey<Item>> exchangeInputsInViewer, boolean stonecutterExchange, Optional<ResourceKey<Item>> stonecutterFrom, int stonecutterFromMultiplier, BlockFamily.SwitchAttrs switchAttrs) {
        this.blocks = blocks.stream().map($ -> {
            Optional<Holder.Reference<Block>> holder = BuiltInRegistries.BLOCK.m_203636_($);
            if (strict) {
                Preconditions.checkArgument(holder.isPresent(), "Block %s not found", $);
            }
            return holder;
        }).filter(Optional::isPresent).map(Optional::get).toList();
        this.items = Stream.concat(this.blocks.stream().map(Holder::m_203334_).map(ItemLike::m_5456_).filter(Predicate.not(Items.AIR::equals)).mapToInt(BuiltInRegistries.ITEM::m_7447_).distinct().mapToObj(BuiltInRegistries.ITEM::m_203300_).map(Optional::orElseThrow), items.stream().map($ -> {
            Optional<Holder.Reference<Item>> holder = BuiltInRegistries.ITEM.m_203636_($);
            if (strict) {
                Preconditions.checkArgument(holder.isPresent(), "Item %s not found", $);
            }
            return holder;
        }).filter(Optional::isPresent).map(Optional::get)).toList();
        this.exchangeInputsInViewer = exchangeInputsInViewer.stream().map($ -> {
            Optional<Holder.Reference<Item>> holder = BuiltInRegistries.ITEM.m_203636_($);
            if (strict) {
                Preconditions.checkArgument(holder.isPresent(), "Item %s not found", $);
            }
            return holder;
        }).filter(Optional::isPresent).map(Optional::get).toList();
        this.stonecutterExchange = stonecutterExchange;
        this.stonecutterFrom = stonecutterFrom.map($ -> {
            Optional<Holder.Reference<Item>> holder = BuiltInRegistries.ITEM.m_203636_($);
            if (strict) {
                Preconditions.checkArgument(holder.isPresent(), "Item %s not found", $);
            }
            return holder;
        }).filter(Optional::isPresent).map(Optional::get);
        this.stonecutterFromMultiplier = stonecutterFromMultiplier;
        this.switchAttrs = switchAttrs;
        Preconditions.checkArgument(!blocks.isEmpty() || !items.isEmpty(), "No entries found in family");
        Preconditions.checkArgument(this.blocks().distinct().count() == (long) this.blocks.size(), "Duplicate blocks found in family %s", this);
        Preconditions.checkArgument(this.items().distinct().count() == (long) this.items.size(), "Duplicate items found in family %s", this);
    }

    public List<Holder.Reference<Block>> blockHolders() {
        return this.blocks;
    }

    public List<Holder.Reference<Item>> itemHolders() {
        return this.items;
    }

    public List<Holder.Reference<Item>> exchangeInputsInViewer() {
        return this.exchangeInputsInViewer;
    }

    public Stream<Block> blocks() {
        return this.blocks.stream().map(Holder::m_203334_);
    }

    public Stream<Item> items() {
        return this.items.stream().map(Holder::m_203334_);
    }

    public boolean stonecutterExchange() {
        return this.stonecutterExchange;
    }

    public Optional<Holder.Reference<Item>> stonecutterSource() {
        return this.stonecutterFrom;
    }

    public int stonecutterSourceMultiplier() {
        return this.stonecutterFromMultiplier;
    }

    public Ingredient stonecutterSourceIngredient() {
        return (Ingredient) this.stonecutterFrom.map(holder -> Ingredient.of((ItemLike) holder.value())).orElse(Ingredient.EMPTY);
    }

    public BlockFamily.SwitchAttrs switchAttrs() {
        return this.switchAttrs;
    }

    protected Ingredient toIngredient(List<? extends Holder<Item>> items) {
        return Ingredient.of((ItemLike[]) items.stream().map(Holder::m_203334_).filter(item -> BlockFamilies.getConvertRatio(item) >= 1.0F).toArray(ItemLike[]::new));
    }

    public Ingredient ingredient() {
        if (this.ingredient == null) {
            this.ingredient = this.toIngredient(this.items);
        }
        return this.ingredient;
    }

    public Ingredient ingredientInViewer() {
        if (this.ingredientInViewer == null) {
            if (this.exchangeInputsInViewer.isEmpty()) {
                this.ingredientInViewer = this.ingredient();
            } else {
                this.ingredientInViewer = this.toIngredient(this.exchangeInputsInViewer);
            }
        }
        return this.ingredientInViewer;
    }

    public boolean contains(Item item) {
        return this.items.stream().anyMatch(h -> ((Item) h.value()).asItem() == item);
    }

    public String toString() {
        return "BlockFamily{blocks=" + this.blocks + ", stonecutterFrom=" + this.stonecutterFrom + "}";
    }

    public static record SwitchAttrs(boolean enabled, boolean cascading, boolean creativeOnly) {

        public static final Codec<BlockFamily.SwitchAttrs> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.BOOL.optionalFieldOf("enabled", true).forGetter(BlockFamily.SwitchAttrs::enabled), Codec.BOOL.optionalFieldOf("cascading", false).forGetter(BlockFamily.SwitchAttrs::cascading), Codec.BOOL.optionalFieldOf("creative_only", false).forGetter(BlockFamily.SwitchAttrs::creativeOnly)).apply(instance, BlockFamily.SwitchAttrs::create));

        public static final BlockFamily.SwitchAttrs DISABLED = new BlockFamily.SwitchAttrs(false, false, false);

        private static final Interner<BlockFamily.SwitchAttrs> INTERNER = Interners.newStrongInterner();

        public static BlockFamily.SwitchAttrs create(boolean enabled, boolean cascading, boolean creativeOnly) {
            if (!enabled) {
                Preconditions.checkArgument(!cascading, "Cascading switch must be disabled if switch is disabled");
                Preconditions.checkArgument(!creativeOnly, "Creative only switch must be disabled if switch is disabled");
                return DISABLED;
            } else {
                return (BlockFamily.SwitchAttrs) INTERNER.intern(new BlockFamily.SwitchAttrs(true, cascading, creativeOnly));
            }
        }
    }
}