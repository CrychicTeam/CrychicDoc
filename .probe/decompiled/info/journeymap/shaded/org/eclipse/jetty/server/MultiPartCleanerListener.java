package info.journeymap.shaded.org.eclipse.jetty.server;

import info.journeymap.shaded.org.eclipse.jetty.server.handler.ContextHandler;
import info.journeymap.shaded.org.eclipse.jetty.util.MultiException;
import info.journeymap.shaded.org.eclipse.jetty.util.MultiPartInputStreamParser;
import info.journeymap.shaded.org.javax.servlet.ServletRequestEvent;
import info.journeymap.shaded.org.javax.servlet.ServletRequestListener;

public class MultiPartCleanerListener implements ServletRequestListener {

    public static final MultiPartCleanerListener INSTANCE = new MultiPartCleanerListener();

    protected MultiPartCleanerListener() {
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        MultiPartInputStreamParser mpis = (MultiPartInputStreamParser) sre.getServletRequest().getAttribute("info.journeymap.shaded.org.eclipse.jetty.multiPartInputStream");
        if (mpis != null) {
            ContextHandler.Context context = (ContextHandler.Context) sre.getServletRequest().getAttribute("info.journeymap.shaded.org.eclipse.jetty.multiPartContext");
            if (context == sre.getServletContext()) {
                try {
                    mpis.deleteParts();
                } catch (MultiException var5) {
                    sre.getServletContext().log("Errors deleting multipart tmp files", var5);
                }
            }
        }
    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
    }
}