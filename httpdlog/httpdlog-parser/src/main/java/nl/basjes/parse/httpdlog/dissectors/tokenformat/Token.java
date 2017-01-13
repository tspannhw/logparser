/*
 * Apache HTTPD & NGINX Access log parsing made easy
 * Copyright (C) 2011-2017 Niels Basjes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.basjes.parse.httpdlog.dissectors.tokenformat;

import nl.basjes.parse.core.Casts;
import nl.basjes.parse.core.Dissector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;

public class Token {
    private static final Logger LOG = LoggerFactory.getLogger(Token.class);

    private final String name;
    private final String type;
    private final String regex;
    private final int startPos;
    private final int length;
    private final EnumSet<Casts> casts;
    private final int prio;
    protected String warningMessageWhenUsed = null;

    // In some cases a token needs a custom dissector.
    private Dissector customDissector = null;

    public Token(
            final String nName,
            final String nType,
            final EnumSet<Casts> nCasts,
            final String nRegex,
            final int nStartPos,
            final int nLength,
            final int nPrio) {

        // RFC 2616 Section 4.2 states: "Field names are case-insensitive."
        name = nName.toLowerCase();
        type = nType;
        regex = nRegex;
        startPos = nStartPos;
        length = nLength;
        casts = nCasts;
        prio = nPrio;
    }

    public void setCustomDissector(Dissector dissector) {
        customDissector = dissector;
    }

    public Dissector getCustomDissector() {
        return customDissector;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getRegex() {
        return regex;
    }

    public int getStartPos() {
        return startPos;
    }

    public int getLength() {
        return length;
    }

    public EnumSet<Casts> getCasts() {
        return casts;
    }

    public int getPrio() {
        return prio;
    }

    public void setWarningMessageWhenUsed(String message) {
        warningMessageWhenUsed = message;
    }

    public void tokenWasUsed() {
        if (warningMessageWhenUsed != null) {
            LOG.warn("------------------------------------------------------------------------");
            LOG.warn(warningMessageWhenUsed, getType()+':'+getName());
            LOG.warn("------------------------------------------------------------------------");
        }
    }

    // This is used by your favorite debugger.
    @Override
    public String toString() {
        return "{" + type + ':' + name + " (" + startPos + "+" + length + ");Prio=" + prio + "}";
    }
}
