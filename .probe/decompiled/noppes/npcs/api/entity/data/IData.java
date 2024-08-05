package noppes.npcs.api.entity.data;

public interface IData {

    void put(String var1, Object var2);

    Object get(String var1);

    void remove(String var1);

    boolean has(String var1);

    String[] getKeys();

    void clear();
}