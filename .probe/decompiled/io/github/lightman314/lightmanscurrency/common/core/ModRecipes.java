package io.github.lightman314.lightmanscurrency.common.core;

import io.github.lightman314.lightmanscurrency.common.crafting.CoinMintRecipe;
import io.github.lightman314.lightmanscurrency.common.crafting.MasterTicketRecipe;
import io.github.lightman314.lightmanscurrency.common.crafting.TicketRecipe;
import io.github.lightman314.lightmanscurrency.common.crafting.WalletUpgradeRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {

    public static final RegistryObject<RecipeSerializer<WalletUpgradeRecipe>> WALLET_UPGRADE = ModRegistries.RECIPE_SERIALIZERS.register("crafting_wallet_upgrade", WalletUpgradeRecipe.Serializer::new);

    public static final RegistryObject<RecipeSerializer<CoinMintRecipe>> COIN_MINT = ModRegistries.RECIPE_SERIALIZERS.register("coin_mint", CoinMintRecipe.Serializer::new);

    public static final RegistryObject<RecipeSerializer<TicketRecipe>> TICKET = ModRegistries.RECIPE_SERIALIZERS.register("ticket", TicketRecipe.Serializer::new);

    public static final RegistryObject<RecipeSerializer<MasterTicketRecipe>> TICKET_MASTER = ModRegistries.RECIPE_SERIALIZERS.register("ticket_master", MasterTicketRecipe.Serializer::new);

    public static void init() {
    }
}