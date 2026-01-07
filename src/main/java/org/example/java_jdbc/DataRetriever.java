package org.example.java_jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {

    private DBConnection dbConnection;

    public DataRetriever(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    // a) findTeamById
    public Team findTeamById(Integer id) throws SQLException {
        Connection conn = dbConnection.getDBConnection();

        Team team = null;

        PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM team WHERE id = ?");
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            team = new Team(
                    rs.getInt("id"),
                    rs.getString("name"),
                    Continent.valueOf(rs.getString("continent"))
            );
        }

        if (team == null) return null;

        PreparedStatement psPlayers = conn.prepareStatement(
                "SELECT * FROM player WHERE id_team = ?");
        psPlayers.setInt(1, id);

        ResultSet rp = psPlayers.executeQuery();
        List<Players> players = new ArrayList<>();

        while (rp.next()) {
            players.add(new Players(
                    rp.getInt("id"),
                    rp.getString("name"),
                    rp.getInt("age"),
                    Position.valueOf(rp.getString("position")),
                    team
            ));
        }

        team.setPlayers(players);
        return team;
    }

    // b) Pagination
    public List<Players> findPlayers(int page, int size) throws SQLException {
        List<Players> players = new ArrayList<>();
        int offset = (page - 1) * size;

        Connection conn = dbConnection.getDBConnection();
        PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM player ORDER BY id LIMIT ? OFFSET ?");
        ps.setInt(1, size);
        ps.setInt(2, offset);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            players.add(new Players(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("age"),
                    Position.valueOf(rs.getString("position")),
                    null
            ));
        }
        return players;
    }

    // c) Atomicité
    public List<Players> createPlayers(List<Players> newPlayers) throws SQLException {
        Connection conn = dbConnection.getDBConnection();
        conn.setAutoCommit(false);

        try {
            for (Players p : newPlayers) {
                PreparedStatement check = conn.prepareStatement(
                        "SELECT id FROM player WHERE name = ?");
                check.setString(1, p.getName());
                if (check.executeQuery().next()) {
                    throw new RuntimeException("Player already exists");
                }

                PreparedStatement insert = conn.prepareStatement(
                        "INSERT INTO player(name, age, position) VALUES (?, ?, ?)");
                insert.setString(1, p.getName());
                insert.setInt(2, p.getAge());
                insert.setString(3, p.getPosition().name());
                insert.executeUpdate();
            }
            conn.commit();
            return newPlayers;
        } catch (Exception e) {
            conn.rollback();
            throw e;
        }
    }

    // d) saveTeam
    public Team saveTeam(Team team) throws SQLException {
        Connection conn = dbConnection.getDBConnection();

        PreparedStatement clear = conn.prepareStatement(
                "UPDATE player SET id_team = NULL WHERE id_team = ?");
        clear.setInt(1, team.getId());
        clear.executeUpdate();

        for (Players p : team.getPlayers()) {
            PreparedStatement add = conn.prepareStatement(
                    "UPDATE player SET id_team = ? WHERE id = ?");
            add.setInt(1, team.getId());
            add.setInt(2, p.getId());
            add.executeUpdate();
        }
        return team;
    }

    // e) findTeamsByPlayerName
    public List<Team> findTeamsByPlayerName(String name) throws SQLException {
        List<Team> teams = new ArrayList<>();
        Connection conn = dbConnection.getDBConnection();

        PreparedStatement ps = conn.prepareStatement(
                """
                SELECT DISTINCT t.*
                FROM team t
                JOIN player p ON p.id_team = t.id
                WHERE p.name ILIKE ?
                """
        );
        ps.setString(1, "%" + name + "%");

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            teams.add(new Team(
                    rs.getInt("id"),
                    rs.getString("name"),
                    Continent.valueOf(rs.getString("continent"))
            ));
        }
        return teams;
    }

    // f) findPlayersByCriteria
    public List<Players> findPlayersByCriteria(
            String playerName,
            Position position,
            String teamName,
            Continent continent,
            int page, int size) throws SQLException {

        List<Players> players = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT p.* FROM player p LEFT JOIN team t ON p.id_team = t.id WHERE 1=1 ");

        if (playerName != null) sql.append("AND p.name ILIKE ? ");
        if (position != null) sql.append("AND p.position = ? ");
        if (teamName != null) sql.append("AND t.name ILIKE ? ");
        if (continent != null) sql.append("AND t.continent = ? ");

        sql.append("LIMIT ? OFFSET ?");

        Connection conn = dbConnection.getDBConnection();
        PreparedStatement ps = conn.prepareStatement(sql.toString());

        int i = 1;
        if (playerName != null) ps.setString(i++, "%" + playerName + "%");
        if (position != null) ps.setString(i++, position.name());
        if (teamName != null) ps.setString(i++, "%" + teamName + "%");
        if (continent != null) ps.setString(i++, continent.name());

        ps.setInt(i++, size);
        ps.setInt(i, (page - 1) * size);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            players.add(new Players(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("age"),
                    Position.valueOf(rs.getString("position")),
                    null
            ));
        }
        return players;
    }

    public List<Players> getAllPlayers() throws SQLException {
        List<Players> players = new ArrayList<Players>();

        String sql = "SELECT id, name_player, goal_nb FROM player";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Players player = new Players();
            player.setId((int) rs.getLong("id"));
            player.setName(rs.getString("name"));

            // gestion du NULL
            Integer goalNb = rs.getObject("goal_nb", Integer.class);
            player.setGoalNb(goalNb);

            players.add(player);
        }

        return players;
    }
}
