package net.mehvahdjukaar.supplementaries.integration.forge;

import dan200.computercraft.api.ForgeComputerCraftAPI;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.shared.Capabilities;
import dan200.computercraft.shared.media.items.PrintoutItem;
import java.util.Objects;
import net.mehvahdjukaar.supplementaries.common.block.tiles.SpeakerBlockTile;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CCCompatImpl {

    public static void setup() {
        ForgeComputerCraftAPI.registerPeripheralProvider((level, blockPos, direction) -> {
            BlockEntity tile = level.getBlockEntity(blockPos);
            return tile instanceof SpeakerBlockTile ? tile.getCapability(Capabilities.CAPABILITY_PERIPHERAL, direction) : LazyOptional.empty();
        });
    }

    public static int getPages(ItemStack itemstack) {
        return PrintoutItem.getPageCount(itemstack);
    }

    public static String[] getText(ItemStack itemstack) {
        return PrintoutItem.getText(itemstack);
    }

    public static boolean isPrintedBook(Item item) {
        return item instanceof PrintoutItem;
    }

    public static boolean isPeripheralCap(Capability<?> cap) {
        return cap == Capabilities.CAPABILITY_PERIPHERAL;
    }

    public static LazyOptional<Object> getPeripheralSupplier(SpeakerBlockTile tile) {
        return LazyOptional.of(() -> new CCCompatImpl.SpeakerPeripheral(tile));
    }

    public static final class SpeakerPeripheral implements IPeripheral {

        private final SpeakerBlockTile tile;

        public SpeakerPeripheral(SpeakerBlockTile tile) {
            this.tile = tile;
        }

        @LuaFunction
        public void setNarrator(SpeakerBlockTile.Mode mode) {
            this.tile.setMode(mode);
            this.tile.m_6596_();
        }

        @LuaFunction
        public SpeakerBlockTile.Mode getMode() {
            return this.tile.getMode();
        }

        @LuaFunction
        public void setMessage(String message) {
            this.tile.setMessage(Component.literal(message));
            this.tile.m_6596_();
        }

        @LuaFunction
        public String getMessage() {
            return this.tile.getMessage(false).getString();
        }

        @LuaFunction
        public void setName(String name) {
            this.tile.setCustomName(Component.literal(name));
            this.tile.m_6596_();
        }

        @LuaFunction
        public String getName() {
            return this.tile.getName().getString();
        }

        @LuaFunction
        public double getVolume() {
            return this.tile.getVolume();
        }

        @LuaFunction
        public void setVolume(double volume) {
            this.tile.setVolume(volume);
            this.tile.m_6596_();
        }

        @LuaFunction
        public void activate() {
            this.tile.sendMessage();
        }

        @NotNull
        public String getType() {
            return "speaker_block";
        }

        public boolean equals(@Nullable IPeripheral other) {
            return Objects.equals(this, other);
        }

        public SpeakerBlockTile tile() {
            return this.tile;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else if (obj != null && obj.getClass() == this.getClass()) {
                CCCompatImpl.SpeakerPeripheral that = (CCCompatImpl.SpeakerPeripheral) obj;
                return Objects.equals(this.tile, that.tile);
            } else {
                return false;
            }
        }

        public int hashCode() {
            return Objects.hash(new Object[] { this.tile });
        }

        public String toString() {
            return "SpeakerPeripheral[tile=" + this.tile + "]";
        }
    }
}