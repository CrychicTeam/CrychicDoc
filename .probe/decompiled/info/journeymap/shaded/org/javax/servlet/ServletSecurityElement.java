package info.journeymap.shaded.org.javax.servlet;

import info.journeymap.shaded.org.javax.servlet.annotation.HttpMethodConstraint;
import info.journeymap.shaded.org.javax.servlet.annotation.ServletSecurity;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class ServletSecurityElement extends HttpConstraintElement {

    private Collection<String> methodNames;

    private Collection<HttpMethodConstraintElement> methodConstraints;

    public ServletSecurityElement() {
        this.methodConstraints = new HashSet();
        this.methodNames = Collections.emptySet();
    }

    public ServletSecurityElement(HttpConstraintElement constraint) {
        super(constraint.getEmptyRoleSemantic(), constraint.getTransportGuarantee(), constraint.getRolesAllowed());
        this.methodConstraints = new HashSet();
        this.methodNames = Collections.emptySet();
    }

    public ServletSecurityElement(Collection<HttpMethodConstraintElement> methodConstraints) {
        this.methodConstraints = (Collection<HttpMethodConstraintElement>) (methodConstraints == null ? new HashSet() : methodConstraints);
        this.methodNames = this.checkMethodNames(this.methodConstraints);
    }

    public ServletSecurityElement(HttpConstraintElement constraint, Collection<HttpMethodConstraintElement> methodConstraints) {
        super(constraint.getEmptyRoleSemantic(), constraint.getTransportGuarantee(), constraint.getRolesAllowed());
        this.methodConstraints = (Collection<HttpMethodConstraintElement>) (methodConstraints == null ? new HashSet() : methodConstraints);
        this.methodNames = this.checkMethodNames(this.methodConstraints);
    }

    public ServletSecurityElement(ServletSecurity annotation) {
        super(annotation.value().value(), annotation.value().transportGuarantee(), annotation.value().rolesAllowed());
        this.methodConstraints = new HashSet();
        for (HttpMethodConstraint constraint : annotation.httpMethodConstraints()) {
            this.methodConstraints.add(new HttpMethodConstraintElement(constraint.value(), new HttpConstraintElement(constraint.emptyRoleSemantic(), constraint.transportGuarantee(), constraint.rolesAllowed())));
        }
        this.methodNames = this.checkMethodNames(this.methodConstraints);
    }

    public Collection<HttpMethodConstraintElement> getHttpMethodConstraints() {
        return Collections.unmodifiableCollection(this.methodConstraints);
    }

    public Collection<String> getMethodNames() {
        return Collections.unmodifiableCollection(this.methodNames);
    }

    private Collection<String> checkMethodNames(Collection<HttpMethodConstraintElement> methodConstraints) {
        Collection<String> methodNames = new HashSet();
        for (HttpMethodConstraintElement methodConstraint : methodConstraints) {
            String methodName = methodConstraint.getMethodName();
            if (!methodNames.add(methodName)) {
                throw new IllegalArgumentException("Duplicate HTTP method name: " + methodName);
            }
        }
        return methodNames;
    }
}