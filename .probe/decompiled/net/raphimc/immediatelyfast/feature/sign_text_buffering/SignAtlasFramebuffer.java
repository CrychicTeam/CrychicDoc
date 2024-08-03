package net.raphimc.immediatelyfast.feature.sign_text_buffering;

import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.lwjgl.opengl.GL11C;

public class SignAtlasFramebuffer extends RenderTarget implements AutoCloseable {

    public static final int ATLAS_SIZE = 4096;

    private final ResourceLocation textureId;

    private final SignAtlasFramebuffer.Slot rootSlot;

    public SignAtlasFramebuffer() {
        super(false);
        this.m_83941_(4096, 4096, Minecraft.ON_OSX);
        Minecraft.getInstance().getMainRenderTarget().bindWrite(true);
        this.textureId = new ResourceLocation("immediatelyfast", "sign_atlas/" + this.f_83923_);
        Minecraft.getInstance().getTextureManager().register(this.textureId, new SignAtlasFramebuffer.FboTexture());
        this.rootSlot = new SignAtlasFramebuffer.Slot(null, 0, 0, 4096, 4096);
    }

    public void close() {
        this.m_83930_();
        Minecraft.getInstance().getMainRenderTarget().bindWrite(true);
    }

    public SignAtlasFramebuffer.Slot findSlot(int width, int height) {
        return this.rootSlot.findSlot(width, height);
    }

    public void clear() {
        this.m_83954_(Minecraft.ON_OSX);
        Minecraft.getInstance().getMainRenderTarget().bindWrite(true);
        this.rootSlot.subSlot1 = null;
        this.rootSlot.subSlot2 = null;
    }

    public ResourceLocation getTextureId() {
        return this.textureId;
    }

    private class FboTexture extends AbstractTexture {

        @Override
        public void load(ResourceManager manager) {
        }

        @Override
        public void releaseId() {
        }

        @Override
        public int getId() {
            return SignAtlasFramebuffer.this.f_83923_;
        }
    }

    public class Slot {

        public final int x;

        public final int y;

        public final int width;

        public final int height;

        public final SignAtlasFramebuffer.Slot parentSlot;

        public SignAtlasFramebuffer.Slot subSlot1;

        public SignAtlasFramebuffer.Slot subSlot2;

        public boolean occupied;

        public Slot(SignAtlasFramebuffer.Slot parentSlot, int x, int y, int width, int height) {
            this.parentSlot = parentSlot;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public void markFree() {
            if (this.subSlot1 != null || this.subSlot2 != null) {
                throw new UnsupportedOperationException("Cannot mark slot as free if it has sub slots");
            } else if (!this.occupied) {
                throw new UnsupportedOperationException("Cannot mark slot as free if it is not occupied");
            } else {
                this.occupied = false;
                removeUnoccupiedSubSlots(this);
                GL11C.glScissor(this.x, 4096 - this.y - this.height, this.width, this.height);
                GL11C.glEnable(3089);
                SignAtlasFramebuffer.this.m_83954_(Minecraft.ON_OSX);
                GL11C.glDisable(3089);
                Minecraft.getInstance().getMainRenderTarget().bindWrite(true);
            }
        }

        public SignAtlasFramebuffer.Slot findSlot(int width, int height) {
            if (this.subSlot1 != null && this.subSlot2 != null) {
                SignAtlasFramebuffer.Slot slot = this.subSlot1.findSlot(width, height);
                if (slot == null) {
                    slot = this.subSlot2.findSlot(width, height);
                }
                return slot;
            } else if (this.occupied) {
                return null;
            } else if (width > this.width || height > this.height) {
                return null;
            } else if (width == this.width && height == this.height) {
                this.occupied = true;
                return this;
            } else {
                int k = this.width - width;
                int l = this.height - height;
                if (k > l) {
                    this.subSlot1 = SignAtlasFramebuffer.this.new Slot(this, this.x, this.y, width, this.height);
                    this.subSlot2 = SignAtlasFramebuffer.this.new Slot(this, this.x + width, this.y, this.width - width, this.height);
                } else {
                    this.subSlot1 = SignAtlasFramebuffer.this.new Slot(this, this.x, this.y, this.width, height);
                    this.subSlot2 = SignAtlasFramebuffer.this.new Slot(this, this.x, this.y + height, this.width, this.height - height);
                }
                return this.subSlot1.findSlot(width, height);
            }
        }

        private static void removeUnoccupiedSubSlots(SignAtlasFramebuffer.Slot slot) {
            if (slot != null) {
                removeUnoccupiedSubSlots(slot.parentSlot);
                boolean subSlot1Unoccupied = slot.subSlot1 != null && !hasOccupiedSlot(slot.subSlot1);
                boolean subSlot2Unoccupied = slot.subSlot2 != null && !hasOccupiedSlot(slot.subSlot2);
                if (subSlot1Unoccupied && subSlot2Unoccupied) {
                    slot.subSlot1 = null;
                    slot.subSlot2 = null;
                }
            }
        }

        private static boolean hasOccupiedSlot(SignAtlasFramebuffer.Slot slot) {
            if (slot == null) {
                return false;
            } else {
                return slot.occupied ? true : hasOccupiedSlot(slot.subSlot1) || hasOccupiedSlot(slot.subSlot2);
            }
        }
    }
}