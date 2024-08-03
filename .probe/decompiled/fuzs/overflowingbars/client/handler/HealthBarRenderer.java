package fuzs.overflowingbars.client.handler;

import com.mojang.blaze3d.systems.RenderSystem;
import fuzs.overflowingbars.OverflowingBars;
import fuzs.overflowingbars.config.ClientConfig;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class HealthBarRenderer {

    public static final HealthBarRenderer INSTANCE = new HealthBarRenderer();

    private final RandomSource random = RandomSource.create();

    private int tickCount;

    private int lastHealth;

    private int displayHealth;

    private long lastHealthTime;

    private long healthBlinkTime;

    public void onStartTick(Minecraft minecraft) {
        this.tickCount++;
    }

    public void renderPlayerHealth(GuiGraphics guiGraphics, int posX, int posY, Player player, ProfilerFiller profiler) {
        profiler.push("health");
        BarOverlayRenderer.resetRenderState();
        RenderSystem.enableBlend();
        int currentHealth = Mth.ceil(player.m_21223_());
        boolean blink = this.healthBlinkTime > (long) this.tickCount && (this.healthBlinkTime - (long) this.tickCount) / 3L % 2L == 1L;
        long millis = Util.getMillis();
        if (currentHealth < this.lastHealth && player.f_19802_ > 0) {
            this.lastHealthTime = millis;
            this.healthBlinkTime = (long) (this.tickCount + 20);
        } else if (currentHealth > this.lastHealth && player.f_19802_ > 0) {
            this.lastHealthTime = millis;
            this.healthBlinkTime = (long) (this.tickCount + 10);
        }
        if (millis - this.lastHealthTime > 1000L) {
            this.displayHealth = currentHealth;
            this.lastHealthTime = millis;
        }
        this.lastHealth = currentHealth;
        int displayHealth = this.displayHealth;
        this.random.setSeed((long) (this.tickCount * 312871));
        float maxHealth = Math.max((float) player.m_21133_(Attributes.MAX_HEALTH), (float) Math.max(displayHealth, currentHealth));
        int currentAbsorption = Mth.ceil(player.getAbsorptionAmount());
        int heartOffsetByRegen = -1;
        if (player.m_21023_(MobEffects.REGENERATION)) {
            heartOffsetByRegen = this.tickCount % Mth.ceil(Math.min(20.0F, maxHealth) + 5.0F);
        }
        this.renderHearts(guiGraphics, player, posX, posY, heartOffsetByRegen, maxHealth, currentHealth, displayHealth, currentAbsorption, blink);
        RenderSystem.disableBlend();
        profiler.pop();
    }

    private void renderHearts(GuiGraphics guiGraphics, Player player, int posX, int posY, int heartOffsetByRegen, float maxHealth, int currentHealth, int displayHealth, int currentAbsorptionHealth, boolean blink) {
        boolean hardcore = player.m_9236_().getLevelData().isHardcore();
        int normalHearts = Math.min(10, Mth.ceil((double) maxHealth / 2.0));
        int maxAbsorptionHearts = 20 - normalHearts;
        int absorptionHearts = Math.min(20 - normalHearts, Mth.ceil((double) currentAbsorptionHealth / 2.0));
        for (int currentHeart = 0; currentHeart < normalHearts + absorptionHearts; currentHeart++) {
            int currentPosX = posX + currentHeart % 10 * 8;
            int currentPosY = posY - currentHeart / 10 * 10;
            if (currentHealth + currentAbsorptionHealth <= 4) {
                currentPosY += this.random.nextInt(2);
            }
            if (currentHeart < normalHearts && heartOffsetByRegen == currentHeart) {
                currentPosY -= 2;
            }
            this.renderHeart(guiGraphics, HealthBarRenderer.HeartType.CONTAINER, currentPosX, currentPosY, blink, false, hardcore);
            if (currentHeart >= normalHearts) {
                int currentAbsorption = currentHeart * 2 - normalHearts * 2;
                if (currentAbsorption < currentAbsorptionHealth) {
                    int maxAbsorptionHealth = maxAbsorptionHearts * 2;
                    boolean halfHeart = currentAbsorption + 1 == currentAbsorptionHealth % maxAbsorptionHealth;
                    boolean orange = currentAbsorptionHealth > maxAbsorptionHealth && currentAbsorption + 1 <= (currentAbsorptionHealth - 1) % maxAbsorptionHealth + 1;
                    if (halfHeart && orange) {
                        this.renderHeart(guiGraphics, HealthBarRenderer.HeartType.forPlayer(player, true, false), currentPosX, currentPosY, false, false, hardcore);
                    }
                    this.renderHeart(guiGraphics, HealthBarRenderer.HeartType.forPlayer(player, true, orange), currentPosX, currentPosY, false, halfHeart, hardcore);
                }
            }
            if (blink && currentHeart * 2 < Math.min(20, displayHealth)) {
                boolean halfHeart = currentHeart * 2 + 1 == (displayHealth - 1) % 20 + 1;
                boolean orange = displayHealth > 20 && currentHeart * 2 + 1 <= (displayHealth - 1) % 20 + 1;
                if (halfHeart && orange) {
                    this.renderHeart(guiGraphics, HealthBarRenderer.HeartType.forPlayer(player, false, false), currentPosX, currentPosY, true, false, hardcore);
                }
                this.renderHeart(guiGraphics, HealthBarRenderer.HeartType.forPlayer(player, false, orange || OverflowingBars.CONFIG.get(ClientConfig.class).health.colorizeFirstRow && currentHeart * 2 + 1 <= (displayHealth - 1) % 20 + 1), currentPosX, currentPosY, true, halfHeart, hardcore);
            }
            if (currentHeart * 2 < Math.min(20, currentHealth)) {
                boolean halfHeart = currentHeart * 2 + 1 == (currentHealth - 1) % 20 + 1;
                boolean orange = currentHealth > 20 && currentHeart * 2 + 1 <= (currentHealth - 1) % 20 + 1;
                if (halfHeart && orange) {
                    this.renderHeart(guiGraphics, HealthBarRenderer.HeartType.forPlayer(player, false, false), currentPosX, currentPosY, false, false, hardcore);
                }
                this.renderHeart(guiGraphics, HealthBarRenderer.HeartType.forPlayer(player, false, orange || OverflowingBars.CONFIG.get(ClientConfig.class).health.colorizeFirstRow && currentHeart * 2 + 1 <= (currentHealth - 1) % 20 + 1), currentPosX, currentPosY, false, halfHeart, hardcore);
            }
        }
    }

    private void renderHeart(GuiGraphics guiGraphics, HealthBarRenderer.HeartType heartType, int posX, int posY, boolean blink, boolean halfHeart, boolean hardcore) {
        guiGraphics.blit(heartType.textureSheet, posX, posY, heartType.getX(halfHeart, blink), heartType.getY(hardcore), 9, 9);
    }

    static enum HeartType {

        CONTAINER(0, false),
        NORMAL(2, true),
        POISONED(4, true),
        WITHERED(6, true),
        ABSORBING(8, false),
        FROZEN(9, false),
        ORANGE(0, 3, 4, BarOverlayRenderer.OVERFLOWING_ICONS_LOCATION, true);

        private final int textureIndexX;

        private final int textureIndexY;

        private final int hardcoreIndexY;

        public final ResourceLocation textureSheet;

        private final boolean canBlink;

        private HeartType(int textureIndexX, boolean blink) {
            this(textureIndexX, 0, 5, BarOverlayRenderer.GUI_ICONS_LOCATION, blink);
        }

        private HeartType(int textureIndexX, int textureIndexY, int hardcoreIndexY, ResourceLocation textureSheet, boolean blink) {
            this.textureIndexX = textureIndexX;
            this.textureIndexY = textureIndexY;
            this.hardcoreIndexY = hardcoreIndexY;
            this.textureSheet = textureSheet;
            this.canBlink = blink;
        }

        public int getX(boolean halfHeart, boolean blink) {
            int i;
            if (this == CONTAINER) {
                i = blink ? 1 : 0;
            } else {
                int j = halfHeart ? 1 : 0;
                int k = this.canBlink && blink ? 2 : 0;
                i = j + k;
            }
            return (this == ORANGE ? 0 : 16) + (this.textureIndexX * 2 + i) * 9;
        }

        public int getY(boolean hardcore) {
            return (hardcore ? this.hardcoreIndexY : this.textureIndexY) * 9;
        }

        public static HealthBarRenderer.HeartType forPlayer(Player player, boolean absorbing, boolean orange) {
            if (player.m_21023_(MobEffects.WITHER)) {
                return WITHERED;
            } else if (player.m_21023_(MobEffects.POISON)) {
                return POISONED;
            } else if (player.m_146890_()) {
                return FROZEN;
            } else {
                boolean inverse = OverflowingBars.CONFIG.get(ClientConfig.class).health.inverseColoring;
                if (!orange) {
                    return absorbing ? ABSORBING : (inverse ? ORANGE : NORMAL);
                } else {
                    return !absorbing && inverse ? NORMAL : ORANGE;
                }
            }
        }
    }
}