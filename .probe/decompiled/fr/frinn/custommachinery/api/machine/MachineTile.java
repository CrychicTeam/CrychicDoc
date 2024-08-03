package fr.frinn.custommachinery.api.machine;

import fr.frinn.custommachinery.api.component.IMachineComponentManager;
import fr.frinn.custommachinery.api.crafting.IProcessor;
import fr.frinn.custommachinery.api.guielement.IGuiElement;
import fr.frinn.custommachinery.api.upgrade.IMachineUpgradeManager;
import java.util.List;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class MachineTile extends BlockEntity {

    public MachineTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public abstract ICustomMachine getMachine();

    public abstract void refreshMachine(@Nullable ResourceLocation var1);

    public abstract void setPaused(boolean var1);

    public abstract boolean isPaused();

    public abstract boolean isUnloaded();

    public abstract MachineStatus getStatus();

    public abstract Component getMessage();

    public abstract void setStatus(MachineStatus var1, Component var2);

    public void setStatus(MachineStatus status) {
        this.setStatus(status, Component.empty());
    }

    public abstract void resetProcess();

    public abstract void refreshClientData();

    public abstract IMachineComponentManager getComponentManager();

    public abstract IMachineUpgradeManager getUpgradeManager();

    public abstract IProcessor getProcessor();

    public abstract IMachineAppearance getAppearance();

    public abstract void setCustomAppearance(@Nullable IMachineAppearance var1);

    public abstract List<IGuiElement> getGuiElements();

    public abstract void setCustomGuiElements(@Nullable List<IGuiElement> var1);

    public abstract void setOwner(LivingEntity var1);

    @Nullable
    public abstract UUID getOwnerId();

    @Nullable
    public abstract Component getOwnerName();

    public boolean isOwner(LivingEntity entity) {
        return entity.m_20148_().equals(this.getOwnerId());
    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.getOwnerId() != null && this.m_58904_() != null && this.m_58904_().getServer() != null) {
            ServerPlayer player = this.m_58904_().getServer().getPlayerList().getPlayer(this.getOwnerId());
            if (player != null) {
                return player;
            } else {
                for (ServerLevel level : this.m_58904_().getServer().getAllLevels()) {
                    Entity entity = level.getEntity(this.getOwnerId());
                    if (entity instanceof LivingEntity) {
                        return (LivingEntity) entity;
                    }
                }
                return null;
            }
        } else {
            return null;
        }
    }
}