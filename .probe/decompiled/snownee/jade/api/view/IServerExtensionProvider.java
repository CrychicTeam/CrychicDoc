package snownee.jade.api.view;

import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.IJadeProvider;

public interface IServerExtensionProvider<IN, OUT> extends IJadeProvider {

    @Nullable
    List<ViewGroup<OUT>> getGroups(ServerPlayer var1, ServerLevel var2, IN var3, boolean var4);
}