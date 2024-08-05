package noppes.npcs.api.wrapper;

import com.google.common.collect.Multimap;
import com.google.gson.JsonParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.WritableBookItem;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.registries.ForgeRegistries;
import noppes.npcs.ItemStackEmptyWrapper;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.INbt;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.IMob;
import noppes.npcs.api.entity.data.IData;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.items.ItemScripted;

public class ItemStackWrapper implements IItemStack, ICapabilitySerializable<CompoundTag> {

    private Map<String, Object> tempData = new HashMap();

    public static Capability<ItemStackWrapper> ITEMSCRIPTEDDATA_CAPABILITY = CapabilityManager.get(new CapabilityToken<ItemStackWrapper>() {
    });

    private LazyOptional<ItemStackWrapper> instance = LazyOptional.of(() -> this);

    private static final EquipmentSlot[] VALID_EQUIPMENT_SLOTS = new EquipmentSlot[] { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET };

    public ItemStack item;

    private CompoundTag storedData = new CompoundTag();

    public static ItemStackWrapper AIR = new ItemStackEmptyWrapper();

    private final IData tempdata = new IData() {

        @Override
        public void put(String key, Object value) {
            ItemStackWrapper.this.tempData.put(key, value);
        }

        @Override
        public Object get(String key) {
            return ItemStackWrapper.this.tempData.get(key);
        }

        @Override
        public void remove(String key) {
            ItemStackWrapper.this.tempData.remove(key);
        }

        @Override
        public boolean has(String key) {
            return ItemStackWrapper.this.tempData.containsKey(key);
        }

        @Override
        public void clear() {
            ItemStackWrapper.this.tempData.clear();
        }

        @Override
        public String[] getKeys() {
            return (String[]) ItemStackWrapper.this.tempData.keySet().toArray(new String[ItemStackWrapper.this.tempData.size()]);
        }
    };

    private final IData storeddata = new IData() {

        @Override
        public void put(String key, Object value) {
            if (value instanceof Number) {
                ItemStackWrapper.this.storedData.putDouble(key, ((Number) value).doubleValue());
            } else if (value instanceof String) {
                ItemStackWrapper.this.storedData.putString(key, (String) value);
            }
        }

        @Override
        public Object get(String key) {
            if (!ItemStackWrapper.this.storedData.contains(key)) {
                return null;
            } else {
                Tag base = ItemStackWrapper.this.storedData.get(key);
                return base instanceof NumericTag ? ((NumericTag) base).getAsDouble() : base.getAsString();
            }
        }

        @Override
        public void remove(String key) {
            ItemStackWrapper.this.storedData.remove(key);
        }

        @Override
        public boolean has(String key) {
            return ItemStackWrapper.this.storedData.contains(key);
        }

        @Override
        public void clear() {
            ItemStackWrapper.this.storedData = new CompoundTag();
        }

        @Override
        public String[] getKeys() {
            return (String[]) ItemStackWrapper.this.storedData.getAllKeys().toArray(new String[ItemStackWrapper.this.storedData.getAllKeys().size()]);
        }
    };

    private static final ResourceLocation key = new ResourceLocation("customnpcs", "itemscripteddata");

    protected ItemStackWrapper(ItemStack item) {
        this.item = item;
    }

    @Override
    public IData getTempdata() {
        return this.tempdata;
    }

    @Override
    public IData getStoreddata() {
        return this.storeddata;
    }

    @Override
    public int getStackSize() {
        return this.item.getCount();
    }

    @Override
    public void setStackSize(int size) {
        if (size > this.getMaxStackSize()) {
            throw new CustomNPCsException("Can't set the stacksize bigger than MaxStacksize");
        } else {
            this.item.setCount(size);
        }
    }

    @Override
    public void setAttribute(String name, double value) {
        this.setAttribute(name, value, -1);
    }

    @Override
    public void setAttribute(String name, double value, int slot) {
        if (slot >= -1 && slot <= 5) {
            CompoundTag compound = this.item.getTag();
            if (compound == null) {
                this.item.setTag(compound = new CompoundTag());
            }
            ListTag nbttaglist = compound.getList("AttributeModifiers", 10);
            ListTag newList = new ListTag();
            for (int i = 0; i < nbttaglist.size(); i++) {
                CompoundTag c = nbttaglist.getCompound(i);
                if (!c.getString("AttributeName").equals(name)) {
                    newList.add(c);
                }
            }
            if (value != 0.0) {
                CompoundTag nbttagcompound = new AttributeModifier(name, value, AttributeModifier.Operation.ADDITION).save();
                nbttagcompound.putString("AttributeName", name);
                if (slot >= 0) {
                    nbttagcompound.putString("Slot", EquipmentSlot.values()[slot].getName());
                }
                newList.add(nbttagcompound);
            }
            compound.put("AttributeModifiers", newList);
        } else {
            throw new CustomNPCsException("Slot has to be between -1 and 5, given was: " + slot);
        }
    }

