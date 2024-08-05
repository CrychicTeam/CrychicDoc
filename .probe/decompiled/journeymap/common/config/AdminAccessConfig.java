package journeymap.common.config;

import java.util.List;

public class AdminAccessConfig implements AdminConfig {

    private AdminConfig adminConfig;

    private static AdminAccessConfig instance;

    public AdminAccessConfig() {
        instance = this;
    }

    public static AdminAccessConfig getInstance() {
        if (instance == null) {
            instance = new AdminAccessConfig();
        }
        return instance;
    }

    @Override
    public boolean getOpAccess() {
        return this.adminConfig.getOpAccess();
    }

    @Override
    public List<String> getAdmins() {
        return this.adminConfig.getAdmins();
    }

    public void load(AdminConfig adminConfig) {
        this.adminConfig = adminConfig;
        this.load();
    }

    @Override
    public void load() {
        this.adminConfig.load();
    }
}