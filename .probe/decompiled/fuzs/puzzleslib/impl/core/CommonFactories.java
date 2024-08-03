package fuzs.puzzleslib.impl.core;

import fuzs.puzzleslib.api.core.v1.ContentRegistrationFlags;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.ServiceProviderHelper;
import fuzs.puzzleslib.api.init.v2.GameRulesFactory;
import fuzs.puzzleslib.api.init.v2.PotionBrewingRegistry;
import fuzs.puzzleslib.api.item.v2.ToolTypeHelper;
import fuzs.puzzleslib.api.item.v2.crafting.CombinedIngredients;
import java.util.Set;

public interface CommonFactories {

    CommonFactories INSTANCE = ServiceProviderHelper.load(CommonFactories.class);

    void constructMod(String var1, ModConstructor var2, Set<ContentRegistrationFlags> var3, Set<ContentRegistrationFlags> var4);

    ModContext getModContext(String var1);

    ProxyImpl getClientProxy();

    ProxyImpl getServerProxy();

    PotionBrewingRegistry getPotionBrewingRegistry();

    GameRulesFactory getGameRulesFactory();

    void registerLoadingHandlers();

    void registerEventHandlers();

    ToolTypeHelper getToolTypeHelper();

    CombinedIngredients getCombinedIngredients();
}