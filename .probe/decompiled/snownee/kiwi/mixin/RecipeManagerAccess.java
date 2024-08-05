package snownee.kiwi.mixin;

import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ RecipeManager.class })
public interface RecipeManagerAccess {

    @Accessor(remap = false)
    ICondition.IContext getContext();
}