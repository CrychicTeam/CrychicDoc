package se.mickelus.tetra.module.schematic;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.registries.ForgeRegistries;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.advancements.ImprovementCraftCriterion;
import se.mickelus.tetra.aspect.TetraEnchantmentHelper;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.module.ItemModuleMajor;
import se.mickelus.tetra.module.data.GlyphData;
import se.mickelus.tetra.module.data.ToolData;

@ParametersAreNonnullByDefault
public class BookEnchantSchematic implements UpgradeSchematic {

    private static final String localizationPrefix = "tetra/schematic/";

    private static final String key = "book_enchant";

    private static final String nameSuffix = ".name";

    private static final String descriptionSuffix = ".description";

    private static final String slotSuffix = ".slot1";

    private final GlyphData glyph = new GlyphData(GuiTextures.glyphs, 96, 224);

    @Override
    public String getKey() {
        return "book_enchant";
    }

    @Override
    public String getName() {
        return I18n.get("tetra/schematic/book_enchant.name");
    }

    @Override
    public String[] getSources() {
        return new String[] { "tetra" };
    }

    @Override
    public String getDescription(ItemStack itemStack) {
        return I18n.get("tetra/schematic/book_enchant.description");
    }

    @Override
    public int getNumMaterialSlots() {
        return 1;
    }

    @Override
    public String getSlotName(ItemStack itemStack, int index) {
        return I18n.get("tetra/schematic/book_enchant.slot1");
    }

    @Override
    public ItemStack[] getSlotPlaceholders(ItemStack itemStack, int index) {
        return new ItemStack[] { Items.ENCHANTED_BOOK.getDefaultInstance() };
    }

    @Override
    public int getRequiredQuantity(ItemStack itemStack, int index, ItemStack materialStack) {
        return 1;
    }

    @Override
    public boolean acceptsMaterial(ItemStack itemStack, String itemSlot, int index, ItemStack materialStack) {
        if (materialStack.isEmpty()) {
            return false;
        } else {
            ItemModuleMajor module = (ItemModuleMajor) CastOptional.cast(itemStack.getItem(), IModularItem.class).map(item -> item.getModuleFromSlot(itemStack, itemSlot)).flatMap(mod -> CastOptional.cast(mod, ItemModuleMajor.class)).orElse(null);
            Map<Enchantment, Integer> currentEnchantments = EnchantmentHelper.getEnchantments(itemStack);
            return module != null && materialStack.getItem() instanceof EnchantedBookItem && EnchantmentHelper.getEnchantments(materialStack).entrySet().stream().anyMatch(entry -> this.acceptsEnchantment(itemStack, module, currentEnchantments.keySet(), (Enchantment) entry.getKey(), (Integer) entry.getValue()));
        }
    }

    protected boolean stacksEnchantment(ItemStack itemStack, ItemModuleMajor module, Enchantment enchantment, int level) {
        Map<Enchantment, Integer> moduleEnchantments = module.getEnchantments(itemStack);
        if (!moduleEnchantments.containsKey(enchantment)) {
            return false;
        } else {
            int currentLevel = (Integer) moduleEnchantments.get(enchantment);
            return level >= currentLevel && currentLevel < enchantment.getMaxLevel();
        }
    }

    protected boolean acceptsEnchantment(ItemStack itemStack, ItemModuleMajor module, Set<Enchantment> currentEnchantments, Enchantment enchantment, int level) {
        return module.acceptsEnchantment(itemStack, enchantment, false) && (this.stacksEnchantment(itemStack, module, enchantment, level) || EnchantmentHelper.isEnchantmentCompatible(currentEnchantments, enchantment));
    }

    @Override
    public boolean isMaterialsValid(ItemStack itemStack, String itemSlot, ItemStack[] materials) {
        return this.acceptsMaterial(itemStack, itemSlot, 0, materials[0]);
    }

    @Override
    public boolean isRelevant(ItemStack itemStack) {
        return itemStack.getItem() instanceof IModularItem;
    }

    @Override
    public boolean isApplicableForSlot(String slot, ItemStack targetStack) {
        return (Boolean) CastOptional.cast(targetStack.getItem(), IModularItem.class).map(item -> item.getModuleFromSlot(targetStack, slot)).map(module -> module.getMagicCapacityGain(targetStack) > 0).orElse(false);
    }

    @Override
    public boolean canApplyUpgrade(Player player, ItemStack itemStack, ItemStack[] materials, String slot, Map<ToolAction, Integer> availableTools) {
        return this.isMaterialsValid(itemStack, slot, materials) && (player.isCreative() || player.experienceLevel >= this.getExperienceCost(itemStack, materials, slot));
    }

