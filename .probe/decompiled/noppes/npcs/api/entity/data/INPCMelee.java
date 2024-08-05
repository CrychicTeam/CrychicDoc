package noppes.npcs.api.entity.data;

public interface INPCMelee {

    int getStrength();

    void setStrength(int var1);

    int getDelay();

    void setDelay(int var1);

    int getRange();

    void setRange(int var1);

    int getKnockback();

    void setKnockback(int var1);

    int getEffectType();

    int getEffectTime();

    int getEffectStrength();

    void setEffect(int var1, int var2, int var3);
}