package com.simibubi.create.content.contraptions;

import com.google.common.base.Predicates;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllMovementBehaviours;
import com.simibubi.create.AllPackets;
import com.simibubi.create.content.contraptions.actors.harvester.HarvesterMovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovingInteractionBehaviour;
import com.simibubi.create.content.contraptions.sync.ClientMotionPacket;
import com.simibubi.create.content.kinetics.base.BlockBreakingMovementBehaviour;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.collision.ContinuousOBBCollider;
import com.simibubi.create.foundation.collision.Matrix3d;
import com.simibubi.create.foundation.collision.OrientedBB;
import com.simibubi.create.foundation.damageTypes.CreateDamageSources;
import com.simibubi.create.foundation.utility.BlockHelper;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.commons.lang3.tuple.MutablePair;

public class ContraptionCollider {

    private static MutablePair<WeakReference<AbstractContraptionEntity>, Double> safetyLock = new MutablePair();

    private static Map<AbstractContraptionEntity, Map<Player, Double>> remoteSafetyLocks = new WeakHashMap();

    private static int packetCooldown = 0;

    static void collideEntities(AbstractContraptionEntity contraptionEntity) {
        Level world = contraptionEntity.m_20193_();
        Contraption contraption = contraptionEntity.getContraption();
        AABB bounds = contraptionEntity.m_20191_();
        if (contraption != null) {
            if (bounds != null) {
                Vec3 contraptionPosition = contraptionEntity.m_20182_();
                Vec3 contraptionMotion = contraptionPosition.subtract(contraptionEntity.getPrevPositionVec());
                Vec3 anchorVec = contraptionEntity.getAnchorVec();
                AbstractContraptionEntity.ContraptionRotationState rotation = null;
                if (world.isClientSide() && safetyLock.left != null && ((WeakReference) safetyLock.left).get() == contraptionEntity) {
                    DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> saveClientPlayerFromClipping(contraptionEntity, contraptionMotion));
                }
                boolean skipClientPlayer = false;
                for (Entity entity : world.m_6443_(Entity.class, bounds.inflate(2.0).expandTowards(0.0, 32.0, 0.0), contraptionEntity::m_7337_)) {
                    if (entity.isAlive()) {
                        ContraptionCollider.PlayerType playerType = getPlayerType(entity);
                        if (playerType != ContraptionCollider.PlayerType.REMOTE) {
                            entity.getSelfAndPassengers().forEach(e -> {
                                if (e instanceof ServerPlayer) {
                                    ((ServerPlayer) e).connection.aboveGroundTickCount = 0;
                                }
                            });
                            if (playerType != ContraptionCollider.PlayerType.SERVER) {
                                if (playerType == ContraptionCollider.PlayerType.CLIENT) {
                                    if (skipClientPlayer) {
                                        continue;
                                    }
                                    skipClientPlayer = true;
                                }
                                if (rotation == null) {
                                    rotation = contraptionEntity.getRotationState();
                                }
                                Matrix3d rotationMatrix = rotation.asMatrix();
                                Vec3 entityPosition = entity.position();
                                AABB entityBounds = entity.getBoundingBox();
                                Vec3 motion = entity.getDeltaMovement();
                                float yawOffset = rotation.getYawOffset();
                                Vec3 position = getWorldToLocalTranslation(entity, anchorVec, rotationMatrix, yawOffset);
                                motion = motion.subtract(contraptionMotion);
                                motion = rotationMatrix.transform(motion);
                                AABB localBB = entityBounds.move(position).inflate(1.0E-7);
                                OrientedBB obb = new OrientedBB(localBB);
                                obb.setRotation(rotationMatrix);
                                List<AABB> collidableBBs = (List<AABB>) contraption.getSimplifiedEntityColliders().orElseGet(() -> {
                                    List<AABB> bbs = new ArrayList();
                                    List<VoxelShape> potentialHits = getPotentiallyCollidedShapes(world, contraption, localBB.expandTowards(motion));
                                    potentialHits.forEach(shape -> shape.toAabbs().forEach(bbs::add));
                                    return bbs;
                                });
                                MutableObject<Vec3> collisionResponse = new MutableObject(Vec3.ZERO);
                                MutableObject<Vec3> normal = new MutableObject(Vec3.ZERO);
                                MutableObject<Vec3> location = new MutableObject(Vec3.ZERO);
                                MutableBoolean surfaceCollision = new MutableBoolean(false);
                                MutableFloat temporalResponse = new MutableFloat(1.0F);
                                Vec3 obbCenter = obb.getCenter();
                                boolean doHorizontalPass = !rotation.hasVerticalRotation();
                                for (boolean horizontalPass : Iterate.trueAndFalse) {
                                    boolean verticalPass = !horizontalPass || !doHorizontalPass;
                                    for (AABB bb : collidableBBs) {
                                        Vec3 currentResponse = (Vec3) collisionResponse.getValue();
                                        Vec3 currentCenter = obbCenter.add(currentResponse);
                                        if (!(Math.abs(currentCenter.x - bb.getCenter().x) - entityBounds.getXsize() - 1.0 > bb.getXsize() / 2.0) && !(Math.abs(currentCenter.y + motion.y - bb.getCenter().y) - entityBounds.getYsize() - 1.0 > bb.getYsize() / 2.0) && !(Math.abs(currentCenter.z - bb.getCenter().z) - entityBounds.getZsize() - 1.0 > bb.getZsize() / 2.0)) {
                                            obb.setCenter(currentCenter);
                                            ContinuousOBBCollider.ContinuousSeparationManifold intersect = obb.intersect(bb, motion);
                                            if (intersect != null) {
                                                if (verticalPass && surfaceCollision.isFalse()) {
                                                    surfaceCollision.setValue(intersect.isSurfaceCollision());
                                                }
                                                double timeOfImpact = intersect.getTimeOfImpact();
                                                boolean isTemporal = timeOfImpact > 0.0 && timeOfImpact < 1.0;
                                                Vec3 collidingNormal = intersect.getCollisionNormal();
                                                Vec3 collisionPosition = intersect.getCollisionPosition();
                                                if (!isTemporal) {
                                                    Vec3 separation = intersect.asSeparationVec((double) entity.getStepHeight());
                                                    if (separation != null && !separation.equals(Vec3.ZERO)) {
                                                        collisionResponse.setValue(currentResponse.add(separation));
                                                        timeOfImpact = 0.0;
                                                    }
                                                }
                                                boolean nearest = timeOfImpact >= 0.0 && (double) temporalResponse.getValue().floatValue() > timeOfImpact;
                                                if (collidingNormal != null && nearest) {
                                                    normal.setValue(collidingNormal);
                                                }
                                                if (collisionPosition != null && nearest) {
                                                    location.setValue(collisionPosition);
                                                }
                                                if (isTemporal && (double) temporalResponse.getValue().floatValue() > timeOfImpact) {
                                                    temporalResponse.setValue(timeOfImpact);
                                                }
                                            }
                                        }
                                    }
                                    if (verticalPass) {
                                        break;
                                    }
                                    boolean noVerticalMotionResponse = temporalResponse.getValue() == 1.0F;
                                    boolean noVerticalCollision = ((Vec3) collisionResponse.getValue()).y == 0.0;
                                    if (noVerticalCollision && noVerticalMotionResponse) {
                                        break;
                                    }
                                    collisionResponse.setValue(((Vec3) collisionResponse.getValue()).multiply(1.0078125, 0.0, 1.0078125));
                                }
                                Vec3 entityMotion = entity.getDeltaMovement();
                                Vec3 entityMotionNoTemporal = entityMotion;
                                Vec3 collisionNormal = (Vec3) normal.getValue();
                                Vec3 collisionLocation = (Vec3) location.getValue();
                                Vec3 totalResponse = (Vec3) collisionResponse.getValue();
                                boolean hardCollision = !totalResponse.equals(Vec3.ZERO);
                                boolean temporalCollision = temporalResponse.getValue() != 1.0F;
                                Vec3 motionResponse = !temporalCollision ? motion : motion.normalize().scale(motion.length() * (double) temporalResponse.getValue().floatValue());
                                rotationMatrix.transpose();
                                motionResponse = rotationMatrix.transform(motionResponse).add(contraptionMotion);
                                totalResponse = rotationMatrix.transform(totalResponse);
                                totalResponse = VecHelper.rotate(totalResponse, (double) yawOffset, Direction.Axis.Y);
                                collisionNormal = rotationMatrix.transform(collisionNormal);
                                collisionNormal = VecHelper.rotate(collisionNormal, (double) yawOffset, Direction.Axis.Y);
                                collisionNormal = collisionNormal.normalize();
                                collisionLocation = rotationMatrix.transform(collisionLocation);
                                collisionLocation = VecHelper.rotate(collisionLocation, (double) yawOffset, Direction.Axis.Y);
                                rotationMatrix.transpose();
                                double bounce = 0.0;
                                double slide = 0.0;
                                if (!collisionLocation.equals(Vec3.ZERO)) {
                                    collisionLocation = collisionLocation.add(entity.position().add(entity.getBoundingBox().getCenter()).scale(0.5));
                                    if (temporalCollision) {
                                        collisionLocation = collisionLocation.add(0.0, motionResponse.y, 0.0);
                                    }
                                    BlockPos pos = BlockPos.containing(contraptionEntity.toLocalVector(entity.position(), 0.0F));
                                    if (contraption.getBlocks().containsKey(pos)) {
                                        BlockState blockState = ((StructureTemplate.StructureBlockInfo) contraption.getBlocks().get(pos)).state();
                                        if (blockState.m_204336_(BlockTags.CLIMBABLE)) {
                                            surfaceCollision.setTrue();
                                            totalResponse = totalResponse.add(0.0, 0.1F, 0.0);
                                        }
                                    }
                                    pos = BlockPos.containing(contraptionEntity.toLocalVector(collisionLocation, 0.0F));
                                    if (contraption.getBlocks().containsKey(pos)) {
                                        BlockState blockState = ((StructureTemplate.StructureBlockInfo) contraption.getBlocks().get(pos)).state();
                                        MovingInteractionBehaviour movingInteractionBehaviour = (MovingInteractionBehaviour) contraption.interactors.get(pos);
                                        if (movingInteractionBehaviour != null) {
                                            movingInteractionBehaviour.handleEntityCollision(entity, pos, contraptionEntity);
                                        }
                                        bounce = BlockHelper.getBounceMultiplier(blockState.m_60734_());
                                        slide = (double) Math.max(0.0F, blockState.getFriction(contraption.world, pos, entity) - 0.6F);
                                    }
                                }
                                boolean hasNormal = !collisionNormal.equals(Vec3.ZERO);
                                boolean anyCollision = hardCollision || temporalCollision;
                                if (bounce > 0.0 && hasNormal && anyCollision && bounceEntity(entity, collisionNormal, contraptionEntity, bounce)) {
                                    entity.level().playSound(playerType == ContraptionCollider.PlayerType.CLIENT ? (Player) entity : null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.SLIME_BLOCK_FALL, SoundSource.BLOCKS, 0.5F, 1.0F);
                                } else {
                                    if (temporalCollision) {
                                        double idealVerticalMotion = motionResponse.y;
                                        if (idealVerticalMotion != entityMotion.y) {
                                            entity.setDeltaMovement(entityMotion.multiply(1.0, 0.0, 1.0).add(0.0, idealVerticalMotion, 0.0));
                                            entityMotion = entity.getDeltaMovement();
                                        }
                                    }
                                    if (hardCollision) {
                                        double motionX = entityMotion.x();
                                        double motionY = entityMotion.y();
                                        double motionZ = entityMotion.z();
                                        double intersectX = totalResponse.x();
                                        double intersectY = totalResponse.y();
                                        double intersectZ = totalResponse.z();
                                        double horizonalEpsilon = 0.0078125;
                                        if (motionX != 0.0 && Math.abs(intersectX) > horizonalEpsilon && motionX > 0.0 == intersectX < 0.0) {
                                            entityMotion = entityMotion.multiply(0.0, 1.0, 1.0);
                                        }
                                        if (motionY != 0.0 && intersectY != 0.0 && motionY > 0.0 == intersectY < 0.0) {
                                            entityMotion = entityMotion.multiply(1.0, 0.0, 1.0).add(0.0, contraptionMotion.y, 0.0);
                                        }
                                        if (motionZ != 0.0 && Math.abs(intersectZ) > horizonalEpsilon && motionZ > 0.0 == intersectZ < 0.0) {
                                            entityMotion = entityMotion.multiply(1.0, 1.0, 0.0);
                                        }
                                    }
                                    if (bounce == 0.0 && slide > 0.0 && hasNormal && anyCollision && rotation.hasVerticalRotation()) {
                                        double slideFactor = collisionNormal.multiply(1.0, 0.0, 1.0).length() * 1.25;
                                        Vec3 motionIn = entityMotionNoTemporal.multiply(0.0, 0.9, 0.0).add(0.0, -0.01F, 0.0);
                                        Vec3 slideNormal = collisionNormal.cross(motionIn.cross(collisionNormal)).normalize();
                                        Vec3 newMotion = entityMotion.multiply(0.85, 0.0, 0.85).add(slideNormal.scale((0.2F + slide) * motionIn.length() * slideFactor).add(0.0, -0.1F - collisionNormal.y * 0.125, 0.0));
                                        entity.setDeltaMovement(newMotion);
                                        entityMotion = entity.getDeltaMovement();
                                    }
                                    if (hardCollision || !surfaceCollision.isFalse()) {
                                        Vec3 allowedMovement = collide(totalResponse, entity);
                                        entity.setPos(entityPosition.x + allowedMovement.x, entityPosition.y + allowedMovement.y, entityPosition.z + allowedMovement.z);
                                        entityPosition = entity.position();
                                        entityMotion = handleDamageFromTrain(world, contraptionEntity, contraptionMotion, entity, entityMotion, playerType);
                                        entity.hurtMarked = true;
                                        Vec3 contactPointMotion = Vec3.ZERO;
                                        if (surfaceCollision.isTrue()) {
                                            contraptionEntity.registerColliding(entity);
                                            entity.fallDistance = 0.0F;
                                            for (Entity rider : entity.getIndirectPassengers()) {
                                                if (getPlayerType(rider) == ContraptionCollider.PlayerType.CLIENT) {
                                                    AllPackets.getChannel().sendToServer(new ClientMotionPacket(rider.getDeltaMovement(), true, 0.0F));
                                                }
                                            }
                                            boolean canWalk = bounce != 0.0 || slide == 0.0;
                                            if (canWalk || !rotation.hasVerticalRotation()) {
                                                if (canWalk) {
                                                    entity.setOnGround(true);
                                                }
                                                if (entity instanceof ItemEntity) {
                                                    entityMotion = entityMotion.multiply(0.5, 1.0, 0.5);
                                                }
                                            }
                                            contactPointMotion = contraptionEntity.getContactPointMotion(entityPosition);
                                            allowedMovement = collide(contactPointMotion, entity);
                                            entity.setPos(entityPosition.x + allowedMovement.x, entityPosition.y, entityPosition.z + allowedMovement.z);
                                        }
                                        entity.setDeltaMovement(entityMotion);
                                        if (playerType == ContraptionCollider.PlayerType.CLIENT) {
                                            double d0 = entity.getX() - entity.xo - contactPointMotion.x;
                                            double d1 = entity.getZ() - entity.zo - contactPointMotion.z;
                                            float limbSwing = Mth.sqrt((float) (d0 * d0 + d1 * d1)) * 4.0F;
                                            if (limbSwing > 1.0F) {
                                                limbSwing = 1.0F;
                                            }
                                            AllPackets.getChannel().sendToServer(new ClientMotionPacket(entityMotion, true, limbSwing));
                                            if (entity.onGround() && contraption instanceof TranslatingContraption) {
                                                safetyLock.setLeft(new WeakReference(contraptionEntity));
                                                safetyLock.setRight(entity.getY() - contraptionEntity.m_20186_());
                                            }
                                        }
                                    }
                                }
                            }
                        } else if (contraption instanceof TranslatingContraption) {
                            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> saveRemotePlayerFromClipping((Player) entity, contraptionEntity, contraptionMotion));
                        }
                    }
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void saveClientPlayerFromClipping(AbstractContraptionEntity contraptionEntity, Vec3 contraptionMotion) {
        LocalPlayer entity = Minecraft.getInstance().player;
        if (!entity.m_20159_()) {
            double prevDiff = (Double) safetyLock.right;
            double currentDiff = entity.m_20186_() - contraptionEntity.m_20186_();
            double motion = contraptionMotion.subtract(entity.m_20184_()).y;
            double trend = Math.signum(currentDiff - prevDiff);
            ClientPacketListener handler = entity.connection;
            if (handler.getOnlinePlayers().size() > 1) {
                if (packetCooldown > 0) {
                    packetCooldown--;
                }
                if (packetCooldown == 0) {
                    AllPackets.getChannel().sendToServer(new ContraptionColliderLockPacket.ContraptionColliderLockPacketRequest(contraptionEntity.m_19879_(), currentDiff));
                    packetCooldown = 3;
                }
            }
            if (trend != 0.0) {
                if (trend != Math.signum(motion)) {
                    double speed = contraptionMotion.multiply(0.0, 1.0, 0.0).lengthSqr();
                    if (!(trend > 0.0) || !(speed < 0.1)) {
                        if (!(speed < 0.05)) {
                            if (!savePlayerFromClipping(entity, contraptionEntity, contraptionMotion, prevDiff)) {
                                safetyLock.setLeft(null);
                            }
                        }
                    }
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void lockPacketReceived(int contraptionId, int remotePlayerId, double suggestedOffset) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level.getEntity(contraptionId) instanceof ControlledContraptionEntity contraptionEntity) {
            if (level.getEntity(remotePlayerId) instanceof RemotePlayer player) {
                ((Map) remoteSafetyLocks.computeIfAbsent(contraptionEntity, $ -> new WeakHashMap())).put(player, suggestedOffset);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void saveRemotePlayerFromClipping(Player entity, AbstractContraptionEntity contraptionEntity, Vec3 contraptionMotion) {
        if (!entity.m_20159_()) {
            Map<Player, Double> locksOnThisContraption = (Map<Player, Double>) remoteSafetyLocks.getOrDefault(contraptionEntity, Collections.emptyMap());
            double prevDiff = (Double) locksOnThisContraption.getOrDefault(entity, entity.m_20186_() - contraptionEntity.m_20186_());
            if (!savePlayerFromClipping(entity, contraptionEntity, contraptionMotion, prevDiff) && locksOnThisContraption.containsKey(entity)) {
                locksOnThisContraption.remove(entity);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static boolean savePlayerFromClipping(Player entity, AbstractContraptionEntity contraptionEntity, Vec3 contraptionMotion, double yStartOffset) {
        AABB bb = entity.m_20191_().deflate(0.25, 0.0, 0.25);
        double shortestDistance = Double.MAX_VALUE;
        double yStart = (double) entity.getStepHeight() + contraptionEntity.m_20186_() + yStartOffset;
        double rayLength = Math.max(5.0, Math.abs(entity.m_20186_() - yStart));
        for (int rayIndex = 0; rayIndex < 4; rayIndex++) {
            Vec3 start = new Vec3(rayIndex / 2 == 0 ? bb.minX : bb.maxX, yStart, rayIndex % 2 == 0 ? bb.minZ : bb.maxZ);
            Vec3 end = start.add(0.0, -rayLength, 0.0);
            BlockHitResult hitResult = ContraptionHandlerClient.rayTraceContraption(start, end, contraptionEntity);
            if (hitResult != null) {
                Vec3 hit = contraptionEntity.toGlobalVector(hitResult.m_82450_(), 1.0F);
                double hitDiff = start.y - hit.y;
                if (shortestDistance > hitDiff) {
                    shortestDistance = hitDiff;
                }
            }
        }
        if (shortestDistance > rayLength) {
            return false;
        } else {
            entity.m_6034_(entity.m_20185_(), yStart - shortestDistance, entity.m_20189_());
            return true;
        }
    }

    private static Vec3 handleDamageFromTrain(Level world, AbstractContraptionEntity contraptionEntity, Vec3 contraptionMotion, Entity entity, Vec3 entityMotion, ContraptionCollider.PlayerType playerType) {
        if (!(contraptionEntity instanceof CarriageContraptionEntity cce)) {
            return entityMotion;
        } else if (!entity.onGround()) {
            return entityMotion;
        } else {
            CompoundTag persistentData = entity.getPersistentData();
            if (persistentData.contains("ContraptionGrounded")) {
                persistentData.remove("ContraptionGrounded");
                return entityMotion;
            } else if (cce.collidingEntities.containsKey(entity)) {
                return entityMotion;
            } else if (entity instanceof ItemEntity) {
                return entityMotion;
            } else if (cce.nonDamageTicks != 0) {
                return entityMotion;
            } else if (!AllConfigs.server().trains.trainsCauseDamage.get()) {
                return entityMotion;
            } else {
                Vec3 diffMotion = contraptionMotion.subtract(entity.getDeltaMovement());
                if (!(diffMotion.length() <= 0.35F) && !(contraptionMotion.length() <= 0.35F)) {
                    DamageSource source = CreateDamageSources.runOver(world, contraptionEntity);
                    double damage = diffMotion.length();
                    if (entity.getClassification(false) == MobCategory.MONSTER) {
                        damage *= 2.0;
                    }
                    if (entity instanceof Player p && (p.isCreative() || p.isSpectator())) {
                        return entityMotion;
                    }
                    if (playerType == ContraptionCollider.PlayerType.CLIENT) {
                        AllPackets.getChannel().sendToServer(new TrainCollisionPacket((int) (damage * 16.0), contraptionEntity.m_19879_()));
                        world.playSound((Player) entity, entity.blockPosition(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.NEUTRAL, 1.0F, 0.75F);
                    } else {
                        entity.hurt(source, (float) ((int) (damage * 16.0)));
                        world.playSound(null, entity.blockPosition(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.NEUTRAL, 1.0F, 0.75F);
                        if (!entity.isAlive()) {
                            contraptionEntity.getControllingPlayer().map(world::m_46003_).ifPresent(AllAdvancements.TRAIN_ROADKILL::awardTo);
                        }
                    }
                    Vec3 added = entityMotion.add(contraptionMotion.multiply(1.0, 0.0, 1.0).normalize().add(0.0, 0.25, 0.0).scale(damage * 4.0)).add(diffMotion);
                    return VecHelper.clamp(added, 3.0F);
                } else {
                    return entityMotion;
                }
            }
        }
    }

    static boolean bounceEntity(Entity entity, Vec3 normal, AbstractContraptionEntity contraption, double factor) {
        if (factor == 0.0) {
            return false;
        } else if (entity.isSuppressingBounce()) {
            return false;
        } else {
            Vec3 contactPointMotion = contraption.getContactPointMotion(entity.position());
            Vec3 motion = entity.getDeltaMovement().subtract(contactPointMotion);
            Vec3 deltav = normal.scale(factor * 2.0 * motion.dot(normal));
            if (deltav.dot(deltav) < 0.1F) {
                return false;
            } else {
                entity.setDeltaMovement(entity.getDeltaMovement().subtract(deltav));
                return true;
            }
        }
    }

    public static Vec3 getWorldToLocalTranslation(Entity entity, Vec3 anchorVec, Matrix3d rotationMatrix, float yawOffset) {
        Vec3 entityPosition = entity.position();
        Vec3 centerY = new Vec3(0.0, entity.getBoundingBox().getYsize() / 2.0, 0.0);
        Vec3 position = entityPosition.add(centerY);
        position = worldToLocalPos(position, anchorVec, rotationMatrix, yawOffset);
        position = position.subtract(centerY);
        return position.subtract(entityPosition);
    }

    public static Vec3 worldToLocalPos(Vec3 worldPos, AbstractContraptionEntity contraptionEntity) {
        return worldToLocalPos(worldPos, contraptionEntity.getAnchorVec(), contraptionEntity.getRotationState());
    }

    public static Vec3 worldToLocalPos(Vec3 worldPos, Vec3 anchorVec, AbstractContraptionEntity.ContraptionRotationState rotation) {
        return worldToLocalPos(worldPos, anchorVec, rotation.asMatrix(), rotation.getYawOffset());
    }

    public static Vec3 worldToLocalPos(Vec3 worldPos, Vec3 anchorVec, Matrix3d rotationMatrix, float yawOffset) {
        Vec3 localPos = worldPos.subtract(anchorVec);
        localPos = localPos.subtract(VecHelper.CENTER_OF_ORIGIN);
        localPos = VecHelper.rotate(localPos, (double) (-yawOffset), Direction.Axis.Y);
        localPos = rotationMatrix.transform(localPos);
        return localPos.add(VecHelper.CENTER_OF_ORIGIN);
    }

    static Vec3 collide(Vec3 vec0, Entity e) {
        AABB aabb = e.getBoundingBox();
        List<VoxelShape> list = e.level().m_183134_(e, aabb.expandTowards(vec0));
        Vec3 vec3 = vec0.lengthSqr() == 0.0 ? vec0 : Entity.collideBoundingBox(e, vec0, aabb, e.level(), list);
        boolean flag = vec0.x != vec3.x;
        boolean flag1 = vec0.y != vec3.y;
        boolean flag2 = vec0.z != vec3.z;
        boolean flag3 = flag1 && vec0.y < 0.0;
        if (e.getStepHeight() > 0.0F && flag3 && (flag || flag2)) {
            Vec3 vec31 = Entity.collideBoundingBox(e, new Vec3(vec0.x, (double) e.getStepHeight(), vec0.z), aabb, e.level(), list);
            Vec3 vec32 = Entity.collideBoundingBox(e, new Vec3(0.0, (double) e.getStepHeight(), 0.0), aabb.expandTowards(vec0.x, 0.0, vec0.z), e.level(), list);
            if (vec32.y < (double) e.getStepHeight()) {
                Vec3 vec33 = Entity.collideBoundingBox(e, new Vec3(vec0.x, 0.0, vec0.z), aabb.move(vec32), e.level(), list).add(vec32);
                if (vec33.horizontalDistanceSqr() > vec31.horizontalDistanceSqr()) {
                    vec31 = vec33;
                }
            }
            if (vec31.horizontalDistanceSqr() > vec3.horizontalDistanceSqr()) {
                return vec31.add(Entity.collideBoundingBox(e, new Vec3(0.0, -vec31.y + vec0.y, 0.0), aabb.move(vec31), e.level(), list));
            }
        }
        return vec3;
    }

    private static ContraptionCollider.PlayerType getPlayerType(Entity entity) {
        if (!(entity instanceof Player)) {
            return ContraptionCollider.PlayerType.NONE;
        } else if (!entity.level().isClientSide) {
            return ContraptionCollider.PlayerType.SERVER;
        } else {
            MutableBoolean isClient = new MutableBoolean(false);
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> isClient.setValue(isClientPlayerEntity(entity)));
            return isClient.booleanValue() ? ContraptionCollider.PlayerType.CLIENT : ContraptionCollider.PlayerType.REMOTE;
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static boolean isClientPlayerEntity(Entity entity) {
        return entity instanceof LocalPlayer;
    }

    private static List<VoxelShape> getPotentiallyCollidedShapes(Level world, Contraption contraption, AABB localBB) {
        double height = localBB.getYsize();
        double width = localBB.getXsize();
        double horizontalFactor = height > width && width != 0.0 ? height / width : 1.0;
        double verticalFactor = width > height && height != 0.0 ? width / height : 1.0;
        AABB blockScanBB = localBB.inflate(0.5);
        blockScanBB = blockScanBB.inflate(horizontalFactor, verticalFactor, horizontalFactor);
        BlockPos min = BlockPos.containing(blockScanBB.minX, blockScanBB.minY, blockScanBB.minZ);
        BlockPos max = BlockPos.containing(blockScanBB.maxX, blockScanBB.maxY, blockScanBB.maxZ);
        return BlockPos.betweenClosedStream(min, max).filter(contraption.getBlocks()::containsKey).filter(Predicates.not(contraption::isHiddenInPortal)).map(p -> {
            BlockState blockState = ((StructureTemplate.StructureBlockInfo) contraption.getBlocks().get(p)).state();
            BlockPos pos = ((StructureTemplate.StructureBlockInfo) contraption.getBlocks().get(p)).pos();
            VoxelShape collisionShape = blockState.m_60812_(world, p);
            return collisionShape.move((double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_());
        }).filter(Predicates.not(VoxelShape::m_83281_)).toList();
    }

    public static boolean collideBlocks(AbstractContraptionEntity contraptionEntity) {
        if (!contraptionEntity.supportsTerrainCollision()) {
            return false;
        } else {
            Level world = contraptionEntity.m_20193_();
            Vec3 motion = contraptionEntity.m_20184_();
            TranslatingContraption contraption = (TranslatingContraption) contraptionEntity.getContraption();
            AABB bounds = contraptionEntity.m_20191_();
            Vec3 position = contraptionEntity.m_20182_();
            BlockPos gridPos = BlockPos.containing(position);
            if (contraption == null) {
                return false;
            } else if (bounds == null) {
                return false;
            } else if (motion.equals(Vec3.ZERO)) {
                return false;
            } else {
                Direction movementDirection = Direction.getNearest(motion.x, motion.y, motion.z);
                if (movementDirection.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
                    gridPos = gridPos.relative(movementDirection);
                }
                if (isCollidingWithWorld(world, contraption, gridPos, movementDirection)) {
                    return true;
                } else {
                    for (ControlledContraptionEntity otherContraptionEntity : world.m_6443_(ControlledContraptionEntity.class, bounds.inflate(1.0), e -> !e.equals(contraptionEntity))) {
                        if (otherContraptionEntity.supportsTerrainCollision()) {
                            Vec3 otherMotion = otherContraptionEntity.m_20184_();
                            TranslatingContraption otherContraption = (TranslatingContraption) otherContraptionEntity.getContraption();
                            AABB otherBounds = otherContraptionEntity.m_20191_();
                            Vec3 otherPosition = otherContraptionEntity.m_20182_();
                            if (otherContraption == null) {
                                return false;
                            }
                            if (otherBounds == null) {
                                return false;
                            }
                            if (bounds.move(motion).intersects(otherBounds.move(otherMotion))) {
                                for (BlockPos colliderPos : contraption.getOrCreateColliders(world, movementDirection)) {
                                    colliderPos = colliderPos.offset(gridPos).subtract(BlockPos.containing(otherPosition));
                                    if (otherContraption.getBlocks().containsKey(colliderPos)) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                    return false;
                }
            }
        }
    }

    public static boolean isCollidingWithWorld(Level world, TranslatingContraption contraption, BlockPos anchor, Direction movementDirection) {
        for (BlockPos pos : contraption.getOrCreateColliders(world, movementDirection)) {
            BlockPos colliderPos = pos.offset(anchor);
            if (!world.isLoaded(colliderPos)) {
                return true;
            }
            BlockState collidedState = world.getBlockState(colliderPos);
            StructureTemplate.StructureBlockInfo blockInfo = (StructureTemplate.StructureBlockInfo) contraption.getBlocks().get(pos);
            boolean emptyCollider = collidedState.m_60812_(world, pos).isEmpty();
            if (!(collidedState.m_60734_() instanceof CocoaBlock)) {
                MovementBehaviour movementBehaviour = AllMovementBehaviours.getBehaviour(blockInfo.state());
                if (movementBehaviour != null) {
                    if (movementBehaviour instanceof BlockBreakingMovementBehaviour behaviour) {
                        if (!behaviour.canBreak(world, colliderPos, collidedState) && !emptyCollider) {
                            return true;
                        }
                        continue;
                    }
                    if (movementBehaviour instanceof HarvesterMovementBehaviour harvesterMovementBehaviour) {
                        if (!harvesterMovementBehaviour.isValidCrop(world, colliderPos, collidedState) && !harvesterMovementBehaviour.isValidOther(world, colliderPos, collidedState) && !emptyCollider) {
                            return true;
                        }
                        continue;
                    }
                }
                if ((!AllBlocks.PULLEY_MAGNET.has(collidedState) || !pos.equals(BlockPos.ZERO) || movementDirection != Direction.UP) && !collidedState.m_247087_() && !emptyCollider) {
                    return true;
                }
            }
        }
        return false;
    }

    static enum PlayerType {

        NONE, CLIENT, REMOTE, SERVER
    }
}