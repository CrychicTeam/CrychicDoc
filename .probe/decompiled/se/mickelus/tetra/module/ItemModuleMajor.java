package se.mickelus.tetra.module;

import com.google.common.collect.Multimap;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.ConfigHandler;
import se.mickelus.tetra.TetraMod;
import se.mickelus.tetra.aspect.TetraEnchantmentHelper;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.items.modular.ItemColors;
import se.mickelus.tetra.module.data.AspectData;
import se.mickelus.tetra.module.data.EffectData;
import se.mickelus.tetra.module.data.ImprovementData;
import se.mickelus.tetra.module.data.ItemProperties;
import se.mickelus.tetra.module.data.ModuleModel;
import se.mickelus.tetra.module.data.TierData;
import se.mickelus.tetra.module.data.ToolData;
import se.mickelus.tetra.module.data.TweakData;
import se.mickelus.tetra.module.improvement.SettlePacket;
import se.mickelus.tetra.properties.AttributeHelper;

public abstract class ItemModuleMajor extends ItemModule {

    public static final String settleImprovement = "settled";

    public static final String arrestedImprovement = "arrested";

    protected ImprovementData[] improvements = new ImprovementData[0];

    protected int settleMax = 0;

    private String settleProgressKey = "/settle_progress";

    public ItemModuleMajor(String slotKey, String moduleKey) {
        super(slotKey, moduleKey);
        this.settleProgressKey = this.getSlot() + this.settleProgressKey;
    }

    public static void addImprovement(ItemStack itemStack, String slot, String improvement, int level) {
        IModularItem item = (IModularItem) itemStack.getItem();
        CastOptional.cast(item.getModuleFromSlot(itemStack, slot), ItemModuleMajor.class).filter(module -> module.acceptsImprovementLevel(improvement, level)).ifPresent(module -> module.addImprovement(itemStack, improvement, level));
    }

    public static void removeImprovement(ItemStack itemStack, String slot, String improvement) {
        if (itemStack.hasTag()) {
            itemStack.getTag().remove(slot + ":" + improvement);
        }
    }

    public void tickProgression(LivingEntity entity, ItemStack itemStack, int multiplier) {
        int settleMaxCount = this.getSettleMaxCount(itemStack);
        if (settleMaxCount != 0) {
            CompoundTag tag = itemStack.getOrCreateTag();
            int settleLevel = this.getImprovementLevel(itemStack, "settled");
            if (settleLevel < settleMaxCount && this.getImprovementLevel(itemStack, "arrested") == -1) {
                int settleProgress = this.getSettleProgress(itemStack);
                settleProgress -= multiplier;
                tag.putInt(this.settleProgressKey, settleProgress);
                if (settleProgress <= 0) {
                    this.addImprovement(itemStack, "settled", settleLevel == -1 ? 1 : settleLevel + 1);
                    tag.remove(this.settleProgressKey);
                    if (entity instanceof ServerPlayer) {
                        TetraMod.packetHandler.sendTo(new SettlePacket(itemStack, this.getSlot()), (ServerPlayer) entity);
                        IModularItem.updateIdentifier(tag);
                    }
                }
            }
        }
    }

    public int getSettleProgress(ItemStack itemStack) {
        return (Integer) Optional.ofNullable(itemStack.getTag()).filter(tag -> tag.contains(this.settleProgressKey)).map(tag -> tag.getInt(this.settleProgressKey)).orElseGet(() -> this.getSettleLimit(itemStack));
    }

    public int getSettleLimit(ItemStack itemStack) {
        return (int) (((double) ConfigHandler.settleLimitBase.get().intValue() + (double) this.getDurability(itemStack) * ConfigHandler.settleLimitDurabilityMultiplier.get()) * Math.max((double) this.getImprovementLevel(itemStack, "settled") * ConfigHandler.settleLimitLevelMultiplier.get(), 1.0));
    }

    public int getSettleMaxCount(ItemStack itemStack) {
        if (this.settleMax == 0) {
            return 0;
        } else {
            int integrity = this.getVariantData(itemStack).integrity;
            if (integrity <= -4 || integrity >= 6) {
                return this.settleMax;
            } else {
                return integrity != 0 ? 1 : 0;
            }
        }
    }

    protected void clearProgression(ItemStack itemStack) {
        if (itemStack.hasTag()) {
            itemStack.getTag().remove(String.format(this.settleProgressKey, this.getSlot()));
        }
    }

    public int getImprovementLevel(ItemStack itemStack, String improvementKey) {
        return (Integer) Optional.ofNullable(itemStack.getTag()).filter(tag -> tag.contains(this.slotTagKey + ":" + improvementKey)).map(tag -> tag.getInt(this.slotTagKey + ":" + improvementKey)).orElse(-1);
    }

    public ImprovementData getImprovement(ItemStack itemStack, String improvementKey) {
        if (itemStack.hasTag()) {
            CompoundTag tag = itemStack.getTag();
            return (ImprovementData) Arrays.stream(this.improvements).filter(improvement -> improvementKey.equals(improvement.key)).filter(improvement -> tag.contains(this.slotTagKey + ":" + improvement.key)).filter(improvement -> improvement.level == tag.getInt(this.slotTagKey + ":" + improvement.key)).findAny().orElse(null);
        } else {
            return null;
        }
    }

