package com.mojang.realmsclient.dto;

import com.google.common.annotations.VisibleForTesting;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.util.JsonUtils;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import org.slf4j.Logger;

public class UploadInfo extends ValueObject {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final String DEFAULT_SCHEMA = "http://";

    private static final int DEFAULT_PORT = 8080;

    private static final Pattern URI_SCHEMA_PATTERN = Pattern.compile("^[a-zA-Z][-a-zA-Z0-9+.]+:");

    private final boolean worldClosed;

    @Nullable
    private final String token;

    private final URI uploadEndpoint;

    private UploadInfo(boolean boolean0, @Nullable String string1, URI uRI2) {
        this.worldClosed = boolean0;
        this.token = string1;
        this.uploadEndpoint = uRI2;
    }

    @Nullable
    public static UploadInfo parse(String string0) {
        try {
            JsonParser $$1 = new JsonParser();
            JsonObject $$2 = $$1.parse(string0).getAsJsonObject();
            String $$3 = JsonUtils.getStringOr("uploadEndpoint", $$2, null);
            if ($$3 != null) {
                int $$4 = JsonUtils.getIntOr("port", $$2, -1);
                URI $$5 = assembleUri($$3, $$4);
                if ($$5 != null) {
                    boolean $$6 = JsonUtils.getBooleanOr("worldClosed", $$2, false);
                    String $$7 = JsonUtils.getStringOr("token", $$2, null);
                    return new UploadInfo($$6, $$7, $$5);
                }
            }
        } catch (Exception var8) {
            LOGGER.error("Could not parse UploadInfo: {}", var8.getMessage());
        }
        return null;
    }

    @Nullable
    @VisibleForTesting
    public static URI assembleUri(String string0, int int1) {
        Matcher $$2 = URI_SCHEMA_PATTERN.matcher(string0);
        String $$3 = ensureEndpointSchema(string0, $$2);
        try {
            URI $$4 = new URI($$3);
            int $$5 = selectPortOrDefault(int1, $$4.getPort());
            return $$5 != $$4.getPort() ? new URI($$4.getScheme(), $$4.getUserInfo(), $$4.getHost(), $$5, $$4.getPath(), $$4.getQuery(), $$4.getFragment()) : $$4;
        } catch (URISyntaxException var6) {
            LOGGER.warn("Failed to parse URI {}", $$3, var6);
            return null;
        }
    }

    private static int selectPortOrDefault(int int0, int int1) {
        if (int0 != -1) {
            return int0;
        } else {
            return int1 != -1 ? int1 : 8080;
        }
    }

    private static String ensureEndpointSchema(String string0, Matcher matcher1) {
        return matcher1.find() ? string0 : "http://" + string0;
    }

    public static String createRequest(@Nullable String string0) {
        JsonObject $$1 = new JsonObject();
        if (string0 != null) {
            $$1.addProperty("token", string0);
        }
        return $$1.toString();
    }

    @Nullable
    public String getToken() {
        return this.token;
    }

    public URI getUploadEndpoint() {
        return this.uploadEndpoint;
    }

    public boolean isWorldClosed() {
        return this.worldClosed;
    }
}