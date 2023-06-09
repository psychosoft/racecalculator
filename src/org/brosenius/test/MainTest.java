package org.brosenius.test;
import org.brosenius.domain.Race;
import org.brosenius.racecalculator.RaceCalculatorUtil;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.brosenius.domain.Gender.FEMALE;

public class MainTest {

    Vector<Race> listOfIndividualResults = new Vector<>();

    @Test
    public void testCalculation() {
        setupData();
        Vector<Vector<Race>> winningTeamsWithBestRacesSortedAscending = RaceCalculatorUtil.calculateAndPrintResult(listOfIndividualResults);

        winningTeamsWithBestRacesSortedAscending.forEach(races -> {
            /**
             * Test for at least one female per winning team
             */
            assert(races.stream().filter(r -> RaceCalculatorUtil.classGenderMap.get(r.getClassID()) == FEMALE).collect(Collectors.toCollection(Vector::new)).size() > 0);
            /**
             * Test that all calculated teams have correct number of contestants
             * @see RaceCalculatorUtil.numTopRidersPerTeam
             */
            assert(races.size() == RaceCalculatorUtil.numTopRidersPerTeam);
            /**
             * Test winners per team time calculation
             */
            int team1Total = getTeamTotal(1);
            assert(team1Total == (44010 + 41450 + 31450));
            int team2Total = getTeamTotal(2);
            assert(team2Total == (22140 + 30760 + 44130));
            int team3Total = getTeamTotal(3);
            assert(team3Total == (32000 + 35980 + 38130));
        });
    }

    private int getTeamTotal(int teamId){
        return RaceCalculatorUtil.selectBestRacesForTeamIdIncludeMinimumOneFemale(teamId, RaceCalculatorUtil.numTopRidersPerTeam, listOfIndividualResults).stream().mapToInt(Race::getTotal).sum();
    }

    private void setupData() {
        listOfIndividualResults.add(new Race(2,1,1,44010));
        listOfIndividualResults.add(new Race(1,2,5,22140));
        listOfIndividualResults.add(new Race(1,2,11,30760));
        listOfIndividualResults.add(new Race(1,2,4,41010));
        listOfIndividualResults.add(new Race(1,3,3,41410));
        listOfIndividualResults.add(new Race(1,1,2,41450));
        listOfIndividualResults.add(new Race(1,1,2,31450));
        listOfIndividualResults.add(new Race(2,1,2,51450));
        listOfIndividualResults.add(new Race(2,2,6,44130));
        listOfIndividualResults.add(new Race(1,3,7,32000));
        listOfIndividualResults.add(new Race(2,3,9,35980));
        listOfIndividualResults.add(new Race(1,3,8,38130));
        listOfIndividualResults.add(new Race(1,3,10,39000));
    }

}
