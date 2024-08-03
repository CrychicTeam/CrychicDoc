package net.minecraft.server.network;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mojang.authlib.GameProfile;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.network.chat.FilterMask;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.thread.ProcessorMailbox;
import org.slf4j.Logger;

public class TextFilterClient implements AutoCloseable {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final AtomicInteger WORKER_COUNT = new AtomicInteger(1);

    private static final ThreadFactory THREAD_FACTORY = p_10148_ -> {
        Thread $$1 = new Thread(p_10148_);
        $$1.setName("Chat-Filter-Worker-" + WORKER_COUNT.getAndIncrement());
        return $$1;
    };

    private static final String DEFAULT_ENDPOINT = "v1/chat";

    private final URL chatEndpoint;

    private final TextFilterClient.MessageEncoder chatEncoder;

    final URL joinEndpoint;

    final TextFilterClient.JoinOrLeaveEncoder joinEncoder;

    final URL leaveEndpoint;

    final TextFilterClient.JoinOrLeaveEncoder leaveEncoder;

    private final String authKey;

    final TextFilterClient.IgnoreStrategy chatIgnoreStrategy;

    final ExecutorService workerPool;

    private TextFilterClient(URL uRL0, TextFilterClient.MessageEncoder textFilterClientMessageEncoder1, URL uRL2, TextFilterClient.JoinOrLeaveEncoder textFilterClientJoinOrLeaveEncoder3, URL uRL4, TextFilterClient.JoinOrLeaveEncoder textFilterClientJoinOrLeaveEncoder5, String string6, TextFilterClient.IgnoreStrategy textFilterClientIgnoreStrategy7, int int8) {
        this.authKey = string6;
        this.chatIgnoreStrategy = textFilterClientIgnoreStrategy7;
        this.chatEndpoint = uRL0;
        this.chatEncoder = textFilterClientMessageEncoder1;
        this.joinEndpoint = uRL2;
        this.joinEncoder = textFilterClientJoinOrLeaveEncoder3;
        this.leaveEndpoint = uRL4;
        this.leaveEncoder = textFilterClientJoinOrLeaveEncoder5;
        this.workerPool = Executors.newFixedThreadPool(int8, THREAD_FACTORY);
    }

    private static URL getEndpoint(URI uRI0, @Nullable JsonObject jsonObject1, String string2, String string3) throws MalformedURLException {
        String $$4 = getEndpointFromConfig(jsonObject1, string2, string3);
        return uRI0.resolve("/" + $$4).toURL();
    }

    private static String getEndpointFromConfig(@Nullable JsonObject jsonObject0, String string1, String string2) {
        return jsonObject0 != null ? GsonHelper.getAsString(jsonObject0, string1, string2) : string2;
    }

    @Nullable
    public static TextFilterClient createFromConfig(String string0) {
        if (Strings.isNullOrEmpty(string0)) {
            return null;
        } else {
            try {
                JsonObject $$1 = GsonHelper.parse(string0);
                URI $$2 = new URI(GsonHelper.getAsString($$1, "apiServer"));
                String $$3 = GsonHelper.getAsString($$1, "apiKey");
                if ($$3.isEmpty()) {
                    throw new IllegalArgumentException("Missing API key");
                } else {
                    int $$4 = GsonHelper.getAsInt($$1, "ruleId", 1);
                    String $$5 = GsonHelper.getAsString($$1, "serverId", "");
                    String $$6 = GsonHelper.getAsString($$1, "roomId", "Java:Chat");
                    int $$7 = GsonHelper.getAsInt($$1, "hashesToDrop", -1);
                    int $$8 = GsonHelper.getAsInt($$1, "maxConcurrentRequests", 7);
                    JsonObject $$9 = GsonHelper.getAsJsonObject($$1, "endpoints", null);
                    String $$10 = getEndpointFromConfig($$9, "chat", "v1/chat");
                    boolean $$11 = $$10.equals("v1/chat");
                    URL $$12 = $$2.resolve("/" + $$10).toURL();
                    URL $$13 = getEndpoint($$2, $$9, "join", "v1/join");
                    URL $$14 = getEndpoint($$2, $$9, "leave", "v1/leave");
                    TextFilterClient.JoinOrLeaveEncoder $$15 = p_215310_ -> {
                        JsonObject $$3x = new JsonObject();
                        $$3x.addProperty("server", $$5);
                        $$3x.addProperty("room", $$6);
                        $$3x.addProperty("user_id", p_215310_.getId().toString());
                        $$3x.addProperty("user_display_name", p_215310_.getName());
                        return $$3x;
                    };
                    TextFilterClient.MessageEncoder $$16;
                    if ($$11) {
                        $$16 = (p_238214_, p_238215_) -> {
                            JsonObject $$5x = new JsonObject();
                            $$5x.addProperty("rule", $$4);
                            $$5x.addProperty("server", $$5);
                            $$5x.addProperty("room", $$6);
                            $$5x.addProperty("player", p_238214_.getId().toString());
                            $$5x.addProperty("player_display_name", p_238214_.getName());
                            $$5x.addProperty("text", p_238215_);
                            $$5x.addProperty("language", "*");
                            return $$5x;
                        };
                    } else {
                        String $$17 = String.valueOf($$4);
                        $$16 = (p_238220_, p_238221_) -> {
                            JsonObject $$5x = new JsonObject();
                            $$5x.addProperty("rule_id", $$17);
                            $$5x.addProperty("category", $$5);
                            $$5x.addProperty("subcategory", $$6);
                            $$5x.addProperty("user_id", p_238220_.getId().toString());
                            $$5x.addProperty("user_display_name", p_238220_.getName());
                            $$5x.addProperty("text", p_238221_);
                            $$5x.addProperty("language", "*");
                            return $$5x;
                        };
                    }
                    TextFilterClient.IgnoreStrategy $$19 = TextFilterClient.IgnoreStrategy.select($$7);
                    String $$20 = Base64.getEncoder().encodeToString($$3.getBytes(StandardCharsets.US_ASCII));
                    return new TextFilterClient($$12, $$16, $$13, $$15, $$14, $$15, $$20, $$19, $$8);
                }
            } catch (Exception var19) {
                LOGGER.warn("Failed to parse chat filter config {}", string0, var19);
                return null;
            }
        }
    }

