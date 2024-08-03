package com.mna.entities.utility;

import com.mna.entities.EntityInit;
import com.mna.tools.BlockUtils;
import com.mna.tools.InventoryUtilities;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;

public class FillHole extends Entity {

    private static final String NBT_BLOCK = "place_block";

    private static final String NBT_POSITIONS = "positions";

    private static final String NBT_INDEX = "place_index";

    private static final String NBT_RADIUS = "radius";

    private static final String NBT_HEIGHT = "height";

    private static final String NBT_FINISHED_SCAN = "finished_scan";

    private static final String NBT_PLAYER_ID = "player_uuid";

    private Block placeBlock;

    private ArrayList<BlockPos> positions;

    private int placeIndex = 0;

    private byte radius = 0;

    private byte height = 0;

    private boolean isValid = false;

    private boolean finishedScan = false;

    private UUID playerID = null;

    private Player player = null;

    public FillHole(EntityType<?> p_i48580_1_, Level p_i48580_2_) {
        super(p_i48580_1_, p_i48580_2_);
        this.positions = new ArrayList();
        this.f_19850_ = false;
    }

    public FillHole(Level world, Block toPlace, BlockPos center, Player caster, int height, int radius) {
        this(EntityInit.FILL_HOLE.get(), world);
        this.setData(toPlace, center, caster, height, radius);
    }

    @Override
    public void tick() {
        if (!this.m_9236_().isClientSide()) {
            if (!this.isValid) {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            } else {
                if (!this.finishedScan) {
                    if (this.placeIndex < this.height) {
                        if (!this.scanLayer(this.m_20183_().offset(0, this.placeIndex++, 0))) {
                            this.positions = (ArrayList<BlockPos>) this.positions.stream().map(bp -> bp.asLong()).distinct().map(l -> BlockPos.of(l)).collect(Collectors.toList());
                            this.finishedScan = true;
                            this.placeIndex = 0;
                        }
                    } else {
                        this.positions = (ArrayList<BlockPos>) this.positions.stream().map(bp -> bp.asLong()).distinct().map(l -> BlockPos.of(l)).collect(Collectors.toList());
                        this.placeIndex = 0;
                        this.finishedScan = true;
                    }
                } else if (!this.tryPlaceBlock()) {
                    this.m_142687_(Entity.RemovalReason.DISCARDED);
                }
            }
        }
    }

    private boolean tryPlaceBlock() {
        Player player = this.resolvePlayer();
        if (player == null) {
            return false;
        } else if (this.placeIndex >= this.positions.size()) {
            return false;
        } else {
            BlockPos placePos = (BlockPos) this.positions.get(this.placeIndex++);
            ItemStack stack = new ItemStack(this.placeBlock);
            BlockHitResult bhr = new BlockHitResult(Vec3.atCenterOf(placePos), this.m_6350_(), placePos, true);
            BlockPlaceContext ctx = new BlockPlaceContext(player, InteractionHand.OFF_HAND, stack, bhr);
            BlockState prevState = this.m_9236_().getBlockState(placePos);
            if (this.m_9236_().m_45784_(this) && prevState.m_60629_(ctx) && BlockUtils.placeBlock((ServerLevel) this.m_9236_(), placePos, Direction.DOWN, this.placeBlock.defaultBlockState(), player) && !player.isCreative() && !InventoryUtilities.removeItemFromInventory(stack, true, true, new InvWrapper(player.getInventory()))) {
                this.m_9236_().setBlock(placePos, prevState, 3);
                return false;
            } else {
                return true;
            }
        }
    }

    @Nullable
    private Player resolvePlayer() {
        if (this.playerID == null) {
            return null;
        } else {
            if (this.player == null) {
                this.player = this.m_9236_().m_46003_(this.playerID);
            }
            return this.player;
        }
    }

    public void setData(Block toPlace, BlockPos center, Player caster, int height, int radius) {
        this.placeBlock = toPlace;
        this.m_6034_((double) ((float) center.m_123341_() + 0.5F), (double) ((float) center.m_123342_() + 0.5F), (double) ((float) center.m_123343_() + 0.5F));
        this.height = (byte) height;
        this.radius = (byte) radius;
        if (caster.getGameProfile() != null) {
            this.playerID = caster.getGameProfile().getId();
            this.player = caster;
            this.isValid = true;
        } else {
            this.isValid = false;
        }
    }

