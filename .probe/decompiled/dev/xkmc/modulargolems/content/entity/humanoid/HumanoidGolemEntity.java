package dev.xkmc.modulargolems.content.entity.humanoid;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import dev.xkmc.modulargolems.content.entity.common.SweepGolemEntity;
import dev.xkmc.modulargolems.content.entity.dog.DogGolemEntity;
import dev.xkmc.modulargolems.content.entity.goals.GolemMeleeGoal;
import dev.xkmc.modulargolems.content.entity.ranged.GolemBowAttackGoal;
import dev.xkmc.modulargolems.content.entity.ranged.GolemCrossbowAttackGoal;
import dev.xkmc.modulargolems.content.entity.ranged.GolemShooterHelper;
import dev.xkmc.modulargolems.content.entity.ranged.GolemTridentAttackGoal;
import dev.xkmc.modulargolems.content.item.golem.GolemHolder;
import dev.xkmc.modulargolems.events.event.GolemBowAttackEvent;
import dev.xkmc.modulargolems.events.event.GolemDamageShieldEvent;
import dev.xkmc.modulargolems.events.event.GolemDisableShieldEvent;
import dev.xkmc.modulargolems.events.event.GolemEquipEvent;
import dev.xkmc.modulargolems.events.event.GolemSweepEvent;
import dev.xkmc.modulargolems.events.event.GolemThrowableEvent;
import dev.xkmc.modulargolems.init.advancement.GolemTriggers;
import dev.xkmc.modulargolems.init.data.MGConfig;
import java.util.Arrays;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolActions;

@SerialClass
public class HumanoidGolemEntity extends SweepGolemEntity<HumanoidGolemEntity, HumaniodGolemPartType> implements CrossbowAttackMob {

    private static final EntityDataAccessor<Boolean> IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(HumanoidGolemEntity.class, EntityDataSerializers.BOOLEAN);

    private final GolemBowAttackGoal bowGoal = new GolemBowAttackGoal(this, 1.0, 20, 15.0F);

    private final GolemCrossbowAttackGoal crossbowGoal = new GolemCrossbowAttackGoal(this, 1.0, 15.0F);

    private final GolemMeleeGoal meleeGoal = new GolemMeleeGoal(this);

    private final GolemTridentAttackGoal tridentGoal = new GolemTridentAttackGoal(this, 1.0, 40, 15.0F, this.meleeGoal);

    @SerialField(toClient = true)
    public int shieldCooldown = 0;

    @SerialField
    private ItemStack backupHand = ItemStack.EMPTY;

    @SerialField
    private ItemStack arrowSlot = ItemStack.EMPTY;

    protected boolean rendering;

    protected boolean render_trigger = false;

    public HumanoidGolemEntity(EntityType<HumanoidGolemEntity> type, Level level) {
        super(type, level);
        if (!this.m_9236_().isClientSide) {
            this.reassessWeaponGoal();
            this.groundNavigation.setCanOpenDoors(true);
        }
    }

    public void reassessWeaponGoal() {
        if (!this.m_9236_().isClientSide) {
            this.f_21345_.removeGoal(this.meleeGoal);
            this.f_21345_.removeGoal(this.bowGoal);
            this.f_21345_.removeGoal(this.crossbowGoal);
            this.f_21345_.removeGoal(this.tridentGoal);
            InteractionHand hand = this.getWeaponHand();
            ItemStack weapon = this.m_21120_(hand);
            if (!weapon.isEmpty() && GolemShooterHelper.isValidThrowableWeapon(this, weapon, hand).isThrowable()) {
                this.f_21345_.addGoal(2, this.tridentGoal);
                this.f_21345_.addGoal(3, this.meleeGoal);
                return;
            }
            if (!weapon.isEmpty() && weapon.getItem() instanceof BowItem) {
                this.bowGoal.setMinAttackInterval(20);
                this.f_21345_.addGoal(2, this.bowGoal);
                return;
            }
            if (!weapon.isEmpty() && weapon.getItem() instanceof CrossbowItem) {
                this.f_21345_.addGoal(2, this.crossbowGoal);
                return;
            }
            this.f_21345_.addGoal(2, this.meleeGoal);
        }
    }

