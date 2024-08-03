package org.violetmoon.quark.content.tools.entity.rang;

import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import java.util.List;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.base.handler.QuarkSounds;
import org.violetmoon.quark.content.mobs.entity.Toretoise;
import org.violetmoon.quark.content.tools.config.PickarangType;
import org.violetmoon.quark.content.tools.module.PickarangModule;

public abstract class AbstractPickarang<T extends AbstractPickarang<T>> extends Projectile {

    private static final EntityDataAccessor<ItemStack> STACK = SynchedEntityData.defineId(AbstractPickarang.class, EntityDataSerializers.ITEM_STACK);

    private static final EntityDataAccessor<Boolean> RETURNING = SynchedEntityData.defineId(AbstractPickarang.class, EntityDataSerializers.BOOLEAN);

    protected LivingEntity owner;

    private UUID ownerId;

    protected int liveTime;

    private int slot;

    private int blockHitCount;

    private IntOpenHashSet entitiesHit;

    private static final String TAG_RETURNING = "returning";

    private static final String TAG_LIVE_TIME = "liveTime";

    private static final String TAG_BLOCKS_BROKEN = "hitCount";

    private static final String TAG_RETURN_SLOT = "returnSlot";

    private static final String TAG_ITEM_STACK = "itemStack";

    public AbstractPickarang(EntityType<? extends AbstractPickarang<?>> type, Level worldIn) {
        super(type, worldIn);
    }

    public AbstractPickarang(EntityType<? extends AbstractPickarang<?>> type, Level worldIn, LivingEntity throwerIn) {
        super(type, worldIn);
        Vec3 pos = throwerIn.m_20182_();
        this.m_6034_(pos.x, pos.y + (double) throwerIn.m_20192_(), pos.z);
        this.ownerId = throwerIn.m_20148_();
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        double d0 = this.m_20191_().getSize() * 4.0;
        if (Double.isNaN(d0)) {
            d0 = 4.0;
        }
        d0 *= 64.0;
        return distance < d0 * d0;
    }

    public void shoot(Entity entityThrower, float rotationPitchIn, float rotationYawIn, float pitchOffset, float velocity, float inaccuracy) {
        float f = -Mth.sin(rotationYawIn * (float) (Math.PI / 180.0)) * Mth.cos(rotationPitchIn * (float) (Math.PI / 180.0));
        float f1 = -Mth.sin((rotationPitchIn + pitchOffset) * (float) (Math.PI / 180.0));
        float f2 = Mth.cos(rotationYawIn * (float) (Math.PI / 180.0)) * Mth.cos(rotationPitchIn * (float) (Math.PI / 180.0));
        this.shoot((double) f, (double) f1, (double) f2, velocity, inaccuracy);
        Vec3 Vector3d = entityThrower.getDeltaMovement();
        this.m_20256_(this.m_20184_().add(Vector3d.x, entityThrower.onGround() ? 0.0 : Vector3d.y, Vector3d.z));
    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vec3 vec = new Vec3(x, y, z).normalize().add(this.f_19796_.nextGaussian() * 0.0075F * (double) inaccuracy, this.f_19796_.nextGaussian() * 0.0075F * (double) inaccuracy, this.f_19796_.nextGaussian() * 0.0075F * (double) inaccuracy).scale((double) velocity);
        this.m_20256_(vec);
        float f = (float) vec.horizontalDistance();
        this.m_146922_((float) (Mth.atan2(vec.x, vec.z) * 180.0F / (float) Math.PI));
        this.m_146926_((float) (Mth.atan2(vec.y, (double) f) * 180.0F / (float) Math.PI));
        this.f_19859_ = this.m_146908_();
        this.f_19860_ = this.m_146909_();
    }

    @Override
    public void lerpMotion(double x, double y, double z) {
        this.m_20334_(x, y, z);
        if (this.f_19860_ == 0.0F && this.f_19859_ == 0.0F) {
            float f = (float) Math.sqrt(x * x + z * z);
            this.m_146922_((float) (Mth.atan2(x, z) * 180.0F / (float) Math.PI));
            this.m_146926_((float) (Mth.atan2(y, (double) f) * 180.0F / (float) Math.PI));
            this.f_19859_ = this.m_146908_();
            this.f_19860_ = this.m_146909_();
        }
    }