    @Override
    public double getAttribute(String name) {
        CompoundTag compound = this.item.getTag();
        if (compound == null) {
            return 0.0;
        } else {
            Multimap<Attribute, AttributeModifier> map = this.item.getAttributeModifiers(EquipmentSlot.MAINHAND);
            for (Entry<Attribute, AttributeModifier> entry : map.entries()) {
                if (((Attribute) entry.getKey()).getDescriptionId().equals(name)) {
                    AttributeModifier mod = (AttributeModifier) entry.getValue();
                    return mod.getAmount();
                }
            }
            return 0.0;
        }
    }

    @Override
    public boolean hasAttribute(String name) {
        CompoundTag compound = this.item.getTag();
        if (compound == null) {
            return false;
        } else {
            ListTag nbttaglist = compound.getList("AttributeModifiers", 10);
            for (int i = 0; i < nbttaglist.size(); i++) {
                CompoundTag c = nbttaglist.getCompound(i);
                if (c.getString("AttributeName").equals(name)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public void addEnchantment(String id, int strenght) {
        Enchantment ench = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(id));
        if (ench == null) {
            throw new CustomNPCsException("Unknown enchant id:" + id);
        } else {
            this.item.enchant(ench, strenght);
        }
    }

    @Override
    public boolean isEnchanted() {
        return this.item.isEnchanted();
    }

    @Override
    public boolean hasEnchant(String id) {
        Enchantment ench = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(id));
        if (ench == null) {
            throw new CustomNPCsException("Unknown enchant id:" + id);
        } else if (!this.isEnchanted()) {
            return false;
        } else {
            ListTag list = this.item.getEnchantmentTags();
            for (int i = 0; i < list.size(); i++) {
                CompoundTag compound = list.getCompound(i);
                if (compound.getString("id").equalsIgnoreCase(id)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public boolean removeEnchant(String id) {
        Enchantment ench = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(id));
        if (ench == null) {
            throw new CustomNPCsException("Unknown enchant id:" + id);
        } else if (!this.isEnchanted()) {
            return false;
        } else {
            ListTag list = this.item.getEnchantmentTags();
            ListTag newList = new ListTag();
            for (int i = 0; i < list.size(); i++) {
                CompoundTag compound = list.getCompound(i);
                if (!compound.getString("id").equalsIgnoreCase(id)) {
                    newList.add(compound);
                }
            }
            if (list.size() == newList.size()) {
                return false;
            } else {
                this.item.getTag().put("ench", newList);
                return true;
            }
        }
    }

    @Override
    public boolean isBlock() {
        Block block = Block.byItem(this.item.getItem());
        return block != null && block != Blocks.AIR;
    }

    @Override
    public boolean hasCustomName() {
        return this.item.hasCustomHoverName();
    }

    @Override
    public void setCustomName(String name) {
        this.item.setHoverName(Component.translatable(name));
    }

    @Override
    public String getDisplayName() {
        return this.item.getHoverName().getString();
    }

    @Override
    public String getItemName() {
        return this.item.getItem().getName(this.item).getString();
    }

    @Override
    public String getName() {
        return ForgeRegistries.ITEMS.getKey(this.item.getItem()).toString();
    }

    @Override
    public INbt getNbt() {
        CompoundTag compound = this.item.getTag();
        if (compound == null) {
            this.item.setTag(compound = new CompoundTag());
        }
        return NpcAPI.Instance().getINbt(compound);
    }

    @Override
    public boolean hasNbt() {
        CompoundTag compound = this.item.getTag();
        return compound != null && !compound.isEmpty();
    }

    @Override
    public ItemStack getMCItemStack() {
        return this.item;
    }

    public static ItemStack MCItem(IItemStack item) {
        return item == null ? ItemStack.EMPTY : item.getMCItemStack();
    }

    @Override
    public void damageItem(int damage, IMob living) {
        if (living != null) {
            this.item.hurtAndBreak(damage, living == null ? null : living.getMCEntity(), e -> e.m_21166_(EquipmentSlot.MAINHAND));
        } else if (this.item.isDamageableItem()) {
            if (this.item.getDamageValue() <= damage) {
                this.item.shrink(1);
                this.item.setDamageValue(0);
            } else {
                this.item.setDamageValue(this.item.getDamageValue() - damage);
            }
        }
    }

    @Override
    public boolean isBook() {
        return false;
    }

    @Override
    public int getFoodLevel() {
        return this.item.getItem().getFoodProperties() != null ? this.item.getItem().getFoodProperties().getNutrition() : 0;
    }

    @Override
    public IItemStack copy() {
        return createNew(this.item.copy());
    }

    @Override
    public int getMaxStackSize() {
        return this.item.getMaxStackSize();
    }

    @Override
    public boolean isDamageable() {
        return this.item.isDamageableItem();
    }

    @Override
    public int getDamage() {
        return this.item.getDamageValue();
    }

    @Override
    public void setDamage(int value) {
        this.item.setDamageValue(value);
    }

    @Deprecated
    public int getItemDamage() {
        return this.item.getDamageValue();
    }

    @Deprecated
    public void setItemDamage(int value) {
        this.item.setDamageValue(value);
    }

    @Override
    public int getMaxDamage() {
        return this.item.getMaxDamage();
    }

    @Override
    public INbt getItemNbt() {
        CompoundTag compound = new CompoundTag();
        this.item.save(compound);
        return NpcAPI.Instance().getINbt(compound);
    }

    @Override
    public double getAttackDamage() {
        Multimap<Attribute, AttributeModifier> map = this.item.getAttributeModifiers(EquipmentSlot.MAINHAND);
        double damage = 0.0;
        for (Entry<Attribute, AttributeModifier> entry : map.entries()) {
            if (entry.getKey() == Attributes.ATTACK_DAMAGE) {
                AttributeModifier mod = (AttributeModifier) entry.getValue();
                damage = mod.getAmount();
            }
        }
        return damage + (double) EnchantmentHelper.getDamageBonus(this.item, MobType.UNDEFINED);
    }

    @Override
    public boolean isEmpty() {
        return this.item.isEmpty();
    }

    @Override
    public int getType() {
        if (this.item.getItem() instanceof IPlantable) {
            return 5;
        } else {
            return this.item.getItem() instanceof SwordItem ? 4 : 0;
        }
    }

    @Override
    public boolean isWearable() {
        for (EquipmentSlot slot : VALID_EQUIPMENT_SLOTS) {
            if (this.item.getItem().canEquip(this.item, slot, EntityNPCInterface.CommandPlayer)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
        return capability == ITEMSCRIPTEDDATA_CAPABILITY ? this.instance.cast() : LazyOptional.empty();
    }

    public static void register(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStackWrapper wrapper = createNew(event.getObject());
        event.addCapability(key, wrapper);
    }

    private static ItemStackWrapper createNew(ItemStack item) {
        if (item == null || item.isEmpty()) {
            return AIR;
        } else if (item.getItem() instanceof ItemScripted) {
            return new ItemScriptedWrapper(item);
        } else if (item.getItem() == Items.WRITTEN_BOOK || item.getItem() == Items.WRITABLE_BOOK || item.getItem() instanceof WritableBookItem || item.getItem() instanceof WrittenBookItem) {
            return new ItemBookWrapper(item);
        } else if (item.getItem() instanceof ArmorItem) {
            return new ItemArmorWrapper(item);
        } else {
            Block block = Block.byItem(item.getItem());
            return (ItemStackWrapper) (block != Blocks.AIR ? new ItemBlockWrapper(item) : new ItemStackWrapper(item));
        }
    }

    @Override
    public String[] getLore() {
        CompoundTag compound = this.item.getTagElement("display");
        if (compound != null && compound.getTagType("Lore") == 9) {
            ListTag nbttaglist = compound.getList("Lore", 8);
            if (nbttaglist.isEmpty()) {
                return new String[0];
            } else {
                List<String> lore = new ArrayList();
                for (int i = 0; i < nbttaglist.size(); i++) {
                    lore.add(nbttaglist.getString(i));
                }
                return (String[]) lore.toArray(new String[lore.size()]);
            }
        } else {
            return new String[0];
        }
    }

    @Override
    public void setLore(String[] lore) {
        CompoundTag compound = this.item.getOrCreateTagElement("display");
        if (lore != null && lore.length != 0) {
            ListTag nbtlist = new ListTag();
            for (String s : lore) {
                try {
                    Component.Serializer.fromJson(s);
                } catch (JsonParseException var9) {
                    s = Component.Serializer.toJson(Component.translatable(s));
                }
                nbtlist.add(StringTag.valueOf(s));
            }
            compound.put("Lore", nbtlist);
        } else {
            compound.remove("Lore");
        }
    }

    public CompoundTag serializeNBT() {
        return this.getMCNbt();
    }

    public void deserializeNBT(CompoundTag nbt) {
        this.setMCNbt(nbt);
    }

    public CompoundTag getMCNbt() {
        CompoundTag compound = new CompoundTag();
        if (!this.storedData.isEmpty()) {
            compound.put("StoredData", this.storedData);
        }
        return compound;
    }

    public void setMCNbt(CompoundTag compound) {
        this.storedData = compound.getCompound("StoredData");
    }

    @Override
    public void removeNbt() {
        this.item.setTag(null);
    }

    @Override
    public boolean compare(IItemStack item, boolean ignoreNBT) {
        if (item == null) {
            item = AIR;
        }
        return NoppesUtilPlayer.compareItems(this.getMCItemStack(), item.getMCItemStack(), false, ignoreNBT);
    }
}