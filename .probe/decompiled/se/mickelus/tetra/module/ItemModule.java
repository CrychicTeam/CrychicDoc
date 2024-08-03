package se.mickelus.tetra.module;

import com.google.common.collect.Multimap;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ToolAction;
import org.apache.commons.lang3.StringUtils;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.ConfigHandler;
import se.mickelus.tetra.aspect.ItemAspect;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.module.data.AspectData;
import se.mickelus.tetra.module.data.EffectData;
import se.mickelus.tetra.module.data.ItemProperties;
import se.mickelus.tetra.module.data.ModuleModel;
import se.mickelus.tetra.module.data.TierData;
import se.mickelus.tetra.module.data.ToolData;
import se.mickelus.tetra.module.data.TweakData;
import se.mickelus.tetra.module.data.VariantData;
import se.mickelus.tetra.module.schematic.RepairDefinition;
import se.mickelus.tetra.properties.AttributeHelper;
import se.mickelus.tetra.properties.IToolProvider;

public abstract class ItemModule implements IToolProvider {

    public static final float repairLevelFactor = 10.0F;

    protected final String slotTagKey;

    protected final String moduleKey;

    protected final String variantTagKey;

    protected VariantData[] variantData = new VariantData[0];

    protected TweakData[] tweaks = new TweakData[0];

    protected Priority renderLayer = Priority.BASE;

    protected Priority namePriority = Priority.BASE;

    protected Priority prefixPriority = Priority.BASE;

    protected boolean perk = false;

    public ItemModule(String slotKey, String moduleKey) {
        this.slotTagKey = slotKey;
        this.moduleKey = moduleKey;
        this.variantTagKey = moduleKey + "_material";
    }

    public static String getName(String moduleKey, String variantKey) {
        if (I18n.exists("tetra.variant." + variantKey)) {
            return I18n.get("tetra.variant." + variantKey);
        } else {
            if (I18n.exists("tetra.module." + moduleKey + ".material_name")) {
                String variant = variantKey.substring(variantKey.indexOf(47) + 1);
                if (I18n.exists("tetra.material." + variant + ".prefix")) {
                    return StringUtils.capitalize(I18n.get("tetra.module." + moduleKey + ".material_name", I18n.get("tetra.material." + variant + ".prefix")).toLowerCase());
                }
            }
            return I18n.get("tetra.variant." + variantKey);
        }
    }

    public String getKey() {
        return this.moduleKey;
    }

    public String getUnlocalizedName() {
        return this.moduleKey;
    }

    public String getSlot() {
        return this.slotTagKey;
    }

    public void addModule(ItemStack targetStack, String variantKey, Player player) {
        CompoundTag tag = targetStack.getOrCreateTag();
        tag.putString(this.slotTagKey, this.moduleKey);
        tag.putString(this.variantTagKey, variantKey);
    }

    public final ItemStack[] removeModule(ItemStack targetStack) {
        return this.removeModule(targetStack, false);
    }

    public ItemStack[] removeModule(ItemStack targetStack, boolean upgrade) {
        CompoundTag tag = targetStack.getOrCreateTag();
        tag.remove(this.slotTagKey);
        tag.remove(this.variantTagKey);
        return new ItemStack[0];
    }

    public void postRemove(ItemStack targetStack, Player player) {
    }

    public VariantData[] getVariantData() {
        return this.variantData;
    }

    public VariantData getVariantData(ItemStack itemStack) {
        return (VariantData) Optional.ofNullable(itemStack.getTag()).map(tag -> tag.getString(this.variantTagKey)).map(key -> this.getVariantData(key)).orElseGet(this::getDefaultData);
    }

    public VariantData getVariantData(String variantKey) {
        return (VariantData) Arrays.stream(this.variantData).filter(moduleData -> moduleData.key.equals(variantKey)).findAny().orElseGet(this::getDefaultData);
    }

    public ItemProperties getProperties(ItemStack itemStack) {
        return (ItemProperties) Arrays.stream(this.getTweaks(itemStack)).map(tweak -> tweak.getProperties(this.getTweakStep(itemStack, tweak))).reduce(ItemProperties.merge(new ItemProperties(), this.getVariantData(itemStack)), ItemProperties::merge);
    }

