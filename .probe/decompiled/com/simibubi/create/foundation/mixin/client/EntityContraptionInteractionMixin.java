package com.simibubi.create.foundation.mixin.client;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.ContraptionCollider;
import com.simibubi.create.content.contraptions.ContraptionHandler;
import java.lang.ref.Reference;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.logging.log4j.util.TriConsumer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ Entity.class })
public abstract class EntityContraptionInteractionMixin extends CapabilityProvider<Entity> {

    @Shadow
    public Level level;

    @Shadow
    private Vec3 position;

    @Shadow
    private float nextStep;

    @Shadow
    @Final
    protected RandomSource random;

    @Shadow
    private EntityDimensions dimensions;

    private EntityContraptionInteractionMixin(Class<Entity> baseClass) {
        super(baseClass);
    }

    @Shadow
    protected abstract float nextStep();

    @Shadow
    protected abstract void playStepSound(BlockPos var1, BlockState var2);

    @Unique
    private Stream<AbstractContraptionEntity> create$getIntersectionContraptionsStream() {
        return ContraptionHandler.loadedContraptions.get(this.level).values().stream().map(Reference::get).filter(cEntity -> cEntity != null && cEntity.collidingEntities.containsKey((Entity) this));
    }

    @Unique
    private Set<AbstractContraptionEntity> create$getIntersectingContraptions() {
        Set<AbstractContraptionEntity> contraptions = (Set<AbstractContraptionEntity>) this.create$getIntersectionContraptionsStream().collect(Collectors.toSet());
        contraptions.addAll(this.level.m_45976_(AbstractContraptionEntity.class, ((Entity) this).getBoundingBox().inflate(1.0)));
        return contraptions;
    }

    @Unique
    private void forCollision(Vec3 worldPos, TriConsumer<Contraption, BlockState, BlockPos> action) {
        this.create$getIntersectingContraptions().forEach(cEntity -> {
            Vec3 localPos = ContraptionCollider.worldToLocalPos(worldPos, cEntity);
            BlockPos blockPos = BlockPos.containing(localPos);
            Contraption contraption = cEntity.getContraption();
            StructureTemplate.StructureBlockInfo info = (StructureTemplate.StructureBlockInfo) contraption.getBlocks().get(blockPos);
            if (info != null) {
                BlockState blockstate = info.state();
                action.accept(contraption, blockstate, blockPos);
            }
        });
    }

    @Inject(method = { "move" }, at = { @At(value = "JUMP", opcode = 154, ordinal = 7) })
    private void create$contraptionStepSounds(MoverType mover, Vec3 movement, CallbackInfo ci) {
        Vec3 worldPos = this.position.add(0.0, -0.2, 0.0);
        MutableBoolean stepped = new MutableBoolean(false);
        this.forCollision(worldPos, (contraption, state, pos) -> {
            this.playStepSound(pos, state);
            stepped.setTrue();
        });
        if (stepped.booleanValue()) {
            this.nextStep = this.nextStep();
        }
    }

    @Inject(method = { "move" }, at = { @At("TAIL") })
    private void create$onMove(MoverType mover, Vec3 movement, CallbackInfo ci) {
        if (this.level.isClientSide) {
            Entity self = (Entity) this;
            if (!self.onGround()) {
                if (!self.isPassenger()) {
                    Vec3 worldPos = this.position.add(0.0, -0.2, 0.0);
                    boolean onAtLeastOneContraption = this.create$getIntersectionContraptionsStream().anyMatch(cEntity -> {
                        Vec3 localPos = ContraptionCollider.worldToLocalPos(worldPos, cEntity);
                        BlockPos blockPos = BlockPos.containing(localPos);
                        Contraption contraption = cEntity.getContraption();
                        StructureTemplate.StructureBlockInfo info = (StructureTemplate.StructureBlockInfo) contraption.getBlocks().get(blockPos);
                        if (info == null) {
                            return false;
                        } else {
                            cEntity.registerColliding(self);
                            return true;
                        }
                    });
                    if (onAtLeastOneContraption) {
                        self.setOnGround(true);
                        self.getPersistentData().putBoolean("ContraptionGrounded", true);
                    }
                }
            }
        }
    }

    @Inject(method = { "spawnSprintParticle" }, at = { @At("TAIL") })
    private void create$onSpawnSprintParticle(CallbackInfo ci) {
        Entity self = (Entity) this;
        Vec3 worldPos = this.position.add(0.0, -0.2, 0.0);
        BlockPos particlePos = BlockPos.containing(worldPos);
        this.forCollision(worldPos, (contraption, state, pos) -> {
            if (!state.addRunningEffects(this.level, pos, self) && state.m_60799_() != RenderShape.INVISIBLE) {
                Vec3 speed = self.getDeltaMovement();
                this.level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state).setPos(particlePos), self.getX() + ((double) this.random.nextFloat() - 0.5) * (double) this.dimensions.width, self.getY() + 0.1, self.getZ() + ((double) this.random.nextFloat() - 0.5) * (double) this.dimensions.height, speed.x * -4.0, 1.5, speed.z * -4.0);
            }
        });
    }
}