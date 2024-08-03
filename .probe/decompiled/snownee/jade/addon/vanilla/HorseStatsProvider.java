package snownee.jade.addon.vanilla;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Llama;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.Identifiers;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.overlay.DisplayHelper;

public enum HorseStatsProvider implements IEntityComponentProvider {

    INSTANCE;

    private static final double MAX_JUMP_HEIGHT = getJumpHeight((double) AbstractHorse.MAX_JUMP_STRENGTH);

    private static final double MAX_MOVEMENT_SPEED = (double) AbstractHorse.MAX_MOVEMENT_SPEED * 42.16;

    private static Component switchText(String key, boolean showMax, double value, double max) {
        IThemeHelper t = IThemeHelper.get();
        Component valueText = t.info(DisplayHelper.dfCommas.format(value));
        return showMax ? Component.translatable(key, Component.translatable("jade.fraction", valueText, DisplayHelper.dfCommas.format(max))) : Component.translatable(key, valueText);
    }

    private static double getJumpHeight(double jumpStrength) {
        return -0.1817584952 * jumpStrength * jumpStrength * jumpStrength + 3.689713992 * jumpStrength * jumpStrength + 2.128599134 * jumpStrength - 0.343930367;
    }

    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
        AbstractHorse horse = (AbstractHorse) accessor.getEntity();
        boolean showMax = accessor.showDetails();
        if (horse instanceof Llama llama) {
            tooltip.add(switchText("jade.llamaStrength", showMax, (double) llama.getStrength(), 5.0));
        } else if (!(horse instanceof Camel)) {
            double jumpStrength = horse.m_21172_(Attributes.JUMP_STRENGTH);
            double jumpHeight = getJumpHeight(jumpStrength);
            double speed = horse.m_21172_(Attributes.MOVEMENT_SPEED) * 42.16;
            tooltip.add(switchText("jade.horseStat.jump", showMax, jumpHeight, MAX_JUMP_HEIGHT));
            tooltip.add(switchText("jade.horseStat.speed", showMax, speed, MAX_MOVEMENT_SPEED));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return Identifiers.MC_HORSE_STATS;
    }
}