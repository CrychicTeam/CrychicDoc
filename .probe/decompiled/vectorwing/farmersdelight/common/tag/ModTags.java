package vectorwing.farmersdelight.common.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {

    public static final TagKey<Block> MINEABLE_WITH_KNIFE = modBlockTag("mineable/knife");

    public static final TagKey<Block> TERRAIN = modBlockTag("terrain");

    public static final TagKey<Block> STRAW_BLOCKS = modBlockTag("straw_blocks");

    public static final TagKey<Block> WILD_CROPS = modBlockTag("wild_crops");

    public static final TagKey<Block> ROPES = modBlockTag("ropes");

    public static final TagKey<Block> HEAT_SOURCES = modBlockTag("heat_sources");

    public static final TagKey<Block> HEAT_CONDUCTORS = modBlockTag("heat_conductors");

    public static final TagKey<Block> TRAY_HEAT_SOURCES = modBlockTag("tray_heat_sources");

    public static final TagKey<Block> COMPOST_ACTIVATORS = modBlockTag("compost_activators");

    public static final TagKey<Block> MUSHROOM_COLONY_GROWABLE_ON = modBlockTag("mushroom_colony_growable_on");

    public static final TagKey<Block> UNAFFECTED_BY_RICH_SOIL = modBlockTag("unaffected_by_rich_soil");

    public static final TagKey<Block> DROPS_CAKE_SLICE = modBlockTag("drops_cake_slice");

    public static final TagKey<Item> WILD_CROPS_ITEM = modItemTag("wild_crops");

    public static final TagKey<Item> STRAW_HARVESTERS = modItemTag("straw_harvesters");

    public static final TagKey<Item> WOLF_PREY = modItemTag("wolf_prey");

    public static final TagKey<Item> CABBAGE_ROLL_INGREDIENTS = modItemTag("cabbage_roll_ingredients");

    public static final TagKey<Item> OFFHAND_EQUIPMENT = modItemTag("offhand_equipment");

    public static final TagKey<Item> KNIVES = modItemTag("tools/knives");

    public static final TagKey<Item> CANVAS_SIGNS = modItemTag("canvas_signs");

    public static final TagKey<Item> HANGING_CANVAS_SIGNS = modItemTag("hanging_canvas_signs");

    public static final TagKey<Item> WOODEN_CABINETS = modItemTag("cabinets/wooden");

    public static final TagKey<Item> CABINETS = modItemTag("cabinets");

    public static final TagKey<Item> SERVING_CONTAINERS = modItemTag("serving_containers");

    public static final TagKey<Item> FLAT_ON_CUTTING_BOARD = modItemTag("flat_on_cutting_board");

    public static final TagKey<EntityType<?>> DOG_FOOD_USERS = modEntityTag("dog_food_users");

    public static final TagKey<EntityType<?>> HORSE_FEED_USERS = modEntityTag("horse_feed_users");

    public static final TagKey<EntityType<?>> HORSE_FEED_TEMPTED = modEntityTag("horse_feed_tempted");

    private static TagKey<Item> modItemTag(String path) {
        return ItemTags.create(new ResourceLocation("farmersdelight", path));
    }

    private static TagKey<Block> modBlockTag(String path) {
        return BlockTags.create(new ResourceLocation("farmersdelight", path));
    }

    private static TagKey<EntityType<?>> modEntityTag(String path) {
        return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("farmersdelight", path));
    }
}