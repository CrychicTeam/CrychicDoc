package se.mickelus.mutil;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.ExecutionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Perks {

    private static final Logger logger = LogManager.getLogger();

    private static volatile Perks.Data data;

    public static void init(String uuid) {
        if (!ConfigHandler.client.queryPerks.get()) {
            logger.info("Perks query disabled, skipping fetch!");
            data = new Perks.Data();
        } else {
            try {
                Gson gson = new GsonBuilder().create();
                HttpRequest request = HttpRequest.newBuilder(new URI("https://mickelus.se/util/perks/" + uuid.replace("-", ""))).header("Accept", "application/json").build();
                HttpClient.newHttpClient().sendAsync(request, BodyHandlers.ofString()).thenApply(HttpResponse::body).thenApply(body -> (Perks.Data) gson.fromJson(body, Perks.Data.class)).thenAccept(Perks::setData).get();
            } catch (ExecutionException | InterruptedException | URISyntaxException var3) {
                logger.warn("Failed to get perk data: " + var3.getMessage());
                data = new Perks.Data();
            }
        }
    }

    public static synchronized Perks.Data getData() {
        return data;
    }

    private static synchronized void setData(Perks.Data newData) {
        data = newData;
    }

    public static class Data {

        public int support;

        public int contribute;

        public int community;

        public int moderate;

        public String toString() {
            return "PerkData{support=" + this.support + ", contribute=" + this.contribute + ", community=" + this.community + ", moderate=" + this.moderate + "}";
        }
    }
}