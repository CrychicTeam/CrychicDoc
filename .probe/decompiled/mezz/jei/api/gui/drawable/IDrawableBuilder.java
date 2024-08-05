package mezz.jei.api.gui.drawable;

import mezz.jei.api.gui.ITickTimer;

public interface IDrawableBuilder {

    IDrawableBuilder setTextureSize(int var1, int var2);

    IDrawableBuilder addPadding(int var1, int var2, int var3, int var4);

    IDrawableBuilder trim(int var1, int var2, int var3, int var4);

    IDrawableStatic build();

    IDrawableAnimated buildAnimated(int var1, IDrawableAnimated.StartDirection var2, boolean var3);

    IDrawableAnimated buildAnimated(ITickTimer var1, IDrawableAnimated.StartDirection var2);
}