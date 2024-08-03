package mezz.jei.gui.ingredients;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.common.config.IngredientSortStage;
import mezz.jei.gui.config.IngredientTypeSortingConfig;
import mezz.jei.gui.config.ModNameSortingConfig;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class IngredientSorterComparators {

    private final IngredientFilter ingredientFilter;

    private final IIngredientManager ingredientManager;

    private final ModNameSortingConfig modNameSortingConfig;

    private final IngredientTypeSortingConfig ingredientTypeSortingConfig;

    public IngredientSorterComparators(IngredientFilter ingredientFilter, IIngredientManager ingredientManager, ModNameSortingConfig modNameSortingConfig, IngredientTypeSortingConfig ingredientTypeSortingConfig) {
        this.ingredientFilter = ingredientFilter;
        this.ingredientManager = ingredientManager;
        this.modNameSortingConfig = modNameSortingConfig;
        this.ingredientTypeSortingConfig = ingredientTypeSortingConfig;
    }

    public Comparator<IListElementInfo<?>> getComparator(List<IngredientSortStage> ingredientSorterStages) {
        return (Comparator<IListElementInfo<?>>) ingredientSorterStages.stream().map(this::getComparator).reduce(Comparator::thenComparing).orElseGet(this::getDefault);
    }

    public Comparator<IListElementInfo<?>> getComparator(IngredientSortStage ingredientSortStage) {
        return switch(ingredientSortStage) {
            case ALPHABETICAL ->
                getAlphabeticalComparator();
            case CREATIVE_MENU ->
                getCreativeMenuComparator();
            case INGREDIENT_TYPE ->
                this.getIngredientTypeComparator();
            case MOD_NAME ->
                this.getModNameComparator();
            case TAG ->
                this.getTagComparator();
            case ARMOR ->
                getArmorComparator();
            case MAX_DURABILITY ->
                getMaxDurabilityComparator();
        };
    }

    public Comparator<IListElementInfo<?>> getDefault() {
        return this.getModNameComparator().thenComparing(this.getIngredientTypeComparator()).thenComparing(getCreativeMenuComparator());
    }

    private static Comparator<IListElementInfo<?>> getCreativeMenuComparator() {
        return Comparator.comparingInt(o -> {
            IListElement<?> element = o.getElement();
            return element.getOrderIndex();
        });
    }

    private static Comparator<IListElementInfo<?>> getAlphabeticalComparator() {
        return Comparator.comparing(IListElementInfo::getName);
    }

    private Comparator<IListElementInfo<?>> getModNameComparator() {
        Set<String> modNames = this.ingredientFilter.getModNamesForSorting();
        return this.modNameSortingConfig.getComparatorFromMappedValues(modNames);
    }

    private Comparator<IListElementInfo<?>> getIngredientTypeComparator() {
        Collection<IIngredientType<?>> ingredientTypes = this.ingredientManager.getRegisteredIngredientTypes();
        Set<String> ingredientTypeStrings = (Set<String>) ingredientTypes.stream().map(IngredientTypeSortingConfig::getIngredientTypeString).collect(Collectors.toSet());
        return this.ingredientTypeSortingConfig.getComparatorFromMappedValues(ingredientTypeStrings);
    }

    private static Comparator<IListElementInfo<?>> getMaxDurabilityComparator() {
        Comparator<IListElementInfo<?>> maxDamage = Comparator.comparing(o -> getItemStack(o).getMaxDamage());
        return maxDamage.reversed();
    }

    private Comparator<IListElementInfo<?>> getTagComparator() {
        Comparator<IListElementInfo<?>> isTagged = Comparator.comparing(this::hasTag);
        Comparator<IListElementInfo<?>> tag = Comparator.comparing(this::getTagForSorting);
        return isTagged.reversed().thenComparing(tag);
    }

    private static Comparator<IListElementInfo<?>> getArmorComparator() {
        Comparator<IListElementInfo<?>> isArmorComp = Comparator.comparing(o -> isArmor(getItemStack(o)));
        Comparator<IListElementInfo<?>> armorSlot = Comparator.comparing(o -> getArmorSlotIndex(getItemStack(o)));
        Comparator<IListElementInfo<?>> armorDamage = Comparator.comparing(o -> getArmorDamageReduce(getItemStack(o)));
        Comparator<IListElementInfo<?>> armorToughness = Comparator.comparing(o -> getArmorToughness(getItemStack(o)));
        Comparator<IListElementInfo<?>> maxDamage = Comparator.comparing(o -> getArmorDurability(getItemStack(o)));
        return isArmorComp.reversed().thenComparing(armorSlot.reversed()).thenComparing(armorDamage.reversed()).thenComparing(armorToughness.reversed()).thenComparing(maxDamage.reversed());
    }

    private static boolean isArmor(ItemStack itemStack) {
        Item item = itemStack.getItem();
        return item instanceof ArmorItem;
    }

    private static int getArmorSlotIndex(ItemStack itemStack) {
        return itemStack.getItem() instanceof ArmorItem armorItem ? armorItem.getEquipmentSlot().getFilterFlag() : 0;
    }

    private static int getArmorDamageReduce(ItemStack itemStack) {
        return itemStack.getItem() instanceof ArmorItem armorItem ? armorItem.getDefense() : 0;
    }

    private static float getArmorToughness(ItemStack itemStack) {
        return itemStack.getItem() instanceof ArmorItem armorItem ? armorItem.getToughness() : 0.0F;
    }

    private static int getArmorDurability(ItemStack itemStack) {
        return isArmor(itemStack) ? itemStack.getMaxDamage() : 0;
    }

    private String getTagForSorting(IListElementInfo<?> elementInfo) {
        return (String) elementInfo.getTagIds(this.ingredientManager).max(Comparator.comparing(IngredientSorterComparators::tagCount)).map(ResourceLocation::m_135815_).orElse("");
    }

    private static int tagCount(ResourceLocation tagId) {
        if (tagId.toString().equals("itemfilters:check_nbt")) {
            return 0;
        } else {
            TagKey<Item> tagKey = TagKey.create(Registries.ITEM, tagId);
            return (Integer) BuiltInRegistries.ITEM.m_203431_(tagKey).map(HolderSet.ListBacked::m_203632_).orElse(0);
        }
    }

    private boolean hasTag(IListElementInfo<?> elementInfo) {
        return !this.getTagForSorting(elementInfo).isEmpty();
    }

    public static <V> ItemStack getItemStack(IListElementInfo<V> ingredientInfo) {
        ITypedIngredient<V> ingredient = ingredientInfo.getTypedIngredient();
        Object var3 = ingredient.getIngredient();
        return var3 instanceof ItemStack ? (ItemStack) var3 : ItemStack.EMPTY;
    }
}