package vazkii.patchouli.api;

public interface IVariableProvider {

    IVariable get(String var1);

    boolean has(String var1);
}