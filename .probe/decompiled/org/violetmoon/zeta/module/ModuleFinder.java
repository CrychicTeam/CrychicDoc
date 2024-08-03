package org.violetmoon.zeta.module;

import java.util.function.Supplier;
import java.util.stream.Stream;

public interface ModuleFinder extends Supplier<Stream<ZetaLoadModuleAnnotationData>> {

    default ModuleFinder and(ModuleFinder other) {
        return () -> Stream.concat((Stream) this.get(), (Stream) other.get());
    }
}