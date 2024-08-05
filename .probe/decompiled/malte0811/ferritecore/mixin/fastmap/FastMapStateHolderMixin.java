package malte0811.ferritecore.mixin.fastmap;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Table;
import java.util.Map;
import malte0811.ferritecore.ducks.FastMapStateHolder;
import malte0811.ferritecore.fastmap.FastMap;
import malte0811.ferritecore.impl.StateHolderImpl;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ StateHolder.class })
public abstract class FastMapStateHolderMixin<O, S> implements FastMapStateHolder<S> {

    @Mutable
    @Shadow
    @Final
    private ImmutableMap<Property<?>, Comparable<?>> values;

    @Shadow
    private Table<Property<?>, Comparable<?>, S> neighbours;

    private int ferritecore_globalTableIndex;

    private FastMap<S> ferritecore_globalTable;

    @Redirect(method = { "setValue", "trySetValue" }, at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Table;get(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", remap = false))
    public Object getNeighborFromFastMap(Table<?, ?, ?> ignore, Object rowKey, Object columnKey) {
        return this.ferritecore_globalTable.withUnsafe(this.ferritecore_globalTableIndex, (Property) rowKey, columnKey);
    }

    @Overwrite
    public void populateNeighbours(Map<Map<Property<?>, Comparable<?>>, S> states) {
        StateHolderImpl.populateNeighbors(states, this);
    }

    @Override
    public FastMap<S> getStateMap() {
        return this.ferritecore_globalTable;
    }

    @Override
    public int getStateIndex() {
        return this.ferritecore_globalTableIndex;
    }

    @Override
    public ImmutableMap<Property<?>, Comparable<?>> getVanillaPropertyMap() {
        return this.values;
    }

    @Override
    public void replacePropertyMap(ImmutableMap<Property<?>, Comparable<?>> newMap) {
        this.values = newMap;
    }

    @Override
    public void setStateMap(FastMap<S> newValue) {
        this.ferritecore_globalTable = newValue;
    }

    @Override
    public void setStateIndex(int newValue) {
        this.ferritecore_globalTableIndex = newValue;
    }

    @Override
    public void setNeighborTable(Table<Property<?>, Comparable<?>, S> table) {
        this.neighbours = table;
    }

    @Override
    public Table<Property<?>, Comparable<?>, S> getNeighborTable() {
        return this.neighbours;
    }
}