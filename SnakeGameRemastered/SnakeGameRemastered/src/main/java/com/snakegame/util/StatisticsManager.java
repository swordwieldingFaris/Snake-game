package com.snakegame.util;

import com.snakegame.model.GameStatistics;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Handles persistence for lifetime game statistics.
 */
public final class StatisticsManager {
    private static final String FILE_NAME = "player-stats.dat";

    private StatisticsManager() {
    }

    public static GameStatistics load() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (GameStatistics) in.readObject();
        } catch (Exception ignored) {
            return new GameStatistics();
        }
    }

    public static void save(GameStatistics statistics) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(statistics);
        } catch (Exception ignored) {
        }
    }
}
