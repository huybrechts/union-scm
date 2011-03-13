package hudson.plugins.unionscm;

import com.strangeberry.jmdns.tools.Browser;
import hudson.Extension;
import hudson.model.Descriptor;
import hudson.scm.ChangeLogSet;
import hudson.scm.RepositoryBrowser;
import org.jvnet.tiger_types.Types;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Tom Huybrechts
 */
public class UnionSCMRepositoryBrowser extends RepositoryBrowser<ChangeLogSet.Entry> {
    private Map<Class,RepositoryBrowser> map;

    public UnionSCMRepositoryBrowser(UnionSCM scm) {
        map = new HashMap<Class, RepositoryBrowser>();
        for (UnionSCM.ChildSCM child: scm.getScms()) {
            RepositoryBrowser<?> b = child.getScm().getBrowser();
            ParameterizedType t = (ParameterizedType) Types.getBaseClass(b.getClass(), RepositoryBrowser.class);
            Class entryType = Types.erasure(t.getActualTypeArguments()[0]);
            map.put(entryType, b);
        }
    }


    @Override
    public URL getChangeSetLink(ChangeLogSet.Entry changeSet) throws IOException {
        RepositoryBrowser b = map.get(changeSet.getClass());
        if (b == null) {
            return null;
        } else {
            return b.getChangeSetLink(changeSet);
        }
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<RepositoryBrowser<?>> {

        @Override
        public String getDisplayName() {
            return "UnionSCM";
        }
    }
}
