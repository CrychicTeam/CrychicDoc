package mezz.jei.library.plugins.vanilla.brewing;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.core.collect.SetMultiMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;

public class BrewingRecipeUtil {

    public static final ItemStack POTION = new ItemStack(Items.POTION);

    public static final ItemStack WATER_BOTTLE = PotionUtils.setPotion(POTION.copy(), Potions.WATER);

    private final Map<String, Integer> brewingStepCache = new HashMap();

    private final SetMultiMap<String, String> potionMap = new SetMultiMap<>();

    private final IIngredientHelper<ItemStack> itemStackHelper;

    public BrewingRecipeUtil(IIngredientHelper<ItemStack> itemStackHelper) {
        this.itemStackHelper = itemStackHelper;
        this.clearCache();
    }

    public void addRecipe(List<ItemStack> inputPotions, ItemStack outputPotion) {
        String potionOutputUid = this.itemStackHelper.getUniqueId(outputPotion, UidContext.Recipe);
        for (ItemStack inputPotion : inputPotions) {
            String potionInputUid = this.itemStackHelper.getUniqueId(inputPotion, UidContext.Recipe);
            this.potionMap.put(potionOutputUid, potionInputUid);
        }
        this.clearCache();
    }

    public int getBrewingSteps(ItemStack outputPotion) {
        String potionInputUid = this.itemStackHelper.getUniqueId(outputPotion, UidContext.Recipe);
        return this.getBrewingSteps(potionInputUid, new HashSet());
    }

    private void clearCache() {
        if (this.brewingStepCache.size() != 1) {
            this.brewingStepCache.clear();
            String waterBottleUid = this.itemStackHelper.getUniqueId(WATER_BOTTLE, UidContext.Recipe);
            this.brewingStepCache.put(waterBottleUid, 0);
        }
    }

    private int getBrewingSteps(String potionOutputUid, Set<String> previousSteps) {
        Integer cachedBrewingSteps = (Integer) this.brewingStepCache.get(potionOutputUid);
        if (cachedBrewingSteps != null) {
            return cachedBrewingSteps;
        } else if (!previousSteps.add(potionOutputUid)) {
            return Integer.MAX_VALUE;
        } else {
            Collection<String> prevPotions = this.potionMap.get(potionOutputUid);
            int minPrevSteps = prevPotions.stream().mapToInt(prevPotion -> this.getBrewingSteps(prevPotion, previousSteps)).min().orElse(Integer.MAX_VALUE);
            int brewingSteps = minPrevSteps == Integer.MAX_VALUE ? Integer.MAX_VALUE : minPrevSteps + 1;
            this.brewingStepCache.put(potionOutputUid, brewingSteps);
            return brewingSteps;
        }
    }
}