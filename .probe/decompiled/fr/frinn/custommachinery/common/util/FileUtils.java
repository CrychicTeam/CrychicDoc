package fr.frinn.custommachinery.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonWriter;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.DataResult.PartialResult;
import dev.architectury.platform.Platform;
import fr.frinn.custommachinery.CustomMachinery;
import fr.frinn.custommachinery.common.machine.CustomMachine;
import fr.frinn.custommachinery.common.machine.MachineLocation;
import fr.frinn.custommachinery.common.network.SUpdateMachinesPacket;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class FileUtils {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void writeNewMachineJson(MinecraftServer server, CustomMachine machine, boolean kubejs) {
        if (kubejs && !Platform.isModLoaded("kubejs")) {
            CustomMachinery.LOGGER.error("Can't write new machine json {} in kubejs data folder because KubeJS isn't present", machine.getId());
        } else {
            DataResult<JsonElement> result = CustomMachine.CODEC.encodeStart(JsonOps.INSTANCE, machine);
            if (result.error().isPresent()) {
                CustomMachinery.LOGGER.error("Can't write new machine json: {}\n{}", machine.getId().getPath(), ((PartialResult) result.error().get()).message());
            } else {
                if (result.result().isPresent()) {
                    JsonElement json = (JsonElement) result.result().get();
                    String root = server.getServerDirectory().getAbsolutePath();
                    root = root.substring(0, root.length() - 2);
                    if (kubejs) {
                        root = root + File.separator + "kubejs" + File.separator + "data" + File.separator + machine.getId().getNamespace() + File.separator + "machines";
                    }
                    File file = new File(root, machine.getId().getPath() + ".json");
                    CustomMachinery.LOGGER.info("Writing new machine: {} in {}", machine.getLocation().getId(), file.getPath());
                    try {
                        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
                            CustomMachinery.LOGGER.error("Can't create directory for '{}'", file.getParentFile().getAbsolutePath());
                        }
                        if (!file.exists() && !file.createNewFile()) {
                            CustomMachinery.LOGGER.error("Can't write new machine file in '{}'", file.getAbsolutePath());
                        } else {
                            JsonWriter writer = GSON.newJsonWriter(new FileWriter(file));
                            GSON.toJson(json, writer);
                            writer.close();
                            if (kubejs) {
                                CustomMachinery.MACHINES.put(machine.getId(), machine);
                                MachineList.setNeedRefresh();
                                new SUpdateMachinesPacket(CustomMachinery.MACHINES).sendToAll(server);
                            }
                        }
                    } catch (IOException var8) {
                        CustomMachinery.LOGGER.error("Error while writing new machine to file: {}\n{}\n{}", file.getAbsolutePath(), var8.getMessage(), ExceptionUtils.getStackTrace(var8));
                    }
                }
            }
        }
    }

    public static void writeMachineJson(MinecraftServer server, CustomMachine machine) {
        MachineLocation location = machine.getLocation();
        File machineJson = location.getFile(server);
        if (machineJson == null) {
            CustomMachinery.LOGGER.error("Error while editing machine: {}\nCan't edit machine loaded with {}", location.getId(), location.getLoader().toString());
        } else if (machineJson.exists() && !machineJson.isDirectory()) {
            try {
                JsonWriter writer = GSON.newJsonWriter(new FileWriter(machineJson));
                label68: {
                    try {
                        DataResult<JsonElement> result = CustomMachine.CODEC.encodeStart(MachineJsonOps.INSTANCE, machine);
                        if (result.error().isPresent()) {
                            CustomMachinery.LOGGER.error("Can't edit machine json: {}\n{}", machine.getId().getPath(), ((PartialResult) result.error().get()).message());
                            break label68;
                        }
                        if (result.result().isPresent()) {
                            JsonElement json = (JsonElement) result.result().get();
                            GSON.toJson(json, writer);
                            CustomMachinery.LOGGER.info("Successfully edited machine: {} at location '{}'", location.getId(), machineJson.getAbsolutePath());
                        }
                    } catch (Throwable var8) {
                        if (writer != null) {
                            try {
                                writer.close();
                            } catch (Throwable var7) {
                                var8.addSuppressed(var7);
                            }
                        }
                        throw var8;
                    }
                    if (writer != null) {
                        writer.close();
                    }
                    return;
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException var9) {
                CustomMachinery.LOGGER.error("Error while editing machine to file: {}\n{}\n{}", machineJson.getAbsolutePath(), var9.getMessage(), ExceptionUtils.getStackTrace(var9));
            }
        } else {
            CustomMachinery.LOGGER.error("Error while editing machine: {}\nFile '{}' doesn't exist", location.getId(), machineJson.getAbsolutePath());
        }
    }

    public static void deleteMachineJson(MinecraftServer server, MachineLocation location) {
        File machineJson = location.getFile(server);
        if (machineJson == null) {
            CustomMachinery.LOGGER.error("Error while deleting machine: {}\nCan't delete machine loaded with {}", location.getId(), location.getLoader().toString());
        } else if (!machineJson.exists() || machineJson.isDirectory()) {
            CustomMachinery.LOGGER.error("Error while deleting machine: {}\nFile '{}' doesn't exist", location.getId(), machineJson.getAbsolutePath());
        } else if (!machineJson.delete()) {
            CustomMachinery.LOGGER.error("Error while deleting machine: {}\nFile '{}' can't be deleted", location.getId(), machineJson.getAbsolutePath());
        } else {
            CustomMachinery.LOGGER.info("Successfully deleted machine: {} at location '{}'", location.getId(), machineJson.getAbsolutePath());
        }
    }
}