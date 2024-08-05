package dev.latvian.mods.kubejs.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.latvian.mods.kubejs.bindings.TextWrapper;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.util.NotificationBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;

public class NotificationToast implements Toast {

    public static final Map<Integer, BiFunction<Minecraft, String, NotificationToast.ToastIcon>> ICONS = new HashMap(Map.of(1, NotificationToast.TextureIcon::new, 2, NotificationToast.ItemIcon::new, 3, NotificationToast.AtlasIcon::of));

    private final NotificationBuilder notification;

    private final long duration;

    private final NotificationToast.ToastIcon icon;

    private final List<FormattedCharSequence> text;

    private int width;

    private int height;

    private long lastChanged;

    private boolean changed;

    public NotificationToast(Minecraft mc, NotificationBuilder notification) {
        this.notification = notification;
        this.duration = notification.duration.toMillis();
        this.icon = ICONS.containsKey(this.notification.iconType) ? (NotificationToast.ToastIcon) ((BiFunction) ICONS.get(this.notification.iconType)).apply(mc, this.notification.icon) : null;
        this.text = new ArrayList(2);
        this.width = 0;
        this.height = 0;
        if (!TextWrapper.isEmpty(notification.text)) {
            this.text.addAll(mc.font.split(notification.text, 240));
        }
        for (FormattedCharSequence l : this.text) {
            this.width = Math.max(this.width, mc.font.width(l));
        }
        this.width += 12;
        if (this.icon != null) {
            this.width += 24;
        }
        this.height = Math.max(this.text.size() * 10 + 12, 28);
        if (this.text.isEmpty() && this.icon != null) {
            this.width = 28;
            this.height = 28;
        }
    }

    @Override
    public int width() {
        return this.width;
    }

    @Override
    public int height() {
        return this.height;
    }

    private void drawRectangle(Matrix4f m, int x0, int y0, int x1, int y1, int r, int g, int b) {
        RenderSystem.setShader(GameRenderer::m_172811_);
        BufferBuilder buf = Tesselator.getInstance().getBuilder();
        buf.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        buf.m_252986_(m, (float) x0, (float) y1, 0.0F).color(r, g, b, 255).endVertex();
        buf.m_252986_(m, (float) x1, (float) y1, 0.0F).color(r, g, b, 255).endVertex();
        buf.m_252986_(m, (float) x1, (float) y0, 0.0F).color(r, g, b, 255).endVertex();
        buf.m_252986_(m, (float) x0, (float) y0, 0.0F).color(r, g, b, 255).endVertex();
        BufferUploader.drawWithShader(buf.end());
    }

    @Override
    public Toast.Visibility render(GuiGraphics graphics, ToastComponent toastComponent, long l) {
        if (this.changed) {
            this.lastChanged = l;
            this.changed = false;
        }
        Minecraft mc = toastComponent.getMinecraft();
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        poseStack.translate(-2.0, 2.0, 0.0);
        Matrix4f m = poseStack.last().pose();
        int w = this.width();
        int h = this.height();
        int oc = this.notification.outlineColor.getRgbJS();
        int ocr = FastColor.ARGB32.red(oc);
        int ocg = FastColor.ARGB32.green(oc);
        int ocb = FastColor.ARGB32.blue(oc);
        int bc = this.notification.borderColor.getRgbJS();
        int bcr = FastColor.ARGB32.red(bc);
        int bcg = FastColor.ARGB32.green(bc);
        int bcb = FastColor.ARGB32.blue(bc);
        int bgc = this.notification.backgroundColor.getRgbJS();
        int bgcr = FastColor.ARGB32.red(bgc);
        int bgcg = FastColor.ARGB32.green(bgc);
        int bgcb = FastColor.ARGB32.blue(bgc);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        this.drawRectangle(m, 2, 0, w - 2, h, ocr, ocg, ocb);
        this.drawRectangle(m, 0, 2, w, h - 2, ocr, ocg, ocb);
        this.drawRectangle(m, 1, 1, w - 1, h - 1, ocr, ocg, ocb);
        this.drawRectangle(m, 2, 1, w - 2, h - 1, bcr, bcg, bcb);
        this.drawRectangle(m, 1, 2, w - 1, h - 2, bcr, bcg, bcb);
        this.drawRectangle(m, 2, 2, w - 2, h - 2, bgcr, bgcg, bgcb);
        if (this.icon != null) {
            this.icon.draw(mc, graphics, 14, h / 2, this.notification.iconSize);
        }
        int th = this.icon == null ? 6 : 26;
        int tv = (h - this.text.size() * 10) / 2 + 1;
        for (int i = 0; i < this.text.size(); i++) {
            graphics.drawString(mc.font, (FormattedCharSequence) this.text.get(i), th, tv + i * 10, 16777215, this.notification.textShadow);
        }
        poseStack.popPose();
        return l - this.lastChanged < this.duration ? Toast.Visibility.SHOW : Toast.Visibility.HIDE;
    }

