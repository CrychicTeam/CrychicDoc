package snownee.lychee.mixin;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Marker;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import snownee.lychee.core.ActionRuntime;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.post.Delay;
import snownee.lychee.core.recipe.ILycheeRecipe;
import snownee.lychee.fragment.Fragments;
import snownee.lychee.util.CommonProxy;

@Mixin({ Marker.class })
public class MarkerMixin implements Delay.LycheeMarker {

    @Unique
    private int lychee$ticks;

    @Unique
    private ILycheeRecipe<?> lychee$recipe;

    @Unique
    private LycheeContext lychee$ctx;

    @Override
    public void lychee$setContext(ILycheeRecipe<?> recipe, LycheeContext ctx) {
        this.lychee$ctx = ctx;
        this.lychee$recipe = recipe;
    }

    @Override
    public LycheeContext lychee$getContext() {
        return this.lychee$ctx;
    }

    @Override
    public void lychee$addDelay(int delay) {
        this.lychee$ticks += delay;
    }

    @Inject(at = { @At("HEAD") }, method = { "tick" })
    private void lychee_tick(CallbackInfo ci) {
        if (this.lychee$recipe != null && this.lychee$ctx != null) {
            if (this.lychee$ticks-- <= 0) {
                this.lychee$ctx.runtime.state = ActionRuntime.State.RUNNING;
                this.lychee$ctx.runtime.run(this.lychee$recipe, this.lychee$ctx);
                if (this.lychee$ctx.runtime.state == ActionRuntime.State.STOPPED) {
                    this.getEntity().m_146870_();
                }
            }
        }
    }

    @Inject(at = { @At("HEAD") }, method = { "readAdditionalSaveData" })
    private void lychee_readAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        if (compoundTag.contains("lychee:ctx")) {
            this.lychee$ticks = compoundTag.getInt("lychee:ticks");
            this.lychee$ctx = LycheeContext.load((JsonObject) Fragments.GSON.fromJson(compoundTag.getString("lychee:ctx"), JsonObject.class), this);
            ResourceLocation recipeId = ResourceLocation.tryParse(compoundTag.getString("lychee:recipe"));
            Recipe<?> recipe = CommonProxy.recipe(recipeId);
            if (recipe instanceof ILycheeRecipe) {
                this.lychee$recipe = (ILycheeRecipe<?>) recipe;
            }
        }
        if (this.lychee$recipe == null && this.getEntity().m_8077_() && "lychee".equals(this.getEntity().m_7770_().getString())) {
            this.getEntity().m_146870_();
        }
    }

    @Inject(at = { @At("HEAD") }, method = { "addAdditionalSaveData" })
    private void lychee_addAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        if (this.lychee$recipe != null && this.lychee$ctx != null) {
            compoundTag.putInt("lychee:ticks", this.lychee$ticks);
            compoundTag.putString("lychee:ctx", this.lychee$ctx.save().toString());
            compoundTag.putString("lychee:recipe", this.lychee$recipe.lychee$getId().toString());
        }
    }
}