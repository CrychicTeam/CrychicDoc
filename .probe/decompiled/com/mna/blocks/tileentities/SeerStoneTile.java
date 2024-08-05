package com.mna.blocks.tileentities;

import com.mna.api.blocks.DirectionalPoint;
import com.mna.api.blocks.tile.OwnerInformation;
import com.mna.api.blocks.tile.TileEntityWithInventory;
import com.mna.api.items.IPhylacteryItem;
import com.mna.blocks.artifice.SeerStoneBlock;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.gui.containers.block.ContainerSeerStone;
import com.mna.items.ItemInit;
import com.mna.items.ritual.ItemPlayerCharm;
import com.mna.tools.SummonUtils;
import com.mna.tools.math.MathUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.extensions.IForgeBlockEntity;

public class SeerStoneTile extends TileEntityWithInventory implements MenuProvider, IForgeBlockEntity, Consumer<FriendlyByteBuf> {

    private static final int TARGET_TIME = 100;

    private static final Direction[] offsets = new Direction[] { Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST };

    private LivingEntity target = null;

    private int findTargetCounter = 0;

    private int crystalAngle = 0;

    private OwnerInformation ownerInfo;

    public static final int WHITELIST_START = 0;

    public static final int WHITELIST_END = 17;

    public static final int BLACKLIST_START = 18;

    public static final int BLACKLIST_END = 35;

    public static final int MARKING_START = 36;

    public static final int MARKING_END = 37;

    private boolean targetDefaultMobs = true;

    private ArrayList<EntityType<?>> mobWhitelist = new ArrayList();

    private ArrayList<EntityType<?>> mobBlacklist = new ArrayList();

    private ArrayList<UUID> playerTargetWhiteList = new ArrayList();

    private ArrayList<UUID> playerTargetBlackList = new ArrayList();

    private AABB searchArea;

    public SeerStoneTile(BlockPos pos, BlockState state) {
        super(TileEntityInit.SEER_STONE.get(), pos, state, 38);
        this.ownerInfo = new OwnerInformation();
        this.buildFilters();
    }

    public static void Tick(Level level, BlockPos pos, BlockState state, SeerStoneTile blockEntity) {
        if (!blockEntity.hasTarget()) {
            blockEntity.crystalAngle -= 5;
            if (blockEntity.crystalAngle < 0) {
                blockEntity.crystalAngle = 0;
            }
            blockEntity.acquireTarget();
        } else {
            blockEntity.crystalAngle += 5;
            if (blockEntity.crystalAngle > 90) {
                blockEntity.crystalAngle = 90;
            }
            if (blockEntity.getTarget() == null || !blockEntity.getTarget().isAlive() || blockEntity.isTargetOutsideSearchArea()) {
                blockEntity.clearTarget();
            }
        }
    }

    public boolean hasTarget() {
        if (this.target == null) {
            return false;
        } else {
            boolean retainTarget = this.target.isAlive() && this.isMobWhitelisted(this.target);
            if (this.mobBlacklist.size() > 0) {
                retainTarget &= !this.isMobBlacklisted(this.target);
            }
            if (retainTarget && !(this.target.m_20238_(new Vec3((double) this.m_58899_().m_123341_(), (double) this.m_58899_().m_123342_(), (double) this.m_58899_().m_123343_())) > 2304.0)) {
                return true;
            } else {
                this.clearTarget();
                this.acquireTarget(true);
                return false;
            }
        }
    }

    private void acquireTarget() {
        this.acquireTarget(false);
    }

    private void acquireTarget(boolean force) {
        this.findTargetCounter++;
        if (this.findTargetCounter >= 100 || force) {
            this.findTargetCounter = 0;
            Vec3 myPos = new Vec3((double) this.m_58899_().m_123341_(), (double) this.m_58899_().m_123342_(), (double) this.m_58899_().m_123343_());
            List<LivingEntity> potentialTargets = this.m_58904_().m_6443_(LivingEntity.class, this.searchArea, e -> {
                if (SummonUtils.isSummon(e)) {
                    LivingEntity summoner = SummonUtils.getSummoner(e);
                    if (summoner != null && this.ownerInfo.isFriendlyTo(summoner)) {
                        return false;
                    }
                }
                boolean subPredicate = this.isMobWhitelisted(e);
                if (this.mobBlacklist.size() > 0) {
                    subPredicate &= !this.isMobBlacklisted(e);
                }
                return e.isAlive() && subPredicate;
            });
            if (potentialTargets.size() > 0) {
                potentialTargets.sort((o1, o2) -> {
                    Double o1Dist = o1.m_20238_(myPos);
                    Double o2Dist = o2.m_20238_(myPos);
                    return o1Dist.compareTo(o2Dist);
                });
                this.target = (LivingEntity) potentialTargets.get(0);
                for (int i = 0; i < offsets.length; i++) {
                    BlockEntity be = this.m_58904_().getBlockEntity(this.m_58899_().offset(offsets[i].getNormal()));
                    if (be != null && be instanceof ElementalSentryTile) {
                        ((ElementalSentryTile) be).forceTarget(this.target);
                    }
                }
                this.m_58904_().setBlockAndUpdate(this.m_58899_(), (BlockState) this.m_58900_().m_61124_(SeerStoneBlock.HAS_TARGET, true));
            }
        }
    }

