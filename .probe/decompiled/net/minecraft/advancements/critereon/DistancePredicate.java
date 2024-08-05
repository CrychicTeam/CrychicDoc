package net.minecraft.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import javax.annotation.Nullable;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;

public class DistancePredicate {

    public static final DistancePredicate ANY = new DistancePredicate(MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY);

    private final MinMaxBounds.Doubles x;

    private final MinMaxBounds.Doubles y;

    private final MinMaxBounds.Doubles z;

    private final MinMaxBounds.Doubles horizontal;

    private final MinMaxBounds.Doubles absolute;

    public DistancePredicate(MinMaxBounds.Doubles minMaxBoundsDoubles0, MinMaxBounds.Doubles minMaxBoundsDoubles1, MinMaxBounds.Doubles minMaxBoundsDoubles2, MinMaxBounds.Doubles minMaxBoundsDoubles3, MinMaxBounds.Doubles minMaxBoundsDoubles4) {
        this.x = minMaxBoundsDoubles0;
        this.y = minMaxBoundsDoubles1;
        this.z = minMaxBoundsDoubles2;
        this.horizontal = minMaxBoundsDoubles3;
        this.absolute = minMaxBoundsDoubles4;
    }

    public static DistancePredicate horizontal(MinMaxBounds.Doubles minMaxBoundsDoubles0) {
        return new DistancePredicate(MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, minMaxBoundsDoubles0, MinMaxBounds.Doubles.ANY);
    }

    public static DistancePredicate vertical(MinMaxBounds.Doubles minMaxBoundsDoubles0) {
        return new DistancePredicate(MinMaxBounds.Doubles.ANY, minMaxBoundsDoubles0, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY);
    }

    public static DistancePredicate absolute(MinMaxBounds.Doubles minMaxBoundsDoubles0) {
        return new DistancePredicate(MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, minMaxBoundsDoubles0);
    }

    public boolean matches(double double0, double double1, double double2, double double3, double double4, double double5) {
        float $$6 = (float) (double0 - double3);
        float $$7 = (float) (double1 - double4);
        float $$8 = (float) (double2 - double5);
        if (!this.x.matches((double) Mth.abs($$6)) || !this.y.matches((double) Mth.abs($$7)) || !this.z.matches((double) Mth.abs($$8))) {
            return false;
        } else {
            return !this.horizontal.matchesSqr((double) ($$6 * $$6 + $$8 * $$8)) ? false : this.absolute.matchesSqr((double) ($$6 * $$6 + $$7 * $$7 + $$8 * $$8));
        }
    }

    public static DistancePredicate fromJson(@Nullable JsonElement jsonElement0) {
        if (jsonElement0 != null && !jsonElement0.isJsonNull()) {
            JsonObject $$1 = GsonHelper.convertToJsonObject(jsonElement0, "distance");
            MinMaxBounds.Doubles $$2 = MinMaxBounds.Doubles.fromJson($$1.get("x"));
            MinMaxBounds.Doubles $$3 = MinMaxBounds.Doubles.fromJson($$1.get("y"));
            MinMaxBounds.Doubles $$4 = MinMaxBounds.Doubles.fromJson($$1.get("z"));
            MinMaxBounds.Doubles $$5 = MinMaxBounds.Doubles.fromJson($$1.get("horizontal"));
            MinMaxBounds.Doubles $$6 = MinMaxBounds.Doubles.fromJson($$1.get("absolute"));
            return new DistancePredicate($$2, $$3, $$4, $$5, $$6);
        } else {
            return ANY;
        }
    }

    public JsonElement serializeToJson() {
        if (this == ANY) {
            return JsonNull.INSTANCE;
        } else {
            JsonObject $$0 = new JsonObject();
            $$0.add("x", this.x.m_55328_());
            $$0.add("y", this.y.m_55328_());
            $$0.add("z", this.z.m_55328_());
            $$0.add("horizontal", this.horizontal.m_55328_());
            $$0.add("absolute", this.absolute.m_55328_());
            return $$0;
        }
    }
}