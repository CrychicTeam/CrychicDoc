package harmonised.pmmo.features.autovalues;

import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.api.enums.ReqType;
import harmonised.pmmo.util.MsLoggy;
import harmonised.pmmo.util.Reference;
import harmonised.pmmo.util.RegistryUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;

public class AutoItem {

    private static final double BASE_ATK_SPD = 1.6;

    private static final double BASE_DURABILITY = 59.0;

    private static final double BASE_DAMAGE = 4.0;

    public static final ReqType[] REQTYPES = new ReqType[] { ReqType.WEAR, ReqType.USE_ENCHANTMENT, ReqType.TOOL, ReqType.WEAPON };

    public static final EventType[] EVENTTYPES = new EventType[] { EventType.ANVIL_REPAIR, EventType.BLOCK_PLACE, EventType.CRAFT, EventType.CONSUME, EventType.ENCHANT, EventType.FISH, EventType.SMELT };

    public static Map<String, Integer> processReqs(ReqType type, ResourceLocation stackID) {
        if (!type.itemApplicable) {
            return new HashMap();
        } else {
            Map<String, Integer> outMap = new HashMap();
            ItemStack stack = new ItemStack(ForgeRegistries.ITEMS.getValue(stackID));
            switch(type) {
                case WEAR:
                    if (stack.getItem() instanceof TieredItem) {
                        if (stack.getItem() instanceof SwordItem) {
                            outMap.putAll(getUtensilData(AutoValueConfig.UtensilTypes.SWORD, type, stack, true));
                        } else if (stack.getItem() instanceof AxeItem) {
                            outMap.putAll(getUtensilData(AutoValueConfig.UtensilTypes.AXE, type, stack, false));
                        } else if (stack.getItem() instanceof PickaxeItem) {
                            outMap.putAll(getUtensilData(AutoValueConfig.UtensilTypes.PICKAXE, type, stack, false));
                        } else if (stack.getItem() instanceof ShovelItem) {
                            outMap.putAll(getUtensilData(AutoValueConfig.UtensilTypes.SHOVEL, type, stack, false));
                        } else if (stack.getItem() instanceof HoeItem) {
                            outMap.putAll(getUtensilData(AutoValueConfig.UtensilTypes.HOE, type, stack, false));
                        }
                    } else if (stack.getItem() instanceof ArmorItem) {
                        outMap.putAll(getWearableData(type, stack, true));
                    } else if (stack.getItem() instanceof ElytraItem) {
                        outMap.putAll(getWearableData(type, stack, false));
                    }
                    break;
                case USE_ENCHANTMENT:
                    double scale = 0.0;
                    for (Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments(stack).entrySet()) {
                        scale += (double) (entry.getValue() / ((Enchantment) entry.getKey()).getMaxLevel());
                    }
                    for (Entry<String, Integer> entry : AutoValueConfig.getItemReq(type).entrySet()) {
                        outMap.put((String) entry.getKey(), (int) ((double) ((Integer) entry.getValue()).intValue() * scale));
                    }
                    break;
                case TOOL:
                    if (stack.getItem() instanceof TieredItem) {
                        if (stack.getItem() instanceof SwordItem) {
                            outMap.putAll(getUtensilData(AutoValueConfig.UtensilTypes.SWORD, type, stack, false));
                        } else if (stack.getItem() instanceof AxeItem) {
                            outMap.putAll(getUtensilData(AutoValueConfig.UtensilTypes.AXE, type, stack, false));
                        } else if (stack.getItem() instanceof PickaxeItem) {
                            outMap.putAll(getUtensilData(AutoValueConfig.UtensilTypes.PICKAXE, type, stack, false));
                        } else if (stack.getItem() instanceof ShovelItem) {
                            outMap.putAll(getUtensilData(AutoValueConfig.UtensilTypes.SHOVEL, type, stack, false));
                        } else if (stack.getItem() instanceof HoeItem) {
                            outMap.putAll(getUtensilData(AutoValueConfig.UtensilTypes.HOE, type, stack, false));
                        }
                    }
                    break;
                case WEAPON:
                    if (stack.getItem() instanceof TieredItem) {
                        if (stack.getItem() instanceof SwordItem) {
                            outMap.putAll(getUtensilData(AutoValueConfig.UtensilTypes.SWORD, type, stack, true));
                        } else if (stack.getItem() instanceof AxeItem) {
                            outMap.putAll(getUtensilData(AutoValueConfig.UtensilTypes.AXE, type, stack, true));
                        } else if (stack.getItem() instanceof PickaxeItem) {
                            outMap.putAll(getUtensilData(AutoValueConfig.UtensilTypes.PICKAXE, type, stack, true));
                        } else if (stack.getItem() instanceof ShovelItem) {
                            outMap.putAll(getUtensilData(AutoValueConfig.UtensilTypes.SHOVEL, type, stack, true));
                        } else if (stack.getItem() instanceof HoeItem) {
                            outMap.putAll(getUtensilData(AutoValueConfig.UtensilTypes.HOE, type, stack, true));
                        }
                    }
                    break;
                case BREAK:
                    if (stack.getItem() instanceof BlockItem) {
                        outMap.putAll(AutoBlock.processReqs(type, stackID));
                    }
            }
            return outMap;
        }
    }

