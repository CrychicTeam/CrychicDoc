package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.faction.IFaction;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.factions.Factions;
import java.util.Arrays;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayerFactory;

public class ComponentBloom extends SpellEffect {

    public ComponentBloom(ResourceLocation guiIcon) {
        super(guiIcon);
        this.addReagent(new ItemStack(Items.BONE_MEAL), false, true, true, new IFaction[] { Factions.FEY });
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (target.isBlock()) {
            ItemStack bmStack = new ItemStack(Items.BONE_MEAL);
            BoneMealItem.applyBonemeal(bmStack, context.getServerLevel(), target.getBlock(), (Player) (source.isPlayerCaster() ? source.getPlayer() : FakePlayerFactory.getMinecraft(context.getServerLevel())));
            BoneMealItem.growWaterPlant(bmStack, context.getServerLevel(), target.getBlock(), target.getBlockFace(this));
            return ComponentApplicationResult.SUCCESS;
        } else {
            return ComponentApplicationResult.FAIL;
        }
    }

    @Override
    public SoundEvent SoundEffect() {
        return SFX.Spell.Impact.Single.WATER;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.WATER;
    }

    @Override
    public void SpawnParticles(Level world, Vec3 impact_position, Vec3 normal, int age, LivingEntity caster, ISpellDefinition recipe) {
        if (age <= 1) {
            BlockPos imp = BlockPos.containing(impact_position);
            for (int i = 0; i < 10; i++) {
                world.addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.EARTH.get()), caster), (double) imp.m_123341_() + 0.5, (double) imp.m_123342_() + 0.5, (double) imp.m_123343_() + 0.5, 0.15686275F, 0.3882353F, 0.21960784F);
            }
        }
    }

    @Override
    public boolean canBeChanneled() {
        return true;
    }

    @Override
    public boolean targetsEntities() {
        return false;
    }

    @Override
    public float initialComplexity() {
        return 50.0F;
    }

    @Override
    public int requiredXPForRote() {
        return 325;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.UTILITY;
    }

    @Override
    public List<Affinity> getValidTinkerAffinities() {
        return Arrays.asList(Affinity.WATER, Affinity.EARTH);
    }
}