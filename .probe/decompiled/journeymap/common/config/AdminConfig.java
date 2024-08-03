package journeymap.common.config;

import java.util.List;

public interface AdminConfig {

    boolean getOpAccess();

    List<String> getAdmins();

    void load();
}