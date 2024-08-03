package com.mna.spells;

import com.google.common.collect.UnmodifiableIterator;
import com.mna.api.affinity.Affinity;
import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.capabilities.resource.CastingResourceIDs;
import com.mna.api.spells.adjusters.SpellAdjustingContext;
import com.mna.api.spells.adjusters.SpellCastStage;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.collections.Components;
import com.mna.api.spells.collections.Shapes;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.capabilities.worlddata.WorldMagicProvider;
import com.mna.effects.EffectInit;
import com.mna.enchantments.base.ModifierEnchantment;
import com.mna.factions.Factions;
import com.mna.interop.CuriosInterop;
import com.mna.items.ItemInit;
import com.mna.items.base.IHellfireItem;
import com.mna.items.sorcery.ItemSpell;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.util.LazyOptional;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypePreset;

public final class SpellAdjusters {

    private static final float AFFINITY_MANA_COST_FACTOR = 0.5F;

    public static final void modifyBasedOnAffinity(ISpellDefinition recipe, @Nullable LivingEntity caster) {
        if (caster != null) {
            LazyOptional<IPlayerMagic> magicContainer = caster.getCapability(PlayerMagicProvider.MAGIC);
            if (magicContainer.isPresent()) {
                IPlayerMagic magic = magicContainer.orElse(null);
                if (magic != null) {
                    float base_mana_cost = recipe.getManaCost();
                    float modification_reduce = 0.0F;
                    float modification_increase = 0.0F;
                    HashMap<Affinity, Float> affList = recipe.getAffinity();
                    for (Affinity affinity : affList.keySet()) {
                        Affinity mainAdjuster = affinity.getShiftAffinity();
                        Affinity oppositeAdjuster = affinity.getOpposite().getShiftAffinity();
                        modification_reduce += magic.getAffinityDepth(mainAdjuster) * 0.5F / 100.0F * affList.get(affinity);
                        modification_increase += magic.getAffinityDepth(oppositeAdjuster) * 0.5F / 100.0F * affList.get(affinity);
                    }
                    recipe.setManaCost(base_mana_cost - base_mana_cost * modification_reduce + base_mana_cost * modification_increase);
                }
            }
        }
    }

    public static final void modifyChanneled(ISpellDefinition recipe, @Nullable LivingEntity caster) {
        if (recipe.isChanneled()) {
            float manaCostPerTick = recipe.getManaCost() / 20.0F;
            recipe.setManaCost(manaCostPerTick * 0.5F);
        }
    }

    public static final void modifySigils(ISpellDefinition recipe, @Nullable LivingEntity caster) {
        if (recipe.getShape() != null && recipe.getShape().getPart() != null && recipe.getShape().getPart() == Shapes.RUNE) {
            recipe.setManaCost(recipe.getManaCost() / 2.0F);
        }
    }

    public static final boolean checkHellfireStaff(SpellAdjustingContext context) {
        return context.stack.getItem() instanceof IHellfireItem;
    }

    public static final void modifyHellfireStaff(ISpellDefinition recipe, @Nullable LivingEntity caster) {
        if (caster != null) {
            if (recipe.getAffinity().containsKey(Affinity.FIRE)) {
                recipe.iterateComponents(c -> {
                    if (((SpellEffect) c.getPart()).getAffinity() == Affinity.FIRE) {
                        UnmodifiableIterator var1x = c.getContainedAttributes().iterator();
                        while (var1x.hasNext()) {
                            Attribute attr = (Attribute) var1x.next();
                            if (((SpellEffect) c.getPart()).isHellfireBoosted(attr)) {
                                c.setValue(attr, c.getValue(attr) * 2.0F);
                            }
                        }
                    }
                });
                recipe.setOverrideAffinity(Affinity.HELLFIRE);
                recipe.setManaCost(recipe.getManaCost() * 3.0F);
            }
        }
    }

