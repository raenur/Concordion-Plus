package org.agileinsider.concordion.specifications;

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

import org.agileinsider.concordion.ConcordionPlusAcceptanceTest;
import org.agileinsider.concordion.junit.ConcordionPlus;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
public class ConcordionPlusTest extends ConcordionPlusAcceptanceTest {
    @RunWith(ConcordionPlus.class)
    public static class AnnotatedTest {
        public String getText() {
            return "unexpected text";
        }
    }

    @Override
    public Object getFixture() {
        return new AnnotatedTest();
    }
}