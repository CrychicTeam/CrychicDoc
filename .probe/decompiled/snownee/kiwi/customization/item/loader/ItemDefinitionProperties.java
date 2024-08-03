package snownee.kiwi.customization.item.loader;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import snownee.kiwi.util.codec.CustomizationCodecs;
import snownee.kiwi.util.codec.JavaOps;

public record ItemDefinitionProperties(Optional<ResourceLocation> colorProvider, ItemDefinitionProperties.PartialVanillaProperties vanillaProperties) {

    private static final ItemDefinitionProperties EMPTY;

    public static MapCodec<ItemDefinitionProperties> mapCodec() {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(CustomizationCodecs.strictOptionalField(ResourceLocation.CODEC, "color_provider").forGetter(ItemDefinitionProperties::colorProvider), ItemDefinitionProperties.PartialVanillaProperties.MAP_CODEC.forGetter(ItemDefinitionProperties::vanillaProperties)).apply(instance, ItemDefinitionProperties::new));
    }

    public static MapCodec<Optional<ItemDefinitionProperties>> mapCodecField() {
        return mapCodec().codec().optionalFieldOf("properties");
    }

    public static ItemDefinitionProperties empty() {
        return EMPTY;
    }

    public ItemDefinitionProperties merge(ItemDefinitionProperties templateProps) {
        return new ItemDefinitionProperties(or(this.colorProvider, templateProps.colorProvider), this.vanillaProperties.merge(templateProps.vanillaProperties));
    }

    private static <T> Optional<T> or(Optional<T> a, Optional<T> b) {
        return a.isPresent() ? a : b;
    }

    static {
        ItemDefinitionProperties.PartialVanillaProperties vanillaProperties = (ItemDefinitionProperties.PartialVanillaProperties) ItemDefinitionProperties.PartialVanillaProperties.MAP_CODEC.codec().parse(JavaOps.INSTANCE, Map.of()).getOrThrow(false, e -> {
            throw new IllegalStateException("Failed to parse empty ItemDefinitionProperties: " + e);
        });
        EMPTY = new ItemDefinitionProperties(Optional.empty(), vanillaProperties);
    }

    public static record PartialVanillaProperties(Optional<Integer> maxStackSize, Optional<Integer> maxDamage, Optional<ResourceKey<Item>> craftingRemainingItem, Optional<FoodProperties> food, Optional<Rarity> rarity) {

        public static final MapCodec<ItemDefinitionProperties.PartialVanillaProperties> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Codec.intRange(1, 64).optionalFieldOf("stacks_to").forGetter(ItemDefinitionProperties.PartialVanillaProperties::maxStackSize), Codec.intRange(1, Integer.MAX_VALUE).optionalFieldOf("max_damage").forGetter(ItemDefinitionProperties.PartialVanillaProperties::maxDamage), ResourceKey.codec(Registries.ITEM).optionalFieldOf("crafting_remaining_item").forGetter(ItemDefinitionProperties.PartialVanillaProperties::craftingRemainingItem), CustomizationCodecs.FOOD.optionalFieldOf("food").forGetter(ItemDefinitionProperties.PartialVanillaProperties::food), CustomizationCodecs.RARITY_CODEC.optionalFieldOf("rarity").forGetter(ItemDefinitionProperties.PartialVanillaProperties::rarity)).apply(instance, ItemDefinitionProperties.PartialVanillaProperties::new));

        public ItemDefinitionProperties.PartialVanillaProperties merge(ItemDefinitionProperties.PartialVanillaProperties templateProps) {
            return new ItemDefinitionProperties.PartialVanillaProperties(ItemDefinitionProperties.or(this.maxStackSize, templateProps.maxStackSize), ItemDefinitionProperties.or(this.maxDamage, templateProps.maxDamage), ItemDefinitionProperties.or(this.craftingRemainingItem, templateProps.craftingRemainingItem), ItemDefinitionProperties.or(this.food, templateProps.food), ItemDefinitionProperties.or(this.rarity, templateProps.rarity));
        }
    }
}