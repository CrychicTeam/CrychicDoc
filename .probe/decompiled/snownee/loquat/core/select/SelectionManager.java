package snownee.loquat.core.select;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import snownee.loquat.LoquatConfig;
import snownee.loquat.core.AreaManager;
import snownee.loquat.core.area.Area;
import snownee.loquat.duck.LoquatPlayer;
import snownee.loquat.network.SSyncSelectionPacket;
import snownee.loquat.util.LoquatUtil;
import snownee.loquat.util.TransformUtil;

public class SelectionManager {

    private final List<PosSelection> selections;

    private final List<UUID> selectedAreas;

    private boolean lastOneIncomplete;

    public SelectionManager(boolean isClientSide) {
        this.selections = (List<PosSelection>) (isClientSide ? Collections.synchronizedList(Lists.newArrayList()) : Lists.newArrayList());
        this.selectedAreas = (List<UUID>) (isClientSide ? Collections.synchronizedList(Lists.newArrayList()) : Lists.newArrayList());
    }

    public static SelectionManager of(Player player) {
        return ((LoquatPlayer) player).loquat$getSelectionManager();
    }

    public static boolean isHoldingTool(Player player) {
        return LoquatConfig.selectionItem != Items.AIR && player.m_20310_(2) && player.isCreative() && player.m_21205_().is(LoquatConfig.selectionItem);
    }

    public boolean leftClickBlock(ServerLevel world, BlockPos pos, ServerPlayer player) {
        if (!isHoldingTool(player)) {
            return false;
        } else {
            if (world.m_7702_(pos) instanceof StructureBlockEntity be) {
                if (player.m_6144_()) {
                    this.selectedAreas.clear();
                    Vec3i size = be.getStructureSize();
                    if (size.getX() > 0 && size.getY() > 0 && size.getZ() > 0) {
                        AABB aabb = TransformUtil.getAABB(be);
                        AreaManager.of(world).areas().forEach(areax -> {
                            if (areax.inside(aabb)) {
                                this.selectedAreas.add(areax.getUuid());
                            }
                        });
                    }
                    player.displayClientMessage(Component.translatable("loquat.msg.shiftClickStructureBlock"), false);
                } else if (this.selections.size() == 1) {
                    AABB aabb = ((PosSelection) this.selections.get(0)).toAABB();
                    be.setStructurePos(new BlockPos((int) aabb.minX, (int) aabb.minY, (int) aabb.minZ).subtract(pos));
                    if (be.getMode() != StructureMode.LOAD) {
                        be.setStructureSize(new BlockPos((int) aabb.getXsize(), (int) aabb.getYsize(), (int) aabb.getZsize()));
                    }
                }
                be.setShowBoundingBox(true);
                be.m_6596_();
                BlockState blockState = world.m_8055_(pos);
                world.sendBlockUpdated(pos, blockState, blockState, 3);
            } else if (player.m_6144_()) {
                AreaManager manager = AreaManager.of(world);
                for (Area area : manager.areas()) {
                    if (area.contains(pos)) {
                        if (this.selectedAreas.contains(area.getUuid())) {
                            this.selectedAreas.remove(area.getUuid());
                        } else {
                            this.selectedAreas.add(area.getUuid());
                        }
                    }
                }
            } else {
                if (this.selections.isEmpty()) {
                    this.lastOneIncomplete = false;
                }
                if (this.lastOneIncomplete) {
                    ((PosSelection) this.selections.get(this.selections.size() - 1)).pos2 = pos;
                } else {
                    this.selections.add(new PosSelection(pos));
                }
                this.lastOneIncomplete = !this.lastOneIncomplete;
            }
            SSyncSelectionPacket.sync(player);
            return true;
        }
    }

    public boolean rightClickItem(ServerLevel world, HitResult hit, ServerPlayer player) {
        if (isHoldingTool(player) && player.m_6144_()) {
            if (hit instanceof BlockHitResult blockHit && world.m_7702_(blockHit.getBlockPos()) instanceof StructureBlockEntity be && be.getMode() == StructureMode.LOAD) {
                Vec3i size = be.getStructureSize();
                if (size.getX() >= 1 && size.getY() >= 1 && size.getZ() >= 1) {
                    AABB aabb = TransformUtil.getAABB(be);
                    LoquatUtil.emptyBlocks(world, () -> BlockPos.betweenClosedStream(aabb));
                    AreaManager.of(world).removeAllInside(aabb);
                    player.displayClientMessage(Component.translatable("loquat.msg.shiftUseStructureBlock"), false);
                    SSyncSelectionPacket.sync(player);
                    return true;
                }
                return false;
            }
            this.reset(player);
            return true;
        } else {
            return false;
        }
    }

    public boolean isSelected(Area area) {
        return this.selectedAreas.contains(area.getUuid());
    }

    public Stream<Area> getSelectedAreas(ServerLevel world) {
        AreaManager areaManager = AreaManager.of(world);
        List<Area> areas = Lists.newArrayListWithExpectedSize(this.selectedAreas.size());
        this.selectedAreas.removeIf(uuid -> {
            Area area = areaManager.get(uuid);
            if (area == null) {
                return true;
            } else {
                areas.add(area);
                return false;
            }
        });
        return areas.stream();
    }

    public void reset(ServerPlayer player) {
        this.selections.clear();
        this.selectedAreas.clear();
        this.lastOneIncomplete = false;
        SSyncSelectionPacket.sync(player);
    }

    public List<PosSelection> getSelections() {
        return this.selections;
    }

    public List<UUID> getSelectedAreas() {
        return this.selectedAreas;
    }

    public boolean isLastOneIncomplete() {
        return this.lastOneIncomplete;
    }
}