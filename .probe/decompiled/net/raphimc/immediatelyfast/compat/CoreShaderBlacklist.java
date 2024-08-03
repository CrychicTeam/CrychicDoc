package net.raphimc.immediatelyfast.compat;

import java.util.List;

public class CoreShaderBlacklist {

    private static final List<String> BLACKLIST = List.of("position_color", "position_tex", "position_tex_color", "rendertype_text", "rendertype_text_background", "rendertype_text_background_see_through", "rendertype_text_intensity", "rendertype_text_intensity_see_through", "rendertype_text_see_through", "rendertype_entity_translucent_cull", "rendertype_item_entity_translucent_cull");

    public static boolean isBlacklisted(String name) {
        return BLACKLIST.contains(name);
    }

    public static List<String> getBlacklist() {
        return BLACKLIST;
    }
}