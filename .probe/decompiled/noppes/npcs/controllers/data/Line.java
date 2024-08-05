package noppes.npcs.controllers.data;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import noppes.npcs.api.entity.data.ILine;

public class Line implements ILine {

    protected String text = "";

    protected String sound = "";

    private boolean showText = true;

    public Line() {
    }

    public Line(String text) {
        this.text = text;
    }

    public Line copy() {
        Line line = new Line(this.text);
        line.sound = this.sound;
        line.showText = this.showText;
        return line;
    }

    public static Line formatTarget(Line line, LivingEntity entity) {
        if (entity == null) {
            return line;
        } else {
            Line line2 = line.copy();
            if (entity instanceof Player) {
                line2.text = line2.text.replace("@target", ((Player) entity).getDisplayName().getString());
            } else {
                line2.text = line2.text.replace("@target", entity.m_7755_().getString());
            }
            return line;
        }
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public void setText(String text) {
        if (text == null) {
            text = "";
        }
        this.text = text;
    }

    @Override
    public String getSound() {
        return this.sound;
    }

    @Override
    public void setSound(String sound) {
        if (sound == null) {
            sound = "";
        }
        this.sound = sound;
    }

    @Override
    public boolean getShowText() {
        return this.showText;
    }

    @Override
    public void setShowText(boolean show) {
        this.showText = show;
    }
}