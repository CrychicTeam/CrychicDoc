package icyllis.modernui.mc.mixin;

import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Deprecated
@Mixin({ FoodData.class })
public interface AccessFoodData {

    @Accessor("exhaustionLevel")
    float getExhaustionLevel();

    @Accessor("exhaustionLevel")
    void setExhaustionLevel(float var1);
}