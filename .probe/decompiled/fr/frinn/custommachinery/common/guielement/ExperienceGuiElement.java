package fr.frinn.custommachinery.common.guielement;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.guielement.GuiElementType;
import fr.frinn.custommachinery.api.machine.MachineTile;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.impl.guielement.AbstractGuiElement;
import fr.frinn.custommachinery.impl.guielement.AbstractTexturedGuiElement;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class ExperienceGuiElement extends AbstractTexturedGuiElement {

    public static final ResourceLocation BASE_TEXTURE = new ResourceLocation("custommachinery", "textures/gui/base_xp.png");

    public static final ResourceLocation BASE_TEXTURE_HOVERED = new ResourceLocation("custommachinery", "textures/gui/base_xp_hovered.png");

    private final ExperienceGuiElement.DisplayMode displayMode;

    private final ExperienceGuiElement.Mode mode;

    public static final NamedCodec<ExperienceGuiElement> CODEC = NamedCodec.record(experienceGuiElement -> experienceGuiElement.group(makePropertiesCodec(BASE_TEXTURE, BASE_TEXTURE_HOVERED).forGetter(AbstractGuiElement::getProperties), NamedCodec.enumCodec(ExperienceGuiElement.DisplayMode.class).optionalFieldOf("display", ExperienceGuiElement.DisplayMode.LEVEL).forGetter(element -> element.displayMode), NamedCodec.enumCodec(ExperienceGuiElement.Mode.class).fieldOf("mode").forGetter(element -> element.mode)).apply(experienceGuiElement, ExperienceGuiElement::new), "Experience gui element");

    public ExperienceGuiElement(AbstractGuiElement.Properties properties, ExperienceGuiElement.DisplayMode displayMode, ExperienceGuiElement.Mode mode) {
        super(properties);
        this.displayMode = displayMode;
        this.mode = mode;
    }

    @Override
    public GuiElementType<ExperienceGuiElement> getType() {
        return (GuiElementType<ExperienceGuiElement>) Registration.EXPERIENCE_GUI_ELEMENT.get();
    }

    @Override
    public void handleClick(byte button, MachineTile tile, AbstractContainerMenu container, ServerPlayer player) {
        super.handleClick(button, tile, container, player);
        tile.getComponentManager().getComponent((MachineComponentType) Registration.EXPERIENCE_MACHINE_COMPONENT.get()).ifPresent(component -> {
            switch(this.mode) {
                case INPUT_ONE:
                    component.addLevelToPlayer(-1, player);
                    break;
                case INPUT_TEN:
                    component.addLevelToPlayer(-10, player);
                    break;
                case INPUT_ALL:
                    component.addAllLevelToPlayer(false, player);
                    break;
                case OUTPUT_ONE:
                    component.addLevelToPlayer(1, player);
                    break;
                case OUTPUT_TEN:
                    component.addLevelToPlayer(10, player);
                    break;
                case OUTPUT_ALL:
                    component.addAllLevelToPlayer(true, player);
            }
        });
    }

    public ExperienceGuiElement.DisplayMode getDisplayMode() {
        return this.displayMode;
    }

    public ExperienceGuiElement.Mode getMode() {
        return this.mode;
    }

    public static enum DisplayMode {

        LITERAL, LEVEL, BOTH;

        public boolean isLiteral() {
            return this == LITERAL;
        }

        public boolean isLevel() {
            return this == LEVEL;
        }

        public boolean isBoth() {
            return this == BOTH;
        }

        public static ExperienceGuiElement.DisplayMode of(String value) {
            if (value.equalsIgnoreCase("literal")) {
                return LITERAL;
            } else if (value.equalsIgnoreCase("level")) {
                return LEVEL;
            } else {
                return value.equalsIgnoreCase("both") ? BOTH : null;
            }
        }
    }

    public static enum Mode {

        INPUT_ONE,
        INPUT_TEN,
        INPUT_ALL,
        OUTPUT_ONE,
        OUTPUT_TEN,
        OUTPUT_ALL,
        DISPLAY,
        DISPLAY_BAR;

        public boolean isInputOne() {
            return this == INPUT_ONE;
        }

        public boolean isInputAll() {
            return this == INPUT_ALL;
        }

        public boolean isInput() {
            return this.isInputOne() || this.isInputAll();
        }

        public boolean isOutputAll() {
            return this == OUTPUT_ALL;
        }

        public boolean isOutputOne() {
            return this == OUTPUT_ONE;
        }

        public boolean isOutput() {
            return this.isOutputOne() || this.isOutputAll();
        }

        public boolean isDisplay() {
            return this == DISPLAY || this == DISPLAY_BAR;
        }

        public boolean isDisplayBar() {
            return this == DISPLAY_BAR;
        }

        public Component title() {
            return switch(this) {
                case INPUT_ONE ->
                    Component.translatable("custommachinery.gui.element.experience.input_one");
                case INPUT_TEN ->
                    Component.translatable("custommachinery.gui.element.experience.input_ten");
                case INPUT_ALL ->
                    Component.translatable("custommachinery.gui.element.experience.input_all");
                case OUTPUT_ONE ->
                    Component.translatable("custommachinery.gui.element.experience.output_one");
                case OUTPUT_TEN ->
                    Component.translatable("custommachinery.gui.element.experience.output_ten");
                case OUTPUT_ALL ->
                    Component.translatable("custommachinery.gui.element.experience.output_all");
                case DISPLAY, DISPLAY_BAR ->
                    Component.empty();
            };
        }
    }
}