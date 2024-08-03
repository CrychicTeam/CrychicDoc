package dev.xkmc.l2library.serial.conditions;

import java.util.ArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.config.ModConfig;

public record BooleanValueCondition(String path, ArrayList<String> line, boolean expected) implements ICondition {

    public static final ResourceLocation ID = new ResourceLocation("l2library", "boolean_config");

    public static BooleanValueCondition of(String file, ForgeConfigSpec.ConfigValue<Boolean> config, boolean value) {
        return new BooleanValueCondition(file, new ArrayList(config.getPath()), value);
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
                if (line instanceof Boolean bool && bool == this.expected) {
                    return true;
                }
                return false;
            }
        }
    }
}