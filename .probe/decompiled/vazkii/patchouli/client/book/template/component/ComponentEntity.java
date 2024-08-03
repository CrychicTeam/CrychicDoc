package vazkii.patchouli.client.book.template.component;

import com.google.gson.annotations.SerializedName;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.client.base.ClientTicker;
import vazkii.patchouli.client.book.BookContentsBuilder;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.BookPage;
import vazkii.patchouli.client.book.gui.GuiBookEntry;
import vazkii.patchouli.client.book.page.PageEntity;
import vazkii.patchouli.client.book.template.TemplateComponent;
import vazkii.patchouli.common.util.EntityUtil;

public class ComponentEntity extends TemplateComponent {

    @SerializedName("entity")
    public IVariable entityId;

    @SerializedName("render_size")
    float renderSize = 100.0F;

    boolean rotate = true;

    @SerializedName("default_rotation")
    float defaultRotation = -45.0F;

    transient boolean errored;

    transient Entity entity;

    transient Function<Level, Entity> creator;

    transient float renderScale;

    transient float offset;

    @Override
    public void build(BookContentsBuilder builder, BookPage page, BookEntry entry, int pageNum) {
        this.creator = EntityUtil.loadEntity(this.entityId.asString());
    }

    @Override
    public void onDisplayed(BookPage page, GuiBookEntry parent, int left, int top) {
        this.loadEntity(page.mc.level);
    }

    @Override
    public void render(GuiGraphics graphics, BookPage page, int mouseX, int mouseY, float pticks) {
        if (this.errored) {
            graphics.drawString(page.fontRenderer, Component.translatable("patchouli.gui.lexicon.loading_error"), this.x, this.y, 16711680, false);
        }
        if (this.entity != null) {
            float rotation = this.rotate ? ClientTicker.total : this.defaultRotation;
            PageEntity.renderEntity(graphics, this.entity, (float) this.x, (float) this.y, rotation, this.renderScale, this.offset);
        }
    }

    @Override
    public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
        super.onVariablesAvailable(lookup);
        this.entityId = (IVariable) lookup.apply(this.entityId);
    }

    private void loadEntity(Level world) {
        if (!this.errored && (this.entity == null || !this.entity.isAlive() || this.entity.level() != world)) {
            try {
                this.entity = (Entity) this.creator.apply(world);
                float width = this.entity.getBbWidth();
                float height = this.entity.getBbHeight();
                float entitySize = Math.max(width, height);
                entitySize = Math.max(1.0F, entitySize);
                this.renderScale = this.renderSize / entitySize * 0.8F;
                this.offset = Math.max(height, entitySize) * 0.5F;
            } catch (Exception var5) {
                this.errored = true;
                PatchouliAPI.LOGGER.error("Failed to load entity", var5);
            }
        }
    }
}