    public VariantData getDefaultData() {
        return this.variantData.length > 0 ? this.variantData[0] : new VariantData();
    }

    public String getName(ItemStack itemStack) {
        String key = this.getVariantData(itemStack).key;
        return getName(this.getUnlocalizedName(), key);
    }

    public String getDescription(ItemStack itemStack) {
        String descriptionKey = "tetra.variant." + this.getVariantData(itemStack).key + ".description";
        return I18n.exists(descriptionKey) ? I18n.get(descriptionKey) : I18n.get("tetra.module." + this.getUnlocalizedName() + ".description");
    }

    public String getItemName(ItemStack itemStack) {
        String variantItemNameKey = "tetra.variant." + this.getVariantData(itemStack).key + ".item_name";
        if (I18n.exists(variantItemNameKey)) {
            return I18n.get(variantItemNameKey);
        } else {
            String moduleItemNameKey = "tetra.module." + this.getUnlocalizedName() + ".item_name";
            return I18n.exists(moduleItemNameKey) ? I18n.get(moduleItemNameKey) : null;
        }
    }

    public Priority getItemNamePriority(ItemStack itemStack) {
        return this.namePriority;
    }

    public String getItemPrefix(ItemStack itemStack) {
        String key = this.getVariantData(itemStack).key;
        String variantPrefixKey = "tetra.variant." + key + ".prefix";
        if (I18n.exists(variantPrefixKey)) {
            return I18n.get(variantPrefixKey);
        } else {
            String modulePrefixKey = "tetra.module." + this.getUnlocalizedName() + ".prefix";
            if (I18n.exists(modulePrefixKey)) {
                String prefix = I18n.get(modulePrefixKey);
                if (prefix.startsWith("Format error:")) {
                    String variant = key.substring(key.indexOf(47) + 1);
                    return StringUtils.capitalize(I18n.get(modulePrefixKey, I18n.get("tetra.material." + variant + ".prefix").toLowerCase()));
                } else {
                    return prefix;
                }
            } else {
                return null;
            }
        }
    }

    public Priority getItemPrefixPriority(ItemStack itemStack) {
        return this.prefixPriority;
    }

    public int getIntegrityGain(ItemStack itemStack) {
        return Math.max(this.getProperties(itemStack).integrity, 0);
    }

    public int getIntegrityCost(ItemStack itemStack) {
        return Math.max(this.getProperties(itemStack).integrityUsage, 0);
    }

    public int getMagicCapacity(ItemStack itemStack) {
        return this.getMagicCapacityGain(itemStack) - this.getMagicCapacityCost(itemStack);
    }

    public int getMagicCapacityGain(ItemStack itemStack) {
        int magicCapacity = this.getVariantData(itemStack).magicCapacity;
        if (magicCapacity > 0) {
            float stabilityMultiplier = (Float) CastOptional.cast(itemStack.getItem(), IModularItem.class).map(item -> item.getStabilityModifier(itemStack)).orElse(1.0F);
            return Math.round((float) magicCapacity * ConfigHandler.magicCapacityMultiplier.get().floatValue() * stabilityMultiplier);
        } else {
            return 0;
        }
    }

    public int getMagicCapacityCost(ItemStack itemStack) {
        int magicCapacity = this.getVariantData(itemStack).magicCapacity;
        return magicCapacity < 0 ? -magicCapacity : 0;
    }

    public float getDestabilizationChance(ItemStack itemStack, float probabilityMultiplier) {
        return this.getDestabilizationChance(-this.getMagicCapacity(itemStack), this.getMagicCapacityGain(itemStack), probabilityMultiplier);
    }

    public float getDestabilizationChance(int instability, int capacity, float probabilityMultiplier) {
        return capacity > 0 ? Math.max(probabilityMultiplier * (float) instability / (float) capacity, 0.0F) : 0.0F;
    }

    public int getDurability(ItemStack itemStack) {
        return this.getProperties(itemStack).durability;
    }

