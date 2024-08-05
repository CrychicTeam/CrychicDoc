package se.mickelus.tetra.aspect;

import com.google.common.collect.HashBiMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;
import org.apache.commons.lang3.tuple.Pair;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.module.ItemModule;
import se.mickelus.tetra.module.ItemModuleMajor;

public class TetraEnchantmentHelper {

    private static final Map<ItemAspect, TetraEnchantmentHelper.EnchantmentRules> aspectMap = HashBiMap.create();

    public static void init() {
        aspectMap.put(ItemAspect.armor, new TetraEnchantmentHelper.EnchantmentRules("additions/armor", "exclusions/armor", EnchantmentCategory.ARMOR));
        aspectMap.put(ItemAspect.armorFeet, new TetraEnchantmentHelper.EnchantmentRules("additions/armor_feet", "exclusions/armor_feet", EnchantmentCategory.ARMOR_FEET));
        aspectMap.put(ItemAspect.armorLegs, new TetraEnchantmentHelper.EnchantmentRules("additions/armor_legs", "exclusions/armor_legs", EnchantmentCategory.ARMOR_LEGS));
        aspectMap.put(ItemAspect.armorChest, new TetraEnchantmentHelper.EnchantmentRules("additions/armor_chest", "exclusions/armor_chest", EnchantmentCategory.ARMOR_CHEST));
        aspectMap.put(ItemAspect.armorHead, new TetraEnchantmentHelper.EnchantmentRules("additions/armor_head", "exclusions/armor_head", EnchantmentCategory.ARMOR_HEAD));
        aspectMap.put(ItemAspect.edgedWeapon, new TetraEnchantmentHelper.EnchantmentRules("additions/edged_weapon", "exclusions/edged_weapon", EnchantmentCategory.WEAPON, fromName("SWORD_OR_AXE")));
        aspectMap.put(ItemAspect.bluntWeapon, new TetraEnchantmentHelper.EnchantmentRules("additions/blunt_weapon", "exclusions/blunt_weapon", EnchantmentCategory.WEAPON));
        aspectMap.put(ItemAspect.pointyWeapon, new TetraEnchantmentHelper.EnchantmentRules("additions/pointy_weapon", "exclusions/pointy_weapon", EnchantmentCategory.TRIDENT));
        aspectMap.put(ItemAspect.throwable, new TetraEnchantmentHelper.EnchantmentRules("additions/throwable", "exclusions/throwable", (EnchantmentCategory) null));
        aspectMap.put(ItemAspect.blockBreaker, new TetraEnchantmentHelper.EnchantmentRules("additions/block_breaker", "exclusions/block_breaker", EnchantmentCategory.DIGGER));
        aspectMap.put(ItemAspect.fishingRod, new TetraEnchantmentHelper.EnchantmentRules("additions/fishing_rod", "exclusions/fishing_rod", EnchantmentCategory.FISHING_ROD));
        aspectMap.put(ItemAspect.breakable, new TetraEnchantmentHelper.EnchantmentRules("additions/breakable", "exclusions/breakable", EnchantmentCategory.BREAKABLE));
        aspectMap.put(ItemAspect.bow, new TetraEnchantmentHelper.EnchantmentRules("additions/bow", "exclusions/bow", EnchantmentCategory.BOW));
        aspectMap.put(ItemAspect.wearable, new TetraEnchantmentHelper.EnchantmentRules("additions/wearable", "exclusions/wearable", EnchantmentCategory.WEARABLE));
        aspectMap.put(ItemAspect.crossbow, new TetraEnchantmentHelper.EnchantmentRules("additions/crossbow", "exclusions/crossbow", EnchantmentCategory.CROSSBOW));
        aspectMap.put(ItemAspect.vanishable, new TetraEnchantmentHelper.EnchantmentRules("additions/vanishable", "exclusions/vanishable", EnchantmentCategory.VANISHABLE));
    }

    private static EnchantmentCategory fromName(String enchantmentCategoryName) {
        try {
            return EnchantmentCategory.valueOf(enchantmentCategoryName);
        } catch (IllegalArgumentException var2) {
            return null;
        }
    }

