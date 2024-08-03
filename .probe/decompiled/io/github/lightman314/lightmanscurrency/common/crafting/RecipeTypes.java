package io.github.lightman314.lightmanscurrency.common.crafting;

import io.github.lightman314.lightmanscurrency.common.core.ModRegistries;
import javax.annotation.Nonnull;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.RegistryObject;

public class RecipeTypes {

    public static final RegistryObject<RecipeType<CoinMintRecipe>> COIN_MINT = register("coin_mint");

    public static final RegistryObject<RecipeType<TicketStationRecipe>> TICKET = register("ticket");

    public static void init() {
    }

    private static <T extends Recipe<?>> RegistryObject<RecipeType<T>> register(@Nonnull String id) {
        return ModRegistries.RECIPE_TYPES.register(id, () -> RecipeType.simple(new ResourceLocation("lightmanscurrency", id)));
    }
}