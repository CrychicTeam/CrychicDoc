package me.shedaniel.clothconfig2.api;

import java.util.List;
import me.shedaniel.math.Point;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;

public interface Tooltip {

    static Tooltip of(Point location, Component... text) {
        return QueuedTooltip.create(location, text);
    }

    static Tooltip of(Point location, FormattedText... text) {
        return QueuedTooltip.create(location, text);
    }

    static Tooltip of(Point location, FormattedCharSequence... text) {
        return QueuedTooltip.create(location, text);
    }

    Point getPoint();

    default int getX() {
        return this.getPoint().getX();
    }

    default int getY() {
        return this.getPoint().getY();
    }

    List<FormattedCharSequence> getText();
}