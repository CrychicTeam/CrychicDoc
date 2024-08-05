package com.github.alexthe666.citadel.client.game;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.Vec3i;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.registries.ForgeRegistries;

public class Tetris {

    protected final RandomSource random = RandomSource.create();

    private boolean started = false;

    private int score;

    private int renderTime = 0;

    private int keyCooldown;

    private static int HEIGHT = 20;

    private TetrominoShape fallingShape;

    private BlockState fallingBlock;

    private float fallingX;

    private float prevFallingY;

    private float fallingY;

    private Rotation fallingRotation;

    private BlockState[][] settledBlocks = new BlockState[10][HEIGHT];

    private boolean gameOver = false;

    private TetrominoShape nextShape;

    private BlockState nextBlock;

    private boolean[] flashingLayer = new boolean[HEIGHT];

    private int flashFor = 0;

    private final Block[] allRegisteredBlocks = (Block[]) ForgeRegistries.BLOCKS.getValues().stream().toArray(Block[]::new);

    public Tetris() {
        this.reset();
    }

    public void tick() {
        this.renderTime++;
        this.prevFallingY = this.fallingY;
        if (this.keyCooldown > 0) {
            this.keyCooldown--;
        }
        if (this.started && !this.gameOver) {
            if (this.fallingShape == null) {
                this.generateTetromino();
                this.generateNextTetromino();
            } else if (this.groundedTetromino()) {
                this.groundTetromino();
                this.fallingShape = null;
            } else {
                float f = 0.15F;
                if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 264)) {
                    f = 1.0F;
                }
                this.fallingY += f;
                if (this.keyPressed(263) && !this.isBlocksInOffset(-1, 0)) {
                    this.fallingX = (float) this.restrictTetrominoX((int) (Math.floor((double) this.fallingX) - 1.0));
                }
                if (this.keyPressed(262) && !this.isBlocksInOffset(1, 0)) {
                    this.fallingX = (float) this.restrictTetrominoX((int) (Math.ceil((double) this.fallingX) + 1.0));
                }
                if (this.keyPressed(265) && this.fallingRotation != null && this.fallingShape != TetrominoShape.SQUARE) {
                    this.fallingRotation = this.fallingRotation.getRotated(Rotation.CLOCKWISE_90);
                    this.fallingX = (float) this.restrictTetrominoX((int) Math.floor((double) this.fallingX));
                }
            }
        }
        if (this.flashFor > 0) {
            this.flashFor--;
            if (this.flashFor == 0) {
                for (int j = 0; j < HEIGHT; j++) {
                    if (this.flashingLayer[j]) {
                        for (int k = j; k < HEIGHT; k++) {
                            for (int i = 0; i < 10; i++) {
                                this.settledBlocks[i][k] = k < HEIGHT - 1 ? this.settledBlocks[i][k + 1] : null;
                            }
                        }
                    }
                }
                int cleared = 0;
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.PLAYER_LEVELUP, 1.0F));
                for (int i = 0; i < this.flashingLayer.length; i++) {
                    if (this.flashingLayer[i]) {
                        cleared++;
                    }
                    this.flashingLayer[i] = false;
                }
                if (cleared == 1) {
                    this.score += 40;
                } else if (cleared == 2) {
                    this.score += 100;
                } else if (cleared == 3) {
                    this.score += 300;
                } else if (cleared >= 4) {
                    this.score += 1200 * (cleared - 3);
                }
            }
        }
        if (this.keyPressed(84)) {
            this.started = true;
            this.reset();
        }
    }

    private boolean groundedTetromino() {
        for (Vec3i vec : this.fallingShape.getRelativePositions()) {
            Vec3i vec2 = transform(vec, this.fallingRotation, Vec3i.ZERO);
            int x = Math.round(this.fallingX) + vec2.getX();
            int y = HEIGHT - (int) Math.ceil((double) this.fallingY) - vec2.getY();
            if (y < 0) {
                return true;
            }
            if (x >= 0 && x < 10 && y >= 0 && y < HEIGHT && (y <= 0 || this.settledBlocks[x][y - 1] != null)) {
                return true;
            }
        }
        return false;
    }

    private void groundTetromino() {
        for (Vec3i vec : this.fallingShape.getRelativePositions()) {
            Vec3i vec2 = transform(vec, this.fallingRotation, Vec3i.ZERO);
            int x = Math.round(this.fallingX) + vec2.getX();
            int y = HEIGHT - (int) Math.ceil((double) this.fallingY) - vec2.getY();
            if (x >= 0 && x < 10 && y >= 0 && y < HEIGHT) {
                if (y >= HEIGHT - 1) {
                    this.gameOver = true;
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.PLAYER_DEATH, 1.0F));
                }
                if (this.settledBlocks[x][y] == null) {
                    this.settledBlocks[x][y] = this.fallingBlock;
                }
            }
        }
        boolean flag = false;
        for (int j = 0; j < HEIGHT; j++) {
            for (int i = 0; i < 10 && this.settledBlocks[i][j] != null; i++) {
                if (i == 9) {
                    this.flashingLayer[j] = true;
                    flag = true;
                }
            }
        }
        if (flag) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0F));
            this.flashFor = 20;
        }
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(this.fallingBlock.m_60827_().getPlaceSound(), 1.0F));
    }

    private boolean isBlocksInOffset(int xOffset, int yOffset) {
        for (Vec3i vec : this.fallingShape.getRelativePositions()) {
            Vec3i vec2 = transform(vec, this.fallingRotation, Vec3i.ZERO);
            int x = Math.round(this.fallingX) + vec2.getX() + xOffset;
            int y = HEIGHT - (int) Math.ceil((double) this.fallingY) - vec2.getY() + yOffset;
            if (x >= 0 && x < 10 && y >= 0 && y < HEIGHT && (y <= 0 || this.settledBlocks[x][y] != null)) {
                return true;
            }
        }
        return false;
    }

    public boolean isStarted() {
        return this.started;
    }

    private boolean keyPressed(int keyId) {
        if (this.keyCooldown == 0 && InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), keyId)) {
            this.keyCooldown = 4;
            return true;
        } else {
            return false;
        }
    }

    private void generateNextTetromino() {
        BlockState randomState = Blocks.DIRT.defaultBlockState();
        for (int tries = 0; tries < 5; tries++) {
            if (this.allRegisteredBlocks.length > 1) {
                BlockState block = this.allRegisteredBlocks[this.random.nextInt(this.allRegisteredBlocks.length - 1)].defaultBlockState();
                BakedModel blockModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(block);
                if (!block.m_60713_(Blocks.GLOWSTONE) && !blockModel.isCustomRenderer() && blockModel.getRenderTypes(block, this.random, ModelData.EMPTY).contains(RenderType.solid())) {
                    randomState = block;
                    break;
                }
            }
        }
        this.nextShape = TetrominoShape.getRandom(this.random);
        this.nextBlock = randomState;
    }

    private void generateTetromino() {
        this.fallingShape = this.nextShape;
        this.fallingBlock = this.nextBlock;
        this.fallingRotation = Rotation.getRandom(this.random);
        this.fallingX = (float) this.restrictTetrominoX(this.random.nextInt(10));
        this.prevFallingY = 0.0F;
        this.fallingY = -2.0F;
    }

    private int restrictTetrominoX(int xIn) {
        int minShapeX = 0;
        int maxShapeX = 0;
        for (Vec3i vec : this.fallingShape.getRelativePositions()) {
            Vec3i vec2 = transform(vec, this.fallingRotation, Vec3i.ZERO);
            if (vec2.getX() < minShapeX) {
                minShapeX = vec2.getX();
            }
            if (vec2.getX() > maxShapeX) {
                maxShapeX = vec2.getX();
            }
        }
        if (xIn + minShapeX < 0) {
            xIn = Math.max(xIn - minShapeX, minShapeX);
        }
        if (xIn + maxShapeX > 9) {
            xIn = Math.min(xIn - maxShapeX, 9 - maxShapeX);
        }
        return xIn;
    }

    private void renderTetromino(TetrominoShape shape, BlockState blockState, Rotation fallingRotation, float x, float y, float scale, float offsetX, float offsetY) {
        for (Vec3i vec : shape.getRelativePositions()) {
            Vec3i vec2 = transform(vec, fallingRotation, Vec3i.ZERO);
            this.renderBlockState(blockState, offsetX + (x + (float) vec2.getX()) * scale, offsetY + (y + (float) vec2.getY()) * scale, scale);
        }
    }

    private void renderBlockState(BlockState state, float offsetX, float offsetY, float size) {
        TextureAtlasSprite sprite = Minecraft.getInstance().getBlockRenderer().getBlockModel(state).getParticleIcon(ModelData.EMPTY);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        float f = size * 0.5F;
        bufferbuilder.m_5483_((double) (-f + offsetX), (double) (f + offsetY), 80.0).uv(sprite.getU0(), sprite.getV1()).endVertex();
        bufferbuilder.m_5483_((double) (f + offsetX), (double) (f + offsetY), 80.0).uv(sprite.getU1(), sprite.getV1()).endVertex();
        bufferbuilder.m_5483_((double) (f + offsetX), (double) (-f + offsetY), 80.0).uv(sprite.getU1(), sprite.getV0()).endVertex();
        bufferbuilder.m_5483_((double) (-f + offsetX), (double) (-f + offsetY), 80.0).uv(sprite.getU0(), sprite.getV0()).endVertex();
        tesselator.end();
    }

    public void render(TitleScreen screen, GuiGraphics guiGraphics, float partialTick) {
        float scale = Math.min((float) screen.f_96543_ / 15.0F, (float) screen.f_96544_ / (float) HEIGHT);
        float offsetX = (float) screen.f_96543_ / 2.0F - scale * 5.0F;
        float offsetY = scale * 0.5F;
        if (this.started) {
            guiGraphics.fill(RenderType.guiOverlay(), (int) ((float) screen.f_96543_ * 0.05F), (int) ((float) screen.f_96544_ * 0.3F), (int) ((float) screen.f_96543_ * 0.05F) + 70, (int) ((float) screen.f_96544_ * 0.5F), -1873784752);
            guiGraphics.fill(RenderType.guiOverlay(), (int) ((float) screen.f_96543_ * 0.7F), (int) ((float) screen.f_96544_ * 0.3F), (int) ((float) screen.f_96543_ * 0.7F) + 130, (int) ((float) screen.f_96544_ * 0.84F), -1873784752);
            RenderSystem.setShader(GameRenderer::m_172817_);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
            for (int i = 0; i < this.settledBlocks.length; i++) {
                int max = this.settledBlocks[i].length;
                for (int j = 0; j < max; j++) {
                    BlockState state = this.settledBlocks[i][j];
                    if (this.flashingLayer[j] && this.renderTime % 4 < 2) {
                        state = Blocks.GLOWSTONE.defaultBlockState();
                    }
                    if (state != null) {
                        this.renderBlockState(state, offsetX + (float) i * scale, offsetY + (float) (max - j - 1) * scale, scale);
                    }
                }
            }
            if (this.fallingShape != null) {
                float lerpedFallingY = this.prevFallingY + (this.fallingY - this.prevFallingY) * partialTick;
                this.renderTetromino(this.fallingShape, this.fallingBlock, this.fallingRotation, this.fallingX, lerpedFallingY, scale, offsetX, offsetY);
            }
            if (this.nextShape != null) {
                this.renderTetromino(this.nextShape, this.nextBlock, Rotation.NONE, 0.0F, 0.0F, scale, (float) screen.f_96543_ * 0.85F, (float) screen.f_96544_ * 0.4F);
            }
            float hue = (float) (System.currentTimeMillis() % 6000L) / 6000.0F;
            int rainbow = Color.HSBtoRGB(hue, 0.6F, 1.0F);
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(2.0F, 2.0F, 2.0F);
            int var10003 = (int) ((float) screen.f_96543_ * 0.065F);
            guiGraphics.drawCenteredString(Minecraft.getInstance().font, "SCORE", var10003, (int) ((float) screen.f_96544_ * 0.175F), rainbow);
            guiGraphics.drawCenteredString(Minecraft.getInstance().font, this.score + "", (int) ((float) screen.f_96543_ * 0.065F), (int) ((float) screen.f_96544_ * 0.175F) + 10, rainbow);
            guiGraphics.pose().popPose();
            var10003 = (int) ((float) screen.f_96543_ * 0.71F);
            guiGraphics.drawString(Minecraft.getInstance().font, "[LEFT ARROW] move left", var10003, (int) ((float) screen.f_96544_ * 0.55F), rainbow);
            var10003 = (int) ((float) screen.f_96543_ * 0.71F);
            guiGraphics.drawString(Minecraft.getInstance().font, "[RIGHT ARROW] move right", var10003, (int) ((float) screen.f_96544_ * 0.55F) + 10, rainbow);
            var10003 = (int) ((float) screen.f_96543_ * 0.71F);
            guiGraphics.drawString(Minecraft.getInstance().font, "[UP ARROW] rotate", var10003, (int) ((float) screen.f_96544_ * 0.55F) + 20, rainbow);
            var10003 = (int) ((float) screen.f_96543_ * 0.71F);
            guiGraphics.drawString(Minecraft.getInstance().font, "[DOWN ARROW] quick drop", var10003, (int) ((float) screen.f_96544_ * 0.55F) + 30, rainbow);
            var10003 = (int) ((float) screen.f_96543_ * 0.71F);
            guiGraphics.drawString(Minecraft.getInstance().font, "[T] start over", var10003, (int) ((float) screen.f_96544_ * 0.55F) + 50, rainbow);
            guiGraphics.drawString(Minecraft.getInstance().font, "Happy april fools from Citadel", 5, 5, rainbow);
            if (this.gameOver) {
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate((float) ((int) ((float) screen.f_96543_ * 0.5F)), (float) ((int) ((float) screen.f_96544_ * 0.5F)), 150.0F);
                guiGraphics.pose().scale(3.0F + (float) Math.sin((double) hue * Math.PI) * 0.4F, 3.0F + (float) Math.sin((double) hue * Math.PI) * 0.4F, 3.0F + (float) Math.sin((double) hue * Math.PI) * 0.4F);
                guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees((float) Math.sin((double) hue * Math.PI) * 10.0F));
                guiGraphics.drawCenteredString(Minecraft.getInstance().font, "GAME OVER", 0, 0, rainbow);
                guiGraphics.pose().popPose();
            }
        }
    }

    public void reset() {
        this.score = 0;
        for (int i = 0; i < this.settledBlocks.length; i++) {
            for (int j = 0; j < this.settledBlocks[i].length; j++) {
                this.settledBlocks[i][j] = null;
            }
        }
        this.gameOver = false;
        for (int i = 0; i < this.flashingLayer.length; i++) {
            this.flashingLayer[i] = false;
        }
        this.generateNextTetromino();
        this.generateTetromino();
        this.generateNextTetromino();
    }

    private static Vec3i transform(Vec3i vec3i, Rotation rotation, Vec3i relativeTo) {
        int i = vec3i.getX();
        int k = vec3i.getY();
        int j = vec3i.getZ();
        boolean flag = true;
        int l = relativeTo.getX();
        int i1 = relativeTo.getY();
        switch(rotation) {
            case COUNTERCLOCKWISE_90:
                return new Vec3i(l - i1 + k, l + i1 - i, j);
            case CLOCKWISE_90:
                return new Vec3i(l + i1 - k, i1 - l + i, j);
            case CLOCKWISE_180:
                return new Vec3i(l + l - i, i1 + i1 - k, j);
            default:
                return flag ? new Vec3i(i, k, j) : vec3i;
        }
    }
}