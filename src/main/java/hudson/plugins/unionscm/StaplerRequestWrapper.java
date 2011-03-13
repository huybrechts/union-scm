package hudson.plugins.unionscm;

import net.sf.json.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.kohsuke.stapler.Ancestor;
import org.kohsuke.stapler.Stapler;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.security.Principal;
import java.util.*;

/**
 * Author: Tom Huybrechts
 */
public class StaplerRequestWrapper implements StaplerRequest {

    private StaplerRequest wrapped;

    public StaplerRequestWrapper(StaplerRequest wrapped) {
        this.wrapped = wrapped;
    }

    public Stapler getStapler() {
        return wrapped.getStapler();
    }

    public String getRestOfPath() {
        return wrapped.getRestOfPath();
    }

    public String getOriginalRestOfPath() {
        return wrapped.getOriginalRestOfPath();
    }

    public ServletContext getServletContext() {
        return wrapped.getServletContext();
    }

    public RequestDispatcher getView(Object it, String viewName) throws IOException {
        return wrapped.getView(it, viewName);
    }

    public RequestDispatcher getView(Class clazz, String viewName) throws IOException {
        return wrapped.getView(clazz, viewName);
    }

    public String getRootPath() {
        return wrapped.getRootPath();
    }

    public String getReferer() {
        return wrapped.getReferer();
    }

    public List<Ancestor> getAncestors() {
        return wrapped.getAncestors();
    }

    public Ancestor findAncestor(Class type) {
        return wrapped.findAncestor(type);
    }

    public <T> T findAncestorObject(Class<T> type) {
        return wrapped.findAncestorObject(type);
    }

    public Ancestor findAncestor(Object o) {
        return wrapped.findAncestor(o);
    }

    public boolean hasParameter(String name) {
        return wrapped.hasParameter(name);
    }

    public String getOriginalRequestURI() {
        return wrapped.getOriginalRequestURI();
    }

    public boolean checkIfModified(long timestampOfResource, StaplerResponse rsp) {
        return wrapped.checkIfModified(timestampOfResource, rsp);
    }

    public boolean checkIfModified(Date timestampOfResource, StaplerResponse rsp) {
        return wrapped.checkIfModified(timestampOfResource, rsp);
    }

    public boolean checkIfModified(Calendar timestampOfResource, StaplerResponse rsp) {
        return wrapped.checkIfModified(timestampOfResource, rsp);
    }

    public boolean checkIfModified(long timestampOfResource, StaplerResponse rsp, long expiration) {
        return wrapped.checkIfModified(timestampOfResource, rsp, expiration);
    }

    public void bindParameters(Object bean) {
        wrapped.bindParameters(bean);
    }

    public void bindParameters(Object bean, String prefix) {
        wrapped.bindParameters(bean, prefix);
    }

    public <T> List<T> bindParametersToList(Class<T> type, String prefix) {
        return wrapped.bindParametersToList(type, prefix);
    }

    public <T> T bindParameters(Class<T> type, String prefix) {
        return wrapped.bindParameters(type, prefix);
    }

    public <T> T bindParameters(Class<T> type, String prefix, int index) {
        return wrapped.bindParameters(type, prefix, index);
    }

    public <T> T bindJSON(Class<T> type, JSONObject src) {
        return wrapped.bindJSON(type, src);
    }

    public <T> T bindJSON(Type genericType, Class<T> erasure, Object json) {
        return wrapped.bindJSON(genericType, erasure, json);
    }

    public void bindJSON(Object bean, JSONObject src) {
        wrapped.bindJSON(bean, src);
    }

    public <T> List<T> bindJSONToList(Class<T> type, Object src) {
        return wrapped.bindJSONToList(type, src);
    }

    public JSONObject getSubmittedForm() throws ServletException {
        return wrapped.getSubmittedForm();
    }

    public FileItem getFileItem(String name) throws ServletException, IOException {
        return wrapped.getFileItem(name);
    }

    public boolean isJavaScriptProxyCall() {
        return wrapped.isJavaScriptProxyCall();
    }

    public String getAuthType() {
        return wrapped.getAuthType();
    }

    public Cookie[] getCookies() {
        return wrapped.getCookies();
    }

    public long getDateHeader(String name) {
        return wrapped.getDateHeader(name);
    }

    public String getHeader(String name) {
        return wrapped.getHeader(name);
    }

    public Enumeration getHeaders(String name) {
        return wrapped.getHeaders(name);
    }

    public Enumeration getHeaderNames() {
        return wrapped.getHeaderNames();
    }

