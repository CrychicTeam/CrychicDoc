package se.mickelus.tetra.items.modular;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import java.util.Comparator;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ObjectHolder;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.effect.CritEffect;
import se.mickelus.tetra.effect.EffectHelper;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.effect.ItemEffectHandler;
import se.mickelus.tetra.effect.JankEffect;
import se.mickelus.tetra.items.modular.impl.ModularSingleHeadedItem;
import se.mickelus.tetra.items.modular.impl.shield.ModularShieldItem;
import se.mickelus.tetra.util.ToolActionHelper;

@ParametersAreNonnullByDefault
public class ThrownModularItemEntity extends AbstractArrow implements IEntityAdditionalSpawnData {

    public static final String unlocalizedName = "thrown_modular_item";

    public static final String stackKey = "stack";

    public static final String dealtDamageKey = "dealtDamage";

    public static final String preferredSlotKey = "preferredSlot";

    private static final EntityDataAccessor<Byte> LOYALTY_LEVEL = SynchedEntityData.defineId(ThrownModularItemEntity.class, EntityDataSerializers.BYTE);

    @ObjectHolder(registryName = "entity_type", value = "tetra:thrown_modular_item")
    public static EntityType<ThrownModularItemEntity> type;

    public static int preferUnavailable = -3;

    public static int preferOffhand = -2;

    public static int preferToolbelt = -1;

    public int returningTicks;

    private ItemStack thrownStack = new ItemStack(Items.TRIDENT);

    private boolean dealtDamage;

    private int preferredSlot = -1;

    private IntOpenHashSet hitEntities = new IntOpenHashSet(5);

    private int hitBlocks;

    private int despawnTimer = 0;

