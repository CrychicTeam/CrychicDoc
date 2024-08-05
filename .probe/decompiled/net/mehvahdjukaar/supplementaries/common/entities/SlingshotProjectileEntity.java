package net.mehvahdjukaar.supplementaries.common.entities;

import com.google.common.base.Suppliers;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.entity.IExtraClientSpawnData;
import net.mehvahdjukaar.moonlight.api.entity.ImprovedProjectileEntity;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.util.FakePlayerManager;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.moonlight.api.util.math.MthUtils;
import net.mehvahdjukaar.supplementaries.common.events.overrides.InteractEventsHandler;
import net.mehvahdjukaar.supplementaries.common.items.BombItem;
import net.mehvahdjukaar.supplementaries.common.utils.ItemsUtil;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.integration.FlanCompat;
import net.mehvahdjukaar.supplementaries.reg.ModEntities;
import net.mehvahdjukaar.supplementaries.reg.ModParticles;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.EnderpearlItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SnowballItem;
import net.minecraft.world.item.ThrowablePotionItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class SlingshotProjectileEntity extends ImprovedProjectileEntity implements IExtraClientSpawnData {

    private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(SlingshotProjectileEntity.class, EntityDataSerializers.BYTE);

    protected int MAX_AGE = 700;

    private float xRotInc;

    private float yRotInc;

    private float particleCooldown = 0.0F;

    private final Supplier<Integer> light = Suppliers.memoize(() -> {
        if (this.m_7846_().getItem() instanceof BlockItem blockItem) {
            Block b = blockItem.getBlock();
            return b.defaultBlockState().m_60791_();
        } else {
            return 0;
        }
    });

    public SlingshotProjectileEntity(LivingEntity thrower, Level world, ItemStack item, ItemStack throwerStack) {
        this(world, item, throwerStack);
        this.m_6034_(thrower.m_20185_(), thrower.m_20188_() - 0.1, thrower.m_20189_());
        this.m_5602_(thrower);
    }

    public SlingshotProjectileEntity(Level world, ItemStack item, ItemStack throwerStack) {
        super((EntityType<? extends ThrowableItemProjectile>) ModEntities.SLINGSHOT_PROJECTILE.get(), world);
        this.maxAge = this.MAX_AGE;
        this.m_37446_(item);
        this.f_19804_.set(ID_LOYALTY, (byte) EnchantmentHelper.getLoyalty(throwerStack));
        this.m_20242_(EnchantmentHelper.getItemEnchantmentLevel((Enchantment) ModRegistry.STASIS_ENCHANTMENT.get(), throwerStack) != 0);
        this.yRotInc = (float) (this.f_19796_.nextBoolean() ? 1 : -1) * (float) (4.0 * this.f_19796_.nextGaussian() + 7.0);
        this.xRotInc = (float) (this.f_19796_.nextBoolean() ? 1 : -1) * (float) (4.0 * this.f_19796_.nextGaussian() + 7.0);
        this.m_146926_(this.f_19796_.nextFloat() * 360.0F);
        this.m_146922_(this.f_19796_.nextFloat() * 360.0F);
        this.f_19860_ = this.m_146909_();
        this.f_19859_ = this.m_146908_();
    }

    public SlingshotProjectileEntity(EntityType<SlingshotProjectileEntity> type, Level world) {
        super(type, world);
        this.maxAge = this.MAX_AGE;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return PlatHelper.getEntitySpawnPacket(this);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(ID_LOYALTY, (byte) 0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.f_19804_.set(ID_LOYALTY, tag.getByte("Loyalty"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putByte("Loyalty", this.f_19804_.get(ID_LOYALTY));
    }

    public void setLoyalty(ItemStack stack) {
        this.f_19804_.set(ID_LOYALTY, (byte) EnchantmentHelper.getLoyalty(stack));
    }

    @Override
    protected Item getDefaultItem() {
        return Items.STONE;
    }

    @Override
    protected void onHitEntity(EntityHitResult entityRayTraceResult) {
        super.m_5790_(entityRayTraceResult);
        if (!this.trySplashPotStuff() && entityRayTraceResult.getEntity() instanceof EnderMan enderman && this.m_7846_().getItem() instanceof BlockItem bi) {
            Block block = bi.getBlock();
            if ((block.builtInRegistryHolder().is(BlockTags.ENDERMAN_HOLDABLE) || (Boolean) CommonConfigs.Tools.UNRESTRICTED_SLINGSHOT.get()) && enderman.getCarriedBlock() == null) {
                enderman.setCarriedBlock(block.defaultBlockState());
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult hit) {
        super.m_8060_(hit);
        if (!this.touchedGround) {
            Entity owner = this.m_19749_();
            Level level = this.m_9236_();
            boolean success = this.trySplashPotStuff();
            if (!success && owner instanceof Player player) {
                if (!Utils.mayPerformBlockAction(player, hit.getBlockPos(), this.m_7846_())) {
                    return;
                }
                if (CompatHandler.FLAN && (level.isClientSide || !FlanCompat.canPlace(player, hit.getBlockPos()))) {
                    return;
                }
            }
            if (!success) {
                ItemStack stack = this.m_7846_();
                Item item = stack.getItem();
                Player player;
                if (owner instanceof Player p) {
                    player = p;
                } else {
                    player = FakePlayerManager.getDefault(this, this);
                }
                InteractionResult overrideResult = InteractEventsHandler.onItemUsedOnBlockHP(player, level, stack, InteractionHand.MAIN_HAND, hit);
                if (overrideResult.consumesAction()) {
                    success = true;
                } else {
                    overrideResult = InteractEventsHandler.onItemUsedOnBlock(player, level, stack, InteractionHand.MAIN_HAND, hit);
                    if (overrideResult.consumesAction()) {
                        success = true;
                    }
                }
                if (!success) {
                    Player fakePlayer = FakePlayerManager.getDefault(this, player);
                    success = ItemsUtil.place(item, new BlockPlaceContext(level, fakePlayer, InteractionHand.MAIN_HAND, this.m_7846_(), hit)).consumesAction();
                }
            }
            if (success) {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
        }
    }

    private boolean trySplashPotStuff() {
        if (this.m_19749_() instanceof LivingEntity le) {
            Projectile ent = null;
            Item item = this.m_7846_().getItem();
            Level level = this.m_9236_();
            if (item instanceof ThrowablePotionItem) {
                ThrownPotion p = new ThrownPotion(level, le);
                p.m_6034_(this.m_20185_(), this.m_20186_(), this.m_20189_());
                p.m_37446_(this.m_7846_());
                ent = p;
            } else if (item == Items.FIRE_CHARGE) {
                SmallFireball p = new SmallFireball(level, le, this.m_20185_(), this.m_20186_(), this.m_20189_());
                p.m_6034_(this.m_20185_(), this.m_20186_(), this.m_20189_());
                p.m_37010_(this.m_7846_());
                ent = p;
            } else if (item instanceof SnowballItem) {
                Snowball s = new Snowball(level, le);
                s.m_6034_(this.m_20185_(), this.m_20186_(), this.m_20189_());
                s.m_37446_(this.m_7846_());
                ent = s;
            } else if (item instanceof BombItem bi) {
                BombEntity s = new BombEntity(level, le, bi.getType());
                s.m_6034_(this.m_20185_(), this.m_20186_(), this.m_20189_());
                s.m_37446_(this.m_7846_());
                ent = s;
            } else if (item instanceof EnderpearlItem) {
                ThrownEnderpearl s = new ThrownEnderpearl(level, le);
                s.m_6034_(this.m_20185_(), this.m_20186_(), this.m_20189_());
                s.m_37446_(this.m_7846_());
                ent = s;
            }
            if (ent != null) {
                level.m_7967_(ent);
                ent.tick();
                return true;
            }
        }
        return false;
    }

    @Override
    public void tick() {
        if (this.isNoPhysics()) {
            int i = this.f_19804_.get(ID_LOYALTY);
            Entity owner = this.m_19749_();
            if (i > 0 && this.isAcceptableReturnOwner(owner)) {
                Vec3 vector3d = new Vec3(owner.getX() - this.m_20185_(), owner.getEyeY() - this.m_20186_(), owner.getZ() - this.m_20189_());
                this.m_20343_(this.m_20185_(), this.m_20186_() + vector3d.y * 0.015 * (double) i, this.m_20189_());
                if (this.m_9236_().isClientSide) {
                    this.f_19791_ = this.m_20186_();
                }
                double d0 = 0.05 * (double) i;
                this.m_20256_(this.m_20184_().scale(0.95).add(vector3d.normalize().scale(d0)));
            }
        }
        super.tick();
    }

    private boolean isAcceptableReturnOwner(Entity owner) {
        return owner != null && owner.isAlive() ? !(owner instanceof ServerPlayer) || !owner.isSpectator() : false;
    }

    @Override
    public void playerTouch(Player playerEntity) {
        if (this.isNoPhysics() || this.touchedGround) {
            boolean success = playerEntity.getAbilities().instabuild || playerEntity.getInventory().add(this.m_7846_());
            Level level = this.m_9236_();
            if (!level.isClientSide) {
                if (!success) {
                    this.m_5552_(this.m_7846_(), 0.1F);
                }
            } else {
                level.playLocalSound(this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, (this.f_19796_.nextFloat() - this.f_19796_.nextFloat()) * 1.4F + 2.0F, false);
            }
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    @Override
    public boolean hasReachedEndOfLife() {
        return this.m_20068_() && this.m_20184_().lengthSqr() < 0.005 ? true : super.hasReachedEndOfLife() && !this.isNoPhysics();
    }

    @Override
    public void reachedEndOfLife() {
        if (this.f_19804_.get(ID_LOYALTY) != 0 && this.isAcceptableReturnOwner(this.m_19749_())) {
            this.setNoPhysics(true);
            this.groundTime = 0;
        } else {
            this.m_5552_(this.m_7846_(), 0.1F);
            super.reachedEndOfLife();
        }
    }

    @Override
    protected void updateRotation() {
        if (!this.isNoPhysics()) {
            this.f_19860_ = this.m_146909_();
            this.f_19859_ = this.m_146908_();
            this.m_146926_(this.m_146909_() + this.xRotInc);
            this.m_146922_(this.m_146908_() + this.yRotInc);
            this.particleCooldown++;
        } else {
            super.m_37283_();
        }
    }

    @Override
    public void spawnTrailParticles() {
        Vec3 newPos = this.m_20182_();
        Vec3 currentPos = new Vec3(this.f_19854_, this.f_19855_, this.f_19856_);
        if (!this.isNoPhysics()) {
            double d = this.m_20184_().length();
            if (this.f_19797_ > 1 && d * (double) this.f_19797_ > 1.5) {
                if (this.m_20068_()) {
                    Vec3 rot = new Vec3(0.325, 0.0, 0.0).yRot((float) this.f_19797_ * 0.32F);
                    Vec3 movement = this.m_20184_();
                    Vec3 offset = MthUtils.changeBasisN(movement, rot);
                    double px = newPos.x + offset.x;
                    double py = newPos.y + offset.y;
                    double pz = newPos.z + offset.z;
                    movement = movement.scale(0.25);
                    this.m_9236_().addParticle((ParticleOptions) ModParticles.STASIS_PARTICLE.get(), px, py, pz, movement.x, movement.y, movement.z);
                } else {
                    double interval = 4.0 / (d * 0.95 + 0.05);
                    if ((double) this.particleCooldown > interval) {
                        this.particleCooldown = (float) ((double) this.particleCooldown - interval);
                        double x = currentPos.x;
                        double y = currentPos.y;
                        double z = currentPos.z;
                        this.m_9236_().addParticle((ParticleOptions) ModParticles.SLINGSHOT_PARTICLE.get(), x, y, z, 0.0, 0.01, 0.0);
                    }
                }
            }
        }
    }

    @Override
    protected float getInertia() {
        return this.m_20068_() ? (float) ((Double) CommonConfigs.Tools.SLINGSHOT_DECELERATION.get()).doubleValue() : super.getInertia();
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        Entity entity = this.m_19749_();
        int id = -1;
        if (entity != null) {
            id = entity.getId();
        }
        buffer.writeInt(id);
        buffer.writeFloat(this.xRotInc);
        buffer.writeFloat(this.yRotInc);
        buffer.writeFloat(this.m_146909_());
        buffer.writeFloat(this.m_146908_());
    }

    @Override
    public void readSpawnData(FriendlyByteBuf buffer) {
        int id = buffer.readInt();
        if (id != -1) {
            this.m_5602_(this.m_9236_().getEntity(id));
        }
        this.xRotInc = buffer.readFloat();
        this.yRotInc = buffer.readFloat();
        this.m_146926_(buffer.readFloat());
        this.m_146922_(buffer.readFloat());
        this.f_19860_ = this.m_146909_();
        this.f_19859_ = this.m_146908_();
    }

    public int getLightEmission() {
        return (Integer) this.light.get();
    }
}