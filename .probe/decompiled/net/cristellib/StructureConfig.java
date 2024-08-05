package net.cristellib;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.cristellib.config.ConfigType;
import net.cristellib.config.ConfigUtil;
import net.cristellib.config.Placement;
import net.cristellib.util.Util;
import net.minecraft.resources.ResourceLocation;

public class StructureConfig {

    private final Path path;

    private String header = "";

    private HashMap<String, String> comments = new HashMap();

    private final ConfigType type;

    private final Map<ResourceLocation, List<String>> structures = new HashMap();

    private final Map<ResourceLocation, Placement> structurePlacement = new HashMap();

    private final List<Pair<String, ResourceLocation>> structureSets = new ArrayList();

    private StructureConfig(Path path, ConfigType type) {
        this.path = path;
        this.type = type;
    }

    protected void addSet(Pair<String, ResourceLocation> set) {
        this.structureSets.add(set);
    }

    protected void addSetsToRuntimePack() {
        Map<String, Boolean> map = ConfigUtil.readConfig(this.path);
        Map<String, Placement> map2 = ConfigUtil.readPlacementConfig(this.path);
        for (Pair<String, ResourceLocation> s : this.structureSets) {
            ResourceLocation location = (ResourceLocation) s.getSecond();
            JsonElement e = null;
            ResourceLocation structureLocation = Util.getLocationForStructureSet(location);
            if (CristelLib.DATA_PACK.hasResource(structureLocation)) {
                e = CristelLib.DATA_PACK.getResource(structureLocation);
            }
            if (e == null) {
                e = ConfigUtil.getSetElement((String) s.getFirst(), location);
            }
            if (e instanceof JsonObject object) {
                JsonObject objectOld = object.deepCopy();
                if (!this.type.equals(ConfigType.ENABLE_DISABLE)) {
                    if (this.type.equals(ConfigType.PLACEMENT) && map2.containsKey(location.getPath())) {
                        JsonObject a = object.get("placement").getAsJsonObject();
                        Placement p = (Placement) map2.get(location.getPath());
                        a.addProperty("salt", p.salt);
                        a.addProperty("spacing", p.spacing);
                        a.addProperty("separation", p.separation);
                        double f = p.frequency;
                        if (f != 0.0 && a.has("frequency") && (double) a.get("frequency").getAsFloat() != f) {
                            a.addProperty("frequency", f);
                        }
                    }
                } else {
                    JsonArray array = object.get("structures").getAsJsonArray();
                    List<JsonElement> elements = new ArrayList();
                    for (JsonElement eInArray : array) {
                        String structureName = eInArray.getAsJsonObject().get("structure").getAsString().split(":")[1];
                        if (map.containsKey(structureName) && !(Boolean) map.get(structureName)) {
                            elements.add(eInArray);
                        }
                    }
                    elements.forEach(array::remove);
                }
                if (!object.equals(objectOld)) {
                    CristelLib.DATA_PACK.addStructureSet(location, object);
                }
            } else {
                CristelLib.LOGGER.error("Set for {} {} is not a JsonObject", s.getFirst(), location);
            }
        }
    }

    protected void writeConfig() {
        if (this.type.equals(ConfigType.ENABLE_DISABLE) && this.structures.isEmpty()) {
            this.readSetsAndAddStructures();
            ConfigUtil.createConfig(this);
        } else if (this.type.equals(ConfigType.PLACEMENT) && this.structurePlacement.isEmpty()) {
            this.readSetsAndAddPlacements();
            ConfigUtil.createPlacementConfig(this);
        }
    }

    private void readSetsAndAddStructures() {
        for (Pair<String, ResourceLocation> p : this.structureSets) {
            ResourceLocation setLocation = (ResourceLocation) p.getSecond();
            JsonElement e = ConfigUtil.getSetElement((String) p.getFirst(), setLocation);
            if (e == null) {
                CristelLib.LOGGER.error("Set for {} {} is not a JsonObject", p.getFirst(), setLocation);
            } else {
                JsonArray a = e.getAsJsonObject().get("structures").getAsJsonArray();
                List<String> structureList = new ArrayList();
                for (JsonElement element : a) {
                    if (element instanceof JsonObject) {
                        structureList.add(element.getAsJsonObject().get("structure").getAsString());
                    }
                }
                this.structures.put(setLocation, structureList);
            }
        }
    }

    private void readSetsAndAddPlacements() {
        for (Pair<String, ResourceLocation> pair : this.structureSets) {
            ResourceLocation setLocation = (ResourceLocation) pair.getSecond();
            JsonElement e = ConfigUtil.getSetElement((String) pair.getFirst(), setLocation);
            if (e == null) {
                CristelLib.LOGGER.error("Set for {} {} is not a JsonObject", pair.getFirst(), setLocation);
            } else {
                JsonObject a = e.getAsJsonObject().get("placement").getAsJsonObject();
                Placement p = new Placement();
                JsonElement salt = a.get("salt");
                JsonElement spacing = a.get("spacing");
                JsonElement separation = a.get("separation");
                JsonElement frequency = a.get("frequency");
                if (salt != null) {
                    p.salt = salt.getAsInt();
                }
                if (spacing != null) {
                    p.spacing = spacing.getAsInt();
                }
                if (separation != null) {
                    p.separation = separation.getAsInt();
                }
                if (frequency != null) {
                    p.frequency = frequency.getAsDouble();
                }
                this.structurePlacement.put(setLocation, p);
            }
        }
    }

    public static StructureConfig create(Path path, String name, ConfigType type) {
        return new StructureConfig(path.resolve(name + ".json5"), type);
    }

    public static StructureConfig createWithDefaultConfigPath(String subPath, String name, ConfigType type) {
        return new StructureConfig(CristelLibExpectPlatform.getConfigDirectory().resolve(subPath).resolve(name + ".json5"), type);
    }

    public static StructureConfig createWithDefaultConfigPath(String name, ConfigType type) {
        return new StructureConfig(CristelLibExpectPlatform.getConfigDirectory().resolve(name + ".json5"), type);
    }

    public void setComments(HashMap<String, String> comments) {
        this.comments = comments;
    }

    public void setHeader(String header) {
        this.header = "/*\n" + header + "*/";
    }

    public String getHeader() {
        return this.header;
    }

    public HashMap<String, String> getComments() {
        return this.comments;
    }

    public Map<ResourceLocation, List<String>> getStructures() {
        return this.structures;
    }

    public Map<ResourceLocation, Placement> getStructurePlacement() {
        return this.structurePlacement;
    }

    public Path getPath() {
        return this.path;
    }
}