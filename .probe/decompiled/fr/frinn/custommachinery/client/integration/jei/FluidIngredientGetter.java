package fr.frinn.custommachinery.client.integration.jei;

import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.common.component.FluidMachineComponent;
import fr.frinn.custommachinery.common.guielement.FluidGuiElement;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.impl.guielement.AbstractGuiElementWidget;
import fr.frinn.custommachinery.impl.integration.jei.WidgetToJeiIngredientRegistry;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.helpers.IPlatformFluidHelper;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.runtime.IClickableIngredient;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.client.renderer.Rect2i;
import org.jetbrains.annotations.Nullable;

public class FluidIngredientGetter implements WidgetToJeiIngredientRegistry.IngredientGetter<FluidGuiElement> {

    @Nullable
    @Override
    public <T> IClickableIngredient<T> getIngredient(AbstractGuiElementWidget<FluidGuiElement> widget, double mouseX, double mouseY, IJeiHelpers helpers) {
        FluidMachineComponent component = (FluidMachineComponent) widget.getScreen().getTile().getComponentManager().getComponentHandler((MachineComponentType<T>) Registration.FLUID_MACHINE_COMPONENT.get()).flatMap(fluidHandler -> fluidHandler.getComponentForID(widget.getElement().getComponentId())).orElse(null);
        if (component == null) {
            return null;
        } else {
            IIngredientManager manager = helpers.getIngredientManager();
            IPlatformFluidHelper fluidHelper = helpers.getPlatformFluidHelper();
            final ITypedIngredient ingredient = (ITypedIngredient) manager.createTypedIngredient(fluidHelper.getFluidIngredientType(), fluidHelper.create(component.getFluidStack().getFluid(), component.getFluidStack().getAmount(), component.getFluidStack().getTag())).orElse(null);
            return ingredient == null ? null : new IClickableIngredient<T>() {

                @Override
                public ITypedIngredient<T> getTypedIngredient() {
                    return ingredient;
                }

                @Override
                public Rect2i getArea() {
                    return new Rect2i(widget.m_252754_(), widget.m_252907_(), widget.m_5711_(), widget.m_93694_());
                }
            };
        }
    }
}