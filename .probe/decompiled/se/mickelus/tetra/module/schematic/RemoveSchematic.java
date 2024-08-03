package se.mickelus.tetra.module.schematic;

import java.util.Collections;
import java.util.Map;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ToolAction;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.ConfigHandler;
import se.mickelus.tetra.TetraToolActions;
import se.mickelus.tetra.aspect.TetraEnchantmentHelper;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.module.ItemModule;
import se.mickelus.tetra.module.data.GlyphData;

@ParametersAreNonnullByDefault
public class RemoveSchematic extends BaseSchematic {

    private static final String localizationPrefix = "tetra/schematic/";

    private static final String nameSuffix = ".name";

    private static final String descriptionSuffix = ".description";

    private final String identifier = "remove";

    private final GlyphData glyph = new GlyphData(GuiTextures.glyphs, 80, 224);

    @Override
    public String getKey() {
        return "remove";
    }

    @Override
    public String getName() {
        return I18n.get("tetra/schematic/remove.name");
    }

    @Override
    public String[] getSources() {
        return new String[] { "tetra" };
    }

    @Override
    public String getDescription(ItemStack itemStack) {
        return I18n.get("tetra/schematic/remove.description");
    }

    @Override
    public int getNumMaterialSlots() {
        return 0;
    }

    @Override
    public String getSlotName(ItemStack itemStack, int index) {
        return "";
    }

    @Override
    public int getRequiredQuantity(ItemStack itemStack, int index, ItemStack materialStack) {
        return 0;
    }

    @Override
    public boolean acceptsMaterial(ItemStack itemStack, String itemSlot, int index, ItemStack materialStack) {
        return false;
    }

    @Override
    public boolean isRelevant(ItemStack itemStack) {
        return itemStack.getItem() instanceof IModularItem;
    }

    @Override
    public boolean isApplicableForSlot(String slot, ItemStack targetStack) {
        return (Boolean) CastOptional.cast(targetStack.getItem(), IModularItem.class).map(item -> !item.isModuleRequired(targetStack, slot) && item.getModuleFromSlot(targetStack, slot) != null).orElse(false);
    }

    @Override
    public boolean canApplyUpgrade(Player player, ItemStack itemStack, ItemStack[] materials, String slot, Map<ToolAction, Integer> availableTools) {
        return !this.isIntegrityViolation(player, itemStack, materials, slot) && this.checkTools(itemStack, materials, availableTools);
    }

    @Override
    public boolean isMaterialsValid(ItemStack itemStack, String itemSlot, ItemStack[] materials) {
        return true;
    }

    @Override
    public ItemStack applyUpgrade(ItemStack itemStack, ItemStack[] materials, boolean consumeMaterials, String slot, Player player) {
        ItemStack upgradedStack = itemStack.copy();
        IModularItem item = (IModularItem) itemStack.getItem();
        float durabilityFactor = 0.0F;
        if (consumeMaterials && upgradedStack.isDamageableItem()) {
            durabilityFactor = (float) upgradedStack.getDamageValue() * 1.0F / (float) upgradedStack.getMaxDamage();
        }
        float honingFactor = Mth.clamp(1.0F * (float) item.getHoningProgress(upgradedStack) / (float) item.getHoningLimit(upgradedStack), 0.0F, 1.0F);
        ItemModule previousModule = item.getModuleFromSlot(upgradedStack, slot);
        if (previousModule != null) {
            TetraEnchantmentHelper.removeEnchantments(upgradedStack, slot);
            previousModule.removeModule(upgradedStack);
            if (consumeMaterials) {
                previousModule.postRemove(upgradedStack, player);
            }
        }
        if (consumeMaterials) {
            if (ConfigHandler.moduleProgression.get() && IModularItem.isHoneable(upgradedStack)) {
                item.setHoningProgress(upgradedStack, (int) Math.ceil((double) (honingFactor * (float) item.getHoningLimit(upgradedStack))));
            }
            if (upgradedStack.isDamageableItem()) {
                upgradedStack.setDamageValue((int) (durabilityFactor * (float) upgradedStack.getMaxDamage()));
            }
        }
        return upgradedStack;
    }

    @Override
    public Map<ToolAction, Integer> getRequiredToolLevels(ItemStack targetStack, ItemStack[] materials) {
        return Collections.singletonMap(TetraToolActions.hammer, 1);
    }

    @Override
    public SchematicType getType() {
        return SchematicType.other;
    }

    @Override
    public GlyphData getGlyph() {
        return this.glyph;
    }
}