package se.mickelus.tetra.module.schematic;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolAction;
import org.apache.commons.lang3.ArrayUtils;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.module.ItemModuleMajor;
import se.mickelus.tetra.module.data.GlyphData;
import se.mickelus.tetra.module.improvement.DestabilizationEffect;

@ParametersAreNonnullByDefault
public class CleanseSchematic implements UpgradeSchematic {

    private static final String localizationPrefix = "tetra/schematic/";

    private static final String key = "cleanse";

    private static final String nameSuffix = ".name";

    private static final String descriptionSuffix = ".description";

    private static final String slotLabel = "item.minecraft.lapis_lazuli";

    private final GlyphData glyph = new GlyphData("textures/gui/workbench.png", 80, 32);

    @Override
    public String getKey() {
        return "cleanse";
    }

    @Override
    public String getName() {
        return I18n.get("tetra/schematic/cleanse.name");
    }

    @Override
    public String[] getSources() {
        return new String[] { "tetra" };
    }

    @Override
    public String getDescription(ItemStack itemStack) {
        return I18n.get("tetra/schematic/cleanse.description");
    }

    @Override
    public int getNumMaterialSlots() {
        return 1;
    }

    @Override
    public String getSlotName(ItemStack itemStack, int index) {
        return I18n.get("item.minecraft.lapis_lazuli");
    }

    @Override
    public ItemStack[] getSlotPlaceholders(ItemStack itemStack, int index) {
        return new ItemStack[] { Items.LAPIS_LAZULI.getDefaultInstance() };
    }

    @Override
    public int getRequiredQuantity(ItemStack itemStack, int index, ItemStack materialStack) {
        return 1;
    }

    @Override
    public boolean acceptsMaterial(ItemStack itemStack, String itemSlot, int index, ItemStack materialStack) {
        return materialStack.is(Tags.Items.GEMS_LAPIS);
    }

    @Override
    public boolean isMaterialsValid(ItemStack itemStack, String itemSlot, ItemStack[] materials) {
        return this.acceptsMaterial(itemStack, itemSlot, 0, materials[0]);
    }

    @Override
    public boolean isRelevant(ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean isApplicableForSlot(String slot, ItemStack targetStack) {
        String[] destabilizationKeys = DestabilizationEffect.getKeys();
        return ((Stream) CastOptional.cast(targetStack.getItem(), IModularItem.class).map(item -> item.getModuleFromSlot(targetStack, slot)).filter(module -> module instanceof ItemModuleMajor).map(module -> (ItemModuleMajor) module).map(module -> Arrays.stream(module.getImprovements(targetStack))).orElse(Stream.empty())).anyMatch(improvement -> ArrayUtils.contains(destabilizationKeys, improvement.key));
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
        String[] destabilizationKeys = DestabilizationEffect.getKeys();
        CastOptional.cast(itemStack.getItem(), IModularItem.class).map(item -> item.getModuleFromSlot(itemStack, slot)).filter(module -> module instanceof ItemModuleMajor).map(module -> (ItemModuleMajor) module).ifPresent(module -> Arrays.stream(destabilizationKeys).forEach(key -> module.removeImprovement(upgradedStack, key)));
        if (consumeMaterials) {
            materials[0].shrink(1);
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
        String[] destabilizationKeys = DestabilizationEffect.getKeys();
        int cost = ((Stream) CastOptional.cast(targetStack.getItem(), IModularItem.class).map(item -> item.getModuleFromSlot(targetStack, slot)).filter(module -> module instanceof ItemModuleMajor).map(module -> (ItemModuleMajor) module).map(module -> Arrays.stream(module.getImprovements(targetStack))).orElse(Stream.empty())).filter(improvement -> ArrayUtils.contains(destabilizationKeys, improvement.key)).mapToInt(improvement -> improvement.level + 1).sum();
        return cost + 3;
    }

    @Override
    public SchematicType getType() {
        return SchematicType.other;
    }

    @Override
    public GlyphData getGlyph() {
        return this.glyph;
    }

    @Override
    public OutcomePreview[] getPreviews(ItemStack targetStack, String slot) {
        return new OutcomePreview[0];
    }

    @Override
    public float getSeverity(ItemStack itemStack, ItemStack[] materials, String slot) {
        return 0.0F;
    }
}