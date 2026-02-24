package hProjekt.controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import hProjekt.Config;

/**
 * Controller for managing the leaderboard functionality.
 * This class handles reading from, writing to, and initializing the leaderboard
 * CSV file.
 */
public class LeaderboardController {
    /**
     * Ensures the leaderboard CSV file exists.
     * If the file does not exist, it creates the file along with its parent
     * directories
     * and writes a header row to the file.
     */
    public static void initializeCsv() {
        try {
            if (!Files.exists(Config.CSV_PATH)) {
                Files.createDirectories(Config.CSV_PATH.getParent());
                final BufferedWriter writer = Files.newBufferedWriter(Config.CSV_PATH);
                writer.write("PlayerName,AI,Timestamp,Score\n"); // CSV Header
                writer.close();
            }
        } catch (final IOException e) {
            System.out.println("Couldn't create the leaderboard csv file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Appends a new player's data to the leaderboard CSV file.
     * Ensures the file is initialized before writing. Each entry includes
     * the player's name, AI status, a timestamp, and the player's score.
     *
     * @param playerName The name of the player.
     * @param score      The score achieved by the player.
     * @param ai         Indicates whether the player is an AI (true) or a human
     *                   (false).
     */
    @StudentImplementationRequired("P4.1")
    public static void savePlayerData(final String playerName, final int score, final boolean ai) {
        // TODO: P4.1
        org.tudalgo.algoutils.student.Student.crash("P4.1 - Remove if implemented");
    }

    /**
     * Reads the leaderboard data from the CSV file and loads it into a list of
     * LeaderboardEntry objects.
     *
     * @return A list of LeaderboardEntry objects containing player data from the
     *         CSV file.
     */
    @StudentImplementationRequired("P4.2")
    public static List<LeaderboardEntry> loadLeaderboardData() {
        // TODO: P4.2
        return org.tudalgo.algoutils.student.Student.crash("P4.2 - Remove if implemented");
    }
}
