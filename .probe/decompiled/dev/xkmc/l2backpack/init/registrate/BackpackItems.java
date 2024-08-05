package dev.xkmc.l2backpack.init.registrate;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2backpack.content.backpack.BackpackItem;
import dev.xkmc.l2backpack.content.bag.BookBag;
import dev.xkmc.l2backpack.content.bag.EquipmentBag;
import dev.xkmc.l2backpack.content.drawer.DrawerItem;
import dev.xkmc.l2backpack.content.quickswap.armorswap.ArmorSetSwap;
import dev.xkmc.l2backpack.content.quickswap.armorswap.ArmorSwap;
import dev.xkmc.l2backpack.content.quickswap.merged.EnderSwitch;
import dev.xkmc.l2backpack.content.quickswap.merged.MultiSwitch;
import dev.xkmc.l2backpack.content.quickswap.quiver.Quiver;
import dev.xkmc.l2backpack.content.quickswap.scabbard.Scabbard;
import dev.xkmc.l2backpack.content.remote.drawer.EnderDrawerItem;
import dev.xkmc.l2backpack.content.remote.player.EnderBackpackItem;
import dev.xkmc.l2backpack.content.remote.worldchest.WorldChestItem;
import dev.xkmc.l2backpack.content.tool.DestroyTweakerTool;
import dev.xkmc.l2backpack.content.tool.PickupTweakerTool;
import dev.xkmc.l2backpack.init.L2Backpack;
import dev.xkmc.l2backpack.init.data.TagGen;
import java.util.Objects;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;

@MethodsReturnNonnullByDefault
public class BackpackItems {

    public static final RegistryEntry<CreativeModeTab> TAB = L2Backpack.REGISTRATE.buildL2CreativeTab("backpack", "L2 Backpack", e -> e.icon(BackpackItems.ENDER_BACKPACK::asStack));

    public static final ItemEntry<BackpackItem>[] BACKPACKS = new ItemEntry[16];

    public static final ItemEntry<WorldChestItem>[] DIMENSIONAL_STORAGE;

    public static final ItemEntry<EnderBackpackItem> ENDER_BACKPACK;

    public static final ItemEntry<Item> ENDER_POCKET;

    public static final ItemEntry<PickupTweakerTool> PICKUP_TWEAKER;

    public static final ItemEntry<DestroyTweakerTool> DESTROY_TWEAKER;

    public static final ItemEntry<EquipmentBag> ARMOR_BAG;

    public static final ItemEntry<BookBag> BOOK_BAG;

    public static final ItemEntry<Quiver> QUIVER;

    public static final ItemEntry<Scabbard> SCABBARD;

    public static final ItemEntry<ArmorSwap> ARMOR_SWAP;

    public static final ItemEntry<ArmorSetSwap> SUIT_SWAP;

    public static final ItemEntry<MultiSwitch> MULTI_SWITCH;

    public static final ItemEntry<EnderSwitch> ENDER_SWITCH;

    public static final ItemEntry<DrawerItem> DRAWER;

    public static final ItemEntry<EnderDrawerItem> ENDER_DRAWER;

    public static <T extends Quiver> void createArrowBagModel(DataGenContext<Item, T> ctx, RegistrateItemModelProvider pvd) {
        ItemModelBuilder builder = (ItemModelBuilder) pvd.withExistingParent(ctx.getName(), "generated");
        builder.texture("layer0", "item/" + ctx.getName() + "_0");
        for (int i = 1; i < 4; i++) {
            String name = ctx.getName() + "_" + i;
            ItemModelBuilder ret = (ItemModelBuilder) pvd.withExistingParent(name, "generated");
            ret.texture("layer0", "item/" + name);
            ItemModelBuilder.OverrideBuilder override = builder.override();
            override.predicate(new ResourceLocation("l2backpack", "arrow"), (float) i);
            override.model(new ModelFile.UncheckedModelFile("l2backpack:item/" + name));
        }
    }

    public static void register() {
    }

    public static ItemEntry<Item> simpleItem(String id) {
        return L2Backpack.REGISTRATE.item(id, Item::new).defaultModel().defaultLang().register();
    }

