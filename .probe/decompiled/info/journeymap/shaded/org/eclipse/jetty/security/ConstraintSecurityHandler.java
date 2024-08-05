package info.journeymap.shaded.org.eclipse.jetty.security;

import info.journeymap.shaded.org.eclipse.jetty.http.PathMap;
import info.journeymap.shaded.org.eclipse.jetty.server.HttpConfiguration;
import info.journeymap.shaded.org.eclipse.jetty.server.Request;
import info.journeymap.shaded.org.eclipse.jetty.server.Response;
import info.journeymap.shaded.org.eclipse.jetty.server.UserIdentity;
import info.journeymap.shaded.org.eclipse.jetty.server.handler.ContextHandler;
import info.journeymap.shaded.org.eclipse.jetty.util.URIUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.util.security.Constraint;
import info.journeymap.shaded.org.javax.servlet.HttpConstraintElement;
import info.journeymap.shaded.org.javax.servlet.HttpMethodConstraintElement;
import info.journeymap.shaded.org.javax.servlet.ServletSecurityElement;
import info.journeymap.shaded.org.javax.servlet.annotation.ServletSecurity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

public class ConstraintSecurityHandler extends SecurityHandler implements ConstraintAware {

    private static final Logger LOG = Log.getLogger(SecurityHandler.class);

    private static final String OMISSION_SUFFIX = ".omission";

    private static final String ALL_METHODS = "*";

    private final List<ConstraintMapping> _constraintMappings = new CopyOnWriteArrayList();

    private final Set<String> _roles = new CopyOnWriteArraySet();

    private final PathMap<Map<String, RoleInfo>> _constraintMap = new PathMap<>();

    private boolean _denyUncoveredMethods = false;

    public static Constraint createConstraint() {
        return new Constraint();
    }

    public static Constraint createConstraint(Constraint constraint) {
        try {
            return (Constraint) constraint.clone();
        } catch (CloneNotSupportedException var2) {
            throw new IllegalStateException(var2);
        }
    }

    public static Constraint createConstraint(String name, boolean authenticate, String[] roles, int dataConstraint) {
        Constraint constraint = createConstraint();
        if (name != null) {
            constraint.setName(name);
        }
        constraint.setAuthenticate(authenticate);
        constraint.setRoles(roles);
        constraint.setDataConstraint(dataConstraint);
        return constraint;
    }

    public static Constraint createConstraint(String name, HttpConstraintElement element) {
        return createConstraint(name, element.getRolesAllowed(), element.getEmptyRoleSemantic(), element.getTransportGuarantee());
    }

    public static Constraint createConstraint(String name, String[] rolesAllowed, ServletSecurity.EmptyRoleSemantic permitOrDeny, ServletSecurity.TransportGuarantee transport) {
        Constraint constraint = createConstraint();
        if (rolesAllowed != null && rolesAllowed.length != 0) {
            constraint.setAuthenticate(true);
            constraint.setRoles(rolesAllowed);
            constraint.setName(name + "-RolesAllowed");
        } else if (permitOrDeny.equals(ServletSecurity.EmptyRoleSemantic.DENY)) {
            constraint.setName(name + "-Deny");
            constraint.setAuthenticate(true);
        } else {
            constraint.setName(name + "-Permit");
            constraint.setAuthenticate(false);
        }
        constraint.setDataConstraint(transport.equals(ServletSecurity.TransportGuarantee.CONFIDENTIAL) ? 2 : 0);
        return constraint;
    }

    public static List<ConstraintMapping> getConstraintMappingsForPath(String pathSpec, List<ConstraintMapping> constraintMappings) {
        if (pathSpec != null && !"".equals(pathSpec.trim()) && constraintMappings != null && constraintMappings.size() != 0) {
            List<ConstraintMapping> mappings = new ArrayList();
            for (ConstraintMapping mapping : constraintMappings) {
                if (pathSpec.equals(mapping.getPathSpec())) {
                    mappings.add(mapping);
                }
            }
            return mappings;
        } else {
            return Collections.emptyList();
        }
    }

    public static List<ConstraintMapping> removeConstraintMappingsForPath(String pathSpec, List<ConstraintMapping> constraintMappings) {
        if (pathSpec != null && !"".equals(pathSpec.trim()) && constraintMappings != null && constraintMappings.size() != 0) {
            List<ConstraintMapping> mappings = new ArrayList();
            for (ConstraintMapping mapping : constraintMappings) {
                if (!pathSpec.equals(mapping.getPathSpec())) {
                    mappings.add(mapping);
                }
            }
            return mappings;
        } else {
            return Collections.emptyList();
        }
    }

