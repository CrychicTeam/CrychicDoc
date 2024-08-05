package mezz.jei.library.load.registration;

import mezz.jei.api.ingredients.IIngredientTypeWithSubtypes;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.common.platform.IPlatformFluidHelperInternal;
import mezz.jei.common.platform.Services;
import mezz.jei.common.util.ErrorUtil;
import mezz.jei.library.ingredients.subtypes.SubtypeInterpreters;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SubtypeRegistration implements ISubtypeRegistration {

    private static final Logger LOGGER = LogManager.getLogger();

    private final SubtypeInterpreters interpreters = new SubtypeInterpreters();

    @Override
    public void useNbtForSubtypes(Item... items) {
        for (Item item : items) {
            this.registerSubtypeInterpreter(item, SubtypeRegistration.AllNbt.INSTANCE);
        }
    }

    @Override
    public void useNbtForSubtypes(Fluid... fluids) {
        IPlatformFluidHelperInternal<?> fluidHelper = Services.PLATFORM.getFluidHelper();
        this.useNbtForSubtypes(fluidHelper, fluids);
    }

    private <T> void useNbtForSubtypes(IPlatformFluidHelperInternal<T> fluidHelper, Fluid... fluids) {
        IIngredientTypeWithSubtypes<Fluid, T> type = fluidHelper.getFluidIngredientType();
        IIngredientSubtypeInterpreter<T> allNbt = fluidHelper.getAllNbtSubtypeInterpreter();
        for (Fluid fluid : fluids) {
            this.registerSubtypeInterpreter(type, fluid, allNbt);
        }
    }

    @Override
    public <B, I> void registerSubtypeInterpreter(IIngredientTypeWithSubtypes<B, I> type, B base, IIngredientSubtypeInterpreter<I> interpreter) {
        ErrorUtil.checkNotNull(type, "type");
        ErrorUtil.checkNotNull(base, "base");
        ErrorUtil.checkNotNull(interpreter, "interpreter");
        Class<? extends B> ingredientBaseClass = type.getIngredientBaseClass();
        if (!ingredientBaseClass.isInstance(base)) {
            throw new IllegalArgumentException(String.format("base (%s) must be an instance of %s", base.getClass(), ingredientBaseClass));
        } else if (this.interpreters.contains(type, base)) {
            LOGGER.error("An interpreter is already registered for this: {}", base, new IllegalArgumentException());
        } else {
            this.interpreters.addInterpreter(type, base, interpreter);
        }
    }

    public SubtypeInterpreters getInterpreters() {
        return this.interpreters;
    }

    private static class AllNbt implements IIngredientSubtypeInterpreter<ItemStack> {

        public static final SubtypeRegistration.AllNbt INSTANCE = new SubtypeRegistration.AllNbt();

        public String apply(ItemStack itemStack, UidContext context) {
            CompoundTag nbtTagCompound = itemStack.getTag();
            return nbtTagCompound != null && !nbtTagCompound.isEmpty() ? nbtTagCompound.toString() : "";
        }
    }
}