    public ImprovementData[] getImprovements(ItemStack itemStack) {
        if (itemStack.hasTag()) {
            CompoundTag tag = itemStack.getTag();
            return (ImprovementData[]) Arrays.stream(this.improvements).filter(improvement -> tag.contains(this.slotTagKey + ":" + improvement.key)).filter(improvement -> improvement.level == tag.getInt(this.slotTagKey + ":" + improvement.key)).toArray(ImprovementData[]::new);
        } else {
            return new ImprovementData[0];
        }
    }

    public boolean acceptsImprovement(String improvementKey) {
        return Arrays.stream(this.improvements).map(improvement -> improvement.key).anyMatch(improvementKey::equals);
    }

    public boolean acceptsImprovementLevel(String improvementKey, int level) {
        return Arrays.stream(this.improvements).filter(improvement -> improvementKey.equals(improvement.key)).anyMatch(improvement -> level == improvement.level);
    }

    public void addImprovement(ItemStack itemStack, String improvementKey, int level) {
        this.removeCollidingImprovements(itemStack, improvementKey, level);
        itemStack.getOrCreateTag().putInt(this.slotTagKey + ":" + improvementKey, level);
    }

    public void removeCollidingImprovements(ItemStack itemStack, String improvementKey, int level) {
        Arrays.stream(this.improvements).filter(improvement -> improvementKey.equals(improvement.key)).filter(improvement -> level == improvement.level).filter(improvement -> improvement.group != null).map(improvement -> improvement.group).findFirst().ifPresent(group -> Arrays.stream(this.getImprovements(itemStack)).filter(improvement -> group.equals(improvement.group)).forEach(improvement -> removeImprovement(itemStack, this.slotTagKey, improvement.key)));
    }

    public void removeImprovement(ItemStack itemStack, String improvement) {
        removeImprovement(itemStack, this.slotTagKey, improvement);
    }

    public void removeEnchantments(ItemStack itemStack) {
        TetraEnchantmentHelper.removeEnchantments(itemStack, this.getSlot());
    }

    public boolean acceptsEnchantment(ItemStack itemStack, Enchantment enchantment, boolean fromTable) {
        return Optional.ofNullable(this.getAspects(itemStack)).map(TierData::getLevelMap).filter(aspects -> TetraEnchantmentHelper.isApplicableForAspects(enchantment, fromTable, aspects)).isPresent();
    }

    public EnchantmentCategory[] getApplicableEnchantmentCategories(ItemStack itemStack, boolean fromTable) {
        int requiredLevel = fromTable ? 2 : 1;
        return (EnchantmentCategory[]) ((Stream) Optional.ofNullable(this.getAspects(itemStack)).map(TierData::getLevelMap).map(Map::entrySet).map(Collection::stream).orElseGet(Stream::empty)).filter(entry -> (Integer) entry.getValue() >= requiredLevel).map(Entry::getKey).map(TetraEnchantmentHelper::getEnchantmentCategories).flatMap(Arrays::stream).toArray(EnchantmentCategory[]::new);
    }

    public Set<String> getEnchantmentKeys(ItemStack itemStack) {
        CompoundTag mappings = itemStack.getTagElement("EnchantmentMapping");
        return mappings != null ? (Set) mappings.getAllKeys().stream().filter(key -> this.getSlot().equals(mappings.get(key).getAsString())).collect(Collectors.toSet()) : Collections.emptySet();
    }

    public Map<String, Integer> getEnchantmentsPrimitive(ItemStack itemStack) {
        CompoundTag mappings = itemStack.getTagElement("EnchantmentMapping");
        return itemStack.hasTag() && mappings != null ? (Map) itemStack.getTag().getList("Enchantments", 10).stream().map(tag -> (CompoundTag) tag).filter(tag -> this.getSlot().equals(mappings.getString(tag.getString("id")))).map(TetraEnchantmentHelper::getEnchantmentPrimitive).filter(Objects::nonNull).collect(Collectors.toMap(Pair::getLeft, Pair::getRight)) : Collections.emptyMap();
    }

    public Map<Enchantment, Integer> getEnchantments(ItemStack itemStack) {
        CompoundTag mappings = itemStack.getTagElement("EnchantmentMapping");
        return itemStack.hasTag() && mappings != null ? (Map) itemStack.getTag().getList("Enchantments", 10).stream().map(tag -> (CompoundTag) tag).filter(tag -> this.getSlot().equals(mappings.getString(tag.getString("id")))).map(TetraEnchantmentHelper::getEnchantment).filter(Objects::nonNull).collect(Collectors.toMap(Pair::getLeft, Pair::getRight)) : Collections.emptyMap();
    }

    public int getEnchantmentMagicCapacityCost(ItemStack itemStack) {
        return -this.getEnchantments(itemStack).entrySet().stream().mapToInt(entry -> TetraEnchantmentHelper.getEnchantmentCapacityCost((Enchantment) entry.getKey(), (Integer) entry.getValue())).sum();
    }

