package io.github.lightman314.lightmanscurrency.client.gui.widget;

import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.money.input.MoneyValueWidget;
import io.github.lightman314.lightmanscurrency.client.gui.easy.WidgetAddon;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.PlainButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyWidgetWithChildren;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import io.github.lightman314.lightmanscurrency.client.util.TextRenderUtil;
import io.github.lightman314.lightmanscurrency.util.TimeUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nonnull;

public class TimeInputWidget extends EasyWidgetWithChildren {

    private final List<TimeUtil.TimeUnit> relevantUnits;

    private final int spacing;

    public long maxDuration = Long.MAX_VALUE;

    public long minDuration = 0L;

    private final Consumer<TimeUtil.TimeData> timeConsumer;

    long days = 0L;

    long hours = 0L;

    long minutes = 0L;

    long seconds = 0L;

    private final List<EasyButton> buttons = new ArrayList();

    public TimeUtil.TimeData getTime() {
        return new TimeUtil.TimeData(this.days, this.hours, this.minutes, this.seconds);
    }

    public TimeInputWidget(ScreenPosition pos, int spacing, TimeUtil.TimeUnit largestUnit, TimeUtil.TimeUnit smallestUnit, Consumer<TimeUtil.TimeData> timeConsumer) {
        this(pos.x, pos.y, spacing, largestUnit, smallestUnit, timeConsumer);
    }

    public TimeInputWidget(int x, int y, int spacing, TimeUtil.TimeUnit largestUnit, TimeUtil.TimeUnit smallestUnit, Consumer<TimeUtil.TimeData> timeConsumer) {
        super(x, y, 0, 0);
        this.timeConsumer = timeConsumer;
        this.relevantUnits = this.getRelevantUnits(largestUnit, smallestUnit);
        this.spacing = spacing;
    }

    public TimeInputWidget withAddons(WidgetAddon... addons) {
        this.withAddonsInternal(addons);
        return this;
    }

    @Override
    public void addChildren() {
        for (int i = 0; i < this.relevantUnits.size(); i++) {
            TimeUtil.TimeUnit unit = (TimeUtil.TimeUnit) this.relevantUnits.get(i);
            int xPos = this.m_252754_() + (20 + this.spacing) * i;
            this.buttons.add(this.addChild(new PlainButton(xPos, this.m_252907_(), b -> this.addTime(unit), MoneyValueWidget.SPRITE_UP_ARROW)));
            this.buttons.add(this.addChild(new PlainButton(xPos, this.m_252907_() + 23, b -> this.removeTime(unit), MoneyValueWidget.SPRITE_DOWN_ARROW)));
        }
    }

    public void setTime(long milliseconds) {
        this.setTimeInternal(milliseconds);
        this.validateTime();
        this.timeConsumer.accept(this.getTime());
    }

    public void setTime(TimeUtil.TimeData time) {
        this.setTimeInternal(time);
        this.validateTime();
        this.timeConsumer.accept(this.getTime());
    }

    public void setTime(long days, long hours, long minutes, long seconds) {
        this.setTimeInternal(days, hours, minutes, seconds);
        this.validateTime();
        this.timeConsumer.accept(this.getTime());
    }

    private void setTimeInternal(long milliseconds) {
        this.setTimeInternal(new TimeUtil.TimeData(milliseconds));
    }

    private void setTimeInternal(TimeUtil.TimeData time) {
        this.setTimeInternal(time.days, time.hours, time.minutes, time.seconds);
    }

    private void setTimeInternal(long days, long hours, long minutes, long seconds) {
        this.days = days;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        if (!this.validUnit(TimeUtil.TimeUnit.DAY)) {
            this.hours = this.hours + this.days * 24L;
            this.days = 0L;
        }
        if (!this.validUnit(TimeUtil.TimeUnit.HOUR)) {
            this.minutes = this.minutes + this.hours * 60L;
            this.hours = 0L;
        }
        if (!this.validUnit(TimeUtil.TimeUnit.MINUTE)) {
            this.seconds = this.seconds + this.minutes * 60L;
            this.minutes = 0L;
        }
        if (!this.validUnit(TimeUtil.TimeUnit.SECOND)) {
            this.seconds = 0L;
        }
    }

