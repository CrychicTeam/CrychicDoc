package journeymap.common.version;

import com.google.common.io.CharStreams;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import journeymap.client.JourneymapClient;
import journeymap.common.Journeymap;
import journeymap.common.LoaderHooks;
import journeymap.common.thread.JMThreadFactory;

public class VersionCheck {

    private static volatile ExecutorService executorService;

    private static volatile Boolean updateCheckEnabled = JourneymapClient.getInstance().isUpdateCheckEnabled();

    private static volatile Boolean versionIsCurrent = true;

    private static volatile Boolean versionIsChecked;

    private static volatile String versionAvailable;

    private static volatile String downloadUrl;

    public static Boolean getVersionIsCurrent() {
        if (versionIsChecked == null) {
            checkVersion();
        }
        return versionIsCurrent;
    }

    public static Boolean getVersionIsChecked() {
        if (versionIsChecked == null) {
            checkVersion();
        }
        return versionIsChecked;
    }

    public static String getVersionAvailable() {
        if (versionIsChecked == null) {
            checkVersion();
        }
        return versionAvailable;
    }

    public static String getDownloadUrl() {
        if (versionIsChecked == null) {
            checkVersion();
        }
        return downloadUrl;
    }

    private static synchronized void checkVersion() {
        versionIsChecked = false;
        versionIsCurrent = true;
        versionAvailable = "0";
        if (!updateCheckEnabled) {
            Journeymap.getLogger().info("Update check disabled in properties file.");
        } else {
            executorService = Executors.newSingleThreadExecutor(new JMThreadFactory("VersionCheck"));
            try {
                executorService.submit(() -> {
                    String currentVersion = Journeymap.JM_VERSION.toString();
                    boolean currentIsRelease = Journeymap.JM_VERSION.isRelease();
                    JsonObject project = makeGetRequest(Journeymap.VERSION_URL);
                    JsonObject downloadUrls = makeGetRequest(Journeymap.VERSION_URL + "/references");
                    if (project != null && downloadUrls != null) {
                        JsonObject promos = project.get("promos").getAsJsonObject();
                        Set<String> versions = project.get("promos").getAsJsonObject().keySet();
                        if (versions == null) {
                            Journeymap.getLogger().warn("No versions found online for " + LoaderHooks.getMCVersion());
                        } else {
                            for (String name : versions) {
                                String file = promos.get(name).getAsString();
                                try {
                                    if ((!currentIsRelease || name.contains("recommended")) && name.contains(LoaderHooks.getMCVersion())) {
                                        downloadUrl = downloadUrls.get(name).getAsString();
                                        if (!isCurrent(currentVersion, file)) {
                                            versionAvailable = file;
                                            versionIsCurrent = false;
                                            versionIsChecked = true;
                                            Journeymap.getLogger().info(String.format("Newer version online: JourneyMap %s for Minecraft %s on %s", versionAvailable, LoaderHooks.getMCVersion(), downloadUrl));
                                            break;
                                        }
                                    }
                                } catch (Exception var10) {
                                    Journeymap.getLogger().error("Could not parse download info: " + file, var10);
                                }
                            }
                        }
                        if (!versionIsChecked) {
                            versionAvailable = currentVersion;
                            versionIsCurrent = true;
                            versionIsChecked = true;
                            downloadUrl = Journeymap.DOWNLOAD_URL;
                        }
                    }
                    if (!versionIsCurrent) {
                    }
                });
            } catch (Exception var4) {
                Journeymap.getLogger().error("Error with version checking");
            } finally {
                executorService.shutdown();
                executorService = null;
            }
        }
    }

    private static JsonObject makeGetRequest(String url) {
        InputStreamReader in = null;
        JsonObject var9;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("Java-http-client/").append(System.getProperty("java.version")).append(' ');
            sb.append("journeymap").append('/').append(Journeymap.JM_VERSION);
            String userAgent = sb.toString();
            URI uri = URI.create(url);
            HttpRequest request = HttpRequest.newBuilder().uri(uri).timeout(Duration.ofSeconds(6L)).setHeader("Content-type", "application/json").setHeader("User-Agent", userAgent).GET().build();
            HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(6L)).build();
            HttpResponse<InputStream> response = httpClient.send(request, BodyHandlers.ofInputStream());
            if (response.statusCode() / 200 != 1) {
                Journeymap.getLogger().error(String.format("Version check to %s returned: %s ", uri, response.statusCode()));
                return null;
            }
            in = new InputStreamReader((InputStream) response.body());
            String rawResponse = CharStreams.toString(in);
            var9 = JsonParser.parseString(rawResponse).getAsJsonObject();
        } catch (Throwable var20) {
            Journeymap.getLogger().error("Could not check version URL {}", url, var20);
            updateCheckEnabled = false;
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException var19) {
                }
            }
        }
        return var9;
    }

    private static boolean isCurrent(String thisVersionStr, String availableVersionStr) {
        if (thisVersionStr.equals(availableVersionStr)) {
            return true;
        } else {
            Version thisVersion = Version.from(thisVersionStr, null);
            Version availableVersion = Version.from(availableVersionStr, null);
            return !availableVersion.isNewerThan(thisVersion);
        }
    }
}