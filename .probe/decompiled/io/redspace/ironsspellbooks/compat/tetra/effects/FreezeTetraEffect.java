package io.redspace.ironsspellbooks.compat.tetra.effects;

import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.getter.IStatGetter;
import se.mickelus.tetra.gui.stats.getter.LabelGetterBasic;
import se.mickelus.tetra.gui.stats.getter.StatGetterEffectLevel;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterDecimal;
import se.mickelus.tetra.items.modular.ModularItem;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;

public class FreezeTetraEffect {

    public static final ItemEffect freezeOnHit = ItemEffect.get("irons_spellbooks:freeze");

    public static final String freezeName = "irons_spellbooks.tetra_effect.freeze";

    public static final String freezeTooltip = "irons_spellbooks.tetra_effect.freeze.tooltip";

    @OnlyIn(Dist.CLIENT)
    public static void addGuiBars() {
        IStatGetter effectStatGetter = new StatGetterEffectLevel(freezeOnHit, 1.0);
        GuiStatBar effectBar = new GuiStatBar(0, 0, 59, "irons_spellbooks.tetra_effect.freeze", 0.0, 30.0, false, effectStatGetter, LabelGetterBasic.decimalLabel, new TooltipGetterDecimal("irons_spellbooks.tetra_effect.freeze.tooltip", effectStatGetter));
        WorkbenchStatsGui.addBar(effectBar);
        HoloStatsGui.addBar(effectBar);
    }

    public static void handleLivingAttackEvent(LivingAttackEvent event) {
        LivingEntity attackedEntity = event.getEntity();
        DamageSource source = event.getSource();
        if (source.getEntity() instanceof LivingEntity livingAttacker) {
            ItemStack heldStack = livingAttacker.getMainHandItem();
            if (heldStack.getItem() instanceof ModularItem item) {
                int level = item.getEffectLevel(heldStack, freezeOnHit);
                if (level > 0) {
                    if (attackedEntity.canFreeze()) {
                        attackedEntity.m_146917_(attackedEntity.m_146888_() + level * 20);
                    }
                    MagicManager.spawnParticles(attackedEntity.f_19853_, ParticleHelper.SNOWFLAKE, attackedEntity.m_20185_(), attackedEntity.m_20186_() + (double) (attackedEntity.m_20206_() * 0.5F), attackedEntity.m_20189_(), 10, (double) (attackedEntity.m_20205_() * 0.5F), (double) (attackedEntity.m_20206_() * 0.5F), (double) (attackedEntity.m_20205_() * 0.5F), 0.03, false);
                }
            }
        }
    }
}