    @Override
    public ItemStack getProjectile(ItemStack pShootable) {
        if (pShootable.getItem() instanceof ProjectileWeaponItem) {
            Predicate<ItemStack> predicate = ((ProjectileWeaponItem) pShootable.getItem()).getSupportedHeldProjectiles();
            ItemStack stack = ProjectileWeaponItem.getHeldProjectile(this, predicate);
            if (stack.isEmpty() && !this.arrowSlot.isEmpty() && predicate.test(this.arrowSlot)) {
                stack = this.arrowSlot;
            }
            return ForgeHooks.getProjectile(this, pShootable, stack);
        } else {
            return ForgeHooks.getProjectile(this, pShootable, ItemStack.EMPTY);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.m_7378_(pCompound);
        this.reassessWeaponGoal();
    }

    @Override
    public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack) {
        super.m_8061_(pSlot, pStack);
        if (!this.m_9236_().isClientSide) {
            this.reassessWeaponGoal();
        }
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(IS_CHARGING_CROSSBOW, false);
    }

    protected AbstractArrow getArrow(ItemStack pArrowStack, float pVelocity) {
        return ProjectileUtil.getMobArrow(this, pArrowStack, pVelocity);
    }

    @Override
    public boolean canFireProjectileWeapon(ProjectileWeaponItem pProjectileWeapon) {
        return true;
    }

    public boolean isChargingCrossbow() {
        return this.f_19804_.get(IS_CHARGING_CROSSBOW);
    }

    @Override
    public void setChargingCrossbow(boolean pIsCharging) {
        this.f_19804_.set(IS_CHARGING_CROSSBOW, pIsCharging);
    }

    @Override
    public void shootCrossbowProjectile(LivingEntity pTarget, ItemStack pCrossbowStack, Projectile pProjectile, float pProjectileAngle) {
        this.shootCrossbowProjectile(this, pTarget, pProjectile, pProjectileAngle, 1.6F);
    }

    @Override
    public void shootCrossbowProjectile(LivingEntity pUser, LivingEntity pTarget, Projectile pProjectile, float pProjectileAngle, float pVelocity) {
        GolemShooterHelper.shootAimHelper(pTarget, pProjectile);
        pUser.m_5496_(SoundEvents.CROSSBOW_SHOOT, 1.0F, 1.0F / (pUser.getRandom().nextFloat() * 0.4F + 0.8F));
    }

    @Override
    public void onCrossbowAttackPerformed() {
        this.f_20891_ = 0;
    }

    @Override
    public void performCrossbowAttack(LivingEntity pUser, float pVelocity) {
        InteractionHand interactionhand = ProjectileUtil.getWeaponHoldingHand(pUser, item -> item instanceof CrossbowItem);
        ItemStack itemstack = pUser.getItemInHand(interactionhand);
        if (pUser.isHolding(is -> is.getItem() instanceof CrossbowItem)) {
            CrossbowItem.performShooting(pUser.m_9236_(), pUser, interactionhand, itemstack, pVelocity, 0.0F);
        }
        this.onCrossbowAttackPerformed();
    }

    public InteractionHand getWeaponHand() {
        ItemStack stack = this.m_21205_();
        InteractionHand hand = InteractionHand.MAIN_HAND;
        if (stack.canPerformAction(ToolActions.SHIELD_BLOCK)) {
            hand = InteractionHand.OFF_HAND;
        }
        return hand;
    }

    @Override
    public void performRangedAttack(LivingEntity pTarget, float dist) {
        InteractionHand hand = this.getWeaponHand();
        ItemStack stack = this.m_21120_(hand);
        GolemThrowableEvent throwable = GolemShooterHelper.isValidThrowableWeapon(this, stack, hand);
        if (throwable.isThrowable()) {
            Projectile projectile = throwable.createProjectile(this.m_9236_());
            GolemShooterHelper.shootAimHelper(pTarget, projectile);
            this.m_5496_(SoundEvents.TRIDENT_THROW, 1.0F, 1.0F / (this.m_217043_().nextFloat() * 0.4F + 0.8F));
            projectile.getPersistentData().putInt("DespawnFactor", 20);
            this.m_9236_().m_7967_(projectile);
            stack.hurtAndBreak(1, this, e -> e.m_21190_(InteractionHand.MAIN_HAND));
        } else if (stack.getItem() instanceof CrossbowItem) {
            this.performCrossbowAttack(this, 3.0F);
        } else if (stack.getItem() instanceof BowItem bow) {
            ItemStack arrowStack = this.getProjectile(stack);
            if (arrowStack.isEmpty()) {
                return;
            }
            AbstractArrow arrowEntity = bow.customArrow(this.getArrow(arrowStack, dist));
            boolean infinite = GolemShooterHelper.arrowIsInfinite(arrowStack, stack);
            GolemBowAttackEvent event = new GolemBowAttackEvent(this, stack, hand, arrowEntity, infinite);
            MinecraftForge.EVENT_BUS.post(event);
            arrowEntity = event.getArrow();
            if (event.isNoPickup()) {
                arrowEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            } else {
                arrowEntity.pickup = AbstractArrow.Pickup.ALLOWED;
            }
            if (!event.isNoConsume()) {
                arrowStack.shrink(1);
            }
            GolemShooterHelper.shootAimHelper(pTarget, arrowEntity, event.speed(), event.gravity());
            this.m_5496_(SoundEvents.ARROW_SHOOT, 1.0F, 1.0F / (this.m_217043_().nextFloat() * 0.4F + 0.8F));
            arrowEntity.getPersistentData().putInt("DespawnFactor", 20);
            this.m_9236_().m_7967_(arrowEntity);
            stack.hurtAndBreak(1, this, e -> e.m_21190_(InteractionHand.MAIN_HAND));
        }
    }

