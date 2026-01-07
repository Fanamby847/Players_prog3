package org.example.java_jdbc;



public class Players {
    private Integer id;
    private String name;
    private Integer age;
    private Position position;
    private Team team;
    private Integer goalNb;

    public Players(Integer id, String name, Integer age, Position position, Team team) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.position = position;
        this.team = team;
    }

    public Players(){}

    public Integer getGoalNb() {
        return goalNb;
    }

    public void setGoalNb(Integer goalNb) {
        this.goalNb = goalNb;
    }

    public Integer getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Integer getAge() {
        return age;
    }
    public Position getPosition() {
        return position;
    }
    public Team getTeam() {
        return team;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
