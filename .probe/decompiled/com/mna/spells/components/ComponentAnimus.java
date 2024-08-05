package com.mna.spells.components;

import com.mna.advancements.CustomAdvancementTriggers;
import com.mna.api.affinity.Affinity;
import com.mna.api.faction.IFaction;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.collections.Components;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.blocks.BlockInit;
import com.mna.blocks.decoration.ClayMugBlock;
import com.mna.entities.summon.AnimusBlock;
import com.mna.entities.summon.GreaterAnimus;
import com.mna.factions.Factions;
import com.mna.tools.BlockUtils;
import com.mna.tools.SummonUtils;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;

public class ComponentAnimus extends SpellEffect {

    private final boolean greater;

    public ComponentAnimus(ResourceLocation guiIcon, boolean greater) {
        super(guiIcon, greater ? new AttributeValuePair[] { new AttributeValuePair(Attribute.DURATION, 10.0F, 5.0F, 60.0F, 5.0F, 2.0F), new AttributeValuePair(Attribute.LESSER_MAGNITUDE, 0.0F, 0.0F, 500.0F, 50.0F, 0.0F) } : new AttributeValuePair[] { new AttributeValuePair(Attribute.DURATION, 10.0F, 5.0F, 60.0F, 5.0F, 2.0F) });
        this.greater = greater;
    }

    @Override
    public int requiredXPForRote() {
        return 200;
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (context.countAffectedBlocks(this) > 0) {
            return ComponentApplicationResult.FAIL;
        } else {
            if (target.isBlock()) {
                BlockState state = context.getServerLevel().m_8055_(target.getBlock());
                if (!context.getServerLevel().m_46859_(target.getBlock()) && context.getServerLevel().m_6425_(target.getBlock()).isEmpty() && !(state.m_60734_() instanceof EntityBlock) && BlockUtils.canDestroyBlock(source.getPlayer(), context.getServerLevel(), target.getBlock(), Tiers.IRON)) {
                    if (state.m_60734_() == BlockInit.CLAY_MUG.get() && (Boolean) state.m_61143_(ClayMugBlock.HAS_LIQUID)) {
                        CustomAdvancementTriggers.BE_OUR_GUEST.trigger((ServerPlayer) source.getPlayer());
                    }
                    AnimusBlock animatedBlockEntity = new AnimusBlock(context.getServerLevel(), state, target.getBlock(), (int) modificationData.getValue(Attribute.DURATION) * 20);
                    context.getServerLevel().addFreshEntity(animatedBlockEntity);
                    target.overrideSpellTarget(animatedBlockEntity);
                    return ComponentApplicationResult.SUCCESS;
                }
            } else if (target.isEntity() && target.getEntity() == source.getCaster()) {
                ItemStack offhand = source.getCaster().getItemInHand(source.getHand() == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
                if (!offhand.isEmpty()) {
                    ItemStack summonStack = offhand.copy();
                    summonStack.setCount(1);
                    if (summonStack.getItem() instanceof BlockItem) {
                        BlockState state = ((BlockItem) summonStack.getItem()).getBlock().defaultBlockState();
                        if (source.isPlayerCaster() && state.m_60734_() == BlockInit.CLAY_MUG.get() && (Boolean) state.m_61143_(ClayMugBlock.HAS_LIQUID)) {
                            CustomAdvancementTriggers.BE_OUR_GUEST.trigger((ServerPlayer) source.getPlayer());
                        }
                        AnimusBlock animatedBlockEntity = new AnimusBlock(context.getServerLevel(), state, BlockPos.containing(source.getOrigin()), (int) (modificationData.getValue(Attribute.DURATION) * 20.0F));
                        animatedBlockEntity.doNotClearOrigin();
                        context.getServerLevel().addFreshEntity(animatedBlockEntity);
                        target.overrideSpellTarget(animatedBlockEntity);
                        offhand.shrink(1);
                        return ComponentApplicationResult.SUCCESS;
                    }
                    if (this.greater) {
                        GreaterAnimus eag = new GreaterAnimus(context.getLevel(), summonStack, modificationData.getValue(Attribute.LESSER_MAGNITUDE));
                        eag.m_146884_(source.getOrigin());
                        SummonUtils.addTrackedEntity(source.getCaster(), eag);
                        SummonUtils.setSummon(eag, source.getCaster(), true, (int) modificationData.getValue(Attribute.DURATION) * 20);
                        context.getLevel().m_7967_(eag);
                        target.overrideSpellTarget(eag);
                        offhand.shrink(1);
                        return ComponentApplicationResult.SUCCESS;
                    }
                }
            }
            return ComponentApplicationResult.FAIL;
        }
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.WATER;
    }

    @Override
    public float initialComplexity() {
        return this.greater ? 60.0F : 10.0F;
    }

    @Override
    public boolean targetsEntities() {
        return true;
    }

    @Override
    public boolean canBeChanneled() {
        return false;
    }

    @Override
    public IFaction getFactionRequirement() {
        return Factions.COUNCIL;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.UTILITY;
    }

    @Override
    public boolean isSilverSpell() {
        return this.greater;
    }

    public static final void adjustSpell(ISpellDefinition recipe, @Nullable LivingEntity caster) {
        if (recipe.isValid()) {
            if (!recipe.getShape().getPart().affectsCaster()) {
                recipe.getComponents().stream().filter(c -> c.getPart() == Components.ANIMUS).findFirst().ifPresent(c -> recipe.setManaCost(recipe.getManaCost() + c.getValue(Attribute.LESSER_MAGNITUDE)));
            }
        }
    }
}