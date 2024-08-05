package dev.xkmc.l2artifacts.compat;

import com.tterrag.registrate.providers.RegistrateLangProvider;

public enum PatchouliLang {

    TITLE("title", "L2Artifacts Guide"), LANDING("landing", "Welcome to L2Artifact");

    private final String key;

    private final String def;

    private PatchouliLang(String key, String def) {
        this.key = "patchouli.l2artifacts." + key;
        this.def = def;
    }

    public static void genLang(RegistrateLangProvider pvd) {
        for (PatchouliLang lang : values()) {
            pvd.add(lang.key, lang.def);
        }
    }
}