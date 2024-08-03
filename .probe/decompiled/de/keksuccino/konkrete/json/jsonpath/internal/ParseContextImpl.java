package de.keksuccino.konkrete.json.jsonpath.internal;

import de.keksuccino.konkrete.json.jsonpath.Configuration;
import de.keksuccino.konkrete.json.jsonpath.DocumentContext;
import de.keksuccino.konkrete.json.jsonpath.ParseContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ParseContextImpl implements ParseContext {

    private final Configuration configuration;

    public ParseContextImpl() {
        this(Configuration.defaultConfiguration());
    }

    public ParseContextImpl(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public DocumentContext parse(Object json) {
        Utils.notNull(json, "json object can not be null");
        return new JsonContext(json, this.configuration);
    }

    @Override
    public DocumentContext parse(String json) {
        Utils.notEmpty(json, "json string can not be null or empty");
        Object obj = this.configuration.jsonProvider().parse(json);
        return new JsonContext(obj, this.configuration);
    }

    @Override
    public DocumentContext parseUtf8(byte[] json) {
        Utils.notEmpty(json, "json bytes can not be null or empty");
        Object obj = this.configuration.jsonProvider().parse(json);
        return new JsonContext(obj, this.configuration);
    }

    @Override
    public DocumentContext parse(InputStream json) {
        return this.parse(json, "UTF-8");
    }

    @Override
    public DocumentContext parse(InputStream json, String charset) {
        Utils.notNull(json, "json input stream can not be null");
        Utils.notNull(charset, "charset can not be null");
        JsonContext var4;
        try {
            Object obj = this.configuration.jsonProvider().parse(json, charset);
            var4 = new JsonContext(obj, this.configuration);
        } finally {
            Utils.closeQuietly(json);
        }
        return var4;
    }

    @Override
    public DocumentContext parse(File json) throws IOException {
        Utils.notNull(json, "json file can not be null");
        FileInputStream fis = null;
        DocumentContext var3;
        try {
            fis = new FileInputStream(json);
            var3 = this.parse((InputStream) fis);
        } finally {
            Utils.closeQuietly(fis);
        }
        return var3;
    }

    @Deprecated
    @Override
    public DocumentContext parse(URL url) throws IOException {
        Utils.notNull(url, "url can not be null");
        InputStream fis = null;
        DocumentContext var3;
        try {
            fis = url.openStream();
            var3 = this.parse(fis);
        } finally {
            Utils.closeQuietly(fis);
        }
        return var3;
    }
}