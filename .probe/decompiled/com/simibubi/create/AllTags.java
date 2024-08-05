package com.simibubi.create;

import com.simibubi.create.foundation.utility.Lang;
import java.util.Collections;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class AllTags {

    public static <T> TagKey<T> optionalTag(IForgeRegistry<T> registry, ResourceLocation id) {
        return registry.tags().createOptionalTagKey(id, Collections.emptySet());
    }

    public static <T> TagKey<T> forgeTag(IForgeRegistry<T> registry, String path) {
        return optionalTag(registry, new ResourceLocation("forge", path));
    }

    public static TagKey<Block> forgeBlockTag(String path) {
        return forgeTag(ForgeRegistries.BLOCKS, path);
    }

    public static TagKey<Item> forgeItemTag(String path) {
        return forgeTag(ForgeRegistries.ITEMS, path);
    }

    public static TagKey<Fluid> forgeFluidTag(String path) {
        return forgeTag(ForgeRegistries.FLUIDS, path);
    }

    public static void init() {
        AllTags.AllBlockTags.init();
        AllTags.AllItemTags.init();
        AllTags.AllFluidTags.init();
        AllTags.AllEntityTags.init();
        AllTags.AllRecipeSerializerTags.init();
    }

    public static enum AllBlockTags {

        BRITTLE,
        CASING,
        CONTRAPTION_INVENTORY_DENY,
        COPYCAT_ALLOW,
        COPYCAT_DENY,
        FAN_PROCESSING_CATALYSTS_BLASTING(AllTags.NameSpace.MOD, "fan_processing_catalysts/blasting"),
        FAN_PROCESSING_CATALYSTS_HAUNTING(AllTags.NameSpace.MOD, "fan_processing_catalysts/haunting"),
        FAN_PROCESSING_CATALYSTS_SMOKING(AllTags.NameSpace.MOD, "fan_processing_catalysts/smoking"),
        FAN_PROCESSING_CATALYSTS_SPLASHING(AllTags.NameSpace.MOD, "fan_processing_catalysts/splashing"),
        FAN_TRANSPARENT,
        GIRDABLE_TRACKS,
        MOVABLE_EMPTY_COLLIDER,
        NON_MOVABLE,
        ORE_OVERRIDE_STONE,
        PASSIVE_BOILER_HEATERS,
        SAFE_NBT,
        SEATS,
        TOOLBOXES,
        TRACKS,
        TREE_ATTACHMENTS,
        VALVE_HANDLES,
        WINDMILL_SAILS,
        WRENCH_PICKUP,
        RELOCATION_NOT_SUPPORTED(AllTags.NameSpace.FORGE),
        WG_STONE(AllTags.NameSpace.FORGE),
        SLIMY_LOGS(AllTags.NameSpace.TIC),
        NON_DOUBLE_DOOR(AllTags.NameSpace.QUARK);

        public final TagKey<Block> tag;

        public final boolean alwaysDatagen;

        private AllBlockTags() {
            this(AllTags.NameSpace.MOD);
        }

        private AllBlockTags(AllTags.NameSpace namespace) {
            this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        private AllBlockTags(AllTags.NameSpace namespace, String path) {
            this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        private AllBlockTags(AllTags.NameSpace namespace, boolean optional, boolean alwaysDatagen) {
            this(namespace, null, optional, alwaysDatagen);
        }

        private AllBlockTags(AllTags.NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
            ResourceLocation id = new ResourceLocation(namespace.id, path == null ? Lang.asId(this.name()) : path);
            if (optional) {
                this.tag = AllTags.optionalTag(ForgeRegistries.BLOCKS, id);
            } else {
                this.tag = BlockTags.create(id);
            }
            this.alwaysDatagen = alwaysDatagen;
        }

        public boolean matches(Block block) {
            return block.builtInRegistryHolder().is(this.tag);
        }

        public boolean matches(ItemStack stack) {
            return stack != null && stack.getItem() instanceof BlockItem blockItem && this.matches(blockItem.getBlock());
        }

        public boolean matches(BlockState state) {
            return state.m_204336_(this.tag);
        }

        private static void init() {
        }
    }

    public static enum AllEntityTags {

        BLAZE_BURNER_CAPTURABLE, IGNORE_SEAT;

        public final TagKey<EntityType<?>> tag;

        public final boolean alwaysDatagen;

        private AllEntityTags() {
            this(AllTags.NameSpace.MOD);
        }

        private AllEntityTags(AllTags.NameSpace namespace) {
            this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        private AllEntityTags(AllTags.NameSpace namespace, String path) {
            this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        private AllEntityTags(AllTags.NameSpace namespace, boolean optional, boolean alwaysDatagen) {
            this(namespace, null, optional, alwaysDatagen);
        }

        private AllEntityTags(AllTags.NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
            ResourceLocation id = new ResourceLocation(namespace.id, path == null ? Lang.asId(this.name()) : path);
            if (optional) {
                this.tag = AllTags.optionalTag(ForgeRegistries.ENTITY_TYPES, id);
            } else {
                this.tag = TagKey.create(Registries.ENTITY_TYPE, id);
            }
            this.alwaysDatagen = alwaysDatagen;
        }

        public boolean matches(EntityType<?> type) {
            return type.is(this.tag);
        }

        public boolean matches(Entity entity) {
            return this.matches(entity.getType());
        }

        private static void init() {
        }
    }

    public static enum AllFluidTags {

        BOTTOMLESS_ALLOW(AllTags.NameSpace.MOD, "bottomless/allow"),
        BOTTOMLESS_DENY(AllTags.NameSpace.MOD, "bottomless/deny"),
        FAN_PROCESSING_CATALYSTS_BLASTING(AllTags.NameSpace.MOD, "fan_processing_catalysts/blasting"),
        FAN_PROCESSING_CATALYSTS_HAUNTING(AllTags.NameSpace.MOD, "fan_processing_catalysts/haunting"),
        FAN_PROCESSING_CATALYSTS_SMOKING(AllTags.NameSpace.MOD, "fan_processing_catalysts/smoking"),
        FAN_PROCESSING_CATALYSTS_SPLASHING(AllTags.NameSpace.MOD, "fan_processing_catalysts/splashing"),
        HONEY(AllTags.NameSpace.FORGE);

        public final TagKey<Fluid> tag;

        public final boolean alwaysDatagen;

        private AllFluidTags() {
            this(AllTags.NameSpace.MOD);
        }

        private AllFluidTags(AllTags.NameSpace namespace) {
            this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        private AllFluidTags(AllTags.NameSpace namespace, String path) {
            this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        private AllFluidTags(AllTags.NameSpace namespace, boolean optional, boolean alwaysDatagen) {
            this(namespace, null, optional, alwaysDatagen);
        }

        private AllFluidTags(AllTags.NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
            ResourceLocation id = new ResourceLocation(namespace.id, path == null ? Lang.asId(this.name()) : path);
            if (optional) {
                this.tag = AllTags.optionalTag(ForgeRegistries.FLUIDS, id);
            } else {
                this.tag = FluidTags.create(id);
            }
            this.alwaysDatagen = alwaysDatagen;
        }

        public boolean matches(Fluid fluid) {
            return fluid.is(this.tag);
        }

        public boolean matches(FluidState state) {
            return state.is(this.tag);
        }

        private static void init() {
        }
    }

    public static enum AllItemTags {

        BLAZE_BURNER_FUEL_REGULAR(AllTags.NameSpace.MOD, "blaze_burner_fuel/regular"),
        BLAZE_BURNER_FUEL_SPECIAL(AllTags.NameSpace.MOD, "blaze_burner_fuel/special"),
        CASING,
        CONTRAPTION_CONTROLLED,
        CREATE_INGOTS,
        CRUSHED_RAW_MATERIALS,
        DEPLOYABLE_DRINK,
        MODDED_STRIPPED_LOGS,
        MODDED_STRIPPED_WOOD,
        PRESSURIZED_AIR_SOURCES,
        SANDPAPER,
        SEATS,
        SLEEPERS,
        TOOLBOXES,
        UPRIGHT_ON_BELT,
        VALVE_HANDLES,
        VANILLA_STRIPPED_LOGS,
        VANILLA_STRIPPED_WOOD,
        STRIPPED_LOGS(AllTags.NameSpace.FORGE),
        STRIPPED_WOOD(AllTags.NameSpace.FORGE),
        PLATES(AllTags.NameSpace.FORGE),
        WRENCH(AllTags.NameSpace.FORGE, "tools/wrench");

        public final TagKey<Item> tag;

        public final boolean alwaysDatagen;

        private AllItemTags() {
            this(AllTags.NameSpace.MOD);
        }

        private AllItemTags(AllTags.NameSpace namespace) {
            this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        private AllItemTags(AllTags.NameSpace namespace, String path) {
            this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        private AllItemTags(AllTags.NameSpace namespace, boolean optional, boolean alwaysDatagen) {
            this(namespace, null, optional, alwaysDatagen);
        }

        private AllItemTags(AllTags.NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
            ResourceLocation id = new ResourceLocation(namespace.id, path == null ? Lang.asId(this.name()) : path);
            if (optional) {
                this.tag = AllTags.optionalTag(ForgeRegistries.ITEMS, id);
            } else {
                this.tag = ItemTags.create(id);
            }
            this.alwaysDatagen = alwaysDatagen;
        }

        public boolean matches(Item item) {
            return item.builtInRegistryHolder().is(this.tag);
        }

        public boolean matches(ItemStack stack) {
            return stack.is(this.tag);
        }

        private static void init() {
        }
    }

    public static enum AllRecipeSerializerTags {

        AUTOMATION_IGNORE;

        public final TagKey<RecipeSerializer<?>> tag;

        public final boolean alwaysDatagen;

        private AllRecipeSerializerTags() {
            this(AllTags.NameSpace.MOD);
        }

        private AllRecipeSerializerTags(AllTags.NameSpace namespace) {
            this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        private AllRecipeSerializerTags(AllTags.NameSpace namespace, String path) {
            this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        private AllRecipeSerializerTags(AllTags.NameSpace namespace, boolean optional, boolean alwaysDatagen) {
            this(namespace, null, optional, alwaysDatagen);
        }

        private AllRecipeSerializerTags(AllTags.NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
            ResourceLocation id = new ResourceLocation(namespace.id, path == null ? Lang.asId(this.name()) : path);
            if (optional) {
                this.tag = AllTags.optionalTag(ForgeRegistries.RECIPE_SERIALIZERS, id);
            } else {
                this.tag = TagKey.create(Registries.RECIPE_SERIALIZER, id);
            }
            this.alwaysDatagen = alwaysDatagen;
        }

        public boolean matches(RecipeSerializer<?> recipeSerializer) {
            return ((Holder) ForgeRegistries.RECIPE_SERIALIZERS.getHolder(recipeSerializer).orElseThrow()).is(this.tag);
        }

        private static void init() {
        }
    }

    public static enum NameSpace {

        MOD("create", false, true), FORGE("forge"), TIC("tconstruct"), QUARK("quark");

        public final String id;

        public final boolean optionalDefault;

        public final boolean alwaysDatagenDefault;

        private NameSpace(String id) {
            this(id, true, false);
        }

        private NameSpace(String id, boolean optionalDefault, boolean alwaysDatagenDefault) {
            this.id = id;
            this.optionalDefault = optionalDefault;
            this.alwaysDatagenDefault = alwaysDatagenDefault;
        }
    }
}