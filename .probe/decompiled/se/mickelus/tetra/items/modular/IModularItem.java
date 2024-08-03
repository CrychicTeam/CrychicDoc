package se.mickelus.tetra.items.modular;

import com.google.common.cache.Cache;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.mojang.datafixers.util.Pair;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.DigDurabilityEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.forgespi.Environment;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.ConfigHandler;
import se.mickelus.tetra.TetraMod;
import se.mickelus.tetra.Tooltips;
import se.mickelus.tetra.effect.ApplyUsageEffectsEvent;
import se.mickelus.tetra.effect.EnderReverbEffect;
import se.mickelus.tetra.effect.FierySelfEffect;
import se.mickelus.tetra.effect.HauntedEffect;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.GuiModuleOffsets;
import se.mickelus.tetra.module.ItemModule;
import se.mickelus.tetra.module.ItemModuleMajor;
import se.mickelus.tetra.module.ItemUpgradeRegistry;
import se.mickelus.tetra.module.Priority;
import se.mickelus.tetra.module.data.EffectData;
import se.mickelus.tetra.module.data.ImprovementData;
import se.mickelus.tetra.module.data.ItemProperties;
import se.mickelus.tetra.module.data.ModuleModel;
import se.mickelus.tetra.module.data.SynergyData;
import se.mickelus.tetra.module.improvement.DestabilizationEffect;
import se.mickelus.tetra.module.improvement.HonePacket;
import se.mickelus.tetra.module.schematic.RepairDefinition;
import se.mickelus.tetra.properties.AttributeHelper;

public interface IModularItem {

    Logger logger = LogManager.getLogger();

    GuiModuleOffsets[] defaultMajorOffsets = new GuiModuleOffsets[] { new GuiModuleOffsets(), new GuiModuleOffsets(4, 0), new GuiModuleOffsets(4, 0, 4, 18), new GuiModuleOffsets(4, 0, 4, 18, -4, 0), new GuiModuleOffsets(4, 0, 4, 18, -4, 0, -4, 18) };

    GuiModuleOffsets[] defaultMinorOffsets = new GuiModuleOffsets[] { new GuiModuleOffsets(), new GuiModuleOffsets(-21, 12), new GuiModuleOffsets(-18, 5, -18, 18), new GuiModuleOffsets(-12, -1, -21, 12, -12, 25) };

    String identifierKey = "id";

    String repairCountKey = "repairCount";

    String cooledStrengthKey = "cooledStrength";

    String honeProgressKey = "honing_progress";

    String honeAvailableKey = "honing_available";

    String honeCountKey = "honing_count";

    static void updateIdentifier(ItemStack itemStack) {
        updateIdentifier(itemStack.getOrCreateTag());
    }

    static void updateIdentifier(CompoundTag nbt) {
        nbt.putString("id", UUID.randomUUID().toString());
    }

    static void putModuleInSlot(ItemStack itemStack, String slot, String module, String moduleVariantKey, String moduleVariant) {
        CompoundTag tag = itemStack.getOrCreateTag();
        tag.putString(slot, module);
        tag.putString(moduleVariantKey, moduleVariant);
    }

    static void putModuleInSlot(ItemStack itemStack, String slot, String module, String moduleVariant) {
        CompoundTag tag = itemStack.getOrCreateTag();
        tag.putString(slot, module);
        tag.putString(module + "_material", moduleVariant);
    }

    static int getIntegrityGain(ItemStack itemStack) {
        return (Integer) CastOptional.cast(itemStack.getItem(), IModularItem.class).map(item -> item.getPropertiesCached(itemStack)).map(properties -> properties.integrity).orElse(0);
    }

    static int getIntegrityCost(ItemStack itemStack) {
        return (Integer) CastOptional.cast(itemStack.getItem(), IModularItem.class).map(item -> item.getPropertiesCached(itemStack)).map(properties -> properties.integrityUsage).orElse(0);
    }

    static boolean isHoneable(ItemStack itemStack) {
        return (Boolean) Optional.ofNullable(itemStack.getTag()).map(tag -> tag.contains("honing_available")).orElse(false);
    }

    static int getHoningSeed(ItemStack itemStack) {
        return (Integer) Optional.ofNullable(itemStack.getTag()).map(tag -> tag.getInt("honing_count")).orElse(0);
    }

    static void removeHoneable(ItemStack itemStack) {
        CompoundTag tag = itemStack.getTag();
        if (tag != null) {
            tag.remove("honing_available");
            tag.remove("honing_progress");
            tag.putInt("honing_count", tag.getInt("honing_count") + 1);
        }
    }

