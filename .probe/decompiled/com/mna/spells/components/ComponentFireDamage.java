package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.entities.DamageHelper;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IDamageComponent;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.blocks.BlockInit;
import com.mna.config.GeneralConfig;
import com.mna.interop.CuriosInterop;
import com.mna.items.ItemInit;
import com.mna.tools.BlockUtils;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import top.theillusivec4.curios.api.SlotTypePreset;

public class ComponentFireDamage extends SpellEffect implements IDamageComponent {

    public ComponentFireDamage(ResourceLocation icon) {
        super(icon, new AttributeValuePair(Attribute.DAMAGE, 5.0F, 1.0F, 20.0F, 0.5F, 3.0F), new AttributeValuePair(Attribute.DURATION, 3.0F, 0.0F, 10.0F, 1.0F, 2.0F), new AttributeValuePair(Attribute.PRECISION, 0.0F, 0.0F, 1.0F, 1.0F, 5.0F));
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (target.isEntity()) {
            if (target.getEntity() instanceof LivingEntity) {
                target.getEntity().setSecondsOnFire((int) modificationData.getValue(Attribute.DURATION));
                float damage = modificationData.getValue(Attribute.DAMAGE) * GeneralConfig.getDamageMultiplier();
                if (target.getEntity().isOnFire()) {
                    damage *= 1.25F;
                }
                target.getEntity().hurt(DamageHelper.createSourcedType(DamageTypes.FIREBALL, context.getLevel().registryAccess(), source.getCaster()), damage);
                return ComponentApplicationResult.SUCCESS;
            }
        } else if (target.isBlock() && !context.getServerLevel().m_46859_(target.getBlock())) {
            BlockEntity be = context.getServerLevel().m_7702_(target.getBlock());
            if (be != null && be instanceof AbstractFurnaceBlockEntity) {
                int burnTime = 105 * (int) modificationData.getValue(Attribute.DAMAGE);
                burnTime = (int) ((float) burnTime * Math.max(modificationData.getValue(Attribute.DURATION), 1.0F));
                if (source.isPlayerCaster() && CuriosInterop.IsItemInCurioSlot(ItemInit.EMBERGLOW_BRACELET.get(), source.getCaster(), SlotTypePreset.BRACELET)) {
                    burnTime *= 10;
                }
                AbstractFurnaceBlockEntity furnace = (AbstractFurnaceBlockEntity) be;
                Field f = ObfuscationReflectionHelper.findField(AbstractFurnaceBlockEntity.class, "litTime");
                Field f2 = ObfuscationReflectionHelper.findField(AbstractFurnaceBlockEntity.class, "litDuration");
                try {
                    if (f != null && f2 != null && f.getInt(furnace) < burnTime) {
                        f.set(furnace, burnTime);
                        f2.set(furnace, burnTime);
                        BlockState state = context.getServerLevel().m_8055_(target.getBlock());
                        if (state.m_61138_(AbstractFurnaceBlock.LIT)) {
                            context.getServerLevel().m_7731_(target.getBlock(), (BlockState) state.m_61124_(AbstractFurnaceBlock.LIT, true), 3);
                        }
                    }
                } catch (Throwable var11) {
                }
            } else if (modificationData.getValue(Attribute.PRECISION) == 0.0F && context.getServerLevel().m_46859_(target.getBlock().above())) {
                if (context.getSpell().getAffinity().containsKey(Affinity.HELLFIRE)) {
                    BlockUtils.placeBlock(context.getServerLevel(), target.getBlock().above(), target.getBlockFace(this), BlockInit.HELLFIRE.get().defaultBlockState(), source.getPlayer());
                } else {
                    BlockState fireState = BaseFireBlock.getState(context.getServerLevel(), target.getBlock().above());
                    BlockUtils.placeBlock(context.getServerLevel(), target.getBlock().above(), target.getBlockFace(this), fireState, source.getPlayer());
                }
                return ComponentApplicationResult.SUCCESS;
            }
        }
        return ComponentApplicationResult.FAIL;
    }

    @Override
    public void SpawnParticles(Level world, Vec3 impact_position, Vec3 normal, int age, LivingEntity caster, ISpellDefinition recipe) {
        Random rndm = new Random(1234L);
        if (age < 5) {
            for (int i = 0; i < 25; i++) {
                if (recipe.getAffinity().containsKey(Affinity.HELLFIRE)) {
                    world.addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.HELLFIRE.get()), caster), impact_position.x + (double) rndm.nextFloat() - 0.5, impact_position.y + (double) rndm.nextFloat() - 0.5, impact_position.z + (double) rndm.nextFloat() - 0.5, 0.0, 0.0, 0.0);
                } else {
                    world.addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.FLAME.get()), caster), impact_position.x + (double) rndm.nextFloat() - 0.5, impact_position.y + (double) rndm.nextFloat() - 0.5, impact_position.z + (double) rndm.nextFloat() - 0.5, 0.0, 0.0, 0.0);
                }
            }
        }
    }

    @Override
    public SoundEvent SoundEffect() {
        return SFX.Spell.Impact.Single.FIRE;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.FIRE;
    }

    @Override
    public float initialComplexity() {
        return 10.0F;
    }

    @Override
    public int requiredXPForRote() {
        return 500;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.HARMFUL;
    }

    @Override
    public List<Affinity> getValidTinkerAffinities() {
        return Arrays.asList(Affinity.FIRE);
    }
}