package org.violetmoon.zeta.module;

import java.util.Objects;
import java.util.ServiceLoader;
import java.util.stream.Stream;
import org.violetmoon.zeta.Zeta;

public class ServiceLoaderModuleFinder implements ModuleFinder {

    private final Zeta z;

    public ServiceLoaderModuleFinder(Zeta z) {
        this.z = z;
    }

    public Stream<ZetaLoadModuleAnnotationData> get() {
        return ServiceLoader.load(ZetaModule.class).stream().map(provider -> {
            ZetaLoadModule annotation = (ZetaLoadModule) provider.type().getAnnotation(ZetaLoadModule.class);
            if (annotation == null) {
                this.z.log.warn("Module class " + provider.type().getName() + " was found through ServiceLoader, but does not have a @ZetaLoadModule annotation. Skipping");
                return null;
            } else {
                return ZetaLoadModuleAnnotationData.fromAnnotation(provider.type(), annotation);
            }
        }).filter(Objects::nonNull);
    }
}