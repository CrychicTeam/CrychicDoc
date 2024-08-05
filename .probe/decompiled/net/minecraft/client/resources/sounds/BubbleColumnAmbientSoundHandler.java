package net.minecraft.client.resources.sounds;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BubbleColumnBlock;
import net.minecraft.world.level.block.state.BlockState;

public class BubbleColumnAmbientSoundHandler implements AmbientSoundHandler {

    private final LocalPlayer player;

    private boolean wasInBubbleColumn;

    private boolean firstTick = true;

    public BubbleColumnAmbientSoundHandler(LocalPlayer localPlayer0) {
        this.player = localPlayer0;
    }

    @Override
    public void tick() {
        Level $$0 = this.player.m_9236_();
        BlockState $$1 = (BlockState) $$0.m_46847_(this.player.m_20191_().inflate(0.0, -0.4F, 0.0).deflate(1.0E-6)).filter(p_119669_ -> p_119669_.m_60713_(Blocks.BUBBLE_COLUMN)).findFirst().orElse(null);
        if ($$1 != null) {
            if (!this.wasInBubbleColumn && !this.firstTick && $$1.m_60713_(Blocks.BUBBLE_COLUMN) && !this.player.m_5833_()) {
                boolean $$2 = (Boolean) $$1.m_61143_(BubbleColumnBlock.DRAG_DOWN);
                if ($$2) {
                    this.player.playSound(SoundEvents.BUBBLE_COLUMN_WHIRLPOOL_INSIDE, 1.0F, 1.0F);
                } else {
                    this.player.playSound(SoundEvents.BUBBLE_COLUMN_UPWARDS_INSIDE, 1.0F, 1.0F);
                }
            }
            this.wasInBubbleColumn = true;
        } else {
            this.wasInBubbleColumn = false;
        }
        this.firstTick = false;
    }
}