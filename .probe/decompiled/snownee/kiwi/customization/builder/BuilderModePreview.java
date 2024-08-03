package snownee.kiwi.customization.builder;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Map.Entry;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Matrix4f;
import snownee.kiwi.util.KHolder;
import snownee.kiwi.util.NotNullByDefault;

@NotNullByDefault
public class BuilderModePreview implements DebugRenderer.SimpleDebugRenderer {

    public KHolder<BuilderRule> rule;

    public BlockPos pos;

    private BlockState blockState = Blocks.AIR.defaultBlockState();

    public List<BlockPos> positions = List.of();

    private final ListMultimap<Direction, AABB> faces = ArrayListMultimap.create(6, 32);

    private long lastUpdateTime;

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, double pCamX, double pCamY, double pCamZ) {
        Minecraft mc = Minecraft.getInstance();
        if (BuildersButton.isBuilderModeOn() && mc.hitResult instanceof BlockHitResult hitResult && mc.hitResult.getType() != HitResult.Type.MISS) {
            long millis = Util.getMillis();
            Player player = (Player) Objects.requireNonNull(mc.player);
            BlockState blockState = player.m_9236_().getBlockState(hitResult.getBlockPos());
            if (millis - this.lastUpdateTime > 200L || !hitResult.getBlockPos().equals(this.pos) || this.blockState != blockState) {
                this.lastUpdateTime = millis;
                this.blockState = blockState;
                this.updatePositions(player, hitResult);
            }
            if (!this.positions.isEmpty()) {
                VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.debugQuads());
                Matrix4f pose = pPoseStack.last().pose();
                float r = 1.0F;
                float g = 1.0F;
                float b = 1.0F;
                float a = 0.08F + Mth.sin((float) millis / 350.0F) * 0.05F;
                for (Entry<Direction, Collection<AABB>> entry : this.faces.asMap().entrySet()) {
                    Direction direction = (Direction) entry.getKey();
                    for (AABB aabb : (Collection) entry.getValue()) {
                        aabb = aabb.move(-pCamX, -pCamY, -pCamZ);
                        this.drawFace(pose, vertexconsumer, aabb, direction, r, g, b, a);
                    }
                }
            }
        } else {
            this.positions = List.of();
        }
    }

    private void updatePositions(Player player, BlockHitResult hitResult) {
        Minecraft mc = Minecraft.getInstance();
        if (!mc.isPaused()) {
            InteractionHand hand = InteractionHand.MAIN_HAND;
            ItemStack itemStack = player.m_21120_(hand);
            this.rule = null;
            this.pos = null;
            this.positions = List.of();
            for (KHolder<BuilderRule> holder : BuilderRules.find(this.blockState.m_60734_())) {
                if (holder.value().matches(player, itemStack, this.blockState)) {
                    this.positions = holder.value().searchPositions(new UseOnContext(player, hand, hitResult));
                    if (!this.positions.isEmpty()) {
                        this.rule = holder;
                        this.pos = hitResult.getBlockPos();
                        this.faces.clear();
                        VoxelShape fullShape = (VoxelShape) this.positions.stream().map(BuilderModePreview::getShape).reduce(Shapes.empty(), (a, b) -> Shapes.joinUnoptimized(a, b, BooleanOp.OR));
                        fullShape = fullShape.optimize();
                        List<AABB> aabbs = fullShape.toAabbs();
                        for (Direction direction : snownee.kiwi.util.Util.DIRECTIONS) {
                            for (AABB aabb : aabbs) {
                                VoxelShape faceShape = getFaceShape(aabb, direction);
                                faceShape = Shapes.join(faceShape, fullShape, BooleanOp.ONLY_FIRST);
                                if (!faceShape.isEmpty()) {
                                    for (AABB faceShapeAabb : faceShape.toAabbs()) {
                                        this.faces.put(direction, faceShapeAabb);
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    private void drawFace(Matrix4f pose, VertexConsumer consumer, AABB aabb, Direction face, float r, float g, float b, float a) {
        float minX = (float) aabb.minX;
        float minY = (float) aabb.minY;
        float minZ = (float) aabb.minZ;
        float maxX = (float) aabb.maxX;
        float maxY = (float) aabb.maxY;
        float maxZ = (float) aabb.maxZ;
        switch(face) {
            case DOWN:
                consumer.vertex(pose, minX, minY, minZ).color(r, g, b, a).endVertex();
                consumer.vertex(pose, minX, minY, maxZ).color(r, g, b, a).endVertex();
                consumer.vertex(pose, maxX, minY, maxZ).color(r, g, b, a).endVertex();
                consumer.vertex(pose, maxX, minY, minZ).color(r, g, b, a).endVertex();
                break;
            case UP:
                consumer.vertex(pose, minX, maxY, minZ).color(r, g, b, a).endVertex();
                consumer.vertex(pose, maxX, maxY, minZ).color(r, g, b, a).endVertex();
                consumer.vertex(pose, maxX, maxY, maxZ).color(r, g, b, a).endVertex();
                consumer.vertex(pose, minX, maxY, maxZ).color(r, g, b, a).endVertex();
                break;
            case NORTH:
                consumer.vertex(pose, minX, minY, minZ).color(r, g, b, a).endVertex();
                consumer.vertex(pose, maxX, minY, minZ).color(r, g, b, a).endVertex();
                consumer.vertex(pose, maxX, maxY, minZ).color(r, g, b, a).endVertex();
                consumer.vertex(pose, minX, maxY, minZ).color(r, g, b, a).endVertex();
                break;
            case SOUTH:
                consumer.vertex(pose, minX, minY, maxZ).color(r, g, b, a).endVertex();
                consumer.vertex(pose, minX, maxY, maxZ).color(r, g, b, a).endVertex();
                consumer.vertex(pose, maxX, maxY, maxZ).color(r, g, b, a).endVertex();
                consumer.vertex(pose, maxX, minY, maxZ).color(r, g, b, a).endVertex();
                break;
            case WEST:
                consumer.vertex(pose, minX, minY, minZ).color(r, g, b, a).endVertex();
                consumer.vertex(pose, minX, minY, maxZ).color(r, g, b, a).endVertex();
                consumer.vertex(pose, minX, maxY, maxZ).color(r, g, b, a).endVertex();
                consumer.vertex(pose, minX, maxY, minZ).color(r, g, b, a).endVertex();
                break;
            case EAST:
                consumer.vertex(pose, maxX, minY, minZ).color(r, g, b, a).endVertex();
                consumer.vertex(pose, maxX, maxY, minZ).color(r, g, b, a).endVertex();
                consumer.vertex(pose, maxX, maxY, maxZ).color(r, g, b, a).endVertex();
                consumer.vertex(pose, maxX, minY, maxZ).color(r, g, b, a).endVertex();
        }
    }

    private static VoxelShape getFaceShape(AABB aabb, Direction face) {
        aabb = switch(face) {
            case DOWN ->
                new AABB(aabb.minX, aabb.minY - 1.0E-4, aabb.minZ, aabb.maxX, aabb.minY, aabb.maxZ);
            case UP ->
                new AABB(aabb.minX, aabb.maxY, aabb.minZ, aabb.maxX, aabb.maxY + 1.0E-4, aabb.maxZ);
            case NORTH ->
                new AABB(aabb.minX, aabb.minY, aabb.minZ - 1.0E-4, aabb.maxX, aabb.maxY, aabb.minZ);
            case SOUTH ->
                new AABB(aabb.minX, aabb.minY, aabb.maxZ, aabb.maxX, aabb.maxY, aabb.maxZ + 1.0E-4);
            case WEST ->
                new AABB(aabb.minX - 1.0E-4, aabb.minY, aabb.minZ, aabb.minX, aabb.maxY, aabb.maxZ);
            case EAST ->
                new AABB(aabb.maxX, aabb.minY, aabb.minZ, aabb.maxX + 1.0E-4, aabb.maxY, aabb.maxZ);
        };
        return Shapes.create(aabb);
    }

    private static VoxelShape getShape(BlockPos pos) {
        ClientLevel level = (ClientLevel) Objects.requireNonNull(Minecraft.getInstance().level);
        BlockState blockState = level.m_8055_(pos);
        return blockState.m_60808_(level, pos).move((double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_());
    }
}