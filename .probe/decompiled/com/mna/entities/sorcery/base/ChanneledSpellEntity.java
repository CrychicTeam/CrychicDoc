package com.mna.entities.sorcery.base;

import com.mna.api.affinity.Affinity;
import com.mna.api.sound.SFX;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.sound.EntityAliveLoopingSound;
import com.mna.spells.crafting.SpellRecipe;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import org.apache.commons.lang3.mutable.MutableInt;

public abstract class ChanneledSpellEntity extends Entity {

    private static final EntityDataAccessor<Integer> CASTER_ID = SynchedEntityData.defineId(ChanneledSpellEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<CompoundTag> SPELL_RECIPE = SynchedEntityData.defineId(ChanneledSpellEntity.class, EntityDataSerializers.COMPOUND_TAG);

    private static final String NBT_CASTER_UUID = "caster_uuid";

    private static final String NBT_SPELL = "spell";

    private LivingEntity __cachedCaster = null;

    private SpellRecipe __cachedRecipe = null;

    private boolean __playedsound = false;

    private Affinity[] cachedAffinities = null;

    public ChanneledSpellEntity(EntityType<? extends ChanneledSpellEntity> entityType, Level world) {
        super(entityType, world);
        this.m_20242_(true);
        this.m_20331_(true);
    }

    public ChanneledSpellEntity(EntityType<? extends ChanneledSpellEntity> entityType, LivingEntity caster, ISpellDefinition spell, Level world) {
        this(entityType, world);
        this.setCaster(caster);
        this.setSpell(spell);
    }

    @Override
    public void tick() {
        LivingEntity caster = this.getCaster();
        if (caster != null && caster.isAlive() && caster.m_9236_().dimension().equals(this.m_9236_().dimension()) && caster.getUseItemRemainingTicks() > 0 && !caster.getUseItem().isEmpty()) {
            if (!this.m_9236_().isClientSide() && this.f_19797_ >= this.getMaxAge()) {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            } else {
                SpellRecipe recipe = this.getSpell();
                if (!this.m_9236_().isClientSide() && !recipe.isValid()) {
                    this.m_142687_(Entity.RemovalReason.DISCARDED);
                } else {
                    boolean distanceSkip = this.m_20182_().distanceToSqr(this.f_19854_, this.f_19855_, this.f_19856_) > 4.0;
                    this.f_19854_ = this.m_20185_();
                    this.f_19855_ = this.m_20186_();
                    this.f_19856_ = this.m_20189_();
                    if (!distanceSkip) {
                        if (!this.m_9236_().isClientSide()) {
                            if (this.f_19797_ > 0 && (this.getApplicationRate() == 1 || this.f_19797_ % this.getApplicationRate() == 0)) {
                                this.applyEffect(caster.getUseItem(), recipe, caster, (ServerLevel) this.m_9236_());
                            }
                        } else {
                            this.spawnParticles();
                            this.playSounds();
                        }
                    }
                }
            }
        } else {
            if (!this.m_9236_().isClientSide()) {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
        }
    }

    protected int getApplicationRate() {
        return 10;
    }

    protected abstract void applyEffect(ItemStack var1, SpellRecipe var2, LivingEntity var3, ServerLevel var4);

    protected SoundEvent getSoundEffect(SpellRecipe recipe) {
        switch(recipe.getHighestAffinity()) {
            case ARCANE:
                return SFX.Loops.ARCANE;
            case EARTH:
                return SFX.Loops.EARTH;
            case ENDER:
                return SFX.Loops.ENDER;
            case FIRE:
                return SFX.Loops.FIRE;
            case HELLFIRE:
                return SFX.Loops.FIRE;
            case LIGHTNING:
                return SFX.Loops.LIGHTNING;
            case WATER:
                return SFX.Loops.WATER;
            case ICE:
                return SFX.Loops.ICE;
            case WIND:
                return SFX.Loops.AIR;
            case UNKNOWN:
            default:
                return SFX.Loops.MANAWEAVING;
        }
    }

    protected abstract void spawnAirParticles(SpellRecipe var1);

    protected abstract void spawnEarthParticles(SpellRecipe var1);

    protected abstract void spawnFireParticles(SpellRecipe var1, boolean var2, boolean var3);

    protected abstract void spawnWaterParticles(SpellRecipe var1, boolean var2);

    protected abstract void spawnEnderParticles(SpellRecipe var1);

    protected abstract void spawnArcaneParticles(SpellRecipe var1);

    protected void spawnParticles() {
        SpellRecipe recipe = this.getSpell();
        if (recipe != null) {
            if (this.cachedAffinities == null) {
                this.cachedAffinities = (Affinity[]) recipe.getAffinity().keySet().toArray(new Affinity[0]);
            }
            switch(this.cachedAffinities[(int) (Math.random() * (double) this.cachedAffinities.length)]) {
                case ARCANE:
                    this.spawnArcaneParticles(recipe);
                    break;
                case EARTH:
                    this.spawnEarthParticles(recipe);
                    break;
                case ENDER:
                    this.spawnEnderParticles(recipe);
                    break;
                case FIRE:
                    this.spawnFireParticles(recipe, false, false);
                    break;
                case HELLFIRE:
                    this.spawnFireParticles(recipe, true, false);
                    break;
                case LIGHTNING:
                    this.spawnFireParticles(recipe, false, true);
                    break;
                case WATER:
                    this.spawnWaterParticles(recipe, false);
                    break;
                case ICE:
                    this.spawnWaterParticles(recipe, true);
                    break;
                case WIND:
                    this.spawnAirParticles(recipe);
                    break;
                case UNKNOWN:
                default:
                    this.spawnArcaneParticles(recipe);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected void playSounds() {
        if (!this.__playedsound) {
            SpellRecipe recipe = this.getSpell();
            if (recipe != null) {
                Minecraft.getInstance().getSoundManager().play(new EntityAliveLoopingSound(this.getSoundEffect(recipe), this));
                this.__playedsound = true;
            }
        }
    }

    protected float getShapeAttributeByAge(Attribute attr) {
        SpellRecipe recipe = this.getSpell();
        if (recipe != null && recipe.getShape() != null) {
            float adjusted = recipe.getShape().getValue(attr);
            float base = recipe.getShape().getDefaultValue(attr);
            return adjusted < base ? adjusted : base + (adjusted - base) * Math.min((float) this.f_19797_ / 30.0F, 1.0F);
        } else {
            return 0.0F;
        }
    }

    @Nullable
    public LivingEntity getCaster() {
        if (this.__cachedCaster == null) {
            int id = this.f_19804_.get(CASTER_ID);
            Entity found = this.m_9236_().getEntity(id);
            if (found != null && found instanceof LivingEntity) {
                this.__cachedCaster = (LivingEntity) found;
            }
        }
        return this.__cachedCaster;
    }

    public void setCaster(LivingEntity caster) {
        if (caster != null) {
            this.f_19804_.set(CASTER_ID, caster.m_19879_());
        }
    }

    public float getShapeAttribute(Attribute attr) {
        SpellRecipe recipe = this.getSpell();
        return recipe != null && recipe.getShape() != null ? recipe.getShape().getValue(attr) : 0.0F;
    }

    protected int getMaxAge() {
        if (this.getSpell() != null && this.getSpell().getShape() != null && this.getSpell().getShape().getPart() != null && this.getCaster() != null) {
            float multiplier = 1.0F;
            if (this.getCaster() != null && EnchantmentHelper.getEnchantments(this.getCaster().getUseItem()).containsKey(Enchantments.CHANNELING)) {
                multiplier = 2.0F;
            }
            return (int) ((float) this.getSpell().getShape().getPart().maxChannelTime(this.getSpell().getShape()) * multiplier);
        } else {
            return 100;
        }
    }

    public SpellRecipe getSpell() {
        if (this.__cachedRecipe == null) {
            this.__cachedRecipe = SpellRecipe.fromNBT(this.f_19804_.get(SPELL_RECIPE));
        }
        return this.__cachedRecipe;
    }

    public void setSpell(ISpellDefinition spell) {
        CompoundTag nbt = new CompoundTag();
        spell.writeToNBT(nbt);
        this.f_19804_.set(SPELL_RECIPE, nbt);
    }

    protected boolean losCheck(Entity other) {
        Vec3 vector3d = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        Vec3 vector3d1 = new Vec3(other.getX(), other.getEyeY(), other.getZ());
        return this.m_9236_().m_45547_(new ClipContext(vector3d, vector3d1, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() == HitResult.Type.MISS;
    }

    protected boolean losCheck(BlockPos pos) {
        if (this.__cachedRecipe != null && this.__cachedRecipe.getComponents().stream().noneMatch(c -> ((SpellEffect) c.getPart()).getUseTag() == SpellPartTags.HARMFUL)) {
            return true;
        } else {
            Vec3 vector3d = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
            Vec3 vector3d1 = Vec3.atCenterOf(pos);
            HitResult result = this.m_9236_().m_45547_(new ClipContext(vector3d, vector3d1, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this));
            return result.getType() == HitResult.Type.BLOCK ? ((BlockHitResult) result).getBlockPos().equals(pos) : result.getType() == HitResult.Type.MISS;
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.put("spell", this.f_19804_.get(SPELL_RECIPE));
        int casterId = this.f_19804_.get(CASTER_ID);
        compound.putInt("caster_uuid", casterId);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("spell")) {
            this.f_19804_.set(SPELL_RECIPE, (CompoundTag) compound.get("spell"));
        }
        if (compound.contains("caster_uuid")) {
            this.f_19804_.set(CASTER_ID, compound.getInt("caster_uuid"));
        }
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(CASTER_ID, -1);
        this.f_19804_.define(SPELL_RECIPE, new CompoundTag());
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public final int getOverrideColor() {
        LivingEntity caster = this.getCaster();
        SpellRecipe recipe = this.getSpell();
        if (caster != null && recipe != null) {
            MutableInt color = new MutableInt(-1);
            if (caster instanceof Player) {
                caster.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> color.setValue(m.getParticleColorOverride()));
            }
            if (color.getValue() == -1) {
                color.setValue(recipe.getParticleColorOverride());
            }
            return color.getValue();
        } else {
            return -1;
        }
    }
}