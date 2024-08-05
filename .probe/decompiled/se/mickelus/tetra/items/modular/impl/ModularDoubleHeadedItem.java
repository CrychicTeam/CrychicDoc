package se.mickelus.tetra.items.modular.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.mojang.datafixers.util.Pair;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.mickelus.mutil.network.PacketHandler;
import se.mickelus.tetra.ConfigHandler;
import se.mickelus.tetra.TetraToolActions;
import se.mickelus.tetra.blocks.workbench.BasicWorkbenchBlock;
import se.mickelus.tetra.data.DataManager;
import se.mickelus.tetra.effect.ChargedAbilityEffect;
import se.mickelus.tetra.gui.GuiModuleOffsets;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.items.modular.ItemModularHandheld;
import se.mickelus.tetra.module.Priority;
import se.mickelus.tetra.module.SchematicRegistry;
import se.mickelus.tetra.module.data.ToolData;
import se.mickelus.tetra.module.schematic.RepairSchematic;
import se.mickelus.tetra.properties.AttributeHelper;

@ParametersAreNonnullByDefault
public class ModularDoubleHeadedItem extends ItemModularHandheld {

    public static final String headLeftKey = "double/head_left";

    public static final String headRightKey = "double/head_right";

    public static final String handleKey = "double/handle";

    public static final String bindingKey = "double/binding";

    public static final String accessoryKey = "double/accessory";

    public static final String leftSuffix = "_left";

    public static final String rightSuffix = "_right";

    public static final String identifier = "modular_double";

    private static final Logger logger = LogManager.getLogger();

    private static final GuiModuleOffsets majorOffsets = new GuiModuleOffsets(-13, -1, 3, 19, -13, 19);

    private static final GuiModuleOffsets minorOffsets = new GuiModuleOffsets(6, 1);

    @ObjectHolder(registryName = "item", value = "tetra:modular_double")
    public static ModularDoubleHeadedItem instance;

    public ModularDoubleHeadedItem() {
        super(new Item.Properties().stacksTo(1).fireResistant());
        this.entityHitDamage = 2;
        this.majorModuleKeys = new String[] { "double/head_left", "double/head_right", "double/handle" };
        this.minorModuleKeys = new String[] { "double/binding" };
        this.requiredModules = new String[] { "double/handle", "double/head_left", "double/head_right" };
        this.updateConfig(ConfigHandler.honedoubleBase.get(), ConfigHandler.honedoubleIntegrityMultiplier.get());
        SchematicRegistry.instance.registerSchematic(new RepairSchematic(this, "modular_double"));
    }

    public void updateConfig(int honeBase, int honeIntegrityMultiplier) {
        this.honeBase = honeBase;
        this.honeIntegrityMultiplier = honeIntegrityMultiplier;
    }

    @Override
    public void commonInit(PacketHandler packetHandler) {
        DataManager.instance.synergyData.onReload(() -> this.synergies = DataManager.instance.getSynergyData("double/"));
    }

    public static Collection<ItemStack> getCreativeTabItemStacks() {
        return Lists.newArrayList(new ItemStack[] { setupHammerStack("oak", "stick"), setupHammerStack("stone", "stick"), setupHammerStack("iron", "spruce"), setupHammerStack("blackstone", "spruce"), setupHammerStack("obsidian", "iron"), setupHammerStack("netherite", "forged_beam") });
    }

