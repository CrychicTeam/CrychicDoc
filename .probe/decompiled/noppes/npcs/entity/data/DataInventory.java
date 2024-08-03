package noppes.npcs.entity.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.ForgeHooks;
import noppes.npcs.NBTTags;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.data.INPCInventory;
import noppes.npcs.api.event.NpcEvent;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.api.wrapper.ItemStackWrapper;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.ValueUtil;

public class DataInventory extends SimpleContainer implements INPCInventory {

    public Map<Integer, IItemStack> drops = new HashMap();

    public Map<Integer, Float> dropchance = new HashMap();

    public Map<Integer, IItemStack> weapons = new HashMap();

    public Map<Integer, IItemStack> armor = new HashMap();

    private int minExp = 0;

    private int maxExp = 0;

    public int lootMode = 0;

    private EntityNPCInterface npc;

    public DataInventory(EntityNPCInterface npc) {
        super();
        this.npc = npc;
    }

    public CompoundTag save(CompoundTag nbttagcompound) {
        nbttagcompound.putInt("MinExp", this.minExp);
        nbttagcompound.putInt("MaxExp", this.maxExp);
        nbttagcompound.put("NpcInv", NBTTags.nbtIItemStackMap(this.drops));
        nbttagcompound.put("Armor", NBTTags.nbtIItemStackMap(this.armor));
        nbttagcompound.put("Weapons", NBTTags.nbtIItemStackMap(this.weapons));
        nbttagcompound.put("DropChance", NBTTags.nbtFloatMap(this.dropchance));
        nbttagcompound.putInt("LootMode", this.lootMode);
        return nbttagcompound;
    }

    public void load(CompoundTag nbttagcompound) {
        this.minExp = nbttagcompound.getInt("MinExp");
        this.maxExp = nbttagcompound.getInt("MaxExp");
        this.drops = NBTTags.getIItemStackMap(nbttagcompound.getList("NpcInv", 10));
        this.armor = NBTTags.getIItemStackMap(nbttagcompound.getList("Armor", 10));
        this.weapons = NBTTags.getIItemStackMap(nbttagcompound.getList("Weapons", 10));
        this.dropchance = NBTTags.getFloatIntegerMap(nbttagcompound.getList("DropChance", 10));
        this.lootMode = nbttagcompound.getInt("LootMode");
    }

    @Override
    public IItemStack getArmor(int slot) {
        return (IItemStack) this.armor.get(slot);
    }

    @Override
    public void setArmor(int slot, IItemStack item) {
        this.armor.put(slot, item);
        this.npc.updateClient = true;
    }

    @Override
    public IItemStack getRightHand() {
        return (IItemStack) this.weapons.get(0);
    }

    @Override
    public void setRightHand(IItemStack item) {
        this.weapons.put(0, item);
        this.npc.updateClient = true;
    }

    @Override
    public IItemStack getProjectile() {
        return (IItemStack) this.weapons.get(1);
    }

    @Override
    public void setProjectile(IItemStack item) {
        this.weapons.put(1, item);
        this.npc.updateAI = true;
    }

    @Override
    public IItemStack getLeftHand() {
        return (IItemStack) this.weapons.get(2);
    }

    @Override
    public void setLeftHand(IItemStack item) {
        this.weapons.put(2, item);
        this.npc.updateClient = true;
    }

    @Override
    public IItemStack getDropItem(int slot) {
        if (slot >= 0 && slot <= 20) {
            IItemStack item = (IItemStack) this.npc.inventory.drops.get(slot);
            return (IItemStack) (item == null ? ItemStackWrapper.AIR : NpcAPI.Instance().getIItemStack(item.getMCItemStack()));
        } else {
            throw new CustomNPCsException("Bad slot number: " + slot);
        }
    }

