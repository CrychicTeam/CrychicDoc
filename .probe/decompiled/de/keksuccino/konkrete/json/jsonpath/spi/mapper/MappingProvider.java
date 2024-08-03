package de.keksuccino.konkrete.json.jsonpath.spi.mapper;

import de.keksuccino.konkrete.json.jsonpath.Configuration;
import de.keksuccino.konkrete.json.jsonpath.TypeRef;

public interface MappingProvider {

    <T> T map(Object var1, Class<T> var2, Configuration var3);

    <T> T map(Object var1, TypeRef<T> var2, Configuration var3);
}