    void processJoinOrLeave(GameProfile gameProfile0, URL uRL1, TextFilterClient.JoinOrLeaveEncoder textFilterClientJoinOrLeaveEncoder2, Executor executor3) {
        executor3.execute(() -> {
            JsonObject $$3 = textFilterClientJoinOrLeaveEncoder2.encode(gameProfile0);
            try {
                this.processRequest($$3, uRL1);
            } catch (Exception var6) {
                LOGGER.warn("Failed to send join/leave packet to {} for player {}", new Object[] { uRL1, gameProfile0, var6 });
            }
        });
    }

    CompletableFuture<FilteredText> requestMessageProcessing(GameProfile gameProfile0, String string1, TextFilterClient.IgnoreStrategy textFilterClientIgnoreStrategy2, Executor executor3) {
        return string1.isEmpty() ? CompletableFuture.completedFuture(FilteredText.EMPTY) : CompletableFuture.supplyAsync(() -> {
            JsonObject $$3 = this.chatEncoder.encode(gameProfile0, string1);
            try {
                JsonObject $$4 = this.processRequestResponse($$3, this.chatEndpoint);
                boolean $$5 = GsonHelper.getAsBoolean($$4, "response", false);
                if ($$5) {
                    return FilteredText.passThrough(string1);
                } else {
                    String $$6 = GsonHelper.getAsString($$4, "hashed", null);
                    if ($$6 == null) {
                        return FilteredText.fullyFiltered(string1);
                    } else {
                        JsonArray $$7 = GsonHelper.getAsJsonArray($$4, "hashes");
                        FilterMask $$8 = this.parseMask(string1, $$7, textFilterClientIgnoreStrategy2);
                        return new FilteredText(string1, $$8);
                    }
                }
            } catch (Exception var10) {
                LOGGER.warn("Failed to validate message '{}'", string1, var10);
                return FilteredText.fullyFiltered(string1);
            }
        }, executor3);
    }

    private FilterMask parseMask(String string0, JsonArray jsonArray1, TextFilterClient.IgnoreStrategy textFilterClientIgnoreStrategy2) {
        if (jsonArray1.isEmpty()) {
            return FilterMask.PASS_THROUGH;
        } else if (textFilterClientIgnoreStrategy2.shouldIgnore(string0, jsonArray1.size())) {
            return FilterMask.FULLY_FILTERED;
        } else {
            FilterMask $$3 = new FilterMask(string0.length());
            for (int $$4 = 0; $$4 < jsonArray1.size(); $$4++) {
                $$3.setFiltered(jsonArray1.get($$4).getAsInt());
            }
            return $$3;
        }
    }

    public void close() {
        this.workerPool.shutdownNow();
    }

    private void drainStream(InputStream inputStream0) throws IOException {
        byte[] $$1 = new byte[1024];
        while (inputStream0.read($$1) != -1) {
        }
    }

    private JsonObject processRequestResponse(JsonObject jsonObject0, URL uRL1) throws IOException {
        HttpURLConnection $$2 = this.makeRequest(jsonObject0, uRL1);
        InputStream $$3 = $$2.getInputStream();
        JsonObject var13;
        label74: {
            try {
                if ($$2.getResponseCode() == 204) {
                    var13 = new JsonObject();
                    break label74;
                }
                try {
                    var13 = Streams.parse(new JsonReader(new InputStreamReader($$3, StandardCharsets.UTF_8))).getAsJsonObject();
                } finally {
                    this.drainStream($$3);
                }
            } catch (Throwable var12) {
                if ($$3 != null) {
                    try {
                        $$3.close();
                    } catch (Throwable var10) {
                        var12.addSuppressed(var10);
                    }
                }
                throw var12;
            }
            if ($$3 != null) {
                $$3.close();
            }
            return var13;
        }
        if ($$3 != null) {
            $$3.close();
        }
        return var13;
    }

