package net.minecraft.client.renderer.block.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Either;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import org.joml.Vector3f;

public class ItemModelGenerator {

    public static final List<String> LAYERS = Lists.newArrayList(new String[] { "layer0", "layer1", "layer2", "layer3", "layer4" });

    private static final float MIN_Z = 7.5F;

    private static final float MAX_Z = 8.5F;

    public BlockModel generateBlockModel(Function<Material, TextureAtlasSprite> functionMaterialTextureAtlasSprite0, BlockModel blockModel1) {
        Map<String, Either<Material, String>> $$2 = Maps.newHashMap();
        List<BlockElement> $$3 = Lists.newArrayList();
        for (int $$4 = 0; $$4 < LAYERS.size(); $$4++) {
            String $$5 = (String) LAYERS.get($$4);
            if (!blockModel1.hasTexture($$5)) {
                break;
            }
            Material $$6 = blockModel1.getMaterial($$5);
            $$2.put($$5, Either.left($$6));
            SpriteContents $$7 = ((TextureAtlasSprite) functionMaterialTextureAtlasSprite0.apply($$6)).contents();
            $$3.addAll(this.processFrames($$4, $$5, $$7));
        }
        $$2.put("particle", blockModel1.hasTexture("particle") ? Either.left(blockModel1.getMaterial("particle")) : (Either) $$2.get("layer0"));
        BlockModel $$8 = new BlockModel(null, $$3, $$2, false, blockModel1.getGuiLight(), blockModel1.getTransforms(), blockModel1.getOverrides());
        $$8.name = blockModel1.name;
        return $$8;
    }

    private List<BlockElement> processFrames(int int0, String string1, SpriteContents spriteContents2) {
        Map<Direction, BlockElementFace> $$3 = Maps.newHashMap();
        $$3.put(Direction.SOUTH, new BlockElementFace(null, int0, string1, new BlockFaceUV(new float[] { 0.0F, 0.0F, 16.0F, 16.0F }, 0)));
        $$3.put(Direction.NORTH, new BlockElementFace(null, int0, string1, new BlockFaceUV(new float[] { 16.0F, 0.0F, 0.0F, 16.0F }, 0)));
        List<BlockElement> $$4 = Lists.newArrayList();
        $$4.add(new BlockElement(new Vector3f(0.0F, 0.0F, 7.5F), new Vector3f(16.0F, 16.0F, 8.5F), $$3, null, true));
        $$4.addAll(this.createSideElements(spriteContents2, string1, int0));
        return $$4;
    }

    private List<BlockElement> createSideElements(SpriteContents spriteContents0, String string1, int int2) {
        float $$3 = (float) spriteContents0.width();
        float $$4 = (float) spriteContents0.height();
        List<BlockElement> $$5 = Lists.newArrayList();
        for (ItemModelGenerator.Span $$6 : this.getSpans(spriteContents0)) {
            float $$7 = 0.0F;
            float $$8 = 0.0F;
            float $$9 = 0.0F;
            float $$10 = 0.0F;
            float $$11 = 0.0F;
            float $$12 = 0.0F;
            float $$13 = 0.0F;
            float $$14 = 0.0F;
            float $$15 = 16.0F / $$3;
            float $$16 = 16.0F / $$4;
            float $$17 = (float) $$6.getMin();
            float $$18 = (float) $$6.getMax();
            float $$19 = (float) $$6.getAnchor();
            ItemModelGenerator.SpanFacing $$20 = $$6.getFacing();
            switch($$20) {
                case UP:
                    $$11 = $$17;
                    $$7 = $$17;
                    $$9 = $$12 = $$18 + 1.0F;
                    $$13 = $$19;
                    $$8 = $$19;
                    $$10 = $$19;
                    $$14 = $$19 + 1.0F;
                    break;
                case DOWN:
                    $$13 = $$19;
                    $$14 = $$19 + 1.0F;
                    $$11 = $$17;
                    $$7 = $$17;
                    $$9 = $$12 = $$18 + 1.0F;
                    $$8 = $$19 + 1.0F;
                    $$10 = $$19 + 1.0F;
                    break;
                case LEFT:
                    $$11 = $$19;
                    $$7 = $$19;
                    $$9 = $$19;
                    $$12 = $$19 + 1.0F;
                    $$14 = $$17;
                    $$8 = $$17;
                    $$10 = $$13 = $$18 + 1.0F;
                    break;
                case RIGHT:
                    $$11 = $$19;
                    $$12 = $$19 + 1.0F;
                    $$7 = $$19 + 1.0F;
                    $$9 = $$19 + 1.0F;
                    $$14 = $$17;
                    $$8 = $$17;
                    $$10 = $$13 = $$18 + 1.0F;
            }
            $$7 *= $$15;
            $$9 *= $$15;
            $$8 *= $$16;
            $$10 *= $$16;
            $$8 = 16.0F - $$8;
            $$10 = 16.0F - $$10;
            $$11 *= $$15;
            $$12 *= $$15;
            $$13 *= $$16;
            $$14 *= $$16;
            Map<Direction, BlockElementFace> $$21 = Maps.newHashMap();
            $$21.put($$20.getDirection(), new BlockElementFace(null, int2, string1, new BlockFaceUV(new float[] { $$11, $$13, $$12, $$14 }, 0)));
            switch($$20) {
                case UP:
                    $$5.add(new BlockElement(new Vector3f($$7, $$8, 7.5F), new Vector3f($$9, $$8, 8.5F), $$21, null, true));
                    break;
                case DOWN:
                    $$5.add(new BlockElement(new Vector3f($$7, $$10, 7.5F), new Vector3f($$9, $$10, 8.5F), $$21, null, true));
                    break;
                case LEFT:
                    $$5.add(new BlockElement(new Vector3f($$7, $$8, 7.5F), new Vector3f($$7, $$10, 8.5F), $$21, null, true));
                    break;
                case RIGHT:
                    $$5.add(new BlockElement(new Vector3f($$9, $$8, 7.5F), new Vector3f($$9, $$10, 8.5F), $$21, null, true));
            }
        }
        return $$5;
    }

