package com.simibubi.create.content.equipment.potatoCannon;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.equipment.zapper.ShootableGadgetRenderHandler;
import com.simibubi.create.foundation.particle.AirParticleData;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class PotatoCannonRenderHandler extends ShootableGadgetRenderHandler {

    private float nextPitch;

    @Override
    protected void playSound(InteractionHand hand, Vec3 position) {
        PotatoProjectileEntity.playLaunchSound(Minecraft.getInstance().level, position, this.nextPitch);
    }

    @Override
    protected boolean appliesTo(ItemStack stack) {
        return ((PotatoCannonItem) AllItems.POTATO_CANNON.get()).isCannon(stack);
    }

    public void beforeShoot(float nextPitch, Vec3 location, Vec3 motion, ItemStack stack) {
        this.nextPitch = nextPitch;
        if (!stack.isEmpty()) {
            ClientLevel world = Minecraft.getInstance().level;
            for (int i = 0; i < 2; i++) {
                Vec3 m = VecHelper.offsetRandomly(motion.scale(0.1F), world.f_46441_, 0.025F);
                world.addParticle(new ItemParticleOption(ParticleTypes.ITEM, stack), location.x, location.y, location.z, m.x, m.y, m.z);
                Vec3 m2 = VecHelper.offsetRandomly(motion.scale(2.0), world.f_46441_, 0.5F);
                world.addParticle(new AirParticleData(1.0F, 0.25F), location.x, location.y, location.z, m2.x, m2.y, m2.z);
            }
        }
    }

    @Override
    protected void transformTool(PoseStack ms, float flip, float equipProgress, float recoil, float pt) {
        ms.translate(flip * -0.1F, 0.0F, 0.14F);
        ms.scale(0.75F, 0.75F, 0.75F);
        TransformStack.cast(ms).rotateX((double) (recoil * 80.0F));
    }

    @Override
    protected void transformHand(PoseStack ms, float flip, float equipProgress, float recoil, float pt) {
        ms.translate((double) flip * -0.09, -0.275, -0.25);
        TransformStack.cast(ms).rotateZ((double) (flip * -10.0F));
    }
}