    static String getImprovementName(String key, int level) {
        String name = null;
        if (I18n.exists("tetra.improvement." + key + ".name")) {
            name = I18n.get("tetra.improvement." + key + ".name");
        } else {
            int lastSlash = key.lastIndexOf("/");
            if (lastSlash != -1) {
                String templateKey = "tetra.improvement." + key.substring(0, lastSlash) + ".name";
                if (I18n.exists(templateKey)) {
                    String materialKey = "tetra.material." + key.substring(lastSlash + 1) + ".prefix";
                    if (I18n.exists(materialKey)) {
                        name = StringUtils.capitalize(I18n.get(templateKey, I18n.get(materialKey).toLowerCase()));
                    }
                }
            }
            if (name == null) {
                name = "tetra.improvement." + key + ".name";
            }
        }
        if (level > 0) {
            name = name + " " + I18n.get("enchantment.level." + level);
        }
        return name;
    }

    static String getImprovementDescription(String key) {
        if (I18n.exists("tetra.improvement." + key + ".description")) {
            return I18n.get("tetra.improvement." + key + ".description");
        } else {
            int lastSlash = key.lastIndexOf("/");
            if (lastSlash != -1) {
                String splitKey = "tetra.improvement." + key.substring(0, lastSlash) + ".description";
                if (I18n.exists(splitKey)) {
                    return I18n.get(splitKey);
                }
            }
            return "tetra.improvement." + key + ".description";
        }
    }

    static ItemStack removeAllEnchantments(ItemStack itemStack) {
        itemStack.removeTagKey("Enchantments");
        itemStack.removeTagKey("StoredEnchantments");
        Arrays.stream(((IModularItem) itemStack.getItem()).getMajorModules(itemStack)).filter(Objects::nonNull).forEach(module -> module.removeEnchantments(itemStack));
        updateIdentifier(itemStack);
        return itemStack;
    }

    Item getItem();

    default ItemStack getDefaultStack() {
        return new ItemStack(this.getItem());
    }

    @Nullable
    default String getIdentifier(ItemStack itemStack) {
        return itemStack.hasTag() ? itemStack.getTag().getString("id") : null;
    }

    default String getDataCacheKey(ItemStack itemStack) {
        return (String) Optional.ofNullable(this.getIdentifier(itemStack)).filter(id -> !id.isEmpty()).orElseGet(() -> itemStack.hasTag() ? itemStack.getTag().toString() : "INVALID-" + this.getItem().toString());
    }

    default String getModelCacheKey(ItemStack itemStack, LivingEntity entity) {
        return (String) Optional.ofNullable(this.getIdentifier(itemStack)).filter(id -> !id.isEmpty()).orElseGet(() -> itemStack.hasTag() ? itemStack.getTag().toString() : "INVALID-" + this.getItem().toString());
    }

    void clearCaches();

    String[] getMajorModuleKeys(ItemStack var1);

    String[] getMinorModuleKeys(ItemStack var1);

    String[] getRequiredModules(ItemStack var1);

    default boolean isModuleRequired(ItemStack itemStack, String moduleSlot) {
        return ArrayUtils.contains(this.getRequiredModules(itemStack), moduleSlot);
    }

    default Collection<ItemModule> getAllModules(ItemStack stack) {
        CompoundTag stackTag = stack.getTag();
        return (Collection<ItemModule>) (stackTag != null ? (Collection) Stream.concat(Arrays.stream(this.getMajorModuleKeys(stack)), Arrays.stream(this.getMinorModuleKeys(stack))).map(stackTag::m_128461_).map(ItemUpgradeRegistry.instance::getModule).filter(Objects::nonNull).collect(Collectors.toList()) : Collections.emptyList());
    }

    default ItemModuleMajor[] getMajorModules(ItemStack itemStack) {
        String[] majorModuleKeys = this.getMajorModuleKeys(itemStack);
        ItemModuleMajor[] modules = new ItemModuleMajor[majorModuleKeys.length];
        CompoundTag tag = itemStack.getTag();
        if (tag != null) {
            for (int i = 0; i < majorModuleKeys.length; i++) {
                String moduleName = tag.getString(majorModuleKeys[i]);
                ItemModule module = ItemUpgradeRegistry.instance.getModule(moduleName);
                if (module instanceof ItemModuleMajor) {
                    modules[i] = (ItemModuleMajor) module;
                }
            }
        }
        return modules;
    }

    default ItemModule[] getMinorModules(ItemStack itemStack) {
        String[] minorModuleKeys = this.getMinorModuleKeys(itemStack);
        ItemModule[] modules = new ItemModule[minorModuleKeys.length];
        CompoundTag tag = itemStack.getTag();
        if (tag != null) {
            for (int i = 0; i < minorModuleKeys.length; i++) {
                String moduleName = tag.getString(minorModuleKeys[i]);
                ItemModule module = ItemUpgradeRegistry.instance.getModule(moduleName);
                modules[i] = module;
            }
        }
        return modules;
    }

    default int getNumMajorModules(ItemStack itemStack) {
        return this.getMajorModuleKeys(itemStack).length;
    }

