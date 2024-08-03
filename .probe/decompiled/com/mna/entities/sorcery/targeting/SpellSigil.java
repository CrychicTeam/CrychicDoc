package com.mna.entities.sorcery.targeting;

import com.mna.ManaAndArtifice;
import com.mna.api.affinity.Affinity;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.base.ISpellSigil;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.capabilities.worlddata.WorldMagicProvider;
import com.mna.entities.EntityInit;
import com.mna.items.ItemInit;
import com.mna.spells.SpellCaster;
import com.mna.spells.crafting.SpellRecipe;
import com.mna.tools.math.MathUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.network.NetworkHooks;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class SpellSigil extends Entity implements ISpellSigil<SpellSigil> {

    private static final EntityDataAccessor<Integer> AFFINITY = SynchedEntityData.defineId(SpellSigil.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> MAGNITUDE = SynchedEntityData.defineId(SpellSigil.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Float> RADIUS = SynchedEntityData.defineId(SpellSigil.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Optional<UUID>> CASTER = SynchedEntityData.defineId(SpellSigil.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Boolean> PERMANENT = SynchedEntityData.defineId(SpellSigil.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> COOLDOWN = SynchedEntityData.defineId(SpellSigil.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> CHARGES = SynchedEntityData.defineId(SpellSigil.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> PLAYERLESS = SynchedEntityData.defineId(SpellSigil.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Byte> SIZE = SynchedEntityData.defineId(SpellSigil.class, EntityDataSerializers.BYTE);

    private static final EntityDataAccessor<Byte> CAST_WITH_BONUS = SynchedEntityData.defineId(SpellSigil.class, EntityDataSerializers.BYTE);

    private UUID myID;

    private ISpellDefinition recipe;

    private Player _cachedCaster;

    private ArrayList<Entity> targetedEntities;

    private int cooldownTicks = 0;

    public SpellSigil(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
        this.targetedEntities = new ArrayList();
        this.myID = UUID.randomUUID();
    }

    public SpellSigil(Player caster, ISpellDefinition spell) {
        this(EntityInit.SPELL_RUNE.get(), caster.m_9236_());
        this.setCasterAndSpell(caster, spell);
    }

    public void setCastWithBonus(byte bonus) {
        this.f_19804_.set(CAST_WITH_BONUS, bonus);
    }

    @Override
    public void tick() {
        if (!this.f_19804_.get(PLAYERLESS) && !this.m_9236_().isClientSide() && this.f_19797_ % 20 == 0) {
            if (this.getCasterID() != null) {
                this.m_9236_().getCapability(WorldMagicProvider.MAGIC).ifPresent(m -> {
                    if (m.wasSigilRemoved(this.getCasterID(), this.myID)) {
                        this.m_142687_(Entity.RemovalReason.DISCARDED);
                    }
                });
            } else {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
        }
        List<Entity> entities = this.m_9236_().m_45933_(this, this.m_20191_());
        if (!this.m_9236_().isClientSide()) {
            if (this.cooldownTicks > 0 && entities.size() == 0) {
                this.cooldownTicks--;
            }
            if (this.cooldownTicks <= 0) {
                this.setOnCooldown(false);
            }
        }
        entities.forEach(e -> this.push(e));
    }

    private void setCasterAndSpell(Player caster, ISpellDefinition spell) {
        this._cachedCaster = caster;
        this.setCasterID(caster.m_20148_());
        this.recipe = spell;
        this.f_19804_.set(AFFINITY, spell.getHighestAffinity().ordinal());
        this.f_19804_.set(RADIUS, this.recipe.getShape().getValue(Attribute.RADIUS));
        this.f_19804_.set(MAGNITUDE, (int) spell.getShape().getValue(Attribute.MAGNITUDE));
        this.f_19804_.set(PERMANENT, false);
        this.f_19804_.set(COOLDOWN, false);
        this.f_19804_.set(CHARGES, 1);
        this.f_19804_.set(PLAYERLESS, false);
        this.f_19804_.set(CAST_WITH_BONUS, (byte) 0);
    }

    public void setPermanent() {
        this.f_19804_.set(PERMANENT, true);
    }

    public boolean isPermanent() {
        return this.f_19804_.get(PERMANENT);
    }

    public void setSize(byte size) {
        size = (byte) MathUtils.clamp(size, 1, 5);
        this.f_19804_.set(SIZE, size);
        this.m_20011_(this.makeBoundingBox());
    }

    @Override
    protected AABB makeBoundingBox() {
        byte size = this.getSize();
        return new AABB(this.m_20182_().add(-0.5 * (double) size, 0.0, -0.5 * (double) size), this.m_20182_().add(0.5 * (double) size, 0.1, 0.5 * (double) size));
    }

    public byte getSize() {
        return this.f_19804_.get(SIZE);
    }

    public void setOnCooldown(boolean onCooldown) {
        this.f_19804_.set(COOLDOWN, onCooldown);
        this.cooldownTicks = 60;
    }

    public boolean isOnCooldown() {
        return this.f_19804_.get(COOLDOWN);
    }

    public void addCharge() {
        int numCharges = this.getCharges();
        if (numCharges < 10) {
            this.f_19804_.set(CHARGES, numCharges + 1);
        }
    }

    public void consumeCharge() {
        this.f_19804_.set(CHARGES, this.getCharges() - 1);
        this.setOnCooldown(true);
    }

    public int getCharges() {
        return this.f_19804_.get(CHARGES);
    }

    public Affinity getAffinity() {
        return Affinity.values()[this.f_19804_.get(AFFINITY)];
    }

    @Override
    public InteractionResult interact(Player pPlayer, InteractionHand pHand) {
        if (pPlayer.m_21120_(pHand).getItem() == ItemInit.__DEBUG.get()) {
            this.f_19804_.set(PLAYERLESS, true);
            this.f_19804_.set(CASTER, Optional.of(Util.NIL_UUID));
            this._cachedCaster = null;
            if (!pPlayer.m_9236_().isClientSide()) {
                pPlayer.m_213846_(Component.translatable("item.mna.debug_wand.playerless"));
            }
            return InteractionResult.sidedSuccess(pPlayer.m_9236_().isClientSide());
        } else {
            return super.interact(pPlayer, pHand);
        }
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public void push(Entity entityIn) {
        if (entityIn instanceof LivingEntity || entityIn instanceof ItemEntity) {
            if (this.f_19804_.get(MAGNITUDE) == 2) {
                if (!(entityIn instanceof LivingEntity)) {
                    return;
                }
            } else if (this.f_19804_.get(MAGNITUDE) == 3 && !(entityIn instanceof Player)) {
                return;
            }
            this.trigger(entityIn, false);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        this.trigger(source.getDirectEntity(), true);
        return true;
    }

    private void trigger(Entity e, boolean forceRemove) {
        if (this.m_6084_() && (forceRemove || !this.isOnCooldown())) {
            if (!this.isPermanent() || this.getCharges() > 0) {
                float radius = this.f_19804_.get(RADIUS);
                if (this.m_9236_().isClientSide()) {
                    this.spawnParticles(radius);
                } else {
                    if (!this.isPermanent() || forceRemove) {
                        this.m_142687_(Entity.RemovalReason.DISCARDED);
                    }
                    if (this.recipe == null) {
                        return;
                    }
                    SpellContext context = new SpellContext(this.m_9236_(), this.recipe, this);
                    boolean applied = this.targetEntities(radius, context) || this.targetBlocks(radius, context);
                    if (applied) {
                        if (this.isPermanent()) {
                            this.consumeCharge();
                        }
                        if (!this.m_9236_().isClientSide() && !this.m_6084_() && !this.f_19804_.get(PLAYERLESS)) {
                            this.m_9236_().getCapability(WorldMagicProvider.MAGIC).ifPresent(m -> m.removeSigil(this.getCaster() != null ? this.getCaster().m_20148_() : null, this.myID));
                        }
                    }
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void spawnParticles(float radius) {
        Affinity affinity = Affinity.values()[this.f_19804_.get(AFFINITY)];
        MAParticleType particle = null;
        SoundEvent sound = null;
        switch(affinity) {
            case ARCANE:
                particle = ParticleInit.ARCANE.get();
                sound = radius < 2.0F ? SFX.Spell.Impact.Single.ARCANE : SFX.Spell.Impact.AoE.ARCANE;
                break;
            case EARTH:
                particle = ParticleInit.DUST.get();
                sound = radius < 2.0F ? SFX.Spell.Impact.Single.EARTH : SFX.Spell.Impact.AoE.EARTH;
                break;
            case ENDER:
                particle = ParticleInit.ENDER.get();
                sound = SFX.Spell.Impact.Single.ENDER;
                break;
            case FIRE:
                particle = ParticleInit.FLAME.get();
                sound = radius < 2.0F ? SFX.Spell.Impact.Single.FIRE : SFX.Spell.Impact.AoE.FIRE;
                break;
            case HELLFIRE:
                particle = ParticleInit.HELLFIRE.get();
                sound = radius < 2.0F ? SFX.Spell.Impact.Single.FIRE : SFX.Spell.Impact.AoE.FIRE;
                break;
            case WATER:
                particle = ParticleInit.FROST.get();
                sound = SFX.Spell.Impact.Single.ICE;
                break;
            case WIND:
                particle = ParticleInit.AIR_VELOCITY.get();
                sound = radius < 2.0F ? SFX.Spell.Impact.Single.WIND : SFX.Spell.Impact.AoE.WIND;
                break;
            case UNKNOWN:
            default:
                particle = ParticleInit.SPARKLE_STATIONARY.get();
        }
        for (int i = 0; (float) i < 20.0F * radius; i++) {
            Vec3 position = this.m_20182_().add((double) (-radius) + Math.random() * (double) radius, (double) (-radius) + Math.random() * (double) radius, (double) (-radius) + Math.random() * (double) radius);
            MAParticleType inst = new MAParticleType(particle);
            if (this.recipe != null) {
                inst = this.recipe.colorParticle(inst, this.getOrCreatePlayer());
            }
            this.m_9236_().addParticle(inst, position.x, position.y, position.z, 0.0, 0.4, 0.0);
        }
        if (sound != null) {
            this.m_9236_().playLocalSound(this.m_20185_(), this.m_20186_(), this.m_20189_(), sound, SoundSource.PLAYERS, 1.0F, (float) (0.8F + Math.random() * 0.4F), false);
        }
    }

    private boolean targetEntities(float radius, SpellContext context) {
        if (this.recipe != null && this.recipe.isValid()) {
            if (!this.recipe.getComponents().stream().anyMatch(c -> ((SpellEffect) c.getPart()).targetsEntities())) {
                return false;
            } else {
                SpellSource source = new SpellSource(this.getOrCreatePlayer(), InteractionHand.MAIN_HAND);
                MutableBoolean applied_success = new MutableBoolean(false);
                List<Entity> entities = this.m_9236_().m_45933_(this, this.m_20191_().inflate((double) (radius - 1.0F)));
                this.targetedEntities.clear();
                entities.forEach(e -> {
                    if (!this.targetedEntities.contains(e)) {
                        this.targetedEntities.add(e);
                        if (SpellCaster.ApplyComponents(this.recipe, source, new SpellTarget(e), context).values().stream().anyMatch(c -> c.is_success)) {
                            applied_success.setTrue();
                        }
                    }
                });
                return applied_success.booleanValue();
            }
        } else {
            return false;
        }
    }

    protected Player getOrCreatePlayer() {
        Player caster = this.getCaster();
        return (Player) (caster != null && caster.m_20280_(this) < 4096.0 ? caster : FakePlayerFactory.getMinecraft((ServerLevel) this.m_9236_()));
    }

    private boolean targetBlocks(float radius, SpellContext context) {
        if (this.recipe != null && this.recipe.isValid()) {
            if (!this.recipe.getComponents().stream().anyMatch(c -> ((SpellEffect) c.getPart()).targetsBlocks())) {
                return false;
            } else {
                SpellSource source = new SpellSource(this.getOrCreatePlayer(), InteractionHand.MAIN_HAND);
                MutableBoolean applied_success = new MutableBoolean(false);
                int iRadius = (int) Math.floor((double) radius);
                for (int i = -iRadius; i <= iRadius; i++) {
                    for (int j = -iRadius; j <= iRadius; j++) {
                        for (int k = -iRadius; k <= iRadius; k++) {
                            BlockPos offset = new BlockPos(i, j, k);
                            if (SpellCaster.ApplyComponents(this.recipe, source, new SpellTarget(offset, Direction.DOWN), context).values().stream().anyMatch(c -> c.is_success)) {
                                applied_success.setTrue();
                            }
                        }
                    }
                }
                return applied_success.booleanValue();
            }
        } else {
            return false;
        }
    }

    @Nullable
    private Player getCaster() {
        if (this._cachedCaster == null && this.getCasterID() != null) {
            this._cachedCaster = this.m_9236_().m_46003_(this.getCasterID());
        }
        return this._cachedCaster;
    }

    @Nullable
    private UUID getCasterID() {
        return (UUID) this.f_19804_.get(CASTER).get();
    }

    public boolean isCaster(Player player) {
        return player.getGameProfile() != null && player.getGameProfile().getId() != null && this.getCasterID() != null ? this.getCasterID().equals(player.getGameProfile().getId()) : false;
    }

    private void setCasterID(UUID casterID) {
        this.f_19804_.set(CASTER, Optional.of(casterID));
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (key == CASTER) {
            this._cachedCaster = null;
        }
        super.onSyncedDataUpdated(key);
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(AFFINITY, Affinity.UNKNOWN.ordinal());
        this.f_19804_.define(RADIUS, 1.0F);
        this.f_19804_.define(CASTER, Optional.empty());
        this.f_19804_.define(MAGNITUDE, 0);
        this.f_19804_.define(PERMANENT, false);
        this.f_19804_.define(COOLDOWN, false);
        this.f_19804_.define(CHARGES, 1);
        this.f_19804_.define(PLAYERLESS, true);
        this.f_19804_.define(SIZE, (byte) 1);
        this.f_19804_.define(CAST_WITH_BONUS, (byte) 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("caster")) {
            try {
                this.setCasterID(UUID.fromString(compound.getString("caster")));
            } catch (Exception var4) {
                ManaAndArtifice.LOGGER.error("Failed to load caster ID when loading rune entity");
                ManaAndArtifice.LOGGER.throwing(var4);
            }
        }
        if (compound.contains("recipe")) {
            this.recipe = SpellRecipe.fromNBT(compound.getCompound("recipe"));
        }
        if (compound.contains("affinity")) {
            this.f_19804_.set(AFFINITY, compound.getInt("affinity"));
        }
        if (compound.contains("charges")) {
            this.f_19804_.set(CHARGES, compound.getInt("charges"));
        }
        if (compound.contains("permanent")) {
            this.f_19804_.set(PERMANENT, compound.getBoolean("permanent"));
        }
        if (compound.contains("cooldown")) {
            this.f_19804_.set(COOLDOWN, compound.getBoolean("cooldown"));
        }
        if (compound.contains("customSize")) {
            this.setSize(compound.getByte("customSize"));
        }
        if (compound.contains("cooldownTicks")) {
            this.cooldownTicks = compound.getInt("cooldownTicks");
        }
        if (compound.contains("invisible")) {
            this.m_6842_(compound.getBoolean("invisible"));
        }
        if (compound.contains("bonus")) {
            this.f_19804_.set(CAST_WITH_BONUS, compound.getByte("bonus"));
        }
        if (compound.contains("myUUID")) {
            try {
                this.myID = UUID.fromString(compound.getString("myUUID"));
            } catch (Exception var3) {
                ManaAndArtifice.LOGGER.error("Failed to load UUID when loading EntitySpellRune");
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putString("caster", this.getCasterID() != null ? this.getCasterID().toString() : "");
        compound.putString("myUUID", this.myID.toString());
        compound.putInt("affinity", this.f_19804_.get(AFFINITY));
        compound.putInt("charges", this.f_19804_.get(CHARGES));
        compound.putBoolean("permanent", this.f_19804_.get(PERMANENT));
        compound.putBoolean("cooldown", this.f_19804_.get(COOLDOWN));
        compound.putByte("customSize", this.getSize());
        compound.putInt("cooldownTicks", this.cooldownTicks);
        compound.putBoolean("invisible", this.m_20145_());
        compound.putByte("bonus", this.f_19804_.get(CAST_WITH_BONUS));
        CompoundTag recipeNBT = new CompoundTag();
        if (this.recipe != null) {
            this.recipe.writeToNBT(recipeNBT);
        }
        compound.put("recipe", recipeNBT);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public void onAddedToWorld() {
        super.onAddedToWorld();
        if (!this.m_9236_().isClientSide() && this.getCaster() != null) {
            this.m_9236_().getCapability(WorldMagicProvider.MAGIC).ifPresent(m -> m.addSigil(this.getCaster(), this, this.f_19804_.get(CAST_WITH_BONUS)));
        }
    }

    @Override
    public UUID getID() {
        return this.myID;
    }

    @Override
    public int getCountBonus() {
        return this.f_19804_.get(CAST_WITH_BONUS);
    }
}