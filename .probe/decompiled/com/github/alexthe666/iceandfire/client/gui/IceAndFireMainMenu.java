package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.client.ForgeHooksClient;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

public class IceAndFireMainMenu extends TitleScreen {

    public static final int LAYER_COUNT = 2;

    public static final ResourceLocation splash = new ResourceLocation("iceandfire", "splashes.txt");

    private static final ResourceLocation MINECRAFT_TITLE_TEXTURES = new ResourceLocation("textures/gui/title/minecraft.png");

    private static final ResourceLocation BESTIARY_TEXTURE = new ResourceLocation("iceandfire:textures/gui/main_menu/bestiary_menu.png");

    private static final ResourceLocation TABLE_TEXTURE = new ResourceLocation("iceandfire:textures/gui/main_menu/table.png");

    public static ResourceLocation[] pageFlipTextures;

    public static ResourceLocation[] drawingTextures = new ResourceLocation[22];

    private int layerTick;

    private String splashText;

    private boolean isFlippingPage = false;

    private int pageFlip = 0;

    private IceAndFireMainMenu.Picture[] drawnPictures;

    private IceAndFireMainMenu.Enscription[] drawnEnscriptions;

    private float globalAlpha = 1.0F;

    public IceAndFireMainMenu() {
        pageFlipTextures = new ResourceLocation[] { new ResourceLocation("iceandfire", "textures/gui/main_menu/page_1.png"), new ResourceLocation("iceandfire", "textures/gui/main_menu/page_2.png"), new ResourceLocation("iceandfire", "textures/gui/main_menu/page_3.png"), new ResourceLocation("iceandfire", "textures/gui/main_menu/page_4.png"), new ResourceLocation("iceandfire", "textures/gui/main_menu/page_5.png"), new ResourceLocation("iceandfire", "textures/gui/main_menu/page_6.png") };
        for (int i = 0; i < drawingTextures.length; i++) {
            drawingTextures[i] = new ResourceLocation("iceandfire", "textures/gui/main_menu/drawing_" + (i + 1) + ".png");
        }
        this.resetDrawnImages();
        String branch = "1.17";
        try {
            BufferedReader reader = getURLContents("https://raw.githubusercontent.com/Alex-the-666/Ice_and_Fire/1.17/src/main/resources/assets/iceandfire/splashes.txt", "assets/iceandfire/splashes.txt");
            try {
                List<String> list = IOUtils.readLines(reader);
                if (!list.isEmpty()) {
                    do {
                        this.splashText = (String) list.get(ThreadLocalRandom.current().nextInt(list.size()));
                    } while (this.splashText.hashCode() == 125780783);
                }
            } catch (Throwable var6) {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Throwable var5) {
                        var6.addSuppressed(var5);
                    }
                }
                throw var6;
            }
            if (reader != null) {
                reader.close();
            }
        } catch (IOException var7) {
            IceAndFire.LOGGER.error("Exception trying to collect splash screen lines: ", var7);
        }
    }

    public static BufferedReader getURLContents(String urlString, String backupFileLoc) {
        BufferedReader reader = null;
        boolean useBackup = false;
        URL url;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException var8) {
            url = null;
            useBackup = true;
        }
        if (url != null) {
            URLConnection connection = null;
            try {
                connection = url.openConnection();
                connection.setConnectTimeout(200);
                InputStream is = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(is));
            } catch (IOException var7) {
                IceAndFire.LOGGER.warn("Ice and Fire couldn't download splash texts for main menu");
                useBackup = true;
            }
        }
        if (useBackup) {
            InputStream is = IceAndFireMainMenu.class.getClassLoader().getResourceAsStream(backupFileLoc);
            if (is != null) {
                reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            }
        }
        return reader;
    }

    private void resetDrawnImages() {
        this.globalAlpha = 0.0F;
        Random random = ThreadLocalRandom.current();
        this.drawnPictures = new IceAndFireMainMenu.Picture[1 + random.nextInt(2)];
        boolean left = random.nextBoolean();
        for (int i = 0; i < this.drawnPictures.length; i++) {
            left = !left;
            int y = random.nextInt(25);
            int x;
            if (left) {
                x = -15 - random.nextInt(20) - 128;
            } else {
                x = 30 + random.nextInt(20);
            }
            this.drawnPictures[i] = new IceAndFireMainMenu.Picture(random.nextInt(drawingTextures.length - 1), x, y, 0.5F, random.nextFloat() * 0.5F + 0.5F);
        }
        this.drawnEnscriptions = new IceAndFireMainMenu.Enscription[4 + random.nextInt(8)];
        for (int i = 0; i < this.drawnEnscriptions.length; i++) {
            left = !left;
            int y = 10 + random.nextInt(130);
            int x;
            if (left) {
                x = -30 - random.nextInt(30) - 50;
            } else {
                x = 30 + random.nextInt(30);
            }
            String s1 = "missingno";
            this.drawnEnscriptions[i] = new IceAndFireMainMenu.Enscription(s1, x, y, random.nextFloat() * 0.5F + 0.5F, 10259323);
        }
    }

    @Override
    public void tick() {
        super.tick();
        float flipTick = (float) (this.layerTick % 40);
        if (this.globalAlpha < 1.0F && !this.isFlippingPage && flipTick < 30.0F) {
            this.globalAlpha += 0.1F;
        }
        if (this.globalAlpha > 0.0F && flipTick > 30.0F) {
            this.globalAlpha -= 0.1F;
        }
        if (flipTick == 0.0F && !this.isFlippingPage) {
            this.isFlippingPage = true;
        }
        if (this.isFlippingPage) {
            if (this.layerTick % 2 == 0) {
                this.pageFlip++;
            }
            if (this.pageFlip == 6) {
                this.pageFlip = 0;
                this.isFlippingPage = false;
                this.resetDrawnImages();
            }
        }
        this.layerTick++;
    }

    @Override
    public void render(@NotNull GuiGraphics ms, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        int width = this.f_96543_;
        int height = this.f_96544_;
        ms.blit(TABLE_TEXTURE, 0, 0, 0.0F, 0.0F, width, height, width, height);
        ms.blit(BESTIARY_TEXTURE, 50, 0, 0.0F, 0.0F, width - 100, height, width - 100, height);
        float f11 = 1.0F;
        int l = Mth.ceil(f11 * 255.0F) << 24;
        if (this.isFlippingPage) {
            ms.blit(pageFlipTextures[Math.min(5, this.pageFlip)], 50, 0, 0.0F, 0.0F, width - 100, height, width - 100, height);
        } else {
            int middleX = width / 2;
            int middleY = height / 5;
            float widthScale = (float) width / 427.0F;
            float heightScale = (float) height / 427.0F;
            float imageScale = Math.min(widthScale, heightScale) * 192.0F;
            for (IceAndFireMainMenu.Picture picture : this.drawnPictures) {
                float alpha = picture.alpha * this.globalAlpha + 0.01F;
                RenderSystem.enableBlend();
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                ms.blit(drawingTextures[picture.image], (int) ((float) picture.x * widthScale) + middleX, (int) ((float) picture.y * heightScale + (float) middleY), 0.0F, 0.0F, (int) imageScale, (int) imageScale, (int) imageScale, (int) imageScale);
                RenderSystem.disableBlend();
            }
        }
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager._enableBlend();
        this.getMinecraft().font.drawInBatch("Ice and Fire " + ChatFormatting.YELLOW + IceAndFire.VERSION, 2.0F, (float) (height - 10), -1, false, ms.pose().last().pose(), ms.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        ms.blit(MINECRAFT_TITLE_TEXTURES, width / 2 - 128, 10, 0.0F, 0.0F, 256, 64, 256, 64);
        ForgeHooksClient.renderMainMenu(this, ms, this.getMinecraft().font, width, height, l);
        if (this.splashText != null) {
            ms.pose().pushPose();
            ms.pose().translate((double) (this.f_96543_ / 2 + 90), 70.0, 0.0);
            ms.pose().mulPose(Axis.ZP.rotationDegrees(-20.0F));
            float f2 = 1.8F - Mth.abs(Mth.sin((float) (Util.getMillis() % 1000L) / 1000.0F * (float) (Math.PI * 2)) * 0.1F);
            f2 = f2 * 100.0F / (float) (this.f_96547_.width(this.splashText) + 32);
            ms.pose().scale(f2, f2, f2);
            ms.drawCenteredString(this.f_96547_, this.splashText, 0, -8, 16776960 | l);
            ms.pose().popPose();
        }
        String s1 = "Copyright Mojang AB. Do not distribute!";
        Font font = this.getMinecraft().font;
        ms.drawString(font, s1, width - this.getMinecraft().font.width(s1) - 2, height - 10, -1);
        for (int i = 0; i < this.f_169369_.size(); i++) {
            ((Renderable) this.f_169369_.get(i)).render(ms, mouseX, mouseY, partialTicks);
        }
        for (int i = 0; i < this.f_169369_.size(); i++) {
            ((Renderable) this.f_169369_.get(i)).render(ms, mouseX, mouseY, this.getMinecraft().getFrameTime());
        }
    }

    private class Enscription {

        public Enscription(String text, int x, int y, float alpha, int color) {
        }
    }

    private class Picture {

        int image;

        int x;

        int y;

        float alpha;

        public Picture(int image, int x, int y, float alpha, float scale) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.alpha = alpha;
        }
    }
}