    public static List<ConstraintMapping> createConstraintsWithMappingsForPath(String name, String pathSpec, ServletSecurityElement securityElement) {
        List<ConstraintMapping> mappings = new ArrayList();
        Constraint httpConstraint = null;
        ConstraintMapping httpConstraintMapping = null;
        if (securityElement.getEmptyRoleSemantic() != ServletSecurity.EmptyRoleSemantic.PERMIT || securityElement.getRolesAllowed().length != 0 || securityElement.getTransportGuarantee() != ServletSecurity.TransportGuarantee.NONE) {
            httpConstraint = createConstraint(name, securityElement);
            httpConstraintMapping = new ConstraintMapping();
            httpConstraintMapping.setPathSpec(pathSpec);
            httpConstraintMapping.setConstraint(httpConstraint);
            mappings.add(httpConstraintMapping);
        }
        List<String> methodOmissions = new ArrayList();
        Collection<HttpMethodConstraintElement> methodConstraintElements = securityElement.getHttpMethodConstraints();
        if (methodConstraintElements != null) {
            for (HttpMethodConstraintElement methodConstraintElement : methodConstraintElements) {
                Constraint methodConstraint = createConstraint(name, methodConstraintElement);
                ConstraintMapping mapping = new ConstraintMapping();
                mapping.setConstraint(methodConstraint);
                mapping.setPathSpec(pathSpec);
                if (methodConstraintElement.getMethodName() != null) {
                    mapping.setMethod(methodConstraintElement.getMethodName());
                    methodOmissions.add(methodConstraintElement.getMethodName());
                }
                mappings.add(mapping);
            }
        }
        if (methodOmissions.size() > 0 && httpConstraintMapping != null) {
            httpConstraintMapping.setMethodOmissions((String[]) methodOmissions.toArray(new String[methodOmissions.size()]));
        }
        return mappings;
    }

    @Override
    public List<ConstraintMapping> getConstraintMappings() {
        return this._constraintMappings;
    }

    @Override
    public Set<String> getRoles() {
        return this._roles;
    }

    public void setConstraintMappings(List<ConstraintMapping> constraintMappings) {
        this.setConstraintMappings(constraintMappings, null);
    }

    public void setConstraintMappings(ConstraintMapping[] constraintMappings) {
        this.setConstraintMappings(Arrays.asList(constraintMappings), null);
    }

    @Override
    public void setConstraintMappings(List<ConstraintMapping> constraintMappings, Set<String> roles) {
        this._constraintMappings.clear();
        this._constraintMappings.addAll(constraintMappings);
        if (roles == null) {
            roles = new HashSet();
            for (ConstraintMapping cm : constraintMappings) {
                String[] cmr = cm.getConstraint().getRoles();
                if (cmr != null) {
                    for (String r : cmr) {
                        if (!"*".equals(r)) {
                            roles.add(r);
                        }
                    }
                }
            }
        }
        this.setRoles(roles);
        if (this.isStarted()) {
            for (ConstraintMapping mapping : this._constraintMappings) {
                this.processConstraintMapping(mapping);
            }
        }
    }

    public void setRoles(Set<String> roles) {
        this._roles.clear();
        this._roles.addAll(roles);
    }

    @Override
    public void addConstraintMapping(ConstraintMapping mapping) {
        this._constraintMappings.add(mapping);
        if (mapping.getConstraint() != null && mapping.getConstraint().getRoles() != null) {
            for (String role : mapping.getConstraint().getRoles()) {
                if (!"*".equals(role) && !"**".equals(role)) {
                    this.addRole(role);
                }
            }
        }
        if (this.isStarted()) {
            this.processConstraintMapping(mapping);
        }
    }

    @Override
    public void addRole(String role) {
        boolean modified = this._roles.add(role);
        if (this.isStarted() && modified) {
            for (Map<String, RoleInfo> map : this._constraintMap.values()) {
                for (RoleInfo info : map.values()) {
                    if (info.isAnyRole()) {
                        info.addRole(role);
                    }
                }
            }
        }
    }

    @Override
    protected void doStart() throws Exception {
        this._constraintMap.clear();
        if (this._constraintMappings != null) {
            for (ConstraintMapping mapping : this._constraintMappings) {
                this.processConstraintMapping(mapping);
            }
        }
        this.checkPathsWithUncoveredHttpMethods();
        super.doStart();
    }

    @Override
    protected void doStop() throws Exception {
        super.doStop();
        this._constraintMap.clear();
    }

