package org.example.java_jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DataRetrieverTest {

    private DataRetriever dataRetriever;

    @BeforeEach
    void setup() {
        DBConnection dbConnection = new DBConnection();
        dataRetriever = new DataRetriever(dbConnection);
    }

    // a) Team findTeamById id = 1
    @Test
    void testFindTeamById_realMadrid() throws SQLException {
        Team team = dataRetriever.findTeamById(1);

        assertNotNull(team);
        assertEquals("Real Madrid", team.getName());
        assertEquals(3, team.getPlayers().size());
    }

    // b) Team findTeamById id = 5
    @Test
    void testFindTeamById_interMiami() throws SQLException {
        Team team = dataRetriever.findTeamById(5);

        assertNotNull(team);
        assertEquals("Inter Miami", team.getName());
        assertTrue(team.getPlayers().isEmpty());
    }

    // c) Pagination page=1 size=2
    @Test
    void testFindPlayers_page1_size2() throws SQLException {
        List<Players> players = dataRetriever.findPlayers(1, 2);

        assertEquals(2, players.size());
        assertEquals("Thibaut Courtois", players.get(0).getName());
        assertEquals("Dani Carvajal", players.get(1).getName());
    }

    // d) Pagination page=3 size=5
    @Test
    void testFindPlayers_emptyPage() throws SQLException {
        List<Players> players = dataRetriever.findPlayers(3, 5);

        assertTrue(players.isEmpty());
    }

    // e) findTeamsByPlayerName "an"
    @Test
    void testFindTeamsByPlayerName() throws SQLException {
        List<Team> teams = dataRetriever.findTeamsByPlayerName("an");

        assertEquals(2, teams.size());
        assertTrue(
                teams.stream().anyMatch(t -> t.getName().equals("Real Madrid"))
        );
        assertTrue(
                teams.stream().anyMatch(t -> t.getName().equals("Atletico Madrid"))
        );
    }

    // f) findPlayersByCriteria
    @Test
    void testFindPlayersByCriteria() throws SQLException {
        List<Players> players = dataRetriever.findPlayersByCriteria(
                "ud",
                Position.MIDF,
                "Madrid",
                Continent.EUROPA,
                1,
                10
        );

        assertEquals(1, players.size());
        assertEquals("Jude Bellingham", players.get(0).getName());
    }

    // g) createPlayers → RuntimeException
    @Test
    void testCreatePlayers_failAtomicity() {
        List<Players> players = List.of(
                new Players(null, "Jude Bellingham", 23, Position.STR, null),
                new Players(null, "Pedri", 24, Position.MIDF, null)
        );

        assertThrows(RuntimeException.class, () ->
                dataRetriever.createPlayers(players)
        );
    }

    // h) createPlayers → succès
    @Test
    void testCreatePlayers_success() throws SQLException {
        List<Players> players = List.of(
                new Players(null, "Vini", 25, Position.STR, null),
                new Players(null, "PedriNew", 24, Position.MIDF, null)
        );

        List<Players> result = dataRetriever.createPlayers(players);

        assertEquals(2, result.size());
    }

    // i) saveTeam → ajouter joueur
    @Test
    void testSaveTeam_addPlayer() throws SQLException {
        Team team = dataRetriever.findTeamById(1);

        Players newPlayer = new Players(6, "Vini", 25, Position.STR, null);
        team.getPlayers().add(newPlayer);

        Team updatedTeam = dataRetriever.saveTeam(team);

        assertTrue(
                updatedTeam.getPlayers().stream()
                        .anyMatch(p -> p.getName().equals("Vini"))
        );
    }

    // j) saveTeam → enlever tous les joueurs
    @Test
    void testSaveTeam_removeAllPlayers() throws SQLException {
        Team barca = dataRetriever.findTeamById(2);
        barca.setPlayers(List.of());

        Team updated = dataRetriever.saveTeam(barca);

        assertTrue(updated.getPlayers().isEmpty());
    }
}
