package Model;

public class TileFactory {
    private static int[] generateRandomIndices(int gridSize, int limit) {
        int[] randomNumbers = new int[limit];
        boolean[] used = new boolean[gridSize * gridSize];
        int count = 0;

        while (count < limit) {
            int random = (int) (Math.random() * (gridSize * gridSize)) + 1;
            if (!used[random - 1]) {
                randomNumbers[count++] = random;
                used[random - 1] = true;
            }
        }

        return randomNumbers;
    }

    public static Tile createTile(int count, double x, double y, double width, double height, int[] supriseJumpTiles, int[] questionTiles) {
        if (contains(supriseJumpTiles, count)) {
            return new Tile(count, Tile.TileType.SURPRISE_JUMP, x, y, width, height);
        } else if (contains(questionTiles, count)) {
            return new Tile(count, Tile.TileType.QUESTION, x, y, width, height);
        } else {
            return new Tile(count, Tile.TileType.NORMAL, x, y, width, height);
        }
    }

    private static boolean contains(int[] array, int element) {
        for (int value : array) {
            if (value == element) {
                return true;
            }
        }
        return false;
    }

    public static int[] generateRandomNumbers(int gridSize, int limit) {
        return generateRandomIndices(gridSize, limit);
    }
}