    public static final boolean checkArcaneCrown(SpellAdjustingContext context) {
        return context.caster != null && context.caster instanceof Player ? ((Player) context.caster).getInventory().armor.get(EquipmentSlot.HEAD.getIndex()).getItem() == ItemInit.ARCANE_CROWN.get() || CuriosInterop.IsItemInCurioSlot(ItemInit.ARCANE_CROWN.get(), context.caster, SlotTypePreset.HEAD) : false;
    }

    public static final void modifyArcaneCrown(ISpellDefinition recipe, @Nullable LivingEntity caster) {
        if (caster != null) {
            recipe.setManaCost(recipe.getManaCost() * 0.85F);
        }
    }

    public static final void modifyBreakRings(ISpellDefinition recipe, @Nullable LivingEntity caster) {
        if (caster != null) {
            recipe.getComponents().stream().filter(c -> c.getPart() == Components.BREAK || c.getPart() == Components.EXCHANGE).findFirst().ifPresent(c -> {
                int magnitudeIncrease = 0;
                if (CuriosApi.getCuriosHelper().findFirstCurio(caster, ItemInit.BREAK_RING_GREATER.get()).isPresent()) {
                    magnitudeIncrease = 2;
                } else if (CuriosApi.getCuriosHelper().findFirstCurio(caster, ItemInit.BREAK_RING_LESSER.get()).isPresent()) {
                    magnitudeIncrease = 1;
                }
                for (int i = 0; i < magnitudeIncrease; i++) {
                    c.stepUpIgnoreMax(Attribute.MAGNITUDE);
                }
            });
        }
    }

    public static final boolean checkRingOfTheSkies(SpellAdjustingContext context) {
        if (context.caster == null) {
            return false;
        } else {
            return context.stage != SpellCastStage.CASTING && context.stage != SpellCastStage.SPELL_TOOLTIP ? false : CuriosInterop.IsItemInCurioSlot(ItemInit.AIR_CAST_RING.get(), context.caster, SlotTypePreset.RING);
        }
    }

    public static final void modifyAirCastRing(ISpellDefinition recipe, @Nullable LivingEntity caster) {
        if (caster != null) {
            recipe.getComponents().stream().filter(c -> c.getPart() == Components.FLING).findFirst().ifPresent(c -> c.stepUpIgnoreMax(Attribute.SPEED));
        }
    }

    public static final void modifyBattlemageAmulet(ISpellDefinition recipe, @Nullable LivingEntity caster) {
        if (caster != null) {
            if (CuriosApi.getCuriosHelper().findFirstCurio(caster, ItemInit.BATTLEMAGE_AMULET.get()).isPresent()) {
                ItemStack mainHand = caster.getMainHandItem();
                ItemStack offHand = caster.getOffhandItem();
                if (mainHand.getItem() instanceof SwordItem && offHand.getItem() instanceof ItemSpell || offHand.getItem() instanceof SwordItem && mainHand.getItem() instanceof ItemSpell) {
                    recipe.getComponents().forEach(c -> c.setMultiplier(Attribute.DAMAGE, c.getMultiplier(Attribute.DAMAGE) + 0.3F));
                }
            }
        }
    }

    public static final boolean checkEldrinBracelet(SpellAdjustingContext context) {
        if (context.caster == null || !(context.caster instanceof Player)) {
            return false;
        } else if (context.stage != SpellCastStage.SPELL_TOOLTIP && context.stage != SpellCastStage.CALCULATING_MANA_COST) {
            if (context.stage == SpellCastStage.CASTING && ItemInit.ELDRIN_BRACELET.get().isEquippedAndHasMana(context.caster, 1.0F, true)) {
                ItemInit.ELDRIN_BRACELET.get().usedByPlayer((Player) context.caster);
                return true;
            } else {
                return false;
            }
        } else {
            return ItemInit.ELDRIN_BRACELET.get().isEquippedAndHasMana(context.caster, 1.0F, false);
        }
    }

