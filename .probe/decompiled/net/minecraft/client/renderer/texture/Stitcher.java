package net.minecraft.client.renderer.texture;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class Stitcher<T extends Stitcher.Entry> {

    private static final Comparator<Stitcher.Holder<?>> HOLDER_COMPARATOR = Comparator.comparing(p_118201_ -> -p_118201_.height).thenComparing(p_118199_ -> -p_118199_.width).thenComparing(p_247945_ -> p_247945_.entry.name());

    private final int mipLevel;

    private final List<Stitcher.Holder<T>> texturesToBeStitched = new ArrayList();

    private final List<Stitcher.Region<T>> storage = new ArrayList();

    private int storageX;

    private int storageY;

    private final int maxWidth;

    private final int maxHeight;

    public Stitcher(int int0, int int1, int int2) {
        this.mipLevel = int2;
        this.maxWidth = int0;
        this.maxHeight = int1;
    }

    public int getWidth() {
        return this.storageX;
    }

    public int getHeight() {
        return this.storageY;
    }

    public void registerSprite(T t0) {
        Stitcher.Holder<T> $$1 = new Stitcher.Holder<>(t0, this.mipLevel);
        this.texturesToBeStitched.add($$1);
    }

    public void stitch() {
        List<Stitcher.Holder<T>> $$0 = new ArrayList(this.texturesToBeStitched);
        $$0.sort(HOLDER_COMPARATOR);
        for (Stitcher.Holder<T> $$1 : $$0) {
            if (!this.addToStorage($$1)) {
                throw new StitcherException($$1.entry, (Collection<Stitcher.Entry>) $$0.stream().map(p_247946_ -> p_247946_.entry).collect(ImmutableList.toImmutableList()));
            }
        }
    }

    public void gatherSprites(Stitcher.SpriteLoader<T> stitcherSpriteLoaderT0) {
        for (Stitcher.Region<T> $$1 : this.storage) {
            $$1.walk(stitcherSpriteLoaderT0);
        }
    }

    static int smallestFittingMinTexel(int int0, int int1) {
        return (int0 >> int1) + ((int0 & (1 << int1) - 1) == 0 ? 0 : 1) << int1;
    }

    private boolean addToStorage(Stitcher.Holder<T> stitcherHolderT0) {
        for (Stitcher.Region<T> $$1 : this.storage) {
            if ($$1.add(stitcherHolderT0)) {
                return true;
            }
        }
        return this.expand(stitcherHolderT0);
    }

    private boolean expand(Stitcher.Holder<T> stitcherHolderT0) {
        int $$1 = Mth.smallestEncompassingPowerOfTwo(this.storageX);
        int $$2 = Mth.smallestEncompassingPowerOfTwo(this.storageY);
        int $$3 = Mth.smallestEncompassingPowerOfTwo(this.storageX + stitcherHolderT0.width);
        int $$4 = Mth.smallestEncompassingPowerOfTwo(this.storageY + stitcherHolderT0.height);
        boolean $$5 = $$3 <= this.maxWidth;
        boolean $$6 = $$4 <= this.maxHeight;
        if (!$$5 && !$$6) {
            return false;
        } else {
            boolean $$7 = $$5 && $$1 != $$3;
            boolean $$8 = $$6 && $$2 != $$4;
            boolean $$9;
            if ($$7 ^ $$8) {
                $$9 = $$7;
            } else {
                $$9 = $$5 && $$1 <= $$2;
            }
            Stitcher.Region<T> $$11;
            if ($$9) {
                if (this.storageY == 0) {
                    this.storageY = $$4;
                }
                $$11 = new Stitcher.Region<>(this.storageX, 0, $$3 - this.storageX, this.storageY);
                this.storageX = $$3;
            } else {
                $$11 = new Stitcher.Region<>(0, this.storageY, this.storageX, $$4 - this.storageY);
                this.storageY = $$4;
            }
            $$11.add(stitcherHolderT0);
            this.storage.add($$11);
            return true;
        }
    }

    public interface Entry {

        int width();

        int height();

        ResourceLocation name();
    }

    static record Holder<T extends Stitcher.Entry>(T f_244486_, int f_118203_, int f_118204_) {

        private final T entry;

        private final int width;

        private final int height;

        public Holder(T p_250261_, int p_250127_) {
            this(p_250261_, Stitcher.smallestFittingMinTexel(p_250261_.width(), p_250127_), Stitcher.smallestFittingMinTexel(p_250261_.height(), p_250127_));
        }

        private Holder(T f_244486_, int f_118203_, int f_118204_) {
            this.entry = f_244486_;
            this.width = f_118203_;
            this.height = f_118204_;
        }
    }

    public static class Region<T extends Stitcher.Entry> {

        private final int originX;

        private final int originY;

        private final int width;

        private final int height;

        @Nullable
        private List<Stitcher.Region<T>> subSlots;

        @Nullable
        private Stitcher.Holder<T> holder;

        public Region(int int0, int int1, int int2, int int3) {
            this.originX = int0;
            this.originY = int1;
            this.width = int2;
            this.height = int3;
        }

        public int getX() {
            return this.originX;
        }

        public int getY() {
            return this.originY;
        }

        public boolean add(Stitcher.Holder<T> stitcherHolderT0) {
            if (this.holder != null) {
                return false;
            } else {
                int $$1 = stitcherHolderT0.width;
                int $$2 = stitcherHolderT0.height;
                if ($$1 <= this.width && $$2 <= this.height) {
                    if ($$1 == this.width && $$2 == this.height) {
                        this.holder = stitcherHolderT0;
                        return true;
                    } else {
                        if (this.subSlots == null) {
                            this.subSlots = new ArrayList(1);
                            this.subSlots.add(new Stitcher.Region(this.originX, this.originY, $$1, $$2));
                            int $$3 = this.width - $$1;
                            int $$4 = this.height - $$2;
                            if ($$4 > 0 && $$3 > 0) {
                                int $$5 = Math.max(this.height, $$3);
                                int $$6 = Math.max(this.width, $$4);
                                if ($$5 >= $$6) {
                                    this.subSlots.add(new Stitcher.Region(this.originX, this.originY + $$2, $$1, $$4));
                                    this.subSlots.add(new Stitcher.Region(this.originX + $$1, this.originY, $$3, this.height));
                                } else {
                                    this.subSlots.add(new Stitcher.Region(this.originX + $$1, this.originY, $$3, $$2));
                                    this.subSlots.add(new Stitcher.Region(this.originX, this.originY + $$2, this.width, $$4));
                                }
                            } else if ($$3 == 0) {
                                this.subSlots.add(new Stitcher.Region(this.originX, this.originY + $$2, $$1, $$4));
                            } else if ($$4 == 0) {
                                this.subSlots.add(new Stitcher.Region(this.originX + $$1, this.originY, $$3, $$2));
                            }
                        }
                        for (Stitcher.Region<T> $$7 : this.subSlots) {
                            if ($$7.add(stitcherHolderT0)) {
                                return true;
                            }
                        }
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }

        public void walk(Stitcher.SpriteLoader<T> stitcherSpriteLoaderT0) {
            if (this.holder != null) {
                stitcherSpriteLoaderT0.load(this.holder.entry, this.getX(), this.getY());
            } else if (this.subSlots != null) {
                for (Stitcher.Region<T> $$1 : this.subSlots) {
                    $$1.walk(stitcherSpriteLoaderT0);
                }
            }
        }

        public String toString() {
            return "Slot{originX=" + this.originX + ", originY=" + this.originY + ", width=" + this.width + ", height=" + this.height + ", texture=" + this.holder + ", subSlots=" + this.subSlots + "}";
        }
    }

    public interface SpriteLoader<T extends Stitcher.Entry> {

        void load(T var1, int var2, int var3);
    }
}