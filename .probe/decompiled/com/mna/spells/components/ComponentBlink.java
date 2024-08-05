package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
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
import com.mna.api.timing.DelayedEventQueue;
import com.mna.api.timing.TimedDelayedEvent;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.items.ItemInit;
import com.mna.tools.TeleportHelper;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.curios.api.CuriosApi;

public class ComponentBlink extends SpellEffect {

    public ComponentBlink(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.RANGE, 5.0F, 5.0F, 16.0F, 1.0F, 2.0F));
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (target == SpellTarget.NONE) {
            return ComponentApplicationResult.FAIL;
        } else {
            Vec3 targetPosition = null;
            LivingEntity tgt = null;
            if (target.isLivingEntity()) {
                if (source.isPlayerCaster() && ItemInit.BLINK_PRECISION_RING.get().isEquippedAndHasMana(source.getCaster(), 1.0F, false) && source.getCaster() != target.getEntity()) {
                    float step = 1.5F;
                    Vec3 delta = target.getLivingEntity().m_20156_().normalize().scale((double) step);
                    tgt = source.getCaster();
                    do {
                        targetPosition = target.getLivingEntity().m_20182_().subtract(delta);
                        if (targetPosition.distanceTo(source.getCaster().m_20182_()) < (double) step) {
                            ((Player) source.getCaster()).m_213846_(Component.translatable("mna:components/blink.failed"));
                            return ComponentApplicationResult.FAIL;
                        }
                    } while (!TeleportHelper.coordsValidForBlink(context.getServerLevel(), (int) targetPosition.x(), (int) targetPosition.y(), (int) targetPosition.z()));
                    Vec3 targetEye = target.getLivingEntity().m_146892_();
                    Player looker = source.getPlayer();
                    DelayedEventQueue.pushEvent(context.getLevel(), new TimedDelayedEvent<>("look", 1, targetEye, (k, v) -> looker.m_7618_(EntityAnchorArgument.Anchor.EYES, targetEye)));
                    CuriosApi.getCuriosHelper().findFirstCurio(source.getCaster(), ItemInit.BLINK_PRECISION_RING.get()).ifPresent(t -> ItemInit.BLINK_PRECISION_RING.get().consumeMana(t.stack(), 1.0F, source.getPlayer()));
                } else {
                    tgt = target.getLivingEntity();
                    targetPosition = TeleportHelper.calculateBlinkPosition((double) modificationData.getValue(Attribute.RANGE), tgt, tgt.m_20156_(), context.getServerLevel());
                }
            } else {
                if (!target.isBlock()) {
                    return ComponentApplicationResult.FAIL;
                }
                if (source.hasCasterReference() && source.isPlayerCaster() && ItemInit.BLINK_PRECISION_RING.get().isEquippedAndHasMana(source.getCaster(), 1.0F, false)) {
                    tgt = source.getCaster();
                    BlockPos check = target.getBlock().above();
                    if (TeleportHelper.coordsValidForBlink(context.getServerLevel(), check.m_123341_(), check.m_123342_(), check.m_123343_())) {
                        targetPosition = target.getPosition().add(0.0, 1.0, 0.0);
                        if (targetPosition.distanceTo(tgt.m_20182_()) > (double) (modificationData.getValue(Attribute.RANGE) * 2.0F)) {
                            if (tgt instanceof Player) {
                                ((Player) tgt).m_213846_(Component.translatable("mna:components/blink.toofar"));
                            }
                            return ComponentApplicationResult.FAIL;
                        }
                    }
                    if (source.isPlayerCaster()) {
                        CuriosApi.getCuriosHelper().findFirstCurio(source.getCaster(), ItemInit.BLINK_PRECISION_RING.get()).ifPresent(t -> ItemInit.BLINK_PRECISION_RING.get().consumeMana(t.stack(), 1.0F, source.getPlayer()));
                    }
                }
            }
            if (tgt == source.getCaster() && source.isPlayerCaster()) {
                IPlayerMagic magic = (IPlayerMagic) source.getPlayer().getCapability(PlayerMagicProvider.MAGIC).orElse(null);
                if (magic == null || magic.getAirCasts() >= magic.getAirCastLimit(source.getPlayer(), context.getSpell())) {
                    return ComponentApplicationResult.FAIL;
                }
                if (!source.getPlayer().m_20096_()) {
                    magic.incrementAirCasts(source.getPlayer(), context.getSpell());
                }
            }
            if (context.getServerLevel().m_5776_() || tgt == null) {
                return ComponentApplicationResult.FAIL;
            } else if (targetPosition == null) {
                if (tgt instanceof Player) {
                    ((Player) tgt).m_213846_(Component.translatable("mna:components/blink.failed"));
                }
                return ComponentApplicationResult.FAIL;
            } else {
                TeleportHelper.teleportEntity(tgt, context.getServerLevel().m_46472_(), targetPosition);
                return ComponentApplicationResult.SUCCESS;
            }
        }
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.ENDER;
    }

    @Override
    public void SpawnParticles(Level world, Vec3 impact_position, Vec3 normal, int age, LivingEntity caster, ISpellDefinition recipe) {
        if (age == 0) {
            float particle_spread = 1.0F;
            float v = 1.0F;
            int particleCount = 25;
            for (int i = 0; i < particleCount; i++) {
                Vec3 velocity = new Vec3(0.0, Math.random() * (double) v, 0.0);
                world.addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.ENDER_VELOCITY.get()), caster), impact_position.x + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, impact_position.y + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, impact_position.z + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, velocity.x, velocity.y, velocity.z);
            }
        }
    }

    @Override
    public float initialComplexity() {
        return 10.0F;
    }

    @Override
    public int requiredXPForRote() {
        return 300;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.SELF;
    }
}