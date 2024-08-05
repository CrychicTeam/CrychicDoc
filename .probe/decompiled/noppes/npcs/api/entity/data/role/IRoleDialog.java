package noppes.npcs.api.entity.data.role;

public interface IRoleDialog {

    String getDialog();

    void setDialog(String var1);

    String getOption(int var1);

    void setOption(int var1, String var2);

    String getOptionDialog(int var1);

    void setOptionDialog(int var1, String var2);
}