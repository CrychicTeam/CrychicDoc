package net.mehvahdjukaar.supplementaries.common.utils;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.api.IFlowerModelProvider;
import net.mehvahdjukaar.supplementaries.common.utils.forge.FlowerPotHandlerImpl;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public class FlowerPotHandler {

    private static final Map<Item, ResourceLocation> SPECIAL_FLOWER_BOX_FLOWERS = new IdentityHashMap();

    public static final List<ResourceLocation> CUSTOM_MODELS = new ArrayList();

    @Contract
    @ExpectPlatform
    @Transformed
    public static Block getEmptyPot(FlowerPotBlock fullPot) {
        return FlowerPotHandlerImpl.getEmptyPot(fullPot);
    }

    @Contract
    @ExpectPlatform
    @Transformed
    public static Block getFullPot(FlowerPotBlock emptyPot, Block flowerBlock) {
        return FlowerPotHandlerImpl.getFullPot(emptyPot, flowerBlock);
    }

    @ExpectPlatform
    @Transformed
    public static boolean isEmptyPot(Block b) {
        return FlowerPotHandlerImpl.isEmptyPot(b);
    }

    @ExpectPlatform
    @Transformed
    public static void setup() {
        FlowerPotHandlerImpl.setup();
    }

    public static void registerCustomFlower(Item item, ResourceLocation model) {
        SPECIAL_FLOWER_BOX_FLOWERS.put(item, model);
    }

    private static void registerCompatFlower(String itemRes) {
        ResourceLocation id = new ResourceLocation(itemRes);
        Optional<Item> opt = BuiltInRegistries.ITEM.m_6612_(id);
        if (opt.isPresent()) {
            ResourceLocation res = Supplementaries.res("block/plants/" + id.getPath());
            CUSTOM_MODELS.add(res);
            registerCustomFlower((Item) opt.get(), res);
        }
    }

    @Nullable
    public static ResourceLocation getSpecialFlowerModel(Item i) {
        ResourceLocation res = (ResourceLocation) SPECIAL_FLOWER_BOX_FLOWERS.get(i);
        if (res != null) {
            return res;
        } else if (i instanceof IFlowerModelProvider flowerModelProvider) {
            return flowerModelProvider.getModel();
        } else {
            if (i instanceof BlockItem blockItem && blockItem.getBlock() instanceof IFlowerModelProvider flowerModelProvider) {
                return flowerModelProvider.getModel();
            }
            return null;
        }
    }

    public static boolean hasSpecialFlowerModel(Item i) {
        return getSpecialFlowerModel(i) != null;
    }

    static {
        List<String> toAdd = new ArrayList();
        toAdd.add(Utils.getID(Items.CACTUS).toString());
        toAdd.add(Utils.getID(Items.FLOWERING_AZALEA).toString());
        toAdd.add(Utils.getID(Items.AZALEA).toString());
        toAdd.add(Utils.getID((Item) ModRegistry.FLAX_SEEDS_ITEM.get()).toString());
        if (CompatHandler.SNOWYSPIRIT) {
            toAdd.add("snowyspirit:ginger_flower");
        }
        if (CompatHandler.HAUNTEDHARVEST) {
            toAdd.add("hauntedharvest:kernels");
        }
        if (CompatHandler.QUARK) {
            Item[] items = new Item[] { Items.SUGAR_CANE, Items.BEETROOT_SEEDS, Items.CARROT, Items.CHORUS_FLOWER, Items.POTATO, Items.GRASS, Items.COCOA_BEANS, Items.WHEAT_SEEDS, Items.VINE, Items.LARGE_FERN, Items.SWEET_BERRIES, Items.WEEPING_VINES, Items.NETHER_SPROUTS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.TALL_GRASS, Items.SEA_PICKLE, Items.NETHER_WART };
            toAdd.add("quark:chorus_weeds");
            toAdd.add("quark:root");
            toAdd.add("quark:chorus_twist");
            Arrays.stream(items).forEach(i -> toAdd.add(Utils.getID(i).toString()));
        }
        if (CompatHandler.POKECUBE_LEGENDS) {
            toAdd.add("pokecube_legends:crystallized_cactus");
        }
        if (CompatHandler.POKECUBE) {
            String[] berries = new String[] { "pokecube:berry_aspear", "pokecube:berry_cheri", "pokecube:berry_chesto", "pokecube:berry_cornn", "pokecube:berry_enigma", "pokecube:berry_grepa", "pokecube:berry_hondew", "pokecube:berry_jaboca", "pokecube:berry_kelpsy", "pokecube:berry_leppa", "pokecube:berry_lum", "pokecube:berry_nanab", "pokecube:berry_null", "pokecube:berry_oran", "pokecube:berry_pecha", "pokecube:berry_persim", "pokecube:berry_pinap", "pokecube:berry_pomeg", "pokecube:berry_qualot", "pokecube:berry_rawst", "pokecube:berry_rowap", "pokecube:berry_sitrus", "pokecube:berry_tamato" };
            toAdd.addAll(Arrays.asList(berries));
        }
        if (CompatHandler.MOREMINECARTS) {
            toAdd.add("moreminecarts:chunkrodite_block");
            toAdd.add("moreminecarts:glass_cactus");
        }
        if (CompatHandler.HABITAT) {
            toAdd.add("habitat:rafflesia");
            toAdd.add("habitat:orange_ball_cactus");
            toAdd.add("habitat:red_ball_cactus");
            toAdd.add("habitat:pink_ball_cactus");
            toAdd.add("habitat:yellow_ball_cactus");
            toAdd.add("habitat:kabloom_pulp");
        }
        if (CompatHandler.ENDERGETIC) {
            toAdd.add("endergetic:tall_poise_bush");
        }
        if (CompatHandler.SIMPLEFARMING) {
            toAdd.add("simplefarming:cantaloupe_block");
            toAdd.add("simplefarming:honeydew_block");
            toAdd.add("simplefarming:squash_block");
        }
        if (CompatHandler.ATMOSPHERIC) {
            toAdd.add("atmospheric:barrel_cactus");
        }
        if (CompatHandler.OVERWEIGHT_FARMING) {
            toAdd.add("overweight_farming:overweight_cabbage");
            toAdd.add("overweight_farming:overweight_potato");
            toAdd.add("overweight_farming:overweight_poisonous_potato");
            toAdd.add("overweight_farming:overweight_carrot");
            toAdd.add("overweight_farming:overweight_onion");
            toAdd.add("overweight_farming:overweight_cabbage");
        }
        toAdd.forEach(FlowerPotHandler::registerCompatFlower);
    }
}