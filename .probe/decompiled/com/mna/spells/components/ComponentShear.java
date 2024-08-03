package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.tools.InventoryUtilities;
import com.mna.tools.ShearHelper;
import com.mojang.datafixers.util.Pair;
import java.util.Arrays;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

public class ComponentShear extends SpellEffect {

    public ComponentShear(ResourceLocation guiIcon) {
        super(guiIcon);
    }

    @Override
    public int requiredXPForRote() {
        return 100;
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (!source.isPlayerCaster()) {
            return ComponentApplicationResult.FAIL;
        } else {
            List<ItemStack> loot = null;
            if (target.isEntity()) {
                loot = ShearHelper.shearEntity(context.getServerLevel(), source.getPlayer(), target.getEntity(), source.getHand());
            } else if (target.isBlock()) {
                loot = ShearHelper.shearBlock(context.getServerLevel(), target.getBlock(), target.getBlockFace(this), source.getPlayer());
            }
            if (loot == null) {
                return ComponentApplicationResult.FAIL;
            } else {
                Pair<Boolean, Boolean> captureRedirect = InventoryUtilities.getCaptureAndRedirect(source.getPlayer());
                if ((Boolean) captureRedirect.getFirst()) {
                    InventoryUtilities.redirectCaptureOrDrop(source.getPlayer(), context.getServerLevel(), loot, (Boolean) captureRedirect.getSecond());
                } else {
                    RandomSource rand = context.getLevel().getRandom();
                    for (ItemStack d : loot) {
                        ItemEntity ent = new ItemEntity(context.getLevel(), target.getPosition().x + Math.random(), target.getPosition().y, target.getPosition().z + Math.random(), d);
                        ent.m_20256_(ent.m_20184_().add((double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F), (double) (rand.nextFloat() * 0.05F), (double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F)));
                        context.getLevel().m_7967_(ent);
                    }
                }
                return ComponentApplicationResult.SUCCESS;
            }
        }
    }

    @Override
    public SoundEvent SoundEffect() {
        return SoundEvents.BEEHIVE_SHEAR;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.EARTH;
    }

    @Override
    public float initialComplexity() {
        return 10.0F;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.UTILITY;
    }

    @Override
    public List<Affinity> getValidTinkerAffinities() {
        return Arrays.asList(Affinity.EARTH, Affinity.WATER, Affinity.WIND);
    }
}