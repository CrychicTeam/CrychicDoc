package com.simibubi.create.infrastructure.ponder;

import java.util.function.BiConsumer;

public class GeneralText {

    public static void provideLang(BiConsumer<String, String> consumer) {
        consume(consumer, "ponder.hold_to_ponder", "Hold [%1$s] to Ponder");
        consume(consumer, "ponder.subject", "Subject of this scene");
        consume(consumer, "ponder.pondering", "Pondering about...");
        consume(consumer, "ponder.identify_mode", "Identify mode active.\nUnpause with [%1$s]");
        consume(consumer, "ponder.associated", "Associated Entries");
        consume(consumer, "ponder.close", "Close");
        consume(consumer, "ponder.identify", "Identify");
        consume(consumer, "ponder.next", "Next Scene");
        consume(consumer, "ponder.next_up", "Up Next:");
        consume(consumer, "ponder.previous", "Previous Scene");
        consume(consumer, "ponder.replay", "Replay");
        consume(consumer, "ponder.think_back", "Think Back");
        consume(consumer, "ponder.slow_text", "Comfy Reading");
        consume(consumer, "ponder.exit", "Exit");
        consume(consumer, "ponder.welcome", "Welcome to Ponder");
        consume(consumer, "ponder.categories", "Available Categories in Create");
        consume(consumer, "ponder.index_description", "Click one of the icons to learn about its associated Items and Blocks");
        consume(consumer, "ponder.index_title", "Ponder Index");
    }

    private static void consume(BiConsumer<String, String> consumer, String key, String enUS) {
        consumer.accept("create." + key, enUS);
    }
}