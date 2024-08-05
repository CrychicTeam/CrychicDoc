package org.violetmoon.zeta.client.event.load;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.function.Predicate;
import net.minecraft.client.KeyMapping;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.client.key.PredicatedKeyBinding;
import org.violetmoon.zeta.client.key.SortedKeyBinding;
import org.violetmoon.zeta.client.key.SortedPredicatedKeyBinding;
import org.violetmoon.zeta.event.bus.IZetaLoadEvent;

public interface ZKeyMapping extends IZetaLoadEvent {

    KeyMapping register(KeyMapping var1);

    default KeyMapping init(String id, @Nullable String key, String group) {
        return this.register(new KeyMapping(id, InputConstants.Type.KEYSYM, this.getKeyCode(key, InputConstants.Type.KEYSYM), group));
    }

    default KeyMapping init(String id, @Nullable String key, String group, int sortPriority) {
        return this.register(new SortedKeyBinding(id, InputConstants.Type.KEYSYM, this.getKeyCode(key, InputConstants.Type.KEYSYM), group, sortPriority));
    }

    default KeyMapping init(String id, @Nullable String key, String group, Predicate<InputConstants.Key> allowed) {
        return this.register(new PredicatedKeyBinding(id, InputConstants.Type.KEYSYM, this.getKeyCode(key, InputConstants.Type.KEYSYM), group, allowed));
    }

    default KeyMapping init(String id, @Nullable String key, String group, int sortPriority, Predicate<InputConstants.Key> allowed) {
        return this.register(new SortedPredicatedKeyBinding(id, InputConstants.Type.KEYSYM, this.getKeyCode(key, InputConstants.Type.KEYSYM), group, sortPriority, allowed));
    }

    default KeyMapping initMouse(String id, int key, String group) {
        return this.register(new KeyMapping(id, InputConstants.Type.MOUSE, this.getKeyCode(Integer.toString(key), InputConstants.Type.MOUSE), group));
    }

    default KeyMapping initMouse(String id, int key, String group, int sortPriority) {
        return this.register(new SortedKeyBinding(id, InputConstants.Type.MOUSE, this.getKeyCode(Integer.toString(key), InputConstants.Type.MOUSE), group, sortPriority));
    }

    default KeyMapping initMouse(String id, int key, String group, Predicate<InputConstants.Key> allowed) {
        return this.register(new PredicatedKeyBinding(id, InputConstants.Type.MOUSE, this.getKeyCode(Integer.toString(key), InputConstants.Type.MOUSE), group, allowed));
    }

    default KeyMapping initMouse(String id, int key, String group, int sortPriority, Predicate<InputConstants.Key> allowed) {
        return this.register(new SortedPredicatedKeyBinding(id, InputConstants.Type.MOUSE, this.getKeyCode(Integer.toString(key), InputConstants.Type.MOUSE), group, sortPriority, allowed));
    }

    default String getKeyPrefix(InputConstants.Type type) {
        return switch(type) {
            case MOUSE ->
                "key.mouse.";
            case KEYSYM ->
                "key.keyboard.";
            case SCANCODE ->
                "scancode.";
        };
    }

    default int getKeyCode(@Nullable String key, InputConstants.Type type) {
        return (key == null ? InputConstants.UNKNOWN : InputConstants.getKey(this.getKeyPrefix(type) + key)).getValue();
    }
}