    public static final void modifyEldrinBracelet(ISpellDefinition recipe, @Nullable LivingEntity caster) {
        if (caster != null) {
            caster.m_9236_().getCapability(WorldMagicProvider.MAGIC).ifPresent(m -> {
                BlockPos casterPos = new BlockPos(caster.m_20183_().m_123341_(), 0, caster.m_20183_().m_123343_());
                m.getWellspringRegistry().getNearbyNodes(casterPos, 0, 10).forEach((pos, node) -> {
                    double dist = casterPos.m_123331_(pos);
                    float pctReduction = (float) (0.5 * ((100.0 - dist) / 100.0));
                    recipe.setManaCost(recipe.getManaCost() - recipe.getManaCost() * pctReduction);
                });
            });
        }
    }

    public static final boolean checkCircleOfPower(SpellAdjustingContext context) {
        if (context.caster == null) {
            return false;
        } else {
            return context.stage != SpellCastStage.CASTING && context.stage != SpellCastStage.SPELL_TOOLTIP ? false : context.caster.hasEffect(EffectInit.CIRCLE_OF_POWER.get());
        }
    }

    public static final void modifyCircleOfPower(ISpellDefinition recipe, @Nullable LivingEntity caster) {
        if (caster != null) {
            float originalMana = recipe.getManaCost();
            int amp = caster.getEffect(EffectInit.CIRCLE_OF_POWER.get()).getAmplifier() + 1;
            recipe.iterateComponents(c -> c.getContainedAttributes().forEach(attr -> {
                if (attr == Attribute.SPEED) {
                    c.setValue(attr, c.getValue(attr) + (float) amp);
                }
                if (attr == Attribute.DAMAGE) {
                    c.setMultiplier(attr, 1.0F + 0.25F * (float) amp);
                }
                if (attr == Attribute.DURATION) {
                    c.setMultiplier(attr, (float) (2 * amp));
                }
            }));
            recipe.setManaCost(originalMana);
        }
    }

    public static final void modifyAmplifyMagic(ISpellDefinition recipe, @Nullable LivingEntity caster) {
        if (caster != null) {
            if (caster.hasEffect(EffectInit.AMPLIFY_MAGIC.get())) {
                int amplifier = caster.getEffect(EffectInit.AMPLIFY_MAGIC.get()).getAmplifier();
                recipe.setManaCost(recipe.getManaCost() * (1.0F - 0.05F * (float) amplifier));
            }
            if (caster.hasEffect(EffectInit.DAMPEN_MAGIC.get())) {
                int amplifier = caster.getEffect(EffectInit.DAMPEN_MAGIC.get()).getAmplifier();
                recipe.setManaCost(recipe.getManaCost() * (1.0F + 0.05F * (float) amplifier));
            }
        }
    }

