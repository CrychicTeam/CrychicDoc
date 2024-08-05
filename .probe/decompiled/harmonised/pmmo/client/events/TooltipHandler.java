package harmonised.pmmo.client.events;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.datafixers.util.Pair;
import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.api.enums.ModifierDataType;
import harmonised.pmmo.api.enums.ObjectType;
import harmonised.pmmo.api.enums.ReqType;
import harmonised.pmmo.client.gui.StatsScreen;
import harmonised.pmmo.client.utils.DP;
import harmonised.pmmo.config.Config;
import harmonised.pmmo.config.SkillsConfig;
import harmonised.pmmo.config.codecs.SkillData;
import harmonised.pmmo.config.codecs.VeinData;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.core.CoreUtils;
import harmonised.pmmo.setup.ClientSetup;
import harmonised.pmmo.setup.datagen.LangProvider;
import harmonised.pmmo.util.RegistryUtil;
import harmonised.pmmo.util.TagUtils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "pmmo", bus = Bus.FORGE, value = { Dist.CLIENT })
public class TooltipHandler {

    public static boolean tooltipOn = true;

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        if (tooltipOn) {
            Player player = event.getEntity();
            if (player != null) {
                Core core = Core.get(LogicalSide.CLIENT);
                ItemStack stack = event.getItemStack();
                boolean isBlockItem = stack.getItem() instanceof BlockItem;
                ResourceLocation itemID = RegistryUtil.getId(stack);
                if (itemID == null) {
                    return;
                }
                if (!ClientSetup.OPEN_MENU.isUnbound() && InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), ClientSetup.OPEN_MENU.getKey().getValue())) {
                    Minecraft.getInstance().setScreen(new StatsScreen(stack));
                    return;
                }
                Arrays.stream(isBlockItem ? ReqType.BLOCKITEM_APPLICABLE_EVENTS : ReqType.ITEM_APPLICABLE_EVENTS).filter(type -> Config.tooltipReqEnabled(type).get()).map(type -> Pair.of(type, getReqData(core, type, stack))).filter(pair -> !((Map) pair.getSecond()).isEmpty()).forEach(pair -> addRequirementTooltip(((ReqType) pair.getFirst()).tooltipTranslation, event, (Map<String, Integer>) pair.getSecond(), core));
                Arrays.stream(isBlockItem ? EventType.BLOCKITEM_APPLICABLE_EVENTS : EventType.ITEM_APPLICABLE_EVENTS).filter(type -> Config.tooltipXpEnabled(type).get()).map(type -> Pair.of(type, getXpData(core, type, player, stack))).filter(pair -> !((Map) pair.getSecond()).isEmpty()).forEach(pair -> addXpValueTooltip(((EventType) pair.getFirst()).tooltipTranslation, event, (Map<String, Long>) pair.getSecond(), core));
                Stream.of(ModifierDataType.HELD, ModifierDataType.WORN).filter(type -> Config.tooltipBonusEnabled(type).get()).map(type -> Pair.of(type, core.getObjectModifierMap(ObjectType.ITEM, itemID, type, TagUtils.stackTag(stack)))).filter(pair -> !((Map) pair.getSecond()).isEmpty()).forEach(pair -> addModifierTooltip(((ModifierDataType) pair.getFirst()).tooltip, event, (Map<String, Double>) pair.getSecond(), core));
                VeinData veinData = core.getLoader().ITEM_LOADER.getData(itemID).veinData();
                if (!veinData.isUnconfigured() && !veinData.equals(VeinData.EMPTY) && Config.VEIN_ENABLED.get()) {
                    addVeinTooltip(LangProvider.VEIN_TOOLTIP, event, veinData, isBlockItem);
                }
            }
        }
    }

    private static void addRequirementTooltip(LangProvider.Translation header, ItemTooltipEvent event, Map<String, Integer> reqs, Core core) {
        event.getToolTip().add(header.asComponent());
        for (Entry<String, Integer> req : reqs.entrySet()) {
            event.getToolTip().add(Component.translatable("pmmo." + (String) req.getKey()).append(Component.literal(" " + req.getValue())).setStyle(CoreUtils.getSkillStyle((String) req.getKey())));
        }
    }

    private static void addXpValueTooltip(LangProvider.Translation header, ItemTooltipEvent event, Map<String, Long> values, Core core) {
        event.getToolTip().add(header.asComponent());
        values.entrySet().stream().filter(entry -> (Long) entry.getValue() > 0L).forEach(value -> event.getToolTip().add(Component.translatable("pmmo." + (String) value.getKey()).append(Component.literal(" " + value.getValue())).setStyle(CoreUtils.getSkillStyle((String) value.getKey()))));
    }

    private static void addModifierTooltip(LangProvider.Translation header, ItemTooltipEvent event, Map<String, Double> values, Core core) {
        event.getToolTip().add(header.asComponent());
        values.entrySet().stream().filter(entry -> (Double) entry.getValue() != 0.0 && (Double) entry.getValue() != 1.0).forEach(modifier -> event.getToolTip().add(Component.translatable("pmmo." + (String) modifier.getKey()).append(Component.literal(" " + modifierPercent((Double) modifier.getValue()))).setStyle(CoreUtils.getSkillStyle((String) modifier.getKey()))));
    }

    private static void addVeinTooltip(LangProvider.Translation header, ItemTooltipEvent event, VeinData data, boolean isBlockItem) {
        event.getToolTip().add(header.asComponent());
        event.getToolTip().add(LangProvider.VEIN_DATA.asComponent(data.chargeCap.orElse(0), DP.dp((Double) data.chargeRate.orElse(0.0) * 2.0)));
        if (isBlockItem) {
            event.getToolTip().add(LangProvider.VEIN_BREAK.asComponent(data.consumeAmount.orElse(0)));
        }
    }

    private static String modifierPercent(Double value) {
        return DP.dp((value - 1.0) * 100.0) + "%";
    }

    private static Map<String, Long> getXpData(Core core, EventType type, Player player, ItemStack stack) {
        Map<String, Long> map = core.getExperienceAwards(type, stack, player, new CompoundTag());
        if (stack.getItem() instanceof BlockItem) {
            map = core.getCommonXpAwardData(new HashMap(), type, RegistryUtil.getId(stack), player, ObjectType.BLOCK, TagUtils.stackTag(stack));
        }
        CoreUtils.processSkillGroupXP(map);
        return map;
    }

    private static Map<String, Integer> getReqData(Core core, ReqType type, ItemStack stack) {
        if (!Config.reqEnabled(type).get()) {
            return new HashMap();
        } else {
            Map<String, Integer> map = type == ReqType.USE_ENCHANTMENT ? core.getEnchantReqs(stack) : core.getReqMap(type, stack, true);
            if (stack.getItem() instanceof BlockItem) {
                map.putAll(core.getCommonReqData(new HashMap(), ObjectType.BLOCK, RegistryUtil.getId(stack), type, TagUtils.stackTag(stack)));
            }
            CoreUtils.processSkillGroupReqs(map);
            if (!Config.HIDE_MET_REQS.get()) {
                return map;
            } else {
                new HashMap(map).forEach((skill, level) -> {
                    if (((SkillData) SkillsConfig.SKILLS.get().getOrDefault(skill, SkillData.Builder.getDefault())).isSkillGroup()) {
                        int total = (Integer) ((SkillData) SkillsConfig.SKILLS.get().get(skill)).getGroup().keySet().stream().map(groupskill -> core.getData().getPlayerSkillLevel(groupskill, null)).collect(Collectors.summingInt(Integer::intValue));
                        if (level <= total) {
                            map.remove(skill);
                        }
                    } else if (core.getData().getPlayerSkillLevel(skill, null) >= level) {
                        map.remove(skill);
                    }
                });
                return map;
            }
        }
    }
}