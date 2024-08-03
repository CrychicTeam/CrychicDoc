package com.mna.capabilities.playerdata.magic;

import com.mna.ManaAndArtifice;
import com.mna.api.affinity.Affinity;
import com.mna.api.capabilities.IPlayerMagic;
import java.util.HashMap;
import java.util.Map.Entry;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerMagicProvider implements ICapabilitySerializable<Tag> {

    final String KEY_CASTING_RESOURCE_TYPE = "casting_resource_id";

    final String KEY_TELEPORT_SALT = "teleport_salt";

    final String KEY_PORTAL_COOLDOWN = "portal_cooldown";

    final String KEY_RIFT = "rift";

    final String KEY_GRIMOIRE = "grimoire";

    final String KEY_ROTE = "rote";

    final String KEY_AFFINITY = "affinity";

    final String KEY_MAGIC_LEVEL = "magic_level";

    final String KEY_MAGIC_XP = "magic_xp";

    final String KEY_AIR_CASTS = "air_casts";

    final String KEY_AIR_JUMPS = "air_jumps";

    final String KEY_DID_ALLOW_FLIGHT = "did_allow_flying";

    final String KEY_MAX_MANA_MODIFIERS = "max_mana_modifiers";

    final String KEY_LAST_CODEX_ENTRY = "last_codex_entry";

    final String KEY_CANTRIPS = "cantrips";

    final String KEY_ARMOR_REPAIR = "banked_armor_repair";

    final String KEY_SPELL_COLOR = "spell_color_override";

    final String KEY_EMBER_COOLDOWN = "ember_cooldown";

    public static final Capability<IPlayerMagic> MAGIC = CapabilityManager.get(new CapabilityToken<IPlayerMagic>() {
    });

    private final LazyOptional<IPlayerMagic> holder = LazyOptional.of(PlayerMagic::new);

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return MAGIC.orEmpty(cap, this.holder);
    }

    @Override
    public Tag serializeNBT() {
        IPlayerMagic instance = this.holder.orElse(null);
        CompoundTag nbt = new CompoundTag();
        nbt.putString("casting_resource_id", instance.getCastingResource().getRegistryName().toString());
        instance.getCastingResource().writeNBT(nbt);
        nbt.putInt("teleport_salt", instance.getTeleportSalt());
        nbt.putInt("air_casts", instance.getAirCasts());
        nbt.putInt("air_jumps", instance.getAirJumps());
        nbt.putInt("ember_cooldown", instance.getEmberCooldown());
        nbt.putInt("spell_color_override", instance.getParticleColorOverride());
        nbt.put("rift", this.saveInventory(instance.getRiftInventory()));
        nbt.put("grimoire", this.saveInventory(instance.getGrimoireInventory()));
        nbt.put("rote", this.saveInventory(instance.getRoteInventory()));
        nbt.putBoolean("did_allow_flying", instance.didAllowFlying());
        for (Affinity aff : Affinity.values()) {
            nbt.putFloat("affinity_" + aff.toString().toLowerCase(), instance.getAffinityDepth(aff));
        }
        nbt.putInt("magic_level", instance.getMagicLevel());
        nbt.putInt("magic_xp", instance.getMagicXP());
        instance.getChronoAnchorData().writeToNBT(nbt);
        nbt.put("cantrips", instance.getCantripData().writeToNBT(false));
        ListTag armor_repair = new ListTag();
        for (Entry<Integer, Float> bank_slot : instance.getBankedArmorRepair().entrySet()) {
            CompoundTag bank = new CompoundTag();
            bank.putInt("key", (Integer) bank_slot.getKey());
            bank.putFloat("value", (Float) bank_slot.getValue());
            armor_repair.add(bank);
        }
        nbt.put("banked_armor_repair", armor_repair);
        return nbt;
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        IPlayerMagic instance = this.holder.orElse(null);
        if (nbt instanceof CompoundTag cnbt) {
            if (cnbt.contains("magic_level")) {
                instance.setMagicLevel(null, cnbt.getInt("magic_level"));
            }
            if (cnbt.contains("casting_resource_id")) {
                instance.setCastingResourceType(new ResourceLocation(cnbt.getString("casting_resource_id")));
            }
            instance.getCastingResource().readNBT(cnbt);
            instance.getCastingResource().setNeedsSync();
            if (cnbt.contains("teleport_salt")) {
                instance.setTeleportSalt(cnbt.getInt("teleport_salt"));
            }
            if (cnbt.contains("portal_cooldown")) {
                instance.setPortalCooldown(cnbt.getInt("portal_cooldown"));
            }
            if (cnbt.contains("spell_color_override")) {
                instance.setParticleColorOverride(cnbt.getInt("spell_color_override"));
            }
            if (cnbt.contains("air_casts")) {
                instance.setAirCasts(cnbt.getInt("air_casts"));
            }
            if (cnbt.contains("air_jumps")) {
                instance.setAirJumps(cnbt.getInt("air_jumps"));
            }
            if (cnbt.contains("ember_cooldown")) {
                instance.setEmberCooldown(cnbt.getInt("ember_cooldown"));
            }
            if (cnbt.contains("did_allow_flying")) {
                instance.setDidAllowFlying(cnbt.getBoolean("did_allow_flying"));
            }
            if (cnbt.contains("rift")) {
                this.readInventory(instance.getRiftInventory(), cnbt.getCompound("rift"));
            }
            if (cnbt.contains("grimoire")) {
                this.readInventory(instance.getGrimoireInventory(), cnbt.getCompound("grimoire"));
            }
            if (cnbt.contains("rote")) {
                this.readInventory(instance.getRoteInventory(), cnbt.getCompound("rote"));
            }
            if (cnbt.contains("magic_xp")) {
                instance.setMagicXP(cnbt.getInt("magic_xp"));
            }
            for (Affinity aff : Affinity.values()) {
                if (cnbt.contains("affinity_" + aff.toString().toLowerCase())) {
                    instance.setAffinityDepth(aff, cnbt.getFloat("affinity_" + aff.toString().toLowerCase()));
                }
            }
            instance.getChronoAnchorData().readFromNBT(cnbt);
            if (cnbt.contains("cantrips")) {
                instance.getCantripData().readFromNBT((CompoundTag) cnbt.get("cantrips"));
            }
            if (cnbt.contains("banked_armor_repair")) {
                ListTag banked_armor = cnbt.getList("banked_armor_repair", 10);
                HashMap<Integer, Float> parsed_banked_armor = new HashMap();
                for (int i = 0; i < banked_armor.size(); i++) {
                    CompoundTag bank = banked_armor.getCompound(i);
                    if (bank.contains("key") && bank.contains("value")) {
                        int key = bank.getInt("key");
                        float value = bank.getFloat("value");
                        parsed_banked_armor.put(key, value);
                    }
                }
                instance.setBankedArmorRepair(parsed_banked_armor);
            }
            instance.validate();
            instance.forceSync();
        } else {
            ManaAndArtifice.LOGGER.error("Mana NBT passed back not an instance of CompoundNBT - save data was NOT loaded!");
        }
    }

    private CompoundTag saveInventory(Container inventory) {
        NonNullList<ItemStack> inventoryItems = NonNullList.create();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            inventoryItems.add(inventory.getItem(i));
        }
        CompoundTag inv = new CompoundTag();
        ContainerHelper.saveAllItems(inv, inventoryItems);
        return inv;
    }

    private void readInventory(Container inventory, CompoundTag nbt) {
        NonNullList<ItemStack> inventoryItems = NonNullList.withSize(inventory.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, inventoryItems);
        for (int i = 0; i < inventoryItems.size(); i++) {
            if (inventory.getContainerSize() > i) {
                inventory.setItem(i, inventoryItems.get(i));
            }
        }
    }
}