package com.simibubi.create.content.trains.display;

import com.google.common.base.Strings;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.NBTHelper;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class FlapDisplaySection {

    static final Map<String, String[]> LOADED_FLAP_CYCLES = new HashMap();

    static Random r = new Random();

    public static final float MONOSPACE = 7.0F;

    public static final float WIDE_MONOSPACE = 9.0F;

    float size;

    boolean singleFlap;

    boolean hasGap;

    boolean rightAligned;

    boolean wideFlaps;

    boolean sendTransition;

    String cycle;

    Component component;

    String[] cyclingOptions;

    boolean[] spinning;

    int spinningTicks;

    String text;

    public FlapDisplaySection(float width, String cycle, boolean singleFlap, boolean hasGap) {
        this.size = width;
        this.cycle = cycle;
        this.hasGap = hasGap;
        this.singleFlap = singleFlap;
        this.spinning = new boolean[singleFlap ? 1 : Math.max(0, (int) (width / 7.0F))];
        this.text = Strings.repeat(" ", this.spinning.length);
        this.component = null;
    }

    public FlapDisplaySection rightAligned() {
        this.rightAligned = true;
        return this;
    }

    public FlapDisplaySection wideFlaps() {
        this.wideFlaps = true;
        return this;
    }

    public void setText(Component component) {
        this.component = component;
        this.sendTransition = true;
    }

    public void refresh(boolean transition) {
        if (this.component != null) {
            String newText = this.component.getString();
            if (!this.singleFlap) {
                if (this.rightAligned) {
                    newText = newText.trim();
                }
                newText = newText.toUpperCase(Locale.ROOT);
                newText = newText.substring(0, Math.min(this.spinning.length, newText.length()));
                String whitespace = Strings.repeat(" ", this.spinning.length - newText.length());
                newText = this.rightAligned ? whitespace + newText : newText + whitespace;
                if (!this.text.isEmpty()) {
                    for (int i = 0; i < this.spinning.length; i++) {
                        this.spinning[i] = this.spinning[i] | (transition && this.text.charAt(i) != newText.charAt(i));
                    }
                }
            } else if (!this.text.isEmpty()) {
                this.spinning[0] = this.spinning[0] | (transition && !newText.equals(this.text));
            }
            this.text = newText;
            this.spinningTicks = 0;
        }
    }

    public int tick(boolean instant) {
        if (this.cyclingOptions == null) {
            return 0;
        } else {
            int max = Math.max(4, (int) ((float) this.cyclingOptions.length * 1.75F));
            if (this.spinningTicks > max) {
                return 0;
            } else {
                this.spinningTicks++;
                if (this.spinningTicks <= max && this.spinningTicks < 2) {
                    return this.spinningTicks == 1 ? 0 : this.spinning.length;
                } else {
                    int spinningFlaps = 0;
                    for (int i = 0; i < this.spinning.length; i++) {
                        int increasingChance = Mth.clamp(8 - this.spinningTicks, 1, 10);
                        boolean continueSpin = !instant && r.nextInt(increasingChance * max / 4) != 0;
                        continueSpin &= max > 5 || this.spinningTicks < 2;
                        this.spinning[i] = this.spinning[i] & continueSpin;
                        if (i > 0 && r.nextInt(3) > 0) {
                            this.spinning[i - 1] = this.spinning[i - 1] & continueSpin;
                        }
                        if (i < this.spinning.length - 1 && r.nextInt(3) > 0) {
                            this.spinning[i + 1] = this.spinning[i + 1] & continueSpin;
                        }
                        if (this.spinningTicks > max) {
                            this.spinning[i] = false;
                        }
                        if (this.spinning[i]) {
                            spinningFlaps++;
                        }
                    }
                    return spinningFlaps;
                }
            }
        }
    }

    public float getSize() {
        return this.size;
    }

    public CompoundTag write() {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("Width", this.size);
        tag.putString("Cycle", this.cycle);
        if (this.rightAligned) {
            NBTHelper.putMarker(tag, "RightAligned");
        }
        if (this.singleFlap) {
            NBTHelper.putMarker(tag, "SingleFlap");
        }
        if (this.hasGap) {
            NBTHelper.putMarker(tag, "Gap");
        }
        if (this.wideFlaps) {
            NBTHelper.putMarker(tag, "Wide");
        }
        if (this.component != null) {
            tag.putString("Text", Component.Serializer.toJson(this.component));
        }
        if (this.sendTransition) {
            NBTHelper.putMarker(tag, "Transition");
        }
        this.sendTransition = false;
        return tag;
    }

    public static FlapDisplaySection load(CompoundTag tag) {
        float width = tag.getFloat("Width");
        String cycle = tag.getString("Cycle");
        boolean singleFlap = tag.contains("SingleFlap");
        boolean hasGap = tag.contains("Gap");
        FlapDisplaySection section = new FlapDisplaySection(width, cycle, singleFlap, hasGap);
        section.cyclingOptions = getFlapCycle(cycle);
        section.rightAligned = tag.contains("RightAligned");
        section.wideFlaps = tag.contains("Wide");
        if (!tag.contains("Text")) {
            return section;
        } else {
            section.component = Component.Serializer.fromJson(tag.getString("Text"));
            section.refresh(tag.getBoolean("Transition"));
            return section;
        }
    }

    public void update(CompoundTag tag) {
        this.component = Component.Serializer.fromJson(tag.getString("Text"));
        if (this.cyclingOptions == null) {
            this.cyclingOptions = getFlapCycle(this.cycle);
        }
        this.refresh(tag.getBoolean("Transition"));
    }

    public boolean renderCharsIndividually() {
        return !this.singleFlap;
    }

    public Component getText() {
        return this.component;
    }

    public static String[] getFlapCycle(String key) {
        return (String[]) LOADED_FLAP_CYCLES.computeIfAbsent(key, k -> Lang.translateDirect("flap_display.cycles." + key).getString().split(";"));
    }
}