    private boolean defaultPredicate(LivingEntity e) {
        return this.targetDefaultMobs ? e instanceof Enemy || e instanceof FlyingMob : false;
    }

    private boolean isMobWhitelisted(LivingEntity e) {
        boolean subPredicate = this.mobWhitelist.contains(e.m_6095_());
        if (subPredicate && e instanceof Player && this.playerTargetWhiteList.size() > 0) {
            subPredicate = this.playerTargetWhiteList.contains(((Player) e).getGameProfile().getId());
        }
        if (this.targetDefaultMobs) {
            subPredicate |= this.defaultPredicate(e);
        }
        return subPredicate;
    }

    private boolean isMobBlacklisted(LivingEntity e) {
        boolean subPredicate = this.mobBlacklist.contains(e.m_6095_());
        if (subPredicate && e instanceof Player && this.playerTargetBlackList.size() > 0) {
            subPredicate = this.playerTargetBlackList.contains(((Player) e).getGameProfile().getId());
        }
        return subPredicate;
    }

    public void buildFilters() {
        this.playerTargetWhiteList.clear();
        this.playerTargetBlackList.clear();
        this.mobWhitelist.clear();
        this.mobBlacklist.clear();
        this.targetDefaultMobs = false;
        for (int i = 0; i <= 17; i++) {
            ItemStack stack = this.m_8020_(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof IPhylacteryItem) {
                    EntityType<?> type = ((IPhylacteryItem) stack.getItem()).getContainedEntity(stack);
                    if (type != null) {
                        if (!this.mobWhitelist.contains(type)) {
                            this.mobWhitelist.add(type);
                        }
                    } else {
                        this.targetDefaultMobs = true;
                    }
                } else if (stack.getItem() instanceof ItemPlayerCharm) {
                    UUID uuid = ItemInit.PLAYER_CHARM.get().getPlayerUUID(stack);
                    if (!this.mobWhitelist.contains(EntityType.PLAYER)) {
                        this.mobWhitelist.add(EntityType.PLAYER);
                    }
                    if (uuid != null) {
                        this.playerTargetWhiteList.add(uuid);
                    }
                }
            }
        }
        if (this.mobWhitelist.isEmpty()) {
            this.targetDefaultMobs = true;
        }
        for (int ix = 18; ix <= 35; ix++) {
            ItemStack stack = this.m_8020_(ix);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof IPhylacteryItem) {
                    EntityType<?> type = ((IPhylacteryItem) stack.getItem()).getContainedEntity(stack);
                    if (type != null && !this.mobBlacklist.contains(type)) {
                        this.mobBlacklist.add(type);
                    }
                } else if (stack.getItem() instanceof ItemPlayerCharm) {
                    UUID uuidx = ItemInit.PLAYER_CHARM.get().getPlayerUUID(stack);
                    if (!this.mobBlacklist.contains(EntityType.PLAYER)) {
                        this.mobBlacklist.add(EntityType.PLAYER);
                    }
                    if (uuidx != null) {
                        this.playerTargetBlackList.add(uuidx);
                    }
                }
            }
        }
        DirectionalPoint a = ItemInit.RUNE_MARKING.get().getDirectionalPoint(this.m_8020_(36));
        DirectionalPoint b = ItemInit.RUNE_MARKING.get().getDirectionalPoint(this.m_8020_(37));
        if (a.isValid() && b.isValid()) {
            this.searchArea = MathUtils.createInclusiveBB(a.getPosition(), b.getPosition());
        } else {
            this.searchArea = new AABB(this.m_58899_()).inflate(32.0);
        }
    }

    private void clearTarget() {
        this.target = null;
        this.m_58904_().setBlockAndUpdate(this.m_58899_(), (BlockState) this.m_58900_().m_61124_(SeerStoneBlock.HAS_TARGET, false));
    }

    public Entity getTarget() {
        return this.target;
    }

    public void copyTargetTo(ElementalSentryTile other) {
        other.forceTarget(this.target);
    }

    private boolean isTargetOutsideSearchArea() {
        return this.searchArea != null && this.target != null && this.target.isAlive() ? !this.searchArea.intersects(this.target.m_20191_()) : false;
    }

    public int getCrystalAngle() {
        return this.crystalAngle;
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        this.ownerInfo.save(compound);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.ownerInfo.load(compound);
        this.buildFilters();
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Seer Stone");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new ContainerSeerStone(id, playerInventory, this);
    }

    public void accept(FriendlyByteBuf packetBuffer) {
        packetBuffer.writeBlockPos(this.m_58899_());
    }

    public void setOwner(Player owner) {
        this.ownerInfo.setOwner(owner);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag);
        return tag;
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(pkt.getTag());
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        int[] slots = new int[this.m_6643_()];
        int i = 0;
        while (i < this.m_6643_()) {
            slots[i] = i++;
        }
        return slots;
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack itemStackIn) {
        return true;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, Direction direction) {
        return true;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return true;
    }
}