    @Override
    public void setDropItem(int slot, IItemStack item, float chance) {
        if (slot >= 0 && slot <= 20) {
            chance = ValueUtil.correctFloat(chance, 1.0F, 100.0F);
            if (item != null && !item.isEmpty()) {
                this.dropchance.put(slot, chance);
                this.drops.put(slot, item);
            } else {
                this.dropchance.remove(slot);
                this.drops.remove(slot);
            }
        } else {
            throw new CustomNPCsException("Bad slot number: " + slot);
        }
    }

    @Override
    public IItemStack[] getItemsRNG() {
        ArrayList<IItemStack> list = new ArrayList();
        for (int i : this.drops.keySet()) {
            IItemStack item = (IItemStack) this.drops.get(i);
            if (item != null && !item.isEmpty()) {
                float dchance = 100.0F;
                if (this.dropchance.containsKey(i)) {
                    dchance = (Float) this.dropchance.get(i);
                }
                float chance = (float) this.npc.m_9236_().random.nextInt(100) + dchance;
                if (chance >= 100.0F) {
                    list.add(item);
                }
            }
        }
        return (IItemStack[]) list.toArray(new IItemStack[list.size()]);
    }

    public void dropStuff(NpcEvent.DiedEvent event, Entity entity, DamageSource damagesource) {
        ArrayList<ItemEntity> list = new ArrayList();
        if (event.droppedItems != null) {
            for (IItemStack item : event.droppedItems) {
                ItemEntity e = this.getItemEntity(item.getMCItemStack().copy());
                if (e != null) {
                    list.add(e);
                }
            }
        }
        int enchant = 0;
        if (damagesource.getEntity() instanceof Player) {
            enchant = EnchantmentHelper.getMobLooting((LivingEntity) damagesource.getEntity());
        }
        if (!ForgeHooks.onLivingDrops(this.npc, damagesource, list, enchant, true)) {
            for (ItemEntity itemx : list) {
                if (this.lootMode == 1 && entity instanceof Player) {
                    Player player = (Player) entity;
                    itemx.setPickUpDelay(2);
                    this.npc.m_9236_().m_7967_(itemx);
                    ItemStack stack = itemx.getItem();
                    int i = stack.getCount();
                    if (player.getInventory().add(stack)) {
                        entity.level().playSound(null, player.m_20185_(), player.m_20186_(), player.m_20189_(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((player.m_217043_().nextFloat() - player.m_217043_().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                        player.m_7938_(itemx, i);
                        if (stack.getCount() <= 0) {
                            itemx.m_142687_(Entity.RemovalReason.DISCARDED);
                        }
                    }
                } else {
                    this.npc.m_9236_().m_7967_(itemx);
                }
            }
        }
        int exp = event.expDropped;
        while (exp > 0) {
            int var2x = ExperienceOrb.getExperienceValue(exp);
            exp -= var2x;
            if (this.lootMode == 1 && entity instanceof Player) {
                this.npc.m_9236_().m_7967_(new ExperienceOrb(entity.level(), entity.getX(), entity.getY(), entity.getZ(), var2x));
            } else {
                this.npc.m_9236_().m_7967_(new ExperienceOrb(this.npc.m_9236_(), this.npc.m_20185_(), this.npc.m_20186_(), this.npc.m_20189_(), var2x));
            }
        }
    }

    public ItemEntity getItemEntity(ItemStack itemstack) {
        if (itemstack != null && !itemstack.isEmpty()) {
            ItemEntity entityitem = new ItemEntity(this.npc.m_9236_(), this.npc.m_20185_(), this.npc.m_20186_() - 0.3F + (double) this.npc.m_20192_(), this.npc.m_20189_(), itemstack);
            entityitem.setPickUpDelay(40);
            float f2 = this.npc.m_217043_().nextFloat() * 0.5F;
            float f4 = this.npc.m_217043_().nextFloat() * 3.141593F * 2.0F;
            entityitem.m_20334_((double) (-Mth.sin(f4) * f2), 0.2F, (double) (Mth.cos(f4) * f2));
            return entityitem;
        } else {
            return null;
        }
    }

    @Override
    public int getContainerSize() {
        return 15;
    }

    @Override
    public ItemStack getItem(int i) {
        if (i < 4) {
            return ItemStackWrapper.MCItem(this.getArmor(i));
        } else {
            return i < 7 ? ItemStackWrapper.MCItem((IItemStack) this.weapons.get(i - 4)) : ItemStackWrapper.MCItem((IItemStack) this.drops.get(i - 7));
        }
    }

    @Override
    public ItemStack removeItem(int par1, int limbSwingAmount) {
        int i = 0;
        Map<Integer, IItemStack> var3;
        if (par1 >= 7) {
            var3 = this.drops;
            par1 -= 7;
        } else if (par1 >= 4) {
            var3 = this.weapons;
            par1 -= 4;
            i = 1;
        } else {
            var3 = this.armor;
            i = 2;
        }
        ItemStack var4 = null;
        if (var3.get(par1) != null) {
            if (((IItemStack) var3.get(par1)).getMCItemStack().getCount() <= limbSwingAmount) {
                var4 = ((IItemStack) var3.get(par1)).getMCItemStack();
                var3.put(par1, null);
            } else {
                var4 = ((IItemStack) var3.get(par1)).getMCItemStack().split(limbSwingAmount);
                if (((IItemStack) var3.get(par1)).getMCItemStack().getCount() == 0) {
                    var3.put(par1, null);
                }
            }
        }
        if (i == 1) {
            this.weapons = var3;
        }
        if (i == 2) {
            this.armor = var3;
        }
        return var4 == null ? ItemStack.EMPTY : var4;
    }

    @Override
    public ItemStack removeItemNoUpdate(int par1) {
        int i = 0;
        Map<Integer, IItemStack> var2;
        if (par1 >= 7) {
            var2 = this.drops;
            par1 -= 7;
        } else if (par1 >= 4) {
            var2 = this.weapons;
            par1 -= 4;
            i = 1;
        } else {
            var2 = this.armor;
            i = 2;
        }
        if (var2.get(par1) != null) {
            ItemStack var3 = ((IItemStack) var2.get(par1)).getMCItemStack();
            var2.put(par1, null);
            if (i == 1) {
                this.weapons = var2;
            }
            if (i == 2) {
                this.armor = var2;
            }
            return var3;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setItem(int par1, ItemStack limbSwingAmountItemStack) {
        int i = 0;
        Map<Integer, IItemStack> var3;
        if (par1 >= 7) {
            var3 = this.drops;
            par1 -= 7;
        } else if (par1 >= 4) {
            var3 = this.weapons;
            par1 -= 4;
            i = 1;
        } else {
            var3 = this.armor;
            i = 2;
        }
        var3.put(par1, NpcAPI.Instance().getIItemStack(limbSwingAmountItemStack));
        if (i == 1) {
            this.weapons = var3;
        }
        if (i == 2) {
            this.armor = var3;
        }
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public boolean stillValid(Player var1) {
        return true;
    }

    @Override
    public boolean canPlaceItem(int i, ItemStack itemstack) {
        return true;
    }

    @Override
    public void setChanged() {
    }

    @Override
    public void startOpen(Player player) {
    }

    @Override
    public void stopOpen(Player player) {
    }

    @Override
    public int getExpMin() {
        return this.npc.inventory.minExp;
    }

    @Override
    public int getExpMax() {
        return this.npc.inventory.maxExp;
    }

    @Override
    public int getExpRNG() {
        int exp = this.minExp;
        if (this.maxExp - this.minExp > 0) {
            exp += this.npc.m_9236_().random.nextInt(this.maxExp - this.minExp);
        }
        return exp;
    }

    @Override
    public void setExp(int min, int max) {
        min = Math.min(min, max);
        this.npc.inventory.minExp = min;
        this.npc.inventory.maxExp = max;
    }

    @Override
    public boolean isEmpty() {
        for (int slot = 0; slot < this.getContainerSize(); slot++) {
            ItemStack item = this.getItem(slot);
            if (!NoppesUtilServer.IsItemStackNull(item) && !item.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void clearContent() {
    }
}