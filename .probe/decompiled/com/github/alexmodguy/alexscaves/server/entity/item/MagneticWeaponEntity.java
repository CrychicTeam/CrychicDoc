package com.github.alexmodguy.alexscaves.server.entity.item;

import com.github.alexmodguy.alexscaves.server.enchantment.ACEnchantmentRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.TeletorEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACAdvancementTriggerRegistry;
import com.google.common.collect.Multimap;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class MagneticWeaponEntity extends Entity {

    private static final EntityDataAccessor<ItemStack> ITEMSTACK = SynchedEntityData.defineId(MagneticWeaponEntity.class, EntityDataSerializers.ITEM_STACK);

    private static final EntityDataAccessor<Optional<UUID>> CONTROLLER_UUID = SynchedEntityData.defineId(MagneticWeaponEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Integer> CONTROLLER_ID = SynchedEntityData.defineId(MagneticWeaponEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> TARGET_ID = SynchedEntityData.defineId(MagneticWeaponEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> IDLING = SynchedEntityData.defineId(MagneticWeaponEntity.class, EntityDataSerializers.BOOLEAN);

    private float prevStrikeProgress;

    private float strikeProgress;

    private float prevReturnProgress;

    private float returnProgress;

    private int playerUseCooldown = 0;

    private boolean comingBack = false;

    private float destroyBlockProgress;

    private BlockPos lastSelectedBlock;

    private int totalMiningTime = 0;

    private boolean hadPlayerController = false;

    private boolean spawnedItem = false;

    public boolean returnFlag = false;

    public MagneticWeaponEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public MagneticWeaponEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(ACEntityRegistry.MAGNETIC_WEAPON.get(), level);
        this.m_20011_(this.m_142242_());
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(ITEMSTACK, new ItemStack(Items.IRON_SWORD));
        this.f_19804_.define(CONTROLLER_UUID, Optional.empty());
        this.f_19804_.define(CONTROLLER_ID, -1);
        this.f_19804_.define(TARGET_ID, -1);
        this.f_19804_.define(IDLING, true);
    }

    @Override
    public void tick() {
        super.tick();
        this.prevStrikeProgress = this.strikeProgress;
        this.prevReturnProgress = this.returnProgress;
        Entity controller = this.getController();
        Entity target = this.getTarget();
        if (!this.m_9236_().isClientSide) {
            if (!this.comingBack && (!(controller instanceof TeletorEntity) || target != null && target.isAlive())) {
                this.f_19794_ = false;
            } else {
                this.f_19794_ = true;
            }
            if (controller == null && this.f_19797_ > 20 || this.getItemStack().isEmpty()) {
                if (this.hadPlayerController) {
                    this.plopItem();
                    this.hadPlayerController = false;
                }
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
        }
        if ((this.getTarget() == null || this.comingBack || this.playerUseCooldown > 0) && this.strikeProgress > 0.0F) {
            this.strikeProgress = Math.max(0.0F, this.strikeProgress - 0.1F);
        }
        if (controller instanceof TeletorEntity teletor) {
            this.f_19804_.set(CONTROLLER_ID, teletor.m_19879_());
            teletor.setWeaponUUID(this.m_20148_());
            if (!this.m_9236_().isClientSide) {
                Entity e = teletor.m_5448_();
                this.f_19804_.set(TARGET_ID, e != null && e.isAlive() ? e.getId() : -1);
            }
            boolean attacking = !this.comingBack && target != null && target.isAlive();
            Vec3 vec3 = attacking ? target.getEyePosition() : teletor.getWeaponPosition();
            Vec3 want = vec3.subtract(this.m_20182_());
            if (target != null && !this.comingBack) {
                this.f_19804_.set(IDLING, false);
                if (want.length() < (double) (target.getBbWidth() + 1.0F)) {
                    if (this.strikeProgress < 1.0F) {
                        this.strikeProgress = Math.max(0.0F, this.strikeProgress + 0.35F);
                    } else {
                        this.hurtEntity(teletor, target);
                        this.comingBack = true;
                    }
                } else if (want.length() > 32.0) {
                    this.comingBack = true;
                }
            }
            this.directMovementTowards(vec3, 0.1F);
            if (this.m_20270_(controller) < 2.5F && this.m_20186_() > controller.getY()) {
                this.f_19804_.set(IDLING, true);
                if (this.comingBack) {
                    this.comingBack = false;
                }
            }
        } else if (controller instanceof Player player) {
            Vec3 moveTo = null;
            this.f_19804_.set(CONTROLLER_ID, controller.getId());
            this.f_19804_.set(IDLING, false);
            this.comingBack = !this.isOwnerWearingGauntlet();
            float speed = 0.1F;
            boolean haste = false;
            if (this.isOwnerWearingGauntlet()) {
                this.hadPlayerController = true;
                float maxDist = 30.0F;
                if (this.getController() instanceof LivingEntity living && living.getUseItem().is(ACItemRegistry.GALENA_GAUNTLET.get())) {
                    ItemStack useItem = living.getUseItem();
                    haste = useItem.getEnchantmentLevel(ACEnchantmentRegistry.FERROUS_HASTE.get()) > 0;
                    int fieldExtension = useItem.getEnchantmentLevel(ACEnchantmentRegistry.FIELD_EXTENSION.get());
                    maxDist += (float) fieldExtension * 5.0F;
                }
                BlockPos miningBlock;
                label241: {
                    miningBlock = null;
                    HitResult hitresult = ProjectileUtil.getHitResultOnViewVector(player, Entity::m_271807_, (double) maxDist);
                    if (hitresult instanceof EntityHitResult entityHitResult && this.playerUseCooldown == 0) {
                        Entity entity = entityHitResult.getEntity();
                        moveTo = entity.position().add(0.0, (double) (entity.getBbHeight() * 0.5F), 0.0);
                        speed = 0.2F;
                        if (this.m_20270_(entity) < entity.getBbWidth() + 1.5F) {
                            if (this.strikeProgress < 1.0F) {
                                this.strikeProgress = Math.max(0.0F, this.strikeProgress + 0.35F);
                            } else {
                                this.hurtEntity(player, entity);
                                this.playerUseCooldown = haste ? 3 : 5 + this.f_19796_.nextInt(5);
                            }
                        }
                        break label241;
                    }
                    moveTo = player.m_146892_().add(player.m_20252_(1.0F).scale((double) (maxDist - 20.0F)));
                    if ((hitresult.getType() == HitResult.Type.BLOCK || hitresult.getLocation().subtract(player.m_146892_()).length() < (double) maxDist) && hitresult instanceof BlockHitResult blockHitResult) {
                        if (this.m_20238_(Vec3.atCenterOf(blockHitResult.getBlockPos())) < 2.25) {
                            miningBlock = blockHitResult.getBlockPos();
                        }
                        if (!this.m_9236_().getBlockState(blockHitResult.getBlockPos()).m_60795_()) {
                            moveTo = hitresult.getLocation();
                        }
                    }
                }
                if (miningBlock != null) {
                    if (this.lastSelectedBlock == null || !this.lastSelectedBlock.equals(miningBlock)) {
                        if (this.lastSelectedBlock != null) {
                            this.m_9236_().destroyBlockProgress(player.m_19879_(), this.lastSelectedBlock, -1);
                        }
                        this.lastSelectedBlock = miningBlock;
                        this.destroyBlockProgress = 0.0F;
                    }
                    BlockState miningState = this.m_9236_().getBlockState(miningBlock);
                    SoundType soundType = miningState.m_60827_();
                    float f = miningState.m_60800_(this.m_9236_(), miningBlock);
                    float itemDestroySpeed = this.getDigSpeed(player, miningState, miningBlock);
                    if (itemDestroySpeed > 1.0F) {
                        if (this.totalMiningTime % 4 == 0) {
                            this.m_5496_(soundType.getHitSound(), (soundType.getVolume() + 1.0F) / 8.0F, soundType.getPitch() * 0.5F);
                        }
                        this.totalMiningTime++;
                        this.strikeProgress = (float) Math.abs(Math.sin((double) ((float) this.f_19797_ * 0.6F)) * 1.2F - 0.2F);
                        float j = itemDestroySpeed / f / (float) (haste ? 8 : 10);
                        this.destroyBlockProgress += j;
                        this.m_9236_().destroyBlockProgress(player.m_19879_(), this.lastSelectedBlock, (int) (this.destroyBlockProgress * 10.0F));
                        if (this.destroyBlockProgress >= 1.0F && !this.m_9236_().isClientSide) {
                            this.damageItem(1);
                            ItemStack itemStack = this.getItemStack();
                            itemStack.mineBlock(this.m_9236_(), miningState, miningBlock, player);
                            int fortuneLevel = itemStack.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);
                            int silkTouchLevel = itemStack.getEnchantmentLevel(Enchantments.SILK_TOUCH);
                            int exp = miningState.getExpDrop(this.m_9236_(), this.m_9236_().random, miningBlock, fortuneLevel, silkTouchLevel);
                            ForgeEventFactory.onPlayerDestroyItem(player, itemStack, InteractionHand.MAIN_HAND);
                            boolean flag = this.m_9236_().m_46961_(miningBlock, false);
                            miningState.m_60734_().playerDestroy(this.m_9236_(), player, miningBlock, miningState, this.m_9236_().getBlockEntity(miningBlock), itemStack);
                            if (flag && exp > 0 && this.m_9236_() instanceof ServerLevel serverLevel) {
                                miningState.m_60734_().popExperience(serverLevel, miningBlock, exp);
                            }
                            this.destroyBlockProgress = 0.0F;
                        }
                    }
                }
            } else {
                if (this.returnProgress < 1.0F) {
                    this.returnProgress = Math.min(1.0F, this.returnProgress + 0.2F);
                }
                if (this.lastSelectedBlock != null) {
                    this.m_9236_().destroyBlockProgress(player.m_19879_(), this.lastSelectedBlock, -1);
                    this.lastSelectedBlock = null;
                }
                moveTo = player.m_20182_().add(0.0, 1.0, 0.0);
                if ((double) this.m_20270_(controller) < 1.4 && !this.m_213877_()) {
                    if (!this.spawnedItem && player.addItem(this.getItemStack())) {
                        this.spawnedItem = true;
                        this.m_142687_(Entity.RemovalReason.DISCARDED);
                    } else {
                        this.plopItem();
                    }
                }
            }
            if (moveTo != null) {
                this.directMovementTowards(moveTo, speed);
            }
        }
        if (this.playerUseCooldown > 0) {
            this.playerUseCooldown--;
        }
        this.m_6478_(MoverType.SELF, this.m_20184_());
        this.m_20256_(this.m_20184_().scale(0.9F));
    }

    private void plopItem() {
        if (!this.spawnedItem) {
            this.spawnedItem = true;
            ItemEntity itementity = this.m_19983_(this.getItemStack());
            if (itementity != null) {
                itementity.setNoPickUpDelay();
            }
        }
        this.m_142687_(Entity.RemovalReason.DISCARDED);
    }

    public void damageItem(int damageAmount) {
        if (this.getController() instanceof LivingEntity living) {
            if (living instanceof Player player && player.isCreative()) {
                return;
            }
            ItemStack stack = this.getItemStack();
            if (stack.isDamageableItem()) {
                stack.hurtAndBreak(damageAmount, living, player1 -> player1.broadcastBreakEvent(player1.getUsedItemHand()));
                if (stack.getDamageValue() >= stack.getMaxDamage()) {
                    this.m_142687_(Entity.RemovalReason.DISCARDED);
                }
            }
        }
    }

    public float getDigSpeed(Player player, BlockState state, @Nullable BlockPos pos) {
        ItemStack stack = this.getItemStack();
        float f = stack.getDestroySpeed(state);
        if (f > 1.0F) {
            int i = EnchantmentHelper.getBlockEfficiency(player);
            if (i > 0 && !stack.isEmpty()) {
                f += (float) (i * i + 1);
            }
        }
        if (MobEffectUtil.hasDigSpeed(player)) {
            f *= 1.0F + (float) (MobEffectUtil.getDigSpeedAmplification(player) + 1) * 0.2F;
        }
        if (player.m_21023_(MobEffects.DIG_SLOWDOWN)) {
            f *= switch(player.m_21124_(MobEffects.DIG_SLOWDOWN).getAmplifier()) {
                case 0 ->
                    0.3F;
                case 1 ->
                    0.09F;
                case 2 ->
                    0.0027F;
                default ->
                    8.1E-4F;
            };
        }
        if (this.m_204029_(FluidTags.WATER) && !EnchantmentHelper.hasAquaAffinity(player)) {
            f /= 5.0F;
        }
        if (!this.m_20096_()) {
            f /= 5.0F;
        }
        return ForgeEventFactory.getBreakSpeed(player, state, f, pos);
    }

    private void hurtEntity(LivingEntity holder, Entity target) {
        ItemStack itemStack = this.getItemStack();
        float f = (float) holder.getAttributeValue(Attributes.ATTACK_DAMAGE) + (float) this.getDamageForItem(itemStack);
        float f1 = (float) holder.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
        if (target instanceof LivingEntity) {
            f += EnchantmentHelper.getDamageBonus(itemStack, ((LivingEntity) target).getMobType());
            f1 += (float) EnchantmentHelper.getEnchantmentLevel(Enchantments.KNOCKBACK, holder);
        }
        int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FIRE_ASPECT, itemStack);
        if (i > 0) {
            target.setSecondsOnFire(i * 4);
        }
        if (target.hurt(this.m_269291_().mobAttack(holder), f)) {
            holder.m_19970_(holder, target);
            this.damageItem(1);
            if (f1 > 0.0F && target instanceof LivingEntity) {
                ((LivingEntity) target).knockback((double) (f1 * 0.5F), (double) Mth.sin(this.m_146908_() * (float) (Math.PI / 180.0)), (double) (-Mth.cos(this.m_146908_() * (float) (Math.PI / 180.0))));
                this.m_20256_(this.m_20184_().multiply(0.6, 1.0, 0.6));
            }
        }
        if (this.m_6060_()) {
            target.setSecondsOnFire(5);
        }
        if (holder instanceof Player player && target instanceof LivingEntity living) {
            itemStack.hurtEnemy(living, player);
            living.setLastHurtByPlayer(player);
            if (living.getHealth() <= 0.0F && player.m_20270_(target) >= 19.5F) {
                ACAdvancementTriggerRegistry.KILL_MOB_WITH_GALENA_GAUNTLET.triggerForEntity(player);
            }
        }
    }

    private void directMovementTowards(Vec3 moveTo, float speed) {
        Vec3 want = moveTo.subtract(this.m_20182_());
        if (want.length() > 1.0) {
            want = want.normalize();
        }
        float targetXRot = (float) (-(Mth.atan2(want.y, want.horizontalDistance()) * 180.0F / (float) Math.PI));
        float targetYRot = (float) (-Mth.atan2(want.x, want.z) * 180.0F / (float) Math.PI);
        if (this.isIdling()) {
            targetXRot = this.m_146909_();
            targetYRot = this.m_146908_() + 5.0F;
        }
        this.m_146926_(Mth.approachDegrees(this.m_146909_(), targetXRot, 5.0F));
        this.m_146922_(Mth.approachDegrees(this.m_146908_(), targetYRot, 5.0F));
        this.m_20256_(this.m_20184_().add(want.scale((double) speed)));
    }

    private boolean isOwnerWearingGauntlet() {
        if (this.getController() instanceof LivingEntity living && living.getUseItem().is(ACItemRegistry.GALENA_GAUNTLET.get()) && living.isAlive() && !this.returnFlag) {
            return true;
        }
        return false;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("WeaponStack")) {
            this.setItemStack(ItemStack.of(tag.getCompound("WeaponStack")));
        }
        if (tag.hasUUID("ControllerUUID")) {
            this.setControllerUUID(tag.getUUID("ControllerUUID"));
        }
    }

    public double getDamageForItem(ItemStack itemStack) {
        Multimap<Attribute, AttributeModifier> map = itemStack.getAttributeModifiers(EquipmentSlot.MAINHAND);
        if (map.isEmpty()) {
            return 0.0;
        } else {
            double d = 0.0;
            for (AttributeModifier mod : map.get(Attributes.ATTACK_DAMAGE)) {
                d += mod.getAmount();
            }
            return d;
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        if (!this.getItemStack().isEmpty()) {
            CompoundTag stackTag = new CompoundTag();
            this.getItemStack().save(stackTag);
            tag.put("WeaponStack", stackTag);
        }
        if (this.getControllerUUID() != null) {
            tag.putUUID("ControllerUUID", this.getControllerUUID());
        }
    }

    public ItemStack getItemStack() {
        return this.f_19804_.get(ITEMSTACK);
    }

    public void setItemStack(ItemStack item) {
        this.f_19804_.set(ITEMSTACK, item);
    }

    public boolean isIdling() {
        return this.f_19804_.get(IDLING);
    }

    @Nullable
    public UUID getControllerUUID() {
        return (UUID) this.f_19804_.get(CONTROLLER_UUID).orElse(null);
    }

    public void setControllerUUID(@Nullable UUID uniqueId) {
        this.f_19804_.set(CONTROLLER_UUID, Optional.ofNullable(uniqueId));
    }

    public Entity getController() {
        if (!this.m_9236_().isClientSide) {
            UUID id = this.getControllerUUID();
            return id == null ? null : ((ServerLevel) this.m_9236_()).getEntity(id);
        } else {
            int id = this.f_19804_.get(CONTROLLER_ID);
            return id == -1 ? null : this.m_9236_().getEntity(id);
        }
    }

    public Entity getTarget() {
        int id = this.f_19804_.get(TARGET_ID);
        return id == -1 ? null : this.m_9236_().getEntity(id);
    }

    public float getStrikeProgress(float partialTick) {
        return this.prevStrikeProgress + (this.strikeProgress - this.prevStrikeProgress) * partialTick;
    }

    public float getReturnProgress(float partialTick) {
        return this.prevReturnProgress + (this.returnProgress - this.prevReturnProgress) * partialTick;
    }

    public Vec3 getControllerHandPos(Player controller, float partialTicks) {
        float yBodyRot = Mth.lerp(partialTicks, controller.f_20884_, controller.f_20883_);
        boolean mainHand = controller.m_21120_(InteractionHand.MAIN_HAND).is(ACItemRegistry.GALENA_GAUNTLET.get());
        Vec3 offset = new Vec3((double) (controller.m_20205_() * (mainHand ? -0.75F : 0.75F)), (double) (controller.m_20206_() * 0.68F), (double) (controller.m_20205_() * -0.1F)).yRot((float) Math.toRadians((double) (-yBodyRot)));
        Vec3 armViewExtra = controller.m_20252_(partialTicks).normalize().scale(0.75);
        return controller.m_20318_(partialTicks).add(offset).add(armViewExtra);
    }
}