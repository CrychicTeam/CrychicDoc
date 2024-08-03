package snownee.lychee.core.post;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;
import snownee.lychee.PostActionTypes;
import snownee.lychee.core.def.BlockPredicateHelper;
import snownee.lychee.util.CommonProxy;

public class CycleStateProperty extends PlaceBlock {

    public final Property<?> property;

    public CycleStateProperty(BlockPredicate block, BlockPos offset, Property<?> property) {
        super(block, offset);
        this.property = property;
    }

    @Override
    public PostActionType<?> getType() {
        return PostActionTypes.CYCLE_STATE_PROPERTY;
    }

    @Nullable
    @Override
    protected BlockState getNewState(BlockState oldState) {
        try {
            return (BlockState) oldState.m_61122_(this.property);
        } catch (Throwable var3) {
            return null;
        }
    }

    public static Property<?> findProperty(BlockPredicate blockPredicate, String name) {
        BlockState block = BlockPredicateHelper.anyBlockState(blockPredicate);
        for (Property<?> property : block.m_61147_()) {
            if (name.equals(property.getName())) {
                return property;
            }
        }
        throw new IllegalArgumentException("Unknown property name: " + name);
    }

    public static class Type extends PostActionType<CycleStateProperty> {

        public CycleStateProperty fromJson(JsonObject o) {
            BlockPos offset = CommonProxy.parseOffset(o);
            BlockPredicate block = BlockPredicateHelper.fromJson(o.get("block"));
            return new CycleStateProperty(block, offset, CycleStateProperty.findProperty(block, GsonHelper.getAsString(o, "property")));
        }

        public void toJson(CycleStateProperty action, JsonObject o) {
            PostActionTypes.PLACE.toJson(action, o);
            o.addProperty("property", action.property.getName());
        }

        public CycleStateProperty fromNetwork(FriendlyByteBuf buf) {
            BlockPredicate block = BlockPredicateHelper.fromNetwork(buf);
            return new CycleStateProperty(block, buf.readBlockPos(), CycleStateProperty.findProperty(block, buf.readUtf()));
        }

        public void toNetwork(CycleStateProperty action, FriendlyByteBuf buf) {
            PostActionTypes.PLACE.toNetwork(action, buf);
            buf.writeUtf(action.property.getName());
        }
    }
}