    protected void processConstraintMapping(ConstraintMapping mapping) {
        Map<String, RoleInfo> mappings = (Map<String, RoleInfo>) this._constraintMap.get(mapping.getPathSpec());
        if (mappings == null) {
            mappings = new HashMap();
            this._constraintMap.put(mapping.getPathSpec(), mappings);
        }
        RoleInfo allMethodsRoleInfo = (RoleInfo) mappings.get("*");
        if (allMethodsRoleInfo == null || !allMethodsRoleInfo.isForbidden()) {
            if (mapping.getMethodOmissions() != null && mapping.getMethodOmissions().length > 0) {
                this.processConstraintMappingWithMethodOmissions(mapping, mappings);
            } else {
                String httpMethod = mapping.getMethod();
                if (httpMethod == null) {
                    httpMethod = "*";
                }
                RoleInfo roleInfo = (RoleInfo) mappings.get(httpMethod);
                if (roleInfo == null) {
                    roleInfo = new RoleInfo();
                    mappings.put(httpMethod, roleInfo);
                    if (allMethodsRoleInfo != null) {
                        roleInfo.combine(allMethodsRoleInfo);
                    }
                }
                if (!roleInfo.isForbidden()) {
                    this.configureRoleInfo(roleInfo, mapping);
                    if (roleInfo.isForbidden() && httpMethod.equals("*")) {
                        mappings.clear();
                        mappings.put("*", roleInfo);
                    }
                }
            }
        }
    }

