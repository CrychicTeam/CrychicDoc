package com.mna.api.blocks.tile;

import com.mna.api.ManaAndArtificeMod;
import com.mna.api.affinity.Affinity;
import com.mna.api.capabilities.IWellspringNodeRegistry;
import com.mna.capabilities.chunkdata.ChunkMagicProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableFloat;

public interface IEldrinConsumerTile {

    default float consume(Player player, BlockPos blockPos, Vec3 destPos, Affinity type, float amount, float minimumSupply) {
        return player != null && player.getGameProfile() != null && player.getGameProfile().getId() != null ? this.consume(player, player.m_9236_(), blockPos, destPos, type, amount, minimumSupply) : 0.0F;
    }

    default float consume(Player player, BlockPos blockPos, Vec3 destPos, Affinity type, float amount) {
        return player != null && player.getGameProfile() != null && player.getGameProfile().getId() != null ? this.consume(player, player.m_9236_(), blockPos, destPos, type, amount, -1.0F) : 0.0F;
    }

    default float consume(Player owner, Level world, BlockPos blockPos, Vec3 destPos, Affinity type, float amount, float minimumSupply) {
        if (owner == null) {
            return 0.0F;
        } else {
            float supplied = 0.0F;
            LevelChunk chunkAccess = world.getChunkAt(blockPos);
            ChunkPos chunkPos = chunkAccess.m_7697_();
            ArrayList<BlockPos> suppliers = new ArrayList();
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    chunkAccess = world.getChunk(chunkPos.x + i, chunkPos.z + j);
                    chunkAccess.getCapability(ChunkMagicProvider.MAGIC).ifPresent(c -> suppliers.addAll((Collection) c.getKnownEldrinSuppliers().stream().map(l -> BlockPos.of(l)).collect(Collectors.toList())));
                }
            }
            boolean foundOneSupplier = false;
            for (BlockPos pos : suppliers) {
                BlockEntity blockEntity = world.getBlockEntity(pos);
                if (blockEntity != null && blockEntity instanceof IEldrinCapacitorTile) {
                    IEldrinCapacitorTile supplier = (IEldrinCapacitorTile) blockEntity;
                    if (supplier.canSupply(type, owner)) {
                        foundOneSupplier = true;
                        if (!(minimumSupply > 0.0F) || !(supplier.getChargeRate() < minimumSupply)) {
                            supplied += supplier.supply(owner, destPos, type, amount - supplied, false);
                            if (supplied >= amount) {
                                break;
                            }
                        }
                    }
                }
            }
            return !foundOneSupplier ? -1.0F : supplied;
        }
    }

    default float consumeDirect(UUID owner, Level world, Affinity type, float amount) {
        MutableFloat consumed = new MutableFloat(0.0F);
        world.getCapability(ManaAndArtificeMod.getWorldMagicCapability()).ifPresent(magic -> {
            IWellspringNodeRegistry wellsprings = magic.getWellspringRegistry();
            if (wellsprings != null) {
                consumed.setValue(wellsprings.consumePower(owner, world, type, amount));
            }
        });
        return consumed.getValue();
    }
}