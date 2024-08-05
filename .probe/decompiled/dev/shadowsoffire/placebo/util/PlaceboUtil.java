package dev.shadowsoffire.placebo.util;

import com.google.common.collect.ImmutableList;
import dev.shadowsoffire.placebo.Placebo;
import dev.shadowsoffire.placebo.recipe.RecipeHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class PlaceboUtil {

    static boolean late = false;

    static Map<ResourceLocation, RecipeType<?>> unregisteredTypes = new HashMap();

    @SafeVarargs
    public static <T> List<T> asList(T... objs) {
        ArrayList<T> list = new ArrayList();
        for (T t : objs) {
            list.add(t);
        }
        return list;
    }

    public static CompoundTag getStackNBT(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null) {
            stack.setTag(tag = new CompoundTag());
        }
        return tag;
    }

    public static ItemStack[] toStackArray(Object... args) {
        ItemStack[] out = new ItemStack[args.length];
        for (int i = 0; i < args.length; i++) {
            out[i] = RecipeHelper.makeStack(args[i]);
        }
        return out;
    }

    @Deprecated
    public static <B extends Block & IReplacementBlock> void registerOverride(Block old, B block, final String modid) {
        ResourceLocation key = ForgeRegistries.BLOCKS.getKey(old);
        ForgeRegistries.BLOCKS.register(key, block);
        ForgeRegistries.ITEMS.register(key, new BlockItem(block, new Item.Properties()) {

            public String getCreatorModId(ItemStack itemStack) {
                return modid;
            }
        });
        overrideStates(old, block);
    }

    @Deprecated
    public static <B extends Block & IReplacementBlock> void overrideStates(Block old, B block) {
        block.setStateContainer(old.getStateDefinition());
        block._setDefaultState(old.defaultBlockState());
        block.getStateDefinition().getPossibleStates().forEach(b -> b.f_61112_ = block);
        block.getStateDefinition().owner = block;
    }

    public static <T> List<T> toMutable(List<T> list) {
        if (list instanceof ImmutableList) {
            list = new ArrayList(list);
        }
        return list;
    }

    public static boolean tryHarvestBlock(ServerPlayer player, BlockPos pos) {
        return player.gameMode.destroyBlock(pos);
    }

    public static void addLore(ItemStack stack, Component lore) {
        CompoundTag display = stack.getOrCreateTagElement("display");
        ListTag tag = display.getList("Lore", 8);
        tag.add(StringTag.valueOf(Component.Serializer.toJson(lore)));
        display.put("Lore", tag);
    }

    @Deprecated
    public static <T extends Recipe<?>> RecipeType<T> makeRecipeType(final String pIdentifier) {
        if (late) {
            throw new RuntimeException("Attempted to register a recipe type after the registration period closed.");
        } else {
            RecipeType<T> type = new RecipeType<T>() {

                public String toString() {
                    return pIdentifier;
                }
            };
            unregisteredTypes.put(new ResourceLocation(pIdentifier), type);
            return type;
        }
    }

    @Deprecated
    public static void registerTypes() {
        unregisteredTypes.forEach((key, type) -> ForgeRegistries.RECIPE_TYPES.register(key, type));
        Placebo.LOGGER.debug("Registered {} recipe types.", unregisteredTypes.size());
        unregisteredTypes.clear();
        late = true;
    }

    public static <T extends TextColor> void registerCustomColor(T color) {
        TextColor.NAMED_COLORS.put(color.serialize(), color);
    }
}