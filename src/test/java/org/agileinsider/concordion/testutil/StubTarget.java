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
import org.concordion.api.Target;
import org.concordion.internal.util.Check;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class StubTarget implements Target {

    private final LinkedHashMap<Resource, String> writtenStrings = new LinkedHashMap<Resource, String>();
    private final List<Resource> copiedResources = new ArrayList<Resource>();

    public void copyTo(Resource resource, InputStream inputStream) throws IOException {
        copiedResources.add(resource);
    }

    public void delete(Resource resource) throws IOException {
    }

    public void write(Resource resource, String s) throws IOException {
        writtenStrings.put(resource, s);
    }

    public String getWrittenString(Resource resource) {
        Check.isTrue(writtenStrings.containsKey(resource), "Expected resource '" + resource.getPath() + "' was not written to target");
        return writtenStrings.get(resource);
    }

    public boolean exists(Resource resource) {
        return hasCopiedResource(resource) || writtenStrings.containsKey(resource);
    }

    public boolean hasCopiedResource(Resource resource) {
        return copiedResources.contains(resource);
    }

    public OutputStream getOutputStream(Resource resource) {
        throw new UnsupportedOperationException("not implemented on StubTarget");
    }
}
