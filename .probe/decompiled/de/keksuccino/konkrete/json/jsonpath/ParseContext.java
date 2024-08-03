package de.keksuccino.konkrete.json.jsonpath;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public interface ParseContext {

    DocumentContext parse(String var1);

    DocumentContext parse(Object var1);

    DocumentContext parse(InputStream var1);

    DocumentContext parse(InputStream var1, String var2);

    DocumentContext parse(File var1) throws IOException;

    DocumentContext parseUtf8(byte[] var1);

    @Deprecated
    DocumentContext parse(URL var1) throws IOException;
}