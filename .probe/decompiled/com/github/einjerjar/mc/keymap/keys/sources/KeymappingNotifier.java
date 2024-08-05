package com.github.einjerjar.mc.keymap.keys.sources;

import com.github.einjerjar.mc.keymap.Keymap;
import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import com.github.einjerjar.mc.keymap.keys.sources.keymap.KeymapSource;
import com.github.einjerjar.mc.keymap.keys.sources.keymap.KeymapSources;
import com.github.einjerjar.mc.keymap.keys.wrappers.keys.KeyHolder;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import java.util.Map.Entry;
import org.jetbrains.annotations.NotNull;

public class KeymappingNotifier {

    protected static final Multimap<Integer, KeyHolder> keys = ArrayListMultimap.create();

    protected static final Multimap<Integer, KeymappingNotifier.KeybindingRegistrySubscriber> subscribers = ArrayListMultimap.create();

    public static Multimap<Integer, KeyHolder> keys() {
        return ImmutableMultimap.copyOf(keys);
    }

    public static Multimap<Integer, KeymappingNotifier.KeybindingRegistrySubscriber> subscribers() {
        return ImmutableMultimap.copyOf(subscribers);
    }

    public static boolean containsHolder(KeyHolder holder) {
        return keys.containsValue(holder);
    }

    public static boolean containsSubscriber(KeymappingNotifier.KeybindingRegistrySubscriber subscriber) {
        return subscribers.containsValue(subscriber);
    }

    public static void clearSubscribers() {
        subscribers.clear();
    }

    public static void loadKeys() {
        keys.clear();
        if (!KeymapSources.collected()) {
            KeymapSources.collect();
        }
        for (KeymapSource s : KeymapSources.sources()) {
            if (s.canUseSource()) {
                for (KeyHolder holder : s.getKeyHolders()) {
                    keys.put(holder.getSingleCode(), holder);
                }
            }
        }
    }

    public static void load() {
        clearSubscribers();
        loadKeys();
    }

    public static void addKey(int code, KeyHolder holder) {
        if (containsHolder(holder)) {
            Keymap.logger().error("!! ADD_KEY IGNORED : REMOVE EXISTING HOLDER VALUE FIRST !!");
        } else {
            keys.put(code, holder);
        }
    }

    public static void removeKey(int code, KeyHolder holder) {
        keys.remove(code, holder);
    }

    public static void notifyAllSubscriber() {
        notifyAllSubscriber(false);
    }

    public static void notifyAllSubscriber(boolean selected) {
        for (Entry<Integer, KeymappingNotifier.KeybindingRegistrySubscriber> e : subscribers.entries()) {
            ((KeymappingNotifier.KeybindingRegistrySubscriber) e.getValue()).keybindingRegistryUpdated(selected);
        }
    }

    public static void notifySubscriber(int code, boolean selected) {
        if (subscribers().containsKey(code)) {
            for (KeymappingNotifier.KeybindingRegistrySubscriber subscriber : subscribers().get(code)) {
                subscriber.keybindingRegistryUpdated(selected);
            }
        }
    }

    public static void subscribe(Integer key, KeymappingNotifier.KeybindingRegistrySubscriber subscriber) {
        subscribers.put(key, subscriber);
    }

    public static void unsubscribe(Integer key, KeymappingNotifier.KeybindingRegistrySubscriber subscriber) {
        subscribers.remove(key, subscriber);
    }

    public static Integer keyOf(@NotNull KeyHolder holder) {
        if (!containsHolder(holder)) {
            return -99;
        } else {
            for (Entry<Integer, KeyHolder> entry : keys.entries()) {
                if (((KeyHolder) entry.getValue()).equals(holder)) {
                    return (Integer) entry.getKey();
                }
            }
            return -99;
        }
    }

    public static void updateKey(Integer lastCode, Integer newCode, KeyHolder holder) {
        removeKey(lastCode, holder);
        if (containsHolder(holder)) {
            String msg = String.format("KeyHolder was not removed by the last removeKey call! [lastCode=%d, newCode=%d, keyOf=%d, holder=%s]", lastCode, newCode, keyOf(holder), holder.getTranslatableName());
            if (KeymapConfig.instance().crashOnProblematicError()) {
                throw new RuntimeException(msg);
            }
            Keymap.logger().fatal(msg);
        }
        addKey(newCode, holder);
        for (KeymappingNotifier.KeybindingRegistrySubscriber s : subscribers().get(lastCode)) {
            s.keybindingRegistryUpdated(false);
        }
        for (KeymappingNotifier.KeybindingRegistrySubscriber s : subscribers().get(newCode)) {
            s.keybindingRegistryUpdated(false);
        }
    }

    public interface KeybindingRegistrySubscriber {

        void keybindingRegistryUpdated(boolean var1);
    }
}