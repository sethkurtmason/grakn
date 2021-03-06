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

package grakn.core.graql.admin;

import grakn.core.graql.DeleteQuery;
import grakn.core.graql.Match;
import grakn.core.graql.Var;

import javax.annotation.CheckReturnValue;
import java.util.Set;

/**
 * Admin class for inspecting and manipulating a DeleteQuery
 *
 */
public interface DeleteQueryAdmin extends DeleteQuery {
    /**
     * @return the {@link Match} this delete query is operating on
     */
    @CheckReturnValue
    Match match();

    /**
     * Get the {@link Var}s to delete on each result of {@link #match()}.
     */
    @CheckReturnValue
    Set<Var> vars();
}