    public ThrownModularItemEntity(EntityType<? extends ThrownModularItemEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    public ThrownModularItemEntity(Level worldIn, Player thrower, ItemStack thrownStackIn) {
        super(type, thrower, worldIn);
        this.thrownStack = thrownStackIn.copy();
        this.f_19804_.set(LOYALTY_LEVEL, (byte) EnchantmentHelper.getLoyalty(thrownStackIn));
        this.preferredSlot = thrower.m_7655_() == InteractionHand.MAIN_HAND ? thrower.getInventory().selected : preferOffhand;
        CastOptional.cast(this.thrownStack.getItem(), ItemModularHandheld.class).ifPresent(item -> {
            double critModifier = CritEffect.rollMultiplier(thrower.m_217043_(), item, this.thrownStack);
            this.m_36767_((byte) ((int) Math.round((double) this.getEffectLevel(ItemEffect.piercing) * critModifier)));
            if (critModifier != 1.0 && this.m_9236_() instanceof ServerLevel serverLevel) {
                Vec3 pos = thrower.m_20299_(0.0F).add(thrower.m_20154_());
                serverLevel.sendParticles(ParticleTypes.ENCHANTED_HIT, pos.x(), pos.y(), pos.z(), 15, 0.2, 0.2, 0.2, 0.0);
            }
        });
        if (this.thrownStack.getItem() instanceof ModularSingleHeadedItem) {
            this.m_36740_(SoundEvents.TRIDENT_HIT_GROUND);
        } else if (this.thrownStack.getItem() instanceof ModularShieldItem) {
            this.m_36740_(SoundEvents.PLAYER_ATTACK_KNOCKBACK);
        } else {
            this.m_36740_(SoundEvents.PLAYER_ATTACK_WEAK);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public ThrownModularItemEntity(Level worldIn, double x, double y, double z) {
        super(type, x, y, z, worldIn);
    }

    public ThrownModularItemEntity(PlayMessages.SpawnEntity packet, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(LOYALTY_LEVEL, (byte) 0);
    }

    @Override
    public void tick() {
        if (this.f_36704_ > 4) {
            this.dealtDamage = true;
        }
        Entity shooter = this.m_19749_();
        if ((this.dealtDamage || this.m_36797_()) && shooter != null) {
            int loyaltyLevel = this.f_19804_.get(LOYALTY_LEVEL);
            if (loyaltyLevel > 0 && !this.shouldReturnToThrower()) {
                if (!this.m_9236_().isClientSide && this.f_36705_ == AbstractArrow.Pickup.ALLOWED) {
                    this.m_5552_(this.getPickupItem(), 0.1F);
                }
                this.m_146870_();
            } else if (loyaltyLevel > 0) {
                this.m_36790_(true);
                Vec3 Vector3d = new Vec3(shooter.getX() - this.m_20185_(), shooter.getEyeY() - this.m_20186_(), shooter.getZ() - this.m_20189_());
                this.m_20343_(this.m_20185_(), this.m_20186_() + Vector3d.y * 0.015 * (double) loyaltyLevel, this.m_20189_());
                if (this.m_9236_().isClientSide) {
                    this.f_19791_ = this.m_20186_();
                }
                double speed = 0.05 * (double) loyaltyLevel;
                this.m_20256_(this.m_20184_().scale(0.95).add(Vector3d.normalize().scale(speed)));
                if (this.returningTicks == 0) {
                    this.m_5496_(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                    this.m_20256_(Vector3d.scale(0.01));
                }
                this.returningTicks++;
            }
        }
        super.tick();
    }

    private boolean shouldReturnToThrower() {
        Entity entity = this.m_19749_();
        return entity != null && entity.isAlive() ? !(entity instanceof ServerPlayer) || !entity.isSpectator() : false;
    }

    public boolean hasDealtDamage() {
        return this.dealtDamage;
    }

    @Override
    public boolean onGround() {
        return this.f_36704_ > 0;
    }

    private int getEffectLevel(ItemEffect effect) {
        return (Integer) CastOptional.cast(this.thrownStack.getItem(), IModularItem.class).map(item -> item.getEffectLevel(this.thrownStack, effect)).orElse(-1);
    }

    private float getEffectEfficiency(ItemEffect effect) {
        return (Float) CastOptional.cast(this.thrownStack.getItem(), IModularItem.class).map(item -> item.getEffectEfficiency(this.thrownStack, effect)).orElse(-1.0F);
    }

    @Override
    protected ItemStack getPickupItem() {
        return this.thrownStack.copy();
    }

    public ItemStack getThrownStack() {
        return this.thrownStack.copy();
    }

    @Nullable
    @Override
    protected EntityHitResult findHitEntity(Vec3 startVec, Vec3 endVec) {
        return this.dealtDamage ? null : super.findHitEntity(startVec, endVec);
    }

    @Override
    protected void onHit(HitResult rayTraceResult) {
        if (rayTraceResult.getType() == HitResult.Type.BLOCK && !this.dealtDamage) {
            BlockPos pos = ((BlockHitResult) rayTraceResult).getBlockPos();
            Entity shooter = this.m_19749_();
            BlockState blockState = this.m_9236_().getBlockState(pos);
            ItemModularHandheld item = (ItemModularHandheld) CastOptional.cast(this.thrownStack.getItem(), ItemModularHandheld.class).orElse(null);
            if (ToolActionHelper.isEffectiveOn(this.thrownStack, blockState) && shooter instanceof Player player && item != null) {
                double destroySpeed = (double) item.getDestroySpeed(this.thrownStack, blockState);
                if (destroySpeed > 1.0 && destroySpeed * (double) item.getEffectEfficiency(this.thrownStack, ItemEffect.throwable) > (double) blockState.m_60800_(this.m_9236_(), pos)) {
                    if (shooter instanceof ServerPlayer serverPlayer) {
                        EffectHelper.sendEventToPlayer(serverPlayer, 2001, pos, Block.getId(blockState));
                    }
                    item.applyBlockBreakEffects(this.thrownStack, this.m_9236_(), blockState, pos, player);
                    this.hitBlocks++;
                    boolean canPierce = this.getEffectLevel(ItemEffect.piercingHarvest) > 0 && this.hitBlocks < this.m_36796_();
                    if (canPierce) {
                        this.m_20256_(this.m_20184_().normalize().scale(0.8F));
                    } else {
                        this.dealtDamage = true;
                        super.m_6532_(rayTraceResult);
                    }
                    this.breakBlock(player, pos, blockState);
                    if (canPierce) {
                        this.hitAdditional();
                    }
                    return;
                }
                if (shooter instanceof ServerPlayer serverPlayer) {
                    serverPlayer.playNotifySound(SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 0.5F, 0.5F);
                }
            }
            if (!this.m_9236_().isClientSide && shooter != null) {
                int jankLevel = this.getEffectLevel(ItemEffect.janking);
                if (jankLevel > 0) {
                    JankEffect.jankItemsDelayed((ServerLevel) this.m_9236_(), pos, jankLevel, this.getEffectEfficiency(ItemEffect.janking), shooter);
                }
            }
        }
        super.m_6532_(rayTraceResult);
    }

    private void hitAdditional() {
        Vec3 position = this.m_20182_();
        Vec3 target = position.add(this.m_20184_());
        HitResult rayTraceResult = this.m_9236_().m_45547_(new ClipContext(position, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (rayTraceResult.getType() == HitResult.Type.BLOCK && !ForgeEventFactory.onProjectileImpact(this, rayTraceResult)) {
            this.onHit(rayTraceResult);
        }
    }

    private void breakBlock(Player shooter, BlockPos pos, BlockState blockState) {
        ItemStack currentItem = shooter.m_21205_();
        shooter.m_21008_(InteractionHand.MAIN_HAND, this.thrownStack);
        EffectHelper.breakBlock(this.m_9236_(), shooter, this.thrownStack, pos, blockState, true, false);
        shooter.m_21008_(InteractionHand.MAIN_HAND, currentItem);
    }

    @Override
    protected void onHitEntity(EntityHitResult raytrace) {
        Entity target = raytrace.getEntity();
        Entity shooter = this.m_19749_();
        Player playerShooter = (Player) CastOptional.cast(shooter, Player.class).orElse(null);
        DamageSource damagesource = this.m_9236_().damageSources().trident(this, (Entity) (shooter == null ? this : shooter));
        SoundEvent soundevent = SoundEvents.TRIDENT_HIT;
        int pierceLevel = this.getEffectLevel(ItemEffect.piercing);
        int ricochetLevel = this.getEffectLevel(ItemEffect.ricochet);
        if (pierceLevel <= 0 && ricochetLevel <= 0) {
            this.dealtDamage = true;
        } else {
            if (this.hitEntities == null) {
                this.hitEntities = new IntOpenHashSet(5);
            }
            if (this.hitEntities.contains(target.getId())) {
                return;
            }
            if (this.hitEntities.size() >= pierceLevel && this.hitEntities.size() >= ricochetLevel) {
                this.dealtDamage = true;
            } else {
                this.hitEntities.add(target.getId());
            }
        }
        ItemStack heldTemp = null;
        if (playerShooter != null) {
            heldTemp = playerShooter.m_21205_();
            playerShooter.m_21008_(InteractionHand.MAIN_HAND, this.thrownStack);
        }
        if (target instanceof LivingEntity && this.thrownStack.getItem() instanceof ItemModularHandheld) {
            LivingEntity targetLivingEntity = (LivingEntity) target;
            ItemModularHandheld item = (ItemModularHandheld) this.thrownStack.getItem();
            double critModifier = CritEffect.rollMultiplier(targetLivingEntity.getRandom(), item, this.thrownStack);
            double damage = item.getAbilityBaseDamage(this.thrownStack) * (double) item.getEffectEfficiency(this.thrownStack, ItemEffect.throwable);
            damage += (double) EnchantmentHelper.getDamageBonus(this.thrownStack, targetLivingEntity.getMobType());
            damage *= critModifier;
            if (target.hurt(damagesource, (float) damage)) {
                if (shooter instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(targetLivingEntity, shooter);
                    EffectHelper.applyEnchantmentHitEffects(this.getPickupItem(), targetLivingEntity, (LivingEntity) shooter);
                    ItemEffectHandler.applyHitEffects(this.thrownStack, targetLivingEntity, (LivingEntity) shooter);
                    item.tickProgression((LivingEntity) shooter, this.thrownStack, 1);
                }
                this.m_7761_(targetLivingEntity);
                if (critModifier != 1.0 && !this.m_9236_().isClientSide) {
                    Vec3 hitVec = raytrace.m_82450_();
                    ((ServerLevel) this.m_9236_()).sendParticles(ParticleTypes.ENCHANTED_HIT, hitVec.x(), hitVec.y(), hitVec.z(), 15, 0.2, 0.2, 0.2, 0.0);
                }
            }
        }
        float f1 = 1.0F;
        if (!this.m_9236_().isClientSide && this.m_9236_().isThundering() && EnchantmentHelper.hasChanneling(this.thrownStack)) {
            BlockPos blockpos = target.blockPosition();
            if (this.m_9236_().m_45527_(blockpos)) {
                LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(this.m_9236_());
                lightning.m_20219_(Vec3.atBottomCenterOf(blockpos));
                lightning.setCause(shooter instanceof ServerPlayer ? (ServerPlayer) shooter : null);
                this.m_9236_().m_7967_(lightning);
                soundevent = SoundEvents.TRIDENT_THUNDER;
                f1 = 5.0F;
            }
        }
        if (playerShooter != null) {
            playerShooter.m_21008_(InteractionHand.MAIN_HAND, heldTemp);
        }
        this.m_5496_(soundevent, f1, 1.0F);
        if (this.dealtDamage) {
            this.m_20256_(this.m_20184_().multiply(-0.01, -0.1, -0.01));
        } else if (ricochetLevel > 0 && !this.m_9236_().isClientSide) {
            Vec3 hitPos = raytrace.m_82450_();
            this.m_20343_(hitPos.x(), hitPos.y(), hitPos.z());
            this.m_20256_((Vec3) this.m_9236_().getEntities(shooter, new AABB(target.blockPosition()).inflate(8.0), entity -> !this.hitEntities.contains(entity.getId()) && entity instanceof LivingEntity && !entity.isInvulnerableTo(damagesource) && (shooter == null || !entity.isAlliedTo(shooter))).stream().map(entity -> entity.position().add(0.0, (double) entity.getBbHeight() * 0.8, 0.0)).map(pos -> pos.subtract(this.m_20182_())).min(Comparator.comparing(Vec3::m_82556_)).map(Vec3::m_82541_).map(direction -> direction.multiply(1.0, 0.5, 1.0)).map(direction -> direction.scale(Math.max(this.m_20184_().length() * 0.5, 0.3))).orElse(this.m_20184_().multiply(-0.01, -0.1, -0.01)));
        }
    }

    @Override
    protected boolean canHitEntity(Entity target) {
        return super.canHitEntity(target) && (this.hitEntities == null || !this.hitEntities.contains(target.getId()));
    }

    @Override
    public void playerTouch(Player entityIn) {
        Entity entity = this.m_19749_();
        if (entity == null || entity.getUUID() == entityIn.m_20148_()) {
            super.playerTouch(entityIn);
        }
    }

    @Override
    protected boolean tryPickup(Player player) {
        if (this.f_36705_ == AbstractArrow.Pickup.ALLOWED && this.preferredSlot != -1) {
            Inventory inventory = player.getInventory();
            if (this.preferredSlot == preferOffhand) {
                ItemStack blockingStack = player.m_21206_();
                player.m_21008_(InteractionHand.OFF_HAND, this.getPickupItem());
                if (!blockingStack.isEmpty()) {
                    player.getInventory().placeItemBackInInventory(blockingStack);
                }
                return true;
            }
            ItemStack blockingStack = inventory.getItem(this.preferredSlot);
            boolean success = inventory.add(this.preferredSlot, this.getPickupItem());
            if (success) {
                if (!blockingStack.isEmpty()) {
                    player.getInventory().placeItemBackInInventory(blockingStack);
                }
                return true;
            }
        }
        return super.tryPickup(player);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("stack", 10)) {
            this.thrownStack = ItemStack.of(compound.getCompound("stack"));
        }
        this.dealtDamage = compound.getBoolean("dealtDamage");
        this.preferredSlot = compound.contains("preferredSlot") ? compound.getInt("preferredSlot") : -1;
        this.f_19804_.set(LOYALTY_LEVEL, (byte) EnchantmentHelper.getLoyalty(this.thrownStack));
        if (this.thrownStack.getItem() instanceof ModularSingleHeadedItem) {
            this.m_36740_(SoundEvents.TRIDENT_HIT_GROUND);
        } else if (this.thrownStack.getItem() instanceof ModularShieldItem) {
            this.m_36740_(SoundEvents.PLAYER_ATTACK_KNOCKBACK);
        } else {
            this.m_36740_(SoundEvents.PLAYER_ATTACK_WEAK);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("stack", this.thrownStack.save(new CompoundTag()));
        compound.putBoolean("dealtDamage", this.dealtDamage);
        compound.putInt("preferredSlot", this.preferredSlot);
    }

    @Override
    public void tickDespawn() {
        int level = this.f_19804_.get(LOYALTY_LEVEL);
        if (this.f_36705_ != AbstractArrow.Pickup.ALLOWED || level <= 0) {
            this.despawnTimer++;
            if (this.despawnTimer >= 500000) {
                this.m_146870_();
            }
        }
    }

    @Override
    protected float getWaterInertia() {
        return 0.99F;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeItem(this.thrownStack);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf buffer) {
        this.thrownStack = buffer.readItem();
    }
}