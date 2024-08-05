package info.journeymap.shaded.org.eclipse.jetty.util.resource;

import info.journeymap.shaded.org.eclipse.jetty.util.IO;
import info.journeymap.shaded.org.eclipse.jetty.util.URIUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

public class JarResource extends URLResource {

    private static final Logger LOG = Log.getLogger(JarResource.class);

    protected JarURLConnection _jarConnection;

    protected JarResource(URL url) {
        super(url, null);
    }

    protected JarResource(URL url, boolean useCaches) {
        super(url, null, useCaches);
    }

    @Override
    public synchronized void close() {
        this._jarConnection = null;
        super.close();
    }

    @Override
    protected synchronized boolean checkConnection() {
        super.checkConnection();
        try {
            if (this._jarConnection != this._connection) {
                this.newConnection();
            }
        } catch (IOException var2) {
            LOG.ignore(var2);
            this._jarConnection = null;
        }
        return this._jarConnection != null;
    }

    protected void newConnection() throws IOException {
        this._jarConnection = (JarURLConnection) this._connection;
    }

    @Override
    public boolean exists() {
        return this._urlString.endsWith("!/") ? this.checkConnection() : super.exists();
    }

    @Override
    public File getFile() throws IOException {
        return null;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        this.checkConnection();
        if (!this._urlString.endsWith("!/")) {
            return new FilterInputStream(this.getInputStream(false)) {

                public void close() throws IOException {
                    this.in = IO.getClosedStream();
                }
            };
        } else {
            URL url = new URL(this._urlString.substring(4, this._urlString.length() - 2));
            return url.openStream();
        }
    }

    @Override
    public void copyTo(File directory) throws IOException {
        if (this.exists()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Extract " + this + " to " + directory);
            }
            String urlString = this.getURL().toExternalForm().trim();
            int endOfJarUrl = urlString.indexOf("!/");
            int startOfJarUrl = endOfJarUrl >= 0 ? 4 : 0;
            if (endOfJarUrl < 0) {
                throw new IOException("Not a valid jar url: " + urlString);
            } else {
                URL jarFileURL = new URL(urlString.substring(startOfJarUrl, endOfJarUrl));
                String subEntryName = endOfJarUrl + 2 < urlString.length() ? urlString.substring(endOfJarUrl + 2) : null;
                boolean subEntryIsDir = subEntryName != null && subEntryName.endsWith("/");
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Extracting entry = " + subEntryName + " from jar " + jarFileURL);
                }
                URLConnection c = jarFileURL.openConnection();
                c.setUseCaches(false);
                InputStream is = c.getInputStream();
                Throwable var10 = null;
                try {
                    JarInputStream jin = new JarInputStream(is);
                    Throwable var12 = null;
                    try {
                        JarEntry entry;
                        while ((entry = jin.getNextJarEntry()) != null) {
                            String entryName = entry.getName();
                            boolean shouldExtract;
                            if (subEntryName != null && entryName.startsWith(subEntryName)) {
                                if (!subEntryIsDir && subEntryName.length() + 1 == entryName.length() && entryName.endsWith("/")) {
                                    subEntryIsDir = true;
                                }
                                if (subEntryIsDir) {
                                    entryName = entryName.substring(subEntryName.length());
                                    if (!entryName.equals("")) {
                                        shouldExtract = true;
                                    } else {
                                        shouldExtract = false;
                                    }
                                } else {
                                    shouldExtract = true;
                                }
                            } else if (subEntryName != null && !entryName.startsWith(subEntryName)) {
                                shouldExtract = false;
                            } else {
                                shouldExtract = true;
                            }
                            if (!shouldExtract) {
                                if (LOG.isDebugEnabled()) {
                                    LOG.debug("Skipping entry: " + entryName);
                                }
                            } else {
                                String dotCheck = entryName.replace('\\', '/');
                                dotCheck = URIUtil.canonicalPath(dotCheck);
                                if (dotCheck == null) {
                                    if (LOG.isDebugEnabled()) {
                                        LOG.debug("Invalid entry: " + entryName);
                                    }
                                } else {
                                    File file = new File(directory, entryName);
                                    if (entry.isDirectory()) {
                                        if (!file.exists()) {
                                            file.mkdirs();
                                        }
                                    } else {
                                        File dir = new File(file.getParent());
                                        if (!dir.exists()) {
                                            dir.mkdirs();
                                        }
                                        OutputStream fout = new FileOutputStream(file);
                                        Throwable var20 = null;
                                        try {
                                            IO.copy(jin, fout);
                                        } catch (Throwable var93) {
                                            var20 = var93;
                                            throw var93;
                                        } finally {
                                            if (fout != null) {
                                                if (var20 != null) {
                                                    try {
                                                        fout.close();
                                                    } catch (Throwable var91) {
                                                        var20.addSuppressed(var91);
                                                    }
                                                } else {
                                                    fout.close();
                                                }
                                            }
                                        }
                                        if (entry.getTime() >= 0L) {
                                            file.setLastModified(entry.getTime());
                                        }
                                    }
                                }
                            }
                        }
                        if (subEntryName == null || subEntryName != null && subEntryName.equalsIgnoreCase("META-INF/MANIFEST.MF")) {
                            Manifest manifest = jin.getManifest();
                            if (manifest != null) {
                                File metaInf = new File(directory, "META-INF");
                                metaInf.mkdir();
                                File f = new File(metaInf, "MANIFEST.MF");
                                OutputStream fout = new FileOutputStream(f);
                                Throwable var105 = null;
                                try {
                                    manifest.write(fout);
                                } catch (Throwable var92) {
                                    var105 = var92;
                                    throw var92;
                                } finally {
                                    if (fout != null) {
                                        if (var105 != null) {
                                            try {
                                                fout.close();
                                            } catch (Throwable var90) {
                                                var105.addSuppressed(var90);
                                            }
                                        } else {
                                            fout.close();
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Throwable var96) {
                        var12 = var96;
                        throw var96;
                    } finally {
                        if (jin != null) {
                            if (var12 != null) {
                                try {
                                    jin.close();
                                } catch (Throwable var89) {
                                    var12.addSuppressed(var89);
                                }
                            } else {
                                jin.close();
                            }
                        }
                    }
                } catch (Throwable var98) {
                    var10 = var98;
                    throw var98;
                } finally {
                    if (is != null) {
                        if (var10 != null) {
                            try {
                                is.close();
                            } catch (Throwable var88) {
                                var10.addSuppressed(var88);
                            }
                        } else {
                            is.close();
                        }
                    }
                }
            }
        }
    }

    public static Resource newJarResource(Resource resource) throws IOException {
        return resource instanceof JarResource ? resource : Resource.newResource("jar:" + resource + "!/");
    }
}