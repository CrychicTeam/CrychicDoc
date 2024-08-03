package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.effects.EffectInit;
import com.mna.entities.boss.BossMonster;
import com.mna.interop.CuriosInterop;
import com.mna.items.ItemInit;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import top.theillusivec4.curios.api.SlotTypePreset;

public class ComponentFling extends SpellEffect {

    public static final String FLING_STRENGTH = "mna:flung";

    public static final String FLING_TIME = "mna:fling_time";

    public ComponentFling(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.SPEED, 1.0F, 1.0F, 3.0F, 1.0F, 5.0F));
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        float speed = modificationData.getValue(Attribute.SPEED);
        if (!target.isEntity()) {
            return ComponentApplicationResult.FAIL;
        } else {
            Entity eTarget = target.getEntity();
            if (target.getEntity() == source.getCaster()) {
                Entity vehicle = source.getCaster().m_20202_();
                if (vehicle != null) {
                    eTarget = vehicle;
                }
            }
            if (eTarget instanceof LivingEntity le) {
                float max_velocity = 2.0F;
                if (CuriosInterop.IsItemInCurioSlot(ItemInit.AIR_CAST_RING.get(), le, SlotTypePreset.RING)) {
                    max_velocity++;
                    speed++;
                }
                MobEffectInstance effect = le.getEffect(EffectInit.ENLARGE.get());
                if (effect != null) {
                    speed = (float) ((double) speed * (1.0 - 0.1 * (double) (effect.getAmplifier() + 1)));
                    max_velocity *= 0.5F;
                }
                effect = le.getEffect(EffectInit.REDUCE.get());
                if (effect != null) {
                    speed = (float) ((double) speed * (1.0 + 0.2 * (double) (effect.getAmplifier() + 1)));
                    max_velocity = (float) ((double) max_velocity * (1.0 + 0.2 * (double) (effect.getAmplifier() + 1)));
                }
                if (source.hasCasterReference() && le == source.getCaster()) {
                    if (source.isPlayerCaster()) {
                        IPlayerMagic magic = (IPlayerMagic) source.getPlayer().getCapability(PlayerMagicProvider.MAGIC).orElse(null);
                        if (magic == null || magic.getAirCasts() >= magic.getAirCastLimit(source.getPlayer(), context.getSpell())) {
                            return ComponentApplicationResult.FAIL;
                        }
                        if (!source.getPlayer().m_20096_()) {
                            magic.incrementAirCasts(source.getPlayer(), context.getSpell());
                        }
                        if (source.getPlayer().m_21120_(source.getHand()).getItem() == ItemInit.ICARIAN_FLIGHT.get()) {
                            max_velocity = 1000.0F;
                        }
                    }
                    Vec3 motion = source.getCaster().m_20154_().normalize();
                    motion = motion.scale((double) speed);
                    source.getCaster().m_5997_(motion.x, motion.y, motion.z);
                    motion = source.getCaster().m_20184_();
                    if (motion.length() > (double) max_velocity) {
                        double scale = (double) max_velocity / motion.length();
                        source.getCaster().m_20256_(motion.scale(scale));
                    }
                    source.getCaster().f_19864_ = true;
                    if (source.isPlayerCaster()) {
                        ((ServerPlayer) source.getPlayer()).connection.send(new ClientboundSetEntityMotionPacket(le));
                    }
                    setFlags(le, speed);
                    return ComponentApplicationResult.SUCCESS;
                } else if (le instanceof Player targetPlayer) {
                    Vec3 motionx = source.hasCasterReference() ? source.getCaster().m_20154_() : targetPlayer.m_20154_();
                    flingTarget(targetPlayer, motionx, speed, max_velocity, source.getCaster() instanceof BossMonster ? 0.5F : 1.0F);
                    return ComponentApplicationResult.SUCCESS;
                } else {
                    float mX = (float) (source.getOrigin().x() - target.getLivingEntity().m_20185_());
                    float mZ = (float) (source.getOrigin().z() - target.getLivingEntity().m_20189_());
                    if (source.getCaster().m_20202_() == le) {
                        mX = (float) (-source.getCaster().m_20156_().x);
                        mZ = (float) (-source.getCaster().m_20156_().z);
                    }
                    flingTarget(le, new Vec3((double) mX, (double) speed, (double) mZ), speed, max_velocity);
                    return ComponentApplicationResult.SUCCESS;
                }
            } else {
                float mX = (float) (source.getOrigin().x() - eTarget.getX());
                float mZ = (float) (source.getOrigin().z() - eTarget.getZ());
                if (source.getCaster().m_20202_() == eTarget) {
                    mX = (float) (-source.getCaster().m_20156_().x);
                    mZ = (float) (-source.getCaster().m_20156_().z);
                }
                eTarget.hasImpulse = true;
                Vec3 vector3d = eTarget.getDeltaMovement();
                Vec3 vector3d1 = new Vec3((double) mX, 0.0, (double) mZ).normalize().scale((double) speed);
                eTarget.setDeltaMovement(vector3d.x / 2.0 - vector3d1.x, eTarget.onGround() ? Math.min(0.4, vector3d.y / 2.0 + (double) speed) : vector3d.y, vector3d.z / 2.0 - vector3d1.z);
                return ComponentApplicationResult.SUCCESS;
            }
        }
    }

    public static void flingTarget(LivingEntity target, Vec3 direction, float strength, float kbResistFactor) {
        flingTarget(target, direction, strength, 2.0F, kbResistFactor);
    }

    public static void flingTarget(LivingEntity target, Vec3 direction, float strength, float max_velocity, float kbResistFactor) {
        if (target != null) {
            MobEffectInstance effect = target.getEffect(EffectInit.ENLARGE.get());
            if (effect != null) {
                strength = (float) ((double) strength * (1.0 - 0.1 * (double) (effect.getAmplifier() + 1)));
                max_velocity *= 0.5F;
            }
            effect = target.getEffect(EffectInit.REDUCE.get());
            if (effect != null) {
                strength = (float) ((double) strength * (1.0 + 0.2 * (double) (effect.getAmplifier() + 1)));
                max_velocity = (float) ((double) max_velocity * (1.0 + 0.2 * (double) (effect.getAmplifier() + 1)));
            }
            if (target instanceof Player targetPlayer) {
                Vec3 motion = direction.normalize().scale((double) strength);
                targetPlayer.m_5997_(motion.x, motion.y, motion.z);
                motion = targetPlayer.m_20184_();
                if (motion.length() > (double) max_velocity) {
                    double scale = (double) max_velocity / motion.length();
                    targetPlayer.m_20256_(motion.scale(scale));
                }
                targetPlayer.f_19864_ = true;
                ((ServerPlayer) targetPlayer).connection.send(new ClientboundSetEntityMotionPacket(targetPlayer));
                if (!CuriosInterop.IsItemInCurioSlot(ItemInit.AIR_CAST_RING.get(), targetPlayer, SlotTypePreset.RING)) {
                    setFlags(targetPlayer, strength);
                }
            } else {
                LivingKnockBackEvent event = ForgeHooks.onLivingKnockBack(target, strength, direction.x, direction.z);
                if (!event.isCanceled()) {
                    double kbRes = target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
                    strength = event.getStrength();
                    direction = new Vec3(event.getRatioX(), 0.0, event.getRatioZ());
                    strength = (float) ((double) strength * (1.0 - kbRes * (double) kbResistFactor));
                    if (!((double) strength <= 0.0)) {
                        target.f_19812_ = true;
                        Vec3 vec3 = target.m_20184_();
                        Vec3 vec31 = new Vec3(direction.x, 0.0, direction.z).normalize().scale((double) strength);
                        target.m_20334_(vec3.x / 2.0 - vec31.x, target.m_20096_() ? Math.min(0.4, vec3.y / 2.0 + (double) strength) : vec3.y, vec3.z / 2.0 - vec31.z);
                    }
                }
                target.m_5997_(0.0, (double) (0.2F * strength), 0.0);
                if (target instanceof PathfinderMob) {
                    ((PathfinderMob) target).m_21573_().stop();
                }
                setFlags(target, strength);
            }
        }
    }

    private static void setFlags(LivingEntity le, float strength) {
        le.getPersistentData().putFloat("mna:flung", strength);
        le.getPersistentData().putLong("mna:fling_time", le.m_9236_().getGameTime());
        le.f_19812_ = true;
    }

    @Override
    public SoundEvent SoundEffect() {
        return SFX.Spell.Impact.Single.WIND;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.WIND;
    }

    @Override
    public void SpawnParticles(Level world, Vec3 impact_position, Vec3 normal, int age, LivingEntity caster, ISpellDefinition recipe) {
        if (age <= 10) {
            float particle_spread = 1.0F;
            int particleCount = 10;
            Vec3 velocity = caster != null ? caster.m_20154_() : new Vec3(0.0, 0.1, 0.0);
            for (int i = 0; i < particleCount; i++) {
                world.addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.AIR_VELOCITY.get()).setScale(0.2F).setColor(10, 10, 10), caster), impact_position.x + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, impact_position.y + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, impact_position.z + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, velocity.x, velocity.y, velocity.z);
            }
        }
    }

    @Override
    public float initialComplexity() {
        return 10.0F;
    }

    @Override
    public boolean targetsBlocks() {
        return false;
    }

    @Override
    public int requiredXPForRote() {
        return 200;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.HARMFUL;
    }
}