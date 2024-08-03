package noppes.npcs.api;

public interface IScoreboardObjective {

    String getName();

    String getDisplayName();

    void setDisplayName(String var1);

    String getCriteria();

    boolean isReadyOnly();

    IScoreboardScore[] getScores();

    IScoreboardScore getScore(String var1);

    boolean hasScore(String var1);

    IScoreboardScore createScore(String var1);

    void removeScore(String var1);
}