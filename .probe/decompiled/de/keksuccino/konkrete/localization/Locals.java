package de.keksuccino.konkrete.localization;

import com.google.common.io.Files;
import de.keksuccino.konkrete.file.FileUtils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

public class Locals {

    private static Map<String, LocalizationPackage> locals = new HashMap();

    public static void getLocalsFromDir(String dir) {
        File f = new File(dir);
        if (f.exists() && f.isDirectory()) {
            for (File f2 : f.listFiles()) {
                getLocalsFromFile(f2);
            }
        }
    }

    public static void getLocalsFromFile(String file) {
        File f = new File(file);
        getLocalsFromFile(f);
    }

    public static void getLocalsFromFile(File f) {
        if (f.exists() && f.isFile() && f.getName().toLowerCase().endsWith(".local")) {
            String language = Files.getNameWithoutExtension(f.getPath());
            LocalizationPackage p;
            if (locals.containsKey(language)) {
                p = (LocalizationPackage) locals.get(language);
            } else {
                p = new LocalizationPackage(language);
            }
            for (String s : FileUtils.getFileLines(f)) {
                if (s.contains("=")) {
                    String key = s.split("[=]", 2)[0].replace(" ", "");
                    String value = s.split("[=]", 2)[1];
                    if (value.startsWith(" ")) {
                        value = value.substring(1);
                    }
                    if (!p.containsKey(key)) {
                        p.addLocalizedString(key, value);
                    } else {
                        System.out.println("################ [KONKRETE] ERROR ################");
                        System.out.println("FAILED TO REGISTER LOCALIZATION KEY!");
                        System.out.println("Key already exists! (Key: " + key + " ; Language: " + language + " ; Value: " + p.getLocalizedString(key) + ")");
                        System.out.println("##################################################");
                    }
                }
            }
            if (!p.isEmpty() && !locals.containsKey(language)) {
                locals.put(language, p);
            }
        }
    }

    public static void copyLocalsFileToDir(ResourceLocation file, String language, String saveDirWithoutFilename) {
        File lang = new File(saveDirWithoutFilename + "/" + language + ".local");
        if (lang.exists()) {
            lang.delete();
        }
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            try {
                br = new BufferedReader(new InputStreamReader(Minecraft.getInstance().getResourceManager().m_215595_(file), StandardCharsets.UTF_8));
                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(lang, false), StandardCharsets.UTF_8));
                String full = "";
                for (String line = br.readLine(); line != null; line = br.readLine()) {
                    full = full + line + "\n";
                }
                bw.write(full);
                bw.flush();
            } finally {
                bw.close();
                br.close();
            }
        } catch (Exception var12) {
            var12.printStackTrace();
        }
    }

    public static String localizeTo(String key, String language, String... dynamicValues) {
        LocalizationPackage p = getPackage(language);
        if (p == null) {
            return key;
        } else {
            String rawLocal = p.getLocalizedString(key);
            if (rawLocal == null) {
                return !language.equals("en_us") ? localizeTo(key, "en_us", dynamicValues) : key;
            } else {
                String local = rawLocal;
                for (String s : dynamicValues) {
                    if (!local.contains("{}")) {
                        break;
                    }
                    local = local.replaceFirst("[{][}]", s);
                }
                return local;
            }
        }
    }

    public static String localize(String key, String... dynamicValues) {
        String playerLang = Minecraft.getInstance().options.languageCode;
        return locals.containsKey(playerLang) ? localizeTo(key, playerLang, dynamicValues) : localizeTo(key, "en_us", dynamicValues);
    }

    public static LocalizationPackage getPackage(String language) {
        return locals.containsKey(language) ? (LocalizationPackage) locals.get(language) : null;
    }
}