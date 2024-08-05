package noppes.npcs.api.item;

public interface IItemBook extends IItemStack {

    String[] getText();

    void setText(String[] var1);

    String getAuthor();

    void setAuthor(String var1);

    String getTitle();

    void setTitle(String var1);
}