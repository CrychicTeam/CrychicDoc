package info.journeymap.shaded.org.eclipse.jetty.security;

public class RoleRunAsToken implements RunAsToken {

    private final String _runAsRole;

    public RoleRunAsToken(String runAsRole) {
        this._runAsRole = runAsRole;
    }

    public String getRunAsRole() {
        return this._runAsRole;
    }

    public String toString() {
        return "RoleRunAsToken(" + this._runAsRole + ")";
    }
}