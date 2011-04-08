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

import nu.xom.Document;
import org.concordion.api.Element;
import org.concordion.internal.XMLParser;

public class ProcessingResult {

    private final String documentXML;

    public ProcessingResult(String documentXML) {
        this.documentXML = documentXML;
    }

    public Document getXOMDocument() {
        try {
            return XMLParser.parse(documentXML);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse resultant XML document", e);
        }
    }

    public Element getRootElement() {
        nu.xom.Element rootElement = getXOMDocument().getRootElement();
        return new Element(rootElement);
    }

    private Element getOutputFragment() {
        Element rootElement = getRootElement();
        return rootElement.getFirstDescendantNamed("fragment");
    }

    public String getOutputFragmentXML() {
        Element outputFragment = getOutputFragment();
        String xml = outputFragment.toXML();
        return xml.replaceAll("</?fragment>", "").replaceAll("\u00A0", "&#160;");
    }

    public String getHead() {
        Element headElement = getHeadElement();
        return headElement.toXML();
    }

    private Element getHeadElement() {
        return getRootElement().getFirstChildElement("head");
    }

    public String getStyles() {
        Element[] styleElements = getHeadElement().getChildElements("style");
        StringBuilder sb = new StringBuilder();
        for (Element styleElement : styleElements) {
            sb.append(styleElement.toXML());
        }
        return sb.toString();
    }
}
