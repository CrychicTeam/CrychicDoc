package me.shedaniel.clothconfig2.api;

import java.util.List;
import me.shedaniel.clothconfig2.impl.ScissorsHandlerImpl;
import me.shedaniel.math.Rectangle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface ScissorsHandler {

    ScissorsHandler INSTANCE = ScissorsHandlerImpl.INSTANCE;

    void clearScissors();

    List<Rectangle> getScissorsAreas();

    void scissor(Rectangle var1);

    void removeLastScissor();

    void applyScissors();
}