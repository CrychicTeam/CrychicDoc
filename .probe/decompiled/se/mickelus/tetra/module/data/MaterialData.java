package se.mickelus.tetra.module.data;

import com.google.common.collect.Multimap;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.TierSortingRegistry;
import se.mickelus.tetra.data.deserializer.AttributesDeserializer;
import se.mickelus.tetra.data.deserializer.ItemTagKeyDeserializer;
import se.mickelus.tetra.module.schematic.OutcomeMaterial;
import se.mickelus.tetra.properties.AttributeHelper;
import se.mickelus.tetra.util.TierHelper;

@ParametersAreNonnullByDefault
public class MaterialData {

    private static final MaterialData defaultValues = new MaterialData();

    public boolean replace = false;

    public String key;

    public String category = "misc";

    public boolean hidden = false;

    public boolean hiddenOutcomes = false;

    public Multimap<Attribute, AttributeModifier> attributes;

    public Float primary;

    public Float secondary;

    public Float tertiary;

    public float durability = 0.0F;

    public float integrityGain = 0.0F;

    public float integrityCost = 0.0F;

    public int magicCapacity = 0;

    public EffectData effects = new EffectData();

    public AspectData aspects = new AspectData();

    public int toolLevel = 0;

    public float toolEfficiency = 0.0F;

    public MaterialColors tints;

    public String[] textures = new String[0];

    public String[] textureOverrides = new String[0];

    public boolean tintOverrides = false;

    public OutcomeMaterial material;

    public ToolData requiredTools;

    public float experienceCost;

    public Set<TagKey<Item>> tags;

    public String[] features = new String[0];

    public Map<String, Integer> improvements = new HashMap();

    public static void copyFields(MaterialData from, MaterialData to) {
        if (from.key != null) {
            to.key = from.key;
        }
        if (from.hidden != defaultValues.hidden) {
            to.hidden = from.hidden;
        }
        if (from.hiddenOutcomes != defaultValues.hiddenOutcomes) {
            to.hiddenOutcomes = from.hiddenOutcomes;
        }
        if (!defaultValues.category.equals(from.category)) {
            to.category = from.category;
        }
        if (from.primary != null) {
            to.primary = from.primary;
        }
        if (from.secondary != null) {
            to.secondary = from.secondary;
        }
        if (from.tertiary != null) {
            to.tertiary = from.tertiary;
        }
        if (from.durability != defaultValues.durability) {
            to.durability = from.durability;
        }
        if (from.integrityGain != defaultValues.integrityGain) {
            to.integrityGain = from.integrityGain;
        }
        if (from.integrityCost != defaultValues.integrityCost) {
            to.integrityCost = from.integrityCost;
        }
        if (from.magicCapacity != defaultValues.magicCapacity) {
            to.magicCapacity = from.magicCapacity;
        }
        if (from.toolLevel != defaultValues.toolLevel) {
            to.toolLevel = from.toolLevel;
        }
        if (from.toolEfficiency != defaultValues.toolEfficiency) {
            to.toolEfficiency = from.toolEfficiency;
        }
        if (from.tints != null) {
            to.tints = from.tints;
        }
        if (from.tintOverrides != defaultValues.tintOverrides) {
            to.tintOverrides = from.tintOverrides;
        }
        to.textureOverrides = (String[]) Stream.concat(Arrays.stream(to.textureOverrides), Arrays.stream(from.textureOverrides)).distinct().toArray(String[]::new);
        to.attributes = AttributeHelper.overwrite(to.attributes, from.attributes);
        to.effects = EffectData.overwrite(to.effects, from.effects);
        to.requiredTools = ToolData.overwrite(to.requiredTools, from.requiredTools);
        if (from.experienceCost != defaultValues.experienceCost) {
            to.experienceCost = from.experienceCost;
        }
        if (from.material != null) {
            to.material = from.material;
        }
        to.textures = (String[]) Stream.concat(Arrays.stream(to.textures), Arrays.stream(from.textures)).distinct().toArray(String[]::new);
        if (from.improvements != null) {
            if (to.improvements != null) {
                Map<String, Integer> merged = new HashMap();
                merged.putAll(to.improvements);
                merged.putAll(from.improvements);
                to.improvements = merged;
            } else {
                to.improvements = from.improvements;
            }
        }
        if (from.tags != null && to.tags != null) {
            to.tags = (Set<TagKey<Item>>) Stream.concat(from.tags.stream(), to.tags.stream()).collect(Collectors.toSet());
        } else if (from.tags != null) {
            to.tags = from.tags;
        }
        to.features = (String[]) Stream.concat(Arrays.stream(to.features), Arrays.stream(from.features)).distinct().toArray(String[]::new);
    }

    public static ModuleModel kneadModel(ModuleModel model, MaterialData material, List<String> availableTextures) {
        if (Arrays.stream(material.textureOverrides).anyMatch(override -> model.location.getPath().equals(override))) {
            ModuleModel copy = model.copy();
            copy.location = appendString(model.location, material.textures[0]);
            copy.tint = material.tintOverrides ? material.tints.texture : 16777215;
            copy.overlayTint = material.tints.texture;
            return copy;
        } else {
            ResourceLocation updatedLocation = (ResourceLocation) Arrays.stream(material.textures).filter(availableTextures::contains).findFirst().map(texture -> appendString(model.location, texture)).orElseGet(() -> appendString(model.location, (String) availableTextures.get(0)));
            ModuleModel copy = model.copy();
            copy.location = updatedLocation;
            copy.tint = material.tints.texture;
            copy.overlayTint = material.tints.texture;
            return copy;
        }
    }