    private boolean validUnit(TimeUtil.TimeUnit unit) {
        return this.relevantUnits.contains(unit);
    }

    private void addTime(TimeUtil.TimeUnit unit) {
        switch(unit) {
            case DAY:
                this.days++;
                break;
            case HOUR:
                this.hours++;
                if (this.hours >= 24L && this.validUnit(TimeUtil.TimeUnit.DAY)) {
                    this.days = this.days + this.hours / 24L;
                    this.hours %= 24L;
                }
                break;
            case MINUTE:
                this.minutes++;
                if (this.minutes >= 60L && this.validUnit(TimeUtil.TimeUnit.HOUR)) {
                    this.hours = this.hours + this.minutes / 60L;
                    this.minutes %= 60L;
                }
                break;
            case SECOND:
                this.seconds++;
                if (this.seconds >= 60L && this.validUnit(TimeUtil.TimeUnit.SECOND)) {
                    this.minutes = this.minutes + this.seconds / 60L;
                    this.seconds %= 60L;
                }
        }
        this.validateTime();
        this.timeConsumer.accept(this.getTime());
    }

    private void removeTime(TimeUtil.TimeUnit unit) {
        this.removeTimeInternal(unit);
        this.validateTime();
        this.timeConsumer.accept(this.getTime());
    }

    private void removeTimeInternal(TimeUtil.TimeUnit unit) {
        switch(unit) {
            case DAY:
                this.days = Math.max(0L, this.days - 1L);
                break;
            case HOUR:
                this.hours--;
                if (this.hours < 0L) {
                    if (this.days > 0L) {
                        this.removeTimeInternal(TimeUtil.TimeUnit.DAY);
                        this.hours += 24L;
                    } else {
                        this.hours = 0L;
                    }
                }
                break;
            case MINUTE:
                this.minutes--;
                if (this.minutes < 0L) {
                    if (this.hours <= 0L && this.days <= 0L) {
                        this.minutes = 0L;
                    } else {
                        this.removeTimeInternal(TimeUtil.TimeUnit.HOUR);
                        this.minutes += 60L;
                    }
                }
                break;
            case SECOND:
                this.seconds--;
                if (this.seconds < 0L) {
                    if (this.minutes <= 0L && this.hours <= 0L && this.days <= 0L) {
                        this.seconds = 0L;
                    } else {
                        this.removeTimeInternal(TimeUtil.TimeUnit.MINUTE);
                        this.seconds += 60L;
                    }
                }
        }
    }

    private void validateTime() {
        long duration = this.getTime().miliseconds;
        if (duration > this.maxDuration) {
            this.setTimeInternal(this.maxDuration);
        }
        if (duration < this.minDuration) {
            this.setTimeInternal(this.minDuration);
        }
    }

    private List<TimeUtil.TimeUnit> getRelevantUnits(TimeUtil.TimeUnit largestUnit, TimeUtil.TimeUnit smallestUnit) {
        List<TimeUtil.TimeUnit> results = new ArrayList();
        List<TimeUtil.TimeUnit> units = TimeUtil.TimeUnit.UNITS_LARGE_TO_SMALL;
        int startIndex = units.indexOf(largestUnit);
        if (startIndex < 0) {
            throw new RuntimeException("TimeUnit '" + largestUnit + "' could not be found on the TimeUnit list!");
        } else {
            for (int i = startIndex; i < units.size(); i++) {
                TimeUtil.TimeUnit unit = (TimeUtil.TimeUnit) units.get(i);
                results.add(unit);
                if (unit == smallestUnit) {
                    break;
                }
            }
            return results;
        }
    }

    @Override
    protected void renderTick() {
        for (EasyButton b : this.buttons) {
            b.f_93623_ = this.f_93623_;
            b.f_93624_ = this.f_93624_;
        }
    }

    @Override
    public void renderWidget(@Nonnull EasyGuiGraphics gui) {
        for (int i = 0; i < this.relevantUnits.size(); i++) {
            TextRenderUtil.drawCenteredText(gui, this.getTime().getUnitString((TimeUtil.TimeUnit) this.relevantUnits.get(i), true), (20 + this.spacing) * i + 10, 12, 16777215);
        }
    }
}