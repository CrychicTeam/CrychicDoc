package com.almostreliable.summoningrituals;

import com.almostreliable.summoningrituals.altar.AltarBlock;
import com.almostreliable.summoningrituals.altar.AltarBlockEntity;
import com.almostreliable.summoningrituals.recipe.AltarRecipe;
import com.almostreliable.summoningrituals.recipe.AltarRecipeSerializer;
import com.almostreliable.summoningrituals.util.TextUtils;
import com.almostreliable.summoningrituals.util.Utils;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

public final class Registration {

    private static final DeferredRegister<Block> BLOCKS = createRegistry(ForgeRegistries.BLOCKS);

    private static final DeferredRegister<Item> ITEMS = createRegistry(ForgeRegistries.ITEMS);

    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = createRegistry(ForgeRegistries.BLOCK_ENTITY_TYPES);

    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, "summoningrituals");

    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = createRegistry(ForgeRegistries.RECIPE_SERIALIZERS);

    public static final RegistryObject<AltarBlock> ALTAR_BLOCK = BLOCKS.register("altar", () -> new AltarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.5F).sound(SoundType.STONE)));

    public static final RegistryObject<AltarBlock> INDESTRUCTIBLE_ALTAR_BLOCK = BLOCKS.register("indestructible_altar", () -> new AltarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(-1.0F, 3600000.0F).sound(SoundType.STONE)));

    public static final RegistryObject<Item> ALTAR_ITEM = ITEMS.register("altar", () -> new BlockItem(ALTAR_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Item> INDESTRUCTIBLE_ALTAR_ITEM = ITEMS.register("indestructible_altar", () -> new BlockItem(INDESTRUCTIBLE_ALTAR_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<BlockEntityType<AltarBlockEntity>> ALTAR_ENTITY = BLOCK_ENTITIES.register("altar", () -> BlockEntityType.Builder.of(AltarBlockEntity::new, ALTAR_BLOCK.get(), INDESTRUCTIBLE_ALTAR_BLOCK.get()).build(null));

    public static final Registration.RecipeEntry<AltarRecipe> ALTAR_RECIPE = Registration.RecipeEntry.register("altar", AltarRecipeSerializer::new);

    private Registration() {
    }

    static void init(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        RECIPE_TYPES.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
    }

    private static <T> DeferredRegister<T> createRegistry(IForgeRegistry<T> registry) {
        return DeferredRegister.create(registry, "summoningrituals");
    }

    public static record RecipeEntry<T extends Recipe<?>>(RegistryObject<RecipeType<T>> type, RegistryObject<? extends RecipeSerializer<T>> serializer) {

        private static <T extends Recipe<?>> Registration.RecipeEntry<T> register(String id, Supplier<? extends RecipeSerializer<T>> serializer) {
            RegistryObject<RecipeType<T>> type = Registration.RECIPE_TYPES.register(id, () -> new RecipeType<T>() {

                public String toString() {
                    return id;
                }
            });
            return new Registration.RecipeEntry<>(type, Registration.RECIPE_SERIALIZERS.register(id, serializer));
        }
    }

    static final class Tab {

        private static final ResourceKey<CreativeModeTab> TAB_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Utils.getRL("tab"));

        private static final CreativeModeTab TAB = CreativeModeTab.builder().title(TextUtils.translate("label", "itemGroup")).icon(() -> new ItemStack(Registration.ALTAR_BLOCK.get())).noScrollBar().build();

        private Tab() {
        }

        static void initContents(BuildCreativeModeTabContentsEvent event) {
            if (event.getTabKey() == TAB_KEY) {
                event.accept(Registration.ALTAR_BLOCK);
                event.accept(Registration.INDESTRUCTIBLE_ALTAR_BLOCK);
            }
        }

        static void registerTab(RegisterEvent event) {
            event.register(Registries.CREATIVE_MODE_TAB, TAB_KEY.location(), () -> TAB);
        }
    }
}