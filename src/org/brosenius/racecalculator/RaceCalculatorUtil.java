package org.brosenius.racecalculator;

import org.brosenius.domain.Gender;
import org.brosenius.domain.Race;
import org.brosenius.domain.compare.RaceTimeComparator;
import org.brosenius.domain.compare.TeamResultComparator;

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
            Vector<Race> bestRaces = selectBestMixedRacesForTeamId(teamID,numTopRidersPerTeam,listOfIndividualResults);
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
            //only print teams that has total time
            if(teamTotal.get() > 0) System.out.println("Team total time:" + teamTotal);
                }
        );

        return teamsWithBestRaces;

    }

    private static long numGender(Vector<Race> races, Gender gender) {
        return races.stream().filter(r -> classGenderMap.get(r.getClassID()) == gender).count();
    }

    private static Gender getMissingGender (Vector<Race> races) {
        return numGender(races, FEMALE) == 0 ? FEMALE : numGender(races, MALE) == 0 ? MALE : null;
    }

    private static boolean getIsMixedClass(Vector<Race> races) {
        return getMissingGender(races) == null ? true : false;
    }

    public static Vector<Race> selectBestMixedRacesForTeamId(int teamId, int numRacesToInclude, Vector<Race> races){

        //will be populated with the extracted best races for the current TeamId
        Vector<Race> bestRaces = new Vector();

        Collections.sort(races, new RaceTimeComparator());

        Vector<Race> racesForTeamId = races.stream().filter(r -> r.getTeamID() == teamId).collect(Collectors.toCollection(Vector::new));

        //Iterate over races per team to collect the fastest ones and store them in bestRaces
        racesForTeamId.stream().forEach(race -> {
                    //Check if we should add another race, limit is specified by the const numRacesToInclude
                    boolean addAnother = bestRaces.size() < numRacesToInclude;
                    //Any missing gender?
                    Gender missingGender = getMissingGender(bestRaces);
                    //Only add any gender if we are not at the last race to add, and we are not missing any gender to qualify for the "Mixed" criteria
                    boolean addAnyGender = bestRaces.size() < numRacesToInclude-1 || getIsMixedClass(bestRaces);
                    //If we are adding the last race, we need to check if previous races is missing out a gender, then we only add it if it is of the correct gender,
                    //otherwise we continue the iteration to look for a race with the correct gender
                    boolean addExplicitGender = bestRaces.size() == numRacesToInclude-1 && !getIsMixedClass(bestRaces) && classGenderMap.get(race.getClassID()) == missingGender;
                    if(addAnother && addAnyGender || addExplicitGender)
                        bestRaces.add(race);
                }
        );
        //Return an empty Vector if we don´t find enough races to qualify the team for mixed class
        //Also, returns empty if a team is mixed but not have enough riders to meet the numRacesToInclude
        return bestRaces.size() < numRacesToInclude ? new Vector() : bestRaces;
    }
}