    public static ItemStack setupHammerStack(String headMaterial, String handleMaterial) {
        ItemStack itemStack = new ItemStack(instance);
        IModularItem.putModuleInSlot(itemStack, "double/head_left", "double/basic_hammer_left", "double/basic_hammer_left_material", "basic_hammer/" + headMaterial);
        IModularItem.putModuleInSlot(itemStack, "double/head_right", "double/basic_hammer_right", "double/basic_hammer_right_material", "basic_hammer/" + headMaterial);
        IModularItem.putModuleInSlot(itemStack, "double/handle", "double/basic_handle", "double/basic_handle_material", "basic_handle/" + handleMaterial);
        IModularItem.updateIdentifier(itemStack);
        return itemStack;
    }

    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Player player = context.getPlayer();
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        InteractionHand hand = context.getHand();
        return player != null && !player.m_6047_() && world.getBlockState(pos).m_60734_().equals(Blocks.CRAFTING_TABLE) && this.getToolLevel(player.m_21120_(hand), TetraToolActions.hammer) > 0 ? BasicWorkbenchBlock.upgradeWorkbench(player, world, pos, hand, context.getClickedFace()) : super.onItemUseFirst(stack, context);
    }

    @Override
    public String getDisplayNamePrefixes(ItemStack itemStack) {
        return (String) Stream.concat(Arrays.stream(this.getImprovements(itemStack)).map(improvement -> Pair.of(improvement.prefixPriority, "tetra.improvement." + improvement.key + ".prefix")).filter(pair -> I18n.exists((String) pair.getSecond())).map(pair -> Pair.of((Priority) pair.getFirst(), I18n.get((String) pair.getSecond()))), this.getAllModules(itemStack).stream().filter(module -> "double/head_left".equals(module.getSlot()) || module.getItemPrefixPriority(itemStack) != Priority.BASE).map(module -> Pair.of(module.getItemPrefixPriority(itemStack), module.getItemPrefix(itemStack))).filter(pair -> pair.getSecond() != null)).sorted(Comparator.comparing(Pair::getFirst).reversed()).limit(2L).map(Pair::getSecond).reduce("", (result, prefix) -> result + prefix + " ");
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(ItemStack itemStack) {
        Multimap<Attribute, AttributeModifier> moduleAttributes = (Multimap<Attribute, AttributeModifier>) Stream.of(this.getModuleFromSlot(itemStack, "double/head_left"), this.getModuleFromSlot(itemStack, "double/head_right")).filter(Objects::nonNull).map(module -> module.getAttributeModifiers(itemStack)).filter(Objects::nonNull).map(modifiers -> (ArrayListMultimap) modifiers.asMap().entrySet().stream().collect(Multimaps.flatteningToMultimap(Entry::getKey, entry -> AttributeHelper.collapse((Collection<AttributeModifier>) entry.getValue()).stream(), ArrayListMultimap::create))).map(Multimap::entries).flatMap(Collection::stream).collect(Multimaps.toMultimap(Entry::getKey, Entry::getValue, ArrayListMultimap::create));
        moduleAttributes = AttributeHelper.retainMax(moduleAttributes, Attributes.ATTACK_DAMAGE);
        moduleAttributes = (Multimap<Attribute, AttributeModifier>) this.getAllModules(itemStack).stream().filter(itemModule -> !"double/head_left".equals(itemModule.getSlot()) && !"double/head_right".equals(itemModule.getSlot())).map(module -> module.getAttributeModifiers(itemStack)).reduce(moduleAttributes, AttributeHelper::merge);
        return (Multimap<Attribute, AttributeModifier>) Arrays.stream(this.getSynergyData(itemStack)).map(synergy -> synergy.attributes).reduce(moduleAttributes, AttributeHelper::merge);
    }

    @Override
    public ToolData getToolDataRaw(ItemStack itemStack) {
        logger.debug("Gathering tool data for {} ({})", this.m_7626_(itemStack).getString(), this.getDataCacheKey(itemStack));
        ToolData result = ToolData.retainMax((Collection<ToolData>) Stream.of(this.getModuleFromSlot(itemStack, "double/head_left"), this.getModuleFromSlot(itemStack, "double/head_right")).filter(Objects::nonNull).map(module -> module.getToolData(itemStack)).filter(Objects::nonNull).collect(Collectors.toList()));
        return (ToolData) Stream.concat(this.getAllModules(itemStack).stream().filter(itemModule -> !"double/head_left".equals(itemModule.getSlot()) && !"double/head_right".equals(itemModule.getSlot())).map(module -> module.getToolData(itemStack)), Arrays.stream(this.getSynergyData(itemStack)).map(synergy -> synergy.tools)).filter(Objects::nonNull).reduce(result, ToolData::merge);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public GuiModuleOffsets getMajorGuiOffsets(ItemStack itemStack) {
        return majorOffsets;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public GuiModuleOffsets getMinorGuiOffsets(ItemStack itemStack) {
        return minorOffsets;
    }

    @Override
    public String getModelCacheKey(ItemStack itemStack, LivingEntity entity) {
        return entity != null && itemStack.equals(entity.getUseItem()) ? (String) Optional.ofNullable(this.getChargeableAbility(itemStack)).map(ChargedAbilityEffect::getModelTransform).map(transform -> super.getModelCacheKey(itemStack, entity) + ":" + transform).orElseGet(() -> super.getModelCacheKey(itemStack, entity)) : super.getModelCacheKey(itemStack, entity);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public String getTransformVariant(ItemStack itemStack, @Nullable LivingEntity entity) {
        ChargedAbilityEffect ability = this.getChargeableAbility(itemStack);
        return entity != null && ability != null && itemStack.equals(entity.getUseItem()) ? ability.getModelTransform() : null;
    }
}