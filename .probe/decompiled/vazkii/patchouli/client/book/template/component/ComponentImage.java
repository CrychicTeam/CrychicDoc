package vazkii.patchouli.client.book.template.component;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.function.UnaryOperator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.client.book.BookContentsBuilder;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.BookPage;
import vazkii.patchouli.client.book.template.TemplateComponent;

public class ComponentImage extends TemplateComponent {

    public String image;

    public int u;

    public int v;

    public int width;

    public int height;

    @SerializedName("texture_width")
    public int textureWidth = 256;

    @SerializedName("texture_height")
    public int textureHeight = 256;

    public float scale = 1.0F;

    transient ResourceLocation resource;

    @Override
    public void build(BookContentsBuilder builder, BookPage page, BookEntry entry, int pageNum) {
        this.resource = new ResourceLocation(this.image);
    }

    @Override
    public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
        super.onVariablesAvailable(lookup);
        this.image = ((IVariable) lookup.apply(IVariable.wrap(this.image))).asString();
    }

    @Override
    public void render(GuiGraphics graphics, BookPage page, int mouseX, int mouseY, float pticks) {
        if (this.scale != 0.0F) {
            graphics.pose().pushPose();
            graphics.pose().translate((float) this.x, (float) this.y, 0.0F);
            graphics.pose().scale(this.scale, this.scale, this.scale);
            graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableBlend();
            graphics.blit(this.resource, 0, 0, (float) this.u, (float) this.v, this.width, this.height, this.textureWidth, this.textureHeight);
            graphics.pose().popPose();
        }
    }
}