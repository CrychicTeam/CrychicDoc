package se.mickelus.tetra.module.schematic;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ToolAction;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.mutil.util.Filter;
import se.mickelus.tetra.ConfigHandler;
import se.mickelus.tetra.advancements.ImprovementCraftCriterion;
import se.mickelus.tetra.advancements.ModuleCraftCriterion;
import se.mickelus.tetra.blocks.workbench.WorkbenchTile;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.module.ItemModule;
import se.mickelus.tetra.module.ItemModuleMajor;
import se.mickelus.tetra.module.ItemUpgradeRegistry;
import se.mickelus.tetra.module.data.GlyphData;
import se.mickelus.tetra.module.data.MaterialMultiplier;
import se.mickelus.tetra.module.data.VariantData;

@ParametersAreNonnullByDefault
public class ConfigSchematic extends BaseSchematic {

    private static final String localizationPrefix = "tetra/schematic/";

    private static final String nameSuffix = ".name";

    private static final String descriptionSuffix = ".description";

    private static final String slotSuffix = ".slot";

    private final SchematicDefinition definition;

    private final String keySuffix;

    private final String moduleSlot;

    public ConfigSchematic(SchematicDefinition definition) throws InvalidSchematicException {
        this(definition, "", null);
    }

    public ConfigSchematic(SchematicDefinition definition, String keySuffix, String moduleSlot) throws InvalidSchematicException {
        this.definition = definition;
        this.keySuffix = keySuffix;
        this.moduleSlot = moduleSlot;
        String[] faultyModuleOutcomes = (String[]) Arrays.stream(definition.outcomes).map(this::getModuleKey).filter(Objects::nonNull).filter(moduleKey -> ItemUpgradeRegistry.instance.getModule(moduleKey) == null).toArray(String[]::new);
        if (faultyModuleOutcomes.length != 0) {
            throw new InvalidSchematicException(definition.key, faultyModuleOutcomes);
        }
    }

    private String getModuleKey(OutcomeDefinition outcome) {
        return outcome.moduleKey != null ? outcome.moduleKey + this.keySuffix : null;
    }

    private Optional<OutcomeDefinition> getOutcomeFromMaterial(ItemStack materialStack, int slot) {
        return Arrays.stream(this.definition.outcomes).filter(outcome -> outcome.materialSlot == slot).filter(outcome -> outcome.material.getPredicate() != null && outcome.material.getPredicate().matches(materialStack)).reduce((a, b) -> b);
    }

    @Override
    public String getKey() {
        return this.definition.key + this.keySuffix;
    }

    @Override
    public String getName() {
        return this.definition.localizationKey != null ? I18n.get("tetra/schematic/" + this.definition.localizationKey + ".name") : I18n.get("tetra/schematic/" + this.definition.key + ".name");
    }

    @Override
    public String[] getSources() {
        return this.definition.sources;
    }

    @Override
    public String getDescription(ItemStack itemStack) {
        return this.definition.localizationKey != null ? I18n.get("tetra/schematic/" + this.definition.localizationKey + ".description") : I18n.get("tetra/schematic/" + this.definition.key + ".description");
    }

    @Nullable
    @Override
    public MaterialMultiplier getMaterialTranslation() {
        return this.definition.translation;
    }

    @Nullable
    @Override
    public String[] getApplicableMaterials() {
        return this.definition.applicableMaterials;
    }

    @Override
    public int getNumMaterialSlots() {
        return this.definition.materialSlotCount;
    }

    @Override
    public String getSlotName(ItemStack itemStack, int index) {
        return this.definition.localizationKey != null ? I18n.get("tetra/schematic/" + this.definition.localizationKey + ".slot" + (index + 1)) : I18n.get("tetra/schematic/" + this.definition.key + ".slot" + (index + 1));
    }

    @Override
    public int getRequiredQuantity(ItemStack itemStack, int index, ItemStack materialStack) {
        return (Integer) this.getOutcomeFromMaterial(materialStack, index).map(outcome -> outcome.material.count).orElse(0);
    }

    @Override
    public boolean acceptsMaterial(ItemStack itemStack, String itemSlot, int index, ItemStack materialStack) {
        return this.getOutcomeFromMaterial(materialStack, index).isPresent();
    }