    public static record AtlasIcon(TextureAtlasSprite sprite) implements NotificationToast.ToastIcon {

        public static NotificationToast.AtlasIcon of(Minecraft mc, String icon) {
            String[] s = icon.split("\\|");
            return s.length == 2 ? new NotificationToast.AtlasIcon((TextureAtlasSprite) mc.getTextureAtlas(new ResourceLocation(s[0])).apply(new ResourceLocation(s[1]))) : new NotificationToast.AtlasIcon((TextureAtlasSprite) mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation(icon)));
        }

        @Override
        public void draw(Minecraft mc, GuiGraphics graphics, int x, int y, int size) {
            RenderSystem.setShaderTexture(0, this.sprite.atlasLocation());
            RenderSystem.setShader(GameRenderer::m_172820_);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            Matrix4f m = graphics.pose().last().pose();
            int p0 = -size / 2;
            int p1 = p0 + size;
            float u0 = this.sprite.getU0();
            float v0 = this.sprite.getV0();
            float u1 = this.sprite.getU1();
            float v1 = this.sprite.getV1();
            BufferBuilder buf = Tesselator.getInstance().getBuilder();
            buf.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            buf.m_252986_(m, (float) (x + p0), (float) (y + p1), 0.0F).uv(u0, v1).color(255, 255, 255, 255).endVertex();
            buf.m_252986_(m, (float) (x + p1), (float) (y + p1), 0.0F).uv(u1, v1).color(255, 255, 255, 255).endVertex();
            buf.m_252986_(m, (float) (x + p1), (float) (y + p0), 0.0F).uv(u1, v0).color(255, 255, 255, 255).endVertex();
            buf.m_252986_(m, (float) (x + p0), (float) (y + p0), 0.0F).uv(u0, v0).color(255, 255, 255, 255).endVertex();
            BufferUploader.drawWithShader(buf.end());
        }
    }

    public static record ItemIcon(ItemStack stack) implements NotificationToast.ToastIcon {

        public ItemIcon(Minecraft ignored, String icon) {
            this(ItemStackJS.of(icon));
        }

        @Override
        public void draw(Minecraft mc, GuiGraphics graphics, int x, int y, int size) {
            PoseStack m = RenderSystem.getModelViewStack();
            m.pushPose();
            m.translate((double) x - 2.0, (double) y + 2.0, 0.0);
            float s = (float) size / 16.0F;
            m.scale(s, s, s);
            RenderSystem.applyModelViewMatrix();
            graphics.renderFakeItem(this.stack, -8, -8);
            m.popPose();
            RenderSystem.applyModelViewMatrix();
        }
    }

    public static record TextureIcon(ResourceLocation texture) implements NotificationToast.ToastIcon {

        public TextureIcon(Minecraft ignored, String icon) {
            this(new ResourceLocation(icon));
        }

        @Override
        public void draw(Minecraft mc, GuiGraphics graphics, int x, int y, int size) {
            RenderSystem.setShaderTexture(0, this.texture);
            RenderSystem.setShader(GameRenderer::m_172820_);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            Matrix4f m = graphics.pose().last().pose();
            int p0 = -size / 2;
            int p1 = p0 + size;
            BufferBuilder buf = Tesselator.getInstance().getBuilder();
            buf.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            buf.m_252986_(m, (float) (x + p0), (float) (y + p1), 0.0F).uv(0.0F, 1.0F).color(255, 255, 255, 255).endVertex();
            buf.m_252986_(m, (float) (x + p1), (float) (y + p1), 0.0F).uv(1.0F, 1.0F).color(255, 255, 255, 255).endVertex();
            buf.m_252986_(m, (float) (x + p1), (float) (y + p0), 0.0F).uv(1.0F, 0.0F).color(255, 255, 255, 255).endVertex();
            buf.m_252986_(m, (float) (x + p0), (float) (y + p0), 0.0F).uv(0.0F, 0.0F).color(255, 255, 255, 255).endVertex();
            BufferUploader.drawWithShader(buf.end());
        }
    }

    public interface ToastIcon {

        void draw(Minecraft var1, GuiGraphics var2, int var3, int var4, int var5);
    }
}