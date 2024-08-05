package github.pitbox46.fightnbtintegration;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import github.pitbox46.fightnbtintegration.mixins.WeaponTypeReloadListenerMixin;
import github.pitbox46.fightnbtintegration.network.SSyncConfig;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCapabilityPresets;

public class Config {

    private static final Logger LOGGER = LogManager.getLogger();

    public static File jsonFile;

    public static Map<String, Config.WeaponSchema> JSON_MAP = new HashMap();

    public static void init(Path folder) {
        jsonFile = new File(getOrCreateDirectory(folder).toFile(), "epicfightnbt.json");
        try {
            if (jsonFile.createNewFile()) {
                Path defaultConfigPath = FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath()).resolve("epicfightnbt.json");
                InputStreamReader defaults = new InputStreamReader(Files.exists(defaultConfigPath, new LinkOption[0]) ? Files.newInputStream(defaultConfigPath) : (InputStream) Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream("assets/fightnbtintegration/epicfightnbt.json")));
                FileOutputStream writer = new FileOutputStream(jsonFile, false);
                int read;
                while ((read = defaults.read()) != -1) {
                    writer.write(read);
                }
                writer.close();
                defaults.close();
            }
        } catch (IOException var5) {
            LOGGER.warn(var5.getMessage());
        }
        readConfig(jsonFile);
    }

    public static Path getOrCreateDirectory(Path path) {
        try {
            return Files.exists(path, new LinkOption[0]) ? path : Files.createDirectory(path);
        } catch (IOException var2) {
            throw new RuntimeException(var2);
        }
    }

    public static SSyncConfig configFileToSSyncConfig() {
        try {
            return new SSyncConfig(new String(Files.readAllBytes(jsonFile.toPath())));
        } catch (IOException var1) {
            var1.printStackTrace();
            return null;
        }
    }

    public static void readConfig(String config) {
        JSON_MAP = flattenMap((Map<String, Map<String, Config.WeaponSchema>>) new Gson().fromJson(config, (new TypeToken<Map<String, Map<String, Config.WeaponSchema>>>() {
        }).getType()));
    }

    public static void readConfig(File path) {
        try {
            Reader reader = new FileReader(path);
            try {
                JSON_MAP = flattenMap((Map<String, Map<String, Config.WeaponSchema>>) new Gson().fromJson(reader, (new TypeToken<Map<String, Map<String, Config.WeaponSchema>>>() {
                }).getType()));
            } catch (Throwable var5) {
                try {
                    reader.close();
                } catch (Throwable var4) {
                    var5.addSuppressed(var4);
                }
                throw var5;
            }
            reader.close();
        } catch (IOException var6) {
            var6.printStackTrace();
            JSON_MAP = new HashMap();
        }
    }

    protected static Map<String, Config.WeaponSchema> flattenMap(Map<String, Map<String, Config.WeaponSchema>> map) {
        Map<String, Config.WeaponSchema> flatMap = new HashMap();
        map.forEach((topKey, bottomMap) -> bottomMap.forEach((bottomKey, schema) -> flatMap.put(topKey + bottomKey, schema)));
        return flatMap;
    }

    public static CapabilityItem findWeaponByNBT(ItemStack stack) {
        if (stack.hasTag()) {
            CompoundTag tag = stack.getTag();
            for (String key : tag.getAllKeys()) {
                key = key + tag.getString(key);
                Config.WeaponSchema schema = (Config.WeaponSchema) JSON_MAP.get(key);
                if (schema != null) {
                    ResourceLocation weaponType = schema.weapon_type.contains(":") ? new ResourceLocation(schema.weapon_type) : new ResourceLocation("epicfight", schema.weapon_type);
                    if (WeaponTypeReloadListenerMixin.getPRESETS().containsKey(weaponType)) {
                        CapabilityItem toReturn = ((CapabilityItem.Builder) ((Function) WeaponTypeReloadListenerMixin.getPRESETS().getOrDefault(weaponType, WeaponCapabilityPresets.SWORD)).apply(stack.getItem())).build();
                        toReturn.setConfigFileAttribute(schema.armor_ignorance, schema.impact, schema.hit_at_once, schema.armor_ignorance, schema.impact, schema.hit_at_once);
                        return toReturn;
                    }
                }
            }
        }
        return CapabilityItem.EMPTY;
    }

    public static class WeaponSchema {

        public double armor_ignorance = 0.0;

        public int hit_at_once = 0;

        public double impact = 0.0;

        public String weapon_type = "sword";
    }
}