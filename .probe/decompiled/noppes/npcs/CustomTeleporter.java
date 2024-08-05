package noppes.npcs;

import java.util.Optional;
import java.util.function.Function;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.portal.PortalForcer;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;

public class CustomTeleporter extends PortalForcer {

    private float yRot;

    private float xRot;

    private Vec3 pos;

    public CustomTeleporter(ServerLevel par1ServerLevel, Vec3 pos, float yRot, float xRot) {
        super(par1ServerLevel);
        this.pos = pos;
        this.yRot = yRot;
        this.xRot = xRot;
    }

    @Override
    public Optional<BlockUtil.FoundRectangle> findPortalAround(BlockPos pos, boolean isNether, WorldBorder border) {
        return Optional.empty();
    }

    public PortalInfo getPortalInfo(Entity entity, ServerLevel destLevel, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
        return new PortalInfo(this.pos, Vec3.ZERO, this.yRot, this.xRot);
    }
}