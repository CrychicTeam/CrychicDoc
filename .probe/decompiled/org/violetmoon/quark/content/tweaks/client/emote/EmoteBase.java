package org.violetmoon.quark.content.tweaks.client.emote;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.TweenManager;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.player.Player;

public abstract class EmoteBase {

    public static final float PI_F = (float) Math.PI;

    public final EmoteDescriptor desc;

    private final TweenManager emoteManager;

    private final HumanoidModel<?> model;

    private final HumanoidModel<?> armorModel;

    private final HumanoidModel<?> armorLegsModel;

    private final EmoteState state;

    private final Player player;

    public float timeDone;

    public float totalTime;

    public float animatedTime;

    private long lastMs;

    public EmoteBase(EmoteDescriptor desc, Player player, HumanoidModel<?> model, HumanoidModel<?> armorModel, HumanoidModel<?> armorLegsModel) {
        this.desc = desc;
        this.emoteManager = new TweenManager();
        this.state = new EmoteState(this);
        this.model = model;
        this.armorModel = armorModel;
        this.armorLegsModel = armorLegsModel;
        this.player = player;
    }

    public void startAllTimelines() {
        this.startTimeline(this.player, this.model);
        this.startTimeline(this.player, this.armorModel);
        this.startTimeline(this.player, this.armorLegsModel);
        this.lastMs = System.currentTimeMillis();
    }

    private void startTimeline(Player player, HumanoidModel<?> model) {
        Timeline timeline = this.getTimeline(player, model).start(this.emoteManager);
        this.totalTime = timeline.getFullDuration();
    }

    public abstract Timeline getTimeline(Player var1, HumanoidModel<?> var2);

    public abstract boolean usesBodyPart(int var1);

    public void rotateAndOffset(PoseStack stack) {
        this.state.rotateAndOffset(stack, this.player);
    }

    public void update() {
        this.state.load(this.model);
        this.state.load(this.armorModel);
        this.state.load(this.armorLegsModel);
        long currTime = System.currentTimeMillis();
        long timeDiff = currTime - this.lastMs;
        this.animatedTime += (float) timeDiff;
        this.emoteManager.update((float) timeDiff);
        this.state.save(this.model);
        this.lastMs = currTime;
        this.timeDone += (float) timeDiff;
    }

    public boolean isDone() {
        return this.timeDone >= this.totalTime || this.player.f_20921_ > 0.0F || this.player.f_20916_ > 0;
    }
}