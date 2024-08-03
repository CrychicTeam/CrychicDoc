package com.simibubi.create.foundation.ponder;

import com.simibubi.create.content.contraptions.actors.trainControls.ControlsBlock;
import com.simibubi.create.content.contraptions.glue.SuperGlueItem;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.RotationIndicatorParticleData;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.crafter.ConnectedInputHandler;
import com.simibubi.create.content.kinetics.crafter.MechanicalCrafterBlockEntity;
import com.simibubi.create.content.kinetics.gauge.SpeedGaugeBlockEntity;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmBlockEntity;
import com.simibubi.create.content.logistics.funnel.FunnelBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;
import com.simibubi.create.content.redstone.displayLink.DisplayLinkBlockEntity;
import com.simibubi.create.content.trains.display.FlapDisplayBlockEntity;
import com.simibubi.create.content.trains.signal.SignalBlockEntity;
import com.simibubi.create.content.trains.station.StationBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.ponder.element.AnimatedSceneElement;
import com.simibubi.create.foundation.ponder.element.BeltItemElement;
import com.simibubi.create.foundation.ponder.element.EntityElement;
import com.simibubi.create.foundation.ponder.element.InputWindowElement;
import com.simibubi.create.foundation.ponder.element.MinecartElement;
import com.simibubi.create.foundation.ponder.element.ParrotElement;
import com.simibubi.create.foundation.ponder.element.TextWindowElement;
import com.simibubi.create.foundation.ponder.element.WorldSectionElement;
import com.simibubi.create.foundation.ponder.instruction.AnimateBlockEntityInstruction;
import com.simibubi.create.foundation.ponder.instruction.AnimateMinecartInstruction;
import com.simibubi.create.foundation.ponder.instruction.AnimateParrotInstruction;
import com.simibubi.create.foundation.ponder.instruction.AnimateWorldSectionInstruction;
import com.simibubi.create.foundation.ponder.instruction.BlockEntityDataInstruction;
import com.simibubi.create.foundation.ponder.instruction.ChaseAABBInstruction;
import com.simibubi.create.foundation.ponder.instruction.CreateMinecartInstruction;
import com.simibubi.create.foundation.ponder.instruction.CreateParrotInstruction;
import com.simibubi.create.foundation.ponder.instruction.DelayInstruction;
import com.simibubi.create.foundation.ponder.instruction.DisplayWorldSectionInstruction;
import com.simibubi.create.foundation.ponder.instruction.EmitParticlesInstruction;
import com.simibubi.create.foundation.ponder.instruction.FadeOutOfSceneInstruction;
import com.simibubi.create.foundation.ponder.instruction.HighlightValueBoxInstruction;
import com.simibubi.create.foundation.ponder.instruction.KeyframeInstruction;
import com.simibubi.create.foundation.ponder.instruction.LineInstruction;
import com.simibubi.create.foundation.ponder.instruction.MarkAsFinishedInstruction;
import com.simibubi.create.foundation.ponder.instruction.MovePoiInstruction;
import com.simibubi.create.foundation.ponder.instruction.OutlineSelectionInstruction;
import com.simibubi.create.foundation.ponder.instruction.PonderInstruction;
import com.simibubi.create.foundation.ponder.instruction.ReplaceBlocksInstruction;
import com.simibubi.create.foundation.ponder.instruction.RotateSceneInstruction;
import com.simibubi.create.foundation.ponder.instruction.ShowInputInstruction;
import com.simibubi.create.foundation.ponder.instruction.TextInstruction;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class SceneBuilder {

    public final SceneBuilder.OverlayInstructions overlay;

    public final SceneBuilder.WorldInstructions world;

    public final SceneBuilder.DebugInstructions debug;

    public final SceneBuilder.EffectInstructions effects;

    public final SceneBuilder.SpecialInstructions special;

    private final PonderScene scene;

    public SceneBuilder(PonderScene ponderScene) {
        this.scene = ponderScene;
        this.overlay = new SceneBuilder.OverlayInstructions();
        this.special = new SceneBuilder.SpecialInstructions();
        this.world = new SceneBuilder.WorldInstructions();
        this.debug = new SceneBuilder.DebugInstructions();
        this.effects = new SceneBuilder.EffectInstructions();
    }

    public void title(String sceneId, String title) {
        this.scene.sceneId = new ResourceLocation(this.scene.getNamespace(), sceneId);
        PonderLocalization.registerSpecific(this.scene.sceneId, "header", title);
    }

    public void configureBasePlate(int xOffset, int zOffset, int basePlateSize) {
        this.scene.basePlateOffsetX = xOffset;
        this.scene.basePlateOffsetZ = zOffset;
        this.scene.basePlateSize = basePlateSize;
    }

    public void scaleSceneView(float factor) {
        this.scene.scaleFactor = factor;
    }

    public void removeShadow() {
        this.scene.hidePlatformShadow = true;
    }

    public void setSceneOffsetY(float yOffset) {
        this.scene.yOffset = yOffset;
    }

    public void showBasePlate() {
        this.world.showSection(this.scene.getSceneBuildingUtil().select.cuboid(new BlockPos(this.scene.getBasePlateOffsetX(), 0, this.scene.getBasePlateOffsetZ()), new Vec3i(this.scene.getBasePlateSize() - 1, 0, this.scene.getBasePlateSize() - 1)), Direction.UP);
    }

    public void addInstruction(PonderInstruction instruction) {
        this.scene.schedule.add(instruction);
    }

    public void addInstruction(Consumer<PonderScene> callback) {
        this.addInstruction(PonderInstruction.simple(callback));
    }

    public void idle(int ticks) {
        this.addInstruction(new DelayInstruction(ticks));
    }

    public void idleSeconds(int seconds) {
        this.idle(seconds * 20);
    }

    public void markAsFinished() {
        this.addInstruction(new MarkAsFinishedInstruction());
    }

    public void rotateCameraY(float degrees) {
        this.addInstruction(new RotateSceneInstruction(0.0F, degrees, true));
    }

    public void addKeyframe() {
        this.addInstruction(KeyframeInstruction.IMMEDIATE);
    }

    public void addLazyKeyframe() {
        this.addInstruction(KeyframeInstruction.DELAYED);
    }

    public class DebugInstructions {

        public void debugSchematic() {
            SceneBuilder.this.addInstruction(scene -> scene.addElement(new WorldSectionElement(scene.getSceneBuildingUtil().select.everywhere())));
        }

        public void addInstructionInstance(PonderInstruction instruction) {
            SceneBuilder.this.addInstruction(instruction);
        }

        public void enqueueCallback(Consumer<PonderScene> callback) {
            SceneBuilder.this.addInstruction(callback);
        }
    }

    public class EffectInstructions {

        public void emitParticles(Vec3 location, EmitParticlesInstruction.Emitter emitter, float amountPerCycle, int cycles) {
            SceneBuilder.this.addInstruction(new EmitParticlesInstruction(location, emitter, amountPerCycle, cycles));
        }

        public void superGlue(BlockPos pos, Direction side, boolean fullBlock) {
            SceneBuilder.this.addInstruction(scene -> SuperGlueItem.spawnParticles(scene.getWorld(), pos, side, fullBlock));
        }

        private void rotationIndicator(BlockPos pos, boolean direction, BlockPos displayPos) {
            SceneBuilder.this.addInstruction(scene -> {
                BlockState blockState = scene.getWorld().getBlockState(pos);
                BlockEntity blockEntity = scene.getWorld().m_7702_(pos);
                if (blockState.m_60734_() instanceof KineticBlock) {
                    if (blockEntity instanceof KineticBlockEntity kbe) {
                        KineticBlock kb = (KineticBlock) blockState.m_60734_();
                        Direction.Axis rotationAxis = kb.getRotationAxis(blockState);
                        float speed = kbe.getTheoreticalSpeed();
                        IRotate.SpeedLevel speedLevel = IRotate.SpeedLevel.of(speed);
                        int color = direction ? (speed > 0.0F ? 15425035 : 1476519) : speedLevel.getColor();
                        int particleSpeed = speedLevel.getParticleSpeed();
                        particleSpeed = (int) ((float) particleSpeed * Math.signum(speed));
                        Vec3 location = VecHelper.getCenterOf(displayPos);
                        RotationIndicatorParticleData particleData = new RotationIndicatorParticleData(color, (float) particleSpeed, kb.getParticleInitialRadius(), kb.getParticleTargetRadius(), 20, rotationAxis.name().charAt(0));
                        for (int i = 0; i < 20; i++) {
                            scene.getWorld().addParticle(particleData, location.x, location.y, location.z, 0.0, 0.0, 0.0);
                        }
                    }
                }
            });
        }

        public void rotationSpeedIndicator(BlockPos pos) {
            this.rotationIndicator(pos, false, pos);
        }

        public void rotationDirectionIndicator(BlockPos pos) {
            this.rotationIndicator(pos, true, pos);
        }

        public void rotationSpeedIndicator(BlockPos pos, BlockPos displayPos) {
            this.rotationIndicator(pos, false, displayPos);
        }

        public void rotationDirectionIndicator(BlockPos pos, BlockPos displayPos) {
            this.rotationIndicator(pos, true, displayPos);
        }

        public void indicateRedstone(BlockPos pos) {
            this.createRedstoneParticles(pos, 16711680, 10);
        }

        public void indicateSuccess(BlockPos pos) {
            this.createRedstoneParticles(pos, 8454058, 10);
        }

        public void createRedstoneParticles(BlockPos pos, int color, int amount) {
            Vector3f rgb = new Color(color).asVectorF();
            SceneBuilder.this.addInstruction(new EmitParticlesInstruction(VecHelper.getCenterOf(pos), EmitParticlesInstruction.Emitter.withinBlockSpace(new DustParticleOptions(rgb, 1.0F), Vec3.ZERO), (float) amount, 2));
        }
    }

    public class OverlayInstructions {

        public TextWindowElement.Builder showText(int duration) {
            TextWindowElement textWindowElement = new TextWindowElement();
            SceneBuilder.this.addInstruction(new TextInstruction(textWindowElement, duration));
            return textWindowElement.new Builder(SceneBuilder.this.scene);
        }

        public TextWindowElement.Builder showSelectionWithText(Selection selection, int duration) {
            TextWindowElement textWindowElement = new TextWindowElement();
            SceneBuilder.this.addInstruction(new TextInstruction(textWindowElement, duration, selection));
            return textWindowElement.new Builder(SceneBuilder.this.scene).pointAt(selection.getCenter());
        }

        public void showControls(InputWindowElement element, int duration) {
            SceneBuilder.this.addInstruction(new ShowInputInstruction(element.clone(), duration));
        }

        public void chaseBoundingBoxOutline(PonderPalette color, Object slot, AABB boundingBox, int duration) {
            SceneBuilder.this.addInstruction(new ChaseAABBInstruction(color, slot, boundingBox, duration));
        }

        public void showCenteredScrollInput(BlockPos pos, Direction side, int duration) {
            this.showFilterSlotInput(SceneBuilder.this.scene.getSceneBuildingUtil().vector.blockSurface(pos, side), side, duration);
        }

        public void showScrollInput(Vec3 location, Direction side, int duration) {
            Direction.Axis axis = side.getAxis();
            float s = 0.0625F;
            float q = 0.25F;
            Vec3 expands = new Vec3(axis == Direction.Axis.X ? (double) s : (double) q, axis == Direction.Axis.Y ? (double) s : (double) q, axis == Direction.Axis.Z ? (double) s : (double) q);
            SceneBuilder.this.addInstruction(new HighlightValueBoxInstruction(location, expands, duration));
        }

        public void showRepeaterScrollInput(BlockPos pos, int duration) {
            this.showFilterSlotInput(SceneBuilder.this.scene.getSceneBuildingUtil().vector.blockSurface(pos, Direction.DOWN).add(0.0, 0.1875, 0.0), Direction.UP, duration);
        }

        public void showFilterSlotInput(Vec3 location, Direction side, int duration) {
            location = location.add(Vec3.atLowerCornerOf(side.getNormal()).scale(-0.0234375));
            Vec3 expands = VecHelper.axisAlingedPlaneOf(side).scale(0.0859375);
            SceneBuilder.this.addInstruction(new HighlightValueBoxInstruction(location, expands, duration));
        }

        public void showLine(PonderPalette color, Vec3 start, Vec3 end, int duration) {
            SceneBuilder.this.addInstruction(new LineInstruction(color, start, end, duration, false));
        }

        public void showBigLine(PonderPalette color, Vec3 start, Vec3 end, int duration) {
            SceneBuilder.this.addInstruction(new LineInstruction(color, start, end, duration, true));
        }

        public void showOutline(PonderPalette color, Object slot, Selection selection, int duration) {
            SceneBuilder.this.addInstruction(new OutlineSelectionInstruction(color, slot, selection, duration));
        }
    }

    public class SpecialInstructions {

        public ElementLink<ParrotElement> birbOnTurntable(BlockPos pos) {
            return this.createBirb(VecHelper.getCenterOf(pos), () -> new ParrotElement.SpinOnComponentPose(pos));
        }

        public ElementLink<ParrotElement> birbOnSpinnyShaft(BlockPos pos) {
            return this.createBirb(VecHelper.getCenterOf(pos).add(0.0, 0.5, 0.0), () -> new ParrotElement.SpinOnComponentPose(pos));
        }

        public ElementLink<ParrotElement> createBirb(Vec3 location, Supplier<? extends ParrotElement.ParrotPose> pose) {
            ElementLink<ParrotElement> link = new ElementLink<>(ParrotElement.class);
            ParrotElement parrot = ParrotElement.create(location, pose);
            SceneBuilder.this.addInstruction(new CreateParrotInstruction(10, Direction.DOWN, parrot));
            SceneBuilder.this.addInstruction(scene -> scene.linkElement(parrot, link));
            return link;
        }

        public void changeBirbPose(ElementLink<ParrotElement> birb, Supplier<? extends ParrotElement.ParrotPose> pose) {
            SceneBuilder.this.addInstruction(scene -> scene.resolve(birb).setPose((ParrotElement.ParrotPose) pose.get()));
        }

        public void conductorBirb(ElementLink<ParrotElement> birb, boolean conductor) {
            SceneBuilder.this.addInstruction(scene -> scene.resolve(birb).setConductor(conductor));
        }

        public void movePointOfInterest(Vec3 location) {
            SceneBuilder.this.addInstruction(new MovePoiInstruction(location));
        }

        public void movePointOfInterest(BlockPos location) {
            this.movePointOfInterest(VecHelper.getCenterOf(location));
        }

        public void rotateParrot(ElementLink<ParrotElement> link, double xRotation, double yRotation, double zRotation, int duration) {
            SceneBuilder.this.addInstruction(AnimateParrotInstruction.rotate(link, new Vec3(xRotation, yRotation, zRotation), duration));
        }

        public void moveParrot(ElementLink<ParrotElement> link, Vec3 offset, int duration) {
            SceneBuilder.this.addInstruction(AnimateParrotInstruction.move(link, offset, duration));
        }

        public ElementLink<MinecartElement> createCart(Vec3 location, float angle, MinecartElement.MinecartConstructor type) {
            ElementLink<MinecartElement> link = new ElementLink<>(MinecartElement.class);
            MinecartElement cart = new MinecartElement(location, angle, type);
            SceneBuilder.this.addInstruction(new CreateMinecartInstruction(10, Direction.DOWN, cart));
            SceneBuilder.this.addInstruction(scene -> scene.linkElement(cart, link));
            return link;
        }

        public void rotateCart(ElementLink<MinecartElement> link, float yRotation, int duration) {
            SceneBuilder.this.addInstruction(AnimateMinecartInstruction.rotate(link, yRotation, duration));
        }

        public void moveCart(ElementLink<MinecartElement> link, Vec3 offset, int duration) {
            SceneBuilder.this.addInstruction(AnimateMinecartInstruction.move(link, offset, duration));
        }

        public <T extends AnimatedSceneElement> void hideElement(ElementLink<T> link, Direction direction) {
            SceneBuilder.this.addInstruction(new FadeOutOfSceneInstruction<>(15, direction, link));
        }
    }

    public class WorldInstructions {

        public void incrementBlockBreakingProgress(BlockPos pos) {
            SceneBuilder.this.addInstruction(scene -> {
                PonderWorld world = scene.getWorld();
                int progress = (Integer) world.getBlockBreakingProgressions().getOrDefault(pos, -1) + 1;
                if (progress == 9) {
                    world.addBlockDestroyEffects(pos, world.getBlockState(pos));
                    world.m_46961_(pos, false);
                    world.setBlockBreakingProgress(pos, 0);
                    scene.forEach(WorldSectionElement.class, WorldSectionElement::queueRedraw);
                } else {
                    world.setBlockBreakingProgress(pos, progress + 1);
                }
            });
        }

        public void showSection(Selection selection, Direction fadeInDirection) {
            SceneBuilder.this.addInstruction(new DisplayWorldSectionInstruction(15, fadeInDirection, selection, Optional.of(SceneBuilder.this.scene::getBaseWorldSection)));
        }

        public void showSectionAndMerge(Selection selection, Direction fadeInDirection, ElementLink<WorldSectionElement> link) {
            SceneBuilder.this.addInstruction(new DisplayWorldSectionInstruction(15, fadeInDirection, selection, Optional.of((Supplier) () -> SceneBuilder.this.scene.resolve(link))));
        }

        public void glueBlockOnto(BlockPos position, Direction fadeInDirection, ElementLink<WorldSectionElement> link) {
            SceneBuilder.this.addInstruction(new DisplayWorldSectionInstruction(15, fadeInDirection, SceneBuilder.this.scene.getSceneBuildingUtil().select.position(position), Optional.of((Supplier) () -> SceneBuilder.this.scene.resolve(link)), position));
        }

        public ElementLink<WorldSectionElement> showIndependentSection(Selection selection, Direction fadeInDirection) {
            DisplayWorldSectionInstruction instruction = new DisplayWorldSectionInstruction(15, fadeInDirection, selection, Optional.empty());
            SceneBuilder.this.addInstruction(instruction);
            return instruction.createLink(SceneBuilder.this.scene);
        }

        public ElementLink<WorldSectionElement> showIndependentSection(Selection selection, Direction fadeInDirection, int fadeInDuration) {
            DisplayWorldSectionInstruction instruction = new DisplayWorldSectionInstruction(fadeInDuration, fadeInDirection, selection, Optional.empty());
            SceneBuilder.this.addInstruction(instruction);
            return instruction.createLink(SceneBuilder.this.scene);
        }

        public ElementLink<WorldSectionElement> showIndependentSectionImmediately(Selection selection) {
            DisplayWorldSectionInstruction instruction = new DisplayWorldSectionInstruction(0, Direction.DOWN, selection, Optional.empty());
            SceneBuilder.this.addInstruction(instruction);
            return instruction.createLink(SceneBuilder.this.scene);
        }

        public void hideSection(Selection selection, Direction fadeOutDirection) {
            WorldSectionElement worldSectionElement = new WorldSectionElement(selection);
            ElementLink<WorldSectionElement> elementLink = new ElementLink<>(WorldSectionElement.class);
            SceneBuilder.this.addInstruction(scene -> {
                scene.getBaseWorldSection().erase(selection);
                scene.linkElement(worldSectionElement, elementLink);
                scene.addElement(worldSectionElement);
                worldSectionElement.queueRedraw();
            });
            this.hideIndependentSection(elementLink, fadeOutDirection);
        }

        public void hideIndependentSection(ElementLink<WorldSectionElement> link, Direction fadeOutDirection) {
            SceneBuilder.this.addInstruction(new FadeOutOfSceneInstruction<>(15, fadeOutDirection, link));
        }

        public void hideIndependentSection(ElementLink<WorldSectionElement> link, Direction fadeOutDirection, int fadeOutDuration) {
            SceneBuilder.this.addInstruction(new FadeOutOfSceneInstruction<>(fadeOutDuration, fadeOutDirection, link));
        }

        public void hideIndependentSectionImmediately(ElementLink<WorldSectionElement> link) {
            SceneBuilder.this.addInstruction(new FadeOutOfSceneInstruction<>(0, Direction.DOWN, link));
        }

        public void restoreBlocks(Selection selection) {
            SceneBuilder.this.addInstruction(scene -> scene.getWorld().restoreBlocks(selection));
        }

        public ElementLink<WorldSectionElement> makeSectionIndependent(Selection selection) {
            WorldSectionElement worldSectionElement = new WorldSectionElement(selection);
            ElementLink<WorldSectionElement> elementLink = new ElementLink<>(WorldSectionElement.class);
            SceneBuilder.this.addInstruction(scene -> {
                scene.getBaseWorldSection().erase(selection);
                scene.linkElement(worldSectionElement, elementLink);
                scene.addElement(worldSectionElement);
                worldSectionElement.queueRedraw();
                worldSectionElement.resetAnimatedTransform();
                worldSectionElement.setVisible(true);
                worldSectionElement.forceApplyFade(1.0F);
            });
            return elementLink;
        }

        public void rotateSection(ElementLink<WorldSectionElement> link, double xRotation, double yRotation, double zRotation, int duration) {
            SceneBuilder.this.addInstruction(AnimateWorldSectionInstruction.rotate(link, new Vec3(xRotation, yRotation, zRotation), duration));
        }

        public void configureCenterOfRotation(ElementLink<WorldSectionElement> link, Vec3 anchor) {
            SceneBuilder.this.addInstruction(scene -> scene.resolve(link).setCenterOfRotation(anchor));
        }

        public void configureStabilization(ElementLink<WorldSectionElement> link, Vec3 anchor) {
            SceneBuilder.this.addInstruction(scene -> scene.resolve(link).stabilizeRotation(anchor));
        }

        public void moveSection(ElementLink<WorldSectionElement> link, Vec3 offset, int duration) {
            SceneBuilder.this.addInstruction(AnimateWorldSectionInstruction.move(link, offset, duration));
        }

        public void rotateBearing(BlockPos pos, float angle, int duration) {
            SceneBuilder.this.addInstruction(AnimateBlockEntityInstruction.bearing(pos, angle, duration));
        }

        public void movePulley(BlockPos pos, float distance, int duration) {
            SceneBuilder.this.addInstruction(AnimateBlockEntityInstruction.pulley(pos, distance, duration));
        }

        public void animateBogey(BlockPos pos, float distance, int duration) {
            SceneBuilder.this.addInstruction(AnimateBlockEntityInstruction.bogey(pos, distance, duration + 1));
        }

        public void moveDeployer(BlockPos pos, float distance, int duration) {
            SceneBuilder.this.addInstruction(AnimateBlockEntityInstruction.deployer(pos, distance, duration));
        }

        public void setBlocks(Selection selection, BlockState state, boolean spawnParticles) {
            SceneBuilder.this.addInstruction(new ReplaceBlocksInstruction(selection, $ -> state, true, spawnParticles));
        }

        public void destroyBlock(BlockPos pos) {
            this.setBlock(pos, Blocks.AIR.defaultBlockState(), true);
        }

        public void setBlock(BlockPos pos, BlockState state, boolean spawnParticles) {
            this.setBlocks(SceneBuilder.this.scene.getSceneBuildingUtil().select.position(pos), state, spawnParticles);
        }

        public void replaceBlocks(Selection selection, BlockState state, boolean spawnParticles) {
            this.modifyBlocks(selection, $ -> state, spawnParticles);
        }

        public void modifyBlock(BlockPos pos, UnaryOperator<BlockState> stateFunc, boolean spawnParticles) {
            this.modifyBlocks(SceneBuilder.this.scene.getSceneBuildingUtil().select.position(pos), stateFunc, spawnParticles);
        }

        public void cycleBlockProperty(BlockPos pos, Property<?> property) {
            this.modifyBlocks(SceneBuilder.this.scene.getSceneBuildingUtil().select.position(pos), s -> s.m_61138_(property) ? (BlockState) s.m_61122_(property) : s, false);
        }

        public void modifyBlocks(Selection selection, UnaryOperator<BlockState> stateFunc, boolean spawnParticles) {
            SceneBuilder.this.addInstruction(new ReplaceBlocksInstruction(selection, stateFunc, false, spawnParticles));
        }

        public void toggleRedstonePower(Selection selection) {
            this.modifyBlocks(selection, s -> {
                if (s.m_61138_(BlockStateProperties.POWER)) {
                    s = (BlockState) s.m_61124_(BlockStateProperties.POWER, s.m_61143_(BlockStateProperties.POWER) == 0 ? 15 : 0);
                }
                if (s.m_61138_(BlockStateProperties.POWERED)) {
                    s = (BlockState) s.m_61122_(BlockStateProperties.POWERED);
                }
                if (s.m_61138_(RedstoneTorchBlock.LIT)) {
                    s = (BlockState) s.m_61122_(RedstoneTorchBlock.LIT);
                }
                return s;
            }, false);
        }

        public <T extends Entity> void modifyEntities(Class<T> entityClass, Consumer<T> entityCallBack) {
            SceneBuilder.this.addInstruction(scene -> scene.forEachWorldEntity(entityClass, entityCallBack));
        }

        public <T extends Entity> void modifyEntitiesInside(Class<T> entityClass, Selection area, Consumer<T> entityCallBack) {
            SceneBuilder.this.addInstruction(scene -> scene.forEachWorldEntity(entityClass, e -> {
                if (area.test(e.blockPosition())) {
                    entityCallBack.accept(e);
                }
            }));
        }

        public void modifyEntity(ElementLink<EntityElement> link, Consumer<Entity> entityCallBack) {
            SceneBuilder.this.addInstruction(scene -> {
                EntityElement resolve = scene.resolve(link);
                if (resolve != null) {
                    resolve.ifPresent(entityCallBack::accept);
                }
            });
        }

        public ElementLink<EntityElement> createEntity(Function<Level, Entity> factory) {
            ElementLink<EntityElement> link = new ElementLink<>(EntityElement.class, UUID.randomUUID());
            SceneBuilder.this.addInstruction(scene -> {
                PonderWorld world = scene.getWorld();
                Entity entity = (Entity) factory.apply(world);
                EntityElement handle = new EntityElement(entity);
                scene.addElement(handle);
                scene.linkElement(handle, link);
                world.m_7967_(entity);
            });
            return link;
        }

        public ElementLink<EntityElement> createItemEntity(Vec3 location, Vec3 motion, ItemStack stack) {
            return this.createEntity(world -> {
                ItemEntity itemEntity = new ItemEntity(world, location.x, location.y, location.z, stack);
                itemEntity.m_20256_(motion);
                return itemEntity;
            });
        }

        public void createItemOnBeltLike(BlockPos location, Direction insertionSide, ItemStack stack) {
            SceneBuilder.this.addInstruction(scene -> {
                PonderWorld world = scene.getWorld();
                if (world.m_7702_(location) instanceof SmartBlockEntity beltBlockEntity) {
                    DirectBeltInputBehaviour behaviour = beltBlockEntity.getBehaviour(DirectBeltInputBehaviour.TYPE);
                    if (behaviour != null) {
                        behaviour.handleInsertion(stack, insertionSide.getOpposite(), false);
                    }
                }
            });
            this.flapFunnel(location.above(), true);
        }

        public ElementLink<BeltItemElement> createItemOnBelt(BlockPos beltLocation, Direction insertionSide, ItemStack stack) {
            ElementLink<BeltItemElement> link = new ElementLink<>(BeltItemElement.class);
            SceneBuilder.this.addInstruction(scene -> {
                PonderWorld world = scene.getWorld();
                if (world.m_7702_(beltLocation) instanceof BeltBlockEntity beltBlockEntity) {
                    DirectBeltInputBehaviour behaviour = beltBlockEntity.getBehaviour(DirectBeltInputBehaviour.TYPE);
                    behaviour.handleInsertion(stack, insertionSide.getOpposite(), false);
                    BeltBlockEntity controllerBE = beltBlockEntity.getControllerBE();
                    if (controllerBE != null) {
                        controllerBE.tick();
                    }
                    TransportedItemStackHandlerBehaviour transporter = beltBlockEntity.getBehaviour(TransportedItemStackHandlerBehaviour.TYPE);
                    transporter.handleProcessingOnAllItems(tis -> {
                        BeltItemElement tracker = new BeltItemElement(tis);
                        scene.addElement(tracker);
                        scene.linkElement(tracker, link);
                        return TransportedItemStackHandlerBehaviour.TransportedResult.doNothing();
                    });
                }
            });
            this.flapFunnel(beltLocation.above(), true);
            return link;
        }

        public void removeItemsFromBelt(BlockPos beltLocation) {
            SceneBuilder.this.addInstruction(scene -> {
                PonderWorld world = scene.getWorld();
                if (world.m_7702_(beltLocation) instanceof SmartBlockEntity beltBlockEntity) {
                    TransportedItemStackHandlerBehaviour transporter = beltBlockEntity.getBehaviour(TransportedItemStackHandlerBehaviour.TYPE);
                    if (transporter != null) {
                        transporter.handleCenteredProcessingOnAllItems(0.52F, tis -> TransportedItemStackHandlerBehaviour.TransportedResult.removeItem());
                    }
                }
            });
        }

        public void stallBeltItem(ElementLink<BeltItemElement> link, boolean stalled) {
            SceneBuilder.this.addInstruction(scene -> {
                BeltItemElement resolve = scene.resolve(link);
                if (resolve != null) {
                    resolve.ifPresent(tis -> tis.locked = stalled);
                }
            });
        }

        public void changeBeltItemTo(ElementLink<BeltItemElement> link, ItemStack newStack) {
            SceneBuilder.this.addInstruction(scene -> {
                BeltItemElement resolve = scene.resolve(link);
                if (resolve != null) {
                    resolve.ifPresent(tis -> tis.stack = newStack);
                }
            });
        }

        public void setKineticSpeed(Selection selection, float speed) {
            this.modifyKineticSpeed(selection, f -> speed);
        }

        public void multiplyKineticSpeed(Selection selection, float modifier) {
            this.modifyKineticSpeed(selection, f -> f * modifier);
        }

        public void modifyKineticSpeed(Selection selection, UnaryOperator<Float> speedFunc) {
            this.modifyBlockEntityNBT(selection, SpeedGaugeBlockEntity.class, nbt -> {
                float newSpeed = (Float) speedFunc.apply(nbt.getFloat("Speed"));
                nbt.putFloat("Value", SpeedGaugeBlockEntity.getDialTarget(newSpeed));
            });
            this.modifyBlockEntityNBT(selection, KineticBlockEntity.class, nbt -> nbt.putFloat("Speed", (Float) speedFunc.apply(nbt.getFloat("Speed"))));
        }

        public void propagatePipeChange(BlockPos pos) {
            this.modifyBlockEntity(pos, PumpBlockEntity.class, be -> be.onSpeedChanged(0.0F));
        }

        public void setFilterData(Selection selection, Class<? extends BlockEntity> teType, ItemStack filter) {
            this.modifyBlockEntityNBT(selection, teType, nbt -> nbt.put("Filter", filter.serializeNBT()));
        }

        public void modifyBlockEntityNBT(Selection selection, Class<? extends BlockEntity> beType, Consumer<CompoundTag> consumer) {
            this.modifyBlockEntityNBT(selection, beType, consumer, false);
        }

        public <T extends BlockEntity> void modifyBlockEntity(BlockPos position, Class<T> beType, Consumer<T> consumer) {
            SceneBuilder.this.addInstruction(scene -> {
                BlockEntity blockEntity = scene.getWorld().m_7702_(position);
                if (beType.isInstance(blockEntity)) {
                    consumer.accept((BlockEntity) beType.cast(blockEntity));
                }
            });
        }

        public void modifyBlockEntityNBT(Selection selection, Class<? extends BlockEntity> teType, Consumer<CompoundTag> consumer, boolean reDrawBlocks) {
            SceneBuilder.this.addInstruction(new BlockEntityDataInstruction(selection, teType, nbt -> {
                consumer.accept(nbt);
                return nbt;
            }, reDrawBlocks));
        }

        public void instructArm(BlockPos armLocation, ArmBlockEntity.Phase phase, ItemStack heldItem, int targetedPoint) {
            this.modifyBlockEntityNBT(SceneBuilder.this.scene.getSceneBuildingUtil().select.position(armLocation), ArmBlockEntity.class, compound -> {
                NBTHelper.writeEnum(compound, "Phase", phase);
                compound.put("HeldItem", heldItem.serializeNBT());
                compound.putInt("TargetPointIndex", targetedPoint);
                compound.putFloat("MovementProgress", 0.0F);
            });
        }

        public void flapFunnel(BlockPos position, boolean outward) {
            this.modifyBlockEntity(position, FunnelBlockEntity.class, funnel -> funnel.flap(!outward));
        }

        public void setCraftingResult(BlockPos crafter, ItemStack output) {
            this.modifyBlockEntity(crafter, MechanicalCrafterBlockEntity.class, mct -> mct.setScriptedResult(output));
        }

        public void connectCrafterInvs(BlockPos position1, BlockPos position2) {
            SceneBuilder.this.addInstruction(s -> {
                ConnectedInputHandler.toggleConnection(s.getWorld(), position1, position2);
                s.forEach(WorldSectionElement.class, WorldSectionElement::queueRedraw);
            });
        }

        public void toggleControls(BlockPos position) {
            this.cycleBlockProperty(position, ControlsBlock.VIRTUAL);
        }

        public void animateTrainStation(BlockPos position, boolean trainPresent) {
            this.modifyBlockEntityNBT(SceneBuilder.this.scene.getSceneBuildingUtil().select.position(position), StationBlockEntity.class, c -> c.putBoolean("ForceFlag", trainPresent));
        }

        public void conductorBlaze(BlockPos position, boolean conductor) {
            this.modifyBlockEntityNBT(SceneBuilder.this.scene.getSceneBuildingUtil().select.position(position), BlazeBurnerBlockEntity.class, c -> c.putBoolean("TrainHat", conductor));
        }

        public void changeSignalState(BlockPos position, SignalBlockEntity.SignalState state) {
            this.modifyBlockEntityNBT(SceneBuilder.this.scene.getSceneBuildingUtil().select.position(position), SignalBlockEntity.class, c -> NBTHelper.writeEnum(c, "State", state));
        }

        public void setDisplayBoardText(BlockPos position, int line, Component text) {
            this.modifyBlockEntity(position, FlapDisplayBlockEntity.class, t -> t.applyTextManually(line, Component.Serializer.toJson(text)));
        }

        public void dyeDisplayBoard(BlockPos position, int line, DyeColor color) {
            this.modifyBlockEntity(position, FlapDisplayBlockEntity.class, t -> t.setColour(line, color));
        }

        public void flashDisplayLink(BlockPos position) {
            this.modifyBlockEntity(position, DisplayLinkBlockEntity.class, linkBlockEntity -> linkBlockEntity.glow.setValue(2.0));
        }
    }
}