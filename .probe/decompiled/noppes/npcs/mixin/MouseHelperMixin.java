package noppes.npcs.mixin;

import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ MouseHandler.class })
public interface MouseHelperMixin {

    @Accessor("activeButton")
    int getActiveButton();

    @Accessor("mouseGrabbed")
    void setGrabbed(boolean var1);

    @Accessor("xpos")
    void setX(double var1);

    @Accessor("ypos")
    void setY(double var1);
}