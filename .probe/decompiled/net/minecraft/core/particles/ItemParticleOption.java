package net.minecraft.core.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.commands.arguments.item.ItemParser;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public class ItemParticleOption implements ParticleOptions {

    public static final ParticleOptions.Deserializer<ItemParticleOption> DESERIALIZER = new ParticleOptions.Deserializer<ItemParticleOption>() {

        public ItemParticleOption fromCommand(ParticleType<ItemParticleOption> p_123721_, StringReader p_123722_) throws CommandSyntaxException {
            p_123722_.expect(' ');
            ItemParser.ItemResult $$2 = ItemParser.parseForItem(BuiltInRegistries.ITEM.m_255303_(), p_123722_);
            ItemStack $$3 = new ItemInput($$2.item(), $$2.nbt()).createItemStack(1, false);
            return new ItemParticleOption(p_123721_, $$3);
        }

        public ItemParticleOption fromNetwork(ParticleType<ItemParticleOption> p_123724_, FriendlyByteBuf p_123725_) {
            return new ItemParticleOption(p_123724_, p_123725_.readItem());
        }
    };

    private final ParticleType<ItemParticleOption> type;

    private final ItemStack itemStack;

    public static Codec<ItemParticleOption> codec(ParticleType<ItemParticleOption> particleTypeItemParticleOption0) {
        return ItemStack.CODEC.xmap(p_123714_ -> new ItemParticleOption(particleTypeItemParticleOption0, p_123714_), p_123709_ -> p_123709_.itemStack);
    }

    public ItemParticleOption(ParticleType<ItemParticleOption> particleTypeItemParticleOption0, ItemStack itemStack1) {
        this.type = particleTypeItemParticleOption0;
        this.itemStack = itemStack1;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeItem(this.itemStack);
    }

    @Override
    public String writeToString() {
        return BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()) + " " + new ItemInput(this.itemStack.getItemHolder(), this.itemStack.getTag()).serialize();
    }

    @Override
    public ParticleType<ItemParticleOption> getType() {
        return this.type;
    }

    public ItemStack getItem() {
        return this.itemStack;
    }
}