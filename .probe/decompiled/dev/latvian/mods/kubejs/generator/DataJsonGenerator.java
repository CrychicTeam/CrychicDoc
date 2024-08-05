package dev.latvian.mods.kubejs.generator;

import dev.latvian.mods.kubejs.script.data.GeneratedData;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;

public class DataJsonGenerator extends ResourceGenerator {

    public DataJsonGenerator(Map<ResourceLocation, GeneratedData> m) {
        super(ConsoleJS.SERVER, m);
    }
}