    default String[] getMajorModuleNames(ItemStack itemStack) {
        return (String[]) Arrays.stream(this.getMajorModuleKeys(itemStack)).map(key -> I18n.get("tetra.slot." + key)).toArray(String[]::new);
    }

    default int getNumMinorModules(ItemStack itemStack) {
        return this.getMinorModuleKeys(itemStack).length;
    }

    default String[] getMinorModuleNames(ItemStack itemStack) {
        return (String[]) Arrays.stream(this.getMinorModuleKeys(itemStack)).map(key -> I18n.get("tetra.slot." + key)).toArray(String[]::new);
    }

    default boolean hasModule(ItemStack itemStack, ItemModule module) {
        return this.getAllModules(itemStack).stream().anyMatch(module::equals);
    }

    default ItemModule getModuleFromSlot(ItemStack itemStack, String slot) {
        return (ItemModule) Optional.ofNullable(itemStack.getTag()).map(tag -> tag.getString(slot)).map(ItemUpgradeRegistry.instance::getModule).orElse(null);
    }

    default void tickProgression(LivingEntity entity, ItemStack itemStack, int multiplier) {
        if (ConfigHandler.moduleProgression.get()) {
            this.tickHoningProgression(entity, itemStack, multiplier);
            for (ItemModuleMajor module : this.getMajorModules(itemStack)) {
                module.tickProgression(entity, itemStack, multiplier);
            }
        }
    }

    default void tickHoningProgression(LivingEntity entity, ItemStack itemStack, int multiplier) {
        if (ConfigHandler.moduleProgression.get() && this.canGainHoneProgress(itemStack)) {
            CompoundTag tag = itemStack.getOrCreateTag();
            if (!isHoneable(itemStack)) {
                int honingProgress;
                if (tag.contains("honing_progress")) {
                    honingProgress = tag.getInt("honing_progress");
                } else {
                    honingProgress = this.getHoningLimit(itemStack);
                }
                honingProgress -= multiplier;
                tag.putInt("honing_progress", honingProgress);
                if (honingProgress <= 0 && !isHoneable(itemStack)) {
                    tag.putBoolean("honing_available", true);
                    if (entity instanceof ServerPlayer) {
                        TetraMod.packetHandler.sendTo(new HonePacket(itemStack), (ServerPlayer) entity);
                    }
                }
            }
        }
    }

    default int getHoningProgress(ItemStack itemStack) {
        return (Integer) Optional.ofNullable(itemStack.getTag()).filter(tag -> tag.contains("honing_progress")).map(tag -> tag.getInt("honing_progress")).orElseGet(() -> this.getHoningLimit(itemStack));
    }

    default void setHoningProgress(ItemStack itemStack, int progress) {
        itemStack.getOrCreateTag().putInt("honing_progress", progress);
        if (progress <= 0) {
            itemStack.getOrCreateTag().putBoolean("honing_available", true);
        } else {
            itemStack.getOrCreateTag().remove("honing_available");
        }
    }

    default int getHoningLimit(ItemStack itemStack) {
        float workableFactor = (100.0F - (float) this.getEffectLevel(itemStack, ItemEffect.workable)) / 100.0F;
        return (int) Math.max((float) (this.getHoneBase(itemStack) + this.getHoneIntegrityMultiplier(itemStack) * getIntegrityCost(itemStack)) * workableFactor, 1.0F);
    }

    int getHoneBase(ItemStack var1);

    int getHoneIntegrityMultiplier(ItemStack var1);

    default int getHoningIntegrityPenalty(ItemStack itemStack) {
        return this.getHoneIntegrityMultiplier(itemStack) * getIntegrityCost(itemStack);
    }

    default int getHonedCount(ItemStack itemStack) {
        return (Integer) Optional.ofNullable(itemStack.getTag()).map(tag -> tag.getInt("honing_count")).orElse(0);
    }

    boolean canGainHoneProgress(ItemStack var1);

    default void applyUsageEffects(LivingEntity entity, ItemStack itemStack, double multiplier) {
        ApplyUsageEffectsEvent event = new ApplyUsageEffectsEvent(entity, itemStack, multiplier);
        MinecraftForge.EVENT_BUS.post(event);
        this.applyPositiveUsageEffects(entity, itemStack, event.getPositiveMultiplier());
        this.applyNegativeUsageEffects(entity, itemStack, event.getNegativeMultiplier());
    }

    default void applyPositiveUsageEffects(LivingEntity entity, ItemStack itemStack, double multiplier) {
        this.tickProgression(entity, itemStack, (int) multiplier);
    }

    default void applyNegativeUsageEffects(LivingEntity entity, ItemStack itemStack, double multiplier) {
        HauntedEffect.perform(entity, itemStack, multiplier);
        FierySelfEffect.perform(entity, itemStack, multiplier);
        EnderReverbEffect.perform(entity, itemStack, multiplier);
    }

