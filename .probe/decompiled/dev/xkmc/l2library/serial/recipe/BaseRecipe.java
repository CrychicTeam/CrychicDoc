package dev.xkmc.l2library.serial.recipe;

import com.tterrag.registrate.util.entry.RegistryEntry;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class BaseRecipe<Rec extends SRec, SRec extends BaseRecipe<?, SRec, Inv>, Inv extends Container> implements Recipe<Inv> {

    private final BaseRecipe.RecType<Rec, SRec, Inv> factory;

    public ResourceLocation id;

    public BaseRecipe(ResourceLocation id, BaseRecipe.RecType<Rec, SRec, Inv> fac) {
        this.id = id;
        this.factory = fac;
    }

    @Override
    public abstract boolean matches(Inv var1, Level var2);

    @Override
    public abstract ItemStack assemble(Inv var1, RegistryAccess var2);

    @Override
    public abstract boolean canCraftInDimensions(int var1, int var2);

    @Override
    public abstract ItemStack getResultItem(RegistryAccess var1);

    @Override
    public final ResourceLocation getId() {
        return this.id;
    }

    @Override
    public final RecipeSerializer<?> getSerializer() {
        return this.factory;
    }

    @Override
    public final RecipeType<?> getType() {
        return (RecipeType<?>) this.factory.type.get();
    }

    public interface RecInv<R extends BaseRecipe<?, R, ?>> extends Container {
    }

    public static class RecType<Rec extends SRec, SRec extends BaseRecipe<?, SRec, Inv>, Inv extends Container> extends RecSerializer<Rec, Inv> {

        public final RegistryEntry<RecipeType<SRec>> type;

        public RecType(Class<Rec> rec, RegistryEntry<RecipeType<SRec>> type) {
            super(rec);
            this.type = type;
        }
    }
}