package org.embeddedt.modernfix.forge.classloading;

public class ClassLoadHack {

    private static final String[] classesToLoadEarly = new String[] { "team.creative.creativecore.common.config.ConfigTypeConveration", "team.creative.creativecore.common.util.ingredient.CreativeIngredient" };

    public static void loadModClasses() {
        for (String clzName : classesToLoadEarly) {
            try {
                Class.forName(clzName);
            } catch (Throwable var5) {
                if (!(var5 instanceof ClassNotFoundException)) {
                    var5.printStackTrace();
                }
            }
        }
    }
}