    private void processRequest(JsonObject jsonObject0, URL uRL1) throws IOException {
        HttpURLConnection $$2 = this.makeRequest(jsonObject0, uRL1);
        InputStream $$3 = $$2.getInputStream();
        try {
            this.drainStream($$3);
        } catch (Throwable var8) {
            if ($$3 != null) {
                try {
                    $$3.close();
                } catch (Throwable var7) {
                    var8.addSuppressed(var7);
                }
            }
            throw var8;
        }
        if ($$3 != null) {
            $$3.close();
        }
    }

    private HttpURLConnection makeRequest(JsonObject jsonObject0, URL uRL1) throws IOException {
        HttpURLConnection $$2 = (HttpURLConnection) uRL1.openConnection();
        $$2.setConnectTimeout(15000);
        $$2.setReadTimeout(2000);
        $$2.setUseCaches(false);
        $$2.setDoOutput(true);
        $$2.setDoInput(true);
        $$2.setRequestMethod("POST");
        $$2.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        $$2.setRequestProperty("Accept", "application/json");
        $$2.setRequestProperty("Authorization", "Basic " + this.authKey);
        $$2.setRequestProperty("User-Agent", "Minecraft server" + SharedConstants.getCurrentVersion().getName());
        OutputStreamWriter $$3 = new OutputStreamWriter($$2.getOutputStream(), StandardCharsets.UTF_8);
        try {
            JsonWriter $$4 = new JsonWriter($$3);
            try {
                Streams.write(jsonObject0, $$4);
            } catch (Throwable var10) {
                try {
                    $$4.close();
                } catch (Throwable var9) {
                    var10.addSuppressed(var9);
                }
                throw var10;
            }
            $$4.close();
        } catch (Throwable var11) {
            try {
                $$3.close();
            } catch (Throwable var8) {
                var11.addSuppressed(var8);
            }
            throw var11;
        }
        $$3.close();
        int $$5 = $$2.getResponseCode();
        if ($$5 >= 200 && $$5 < 300) {
            return $$2;
        } else {
            throw new TextFilterClient.RequestFailedException($$5 + " " + $$2.getResponseMessage());
        }
    }

    public TextFilter createContext(GameProfile gameProfile0) {
        return new TextFilterClient.PlayerContext(gameProfile0);
    }

    @FunctionalInterface
    public interface IgnoreStrategy {

        TextFilterClient.IgnoreStrategy NEVER_IGNORE = (p_10169_, p_10170_) -> false;

        TextFilterClient.IgnoreStrategy IGNORE_FULLY_FILTERED = (p_10166_, p_10167_) -> p_10166_.length() == p_10167_;

        static TextFilterClient.IgnoreStrategy ignoreOverThreshold(int int0) {
            return (p_143742_, p_143743_) -> p_143743_ >= int0;
        }

        static TextFilterClient.IgnoreStrategy select(int int0) {
            return switch(int0) {
                case -1 ->
                    NEVER_IGNORE;
                case 0 ->
                    IGNORE_FULLY_FILTERED;
                default ->
                    ignoreOverThreshold(int0);
            };
        }

        boolean shouldIgnore(String var1, int var2);
    }

    @FunctionalInterface
    interface JoinOrLeaveEncoder {

        JsonObject encode(GameProfile var1);
    }

    @FunctionalInterface
    interface MessageEncoder {

        JsonObject encode(GameProfile var1, String var2);
    }

    class PlayerContext implements TextFilter {

        private final GameProfile profile;

        private final Executor streamExecutor;

        PlayerContext(GameProfile gameProfile0) {
            this.profile = gameProfile0;
            ProcessorMailbox<Runnable> $$1 = ProcessorMailbox.create(TextFilterClient.this.workerPool, "chat stream for " + gameProfile0.getName());
            this.streamExecutor = $$1::m_6937_;
        }

        @Override
        public void join() {
            TextFilterClient.this.processJoinOrLeave(this.profile, TextFilterClient.this.joinEndpoint, TextFilterClient.this.joinEncoder, this.streamExecutor);
        }

        @Override
        public void leave() {
            TextFilterClient.this.processJoinOrLeave(this.profile, TextFilterClient.this.leaveEndpoint, TextFilterClient.this.leaveEncoder, this.streamExecutor);
        }

        @Override
        public CompletableFuture<List<FilteredText>> processMessageBundle(List<String> listString0) {
            List<CompletableFuture<FilteredText>> $$1 = (List<CompletableFuture<FilteredText>>) listString0.stream().map(p_10195_ -> TextFilterClient.this.requestMessageProcessing(this.profile, p_10195_, TextFilterClient.this.chatIgnoreStrategy, this.streamExecutor)).collect(ImmutableList.toImmutableList());
            return Util.sequenceFailFast($$1).exceptionally(p_143747_ -> ImmutableList.of());
        }

        @Override
        public CompletableFuture<FilteredText> processStreamMessage(String string0) {
            return TextFilterClient.this.requestMessageProcessing(this.profile, string0, TextFilterClient.this.chatIgnoreStrategy, this.streamExecutor);
        }
    }

    public static class RequestFailedException extends RuntimeException {

        RequestFailedException(String string0) {
            super(string0);
        }
    }
}