package com.github.alexmodguy.alexscaves.client.particle;

import com.github.alexmodguy.alexscaves.client.model.LuxtructosaurusModel;
import com.github.alexmodguy.alexscaves.client.render.entity.LuxtructosaurusRenderer;
import com.github.alexmodguy.alexscaves.server.entity.living.LuxtructosaurusEntity;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.TabulaModelRenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector4f;

public class LuxtructosaurusAshParticle extends TextureSheetParticle {

    private static final float ACCELERATION_SCALE = 0.0025F;

    private static final int INITIAL_LIFETIME = 300;

    private static final int CURVE_ENDPOINT_TIME = 300;

    private static final float FALL_ACC = 0.25F;

    private static final float WIND_BIG = 2.0F;

    private final SpriteSet sprites;

    private final int luxtructosaurusId;

    private final float particleRandom;

    protected LuxtructosaurusAshParticle(ClientLevel level, double x, double y, double z, int luxtructosaurusId, SpriteSet sprites) {
        super(level, x, y, z, 0.0, 0.0, 0.0);
        this.f_172258_ = 0.96F;
        this.f_172259_ = true;
        this.sprites = sprites;
        this.f_107663_ = this.f_107663_ * (3.0F + this.f_107223_.nextFloat() * 5.0F);
        this.f_107225_ = 300;
        this.m_108339_(sprites);
        this.f_107219_ = true;
        this.luxtructosaurusId = luxtructosaurusId;
        this.setInitialPos();
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        this.f_107215_ = (double) (0.3F - this.f_107223_.nextFloat() * 0.1F);
        this.f_107216_ = (double) (0.2F - this.f_107223_.nextFloat() * 0.1F);
        this.f_107217_ = (double) (0.3F - this.f_107223_.nextFloat() * 0.1F);
        this.f_107226_ = -0.005F - this.f_107223_.nextFloat() * 0.04F;
        this.particleRandom = this.f_107223_.nextFloat();
    }

    @Override
    public void tick() {
        super.m_5989_();
        this.m_108339_(this.sprites);
        float ageSca = (float) this.f_107224_ / (float) this.f_107225_;
        this.m_107271_(1.0F - ageSca);
        this.f_107215_ *= 1.01F;
        this.f_107216_ *= 1.01F;
        this.f_107217_ *= 1.01F;
        this.f_107227_ = Mth.approach(this.f_107227_, 0.2F, 0.025F);
        this.f_107228_ = Mth.approach(this.f_107228_, 0.2F, 0.025F);
        this.f_107229_ = Mth.approach(this.f_107229_, 0.2F, 0.025F);
        if (!this.f_107220_) {
            float f = (float) (300 - this.f_107225_);
            float f1 = Math.min(f / 300.0F, 1.0F);
            double d0 = Math.cos(Math.toRadians((double) (this.particleRandom * 60.0F))) * 5.0 * Math.pow((double) f1, 1.25);
            double d1 = Math.sin(Math.toRadians((double) (this.particleRandom * 60.0F))) * 5.0 * Math.pow((double) f1, 1.25);
            this.f_107215_ += d0 * 0.0025F;
            this.f_107217_ += d1 * 0.0025F;
            this.f_107216_ = this.f_107216_ - (double) this.f_107226_;
            this.m_6257_(this.f_107215_, this.f_107216_, this.f_107217_);
            if (this.f_107218_ || this.f_107225_ < 299 && (this.f_107215_ == 0.0 || this.f_107217_ == 0.0)) {
                this.m_107274_();
            }
            if (!this.f_107220_) {
                this.f_107215_ = this.f_107215_ * (double) this.f_172258_;
                this.f_107216_ = this.f_107216_ * (double) this.f_172258_;
                this.f_107217_ = this.f_107217_ * (double) this.f_172258_;
            }
        }
    }

    @Override
    public float getQuadSize(float partialTicks) {
        return this.f_107663_ * Mth.clamp(1.0F - ((float) this.f_107224_ + partialTicks) / (float) this.f_107225_, 0.0F, 1.0F);
    }

    public void setInitialPos() {
        if (this.luxtructosaurusId != -1 && this.f_107208_.getEntity(this.luxtructosaurusId) instanceof LuxtructosaurusEntity luxtructosaurus) {
            EntityRenderDispatcher manager = Minecraft.getInstance().getEntityRenderDispatcher();
            if (manager.getRenderer(luxtructosaurus) instanceof LuxtructosaurusRenderer luxtructosaurusRenderer) {
                LuxtructosaurusModel model = (LuxtructosaurusModel) luxtructosaurusRenderer.m_7200_();
                for (int attempts = 0; attempts < 5; attempts++) {
                    AdvancedModelBox box = model.getRandomModelPart(this.f_107208_.m_213780_());
                    TabulaModelRenderUtils.ModelBox randomBox = box.cubeList.size() > 0 ? (TabulaModelRenderUtils.ModelBox) box.cubeList.get(this.f_107223_.nextInt(box.cubeList.size())) : null;
                    if (randomBox != null) {
                        float f = this.f_107223_.nextFloat();
                        float f1 = this.f_107223_.nextFloat();
                        float f2 = this.f_107223_.nextFloat();
                        float f3 = Mth.lerp(f, randomBox.posX1, randomBox.posX2) / 16.0F;
                        float f4 = Mth.lerp(f1, randomBox.posY1, randomBox.posY2) / 16.0F;
                        float f5 = Mth.lerp(f2, randomBox.posZ1, randomBox.posZ2) / 16.0F;
                        Vec3 innerOffset = new Vec3((double) f3, (double) f4, (double) f5);
                        Vec3 translate = this.translateAndRotate(box, innerOffset).yRot((float) (Math.PI - (double) (luxtructosaurus.f_20883_ * (float) (Math.PI / 180.0))));
                        this.m_107264_(luxtructosaurus.m_20185_() + translate.x, luxtructosaurus.m_20186_() + translate.y, luxtructosaurus.m_20189_() + translate.z);
                        return;
                    }
                }
            }
            this.f_107230_ = 1.0F;
            this.m_107274_();
        }
    }

    private Vec3 translateAndRotate(AdvancedModelBox box, Vec3 offsetIn) {
        PoseStack translationStack = new PoseStack();
        translationStack.pushPose();
        List<AdvancedModelBox> flipMe = new ArrayList();
        while (box.getParent() != null) {
            box = box.getParent();
            flipMe.add(box);
        }
        Collections.reverse(flipMe);
        for (AdvancedModelBox translateBy : flipMe) {
            translateBy.translateAndRotate(translationStack);
        }
        translationStack.translate(offsetIn.x, offsetIn.y, offsetIn.z);
        Vector4f armOffsetVec = new Vector4f(0.0F, 0.0F, 0.0F, 1.0F);
        armOffsetVec.mul(translationStack.last().pose());
        Vec3 vec3 = new Vec3((double) (-armOffsetVec.x()), (double) (-armOffsetVec.y()), (double) armOffsetVec.z());
        translationStack.popPose();
        return vec3.add(0.0, 2.0, 0.0);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            LuxtructosaurusAshParticle particle = new LuxtructosaurusAshParticle(worldIn, x, y, z, (int) xSpeed, this.spriteSet);
            float hue = worldIn.f_46441_.nextFloat() * 0.1F;
            particle.m_107253_(0.9F + hue, 0.4F + hue, hue);
            return particle;
        }
    }
}