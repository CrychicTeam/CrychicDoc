package io.redspace.ironsspellbooks.compat.tetra.effects;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.network.ClientboundSyncMana;
import io.redspace.ironsspellbooks.setup.Messages;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
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
import se.mickelus.tetra.gui.stats.getter.TooltipGetterPercentage;
import se.mickelus.tetra.items.modular.ModularItem;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;

public class ManaSiphonTetraEffect {

    public static final ItemEffect manaSiphon = ItemEffect.get("irons_spellbooks:mana_siphon");

    public static final String siphonName = "irons_spellbooks.tetra_effect.mana_siphon";

    public static final String siphonTooltip = "irons_spellbooks.tetra_effect.mana_siphon.tooltip";

    @OnlyIn(Dist.CLIENT)
    public static void addGuiBars() {
        IStatGetter effectStatGetter = new StatGetterEffectLevel(manaSiphon, 1.0);
        GuiStatBar effectBar = new GuiStatBar(0, 0, 59, "irons_spellbooks.tetra_effect.mana_siphon", 0.0, 30.0, false, effectStatGetter, LabelGetterBasic.percentageLabel, new TooltipGetterPercentage("irons_spellbooks.tetra_effect.mana_siphon.tooltip", effectStatGetter));
        WorkbenchStatsGui.addBar(effectBar);
        HoloStatsGui.addBar(effectBar);
    }

    public static void handleLivingAttackEvent(LivingAttackEvent event) {
        DamageSource source = event.getSource();
        Entity attacker = source.getEntity();
        Entity victim = event.getEntity();
        if (attacker instanceof ServerPlayer player) {
            ItemStack heldStack = player.m_21205_();
            if (heldStack.getItem() instanceof ModularItem item) {
                int level = item.getEffectLevel(heldStack, manaSiphon);
                if (level > 0) {
                    level = (int) ((float) level * 0.01F);
                    int increment = (int) Math.min((float) level * event.getAmount(), 50.0F);
                    int maxMana = (int) player.m_21133_(AttributeRegistry.MAX_MANA.get());
                    MagicData playerMagicData = MagicData.getPlayerMagicData(player);
                    float newMana = Math.min((float) increment + playerMagicData.getMana(), (float) maxMana);
                    playerMagicData.setMana(newMana);
                    Messages.sendToPlayer(new ClientboundSyncMana(playerMagicData), player);
                    MagicManager.spawnParticles(victim.level, ParticleTypes.GLOW, victim.getX(), victim.getY() + (double) (victim.getBbHeight() * 0.5F), victim.getZ(), 10, (double) (victim.getBbWidth() * 0.5F), (double) (victim.getBbHeight() * 0.5F), (double) (victim.getBbWidth() * 0.5F), victim.level.getRandom().nextDouble() * 0.005, false);
                }
            }
        }
    }
}