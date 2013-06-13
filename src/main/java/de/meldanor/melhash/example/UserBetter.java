/*
 * Copyright (C) 2013 Kilian Gaertner
 * 
 * This file is part of MelHash.
 * 
 * MelHash is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * MelHash is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MelHash.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.meldanor.melhash.example;

import java.util.Date;

public class UserBetter {

    private final String name;
    private final Date regDate;

    private int hash;

    public UserBetter(String name) {
        this.name = name;
        this.regDate = new Date();
    }

    @Override
    public int hashCode() {
        if (hash == 0) {
            int sum = name.hashCode() + regDate.hashCode();
            hash = (sum << 5) - sum;
        }
        return hash;
    }
}