    public void setThrowData(int slot, ItemStack stack) {
        this.slot = slot;
        this.setStack(stack.copy());
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(STACK, new ItemStack(PickarangModule.pickarang));
        this.f_19804_.define(RETURNING, false);
    }

    protected void checkImpact() {
        if (!this.m_9236_().isClientSide) {
            Vec3 motion = this.m_20184_();
            Vec3 position = this.m_20182_();
            Vec3 rayEnd = position.add(motion);
            boolean doEntities = true;
            int tries = 100;
            while (this.m_6084_() && !this.isReturning()) {
                if (doEntities) {
                    EntityHitResult result = this.raycastEntities(position, rayEnd);
                    if (result != null) {
                        this.onHit(result);
                    } else {
                        doEntities = false;
                    }
                } else {
                    HitResult result = this.m_9236_().m_45547_(new ClipContext(position, rayEnd, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
                    if (result.getType() == HitResult.Type.MISS) {
                        return;
                    }
                    this.onHit(result);
                }
                if (tries-- <= 0) {
                    new RuntimeException("Pickarang hit way too much, this shouldn't happen").printStackTrace();
                    return;
                }
            }
        }
    }

    @Nullable
    protected EntityHitResult raycastEntities(Vec3 from, Vec3 to) {
        return ProjectileUtil.getEntityHitResult(this.m_9236_(), this, from, to, this.m_20191_().expandTowards(this.m_20184_()).inflate(1.0), entity -> !entity.isSpectator() && entity.isAlive() && (entity.isPickable() || entity instanceof AbstractPickarang) && entity != this.getThrower() && (this.entitiesHit == null || !this.entitiesHit.contains(entity.getId())));
    }

    protected boolean canDestroyBlock(BlockState state) {
        return !state.m_204336_(PickarangModule.pickarangImmuneTag);
    }

    @Override
    protected void onHit(@NotNull HitResult result) {
        LivingEntity owner = this.getThrower();
        if (result.getType() == HitResult.Type.BLOCK && result instanceof BlockHitResult blockHitResult) {
            BlockPos hit = blockHitResult.getBlockPos();
            BlockState state = this.m_9236_().getBlockState(hit);
            if (this.getPiercingModifier() == 0 || state.m_280296_()) {
                this.addHit();
            }
            if (!(owner instanceof ServerPlayer player)) {
                return;
            }
            float progress = this.getBlockDestroyProgress(state, player, this.m_9236_(), hit);
            if (progress == 0.0F) {
                return;
            }
            float equivalentHardness = 1.0F / (progress * 100.0F);
            if ((double) equivalentHardness <= this.getPickarangType().maxHardness && equivalentHardness >= 0.0F && this.canDestroyBlock(state)) {
                ItemStack prev = player.m_21205_();
                player.m_21008_(InteractionHand.MAIN_HAND, this.getStack());
                if (player.gameMode.destroyBlock(hit)) {
                    this.m_9236_().m_5898_(null, 2001, hit, Block.getId(state));
                } else {
                    this.clank();
                }
                this.setStack(player.m_21205_());
                player.m_21008_(InteractionHand.MAIN_HAND, prev);
            } else {
                this.clank();
            }
        } else if (result.getType() == HitResult.Type.ENTITY && result instanceof EntityHitResult entityHitResult) {
            Entity hitx = entityHitResult.getEntity();
            if (hitx != owner) {
                this.addHit(hitx);
                if (hitx instanceof AbstractPickarang) {
                    ((AbstractPickarang) hitx).setReturning();
                    this.clank();
                } else {
                    ItemStack pickarang = this.getStack();
                    Multimap<Attribute, AttributeModifier> modifiers = pickarang.getAttributeModifiers(EquipmentSlot.MAINHAND);
                    if (owner != null) {
                        ItemStack prev;
                        int ticksSinceLastSwing;
                        label90: {
                            prev = owner.getMainHandItem();
                            owner.setItemInHand(InteractionHand.MAIN_HAND, pickarang);
                            owner.getAttributes().addTransientAttributeModifiers(modifiers);
                            ticksSinceLastSwing = owner.attackStrengthTicker;
                            owner.attackStrengthTicker = (int) (1.0 / owner.getAttributeValue(Attributes.ATTACK_SPEED) * 20.0) + 1;
                            float prevHealth = hitx instanceof LivingEntity ? ((LivingEntity) hitx).getHealth() : 0.0F;
                            PickarangModule.setActivePickarang(this);
                            if (hitx instanceof Toretoise toretoise) {
                                int ore = toretoise.getOreType();
                                if (ore != 0) {
                                    this.addHit(toretoise);
                                    if (this.m_9236_() instanceof ServerLevel serverLevel) {
                                        LootParams.Builder lootBuilder = new LootParams.Builder(serverLevel).withParameter(LootContextParams.TOOL, pickarang);
                                        if (owner instanceof Player player) {
                                            lootBuilder.withLuck(player.getLuck());
                                        }
                                        toretoise.dropOre(ore, lootBuilder);
                                    }
                                    break label90;
                                }
                            }
                            if (owner instanceof Player) {
                                ((Player) owner).attack(hitx);
                            } else {
                                owner.doHurtTarget(hitx);
                            }
                            if (hitx instanceof LivingEntity && ((LivingEntity) hitx).getHealth() == prevHealth) {
                                this.clank();
                            }
                        }
                        PickarangModule.setActivePickarang(null);
                        owner.attackStrengthTicker = ticksSinceLastSwing;
                        this.setStack(owner.getMainHandItem());
                        owner.setItemInHand(InteractionHand.MAIN_HAND, prev);
                        owner.getAttributes().addTransientAttributeModifiers(modifiers);
                    } else {
                        AttributeSupplier.Builder mapBuilder = new AttributeSupplier.Builder();
                        mapBuilder.add(Attributes.ATTACK_DAMAGE, 1.0);
                        AttributeSupplier map = mapBuilder.build();
                        AttributeMap manager = new AttributeMap(map);
                        manager.addTransientAttributeModifiers(modifiers);
                        ItemStack stack = this.getStack();
                        stack.hurt(1, this.m_9236_().random, null);
                        this.setStack(stack);
                        hitx.hurt(this.m_9236_().damageSources().indirectMagic(this, this), (float) manager.getValue(Attributes.ATTACK_DAMAGE));
                    }
                }
            }
        }
    }

    private float getBlockDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        float f = state.m_60800_(level, pos);
        if (f == -1.0F) {
            return 0.0F;
        } else {
            float i = ForgeHooks.isCorrectToolForDrops(state, player) ? 30.0F : 100.0F;
            float digSpeed = this.getPlayerDigSpeed(player, state, pos);
            return digSpeed / f / i;
        }
    }

    private float getPlayerDigSpeed(Player player, BlockState state, @Nullable BlockPos pos) {
        float f = 1.0F;
        if (MobEffectUtil.hasDigSpeed(player)) {
            f *= 1.0F + (float) (MobEffectUtil.getDigSpeedAmplification(player) + 1) * 0.2F;
        }
        if (player.m_21023_(MobEffects.DIG_SLOWDOWN)) {
            float f1 = switch(player.m_21124_(MobEffects.DIG_SLOWDOWN).getAmplifier()) {
                case 0 ->
                    0.3F;
                case 1 ->
                    0.09F;
                case 2 ->
                    0.0027F;
                default ->
                    8.1E-4F;
            };
            f *= f1;
        }
        if (this.isEyeInFluidType(ForgeMod.WATER_TYPE.get())) {
            f /= 5.0F;
        }
        return ForgeEventFactory.getBreakSpeed(player, state, f, pos);
    }

    public void spark() {
        this.m_5496_(QuarkSounds.ENTITY_PICKARANG_SPARK, 1.0F, 1.0F);
        this.setReturning();
    }

    public void clank() {
        this.m_5496_(QuarkSounds.ENTITY_PICKARANG_CLANK, 1.0F, 1.0F);
        this.setReturning();
    }

    public void addHit(Entity entity) {
        if (this.entitiesHit == null) {
            this.entitiesHit = new IntOpenHashSet(5);
        }
        this.entitiesHit.add(entity.getId());
        this.postHit();
    }

    public void postHit() {
        if ((this.entitiesHit == null ? 0 : this.entitiesHit.size()) + this.blockHitCount > this.getPiercingModifier()) {
            this.setReturning();
        } else if (this.getPiercingModifier() > 0) {
            this.m_20256_(this.m_20184_().scale(0.8));
        }
    }

    public void addHit() {
        this.blockHitCount++;
        this.postHit();
    }

    protected void setReturning() {
        this.f_19804_.set(RETURNING, true);
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public void tick() {
        Vec3 pos = this.m_20182_();
        this.f_19790_ = pos.x;
        this.f_19791_ = pos.y;
        this.f_19792_ = pos.z;
        super.tick();
        if (!this.isReturning()) {
            this.checkImpact();
        }
        Vec3 ourMotion = this.m_20184_();
        this.m_6034_(pos.x + ourMotion.x, pos.y + ourMotion.y, pos.z + ourMotion.z);
        float f = (float) ourMotion.horizontalDistance();
        this.m_146922_((float) (Mth.atan2(ourMotion.x, ourMotion.z) * 180.0F / (float) Math.PI));
        this.m_146926_((float) (Mth.atan2(ourMotion.y, (double) f) * 180.0F / (float) Math.PI));
        while (this.m_146909_() - this.f_19860_ < -180.0F) {
            this.f_19860_ -= 360.0F;
        }
        while (this.m_146909_() - this.f_19860_ >= 180.0F) {
            this.f_19860_ += 360.0F;
        }
        while (this.m_146908_() - this.f_19859_ < -180.0F) {
            this.f_19859_ -= 360.0F;
        }
        while (this.m_146908_() - this.f_19859_ >= 180.0F) {
            this.f_19859_ += 360.0F;
        }
        this.m_146926_(Mth.lerp(0.2F, this.f_19860_, this.m_146909_()));
        this.m_146922_(Mth.lerp(0.2F, this.f_19859_, this.m_146908_()));
        float drag;
        if (this.m_20069_()) {
            for (int i = 0; i < 4; i++) {
                this.m_9236_().addParticle(ParticleTypes.BUBBLE, pos.x - ourMotion.x * 0.25, pos.y - ourMotion.y * 0.25, pos.z - ourMotion.z * 0.25, ourMotion.x, ourMotion.y, ourMotion.z);
            }
            drag = 0.8F;
        } else {
            drag = 0.99F;
        }
        if (this.hasDrag()) {
            this.m_20256_(ourMotion.scale((double) drag));
        }
        pos = this.m_20182_();
        this.m_6034_(pos.x, pos.y, pos.z);
        if (this.m_6084_()) {
            ItemStack stack = this.getStack();
            this.emitParticles(pos, ourMotion);
            boolean returning = this.isReturning();
            this.liveTime++;
            LivingEntity owner = this.getThrower();
            if (owner != null && owner.isAlive() && owner instanceof Player) {
                if (!returning) {
                    if (this.liveTime > this.getPickarangType().timeout) {
                        this.setReturning();
                    }
                    if (!this.m_9236_().getWorldBorder().isWithinBounds(this.m_20191_())) {
                        this.spark();
                    }
                } else {
                    this.f_19794_ = true;
                    int eff = this.getEfficiencyModifier();
                    List<ItemEntity> items = this.m_9236_().m_45976_(ItemEntity.class, this.m_20191_().inflate(2.0));
                    List<ExperienceOrb> xp = this.m_9236_().m_45976_(ExperienceOrb.class, this.m_20191_().inflate(2.0));
                    Vec3 ourPos = this.m_20182_();
                    for (ItemEntity item : items) {
                        if (!item.m_20159_()) {
                            item.m_20329_(this);
                            item.setPickUpDelay(5);
                        }
                    }
                    for (ExperienceOrb xpOrb : xp) {
                        if (!xpOrb.m_20159_()) {
                            xpOrb.m_20329_(this);
                        }
                    }
                    Vec3 ownerPos = owner.m_20182_().add(0.0, 1.0, 0.0);
                    Vec3 motion = ownerPos.subtract(ourPos);
                    double motionMag = 3.25 + (double) eff * 0.25;
                    if (motion.lengthSqr() < motionMag) {
                        Player player = (Player) owner;
                        Inventory inventory = player.getInventory();
                        ItemStack stackInSlot = inventory.getItem(this.slot);
                        if (!this.m_9236_().isClientSide) {
                            this.m_5496_(QuarkSounds.ENTITY_PICKARANG_PICKUP, 1.0F, 1.0F);
                            if (player instanceof ServerPlayer sp && this instanceof Flamerang && this.m_6060_() && this.m_20197_().size() > 0) {
                                PickarangModule.useFlamerangTrigger.trigger(sp);
                            }
                            if (!stack.isEmpty()) {
                                if (player.m_6084_() && stackInSlot.isEmpty()) {
                                    inventory.setItem(this.slot, stack);
                                } else if (!player.m_6084_() || !inventory.add(stack)) {
                                    player.drop(stack, false);
                                }
                            }
                            if (player.m_6084_()) {
                                for (ItemEntity itemx : items) {
                                    if (itemx.m_6084_()) {
                                        this.giveItemToPlayer(player, itemx);
                                    }
                                }
                                for (ExperienceOrb xpOrbx : xp) {
                                    if (xpOrbx.m_6084_()) {
                                        xpOrbx.playerTouch(player);
                                    }
                                }
                                for (Entity riding : this.m_20197_()) {
                                    if (riding.isAlive()) {
                                        if (riding instanceof ItemEntity) {
                                            this.giveItemToPlayer(player, (ItemEntity) riding);
                                        } else if (riding instanceof ExperienceOrb) {
                                            riding.playerTouch(player);
                                        }
                                    }
                                }
                            }
                            this.m_146870_();
                        }
                    } else {
                        this.m_20256_(motion.normalize().scale(0.7 + (double) ((float) eff * 0.325F)));
                    }
                }
            } else {
                if (!this.m_9236_().isClientSide) {
                    while (this.m_5830_()) {
                        this.m_6034_(this.m_20185_(), this.m_20186_() + 1.0, this.m_20189_());
                    }
                    this.m_5552_(stack, 0.0F);
                    this.m_146870_();
                }
            }
        }
    }

    public boolean isReturning() {
        return this.f_19804_.get(RETURNING);
    }

    protected void emitParticles(Vec3 pos, Vec3 ourMotion) {
    }

    public boolean hasDrag() {
        return true;
    }

    public abstract PickarangType<T> getPickarangType();

    private void giveItemToPlayer(Player player, ItemEntity itemEntity) {
        itemEntity.setPickUpDelay(0);
        itemEntity.playerTouch(player);
        if (itemEntity.m_6084_()) {
            ItemStack drop = itemEntity.getItem();
            player.drop(drop, false);
            itemEntity.m_146870_();
        }
    }

    @Nullable
    public LivingEntity getThrower() {
        if (this.owner == null && this.ownerId != null && this.m_9236_() instanceof ServerLevel) {
            Entity entity = ((ServerLevel) this.m_9236_()).getEntity(this.ownerId);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity) entity;
            } else {
                this.ownerId = null;
            }
        }
        return this.owner;
    }

