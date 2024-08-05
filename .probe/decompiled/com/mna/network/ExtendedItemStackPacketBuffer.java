package com.mna.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.EncoderException;
import java.io.IOException;
import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ExtendedItemStackPacketBuffer extends FriendlyByteBuf {

    public ExtendedItemStackPacketBuffer(ByteBuf buffer) {
        super(buffer);
    }

    public void writeExtendedItemStack(ItemStack stack) {
        if (stack.isEmpty()) {
            this.writeInt(-1);
        } else {
            this.writeInt(Item.getId(stack.getItem()));
            this.writeInt(stack.getCount());
            CompoundTag nbttagcompound = null;
            if (stack.getItem().getShareTag(stack) != null) {
                nbttagcompound = stack.getItem().getShareTag(stack);
            }
            this.m_130079_(nbttagcompound);
        }
    }

    public ItemStack readExtendedItemStack() {
        int itemID = this.readInt();
        if (itemID < 0) {
            return ItemStack.EMPTY;
        } else {
            int count = this.readInt();
            ItemStack stack = new ItemStack(Item.byId(itemID), count);
            stack.setTag(this.m_130260_());
            return stack;
        }
    }

    public CompoundTag readNBT() {
        int i = this.readerIndex();
        byte b0 = this.readByte();
        if (b0 == 0) {
            return null;
        } else {
            this.readerIndex(i);
            try {
                return NbtIo.read(new ByteBufInputStream(this.copy()), new NbtAccounter(2097152L));
            } catch (IOException var4) {
                throw new EncoderException(var4);
            }
        }
    }

    public void writeNBT(@Nullable CompoundTag nbt) {
        if (nbt == null) {
            this.writeByte(0);
        } else {
            try {
                NbtIo.write(nbt, new ByteBufOutputStream(this));
            } catch (IOException var3) {
                throw new EncoderException(var3);
            }
        }
    }

    public <T, C extends Collection<T>> C readExtendedCollection(IntFunction<C> intFunctionC0, Function<ExtendedItemStackPacketBuffer, T> functionExtendedItemStackPacketBufferT1) {
        int i = this.m_130242_();
        C c = (C) intFunctionC0.apply(i);
        for (int j = 0; j < i; j++) {
            c.add(functionExtendedItemStackPacketBufferT1.apply(this));
        }
        return c;
    }

    public <T> void writeExtendedCollection(Collection<T> collectionT0, BiConsumer<ExtendedItemStackPacketBuffer, T> biConsumerExtendedItemStackPacketBufferT1) {
        this.m_130130_(collectionT0.size());
        for (T t : collectionT0) {
            biConsumerExtendedItemStackPacketBufferT1.accept(this, t);
        }
    }
}