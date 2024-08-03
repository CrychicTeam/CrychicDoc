package de.keksuccino.konkrete.input;

import de.keksuccino.konkrete.math.MathUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class KeyboardHandler {

    private static boolean ctrlPressed = false;

    private static boolean altPressed = false;

    private static boolean keyPressed = false;

    private static int keycode = 0;

    private static int scancode = 0;

    private static char typedChar = " ".charAt(0);

    private static int charModifiers = 0;

    private static int modifiers = 0;

    private static List<Integer> ids = new ArrayList();

    private static Map<Integer, Consumer<KeyboardData>> pressedRaw = new HashMap();

    private static Map<Integer, Consumer<KeyboardData>> releasedRaw = new HashMap();

    private static Map<Integer, Consumer<CharData>> charRaw = new HashMap();

    private static Map<Integer, Consumer<KeyboardData>> keyPressedListeners = new HashMap();

    private static Map<Integer, Consumer<KeyboardData>> keyReleasedListeners = new HashMap();

    private static Map<Integer, Consumer<CharData>> charListeners = new HashMap();

    private static boolean init = false;

    public static void init() {
        if (!init) {
            MinecraftForge.EVENT_BUS.register(new KeyboardHandler());
            addKeyPressedListener(key -> {
                if (key.keycode == 341 || key.keycode == 345) {
                    ctrlPressed = true;
                }
                if (key.keycode == 342) {
                    altPressed = true;
                }
            });
            addKeyReleasedListener(key -> {
                if (key.keycode == 341 || key.keycode == 345) {
                    ctrlPressed = false;
                }
                if (key.keycode == 342) {
                    altPressed = false;
                }
            });
            init = true;
        }
    }

    @SubscribeEvent
    public void onKeyPressPost(ScreenEvent.KeyPressed.Post e) {
        keycode = e.getKeyCode();
        scancode = e.getScanCode();
        modifiers = e.getModifiers();
        keyPressed = true;
        keyPressedListeners.clear();
        keyPressedListeners.putAll(pressedRaw);
        for (Consumer<KeyboardData> c : keyPressedListeners.values()) {
            c.accept(new KeyboardData(e.getKeyCode(), e.getScanCode(), e.getModifiers()));
        }
    }

    @SubscribeEvent
    public void onKeyReleasedPost(ScreenEvent.KeyReleased.Post e) {
        keyPressed = false;
        keyReleasedListeners.clear();
        keyReleasedListeners.putAll(releasedRaw);
        for (Consumer<KeyboardData> c : keyReleasedListeners.values()) {
            c.accept(new KeyboardData(e.getKeyCode(), e.getScanCode(), e.getModifiers()));
        }
    }

    @SubscribeEvent
    public void onCharTyped(ScreenEvent.CharacterTyped.Post e) {
        typedChar = e.getCodePoint();
        charModifiers = e.getModifiers();
        charListeners.clear();
        charListeners.putAll(charRaw);
        for (Consumer<CharData> c : charListeners.values()) {
            c.accept(new CharData(e.getCodePoint(), e.getModifiers()));
        }
    }

    public static boolean isKeyPressed() {
        return keyPressed;
    }

    public static int getCurrentKeyCode() {
        return keycode;
    }

    public static int getCurrentKeyScanCode() {
        return scancode;
    }

    public static int getCurrentKeyModifiers() {
        return modifiers;
    }

    public static char getCurrentChar() {
        return typedChar;
    }

    public static int getCurrentCharModifiers() {
        return charModifiers;
    }

    public static int addKeyPressedListener(Consumer<KeyboardData> c) {
        int id = generateUniqueId();
        pressedRaw.put(id, c);
        return id;
    }

    public static int addKeyReleasedListener(Consumer<KeyboardData> c) {
        int id = generateUniqueId();
        releasedRaw.put(id, c);
        return id;
    }

    public static int addCharTypedListener(Consumer<CharData> c) {
        int id = generateUniqueId();
        charRaw.put(id, c);
        return id;
    }

    public static void removeKeyPressedListener(int id) {
        if (pressedRaw.containsKey(id)) {
            pressedRaw.remove(id);
        }
    }

    public static void removeKeyReleasedListener(int id) {
        if (releasedRaw.containsKey(id)) {
            releasedRaw.remove(id);
        }
    }

    public static void removeCharTypedListener(int id) {
        if (charRaw.containsKey(id)) {
            charRaw.remove(id);
        }
    }

    public static boolean isCtrlPressed() {
        return ctrlPressed;
    }

    public static boolean isAltPressed() {
        return altPressed;
    }

    private static int generateUniqueId() {
        int i = MathUtils.getRandomNumberInRange(100000000, 999999999);
        while (ids.contains(i)) {
            i = MathUtils.getRandomNumberInRange(100000000, 999999999);
        }
        ids.add(i);
        return i;
    }
}