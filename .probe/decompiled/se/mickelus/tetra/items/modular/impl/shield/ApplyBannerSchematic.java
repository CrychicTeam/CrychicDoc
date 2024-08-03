package se.mickelus.tetra.items.modular.impl.shield;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.ToolAction;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.advancements.ImprovementCraftCriterion;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.module.ItemModuleMajor;
import se.mickelus.tetra.module.data.GlyphData;
import se.mickelus.tetra.module.schematic.OutcomePreview;
import se.mickelus.tetra.module.schematic.SchematicType;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

@ParametersAreNonnullByDefault
public class ApplyBannerSchematic implements UpgradeSchematic {

    private static final String localizationPrefix = "tetra/schematic/";

    private static final String key = "shield/plate/banner";

    private static final String nameSuffix = ".name";

    private static final String descriptionSuffix = ".description";

    private static final String slotSuffix = ".slot1";

    private final GlyphData glyph = new GlyphData(GuiTextures.glyphs, 96, 240);

    @Override
    public String getKey() {
        return "shield/plate/banner";
    }

    @Override
    public String getName() {
        return I18n.get("tetra/schematic/shield/plate/banner.name");
    }

    @Override
    public String[] getSources() {
        return new String[] { "tetra" };
    }

    @Override
    public String getDescription(ItemStack itemStack) {
        return I18n.get("tetra/schematic/shield/plate/banner.description");
    }

    @Override
    public int getNumMaterialSlots() {
        return 1;
    }

    @Override
    public String getSlotName(ItemStack itemStack, int index) {
        return I18n.get("tetra/schematic/shield/plate/banner.slot1");
    }

    @Override
    public ItemStack[] getSlotPlaceholders(ItemStack itemStack, int index) {
        return new ItemStack[] { Items.WHITE_BANNER.getDefaultInstance() };
    }

    @Override
    public int getRequiredQuantity(ItemStack itemStack, int index, ItemStack materialStack) {
        return 1;
    }

    @Override
    public boolean acceptsMaterial(ItemStack itemStack, String itemSlot, int index, ItemStack materialStack) {
        return materialStack.getItem() instanceof BannerItem;
    }

    @Override
    public boolean isMaterialsValid(ItemStack itemStack, String itemSlot, ItemStack[] materials) {
        return this.acceptsMaterial(itemStack, itemSlot, 0, materials[0]);
    }

    @Override
    public boolean isRelevant(ItemStack itemStack) {
        return itemStack.getItem() instanceof ModularShieldItem;
    }

    @Override
    public boolean isApplicableForSlot(String slot, ItemStack targetStack) {
        return "shield/plate".equals(slot);
    }

    @Override
    public boolean canApplyUpgrade(Player player, ItemStack itemStack, ItemStack[] materials, String slot, Map<ToolAction, Integer> availableTools) {
        return this.isMaterialsValid(itemStack, slot, materials);
    }

    @Override
    public boolean isIntegrityViolation(Player player, ItemStack itemStack, ItemStack[] materials, String slot) {
        return false;
    }

    @Override
    public ItemStack applyUpgrade(ItemStack itemStack, ItemStack[] materials, boolean consumeMaterials, String slot, Player player) {
        ItemStack upgradedStack = itemStack.copy();
        ItemStack bannerStack = materials[0];
        if (this.isMaterialsValid(itemStack, slot, materials)) {
            CastOptional.cast(itemStack.getItem(), IModularItem.class).map(item -> item.getModuleFromSlot(itemStack, slot)).flatMap(module -> CastOptional.cast(module, ItemModuleMajor.class)).ifPresent(module -> {
                if (module.acceptsImprovementLevel("shield/banner", 0)) {
                    module.addImprovement(upgradedStack, "shield/banner", 0);
                    CompoundTag bannerTag = (CompoundTag) Optional.ofNullable(bannerStack.getTagElement("BlockEntityTag")).map(CompoundTag::m_6426_).orElse(new CompoundTag());
                    bannerTag.putInt("Base", ((BannerItem) bannerStack.getItem()).getColor().getId());
                    upgradedStack.addTagElement("BlockEntityTag", bannerTag.copy());
                    if (consumeMaterials) {
                        materials[0].shrink(1);
                    }
                    if (consumeMaterials && player instanceof ServerPlayer) {
                        ImprovementCraftCriterion.trigger((ServerPlayer) player, itemStack, upgradedStack, this.getKey(), slot, "shield/banner", 0, null, -1);
                    }
                }
            });
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
    public SchematicType getType() {
        return SchematicType.improvement;
    }

    @Override
    public GlyphData getGlyph() {
        return this.glyph;
    }

    @Override
    public OutcomePreview[] getPreviews(ItemStack targetStack, String slot) {
        return new OutcomePreview[0];
    }
}