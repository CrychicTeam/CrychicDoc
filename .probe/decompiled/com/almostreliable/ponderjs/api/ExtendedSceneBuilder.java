package com.almostreliable.ponderjs.api;

import com.almostreliable.ponderjs.mixin.SceneBuilderAccessor;
import com.almostreliable.ponderjs.particles.ParticleInstructions;
import com.almostreliable.ponderjs.util.BlockStateFunction;
import com.almostreliable.ponderjs.util.PonderPlatform;
import com.google.common.base.Preconditions;
import com.simibubi.create.foundation.ponder.ElementLink;
import com.simibubi.create.foundation.ponder.PonderPalette;
import com.simibubi.create.foundation.ponder.PonderScene;
import com.simibubi.create.foundation.ponder.PonderWorld;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.Selection;
import com.simibubi.create.foundation.ponder.element.EntityElement;
import com.simibubi.create.foundation.ponder.element.InputWindowElement;
import com.simibubi.create.foundation.ponder.element.TextWindowElement;
import com.simibubi.create.foundation.ponder.instruction.ShowInputInstruction;
import com.simibubi.create.foundation.utility.Pointing;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ExtendedSceneBuilder extends SceneBuilder {

    private final ParticleInstructions particles;

    private final PonderScene ponderScene;

    public ExtendedSceneBuilder(PonderScene ponderScene) {
        super(ponderScene);
        this.ponderScene = ponderScene;
        ((SceneBuilderAccessor) this).ponderjs$setWorldInstructions(new ExtendedSceneBuilder.ExtendedWorldInstructions());
        ((SceneBuilderAccessor) this).ponderjs$setSpecialInstructions(new ExtendedSceneBuilder.ExtendedSpecialInstructions());
        this.particles = new ParticleInstructions(this);
    }

    public ExtendedSceneBuilder.ExtendedWorldInstructions getWorld() {
        return (ExtendedSceneBuilder.ExtendedWorldInstructions) this.world;
    }

    public ExtendedSceneBuilder.ExtendedWorldInstructions getLevel() {
        return (ExtendedSceneBuilder.ExtendedWorldInstructions) this.world;
    }

    public SceneBuilder.DebugInstructions getDebug() {
        return this.debug;
    }

    public SceneBuilder.OverlayInstructions getOverlay() {
        return this.overlay;
    }

    public SceneBuilder.EffectInstructions getEffects() {
        return this.effects;
    }

    public SceneBuilder.SpecialInstructions getSpecial() {
        return this.special;
    }

    public ParticleInstructions getParticles() {
        return this.particles;
    }

    public void showStructure() {
        this.showStructure(this.ponderScene.getBasePlateSize() * 2);
    }

    public void showStructure(int height) {
        BlockPos start = new BlockPos(this.ponderScene.getBasePlateOffsetX(), 0, this.ponderScene.getBasePlateOffsetZ());
        BlockPos size = new BlockPos(this.ponderScene.getBasePlateSize() - 1, height, this.ponderScene.getBasePlateSize() - 1);
        Selection selection = this.ponderScene.getSceneBuildingUtil().select.cuboid(start, size);
        this.encapsulateBounds(size);
        this.world.showSection(selection, Direction.UP);
    }

    public void encapsulateBounds(BlockPos size) {
        this.addInstruction(ps -> {
            PonderWorld w = ps.getWorld();
            if (w != null) {
                w.getBounds().encapsulate(size);
            }
        });
    }

    public void playSound(SoundEvent soundEvent, SoundSource soundSource, float volume, float pitch) {
        Preconditions.checkArgument(soundEvent != null, "Given sound does not exist");
        if (Minecraft.getInstance().player != null) {
            this.addInstruction(ps -> {
                SimpleSoundInstance sound = new SimpleSoundInstance(soundEvent, soundSource, volume, pitch, SoundInstance.createUnseededRandom(), Minecraft.getInstance().player.m_20183_());
                Minecraft.getInstance().getSoundManager().play(sound);
            });
        }
    }

    public void playSound(SoundEvent soundEvent, float volume) {
        this.playSound(soundEvent, SoundSource.MASTER, volume, 1.0F);
    }

    public void playSound(SoundEvent soundEvent) {
        this.playSound(soundEvent, SoundSource.MASTER, 1.0F, 1.0F);
    }

    public TextWindowElement.Builder text(int duration, String text) {
        return this.overlay.showText(duration).text(text);
    }

    public TextWindowElement.Builder text(int duration, String text, Vec3 position) {
        return this.overlay.showText(duration).text(text).pointAt(position);
    }

    public TextWindowElement.Builder sharedText(int duration, ResourceLocation key) {
        return this.overlay.showText(duration).sharedText(key);
    }

    public TextWindowElement.Builder sharedText(int duration, ResourceLocation key, Vec3 position) {
        return this.overlay.showText(duration).sharedText(key).pointAt(position).colored(PonderPalette.BLUE);
    }

    public InputWindowElement showControls(int duration, Vec3 pos, Pointing pointing) {
        InputWindowElement element = new InputWindowElement(pos, pointing);
        this.addInstruction(new ShowInputInstruction(element, duration));
        return element;
    }

    public class ExtendedSpecialInstructions extends SceneBuilder.SpecialInstructions {

        @HideFromJS
        @Override
        public void movePointOfInterest(BlockPos location) {
            super.movePointOfInterest(location);
        }
    }

    public class ExtendedWorldInstructions extends SceneBuilder.WorldInstructions {

        public ElementLink<EntityElement> createEntity(EntityType<?> entityType, Vec3 position, Consumer<Entity> consumer) {
            return this.createEntity(level -> {
                Entity entity = entityType.create(level);
                Objects.requireNonNull(entity, "Could not create entity of type " + PonderPlatform.getEntityTypeName(entityType));
                entity.setPosRaw(position.x, position.y, position.z);
                entity.setOldPosAndRot();
                entity.lookAt(EntityAnchorArgument.Anchor.FEET, position.add(0.0, 0.0, -1.0));
                consumer.accept(entity);
                return entity;
            });
        }

        public ElementLink<EntityElement> createEntity(EntityType<?> entityType, Vec3 position) {
            return this.createEntity(entityType, position, entity -> {
            });
        }

        public void modifyBlocks(Selection pos, BlockStateFunction function) {
            this.modifyBlocks(pos, true, function);
        }

        public void modifyBlocks(Selection selection, boolean spawnParticles, BlockStateFunction function) {
            this.modifyBlocks(selection, BlockStateFunction.from(function), spawnParticles);
        }

        public void modifyBlocks(Selection selection, BlockStateFunction function, boolean spawnParticles) {
            this.modifyBlocks(selection, BlockStateFunction.from(function), spawnParticles);
        }

        public void modifyBlock(BlockPos pos, BlockStateFunction function, boolean spawnParticles) {
            this.modifyBlock(pos, BlockStateFunction.from(function), spawnParticles);
        }

        public void setBlocks(Selection selection, BlockState blockState) {
            this.setBlocks(selection, true, blockState);
        }

        public void setBlocks(Selection selection, boolean spawnParticles, BlockState blockState) {
            this.setBlocks(selection, blockState, spawnParticles);
        }

        @Deprecated(forRemoval = true)
        public void modifyTileNBT(Selection selection, Consumer<CompoundTag> consumer) {
            ConsoleJS.CLIENT.warn("[PonderJS] modifyTileNBT(selection, (nbt) => {}) is deprecated, use modifyBlockEntityNBT(selection, (nbt) => {}) instead");
            this.modifyBlockEntityNBT(selection, false, consumer);
        }

        @Deprecated(forRemoval = true)
        public void modifyTileNBT(Selection selection, Consumer<CompoundTag> consumer, boolean reDrawBlocks) {
            ConsoleJS.CLIENT.warn("[PonderJS] modifyTileNBT(selection, (nbt) => {}, reDrawBlocks) is deprecated, use modifyBlockEntityNBT(selection, reDrawBlocks, (nbt) => {}) instead");
            this.modifyBlockEntityNBT(selection, BlockEntity.class, consumer, reDrawBlocks);
        }

        public void modifyBlockEntityNBT(Selection selection, Consumer<CompoundTag> consumer) {
            this.modifyBlockEntityNBT(selection, false, consumer);
        }

        public void modifyBlockEntityNBT(Selection selection, boolean reDrawBlocks, Consumer<CompoundTag> consumer) {
            this.modifyBlockEntityNBT(selection, BlockEntity.class, consumer, reDrawBlocks);
        }

        @HideFromJS
        @Override
        public void modifyBlocks(Selection selection, UnaryOperator<BlockState> stateFunc, boolean spawnParticles) {
            super.modifyBlocks(selection, stateFunc, spawnParticles);
        }

        @HideFromJS
        @Override
        public void modifyBlock(BlockPos pos, UnaryOperator<BlockState> stateFunc, boolean spawnParticles) {
            super.modifyBlock(pos, stateFunc, spawnParticles);
        }

        public void removeEntity(ElementLink<EntityElement> link) {
            ExtendedSceneBuilder.this.addInstruction(scene -> {
                EntityElement resolve = scene.resolve(link);
                if (resolve != null) {
                    resolve.ifPresent(Entity::m_146870_);
                }
            });
        }
    }
}