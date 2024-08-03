package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
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
import com.mna.entities.EntityInit;
import com.mna.entities.sorcery.Rift;
import com.mna.gui.containers.providers.NamedRift;
import com.mna.tools.GuiTools;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class ComponentRift extends SpellEffect {

    public ComponentRift(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.PRECISION, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F));
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (context.countAffectedBlocks(this) + context.countAffectedEntities(this) > 0) {
            return ComponentApplicationResult.FAIL;
        } else {
            boolean enderChest = modificationData.getValue(Attribute.PRECISION) == 1.0F;
            if (target.isBlock()) {
                BlockPos p = target.getBlock().offset(target.getBlockFace(this).getNormal());
                for (int count = 0; count < 5 && !context.getServerLevel().m_46859_(p); count++) {
                    p = p.above();
                }
                this.spawnRift(context.getServerLevel(), p, enderChest);
                return ComponentApplicationResult.SUCCESS;
            } else if (!target.isLivingEntity()) {
                return ComponentApplicationResult.FAIL;
            } else {
                context.addAffectedEntity(this, target.getEntity());
                if (source.isPlayerCaster() && target.getLivingEntity() == source.getCaster()) {
                    if (source.getPlayer().containerMenu != source.getPlayer().inventoryMenu) {
                        return ComponentApplicationResult.FAIL;
                    }
                    if (enderChest) {
                        GuiTools.openEnderChest(source.getPlayer());
                    } else {
                        NetworkHooks.openScreen((ServerPlayer) source.getPlayer(), new NamedRift());
                    }
                } else {
                    BlockPos p = target.getLivingEntity().m_20183_().above();
                    this.spawnRift(context.getServerLevel(), p, enderChest);
                }
                return ComponentApplicationResult.SUCCESS;
            }
        }
    }

    private void spawnRift(Level world, BlockPos pos, boolean enderChest) {
        Rift rift = new Rift(EntityInit.RIFT.get(), world);
        rift.setEnderChest(enderChest);
        rift.m_6034_((double) ((float) pos.m_123341_() + 0.5F), (double) ((float) pos.m_123342_() + 0.5F), (double) ((float) pos.m_123343_() + 0.5F));
        world.m_7967_(rift);
    }

    @Override
    public SoundEvent SoundEffect() {
        return SFX.Spell.Impact.Single.ENDER;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.ENDER;
    }

    @Override
    public void SpawnParticles(Level world, Vec3 impact_position, Vec3 normal, int age, LivingEntity caster, ISpellDefinition recipe) {
        Vec3 rotationOffset = new Vec3(0.5, 0.0, 0.0);
        BlockPos bp = BlockPos.containing(impact_position);
        for (int angle = 0; angle < 360; angle += 30) {
            Vec3 point = rotationOffset.yRot((float) ((double) angle * Math.PI / 180.0));
            world.addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.ENDER.get()), caster), (double) ((float) bp.m_123341_() + 0.5F) + point.x, (double) bp.m_123342_(), (double) ((float) bp.m_123343_() + 0.5F) + point.z, (double) ((float) bp.m_123341_() + 0.5F), (double) bp.m_123342_(), (double) ((float) bp.m_123343_() + 0.5F));
        }
    }

    @Override
    public boolean canBeChanneled() {
        return false;
    }

    @Override
    public float initialComplexity() {
        return 30.0F;
    }

    @Override
    public int requiredXPForRote() {
        return 100;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.UTILITY;
    }
}