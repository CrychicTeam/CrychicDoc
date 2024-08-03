package com.github.alexmodguy.alexscaves.server.entity.item;

import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACDamageTypes;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;

public class DinosaurSpiritEntity extends Entity {

    private static final EntityDataAccessor<Optional<UUID>> PLAYER_ID = SynchedEntityData.defineId(DinosaurSpiritEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Integer> DINOSAUR_TYPE = SynchedEntityData.defineId(DinosaurSpiritEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> ATTACKING_ENTITY_ID = SynchedEntityData.defineId(DinosaurSpiritEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> DELAY_SPAWN = SynchedEntityData.defineId(DinosaurSpiritEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> FADING = SynchedEntityData.defineId(DinosaurSpiritEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> USING_ABILITY = SynchedEntityData.defineId(DinosaurSpiritEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Float> ROTATE_OFFSET = SynchedEntityData.defineId(DinosaurSpiritEntity.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Integer> ENCHANTMENT_LEVEL = SynchedEntityData.defineId(DinosaurSpiritEntity.class, EntityDataSerializers.INT);

    private float fadeIn = 0.0F;

    private float prevFadeIn = 0.0F;

    private int duration = 0;

    private float abilityProgress = 0.0F;

    private float prevAbilityProgress = 0.0F;

    private int lSteps;

    private double lx;

    private double ly;

    private double lz;

    private double lxr;

    private double lxd;

    private double lyd;

    private double lzd;

    private boolean dealtDamage = false;

    public DinosaurSpiritEntity(EntityType entityType, Level level) {
        super(entityType, level);
    }

    public DinosaurSpiritEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(ACEntityRegistry.BEHOLDER_EYE.get(), level);
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(PLAYER_ID, Optional.empty());
        this.f_19804_.define(DINOSAUR_TYPE, 0);
        this.f_19804_.define(ATTACKING_ENTITY_ID, -1);
        this.f_19804_.define(DELAY_SPAWN, 0);
        this.f_19804_.define(FADING, false);
        this.f_19804_.define(USING_ABILITY, false);
        this.f_19804_.define(ENCHANTMENT_LEVEL, 0);
        this.f_19804_.define(ROTATE_OFFSET, 0.0F);
    }

    @Override
    public void tick() {
        super.tick();
        Player player = this.getUsingPlayer();
        this.prevFadeIn = this.fadeIn;
        this.prevAbilityProgress = this.abilityProgress;
        if (this.getDelaySpawn() > 0) {
            this.setDelaySpawn(this.getDelaySpawn() - 1);
            this.fadeIn = 0.0F;
            if (this.getDelaySpawn() == 0) {
                this.m_5496_(ACSoundRegistry.EXTINCTION_SPEAR_SUMMON.get(), 1.0F, 1.0F);
            }
        } else {
            if (this.isFading() && this.fadeIn > 0.0F) {
                this.fadeIn--;
            }
            if (!this.isFading() && this.fadeIn < 10.0F) {
                this.fadeIn++;
            }
            if (this.isUsingAbility() && this.abilityProgress < 5.0F) {
                this.abilityProgress++;
            }
            if (!this.isUsingAbility() && this.abilityProgress > 0.0F) {
                this.abilityProgress--;
            }
            if (this.isFading() && this.fadeIn <= 0.0F) {
                this.m_146870_();
            }
            if (this.m_9236_().isClientSide) {
                this.m_9236_().addParticle(ParticleTypes.FLAME, this.m_20208_(1.0), this.m_20187_(), this.m_20262_(1.0), 0.0, 0.0, 0.0);
            }
            this.m_6478_(MoverType.SELF, this.m_20184_());
            this.m_20256_(this.m_20184_().scale(0.9F));
            if (player == null) {
                this.setFading(true);
            } else {
                switch(this.getDinosaurType()) {
                    case SUBTERRANODON:
                        this.tickSubterranodon(player);
                        break;
                    case GROTTOCERATOPS:
                        this.tickGrottoceratops(player);
                        break;
                    case TREMORSAURUS:
                        this.tickTremorsaurus(player);
                }
            }
            if (this.m_9236_().isClientSide) {
                if (this.lSteps > 0) {
                    double d5 = this.m_20185_() + (this.lx - this.m_20185_()) / (double) this.lSteps;
                    double d6 = this.m_20186_() + (this.ly - this.m_20186_()) / (double) this.lSteps;
                    double d7 = this.m_20189_() + (this.lz - this.m_20189_()) / (double) this.lSteps;
                    this.m_146926_(this.m_146909_() + (float) (this.lxr - (double) this.m_146909_()) / (float) this.lSteps);
                    this.lSteps--;
                    this.m_6034_(d5, d6, d7);
                } else {
                    this.m_20090_();
                }
            }
        }
    }

    @Override
    public void lerpTo(double x, double y, double z, float yr, float xr, int steps, boolean b) {
        this.lx = x;
        this.ly = y;
        this.lz = z;
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

    private void tickSubterranodon(Player player) {
        Entity target = this.getAttackingEntity();
        this.m_20256_(this.m_20184_().add(0.0, 0.03 + (double) (0.005F * (float) this.getEnchantmentLevel()), 0.0));
        if (target != null && this.duration < 40 + this.getEnchantmentLevel() * 5) {
            Vec3 targetMovePos = this.m_20182_().subtract(0.0, (double) target.getBbHeight(), 0.0);
            target.setDeltaMovement(Vec3.ZERO);
            target.setPos(targetMovePos.x, targetMovePos.y, targetMovePos.z);
            this.duration++;
        } else {
            this.setFading(true);
        }
    }

    private void tickGrottoceratops(Player player) {
        float rot = this.getRotateOffset() + (float) (player.f_19797_ * 5);
        Vec3 orbitBy = new Vec3(0.0, 1.0, 2.0).yRot((float) (-Math.toRadians((double) rot)));
        Vec3 orbitTarget = player.m_20182_().add(orbitBy).subtract(this.m_20182_());
        this.m_146926_(10.0F);
        this.m_20256_(orbitTarget.scale(0.25));
        this.f_19794_ = true;
        if (!this.m_9236_().isClientSide && !player.m_21211_().is(ACItemRegistry.EXTINCTION_SPEAR.get())) {
            this.setFading(true);
        }
    }

    private void tickTremorsaurus(Player player) {
        Entity target = this.getAttackingEntity();
        if (target != null) {
            this.f_19794_ = true;
            this.m_7618_(EntityAnchorArgument.Anchor.EYES, target.getEyePosition());
            boolean inRange = (double) this.m_20270_(target) < (double) target.getBbWidth() + 3.5;
            if (!inRange) {
                Vec3 targetPos = target.position().subtract(this.m_20182_());
                if (targetPos.length() > 1.0) {
                    targetPos = targetPos.normalize();
                }
                this.m_20256_(targetPos.scale(0.15F));
            }
            this.setUsingAbility(true);
            if (inRange && this.abilityProgress >= 5.0F) {
                if (!this.dealtDamage && target.hurt(ACDamageTypes.causeSpiritDinosaurDamage(this.m_9236_().registryAccess(), player), (float) (3 + 2 * this.getEnchantmentLevel()))) {
                    this.dealtDamage = true;
                }
                this.setFading(true);
            }
        }
        if (this.duration++ > 20) {
            this.setFading(true);
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("UsingPlayerUUID")) {
            this.setPlayerUUID(tag.getUUID("UsingPlayerUUID"));
        }
        this.setDinosaurTypeInt(tag.getInt("DinosaurType"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        UUID uuid1 = this.getPlayerUUID();
        if (uuid1 != null) {
            tag.putUUID("UsingPlayerUUID", uuid1);
        }
        tag.putInt("DinosaurType", this.getDinosaurTypeInt());
    }

    public void setPlayerUUID(UUID uuid) {
        this.f_19804_.set(PLAYER_ID, Optional.ofNullable(uuid));
    }

    public UUID getPlayerUUID() {
        return (UUID) this.f_19804_.get(PLAYER_ID).orElse(null);
    }

    public Player getUsingPlayer() {
        UUID id = this.getPlayerUUID();
        if (id == null) {
            return null;
        } else {
            return (Player) (this.m_9236_().isClientSide ? this.m_9236_().m_46003_(id) : this.m_9236_().getServer().getPlayerList().getPlayer(id));
        }
    }

    private int getDinosaurTypeInt() {
        return this.f_19804_.get(DINOSAUR_TYPE);
    }

    private void setDinosaurTypeInt(int type) {
        this.f_19804_.set(DINOSAUR_TYPE, type);
    }

    public int getDelaySpawn() {
        return this.f_19804_.get(DELAY_SPAWN);
    }

    public void setDelaySpawn(int type) {
        this.f_19804_.set(DELAY_SPAWN, type);
    }

    public int getEnchantmentLevel() {
        return this.f_19804_.get(ENCHANTMENT_LEVEL);
    }

    public void setEnchantmentLevel(int type) {
        this.f_19804_.set(ENCHANTMENT_LEVEL, type);
    }

    public void setAttackingEntityId(int id) {
        this.f_19804_.set(ATTACKING_ENTITY_ID, id);
    }

    public Entity getAttackingEntity() {
        int id = this.f_19804_.get(ATTACKING_ENTITY_ID);
        return id == -1 ? null : this.m_9236_().getEntity(id);
    }

    private float getRotateOffset() {
        return this.f_19804_.get(ROTATE_OFFSET);
    }

    public void setRotateOffset(float rotateOffset) {
        this.f_19804_.set(ROTATE_OFFSET, rotateOffset);
    }

    public DinosaurSpiritEntity.DinosaurType getDinosaurType() {
        return DinosaurSpiritEntity.DinosaurType.values()[Mth.clamp(this.getDinosaurTypeInt(), 0, DinosaurSpiritEntity.DinosaurType.values().length)];
    }

    public void setDinosaurType(DinosaurSpiritEntity.DinosaurType type) {
        this.f_19804_.set(DINOSAUR_TYPE, type.ordinal());
    }

    public boolean isFading() {
        return this.f_19804_.get(FADING);
    }

    public void setFading(boolean bool) {
        this.f_19804_.set(FADING, bool);
    }

    public boolean isUsingAbility() {
        return this.f_19804_.get(USING_ABILITY);
    }

    public void setUsingAbility(boolean bool) {
        this.f_19804_.set(USING_ABILITY, bool);
    }

    public float getFadeIn(float partialTicks) {
        return (this.prevFadeIn + (this.fadeIn - this.prevFadeIn) * partialTicks) * 0.1F;
    }

    public float getAbilityProgress(float partialTicks) {
        return (this.prevAbilityProgress + (this.abilityProgress - this.prevAbilityProgress) * partialTicks) * 0.2F;
    }

    public static enum DinosaurType {

        SUBTERRANODON, GROTTOCERATOPS, TREMORSAURUS
    }
}