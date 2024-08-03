package de.keksuccino.konkrete.json.jsonpath.internal;

import de.keksuccino.konkrete.json.jsonpath.Configuration;
import de.keksuccino.konkrete.json.jsonpath.Option;
import de.keksuccino.konkrete.json.jsonpath.spi.json.GsonJsonProvider;
import de.keksuccino.konkrete.json.jsonpath.spi.json.JsonProvider;
import de.keksuccino.konkrete.json.jsonpath.spi.mapper.GsonMappingProvider;
import de.keksuccino.konkrete.json.jsonpath.spi.mapper.MappingProvider;
import java.util.EnumSet;
import java.util.Set;

public final class DefaultsImpl implements Configuration.Defaults {

    public static final DefaultsImpl INSTANCE = new DefaultsImpl();

    private final MappingProvider mappingProvider = new GsonMappingProvider();

    @Override
    public JsonProvider jsonProvider() {
        return new GsonJsonProvider();
    }

    @Override
    public Set<Option> options() {
        return EnumSet.noneOf(Option.class);
    }

    @Override
    public MappingProvider mappingProvider() {
        return this.mappingProvider;
    }

    private DefaultsImpl() {
    }
}