package vazkii.patchouli.client.book.page;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.function.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.client.base.ClientTicker;
import vazkii.patchouli.client.book.BookContentsBuilder;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.gui.GuiBookEntry;
import vazkii.patchouli.client.book.page.abstr.PageWithText;
import vazkii.patchouli.common.util.EntityUtil;

public class PageEntity extends PageWithText {

    @SerializedName("entity")
    public String entityId;

    float scale = 1.0F;

    @SerializedName("offset")
    float extraOffset = 0.0F;

    String name;

    boolean rotate = true;

    @SerializedName("default_rotation")
    float defaultRotation = -45.0F;

    transient boolean errored;

    transient Entity entity;

    transient Function<Level, Entity> creator;

    transient float renderScale;

    transient float offset;

    @Override
    public void build(Level level, BookEntry entry, BookContentsBuilder builder, int pageNum) {
        super.build(level, entry, builder, pageNum);
        this.creator = EntityUtil.loadEntity(this.entityId);
    }

    @Override
    public void onDisplayed(GuiBookEntry parent, int left, int top) {
        super.onDisplayed(parent, left, top);
        this.loadEntity(parent.getMinecraft().level);
    }

    @Override
    public int getTextHeight() {
        return 115;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float pticks) {
        int x = 5;
        int y = 7;
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        GuiBook.drawFromTexture(graphics, this.book, x, y, 405, 149, 106, 106);
        if (this.name != null && !this.name.isEmpty()) {
            this.parent.drawCenteredStringNoShadow(graphics, this.name, 58, 0, this.book.headerColor);
        } else if (this.entity != null) {
            this.parent.drawCenteredStringNoShadow(graphics, this.entity.getName().getVisualOrderText(), 58, 0, this.book.headerColor);
        }
        if (this.errored) {
            graphics.drawString(this.fontRenderer, I18n.get("patchouli.gui.lexicon.loading_error"), 58, 60, 16711680, true);
        }
        if (this.entity != null) {
            float rotation = this.rotate ? ClientTicker.total : this.defaultRotation;
            renderEntity(graphics, this.entity, 58.0F, 60.0F, rotation, this.renderScale, this.offset);
        }
        super.render(graphics, mouseX, mouseY, pticks);
    }

    public static void renderEntity(GuiGraphics graphics, Entity entity, float x, float y, float rotation, float renderScale, float offset) {
        PoseStack ms = graphics.pose();
        ms.pushPose();
        ms.translate(x, y, 50.0F);
        ms.scale(renderScale, renderScale, renderScale);
        ms.translate(0.0F, offset, 0.0F);
        ms.mulPose(Axis.ZP.rotationDegrees(180.0F));
        ms.mulPose(Axis.YP.rotationDegrees(rotation));
        EntityRenderDispatcher erd = Minecraft.getInstance().getEntityRenderDispatcher();
        MultiBufferSource.BufferSource immediate = Minecraft.getInstance().renderBuffers().bufferSource();
        erd.setRenderShadow(false);
        erd.render(entity, 0.0, 0.0, 0.0, 0.0F, 1.0F, ms, immediate, 15728880);
        erd.setRenderShadow(true);
        immediate.endBatch();
        ms.popPose();
    }

    private void loadEntity(Level world) {
        if (!this.errored && (this.entity == null || !this.entity.isAlive() || this.entity.level() != world)) {
            try {
                this.entity = (Entity) this.creator.apply(world);
                float width = this.entity.getBbWidth();
                float height = this.entity.getBbHeight();
                float entitySize = Math.max(1.0F, Math.max(width, height));
                this.renderScale = 100.0F / entitySize * 0.8F * this.scale;
                this.offset = Math.max(height, entitySize) * 0.5F + this.extraOffset;
            } catch (Exception var5) {
                this.errored = true;
                PatchouliAPI.LOGGER.error("Failed to load entity", var5);
            }
        }
    }
}