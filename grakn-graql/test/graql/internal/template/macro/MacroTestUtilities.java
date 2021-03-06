/*
 * GRAKN.AI - THE KNOWLEDGE GRAPH
 * Copyright (C) 2018 Grakn Labs Ltd
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package grakn.core.graql.internal.template.macro;

import grakn.core.graql.Graql;
import grakn.core.graql.Query;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static grakn.core.graql.Graql.parse;
import static junit.framework.TestCase.assertEquals;

public class MacroTestUtilities {

    public static void assertParseEquals(String template, Map<String, Object> data, String expected){
        List<Query> result = Graql.parser().parseTemplate(template, data).collect(Collectors.toList());
        assertEquals(parse(expected), result.get(0));
    }
}
