package harmonised.pmmo.client.gui;

import harmonised.pmmo.client.gui.component.GuiEnumGroup;
import harmonised.pmmo.client.gui.component.StatScrollWidget;
import harmonised.pmmo.setup.datagen.LangProvider;
import java.util.function.BiFunction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class StatsScreen extends Screen {

    private static final ResourceLocation GUI_BG = new ResourceLocation("pmmo", "textures/gui/screenboxy.png");

    private static final MutableComponent MENU_NAME = Component.literal("Item Detail Screen");

    private StatScrollWidget scrollWidget;

    private BiFunction<Integer, Integer, StatScrollWidget> scrollSupplier;

    private int renderX;

    private int renderY;

    private ItemStack stack = null;

    private BlockPos block = null;

    private Entity entity = null;

    public StatsScreen() {
        super(MENU_NAME);
        this.scrollSupplier = (x, y) -> this.scrollWidget = new StatScrollWidget(206, 200, y, x, 0);
    }

    public StatsScreen(ItemStack stack) {
        super(MENU_NAME);
        this.scrollSupplier = (x, y) -> this.scrollWidget = new StatScrollWidget(206, 200, y, x, this.stack = stack);
    }

    public StatsScreen(BlockPos block) {
        super(MENU_NAME);
        this.scrollSupplier = (x, y) -> this.scrollWidget = new StatScrollWidget(206, 200, y, x, this.block = block);
    }

    public StatsScreen(Entity entity) {
        super(MENU_NAME);
        this.scrollSupplier = (x, y) -> this.scrollWidget = new StatScrollWidget(206, 200, y, x, this.entity = entity);
    }

    public StatsScreen(GlossarySelectScreen.SELECTION selection, GlossarySelectScreen.OBJECT object, String skill, GuiEnumGroup type) {
        super(MENU_NAME);
        this.scrollSupplier = (x, y) -> this.scrollWidget = new StatScrollWidget(206, 200, y, x, selection, object, skill, type);
    }

    @Override
    protected void init() {
        this.renderX = this.f_96543_ / 2 - 128;
        this.renderY = this.f_96544_ / 2 - 128;
        this.m_142416_((StatScrollWidget) this.scrollSupplier.apply(this.renderX + 25, this.renderY + 30));
        this.m_142416_(Button.builder(LangProvider.OPEN_GLOSSARY.asComponent(), b -> Minecraft.getInstance().setScreen(new GlossarySelectScreen())).bounds(this.f_96543_ - 84, 4, 80, 20).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
        if (this.stack == null && this.block == null) {
            if (this.entity != null && this.entity instanceof LivingEntity) {
                InventoryScreen.renderEntityInInventoryFollowsAngle(graphics, this.renderX + this.f_96543_ - 20, this.renderY + 12, 10, (float) (this.renderX + 51) - 100.0F, (float) (this.renderY + 75 - 50) - 100.0F, (LivingEntity) this.entity);
                graphics.drawString(this.f_96547_, this.entity.getDisplayName(), this.renderX + 65, this.renderY + 15, 16777215);
            }
        } else {
            ItemStack renderStack = this.stack == null ? new ItemStack(Minecraft.getInstance().player.m_9236_().getBlockState(this.block).m_60734_().asItem()) : this.stack;
            graphics.renderItem(renderStack, this.renderX + 25, this.renderY + 15);
            graphics.drawString(this.f_96547_, renderStack.getDisplayName(), this.renderX + 65, this.renderY + 15, 16777215);
        }
    }

    @Override
    public void renderBackground(GuiGraphics graphics) {
        graphics.blit(GUI_BG, this.renderX, this.renderY, 0, 0, 256, 256);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrolled) {
        return this.scrollWidget.mouseScrolled(mouseX, mouseY, scrolled) || super.m_6050_(mouseX, mouseY, scrolled);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int partialTicks) {
        return this.scrollWidget.mouseClicked(mouseX, mouseY, partialTicks) || super.m_6375_(mouseX, mouseY, partialTicks);
    }
}