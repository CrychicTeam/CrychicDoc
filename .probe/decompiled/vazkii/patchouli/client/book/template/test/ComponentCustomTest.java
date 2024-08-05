package vazkii.patchouli.client.book.template.test;

import java.util.function.UnaryOperator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.PatchouliAPI;

public class ComponentCustomTest implements ICustomComponent {

    private transient int x;

    private transient int y;

    private transient String text = "";

    @Override
    public void build(int componentX, int componentY, int pageNum) {
        this.x = componentX;
        this.y = componentY;
        PatchouliAPI.LOGGER.debug("Custom Component Test built at ({}, {}) page {}", componentX, componentY, pageNum);
    }

    @Override
    public void render(GuiGraphics graphics, IComponentRenderContext context, float pticks, int mouseX, int mouseY) {
        Component toRender = Component.literal(this.text).setStyle(context.getFont());
        graphics.drawString(Minecraft.getInstance().font, toRender, this.x, this.y, -1, true);
    }

    @Override
    public boolean mouseClicked(IComponentRenderContext context, double mouseX, double mouseY, int mouseButton) {
        PatchouliAPI.LOGGER.debug("Custom Component Test clicked at ({}, {}) button {}", mouseX, mouseY, mouseButton);
        return false;
    }

    @Override
    public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
        this.text = ((IVariable) lookup.apply(IVariable.wrap("First we eat #spaghet#, then we drink #pop#"))).asString();
    }
}