package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityPixie;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import com.github.alexthe666.iceandfire.message.MessageUpdatePixieHouse;
import com.github.alexthe666.iceandfire.message.MessageUpdatePixieHouseModel;
import com.github.alexthe666.iceandfire.message.MessageUpdatePixieJar;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public class TileEntityJar extends BlockEntity {

    private static final float PARTICLE_WIDTH = 0.3F;

    private static final float PARTICLE_HEIGHT = 0.6F;

    public boolean hasPixie;

    public boolean prevHasProduced;

    public boolean hasProduced;

    public boolean tamedPixie;

    public UUID pixieOwnerUUID;

    public int pixieType;

    public int ticksExisted;

    public NonNullList<ItemStack> pixieItems = NonNullList.withSize(1, ItemStack.EMPTY);

    public float rotationYaw;

    public float prevRotationYaw;

    LazyOptional<? extends IItemHandler> downHandler = PixieJarInvWrapper.create(this);

    private final Random rand = new Random();

    public TileEntityJar(BlockPos pos, BlockState state) {
        super(IafTileEntityRegistry.PIXIE_JAR.get(), pos, state);
        this.hasPixie = true;
    }

    public TileEntityJar(BlockPos pos, BlockState state, boolean empty) {
        super(IafTileEntityRegistry.PIXIE_JAR.get(), pos, state);
        this.hasPixie = !empty;
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        compound.putBoolean("HasPixie", this.hasPixie);
        compound.putInt("PixieType", this.pixieType);
        compound.putBoolean("HasProduced", this.hasProduced);
        compound.putBoolean("TamedPixie", this.tamedPixie);
        if (this.pixieOwnerUUID != null) {
            compound.putUUID("PixieOwnerUUID", this.pixieOwnerUUID);
        }
        compound.putInt("TicksExisted", this.ticksExisted);
        ContainerHelper.saveAllItems(compound, this.pixieItems);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
        this.load(packet.getTag());
        if (!this.f_58857_.isClientSide) {
            IceAndFire.sendMSGToAll(new MessageUpdatePixieHouseModel(this.f_58858_.asLong(), packet.getTag().getInt("PixieType")));
        }
    }

    @Override
    public void load(CompoundTag compound) {
        this.hasPixie = compound.getBoolean("HasPixie");
        this.pixieType = compound.getInt("PixieType");
        this.hasProduced = compound.getBoolean("HasProduced");
        this.ticksExisted = compound.getInt("TicksExisted");
        this.tamedPixie = compound.getBoolean("TamedPixie");
        if (compound.hasUUID("PixieOwnerUUID")) {
            this.pixieOwnerUUID = compound.getUUID("PixieOwnerUUID");
        }
        this.pixieItems = NonNullList.withSize(1, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compound, this.pixieItems);
        super.load(compound);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, TileEntityJar entityJar) {
        entityJar.ticksExisted++;
        if (level.isClientSide && entityJar.hasPixie) {
            IceAndFire.PROXY.spawnParticle(EnumParticles.If_Pixie, (double) ((float) pos.m_123341_() + 0.5F) + (double) (entityJar.rand.nextFloat() * 0.3F * 2.0F) - 0.3F, (double) pos.m_123342_() + (double) (entityJar.rand.nextFloat() * 0.6F), (double) ((float) pos.m_123343_() + 0.5F) + (double) (entityJar.rand.nextFloat() * 0.3F * 2.0F) - 0.3F, (double) EntityPixie.PARTICLE_RGB[entityJar.pixieType][0], (double) EntityPixie.PARTICLE_RGB[entityJar.pixieType][1], (double) EntityPixie.PARTICLE_RGB[entityJar.pixieType][2]);
        }
        if (entityJar.ticksExisted % 24000 == 0 && !entityJar.hasProduced && entityJar.hasPixie) {
            entityJar.hasProduced = true;
            if (!level.isClientSide) {
                IceAndFire.sendMSGToAll(new MessageUpdatePixieJar(pos.asLong(), entityJar.hasProduced));
            }
        }
        if (entityJar.hasPixie && entityJar.hasProduced != entityJar.prevHasProduced && entityJar.ticksExisted > 5) {
            if (!level.isClientSide) {
                IceAndFire.sendMSGToAll(new MessageUpdatePixieJar(pos.asLong(), entityJar.hasProduced));
            } else {
                level.playLocalSound((double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, IafSoundRegistry.PIXIE_HURT, SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }
        }
        entityJar.prevRotationYaw = entityJar.rotationYaw;
        if (entityJar.rand.nextInt(30) == 0) {
            entityJar.rotationYaw = entityJar.rand.nextFloat() * 360.0F - 180.0F;
        }
        if (entityJar.hasPixie && entityJar.ticksExisted % 40 == 0 && entityJar.rand.nextInt(6) == 0 && level.isClientSide) {
            level.playLocalSound((double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, IafSoundRegistry.PIXIE_IDLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
        }
        entityJar.prevHasProduced = entityJar.hasProduced;
    }

    public void releasePixie() {
        EntityPixie pixie = new EntityPixie(IafEntityRegistry.PIXIE.get(), this.f_58857_);
        pixie.m_19890_((double) ((float) this.f_58858_.m_123341_() + 0.5F), (double) ((float) this.f_58858_.m_123342_() + 1.0F), (double) ((float) this.f_58858_.m_123343_() + 0.5F), (float) new Random().nextInt(360), 0.0F);
        pixie.m_21008_(InteractionHand.MAIN_HAND, this.pixieItems.get(0));
        pixie.setColor(this.pixieType);
        this.f_58857_.m_7967_(pixie);
        this.hasPixie = false;
        this.pixieType = 0;
        pixie.ticksUntilHouseAI = 500;
        pixie.m_7105_(this.tamedPixie);
        pixie.m_21816_(this.pixieOwnerUUID);
        if (!this.f_58857_.isClientSide) {
            IceAndFire.sendMSGToAll(new MessageUpdatePixieHouse(this.f_58858_.asLong(), false, 0));
        }
    }

    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
        return facing == Direction.DOWN && capability == ForgeCapabilities.ITEM_HANDLER ? this.downHandler.cast() : super.getCapability(capability, facing);
    }
}