    @Override
    protected boolean canAddPassenger(@NotNull Entity passenger) {
        return super.m_7310_(passenger) || passenger instanceof ItemEntity || passenger instanceof ExperienceOrb;
    }

    @Override
    public double getPassengersRidingOffset() {
        return 0.0;
    }

    @NotNull
    @Override
    public SoundSource getSoundSource() {
        return SoundSource.PLAYERS;
    }

    public int getEfficiencyModifier() {
        return Quark.ZETA.itemExtensions.get(this.getStack()).getEnchantmentLevelZeta(this.getStack(), Enchantments.BLOCK_EFFICIENCY);
    }

    public int getPiercingModifier() {
        return Quark.ZETA.itemExtensions.get(this.getStack()).getEnchantmentLevelZeta(this.getStack(), Enchantments.PIERCING);
    }

    public ItemStack getStack() {
        return this.f_19804_.get(STACK);
    }

    public void setStack(ItemStack stack) {
        this.f_19804_.set(STACK, stack);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        this.f_19804_.set(RETURNING, compound.getBoolean("returning"));
        this.liveTime = compound.getInt("liveTime");
        this.blockHitCount = compound.getInt("hitCount");
        this.slot = compound.getInt("returnSlot");
        if (compound.contains("itemStack")) {
            this.setStack(ItemStack.of(compound.getCompound("itemStack")));
        } else {
            this.setStack(new ItemStack(PickarangModule.pickarang));
        }
        if (compound.contains("owner", 10)) {
            Tag owner = compound.get("owner");
            if (owner != null) {
                this.ownerId = NbtUtils.loadUUID(owner);
            }
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        compound.putBoolean("returning", this.isReturning());
        compound.putInt("liveTime", this.liveTime);
        compound.putInt("hitCount", this.blockHitCount);
        compound.putInt("returnSlot", this.slot);
        compound.put("itemStack", this.getStack().serializeNBT());
        if (this.ownerId != null) {
            compound.put("owner", NbtUtils.createUUID(this.ownerId));
        }
    }

    @NotNull
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}