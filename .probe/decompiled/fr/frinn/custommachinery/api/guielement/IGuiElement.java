package fr.frinn.custommachinery.api.guielement;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.machine.MachineTile;
import fr.frinn.custommachinery.impl.codec.RegistrarCodec;
import fr.frinn.custommachinery.impl.guielement.AbstractGuiElement;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;

public interface IGuiElement {

    NamedCodec<IGuiElement> CODEC = RegistrarCodec.GUI_ELEMENT.dispatch(IGuiElement::getType, GuiElementType::getCodec, "Gui Element");

    GuiElementType<? extends IGuiElement> getType();

    int getX();

    int getY();

    int getWidth();

    int getHeight();

    int getPriority();

    List<Component> getTooltips();

    String getId();

    AbstractGuiElement.Properties getProperties();

    default void handleClick(byte button, MachineTile tile, AbstractContainerMenu container, ServerPlayer player) {
    }

    default boolean showInJei() {
        return true;
    }
}