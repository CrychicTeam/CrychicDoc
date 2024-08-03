package dev.xkmc.modulargolems.content.capability;

import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public record SquadEditor(GolemConfigEditor editor) {

    @Nullable
    public UUID getCaptainId() {
        return this.editor().entry().squadConfig.getCaptainId();
    }

    public double getRadius() {
        return this.editor().entry().squadConfig.getRadius();
    }

    public void setCaptainId(@Nullable UUID captainId) {
        this.editor().entry().squadConfig.setCaptainId(captainId);
        this.editor.sync();
    }

    public void setRadius(double Radius) {
        this.editor().entry().squadConfig.setRadius(Radius);
        this.editor.sync();
    }
}