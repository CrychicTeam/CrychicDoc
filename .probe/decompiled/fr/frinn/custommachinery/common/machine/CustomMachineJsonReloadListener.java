package fr.frinn.custommachinery.common.machine;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.DataResult.PartialResult;
import dev.architectury.platform.Platform;
import dev.architectury.utils.GameInstance;
import fr.frinn.custommachinery.CustomMachinery;
import fr.frinn.custommachinery.api.ICustomMachineryAPI;
import fr.frinn.custommachinery.common.integration.kubejs.KubeJSIntegration;
import fr.frinn.custommachinery.common.util.CustomJsonReloadListener;
import fr.frinn.custommachinery.common.util.MachineList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import oshi.util.tuples.Triplet;

public class CustomMachineJsonReloadListener extends CustomJsonReloadListener {

    private static final String MAIN_PACKNAME = "main";

    public CustomMachineJsonReloadListener() {
        super("machines");
    }

    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler) {
        ICustomMachineryAPI.INSTANCE.logger().info("Reading Custom Machinery Machines...");
        CustomMachinery.MACHINES.clear();
        List<Triplet<ResourceLocation, ResourceLocation, JsonObject>> upgradedMachines = new ArrayList();
        map.forEach((idx, json) -> {
            MachineLocation location = this.getMachineLocation(resourceManager, idx);
            ICustomMachineryAPI.INSTANCE.logger().info("Parsing machine json: {} in datapack: {}", idx, location.getPackName());
            if (!json.isJsonObject()) {
                ICustomMachineryAPI.INSTANCE.logger().error("Bad machine JSON: {} must be a json object and not an array or primitive, skipping...", idx);
            } else {
                JsonObject jsonObject = (JsonObject) json;
                if (CustomMachinery.MACHINES.containsKey(idx)) {
                    ICustomMachineryAPI.INSTANCE.logger().error("A machine with id: {} already exists, skipping...", idx);
                } else if (jsonObject.has("parent") && jsonObject.get("parent").isJsonPrimitive() && jsonObject.getAsJsonPrimitive("parent").isString()) {
                    String parentx = jsonObject.getAsJsonPrimitive("parent").getAsString();
                    try {
                        ResourceLocation parentID = new ResourceLocation(parentx);
                        if (map.containsKey(parentID)) {
                            upgradedMachines.add(new Triplet(parentID, idx, jsonObject));
                        } else {
                            ICustomMachineryAPI.INSTANCE.logger().error("Upgraded machine '{}' reference parent machine '{}' which doesn't exist, skipping", idx, parentID);
                        }
                    } catch (ResourceLocationException var10x) {
                        ICustomMachineryAPI.INSTANCE.logger().error("Invalid parent ID '{}' in machine json '{}', skipping...\n{}", parentx, idx, var10x.getMessage());
                    }
                } else {
                    DataResult<CustomMachine> resultx = CustomMachine.CODEC.read(JsonOps.INSTANCE, json);
                    if (resultx.result().isPresent()) {
                        CustomMachine machinex = (CustomMachine) resultx.result().get();
                        machinex.setLocation(location);
                        CustomMachinery.MACHINES.put(idx, machinex);
                        ICustomMachineryAPI.INSTANCE.logger().info("Successfully parsed machine json: {}", idx);
                    } else if (resultx.error().isPresent()) {
                        ICustomMachineryAPI.INSTANCE.logger().error("Error while parsing machine json: {}, skipping...\n{}", idx, ((PartialResult) resultx.error().get()).message());
                    } else {
                        throw new IllegalStateException("No success nor error when parsing machine json: " + idx + ". This can't happen.");
                    }
                }
            }
        });
        while (!upgradedMachines.isEmpty()) {
            Iterator<Triplet<ResourceLocation, ResourceLocation, JsonObject>> iterator = upgradedMachines.iterator();
            while (iterator.hasNext()) {
                Triplet<ResourceLocation, ResourceLocation, JsonObject> triplet = (Triplet<ResourceLocation, ResourceLocation, JsonObject>) iterator.next();
                CustomMachine parent = (CustomMachine) CustomMachinery.MACHINES.get(triplet.getA());
                if (parent == null) {
                    upgradedMachines.stream().filter(t -> ((ResourceLocation) t.getB()).equals(triplet.getA())).findFirst().ifPresentOrElse(t -> {
                        if (((ResourceLocation) t.getA()).equals(triplet.getB())) {
                            ICustomMachineryAPI.INSTANCE.logger().error("Circular reference in upgraded machines '{}' and '{}' both referencing each other as parent", triplet.getB(), t.getA());
                            iterator.remove();
                        }
                    }, () -> {
                        ICustomMachineryAPI.INSTANCE.logger().error("Upgraded machine '{}' reference parent machine '{}' which doesn't exist, skipping", triplet.getB(), triplet.getA());
                        iterator.remove();
                    });
                } else {
                    ResourceLocation id = (ResourceLocation) triplet.getB();
                    DataResult<UpgradedCustomMachine> result = UpgradedCustomMachine.makeCodec(parent).read(JsonOps.INSTANCE, (JsonElement) triplet.getC());
                    if (result.result().isPresent()) {
                        CustomMachine machine = (CustomMachine) result.result().get();
                        machine.setLocation(this.getMachineLocation(resourceManager, id));
                        CustomMachinery.MACHINES.put(id, machine);
                        ICustomMachineryAPI.INSTANCE.logger().info("Successfully parsed machine json: {}", id);
                    } else if (result.error().isPresent()) {
                        ICustomMachineryAPI.INSTANCE.logger().error("Error while parsing machine json: {}, skipping...\n{}", id, ((PartialResult) result.error().get()).message());
                    }
                    iterator.remove();
                }
            }
        }
        ICustomMachineryAPI.INSTANCE.logger().info("Finished creating {} custom machines.", CustomMachinery.MACHINES.keySet().size());
        if (GameInstance.getServer() != null) {
            MachineList.setNeedRefresh();
        }
    }

    private MachineLocation getMachineLocation(ResourceManager resourceManager, ResourceLocation id) {
        ResourceLocation path = new ResourceLocation(id.getNamespace(), "machines/" + id.getPath() + ".json");
        try {
            Resource res = resourceManager.m_215593_(path);
            String packName = res.sourcePackId();
            if (packName.equals("main")) {
                return MachineLocation.fromDefault(id, packName);
            } else if (packName.contains("KubeJS") && Platform.isModLoaded("kubejs")) {
                return KubeJSIntegration.getMachineLocation(res, packName, id);
            } else {
                MachineLocation var13;
                try (PackResources pack = res.source()) {
                    if (!pack.isBuiltin()) {
                        if (pack instanceof FilePackResources) {
                            return MachineLocation.fromDatapackZip(id, packName);
                        }
                        if (pack instanceof PathPackResources) {
                            return MachineLocation.fromDatapack(id, packName);
                        }
                        return MachineLocation.fromDefault(id, "main");
                    }
                    var13 = MachineLocation.fromDefault(id, packName);
                }
                return var13;
            }
        } catch (IOException var11) {
            return MachineLocation.fromDefault(id, "main");
        }
    }
}