    default void applyDamage(int amount, ItemStack itemStack, LivingEntity responsibleEntity) {
        int damage = itemStack.getDamageValue();
        int maxDamage = itemStack.getMaxDamage();
        if (!this.isBroken(damage, maxDamage)) {
            int reducedAmount = this.getReducedDamage(amount, itemStack, responsibleEntity);
            itemStack.hurtAndBreak(reducedAmount, responsibleEntity, breaker -> breaker.broadcastBreakEvent(breaker.getUsedItemHand()));
            if (this.isBroken(damage + reducedAmount, maxDamage) && !responsibleEntity.m_9236_().isClientSide) {
                responsibleEntity.broadcastBreakEvent(responsibleEntity.getUsedItemHand());
                responsibleEntity.m_5496_(SoundEvents.SHIELD_BREAK, 1.0F, 1.0F);
            }
        }
    }

    default int getReducedDamage(int amount, ItemStack itemStack, LivingEntity responsibleEntity) {
        if (amount <= 0) {
            return amount;
        } else {
            int level = this.getEffectLevel(itemStack, ItemEffect.unbreaking);
            int reduction = 0;
            if (level > 0) {
                for (int i = 0; i < amount; i++) {
                    if (DigDurabilityEnchantment.shouldIgnoreDurabilityDrop(itemStack, level, responsibleEntity.m_9236_().random)) {
                        reduction++;
                    }
                }
            }
            return amount - reduction;
        }
    }

    default boolean isBroken(ItemStack itemStack) {
        return this.isBroken(itemStack.getDamageValue(), itemStack.getMaxDamage());
    }

    default boolean isBroken(int damage, int maxDamage) {
        return maxDamage != 0 && damage >= maxDamage - 1;
    }

