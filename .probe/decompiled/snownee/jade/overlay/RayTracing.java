package snownee.jade.overlay;

import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jetbrains.annotations.Nullable;
import snownee.jade.Jade;
import snownee.jade.api.Accessor;
import snownee.jade.api.ui.IElement;
import snownee.jade.impl.ObjectDataCenter;
import snownee.jade.impl.WailaClientRegistration;
import snownee.jade.impl.ui.ItemStackElement;
import snownee.jade.util.CommonProxy;

public class RayTracing {

    public static final RayTracing INSTANCE = new RayTracing();

    public static Predicate<Entity> ENTITY_FILTER = entity -> true;

    private final Minecraft mc = Minecraft.getInstance();

    private HitResult target = null;

    private RayTracing() {
    }

    public static BlockState wrapBlock(BlockGetter level, BlockHitResult hit, CollisionContext context) {
        if (hit.getType() != HitResult.Type.BLOCK) {
            return Blocks.AIR.defaultBlockState();
        } else {
            BlockState blockState = level.getBlockState(hit.getBlockPos());
            FluidState fluidState = blockState.m_60819_();
            if (!fluidState.isEmpty()) {
                if (blockState.m_60713_(Blocks.BARRIER) && WailaClientRegistration.INSTANCE.shouldHide(blockState)) {
                    return fluidState.createLegacyBlock();
                }
                if (blockState.m_60651_(level, hit.getBlockPos(), context).isEmpty()) {
                    return fluidState.createLegacyBlock();
                }
            }
            return blockState;
        }
    }

    @Nullable
    public static EntityHitResult getEntityHitResult(Level worldIn, Entity projectile, Vec3 startVec, Vec3 endVec, AABB boundingBox, Predicate<Entity> filter) {
        double d0 = Double.MAX_VALUE;
        Entity entity = null;
        for (Entity entity1 : worldIn.getEntities(projectile, boundingBox, filter)) {
            AABB axisalignedbb = entity1.getBoundingBox();
            if (axisalignedbb.getSize() < 0.3) {
                axisalignedbb = axisalignedbb.inflate(0.3);
            }
            if (axisalignedbb.contains(startVec)) {
                entity = entity1;
                d0 = 0.0;
                break;
            }
            Optional<Vec3> optional = axisalignedbb.clip(startVec, endVec);
            if (optional.isPresent()) {
                double d1 = startVec.distanceToSqr((Vec3) optional.get());
                if (d1 < d0) {
                    entity = entity1;
                    d0 = d1;
                }
            }
        }
        return entity == null ? null : new EntityHitResult(entity);
    }

    public static boolean isEmptyElement(IElement element) {
        return element == null || element == ItemStackElement.EMPTY;
    }

    public void fire() {
        Entity viewpoint = this.mc.getCameraEntity();
        if (viewpoint != null) {
            if (this.mc.hitResult != null && this.mc.hitResult.getType() == HitResult.Type.ENTITY) {
                Entity targetEntity = ((EntityHitResult) this.mc.hitResult).getEntity();
                if (this.canBeTarget(targetEntity, viewpoint)) {
                    this.target = this.mc.hitResult;
                    return;
                }
            }
            float reach = this.mc.gameMode.getPickRange() + Jade.CONFIG.get().getGeneral().getReachDistance();
            this.target = this.rayTrace(viewpoint, (double) reach, this.mc.getFrameTime());
        }
    }

    public HitResult getTarget() {
        return this.target;
    }

    public HitResult rayTrace(Entity entity, double playerReach, float partialTicks) {
        Vec3 eyePosition = entity.getEyePosition(partialTicks);
        Vec3 traceEnd;
        if (this.mc.hitResult != null && this.mc.hitResult.getType() == HitResult.Type.BLOCK) {
            traceEnd = this.mc.hitResult.getLocation();
            traceEnd = eyePosition.add(traceEnd.subtract(eyePosition).scale(1.01));
        } else {
            Vec3 lookVector = entity.getViewVector(partialTicks);
            traceEnd = eyePosition.add(lookVector.x * playerReach, lookVector.y * playerReach, lookVector.z * playerReach);
        }
        Level world = entity.level();
        AABB bound = new AABB(eyePosition, traceEnd);
        Predicate<Entity> predicate = e -> this.canBeTarget(e, entity);
        EntityHitResult entityResult = getEntityHitResult(world, entity, eyePosition, traceEnd, bound, predicate);
        if (this.mc.hitResult != null && this.mc.hitResult.getType() == HitResult.Type.BLOCK) {
            Vec3 lookVector = entity.getViewVector(partialTicks);
            traceEnd = eyePosition.add(lookVector.x * playerReach, lookVector.y * playerReach, lookVector.z * playerReach);
        }
        BlockState eyeBlock = world.getBlockState(BlockPos.containing(eyePosition));
        ClipContext.Fluid fluidView = ClipContext.Fluid.NONE;
        if (eyeBlock.m_60819_().isEmpty()) {
            fluidView = Jade.CONFIG.get().getGeneral().getDisplayFluids().ctx;
        }
        ClipContext context = new ClipContext(eyePosition, traceEnd, ClipContext.Block.OUTLINE, fluidView, entity);
        BlockHitResult blockResult = world.m_45547_(context);
        if (entityResult != null) {
            if (blockResult.getType() != HitResult.Type.BLOCK) {
                return entityResult;
            }
            double entityDist = entityResult.m_82450_().distanceToSqr(eyePosition);
            double blockDist = blockResult.m_82450_().distanceToSqr(eyePosition);
            if (entityDist < blockDist) {
                return entityResult;
            }
        }
        if (blockResult.getType() == HitResult.Type.BLOCK) {
            CollisionContext collisionContext = CollisionContext.of(entity);
            BlockState state = wrapBlock(world, blockResult, collisionContext);
            if (WailaClientRegistration.INSTANCE.shouldHide(state)) {
                return null;
            }
        }
        return blockResult;
    }

    private boolean canBeTarget(Entity target, Entity viewEntity) {
        if (target.isRemoved()) {
            return false;
        } else if (target.isSpectator()) {
            return false;
        } else if (target == viewEntity.getVehicle()) {
            return false;
        } else {
            if (target instanceof Projectile projectile && projectile.f_19797_ <= 10) {
                return false;
            }
            if (CommonProxy.isMultipartEntity(target) && !target.isPickable()) {
                return false;
            } else {
                if (viewEntity instanceof Player player) {
                    if (target.isInvisibleTo(player)) {
                        return false;
                    }
                    if (this.mc.gameMode.isDestroying() && target.getType() == EntityType.ITEM) {
                        return false;
                    }
                } else if (target.isInvisible()) {
                    return false;
                }
                return !WailaClientRegistration.INSTANCE.shouldHide(target) && ENTITY_FILTER.test(target);
            }
        }
    }

    public IElement getIcon() {
        Accessor<?> accessor = ObjectDataCenter.get();
        if (accessor == null) {
            return null;
        } else {
            IElement icon = ObjectDataCenter.getIcon();
            return isEmptyElement(icon) ? null : icon;
        }
    }
}