package com.mna.spells.components;

import com.mna.api.faction.IFaction;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.SpellReagent;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.spells.SpellCaster;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.mutable.MutableBoolean;

public abstract class PotionEffectComponent extends SpellEffect {

    protected final MobEffect effect;

    protected final RegistryObject<? extends MobEffect> registry_effect;

    protected final boolean modifiesMagnitude;

    protected final boolean modifiesDuration;

    private ArrayList<SpellReagent> permanenceReagents;

    private List<IFaction> permanentFor;

    public PotionEffectComponent(ResourceLocation guiIcon, MobEffect effect, AttributeValuePair... attributeValuePairs) {
        super(guiIcon, attributeValuePairs);
        this.effect = effect;
        this.registry_effect = null;
        List<AttributeValuePair> pairs = Arrays.asList(attributeValuePairs);
        this.modifiesDuration = pairs.stream().anyMatch(p -> p.getAttribute() == Attribute.DURATION);
        this.modifiesMagnitude = pairs.stream().anyMatch(p -> p.getAttribute() == Attribute.MAGNITUDE || p.getAttribute() == Attribute.LESSER_MAGNITUDE);
    }

    @Override
    public void addReagentTooltip(Player player, @Nullable InteractionHand hand, List<Component> tooltip, SpellReagent reagent) {
        List<SpellReagent> permanencyReagents = this.getPermanencyReagents(player, hand);
        if (permanencyReagents != null && permanencyReagents.contains(reagent)) {
            MutableComponent c = Component.literal(String.format("%d x ", reagent.getReagentStack().getCount())).append(reagent.getReagentStack().getHoverName()).append(Component.literal(" (")).append(Component.translatable("item.mna.spell.tooltip.permanent"));
            if (reagent.getOptional()) {
                c.append(Component.literal(", "));
                c.append(Component.translatable("item.mna.spell.tooltip.optional"));
            }
            c.append(Component.literal(")"));
            tooltip.add(c);
        } else {
            super.addReagentTooltip(player, hand, tooltip, reagent);
        }
    }

    public PotionEffectComponent(ResourceLocation guiIcon, RegistryObject<? extends MobEffect> effect, AttributeValuePair... attributeValuePairs) {
        super(guiIcon, attributeValuePairs);
        this.effect = null;
        this.registry_effect = effect;
        List<AttributeValuePair> pairs = Arrays.asList(attributeValuePairs);
        this.modifiesDuration = pairs.stream().anyMatch(p -> p.getAttribute() == Attribute.DURATION);
        this.modifiesMagnitude = pairs.stream().anyMatch(p -> p.getAttribute() == Attribute.MAGNITUDE || p.getAttribute() == Attribute.LESSER_MAGNITUDE);
    }

    protected boolean modifiesDuration() {
        return this.modifiesDuration;
    }

    protected boolean modifiesMagnitude() {
        return this.modifiesMagnitude;
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (target.isLivingEntity()) {
            boolean permanent = this.checkPermanencyReagents(source, target, context);
            int duration = permanent ? -1 : (this.modifiesDuration() ? (int) (modificationData.getValue(Attribute.DURATION) * 20.0F) : 1);
            if (duration < 0) {
                duration = -1;
            }
            int amp = this.modifiesMagnitude() ? this.getPotionAmplitude(source, target, modificationData, context) : 0;
            if (this.applicationPredicate(target.getLivingEntity())) {
                if (source.getCaster() == target.getEntity() && target.getLivingEntity().hasEffect(this.getEffect()) && target.getLivingEntity().getEffect(this.getEffect()).isInfiniteDuration()) {
                    target.getLivingEntity().removeEffect(this.getEffect());
                } else {
                    if (permanent) {
                        List<SpellReagent> permanentReagents = this.getPermanencyReagents(source.getPlayer(), source.getHand());
                        if (!SpellCaster.consumeReagents(source.getPlayer(), source.getHand(), permanentReagents)) {
                            return ComponentApplicationResult.FAIL;
                        }
                    }
                    target.getLivingEntity().addEffect(new MobEffectInstance(this.getEffect(), duration, amp, false, false));
                }
                if (this.getEffect().getCategory() == MobEffectCategory.HARMFUL && source.hasCasterReference()) {
                    target.getLivingEntity().setLastHurtByMob(source.getCaster());
                }
                return ComponentApplicationResult.SUCCESS;
            }
            if (source.isPlayerCaster()) {
                source.getPlayer().m_213846_(Component.translatable("mna:components/potion_effect_component.cannot_apply", Component.translatable(target.getLivingEntity().m_6095_().getDescriptionId()).getString()));
            }
        }
        return ComponentApplicationResult.FAIL;
    }

