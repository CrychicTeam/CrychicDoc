package dev.xkmc.modulargolems.content.capability;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import dev.xkmc.modulargolems.compat.curio.CurioCompatRegistry;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.item.card.PathRecordCard;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;

@SerialClass
public class PathConfig {

    @SerialField
    protected final ArrayList<ItemStack> path = new ArrayList();

    @Nullable
    public static List<PathRecordCard.Pos> getPath(AbstractGolemEntity<?, ?> e) {
        if (ModList.get().isLoaded("curios")) {
            Optional<List<PathRecordCard.Pos>> opt = CurioCompatRegistry.getItem(e, "golem_route").map(PathRecordCard::getList);
            if (opt.isPresent()) {
                return (List<PathRecordCard.Pos>) opt.get();
            }
        }
        GolemConfigEntry config = e.getConfigEntry(null);
        return config != null ? config.pathConfig.getList() : null;
    }

    private List<PathRecordCard.Pos> getList() {
        for (ItemStack e : this.path) {
            if (e.getItem() instanceof PathRecordCard) {
                return PathRecordCard.getList(e);
            }
        }
        return List.of();
    }
}