    public float getDurabilityMultiplier(ItemStack itemStack) {
        return this.getProperties(itemStack).durabilityMultiplier;
    }

    public Collection<RepairDefinition> getRepairDefinitions(ItemStack itemStack) {
        return RepairRegistry.instance.getDefinitions(this.getVariantData(itemStack).key);
    }

    public RepairDefinition getRepairDefinition(ItemStack itemStack, ItemStack materialStack) {
        return (RepairDefinition) RepairRegistry.instance.getDefinitions(this.getVariantData(itemStack).key).stream().filter(definition -> definition.material.isValid()).filter(definition -> definition.material.getPredicate().matches(materialStack)).findFirst().orElse(null);
    }

    public Collection<ToolAction> getRepairRequiredTools(ItemStack itemStack, ItemStack materialStack) {
        return (Collection<ToolAction>) Optional.ofNullable(this.getRepairDefinition(itemStack, materialStack)).map(definition -> definition.requiredTools).map(TierData::getValues).orElseGet(Collections::emptySet);
    }

    public Map<ToolAction, Integer> getRepairRequiredToolLevels(ItemStack itemStack, ItemStack materialStack) {
        return (Map<ToolAction, Integer>) Optional.ofNullable(this.getRepairDefinition(itemStack, materialStack)).map(definition -> definition.requiredTools).map(TierData::getLevelMap).orElseGet(Collections::emptyMap);
    }

    public int getRepairRequiredToolLevel(ItemStack itemStack, ItemStack materialStack, ToolAction tool) {
        return (Integer) Optional.ofNullable(this.getRepairDefinition(itemStack, materialStack)).map(definition -> definition.requiredTools).map(requiredTools -> requiredTools.getLevel(tool)).orElse(0);
    }

    public int getRepairExperienceCost(ItemStack itemStack, ItemStack materialStack) {
        float result = (float) ((Integer) Optional.ofNullable(this.getRepairDefinition(itemStack, materialStack)).map(definition -> definition.experienceCost).orElse(0)).intValue() + (Float) Optional.of(this.getDestabilizationChance(itemStack, 1.0F)).map(capacity -> capacity * 10.0F).orElse(0.0F);
        return Math.max(0, Mth.ceil(result));
    }

    public boolean isTweakable(ItemStack itemStack) {
        if (itemStack.hasTag()) {
            String variant = itemStack.getTag().getString(this.variantTagKey);
            return Arrays.stream(this.tweaks).anyMatch(data -> variant.equals(data.variant));
        } else {
            return false;
        }
    }

    public TweakData[] getTweaks(ItemStack itemStack) {
        if (itemStack.hasTag()) {
            String variant = itemStack.getTag().getString(this.variantTagKey);
            return (TweakData[]) Arrays.stream(this.tweaks).filter(tweak -> variant.equals(tweak.variant)).toArray(TweakData[]::new);
        } else {
            return new TweakData[0];
        }
    }

    public boolean hasTweak(ItemStack itemStack, String tweakKey) {
        return Arrays.stream(this.getTweaks(itemStack)).map(tweak -> tweak.key).anyMatch(tweakKey::equals);
    }

    public int getTweakStep(ItemStack itemStack, TweakData tweak) {
        return (Integer) Optional.ofNullable(itemStack.getTag()).map(tag -> tag.getInt(this.slotTagKey + "_tweak:" + tweak.key)).map(step -> Mth.clamp(step, -tweak.steps, tweak.steps)).orElse(0);
    }

