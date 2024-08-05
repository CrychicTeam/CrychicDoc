package com.github.alexmodguy.alexscaves.server.entity.item;

import com.github.alexmodguy.alexscaves.server.enchantment.ACEnchantmentRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACDamageTypes;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class DesolateDaggerEntity extends Entity {

    private static final EntityDataAccessor<Integer> TARGET_ID = SynchedEntityData.defineId(DesolateDaggerEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Float> STAB = SynchedEntityData.defineId(DesolateDaggerEntity.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Integer> PLAYER_ID = SynchedEntityData.defineId(DesolateDaggerEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<ItemStack> ITEMSTACK = SynchedEntityData.defineId(DesolateDaggerEntity.class, EntityDataSerializers.ITEM_STACK);

    protected final RandomSource orbitRandom = RandomSource.create();

    private float orbitOffset = 0.0F;

    private float prevStab = 0.0F;

    public int orbitFor = 20;

    public ItemStack daggerRenderStack = new ItemStack(ACItemRegistry.DESOLATE_DAGGER.get());

    private int lSteps;

    private double lx;

    private double ly;

    private double lz;

    private double lyr;

    private double lxr;

    private double lxd;

    private double lyd;

    private double lzd;

    private boolean playedSummonNoise = false;

    public DesolateDaggerEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.orbitFor = 20 + level.random.nextInt(10);
    }

    public DesolateDaggerEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(ACEntityRegistry.DESOLATE_DAGGER.get(), level);
        this.m_20011_(this.m_142242_());
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        super.tick();
        this.prevStab = this.getStab();
        Entity entity = this.getTargetEntity();
        if (this.m_9236_().isClientSide) {
            this.m_9236_().addParticle(DustParticleOptions.REDSTONE, this.m_20208_(0.75), this.m_20187_(), this.m_20262_(0.75), 0.0, 0.0, 0.0);
        }
        if (!this.playedSummonNoise) {
            this.m_216990_(ACSoundRegistry.DESOLATE_DAGGER_SUMMON.get());
            this.playedSummonNoise = true;
        }
        if (entity != null) {
            this.f_19794_ = true;
            float invStab = 1.0F - this.getStab();
            Vec3 orbitAround = entity.position().add(0.0, (double) (entity.getBbHeight() * 0.25F), 0.0);
            this.orbitRandom.setSeed((long) this.m_19879_());
            if (this.orbitOffset == 0.0F) {
                this.orbitOffset = (float) this.orbitRandom.nextInt(360);
            }
            Vec3 orbitAdd = new Vec3(0.0, (double) ((this.orbitRandom.nextFloat() + entity.getBbHeight()) * invStab), (double) ((this.orbitRandom.nextFloat() + entity.getBbWidth()) * invStab)).yRot((float) Math.toRadians((double) this.orbitOffset));
            this.m_20256_(orbitAround.add(orbitAdd).subtract(this.m_20182_()));
            if (!this.m_9236_().isClientSide) {
                if (this.orbitFor > 0 && entity.isAlive()) {
                    this.orbitFor--;
                } else {
                    this.setStab(Math.min(this.getStab() + 0.2F, 1.0F));
                }
                if (this.getStab() >= 1.0F) {
                    Entity player = this.getPlayer();
                    Entity damageFrom = (Entity) (player == null ? this : player);
                    float damage = 2.0F + (float) this.getItemStack().getEnchantmentLevel(ACEnchantmentRegistry.IMPENDING_STAB.get()) * 2.0F;
                    if (entity.hurt(ACDamageTypes.causeDesolateDaggerDamage(this.m_9236_().registryAccess(), damageFrom), damage)) {
                        this.m_216990_(ACSoundRegistry.DESOLATE_DAGGER_HIT.get());
                        int healBy = this.getItemStack().getEnchantmentLevel(ACEnchantmentRegistry.SATED_BLADE.get());
                        if (healBy > 0 && damageFrom instanceof Player healPlayer && healPlayer.getFoodData().getSaturationLevel() < 5.0F) {
                            healPlayer.getFoodData().setSaturation(healPlayer.getFoodData().getSaturationLevel() + (float) healBy * 0.1F);
                        }
                    }
                    this.m_146870_();
                }
            }
            double d1 = entity.getZ() - this.m_20189_();
            double d3 = entity.getEyeY() - this.m_20188_();
            double d2 = entity.getX() - this.m_20185_();
            float f = Mth.sqrt((float) (d2 * d2 + d1 * d1));
            this.m_146922_(-((float) Mth.atan2(d2, d1)) * (180.0F / (float) Math.PI));
            this.m_146926_(-((float) (Mth.atan2(d3, (double) f) * 180.0F / (float) Math.PI)));
        } else if (this.f_19797_ > 3) {
            this.f_19794_ = false;
            this.m_146870_();
        }
        this.m_6478_(MoverType.SELF, this.m_20184_());
        this.m_20256_(this.m_20184_().scale(0.9F));
        if (this.m_9236_().isClientSide) {
            if (this.lSteps > 0) {
                double d5 = this.m_20185_() + (this.lx - this.m_20185_()) / (double) this.lSteps;
                double d6 = this.m_20186_() + (this.ly - this.m_20186_()) / (double) this.lSteps;
                double d7 = this.m_20189_() + (this.lz - this.m_20189_()) / (double) this.lSteps;
                this.m_146922_(Mth.wrapDegrees((float) this.lyr));
                this.m_146926_(this.m_146909_() + (float) (this.lxr - (double) this.m_146909_()) / (float) this.lSteps);
                this.lSteps--;
                this.m_6034_(d5, d6, d7);
            } else {
                this.m_20090_();
            }
        }
    }

    public ItemStack getItemStack() {
        return this.f_19804_.get(ITEMSTACK);
    }

    public void setItemStack(ItemStack item) {
        this.f_19804_.set(ITEMSTACK, item);
    }

    @Override
    public void lerpTo(double x, double y, double z, float yr, float xr, int steps, boolean b) {
        this.lx = x;
        this.ly = y;
        this.lz = z;
        this.lyr = (double) yr;
        this.lxr = (double) xr;
        this.lSteps = steps;
        this.m_20334_(this.lxd, this.lyd, this.lzd);
    }

    @Override
    public void lerpMotion(double lerpX, double lerpY, double lerpZ) {
        this.lxd = lerpX;
        this.lyd = lerpY;
        this.lzd = lerpZ;
        this.m_20334_(this.lxd, this.lyd, this.lzd);
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(TARGET_ID, -1);
        this.f_19804_.define(PLAYER_ID, -1);
        this.f_19804_.define(STAB, 0.0F);
        this.f_19804_.define(ITEMSTACK, new ItemStack(Items.IRON_SWORD));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
    }

    private int getTargetId() {
        return this.f_19804_.get(TARGET_ID);
    }

    public void setTargetId(int id) {
        this.f_19804_.set(TARGET_ID, id);
    }

    private int getPlayerId() {
        return this.f_19804_.get(PLAYER_ID);
    }

    public void setPlayerId(int id) {
        this.f_19804_.set(PLAYER_ID, id);
    }

    public float getStab() {
        return this.f_19804_.get(STAB);
    }

    public float getStab(float partialTicks) {
        return this.prevStab + (this.getStab() - this.prevStab) * partialTicks;
    }

    public void setStab(float stab) {
        this.f_19804_.set(STAB, stab);
    }

    private Entity getTargetEntity() {
        int id = this.getTargetId();
        return id == -1 ? null : this.m_9236_().getEntity(id);
    }

    private Entity getPlayer() {
        int id = this.getPlayerId();
        return id == -1 ? null : this.m_9236_().getEntity(id);
    }
}