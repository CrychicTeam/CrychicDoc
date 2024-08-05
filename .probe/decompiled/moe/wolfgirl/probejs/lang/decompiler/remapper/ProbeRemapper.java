package moe.wolfgirl.probejs.lang.decompiler.remapper;

import dev.latvian.mods.rhino.mod.util.RhinoProperties;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;
import moe.wolfgirl.probejs.ProbeJS;
import org.jetbrains.java.decompiler.main.extern.IIdentifierRenamer;
import org.jetbrains.java.decompiler.main.extern.IIdentifierRenamer.Type;

public class ProbeRemapper implements IIdentifierRenamer {

    public final Map<String, String> srgClasses = new HashMap();

    public final Map<String, String> srgMethods = new HashMap();

    public final Map<String, String> srgFields = new HashMap();

    public ProbeRemapper() {
        Path configPath = RhinoProperties.getGameDir().resolve("config/mm.jsmappings");
        if (Files.exists(configPath, new LinkOption[0])) {
            try {
                BufferedInputStream in = new BufferedInputStream(new GZIPInputStream((InputStream) Objects.requireNonNull(Files.newInputStream(configPath))));
                try {
                    this.loadFromRemapped(RemappedClass.loadFrom(in));
                } catch (Throwable var9) {
                    try {
                        in.close();
                    } catch (Throwable var6) {
                        var9.addSuppressed(var6);
                    }
                    throw var9;
                }
                in.close();
            } catch (Exception var10) {
                ProbeJS.LOGGER.error("Failed to load Rhino Minecraft remapper.");
            }
        } else {
            try {
                BufferedInputStream in = new BufferedInputStream(new GZIPInputStream((InputStream) Objects.requireNonNull(RhinoProperties.openResource("mm.jsmappings"))));
                try {
                    this.loadFromRemapped(RemappedClass.loadFrom(in));
                } catch (Throwable var7) {
                    try {
                        in.close();
                    } catch (Throwable var5) {
                        var7.addSuppressed(var5);
                    }
                    throw var7;
                }
                in.close();
            } catch (Exception var8) {
                ProbeJS.LOGGER.error("Failed to load Rhino Minecraft remapper.");
            }
        }
    }

    public void loadFromRemapped(Map<String, RemappedClass> remapped) {
        for (Entry<String, RemappedClass> entry : remapped.entrySet()) {
            String name = (String) entry.getKey();
            RemappedClass remappedClass = (RemappedClass) entry.getValue();
            if (!name.equals(remappedClass.remappedName)) {
                this.srgClasses.put(name, remappedClass.remappedName);
            }
            if (remappedClass.fields != null) {
                this.srgFields.putAll(remappedClass.fields);
            }
            if (remappedClass.emptyMethods != null) {
                for (Entry<String, String> e : remappedClass.emptyMethods.entrySet()) {
                    String method = (String) e.getKey();
                    String remappedMethod = (String) e.getValue();
                    this.srgMethods.put(method + "(", remappedMethod);
                }
            }
            if (remappedClass.methods != null) {
                for (Entry<String, String> e : remappedClass.methods.entrySet()) {
                    String method = (String) e.getKey();
                    String remappedMethod = (String) e.getValue();
                    this.srgMethods.put(method, remappedMethod);
                }
            }
        }
    }

    public boolean toBeRenamed(Type elementType, String className, String element, String descriptor) {
        className = className.replace("/", ".");
        return switch(elementType) {
            case ELEMENT_CLASS ->
                this.srgClasses.containsKey(className);
            case ELEMENT_FIELD ->
                this.srgFields.containsKey(element);
            case ELEMENT_METHOD ->
                this.srgMethods.containsKey(element + descriptor.split("\\)", 2)[0]);
            default ->
                throw new IncompatibleClassChangeError();
        };
    }

    public String getNextClassName(String fullName, String shortName) {
        fullName = fullName.replace("/", ".");
        String[] parts = ((String) this.srgClasses.get(fullName)).split("\\.");
        return parts[parts.length - 1];
    }

    public String getNextFieldName(String className, String field, String descriptor) {
        return (String) this.srgFields.get(field);
    }

    public String getNextMethodName(String className, String method, String descriptor) {
        return (String) this.srgMethods.get(method + descriptor.split("\\)", 2)[0]);
    }
}