package info.journeymap.shaded.org.eclipse.jetty.util.security;

import java.io.Serializable;
import java.util.Arrays;

public class Constraint implements Cloneable, Serializable {

    public static final String __BASIC_AUTH = "BASIC";

    public static final String __FORM_AUTH = "FORM";

    public static final String __DIGEST_AUTH = "DIGEST";

    public static final String __CERT_AUTH = "CLIENT_CERT";

    public static final String __CERT_AUTH2 = "CLIENT-CERT";

    public static final String __SPNEGO_AUTH = "SPNEGO";

    public static final String __NEGOTIATE_AUTH = "NEGOTIATE";

    public static final int DC_UNSET = -1;

    public static final int DC_NONE = 0;

    public static final int DC_INTEGRAL = 1;

    public static final int DC_CONFIDENTIAL = 2;

    public static final int DC_FORBIDDEN = 3;

    public static final String NONE = "NONE";

    public static final String ANY_ROLE = "*";

    public static final String ANY_AUTH = "**";

    private String _name;

    private String[] _roles;

    private int _dataConstraint = -1;

    private boolean _anyRole = false;

    private boolean _anyAuth = false;

    private boolean _authenticate = false;

    public static boolean validateMethod(String method) {
        if (method == null) {
            return false;
        } else {
            method = method.trim();
            return method.equals("FORM") || method.equals("BASIC") || method.equals("DIGEST") || method.equals("CLIENT_CERT") || method.equals("CLIENT-CERT") || method.equals("SPNEGO") || method.equals("NEGOTIATE");
        }
    }

    public Constraint() {
    }

    public Constraint(String name, String role) {
        this.setName(name);
        this.setRoles(new String[] { role });
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getName() {
        return this._name;
    }

    public void setRoles(String[] roles) {
        this._roles = roles;
        this._anyRole = false;
        this._anyAuth = false;
        if (roles != null) {
            for (int i = roles.length; i-- > 0; this._anyAuth = this._anyAuth | "**".equals(roles[i])) {
                this._anyRole = this._anyRole | "*".equals(roles[i]);
            }
        }
    }

    public boolean isAnyRole() {
        return this._anyRole;
    }

    public boolean isAnyAuth() {
        return this._anyAuth;
    }

    public String[] getRoles() {
        return this._roles;
    }

    public boolean hasRole(String role) {
        if (this._anyRole) {
            return true;
        } else {
            if (this._roles != null) {
                int i = this._roles.length;
                while (i-- > 0) {
                    if (role.equals(this._roles[i])) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public void setAuthenticate(boolean authenticate) {
        this._authenticate = authenticate;
    }

    public boolean getAuthenticate() {
        return this._authenticate;
    }

    public boolean isForbidden() {
        return this._authenticate && !this._anyRole && (this._roles == null || this._roles.length == 0);
    }

    public void setDataConstraint(int c) {
        if (c >= 0 && c <= 2) {
            this._dataConstraint = c;
        } else {
            throw new IllegalArgumentException("Constraint out of range");
        }
    }

    public int getDataConstraint() {
        return this._dataConstraint;
    }

    public boolean hasDataConstraint() {
        return this._dataConstraint >= 0;
    }

    public String toString() {
        return "SC{" + this._name + "," + (this._anyRole ? "*" : (this._roles == null ? "-" : Arrays.asList(this._roles).toString())) + "," + (this._dataConstraint == -1 ? "DC_UNSET}" : (this._dataConstraint == 0 ? "NONE}" : (this._dataConstraint == 1 ? "INTEGRAL}" : "CONFIDENTIAL}")));
    }
}