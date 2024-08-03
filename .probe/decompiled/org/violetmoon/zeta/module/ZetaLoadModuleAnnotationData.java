package org.violetmoon.zeta.module;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record ZetaLoadModuleAnnotationData(Class<?> clazz, String category, String name, String description, String[] antiOverlap, boolean enabledByDefault, boolean clientReplacement, int loadPhase) {

    public static ZetaLoadModuleAnnotationData fromAnnotation(Class<?> clazz, ZetaLoadModule annotation) {
        return new ZetaLoadModuleAnnotationData(clazz, annotation.category(), annotation.name(), annotation.description(), annotation.antiOverlap(), annotation.enabledByDefault(), annotation.clientReplacement(), annotation.loadPhase());
    }

    public static ZetaLoadModuleAnnotationData fromForgeThing(Class<?> clazz, Map<String, Object> data) {
        return new ZetaLoadModuleAnnotationData(clazz, (String) data.get("category"), (String) data.getOrDefault("name", ""), (String) data.getOrDefault("description", ""), (String[]) ((List) data.getOrDefault("antiOverlap", new ArrayList())).toArray(new String[0]), (Boolean) data.getOrDefault("enabledByDefault", true), (Boolean) data.getOrDefault("clientReplacement", false), (Integer) data.getOrDefault("loadPhase", 0));
    }
}