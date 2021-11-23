package route;

import helper.Coordinate;
import helper.Directions;

import javax.swing.*;
import java.awt.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class Routing {
    List<Coordinate> selectedCoordinate;
    List<Coordinate> finalRoute;
    List<Coordinate> remainingCoordinates;
    Directions directions;
    Long delayTime;
    int routingDisplaySleepTimes;

    public Routing(List<Coordinate> coordinates, Long delayTime) {
        selectedCoordinate = coordinates;
        finalRoute = new ArrayList<>();
        remainingCoordinates = new ArrayList<>();
        directions = new Directions();
        this.delayTime = delayTime;
    }

    public void startRouting(Graphics g, JTextArea specsArea) {
        long startTime = System.currentTimeMillis();
        selectedCoordinate.sort(Coordinate::compareTo);
        for (Coordinate coordinate : selectedCoordinate) {
            remainingCoordinates.add(new Coordinate(coordinate.getRow(), coordinate.getCol()));
            g.setColor(Color.blue);
            g.drawOval(5 + coordinate.getCol() * 30, 5 + coordinate.getRow() * 30, 10, 10);
            g.setColor(Color.black);
        }

        finalRoute.add(new Coordinate(selectedCoordinate.get(0).getRow(), selectedCoordinate.get(0).getCol()));
        remainingCoordinates.remove(0);
        System.out.println(finalRoute);
        System.out.println(remainingCoordinates);
        boolean failFlag = false;
        while (!remainingCoordinates.isEmpty()) {
            Coordinate currentCoordinate = new Coordinate(finalRoute.get(finalRoute.size() - 1).getRow(), finalRoute.get(finalRoute.size() - 1).getCol());
            Double[] nextHopTotalDistances = new Double[8];
            boolean skipFlag = false;
            if (finalRoute.size() > 96) {
                failFlag = true;
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
                g.setColor(Color.LIGHT_GRAY);
                g.drawOval(5 + currentNextCoor.getCol() * 30, 5 + currentNextCoor.getRow() * 30, 10, 10);
                g.setColor(Color.blue);
                for (Coordinate coordinate : selectedCoordinate) {
                    g.drawOval(5 + coordinate.getCol() * 30, 5 + coordinate.getRow() * 30, 10, 10);
                }
                g.setColor(Color.BLACK);
                try {
                    TimeUnit.MILLISECONDS.sleep(50);
                    routingDisplaySleepTimes++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
            System.out.println("Break");
        }

        long endTime = System.currentTimeMillis();
        System.out.println(finalRoute);
        for (int i = 0; i < finalRoute.size() - 1; i++) {
            g.setColor(Color.red);
            g.drawOval(5 + finalRoute.get(i).getCol() * 30, 5 + finalRoute.get(i).getRow() * 30, 10, 10);
            g.setColor(Color.black);
            myDrawLine(g, finalRoute.get(i), finalRoute.get(i + 1));
            try {
                TimeUnit.SECONDS.sleep(delayTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        g.setColor(Color.red);
        g.drawOval(5 + finalRoute.get(finalRoute.size() - 1).getCol() * 30, 5 + finalRoute.get(finalRoute.size() - 1).getRow() * 30, 10, 10);
        g.setColor(Color.black);


        specsArea.setBackground(Color.yellow);
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        if (failFlag) {
            specsArea.setBackground(Color.red);
            specsArea.append("\nFailed to solve problem in limited time.");
            specsArea.append("\nThis is intelligence-less routing.");
        }
        specsArea.append("\n\nCalculating Time:\n" + (endTime - startTime - 50L * routingDisplaySleepTimes) + "ms");
        specsArea.append("\n\nFinal Route:\n" + finalRoute);
        specsArea.append("\n\nNon-Heap Memory Usage:\n" + memoryMXBean.getNonHeapMemoryUsage().toString());
        specsArea.append("\n\nHeap Memory Usage:\n" + memoryMXBean.getHeapMemoryUsage().toString());
        specsArea.append("\n\nFinal Route:\n" + finalRoute);

    }

    private void myDrawLine(Graphics g, Coordinate start, Coordinate end) {
        g.setColor(Color.GREEN);
        int StartX = 10 + start.getCol() * 30;
        int StartY = 10 + start.getRow() * 30;
        int EndX = 10 + end.getCol() * 30;
        int EndY = 10 + end.getRow() * 30;
        g.drawLine(StartX, StartY, EndX, EndY);
        g.setColor(Color.black);
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
