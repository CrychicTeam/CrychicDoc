package fr.frinn.custommachinery.forge.client;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import fr.frinn.custommachinery.api.machine.MachineStatus;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;

public class CustomMachineModelLoader implements IGeometryLoader<CustomMachineModelLoader.CustomMachineModelGeometry> {

    public static final CustomMachineModelLoader INSTANCE = new CustomMachineModelLoader();

    public CustomMachineModelLoader.CustomMachineModelGeometry read(JsonObject json, JsonDeserializationContext deserializationContext) {
        Map<MachineStatus, ResourceLocation> defaults = new EnumMap(MachineStatus.class);
        if (json.has("defaults") && json.get("defaults").isJsonObject()) {
            JsonObject defaultsJson = json.getAsJsonObject("defaults");
            if (defaultsJson.get("").isJsonPrimitive() && defaultsJson.get("").getAsJsonPrimitive().isString()) {
                ResourceLocation location = ResourceLocation.tryParse(defaultsJson.get("").getAsString());
                Arrays.stream(MachineStatus.values()).forEach(statusx -> defaults.put(statusx, location));
            }
            for (MachineStatus status : MachineStatus.values()) {
                String key = status.name().toLowerCase(Locale.ROOT);
                if (defaultsJson.has(key) && defaultsJson.get(key).isJsonPrimitive() && defaultsJson.get(key).getAsJsonPrimitive().isString()) {
                    defaults.put(status, ResourceLocation.tryParse(defaultsJson.get(key).getAsString()));
                }
            }
        }
        return new CustomMachineModelLoader.CustomMachineModelGeometry(defaults);
    }

    public static class CustomMachineModelGeometry implements IUnbakedGeometry<CustomMachineModelLoader.CustomMachineModelGeometry> {

        private final Map<MachineStatus, ResourceLocation> defaults;

        public CustomMachineModelGeometry(Map<MachineStatus, ResourceLocation> defaults) {
            this.defaults = defaults;
        }

        @Override
        public BakedModel bake(IGeometryBakingContext iGeometryBakingContext, ModelBaker arg, Function<Material, TextureAtlasSprite> function, ModelState arg2, ItemOverrides arg3, ResourceLocation arg4) {
            return new CustomMachineBakedModel(this.defaults);
        }
    }
}