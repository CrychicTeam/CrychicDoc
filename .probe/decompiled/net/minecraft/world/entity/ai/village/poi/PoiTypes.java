package net.minecraft.world.entity.ai.village.poi;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

public class PoiTypes {

    public static final ResourceKey<PoiType> ARMORER = createKey("armorer");

    public static final ResourceKey<PoiType> BUTCHER = createKey("butcher");

    public static final ResourceKey<PoiType> CARTOGRAPHER = createKey("cartographer");

    public static final ResourceKey<PoiType> CLERIC = createKey("cleric");

    public static final ResourceKey<PoiType> FARMER = createKey("farmer");

    public static final ResourceKey<PoiType> FISHERMAN = createKey("fisherman");

    public static final ResourceKey<PoiType> FLETCHER = createKey("fletcher");

    public static final ResourceKey<PoiType> LEATHERWORKER = createKey("leatherworker");

    public static final ResourceKey<PoiType> LIBRARIAN = createKey("librarian");

    public static final ResourceKey<PoiType> MASON = createKey("mason");

    public static final ResourceKey<PoiType> SHEPHERD = createKey("shepherd");

    public static final ResourceKey<PoiType> TOOLSMITH = createKey("toolsmith");

    public static final ResourceKey<PoiType> WEAPONSMITH = createKey("weaponsmith");

    public static final ResourceKey<PoiType> HOME = createKey("home");

    public static final ResourceKey<PoiType> MEETING = createKey("meeting");

    public static final ResourceKey<PoiType> BEEHIVE = createKey("beehive");

    public static final ResourceKey<PoiType> BEE_NEST = createKey("bee_nest");

    public static final ResourceKey<PoiType> NETHER_PORTAL = createKey("nether_portal");

    public static final ResourceKey<PoiType> LODESTONE = createKey("lodestone");

    public static final ResourceKey<PoiType> LIGHTNING_ROD = createKey("lightning_rod");

    private static final Set<BlockState> BEDS = (Set<BlockState>) ImmutableList.of(Blocks.RED_BED, Blocks.BLACK_BED, Blocks.BLUE_BED, Blocks.BROWN_BED, Blocks.CYAN_BED, Blocks.GRAY_BED, Blocks.GREEN_BED, Blocks.LIGHT_BLUE_BED, Blocks.LIGHT_GRAY_BED, Blocks.LIME_BED, Blocks.MAGENTA_BED, Blocks.ORANGE_BED, new Block[] { Blocks.PINK_BED, Blocks.PURPLE_BED, Blocks.WHITE_BED, Blocks.YELLOW_BED }).stream().flatMap(p_218097_ -> p_218097_.getStateDefinition().getPossibleStates().stream()).filter(p_218095_ -> p_218095_.m_61143_(BedBlock.PART) == BedPart.HEAD).collect(ImmutableSet.toImmutableSet());

    private static final Set<BlockState> CAULDRONS = (Set<BlockState>) ImmutableList.of(Blocks.CAULDRON, Blocks.LAVA_CAULDRON, Blocks.WATER_CAULDRON, Blocks.POWDER_SNOW_CAULDRON).stream().flatMap(p_218093_ -> p_218093_.getStateDefinition().getPossibleStates().stream()).collect(ImmutableSet.toImmutableSet());

    private static final Map<BlockState, Holder<PoiType>> TYPE_BY_STATE = Maps.newHashMap();

    private static Set<BlockState> getBlockStates(Block block0) {
        return ImmutableSet.copyOf(block0.getStateDefinition().getPossibleStates());
    }

    private static ResourceKey<PoiType> createKey(String string0) {
        return ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, new ResourceLocation(string0));
    }

    private static PoiType register(Registry<PoiType> registryPoiType0, ResourceKey<PoiType> resourceKeyPoiType1, Set<BlockState> setBlockState2, int int3, int int4) {
        PoiType $$5 = new PoiType(setBlockState2, int3, int4);
        Registry.register(registryPoiType0, resourceKeyPoiType1, $$5);
        registerBlockStates(registryPoiType0.getHolderOrThrow(resourceKeyPoiType1), setBlockState2);
        return $$5;
    }

    private static void registerBlockStates(Holder<PoiType> holderPoiType0, Set<BlockState> setBlockState1) {
        setBlockState1.forEach(p_218081_ -> {
            Holder<PoiType> $$2 = (Holder<PoiType>) TYPE_BY_STATE.put(p_218081_, holderPoiType0);
            if ($$2 != null) {
                throw (IllegalStateException) Util.pauseInIde(new IllegalStateException(String.format(Locale.ROOT, "%s is defined in more than one PoI type", p_218081_)));
            }
        });
    }

    public static Optional<Holder<PoiType>> forState(BlockState blockState0) {
        return Optional.ofNullable((Holder) TYPE_BY_STATE.get(blockState0));
    }

    public static boolean hasPoi(BlockState blockState0) {
        return TYPE_BY_STATE.containsKey(blockState0);
    }

    public static PoiType bootstrap(Registry<PoiType> registryPoiType0) {
        register(registryPoiType0, ARMORER, getBlockStates(Blocks.BLAST_FURNACE), 1, 1);
        register(registryPoiType0, BUTCHER, getBlockStates(Blocks.SMOKER), 1, 1);
        register(registryPoiType0, CARTOGRAPHER, getBlockStates(Blocks.CARTOGRAPHY_TABLE), 1, 1);
        register(registryPoiType0, CLERIC, getBlockStates(Blocks.BREWING_STAND), 1, 1);
        register(registryPoiType0, FARMER, getBlockStates(Blocks.COMPOSTER), 1, 1);
        register(registryPoiType0, FISHERMAN, getBlockStates(Blocks.BARREL), 1, 1);
        register(registryPoiType0, FLETCHER, getBlockStates(Blocks.FLETCHING_TABLE), 1, 1);
        register(registryPoiType0, LEATHERWORKER, CAULDRONS, 1, 1);
        register(registryPoiType0, LIBRARIAN, getBlockStates(Blocks.LECTERN), 1, 1);
        register(registryPoiType0, MASON, getBlockStates(Blocks.STONECUTTER), 1, 1);
        register(registryPoiType0, SHEPHERD, getBlockStates(Blocks.LOOM), 1, 1);
        register(registryPoiType0, TOOLSMITH, getBlockStates(Blocks.SMITHING_TABLE), 1, 1);
        register(registryPoiType0, WEAPONSMITH, getBlockStates(Blocks.GRINDSTONE), 1, 1);
        register(registryPoiType0, HOME, BEDS, 1, 1);
        register(registryPoiType0, MEETING, getBlockStates(Blocks.BELL), 32, 6);
        register(registryPoiType0, BEEHIVE, getBlockStates(Blocks.BEEHIVE), 0, 1);
        register(registryPoiType0, BEE_NEST, getBlockStates(Blocks.BEE_NEST), 0, 1);
        register(registryPoiType0, NETHER_PORTAL, getBlockStates(Blocks.NETHER_PORTAL), 0, 1);
        register(registryPoiType0, LODESTONE, getBlockStates(Blocks.LODESTONE), 0, 1);
        return register(registryPoiType0, LIGHTNING_ROD, getBlockStates(Blocks.LIGHTNING_ROD), 0, 1);
    }
}