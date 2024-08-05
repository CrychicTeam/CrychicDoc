package snownee.jade.gui;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import snownee.jade.Jade;
import snownee.jade.api.config.IWailaConfig;
import snownee.jade.impl.WailaClientRegistration;
import snownee.jade.impl.config.PluginConfig;
import snownee.jade.util.ModIdentification;
import snownee.jade.util.SmoothChasingValue;

public class HomeConfigScreen extends Screen {

    private final RandomSource random = RandomSource.create(42L);

    private final Screen parent;

    private final SmoothChasingValue titleY;

    private final SmoothChasingValue creditHover;

    private final Component credit;

    private final List<HomeConfigScreen.TextParticle> particles = Lists.newArrayList();

    private int creditWidth;

    private boolean hovered;

    private float ticks;

    private byte festival;

    public HomeConfigScreen(Screen parent) {
        super(Component.translatable("gui.jade.configuration"));
        this.parent = parent;
        this.titleY = new SmoothChasingValue().start(8.0F).target(32.0F).withSpeed(0.1F);
        this.creditHover = new SmoothChasingValue();
        LocalDate now = LocalDate.now();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        if (month == 12 && day >= 24 && day <= 26) {
            this.festival = 1;
        } else if (month == 6 && day == 28) {
            this.festival = 2;
        } else {
            Int2IntOpenHashMap newyears = new Int2IntOpenHashMap();
            newyears.put(2024, 210);
            newyears.put(2025, 129);
            newyears.put(2026, 217);
            newyears.put(2027, 206);
            newyears.put(2028, 126);
            newyears.put(2029, 213);
            newyears.put(2030, 203);
            newyears.put(2031, 123);
            newyears.put(2032, 211);
            newyears.put(2033, 131);
            newyears.put(2034, 219);
            newyears.put(2035, 208);
            newyears.put(2036, 128);
            newyears.put(2037, 215);
            newyears.put(2038, 204);
            newyears.put(2039, 124);
            newyears.put(2040, 212);
            newyears.put(2041, 201);
            newyears.put(2042, 122);
            newyears.put(2043, 210);
            int year = now.getYear();
            if (newyears.containsKey(year)) {
                int newyearMonth = newyears.get(year) / 100;
                int newyearDay = newyears.get(year) % 100;
                LocalDate newyearDate = LocalDate.of(year, newyearMonth, newyearDay);
                int newyearDayofyear = newyearDate.getDayOfYear();
                int dayofyear = now.getDayOfYear();
                if (dayofyear >= newyearDayofyear - 1 && dayofyear <= newyearDayofyear + 2) {
                    this.festival = 99;
                }
            }
        }
        this.credit = Component.translatable("gui.jade.by", Component.literal("❤").withStyle(ChatFormatting.RED)).withStyle(s -> {
            if (this.festival != 0) {
                s = s.withColor(15852452);
            }
            return s;
        });
    }

    @Override
    protected void init() {
        Objects.requireNonNull(this.f_96541_);
        this.creditWidth = this.f_96547_.width(this.credit);
        this.m_142416_(Button.builder(Component.translatable("gui.jade.jade_settings"), w -> this.f_96541_.setScreen(new WailaConfigScreen(this))).bounds(this.f_96543_ / 2 - 105, this.f_96544_ / 2 - 10, 100, 20).build());
        this.m_142416_(Button.builder(Component.translatable("gui.jade.plugin_settings"), w -> this.f_96541_.setScreen(new PluginsConfigScreen(this))).bounds(this.f_96543_ / 2 + 5, this.f_96544_ / 2 - 10, 100, 20).build());
        this.m_142416_(Button.builder(CommonComponents.GUI_DONE, w -> this.onClose()).bounds(this.f_96543_ / 2 - 50, this.f_96544_ / 2 + 20, 100, 20).build());
    }