    @Override
    public boolean isTweakable(ItemStack itemStack) {
        String[] improvementKeys = (String[]) Arrays.stream(this.getImprovements(itemStack)).map(improvement -> improvement.key).toArray(String[]::new);
        return Arrays.stream(this.tweaks).anyMatch(tweak -> ArrayUtils.contains(improvementKeys, tweak.improvement)) || super.isTweakable(itemStack);
    }

    @Override
    public TweakData[] getTweaks(ItemStack itemStack) {
        if (itemStack.hasTag()) {
            String variant = itemStack.getTag().getString(this.variantTagKey);
            String[] improvementKeys = (String[]) Arrays.stream(this.getImprovements(itemStack)).map(improvement -> improvement.key).toArray(String[]::new);
            return (TweakData[]) Arrays.stream(this.tweaks).filter(tweak -> variant.equals(tweak.variant) || ArrayUtils.contains(improvementKeys, tweak.improvement)).toArray(TweakData[]::new);
        } else {
            return new TweakData[0];
        }
    }

    @Override
    public ItemStack[] removeModule(ItemStack targetStack, boolean upgrade) {
        ItemStack[] salvage = super.removeModule(targetStack, upgrade);
        if (!upgrade && targetStack.hasTag()) {
            CompoundTag tag = targetStack.getTag();
            Arrays.stream(this.improvements).map(improvement -> this.slotTagKey + ":" + improvement.key).forEach(tag::m_128473_);
            this.clearProgression(targetStack);
        }
        return salvage;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(ItemStack itemStack) {
        return (Multimap<Attribute, AttributeModifier>) Arrays.stream(this.getImprovements(itemStack)).map(improvement -> improvement.attributes).filter(Objects::nonNull).reduce(super.getAttributeModifiers(itemStack), AttributeHelper::merge);
    }

    @Override
    public ItemProperties getProperties(ItemStack itemStack) {
        return (ItemProperties) Arrays.stream(this.getImprovements(itemStack)).reduce(super.getProperties(itemStack), ItemProperties::merge, ItemProperties::merge);
    }

    @Override
    public EffectData getEffectData(ItemStack itemStack) {
        return (EffectData) Arrays.stream(this.getImprovements(itemStack)).map(improvement -> improvement.effects).filter(Objects::nonNull).reduce(super.getEffectData(itemStack), EffectData::merge);
    }

    @Override
    public ToolData getToolData(ItemStack itemStack) {
        return (ToolData) Arrays.stream(this.getImprovements(itemStack)).map(improvement -> improvement.tools).filter(Objects::nonNull).reduce(super.getToolData(itemStack), ToolData::merge);
    }

    @Override
    public AspectData getAspects(ItemStack itemStack) {
        return (AspectData) Arrays.stream(this.getImprovements(itemStack)).map(improvement -> improvement.aspects).filter(Objects::nonNull).reduce(super.getAspects(itemStack), AspectData::merge);
    }

    @Override
    public int getMagicCapacityGain(ItemStack itemStack) {
        return super.getMagicCapacityGain(itemStack) + this.getImprovementMagicCapacityGain(itemStack);
    }

    @Override
    public int getMagicCapacityCost(ItemStack itemStack) {
        return super.getMagicCapacityCost(itemStack) + this.getImprovementMagicCapacityCost(itemStack) + this.getEnchantmentMagicCapacityCost(itemStack);
    }

    public int getImprovementMagicCapacityGain(ItemStack itemStack) {
        return Math.round(ConfigHandler.magicCapacityMultiplier.get().floatValue() * (Float) CastOptional.cast(itemStack.getItem(), IModularItem.class).map(item -> item.getStabilityModifier(itemStack)).orElse(1.0F) * (float) Arrays.stream(this.getImprovements(itemStack)).mapToInt(improvement -> improvement.magicCapacity).filter(magicCapacity -> magicCapacity > 0).sum());
    }

    public int getImprovementMagicCapacityCost(ItemStack itemStack) {
        return -Arrays.stream(this.getImprovements(itemStack)).mapToInt(improvement -> improvement.magicCapacity).filter(integrity -> integrity < 0).sum();
    }

    protected ModuleModel[] getImprovementModels(ItemStack itemStack, int tint) {
        return (ModuleModel[]) Arrays.stream(this.getImprovements(itemStack)).filter(improvement -> improvement.models.length > 0).flatMap(improvement -> Arrays.stream(improvement.models)).map(model -> {
            if (ItemColors.inherit == model.tint) {
                ModuleModel copy = model.copy();
                copy.tint = tint;
                return copy;
            } else {
                return model;
            }
        }).toArray(ModuleModel[]::new);
    }

    @Override
    public ModuleModel[] getModels(ItemStack itemStack) {
        ModuleModel[] models = super.getModels(itemStack);
        return (ModuleModel[]) ArrayUtils.addAll(models, this.getImprovementModels(itemStack, models.length > 0 ? models[0].overlayTint : 16777215));
    }
}