package pie.ilikepiefoo.compat.jade.impl;

import dev.latvian.mods.kubejs.util.ConsoleJS;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import pie.ilikepiefoo.compat.jade.builder.ClientExtensionProviderBuilder;
import pie.ilikepiefoo.compat.jade.builder.ViewGroupBuilder;
import pie.ilikepiefoo.compat.jade.builder.callback.GetClientGroupsCallbackJS;
import snownee.jade.api.Accessor;
import snownee.jade.api.view.ClientViewGroup;
import snownee.jade.api.view.IClientExtensionProvider;
import snownee.jade.api.view.ViewGroup;

public class CustomClientExtensionProvider<IN, OUT> extends CustomJadeProvider<ClientExtensionProviderBuilder<IN, OUT>> implements IClientExtensionProvider<IN, OUT> {

    public CustomClientExtensionProvider(ClientExtensionProviderBuilder<IN, OUT> builder) {
        super(builder);
    }

    @Override
    public List<ClientViewGroup<OUT>> getClientGroups(Accessor<?> accessor, List<ViewGroup<IN>> groups) {
        GetClientGroupsCallbackJS<IN, OUT> callback = new GetClientGroupsCallbackJS<>(accessor, groups);
        try {
            this.builder.getCallback().accept(callback);
        } catch (Throwable var5) {
            ConsoleJS.CLIENT.error("Error while executing client extension provider callback", var5);
            return null;
        }
        return callback.getResultingGroups() != null && !callback.getResultingGroups().isEmpty() ? (List) callback.getResultingGroups().stream().map(ViewGroupBuilder::buildClient).filter(Objects::nonNull).collect(Collectors.toList()) : null;
    }
}