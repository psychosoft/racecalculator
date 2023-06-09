package org.brosenius.test;
import org.brosenius.domain.Race;
import org.brosenius.racecalculator.RaceCalculatorUtil;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.brosenius.domain.Gender.FEMALE;
import static org.brosenius.domain.Gender.MALE;

public class MainTest {

    Vector<Race> listOfIndividualResults = new Vector<>();

    @Test
    public void testCalculation() {
        setupData();
        Vector<Vector<Race>> winningTeamsWithBestRacesSortedAscending = RaceCalculatorUtil.calculateAndPrintResult(listOfIndividualResults);

        winningTeamsWithBestRacesSortedAscending.forEach(races -> {

            boolean qualifiedForMixedCompetition = races.size() > 0;

            if(qualifiedForMixedCompetition) {
                /**
                 * Test for at least one female per mixed team
                 */
                assert (races.stream().filter(r -> RaceCalculatorUtil.classGenderMap.get(r.getClassID()) == FEMALE).collect(Collectors.toCollection(Vector::new)).size() > 0);
                /**
                 * Test for at least one male per mixed team
                 */
                assert (races.stream().filter(r -> RaceCalculatorUtil.classGenderMap.get(r.getClassID()) == MALE).collect(Collectors.toCollection(Vector::new)).size() > 0);
                /**
                 * Test that all calculated races have correct number of contestants
                 * @see RaceCalculatorUtil.numTopRidersPerTeam
                 */
                assert (races.size() == RaceCalculatorUtil.numTopRidersPerTeam);
            }
            /**
             * Test winners per team time calculation
             */
            //teams qualified for mixed competition
            int team1Total = getTeamTotal(1);
            assert(team1Total == (44010 + 41450 + 31450));
            int team2Total = getTeamTotal(2);
            assert(team2Total == (22140 + 30760 + 44130));
            int team3Total = getTeamTotal(3);
            assert(team3Total == (32000 + 35980 + 38130));
            //teams not qualified for mixed competition
            int team4Total = getTeamTotal(4);
            assert(team4Total == (0));
            int team5Total = getTeamTotal(5);
            assert(team5Total == (0));
            int team6Total = getTeamTotal(6);
            assert(team6Total == (0));
        });
    }

    private int getTeamTotal(int teamId){
        return RaceCalculatorUtil.selectBestMixedRacesForTeamId(teamId, RaceCalculatorUtil.numTopRidersPerTeam, listOfIndividualResults).stream().mapToInt(Race::getTotal).sum();
    }

    private void setupData() {
        //Team 1, qualified for mixed, rank #3
        listOfIndividualResults.add(new Race(1,1,2,41450));
        listOfIndividualResults.add(new Race(1,1,2,31450));
        listOfIndividualResults.add(new Race(2,1,2,51450));
        listOfIndividualResults.add(new Race(2,1,1,44010));
        //Team 2,  qualified for mixed, rank #1
        listOfIndividualResults.add(new Race(1,2,5,22140));
        listOfIndividualResults.add(new Race(1,2,11,30760));
        listOfIndividualResults.add(new Race(1,2,4,41010));
        listOfIndividualResults.add(new Race(2,2,6,44130));
        //Team 3,  qualified for mixed, rank #2
        listOfIndividualResults.add(new Race(1,3,3,41410));
        listOfIndividualResults.add(new Race(1,3,7,32000));
        listOfIndividualResults.add(new Race(2,3,9,35980));
        listOfIndividualResults.add(new Race(1,3,8,38130));
        listOfIndividualResults.add(new Race(1,3,10,39000));
        //Team 4, too few contestants, excluded
        listOfIndividualResults.add(new Race(2,4,9,35980));
        listOfIndividualResults.add(new Race(1,4,8,38130));
        //Team 5, only MALE, excluded
        listOfIndividualResults.add(new Race(1,5,9,35980));
        listOfIndividualResults.add(new Race(1,5,8,38130));
        listOfIndividualResults.add(new Race(1,5,8,38130));
        //Team 6, only FEMALE, excluded
        listOfIndividualResults.add(new Race(2,6,9,35980));
        listOfIndividualResults.add(new Race(2,6,8,38130));
        listOfIndividualResults.add(new Race(2,6,8,38130));
        //Team 7, same score as Team 1 qualified for mixed, rank shared #3
        listOfIndividualResults.add(new Race(1,7,2,41450));
        listOfIndividualResults.add(new Race(1,7,2,31450));
        listOfIndividualResults.add(new Race(2,7,2,51450));
        listOfIndividualResults.add(new Race(2,7,1,44010));
    }

}
