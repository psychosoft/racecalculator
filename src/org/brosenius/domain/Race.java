package org.brosenius.domain;
public class Race {
    private int classID;
    private int nameID;
    private int teamID;
    private int total;
    public int getClassID() {
        return classID;
    }
    public void setClassID(int classID) {
        this.classID = classID;
    }
    public int getNameID() {
        return nameID;
    }
    public void setNameID(int nameID) {
        this.nameID = nameID;
    }
    public int getTeamID() {
        return teamID;
    }
    public Race(int classID,  int teamID, int nameID, int total) {
        this.classID = classID;
        this.nameID = nameID;
        this.teamID = teamID;
        this.total = total;
    }
    public void setTeamID(int teamID) {
        this.teamID = teamID;
    }
    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }
}