    public int getIntHeader(String name) {
        return wrapped.getIntHeader(name);
    }

    public String getMethod() {
        return wrapped.getMethod();
    }

    public String getPathInfo() {
        return wrapped.getPathInfo();
    }

    public String getPathTranslated() {
        return wrapped.getPathTranslated();
    }

    public String getContextPath() {
        return wrapped.getContextPath();
    }

    public String getQueryString() {
        return wrapped.getQueryString();
    }

    public String getRemoteUser() {
        return wrapped.getRemoteUser();
    }

    public boolean isUserInRole(String role) {
        return wrapped.isUserInRole(role);
    }

    public Principal getUserPrincipal() {
        return wrapped.getUserPrincipal();
    }

    public String getRequestedSessionId() {
        return wrapped.getRequestedSessionId();
    }

    public String getRequestURI() {
        return wrapped.getRequestURI();
    }

    public StringBuffer getRequestURL() {
        return wrapped.getRequestURL();
    }

    public String getServletPath() {
        return wrapped.getServletPath();
    }

    public HttpSession getSession(boolean create) {
        return wrapped.getSession(create);
    }

    public HttpSession getSession() {
        return wrapped.getSession();
    }

    public boolean isRequestedSessionIdValid() {
        return wrapped.isRequestedSessionIdValid();
    }

    public boolean isRequestedSessionIdFromCookie() {
        return wrapped.isRequestedSessionIdFromCookie();
    }

    public boolean isRequestedSessionIdFromURL() {
        return wrapped.isRequestedSessionIdFromURL();
    }

    public boolean isRequestedSessionIdFromUrl() {
        return wrapped.isRequestedSessionIdFromUrl();
    }

    public Object getAttribute(String name) {
        return wrapped.getAttribute(name);
    }

    public Enumeration getAttributeNames() {
        return wrapped.getAttributeNames();
    }

    public String getCharacterEncoding() {
        return wrapped.getCharacterEncoding();
    }

    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
        wrapped.setCharacterEncoding(env);
    }

    public int getContentLength() {
        return wrapped.getContentLength();
    }

    public String getContentType() {
        return wrapped.getContentType();
    }

    public ServletInputStream getInputStream() throws IOException {
        return wrapped.getInputStream();
    }

    public String getParameter(String name) {
        if (name.startsWith("git.")) {
            String[] parameterValues = wrapped.getParameterValues(name);
            if (parameterValues != null && parameterValues.length >= 2) {
                return parameterValues[1];
            } else {
                return null;
            }
        } else {
            return wrapped.getParameter(name);
        }
    }

    public Enumeration getParameterNames() {
        return wrapped.getParameterNames();
    }

    public String[] getParameterValues(String name) {
        if (name.startsWith("git.")) {
            String[] parameterValues = wrapped.getParameterValues(name);
            if (parameterValues != null && parameterValues.length >= 2) {
                String[] result = new String[parameterValues.length - 1];
                System.arraycopy(parameterValues, 1, result, 0, result.length);
                return result;
            }
        }
        return wrapped.getParameterValues(name);

    }

    public Map getParameterMap() {
        return wrapped.getParameterMap();
    }

    public String getProtocol() {
        return wrapped.getProtocol();
    }

    public String getScheme() {
        return wrapped.getScheme();
    }

    public String getServerName() {
        return wrapped.getServerName();
    }

    public int getServerPort() {
        return wrapped.getServerPort();
    }

    public BufferedReader getReader() throws IOException {
        return wrapped.getReader();
    }

    public String getRemoteAddr() {
        return wrapped.getRemoteAddr();
    }

    public String getRemoteHost() {
        return wrapped.getRemoteHost();
    }

    public void setAttribute(String name, Object o) {
        wrapped.setAttribute(name, o);
    }

    public void removeAttribute(String name) {
        wrapped.removeAttribute(name);
    }

    public Locale getLocale() {
        return wrapped.getLocale();
    }

    public Enumeration getLocales() {
        return wrapped.getLocales();
    }

    public boolean isSecure() {
        return wrapped.isSecure();
    }

    public RequestDispatcher getRequestDispatcher(String path) {
        return wrapped.getRequestDispatcher(path);
    }

    public String getRealPath(String path) {
        return wrapped.getRealPath(path);
    }

    public int getRemotePort() {
        return wrapped.getRemotePort();
    }

    public String getLocalName() {
        return wrapped.getLocalName();
    }

    public String getLocalAddr() {
        return wrapped.getLocalAddr();
    }

    public int getLocalPort() {
        return wrapped.getLocalPort();
    }

}
