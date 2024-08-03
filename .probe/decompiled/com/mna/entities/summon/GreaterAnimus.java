package com.mna.entities.summon;

import com.google.common.collect.Multimap;
import com.mna.api.ManaAndArtificeMod;
import com.mna.api.entities.ai.CastSpellAtTargetGoal;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.tools.MATags;
import com.mna.enchantments.framework.EnchantmentInit;
import com.mna.entities.EntityInit;
import com.mna.spells.crafting.SpellRecipe;
import com.mna.tools.InventoryUtilities;
import com.mna.tools.SummonUtils;
import java.util.Arrays;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;

public class GreaterAnimus extends Monster implements IEntityAdditionalSpawnData, Enemy, RangedAttackMob, CrossbowAttackMob {

    private final RangedAttackGoal genericRangedGoal = new RangedAttackGoal(this, 1.0, 20, 15.0F);

    private final RangedBowAttackGoal<GreaterAnimus> bowGoal = new RangedBowAttackGoal(this, 1.0, 20, 15.0F);

    private final RangedCrossbowAttackGoal<GreaterAnimus> crossbowGoal = new RangedCrossbowAttackGoal<>(this, 1.0, 20.0F);

    private final MeleeAttackGoal meleeGoal = new MeleeAttackGoal(this, 1.2, false);

    private final CastSpellAtTargetGoal<GreaterAnimus> spellGoal = new CastSpellAtTargetGoal<GreaterAnimus>(this, ItemStack.EMPTY, 0.0, -1, 16.0F, false) {

        @Override
        public void onSpellCast(ISpellDefinition spell) {
            this.getEntity().consumeMana(spell);
        }

        @Override
        public void start() {
            super.start();
            ISpellDefinition recipe = ManaAndArtificeMod.getSpellHelper().parseSpellDefinition(this.spell);
            this.attackTime = this.attackCooldown == -1 ? recipe.getCooldown(null) : this.attackCooldown;
        }
    };

    private static final EntityDataAccessor<Boolean> IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(GreaterAnimus.class, EntityDataSerializers.BOOLEAN);

    private static final String NBT_HELDITEM = "held";

    private static final String NBT_MANA = "mana";

    private ItemStack heldItem;

    private float mana;

    private boolean isSpellCaster = false;

    public GreaterAnimus(EntityType<? extends Monster> type, Level world) {
        super(type, world);
        this.heldItem = ItemStack.EMPTY;
    }

    public GreaterAnimus(Level world, ItemStack stack, float mana) {
        this(EntityInit.GREATER_ANIMUS.get(), world);
        this.heldItem = stack;
        this.mana = mana;
        this.reassessLifeGoals();
    }

    public void reassessLifeGoals() {
        if (this.m_9236_() != null && !this.m_9236_().isClientSide()) {
            this.f_21345_.removeGoal(this.meleeGoal);
            this.f_21345_.removeGoal(this.bowGoal);
            this.f_21345_.removeGoal(this.spellGoal);
            if (SpellRecipe.stackContainsSpell(this.heldItem)) {
                this.spellGoal.setSpell(this.heldItem);
                this.f_21345_.addGoal(2, this.spellGoal);
                this.isSpellCaster = true;
            } else if (this.heldItem.is(Items.BOW)) {
                int i = 20;
                this.bowGoal.setMinAttackInterval(i);
                this.f_21345_.addGoal(4, this.bowGoal);
            } else if (this.heldItem.is(Items.CROSSBOW)) {
                this.f_21345_.addGoal(4, this.crossbowGoal);
            } else if (MATags.isItemIn(this.heldItem.getItem(), MATags.Items.ANIMUS_BOWS)) {
                this.f_21345_.addGoal(4, this.genericRangedGoal);
            } else {
                Multimap<Attribute, AttributeModifier> attrs = this.heldItem.getItem().getAttributeModifiers(EquipmentSlot.MAINHAND, this.heldItem);
                attrs.forEach((a, m) -> {
                    try {
                        AttributeInstance ai = this.m_21051_(a);
                        if (ai != null) {
                            ai.addPermanentModifier(m);
                        }
                    } catch (Throwable var4) {
                    }
                });
                this.f_21345_.addGoal(4, this.meleeGoal);
            }
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 35.0).add(Attributes.MOVEMENT_SPEED, 0.33F).add(Attributes.ATTACK_DAMAGE, 3.0);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return !pSource.is(DamageTypes.CACTUS) && !pSource.is(DamageTypes.DROWN) && !pSource.is(DamageTypes.DRY_OUT) && !pSource.is(DamageTypes.FALL) && !pSource.is(DamageTypes.HOT_FLOOR) && !pSource.is(DamageTypes.STARVE) && !pSource.is(DamageTypes.SWEET_BERRY_BUSH) ? super.m_6469_(pSource, pAmount) : false;
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(IS_CHARGING_CROSSBOW, false);
    }

