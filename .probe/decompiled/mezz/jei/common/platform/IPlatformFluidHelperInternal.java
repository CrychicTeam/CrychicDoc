package mezz.jei.common.platform;

import java.util.List;
import java.util.Optional;
import mezz.jei.api.helpers.IPlatformFluidHelper;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;

public interface IPlatformFluidHelperInternal<T> extends IPlatformFluidHelper<T> {

    IIngredientSubtypeInterpreter<T> getAllNbtSubtypeInterpreter();

    IIngredientRenderer<T> createRenderer(long var1, boolean var3, int var4, int var5);

    Optional<TextureAtlasSprite> getStillFluidSprite(T var1);

    Component getDisplayName(T var1);

    int getColorTint(T var1);

    long getAmount(T var1);

    Optional<CompoundTag> getTag(T var1);

    List<Component> getTooltip(T var1, TooltipFlag var2);

    T copy(T var1);

    T normalize(T var1);

    Optional<T> getContainedFluid(ITypedIngredient<?> var1);
}