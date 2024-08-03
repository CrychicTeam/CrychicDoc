package com.simibubi.create.content.kinetics.base;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;

public interface IRotate extends IWrenchable {

    boolean hasShaftTowards(LevelReader var1, BlockPos var2, BlockState var3, Direction var4);

    Direction.Axis getRotationAxis(BlockState var1);

    default IRotate.SpeedLevel getMinimumRequiredSpeedLevel() {
        return IRotate.SpeedLevel.NONE;
    }

    default boolean hideStressImpact() {
        return false;
    }

    default boolean showCapacityWithAnnotation() {
        return false;
    }

    public static enum SpeedLevel {

        NONE(ChatFormatting.DARK_GRAY, 0, 0), SLOW(ChatFormatting.GREEN, 2293538, 10), MEDIUM(ChatFormatting.AQUA, 34047, 20), FAST(ChatFormatting.LIGHT_PURPLE, 16733695, 30);

        private final ChatFormatting textColor;

        private final int color;

        private final int particleSpeed;

        private SpeedLevel(ChatFormatting textColor, int color, int particleSpeed) {
            this.textColor = textColor;
            this.color = color;
            this.particleSpeed = particleSpeed;
        }

        public ChatFormatting getTextColor() {
            return this.textColor;
        }

        public int getColor() {
            return this.color;
        }

        public int getParticleSpeed() {
            return this.particleSpeed;
        }

        public float getSpeedValue() {
            switch(this) {
                case FAST:
                    return AllConfigs.server().kinetics.fastSpeed.get().floatValue();
                case MEDIUM:
                    return AllConfigs.server().kinetics.mediumSpeed.get().floatValue();
                case SLOW:
                    return 1.0F;
                case NONE:
                default:
                    return 0.0F;
            }
        }

        public static IRotate.SpeedLevel of(float speed) {
            speed = Math.abs(speed);
            if ((double) speed >= AllConfigs.server().kinetics.fastSpeed.get()) {
                return FAST;
            } else if ((double) speed >= AllConfigs.server().kinetics.mediumSpeed.get()) {
                return MEDIUM;
            } else {
                return speed >= 1.0F ? SLOW : NONE;
            }
        }

        public static LangBuilder getFormattedSpeedText(float speed, boolean overstressed) {
            IRotate.SpeedLevel speedLevel = of(speed);
            LangBuilder builder = Lang.text(TooltipHelper.makeProgressBar(3, speedLevel.ordinal()));
            builder.translate("tooltip.speedRequirement." + Lang.asId(speedLevel.name())).space().text("(").add(Lang.number((double) Math.abs(speed))).space().translate("generic.unit.rpm").text(")").space();
            if (overstressed) {
                builder.style(ChatFormatting.DARK_GRAY).style(ChatFormatting.STRIKETHROUGH);
            } else {
                builder.style(speedLevel.getTextColor());
            }
            return builder;
        }
    }

    public static enum StressImpact {

        LOW(ChatFormatting.YELLOW, ChatFormatting.GREEN), MEDIUM(ChatFormatting.GOLD, ChatFormatting.YELLOW), HIGH(ChatFormatting.RED, ChatFormatting.GOLD), OVERSTRESSED(ChatFormatting.RED, ChatFormatting.RED);

        private final ChatFormatting absoluteColor;

        private final ChatFormatting relativeColor;

        private StressImpact(ChatFormatting absoluteColor, ChatFormatting relativeColor) {
            this.absoluteColor = absoluteColor;
            this.relativeColor = relativeColor;
        }

        public ChatFormatting getAbsoluteColor() {
            return this.absoluteColor;
        }

        public ChatFormatting getRelativeColor() {
            return this.relativeColor;
        }

        public static IRotate.StressImpact of(double stressPercent) {
            if (stressPercent > 1.0) {
                return OVERSTRESSED;
            } else if (stressPercent > 0.75) {
                return HIGH;
            } else {
                return stressPercent > 0.5 ? MEDIUM : LOW;
            }
        }

        public static boolean isEnabled() {
            return !AllConfigs.server().kinetics.disableStress.get();
        }

        public static LangBuilder getFormattedStressText(double stressPercent) {
            IRotate.StressImpact stressLevel = of(stressPercent);
            return Lang.text(TooltipHelper.makeProgressBar(3, Math.min(stressLevel.ordinal() + 1, 3))).translate("tooltip.stressImpact." + Lang.asId(stressLevel.name())).text(String.format(" (%s%%) ", (int) (stressPercent * 100.0))).style(stressLevel.getRelativeColor());
        }
    }
}