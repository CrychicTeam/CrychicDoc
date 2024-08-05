package com.cupboard.config;

import com.google.gson.JsonObject;

public class CommonConfiguration implements ICommonConfig {

    public boolean showCommandExecutionErrors = true;

    public boolean debugChunkloadAttempts = false;

    public boolean logOffthreadEntityAdd = true;

    public boolean skipErrorOnEntityLoad = false;

    public boolean forceHeapDumpOnOOM = false;

    @Override
    public JsonObject serialize() {
        JsonObject root = new JsonObject();
        JsonObject entry = new JsonObject();
        entry.addProperty("desc:", "Whether to display errors during command execution: default:true");
        entry.addProperty("showCommandExecutionErrors", this.showCommandExecutionErrors);
        root.add("showCommandExecutionErrors", entry);
        JsonObject entry2 = new JsonObject();
        entry2.addProperty("desc:", "Enables debug logging of chunks being forceloaded on serverthread by directly accessing an unloaded chunk, which stalls the server until the chunk finishes loading, incompatible with lithium and its forks: default:false");
        entry2.addProperty("debugChunkloadAttempts", this.debugChunkloadAttempts);
        root.add("debugChunkloadAttempts", entry2);
        JsonObject entry5 = new JsonObject();
        entry5.addProperty("desc:", "Prevent crashes on entity loading: default:false");
        entry5.addProperty("skipErrorOnEntityLoad", this.skipErrorOnEntityLoad);
        root.add("skipErrorOnEntityLoad", entry5);
        JsonObject entry3 = new JsonObject();
        entry3.addProperty("desc:", "Entities should only be added on the server thread itself, cupboard fixes the crashes caused by mods violating that, this option enables the logging of those: default:true");
        entry3.addProperty("logOffthreadEntityAdd", this.logOffthreadEntityAdd);
        root.add("logOffthreadEntityAdd", entry3);
        JsonObject entry4 = new JsonObject();
        entry4.addProperty("desc:", "Enables creating a heap dump automatically once the game crashes with an out of memory issue, use with care heapdumps take a lot of space. default:false");
        entry4.addProperty("forceHeapDumpOnOOM", this.forceHeapDumpOnOOM);
        root.add("forceHeapDumpOnOOM", entry4);
        return root;
    }

    @Override
    public void deserialize(JsonObject data) {
        this.showCommandExecutionErrors = data.get("showCommandExecutionErrors").getAsJsonObject().get("showCommandExecutionErrors").getAsBoolean();
        this.skipErrorOnEntityLoad = data.get("skipErrorOnEntityLoad").getAsJsonObject().get("skipErrorOnEntityLoad").getAsBoolean();
        this.debugChunkloadAttempts = data.get("debugChunkloadAttempts").getAsJsonObject().get("debugChunkloadAttempts").getAsBoolean();
        this.logOffthreadEntityAdd = data.get("logOffthreadEntityAdd").getAsJsonObject().get("logOffthreadEntityAdd").getAsBoolean();
        this.forceHeapDumpOnOOM = data.get("forceHeapDumpOnOOM").getAsJsonObject().get("forceHeapDumpOnOOM").getAsBoolean();
    }
}