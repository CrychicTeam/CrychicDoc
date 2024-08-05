package dev.xkmc.l2artifacts.content.config;

import dev.xkmc.l2artifacts.content.core.LinearFuncHandle;
import dev.xkmc.l2library.serial.config.BaseConfig;
import dev.xkmc.l2library.serial.config.CollectType;
import dev.xkmc.l2library.serial.config.ConfigCollect;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.HashMap;

@SerialClass
public class LinearFuncConfig extends BaseConfig {

    @SerialField
    @ConfigCollect(CollectType.MAP_OVERWRITE)
    public HashMap<LinearFuncHandle, LinearFuncConfig.Entry> map = new HashMap();

    public static record Entry(double base, double slope) {
    }
}