    @Override
    public void onClose() {
        Jade.CONFIG.save();
        PluginConfig.INSTANCE.save();
        WailaClientRegistration.instance().reloadBlocklists();
        ((Minecraft) Objects.requireNonNull(this.f_96541_)).setScreen(this.parent);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
        Objects.requireNonNull(this.f_96541_);
        this.ticks += partialTicks;
        this.m_280273_(guiGraphics);
        boolean smallUI = this.f_96541_.getWindow().getGuiScale() < 3.0;
        int left = this.f_96543_ / 2 - 105;
        int top = this.f_96544_ / 4 - 20;
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate((float) left, (float) top, 0.0F);
        float scale = smallUI ? 2.0F : 1.5F;
        guiGraphics.pose().scale(scale, scale, scale);
        guiGraphics.drawString(this.f_96547_, (String) ModIdentification.getModName("jade").orElse("Jade"), 0, 0, 16777215);
        guiGraphics.pose().scale(0.5F, 0.5F, 0.5F);
        this.titleY.tick(partialTicks);
        String desc2 = I18n.get("gui.jade.configuration.desc2");
        float scaledX;
        float scaledY;
        if (desc2.isEmpty()) {
            guiGraphics.pose().popPose();
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate((float) left, (float) top, 0.0F);
            scaledX = (float) (x - left);
            scaledY = (float) (y - top);
        } else {
            scaledX = (float) (x - left) / scale * 2.0F;
            scaledY = (float) (y - top) / scale * 2.0F;
        }
        this.drawFancyTitle(guiGraphics, I18n.get("gui.jade.configuration.desc1"), Math.min(this.titleY.value, 20.0F), 20.0F, scaledX, scaledY);
        if (!desc2.isEmpty()) {
            this.drawFancyTitle(guiGraphics, desc2, this.titleY.value, 32.0F, scaledX, scaledY);
        }
        guiGraphics.pose().popPose();
        super.render(guiGraphics, x, y, partialTicks);
        int creditX = (int) ((float) this.f_96543_ * 0.5F - (float) this.creditWidth * 0.5F);
        int creditY = (int) ((float) this.f_96544_ * 0.9F - 5.0F);
        boolean hover = x >= creditX && x < creditX + this.creditWidth && y >= creditY && y < creditY + 10;
        if (!this.hovered && hover) {
            this.creditHover.target(1.0F);
        } else if (!hover) {
            this.creditHover.target(0.0F);
        } else if ((double) this.creditHover.value > 0.5) {
            this.creditHover.target(0.0F);
            IntList colors = new IntArrayList();
            String text = "❄";
            if (this.festival == 2) {
                for (int i = 0; i < 11; i++) {
                    colors.add(Mth.hsvToRgb(this.random.nextFloat(), 0.8F, 0.9F));
                }
                text = "❤";
            } else if (this.festival == 1) {
                IntList palette = IntList.of(new int[] { 14083301, 14083301, 15726069, 15726069, 4813172, 15426624 });
                for (int i = 0; i < 11; i++) {
                    colors.add(palette.getInt(this.random.nextInt(palette.size())));
                }
            } else if (this.festival != 99) {
                for (int i = 0; i < 11; i++) {
                    colors.add(Mth.color(1.0F - this.random.nextFloat() * 0.6F, 1.0F, 1.0F));
                }
            } else {
                for (int i = 0; i < 11; i++) {
                    colors.add(this.random.nextBoolean() ? 11010048 : 12589056);
                }
                text = "✐";
            }
            IntListIterator var25 = colors.iterator();
            while (var25.hasNext()) {
                int color = (Integer) var25.next();
                int ox = this.random.nextIntBetweenInclusive(-this.creditWidth / 2, this.creditWidth / 2);
                HomeConfigScreen.TextParticle particle = new HomeConfigScreen.TextParticle(text, (float) this.f_96543_ * 0.5F + (float) ox, (float) (creditY + this.random.nextInt(10)), (float) ox * 0.08F, -5.0F - this.random.nextFloat() * 3.0F, color, 0.75F + this.random.nextFloat() * 0.5F);
                this.particles.add(particle);
                if (this.festival == 99) {
                    particle.age = 8.0F + this.random.nextFloat() * 5.0F;
                }
            }
        }
        this.creditHover.tick(partialTicks);
        this.creditHover.value = Math.min(0.6F, this.creditHover.value);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate((float) this.f_96543_ * 0.5F, (float) creditY, 0.0F);
        scale = 1.0F + this.creditHover.value * 0.2F;
        guiGraphics.pose().scale(scale, scale, scale);
        guiGraphics.pose().translate((float) this.creditWidth * -0.5F, 0.0F, 0.0F);
        guiGraphics.drawString(this.f_96547_, this.credit, 0, 0, 1442840575);
        guiGraphics.pose().popPose();
        this.hovered = hover;
        this.particles.removeIf(p -> {
            p.tick(partialTicks);
            if (p.y > (float) this.f_96544_) {
                return true;
            } else {
                p.render(guiGraphics, this.f_96547_);
                return false;
            }
        });
    }

