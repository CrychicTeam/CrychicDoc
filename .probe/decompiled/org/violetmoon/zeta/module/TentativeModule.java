package org.violetmoon.zeta.module;

import java.util.Locale;
import java.util.Set;
import java.util.function.Function;
import org.apache.commons.lang3.text.WordUtils;
import org.violetmoon.zeta.util.ZetaSide;

public record TentativeModule(Class<? extends ZetaModule> clazz, Class<? extends ZetaModule> keyClass, ZetaCategory category, String displayName, String lowercaseName, String description, Set<String> antiOverlap, boolean enabledByDefault, boolean clientReplacement, int loadPhase) {

    public static TentativeModule from(ZetaLoadModuleAnnotationData data, Function<String, ZetaCategory> categoryResolver) {
        Class<?> clazzUnchecked = data.clazz();
        if (!ZetaModule.class.isAssignableFrom(clazzUnchecked)) {
            throw new RuntimeException("Class " + clazzUnchecked.getName() + " does not extend ZetaModule");
        } else {
            String displayName;
            if (data.name().isEmpty()) {
                displayName = WordUtils.capitalizeFully(clazzUnchecked.getSimpleName().replaceAll("Module$", "").replaceAll("(?<=.)([A-Z])", " $1"));
            } else {
                displayName = data.name();
            }
            String lowercaseName = displayName.toLowerCase(Locale.ROOT).replace(" ", "_");
            boolean clientReplacement = data.clientReplacement();
            Class<? extends ZetaModule> keyClass;
            if (clientReplacement) {
                Class<?> sup = clazzUnchecked.getSuperclass();
                if (!ZetaModule.class.isAssignableFrom(sup) || ZetaModule.class == sup) {
                    throw new RuntimeException("Client extension module " + clazzUnchecked.getName() + " should `extend` the module it's an extension of");
                }
                keyClass = clazzUnchecked.getSuperclass();
            } else {
                keyClass = (Class<? extends ZetaModule>) clazzUnchecked;
                if (data.category() == null || data.category().isEmpty()) {
                    throw new RuntimeException("Module " + clazzUnchecked.getName() + " should specify a category");
                }
            }
            return new TentativeModule((Class<? extends ZetaModule>) clazzUnchecked, keyClass, (ZetaCategory) categoryResolver.apply(data.category()), displayName, lowercaseName, data.description(), Set.of(data.antiOverlap()), data.enabledByDefault(), clientReplacement, data.loadPhase());
        }
    }

    public TentativeModule replaceWith(TentativeModule replacement) {
        return new TentativeModule(replacement.clazz, this.keyClass, this.category, this.displayName, this.lowercaseName, this.description, this.antiOverlap, this.enabledByDefault, false, replacement.loadPhase);
    }

    public boolean appliesTo(ZetaSide side) {
        return switch(side) {
            case CLIENT ->
                true;
            case SERVER ->
                !this.clientReplacement;
        };
    }
}