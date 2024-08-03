package org.violetmoon.quark.addons.oddities.entity;

import java.util.LinkedList;
import java.util.List;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.addons.oddities.item.BackpackItem;
import org.violetmoon.quark.addons.oddities.module.TotemOfHoldingModule;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.tweaks.compat.TotemOfHoldingCuriosCompat;

public class TotemOfHoldingEntity extends Entity {

    private static final String TAG_ITEMS = "storedItems";

    private static final String TAG_CURIOS = "curiosList";

    private static final String TAG_DYING = "dying";

    private static final String TAG_OWNER = "owner";

    private static final EntityDataAccessor<Boolean> DYING = SynchedEntityData.defineId(TotemOfHoldingEntity.class, EntityDataSerializers.BOOLEAN);

    public static final int DEATH_TIME = 40;

    private int deathTicks = 0;

    private String owner;

    private List<ItemStack> storedItems = new LinkedList();

    private List<ItemStack> equipedCurios = new LinkedList();

    public TotemOfHoldingEntity(EntityType<? extends TotemOfHoldingEntity> entityType, Level worldIn) {
        super(entityType, worldIn);
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(DYING, false);
    }

    public void addItem(ItemStack stack) {
        this.storedItems.add(stack);
    }

    public void addCurios(ItemStack stack) {
        this.equipedCurios.add(stack);
    }

    public void setOwner(Player player) {
        this.owner = player.m_20148_().toString();
    }

    private Player getOwnerEntity() {
        for (Player player : this.m_9236_().m_6907_()) {
            String uuid = player.m_20148_().toString();
            if (uuid.equals(this.owner)) {
                return player;
            }
        }
        return null;
    }

    @Override
    public boolean skipAttackInteraction(@NotNull Entity e) {
        if (!this.m_9236_().isClientSide && e instanceof Player player) {
            if (!TotemOfHoldingModule.allowAnyoneToCollect && !player.getAbilities().instabuild) {
                Player owner = this.getOwnerEntity();
                if (e != owner) {
                    return false;
                }
            }
            int drops = Math.min(this.storedItems.size(), 3 + this.m_9236_().random.nextInt(4));
            for (int i = 0; i < drops; i++) {
                ItemStack stack = (ItemStack) this.storedItems.remove(0);
                if (stack.getItem() instanceof ArmorItem armor) {
                    EquipmentSlot slot = armor.getEquipmentSlot();
                    ItemStack curr = player.getItemBySlot(slot);
                    if (curr.isEmpty()) {
                        player.setItemSlot(slot, stack);
                        stack = null;
                    } else if (!(curr.getItem() instanceof BackpackItem) && !EnchantmentHelper.hasBindingCurse(curr) && !EnchantmentHelper.hasBindingCurse(stack)) {
                        player.setItemSlot(slot, stack);
                        stack = curr;
                    }
                } else if (stack.getItem() instanceof ShieldItem) {
                    ItemStack curr = player.getItemBySlot(EquipmentSlot.OFFHAND);
                    if (curr.isEmpty()) {
                        player.setItemSlot(EquipmentSlot.OFFHAND, stack);
                        stack = null;
                    }
                } else if (Quark.ZETA.isModLoaded("curios")) {
                    stack = TotemOfHoldingCuriosCompat.equipCurios(player, this.equipedCurios, stack);
                }
                if (stack != null && !player.addItem(stack)) {
                    this.m_5552_(stack, 0.0F);
                }
            }
            if (this.m_9236_() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.DAMAGE_INDICATOR, this.m_20185_(), this.m_20186_() + 0.5, this.m_20189_(), drops, 0.1, 0.5, 0.1, 0.0);
                serverLevel.sendParticles(ParticleTypes.ENCHANTED_HIT, this.m_20185_(), this.m_20186_() + 0.5, this.m_20189_(), drops, 0.4, 0.5, 0.4, 0.0);
            }
        }
        return false;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.m_6084_()) {
            if (TotemOfHoldingModule.darkSoulsMode) {
                Player owner = this.getOwnerEntity();
                if (owner != null && !this.m_9236_().isClientSide) {
                    String ownerTotem = TotemOfHoldingModule.getTotemUUID(owner);
                    if (!this.m_20148_().toString().equals(ownerTotem)) {
                        this.dropEverythingAndDie();
                    }
                }
            }
            if (this.storedItems.isEmpty() && !this.m_9236_().isClientSide) {
                this.f_19804_.set(DYING, true);
            }
            if (this.isDying()) {
                if (this.deathTicks > 40) {
                    this.m_146870_();
                } else {
                    this.deathTicks++;
                }
            } else if (this.m_9236_().isClientSide) {
                this.m_9236_().addParticle(ParticleTypes.PORTAL, this.m_20185_(), this.m_20186_() + (Math.random() - 0.5) * 0.2, this.m_20189_(), Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5);
            }
        }
    }

    private void dropEverythingAndDie() {
        if (!TotemOfHoldingModule.destroyLostItems) {
            for (ItemStack storedItem : this.storedItems) {
                this.m_5552_(storedItem, 0.0F);
            }
        }
        this.storedItems.clear();
        this.equipedCurios.clear();
        this.m_146870_();
    }

    public int getDeathTicks() {
        return this.deathTicks;
    }

    public boolean isDying() {
        return this.f_19804_.get(DYING);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        ListTag list = compound.getList("storedItems", 10);
        ListTag curiosList = compound.getList("curiosList", 10);
        this.storedItems = new LinkedList();
        this.equipedCurios = new LinkedList();
        for (int i = 0; i < list.size(); i++) {
            CompoundTag cmp = list.getCompound(i);
            ItemStack stack = ItemStack.of(cmp);
            this.storedItems.add(stack);
        }
        for (int i = 0; i < curiosList.size(); i++) {
            CompoundTag cmp = curiosList.getCompound(i);
            ItemStack stack = ItemStack.of(cmp);
            this.equipedCurios.add(stack);
        }
        boolean dying = compound.getBoolean("dying");
        this.f_19804_.set(DYING, dying);
        this.owner = compound.getString("owner");
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compound) {
        ListTag list = new ListTag();
        ListTag curiosList = new ListTag();
        for (ItemStack stack : this.storedItems) {
            list.add(stack.serializeNBT());
        }
        for (ItemStack equipedCurio : this.equipedCurios) {
            curiosList.add(equipedCurio.serializeNBT());
        }
        compound.put("storedItems", list);
        compound.put("curiosList", curiosList);
        compound.putBoolean("dying", this.isDying());
        if (this.owner != null) {
            compound.putString("owner", this.owner);
        }
    }

    @NotNull
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}