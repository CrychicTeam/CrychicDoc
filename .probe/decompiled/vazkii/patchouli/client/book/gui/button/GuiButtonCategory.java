package vazkii.patchouli.client.book.gui.button;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.client.base.ClientTicker;
import vazkii.patchouli.client.book.BookCategory;
import vazkii.patchouli.client.book.BookIcon;
import vazkii.patchouli.client.book.gui.GuiBook;

public class GuiButtonCategory extends Button {

    private static final int ANIM_TIME = 5;

    private final GuiBook parent;

    @Nullable
    private BookCategory category;

    private final BookIcon icon;

    private final Component name;

    private final int u;

    private final int v;

    private float timeHovered;

    public GuiButtonCategory(GuiBook parent, int x, int y, BookCategory category, Button.OnPress onPress) {
        this(parent, x, y, category.getIcon(), category.getName(), onPress);
        this.category = category;
    }

    public GuiButtonCategory(GuiBook parent, int x, int y, BookIcon icon, Component name, Button.OnPress onPress) {
        super(parent.bookLeft + x, parent.bookTop + y, 20, 20, name, onPress, f_252438_);
        this.parent = parent;
        this.u = x;
        this.v = y;
        this.icon = icon;
        this.name = name;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (this.f_93623_) {
            if (this.m_198029_()) {
                this.timeHovered = Math.min(5.0F, this.timeHovered + ClientTicker.delta);
            } else {
                this.timeHovered = Math.max(0.0F, this.timeHovered - ClientTicker.delta);
            }
            float time = Math.max(0.0F, Math.min(5.0F, this.timeHovered + (this.m_198029_() ? partialTicks : -partialTicks)));
            float transparency = 0.5F - time / 5.0F * 0.5F;
            boolean locked = this.category != null && this.category.isLocked();
            if (locked) {
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.7F);
                GuiBook.drawLock(graphics, this.parent.book, this.m_252754_() + 2, this.m_252907_() + 2);
            } else {
                this.icon.render(graphics, this.m_252754_() + 2, this.m_252907_() + 2);
            }
            graphics.pose().pushPose();
            RenderSystem.enableBlend();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, transparency);
            graphics.pose().translate(0.0F, 0.0F, 200.0F);
            GuiBook.drawFromTexture(graphics, this.parent.book, this.m_252754_(), this.m_252907_(), this.u, this.v, this.f_93618_, this.f_93619_);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            if (this.category != null && !this.category.isLocked()) {
                GuiBook.drawMarking(graphics, this.parent.book, this.m_252754_(), this.m_252907_(), 0, this.category.getReadState());
            }
            graphics.pose().popPose();
            if (this.m_198029_()) {
                this.parent.setTooltip((Component) (locked ? Component.translatable("patchouli.gui.lexicon.locked").withStyle(ChatFormatting.GRAY) : this.name));
            }
        }
    }

    @Override
    public void playDownSound(SoundManager soundHandlerIn) {
        if (this.category != null && !this.category.isLocked()) {
            GuiBook.playBookFlipSound(this.parent.book);
        }
    }

    public BookCategory getCategory() {
        return this.category;
    }
}