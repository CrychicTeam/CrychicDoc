package vazkii.patchouli.api;

import java.util.function.UnaryOperator;

public interface IVariablesAvailableCallback {

    void onVariablesAvailable(UnaryOperator<IVariable> var1);
}