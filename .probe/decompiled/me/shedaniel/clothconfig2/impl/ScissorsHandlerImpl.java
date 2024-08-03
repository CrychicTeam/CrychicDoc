package me.shedaniel.clothconfig2.impl;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Window;
import java.util.Collections;
import java.util.List;
import me.shedaniel.clothconfig2.api.ScissorsHandler;
import me.shedaniel.clothconfig2.api.ScissorsScreen;
import me.shedaniel.math.Rectangle;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.ApiStatus.Internal;

@OnlyIn(Dist.CLIENT)
@Internal
public final class ScissorsHandlerImpl implements ScissorsHandler {

    @Internal
    public static final ScissorsHandler INSTANCE = new ScissorsHandlerImpl();

    private final List<Rectangle> scissorsAreas = Lists.newArrayList();

    @Override
    public void clearScissors() {
        this.scissorsAreas.clear();
        this.applyScissors();
    }

    @Override
    public List<Rectangle> getScissorsAreas() {
        return Collections.unmodifiableList(this.scissorsAreas);
    }

    @Override
    public void scissor(Rectangle rectangle) {
        this.scissorsAreas.add(rectangle);
        this.applyScissors();
    }

    @Override
    public void removeLastScissor() {
        if (!this.scissorsAreas.isEmpty()) {
            this.scissorsAreas.remove(this.scissorsAreas.size() - 1);
        }
        this.applyScissors();
    }

    @Override
    public void applyScissors() {
        if (!this.scissorsAreas.isEmpty()) {
            Rectangle r = ((Rectangle) this.scissorsAreas.get(0)).clone();
            for (int i = 1; i < this.scissorsAreas.size(); i++) {
                Rectangle r1 = (Rectangle) this.scissorsAreas.get(i);
                if (!r.intersects(r1)) {
                    if (Minecraft.getInstance().screen instanceof ScissorsScreen) {
                        this._applyScissor(((ScissorsScreen) Minecraft.getInstance().screen).handleScissor(new Rectangle()));
                    } else {
                        this._applyScissor(new Rectangle());
                    }
                    return;
                }
                r.setBounds(r.intersection(r1));
            }
            r.setBounds(Math.min(r.x, r.x + r.width), Math.min(r.y, r.y + r.height), Math.abs(r.width), Math.abs(r.height));
            if (Minecraft.getInstance().screen instanceof ScissorsScreen) {
                this._applyScissor(((ScissorsScreen) Minecraft.getInstance().screen).handleScissor(r));
            } else {
                this._applyScissor(r);
            }
        } else if (Minecraft.getInstance().screen instanceof ScissorsScreen) {
            this._applyScissor(((ScissorsScreen) Minecraft.getInstance().screen).handleScissor(null));
        } else {
            this._applyScissor(null);
        }
    }

    public void _applyScissor(Rectangle r) {
        if (r != null) {
            GlStateManager._enableScissorTest();
            if (r.isEmpty()) {
                GlStateManager._scissorBox(0, 0, 0, 0);
            } else {
                Window window = Minecraft.getInstance().getWindow();
                double scaleFactor = window.getGuiScale();
                GlStateManager._scissorBox((int) ((double) r.x * scaleFactor), (int) ((double) (window.getGuiScaledHeight() - r.height - r.y) * scaleFactor), (int) ((double) r.width * scaleFactor), (int) ((double) r.height * scaleFactor));
            }
        } else {
            GlStateManager._disableScissorTest();
        }
    }
}