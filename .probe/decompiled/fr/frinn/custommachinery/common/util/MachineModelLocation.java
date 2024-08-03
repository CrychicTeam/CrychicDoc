package fr.frinn.custommachinery.common.util;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.DataResult;
import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.impl.util.IMachineModelLocation;
import net.minecraft.ResourceLocationException;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class MachineModelLocation implements IMachineModelLocation {

    public static final NamedCodec<MachineModelLocation> CODEC = NamedCodec.STRING.comapFlatMap(s -> {
        try {
            return DataResult.success(of(s));
        } catch (ResourceLocationException var2) {
            return DataResult.error(var2::getMessage);
        }
    }, MachineModelLocation::toString, "Model location");

    private final String loc;

    @Nullable
    private final BlockState state;

    @Nullable
    Item item;

    @Nullable
    private final ResourceLocation id;

    @Nullable
    private final String properties;

    public static MachineModelLocation of(String loc) {
        if (loc.contains("#")) {
            return new MachineModelLocation(loc, null, null, new ResourceLocation(loc.substring(0, loc.indexOf("#"))), loc.substring(loc.indexOf("#") + 1));
        } else {
            try {
                return new MachineModelLocation(loc, BlockStateParser.parseForBlock(BuiltInRegistries.BLOCK.m_255303_(), new StringReader(loc), false).blockState(), null, null, null);
            } catch (CommandSyntaxException var2) {
                ResourceLocation resourceLocation = new ResourceLocation(loc);
                return BuiltInRegistries.ITEM.m_7804_(resourceLocation) ? new MachineModelLocation(loc, null, BuiltInRegistries.ITEM.get(resourceLocation), resourceLocation, null) : new MachineModelLocation(loc, null, null, new ResourceLocation(loc), null);
            }
        }
    }

    private MachineModelLocation(String loc, @Nullable BlockState state, @Nullable Item item, @Nullable ResourceLocation id, @Nullable String properties) {
        this.loc = loc;
        this.state = state;
        this.item = item;
        this.id = id;
        this.properties = properties;
    }

    @Nullable
    @Override
    public BlockState getState() {
        return this.state;
    }

    @Nullable
    @Override
    public Item getItem() {
        return this.item;
    }

    @Nullable
    @Override
    public ResourceLocation getLoc() {
        return this.id;
    }

    @Nullable
    @Override
    public String getProperties() {
        return this.properties;
    }

    @Override
    public String toString() {
        return this.loc;
    }
}