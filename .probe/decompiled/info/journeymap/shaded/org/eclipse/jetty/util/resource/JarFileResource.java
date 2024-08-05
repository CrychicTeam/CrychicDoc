package info.journeymap.shaded.org.eclipse.jetty.util.resource;

import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

class JarFileResource extends JarResource {

    private static final Logger LOG = Log.getLogger(JarFileResource.class);

    private JarFile _jarFile;

    private File _file;

    private String[] _list;

    private JarEntry _entry;

    private boolean _directory;

    private String _jarUrl;

    private String _path;

    private boolean _exists;

    protected JarFileResource(URL url) {
        super(url);
    }

    protected JarFileResource(URL url, boolean useCaches) {
        super(url, useCaches);
    }

    @Override
    public synchronized void close() {
        this._exists = false;
        this._list = null;
        this._entry = null;
        this._file = null;
        if (!this.getUseCaches() && this._jarFile != null) {
            try {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Closing JarFile " + this._jarFile.getName());
                }
                this._jarFile.close();
            } catch (IOException var2) {
                LOG.ignore(var2);
            }
        }
        this._jarFile = null;
        super.close();
    }

    @Override
    protected synchronized boolean checkConnection() {
        try {
            super.checkConnection();
        } finally {
            if (this._jarConnection == null) {
                this._entry = null;
                this._file = null;
                this._jarFile = null;
                this._list = null;
            }
        }
        return this._jarFile != null;
    }

    @Override
    protected synchronized void newConnection() throws IOException {
        super.newConnection();
        this._entry = null;
        this._file = null;
        this._jarFile = null;
        this._list = null;
        int sep = this._urlString.lastIndexOf("!/");
        this._jarUrl = this._urlString.substring(0, sep + 2);
        this._path = this._urlString.substring(sep + 2);
        if (this._path.length() == 0) {
            this._path = null;
        }
        this._jarFile = this._jarConnection.getJarFile();
        this._file = new File(this._jarFile.getName());
    }

    @Override
    public boolean exists() {
        if (this._exists) {
            return true;
        } else if (this._urlString.endsWith("!/")) {
            String file_url = this._urlString.substring(4, this._urlString.length() - 2);
            try {
                return newResource(file_url).exists();
            } catch (Exception var6) {
                LOG.ignore(var6);
                return false;
            }
        } else {
            boolean check = this.checkConnection();
            if (this._jarUrl != null && this._path == null) {
                this._directory = check;
                return true;
            } else {
                boolean close_jar_file = false;
                JarFile jar_file = null;
                if (check) {
                    jar_file = this._jarFile;
                } else {
                    try {
                        JarURLConnection c = (JarURLConnection) new URL(this._jarUrl).openConnection();
                        c.setUseCaches(this.getUseCaches());
                        jar_file = c.getJarFile();
                        close_jar_file = !this.getUseCaches();
                    } catch (Exception var8) {
                        LOG.ignore(var8);
                    }
                }
                if (jar_file != null && this._entry == null && !this._directory) {
                    JarEntry entry = jar_file.getJarEntry(this._path);
                    if (entry == null) {
                        this._exists = false;
                    } else if (entry.isDirectory()) {
                        this._directory = true;
                        this._entry = entry;
                    } else {
                        JarEntry directory = jar_file.getJarEntry(this._path + '/');
                        if (directory != null) {
                            this._directory = true;
                            this._entry = directory;
                        } else {
                            this._directory = false;
                            this._entry = entry;
                        }
                    }
                }
                if (close_jar_file && jar_file != null) {
                    try {
                        jar_file.close();
                    } catch (IOException var7) {
                        LOG.ignore(var7);
                    }
                }
                this._exists = this._directory || this._entry != null;
                return this._exists;
            }
        }
    }

    @Override
    public boolean isDirectory() {
        return this._urlString.endsWith("/") || this.exists() && this._directory;
    }

    @Override
    public long lastModified() {
        if (!this.checkConnection() || this._file == null) {
            return -1L;
        } else {
            return this.exists() && this._entry != null ? this._entry.getTime() : this._file.lastModified();
        }
    }

    @Override
    public synchronized String[] list() {
        if (this.isDirectory() && this._list == null) {
            List<String> list = null;
            try {
                list = this.listEntries();
            } catch (Exception var3) {
                LOG.warn("Retrying list:" + var3);
                LOG.debug(var3);
                this.close();
                list = this.listEntries();
            }
            if (list != null) {
                this._list = new String[list.size()];
                list.toArray(this._list);
            }
        }
        return this._list;
    }

    private List<String> listEntries() {
        this.checkConnection();
        ArrayList<String> list = new ArrayList(32);
        JarFile jarFile = this._jarFile;
        if (jarFile == null) {
            try {
                JarURLConnection jc = (JarURLConnection) new URL(this._jarUrl).openConnection();
                jc.setUseCaches(this.getUseCaches());
                jarFile = jc.getJarFile();
            } catch (Exception var9) {
                var9.printStackTrace();
                LOG.ignore(var9);
            }
            if (jarFile == null) {
                throw new IllegalStateException();
            }
        }
        Enumeration<JarEntry> e = jarFile.entries();
        String dir = this._urlString.substring(this._urlString.lastIndexOf("!/") + 2);
        while (e.hasMoreElements()) {
            JarEntry entry = (JarEntry) e.nextElement();
            String name = entry.getName().replace('\\', '/');
            if (name.startsWith(dir) && name.length() != dir.length()) {
                String listName = name.substring(dir.length());
                int dash = listName.indexOf(47);
                if (dash >= 0) {
                    if (dash == 0 && listName.length() == 1) {
                        continue;
                    }
                    if (dash == 0) {
                        listName = listName.substring(dash + 1, listName.length());
                    } else {
                        listName = listName.substring(0, dash + 1);
                    }
                    if (list.contains(listName)) {
                        continue;
                    }
                }
                list.add(listName);
            }
        }
        return list;
    }

    @Override
    public long length() {
        if (this.isDirectory()) {
            return -1L;
        } else {
            return this._entry != null ? this._entry.getSize() : -1L;
        }
    }

    public static Resource getNonCachingResource(Resource resource) {
        if (!(resource instanceof JarFileResource)) {
            return resource;
        } else {
            JarFileResource oldResource = (JarFileResource) resource;
            return new JarFileResource(oldResource.getURL(), false);
        }
    }

    @Override
    public boolean isContainedIn(Resource resource) throws MalformedURLException {
        String string = this._urlString;
        int index = string.lastIndexOf("!/");
        if (index > 0) {
            string = string.substring(0, index);
        }
        if (string.startsWith("jar:")) {
            string = string.substring(4);
        }
        URL url = new URL(string);
        return url.sameFile(resource.getURI().toURL());
    }
}