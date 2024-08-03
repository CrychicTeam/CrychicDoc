package com.simibubi.create.foundation.ponder;

import com.jozufozu.flywheel.util.DiffuseLightCalculator;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.UIRenderHelper;
import com.simibubi.create.foundation.outliner.Outliner;
import com.simibubi.create.foundation.ponder.element.PonderElement;
import com.simibubi.create.foundation.ponder.element.PonderOverlayElement;
import com.simibubi.create.foundation.ponder.element.PonderSceneElement;
import com.simibubi.create.foundation.ponder.element.WorldSectionElement;
import com.simibubi.create.foundation.ponder.instruction.HideAllInstruction;
import com.simibubi.create.foundation.ponder.instruction.PonderInstruction;
import com.simibubi.create.foundation.ponder.ui.PonderUI;
import com.simibubi.create.foundation.render.ForcedDiffuseState;
import com.simibubi.create.foundation.render.SuperRenderTypeBuffer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.Pair;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import com.simibubi.create.infrastructure.ponder.PonderIndex;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.apache.commons.lang3.mutable.MutableObject;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class PonderScene {

    public static final String TITLE_KEY = "header";

    private boolean finished;

    private int textIndex;

    ResourceLocation sceneId;

    private IntList keyframeTimes;

    List<PonderInstruction> schedule;

    private List<PonderInstruction> activeSchedule;

    private Map<UUID, PonderElement> linkedElements;

    private Set<PonderElement> elements;

    private List<PonderTag> tags;

    private PonderWorld world;

    private String namespace;

    private ResourceLocation component;

    private PonderScene.SceneTransform transform;

    private PonderScene.SceneCamera camera;

    private Outliner outliner;

    private Vec3 pointOfInterest;

    private Vec3 chasingPointOfInterest;

    private WorldSectionElement baseWorldSection;

    @Nullable
    private Entity renderViewEntity;

    int basePlateOffsetX;

    int basePlateOffsetZ;

    int basePlateSize;

    float scaleFactor;

    float yOffset;

    boolean hidePlatformShadow;

    private boolean stoppedCounting;

    private int totalTime;

    private int currentTime;

    public PonderScene(PonderWorld world, String namespace, ResourceLocation component, Collection<PonderTag> tags) {
        if (world != null) {
            world.scene = this;
        }
        this.pointOfInterest = Vec3.ZERO;
        this.textIndex = 1;
        this.hidePlatformShadow = false;
        this.world = world;
        this.namespace = namespace;
        this.component = component;
        this.outliner = new Outliner();
        this.elements = new HashSet();
        this.linkedElements = new HashMap();
        this.tags = new ArrayList(tags);
        this.schedule = new ArrayList();
        this.activeSchedule = new ArrayList();
        this.transform = new PonderScene.SceneTransform();
        this.basePlateSize = this.getBounds().getXSpan();
        this.camera = new PonderScene.SceneCamera();
        this.baseWorldSection = new WorldSectionElement();
        this.renderViewEntity = world != null ? new ArmorStand(world, 0.0, 0.0, 0.0) : null;
        this.keyframeTimes = new IntArrayList(4);
        this.scaleFactor = 1.0F;
        this.yOffset = 0.0F;
        this.setPointOfInterest(new Vec3(0.0, 4.0, 0.0));
    }

    public void deselect() {
        this.forEach(WorldSectionElement.class, WorldSectionElement::resetSelectedBlock);
    }

    public Pair<ItemStack, BlockPos> rayTraceScene(Vec3 from, Vec3 to) {
        MutableObject<Pair<WorldSectionElement, Pair<Vec3, BlockHitResult>>> nearestHit = new MutableObject();
        MutableDouble bestDistance = new MutableDouble(0.0);
        this.forEach(WorldSectionElement.class, wse -> {
            wse.resetSelectedBlock();
            if (wse.isVisible()) {
                Pair<Vec3, BlockHitResult> rayTrace = wse.rayTrace(this.world, from, to);
                if (rayTrace != null) {
                    double distanceTo = rayTrace.getFirst().distanceTo(from);
                    if (nearestHit.getValue() == null || !(distanceTo >= bestDistance.getValue())) {
                        nearestHit.setValue(Pair.of(wse, rayTrace));
                        bestDistance.setValue(distanceTo);
                    }
                }
            }
        });
        if (nearestHit.getValue() == null) {
            return Pair.of(ItemStack.EMPTY, null);
        } else {
            Pair<Vec3, BlockHitResult> selectedHit = (Pair<Vec3, BlockHitResult>) ((Pair) nearestHit.getValue()).getSecond();
            BlockPos selectedPos = selectedHit.getSecond().getBlockPos();
            BlockPos origin = new BlockPos(this.basePlateOffsetX, 0, this.basePlateOffsetZ);
            if (!this.world.getBounds().isInside(selectedPos)) {
                return Pair.of(ItemStack.EMPTY, null);
            } else if (BoundingBox.fromCorners(origin, origin.offset(new Vec3i(this.basePlateSize - 1, 0, this.basePlateSize - 1))).isInside(selectedPos)) {
                if (PonderIndex.editingModeActive()) {
                    ((WorldSectionElement) ((Pair) nearestHit.getValue()).getFirst()).selectBlock(selectedPos);
                }
                return Pair.of(ItemStack.EMPTY, selectedPos);
            } else {
                ((WorldSectionElement) ((Pair) nearestHit.getValue()).getFirst()).selectBlock(selectedPos);
                BlockState blockState = this.world.getBlockState(selectedPos);
                Direction direction = selectedHit.getSecond().getDirection();
                Vec3 location = selectedHit.getSecond().m_82450_();
                ItemStack pickBlock = blockState.getCloneItemStack(new BlockHitResult(location, direction, selectedPos, true), this.world, selectedPos, Minecraft.getInstance().player);
                return Pair.of(pickBlock, selectedPos);
            }
        }
    }

    public void reset() {
        this.currentTime = 0;
        this.activeSchedule.clear();
        this.schedule.forEach(mdi -> mdi.reset(this));
    }

    public void begin() {
        this.reset();
        this.forEach(pe -> pe.reset(this));
        this.world.restore();
        this.elements.clear();
        this.linkedElements.clear();
        this.keyframeTimes.clear();
        this.transform = new PonderScene.SceneTransform();
        this.finished = false;
        this.setPointOfInterest(new Vec3(0.0, 4.0, 0.0));
        this.baseWorldSection.setEmpty();
        this.baseWorldSection.forceApplyFade(1.0F);
        this.elements.add(this.baseWorldSection);
        this.totalTime = 0;
        this.stoppedCounting = false;
        this.activeSchedule.addAll(this.schedule);
        this.activeSchedule.forEach(i -> i.onScheduled(this));
    }

    public WorldSectionElement getBaseWorldSection() {
        return this.baseWorldSection;
    }

    public float getSceneProgress() {
        return this.totalTime == 0 ? 0.0F : (float) this.currentTime / (float) this.totalTime;
    }

    public void fadeOut() {
        this.reset();
        this.activeSchedule.add(new HideAllInstruction(10, null));
    }

    public void renderScene(SuperRenderTypeBuffer buffer, PoseStack ms, float pt) {
        ForcedDiffuseState.pushCalculator(DiffuseLightCalculator.DEFAULT);
        ms.pushPose();
        Minecraft mc = Minecraft.getInstance();
        Entity prevRVE = mc.cameraEntity;
        mc.cameraEntity = this.renderViewEntity;
        this.forEachVisible(PonderSceneElement.class, e -> e.renderFirst(this.world, buffer, ms, pt));
        mc.cameraEntity = prevRVE;
        for (RenderType type : RenderType.chunkBufferLayers()) {
            this.forEachVisible(PonderSceneElement.class, e -> e.renderLayer(this.world, buffer, type, ms, pt));
        }
        this.forEachVisible(PonderSceneElement.class, e -> e.renderLast(this.world, buffer, ms, pt));
        this.camera.set(this.transform.xRotation.getValue(pt) + 90.0F, this.transform.yRotation.getValue(pt) + 180.0F);
        this.world.renderEntities(ms, buffer, this.camera, pt);
        this.world.renderParticles(ms, buffer, this.camera, pt);
        this.outliner.renderOutlines(ms, buffer, Vec3.ZERO, pt);
        ms.popPose();
        ForcedDiffuseState.popCalculator();
    }

    public void renderOverlay(PonderUI screen, GuiGraphics graphics, float partialTicks) {
        PoseStack ms = graphics.pose();
        ms.pushPose();
        this.forEachVisible(PonderOverlayElement.class, e -> e.render(this, screen, graphics, partialTicks));
        ms.popPose();
    }

    public void setPointOfInterest(Vec3 poi) {
        if (this.chasingPointOfInterest == null) {
            this.pointOfInterest = poi;
        }
        this.chasingPointOfInterest = poi;
    }

    public Vec3 getPointOfInterest() {
        return this.pointOfInterest;
    }

    public void tick() {
        if (this.chasingPointOfInterest != null) {
            this.pointOfInterest = VecHelper.lerp(0.25F, this.pointOfInterest, this.chasingPointOfInterest);
        }
        this.outliner.tickOutlines();
        this.world.tick();
        this.transform.tick();
        this.forEach(e -> e.tick(this));
        if (this.currentTime < this.totalTime) {
            this.currentTime++;
        }
        Iterator<PonderInstruction> iterator = this.activeSchedule.iterator();
        while (iterator.hasNext()) {
            PonderInstruction instruction = (PonderInstruction) iterator.next();
            instruction.tick(this);
            if (instruction.isComplete()) {
                iterator.remove();
                if (instruction.isBlocking()) {
                    break;
                }
            } else if (instruction.isBlocking()) {
                break;
            }
        }
        if (this.activeSchedule.isEmpty()) {
            this.finished = true;
        }
    }

    public void seekToTime(int time) {
        if (time < this.currentTime) {
            throw new IllegalStateException("Cannot seek backwards. Rewind first.");
        } else {
            while (this.currentTime < time && !this.finished) {
                this.forEach(e -> e.whileSkipping(this));
                this.tick();
            }
            this.forEach(WorldSectionElement.class, WorldSectionElement::queueRedraw);
        }
    }

    public void addToSceneTime(int time) {
        if (!this.stoppedCounting) {
            this.totalTime += time;
        }
    }

    public void stopCounting() {
        this.stoppedCounting = true;
    }

    public void markKeyframe(int offset) {
        if (!this.stoppedCounting) {
            this.keyframeTimes.add(this.totalTime + offset);
        }
    }

    public void addElement(PonderElement e) {
        this.elements.add(e);
    }

    public <E extends PonderElement> void linkElement(E e, ElementLink<E> link) {
        this.linkedElements.put(link.getId(), e);
    }

    public <E extends PonderElement> E resolve(ElementLink<E> link) {
        return link.cast((PonderElement) this.linkedElements.get(link.getId()));
    }

    public <E extends PonderElement> void runWith(ElementLink<E> link, Consumer<E> callback) {
        callback.accept(this.resolve(link));
    }

    public <E extends PonderElement, F> F applyTo(ElementLink<E> link, Function<E, F> function) {
        return (F) function.apply(this.resolve(link));
    }

    public void forEach(Consumer<? super PonderElement> function) {
        for (PonderElement elemtent : this.elements) {
            function.accept(elemtent);
        }
    }

    public <T extends PonderElement> void forEach(Class<T> type, Consumer<T> function) {
        for (PonderElement element : this.elements) {
            if (type.isInstance(element)) {
                function.accept((PonderElement) type.cast(element));
            }
        }
    }

    public <T extends PonderElement> void forEachVisible(Class<T> type, Consumer<T> function) {
        for (PonderElement element : this.elements) {
            if (type.isInstance(element) && element.isVisible()) {
                function.accept((PonderElement) type.cast(element));
            }
        }
    }

    public <T extends Entity> void forEachWorldEntity(Class<T> type, Consumer<T> function) {
        this.world.getEntityStream().filter(type::isInstance).map(type::cast).forEach(function);
    }

    public Supplier<String> registerText(String defaultText) {
        String key = "text_" + this.textIndex;
        PonderLocalization.registerSpecific(this.sceneId, key, defaultText);
        Supplier<String> supplier = () -> PonderLocalization.getSpecific(this.sceneId, key);
        this.textIndex++;
        return supplier;
    }

    public SceneBuilder builder() {
        return new SceneBuilder(this);
    }

    public SceneBuildingUtil getSceneBuildingUtil() {
        return new SceneBuildingUtil(this.getBounds());
    }

    public String getTitle() {
        return this.getString("header");
    }

    public String getString(String key) {
        return PonderLocalization.getSpecific(this.sceneId, key);
    }

    public PonderWorld getWorld() {
        return this.world;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public int getKeyframeCount() {
        return this.keyframeTimes.size();
    }

    public int getKeyframeTime(int index) {
        return this.keyframeTimes.getInt(index);
    }

    public List<PonderTag> getTags() {
        return this.tags;
    }

    public ResourceLocation getComponent() {
        return this.component;
    }

    public Set<PonderElement> getElements() {
        return this.elements;
    }

    public BoundingBox getBounds() {
        return this.world == null ? new BoundingBox(BlockPos.ZERO) : this.world.getBounds();
    }

    public ResourceLocation getId() {
        return this.sceneId;
    }

    public PonderScene.SceneTransform getTransform() {
        return this.transform;
    }

    public Outliner getOutliner() {
        return this.outliner;
    }

    public boolean isFinished() {
        return this.finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public int getBasePlateOffsetX() {
        return this.basePlateOffsetX;
    }

    public int getBasePlateOffsetZ() {
        return this.basePlateOffsetZ;
    }

    public boolean shouldHidePlatformShadow() {
        return this.hidePlatformShadow;
    }

    public int getBasePlateSize() {
        return this.basePlateSize;
    }

    public float getScaleFactor() {
        return this.scaleFactor;
    }

    public float getYOffset() {
        return this.yOffset;
    }

    public int getTotalTime() {
        return this.totalTime;
    }

    public int getCurrentTime() {
        return this.currentTime;
    }

    public class SceneCamera extends Camera {

        public void set(float xRotation, float yRotation) {
            this.m_90572_(yRotation, xRotation);
        }
    }

    public class SceneTransform {

        public LerpedFloat xRotation = LerpedFloat.angular().disableSmartAngleChasing().startWithValue(-35.0);

        public LerpedFloat yRotation = LerpedFloat.angular().disableSmartAngleChasing().startWithValue(145.0);

        private int width;

        private int height;

        private double offset;

        private Matrix4f cachedMat;

        public void tick() {
            this.xRotation.tickChaser();
            this.yRotation.tickChaser();
        }

        public void updateScreenParams(int width, int height, double offset) {
            this.width = width;
            this.height = height;
            this.offset = offset;
            this.cachedMat = null;
        }

        public PoseStack apply(PoseStack ms) {
            return this.apply(ms, AnimationTickHolder.getPartialTicks(PonderScene.this.world));
        }

        public PoseStack apply(PoseStack ms, float pt) {
            ms.translate((double) (this.width / 2), (double) (this.height / 2), 200.0 + this.offset);
            ((TransformStack) ((TransformStack) ((TransformStack) ((TransformStack) ((TransformStack) ((TransformStack) TransformStack.cast(ms).rotateX(-35.0)).rotateY(55.0)).translate(this.offset, 0.0, 0.0)).rotateY(-55.0)).rotateX(35.0)).rotateX((double) this.xRotation.getValue(pt))).rotateY((double) this.yRotation.getValue(pt));
            UIRenderHelper.flipForGuiRender(ms);
            float f = 30.0F * PonderScene.this.scaleFactor;
            ms.scale(f, f, f);
            ms.translate((float) PonderScene.this.basePlateSize / -2.0F - (float) PonderScene.this.basePlateOffsetX, -1.0F + PonderScene.this.yOffset, (float) PonderScene.this.basePlateSize / -2.0F - (float) PonderScene.this.basePlateOffsetZ);
            return ms;
        }

        public void updateSceneRVE(float pt) {
            Vec3 v = this.screenToScene((double) (this.width / 2), (double) (this.height / 2), 500, pt);
            if (PonderScene.this.renderViewEntity != null) {
                PonderScene.this.renderViewEntity.setPos(v.x, v.y, v.z);
            }
        }

        public Vec3 screenToScene(double x, double y, int depth, float pt) {
            this.refreshMatrix(pt);
            Vec3 vec = new Vec3(x, y, (double) depth);
            vec = vec.subtract((double) (this.width / 2), (double) (this.height / 2), 200.0 + this.offset);
            vec = VecHelper.rotate(vec, 35.0, Direction.Axis.X);
            vec = VecHelper.rotate(vec, -55.0, Direction.Axis.Y);
            vec = vec.subtract(this.offset, 0.0, 0.0);
            vec = VecHelper.rotate(vec, 55.0, Direction.Axis.Y);
            vec = VecHelper.rotate(vec, -35.0, Direction.Axis.X);
            vec = VecHelper.rotate(vec, (double) (-this.xRotation.getValue(pt)), Direction.Axis.X);
            vec = VecHelper.rotate(vec, (double) (-this.yRotation.getValue(pt)), Direction.Axis.Y);
            float f = 1.0F / (30.0F * PonderScene.this.scaleFactor);
            vec = vec.multiply((double) f, (double) (-f), (double) f);
            return vec.subtract((double) ((float) PonderScene.this.basePlateSize / -2.0F - (float) PonderScene.this.basePlateOffsetX), (double) (-1.0F + PonderScene.this.yOffset), (double) ((float) PonderScene.this.basePlateSize / -2.0F - (float) PonderScene.this.basePlateOffsetZ));
        }

        public Vec2 sceneToScreen(Vec3 vec, float pt) {
            this.refreshMatrix(pt);
            Vector4f vec4 = new Vector4f((float) vec.x, (float) vec.y, (float) vec.z, 1.0F);
            vec4.mul(this.cachedMat);
            return new Vec2(vec4.x(), vec4.y());
        }

        protected void refreshMatrix(float pt) {
            if (this.cachedMat == null) {
                this.cachedMat = this.apply(new PoseStack(), pt).last().pose();
            }
        }
    }
}