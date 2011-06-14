package org.agileinsider.concordion.testutil;

import org.concordion.api.Resource;
import org.concordion.api.Source;
import org.concordion.internal.util.Check;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class StubSource implements Source {
    private Map<Resource, String> resources = new HashMap<Resource, String>();

    public void addResource(Resource resource, String content) {
        resources.put(resource, content);
    }

    public InputStream createInputStream(Resource resource) throws IOException {
        Check.isTrue(canFind(resource), "No such resource exists in simulator: " + resource.getPath());
        return new ByteArrayInputStream(resources.get(resource).getBytes("UTF-8"));
    }

    public boolean canFind(Resource resource) {
        return resources.containsKey(resource);
    }
}
