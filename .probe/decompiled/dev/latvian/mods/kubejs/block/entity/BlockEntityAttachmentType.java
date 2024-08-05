package dev.latvian.mods.kubejs.block.entity;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.typings.desc.ObjectDescJS;
import dev.latvian.mods.kubejs.util.KubeJSPlugins;
import dev.latvian.mods.kubejs.util.Lazy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public record BlockEntityAttachmentType(String type, ObjectDescJS input, Function<Map<String, Object>, BlockEntityAttachment.Factory> factory) {

    public static final Lazy<Map<String, BlockEntityAttachmentType>> ALL = Lazy.of(() -> {
        HashMap<String, BlockEntityAttachmentType> map = new HashMap();
        ArrayList<BlockEntityAttachmentType> list = new ArrayList();
        KubeJSPlugins.forEachPlugin(list, KubeJSPlugin::registerBlockEntityAttachments);
        for (BlockEntityAttachmentType type : list) {
            map.put(type.type, type);
        }
        return map;
    });

    public String toString() {
        return this.type;
    }
}