    public void setTweakStep(ItemStack itemStack, String tweakKey, int step) {
        itemStack.getOrCreateTag().putInt(this.slotTagKey + "_tweak:" + tweakKey, step);
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(ItemStack itemStack) {
        return (Multimap<Attribute, AttributeModifier>) Arrays.stream(this.getTweaks(itemStack)).map(tweak -> tweak.getAttributeModifiers(this.getTweakStep(itemStack, tweak))).filter(Objects::nonNull).reduce(this.getVariantData(itemStack).attributes, AttributeHelper::merge);
    }

    public double getDamageModifier(ItemStack itemStack) {
        return (Double) Optional.ofNullable(this.getAttributeModifiers(itemStack)).map(modifiers -> modifiers.get(Attributes.ATTACK_DAMAGE)).map(AttributeHelper::getAdditionAmount).orElse(0.0);
    }

    public double getDamageMultiplierModifier(ItemStack itemStack) {
        return (Double) Optional.ofNullable(this.getAttributeModifiers(itemStack)).map(modifiers -> modifiers.get(Attributes.ATTACK_DAMAGE)).map(AttributeHelper::getMultiplyAmount).orElse(1.0);
    }

    public double getSpeedModifier(ItemStack itemStack) {
        return (Double) Optional.ofNullable(this.getAttributeModifiers(itemStack)).map(modifiers -> modifiers.get(Attributes.ATTACK_SPEED)).map(AttributeHelper::getAdditionAmount).orElse(0.0);
    }

    public double getSpeedMultiplierModifier(ItemStack itemStack) {
        return (Double) Optional.ofNullable(this.getAttributeModifiers(itemStack)).map(modifiers -> modifiers.get(Attributes.ATTACK_SPEED)).map(AttributeHelper::getMultiplyAmount).orElse(1.0);
    }

    public ModuleModel[] getModels(ItemStack itemStack) {
        return this.getVariantData(itemStack).models;
    }

    public Priority getRenderLayer() {
        return this.renderLayer;
    }

    public int getEffectLevel(ItemStack itemStack, ItemEffect effect) {
        return (Integer) Optional.ofNullable(this.getEffectData(itemStack)).map(data -> data.getLevel(effect)).orElse(0);
    }

    public float getEffectEfficiency(ItemStack itemStack, ItemEffect effect) {
        return (Float) Optional.ofNullable(this.getEffectData(itemStack)).map(data -> data.getEfficiency(effect)).orElse(0.0F);
    }

    public Collection<ItemEffect> getEffects(ItemStack itemStack) {
        return (Collection<ItemEffect>) Optional.ofNullable(this.getEffectData(itemStack)).map(TierData::getValues).orElseGet(Collections::emptySet);
    }

    public EffectData getEffectData(ItemStack itemStack) {
        return (EffectData) Arrays.stream(this.getTweaks(itemStack)).map(tweak -> tweak.getEffectData(this.getTweakStep(itemStack, tweak))).filter(Objects::nonNull).reduce(this.getVariantData(itemStack).effects, EffectData::merge);
    }

    @Override
    public boolean canProvideTools(ItemStack itemStack) {
        return true;
    }

    @Override
    public int getToolLevel(ItemStack itemStack, ToolAction tool) {
        return (Integer) Optional.ofNullable(this.getToolData(itemStack)).map(data -> data.getLevel(tool)).orElse(0);
    }

    @Override
    public float getToolEfficiency(ItemStack itemStack, ToolAction tool) {
        return (Float) Optional.ofNullable(this.getToolData(itemStack)).map(data -> data.getEfficiency(tool)).orElse(0.0F);
    }

    @Override
    public Set<ToolAction> getTools(ItemStack itemStack) {
        return (Set<ToolAction>) Optional.ofNullable(this.getToolData(itemStack)).map(TierData::getValues).orElseGet(Collections::emptySet);
    }

    @Override
    public Map<ToolAction, Integer> getToolLevels(ItemStack itemStack) {
        return (Map<ToolAction, Integer>) Optional.ofNullable(this.getToolData(itemStack)).map(TierData::getLevelMap).orElseGet(Collections::emptyMap);
    }

    @Override
    public ToolData getToolData(ItemStack itemStack) {
        return (ToolData) Arrays.stream(this.getTweaks(itemStack)).map(tweak -> tweak.getToolData(this.getTweakStep(itemStack, tweak))).filter(Objects::nonNull).reduce(this.getVariantData(itemStack).tools, ToolData::merge);
    }

    public AspectData getAspects(ItemStack itemStack) {
        return this.getVariantData(itemStack).aspects;
    }

    public boolean hasAspect(ItemStack itemStack, ItemAspect aspect) {
        return this.getAspects(itemStack).contains(aspect);
    }
}