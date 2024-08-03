package info.journeymap.shaded.org.javax.servlet;

public class HttpMethodConstraintElement extends HttpConstraintElement {

    private String methodName;

    public HttpMethodConstraintElement(String methodName) {
        if (methodName != null && methodName.length() != 0) {
            this.methodName = methodName;
        } else {
            throw new IllegalArgumentException("invalid HTTP method name");
        }
    }

    public HttpMethodConstraintElement(String methodName, HttpConstraintElement constraint) {
        super(constraint.getEmptyRoleSemantic(), constraint.getTransportGuarantee(), constraint.getRolesAllowed());
        if (methodName != null && methodName.length() != 0) {
            this.methodName = methodName;
        } else {
            throw new IllegalArgumentException("invalid HTTP method name");
        }
    }

    public String getMethodName() {
        return this.methodName;
    }
}