    @Override
    public boolean isBlocking() {
        boolean ans = this.shieldCooldown == 0 && this.shieldSlot() != null;
        if (ans && this.rendering) {
            this.render_trigger = true;
        }
        return ans;
    }

    @Override
    public ItemStack getUseItem() {
        ItemStack ans = super.m_21211_();
        if (this.rendering && this.render_trigger) {
            this.render_trigger = false;
            InteractionHand hand = this.shieldSlot();
            if (hand != null) {
                return this.m_21120_(hand);
            }
        }
        return ans;
    }

    @Override
    public void broadcastBreakEvent(EquipmentSlot pSlot) {
        super.m_21166_(pSlot);
        Player player = this.getOwner();
        if (player != null) {
            GolemTriggers.BREAK.trigger((ServerPlayer) player);
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean can_sweep = this.m_21205_().canPerformAction(ToolActions.SWORD_SWEEP);
        if (!can_sweep) {
            if (super.m_7327_(target)) {
                ItemStack stack = this.m_6844_(EquipmentSlot.MAINHAND);
                stack.hurtAndBreak(1, this, self -> self.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                return true;
            }
        } else if (this.performRangedDamage(target, 0.0F, 0.0)) {
            ItemStack stack = this.m_6844_(EquipmentSlot.MAINHAND);
            stack.hurtAndBreak(1, this, self -> self.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            return true;
        }
        return false;
    }

    @Override
    protected AABB getAttackBoundingBox(Entity target, double range) {
        GolemSweepEvent event = new GolemSweepEvent(this, this.m_21205_(), target, range);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getBox();
    }

    @Override
    protected boolean performDamageTarget(Entity target, float damage, double kb) {
        return super.m_7327_(target);
    }

    @Override
    protected InteractionResult mobInteractImpl(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        if (MGConfig.COMMON.strictInteract.get() && !itemstack.isEmpty()) {
            return InteractionResult.PASS;
        } else if (!player.m_6144_()) {
            if (itemstack.isEmpty()) {
                return super.mobInteractImpl(player, hand);
            } else if (!(itemstack.getItem() instanceof GolemHolder) && itemstack.getItem().canFitInsideContainerItems() && this.canModify(player)) {
                GolemEquipEvent event = new GolemEquipEvent(this, itemstack);
                MinecraftForge.EVENT_BUS.post(event);
                if (!event.canEquip()) {
                    return InteractionResult.FAIL;
                } else if (this.m_9236_().isClientSide()) {
                    return InteractionResult.SUCCESS;
                } else {
                    if (this.m_21033_(event.getSlot())) {
                        this.dropSlot(event.getSlot(), false);
                    }
                    if (this.m_21033_(event.getSlot())) {
                        return InteractionResult.FAIL;
                    } else {
                        this.setItemSlot(event.getSlot(), itemstack.split(event.getAmount()));
                        int count = (int) Arrays.stream(EquipmentSlot.values()).filter(e -> !this.m_6844_(e).isEmpty()).count();
                        GolemTriggers.EQUIP.trigger((ServerPlayer) player, count);
                        return InteractionResult.CONSUME;
                    }
                }
            } else {
                return InteractionResult.FAIL;
            }
        } else {
            if (this.canModify(player)) {
                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    this.dropSlot(slot, false);
                }
            }
            if (itemstack.isEmpty()) {
                super.mobInteractImpl(player, hand);
            }
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public double getMyRidingOffset() {
        return -0.35;
    }

    @Override
    protected void hurtArmor(DamageSource source, float damage) {
        if (!(damage <= 0.0F)) {
            damage /= 4.0F;
            if (damage < 1.0F) {
                damage = 1.0F;
            }
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                    ItemStack itemstack = this.m_6844_(slot);
                    if ((!source.is(DamageTypeTags.IS_FIRE) || !itemstack.getItem().isFireResistant()) && itemstack.getItem() instanceof ArmorItem) {
                        itemstack.hurtAndBreak((int) damage, this, entity -> entity.broadcastBreakEvent(slot));
                    }
                }
            }
        }
    }

    @Nullable
    public InteractionHand shieldSlot() {
        return this.m_6844_(EquipmentSlot.MAINHAND).canPerformAction(ToolActions.SHIELD_BLOCK) ? InteractionHand.MAIN_HAND : (this.m_6844_(EquipmentSlot.OFFHAND).canPerformAction(ToolActions.SHIELD_BLOCK) ? InteractionHand.OFF_HAND : null);
    }

    @Override
    protected void hurtCurrentlyUsedShield(float damage) {
        InteractionHand hand = this.shieldSlot();
        if (hand != null) {
            ItemStack stack = this.m_21120_(hand);
            int i = damage < 3.0F ? 0 : 1 + Mth.floor(damage);
            GolemDamageShieldEvent event = new GolemDamageShieldEvent(this, stack, hand, (double) damage, i);
            MinecraftForge.EVENT_BUS.post(event);
            i = event.getCost();
            if (i > 0) {
                stack.hurtAndBreak(i, this, self -> self.m_21190_(hand));
            }
            if (stack.isEmpty()) {
                this.m_21008_(hand, ItemStack.EMPTY);
                this.m_5496_(SoundEvents.SHIELD_BREAK, 0.8F, 0.8F + this.m_9236_().random.nextFloat() * 0.4F);
            } else {
                this.m_5496_(SoundEvents.SHIELD_BLOCK, 0.8F, 0.8F + this.m_9236_().random.nextFloat() * 0.4F);
            }
        }
    }

    @Override
    protected void blockUsingShield(LivingEntity source) {
        super.m_6728_(source);
        InteractionHand hand = this.shieldSlot();
        if (hand != null) {
            ItemStack stack = this.m_21120_(hand);
            boolean canDisable = source.canDisableShield() || source.getMainHandItem().canDisableShield(stack, this, source);
            GolemDisableShieldEvent event = new GolemDisableShieldEvent(this, stack, hand, source, canDisable);
            MinecraftForge.EVENT_BUS.post(event);
            if (event.shouldDisable()) {
                this.shieldCooldown = 100;
                this.m_9236_().broadcastEntityEvent(this, (byte) 30);
            }
        }
    }

    @Override
    public void handleEntityEvent(byte event) {
        if (event == 30) {
            this.shieldCooldown = 100;
        }
        super.m_7822_(event);
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.shieldCooldown = Mth.clamp(this.shieldCooldown - 1, 0, 100);
    }

    @Override
    public void aiStep() {
        super.m_8107_();
        this.attackStep();
    }

    public void attackStep() {
        if (!this.m_9236_().isClientSide()) {
            if (this.inventoryTick <= 0) {
                this.inventoryTick = 4;
                this.switchWeapon(this.getWrapperOfHand(EquipmentSlot.MAINHAND), !this.backupHand.isEmpty() ? this.getBackupHand() : this.getWrapperOfHand(EquipmentSlot.OFFHAND));
            }
        }
    }

    public ItemWrapper getBackupHand() {
        return ItemWrapper.simple(() -> this.backupHand, e -> this.backupHand = e);
    }

    public ItemWrapper getArrowSlot() {
        return ItemWrapper.simple(() -> this.arrowSlot, e -> this.arrowSlot = e);
    }

    private void switchWeapon(ItemWrapper mainhand, ItemWrapper offhand) {
        LivingEntity target = this.m_5448_();
        ItemStack main = mainhand.getItem();
        ItemStack off = offhand.getItem();
        if (main.getItem() instanceof ProjectileWeaponItem) {
            if (!this.getProjectile(main).isEmpty() && off.canPerformAction(ToolActions.SHIELD_BLOCK)) {
                return;
            }
            if (off.isEmpty() || off.getItem() instanceof ProjectileWeaponItem || off.getItem() instanceof ArrowItem) {
                return;
            }
            if (target == null || !this.meleeGoal.canReachTarget(target)) {
                return;
            }
        } else if (off.getItem() instanceof ProjectileWeaponItem) {
            boolean noArrow = this.getProjectile(off).isEmpty();
            if (noArrow) {
                return;
            }
            if (target != null && this.meleeGoal.canReachTarget(target)) {
                return;
            }
        } else if (!main.isEmpty() || off.isEmpty()) {
            return;
        }
        mainhand.setItem(off);
        offhand.setItem(main);
        this.reassessWeaponGoal();
        this.inventoryTick = 10;
    }

    @Override
    public void checkRide(LivingEntity target) {
        if (target instanceof DogGolemEntity || target instanceof AbstractHorse) {
            this.m_20329_(target);
        }
    }
}