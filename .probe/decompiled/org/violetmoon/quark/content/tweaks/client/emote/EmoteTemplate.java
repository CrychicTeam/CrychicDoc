package org.violetmoon.quark.content.tweaks.client.emote;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.TweenEquations;
import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.violetmoon.quark.base.Quark;

public class EmoteTemplate {

    private static final Map<String, Integer> parts = new HashMap();

    private static final Map<String, Integer> tweenables = new HashMap();

    private static final Map<String, EmoteTemplate.Function> functions = new HashMap();

    private static final Map<String, TweenEquation> equations = new HashMap();

    public final String file;

    public List<String> readLines;

    public List<Integer> usedParts;

    public Stack<Timeline> timelineStack;

    public float speed;

    public int tier;

    public boolean compiled = false;

    public boolean compiledOnce = false;

    private List<EmoteSound> activeSounds = Lists.newArrayList();

    public EmoteTemplate(String file) {
        this.file = file;
        this.readAndMakeTimeline(null, null, null);
    }

    public Timeline getTimeline(EmoteDescriptor desc, Player player, HumanoidModel<?> model) {
        this.compiled = false;
        this.speed = 1.0F;
        this.tier = 0;
        if (this.readLines == null) {
            return this.readAndMakeTimeline(desc, player, model);
        } else {
            Timeline timeline = null;
            this.timelineStack = new Stack();
            int i = 0;
            try {
                while (i < this.readLines.size() && !this.compiled) {
                    timeline = this.handle(model, player, timeline, (String) this.readLines.get(i));
                    i++;
                }
            } catch (Exception var7) {
                this.logError(var7, i);
                return Timeline.createSequence();
            }
            return timeline == null ? Timeline.createSequence() : timeline;
        }
    }