    @Override
    protected void registerGoals() {
        this.f_21346_.addGoal(0, new HurtByTargetGoal(this));
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Arrays.asList();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot pSlot) {
        return pSlot != EquipmentSlot.MAINHAND ? ItemStack.EMPTY : this.heldItem;
    }

    @Override
    public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack) {
        if (pSlot == EquipmentSlot.MAINHAND) {
            this.heldItem = pStack.copy();
        }
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.LEFT;
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeItemStack(this.heldItem, false);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.heldItem = additionalData.readItem();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.m_7380_(pCompound);
        pCompound.putFloat("mana", this.mana);
        if (!this.heldItem.isEmpty()) {
            pCompound.put("held", this.heldItem.save(new CompoundTag()));
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.m_7378_(pCompound);
        this.mana = pCompound.getFloat("mana");
        if (pCompound.contains("held")) {
            this.heldItem = ItemStack.of(pCompound.getCompound("held"));
        }
    }

    @Override
    protected void dropAllDeathLoot(DamageSource pDamageSource) {
        LivingEntity summoner = SummonUtils.getSummoner(this);
        if (!this.heldItem.isEmpty() && summoner != null && summoner instanceof Player) {
            ItemStack dropStack = this.heldItem.copy();
            Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(dropStack);
            if ((enchants.containsKey(Enchantments.LOYALTY) || enchants.containsKey(EnchantmentInit.RETURNING.get())) && summoner.isAlive()) {
                dropStack = InventoryUtilities.mergeToPlayerInvPrioritizeOffhand((Player) summoner, dropStack);
            }
            if (!dropStack.isEmpty()) {
                ItemEntity entityItem = new ItemEntity(this.m_9236_(), this.m_20182_().x, this.m_20182_().y, this.m_20182_().z, dropStack);
                this.m_9236_().m_7967_(entityItem);
            }
        }
    }

    @Override
    public void performRangedAttack(LivingEntity pTarget, float pDistanceFactor) {
        ItemStack itemstack = this.m_6298_(this.m_21120_(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof BowItem)));
        AbstractArrow abstractarrow = this.getArrow(itemstack, pDistanceFactor);
        if (this.m_21205_().getItem() instanceof BowItem) {
            abstractarrow = ((BowItem) this.m_21205_().getItem()).customArrow(abstractarrow);
        }
        double d0 = pTarget.m_20185_() - this.m_20185_();
        double d1 = pTarget.m_20227_(0.3333333333333333) - abstractarrow.m_20186_();
        double d2 = pTarget.m_20189_() - this.m_20189_();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        abstractarrow.shoot(d0, d1 + d3 * 0.2F, d2, 1.6F, (float) (14 - this.m_9236_().m_46791_().getId() * 4));
        this.m_5496_(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.m_217043_().nextFloat() * 0.4F + 0.8F));
        this.m_9236_().m_7967_(abstractarrow);
    }

    protected AbstractArrow getArrow(ItemStack pArrowStack, float pDistanceFactor) {
        return ProjectileUtil.getMobArrow(this, pArrowStack, pDistanceFactor);
    }

    public void consumeMana(ISpellDefinition spell) {
        if (!this.m_9236_().isClientSide()) {
            this.mana = this.mana - spell.getManaCost();
            if (this.mana <= 0.0F) {
                this.dropAllDeathLoot(this.m_269291_().fellOutOfWorld());
                this.m_142687_(Entity.RemovalReason.KILLED);
            }
        }
    }

    @Override
    public void setChargingCrossbow(boolean pIsCharging) {
        this.f_19804_.set(IS_CHARGING_CROSSBOW, pIsCharging);
    }

    public boolean isChargingCrossbow() {
        return this.f_19804_.get(IS_CHARGING_CROSSBOW);
    }

    public AbstractIllager.IllagerArmPose getArmPose() {
        if (this.isChargingCrossbow()) {
            return AbstractIllager.IllagerArmPose.CROSSBOW_CHARGE;
        } else if (this.m_21093_(is -> is.getItem() instanceof CrossbowItem)) {
            return AbstractIllager.IllagerArmPose.CROSSBOW_HOLD;
        } else if (this.m_5912_()) {
            if (this.isSpellCaster) {
                return AbstractIllager.IllagerArmPose.SPELLCASTING;
            } else {
                return this.m_21093_(is -> is.getItem() instanceof BowItem) ? AbstractIllager.IllagerArmPose.BOW_AND_ARROW : AbstractIllager.IllagerArmPose.ATTACKING;
            }
        } else {
            return AbstractIllager.IllagerArmPose.NEUTRAL;
        }
    }

    @Override
    public void shootCrossbowProjectile(LivingEntity livingEntity0, ItemStack itemStack1, Projectile projectile2, float float3) {
        this.m_32322_(this, livingEntity0, projectile2, float3, 1.6F);
    }

    @Override
    public void onCrossbowAttackPerformed() {
    }
}