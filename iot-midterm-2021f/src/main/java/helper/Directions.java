package helper;

import java.util.ArrayList;
import java.util.List;

public class Directions {
    List<int[]> directions;
    public Directions(){
        directions = new ArrayList<>();

        final int[] NORTH = {0,-1};
        directions.add(NORTH);

        final int[] NORTHEAST = {1,-1};
        directions.add(NORTHEAST);

        final int[] EAST = {1,0};
        directions.add(EAST);

        final int[] SOUTHEAST = {1,1};
        directions.add(SOUTHEAST);

        final int[] SOUTH = {0,1};
        directions.add(SOUTH);

        final int[] SOUTHWEST = {-1,1};
        directions.add(SOUTHWEST);

        final int[] WEST = {-1,0};
        directions.add(WEST);

        final int[] NORTHWEST = {-1,-1};
        directions.add(NORTHWEST);

    }

    public List<int[]> getDirections() {
        return directions;
    }
}
