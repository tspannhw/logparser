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
package nl.basjes.parse.core;

import nl.basjes.parse.core.exceptions.DisectionFailure;
import nl.basjes.parse.core.exceptions.InvalidDisectorException;

public interface Disector {

    // --------------------------------------------

    /**
     * This method must disect the provided field from the parsable into 'smaller' pieces.
     */
    void disect(final Parsable<?> parsable, final String inputname)
        throws DisectionFailure, InvalidDisectorException;

    // --------------------------------------------

    /**
     * @return The required typename of the input
     */
    String getInputType();

    // --------------------------------------------

    /**
     * What are all possible outputs that can be provided.
     * @return array of "type:name" values that indicates all the possible outputs
     */
    String[] getPossibleOutput();

    // --------------------------------------------

    /**
     * This tells the disector that it should prepare that we will call it soon
     * with 'inputname' and expect to get 'inputname.outputname' because
     * inputname is of the type returned by getInputType and outputname
     * was part of the answer from getPossibleOutput.
     * This can be used by the disector implementation to optimize the internal parsing
     * algorithms and lookup tables and such.
     */
    void prepareForDisect(final String inputname, final String outputname);

    // --------------------------------------------

    /**
     * The framework will tell the disector that it should get ready to run.
     * I.e. finalize the bootstrapping.
     */
    void prepareForRun() throws InvalidDisectorException;

    // --------------------------------------------

}