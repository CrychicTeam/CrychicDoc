package snownee.jade.api.ui;

import java.util.Objects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.Nullable;

public abstract class Element implements IElement {

    protected IElement.Align align = IElement.Align.LEFT;

    protected Vec2 translation = Vec2.ZERO;

    protected ResourceLocation tag;

    protected Vec2 size;

    private static final String DEFAULT_MESSAGE = "";

    protected String message = "";

    @Override
    public IElement size(@Nullable Vec2 size) {
        this.size = size;
        return this;
    }

    @Override
    public Vec2 getCachedSize() {
        if (this.size == null) {
            this.size = this.getSize();
        }
        return this.size;
    }

    @Override
    public IElement align(IElement.Align align) {
        Objects.requireNonNull(align);
        this.align = align;
        return this;
    }

    @Override
    public IElement.Align getAlignment() {
        return this.align;
    }

    @Override
    public IElement translate(Vec2 translation) {
        Objects.requireNonNull(translation);
        this.translation = translation;
        return this;
    }

    @Override
    public Vec2 getTranslation() {
        return this.translation;
    }

    @Override
    public IElement tag(ResourceLocation tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public ResourceLocation getTag() {
        return this.tag;
    }

    @Nullable
    @Override
    public String getCachedMessage() {
        if (this.message == "") {
            this.message = this.getMessage();
        }
        return this.message;
    }

    @Override
    public IElement clearCachedMessage() {
        this.message = "";
        return this;
    }

    @Override
    public IElement message(@Nullable String message) {
        this.message = message;
        return this;
    }
}