    protected boolean checkPermanencyReagents(SpellSource source, SpellTarget target, SpellContext context) {
        if (source.isPlayerCaster() && source.getCaster() == target.getEntity()) {
            if (this.getPermanentFor() != null) {
                Player player = source.getPlayer();
                MutableBoolean factionMatch = new MutableBoolean(false);
                player.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> {
                    if (p.getAlliedFaction() != null) {
                        ArrayList<IFaction> permanentForCopy = new ArrayList(this.getPermanentFor());
                        permanentForCopy.retainAll(p.getAlliedFaction().getAlliedFactions());
                        factionMatch.setValue(permanentForCopy.size() > 0);
                    }
                });
                if (factionMatch.booleanValue()) {
                    return true;
                }
            }
            List<SpellReagent> permanentReagents = this.getPermanencyReagents(source.getPlayer(), source.getHand());
            if (permanentReagents != null && permanentReagents.size() > 0) {
                for (SpellReagent reagent : permanentReagents) {
                    if (context.isReagentMissing(reagent.getReagentStack().getItem())) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    protected int getPotionAmplitude(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        return modificationData.getContainedAttributes().contains(Attribute.LESSER_MAGNITUDE) ? (int) modificationData.getValue(Attribute.LESSER_MAGNITUDE) - 1 : (int) modificationData.getValue(Attribute.MAGNITUDE) - 1;
    }

    private MobEffect getEffect() {
        return this.effect != null ? this.effect : this.registry_effect.get();
    }

    protected boolean applicationPredicate(LivingEntity target) {
        return true;
    }

    @Override
    public boolean targetsBlocks() {
        return false;
    }

    @Override
    public SpellPartTags getUseTag() {
        switch(this.getEffect().getCategory()) {
            case BENEFICIAL:
                return SpellPartTags.FRIENDLY;
            case HARMFUL:
                return SpellPartTags.HARMFUL;
            case NEUTRAL:
            default:
                return SpellPartTags.NEUTRAL;
        }
    }

    @Override
    public final List<SpellReagent> getRequiredReagents(Player caster, @Nullable InteractionHand hand) {
        ArrayList<SpellReagent> reagents = new ArrayList();
        List<SpellReagent> baselineReagents = super.getRequiredReagents(caster, hand);
        if (baselineReagents != null) {
            reagents.addAll(baselineReagents);
        }
        List<SpellReagent> permanencyReagents = this.getPermanencyReagents(caster, hand);
        if (permanencyReagents != null) {
            reagents.addAll(permanencyReagents);
        }
        return reagents;
    }

    protected List<SpellReagent> getPermanencyReagents(Player caster, @Nullable InteractionHand hand) {
        return this.permanenceReagents == null ? null : this.permanenceReagents.stream().filter(r -> !r.isIgnoredBy(caster)).toList();
    }

    protected final void addPermanencyReagent(ItemStack reagentStack, boolean matchNBT, boolean matchDurability, boolean consume, IFaction... ignoredBy) {
        if (this.permanenceReagents == null) {
            this.permanenceReagents = new ArrayList();
        }
        this.permanenceReagents.add(new SpellReagent(this, reagentStack, matchNBT, matchDurability, consume, true, ignoredBy));
    }

    protected final void addPermanencyReagent(ItemStack reagentStack, IFaction... ignoredBy) {
        this.addPermanencyReagent(reagentStack, false, false, true, ignoredBy);
    }

    protected final void makePermanentFor(IFaction... factions) {
        this.permanentFor = Arrays.asList(factions);
    }

    @Nullable
    private List<IFaction> getPermanentFor() {
        return this.permanentFor;
    }
}