    public static ResourceLocation appendString(ResourceLocation resourceLocation, String string) {
        return new ResourceLocation(resourceLocation.getNamespace(), resourceLocation.getPath() + string);
    }

    public MaterialData shallowCopy() {
        MaterialData copy = new MaterialData();
        copyFields(this, copy);
        return copy;
    }

    public static class Deserializer implements JsonDeserializer<MaterialData> {

        private static int getLevel(JsonElement element) {
            return element.getAsJsonPrimitive().isNumber() ? element.getAsInt() : (Integer) Optional.ofNullable(TierSortingRegistry.byName(new ResourceLocation(element.getAsString()))).map(TierHelper::getIndex).map(index -> index + 1).orElse(0);
        }

        public MaterialData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            MaterialData data = new MaterialData();
            if (jsonObject.has("replace")) {
                data.replace = jsonObject.get("replace").getAsBoolean();
            }
            if (jsonObject.has("key")) {
                data.key = jsonObject.get("key").getAsString();
            }
            if (jsonObject.has("category")) {
                data.category = jsonObject.get("category").getAsString();
            }
            if (jsonObject.has("hidden")) {
                data.hidden = jsonObject.get("hidden").getAsBoolean();
            }
            if (jsonObject.has("hiddenOutcomes")) {
                data.hiddenOutcomes = jsonObject.get("hiddenOutcomes").getAsBoolean();
            }
            if (jsonObject.has("attributes")) {
                data.attributes = (Multimap<Attribute, AttributeModifier>) context.deserialize(jsonObject.get("attributes"), AttributesDeserializer.typeToken.getRawType());
            }
            if (jsonObject.has("primary")) {
                data.primary = jsonObject.get("primary").getAsFloat();
            }
            if (jsonObject.has("secondary")) {
                data.secondary = jsonObject.get("secondary").getAsFloat();
            }
            if (jsonObject.has("tertiary")) {
                data.tertiary = jsonObject.get("tertiary").getAsFloat();
            }
            if (jsonObject.has("durability")) {
                data.durability = jsonObject.get("durability").getAsFloat();
            }
            if (jsonObject.has("integrityGain")) {
                data.integrityGain = jsonObject.get("integrityGain").getAsFloat();
            }
            if (jsonObject.has("integrityCost")) {
                data.integrityCost = jsonObject.get("integrityCost").getAsFloat();
            }
            if (jsonObject.has("magicCapacity")) {
                data.magicCapacity = jsonObject.get("magicCapacity").getAsInt();
            }
            if (jsonObject.has("effects")) {
                data.effects = (EffectData) context.deserialize(jsonObject.get("effects"), EffectData.class);
            }
            if (jsonObject.has("aspects")) {
                data.aspects = (AspectData) context.deserialize(jsonObject.get("aspects"), AspectData.class);
            }
            if (jsonObject.has("toolLevel")) {
                data.toolLevel = getLevel(jsonObject.get("toolLevel"));
            }
            if (jsonObject.has("toolEfficiency")) {
                data.toolEfficiency = jsonObject.get("toolEfficiency").getAsFloat();
            }
            if (jsonObject.has("tints")) {
                data.tints = (MaterialColors) context.deserialize(jsonObject.get("tints"), MaterialColors.class);
            }
            if (jsonObject.has("textures")) {
                data.textures = (String[]) context.deserialize(jsonObject.get("textures"), String[].class);
            }
            if (jsonObject.has("tintOverrides")) {
                data.tintOverrides = jsonObject.get("tintOverrides").getAsBoolean();
            }
            if (jsonObject.has("textureOverrides")) {
                data.textureOverrides = (String[]) context.deserialize(jsonObject.get("textureOverrides"), String[].class);
            }
            if (jsonObject.has("material")) {
                data.material = (OutcomeMaterial) context.deserialize(jsonObject.get("material"), OutcomeMaterial.class);
            }
            if (jsonObject.has("requiredTools")) {
                data.requiredTools = (ToolData) context.deserialize(jsonObject.get("requiredTools"), ToolData.class);
            }
            if (jsonObject.has("experienceCost")) {
                data.experienceCost = jsonObject.get("experienceCost").getAsFloat();
            }
            if (jsonObject.has("improvements")) {
                JsonElement improvementsJson = jsonObject.get("improvements");
                if (improvementsJson.isJsonObject()) {
                    data.improvements = (Map<String, Integer>) improvementsJson.getAsJsonObject().entrySet().stream().collect(Collectors.toMap(Entry::getKey, e -> ((JsonElement) e.getValue()).getAsInt()));
                }
            }
            if (jsonObject.has("tags")) {
                data.tags = (Set<TagKey<Item>>) context.deserialize(jsonObject.get("tags"), ItemTagKeyDeserializer.typeToken.getRawType());
            }
            if (jsonObject.has("features")) {
                data.features = (String[]) context.deserialize(jsonObject.get("features"), String[].class);
            }
            return data;
        }
    }
}