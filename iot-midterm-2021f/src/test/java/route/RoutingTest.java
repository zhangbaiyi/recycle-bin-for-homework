package route;

import helper.Coordinate;
import helper.Directions;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

class RoutingTest {

    List<Coordinate> selectedCoordinate;
    List<Coordinate> finalRoute;
    List<Coordinate> remainingCoordinates;
    Directions directions;

    @Test
    void regularNodeStartRoutingTest() {
        selectedCoordinate = new ArrayList<>();
        selectedCoordinate.add(new Coordinate(1,2));
        selectedCoordinate.add(new Coordinate(2,1));
        selectedCoordinate.add(new Coordinate(2,4));
        selectedCoordinate.add(new Coordinate(3,3));
        selectedCoordinate.add(new Coordinate(5,4));
        selectedCoordinate.add(new Coordinate(4,6));
        selectedCoordinate.add(new Coordinate(6,5));
        selectedCoordinate.add(new Coordinate(3,9));
        selectedCoordinate.add(new Coordinate(2,10));
        selectedCoordinate.add(new Coordinate(6,10));

        finalRoute = new ArrayList<>();
        remainingCoordinates = new ArrayList<>();
        directions = new Directions();

        selectedCoordinate.sort(Coordinate::compareTo);
        for (Coordinate coordinate : selectedCoordinate) {
            remainingCoordinates.add(new Coordinate(coordinate.getRow(), coordinate.getCol()));
        }

        finalRoute.add(new Coordinate(selectedCoordinate.get(0).getRow(), selectedCoordinate.get(0).getCol()));
        remainingCoordinates.remove(0);

        System.out.println("selected: "+selectedCoordinate);
        System.out.println("init remaining: "+remainingCoordinates);

        while (!remainingCoordinates.isEmpty()) {
            Coordinate currentCoordinate = new Coordinate(finalRoute.get(finalRoute.size() - 1).getRow(), finalRoute.get(finalRoute.size() - 1).getCol());
            Double[] nextHopTotalDistances = new Double[8];
            boolean skipFlag = false;
            if (finalRoute.size() > 96) {
                finalRoute = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 24; j++) {
                        if (j < 12) {
                            finalRoute.add(new Coordinate((i * 24 + j) / 12, (i * 24 + j) % 12));
                        } else {
                            finalRoute.add(new Coordinate((i * 24 + j) / 12, (11 - (i * 24 + j) % 12)));
                        }
                    }
                }
                break;
            }
            for (int i = 0; i < directions.getDirections().size(); i++) {
                Coordinate currentNextCoor = new Coordinate(currentCoordinate.getRow() + directions.getDirections().get(i)[0], currentCoordinate.getCol() + directions.getDirections().get(i)[1]);
                int nextCorInRemainIndex;
                nextCorInRemainIndex = nextCoorInRemainingCoor(currentNextCoor, remainingCoordinates);
                if (nextCorInRemainIndex != -1) {
                    remainingCoordinates.remove(nextCorInRemainIndex);
                    finalRoute.add(currentNextCoor);
                    skipFlag = true;
                    break;
                }

                Double currentCoorTotalDistance = 0.0;
                for (Coordinate remainingCoordinate : remainingCoordinates) {
                    currentCoorTotalDistance += distance(currentNextCoor, remainingCoordinate);
                    if (currentCoorTotalDistance.isInfinite() || currentCoorTotalDistance.isNaN()) {
                        break;
                    }
                }
                nextHopTotalDistances[i] = currentCoorTotalDistance;
            }

            if (skipFlag) {
                continue;
            }

            int selectedNextHopDirectionIndex = 0;
            for (int i = 0; i < directions.getDirections().size(); i++) {
                if (nextHopTotalDistances[selectedNextHopDirectionIndex] > nextHopTotalDistances[i]) {
                    selectedNextHopDirectionIndex = i;
                }
            }