    @Override
    public boolean isIntegrityViolation(Player player, ItemStack itemStack, ItemStack[] materials, String slot) {
        return false;
    }

    @Override
    public ItemStack applyUpgrade(ItemStack itemStack, ItemStack[] materials, boolean consumeMaterials, String slot, Player player) {
        ItemStack upgradedStack = itemStack.copy();
        ItemModuleMajor module = (ItemModuleMajor) CastOptional.cast(itemStack.getItem(), IModularItem.class).map(item -> item.getModuleFromSlot(itemStack, slot)).filter(mod -> mod instanceof ItemModuleMajor).map(mod -> (ItemModuleMajor) mod).orElse(null);
        if (module != null) {
            Map<Enchantment, Integer> currentEnchantments = EnchantmentHelper.getEnchantments(itemStack);
            EnchantmentHelper.getEnchantments(materials[0]).entrySet().stream().filter(entry -> this.acceptsEnchantment(itemStack, module, currentEnchantments.keySet(), (Enchantment) entry.getKey(), (Integer) entry.getValue())).forEach(entry -> {
                int level = (Integer) entry.getValue();
                if (this.stacksEnchantment(upgradedStack, module, (Enchantment) entry.getKey(), level)) {
                    level = Math.max((Integer) currentEnchantments.get(entry.getKey()) + 1, level);
                    currentEnchantments.put((Enchantment) entry.getKey(), level);
                    EnchantmentHelper.setEnchantments(currentEnchantments, upgradedStack);
                } else {
                    TetraEnchantmentHelper.applyEnchantment(upgradedStack, module.getSlot(), (Enchantment) entry.getKey(), level);
                }
                if (consumeMaterials && player instanceof ServerPlayer) {
                    ImprovementCraftCriterion.trigger((ServerPlayer) player, itemStack, upgradedStack, this.getKey(), slot, "enchantment:" + ForgeRegistries.ENCHANTMENTS.getKey((Enchantment) entry.getKey()).toString(), level, null, -1);
                }
            });
            if (consumeMaterials) {
                materials[0].shrink(1);
            }
        }
        return upgradedStack;
    }

    @Override
    public boolean checkTools(ItemStack targetStack, ItemStack[] materials, Map<ToolAction, Integer> availableTools) {
        return true;
    }

    @Override
    public Map<ToolAction, Integer> getRequiredToolLevels(ItemStack targetStack, ItemStack[] materials) {
        return Collections.emptyMap();
    }

    @Override
    public int getExperienceCost(ItemStack targetStack, ItemStack[] materials, String slot) {
        return CastOptional.cast(targetStack.getItem(), IModularItem.class).map(item -> item.getModuleFromSlot(targetStack, slot)).flatMap(module -> CastOptional.cast(module, ItemModuleMajor.class)).map(module -> EnchantmentHelper.getEnchantments(materials[0])).map(Map::values).stream().flatMap(Collection::stream).mapToInt(lvl -> lvl).sum();
    }

    @Override
    public SchematicType getType() {
        return SchematicType.improvement;
    }

    @Override
    public GlyphData getGlyph() {
        return this.glyph;
    }

    @Override
    public OutcomePreview[] getPreviews(ItemStack targetStack, String slot) {
        ItemModuleMajor module = (ItemModuleMajor) CastOptional.cast(targetStack.getItem(), IModularItem.class).map(item -> item.getModuleFromSlot(targetStack, slot)).flatMap(m -> CastOptional.cast(m, ItemModuleMajor.class)).orElse(null);
        if (module != null) {
            ToolData emptyTools = new ToolData();
            return (OutcomePreview[]) ForgeRegistries.ENCHANTMENTS.getValues().stream().filter(enchantment -> module.acceptsEnchantment(targetStack, enchantment, false)).flatMap(enchantment -> IntStream.range(enchantment.getMinLevel(), enchantment.getMaxLevel() + 1).mapToObj(level -> {
                ItemStack enchantedStack = targetStack.copy();
                EnchantmentHelper.getEnchantments(enchantedStack).keySet().stream().filter(currentEnchantment -> !enchantment.isCompatibleWith(currentEnchantment)).forEach(currentEnchantment -> TetraEnchantmentHelper.removeEnchantment(enchantedStack, currentEnchantment));
                TetraEnchantmentHelper.applyEnchantment(enchantedStack, module.getSlot(), enchantment, level);
                return new OutcomePreviewEnchantment(ForgeRegistries.ENCHANTMENTS.getKey(enchantment).toString(), TetraEnchantmentHelper.getEnchantmentName(enchantment, level), "misc", level, this.glyph, enchantedStack, SchematicType.improvement, emptyTools, new ItemStack[0]);
            })).toArray(OutcomePreview[]::new);
        } else {
            return new OutcomePreview[0];
        }
    }
}