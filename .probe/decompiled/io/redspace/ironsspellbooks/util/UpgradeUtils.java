package io.redspace.ironsspellbooks.util;

import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.capabilities.magic.UpgradeData;
import io.redspace.ironsspellbooks.item.armor.UpgradeType;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class UpgradeUtils {

    public static final Map<EquipmentSlot, UUID> UPGRADE_UUIDS_BY_SLOT = Map.of(EquipmentSlot.HEAD, UUID.fromString("f6c19678-1c70-4d41-ad19-cd84d8610242"), EquipmentSlot.CHEST, UUID.fromString("8d02c916-b0eb-4d17-8414-329b4bd38ae7"), EquipmentSlot.LEGS, UUID.fromString("3739c748-98d4-4a2d-9c25-3b4dec74823d"), EquipmentSlot.FEET, UUID.fromString("41cede88-7881-42dd-aac3-d6ab4b56b1f2"), EquipmentSlot.MAINHAND, UUID.fromString("c3865ad7-1f35-46d4-8b4b-a6b934a1a896"), EquipmentSlot.OFFHAND, UUID.fromString("c508430e-7497-42a9-9a9c-1a324dccca54"));

    public static String getRelevantEquipmentSlot(ItemStack itemStack) {
        if (itemStack.getItem() instanceof ICurioItem curioItem) {
            Set<String> tags = CuriosApi.getCuriosHelper().getCurioTags((Item) curioItem);
            Optional<String> slot = tags.stream().findFirst();
            if (slot.isPresent()) {
                return (String) slot.get();
            }
        } else if (itemStack.getItem() instanceof ArmorItem armorItem) {
            return armorItem.getEquipmentSlot().getName();
        }
        return EquipmentSlot.MAINHAND.getName();
    }

    public static UUID UUIDForSlot(EquipmentSlot slot) {
        return (UUID) UPGRADE_UUIDS_BY_SLOT.get(slot);
    }

    public static void handleAttributeEvent(Multimap<Attribute, AttributeModifier> modifiers, UpgradeData upgradeData, BiConsumer<Attribute, AttributeModifier> addCallback, BiConsumer<Attribute, AttributeModifier> removeCallback, Optional<UUID> uuidOverride) {
        Map<UpgradeType, Integer> upgrades = upgradeData.getUpgrades();
        for (Entry<UpgradeType, Integer> entry : upgrades.entrySet()) {
            UpgradeType upgradeType = (UpgradeType) entry.getKey();
            int count = (Integer) entry.getValue();
            double baseAmount = collectAndRemovePreexistingAttribute(modifiers, upgradeType.getAttribute(), upgradeType.getOperation(), removeCallback);
            UUID uuid;
            if (uuidOverride.isPresent()) {
                uuid = (UUID) uuidOverride.get();
            } else {
                try {
                    uuid = UUIDForSlot(EquipmentSlot.byName(upgradeData.getUpgradedSlot()));
                } catch (IllegalArgumentException var14) {
                    IronsSpellbooks.LOGGER.warn("Invalid UpgradeData NBT: {}", var14.toString());
                    return;
                }
            }
            addCallback.accept(upgradeType.getAttribute(), new AttributeModifier(uuid, "upgrade", baseAmount + (double) (upgradeType.getAmountPerUpgrade() * (float) count), ((UpgradeType) entry.getKey()).getOperation()));
        }
    }

    public static double collectAndRemovePreexistingAttribute(Multimap<Attribute, AttributeModifier> modifiers, Attribute key, AttributeModifier.Operation operationToMatch, BiConsumer<Attribute, AttributeModifier> removeCallback) {
        if (modifiers.containsKey(key)) {
            for (AttributeModifier modifier : modifiers.get(key)) {
                if (modifier.getOperation().equals(operationToMatch)) {
                    removeCallback.accept(key, modifier);
                    return modifier.getAmount();
                }
            }
        }
        return 0.0;
    }
}