package dev.latvian.mods.rhino.mod.util;

import com.google.gson.JsonElement;
import dev.latvian.mods.rhino.util.RemapForJS;

public interface JsonSerializable {

    @RemapForJS("toJson")
    JsonElement toJsonJS();
}