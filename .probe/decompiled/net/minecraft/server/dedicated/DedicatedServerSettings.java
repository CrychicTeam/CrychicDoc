package net.minecraft.server.dedicated;

import java.nio.file.Path;
import java.util.function.UnaryOperator;

public class DedicatedServerSettings {

    private final Path source;

    private DedicatedServerProperties properties;

    public DedicatedServerSettings(Path path0) {
        this.source = path0;
        this.properties = DedicatedServerProperties.fromFile(path0);
    }

    public DedicatedServerProperties getProperties() {
        return this.properties;
    }

    public void forceSave() {
        this.properties.m_139876_(this.source);
    }

    public DedicatedServerSettings update(UnaryOperator<DedicatedServerProperties> unaryOperatorDedicatedServerProperties0) {
        (this.properties = (DedicatedServerProperties) unaryOperatorDedicatedServerProperties0.apply(this.properties)).m_139876_(this.source);
        return this;
    }
}