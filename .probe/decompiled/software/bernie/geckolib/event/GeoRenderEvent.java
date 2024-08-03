package software.bernie.geckolib.event;

import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.GeoObjectRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.GeoReplacedEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public interface GeoRenderEvent {

    GeoRenderer<?> getRenderer();

    public abstract static class Armor extends Event implements GeoRenderEvent {

        private final GeoArmorRenderer<?> renderer;

        public Armor(GeoArmorRenderer<?> renderer) {
            this.renderer = renderer;
        }

        public GeoArmorRenderer<?> getRenderer() {
            return this.renderer;
        }

        @Nullable
        public net.minecraft.world.entity.Entity getEntity() {
            return this.getRenderer().getCurrentEntity();
        }

        @Nullable
        public ItemStack getItemStack() {
            return this.getRenderer().getCurrentStack();
        }

        @Nullable
        public EquipmentSlot getEquipmentSlot() {
            return this.getRenderer().getCurrentSlot();
        }

        public static class CompileRenderLayers extends GeoRenderEvent.Armor {

            public CompileRenderLayers(GeoArmorRenderer<?> renderer) {
                super(renderer);
            }

            public void addLayer(GeoRenderLayer renderLayer) {
                this.getRenderer().addRenderLayer(renderLayer);
            }
        }

        public static class Post extends GeoRenderEvent.Armor {

            private final PoseStack poseStack;

            private final BakedGeoModel model;

            private final MultiBufferSource bufferSource;

            private final float partialTick;

            private final int packedLight;

            public Post(GeoArmorRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
                super(renderer);
                this.poseStack = poseStack;
                this.model = model;
                this.bufferSource = bufferSource;
                this.partialTick = partialTick;
                this.packedLight = packedLight;
            }

            public PoseStack getPoseStack() {
                return this.poseStack;
            }

            public BakedGeoModel getModel() {
                return this.model;
            }

            public MultiBufferSource getBufferSource() {
                return this.bufferSource;
            }

            public float getPartialTick() {
                return this.partialTick;
            }

            public int getPackedLight() {
                return this.packedLight;
            }
        }

        @Cancelable
        public static class Pre extends GeoRenderEvent.Armor {

            private final PoseStack poseStack;

            private final BakedGeoModel model;

            private final MultiBufferSource bufferSource;

            private final float partialTick;

            private final int packedLight;

            public Pre(GeoArmorRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
                super(renderer);
                this.poseStack = poseStack;
                this.model = model;
                this.bufferSource = bufferSource;
                this.partialTick = partialTick;
                this.packedLight = packedLight;
            }

            public PoseStack getPoseStack() {
                return this.poseStack;
            }

            public BakedGeoModel getModel() {
                return this.model;
            }

            public MultiBufferSource getBufferSource() {
                return this.bufferSource;
            }

            public float getPartialTick() {
                return this.partialTick;
            }

            public int getPackedLight() {
                return this.packedLight;
            }
        }
    }

    public abstract static class Block extends Event implements GeoRenderEvent {

        private final GeoBlockRenderer<?> renderer;

        public Block(GeoBlockRenderer<?> renderer) {
            this.renderer = renderer;
        }

        public GeoBlockRenderer<?> getRenderer() {
            return this.renderer;
        }

        public BlockEntity getBlockEntity() {
            return this.getRenderer().getAnimatable();
        }

        public static class CompileRenderLayers extends GeoRenderEvent.Block {

            public CompileRenderLayers(GeoBlockRenderer<?> renderer) {
                super(renderer);
            }

            public void addLayer(GeoRenderLayer renderLayer) {
                this.getRenderer().addRenderLayer(renderLayer);
            }
        }

        public static class Post extends GeoRenderEvent.Block {

            private final PoseStack poseStack;

            private final BakedGeoModel model;

            private final MultiBufferSource bufferSource;

            private final float partialTick;

            private final int packedLight;

            public Post(GeoBlockRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
                super(renderer);
                this.poseStack = poseStack;
                this.model = model;
                this.bufferSource = bufferSource;
                this.partialTick = partialTick;
                this.packedLight = packedLight;
            }

            public PoseStack getPoseStack() {
                return this.poseStack;
            }

            public BakedGeoModel getModel() {
                return this.model;
            }

            public MultiBufferSource getBufferSource() {
                return this.bufferSource;
            }

            public float getPartialTick() {
                return this.partialTick;
            }

            public int getPackedLight() {
                return this.packedLight;
            }
        }

        @Cancelable
        public static class Pre extends GeoRenderEvent.Block {

            private final PoseStack poseStack;

            private final BakedGeoModel model;

            private final MultiBufferSource bufferSource;

            private final float partialTick;

            private final int packedLight;

            public Pre(GeoBlockRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
                super(renderer);
                this.poseStack = poseStack;
                this.model = model;
                this.bufferSource = bufferSource;
                this.partialTick = partialTick;
                this.packedLight = packedLight;
            }

            public PoseStack getPoseStack() {
                return this.poseStack;
            }

            public BakedGeoModel getModel() {
                return this.model;
            }

            public MultiBufferSource getBufferSource() {
                return this.bufferSource;
            }

            public float getPartialTick() {
                return this.partialTick;
            }

            public int getPackedLight() {
                return this.packedLight;
            }
        }
    }

    public abstract static class Entity extends Event implements GeoRenderEvent {

        private final GeoEntityRenderer<?> renderer;

        public Entity(GeoEntityRenderer<?> renderer) {
            this.renderer = renderer;
        }

        public GeoEntityRenderer<?> getRenderer() {
            return this.renderer;
        }

        public net.minecraft.world.entity.Entity getEntity() {
            return this.renderer.getAnimatable();
        }

        public static class CompileRenderLayers extends GeoRenderEvent.Entity {

            public CompileRenderLayers(GeoEntityRenderer<?> renderer) {
                super(renderer);
            }

            public void addLayer(GeoRenderLayer renderLayer) {
                this.getRenderer().addRenderLayer(renderLayer);
            }
        }

        public static class Post extends GeoRenderEvent.Entity {

            private final PoseStack poseStack;

            private final BakedGeoModel model;

            private final MultiBufferSource bufferSource;

            private final float partialTick;

            private final int packedLight;

            public Post(GeoEntityRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
                super(renderer);
                this.poseStack = poseStack;
                this.model = model;
                this.bufferSource = bufferSource;
                this.partialTick = partialTick;
                this.packedLight = packedLight;
            }

            public PoseStack getPoseStack() {
                return this.poseStack;
            }

            public BakedGeoModel getModel() {
                return this.model;
            }

            public MultiBufferSource getBufferSource() {
                return this.bufferSource;
            }

            public float getPartialTick() {
                return this.partialTick;
            }

            public int getPackedLight() {
                return this.packedLight;
            }
        }

        @Cancelable
        public static class Pre extends GeoRenderEvent.Entity {

            private final PoseStack poseStack;

            private final BakedGeoModel model;

            private final MultiBufferSource bufferSource;

            private final float partialTick;

            private final int packedLight;

            public Pre(GeoEntityRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
                super(renderer);
                this.poseStack = poseStack;
                this.model = model;
                this.bufferSource = bufferSource;
                this.partialTick = partialTick;
                this.packedLight = packedLight;
            }

            public PoseStack getPoseStack() {
                return this.poseStack;
            }

            public BakedGeoModel getModel() {
                return this.model;
            }

            public MultiBufferSource getBufferSource() {
                return this.bufferSource;
            }

            public float getPartialTick() {
                return this.partialTick;
            }

            public int getPackedLight() {
                return this.packedLight;
            }
        }
    }

    public abstract static class Item extends Event implements GeoRenderEvent {

        private final GeoItemRenderer<?> renderer;

        public Item(GeoItemRenderer<?> renderer) {
            this.renderer = renderer;
        }

        public GeoItemRenderer<?> getRenderer() {
            return this.renderer;
        }

        public ItemStack getItemStack() {
            return this.getRenderer().getCurrentItemStack();
        }

        public static class CompileRenderLayers extends GeoRenderEvent.Item {

            public CompileRenderLayers(GeoItemRenderer<?> renderer) {
                super(renderer);
            }

            public void addLayer(GeoRenderLayer renderLayer) {
                this.getRenderer().addRenderLayer(renderLayer);
            }
        }

        public static class Post extends GeoRenderEvent.Item {

            private final PoseStack poseStack;

            private final BakedGeoModel model;

            private final MultiBufferSource bufferSource;

            private final float partialTick;

            private final int packedLight;

            public Post(GeoItemRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
                super(renderer);
                this.poseStack = poseStack;
                this.model = model;
                this.bufferSource = bufferSource;
                this.partialTick = partialTick;
                this.packedLight = packedLight;
            }

            public PoseStack getPoseStack() {
                return this.poseStack;
            }

            public BakedGeoModel getModel() {
                return this.model;
            }

            public MultiBufferSource getBufferSource() {
                return this.bufferSource;
            }

            public float getPartialTick() {
                return this.partialTick;
            }

            public int getPackedLight() {
                return this.packedLight;
            }
        }

        @Cancelable
        public static class Pre extends GeoRenderEvent.Item {

            private final PoseStack poseStack;

            private final BakedGeoModel model;

            private final MultiBufferSource bufferSource;

            private final float partialTick;

            private final int packedLight;

            public Pre(GeoItemRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
                super(renderer);
                this.poseStack = poseStack;
                this.model = model;
                this.bufferSource = bufferSource;
                this.partialTick = partialTick;
                this.packedLight = packedLight;
            }

            public PoseStack getPoseStack() {
                return this.poseStack;
            }

            public BakedGeoModel getModel() {
                return this.model;
            }

            public MultiBufferSource getBufferSource() {
                return this.bufferSource;
            }

            public float getPartialTick() {
                return this.partialTick;
            }

            public int getPackedLight() {
                return this.packedLight;
            }
        }
    }

    public abstract static class Object extends Event implements GeoRenderEvent {

        private final GeoObjectRenderer<?> renderer;

        public Object(GeoObjectRenderer<?> renderer) {
            this.renderer = renderer;
        }

        public GeoObjectRenderer<?> getRenderer() {
            return this.renderer;
        }

        public static class CompileRenderLayers extends GeoRenderEvent.Object {

            public CompileRenderLayers(GeoObjectRenderer<?> renderer) {
                super(renderer);
            }

            public void addLayer(GeoRenderLayer renderLayer) {
                this.getRenderer().addRenderLayer(renderLayer);
            }
        }

        public static class Post extends GeoRenderEvent.Object {

            private final PoseStack poseStack;

            private final BakedGeoModel model;

            private final MultiBufferSource bufferSource;

            private final float partialTick;

            private final int packedLight;

            public Post(GeoObjectRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
                super(renderer);
                this.poseStack = poseStack;
                this.model = model;
                this.bufferSource = bufferSource;
                this.partialTick = partialTick;
                this.packedLight = packedLight;
            }

            public PoseStack getPoseStack() {
                return this.poseStack;
            }

            public BakedGeoModel getModel() {
                return this.model;
            }

            public MultiBufferSource getBufferSource() {
                return this.bufferSource;
            }

            public float getPartialTick() {
                return this.partialTick;
            }

            public int getPackedLight() {
                return this.packedLight;
            }
        }

        @Cancelable
        public static class Pre extends GeoRenderEvent.Object {

            private final PoseStack poseStack;

            private final BakedGeoModel model;

            private final MultiBufferSource bufferSource;

            private final float partialTick;

            private final int packedLight;

            public Pre(GeoObjectRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
                super(renderer);
                this.poseStack = poseStack;
                this.model = model;
                this.bufferSource = bufferSource;
                this.partialTick = partialTick;
                this.packedLight = packedLight;
            }

            public PoseStack getPoseStack() {
                return this.poseStack;
            }

            public BakedGeoModel getModel() {
                return this.model;
            }

            public MultiBufferSource getBufferSource() {
                return this.bufferSource;
            }

            public float getPartialTick() {
                return this.partialTick;
            }

            public int getPackedLight() {
                return this.packedLight;
            }
        }
    }

    public abstract static class ReplacedEntity extends Event implements GeoRenderEvent {

        private final GeoReplacedEntityRenderer<?, ?> renderer;

        public ReplacedEntity(GeoReplacedEntityRenderer<?, ?> renderer) {
            this.renderer = renderer;
        }

        public GeoReplacedEntityRenderer<?, ?> getRenderer() {
            return this.renderer;
        }

        public net.minecraft.world.entity.Entity getReplacedEntity() {
            return this.getRenderer().getCurrentEntity();
        }

        public static class CompileRenderLayers extends GeoRenderEvent.ReplacedEntity {

            public CompileRenderLayers(GeoReplacedEntityRenderer<?, ?> renderer) {
                super(renderer);
            }

            public void addLayer(GeoRenderLayer renderLayer) {
                this.getRenderer().addRenderLayer(renderLayer);
            }
        }

        public static class Post extends GeoRenderEvent.ReplacedEntity {

            private final PoseStack poseStack;

            private final BakedGeoModel model;

            private final MultiBufferSource bufferSource;

            private final float partialTick;

            private final int packedLight;

            public Post(GeoReplacedEntityRenderer<?, ?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
                super(renderer);
                this.poseStack = poseStack;
                this.model = model;
                this.bufferSource = bufferSource;
                this.partialTick = partialTick;
                this.packedLight = packedLight;
            }

            public PoseStack getPoseStack() {
                return this.poseStack;
            }

            public BakedGeoModel getModel() {
                return this.model;
            }

            public MultiBufferSource getBufferSource() {
                return this.bufferSource;
            }

            public float getPartialTick() {
                return this.partialTick;
            }

            public int getPackedLight() {
                return this.packedLight;
            }
        }

        @Cancelable
        public static class Pre extends GeoRenderEvent.ReplacedEntity {

            private final PoseStack poseStack;

            private final BakedGeoModel model;

            private final MultiBufferSource bufferSource;

            private final float partialTick;

            private final int packedLight;

            public Pre(GeoReplacedEntityRenderer<?, ?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
                super(renderer);
                this.poseStack = poseStack;
                this.model = model;
                this.bufferSource = bufferSource;
                this.partialTick = partialTick;
                this.packedLight = packedLight;
            }

            public PoseStack getPoseStack() {
                return this.poseStack;
            }

            public BakedGeoModel getModel() {
                return this.model;
            }

            public MultiBufferSource getBufferSource() {
                return this.bufferSource;
            }

            public float getPartialTick() {
                return this.partialTick;
            }

            public int getPackedLight() {
                return this.packedLight;
            }
        }
    }
}