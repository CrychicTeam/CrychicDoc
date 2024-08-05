package dev.xkmc.l2library.serial.conditions;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.config.ModConfig;

public record ListStringValueCondition(String path, ArrayList<String> line, String key) implements ICondition {

    public static final ResourceLocation ID = new ResourceLocation("l2library", "string_list_config");

    public static ListStringValueCondition of(String file, ForgeConfigSpec.ConfigValue<List<String>> config, String key) {
        return new ListStringValueCondition(file, new ArrayList(config.getPath()), key);
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
                if (line instanceof List<?> val && val.contains(this.key)) {
                    return true;
                }
                return false;
            }
        }
    }
}