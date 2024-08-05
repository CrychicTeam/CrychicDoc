package net.cristellib.forge.extraapiutil;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.cristellib.CristelLib;
import net.cristellib.api.CristelLibAPI;
import net.cristellib.api.CristelPlugin;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.language.ModFileScanData;
import net.minecraftforge.forgespi.language.ModFileScanData.AnnotationData;
import org.objectweb.asm.Type;

public class APIFinder {

    public static List<Pair<List<String>, CristelLibAPI>> scanForAPIs() {
        List<Pair<List<String>, CristelLibAPI>> instances = Lists.newArrayList();
        for (ModFileScanData data : ModList.get().getAllScanData()) {
            List<AnnotationData> ebsTargets = data.getAnnotations().stream().filter(annotationData -> Type.getType(CristelPlugin.class).equals(annotationData.annotationType())).toList();
            List<String> modIds = data.getIModInfoData().stream().flatMap(info -> info.getMods().stream()).map(IModInfo::getModId).toList();
            for (AnnotationData ad : ebsTargets) {
                Class<CristelLibAPI> clazz;
                try {
                    clazz = Class.forName(ad.memberName());
                } catch (ClassNotFoundException var10) {
                    CristelLib.LOGGER.error("Failed to load api class {} for @CristelPlugin annotation", ad.clazz(), var10);
                    continue;
                }
                try {
                    instances.add(new Pair(modIds, (CristelLibAPI) clazz.getDeclaredConstructor().newInstance()));
                } catch (Throwable var9) {
                    CristelLib.LOGGER.error("Failed to load api: " + ad.memberName(), var9);
                }
            }
        }
        return instances;
    }
}