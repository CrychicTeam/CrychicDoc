package org.violetmoon.quark.content.automation.block.be;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.automation.block.EnderWatcherBlock;
import org.violetmoon.quark.content.automation.module.EnderWatcherModule;
import org.violetmoon.zeta.block.be.ZetaBlockEntity;

public class EnderWatcherBlockEntity extends ZetaBlockEntity {

    public EnderWatcherBlockEntity(BlockPos pos, BlockState state) {
        super(EnderWatcherModule.blockEntityType, pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, EnderWatcherBlockEntity be) {
        boolean wasLooking = (Boolean) state.m_61143_(EnderWatcherBlock.WATCHED);
        int currWatch = (Integer) state.m_61143_(EnderWatcherBlock.POWER);
        int range = 80;
        int newWatch = 0;
        List<Player> players = level.m_45976_(Player.class, new AABB(be.f_58858_.offset(-range, -range, -range), be.f_58858_.offset(range, range, range)));
        EnderMan fakeEnderman = new EnderMan(EntityType.ENDERMAN, level);
        fakeEnderman.m_6034_((double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5 - (double) fakeEnderman.m_20192_(), (double) pos.m_123343_() + 0.5);
        boolean looking = false;
        for (Player player : players) {
            ItemStack helm = player.getItemBySlot(EquipmentSlot.HEAD);
            fakeEnderman.m_21391_(player, 180.0F, 180.0F);
            if (helm.isEmpty() || !Quark.ZETA.itemExtensions.get(helm).isEnderMaskZeta(helm, player, fakeEnderman)) {
                HitResult result = Quark.ZETA.raytracingUtil.rayTrace(player, level, player, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, 64.0);
                if (result != null && result.getType() == HitResult.Type.BLOCK && ((BlockHitResult) result).getBlockPos().equals(be.f_58858_)) {
                    looking = true;
                    Vec3 vec = result.getLocation();
                    Direction dir = ((BlockHitResult) result).getDirection();
                    double x = Math.abs(vec.x - (double) be.f_58858_.m_123341_() - 0.5) * (double) (1 - Math.abs(dir.getStepX()));
                    double y = Math.abs(vec.y - (double) be.f_58858_.m_123342_() - 0.5) * (double) (1 - Math.abs(dir.getStepY()));
                    double z = Math.abs(vec.z - (double) be.f_58858_.m_123343_() - 0.5) * (double) (1 - Math.abs(dir.getStepZ()));
                    double fract = 1.0 - Math.sqrt(x * x + y * y + z * z) / 0.7071067811865476;
                    int playerWatch = (int) Math.ceil(fract * 15.0);
                    if (playerWatch == 15 && player instanceof ServerPlayer sp) {
                        EnderWatcherModule.watcherCenterTrigger.trigger(sp);
                    }
                    newWatch = Math.max(newWatch, playerWatch);
                }
            }
        }
        if (!level.isClientSide && (looking != wasLooking || currWatch != newWatch)) {
            level.setBlock(be.f_58858_, (BlockState) ((BlockState) level.getBlockState(be.f_58858_).m_61124_(EnderWatcherBlock.WATCHED, looking)).m_61124_(EnderWatcherBlock.POWER, newWatch), 3);
        }
        if (looking) {
            double x = (double) be.f_58858_.m_123341_() - 0.1 + Math.random() * 1.2;
            double y = (double) be.f_58858_.m_123342_() - 0.1 + Math.random() * 1.2;
            double z = (double) be.f_58858_.m_123343_() - 0.1 + Math.random() * 1.2;
            level.addParticle(new DustParticleOptions(new Vector3f(1.0F, 0.0F, 0.0F), 1.0F), x, y, z, 0.0, 0.0, 0.0);
        }
    }
}