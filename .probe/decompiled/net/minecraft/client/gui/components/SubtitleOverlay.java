package net.minecraft.client.gui.components;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEventListener;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class SubtitleOverlay implements SoundEventListener {

    private static final long DISPLAY_TIME = 3000L;

    private final Minecraft minecraft;

    private final List<SubtitleOverlay.Subtitle> subtitles = Lists.newArrayList();

    private boolean isListening;

    public SubtitleOverlay(Minecraft minecraft0) {
        this.minecraft = minecraft0;
    }

    public void render(GuiGraphics guiGraphics0) {
        if (!this.isListening && this.minecraft.options.showSubtitles().get()) {
            this.minecraft.getSoundManager().addListener(this);
            this.isListening = true;
        } else if (this.isListening && !this.minecraft.options.showSubtitles().get()) {
            this.minecraft.getSoundManager().removeListener(this);
            this.isListening = false;
        }
        if (this.isListening && !this.subtitles.isEmpty()) {
            Vec3 $$1 = new Vec3(this.minecraft.player.m_20185_(), this.minecraft.player.m_20188_(), this.minecraft.player.m_20189_());
            Vec3 $$2 = new Vec3(0.0, 0.0, -1.0).xRot(-this.minecraft.player.m_146909_() * (float) (Math.PI / 180.0)).yRot(-this.minecraft.player.m_146908_() * (float) (Math.PI / 180.0));
            Vec3 $$3 = new Vec3(0.0, 1.0, 0.0).xRot(-this.minecraft.player.m_146909_() * (float) (Math.PI / 180.0)).yRot(-this.minecraft.player.m_146908_() * (float) (Math.PI / 180.0));
            Vec3 $$4 = $$2.cross($$3);
            int $$5 = 0;
            int $$6 = 0;
            double $$7 = this.minecraft.options.notificationDisplayTime().get();
            Iterator<SubtitleOverlay.Subtitle> $$8 = this.subtitles.iterator();
            while ($$8.hasNext()) {
                SubtitleOverlay.Subtitle $$9 = (SubtitleOverlay.Subtitle) $$8.next();
                if ((double) $$9.getTime() + 3000.0 * $$7 <= (double) Util.getMillis()) {
                    $$8.remove();
                } else {
                    $$6 = Math.max($$6, this.minecraft.font.width($$9.getText()));
                }
            }
            $$6 += this.minecraft.font.width("<") + this.minecraft.font.width(" ") + this.minecraft.font.width(">") + this.minecraft.font.width(" ");
            for (SubtitleOverlay.Subtitle $$10 : this.subtitles) {
                int $$11 = 255;
                Component $$12 = $$10.getText();
                Vec3 $$13 = $$10.getLocation().subtract($$1).normalize();
                double $$14 = -$$4.dot($$13);
                double $$15 = -$$2.dot($$13);
                boolean $$16 = $$15 > 0.5;
                int $$17 = $$6 / 2;
                int $$18 = 9;
                int $$19 = $$18 / 2;
                float $$20 = 1.0F;
                int $$21 = this.minecraft.font.width($$12);
                int $$22 = Mth.floor(Mth.clampedLerp(255.0F, 75.0F, (float) (Util.getMillis() - $$10.getTime()) / (float) (3000.0 * $$7)));
                int $$23 = $$22 << 16 | $$22 << 8 | $$22;
                guiGraphics0.pose().pushPose();
                guiGraphics0.pose().translate((float) guiGraphics0.guiWidth() - (float) $$17 * 1.0F - 2.0F, (float) (guiGraphics0.guiHeight() - 35) - (float) ($$5 * ($$18 + 1)) * 1.0F, 0.0F);
                guiGraphics0.pose().scale(1.0F, 1.0F, 1.0F);
                guiGraphics0.fill(-$$17 - 1, -$$19 - 1, $$17 + 1, $$19 + 1, this.minecraft.options.getBackgroundColor(0.8F));
                int $$24 = $$23 + -16777216;
                if (!$$16) {
                    if ($$14 > 0.0) {
                        guiGraphics0.drawString(this.minecraft.font, ">", $$17 - this.minecraft.font.width(">"), -$$19, $$24);
                    } else if ($$14 < 0.0) {
                        guiGraphics0.drawString(this.minecraft.font, "<", -$$17, -$$19, $$24);
                    }
                }
                guiGraphics0.drawString(this.minecraft.font, $$12, -$$21 / 2, -$$19, $$24);
                guiGraphics0.pose().popPose();
                $$5++;
            }
        }
    }

    @Override
    public void onPlaySound(SoundInstance soundInstance0, WeighedSoundEvents weighedSoundEvents1) {
        if (weighedSoundEvents1.getSubtitle() != null) {
            Component $$2 = weighedSoundEvents1.getSubtitle();
            if (!this.subtitles.isEmpty()) {
                for (SubtitleOverlay.Subtitle $$3 : this.subtitles) {
                    if ($$3.getText().equals($$2)) {
                        $$3.refresh(new Vec3(soundInstance0.getX(), soundInstance0.getY(), soundInstance0.getZ()));
                        return;
                    }
                }
            }
            this.subtitles.add(new SubtitleOverlay.Subtitle($$2, new Vec3(soundInstance0.getX(), soundInstance0.getY(), soundInstance0.getZ())));
        }
    }

    public static class Subtitle {

        private final Component text;

        private long time;

        private Vec3 location;

        public Subtitle(Component component0, Vec3 vec1) {
            this.text = component0;
            this.location = vec1;
            this.time = Util.getMillis();
        }

        public Component getText() {
            return this.text;
        }

        public long getTime() {
            return this.time;
        }

        public Vec3 getLocation() {
            return this.location;
        }

        public void refresh(Vec3 vec0) {
            this.location = vec0;
            this.time = Util.getMillis();
        }
    }
}