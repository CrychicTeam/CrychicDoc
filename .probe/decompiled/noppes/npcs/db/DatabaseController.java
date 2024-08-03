package noppes.npcs.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import noppes.npcs.CustomNpcs;
import noppes.npcs.shared.common.util.LogWriter;

public class DatabaseController {

    private static final String insertChangelogQuery = "INSERT INTO changelogs (id, query, date) VALUES (?, ?, CURRENT_TIMESTAMP())";

    private static final String[] changelogs = new String[] { "CREATE TABLE quests (id IDENTITY PRIMARY KEY, title VARCHAR(255), type SMALLINT, repeat_type SMALLINT, completion_type SMALLINT, category VARCHAR(255), log_text TEXT, complete_text TEXT, complete_npc VARCHAR(255), next_quest_id INT, command TEXT, mail_data JSON, quest_data JSON, reward_exp INT, reward_items JSON, reward_randomized BOOLEAN, faction_options JSON)" };

    public static void init() {
        try {
            Connection conn = create();
            try {
                Statement state = conn.createStatement();
                state.executeUpdate("CREATE TABLE IF NOT EXISTS changelogs (id INT PRIMARY KEY, query TEXT, date TIMESTAMP, CONSTRAINT PK_CHANGELOGS PRIMARY KEY (ID))");
                for (int i = 0; i < changelogs.length; i++) {
                    if (!state.executeQuery("SELECT 1 FROM changelogs WHERE id = " + i).next()) {
                        state.executeUpdate(changelogs[i]);
                        PreparedStatement insertChangelog = conn.prepareStatement("INSERT INTO changelogs (id, query, date) VALUES (?, ?, CURRENT_TIMESTAMP())");
                        insertChangelog.setInt(1, i);
                        insertChangelog.setString(2, changelogs[i]);
                        insertChangelog.executeUpdate();
                    }
                }
                state.close();
            } catch (Throwable var5) {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (Throwable var4) {
                        var5.addSuppressed(var4);
                    }
                }
                throw var5;
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException var6) {
            LogWriter.except(var6);
        }
    }

    public static Connection create() throws SQLException {
        File f = new File(CustomNpcs.getLevelSaveDirectory(), "database");
        return DriverManager.getConnection("jdbc:h2:" + f.getAbsolutePath() + ";DB_CLOSE_ON_EXIT=FALSE;AUTO_SERVER=TRUE");
    }
}