package org.brosenius.domain.compare;

import org.brosenius.domain.Race;

import java.util.Comparator;

public class RaceTimeComparator implements Comparator<Race> {
        @Override
        public int compare(Race a, Race b) {
            return a.getTotal() == b.getTotal() ? 0 : a.getTotal() < b.getTotal() ? -1 : 1;
        }
    }