    public static final void modifyEfficiencyEnchant(SpellAdjustingContext context) {
        if (context.caster != null) {
            int level = context.stack.getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY);
            if (level != 0) {
                context.spell.setManaCost(context.spell.getManaCost() * (1.0F - 0.05F * (float) level));
            }
        }
    }

    public static final void modifyStepEnchantments(SpellAdjustingContext context) {
        if (context.spell.isValid()) {
            ItemStack spellStack = context.stack;
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(spellStack);
            for (Entry<Enchantment, Integer> e : enchantments.entrySet()) {
                if (e.getKey() instanceof ModifierEnchantment) {
                    ModifierEnchantment me = (ModifierEnchantment) e.getKey();
                    int steps = me.bonusStepsPerLevel * (Integer) e.getValue();
                    if (context.spell.getShape().getContainedAttributes().contains(me.boost)) {
                        for (int i = 0; i < steps; i++) {
                            context.spell.getShape().stepUpIgnoreMax(me.boost);
                        }
                    }
                    context.spell.iterateComponents(c -> {
                        if (c.getContainedAttributes().contains(me.boost)) {
                            for (int ix = 0; ix < steps; ix++) {
                                c.stepUpIgnoreMax(me.boost);
                            }
                        }
                    });
                }
            }
        }
    }

    public static final boolean checkSummerFire(SpellAdjustingContext context) {
        if (!(context.caster instanceof Player player)) {
            return false;
        } else {
            IPlayerProgression progression = (IPlayerProgression) player.getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
            if (progression != null && progression.getAlliedFaction() == Factions.FEY) {
                IPlayerMagic magic = (IPlayerMagic) player.getCapability(PlayerMagicProvider.MAGIC).orElse(null);
                return magic != null && magic.getCastingResource().getRegistryName() == CastingResourceIDs.SUMMER_FIRE;
            } else {
                return false;
            }
        }
    }

    public static final boolean checkWinterIce(SpellAdjustingContext context) {
        if (!(context.caster instanceof Player player)) {
            return false;
        } else {
            IPlayerProgression progression = (IPlayerProgression) player.getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
            if (progression != null && progression.getAlliedFaction() == Factions.FEY) {
                IPlayerMagic magic = (IPlayerMagic) player.getCapability(PlayerMagicProvider.MAGIC).orElse(null);
                return magic != null && magic.getCastingResource().getRegistryName() == CastingResourceIDs.WINTER_ICE;
            } else {
                return false;
            }
        }
    }

    public static final void modifySummerFire(SpellAdjustingContext context) {
        if (context.spell.isValid()) {
            long time = context.caster.m_9236_().getDayTime() % 24000L;
            boolean isDay = time <= 13000L;
            if (isDay) {
                multiplyAttributes(context, 1.25F, Attribute.DURATION, Attribute.DAMAGE);
                multiplyAttributes(context, 1.5F, Attribute.RANGE, Attribute.SPEED);
            } else {
                multiplyAttributes(context, 1.25F, Attribute.DURATION, Attribute.DAMAGE, Attribute.RANGE, Attribute.SPEED);
                stepDownAttributes(context, 2, Attribute.DURATION, Attribute.SPEED);
                stepDownAttributes(context, 3, Attribute.RANGE);
                stepDownAttributes(context, 5, Attribute.DAMAGE);
            }
        }
    }

    public static final void modifyWinterIce(SpellAdjustingContext context) {
        if (context.spell.isValid()) {
            long time = context.caster.m_9236_().getDayTime() % 24000L;
            boolean isNight = time >= 13000L;
            if (isNight) {
                multiplyAttributes(context, 1.25F, Attribute.DURATION, Attribute.DAMAGE);
                multiplyAttributes(context, 1.5F, Attribute.RANGE, Attribute.SPEED);
            } else {
                multiplyAttributes(context, 1.25F, Attribute.DURATION, Attribute.DAMAGE, Attribute.RANGE, Attribute.SPEED);
                stepDownAttributes(context, 2, Attribute.DURATION, Attribute.SPEED);
                stepDownAttributes(context, 3, Attribute.RANGE);
                stepDownAttributes(context, 5, Attribute.DAMAGE);
            }
        }
    }

    private static final void multiplyAttributes(SpellAdjustingContext context, float multiplier, Attribute... attributes) {
        for (Attribute attr : attributes) {
            context.spell.getShape().setMultiplier(attr, multiplier);
            context.spell.iterateComponents(c -> c.setMultiplier(attr, multiplier));
        }
    }

    private static final void stepDownAttributes(SpellAdjustingContext context, int steps, Attribute... attributes) {
        for (Attribute attr : attributes) {
            float minimum = context.spell.getShape().getMinimumValue(attr);
            for (int i = 0; i < steps; i++) {
                context.spell.getShape().stepDown(attr, minimum);
            }
            context.spell.iterateComponents(c -> {
                float c_minimum = c.getMinimumValue(attr);
                for (int ix = 0; ix < steps; ix++) {
                    c.stepDown(attr, c_minimum);
                }
            });
        }
    }
}