    @Override
    public boolean isMaterialsValid(ItemStack itemStack, String itemSlot, ItemStack[] materials) {
        if (this.getNumMaterialSlots() == 0) {
            return true;
        } else if (materials.length < this.definition.materialSlotCount) {
            return false;
        } else {
            for (int i = 0; i < this.definition.materialSlotCount; i++) {
                if (!this.acceptsMaterial(itemStack, itemSlot, i, materials[i]) || materials[i].getCount() < (Integer) this.getOutcomeFromMaterial(materials[i], i).map(outcome -> outcome.material.count).orElse(0)) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public boolean isRelevant(ItemStack itemStack) {
        return this.moduleSlot != null ? ((Stream) CastOptional.cast(itemStack.getItem(), IModularItem.class).map(item -> Stream.concat(Arrays.stream(item.getMajorModuleKeys(itemStack)), Arrays.stream(item.getMinorModuleKeys(itemStack)))).orElseGet(Stream::empty)).anyMatch(this.moduleSlot::equals) : true;
    }

    @Override
    public boolean isApplicableForSlot(String slot, ItemStack targetStack) {
        return this.moduleSlot != null ? this.moduleSlot.equals(slot) : Arrays.stream(this.definition.slots).anyMatch(s -> s.equals(slot));
    }

    @Override
    public boolean matchesRequirements(CraftingContext context) {
        if (!this.definition.hone || ConfigHandler.moduleProgression.get() && IModularItem.isHoneable(context.targetStack)) {
            return this.definition.materialRevealSlot <= -1 || context.player != null && this.hasAnyMaterial(context.player) ? this.definition.requirement == null || this.definition.requirement.test(context) : false;
        } else {
            return false;
        }
    }

    private boolean hasAnyMaterial(Player player) {
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 4; y++) {
                if (this.getOutcomeFromMaterial(player.getInventory().getItem(y * 9 + x), this.definition.materialRevealSlot).isPresent()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isVisibleForPlayer(Player player, @Nullable WorkbenchTile tile, ItemStack targetStack) {
        return true;
    }

    @Override
    public boolean isHoning() {
        return this.definition.hone;
    }

    @Override
    public Map<ToolAction, Integer> getRequiredToolLevels(ItemStack targetStack, ItemStack[] materials) {
        return this.definition.materialSlotCount > 0 ? (Map) IntStream.range(0, materials.length).mapToObj(index -> this.getOutcomeFromMaterial(materials[index], index)).filter(Optional::isPresent).map(Optional::get).flatMap(outcome -> outcome.requiredTools.getLevelMap().entrySet().stream()).collect(Collectors.toMap(Entry::getKey, Entry::getValue, Math::max)) : (Map) Arrays.stream(this.definition.outcomes).findFirst().map(outcome -> outcome.requiredTools.getLevelMap()).orElseGet(Collections::emptyMap);
    }

    @Override
    public int getRequiredToolLevel(ItemStack targetStack, ItemStack[] materials, ToolAction toolAction) {
        return this.definition.materialSlotCount > 0 ? (Integer) IntStream.range(0, materials.length).mapToObj(index -> this.getOutcomeFromMaterial(materials[index], index)).filter(Optional::isPresent).map(Optional::get).map(outcome -> outcome.requiredTools).filter(tools -> tools.contains(toolAction)).map(tools -> tools.getLevel(toolAction)).sorted().findFirst().orElse(0) : (Integer) Arrays.stream(this.definition.outcomes).findFirst().map(outcome -> outcome.requiredTools).filter(tools -> tools.contains(toolAction)).map(tools -> tools.getLevel(toolAction)).orElse(0);
    }

    @Override
    public ItemStack applyUpgrade(ItemStack itemStack, ItemStack[] materials, boolean consumeMaterials, String slot, Player player) {
        ItemStack upgradedStack = itemStack.copy();
        if (this.definition.materialSlotCount > 0) {
            for (int i = 0; i < materials.length; i++) {
                int index = i;
                this.getOutcomeFromMaterial(materials[index], index).ifPresent(outcomex -> {
                    this.applyOutcome(outcomex, upgradedStack, consumeMaterials, slot, player);
                    if (consumeMaterials) {
                        materials[index].shrink(outcomex.material.count);
                        this.triggerAdvancement(outcomex, player, itemStack, upgradedStack, slot);
                    }
                });
            }
        } else {
            for (OutcomeDefinition outcome : this.definition.outcomes) {
                this.applyOutcome(outcome, upgradedStack, consumeMaterials, slot, player);
                if (consumeMaterials) {
                    this.triggerAdvancement(outcome, player, itemStack, upgradedStack, slot);
                }
            }
        }
        return upgradedStack;
    }

    private void applyOutcome(OutcomeDefinition outcome, ItemStack upgradedStack, boolean consumeMaterials, String slot, Player player) {
        if (outcome.moduleKey != null) {
            ItemModule module = ItemUpgradeRegistry.instance.getModule(this.getModuleKey(outcome));
            ItemModule previousModule = this.removePreviousModule(upgradedStack, module.getSlot());
            module.addModule(upgradedStack, outcome.moduleVariant, player);
            outcome.improvements.forEach((key, value) -> ItemModuleMajor.addImprovement(upgradedStack, slot, key, value));
            if (previousModule != null && consumeMaterials) {
                previousModule.postRemove(upgradedStack, player);
            }
        } else {
            outcome.improvements.forEach((key, value) -> ItemModuleMajor.addImprovement(upgradedStack, slot, key, value));
        }
    }

    private void triggerAdvancement(OutcomeDefinition outcome, Player player, ItemStack itemStack, ItemStack upgradedStack, String slot) {
        if (player instanceof ServerPlayer) {
            if (outcome.moduleKey != null) {
                if (outcome.requiredTools.getValues().isEmpty()) {
                    ModuleCraftCriterion.trigger((ServerPlayer) player, itemStack, upgradedStack, this.getKey(), slot, outcome.moduleKey, outcome.moduleVariant, null, -1);
                } else {
                    outcome.requiredTools.getLevelMap().forEach((tool, toolLevel) -> ModuleCraftCriterion.trigger((ServerPlayer) player, itemStack, upgradedStack, this.getKey(), slot, outcome.moduleKey, outcome.moduleVariant, tool, toolLevel));
                }
            }
            outcome.improvements.forEach((improvement, level) -> {
                if (outcome.requiredTools.getValues().isEmpty()) {
                    ImprovementCraftCriterion.trigger((ServerPlayer) player, itemStack, upgradedStack, this.getKey(), slot, improvement, level, null, -1);
                } else {
                    outcome.requiredTools.getLevelMap().forEach((tool, toolLevel) -> ImprovementCraftCriterion.trigger((ServerPlayer) player, itemStack, upgradedStack, this.getKey(), slot, improvement, level, tool, toolLevel));
                }
            });
        }
    }

    @Override
    public boolean willReplace(ItemStack itemStack, ItemStack[] materials, String slot) {
        if (this.definition.materialSlotCount > 0) {
            for (int i = 0; i < materials.length; i++) {
                Optional<OutcomeDefinition> outcomeOptional = this.getOutcomeFromMaterial(materials[i], i);
                if (outcomeOptional.isPresent() && ((OutcomeDefinition) outcomeOptional.get()).moduleKey != null) {
                    return true;
                }
            }
        } else {
            for (OutcomeDefinition outcome : this.definition.outcomes) {
                if (outcome.moduleKey != null) {
                    return true;
                }
            }
        }
        return false;
    }

    protected ItemModule removePreviousModule(ItemStack itemStack, String slot) {
        IModularItem item = (IModularItem) itemStack.getItem();
        ItemModule previousModule = item.getModuleFromSlot(itemStack, slot);
        if (previousModule != null) {
            previousModule.removeModule(itemStack);
        }
        return previousModule;
    }

    @Override
    public int getExperienceCost(ItemStack targetStack, ItemStack[] materials, String slot) {
        int cost = 0;
        if (this.definition.materialSlotCount > 0) {
            for (int i = 0; i < materials.length; i++) {
                cost += this.getOutcomeFromMaterial(materials[i], i).map(outcome -> outcome.experienceCost).orElse(0);
            }
        } else {
            cost += Arrays.stream(this.definition.outcomes).mapToInt(outcome -> outcome.experienceCost).sum();
        }
        return cost;
    }

    @Override
    public SchematicType getType() {
        return this.definition.displayType;
    }

    @Override
    public SchematicRarity getRarity() {
        return this.definition.rarity;
    }

    @Override
    public GlyphData getGlyph() {
        return this.definition.glyph;
    }

    @Override
    public OutcomePreview[] getPreviews(ItemStack targetStack, String slot) {
        return (OutcomePreview[]) Arrays.stream(this.definition.outcomes).filter(outcome -> !outcome.hidden).map(outcome -> {
            ItemStack itemStack = targetStack.copy();
            String key = null;
            String name = "";
            String category = "misc";
            int level = -1;
            this.applyOutcome(outcome, itemStack, false, slot, null);
            GlyphData glyph;
            if (outcome.moduleKey != null) {
                VariantData variant = ItemUpgradeRegistry.instance.getModule(this.getModuleKey(outcome)).getVariantData(outcome.moduleVariant);
                key = outcome.moduleVariant;
                name = this.getVariantName(outcome, itemStack);
                glyph = variant.glyph;
                category = variant.category;
            } else if (outcome.improvements.size() == 1) {
                for (Entry<String, Integer> entry : outcome.improvements.entrySet()) {
                    key = (String) entry.getKey();
                    name = IModularItem.getImprovementName(key, (Integer) entry.getValue());
                    level = (Integer) entry.getValue();
                }
                glyph = this.definition.glyph;
            } else {
                if (outcome.improvements.isEmpty()) {
                    return null;
                }
                key = this.definition.key;
                glyph = this.definition.glyph;
            }
            return new OutcomePreview(outcome.moduleKey, key, name, category, level, glyph, itemStack, this.definition.displayType, outcome.requiredTools, outcome.material.getApplicableItemStacks());
        }).filter(Filter.distinct(preview -> preview.variantKey)).toArray(OutcomePreview[]::new);
    }

    private String getVariantName(OutcomeDefinition outcome, ItemStack itemStack) {
        return (String) Optional.ofNullable(this.getModuleKey(outcome)).map(ItemUpgradeRegistry.instance::getModule).map(module -> module.getName(itemStack)).orElse("");
    }
}