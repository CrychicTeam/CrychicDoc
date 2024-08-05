package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.sound.SFX;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.SpellReagent;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.api.tools.MATags;
import com.mna.config.GeneralConfig;
import com.mna.entities.utility.ImpulseProjectile;
import com.mna.items.ItemInit;
import java.util.Arrays;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ComponentImpulse extends SpellEffect {

    public static final String NBT_CANNONSHOT = "cannon_shot";

    public final List<SpellReagent> requiredReagents = Arrays.asList(new SpellReagent(this, new ItemStack(Items.GUNPOWDER), false, false, true, true));

    public ComponentImpulse(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.DAMAGE, 3.0F, 1.0F, 20.0F, 0.5F, 1.5F), new AttributeValuePair(Attribute.LESSER_MAGNITUDE, 0.0F, 0.0F, 25.0F, 5.0F, 10.0F));
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (!target.isBlock() && context.countAffectedEntities(this) <= 0) {
            ItemStack offhandItem = source.getCaster().getItemInHand(source.getHand() == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
            ItemStack handItem = source.getCaster().getItemInHand(source.getHand());
            if (offhandItem.isEmpty()) {
                return ComponentApplicationResult.FAIL;
            } else {
                float velocityScale = 1.5F;
                float damage = modificationData.getValue(Attribute.DAMAGE) * GeneralConfig.getDamageMultiplier();
                float recoveryChance = modificationData.getValue(Attribute.LESSER_MAGNITUDE) / 100.0F;
                if (!context.isReagentMissing(Items.GUNPOWDER) || handItem.getItem() == ItemInit.HELLFIRE_STAFF.get()) {
                    damage *= 2.0F;
                    velocityScale = 3.0F;
                    if (!context.getLevel().isClientSide()) {
                        context.getLevel().playSound(null, source.getOrigin().x, source.getOrigin().y, source.getOrigin().z, SFX.Spell.Cast.KABLAM, source.isPlayerCaster() ? SoundSource.PLAYERS : SoundSource.HOSTILE, 1.0F, 0.9F + (float) (Math.random() * 0.2F));
                    }
                }
                Vec3 shootVector = null;
                if (target.getEntity() == source.getCaster()) {
                    shootVector = source.getCaster().m_20156_().normalize();
                } else {
                    shootVector = target.getEntity().getEyePosition().subtract(source.getCaster().m_146892_());
                }
                shootVector.scale((double) velocityScale);
                if (offhandItem.getItem() instanceof ArrowItem) {
                    this.shootArrow(offhandItem, damage, shootVector, context, source);
                } else {
                    this.spawnCannonShot(offhandItem, damage, recoveryChance, shootVector, context, source);
                }
                offhandItem.shrink(1);
                context.addAffectedEntity(this, source.getCaster());
                return ComponentApplicationResult.SUCCESS;
            }
        } else {
            return ComponentApplicationResult.FAIL;
        }
    }

    private void spawnCannonShot(ItemStack offhandItem, float damage, float recoveryChance, Vec3 shootVector, SpellContext context, SpellSource source) {
        if (MATags.isItemEqual(offhandItem, MATags.Items.CANNON_HALFBOOST)) {
            damage = (float) ((double) damage * 1.5);
            recoveryChance *= 2.0F;
        } else if (MATags.isItemEqual(offhandItem, MATags.Items.CANNON_FULLBOOST)) {
            damage *= 2.0F;
            recoveryChance *= 4.0F;
        }
        ImpulseProjectile ecs = new ImpulseProjectile(context.getServerLevel(), source.getCaster(), offhandItem.copy(), damage);
        ecs.setChanceForRecovery(recoveryChance);
        ecs.setDamage(damage);
        ecs.m_6686_(shootVector.x, shootVector.y, shootVector.z, 1.0F, 0.0F);
        context.getServerLevel().addFreshEntity(ecs);
    }

    private void shootArrow(ItemStack offhandItem, float damage, Vec3 shootVector, SpellContext context, SpellSource source) {
        AbstractArrow worroa = ProjectileUtil.getMobArrow(source.getCaster(), offhandItem, 1.0F);
        worroa.shoot(shootVector.x, shootVector.y, shootVector.z, damage / 10.0F, 0.0F);
        worroa.setCritArrow(true);
        worroa.getPersistentData().putBoolean("return_on_next_arrow_shot", true);
        ItemStack returnItem = offhandItem.copy();
        returnItem.setCount(1);
        worroa.getPersistentData().put("return_stack", returnItem.save(new CompoundTag()));
        worroa.getPersistentData().putBoolean("cannon_shot", true);
        context.getLevel().m_7967_(worroa);
    }

    @Override
    public SoundEvent SoundEffect() {
        return SFX.Spell.Impact.AoE.EARTH;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.FIRE;
    }

    @Override
    public List<SpellReagent> getRequiredReagents(Player caster, InteractionHand hand) {
        return hand != null && caster.m_21120_(hand).getItem() == ItemInit.HELLFIRE_STAFF.get() ? Arrays.asList() : this.requiredReagents;
    }

    @Override
    public void SpawnParticles(Level world, Vec3 impact_position, Vec3 normal, int age, LivingEntity caster, ISpellDefinition recipe) {
    }

    @Override
    public boolean canBeChanneled() {
        return false;
    }

    @Override
    public float initialComplexity() {
        return 15.0F;
    }

    @Override
    public boolean targetsEntities() {
        return true;
    }

    @Override
    public boolean targetsBlocks() {
        return false;
    }

    @Override
    public int requiredXPForRote() {
        return 100;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.SELF;
    }

    @Override
    public boolean isHellfireBoosted(Attribute attr) {
        return true;
    }

    @Override
    public List<Affinity> getValidTinkerAffinities() {
        return Arrays.asList(Affinity.ARCANE, Affinity.ENDER, Affinity.WIND, Affinity.ICE);
    }
}