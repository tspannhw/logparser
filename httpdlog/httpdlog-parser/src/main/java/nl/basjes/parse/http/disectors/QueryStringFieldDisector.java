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

package nl.basjes.parse.http.disectors;

import nl.basjes.parse.core.Casts;
import nl.basjes.parse.core.Disector;
import nl.basjes.parse.core.Parsable;
import nl.basjes.parse.core.ParsedField;
import nl.basjes.parse.core.exceptions.DisectionFailure;

import java.util.*;

import static nl.basjes.parse.Utils.resilientUrlDecode;

public class QueryStringFieldDisector extends Disector {
    // --------------------------------------------

    private static final String INPUT_TYPE = "HTTP.QUERYSTRING";

    @Override
    public String getInputType() {
        return INPUT_TYPE;
    }

    // --------------------------------------------

    /** This should output all possible types */
    @Override
    public List<String> getPossibleOutput() {
        List<String> result = new ArrayList<>();
        result.add("STRING:*");
        return result;
    }

    // --------------------------------------------

    @Override
    protected void initializeNewInstance(Disector newInstance) {
        // Nothing to do
    }

    // --------------------------------------------

    private final Set<String> requestedParameters = new HashSet<>(16);

    @Override
    public void prepareForDisect(final String inputname, final String outputname) {
        requestedParameters.add(outputname.substring(inputname.length() + 1));
    }

    // --------------------------------------------

    @Override
    public void prepareForRun() {
        // We do not do anything extra here
    }

    // --------------------------------------------

    @Override
    public void disect(final Parsable<?> parsable, final String inputname) throws DisectionFailure {
        final ParsedField field = parsable.getParsableField(INPUT_TYPE, inputname);

        String fieldValue = field.getValue();
        if (fieldValue == null || fieldValue.isEmpty()) {
            return; // Nothing to do here
        }

        String[] allValues = fieldValue.split("&");

        for (String value : allValues) {
            int equalPos = value.indexOf('=');
            if (equalPos == -1) {
                if (!"".equals(value)) {
                    String name = value.toLowerCase();
                    if (requestedParameters.contains(name)) {
                        parsable.addDisection(inputname, getDisectionType(inputname, value), name, "", EnumSet.of(Casts.STRING));
                    }
                }
            } else {
                String name = value.substring(0, equalPos).toLowerCase();
                if (requestedParameters.contains(name)) {
                    try {
                        parsable.addDisection(inputname, getDisectionType(inputname, name), name,
                                resilientUrlDecode(value.substring(equalPos + 1, value.length())), EnumSet.of(Casts.STRING));
                    } catch (IllegalArgumentException e) {
                        // This usually means that there was invalid encoding in the line
                        throw new DisectionFailure(e.getMessage());
                    }
                }
            }
        }
    }

    // --------------------------------------------

    /**
     * This determines the type of the value that was just found.
     * This method is intended to be overruled by a subclass
     */
    public String getDisectionType(final String basename, final String name) {
        return "STRING";
    }

    // --------------------------------------------

}
