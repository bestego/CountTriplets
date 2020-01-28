/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package counttriplets;

import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.toList;

public class Solution {

    // Complete the countTriplets function below.
    static long countTriplets(List<Long> arr, long r) {

        long count = 0;

        class Position {

            int position;
            long count;

            Position(int position, long count) {
                this.position = position;
                this.count = count;
            }

            public int compareTo(Position other) {
                return position - other.position;
            }

            @Override
            public String toString() {
                return position + "(" + count + ")";
            }
        } // Position

        class Positions {

            List<Position> positions;

            Positions() {
                positions = new ArrayList<>();  // LinkedList MUCH slower!!!
            }

            public void add(int position) {
                positions.add(new Position(position, positions.size() + 1));
            }

            /**
             * Get number of positions upto (not inclusing) current position
             *
             * @param position current position
             * @return number of positions upto (not inclusing) current position
             */
            public long getLower(int position) {
                int index = Collections.binarySearch(positions, new Position(position, 0), (p1, p2) -> p1.position - p2.position);

                // matches first position in list
                if (index == -1) {
                    return 0;
                }
                // no match, take nearest lower position
                if (index < 0) {
                    index = -index - 2;
                    return positions.get(index).count;
                }
                // matches position in list, take nearest lower position
                if (index > 0) {
                    return positions.get(index - 1).count;
                }
                return 0;

            } // Positions

            /**
             * Get number of positions right of (not including) current position
             *
             * @param position current position
             * @return number of positions right of (not including) current
             * position
             */
            public long getHigher(int position) {
                int index = Collections.binarySearch(positions, new Position(position, 0), (p1, p2) -> p1.position - p2.position);

                // matches fist position in list
                if (index == -1) {
                    return positions.size();
                }
                // no match, take nearest lowest value 
                if (index < 0) {
                    index = -index - 2;
                    return positions.size() - positions.get(index).count;
                }
                // matcges position is list
                if (index > 0) {
                    return positions.size() - positions.get(index).count;
                }
                return 0;
            }

            @Override
            public String toString() {
                String s = "";
                for (Position p : positions) {
                    s = s + p + ",";
                }
                return s;
            }
        }

        Map<Long, Positions> map = new HashMap<>();
        Positions positions;

        // initial pass creating supporting data sttructure
        for (int i = 0; i < arr.size(); i++) {
            // get current value
            Long value = arr.get(i);

            positions = map.get(value);
            if (positions == null) {
                // if new value
                // create new positions list
                positions = new Positions();
                map.put(value, positions);
            }
            // add position to list and accumulative count
            positions.add(i);
            //System.out.println(value + ": " + positions);
        }

        // main run to calculate number of triplets
        for (int i = 1; i < arr.size() - 1; i++) {
            Long value = arr.get(i);

            // for current position, look to the number of matching values to the left
            Positions left;
            long lower = 0;
            if ((value % r == 0) && (left = map.get(value / r)) != null) {
                lower = left.getLower(i);
            }

            // for current position, look to the number of matching values to the right
            Positions right;
            long higher = 0;
            if ((right = map.get(value * r)) != null) {
                higher = right.getHigher(i);
            }

            // calculate number of triplet for current position
            count = count + lower * higher;
        }
        return count;
    }

    public static void main(String[] args) throws IOException {
        
        long startTime = System.currentTimeMillis();
        
        long n;
        int r;
        List<Long> arr;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(args[0]))) {

            String[] nr = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");
            r = Integer.parseInt(nr[0]);
            n = Long.parseLong(nr[1]);
            arr = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                    .map(Long::parseLong)
                    .collect(toList());
        }
        
        long timeLapse1 = System.currentTimeMillis();
        long result = countTriplets(arr, r);
        long timeLapse2 = System.currentTimeMillis();
        double readingTime = (timeLapse1-startTime)/1000.0;
        double calculationTime = (timeLapse2 - timeLapse1)/1000.0;
        double totalTime = (timeLapse2-startTime)/1000.0;
        System.out.println("Number of triplets: " + result);
        System.out.println("reading:" + readingTime + "s + calculating:" + calculationTime + "s = total:" + totalTime+"s");

    }
}