    private boolean scanLayer(BlockPos center) {
        ArrayList<BlockPos> foundPositions = new ArrayList();
        BlockHitResult posZ = this.m_9236_().m_45547_(new ClipContext(Vec3.atCenterOf(center), Vec3.atCenterOf(center.offset(0, 0, this.radius)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null));
        BlockHitResult negZ = this.m_9236_().m_45547_(new ClipContext(Vec3.atCenterOf(center), Vec3.atCenterOf(center.offset(0, 0, -this.radius)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null));
        BlockHitResult posX = this.m_9236_().m_45547_(new ClipContext(Vec3.atCenterOf(center), Vec3.atCenterOf(center.offset(this.radius, 0, 0)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null));
        BlockHitResult negX = this.m_9236_().m_45547_(new ClipContext(Vec3.atCenterOf(center), Vec3.atCenterOf(center.offset(-this.radius, 0, 0)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null));
        if (posZ.getType() != HitResult.Type.MISS && negZ.getType() != HitResult.Type.MISS || posX.getType() != HitResult.Type.MISS && negX.getType() != HitResult.Type.MISS) {
            int rPZ = posZ.getType() == HitResult.Type.MISS ? this.radius : Math.abs(center.m_123343_() - posZ.getBlockPos().m_123343_());
            int rNZ = negZ.getType() == HitResult.Type.MISS ? -this.radius : -Math.abs(center.m_123343_() - negZ.getBlockPos().m_123343_());
            int rPX = posX.getType() == HitResult.Type.MISS ? this.radius : Math.abs(center.m_123341_() - posX.getBlockPos().m_123341_());
            int rNX = negX.getType() == HitResult.Type.MISS ? -this.radius : -Math.abs(center.m_123341_() - negX.getBlockPos().m_123341_());
            int zLen = rPZ - rNZ;
            int xLen = rPX - rNX;
            boolean zAxis = zLen >= xLen;
            for (int i = zAxis ? rNZ : rNX; i < (zAxis ? rPZ : rPX); i++) {
                BlockPos offsetPos = center.offset(zAxis ? 0 : i, 0, zAxis ? i : 0);
                BlockPos rayEndPos = offsetPos.offset(zAxis ? this.radius : 0, 0, zAxis ? 0 : this.radius);
                BlockPos rayEndNeg = offsetPos.offset(zAxis ? -this.radius : 0, 0, zAxis ? 0 : -this.radius);
                BlockHitResult posPass = this.m_9236_().m_45547_(new ClipContext(Vec3.atCenterOf(offsetPos), Vec3.atCenterOf(rayEndPos), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null));
                BlockHitResult negPass = this.m_9236_().m_45547_(new ClipContext(Vec3.atCenterOf(offsetPos), Vec3.atCenterOf(rayEndNeg), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null));
                if (posPass.getType() != HitResult.Type.MISS || negPass.getType() != HitResult.Type.MISS) {
                    int posPassOffset = posPass.getType() == HitResult.Type.MISS ? 0 : (int) Math.sqrt(offsetPos.m_123331_(posPass.getBlockPos()));
                    int negPassOffset = negPass.getType() == HitResult.Type.MISS ? 0 : (int) (-Math.sqrt(offsetPos.m_123331_(negPass.getBlockPos())));
                    for (int o = negPassOffset + 1; o <= posPassOffset; o++) {
                        BlockPos passStepOffset = offsetPos.offset(zAxis ? o : 0, 0, zAxis ? 0 : o);
                        foundPositions.add(passStepOffset);
                    }
                }
            }
            this.positions.addAll(foundPositions);
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {
        if (nbt.contains("place_block") && nbt.contains("positions") && nbt.contains("place_index") && nbt.contains("radius") && nbt.contains("height") && nbt.contains("finished_scan") && nbt.contains("player_uuid")) {
            this.placeBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(nbt.getString("place_block")));
            if (this.placeBlock != null) {
                ListTag list = nbt.getList("positions", 4);
                list.forEach(l -> this.positions.add(BlockPos.of(((LongTag) l).getAsLong())));
                this.placeIndex = nbt.getInt("place_index");
                this.radius = nbt.getByte("radius");
                this.height = nbt.getByte("height");
                this.finishedScan = nbt.getBoolean("finished_scan");
                this.playerID = UUID.fromString(nbt.getString("player_uuid"));
                this.isValid = true;
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {
        if (this.placeBlock != null || this.isValid) {
            nbt.putString("place_block", ForgeRegistries.BLOCKS.getKey(this.placeBlock).toString());
            nbt.putInt("place_index", this.placeIndex);
            nbt.putByte("height", this.height);
            nbt.putByte("radius", this.radius);
            nbt.putString("player_uuid", this.playerID.toString());
            nbt.putBoolean("finished_scan", this.finishedScan);
            ListTag positions = new ListTag();
            this.positions.forEach(bp -> positions.add(LongTag.valueOf(bp.asLong())));
            nbt.put("positions", positions);
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}