    private void drawFancyTitle(GuiGraphics guiGraphics, String text, float y, float expectY, float mouseX, float mouseY) {
        float distY = Math.abs(y - expectY);
        if (!(distY >= 9.0F)) {
            int color = IWailaConfig.IConfigOverlay.applyAlpha(11184810, 1.0F - distY / 10.0F);
            ((JadeFont) this.f_96547_).jade$setGlint((this.ticks - y / 5.0F) % 90.0F / 45.0F * (float) this.f_96543_, mouseX);
            ((JadeFont) this.f_96547_).jade$setGlintStrength(1.0F, 1.0F - Mth.clamp(Math.abs(mouseY - y) / 20.0F, 0.0F, 1.0F));
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0.0F, y, 0.0F);
            guiGraphics.drawString(this.f_96547_, text, 0, 0, color);
            guiGraphics.pose().popPose();
            ((JadeFont) this.f_96547_).jade$setGlint(Float.NaN, Float.NaN);
        }
    }

    private class TextParticle {

        private float age;

        private String text;

        private float x;

        private float y;

        private float motionX;

        private float motionY;

        private int color;

        private float scale;

        public TextParticle(String text, float x, float y, float motionX, float motionY, int color, float scale) {
            this.text = text;
            this.x = x;
            this.y = y;
            this.motionX = motionX;
            this.motionY = motionY;
            this.color = color;
            this.scale = scale;
        }

        private void tick(float partialTicks) {
            this.x = this.x + this.motionX * partialTicks;
            this.y = this.y + this.motionY * partialTicks;
            this.motionY += 0.98F * partialTicks;
            if (HomeConfigScreen.this.festival == 99) {
                boolean geaterThanZero = this.age > 0.0F;
                this.age -= partialTicks;
                if (geaterThanZero && this.age <= 0.0F) {
                    this.text = HomeConfigScreen.this.random.nextBoolean() ? "✴" : "✳";
                    this.color = HomeConfigScreen.this.random.nextBoolean() ? 16765991 : 15778837;
                    Objects.requireNonNull(HomeConfigScreen.this.f_96541_);
                    HomeConfigScreen.this.f_96541_.getSoundManager().play(SimpleSoundInstance.forUI(HomeConfigScreen.this.random.nextBoolean() ? SoundEvents.FIREWORK_ROCKET_BLAST : SoundEvents.FIREWORK_ROCKET_LARGE_BLAST, 0.7F));
                }
            }
        }

        private void render(GuiGraphics guiGraphics, Font font) {
            if (HomeConfigScreen.this.festival != 99 || !(this.age < -4.0F)) {
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(this.x, this.y, 0.0F);
                guiGraphics.pose().scale(this.scale, this.scale, this.scale);
                guiGraphics.drawString(font, this.text, 0, 0, this.color);
                guiGraphics.pose().popPose();
            }
        }
    }
}