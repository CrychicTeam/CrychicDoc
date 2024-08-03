package snownee.jade.impl.ui;

import java.util.Objects;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.fluid.JadeFluidObject;
import snownee.jade.api.ui.Element;
import snownee.jade.overlay.DisplayHelper;

public class FluidStackElement extends Element {

    private static final Vec2 DEFAULT_SIZE = new Vec2(16.0F, 16.0F);

    private final JadeFluidObject fluid;

    public FluidStackElement(JadeFluidObject fluid) {
        this.fluid = fluid;
        Objects.requireNonNull(fluid);
    }

    @Override
    public Vec2 getSize() {
        return DEFAULT_SIZE;
    }

    @Override
    public void render(GuiGraphics guiGraphics, float x, float y, float maxX, float maxY) {
        Vec2 size = this.getCachedSize();
        DisplayHelper.INSTANCE.drawFluid(guiGraphics, x, y, this.fluid, size.x, size.y, JadeFluidObject.bucketVolume());
    }

    @Nullable
    @Override
    public String getMessage() {
        return null;
    }
}