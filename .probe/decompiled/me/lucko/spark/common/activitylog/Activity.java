package me.lucko.spark.common.activitylog;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.concurrent.TimeUnit;
import me.lucko.spark.common.command.sender.CommandSender;

public final class Activity {

    public static final String DATA_TYPE_URL = "url";

    public static final String DATA_TYPE_FILE = "file";

    private final CommandSender.Data user;

    private final long time;

    private final String type;

    private final String dataType;

    private final String dataValue;

    public static Activity urlActivity(CommandSender user, long time, String type, String url) {
        return new Activity(user.toData(), time, type, "url", url);
    }

    public static Activity fileActivity(CommandSender user, long time, String type, String filePath) {
        return new Activity(user.toData(), time, type, "file", filePath);
    }

    private Activity(CommandSender.Data user, long time, String type, String dataType, String dataValue) {
        this.user = user;
        this.time = time;
        this.type = type;
        this.dataType = dataType;
        this.dataValue = dataValue;
    }

    public CommandSender.Data getUser() {
        return this.user;
    }

    public long getTime() {
        return this.time;
    }

    public String getType() {
        return this.type;
    }

    public String getDataType() {
        return this.dataType;
    }

    public String getDataValue() {
        return this.dataValue;
    }

    public boolean shouldExpire() {
        return this.dataType.equals("url") ? System.currentTimeMillis() - this.time > TimeUnit.DAYS.toMillis(60L) : false;
    }

    public JsonObject serialize() {
        JsonObject object = new JsonObject();
        object.add("user", this.user.serialize());
        object.add("time", new JsonPrimitive(this.time));
        object.add("type", new JsonPrimitive(this.type));
        JsonObject data = new JsonObject();
        data.add("type", new JsonPrimitive(this.dataType));
        data.add("value", new JsonPrimitive(this.dataValue));
        object.add("data", data);
        return object;
    }

    public static Activity deserialize(JsonElement element) {
        JsonObject object = element.getAsJsonObject();
        CommandSender.Data user = CommandSender.Data.deserialize(object.get("user"));
        long time = object.get("time").getAsJsonPrimitive().getAsLong();
        String type = object.get("type").getAsJsonPrimitive().getAsString();
        JsonObject dataObject = object.get("data").getAsJsonObject();
        String dataType = dataObject.get("type").getAsJsonPrimitive().getAsString();
        String dataValue = dataObject.get("value").getAsJsonPrimitive().getAsString();
        return new Activity(user, time, type, dataType, dataValue);
    }
}