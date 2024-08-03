package snownee.jade.api.view;

import java.util.List;
import snownee.jade.api.Accessor;
import snownee.jade.api.IJadeProvider;

public interface IClientExtensionProvider<IN, OUT> extends IJadeProvider {

    List<ClientViewGroup<OUT>> getClientGroups(Accessor<?> var1, List<ViewGroup<IN>> var2);
}