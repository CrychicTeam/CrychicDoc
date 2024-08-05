package vectorwing.farmersdelight.common.tag;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class CompatibilityTags {

    public static final String CREATE = "create";

    public static final TagKey<Block> CREATE_PASSIVE_BOILER_HEATERS = externalBlockTag("create", "passive_boiler_heaters");

    public static final TagKey<Block> CREATE_BRITTLE = externalBlockTag("create", "brittle");

    public static final TagKey<Item> CREATE_UPRIGHT_ON_BELT = externalItemTag("create", "upright_on_belt");

    public static final String CREATE_CA = "createaddition";

    public static final TagKey<Item> CREATE_CA_PLANT_FOODS = externalItemTag("createaddition", "plant_foods");

    public static final TagKey<Item> CREATE_CA_PLANTS = externalItemTag("createaddition", "plants");

    public static final String ORIGINS = "origins";

    public static final TagKey<Item> ORIGINS_MEAT = externalItemTag("origins", "meat");

    public static final String SERENE_SEASONS = "sereneseasons";

    public static final TagKey<Block> SERENE_SEASONS_AUTUMN_CROPS_BLOCK = externalBlockTag("sereneseasons", "autumn_crops");

    public static final TagKey<Block> SERENE_SEASONS_SPRING_CROPS_BLOCK = externalBlockTag("sereneseasons", "spring_crops");

    public static final TagKey<Block> SERENE_SEASONS_SUMMER_CROPS_BLOCK = externalBlockTag("sereneseasons", "summer_crops");

    public static final TagKey<Block> SERENE_SEASONS_WINTER_CROPS_BLOCK = externalBlockTag("sereneseasons", "winter_crops");

    public static final TagKey<Block> SERENE_SEASONS_UNBREAKABLE_FERTILE_CROPS = externalBlockTag("sereneseasons", "unbreakable_infertile_crops");

    public static final TagKey<Item> SERENE_SEASONS_AUTUMN_CROPS = externalItemTag("sereneseasons", "autumn_crops");

    public static final TagKey<Item> SERENE_SEASONS_SPRING_CROPS = externalItemTag("sereneseasons", "spring_crops");

    public static final TagKey<Item> SERENE_SEASONS_SUMMER_CROPS = externalItemTag("sereneseasons", "summer_crops");

    public static final TagKey<Item> SERENE_SEASONS_WINTER_CROPS = externalItemTag("sereneseasons", "winter_crops");

    public static final String TINKERS_CONSTRUCT = "tconstruct";

    public static final TagKey<Item> TINKERS_CONSTRUCT_SEEDS = externalItemTag("tconstruct", "seeds");

    private static TagKey<Item> externalItemTag(String modId, String path) {
        return ItemTags.create(new ResourceLocation(modId, path));
    }

    private static TagKey<Block> externalBlockTag(String modId, String path) {
        return BlockTags.create(new ResourceLocation(modId, path));
    }
}