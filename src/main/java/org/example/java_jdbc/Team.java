package org.example.java_jdbc;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private Integer id;
    private String name;
    private Continent continent;
    private List<Players> players = new ArrayList<>();

    public Team(Integer id, String name, Continent continent) {
        this.id = id;
        this.name = name;
        this.continent = continent;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Continent getContinent() {
        return continent;
    }

    public void setContinent(Continent continent) {
        this.continent = continent;
    }

    public List<Players> getPlayers() {
        return players;
    }

    public void setPlayers(List<Players> players) {
        this.players = players;
    }
}
