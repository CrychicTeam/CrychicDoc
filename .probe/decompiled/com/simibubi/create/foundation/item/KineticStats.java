package com.simibubi.create.foundation.item;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.equipment.goggles.GogglesItem;
import com.simibubi.create.content.kinetics.BlockStressValues;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.crank.ValveHandleBlock;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlock;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import com.simibubi.create.infrastructure.config.AllConfigs;
import com.simibubi.create.infrastructure.config.CKinetics;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import org.jetbrains.annotations.Nullable;

public class KineticStats implements TooltipModifier {

    protected final Block block;

    public KineticStats(Block block) {
        this.block = block;
    }

    @Nullable
    public static KineticStats create(Item item) {
        if (item instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            if (block instanceof IRotate || block instanceof SteamEngineBlock) {
                return new KineticStats(block);
            }
        }
        return null;
    }

    @Override
    public void modify(ItemTooltipEvent context) {
        List<Component> kineticStats = getKineticStats(this.block, context.getEntity());
        if (!kineticStats.isEmpty()) {
            List<Component> tooltip = context.getToolTip();
            tooltip.add(Components.immutableEmpty());
            tooltip.addAll(kineticStats);
        }
    }

    public static List<Component> getKineticStats(Block block, Player player) {
        List<Component> list = new ArrayList();
        CKinetics config = AllConfigs.server().kinetics;
        LangBuilder rpmUnit = Lang.translate("generic.unit.rpm");
        LangBuilder suUnit = Lang.translate("generic.unit.stress");
        boolean hasGoggles = GogglesItem.isWearingGoggles(player);
        boolean showStressImpact;
        if (block instanceof IRotate) {
            showStressImpact = !((IRotate) block).hideStressImpact();
        } else {
            showStressImpact = true;
        }
        if (block instanceof ValveHandleBlock) {
            block = (Block) AllBlocks.COPPER_VALVE_HANDLE.get();
        }
        boolean hasStressImpact = IRotate.StressImpact.isEnabled() && showStressImpact && BlockStressValues.getImpact(block) > 0.0;
        boolean hasStressCapacity = IRotate.StressImpact.isEnabled() && BlockStressValues.hasCapacity(block);
        if (hasStressImpact) {
            Lang.translate("tooltip.stressImpact").style(ChatFormatting.GRAY).addTo(list);
            double impact = BlockStressValues.getImpact(block);
            IRotate.StressImpact impactId = impact >= config.highStressImpact.get() ? IRotate.StressImpact.HIGH : (impact >= config.mediumStressImpact.get() ? IRotate.StressImpact.MEDIUM : IRotate.StressImpact.LOW);
            LangBuilder builder = Lang.builder().add(Lang.text(TooltipHelper.makeProgressBar(3, impactId.ordinal() + 1)).style(impactId.getAbsoluteColor()));
            if (hasGoggles) {
                builder.add(Lang.number(impact)).text("x ").add(rpmUnit).addTo(list);
            } else {
                builder.translate("tooltip.stressImpact." + Lang.asId(impactId.name())).addTo(list);
            }
        }
        if (hasStressCapacity) {
            Lang.translate("tooltip.capacityProvided").style(ChatFormatting.GRAY).addTo(list);
            double capacity = BlockStressValues.getCapacity(block);
            Couple<Integer> generatedRPM = BlockStressValues.getGeneratedRPM(block);
            IRotate.StressImpact impactId = capacity >= config.highCapacity.get() ? IRotate.StressImpact.HIGH : (capacity >= config.mediumCapacity.get() ? IRotate.StressImpact.MEDIUM : IRotate.StressImpact.LOW);
            IRotate.StressImpact opposite = IRotate.StressImpact.values()[IRotate.StressImpact.values().length - 2 - impactId.ordinal()];
            LangBuilder builder = Lang.builder().add(Lang.text(TooltipHelper.makeProgressBar(3, impactId.ordinal() + 1)).style(opposite.getAbsoluteColor()));
            if (hasGoggles) {
                builder.add(Lang.number(capacity)).text("x ").add(rpmUnit).addTo(list);
                if (generatedRPM != null) {
                    LangBuilder amount = Lang.number(capacity * (double) generatedRPM.getSecond().intValue()).add(suUnit);
                    Lang.text(" -> ").add(!generatedRPM.getFirst().equals(generatedRPM.getSecond()) ? Lang.translate("tooltip.up_to", amount) : amount).style(ChatFormatting.DARK_GRAY).addTo(list);
                }
            } else {
                builder.translate("tooltip.capacityProvided." + Lang.asId(impactId.name())).addTo(list);
            }
        }
        return list;
    }
}