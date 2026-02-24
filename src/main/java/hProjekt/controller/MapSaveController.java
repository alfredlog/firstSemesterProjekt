package hProjekt.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import hProjekt.model.grid.HexGrid;
import hProjekt.model.grid.HexGridImpl;
import hProjekt.model.grid.Structure;
import hProjekt.model.grid.Tile;
import hProjekt.model.grid.TilePosition;
import hProjekt.model.mapEditor.SerializableHexGrid;

/**
 * Controller class responsible for persisting and retrieving map data.
 * <p>
 * This utility class handles the serialization and deserialization of maps
 * to and from the local file system. It manages operations saving, loading and
 * listing maps.
 * </p>
 * <p>
 * Maps are stored as binary files with a custom extension ({@code .tobago}) in
 * a specific resource directory relative to the project root (default:
 * {@code src/main/resources/maps}).
 * </p>
 */
public class MapSaveController {
    private static final String MAPS_DIRECTORY = "maps";
    private static final Path MAPS_PATH = Paths.get("../../src/main/resources", MAPS_DIRECTORY);
    private static final String MAP_FILE_EXTENSION = ".tobago";

    /**
     * Saves the current map state to a file with the specified name.
     * <p>
     * This method creates a {@link SerializableHexGrid} from the provided tiles and
     * structures and attempts to save it to a file located in the maps directory.
     * If the directory does not exist, it will be created. The file has the
     * {@code .tobago} extension.
     * </p>
     *
     * @param mapName    the name of the map file to begin creation (must not be
     *                   null or blank).
     * @param tiles      a map associating positions with tile types.
     * @param structures a map associating positions with structure types.
     * @return {@code true} if the map was saved successfully; {@code false} if the
     *         {@code mapName} is invalid (null/blank) or causes a path exception.
     */
    public static boolean saveMap(@NotNull final String mapName, final Map<TilePosition, Tile.Type> tiles,
            final Map<TilePosition, Structure.Type> structures) {
        if (mapName.isBlank()) {
            return false;
        }

        final SerializableHexGrid serializableHexGrid = new SerializableHexGrid(tiles, structures);

        try {
            Files.createDirectories(MAPS_PATH);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        try (final ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
                MAPS_PATH.resolve(mapName + MAP_FILE_EXTENSION).toFile()))) {
            oos.writeObject(serializableHexGrid);
            return true;
        } catch (final IOException e) {
            throw new RuntimeException(e);
        } catch (final InvalidPathException e) {
            return false;
        }
    }

    /**
     * Retrieves a list of names of all saved maps found in the configured maps
     * directory.
     *
     * @return A {@code List<String>} containing the names of all valid map files
     *         found, without their file extensions. Returns an empty list if the
     *         directory does not exist.
     */
    public static List<String> getSavedMaps() {
        try {
            if (Files.notExists(MAPS_PATH)) {
                return List.of();
            }
            return Files.list(MAPS_PATH)
                    .filter(path -> path.toString().endsWith(MAP_FILE_EXTENSION))
                    .map(path -> path.getFileName().toString().replace(MAP_FILE_EXTENSION, ""))
                    .toList();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads a {@link HexGrid} from a binary file associated with the given map
     * name.
     * <p>
     * This method attempts to read a serialized {@link SerializableHexGrid} object
     * from a file located in the maps directory. If successful, it
     * reconstructs and returns a {@link HexGridImpl}.
     * </p>
     *
     * @param mapName The name of the map to load without the file extension.
     *                Must not be {@code null} or blank.
     * @return The loaded {@link HexGrid}, or {@code null} if the map name is
     *         invalid, the file is not found, or the path is invalid.
     */
    public static @Nullable HexGrid loadMap(@NotNull final String mapName) {
        if (mapName.isBlank()) {
            return null;
        }

        try (final ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
                MAPS_PATH.resolve(mapName + MAP_FILE_EXTENSION).toFile()))) {
            final SerializableHexGrid serializableHexGrid = (SerializableHexGrid) ois.readObject();
            return new HexGridImpl(serializableHexGrid.tiles(), serializableHexGrid.structures());
        } catch (final FileNotFoundException | InvalidPathException e) {
            return null;
        } catch (final IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
