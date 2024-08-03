package de.keksuccino.konkrete.json.jsonpath.spi.mapper;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import de.keksuccino.konkrete.json.jsonpath.Configuration;
import de.keksuccino.konkrete.json.jsonpath.JsonPathException;
import de.keksuccino.konkrete.json.jsonpath.TypeRef;
import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GsonMappingProvider implements MappingProvider {

    private static final Logger logger = LoggerFactory.getLogger(GsonMappingProvider.class);

    private final Callable<Gson> factory;

    public GsonMappingProvider(final Gson gson) {
        this(new Callable<Gson>() {

            public Gson call() {
                return gson;
            }
        });
    }

    public GsonMappingProvider(Callable<Gson> factory) {
        this.factory = factory;
    }

    public GsonMappingProvider() {
        try {
            Class.forName("com.google.gson.Gson");
            this.factory = new Callable<Gson>() {

                public Gson call() {
                    return new Gson();
                }
            };
        } catch (ClassNotFoundException var2) {
            logger.error("Gson not found on class path. No converters configured.");
            throw new JsonPathException("Gson not found on path", var2);
        }
    }

    @Override
    public <T> T map(Object source, Class<T> targetType, Configuration configuration) {
        if (source == null) {
            return null;
        } else {
            try {
                return (T) ((Gson) this.factory.call()).getAdapter(targetType).fromJsonTree((JsonElement) source);
            } catch (Exception var5) {
                throw new MappingException(var5);
            }
        }
    }

    @Override
    public <T> T map(Object source, TypeRef<T> targetType, Configuration configuration) {
        if (source == null) {
            return null;
        } else {
            try {
                return (T) ((Gson) this.factory.call()).getAdapter(TypeToken.get(targetType.getType())).fromJsonTree((JsonElement) source);
            } catch (Exception var5) {
                throw new MappingException(var5);
            }
        }
    }
}