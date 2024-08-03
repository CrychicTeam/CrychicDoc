package io.redspace.ironsspellbooks.compat.tetra;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.compat.tetra.effects.FreezeTetraEffect;
import io.redspace.ironsspellbooks.compat.tetra.effects.ManaSiphonTetraEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.getter.IStatGetter;
import se.mickelus.tetra.gui.stats.getter.LabelGetterBasic;
import se.mickelus.tetra.gui.stats.getter.StatGetterAttribute;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterInteger;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterPercentage;
import se.mickelus.tetra.items.modular.impl.ModularBladedItem;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;

public class TetraActualImpl implements ITetraProxy {

    @OnlyIn(Dist.CLIENT)
    @Override
    public void initClient() {
        FreezeTetraEffect.addGuiBars();
        ManaSiphonTetraEffect.addGuiBars();
        createPercentAttributeBar(AttributeRegistry.COOLDOWN_REDUCTION.get(), "cooldown_reduction");
        createPercentAttributeBar(AttributeRegistry.FIRE_SPELL_POWER.get(), "fire_spell_power");
        createPercentAttributeBar(AttributeRegistry.ICE_SPELL_POWER.get(), "ice_spell_power");
        createPercentAttributeBar(AttributeRegistry.LIGHTNING_SPELL_POWER.get(), "lightning_spell_power");
        createPercentAttributeBar(AttributeRegistry.HOLY_SPELL_POWER.get(), "holy_spell_power");
        createPercentAttributeBar(AttributeRegistry.ENDER_SPELL_POWER.get(), "ender_spell_power");
        createPercentAttributeBar(AttributeRegistry.BLOOD_SPELL_POWER.get(), "blood_spell_power");
        createPercentAttributeBar(AttributeRegistry.EVOCATION_SPELL_POWER.get(), "evocation_spell_power");
        createPercentAttributeBar(AttributeRegistry.NATURE_SPELL_POWER.get(), "poison_spell_power");
        createPercentAttributeBar(AttributeRegistry.SPELL_RESIST.get(), "spell_resist");
        IStatGetter manaStat = new StatGetterAttribute(AttributeRegistry.MAX_MANA.get(), true);
        GuiStatBar manaStatBar = new GuiStatBar(0, 0, 59, "attribute.irons_spellbooks.max_mana", 0.0, 500.0, false, manaStat, LabelGetterBasic.integerLabel, new TooltipGetterInteger("irons_spellbooks.tetra_bar.max_mana.tooltip", manaStat));
        WorkbenchStatsGui.addBar(manaStatBar);
        HoloStatsGui.addBar(manaStatBar);
    }

    @Override
    public boolean canImbue(ItemStack itemStack) {
        return itemStack.getItem() instanceof ModularBladedItem;
    }

    @Override
    public void handleLivingAttackEvent(LivingAttackEvent event) {
        if (!event.getEntity().f_19853_.isClientSide) {
            FreezeTetraEffect.handleLivingAttackEvent(event);
            ManaSiphonTetraEffect.handleLivingAttackEvent(event);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void createPercentAttributeBar(Attribute attribute, String languageKey) {
        IStatGetter statGetter = new StatGetterPercentAttribute(attribute);
        GuiStatBar statBar = new GuiStatBar(0, 0, 59, attribute.getDescriptionId(), 0.0, 100.0, false, statGetter, LabelGetterBasic.percentageLabel, new TooltipGetterPercentage("irons_spellbooks.tetra_bar." + languageKey + ".tooltip", statGetter));
        WorkbenchStatsGui.addBar(statBar);
        HoloStatsGui.addBar(statBar);
    }
}