            Coordinate selectedNextHopCoor = new Coordinate(currentCoordinate.getRow() + directions.getDirections().get(selectedNextHopDirectionIndex)[0], currentCoordinate.getCol() + directions.getDirections().get(selectedNextHopDirectionIndex)[1]);
            int toRemoveIndex = -1;
            for (int i = 0; i < remainingCoordinates.size(); i++) {
                if (remainingCoordinates.get(i).getRow() == selectedNextHopCoor.getRow() && remainingCoordinates.get(i).getCol() == selectedNextHopCoor.getCol()) {
                    toRemoveIndex = i;
                    break;
                }
            }
            if (toRemoveIndex >= 0 && toRemoveIndex < remainingCoordinates.size()) {
                remainingCoordinates.remove(toRemoveIndex);
            }
            finalRoute.add(selectedNextHopCoor);
            System.out.println("while-loop info: "+currentCoordinate + " finds next hop: " + selectedNextHopCoor);
        }
        System.out.println(finalRoute);

    }

    @Test
    void boundaryNodeStartRoutingTest_1() {
        selectedCoordinate = new ArrayList<>();
        for(int i=0;i<12;i++){
            selectedCoordinate.add(new Coordinate(0,i));
        }

        finalRoute = new ArrayList<>();
        remainingCoordinates = new ArrayList<>();
        directions = new Directions();

        selectedCoordinate.sort(Coordinate::compareTo);
        for (Coordinate coordinate : selectedCoordinate) {
            remainingCoordinates.add(new Coordinate(coordinate.getRow(), coordinate.getCol()));
        }

        finalRoute.add(new Coordinate(selectedCoordinate.get(0).getRow(), selectedCoordinate.get(0).getCol()));
        remainingCoordinates.remove(0);

        System.out.println("selected: "+selectedCoordinate);
        System.out.println("init remaining: "+remainingCoordinates);

        while (!remainingCoordinates.isEmpty()) {
            Coordinate currentCoordinate = new Coordinate(finalRoute.get(finalRoute.size() - 1).getRow(), finalRoute.get(finalRoute.size() - 1).getCol());
            Double[] nextHopTotalDistances = new Double[8];
            boolean skipFlag = false;
            if (finalRoute.size() > 96) {
                finalRoute = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 24; j++) {
                        if (j < 12) {
                            finalRoute.add(new Coordinate((i * 24 + j) / 12, (i * 24 + j) % 12));
                        } else {
                            finalRoute.add(new Coordinate((i * 24 + j) / 12, (11 - (i * 24 + j) % 12)));
                        }
                    }
                }
                break;
            }
            for (int i = 0; i < directions.getDirections().size(); i++) {
                Coordinate currentNextCoor = new Coordinate(currentCoordinate.getRow() + directions.getDirections().get(i)[0], currentCoordinate.getCol() + directions.getDirections().get(i)[1]);
                int nextCorInRemainIndex;
                nextCorInRemainIndex = nextCoorInRemainingCoor(currentNextCoor, remainingCoordinates);
                if (nextCorInRemainIndex != -1) {
                    remainingCoordinates.remove(nextCorInRemainIndex);
                    finalRoute.add(currentNextCoor);
                    skipFlag = true;
                    break;
                }

                Double currentCoorTotalDistance = 0.0;
                for (Coordinate remainingCoordinate : remainingCoordinates) {
                    currentCoorTotalDistance += distance(currentNextCoor, remainingCoordinate);
                    if (currentCoorTotalDistance.isInfinite() || currentCoorTotalDistance.isNaN()) {
                        break;
                    }
                }
                nextHopTotalDistances[i] = currentCoorTotalDistance;
            }

            if (skipFlag) {
                continue;
            }

            int selectedNextHopDirectionIndex = 0;
            for (int i = 0; i < directions.getDirections().size(); i++) {
                if (nextHopTotalDistances[selectedNextHopDirectionIndex] > nextHopTotalDistances[i]) {
                    selectedNextHopDirectionIndex = i;
                }
            }

            Coordinate selectedNextHopCoor = new Coordinate(currentCoordinate.getRow() + directions.getDirections().get(selectedNextHopDirectionIndex)[0], currentCoordinate.getCol() + directions.getDirections().get(selectedNextHopDirectionIndex)[1]);
            int toRemoveIndex = -1;
            for (int i = 0; i < remainingCoordinates.size(); i++) {
                if (remainingCoordinates.get(i).getRow() == selectedNextHopCoor.getRow() && remainingCoordinates.get(i).getCol() == selectedNextHopCoor.getCol()) {
                    toRemoveIndex = i;
                    break;
                }
            }
            if (toRemoveIndex >= 0 && toRemoveIndex < remainingCoordinates.size()) {
                remainingCoordinates.remove(toRemoveIndex);
            }
            finalRoute.add(selectedNextHopCoor);
            System.out.println("while-loop info: "+currentCoordinate + " finds next hop: " + selectedNextHopCoor);
        }
        System.out.println(finalRoute);

    }

    @Test
    void boundaryNodeStartRoutingTest_2() {
        selectedCoordinate = new ArrayList<>();
        for(int i=0;i<8;i++){
            selectedCoordinate.add(new Coordinate(i,0));
        }

        finalRoute = new ArrayList<>();
        remainingCoordinates = new ArrayList<>();
        directions = new Directions();

        selectedCoordinate.sort(Coordinate::compareTo);
        for (Coordinate coordinate : selectedCoordinate) {
            remainingCoordinates.add(new Coordinate(coordinate.getRow(), coordinate.getCol()));
        }

        finalRoute.add(new Coordinate(selectedCoordinate.get(0).getRow(), selectedCoordinate.get(0).getCol()));
        remainingCoordinates.remove(0);

        System.out.println("selected: "+selectedCoordinate);
        System.out.println("init remaining: "+remainingCoordinates);

        while (!remainingCoordinates.isEmpty()) {
            Coordinate currentCoordinate = new Coordinate(finalRoute.get(finalRoute.size() - 1).getRow(), finalRoute.get(finalRoute.size() - 1).getCol());
            Double[] nextHopTotalDistances = new Double[8];
            boolean skipFlag = false;
            if (finalRoute.size() > 96) {
                finalRoute = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 24; j++) {
                        if (j < 12) {
                            finalRoute.add(new Coordinate((i * 24 + j) / 12, (i * 24 + j) % 12));
                        } else {
                            finalRoute.add(new Coordinate((i * 24 + j) / 12, (11 - (i * 24 + j) % 12)));
                        }
                    }
                }
                break;
            }
            for (int i = 0; i < directions.getDirections().size(); i++) {
                Coordinate currentNextCoor = new Coordinate(currentCoordinate.getRow() + directions.getDirections().get(i)[0], currentCoordinate.getCol() + directions.getDirections().get(i)[1]);
                int nextCorInRemainIndex;
                nextCorInRemainIndex = nextCoorInRemainingCoor(currentNextCoor, remainingCoordinates);
                if (nextCorInRemainIndex != -1) {
                    remainingCoordinates.remove(nextCorInRemainIndex);
                    finalRoute.add(currentNextCoor);
                    skipFlag = true;
                    break;
                }

                Double currentCoorTotalDistance = 0.0;
                for (Coordinate remainingCoordinate : remainingCoordinates) {
                    currentCoorTotalDistance += distance(currentNextCoor, remainingCoordinate);
                    if (currentCoorTotalDistance.isInfinite() || currentCoorTotalDistance.isNaN()) {
                        break;
                    }
                }
                nextHopTotalDistances[i] = currentCoorTotalDistance;
            }

            if (skipFlag) {
                continue;
            }

            int selectedNextHopDirectionIndex = 0;
            for (int i = 0; i < directions.getDirections().size(); i++) {
                if (nextHopTotalDistances[selectedNextHopDirectionIndex] > nextHopTotalDistances[i]) {
                    selectedNextHopDirectionIndex = i;
                }
            }

            Coordinate selectedNextHopCoor = new Coordinate(currentCoordinate.getRow() + directions.getDirections().get(selectedNextHopDirectionIndex)[0], currentCoordinate.getCol() + directions.getDirections().get(selectedNextHopDirectionIndex)[1]);
            int toRemoveIndex = -1;
            for (int i = 0; i < remainingCoordinates.size(); i++) {
                if (remainingCoordinates.get(i).getRow() == selectedNextHopCoor.getRow() && remainingCoordinates.get(i).getCol() == selectedNextHopCoor.getCol()) {
                    toRemoveIndex = i;
                    break;
                }
            }
            if (toRemoveIndex >= 0 && toRemoveIndex < remainingCoordinates.size()) {
                remainingCoordinates.remove(toRemoveIndex);
            }
            finalRoute.add(selectedNextHopCoor);
            System.out.println("while-loop info: "+currentCoordinate + " finds next hop: " + selectedNextHopCoor);
        }
        System.out.println("result: "+finalRoute);

    }

    @Test
    void boundaryNodeStartRoutingTest_3() {
        selectedCoordinate = new ArrayList<>();
        selectedCoordinate.add(new Coordinate(2,0));
        selectedCoordinate.add(new Coordinate(3,0));
        selectedCoordinate.add(new Coordinate(0,5));
        selectedCoordinate.add(new Coordinate(0,6));
        selectedCoordinate.add(new Coordinate(7,5));
        selectedCoordinate.add(new Coordinate(7,6));

        finalRoute = new ArrayList<>();
        remainingCoordinates = new ArrayList<>();
        directions = new Directions();

        selectedCoordinate.sort(Coordinate::compareTo);
        for (Coordinate coordinate : selectedCoordinate) {
            remainingCoordinates.add(new Coordinate(coordinate.getRow(), coordinate.getCol()));
        }

        finalRoute.add(new Coordinate(selectedCoordinate.get(0).getRow(), selectedCoordinate.get(0).getCol()));
        remainingCoordinates.remove(0);

        System.out.println("selected: "+selectedCoordinate);
        System.out.println("init remaining: "+remainingCoordinates);

        while (!remainingCoordinates.isEmpty()) {
            Coordinate currentCoordinate = new Coordinate(finalRoute.get(finalRoute.size() - 1).getRow(), finalRoute.get(finalRoute.size() - 1).getCol());
            Double[] nextHopTotalDistances = new Double[8];
            boolean skipFlag = false;
            if (finalRoute.size() > 96) {
                finalRoute = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 24; j++) {
                        if (j < 12) {
                            finalRoute.add(new Coordinate((i * 24 + j) / 12, (i * 24 + j) % 12));
                        } else {
                            finalRoute.add(new Coordinate((i * 24 + j) / 12, (11 - (i * 24 + j) % 12)));
                        }
                    }
                }
                break;
            }
            for (int i = 0; i < directions.getDirections().size(); i++) {
                Coordinate currentNextCoor = new Coordinate(currentCoordinate.getRow() + directions.getDirections().get(i)[0], currentCoordinate.getCol() + directions.getDirections().get(i)[1]);
                int nextCorInRemainIndex;
                nextCorInRemainIndex = nextCoorInRemainingCoor(currentNextCoor, remainingCoordinates);
                if (nextCorInRemainIndex != -1) {
                    remainingCoordinates.remove(nextCorInRemainIndex);
                    finalRoute.add(currentNextCoor);
                    skipFlag = true;
                    break;
                }

                Double currentCoorTotalDistance = 0.0;
                for (Coordinate remainingCoordinate : remainingCoordinates) {
                    currentCoorTotalDistance += distance(currentNextCoor, remainingCoordinate);
                    if (currentCoorTotalDistance.isInfinite() || currentCoorTotalDistance.isNaN()) {
                        break;
                    }
                }
                nextHopTotalDistances[i] = currentCoorTotalDistance;
            }

            if (skipFlag) {
                continue;
            }

            int selectedNextHopDirectionIndex = 0;
            for (int i = 0; i < directions.getDirections().size(); i++) {
                if (nextHopTotalDistances[selectedNextHopDirectionIndex] > nextHopTotalDistances[i]) {
                    selectedNextHopDirectionIndex = i;
                }
            }

            Coordinate selectedNextHopCoor = new Coordinate(currentCoordinate.getRow() + directions.getDirections().get(selectedNextHopDirectionIndex)[0], currentCoordinate.getCol() + directions.getDirections().get(selectedNextHopDirectionIndex)[1]);
            int toRemoveIndex = -1;
            for (int i = 0; i < remainingCoordinates.size(); i++) {
                if (remainingCoordinates.get(i).getRow() == selectedNextHopCoor.getRow() && remainingCoordinates.get(i).getCol() == selectedNextHopCoor.getCol()) {
                    toRemoveIndex = i;
                    break;
                }
            }
            if (toRemoveIndex >= 0 && toRemoveIndex < remainingCoordinates.size()) {
                remainingCoordinates.remove(toRemoveIndex);
            }
            finalRoute.add(selectedNextHopCoor);
            System.out.println("while-loop info: "+currentCoordinate + " finds next hop: " + selectedNextHopCoor);
        }
        System.out.println("result: "+finalRoute);

    }

    @Test
    void allNodeStartRoutingTest() {
        selectedCoordinate = new ArrayList<>();
        for(int i=0;i<96;i++){
            selectedCoordinate.add(new Coordinate(i/12,i%12));
        }

        finalRoute = new ArrayList<>();
        remainingCoordinates = new ArrayList<>();
        directions = new Directions();

        selectedCoordinate.sort(Coordinate::compareTo);
        for (Coordinate coordinate : selectedCoordinate) {
            remainingCoordinates.add(new Coordinate(coordinate.getRow(), coordinate.getCol()));
        }

        finalRoute.add(new Coordinate(selectedCoordinate.get(0).getRow(), selectedCoordinate.get(0).getCol()));
        remainingCoordinates.remove(0);

        System.out.println("selected: "+selectedCoordinate);
        System.out.println("init remaining: "+remainingCoordinates);

        while (!remainingCoordinates.isEmpty()) {
            Coordinate currentCoordinate = new Coordinate(finalRoute.get(finalRoute.size() - 1).getRow(), finalRoute.get(finalRoute.size() - 1).getCol());
            Double[] nextHopTotalDistances = new Double[8];
            boolean skipFlag = false;
            if (finalRoute.size() > 96) {
                finalRoute = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 24; j++) {
                        if (j < 12) {
                            finalRoute.add(new Coordinate((i * 24 + j) / 12, (i * 24 + j) % 12));
                        } else {
                            finalRoute.add(new Coordinate((i * 24 + j) / 12, (11 - (i * 24 + j) % 12)));
                        }
                    }
                }
                break;
            }

            for (int i = 0; i < directions.getDirections().size(); i++) {
                Coordinate currentNextCoor = new Coordinate(currentCoordinate.getRow() + directions.getDirections().get(i)[0], currentCoordinate.getCol() + directions.getDirections().get(i)[1]);
                int nextCorInRemainIndex;
                nextCorInRemainIndex = nextCoorInRemainingCoor(currentNextCoor, remainingCoordinates);
                if (nextCorInRemainIndex != -1) {
                    remainingCoordinates.remove(nextCorInRemainIndex);
                    finalRoute.add(currentNextCoor);
                    skipFlag = true;
                    break;
                }

                Double currentCoorTotalDistance = 0.0;
                for (Coordinate remainingCoordinate : remainingCoordinates) {
                    currentCoorTotalDistance += distance(currentNextCoor, remainingCoordinate);
                    if (currentCoorTotalDistance.isInfinite() || currentCoorTotalDistance.isNaN()) {
                        break;
                    }
                }
                nextHopTotalDistances[i] = currentCoorTotalDistance;
            }

            if (skipFlag) {
                continue;
            }

            int selectedNextHopDirectionIndex = 0;
            for (int i = 0; i < directions.getDirections().size(); i++) {
                if (nextHopTotalDistances[selectedNextHopDirectionIndex] > nextHopTotalDistances[i]) {
                    selectedNextHopDirectionIndex = i;
                }
            }

            Coordinate selectedNextHopCoor = new Coordinate(currentCoordinate.getRow() + directions.getDirections().get(selectedNextHopDirectionIndex)[0], currentCoordinate.getCol() + directions.getDirections().get(selectedNextHopDirectionIndex)[1]);
            int toRemoveIndex = -1;
            for (int i = 0; i < remainingCoordinates.size(); i++) {
                if (remainingCoordinates.get(i).getRow() == selectedNextHopCoor.getRow() && remainingCoordinates.get(i).getCol() == selectedNextHopCoor.getCol()) {
                    toRemoveIndex = i;
                    break;
                }
            }
            if (toRemoveIndex >= 0 && toRemoveIndex < remainingCoordinates.size()) {
                remainingCoordinates.remove(toRemoveIndex);
            }
            finalRoute.add(selectedNextHopCoor);
            System.out.println("while-loop info: "+currentCoordinate + " finds next hop: " + selectedNextHopCoor);
        }
        System.out.println("result: "+finalRoute);

    }

    private int nextCoorInRemainingCoor(Coordinate next, List<Coordinate> remains) {
        for (int i = 0; i < remains.size(); i++) {
            if (next.getRow() == remains.get(i).getRow() && next.getCol() == remains.get(i).getCol()) {
                return i;
            }
        }
        return -1;
    }

    private Boolean isInRange(Coordinate c1, Coordinate c2) {
        return c1.getCol() >= 0 && c1.getRow() >= 0 && c2.getCol() >= 0 && c2.getRow() >= 0 && c1.getRow() < 8 && c1.getCol() < 12 && c2.getRow() < 8 && c2.getCol() < 12;
    }

    private Double distance(Coordinate c1, Coordinate c2) {
        if (isInRange(c1, c2)) {
            return Math.sqrt(Math.pow(c1.getRow() - c2.getRow(), 2) + Math.pow(c1.getCol() - c2.getCol(), 2));
        } else {
            return Double.MAX_VALUE;
        }
    }
}