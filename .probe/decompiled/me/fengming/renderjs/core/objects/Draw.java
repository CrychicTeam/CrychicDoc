package me.fengming.renderjs.core.objects;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import me.fengming.renderjs.core.RenderObject;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public abstract class Draw extends RenderObject {

    protected float r = 0.0F;

    protected float g = 0.0F;

    protected float b = 0.0F;

    protected float a = 1.0F;

    protected boolean verticesColor = false;

    protected ResourceLocation textureLocation = null;

    protected boolean texture = false;

    public Draw(float[] vertices, RenderObject.ObjectType type) {
        super(vertices, type);
    }

    @Override
    public void loadInner(CompoundTag object) {
        float r = -1.0F;
        float g = -1.0F;
        float b = -1.0F;
        float a = 1.0F;
        if (object.contains("r")) {
            r = object.getFloat("r");
        }
        if (object.contains("g")) {
            g = object.getFloat("g");
        }
        if (object.contains("b")) {
            b = object.getFloat("b");
        }
        if (object.contains("a")) {
            a = object.getFloat("a");
        } else {
            ConsoleJS.CLIENT.error("Missing a necessary key: a");
            this.broken = true;
        }
        if (r == -1.0F || g == -1.0F || b == -1.0F) {
            this.verticesColor = true;
        }
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        if (object.contains("texture_location")) {
            this.textureLocation = new ResourceLocation(object.getString("texture_location"));
            this.texture = true;
        }
    }

    @Override
    public void rjs$render() {
        if (this.texture) {
            RenderSystem.setShaderTexture(99, this.textureLocation);
            RenderSystem.bindTexture(99);
            RenderSystem.setShader(GameRenderer::m_172814_);
        } else {
            RenderSystem.setShader(GameRenderer::m_172811_);
        }
        if (this.type == RenderObject.ObjectType.LINES || this.type == RenderObject.ObjectType.LINE_STRIP) {
            RenderSystem.setShader(GameRenderer::m_172757_);
        }
        super.rjs$render();
    }
}