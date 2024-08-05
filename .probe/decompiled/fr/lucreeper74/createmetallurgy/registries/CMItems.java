package fr.lucreeper74.createmetallurgy.registries;

import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.item.CombustibleItem;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.entry.ItemEntry;
import fr.lucreeper74.createmetallurgy.CreateMetallurgy;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;

public class CMItems {

    public static final ItemEntry<Item> TUNGSTEN_INGOT = taggedIngredient("tungsten_ingot", AllTags.forgeItemTag("ingots/tungsten"), Tags.Items.INGOTS);

    public static final ItemEntry<Item> TUNGSTEN_SHEET = taggedIngredient("tungsten_sheet", AllTags.forgeItemTag("plates/tungsten"), AllTags.forgeItemTag("plates"));

    public static final ItemEntry<Item> TUNGSTEN_NUGGET = taggedIngredient("tungsten_nugget", AllTags.forgeItemTag("nuggets/tungsten"), Tags.Items.NUGGETS);

    public static final ItemEntry<Item> TUNGSTEN_WIRE = taggedIngredient("tungsten_wire", AllTags.forgeItemTag("wires/tungsten"), AllTags.forgeItemTag("wires"));

    public static final ItemEntry<Item> RAW_WOLFRAMITE = taggedIngredient("raw_wolframite", AllTags.forgeItemTag("raw_materials/wolframite"), AllTags.forgeItemTag("raw_materials"));

    public static final ItemEntry<Item> CRUSHED_RAW_WOLFRAMITE = taggedIngredient("crushed_raw_wolframite", AllTags.forgeItemTag("crushed_raw_materials/wolframite"));

    public static final ItemEntry<Item> DIRTY_WOLFRAMITE_DUST = taggedIngredient("dirty_wolframite_dust", AllTags.forgeItemTag("dirty_dusts/dirty_wolframite"), AllTags.forgeItemTag("dirty_dusts"));

    public static final ItemEntry<Item> WOLFRAMITE_DUST = taggedIngredient("wolframite_dust", AllTags.forgeItemTag("dusts/wolframite"), AllTags.forgeItemTag("dusts"));

    public static final ItemEntry<Item> DIRTY_GOLD_DUST = taggedIngredient("dirty_gold_dust", AllTags.forgeItemTag("dirty_dusts/dirty_gold"), AllTags.forgeItemTag("dirty_dusts"));

    public static final ItemEntry<Item> GOLD_DUST = taggedIngredient("gold_dust", AllTags.forgeItemTag("dusts/gold"), AllTags.forgeItemTag("dusts"));

    public static final ItemEntry<Item> DIRTY_IRON_DUST = taggedIngredient("dirty_iron_dust", AllTags.forgeItemTag("dirty_dusts/dirty_iron"), AllTags.forgeItemTag("dirty_dusts"));

    public static final ItemEntry<Item> IRON_DUST = taggedIngredient("iron_dust", AllTags.forgeItemTag("dusts/iron"), AllTags.forgeItemTag("dusts"));

    public static final ItemEntry<Item> DIRTY_COPPER_DUST = taggedIngredient("dirty_copper_dust", AllTags.forgeItemTag("dirty_dusts/dirty_copper"), AllTags.forgeItemTag("dirty_dusts"));

    public static final ItemEntry<Item> COPPER_DUST = taggedIngredient("copper_dust", AllTags.forgeItemTag("dusts/copper"), AllTags.forgeItemTag("dusts"));

    public static final ItemEntry<Item> DIRTY_ZINC_DUST = taggedIngredient("dirty_zinc_dust", AllTags.forgeItemTag("dirty_dusts/dirty_zinc"), AllTags.forgeItemTag("dirty_dusts"));

    public static final ItemEntry<Item> ZINC_DUST = taggedIngredient("zinc_dust", AllTags.forgeItemTag("dusts/zinc"), AllTags.forgeItemTag("dusts"));

    public static final ItemEntry<Item> GRAPHITE_BLANK_MOLD = taggedIngredient("graphite_blank_mold", AllTags.forgeItemTag("graphite_molds/blank"), AllTags.forgeItemTag("graphite_molds"));

    public static final ItemEntry<Item> GRAPHITE_INGOT_MOLD = taggedIngredient("graphite_ingot_mold", AllTags.forgeItemTag("graphite_molds/ingot"), AllTags.forgeItemTag("graphite_molds"));

    public static final ItemEntry<Item> GRAPHITE_NUGGET_MOLD = taggedIngredient("graphite_nugget_mold", AllTags.forgeItemTag("graphite_molds/nugget"), AllTags.forgeItemTag("graphite_molds"));

    public static final ItemEntry<Item> GRAPHITE_PLATE_MOLD = taggedIngredient("graphite_plate_mold", AllTags.forgeItemTag("graphite_molds/plate"), AllTags.forgeItemTag("graphite_molds"));

    public static final ItemEntry<CombustibleItem> COKE = ((ItemBuilder) CreateMetallurgy.REGISTRATE.item("coke", CombustibleItem::new).tag(new TagKey[] { AllTags.forgeItemTag("coal_coke") }).onRegister(i -> i.setBurnTime(2000))).register();

    public static final ItemEntry<Item> GRAPHITE = taggedIngredient("graphite", AllTags.forgeItemTag("graphite"));

    public static final ItemEntry<Item> STEEL_INGOT = taggedIngredient("steel_ingot", AllTags.forgeItemTag("ingots/steel"), Tags.Items.INGOTS);

    public static final ItemEntry<Item> STURDY_WHISK = CreateMetallurgy.REGISTRATE.item("sturdy_whisk", Item::new).register();

    public static final ItemEntry<Item> TUNGSTEN_WIRE_SPOOL = CreateMetallurgy.REGISTRATE.item("tungsten_wire_spool", Item::new).register();

    public static final ItemEntry<Item> SANDPAPER_BELT = CreateMetallurgy.REGISTRATE.item("sandpaper_belt", Item::new).register();

    @SafeVarargs
    private static ItemEntry<Item> taggedIngredient(String name, TagKey<Item>... tags) {
        return CreateMetallurgy.REGISTRATE.item(name, Item::new).tag(tags).register();
    }

    public static void register() {
    }

    static {
        CreateMetallurgy.REGISTRATE.setCreativeTab(CMCreativeTabs.MAIN_CREATIVE_TAB);
    }
}