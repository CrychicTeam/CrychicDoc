package dev.xkmc.modulargolems.content.capability;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class SquadConfig {

    @SerialField
    protected UUID captainId = null;

    @SerialField
    protected double radius = 0.0;

    public double getRadius() {
        return this.radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Nullable
    public UUID getCaptainId() {
        return this.captainId;
    }

    public void setCaptainId(@Nullable UUID captainId) {
        this.captainId = captainId;
    }
}