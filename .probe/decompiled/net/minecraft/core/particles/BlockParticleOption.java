package net.minecraft.core.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BlockParticleOption implements ParticleOptions {

    public static final ParticleOptions.Deserializer<BlockParticleOption> DESERIALIZER = new ParticleOptions.Deserializer<BlockParticleOption>() {

        public BlockParticleOption fromCommand(ParticleType<BlockParticleOption> p_123645_, StringReader p_123646_) throws CommandSyntaxException {
            p_123646_.expect(' ');
            return new BlockParticleOption(p_123645_, BlockStateParser.parseForBlock(BuiltInRegistries.BLOCK.m_255303_(), p_123646_, false).blockState());
        }

        public BlockParticleOption fromNetwork(ParticleType<BlockParticleOption> p_123648_, FriendlyByteBuf p_123649_) {
            return new BlockParticleOption(p_123648_, p_123649_.readById(Block.BLOCK_STATE_REGISTRY));
        }
    };

    private final ParticleType<BlockParticleOption> type;

    private final BlockState state;

    public static Codec<BlockParticleOption> codec(ParticleType<BlockParticleOption> particleTypeBlockParticleOption0) {
        return BlockState.CODEC.xmap(p_123638_ -> new BlockParticleOption(particleTypeBlockParticleOption0, p_123638_), p_123633_ -> p_123633_.state);
    }

    public BlockParticleOption(ParticleType<BlockParticleOption> particleTypeBlockParticleOption0, BlockState blockState1) {
        this.type = particleTypeBlockParticleOption0;
        this.state = blockState1;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeId(Block.BLOCK_STATE_REGISTRY, this.state);
    }

    @Override
    public String writeToString() {
        return BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()) + " " + BlockStateParser.serialize(this.state);
    }

    @Override
    public ParticleType<BlockParticleOption> getType() {
        return this.type;
    }

    public BlockState getState() {
        return this.state;
    }
}