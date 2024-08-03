package me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.serializer;

import me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.error.YAMLException;

public class SerializerException extends YAMLException {

    private static final long serialVersionUID = 2632638197498912433L;

    public SerializerException(String message) {
        super(message);
    }
}