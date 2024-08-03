package snownee.lychee.mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import snownee.lychee.LycheeTags;
import snownee.lychee.core.recipe.LycheeCounter;

@Mixin({ ItemEntity.class })
public class ItemEntityMixin implements LycheeCounter {

    @Unique
    private ResourceLocation lychee$recipeId;

    @Unique
    private int lychee$count;

    @Inject(at = { @At("HEAD") }, method = { "fireImmune" }, cancellable = true)
    private void lychee_fireImmune(CallbackInfoReturnable<Boolean> ci) {
        if (((ItemEntity) this).getItem().is(LycheeTags.FIRE_IMMUNE)) {
            ci.setReturnValue(true);
        }
    }

    @Override
    public void lychee$setRecipeId(@Nullable ResourceLocation id) {
        this.lychee$recipeId = id;
    }

    @Nullable
    @Override
    public ResourceLocation lychee$getRecipeId() {
        return this.lychee$recipeId;
    }

    @Override
    public void lychee$setCount(int count) {
        this.lychee$count = count;
    }

    @Override
    public int lychee$getCount() {
        return this.lychee$count;
    }
}