    @OnlyIn(Dist.CLIENT)
    default List<Component> getTooltip(ItemStack itemStack, @Nullable Level world, TooltipFlag advanced) {
        List<Component> tooltip = Lists.newArrayList();
        if (this.isBroken(itemStack)) {
            tooltip.add(Component.translatable("item.tetra.modular.broken").withStyle(ChatFormatting.DARK_RED, ChatFormatting.ITALIC));
        }
        if (Screen.hasShiftDown()) {
            tooltip.add(Tooltips.expanded);
            Arrays.stream(this.getMajorModules(itemStack)).filter(Objects::nonNull).forEach(module -> {
                tooltip.add(Component.literal("» ").withStyle(ChatFormatting.DARK_GRAY).append(Component.literal(module.getName(itemStack)).withStyle(ChatFormatting.GRAY)));
                module.getEnchantments(itemStack).entrySet().stream().map(entry -> ((Enchantment) entry.getKey()).getFullname((Integer) entry.getValue())).map(text -> Component.literal("  - " + text.getString())).map(text -> text.withStyle(ChatFormatting.DARK_GRAY)).forEach(tooltip::add);
                Arrays.stream(module.getImprovements(itemStack)).map(improvement -> "  - " + this.getImprovementTooltip(improvement.key, improvement.level, true)).map(Component::m_237113_).map(textComponent -> textComponent.withStyle(ChatFormatting.DARK_GRAY)).forEach(tooltip::add);
            });
            Arrays.stream(this.getMinorModules(itemStack)).filter(Objects::nonNull).map(module -> Component.literal(" * ").withStyle(ChatFormatting.DARK_GRAY).append(Component.literal(module.getName(itemStack)).withStyle(ChatFormatting.GRAY))).forEach(tooltip::add);
            if (ConfigHandler.moduleProgression.get() && this.canGainHoneProgress(itemStack)) {
                if (isHoneable(itemStack)) {
                    tooltip.add(Component.literal(" > ").withStyle(ChatFormatting.AQUA).append(Component.translatable("tetra.hone.available").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY))));
                } else {
                    int progress = this.getHoningProgress(itemStack);
                    int base = this.getHoningLimit(itemStack);
                    String percentage = String.format("%.0f", 100.0F * (float) (base - progress) / (float) base);
                    tooltip.add(Component.literal(" > ").withStyle(ChatFormatting.DARK_AQUA).append(Component.translatable("tetra.hone.progress", base - progress, base, percentage).withStyle(ChatFormatting.GRAY)));
                }
            }
        } else {
            ItemStack.appendEnchantmentNames(tooltip, itemStack.getEnchantmentTags());
            tooltip.add(Tooltips.expand);
        }
        return tooltip;
    }

    default String getImprovementTooltip(String key, int level, boolean clearFormatting) {
        return clearFormatting ? ChatFormatting.stripFormatting(getImprovementName(key, level)) : getImprovementName(key, level);
    }

    default Optional<ItemModule> getRepairModule(ItemStack itemStack) {
        List<ItemModule> modules = (List<ItemModule>) this.getAllModules(itemStack).stream().filter(itemModule -> !itemModule.getRepairDefinitions(itemStack).isEmpty()).collect(Collectors.toList());
        if (modules.size() > 0) {
            int repairCount = this.getRepairCount(itemStack);
            return Optional.of((ItemModule) modules.get(repairCount % modules.size()));
        } else {
            return Optional.empty();
        }
    }

    default ItemModule[] getRepairCycle(ItemStack itemStack) {
        return (ItemModule[]) this.getAllModules(itemStack).stream().filter(module -> !module.getRepairDefinitions(itemStack).isEmpty()).toArray(ItemModule[]::new);
    }

    default String getRepairModuleName(ItemStack itemStack) {
        return (String) this.getRepairModule(itemStack).map(module -> module.getName(itemStack)).orElse(null);
    }

    default String getRepairSlot(ItemStack itemStack) {
        return (String) this.getRepairModule(itemStack).map(ItemModule::getSlot).orElse(null);
    }

    default Collection<RepairDefinition> getRepairDefinitions(ItemStack itemStack) {
        return (Collection<RepairDefinition>) this.getRepairModule(itemStack).map(module -> module.getRepairDefinitions(itemStack)).orElse(null);
    }

    default int getRepairMaterialCount(ItemStack itemStack, ItemStack materialStack) {
        return (Integer) this.getRepairModule(itemStack).map(module -> module.getRepairDefinition(itemStack, materialStack)).map(definition -> definition.material.count).orElse(0);
    }

    default int getRepairAmount(ItemStack itemStack) {
        return this.getItem().getMaxDamage(itemStack);
    }

    default Collection<ToolAction> getRepairRequiredTools(ItemStack itemStack, ItemStack materialStack) {
        return (Collection<ToolAction>) this.getRepairModule(itemStack).map(module -> module.getRepairRequiredTools(itemStack, materialStack)).orElseGet(Collections::emptySet);
    }

    default Map<ToolAction, Integer> getRepairRequiredToolLevels(ItemStack itemStack, ItemStack materialStack) {
        return (Map<ToolAction, Integer>) this.getRepairModule(itemStack).map(module -> module.getRepairRequiredToolLevels(itemStack, materialStack)).orElseGet(Collections::emptyMap);
    }

    default int getRepairRequiredToolLevel(ItemStack itemStack, ItemStack materialStack, ToolAction toolAction) {
        return (Integer) this.getRepairModule(itemStack).filter(module -> module.getRepairRequiredTools(itemStack, materialStack).contains(toolAction)).map(module -> module.getRepairRequiredToolLevel(itemStack, materialStack, toolAction)).map(level -> Math.max(1, level)).orElse(0);
    }

    default int getRepairRequiredExperience(ItemStack itemStack, ItemStack materialStack) {
        return (Integer) this.getRepairModule(itemStack).map(module -> module.getRepairExperienceCost(itemStack, materialStack)).orElse(0);
    }

    default int getRepairCount(ItemStack itemStack) {
        return (Integer) Optional.ofNullable(itemStack.getTag()).map(tag -> tag.getInt("repairCount")).orElse(0);
    }

    default void incrementRepairCount(ItemStack itemStack) {
        CompoundTag tag = itemStack.getOrCreateTag();
        tag.putInt("repairCount", tag.getInt("repairCount") + 1);
    }

    default void repair(ItemStack itemStack) {
        this.getItem().setDamage(itemStack, this.getItem().getDamage(itemStack) - this.getRepairAmount(itemStack));
        this.incrementRepairCount(itemStack);
    }

    default float getStabilityModifier(ItemStack itemStack) {
        return 1.0F + (float) (this.getEffectLevel(itemStack, ItemEffect.stabilizing) - this.getEffectLevel(itemStack, ItemEffect.unstable)) / 100.0F;
    }

    default void applyDestabilizationEffects(ItemStack itemStack, Level world, float probabilityMultiplier) {
        if (!world.isClientSide) {
            Arrays.stream(this.getMajorModules(itemStack)).filter(Objects::nonNull).forEach(module -> {
                int instability = -module.getMagicCapacity(itemStack);
                if (instability > 0) {
                    float destabilizationChance = module.getDestabilizationChance(itemStack, probabilityMultiplier);
                    DestabilizationEffect[] possibleEffects = DestabilizationEffect.getEffectsForImprovement(instability, module.getImprovements(itemStack));
                    if (possibleEffects.length > 0) {
                        do {
                            if (destabilizationChance > world.random.nextFloat()) {
                                DestabilizationEffect effect = possibleEffects[world.random.nextInt(possibleEffects.length)];
                                int currentEffectLevel = module.getImprovementLevel(itemStack, effect.destabilizationKey);
                                int newLevel;
                                if (currentEffectLevel >= 0) {
                                    newLevel = currentEffectLevel + 1;
                                } else if (effect.minLevel == effect.maxLevel) {
                                    newLevel = effect.minLevel;
                                } else {
                                    newLevel = effect.minLevel + world.random.nextInt(effect.maxLevel - effect.minLevel);
                                }
                                if (module.acceptsImprovementLevel(effect.destabilizationKey, newLevel)) {
                                    module.addImprovement(itemStack, effect.destabilizationKey, newLevel);
                                }
                            }
                        } while (--destabilizationChance > 1.0F);
                    }
                }
            });
        }
    }

    default void tweak(ItemStack itemStack, String slot, Map<String, Integer> tweaks) {
        ItemModule module = this.getModuleFromSlot(itemStack, slot);
        double durabilityFactor = 0.0;
        if (module != null && module.isTweakable(itemStack)) {
            if (itemStack.isDamageableItem()) {
                durabilityFactor = (double) itemStack.getDamageValue() * 1.0 / (double) itemStack.getMaxDamage();
            }
            tweaks.forEach((tweakKey, step) -> {
                if (module.hasTweak(itemStack, tweakKey)) {
                    module.setTweakStep(itemStack, tweakKey, step);
                }
            });
            if (itemStack.isDamageableItem()) {
                itemStack.setDamageValue((int) Math.floor(durabilityFactor * (double) itemStack.getMaxDamage()));
            }
            updateIdentifier(itemStack);
        }
    }

    default Multimap<Attribute, AttributeModifier> getEffectAttributes(ItemStack itemStack) {
        return AttributeHelper.emptyMap;
    }

    default Multimap<Attribute, AttributeModifier> getModuleAttributes(ItemStack itemStack) {
        return (Multimap<Attribute, AttributeModifier>) this.getAllModules(itemStack).stream().map(module -> module.getAttributeModifiers(itemStack)).filter(Objects::nonNull).reduce(null, AttributeHelper::merge);
    }

    default Multimap<Attribute, AttributeModifier> getAttributeModifiers(ItemStack itemStack) {
        Multimap<Attribute, AttributeModifier> attributes = AttributeHelper.merge(this.getModuleAttributes(itemStack), this.getEffectAttributes(itemStack));
        return (Multimap<Attribute, AttributeModifier>) Arrays.stream(this.getSynergyData(itemStack)).map(synergy -> synergy.attributes).filter(Objects::nonNull).reduce(attributes, AttributeHelper::merge);
    }

    default Multimap<Attribute, AttributeModifier> getAttributeModifiersCollapsed(ItemStack itemStack) {
        if (logger.isDebugEnabled()) {
            logger.debug("Gathering attribute modifiers for {} ({})", this.getItemName(itemStack), this.getDataCacheKey(itemStack));
        }
        return (Multimap<Attribute, AttributeModifier>) Optional.ofNullable(this.getAttributeModifiers(itemStack)).map(modifiers -> (ArrayListMultimap) modifiers.asMap().entrySet().stream().collect(Multimaps.flatteningToMultimap(Entry::getKey, entry -> AttributeHelper.collapse((Collection<AttributeModifier>) entry.getValue()).stream(), ArrayListMultimap::create))).map(this::fixIdentifiers).orElse(null);
    }

    default Multimap<Attribute, AttributeModifier> fixIdentifiers(Multimap<Attribute, AttributeModifier> modifiers) {
        return AttributeHelper.fixIdentifiers(modifiers);
    }

    Cache<String, Multimap<Attribute, AttributeModifier>> getAttributeModifierCache();

    default Multimap<Attribute, AttributeModifier> getAttributeModifiersCached(ItemStack itemStack) {
        try {
            return (Multimap<Attribute, AttributeModifier>) this.getAttributeModifierCache().get(this.getDataCacheKey(itemStack), () -> (Multimap) Optional.ofNullable(this.getAttributeModifiersCollapsed(itemStack)).orElseGet(ImmutableMultimap::of));
        } catch (ExecutionException var3) {
            var3.printStackTrace();
            return this.getAttributeModifiersCollapsed(itemStack);
        }
    }

    default double getAttributeValue(ItemStack itemStack, Attribute attribute) {
        return this.isBroken(itemStack) ? 0.0 : AttributeHelper.getMergedAmount(this.getAttributeModifiersCached(itemStack).get(attribute));
    }

    default double getAttributeValue(ItemStack itemStack, Attribute attribute, double base) {
        return this.isBroken(itemStack) ? 0.0 : AttributeHelper.getMergedAmount(this.getAttributeModifiersCached(itemStack).get(attribute), base);
    }

    default EffectData getEffectData(ItemStack itemStack) {
        if (logger.isDebugEnabled()) {
            logger.debug("Gathering effect data for {} ({})", this.getItemName(itemStack), this.getDataCacheKey(itemStack));
        }
        return (EffectData) Stream.concat(this.getAllModules(itemStack).stream().map(module -> module.getEffectData(itemStack)), Arrays.stream(this.getSynergyData(itemStack)).map(synergy -> synergy.effects)).filter(Objects::nonNull).reduce(null, EffectData::merge);
    }

    Cache<String, EffectData> getEffectDataCache();

    default EffectData getEffectDataCached(ItemStack itemStack) {
        try {
            return (EffectData) this.getEffectDataCache().get(this.getDataCacheKey(itemStack), () -> (EffectData) Optional.ofNullable(this.getEffectData(itemStack)).orElseGet(EffectData::new));
        } catch (ExecutionException var3) {
            var3.printStackTrace();
            return (EffectData) Optional.ofNullable(this.getEffectData(itemStack)).orElseGet(EffectData::new);
        }
    }

    default ItemProperties getProperties(ItemStack itemStack) {
        if (logger.isDebugEnabled()) {
            logger.debug("Gathering properties for {} ({})", this.getItemName(itemStack), this.getDataCacheKey(itemStack));
        }
        return (ItemProperties) Stream.concat(this.getAllModules(itemStack).stream().map(module -> module.getProperties(itemStack)), Arrays.stream(this.getSynergyData(itemStack))).reduce(new ItemProperties(), ItemProperties::merge);
    }

    Cache<String, ItemProperties> getPropertyCache();

    default ItemProperties getPropertiesCached(ItemStack itemStack) {
        try {
            return (ItemProperties) this.getPropertyCache().get(this.getDataCacheKey(itemStack), () -> this.getProperties(itemStack));
        } catch (ExecutionException var3) {
            var3.printStackTrace();
            return this.getProperties(itemStack);
        }
    }

    default Set<TagKey<Item>> getTags(ItemStack itemStack) {
        return this.getPropertiesCached(itemStack).tags;
    }

    default int getEffectLevel(ItemStack itemStack, ItemEffect effect) {
        return this.isBroken(itemStack) ? -1 : this.getEffectDataCached(itemStack).getLevel(effect);
    }

    default float getEffectEfficiency(ItemStack itemStack, ItemEffect effect) {
        return this.isBroken(itemStack) ? 0.0F : this.getEffectDataCached(itemStack).getEfficiency(effect);
    }

    default Collection<ItemEffect> getEffects(ItemStack itemStack) {
        return (Collection<ItemEffect>) (this.isBroken(itemStack) ? Collections.emptyList() : this.getEffectDataCached(itemStack).getValues());
    }

    default ImprovementData[] getImprovements(ItemStack itemStack) {
        return (ImprovementData[]) Arrays.stream(this.getMajorModules(itemStack)).filter(Objects::nonNull).flatMap(module -> Arrays.stream(module.getImprovements(itemStack))).toArray(ImprovementData[]::new);
    }

    default String getDisplayNamePrefixes(ItemStack itemStack) {
        return (String) Stream.concat(Arrays.stream(this.getImprovements(itemStack)).map(improvement -> Pair.of(improvement.prefixPriority, "tetra.improvement." + improvement.key + ".prefix")).filter(pair -> I18n.exists((String) pair.getSecond())).map(pair -> Pair.of((Priority) pair.getFirst(), I18n.get((String) pair.getSecond()))), this.getAllModules(itemStack).stream().map(module -> Pair.of(module.getItemPrefixPriority(itemStack), module.getItemPrefix(itemStack))).filter(pair -> pair.getSecond() != null)).sorted(Comparator.comparing(Pair::getFirst).reversed()).limit(2L).map(Pair::getSecond).reduce("", (result, prefix) -> result + prefix + " ");
    }

    default String getItemName(ItemStack itemStack) {
        if (Environment.get().getDist().isDedicatedServer()) {
            return "";
        } else {
            String name = (String) Arrays.stream(this.getSynergyData(itemStack)).map(synergyData -> synergyData.name).filter(Objects::nonNull).map(key -> "tetra.synergy." + key).filter(I18n::m_118936_).map(x$0 -> I18n.get(x$0)).findFirst().orElse(null);
            if (name == null) {
                name = (String) this.getAllModules(itemStack).stream().sorted(Comparator.comparing(module -> module.getItemNamePriority(itemStack)).reversed()).map(module -> module.getItemName(itemStack)).filter(Objects::nonNull).findFirst().orElse("");
            }
            String prefixes = this.getDisplayNamePrefixes(itemStack);
            return WordUtils.capitalize(prefixes + name);
        }
    }

    SynergyData[] getAllSynergyData(ItemStack var1);

    default SynergyData[] getSynergyData(ItemStack itemStack) {
        SynergyData[] synergies = this.getAllSynergyData(itemStack);
        if (synergies.length > 0) {
            ItemModule[] modules = (ItemModule[]) this.getAllModules(itemStack).stream().sorted(Comparator.comparing(ItemModule::getUnlocalizedName)).toArray(ItemModule[]::new);
            String[] variantKeys = (String[]) this.getAllModules(itemStack).stream().map(module -> module.getVariantData(itemStack)).map(data -> data.key).sorted().toArray(String[]::new);
            String[] improvements = (String[]) Arrays.stream(this.getMajorModules(itemStack)).filter(Objects::nonNull).map(module -> module.getImprovements(itemStack)).flatMap(Arrays::stream).map(data -> data.key).sorted().toArray(String[]::new);
            return (SynergyData[]) Arrays.stream(synergies).filter(synergy -> synergy.modules.length == 0 || this.hasModuleSynergy(itemStack, synergy, modules)).filter(synergy -> synergy.moduleVariants.length == 0 || this.hasVariantSynergy(synergy, variantKeys)).filter(synergy -> synergy.improvements.length == 0 || this.hasImprovementSynergy(synergy, improvements)).toArray(SynergyData[]::new);
        } else {
            return new SynergyData[0];
        }
    }

    default boolean hasImprovementSynergy(SynergyData synergy, String[] improvements) {
        int improvementMatches = 0;
        for (String improvement : improvements) {
            if (improvementMatches == synergy.improvements.length) {
                break;
            }
            if (improvement.equals(synergy.improvements[improvementMatches])) {
                improvementMatches++;
            }
        }
        return synergy.improvements.length > 0 && improvementMatches == synergy.improvements.length;
    }

    default boolean hasVariantSynergy(SynergyData synergy, String[] variantKeys) {
        int variantMatches = 0;
        for (String variantKey : variantKeys) {
            if (variantMatches == synergy.moduleVariants.length) {
                break;
            }
            if (variantKey.equals(synergy.moduleVariants[variantMatches])) {
                variantMatches++;
            }
        }
        return synergy.moduleVariants.length > 0 && variantMatches == synergy.moduleVariants.length;
    }

    default boolean hasModuleSynergy(ItemStack itemStack, SynergyData synergy, ItemModule[] modules) {
        int moduleMatches = 0;
        String variant = null;
        if (synergy.sameVariant) {
            for (ItemModule module : modules) {
                if (moduleMatches == synergy.modules.length) {
                    break;
                }
                String moduleKey = synergy.matchSuffixed ? module.getKey() : module.getUnlocalizedName();
                if (moduleKey.equals(synergy.modules[moduleMatches])) {
                    if (variant == null) {
                        variant = module.getVariantData(itemStack).key;
                    }
                    if (variant.equals(module.getVariantData(itemStack).key)) {
                        moduleMatches++;
                    }
                }
            }
        } else {
            for (ItemModule module : modules) {
                if (moduleMatches == synergy.modules.length) {
                    break;
                }
                String moduleKey = synergy.matchSuffixed ? module.getKey() : module.getUnlocalizedName();
                if (moduleKey.equals(synergy.modules[moduleMatches])) {
                    moduleMatches++;
                }
            }
        }
        return synergy.modules.length > 0 && moduleMatches == synergy.modules.length;
    }

    default void assemble(ItemStack itemStack, @Nullable Level world, float severity) {
        if (itemStack.getDamageValue() > itemStack.getMaxDamage()) {
            itemStack.setDamageValue(itemStack.getMaxDamage());
        }
        if (world != null) {
            this.applyDestabilizationEffects(itemStack, world, severity);
        }
        CompoundTag nbt = itemStack.getOrCreateTag();
        nbt.putInt("HideFlags", 1);
        updateIdentifier(itemStack);
    }

    default boolean acceptsEnchantment(ItemStack itemStack, Enchantment enchantment, boolean fromTable) {
        return Arrays.stream(this.getMajorModules(itemStack)).filter(Objects::nonNull).anyMatch(module -> module.acceptsEnchantment(itemStack, enchantment, fromTable));
    }

    default int getEnchantability(ItemStack itemStack) {
        return (int) (Arrays.stream(this.getMajorModules(itemStack)).filter(Objects::nonNull).mapToInt(module -> module.getMagicCapacity(itemStack)).filter(capacity -> capacity > 0).average().orElse(0.0) / 6.0);
    }

    @OnlyIn(Dist.CLIENT)
    default ImmutableList<ModuleModel> getModels(ItemStack itemStack, @Nullable LivingEntity entity) {
        return (ImmutableList<ModuleModel>) this.getAllModules(itemStack).stream().sorted(Comparator.comparing(ItemModule::getRenderLayer)).flatMap(itemModule -> Arrays.stream(itemModule.getModels(itemStack))).filter(Objects::nonNull).sorted(Comparator.comparing(ModuleModel::getRenderLayer)).collect(Collectors.collectingAndThen(Collectors.toList(), ImmutableList::copyOf));
    }

    @OnlyIn(Dist.CLIENT)
    default String getTransformVariant(ItemStack itemStack, @Nullable LivingEntity entity) {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    default GuiModuleOffsets getMajorGuiOffsets(ItemStack itemStack) {
        return defaultMajorOffsets[this.getNumMajorModules(itemStack)];
    }

    @OnlyIn(Dist.CLIENT)
    default GuiModuleOffsets getMinorGuiOffsets(ItemStack itemStack) {
        return defaultMinorOffsets[this.getNumMinorModules(itemStack)];
    }
}