    public Timeline readAndMakeTimeline(EmoteDescriptor desc, Player player, HumanoidModel<?> model) {
        Timeline timeline = null;
        this.usedParts = new ArrayList();
        this.timelineStack = new Stack();
        int lines = 0;
        this.tier = 0;
        this.compiled = this.compiledOnce = false;
        this.readLines = new ArrayList();
        Timeline var22;
        try {
            BufferedReader reader;
            label181: {
                label182: {
                    reader = this.createReader();
                    Timeline var8;
                    try {
                        label169: {
                            try {
                                while ((s = reader.readLine()) != null && !this.compiled) {
                                    lines++;
                                    this.readLines.add(s);
                                    timeline = this.handle(model, player, timeline, s);
                                }
                            } catch (Exception var16) {
                                this.logError(var16, lines);
                                var8 = this.fallback();
                                break label169;
                            }
                            if (timeline == null) {
                                var22 = this.fallback();
                                break label181;
                            }
                            var22 = timeline;
                            break label182;
                        }
                    } catch (Throwable var17) {
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (Throwable var15) {
                                var17.addSuppressed(var15);
                            }
                        }
                        throw var17;
                    }
                    if (reader != null) {
                        reader.close();
                    }
                    return var8;
                }
                if (reader != null) {
                    reader.close();
                }
                return var22;
            }
            if (reader != null) {
                reader.close();
            }
        } catch (IOException var18) {
            Quark.LOG.warn("Failed to load emote " + desc, var18);
            return this.fallback();
        } finally {
            this.compiledOnce = true;
            if (desc != null) {
                desc.updateTier(this);
            }
        }
        return var22;
    }

    protected BufferedReader createReader() throws FileNotFoundException {
        return new BufferedReader(new InputStreamReader(Quark.class.getResourceAsStream("/assets/quark/emotes/" + this.file)));
    }

    protected Timeline fallback() {
        return Timeline.createSequence();
    }

    private void logError(Exception e, int line) {
        Quark.LOG.error("[Custom Emotes] Error loading line " + (line + 1) + " of emote " + this.file);
        if (!(e instanceof IllegalArgumentException)) {
            Quark.LOG.error("[Custom Emotes] This is an Internal Error, and not one in the emote file, please report it", e);
        } else {
            Quark.LOG.error("[Custom Emotes] " + e.getMessage());
        }
    }

    private Timeline handle(HumanoidModel<?> model, Player player, Timeline timeline, String s) throws IllegalArgumentException {
        s = s.trim();
        if (!s.startsWith("#") && !s.isEmpty()) {
            String[] tokens = s.trim().split(" ");
            String function = tokens[0];
            if (functions.containsKey(function)) {
                return ((EmoteTemplate.Function) functions.get(function)).invoke(this, model, player, timeline, tokens);
            } else {
                throw new IllegalArgumentException("Illegal function name " + function);
            }
        } else {
            return timeline;
        }
    }

    protected void setName(String[] tokens) {
    }

    private static Timeline name(EmoteTemplate em, Timeline timeline, String[] tokens) throws IllegalArgumentException {
        em.setName(tokens);
        return timeline;
    }

    private static Timeline use(EmoteTemplate em, Timeline timeline, String[] tokens) throws IllegalArgumentException {
        if (em.compiledOnce) {
            return timeline;
        } else {
            assertParamSize(tokens, 2);
            String part = tokens[1];
            if (parts.containsKey(part)) {
                em.usedParts.add((Integer) parts.get(part));
                return timeline;
            } else {
                throw new IllegalArgumentException("Illegal part name for function use: " + part);
            }
        }
    }

    private static Timeline unit(EmoteTemplate em, Timeline timeline, String[] tokens) throws IllegalArgumentException {
        assertParamSize(tokens, 2);
        em.speed = Float.parseFloat(tokens[1]);
        return timeline;
    }

    private static Timeline tier(EmoteTemplate em, Timeline timeline, String[] tokens) throws IllegalArgumentException {
        assertParamSize(tokens, 2);
        em.tier = Integer.parseInt(tokens[1]);
        return timeline;
    }

    private static Timeline animation(EmoteTemplate em, Timeline timeline, String[] tokens) throws IllegalArgumentException {
        if (timeline != null) {
            throw new IllegalArgumentException("Illegal use of function animation, animation already started");
        } else {
            assertParamSize(tokens, 2);
            String type = tokens[1];
            Timeline newTimeline = switch(type) {
                case "sequence" ->
                    Timeline.createSequence();
                case "parallel" ->
                    Timeline.createParallel();
                default ->
                    throw new IllegalArgumentException("Illegal animation type: " + type);
            };
            newTimeline.addCallback(2, tween -> {
                EmoteSound.endAll(em.activeSounds);
                em.activeSounds = Lists.newArrayList();
            });
            return newTimeline;
        }
    }

    private static Timeline section(EmoteTemplate em, Timeline timeline, String[] tokens) throws IllegalArgumentException {
        if (timeline == null) {
            throw new IllegalArgumentException("Illegal use of function section, animation not started");
        } else {
            assertParamSize(tokens, 2);
            String type = tokens[1];
            Timeline newTimeline = switch(type) {
                case "sequence" ->
                    Timeline.createSequence();
                case "parallel" ->
                    Timeline.createParallel();
                default ->
                    throw new IllegalArgumentException("Illegal section type: " + type);
            };
            em.timelineStack.push(timeline);
            return newTimeline;
        }
    }

    private static Timeline end(EmoteTemplate em, Timeline timeline, String[] tokens) throws IllegalArgumentException {
        if (timeline == null) {
            throw new IllegalArgumentException("Illegal use of function end, animation not started");
        } else {
            assertParamSize(tokens, 1);
            if (em.timelineStack.isEmpty()) {
                em.compiled = true;
                return timeline;
            } else {
                Timeline poppedLine = (Timeline) em.timelineStack.pop();
                poppedLine.push(timeline);
                return poppedLine;
            }
        }
    }

    private static Timeline move(EmoteTemplate em, HumanoidModel<?> model, Timeline timeline, String[] tokens) throws IllegalArgumentException {
        if (timeline == null) {
            throw new IllegalArgumentException("Illegal use of function move, animation not started");
        } else if (tokens.length < 4) {
            throw new IllegalArgumentException(String.format("Illegal parameter amount for function move: %d (at least 4 are required)", tokens.length));
        } else {
            String partStr = tokens[1];
            if (tweenables.containsKey(partStr)) {
                int part = (Integer) tweenables.get(partStr);
                float time = Float.parseFloat(tokens[2]) * em.speed;
                float target = Float.parseFloat(tokens[3]);
                Tween tween = null;
                boolean valid = model != null;
                if (valid) {
                    tween = Tween.to(model, part, time).target(target);
                }
                if (tokens.length > 4) {
                    int index = 4;
                    while (index < tokens.length) {
                        String cmd = tokens[index++];
                        switch(cmd) {
                            case "delay":
                                assertParamSize("delay", tokens, 1, index);
                                float delayxx = Float.parseFloat(tokens[index++]) * em.speed;
                                if (valid) {
                                    tween = tween.delay(delayxx);
                                }
                                break;
                            case "yoyo":
                                assertParamSize("yoyo", tokens, 2, index);
                                int timesx = Integer.parseInt(tokens[index++]);
                                float delayx = Float.parseFloat(tokens[index++]) * em.speed;
                                if (valid) {
                                    tween = tween.repeatYoyo(timesx, delayx);
                                }
                                break;
                            case "repeat":
                                assertParamSize("repeat", tokens, 2, index);
                                int times = Integer.parseInt(tokens[index++]);
                                float delay = Float.parseFloat(tokens[index++]) * em.speed;
                                if (valid) {
                                    tween = tween.repeat(times, delay);
                                }
                                break;
                            case "ease":
                                assertParamSize("ease", tokens, 1, index);
                                String easeType = tokens[index++];
                                if (!equations.containsKey(easeType)) {
                                    throw new IllegalArgumentException("Easing type " + easeType + " doesn't exist");
                                }
                                if (valid) {
                                    tween.ease((TweenEquation) equations.get(easeType));
                                }
                                break;
                            default:
                                throw new IllegalArgumentException(String.format("Invalid modifier %s for move function", cmd));
                        }
                    }
                }
                return valid ? timeline.push(tween) : timeline;
            } else {
                throw new IllegalArgumentException("Illegal part name for function move: " + partStr);
            }
        }
    }

    private static Timeline sound(EmoteTemplate em, Player player, Timeline timeline, String[] tokens) throws IllegalArgumentException {
        if (timeline == null) {
            throw new IllegalArgumentException("Illegal use of function sound, animation not started");
        } else if (tokens.length < 2) {
            throw new IllegalArgumentException("Expected action (continuous, instant, stop) for function sound");
        } else {
            String playType = tokens[1];
            if (playType.equals("stop")) {
                List<BaseTween<?>> children = timeline.getChildren();
                BaseTween<?> callbackTween = timeline;
                int tweenEvent = 2;
                if (!children.isEmpty()) {
                    tweenEvent = 8;
                    callbackTween = (BaseTween<?>) children.get(children.size() - 1);
                }
                callbackTween.addCallback(tweenEvent, tween -> EmoteSound.endAll(em.activeSounds));
            } else {
                boolean repeating = playType.equals("continuous");
                if (!repeating && !playType.equals("instant")) {
                    throw new IllegalArgumentException(String.format("Invalid modifier %s for sound function", playType));
                }
                assertParamSize(tokens, 4, 6);
                String endCondition = tokens[2];
                boolean endWithSequence = endCondition.equals("section");
                if (!endWithSequence && !endCondition.equals("emote")) {
                    throw new IllegalArgumentException(String.format("Invalid modifier %s for sound function", endCondition));
                }
                String type = tokens[3];
                float volume = 1.0F;
                float pitch = 1.0F;
                try {
                    if (tokens.length >= 5) {
                        volume = Math.min(Float.parseFloat(tokens[4]), 1.5F);
                    }
                    if (tokens.length >= 6) {
                        pitch = Float.parseFloat(tokens[5]);
                    }
                } catch (NumberFormatException var18) {
                    throw new IllegalArgumentException("Illegal number in function sound", var18);
                }
                ResourceLocation soundEvent = new ResourceLocation(type);
                List<BaseTween<?>> children = timeline.getChildren();
                List<EmoteSound> sounds = Lists.newArrayList();
                float finalVolume = volume;
                float finalPitch = pitch;
                BaseTween<?> callbackTween = timeline;
                int tweenEvent = 2;
                if (!children.isEmpty()) {
                    tweenEvent = 8;
                    callbackTween = (BaseTween<?>) children.get(children.size() - 1);
                }
                callbackTween.addCallback(tweenEvent, tween -> EmoteSound.add(em.activeSounds, sounds, player, em, soundEvent, finalVolume, finalPitch, repeating, endWithSequence));
                timeline.addCallback(8, tween -> EmoteSound.endSection(sounds));
            }
            return timeline;
        }
    }

    private static Timeline reset(EmoteTemplate em, HumanoidModel<?> model, Timeline timeline, String[] tokens) throws IllegalArgumentException {
        if (tokens.length < 4) {
            throw new IllegalArgumentException(String.format("Illegal parameter amount for function reset: %d (at least 4 are required)", tokens.length));
        } else {
            String part = tokens[1];
            boolean allParts = part.equals("all");
            if (!allParts && !parts.containsKey(part)) {
                throw new IllegalArgumentException("Illegal part name for function reset: " + part);
            } else {
                String type = tokens[2];
                boolean all = type.equals("all");
                boolean rot = all || type.equals("rotation");
                boolean off = all || type.equals("offset");
                if (!rot && !off) {
                    throw new IllegalArgumentException("Illegal reset type: " + type);
                } else {
                    int partInt = allParts ? 0 : (Integer) parts.get(part);
                    float time = Float.parseFloat(tokens[3]) * em.speed;
                    if (model != null) {
                        Timeline parallel = Timeline.createParallel();
                        int lower = allParts ? 0 : partInt + (rot ? 0 : 3);
                        int upper = allParts ? 21 : partInt + (off ? 21 : 3);
                        for (int i = lower; i < upper; i++) {
                            int piece = i / 3 * 3;
                            if (em.usedParts.contains(piece)) {
                                parallel.push(Tween.to(model, i, time));
                            }
                        }
                        timeline.push(parallel);
                    }
                    return timeline;
                }
            }
        }
    }

    private static Timeline pause(EmoteTemplate em, Timeline timeline, String[] tokens) throws IllegalArgumentException {
        assertParamSize(tokens, 2);
        float ms = Float.parseFloat(tokens[1]) * em.speed;
        return timeline.pushPause(ms);
    }

    private static Timeline yoyo(EmoteTemplate em, Timeline timeline, String[] tokens) throws IllegalArgumentException {
        assertParamSize(tokens, 3);
        int times = Integer.parseInt(tokens[1]);
        float delay = Float.parseFloat(tokens[2]) * em.speed;
        return timeline.repeatYoyo(times, delay);
    }

    private static Timeline repeat(EmoteTemplate em, Timeline timeline, String[] tokens) throws IllegalArgumentException {
        assertParamSize(tokens, 3);
        int times = Integer.parseInt(tokens[1]);
        float delay = Float.parseFloat(tokens[2]) * em.speed;
        return timeline.repeat(times, delay);
    }

    private static void assertParamSize(String[] tokens, int expect) throws IllegalArgumentException {
        if (tokens.length != expect) {
            throw new IllegalArgumentException(String.format("Illegal parameter amount for function %s: %d (expected %d)", tokens[0], tokens.length, expect));
        }
    }

    private static void assertParamSize(String[] tokens, int expectMin, int expectMax) throws IllegalArgumentException {
        if (tokens.length > expectMax || tokens.length < expectMin) {
            throw new IllegalArgumentException(String.format("Illegal parameter amount for function %s: %d (expected between %d and %d)", tokens[0], tokens.length, expectMin, expectMax));
        }
    }

    private static void assertParamSize(String mod, String[] tokens, int expect, int startingFrom) throws IllegalArgumentException {
        if (tokens.length - startingFrom < expect) {
            throw new IllegalArgumentException(String.format("Illegal parameter amount for move modifier %s: %d (expected at least %d)", mod, tokens.length, expect));
        }
    }

    public boolean usesBodyPart(int part) {
        return this.usedParts.contains(part);
    }

    static {
        functions.put("name", (EmoteTemplate.Function) (em, model, player, timeline, tokens) -> name(em, timeline, tokens));
        functions.put("use", (EmoteTemplate.Function) (em, model, player, timeline, tokens) -> use(em, timeline, tokens));
        functions.put("unit", (EmoteTemplate.Function) (em, model, player, timeline, tokens) -> unit(em, timeline, tokens));
        functions.put("animation", (EmoteTemplate.Function) (em, model, player, timeline, tokens) -> animation(em, timeline, tokens));
        functions.put("section", (EmoteTemplate.Function) (em, model, player, timeline, tokens) -> section(em, timeline, tokens));
        functions.put("end", (EmoteTemplate.Function) (em, model, player, timeline, tokens) -> end(em, timeline, tokens));
        functions.put("move", (EmoteTemplate.Function) (em, model, player, timeline, tokens) -> move(em, model, timeline, tokens));
        functions.put("reset", (EmoteTemplate.Function) (em, model, player, timeline, tokens) -> reset(em, model, timeline, tokens));
        functions.put("pause", (EmoteTemplate.Function) (em, model, player, timeline, tokens) -> pause(em, timeline, tokens));
        functions.put("yoyo", (EmoteTemplate.Function) (em, model, player, timeline, tokens) -> yoyo(em, timeline, tokens));
        functions.put("repeat", (EmoteTemplate.Function) (em, model, player, timeline, tokens) -> repeat(em, timeline, tokens));
        functions.put("tier", (EmoteTemplate.Function) (em, model, player, timeline, tokens) -> tier(em, timeline, tokens));
        functions.put("sound", (EmoteTemplate.Function) (em, model, player, timeline, tokens) -> sound(em, player, timeline, tokens));
        Class<?> clazz = ModelAccessor.class;
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            if (f.getType() == int.class) {
                int modifiers = f.getModifiers();
                if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)) {
                    try {
                        int val = f.getInt(null);
                        String name = f.getName().toLowerCase(Locale.ROOT);
                        if (name.matches("^.+?_[xyz]$")) {
                            tweenables.put(name, val);
                        } else {
                            parts.put(name, val);
                        }
                    } catch (Exception var10) {
                        Quark.LOG.error("Failed while attempting to set up model parameters for emotes", var10);
                    }
                }
            }
        }
        clazz = TweenEquations.class;
        fields = clazz.getDeclaredFields();
        for (Field fx : fields) {
            String name = fx.getName().replaceAll("[A-Z]", "_$0").substring(5).toLowerCase(Locale.ROOT);
            try {
                TweenEquation eq = (TweenEquation) fx.get(null);
                equations.put(name, eq);
                if (name.equals("none")) {
                    equations.put("linear", eq);
                }
            } catch (Exception var9) {
                Quark.LOG.error("Failed while attempting to set up tween equations for emotes", var9);
            }
        }
    }

    private interface Function {

        Timeline invoke(EmoteTemplate var1, HumanoidModel<?> var2, Player var3, Timeline var4, String[] var5) throws IllegalArgumentException;
    }
}