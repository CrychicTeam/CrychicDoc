package de.keksuccino.konkrete.json.jsonpath.spi.mapper;

import de.keksuccino.konkrete.json.jsonpath.JsonPathException;

public class MappingException extends JsonPathException {

    public MappingException(Throwable cause) {
        super(cause);
    }

    public MappingException(String message) {
        super(message);
    }
}