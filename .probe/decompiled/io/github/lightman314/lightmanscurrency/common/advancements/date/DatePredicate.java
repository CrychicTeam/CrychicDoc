package io.github.lightman314.lightmanscurrency.common.advancements.date;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.time.LocalDate;
import javax.annotation.Nonnull;
import net.minecraft.util.GsonHelper;

public final class DatePredicate {

    public final int month;

    public final int date;

    public DatePredicate(int month, int date) {
        this.month = month;
        if (this.month >= 1 && this.month <= 12) {
            this.date = date;
            if (this.date < 1 || this.date > 31) {
                throw new IllegalArgumentException("Day must be between 1-31!");
            }
        } else {
            throw new IllegalArgumentException("Month must be between 1-12!");
        }
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("month", this.month);
        json.addProperty("day", this.date);
        return json;
    }

    public static DatePredicate fromJson(@Nonnull JsonElement element) {
        JsonObject json = GsonHelper.convertToJsonObject(element, "date data");
        return new DatePredicate(GsonHelper.getAsInt(json, "month"), GsonHelper.getAsInt(json, "day"));
    }

    private boolean isAfter(@Nonnull DatePredicate start) {
        if (this.month > start.month) {
            return true;
        } else {
            return this.month == start.month ? this.date >= start.date : false;
        }
    }

    public boolean isAfter(@Nonnull LocalDate date) {
        if (this.month > date.getMonthValue()) {
            return true;
        } else {
            return this.month == date.getMonthValue() ? this.date >= date.getDayOfMonth() : false;
        }
    }

    public boolean isBefore(@Nonnull LocalDate date) {
        if (this.month < date.getMonthValue()) {
            return true;
        } else {
            return this.month == date.getMonthValue() ? this.date <= date.getDayOfMonth() : false;
        }
    }

    public static boolean isInRange(@Nonnull DatePredicate start, @Nonnull DatePredicate end) {
        return isInRange(LocalDate.now(), start, end);
    }

    public static boolean isInRange(@Nonnull LocalDate date, @Nonnull DatePredicate start, @Nonnull DatePredicate end) {
        return end.isAfter(start) ? start.isBefore(date) && end.isAfter(date) : start.isBefore(date) || end.isAfter(date);
    }
}