    public static Map<String, Long> processXpGains(EventType type, ResourceLocation stackID) {
        if (!type.itemApplicable) {
            return new HashMap();
        } else {
            Map<String, Long> outMap = new HashMap();
            ItemStack stack = new ItemStack(ForgeRegistries.ITEMS.getValue(stackID));
            switch(type) {
                case ANVIL_REPAIR:
                    if (stack.isRepairable()) {
                        AutoValueConfig.getItemXpAward(type).forEach((skill, xp) -> outMap.put(skill, (long) ((double) xp.longValue() * (double) stack.getMaxDamage() * 0.25)));
                    }
                    break;
                case BLOCK_PLACE:
                case BLOCK_BREAK:
                    if (stack.getItem() instanceof BlockItem) {
                        outMap.putAll(AutoBlock.processXpGains(type, RegistryUtil.getId(((BlockItem) stack.getItem()).getBlock())));
                    }
                    break;
                case CRAFT:
                    if (stack.getItem() instanceof TieredItem) {
                        if (stack.getItem() instanceof SwordItem) {
                            outMap.putAll(getUtensilData(AutoValueConfig.UtensilTypes.SWORD, type, stack, true));
                        } else if (stack.getItem() instanceof AxeItem) {
                            outMap.putAll(getUtensilData(AutoValueConfig.UtensilTypes.AXE, type, stack, false));
                        } else if (stack.getItem() instanceof PickaxeItem) {
                            outMap.putAll(getUtensilData(AutoValueConfig.UtensilTypes.PICKAXE, type, stack, false));
                        } else if (stack.getItem() instanceof ShovelItem) {
                            outMap.putAll(getUtensilData(AutoValueConfig.UtensilTypes.SHOVEL, type, stack, false));
                        } else if (stack.getItem() instanceof HoeItem) {
                            outMap.putAll(getUtensilData(AutoValueConfig.UtensilTypes.HOE, type, stack, false));
                        }
                    } else if (stack.getItem() instanceof ArmorItem) {
                        outMap.putAll(getWearableData(type, stack, true));
                    } else if (stack.getItem() instanceof ElytraItem) {
                        outMap.putAll(getWearableData(type, stack, false));
                    } else {
                        outMap.putAll(AutoValueConfig.getItemXpAward(type));
                    }
                    break;
                case CONSUME:
                    if (stack.isEdible()) {
                        AutoValueConfig.getItemXpAward(type).forEach((skill, xp) -> {
                            FoodProperties properties = stack.getItem().getFoodProperties();
                            Float nutritionScale = (float) properties.getNutrition() * properties.getSaturationModifier();
                            Float xpAward = nutritionScale * (float) xp.longValue();
                            outMap.put(skill, xpAward.longValue());
                        });
                    }
                case BREW:
                    if (ForgeRegistries.ITEMS.tags().getTag(Reference.BREWABLES).contains(stack.getItem())) {
                        outMap.putAll(AutoValueConfig.BREWABLES_OVERRIDE.get());
                    }
                    break;
                case SMELT:
                    if (ForgeRegistries.ITEMS.tags().getTag(Reference.SMELTABLES).contains(stack.getItem())) {
                        outMap.putAll(AutoValueConfig.SMELTABLES_OVERRIDE.get());
                    }
                    break;
                case ENCHANT:
                case FISH:
                    outMap.putAll(AutoValueConfig.getItemXpAward(type));
            }
            return outMap;
        }
    }

