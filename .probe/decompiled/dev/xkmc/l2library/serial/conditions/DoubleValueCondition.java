package dev.xkmc.l2library.serial.conditions;

import java.util.ArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.config.ModConfig;

public record DoubleValueCondition(String path, ArrayList<String> line, double low, double high) implements ICondition {

    public static final ResourceLocation ID = new ResourceLocation("l2library", "double_config");

    public static DoubleValueCondition of(String file, ForgeConfigSpec.ConfigValue<Double> config, double low, double high) {
        return new DoubleValueCondition(file, new ArrayList(config.getPath()), low, high);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public boolean test(ICondition.IContext context) {
        ModConfig file = (ModConfig) ConfigTracker.INSTANCE.fileMap().get(this.path);
        if (file == null) {
            return false;
        } else {
            Object line = file.getConfigData().get(this.line());
            if (line == null) {
                return false;
            } else {
                if (line instanceof Double val && this.low <= val && val <= this.high) {
                    return true;
                }
                return false;
            }
        }
    }
}