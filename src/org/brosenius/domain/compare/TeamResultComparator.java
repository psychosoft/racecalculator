package org.brosenius.domain.compare;

import org.brosenius.domain.Race;

import java.util.Comparator;
import java.util.Vector;

public class TeamResultComparator implements Comparator<Vector<Race>> {

    private int getTotalScore (Vector<Race> race) {
        return race.stream().reduce(0, (acc, race1) -> acc + race1.getTotal(), Integer::sum);
    }
    public int compare(Vector<Race> a, Vector<Race> b) {
        return getTotalScore(a) == getTotalScore(b) ? 0 : getTotalScore(a) < getTotalScore(b) ? -1 : 1;
    }
}
