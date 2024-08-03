package net.mehvahdjukaar.dummmmmmy.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.mehvahdjukaar.dummmmmmy.configs.ClientConfigs;
import net.mehvahdjukaar.dummmmmmy.configs.CritMode;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class DamageNumberParticle extends Particle {

    private static final List<Float> POSITIONS = new ArrayList(Arrays.asList(0.0F, -0.25F, 0.12F, -0.12F, 0.25F));

    private static final DecimalFormat DF2 = new DecimalFormat("#.##");

    private static final DecimalFormat DF1 = new DecimalFormat("#.#");

    private final Font fontRenderer = Minecraft.getInstance().font;

    private final Component text;

    private final int color;

    private final int darkColor;

    private float fadeout = -1.0F;

    private float prevFadeout = -1.0F;

    private float visualDY = 0.0F;

    private float prevVisualDY = 0.0F;

    private float visualDX = 0.0F;

    private float prevVisualDX = 0.0F;

    public DamageNumberParticle(ClientLevel clientLevel, double x, double y, double z, double amount, double dColor, double dz) {
        super(clientLevel, x, y, z);
        this.f_107225_ = 35;
        this.color = amount < 0.0 ? -16711936 : (int) dColor;
        this.darkColor = FastColor.ARGB32.color(255, (int) (this.f_107227_ * 0.25F), (int) (this.f_107227_ * 0.25F), (int) ((double) this.f_107227_ * 0.25));
        double number = Math.abs(ClientConfigs.SHOW_HEARTHS.get() ? amount / 2.0 : amount);
        this.f_107216_ = 1.0;
        int index = CritMode.extractIntegerPart(dz);
        float critMult = CritMode.extractFloatPart(dz);
        if (critMult == 0.0F) {
            this.text = Component.literal((amount < 0.0 ? "+" : "") + DF2.format(number));
        } else {
            this.text = Component.translatable("message.dummmmmmy.crit", DF1.format(number), DF1.format((double) critMult));
        }
        this.f_107215_ = (double) ((Float) POSITIONS.get(index % POSITIONS.size())).floatValue();
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        Vec3 cameraPos = camera.getPosition();
        float particleX = (float) (Mth.lerp((double) partialTicks, this.f_107209_, this.f_107212_) - cameraPos.x());
        float particleY = (float) (Mth.lerp((double) partialTicks, this.f_107210_, this.f_107213_) - cameraPos.y());
        float particleZ = (float) (Mth.lerp((double) partialTicks, this.f_107211_, this.f_107214_) - cameraPos.z());
        int light = ClientConfigs.LIT_UP_PARTICLES.get() ? 15728880 : this.m_6355_(partialTicks);
        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();
        poseStack.translate(particleX, particleY, particleZ);
        double distanceFromCam = new Vec3((double) particleX, (double) particleY, (double) particleZ).length();
        double inc = Mth.clamp(distanceFromCam / 32.0, 0.0, 5.0);
        poseStack.translate(0.0, (1.0 + inc / 4.0) * (double) Mth.lerp(partialTicks, this.prevVisualDY, this.visualDY), 0.0);
        float fadeout = Mth.lerp(partialTicks, this.prevFadeout, this.fadeout);
        float defScale = 0.006F;
        float scale = (float) ((double) defScale * distanceFromCam);
        poseStack.mulPose(camera.rotation());
        poseStack.translate((1.0 + inc) * (double) Mth.lerp(partialTicks, this.prevVisualDX, this.visualDX), 0.0, 0.0);
        poseStack.scale(-scale, -scale, scale);
        poseStack.translate(0.0, 4.0 * (double) (1.0F - fadeout), 0.0);
        poseStack.scale(fadeout, fadeout, fadeout);
        poseStack.translate(0.0, -distanceFromCam / 10.0, 0.0);
        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(770, 771, 1, 0);
        float x1 = 0.5F - (float) this.fontRenderer.width(this.text) / 2.0F;
        this.fontRenderer.drawInBatch(this.text, x1, 0.0F, this.color, false, poseStack.last().pose(), buffer, Font.DisplayMode.NORMAL, 0, light);
        poseStack.translate(1.0, 1.0, 0.03);
        this.fontRenderer.drawInBatch(this.text, x1, 0.0F, this.darkColor, false, poseStack.last().pose(), buffer, Font.DisplayMode.NORMAL, 0, light);
        buffer.endBatch();
        poseStack.popPose();
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        if (this.f_107224_++ >= this.f_107225_) {
            this.m_107274_();
        } else {
            float length = 6.0F;
            this.prevFadeout = this.fadeout;
            this.fadeout = (float) this.f_107224_ > (float) this.f_107225_ - length ? ((float) this.f_107225_ - (float) this.f_107224_) / length : 1.0F;
            this.prevVisualDY = this.visualDY;
            this.visualDY = (float) ((double) this.visualDY + this.f_107216_);
            this.prevVisualDX = this.visualDX;
            this.visualDX = (float) ((double) this.visualDX + this.f_107215_);
            if (Math.sqrt(Mth.square((double) this.visualDX * 1.5) + (double) Mth.square(this.visualDY - 1.0F)) < 0.8999999999999999) {
                this.f_107216_ /= 2.0;
            } else {
                this.f_107216_ = 0.0;
                this.f_107215_ = 0.0;
            }
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        public Factory(SpriteSet spriteSet) {
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new DamageNumberParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
}