package org.brosenius.racecalculator;

import org.brosenius.domain.Gender;
import org.brosenius.domain.Race;
import org.brosenius.domain.compare.RaceTimeComparator;
import org.brosenius.domain.compare.TeamResultComparator;
import org.brosenius.exception.CompetitionCriteriaException;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static org.brosenius.domain.Gender.FEMALE;
import static org.brosenius.domain.Gender.MALE;

public class RaceCalculatorUtil {

    //Singleton
    private RaceCalculatorUtil(){};

    public static Map<Integer, Gender> classGenderMap = new HashMap() {{
        put(1,MALE);
        put(2,FEMALE);
    }};

    public static final int numTopRidersPerTeam = 3;

    public static Vector<Vector<Race>> calculateAndPrintResult(Vector<Race> listOfIndividualResults){

        Map<Integer, List<Race>> racesGroupedByTeamID = listOfIndividualResults.stream().collect(groupingBy(Race::getTeamID));

        Vector<Vector<Race>> teamsWithBestRaces = new Vector(new Vector());

        racesGroupedByTeamID.keySet().stream().forEach(teamID -> {
            Vector<Race> bestRaces = selectBestRacesForTeamIdIncludeMinimumOneFemale(teamID,numTopRidersPerTeam,listOfIndividualResults);
            teamsWithBestRaces.add(bestRaces);
        });

        Collections.sort(teamsWithBestRaces, new TeamResultComparator());

        teamsWithBestRaces.forEach(races -> {
                    AtomicInteger teamTotal = new AtomicInteger();
                    races.forEach(race -> {
                                teamTotal.addAndGet(race.getTotal());
                                System.out.println("Team:" + race.getTeamID() + " Rider:" + race.getNameID() + " Gender:" + classGenderMap.get(race.getClassID()) + " Time:" + race.getTotal());
                            }
                    );
                    System.out.println("Team total time:" + teamTotal);
                }
        );

        return teamsWithBestRaces;

    }

    public static Vector<Race> selectBestRacesForTeamIdIncludeMinimumOneFemale(int teamId,int numRacesToInclude, Vector<Race> races){
        Vector<Race> bestRaces = new Vector();
        Collections.sort(races, new RaceTimeComparator());

        Vector<Race> racesForTeamId = races.stream().filter(r -> r.getTeamID() == teamId).collect(Collectors.toCollection(Vector::new));

        racesForTeamId.stream().forEach(race -> {
                    long numFemales = bestRaces.stream().filter(r -> classGenderMap.get(r.getClassID()) == FEMALE).count();
                    boolean addOnlyFemale = numFemales == 0 && bestRaces.size() == numRacesToInclude-1 && classGenderMap.get(race.getClassID()) == FEMALE;
                    boolean addFemaleOrMale = numFemales > 0 || bestRaces.size() < numTopRidersPerTeam-1;
                    boolean addAnother = bestRaces.size() < numRacesToInclude;
                    if(addAnother && addFemaleOrMale || addOnlyFemale)
                        bestRaces.add(race);
                }
        );

        if(bestRaces.size() < numRacesToInclude) throw new CompetitionCriteriaException("Could not find specified amount of riders for team number: " + teamId);

        return bestRaces;
    }
}