    static {
        ITagManager<Item> manager = (ITagManager<Item>) Objects.requireNonNull(ForgeRegistries.ITEMS.tags());
        TagKey<Item> curios_tag = manager.createTagKey(new ResourceLocation("curios", "back"));
        for (int i = 0; i < 16; i++) {
            DyeColor color = DyeColor.values()[i];
            BACKPACKS[i] = L2Backpack.REGISTRATE.item("backpack_" + color.getName(), p -> new BackpackItem(color, p)).tag(new TagKey[] { TagGen.BACKPACKS, curios_tag }).model((ctx, pvd) -> ((ItemModelBuilder) pvd.getBuilder(ctx.getName())).parent(new ModelFile.UncheckedModelFile("builtin/entity"))).lang(RegistrateLangProvider.toEnglishName(color.getName() + "_backpack")).register();
        }
        DIMENSIONAL_STORAGE = new ItemEntry[16];
        for (int i = 0; i < 16; i++) {
            DyeColor color = DyeColor.values()[i];
            DIMENSIONAL_STORAGE[i] = L2Backpack.REGISTRATE.item("dimensional_storage_" + color.getName(), p -> new WorldChestItem(color, p)).tag(new TagKey[] { TagGen.DIMENSIONAL_STORAGES, curios_tag }).model((ctx, pvd) -> ((ItemModelBuilder) pvd.getBuilder(ctx.getName())).parent(new ModelFile.UncheckedModelFile("builtin/entity"))).lang(RegistrateLangProvider.toEnglishName(color.getName() + "_dimensional_backpack")).register();
        }
        ENDER_BACKPACK = L2Backpack.REGISTRATE.item("ender_backpack", EnderBackpackItem::new).model((ctx, pvd) -> ((ItemModelBuilder) pvd.getBuilder(ctx.getName())).parent(new ModelFile.UncheckedModelFile("builtin/entity"))).tag(new TagKey[] { curios_tag, TagGen.ENDER_CHEST }).defaultLang().register();
        ENDER_POCKET = simpleItem("ender_pocket");
        PICKUP_TWEAKER = L2Backpack.REGISTRATE.item("pickup_tweaker_tool", p -> new PickupTweakerTool(p.stacksTo(1))).defaultModel().defaultLang().register();
        DESTROY_TWEAKER = L2Backpack.REGISTRATE.item("destroy_tweaker_tool", p -> new DestroyTweakerTool(p.stacksTo(1))).defaultModel().defaultLang().register();
        ARMOR_BAG = L2Backpack.REGISTRATE.item("armor_bag", EquipmentBag::new).tag(new TagKey[] { TagGen.BAGS }).model((ctx, pvd) -> pvd.generated(ctx).override().predicate(new ResourceLocation("l2backpack", "fill"), 1.0F).model(((ItemModelBuilder) pvd.getBuilder(ctx.getName() + "_filled")).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", pvd.modLoc("item/" + ctx.getName() + "_filled")))).lang("Equipment Bag").register();
        BOOK_BAG = L2Backpack.REGISTRATE.item("book_bag", BookBag::new).tag(new TagKey[] { TagGen.BAGS }).model((ctx, pvd) -> pvd.generated(ctx).override().predicate(new ResourceLocation("l2backpack", "fill"), 1.0F).model(((ItemModelBuilder) pvd.getBuilder(ctx.getName() + "_filled")).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", pvd.modLoc("item/" + ctx.getName() + "_filled")))).defaultLang().register();
        QUIVER = L2Backpack.REGISTRATE.item("arrow_bag", Quiver::new).model(BackpackItems::createArrowBagModel).tag(new TagKey[] { curios_tag, TagGen.SWAPS }).lang("Quiver").register();
        SCABBARD = L2Backpack.REGISTRATE.item("tool_swap", Scabbard::new).defaultModel().tag(new TagKey[] { curios_tag, TagGen.SWAPS }).defaultLang().register();
        ARMOR_SWAP = L2Backpack.REGISTRATE.item("armor_swap", ArmorSwap::new).defaultModel().tag(new TagKey[] { curios_tag, TagGen.SWAPS }).defaultLang().register();
        SUIT_SWAP = L2Backpack.REGISTRATE.item("suit_swap", ArmorSetSwap::new).defaultModel().tag(new TagKey[] { curios_tag, TagGen.SWAPS }).defaultLang().register();
        MULTI_SWITCH = L2Backpack.REGISTRATE.item("combined_swap", MultiSwitch::new).defaultModel().tag(new TagKey[] { curios_tag, TagGen.SWAPS }).removeTab(TAB.getKey()).defaultLang().register();
        ENDER_SWITCH = L2Backpack.REGISTRATE.item("ender_swap", EnderSwitch::new).defaultModel().tag(new TagKey[] { curios_tag, TagGen.SWAPS, TagGen.ENDER_CHEST }).removeTab(TAB.getKey()).defaultLang().register();
        DRAWER = L2Backpack.REGISTRATE.item("drawer", p -> new DrawerItem((Block) BackpackBlocks.DRAWER.get(), p)).model((ctx, pvd) -> ((ItemModelBuilder) pvd.getBuilder(ctx.getName())).parent(new ModelFile.UncheckedModelFile("builtin/entity"))).tag(new TagKey[] { TagGen.DRAWERS }).defaultLang().register();
        ENDER_DRAWER = L2Backpack.REGISTRATE.item("ender_drawer", p -> new EnderDrawerItem((Block) BackpackBlocks.ENDER_DRAWER.get(), p)).model((ctx, pvd) -> ((ItemModelBuilder) pvd.getBuilder(ctx.getName())).parent(new ModelFile.UncheckedModelFile("builtin/entity"))).tag(new TagKey[] { TagGen.DRAWERS }).defaultLang().register();
    }
}