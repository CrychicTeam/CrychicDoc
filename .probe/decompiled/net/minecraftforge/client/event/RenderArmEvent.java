package net.minecraftforge.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.ApiStatus.Internal;

@Cancelable
public class RenderArmEvent extends Event {

    private final PoseStack poseStack;

    private final MultiBufferSource multiBufferSource;

    private final int packedLight;

    private final AbstractClientPlayer player;

    private final HumanoidArm arm;

    @Internal
    public RenderArmEvent(PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, AbstractClientPlayer player, HumanoidArm arm) {
        this.poseStack = poseStack;
        this.multiBufferSource = multiBufferSource;
        this.packedLight = packedLight;
        this.player = player;
        this.arm = arm;
    }

    public HumanoidArm getArm() {
        return this.arm;
    }

    public PoseStack getPoseStack() {
        return this.poseStack;
    }

    public MultiBufferSource getMultiBufferSource() {
        return this.multiBufferSource;
    }

    public int getPackedLight() {
        return this.packedLight;
    }

    public AbstractClientPlayer getPlayer() {
        return this.player;
    }
}