    public static void registerMapping(ItemAspect aspect, @Nullable EnchantmentCategory category, String additions, String exclusions) {
        registerMapping(aspect, new TetraEnchantmentHelper.EnchantmentRules(additions, exclusions, category));
    }

    public static void registerMapping(ItemAspect aspect, TetraEnchantmentHelper.EnchantmentRules rules) {
        aspectMap.put(aspect, rules);
    }

    public static boolean isApplicableForAspects(Enchantment enchantment, boolean fromTable, Map<ItemAspect, Integer> aspects) {
        int requiredLevel = fromTable ? 2 : 1;
        return aspects.entrySet().stream().filter(entry -> (Integer) entry.getValue() >= requiredLevel).filter(entry -> aspectMap.containsKey(entry.getKey())).anyMatch(entry -> ((TetraEnchantmentHelper.EnchantmentRules) aspectMap.get(entry.getKey())).isApplicable(enchantment));
    }

    public static EnchantmentCategory[] getEnchantmentCategories(ItemAspect aspect) {
        return ((TetraEnchantmentHelper.EnchantmentRules) aspectMap.get(aspect)).categories;
    }

    public static ItemStack removeAllEnchantments(ItemStack itemStack) {
        itemStack.removeTagKey("Enchantments");
        itemStack.removeTagKey("StoredEnchantments");
        itemStack.removeTagKey("EnchantmentMapping");
        IModularItem.updateIdentifier(itemStack);
        return itemStack;
    }

    public static ItemStack transferReplacementEnchantments(ItemStack original, ItemStack replacementStack) {
        Optional.ofNullable(original.getTag()).map(tag -> tag.getList("Enchantments", 10)).filter(enchantments -> enchantments.size() > 0).ifPresent(enchantments -> {
            replacementStack.getOrCreateTag().put("Enchantments", enchantments.copy());
            mapEnchantments(replacementStack);
        });
        return replacementStack;
    }

    public static void applyEnchantment(ItemStack itemStack, String slot, Enchantment enchantment, int level) {
        itemStack.enchant(enchantment, level);
        mapEnchantment(itemStack, slot, enchantment);
    }

    public static void mapEnchantment(ItemStack itemStack, String slot, Enchantment enchantment) {
        CompoundTag map = itemStack.getOrCreateTagElement("EnchantmentMapping");
        map.putString(ForgeRegistries.ENCHANTMENTS.getKey(enchantment).toString(), slot);
    }