    protected void processConstraintMappingWithMethodOmissions(ConstraintMapping mapping, Map<String, RoleInfo> mappings) {
        String[] omissions = mapping.getMethodOmissions();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < omissions.length; i++) {
            if (i > 0) {
                sb.append(".");
            }
            sb.append(omissions[i]);
        }
        sb.append(".omission");
        RoleInfo ri = new RoleInfo();
        mappings.put(sb.toString(), ri);
        this.configureRoleInfo(ri, mapping);
    }

    protected void configureRoleInfo(RoleInfo ri, ConstraintMapping mapping) {
        Constraint constraint = mapping.getConstraint();
        boolean forbidden = constraint.isForbidden();
        ri.setForbidden(forbidden);
        UserDataConstraint userDataConstraint = UserDataConstraint.get(mapping.getConstraint().getDataConstraint());
        ri.setUserDataConstraint(userDataConstraint);
        if (!ri.isForbidden()) {
            boolean checked = mapping.getConstraint().getAuthenticate();
            ri.setChecked(checked);
            if (ri.isChecked()) {
                if (mapping.getConstraint().isAnyRole()) {
                    for (String role : this._roles) {
                        ri.addRole(role);
                    }
                    ri.setAnyRole(true);
                } else if (mapping.getConstraint().isAnyAuth()) {
                    ri.setAnyAuth(true);
                } else {
                    String[] newRoles = mapping.getConstraint().getRoles();
                    for (String role : newRoles) {
                        if (!this._roles.contains(role)) {
                            throw new IllegalArgumentException("Attempt to use undeclared role: " + role + ", known roles: " + this._roles);
                        }
                        ri.addRole(role);
                    }
                }
            }
        }
    }

    @Override
    protected RoleInfo prepareConstraintInfo(String pathInContext, Request request) {
        Map<String, RoleInfo> mappings = this._constraintMap.match(pathInContext);
        if (mappings == null) {
            return null;
        } else {
            String httpMethod = request.getMethod();
            RoleInfo roleInfo = (RoleInfo) mappings.get(httpMethod);
            if (roleInfo == null) {
                List<RoleInfo> applicableConstraints = new ArrayList();
                RoleInfo all = (RoleInfo) mappings.get("*");
                if (all != null) {
                    applicableConstraints.add(all);
                }
                for (Entry<String, RoleInfo> entry : mappings.entrySet()) {
                    if (entry.getKey() != null && ((String) entry.getKey()).endsWith(".omission") && !((String) entry.getKey()).contains(httpMethod)) {
                        applicableConstraints.add(entry.getValue());
                    }
                }
                if (applicableConstraints.size() == 0 && this.isDenyUncoveredHttpMethods()) {
                    roleInfo = new RoleInfo();
                    roleInfo.setForbidden(true);
                } else if (applicableConstraints.size() == 1) {
                    roleInfo = (RoleInfo) applicableConstraints.get(0);
                } else {
                    roleInfo = new RoleInfo();
                    roleInfo.setUserDataConstraint(UserDataConstraint.None);
                    for (RoleInfo r : applicableConstraints) {
                        roleInfo.combine(r);
                    }
                }
            }
            return roleInfo;
        }
    }

    @Override
    protected boolean checkUserDataPermissions(String pathInContext, Request request, Response response, RoleInfo roleInfo) throws IOException {
        if (roleInfo == null) {
            return true;
        } else if (roleInfo.isForbidden()) {
            return false;
        } else {
            UserDataConstraint dataConstraint = roleInfo.getUserDataConstraint();
            if (dataConstraint != null && dataConstraint != UserDataConstraint.None) {
                HttpConfiguration httpConfig = Request.getBaseRequest(request).getHttpChannel().getHttpConfiguration();
                if (dataConstraint != UserDataConstraint.Confidential && dataConstraint != UserDataConstraint.Integral) {
                    throw new IllegalArgumentException("Invalid dataConstraint value: " + dataConstraint);
                } else if (request.isSecure()) {
                    return true;
                } else {
                    if (httpConfig.getSecurePort() > 0) {
                        String scheme = httpConfig.getSecureScheme();
                        int port = httpConfig.getSecurePort();
                        String url = URIUtil.newURI(scheme, request.getServerName(), port, request.getRequestURI(), request.getQueryString());
                        response.setContentLength(0);
                        response.sendRedirect(url);
                    } else {
                        response.sendError(403, "!Secure");
                    }
                    request.setHandled(true);
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    @Override
    protected boolean isAuthMandatory(Request baseRequest, Response base_response, Object constraintInfo) {
        return constraintInfo != null && ((RoleInfo) constraintInfo).isChecked();
    }

    @Override
    protected boolean checkWebResourcePermissions(String pathInContext, Request request, Response response, Object constraintInfo, UserIdentity userIdentity) throws IOException {
        if (constraintInfo == null) {
            return true;
        } else {
            RoleInfo roleInfo = (RoleInfo) constraintInfo;
            if (!roleInfo.isChecked()) {
                return true;
            } else if (roleInfo.isAnyAuth() && request.getUserPrincipal() != null) {
                return true;
            } else {
                boolean isUserInRole = false;
                for (String role : roleInfo.getRoles()) {
                    if (userIdentity.isUserInRole(role, null)) {
                        isUserInRole = true;
                        break;
                    }
                }
                return roleInfo.isAnyRole() && request.getUserPrincipal() != null && isUserInRole ? true : isUserInRole;
            }
        }
    }

    @Override
    public void dump(Appendable out, String indent) throws IOException {
        this.dumpBeans(out, indent, new Collection[] { Collections.singleton(this.getLoginService()), Collections.singleton(this.getIdentityService()), Collections.singleton(this.getAuthenticator()), Collections.singleton(this._roles), this._constraintMap.entrySet() });
    }

    @Override
    public void setDenyUncoveredHttpMethods(boolean deny) {
        this._denyUncoveredMethods = deny;
    }

    @Override
    public boolean isDenyUncoveredHttpMethods() {
        return this._denyUncoveredMethods;
    }

    @Override
    public boolean checkPathsWithUncoveredHttpMethods() {
        Set<String> paths = this.getPathsWithUncoveredHttpMethods();
        if (paths != null && !paths.isEmpty()) {
            for (String p : paths) {
                LOG.warn("{} has uncovered http methods for path: {}", ContextHandler.getCurrentContext(), p);
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug(new Throwable());
            }
            return true;
        } else {
            return false;
        }
    }

    public Set<String> getPathsWithUncoveredHttpMethods() {
        if (this._denyUncoveredMethods) {
            return Collections.emptySet();
        } else {
            Set<String> uncoveredPaths = new HashSet();
            for (String path : this._constraintMap.keySet()) {
                Map<String, RoleInfo> methodMappings = (Map<String, RoleInfo>) this._constraintMap.get(path);
                if (methodMappings.get("*") == null) {
                    boolean hasOmissions = this.omissionsExist(path, methodMappings);
                    for (String method : methodMappings.keySet()) {
                        if (method.endsWith(".omission")) {
                            for (String m : this.getOmittedMethods(method)) {
                                if (!methodMappings.containsKey(m)) {
                                    uncoveredPaths.add(path);
                                }
                            }
                        } else if (!hasOmissions) {
                            uncoveredPaths.add(path);
                        }
                    }
                }
            }
            return uncoveredPaths;
        }
    }

    protected boolean omissionsExist(String path, Map<String, RoleInfo> methodMappings) {
        if (methodMappings == null) {
            return false;
        } else {
            boolean hasOmissions = false;
            for (String m : methodMappings.keySet()) {
                if (m.endsWith(".omission")) {
                    hasOmissions = true;
                }
            }
            return hasOmissions;
        }
    }

    protected Set<String> getOmittedMethods(String omission) {
        if (omission != null && omission.endsWith(".omission")) {
            String[] strings = omission.split("\\.");
            Set<String> methods = new HashSet();
            for (int i = 0; i < strings.length - 1; i++) {
                methods.add(strings[i]);
            }
            return methods;
        } else {
            return Collections.emptySet();
        }
    }
}