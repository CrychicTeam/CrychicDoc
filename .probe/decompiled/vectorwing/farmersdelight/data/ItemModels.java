package vectorwing.farmersdelight.data;

import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModItems;

public class ItemModels extends ItemModelProvider {

    public static final String GENERATED = "item/generated";

    public static final String HANDHELD = "item/handheld";

    public static final ResourceLocation MUG = new ResourceLocation("farmersdelight", "item/mug");

    public ItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, "farmersdelight", existingFileHelper);
    }

    @Override
    protected void registerModels() {
        Set<Item> items = (Set<Item>) ForgeRegistries.ITEMS.getValues().stream().filter(i -> "farmersdelight".equals(ForgeRegistries.ITEMS.getKey(i).getNamespace())).collect(Collectors.toSet());
        items.remove(ModItems.SKILLET.get());
        this.itemGeneratedModel(ModItems.WILD_RICE.get(), this.resourceBlock(this.itemName(ModItems.WILD_RICE.get()) + "_top"));
        items.remove(ModItems.WILD_RICE.get());
        this.itemGeneratedModel(ModItems.BROWN_MUSHROOM_COLONY.get(), this.resourceBlock(this.itemName(ModItems.BROWN_MUSHROOM_COLONY.get()) + "_stage3"));
        items.remove(ModItems.BROWN_MUSHROOM_COLONY.get());
        this.itemGeneratedModel(ModItems.RED_MUSHROOM_COLONY.get(), this.resourceBlock(this.itemName(ModItems.RED_MUSHROOM_COLONY.get()) + "_stage3"));
        items.remove(ModItems.RED_MUSHROOM_COLONY.get());
        this.blockBasedModel(ModItems.TATAMI.get(), "_half");
        items.remove(ModItems.TATAMI.get());
        this.blockBasedModel(ModItems.ORGANIC_COMPOST.get(), "_0");
        items.remove(ModItems.ORGANIC_COMPOST.get());
        Set<Item> mugItems = Sets.newHashSet(new Item[] { ModItems.HOT_COCOA.get(), ModItems.APPLE_CIDER.get(), ModItems.MELON_JUICE.get() });
        takeAll(items, (Item[]) mugItems.toArray(new Item[0])).forEach(item -> this.itemMugModel(item, this.resourceItem(this.itemName(item))));
        Set<Item> spriteBlockItems = Sets.newHashSet(new Item[] { ModItems.FULL_TATAMI_MAT.get(), ModItems.HALF_TATAMI_MAT.get(), ModItems.ROPE.get(), ModItems.CANVAS_SIGN.get(), ModItems.HANGING_CANVAS_SIGN.get(), ModItems.WHITE_CANVAS_SIGN.get(), ModItems.WHITE_HANGING_CANVAS_SIGN.get(), ModItems.ORANGE_CANVAS_SIGN.get(), ModItems.ORANGE_HANGING_CANVAS_SIGN.get(), ModItems.MAGENTA_CANVAS_SIGN.get(), ModItems.MAGENTA_HANGING_CANVAS_SIGN.get(), ModItems.LIGHT_BLUE_CANVAS_SIGN.get(), ModItems.LIGHT_BLUE_HANGING_CANVAS_SIGN.get(), ModItems.YELLOW_CANVAS_SIGN.get(), ModItems.YELLOW_HANGING_CANVAS_SIGN.get(), ModItems.LIME_CANVAS_SIGN.get(), ModItems.LIME_HANGING_CANVAS_SIGN.get(), ModItems.PINK_CANVAS_SIGN.get(), ModItems.PINK_HANGING_CANVAS_SIGN.get(), ModItems.GRAY_CANVAS_SIGN.get(), ModItems.GRAY_HANGING_CANVAS_SIGN.get(), ModItems.LIGHT_GRAY_CANVAS_SIGN.get(), ModItems.LIGHT_GRAY_HANGING_CANVAS_SIGN.get(), ModItems.CYAN_CANVAS_SIGN.get(), ModItems.CYAN_HANGING_CANVAS_SIGN.get(), ModItems.PURPLE_CANVAS_SIGN.get(), ModItems.PURPLE_HANGING_CANVAS_SIGN.get(), ModItems.BLUE_CANVAS_SIGN.get(), ModItems.BLUE_HANGING_CANVAS_SIGN.get(), ModItems.BROWN_CANVAS_SIGN.get(), ModItems.BROWN_HANGING_CANVAS_SIGN.get(), ModItems.GREEN_CANVAS_SIGN.get(), ModItems.GREEN_HANGING_CANVAS_SIGN.get(), ModItems.RED_CANVAS_SIGN.get(), ModItems.RED_HANGING_CANVAS_SIGN.get(), ModItems.BLACK_CANVAS_SIGN.get(), ModItems.BLACK_HANGING_CANVAS_SIGN.get(), ModItems.APPLE_PIE.get(), ModItems.SWEET_BERRY_CHEESECAKE.get(), ModItems.CHOCOLATE_PIE.get(), ModItems.CABBAGE_SEEDS.get(), ModItems.TOMATO_SEEDS.get(), ModItems.ONION.get(), ModItems.RICE.get(), ModItems.ROAST_CHICKEN_BLOCK.get(), ModItems.STUFFED_PUMPKIN_BLOCK.get(), ModItems.HONEY_GLAZED_HAM_BLOCK.get(), ModItems.SHEPHERDS_PIE_BLOCK.get(), ModItems.RICE_ROLL_MEDLEY_BLOCK.get() });
        takeAll(items, (Item[]) spriteBlockItems.toArray(new Item[0])).forEach(item -> this.withExistingParent(this.itemName(item), "item/generated").texture("layer0", this.resourceItem(this.itemName(item))));
        Set<Item> flatBlockItems = Sets.newHashSet(new Item[] { ModItems.SAFETY_NET.get(), ModItems.SANDY_SHRUB.get(), ModItems.WILD_BEETROOTS.get(), ModItems.WILD_CABBAGES.get(), ModItems.WILD_CARROTS.get(), ModItems.WILD_ONIONS.get(), ModItems.WILD_POTATOES.get(), ModItems.WILD_TOMATOES.get() });
        takeAll(items, (Item[]) flatBlockItems.toArray(new Item[0])).forEach(item -> this.itemGeneratedModel(item, this.resourceBlock(this.itemName(item))));
        takeAll(items, i -> i instanceof BlockItem).forEach(item -> this.blockBasedModel(item, ""));
        Set<Item> handheldItems = Sets.newHashSet(new Item[] { ModItems.BARBECUE_STICK.get(), ModItems.HAM.get(), ModItems.SMOKED_HAM.get(), ModItems.FLINT_KNIFE.get(), ModItems.IRON_KNIFE.get(), ModItems.DIAMOND_KNIFE.get(), ModItems.GOLDEN_KNIFE.get(), ModItems.NETHERITE_KNIFE.get() });
        takeAll(items, (Item[]) handheldItems.toArray(new Item[0])).forEach(item -> this.itemHandheldModel(item, this.resourceItem(this.itemName(item))));
        items.forEach(item -> this.itemGeneratedModel(item, this.resourceItem(this.itemName(item))));
    }

    public void blockBasedModel(Item item, String suffix) {
        this.withExistingParent(this.itemName(item), this.resourceBlock(this.itemName(item) + suffix));
    }

    public void itemHandheldModel(Item item, ResourceLocation texture) {
        this.withExistingParent(this.itemName(item), "item/handheld").texture("layer0", texture);
    }

    public void itemGeneratedModel(Item item, ResourceLocation texture) {
        this.withExistingParent(this.itemName(item), "item/generated").texture("layer0", texture);
    }

    public void itemMugModel(Item item, ResourceLocation texture) {
        this.withExistingParent(this.itemName(item), MUG).texture("layer0", texture);
    }

    private String itemName(Item item) {
        return ForgeRegistries.ITEMS.getKey(item).getPath();
    }

    public ResourceLocation resourceBlock(String path) {
        return new ResourceLocation("farmersdelight", "block/" + path);
    }

    public ResourceLocation resourceItem(String path) {
        return new ResourceLocation("farmersdelight", "item/" + path);
    }

    @SafeVarargs
    public static <T> Collection<T> takeAll(Set<? extends T> src, T... items) {
        List<T> ret = Arrays.asList(items);
        for (T item : items) {
            if (!src.contains(item)) {
                FarmersDelight.LOGGER.warn("Item {} not found in set", item);
            }
        }
        if (!src.removeAll(ret)) {
            FarmersDelight.LOGGER.warn("takeAll array didn't yield anything ({})", Arrays.toString(items));
        }
        return ret;
    }

    public static <T> Collection<T> takeAll(Set<T> src, Predicate<T> pred) {
        List<T> ret = new ArrayList();
        Iterator<T> iter = src.iterator();
        while (iter.hasNext()) {
            T item = (T) iter.next();
            if (pred.test(item)) {
                iter.remove();
                ret.add(item);
            }
        }
        if (ret.isEmpty()) {
            FarmersDelight.LOGGER.warn("takeAll predicate yielded nothing", new Throwable());
        }
        return ret;
    }
}