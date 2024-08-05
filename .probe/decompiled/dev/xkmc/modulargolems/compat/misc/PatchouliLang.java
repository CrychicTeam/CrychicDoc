package dev.xkmc.modulargolems.compat.misc;

import com.tterrag.registrate.providers.RegistrateLangProvider;

public enum PatchouliLang {

    TITLE("title", "Modular Golem Guide"), LANDING("landing", "Welcome to Tinker-like golem assembly and upgrade mod");

    private final String key;

    private final String def;

    private PatchouliLang(String key, String def) {
        this.key = "patchouli.modulargolems." + key;
        this.def = def;
    }

    public static void genLang(RegistrateLangProvider pvd) {
        for (PatchouliLang lang : values()) {
            pvd.add(lang.key, lang.def);
        }
    }
}