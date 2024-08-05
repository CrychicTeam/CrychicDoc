package org.violetmoon.quark.addons.oddities.block.be;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.zeta.util.MiscUtil;
import org.violetmoon.zeta.util.SimpleInventoryBlockEntity;

public abstract class AbstractEnchantingTableBlockEntity extends SimpleInventoryBlockEntity implements Nameable {

    public int tickCount;

    public float pageFlip;

    public float pageFlipPrev;

    public float flipT;

    public float flipA;

    public float bookSpread;

    public float bookSpreadPrev;

    public float bookRotation;

    public float bookRotationPrev;

    public float tRot;

    private static final Random rand = new Random();

    private Component customName;

    public AbstractEnchantingTableBlockEntity(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
    }

    @Override
    public int getContainerSize() {
        return 3;
    }

    @Override
    public boolean isAutomationEnabled() {
        return false;
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.m_183515_(compoundTag);
        if (this.hasCustomName()) {
            compoundTag.putString("CustomName", Component.Serializer.toJson(this.customName));
        }
    }

    @Override
    public void load(CompoundTag compound) {
        super.m_142466_(compound);
        if (compound.contains("CustomName", 8)) {
            this.customName = Component.Serializer.fromJson(compound.getString("CustomName"));
        }
    }

    public void tick() {
        this.performVanillaUpdate();
    }

    private void performVanillaUpdate() {
        this.bookSpreadPrev = this.bookSpread;
        this.bookRotationPrev = this.bookRotation;
        Player entityplayer = this.f_58857_.m_45924_((double) ((float) this.f_58858_.m_123341_() + 0.5F), (double) ((float) this.f_58858_.m_123342_() + 0.5F), (double) ((float) this.f_58858_.m_123343_() + 0.5F), 3.0, false);
        if (entityplayer != null) {
            double d0 = entityplayer.m_20185_() - (double) ((float) this.f_58858_.m_123341_() + 0.5F);
            double d1 = entityplayer.m_20189_() - (double) ((float) this.f_58858_.m_123343_() + 0.5F);
            this.tRot = (float) Mth.atan2(d1, d0);
            this.bookSpread += 0.1F;
            if (this.bookSpread < 0.5F || rand.nextInt(40) == 0) {
                float f1 = this.flipT;
                do {
                    this.flipT = this.flipT + (float) (rand.nextInt(4) - rand.nextInt(4));
                } while (f1 == this.flipT);
            }
        } else {
            this.tRot += 0.02F;
            this.bookSpread -= 0.1F;
        }
        while (this.bookRotation >= (float) Math.PI) {
            this.bookRotation -= (float) (Math.PI * 2);
        }
        while (this.bookRotation < (float) -Math.PI) {
            this.bookRotation += (float) (Math.PI * 2);
        }
        while (this.tRot >= (float) Math.PI) {
            this.tRot -= (float) (Math.PI * 2);
        }
        while (this.tRot < (float) -Math.PI) {
            this.tRot += (float) (Math.PI * 2);
        }
        float f2 = this.tRot - this.bookRotation;
        while ((double) f2 >= Math.PI) {
            f2 = (float) ((double) f2 - (Math.PI * 2));
        }
        while ((double) f2 < -Math.PI) {
            f2 = (float) ((double) f2 + (Math.PI * 2));
        }
        this.bookRotation += f2 * 0.4F;
        this.bookSpread = Mth.clamp(this.bookSpread, 0.0F, 1.0F);
        this.tickCount++;
        this.pageFlipPrev = this.pageFlip;
        float f = (this.flipT - this.pageFlip) * 0.4F;
        f = Mth.clamp(f, -0.2F, 0.2F);
        this.flipA = this.flipA + (f - this.flipA) * 0.9F;
        this.pageFlip = this.pageFlip + this.flipA;
    }

    public void dropItem(int i) {
        ItemStack stack = this.m_8020_(i);
        if (!stack.isEmpty()) {
            Containers.dropItemStack(this.f_58857_, (double) this.f_58858_.m_123341_(), (double) this.f_58858_.m_123342_(), (double) this.f_58858_.m_123343_(), stack);
        }
    }

    @NotNull
    @Override
    public Component getName() {
        return (Component) (this.hasCustomName() ? this.customName : Component.translatable("container.enchant"));
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null;
    }

    public void setCustomName(Component customNameIn) {
        this.customName = customNameIn;
    }

    @Override
    public void inventoryChanged(int i) {
        super.inventoryChanged(i);
        this.sync();
    }

    @Override
    protected boolean needsToSyncInventory() {
        return true;
    }

    @Override
    public void sync() {
        MiscUtil.syncTE(this);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}