    private static Map<String, Integer> getUtensilData(AutoValueConfig.UtensilTypes utensil, ReqType type, ItemStack stack, boolean asWeapon) {
        Map<String, Integer> outMap = new HashMap();
        if (stack.getItem() instanceof TieredItem && getTier((TieredItem) stack.getItem()) <= 0.0) {
            return outMap;
        } else {
            double scale = getUtensilAttributes(utensil, stack, asWeapon);
            Map<String, Integer> configValue = type != ReqType.TOOL && (type != ReqType.WEAR || asWeapon) ? (type == ReqType.WEAR && asWeapon ? AutoValueConfig.getItemReq(ReqType.WEAPON) : AutoValueConfig.getItemReq(type)) : AutoValueConfig.getToolReq(stack);
            configValue.forEach((skill, level) -> outMap.put(skill, (int) Math.max(0.0, (double) level.intValue() * scale)));
            MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.AUTO_VALUES, "AutoItem Req Map: " + MsLoggy.mapToString(outMap));
            return outMap;
        }
    }

    private static Map<String, Long> getUtensilData(AutoValueConfig.UtensilTypes utensil, EventType type, ItemStack stack, boolean asWeapon) {
        Map<String, Long> outMap = new HashMap();
        double scale = getUtensilAttributes(utensil, stack, asWeapon);
        AutoValueConfig.getItemXpAward(type).forEach((skill, level) -> outMap.put(skill, Double.valueOf(Math.max(0.0, (double) level.longValue() * scale)).longValue()));
        MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.AUTO_VALUES, "AutoItem XpGain Map: " + MsLoggy.mapToString(outMap));
        return outMap;
    }

    private static Map<String, Integer> getWearableData(ReqType type, ItemStack stack, boolean isArmor) {
        Map<String, Integer> outMap = new HashMap();
        if (stack.getItem() instanceof ArmorItem && ((ArmorItem) stack.getItem()).getMaterial().equals(ArmorMaterials.LEATHER)) {
            return outMap;
        } else {
            double scale = getWearableAttributes(AutoValueConfig.WearableTypes.fromSlot(LivingEntity.getEquipmentSlotForItem(stack), !isArmor), stack, isArmor);
            AutoValueConfig.getItemReq(type).forEach((skill, level) -> outMap.put(skill, (int) Math.max(0.0, (double) level.intValue() * scale)));
            return outMap;
        }
    }

    private static Map<String, Long> getWearableData(EventType type, ItemStack stack, boolean isArmor) {
        Map<String, Long> outMap = new HashMap();
        double scale = getWearableAttributes(AutoValueConfig.WearableTypes.fromSlot(LivingEntity.getEquipmentSlotForItem(stack), !isArmor), stack, isArmor);
        AutoValueConfig.getItemXpAward(type).forEach((skill, level) -> outMap.put(skill, Double.valueOf(Math.max(0.0, (double) level.longValue() * scale)).longValue()));
        return outMap;
    }

    private static double getAttributeAmount(ItemStack stack, EquipmentSlot slot, Attribute attribute) {
        return (Double) stack.getAttributeModifiers(slot).get(attribute).stream().collect(Collectors.summingDouble(a -> a.getAmount()));
    }

    private static double getTier(TieredItem item) {
        return (double) item.getTier().getLevel();
    }

    private static double getDamage(ItemStack stack) {
        return getAttributeAmount(stack, EquipmentSlot.MAINHAND, Attributes.ATTACK_DAMAGE) + (double) EnchantmentHelper.getDamageBonus(stack, null) - 4.0;
    }

    private static double getAttackSpeed(ItemStack stack) {
        return Math.abs(getAttributeAmount(stack, EquipmentSlot.MAINHAND, Attributes.ATTACK_SPEED)) - 1.6;
    }

    private static double getDurability(ItemStack stack) {
        return (double) stack.getMaxDamage() - 59.0;
    }

    private static double getUtensilAttributes(AutoValueConfig.UtensilTypes type, ItemStack stack, boolean asWeapon) {
        double durabilityScale = getDurability(stack) * AutoValueConfig.getUtensilAttribute(type, AutoValueConfig.AttributeKey.DUR);
        double tierScale = getTier((TieredItem) stack.getItem()) * AutoValueConfig.getUtensilAttribute(type, AutoValueConfig.AttributeKey.TIER);
        double damageScale = asWeapon ? getDamage(stack) * AutoValueConfig.getUtensilAttribute(type, AutoValueConfig.AttributeKey.DMG) : 0.0;
        double atkSpdScale = asWeapon ? getAttackSpeed(stack) * AutoValueConfig.getUtensilAttribute(type, AutoValueConfig.AttributeKey.SPD) : 0.0;
        double digSpeedScale = asWeapon ? 0.0 : (double) stack.getDestroySpeed(Blocks.COBWEB.defaultBlockState()) + AutoValueConfig.getUtensilAttribute(type, AutoValueConfig.AttributeKey.DIG);
        MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.AUTO_VALUES, "AutoItem Attributes: DUR=" + durabilityScale + " TIER=" + tierScale + " DMG=" + damageScale + " SPD=" + atkSpdScale + " DIG=" + digSpeedScale);
        return damageScale + atkSpdScale + digSpeedScale + durabilityScale + tierScale;
    }

    private static double getWearableAttributes(AutoValueConfig.WearableTypes type, ItemStack stack, boolean isArmor) {
        double durabilityScale = (double) stack.getMaxDamage() * AutoValueConfig.getWearableAttribute(type, AutoValueConfig.AttributeKey.DUR);
        ArmorMaterial material = isArmor ? ((ArmorItem) stack.getItem()).getMaterial() : null;
        double armorScale = isArmor ? (double) material.getDefenseForType(((ArmorItem) stack.getItem()).getType()) * AutoValueConfig.getWearableAttribute(type, AutoValueConfig.AttributeKey.AMR) : 0.0;
        double toughnessScale = isArmor ? (double) material.getToughness() * AutoValueConfig.getWearableAttribute(type, AutoValueConfig.AttributeKey.TUF) : 0.0;
        double knockbackScale = isArmor ? (double) material.getKnockbackResistance() * AutoValueConfig.getWearableAttribute(type, AutoValueConfig.AttributeKey.KBR) : 0.0;
        MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.AUTO_VALUES, "AutoItem Attributes: DUR=" + durabilityScale + " ARM=" + armorScale + " TUF=" + toughnessScale + " KBR=" + knockbackScale);
        return durabilityScale + armorScale + toughnessScale + knockbackScale;
    }
}