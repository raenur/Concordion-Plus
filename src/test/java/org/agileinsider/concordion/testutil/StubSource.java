package org.agileinsider.concordion.testutil;

/*
 * Copyright 2011 Mark Barnes (mark@agileinsider.org)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
