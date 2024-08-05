package me.srrapero720.embeddiumplus.foundation.fps;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class FPSDisplay {

    private StringBuilder builder = new StringBuilder();

    private boolean split = false;

    private boolean divisor = false;

    public FPSDisplay append(String param) {
        if (this.split) {
            this.builder.append(" - ");
        }
        if (this.divisor) {
            this.builder.append(" | ");
        }
        this.builder.append(param);
        this.split = false;
        this.divisor = true;
        return this;
    }

    public FPSDisplay append(ChatFormatting formatting) {
        return this.append(formatting.toString());
    }

    public FPSDisplay add(int param) {
        this.builder.append(param);
        return this;
    }

    public FPSDisplay add(String param) {
        this.builder.append(param);
        return this;
    }

    public FPSDisplay add(Component component) {
        return this.add(component.getString());
    }

    public FPSDisplay add(ChatFormatting formatting) {
        return this.add(formatting.toString());
    }

    public void split() {
        this.split = true;
        this.divisor = false;
    }

    public boolean isEmpty() {
        return this.builder.isEmpty();
    }

    public void release() {
        this.builder = new StringBuilder();
        this.split = false;
        this.divisor = false;
    }

    public String toString() {
        return this.builder.toString();
    }
}