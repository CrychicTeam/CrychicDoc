package noppes.npcs.shared;

import java.io.File;
import noppes.npcs.CustomNpcs;

public class SharedReferences {

    public static String modid() {
        return "customnpcs";
    }

    public static File dir() {
        return CustomNpcs.Dir;
    }

    public static boolean AllowFullyInvisibleSkins() {
        return true;
    }

    public static boolean VerboseDebug() {
        return CustomNpcs.VerboseDebug;
    }
}