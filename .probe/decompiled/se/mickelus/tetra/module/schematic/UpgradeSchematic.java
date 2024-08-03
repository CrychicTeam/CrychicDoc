package se.mickelus.tetra.module.schematic;

import java.util.Collection;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ToolAction;
import se.mickelus.tetra.blocks.workbench.WorkbenchTile;
import se.mickelus.tetra.module.data.GlyphData;
import se.mickelus.tetra.module.data.MaterialMultiplier;

public interface UpgradeSchematic {

    String getKey();

    String getName();

    String[] getSources();

    String getDescription(@Nullable ItemStack var1);

    @Nullable
    default MaterialMultiplier getMaterialTranslation() {
        return null;
    }

    @Nullable
    default String[] getApplicableMaterials() {
        return null;
    }

    int getNumMaterialSlots();

    String getSlotName(ItemStack var1, int var2);

    default ItemStack[] getSlotPlaceholders(ItemStack itemStack, int index) {
        return new ItemStack[0];
    }

    int getRequiredQuantity(ItemStack var1, int var2, ItemStack var3);

    boolean acceptsMaterial(ItemStack var1, String var2, int var3, ItemStack var4);

    boolean isMaterialsValid(ItemStack var1, String var2, ItemStack[] var3);

    boolean isRelevant(ItemStack var1);

    default boolean matchesRequirements(CraftingContext context) {
        return true;
    }

    boolean isApplicableForSlot(String var1, ItemStack var2);

    default boolean isVisibleForPlayer(Player player, @Nullable WorkbenchTile tile, ItemStack targetStack) {
        return true;
    }

    boolean canApplyUpgrade(Player var1, ItemStack var2, ItemStack[] var3, String var4, Map<ToolAction, Integer> var5);

    boolean isIntegrityViolation(Player var1, ItemStack var2, ItemStack[] var3, String var4);

    ItemStack applyUpgrade(ItemStack var1, ItemStack[] var2, boolean var3, String var4, Player var5);

    boolean checkTools(ItemStack var1, ItemStack[] var2, Map<ToolAction, Integer> var3);

    Map<ToolAction, Integer> getRequiredToolLevels(ItemStack var1, ItemStack[] var2);

    default Collection<ToolAction> getRequiredTools(ItemStack targetStack, ItemStack[] materials) {
        return this.getRequiredToolLevels(targetStack, materials).keySet();
    }

    default int getRequiredToolLevel(ItemStack targetStack, ItemStack[] materials, ToolAction toolAction) {
        return (Integer) this.getRequiredToolLevels(targetStack, materials).getOrDefault(toolAction, 0);
    }

    default int getExperienceCost(ItemStack targetStack, ItemStack[] materials, String slot) {
        return 0;
    }

    default boolean isHoning() {
        return false;
    }

    SchematicType getType();

    default SchematicRarity getRarity() {
        return SchematicRarity.basic;
    }

    GlyphData getGlyph();

    OutcomePreview[] getPreviews(ItemStack var1, String var2);

    default float getSeverity(ItemStack itemStack, ItemStack[] materials, String slot) {
        return 1.0F;
    }

    default boolean willReplace(ItemStack itemStack, ItemStack[] materials, String slot) {
        return false;
    }
}