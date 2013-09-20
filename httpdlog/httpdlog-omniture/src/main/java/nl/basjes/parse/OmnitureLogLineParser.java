/*
 * Apache HTTPD logparsing made easy
 * Copyright (C) 2013 Niels Basjes
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package nl.basjes.parse;

import java.io.IOException;

import nl.basjes.parse.apachehttpdlog.ApacheHttpdLoglineParser;
import nl.basjes.parse.core.exceptions.InvalidDisectorException;
import nl.basjes.parse.core.exceptions.MissingDisectorsException;
import nl.basjes.parse.http.disectors.QueryStringFieldDisector;

public class OmnitureLogLineParser<RECORD> extends ApacheHttpdLoglineParser<RECORD> {

    public OmnitureLogLineParser(Class<RECORD> clazz, String logformat)
        throws IOException, MissingDisectorsException, InvalidDisectorException {
        super(clazz, logformat);
    }

    @Override
    public void addDisectors() {
        super.addDisectors();
        // We must drop this one or we will have conflicts
        dropDisector(QueryStringFieldDisector.class);
        addDisector(new OmnitureQueryStringFieldDisector());
    }
}