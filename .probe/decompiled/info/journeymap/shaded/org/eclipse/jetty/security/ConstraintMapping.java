package info.journeymap.shaded.org.eclipse.jetty.security;

import info.journeymap.shaded.org.eclipse.jetty.util.security.Constraint;

public class ConstraintMapping {

    String _method;

    String[] _methodOmissions;

    String _pathSpec;

    Constraint _constraint;

    public Constraint getConstraint() {
        return this._constraint;
    }

    public void setConstraint(Constraint constraint) {
        this._constraint = constraint;
    }

    public String getMethod() {
        return this._method;
    }

    public void setMethod(String method) {
        this._method = method;
    }

    public String getPathSpec() {
        return this._pathSpec;
    }

    public void setPathSpec(String pathSpec) {
        this._pathSpec = pathSpec;
    }

    public void setMethodOmissions(String[] omissions) {
        this._methodOmissions = omissions;
    }

    public String[] getMethodOmissions() {
        return this._methodOmissions;
    }
}