package net.mehvahdjukaar.supplementaries.client.particles;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.client.util.RotHlpr;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class CannonFireParticle extends TextureSheetParticle {

    private final SpriteSet ringSprites;

    private final SpriteSet boomSprites;

    private final double yaw;

    private final double pitch;

    private TextureAtlasSprite boomSprite;

    private CannonFireParticle(ClientLevel world, double x, double y, double z, double pitch, double yaw, SpriteSet ringSprites, SpriteSet boomSprites) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.m_172260_(0.0, 0.0, 0.0);
        this.pitch = pitch;
        this.yaw = yaw;
        this.ringSprites = ringSprites;
        this.boomSprites = boomSprites;
        this.f_107225_ = 5;
        this.f_107219_ = false;
        this.f_107663_ = 1.25F;
        this.setSpriteFromAge(ringSprites);
    }

    @Override
    public void tick() {
        super.m_5989_();
        this.setSpriteFromAge(this.ringSprites);
    }

    @Override
    public void setSpriteFromAge(SpriteSet sprite) {
        if (!this.f_107220_) {
            this.m_108337_(sprite.get(this.f_107224_, this.f_107225_));
            this.boomSprite = this.boomSprites.get(this.f_107224_, this.f_107225_);
        }
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        Vec3 vec3 = renderInfo.getPosition();
        float px = (float) (Mth.lerp((double) partialTicks, this.f_107209_, this.f_107212_) - vec3.x());
        float py = (float) (Mth.lerp((double) partialTicks, this.f_107210_, this.f_107213_) - vec3.y());
        float pz = (float) (Mth.lerp((double) partialTicks, this.f_107211_, this.f_107214_) - vec3.z());
        Quaternionf quaternionf = new Quaternionf();
        quaternionf.rotateY((float) this.yaw);
        quaternionf.rotateX((float) this.pitch);
        float scale = this.m_5902_(partialTicks);
        Matrix4f mat = new Matrix4f();
        mat.translate(px, py, pz);
        mat.scale(scale, scale, scale);
        mat.rotate(quaternionf);
        float u0 = this.m_5970_();
        float u1 = this.m_5952_();
        float v0 = this.m_5951_();
        float v1 = this.m_5950_();
        int light = this.getLightColor(partialTicks);
        this.drawDoubleQuad(buffer, mat, 1.0F, 0.0F, u0, u1, v0, v1, light);
        mat.translate(0.0F, 0.0F, -0.25F);
        mat.rotate(RotHlpr.YN90);
        float U0 = this.boomSprite.getU0();
        float U1 = this.boomSprite.getU1();
        float V0 = this.boomSprite.getV0();
        float V1 = this.boomSprite.getV1();
        int i = (int) Math.min(4.0F, (float) this.f_107224_ / (float) this.f_107225_ * 5.0F) + 1;
        float d = (float) i / 16.0F;
        float s = 0.25F;
        for (int j = 0; j < 4; j++) {
            mat.rotate(RotHlpr.X90);
            this.drawDoubleQuad(buffer, mat, s, d, U0, U1, V0, V1, light);
        }
    }

    private void drawDoubleQuad(VertexConsumer buffer, Matrix4f mat, float w, float o, float u0, float u1, float v0, float v1, int light) {
        buffer.vertex(mat, -w, -w, o).uv(u1, v1).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(light).endVertex();
        buffer.vertex(mat, -w, w, o).uv(u1, v0).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(light).endVertex();
        buffer.vertex(mat, w, w, o).uv(u0, v0).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(light).endVertex();
        buffer.vertex(mat, w, -w, o).uv(u0, v1).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(light).endVertex();
        buffer.vertex(mat, w, -w, o).uv(u0, v1).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(light).endVertex();
        buffer.vertex(mat, w, w, o).uv(u0, v0).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(light).endVertex();
        buffer.vertex(mat, -w, w, o).uv(u1, v0).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(light).endVertex();
        buffer.vertex(mat, -w, -w, o).uv(u1, v1).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(light).endVertex();
    }

    @Override
    public int getLightColor(float partialTick) {
        return 15728880;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        private final Supplier<SpriteSet> sprites2 = Suppliers.memoize(() -> {
            TextureAtlas atlas = (TextureAtlas) Minecraft.getInstance().getTextureManager().getTexture(TextureAtlas.LOCATION_PARTICLES);
            return new CannonFireParticle.SimpleSpriteSet(List.of(atlas.getSprite(Supplementaries.res("cannon_bang_00")), atlas.getSprite(Supplementaries.res("cannon_bang_01")), atlas.getSprite(Supplementaries.res("cannon_bang_02")), atlas.getSprite(Supplementaries.res("cannon_bang_03")), atlas.getSprite(Supplementaries.res("cannon_bang_04")), atlas.getSprite(Supplementaries.res("empty"))));
        });

        public Factory(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double pitch, double yaw, double zSpeed) {
            Vec3 offset = Vec3.directionFromRotation((float) pitch * (180.0F / (float) Math.PI), -((float) yaw) * (180.0F / (float) Math.PI));
            offset = offset.scale(-0.4063125);
            return new CannonFireParticle(worldIn, x + offset.x, y + offset.y + 0.0625, z + offset.z, pitch, yaw, this.sprites, (SpriteSet) this.sprites2.get());
        }
    }

    static record SimpleSpriteSet(List<TextureAtlasSprite> sprites) implements SpriteSet {

        @Override
        public TextureAtlasSprite get(int age, int lifetime) {
            return (TextureAtlasSprite) this.sprites.get((this.sprites.size() - 1) * age / lifetime);
        }

        @Override
        public TextureAtlasSprite get(RandomSource random) {
            return (TextureAtlasSprite) this.sprites.get(random.nextInt(this.sprites.size()));
        }
    }
}