    public static void mapEnchantments(ItemStack itemStack) {
        CompoundTag mappings = itemStack.getOrCreateTagElement("EnchantmentMapping");
        Map<String, String> mapped = (Map<String, String>) Optional.of(mappings).map(CompoundTag::m_128431_).stream().flatMap(Collection::stream).collect(Collectors.toMap(Function.identity(), mappings::m_128461_));
        Map<String, Integer> capacity = (Map<String, Integer>) Arrays.stream(((IModularItem) itemStack.getItem()).getMajorModules(itemStack)).filter(Objects::nonNull).collect(Collectors.toMap(ItemModule::getSlot, module -> module.getMagicCapacity(itemStack)));
        List<Pair<String, Integer>> unmapped = (List<Pair<String, Integer>>) Optional.of(itemStack.getEnchantmentTags()).stream().flatMap(Collection::stream).map(nbt -> (CompoundTag) nbt).map(nbt -> Pair.of(nbt.getString("id"), nbt.getInt("lvl"))).filter(pair -> !mapped.containsKey(pair.getKey())).collect(Collectors.toList());
        ItemModuleMajor[] modules = ((IModularItem) itemStack.getItem()).getMajorModules(itemStack);
        unmapped.forEach(pair -> {
            Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation((String) pair.getKey()));
            if (enchantment != null) {
                Arrays.stream(modules).filter(Objects::nonNull).filter(module -> module.acceptsEnchantment(itemStack, enchantment, false)).map(ItemModule::getSlot).max(Comparator.comparing(slot -> (Integer) capacity.getOrDefault(slot, 0))).ifPresent(slot -> {
                    mapEnchantment(itemStack, slot, enchantment);
                    int cost = getEnchantmentCapacityCost(enchantment, (Integer) pair.getRight());
                    capacity.merge(slot, cost, Integer::sum);
                });
            }
        });
    }

    @Nullable
    public static Pair<String, Integer> getEnchantmentPrimitive(CompoundTag nbt) {
        return Pair.of(nbt.getString("id"), nbt.getInt("lvl"));
    }

    @Nullable
    public static Pair<Enchantment, Integer> getEnchantment(CompoundTag nbt) {
        return (Pair<Enchantment, Integer>) Optional.ofNullable(ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(nbt.getString("id")))).map(enchantment -> Pair.of(enchantment, nbt.getInt("lvl"))).orElse(null);
    }

    public static int getEnchantmentCapacityCost(Enchantment enchantment, int level) {
        return -(enchantment.getMaxCost(level) + enchantment.getMinCost(level)) / 2;
    }

    public static void removeEnchantment(ItemStack itemStack, Enchantment enchantment) {
        Optional.ofNullable(ForgeRegistries.ENCHANTMENTS.getKey(enchantment)).ifPresent(enchantmentKey -> removeEnchantment(itemStack, enchantmentKey.toString()));
    }

    public static void removeEnchantment(ItemStack itemStack, String enchantment) {
        Optional.ofNullable(itemStack.getTagElement("EnchantmentMapping")).ifPresent(map -> map.remove(enchantment));
        Optional.ofNullable(itemStack.getTag()).map(tag -> tag.getList("Enchantments", 10)).ifPresent(enchantments -> enchantments.removeIf(nbt -> enchantment.equals(((CompoundTag) nbt).getString("id"))));
    }

    public static void removeEnchantments(ItemStack itemStack, String slot) {
        CompoundTag map = itemStack.getTagElement("EnchantmentMapping");
        ListTag enchantments = (ListTag) Optional.ofNullable(itemStack.getTag()).map(tag -> tag.getList("Enchantments", 10)).orElse(null);
        if (map != null && enchantments != null) {
            Set<String> matchingEnchantments = (Set<String>) map.getAllKeys().stream().filter(ench -> slot.equals(map.getString(ench))).collect(Collectors.toSet());
            enchantments.removeIf(nbt -> matchingEnchantments.contains(((CompoundTag) nbt).getString("id")));
            matchingEnchantments.forEach(map::m_128473_);
        }
    }

    public static String getEnchantmentTooltip(Enchantment enchantment, int level, boolean clearFormatting) {
        return clearFormatting ? ChatFormatting.stripFormatting(getEnchantmentName(enchantment, level)) : getEnchantmentName(enchantment, level);
    }

    public static String getEnchantmentName(Enchantment enchantment, int level) {
        return enchantment.getFullname(level).getString();
    }

    public static String getEnchantmentDescription(Enchantment enchantment) {
        return (String) Optional.of(enchantment.getDescriptionId() + ".desc").filter(I18n::m_118936_).map(x$0 -> I18n.get(x$0)).orElse(null);
    }

    public static class EnchantmentRules {

        EnchantmentCategory[] categories;

        TagKey<Enchantment> exclusions;

        TagKey<Enchantment> additions;

        public EnchantmentRules(String additions, String exclusions, EnchantmentCategory... categories) {
            this.categories = (EnchantmentCategory[]) Arrays.stream(categories).filter(Objects::nonNull).toArray(EnchantmentCategory[]::new);
            ITagManager<Enchantment> tags = ForgeRegistries.ENCHANTMENTS.tags();
            this.additions = tags.createTagKey(new ResourceLocation("tetra", additions));
            this.exclusions = tags.createTagKey(new ResourceLocation("tetra", exclusions));
        }

        public boolean isApplicable(Enchantment enchantment) {
            ITagManager<Enchantment> tags = ForgeRegistries.ENCHANTMENTS.tags();
            return (Arrays.asList(this.categories).contains(enchantment.category) || tags.getTag(this.additions).contains(enchantment)) && !tags.getTag(this.exclusions).contains(enchantment);
        }
    }
}