    private List<ItemModelGenerator.Span> getSpans(SpriteContents spriteContents0) {
        int $$1 = spriteContents0.width();
        int $$2 = spriteContents0.height();
        List<ItemModelGenerator.Span> $$3 = Lists.newArrayList();
        spriteContents0.getUniqueFrames().forEach(p_173444_ -> {
            for (int $$5 = 0; $$5 < $$2; $$5++) {
                for (int $$6 = 0; $$6 < $$1; $$6++) {
                    boolean $$7 = !this.isTransparent(spriteContents0, p_173444_, $$6, $$5, $$1, $$2);
                    this.checkTransition(ItemModelGenerator.SpanFacing.UP, $$3, spriteContents0, p_173444_, $$6, $$5, $$1, $$2, $$7);
                    this.checkTransition(ItemModelGenerator.SpanFacing.DOWN, $$3, spriteContents0, p_173444_, $$6, $$5, $$1, $$2, $$7);
                    this.checkTransition(ItemModelGenerator.SpanFacing.LEFT, $$3, spriteContents0, p_173444_, $$6, $$5, $$1, $$2, $$7);
                    this.checkTransition(ItemModelGenerator.SpanFacing.RIGHT, $$3, spriteContents0, p_173444_, $$6, $$5, $$1, $$2, $$7);
                }
            }
        });
        return $$3;
    }

    private void checkTransition(ItemModelGenerator.SpanFacing itemModelGeneratorSpanFacing0, List<ItemModelGenerator.Span> listItemModelGeneratorSpan1, SpriteContents spriteContents2, int int3, int int4, int int5, int int6, int int7, boolean boolean8) {
        boolean $$9 = this.isTransparent(spriteContents2, int3, int4 + itemModelGeneratorSpanFacing0.getXOffset(), int5 + itemModelGeneratorSpanFacing0.getYOffset(), int6, int7) && boolean8;
        if ($$9) {
            this.createOrExpandSpan(listItemModelGeneratorSpan1, itemModelGeneratorSpanFacing0, int4, int5);
        }
    }

    private void createOrExpandSpan(List<ItemModelGenerator.Span> listItemModelGeneratorSpan0, ItemModelGenerator.SpanFacing itemModelGeneratorSpanFacing1, int int2, int int3) {
        ItemModelGenerator.Span $$4 = null;
        for (ItemModelGenerator.Span $$5 : listItemModelGeneratorSpan0) {
            if ($$5.getFacing() == itemModelGeneratorSpanFacing1) {
                int $$6 = itemModelGeneratorSpanFacing1.isHorizontal() ? int3 : int2;
                if ($$5.getAnchor() == $$6) {
                    $$4 = $$5;
                    break;
                }
            }
        }
        int $$7 = itemModelGeneratorSpanFacing1.isHorizontal() ? int3 : int2;
        int $$8 = itemModelGeneratorSpanFacing1.isHorizontal() ? int2 : int3;
        if ($$4 == null) {
            listItemModelGeneratorSpan0.add(new ItemModelGenerator.Span(itemModelGeneratorSpanFacing1, $$8, $$7));
        } else {
            $$4.expand($$8);
        }
    }

    private boolean isTransparent(SpriteContents spriteContents0, int int1, int int2, int int3, int int4, int int5) {
        return int2 >= 0 && int3 >= 0 && int2 < int4 && int3 < int5 ? spriteContents0.isTransparent(int1, int2, int3) : true;
    }

    static class Span {

        private final ItemModelGenerator.SpanFacing facing;

        private int min;

        private int max;

        private final int anchor;

        public Span(ItemModelGenerator.SpanFacing itemModelGeneratorSpanFacing0, int int1, int int2) {
            this.facing = itemModelGeneratorSpanFacing0;
            this.min = int1;
            this.max = int1;
            this.anchor = int2;
        }

        public void expand(int int0) {
            if (int0 < this.min) {
                this.min = int0;
            } else if (int0 > this.max) {
                this.max = int0;
            }
        }

        public ItemModelGenerator.SpanFacing getFacing() {
            return this.facing;
        }

        public int getMin() {
            return this.min;
        }

        public int getMax() {
            return this.max;
        }

        public int getAnchor() {
            return this.anchor;
        }
    }

    static enum SpanFacing {

        UP(Direction.UP, 0, -1), DOWN(Direction.DOWN, 0, 1), LEFT(Direction.EAST, -1, 0), RIGHT(Direction.WEST, 1, 0);

        private final Direction direction;

        private final int xOffset;

        private final int yOffset;

        private SpanFacing(Direction p_111701_, int p_111702_, int p_111703_) {
            this.direction = p_111701_;
            this.xOffset = p_111702_;
            this.yOffset = p_111703_;
        }

        public Direction getDirection() {
            return this.direction;
        }

        public int getXOffset() {
            return this.xOffset;
        }

        public int getYOffset() {
            return this.yOffset;
        }

        boolean isHorizontal() {
            return this == DOWN || this == UP;
        }
    }
}