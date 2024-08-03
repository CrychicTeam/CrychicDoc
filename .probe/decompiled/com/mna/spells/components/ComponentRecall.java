package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.faction.IFaction;
import com.mna.api.items.IPositionalItem;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.factions.Factions;
import com.mna.items.ItemInit;
import com.mna.tools.TeleportHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class ComponentRecall extends SpellEffect {

    public ComponentRecall(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.RANGE, 1.0F, 1.0F, 5.0F, 1.0F, 25.0F), new AttributeValuePair(Attribute.MAGNITUDE, 1.0F, 1.0F, 3.0F, 1.0F, 10.0F));
    }

    @Override
    public int requiredXPForRote() {
        return 200;
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (source.hasCasterReference() && target.isEntity() && target.getEntity().isAlive() && target.getEntity().canChangeDimensions()) {
            ItemStack markingRune = source.getCaster().getMainHandItem().getItem() != ItemInit.RUNE_MARKING.get() && source.getCaster().getMainHandItem().getItem() != ItemInit.BOOK_MARKS.get() ? source.getCaster().getOffhandItem() : source.getCaster().getMainHandItem();
            if (markingRune.getItem() instanceof IPositionalItem) {
                BlockPos pos = ((IPositionalItem) markingRune.getItem()).getLocation(markingRune);
                if (pos != null) {
                    double dist = target.getEntity().blockPosition().m_123331_(pos);
                    double maxDist = (double) (modificationData.getValue(Attribute.RANGE) * 500.0F);
                    if (!(dist > maxDist * maxDist)) {
                        int magnitude = (int) modificationData.getValue(Attribute.MAGNITUDE);
                        if (this.casterTeamCheck(source, target) && this.magnitudeHealthCheck(source, target, magnitude, 20)) {
                            TeleportHelper.teleportEntity(target.getEntity(), context.getServerLevel().m_46472_(), new Vec3((double) pos.m_123341_() + 0.5, (double) (pos.m_123342_() + 1), (double) pos.m_123343_() + 0.5));
                            return ComponentApplicationResult.SUCCESS;
                        }
                        source.getPlayer().m_213846_(Component.translatable("mna:generic.too_powerful"));
                        return ComponentApplicationResult.FAIL;
                    }
                    source.getPlayer().m_213846_(Component.translatable("mna:components/recall.too_far"));
                }
            } else if (source.isPlayerCaster()) {
                source.getPlayer().m_213846_(Component.translatable("mna:components/recall.no_marker"));
            }
        }
        return ComponentApplicationResult.FAIL;
    }

    @Override
    public boolean targetsBlocks() {
        return false;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.ARCANE;
    }

    @Override
    public IFaction getFactionRequirement() {
        return Factions.COUNCIL;
    }

    @Override
    public float initialComplexity() {
        return 50.0F;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.UTILITY;
    }
}