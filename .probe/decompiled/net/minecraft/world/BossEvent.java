package net.minecraft.world;

import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public abstract class BossEvent {

    private final UUID id;

    protected Component name;

    protected float progress;

    protected BossEvent.BossBarColor color;

    protected BossEvent.BossBarOverlay overlay;

    protected boolean darkenScreen;

    protected boolean playBossMusic;

    protected boolean createWorldFog;

    public BossEvent(UUID uUID0, Component component1, BossEvent.BossBarColor bossEventBossBarColor2, BossEvent.BossBarOverlay bossEventBossBarOverlay3) {
        this.id = uUID0;
        this.name = component1;
        this.color = bossEventBossBarColor2;
        this.overlay = bossEventBossBarOverlay3;
        this.progress = 1.0F;
    }

    public UUID getId() {
        return this.id;
    }

    public Component getName() {
        return this.name;
    }

    public void setName(Component component0) {
        this.name = component0;
    }

    public float getProgress() {
        return this.progress;
    }

    public void setProgress(float float0) {
        this.progress = float0;
    }

    public BossEvent.BossBarColor getColor() {
        return this.color;
    }

    public void setColor(BossEvent.BossBarColor bossEventBossBarColor0) {
        this.color = bossEventBossBarColor0;
    }

    public BossEvent.BossBarOverlay getOverlay() {
        return this.overlay;
    }

    public void setOverlay(BossEvent.BossBarOverlay bossEventBossBarOverlay0) {
        this.overlay = bossEventBossBarOverlay0;
    }

    public boolean shouldDarkenScreen() {
        return this.darkenScreen;
    }

    public BossEvent setDarkenScreen(boolean boolean0) {
        this.darkenScreen = boolean0;
        return this;
    }

    public boolean shouldPlayBossMusic() {
        return this.playBossMusic;
    }

    public BossEvent setPlayBossMusic(boolean boolean0) {
        this.playBossMusic = boolean0;
        return this;
    }

    public BossEvent setCreateWorldFog(boolean boolean0) {
        this.createWorldFog = boolean0;
        return this;
    }

    public boolean shouldCreateWorldFog() {
        return this.createWorldFog;
    }

    public static enum BossBarColor {

        PINK("pink", ChatFormatting.RED),
        BLUE("blue", ChatFormatting.BLUE),
        RED("red", ChatFormatting.DARK_RED),
        GREEN("green", ChatFormatting.GREEN),
        YELLOW("yellow", ChatFormatting.YELLOW),
        PURPLE("purple", ChatFormatting.DARK_BLUE),
        WHITE("white", ChatFormatting.WHITE);

        private final String name;

        private final ChatFormatting formatting;

        private BossBarColor(String p_18881_, ChatFormatting p_18882_) {
            this.name = p_18881_;
            this.formatting = p_18882_;
        }

        public ChatFormatting getFormatting() {
            return this.formatting;
        }

        public String getName() {
            return this.name;
        }

        public static BossEvent.BossBarColor byName(String p_18885_) {
            for (BossEvent.BossBarColor $$1 : values()) {
                if ($$1.name.equals(p_18885_)) {
                    return $$1;
                }
            }
            return WHITE;
        }
    }

    public static enum BossBarOverlay {

        PROGRESS("progress"), NOTCHED_6("notched_6"), NOTCHED_10("notched_10"), NOTCHED_12("notched_12"), NOTCHED_20("notched_20");

        private final String name;

        private BossBarOverlay(String p_18901_) {
            this.name = p_18901_;
        }

        public String getName() {
            return this.name;
        }

        public static BossEvent.BossBarOverlay byName(String p_18904_) {
            for (BossEvent.BossBarOverlay $$1 : values()) {
                if ($$1.name.equals(p_18904_)) {
                    return $$1;
                }
            }
            return PROGRESS;
        }
    }
}