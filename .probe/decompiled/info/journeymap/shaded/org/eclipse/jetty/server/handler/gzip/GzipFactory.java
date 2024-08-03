package info.journeymap.shaded.org.eclipse.jetty.server.handler.gzip;

import info.journeymap.shaded.org.eclipse.jetty.server.Request;
import java.util.zip.Deflater;

public interface GzipFactory {

    Deflater getDeflater(Request var1, long var2);

    boolean isMimeTypeGzipable(String var1);

    void recycle(Deflater var1);
}