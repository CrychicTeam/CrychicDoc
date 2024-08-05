package noppes.npcs.api;

public interface IScoreboard {

    IScoreboardObjective[] getObjectives();

    IScoreboardObjective getObjective(String var1);

    boolean hasObjective(String var1);

    void removeObjective(String var1);

    IScoreboardObjective addObjective(String var1, String var2);

    void setPlayerScore(String var1, String var2, int var3);

    int getPlayerScore(String var1, String var2);

    boolean hasPlayerObjective(String var1, String var2);

    void deletePlayerScore(String var1, String var2);

    IScoreboardTeam[] getTeams();

    boolean hasTeam(String var1);

    IScoreboardTeam addTeam(String var1);

    IScoreboardTeam getTeam(String var1);

    void removeTeam(String var1);

    IScoreboardTeam getPlayerTeam(String var1);

    void removePlayerTeam(String var1);

    String[] getPlayerList();
}