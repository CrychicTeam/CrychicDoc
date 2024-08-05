package noppes.npcs.blocks;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;
import noppes.npcs.CustomBlocks;
import noppes.npcs.CustomItems;
import noppes.npcs.EventHooks;
import noppes.npcs.blocks.tiles.TileScripted;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.packets.server.SPacketGuiOpen;

public class BlockScripted extends BlockInterface {

    public static final VoxelShape AABB = Shapes.create(new AABB(0.001F, 0.001F, 0.001F, 0.998F, 0.998F, 0.998F));

    public BlockScripted() {
        super(BlockBehaviour.Properties.copy(Blocks.STONE).sound(SoundType.STONE).strength(5.0F, 10.0F));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileScripted(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return AABB;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter level, BlockPos pos, CollisionContext context) {
        TileScripted tile = (TileScripted) level.getBlockEntity(pos);
        return tile != null && tile.isPassible ? Shapes.empty() : AABB;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            ItemStack currentItem = player.getInventory().getSelected();
            if (currentItem == null || currentItem.getItem() != CustomItems.wand && currentItem.getItem() != CustomItems.scripter) {
                Vec3 vec = ray.m_82450_();
                float x = (float) (vec.x - (double) pos.m_123341_());
                float y = (float) (vec.y - (double) pos.m_123342_());
                float z = (float) (vec.z - (double) pos.m_123343_());
                TileScripted tile = (TileScripted) level.getBlockEntity(pos);
                return EventHooks.onScriptBlockInteract(tile, player, ray.getDirection().get3DDataValue(), x, y, z) ? InteractionResult.FAIL : InteractionResult.SUCCESS;
            } else {
                PlayerData data = PlayerData.get(player);
                data.scriptBlockPos = pos;
                SPacketGuiOpen.sendOpenGui(player, EnumGuiType.ScriptBlock, null, pos);
                return InteractionResult.SUCCESS;
            }
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack item) {
        if (!level.isClientSide && entity instanceof Player player) {
            PlayerData data = PlayerData.get(player);
            data.scriptBlockPos = pos;
            SPacketGuiOpen.sendOpenGui(player, EnumGuiType.ScriptBlock, null, pos);
        }
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entityIn) {
        if (!level.isClientSide) {
            TileScripted tile = (TileScripted) level.getBlockEntity(pos);
            EventHooks.onScriptBlockCollide(tile, entityIn);
        }
    }

    @Override
    public void handlePrecipitation(BlockState state, Level level, BlockPos pos, Biome.Precipitation type) {
        if (!level.isClientSide && type == Biome.Precipitation.RAIN) {
            TileScripted tile = (TileScripted) level.getBlockEntity(pos);
            EventHooks.onScriptBlockRainFill(tile);
        }
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (!level.isClientSide) {
            TileScripted tile = (TileScripted) level.getBlockEntity(pos);
            fallDistance = EventHooks.onScriptBlockFallenUpon(tile, entity, fallDistance);
            super.m_142072_(level, state, pos, entity, fallDistance);
        }
    }

    @Override
    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        if (!level.isClientSide) {
            TileScripted tile = (TileScripted) level.getBlockEntity(pos);
            EventHooks.onScriptBlockClicked(tile, player);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide) {
            TileScripted tile = (TileScripted) level.getBlockEntity(pos);
            EventHooks.onScriptBlockBreak(tile);
        }
        super.m_6810_(state, level, pos, newState, isMoving);
    }

    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (!level.isClientSide) {
            TileScripted tile = (TileScripted) level.getBlockEntity(pos);
            if (EventHooks.onScriptBlockHarvest(tile, player)) {
                return false;
            }
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Override
    public List<ItemStack> getDrops(BlockState blockState0, LootParams.Builder lootParamsBuilder1) {
        return Collections.emptyList();
    }

    public void onBlockExploded(BlockState state, Level level, BlockPos pos, Explosion explosion) {
        if (!level.isClientSide) {
            TileScripted tile = (TileScripted) level.getBlockEntity(pos);
            if (EventHooks.onScriptBlockExploded(tile)) {
                return;
            }
        }
        super.onBlockExploded(state, level, pos, explosion);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos pos2, boolean isMoving) {
        if (!level.isClientSide) {
            TileScripted tile = (TileScripted) level.getBlockEntity(pos);
            EventHooks.onScriptBlockNeighborChanged(tile, pos2);
            int power = 0;
            for (Direction enumfacing : Direction.values()) {
                int p = level.m_277185_(pos.relative(enumfacing), enumfacing);
                if (p > power) {
                    power = p;
                }
            }
            if (tile.prevPower != power && tile.powering <= 0) {
                tile.newPower = power;
            }
        }
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter worldIn, BlockPos pos, Direction side) {
        return this.getDirectSignal(state, worldIn, pos, side);
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction side) {
        return ((TileScripted) level.getBlockEntity(pos)).activePowering;
    }

    public boolean isLadder(BlockState state, LevelReader level, BlockPos pos, LivingEntity entity) {
        return ((TileScripted) level.m_7702_(pos)).isLadder;
    }

    public boolean isValidSpawn(BlockState state, BlockGetter level, BlockPos pos, SpawnPlacements.Type type, @Nullable EntityType<?> entityType) {
        return true;
    }

    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        TileScripted tile = (TileScripted) level.getBlockEntity(pos);
        return tile == null ? 0 : tile.lightValue;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return ((TileScripted) level.getBlockEntity(pos)).isPassible;
    }

    public boolean canEntityDestroy(BlockState state, BlockGetter level, BlockPos pos, Entity entity) {
        return super.canEntityDestroy(state, level, pos, entity);
    }

    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        float f = ((TileScripted) level.getBlockEntity(pos)).blockHardness;
        if (f == -1.0F) {
            return 0.0F;
        } else {
            int i = ForgeHooks.isCorrectToolForDrops(state, player) ? 30 : 100;
            return player.getDigSpeed(state, pos) / f / (float) i;
        }
    }

    public float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        return ((TileScripted) level.getBlockEntity(pos)).blockResistance;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return m_152132_(type, CustomBlocks.tile_scripted, TileScripted::tick);
    }
}