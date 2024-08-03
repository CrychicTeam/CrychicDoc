package me.jellysquid.mods.sodium.mixin.core.model.quad;

import me.jellysquid.mods.sodium.client.model.quad.BakedQuadView;
import me.jellysquid.mods.sodium.client.model.quad.properties.ModelQuadFacing;
import me.jellysquid.mods.sodium.client.model.quad.properties.ModelQuadFlags;
import me.jellysquid.mods.sodium.client.util.ModelQuadUtil;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ BakedQuad.class })
public abstract class BakedQuadMixin implements BakedQuadView {

    @Shadow
    @Final
    protected int[] vertices;

    @Shadow
    @Final
    protected TextureAtlasSprite sprite;

    @Shadow
    @Final
    protected int tintIndex;

    @Shadow
    @Final
    protected Direction direction;

    @Shadow
    @Final
    private boolean shade;

    @Shadow(remap = false)
    @Final
    private boolean hasAmbientOcclusion;

    @Unique
    private int flags;

    @Unique
    private int normal;

    @Unique
    private ModelQuadFacing normalFace;

    @Inject(method = { "<init>([IILnet/minecraft/core/Direction;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;ZZ)V" }, at = { @At("RETURN") })
    private void init(int[] vertexData, int colorIndex, Direction face, TextureAtlasSprite sprite, boolean shade, boolean hasAmbientOcclusion, CallbackInfo ci) {
        this.normal = ModelQuadUtil.calculateNormal(this);
        this.normalFace = ModelQuadUtil.findNormalFace(this.normal);
        this.flags = ModelQuadFlags.getQuadFlags(this, face);
    }

    @Override
    public float getX(int idx) {
        return Float.intBitsToFloat(this.vertices[ModelQuadUtil.vertexOffset(idx) + 0]);
    }

    @Override
    public float getY(int idx) {
        return Float.intBitsToFloat(this.vertices[ModelQuadUtil.vertexOffset(idx) + 0 + 1]);
    }

    @Override
    public float getZ(int idx) {
        return Float.intBitsToFloat(this.vertices[ModelQuadUtil.vertexOffset(idx) + 0 + 2]);
    }

    @Override
    public int getColor(int idx) {
        return this.vertices[ModelQuadUtil.vertexOffset(idx) + 3];
    }

    @Override
    public TextureAtlasSprite getSprite() {
        return this.sprite;
    }

    @Override
    public float getTexU(int idx) {
        return Float.intBitsToFloat(this.vertices[ModelQuadUtil.vertexOffset(idx) + 4]);
    }

    @Override
    public float getTexV(int idx) {
        return Float.intBitsToFloat(this.vertices[ModelQuadUtil.vertexOffset(idx) + 4 + 1]);
    }

    @Override
    public int getLight(int idx) {
        return this.vertices[ModelQuadUtil.vertexOffset(idx) + 6];
    }

    @Override
    public int getForgeNormal(int idx) {
        return this.vertices[ModelQuadUtil.vertexOffset(idx) + 7];
    }

    @Override
    public int getFlags() {
        return this.flags;
    }

    @Override
    public int getColorIndex() {
        return this.tintIndex;
    }

    @Override
    public ModelQuadFacing getNormalFace() {
        return this.normalFace;
    }

    @Override
    public Direction getLightFace() {
        return this.direction;
    }

    @Unique(silent = true)
    @Override
    public boolean hasShade() {
        return this.shade;
    }

    @Override
    public boolean hasAmbientOcclusion() {
        return this.hasAmbientOcclusion;
    }
}