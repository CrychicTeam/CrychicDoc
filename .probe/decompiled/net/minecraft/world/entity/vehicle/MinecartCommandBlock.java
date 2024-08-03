package net.minecraft.world.entity.vehicle;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BaseCommandBlock;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class MinecartCommandBlock extends AbstractMinecart {

    static final EntityDataAccessor<String> DATA_ID_COMMAND_NAME = SynchedEntityData.defineId(MinecartCommandBlock.class, EntityDataSerializers.STRING);

    static final EntityDataAccessor<Component> DATA_ID_LAST_OUTPUT = SynchedEntityData.defineId(MinecartCommandBlock.class, EntityDataSerializers.COMPONENT);

    private final BaseCommandBlock commandBlock = new MinecartCommandBlock.MinecartCommandBase();

    private static final int ACTIVATION_DELAY = 4;

    private int lastActivated;

    public MinecartCommandBlock(EntityType<? extends MinecartCommandBlock> entityTypeExtendsMinecartCommandBlock0, Level level1) {
        super(entityTypeExtendsMinecartCommandBlock0, level1);
    }

    public MinecartCommandBlock(Level level0, double double1, double double2, double double3) {
        super(EntityType.COMMAND_BLOCK_MINECART, level0, double1, double2, double3);
    }

    @Override
    protected Item getDropItem() {
        return Items.MINECART;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.m_20088_().define(DATA_ID_COMMAND_NAME, "");
        this.m_20088_().define(DATA_ID_LAST_OUTPUT, CommonComponents.EMPTY);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        this.commandBlock.load(compoundTag0);
        this.m_20088_().set(DATA_ID_COMMAND_NAME, this.getCommandBlock().getCommand());
        this.m_20088_().set(DATA_ID_LAST_OUTPUT, this.getCommandBlock().getLastOutput());
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        this.commandBlock.save(compoundTag0);
    }

    @Override
    public AbstractMinecart.Type getMinecartType() {
        return AbstractMinecart.Type.COMMAND_BLOCK;
    }

    @Override
    public BlockState getDefaultDisplayBlockState() {
        return Blocks.COMMAND_BLOCK.defaultBlockState();
    }

    public BaseCommandBlock getCommandBlock() {
        return this.commandBlock;
    }

    @Override
    public void activateMinecart(int int0, int int1, int int2, boolean boolean3) {
        if (boolean3 && this.f_19797_ - this.lastActivated >= 4) {
            this.getCommandBlock().performCommand(this.m_9236_());
            this.lastActivated = this.f_19797_;
        }
    }

    @Override
    public InteractionResult interact(Player player0, InteractionHand interactionHand1) {
        return this.commandBlock.usedBy(player0);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor0) {
        super.m_7350_(entityDataAccessor0);
        if (DATA_ID_LAST_OUTPUT.equals(entityDataAccessor0)) {
            try {
                this.commandBlock.setLastOutput(this.m_20088_().get(DATA_ID_LAST_OUTPUT));
            } catch (Throwable var3) {
            }
        } else if (DATA_ID_COMMAND_NAME.equals(entityDataAccessor0)) {
            this.commandBlock.setCommand(this.m_20088_().get(DATA_ID_COMMAND_NAME));
        }
    }

    @Override
    public boolean onlyOpCanSetNbt() {
        return true;
    }

    public class MinecartCommandBase extends BaseCommandBlock {

        @Override
        public ServerLevel getLevel() {
            return (ServerLevel) MinecartCommandBlock.this.m_9236_();
        }

        @Override
        public void onUpdated() {
            MinecartCommandBlock.this.m_20088_().set(MinecartCommandBlock.DATA_ID_COMMAND_NAME, this.m_45438_());
            MinecartCommandBlock.this.m_20088_().set(MinecartCommandBlock.DATA_ID_LAST_OUTPUT, this.m_45437_());
        }

        @Override
        public Vec3 getPosition() {
            return MinecartCommandBlock.this.m_20182_();
        }

        public MinecartCommandBlock getMinecart() {
            return MinecartCommandBlock.this;
        }

        @Override
        public CommandSourceStack createCommandSourceStack() {
            return new CommandSourceStack(this, MinecartCommandBlock.this.m_20182_(), MinecartCommandBlock.this.m_20155_(), this.getLevel(), 2, this.m_45439_().getString(), MinecartCommandBlock.this.m_5446_(), this.getLevel().getServer(), MinecartCommandBlock.this);
        }

        @Override
        public boolean isValid() {
            return !MinecartCommandBlock.this.m_213877_();
        }
    }
}