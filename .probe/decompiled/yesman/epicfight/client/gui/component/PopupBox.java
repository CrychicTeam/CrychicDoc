package yesman.epicfight.client.gui.component;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.function.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.StringUtil;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import yesman.epicfight.client.gui.screen.SelectFromRegistryScreen;

@OnlyIn(Dist.CLIENT)
public class PopupBox<T> extends AbstractWidget implements ResizableComponent {

    public static final ResourceLocation POPUP_ICON = new ResourceLocation("epicfight", "textures/gui/popup_icon.png");

    protected final Screen owner;

    protected final Font font;

    protected final IForgeRegistry<T> registry;

    protected final Function<T, String> displayStringMapper;

    protected T item;

    protected String itemDisplayName;

    private final int x1;

    private final int x2;

    private final int y1;

    private final int y2;

    private final ResizableComponent.HorizontalSizing horizontalSizingOption;

    private final ResizableComponent.VerticalSizing verticalSizingOption;

    public PopupBox(Screen owner, Font font, int x1, int x2, int y1, int y2, ResizableComponent.HorizontalSizing horizontal, ResizableComponent.VerticalSizing vertical, Component title, IForgeRegistry<T> registry) {
        this(owner, font, x1, x2, y1, y2, horizontal, vertical, title, registry, item -> registry.containsValue((T) item) ? registry.getKey((T) item).toString() : item.toString());
    }

    public PopupBox(Screen owner, Font font, int x1, int x2, int y1, int y2, ResizableComponent.HorizontalSizing horizontal, ResizableComponent.VerticalSizing vertical, Component title, IForgeRegistry<T> registry, Function<T, String> displayStringMapper) {
        super(x1, x2, y1, y2, title);
        this.owner = owner;
        this.font = font;
        this.registry = registry;
        this.displayStringMapper = displayStringMapper;
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.horizontalSizingOption = horizontal;
        this.verticalSizingOption = vertical;
    }

    public void setValue(T item) {
        this.item = item;
        this.itemDisplayName = (String) this.displayStringMapper.apply(item);
    }

    @Override
    protected boolean clicked(double x, double y) {
        return this.f_93623_ && this.f_93624_ && x >= (double) this.m_252754_() + (double) this.f_93618_ - 14.0 && y >= (double) this.m_252907_() && x < (double) (this.m_252754_() + this.f_93618_) && y < (double) (this.m_252907_() + this.f_93619_);
    }

    @Override
    public void onClick(double x, double y) {
        this.owner.getMinecraft().setScreen(new SelectFromRegistryScreen<>(this.owner, this.registry, this::setValue));
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        if (!this.f_93623_ || !this.f_93624_) {
            return false;
        } else if (this.m_7972_(button) && this.clicked(x, y)) {
            this.m_7435_(Minecraft.getInstance().getSoundManager());
            this.onClick(x, y);
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        int outlineColor = this.m_93696_() ? -1 : -6250336;
        guiGraphics.fill(this.m_252754_() - 1, this.m_252907_() - 1, this.m_252754_() + this.f_93618_ + 1, this.m_252907_() + this.f_93619_ + 1, outlineColor);
        guiGraphics.fill(this.m_252754_(), this.m_252907_(), this.m_252754_() + this.f_93618_, this.m_252907_() + this.f_93619_, -16777216);
        String correctedString = StringUtil.isNullOrEmpty(this.itemDisplayName) ? "" : this.font.plainSubstrByWidth(this.itemDisplayName, this.f_93618_ - 16);
        guiGraphics.drawString(this.font, correctedString, this.m_252754_() + 4, this.m_252907_() + this.f_93619_ / 2 - 9 / 2 + 1, 16777215, false);
        RenderSystem.enableBlend();
        this.m_280322_(guiGraphics, POPUP_ICON, this.m_252754_() + this.f_93618_ - this.f_93619_, this.m_252907_(), 0, 0, 0, this.f_93619_, this.f_93619_, 16, 16);
        RenderSystem.disableBlend();
    }

    @Override
    protected MutableComponent createNarrationMessage() {
        Component component = this.m_6035_();
        return Component.translatable("gui.epicfight.narrate.popbupBox", component);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementInput) {
        narrationElementInput.add(NarratedElementType.TITLE, this.createNarrationMessage());
    }

    @Override
    public int getX1() {
        return this.x1;
    }

    @Override
    public int getX2() {
        return this.x2;
    }

    @Override
    public int getY1() {
        return this.y1;
    }

    @Override
    public int getY2() {
        return this.y2;
    }

    @Override
    public ResizableComponent.HorizontalSizing getHorizontalSizingOption() {
        return this.horizontalSizingOption;
    }

    @Override
    public ResizableComponent.VerticalSizing getVerticalSizingOption() {
        return this.verticalSizingOption;
    }

    @OnlyIn(Dist.CLIENT)
    public static class SoundPopupBox extends PopupBox<SoundEvent> {

        public SoundPopupBox(Screen owner, Font font, int x, int y, int width, int height, ResizableComponent.HorizontalSizing horizontal, ResizableComponent.VerticalSizing vertical, Component title) {
            super(owner, font, x, y, width, height, horizontal, vertical, title, ForgeRegistries.SOUND_EVENTS);
        }

        @Override
        public void onClick(double x, double y) {
            this.owner.getMinecraft().setScreen(new SelectFromRegistryScreen<>(this.owner, this.registry, soundevent -> Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(soundevent, 1.0F)), this::setValue));
        }
    }
}