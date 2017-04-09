package trip;

import graph.DirectedGraph;
import graph.LabeledGraph;
import graph.SimpleShortestPaths;

import java.io.FileReader;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.FileNotFoundException;

import static trip.Main.error;

/**
 * Encapsulates a map containing sites, positions, and road distances between
 * them.
 *
 * @author Shrey Malhotra
 */
class Trip {

    /**
     * Read map file named NAME into out map graph.
     */
    void readMap(String name) {
        int n;
        n = 0;
        try {
            Scanner inp = new Scanner(new FileReader(name));
            while (inp.hasNext()) {
                n++;
                switch (inp.next()) {
                case "L":
                    addLocation(inp.next(), inp.nextDouble(), inp.nextDouble());
                    break;
                case "R":
                    addRoad(inp.next(), inp.next(), inp.nextDouble(),
                            Direction.parse(inp.next()), inp.next());
                    break;
                default:
                    error("map entry #%d: unknown type", n);
                    break;
                }
            }
        } catch (FileNotFoundException excp) {
            error(excp.getMessage());
        } catch (InputMismatchException excp) {
            error("bad entry #%d", n);
        } catch (NoSuchElementException excp) {
            error("entry incomplete at end of file");
        }
    }

    /**
     * Produce a report on the standard output of a shortest journey from
     * DESTS.get(0), then DESTS.get(1), ....
     */
    void makeTrip(List<String> dests) {
        if (dests.size() < 2) {
            error("must have at least two locations for a trip");
        }

        System.out.printf("From %s:%n%n", dests.get(0));
        int step;

        step = 1;
        for (int i = 1; i < dests.size(); i++) {
            Integer
                    from = _sites.get(dests.get(i - 1)),
                    to = _sites.get(dests.get(i));
            if (from == null) {
                error("No location named %s", dests.get(i - 1));
            } else if (to == null) {
                error("No location named %s", dests.get(i));
            }
            TripPlan plan = new TripPlan(from, to);
            plan.setPaths();
            List<Integer> segment = plan.pathTo(to);
            step = reportSegment(step, from, segment);
        }
    }

    /**
     * Print out a written description of the location sequence SEGMENT,
     * starting at FROM, and numbering the lines of the description starting
     * at SEQ.  That is, FROM and each item in SEGMENT are the
     * numbers of vertices representing locations.  Together, they
     * specify the starting point and vertices along a path where
     * each vertex is joined to the next by an edge.  Returns the
     * next sequence number.  The format is as described in the
     * project specification.  That is, each line but the last in the
     * segment is formated like this example:
     * 1. Take University_Ave west for 0.1 miles.
     * and the last like this:
     * 5. Take I-80 west for 8.4 miles to San_Francisco.
     * Addjacent roads with the same name and direction are combined.
     */
    int reportSegment(int seq, int from, List<Integer> segment) {
        double totaldist = 0;
        for (int i = 1; i < segment.size(); i++) {
            Road currpath = _map.getLabel(segment.get(i - 1), segment.get(i));
            Road nextpath = _map.getLabel(segment.get(i), segment.get(i + 1));
            String name = currpath.toString();
            Direction dir = currpath.direction();
            String currdirection = dir.fullName();
            double dist = currpath.length();
            if ((dir.equals(nextpath.direction()))
                    && (name.equals(nextpath.toString()))) {
                totaldist += dist;
                if (i == segment.size() - 2) {
                    totaldist += nextpath.length();
                    System.out.printf("%d. Take %s %s for %.1f miles to %s.%n",
                            seq, name, currdirection, totaldist,
                            _map.getLabel(segment.get(i + 1)).toString());
                    seq++;
                    break;
                }
                continue;
            }
            totaldist += dist;
            System.out.printf("%d. Take %s %s for %.1f miles.%n",
                    seq, name, currdirection, totaldist);
            totaldist = 0;
            seq++;
            if (i == segment.size() - 2) {
                totaldist += dist;
                System.out.printf("%d. Take %s %s for %.1f miles to %s.%n", seq,
                        nextpath.toString(), nextpath.direction().fullName(),
                        nextpath.length(),
                        _map.getLabel(segment.get(i + 1)).toString());
                seq++;
                break;
            }
        }
        return seq;
    }

    /**
     * Add a new location named NAME at (X, Y).
     */
    private void addLocation(String name, double x, double y) {
        if (_sites.containsKey(name)) {
            error("multiple entries for %s", name);
        }
        int v = _map.add(new Location(name, x, y));
        _sites.put(name, v);
    }

    /**
     * Add a stretch of road named NAME from the Location named FROM
     * to the location named TO, running in direction DIR, and
     * LENGTH miles long.  Add a reverse segment going back from TO
     * to FROM.
     */
    private void addRoad(String from, String name, double length,
                         Direction dir, String to) {
        Integer v0 = _sites.get(from),
                v1 = _sites.get(to);
        if (v0 == 0 || v1 == 0) {
            throw new AssertionError("Error: SOME KINDA ERROR");
        }
        if (v0 == null) {
            error("location %s not defined", from);
        } else if (v1 == null) {
            error("location %s not defined", to);
        }
        Road r1 = new Road(name, dir, length);
        _map.add(v0, v1, r1);
        Road r2 = new Road(name, dir.reverse(), length);
        _map.add(v1, v0, r2);
    }

    /**
     * Represents the network of Locations and Roads.
     */
    private RoadMap _map = new RoadMap();
    /**
     * Mapping of Location names to corresponding map vertices.
     */
    private HashMap<String, Integer> _sites = new HashMap<>();

    /**
     * A labeled directed graph of Locations whose edges are labeled by
     * Roads.
     */
    private static class RoadMap extends LabeledGraph<Location, Road> {
        /**
         * An empty RoadMap.
         */
        RoadMap() {
            super(new DirectedGraph());
        }
    }

    /**
     * Paths in _map from a given location.
     */
    private class TripPlan extends SimpleShortestPaths {
        /**
         * A plan for travel from START to DEST according to _map.
         */
        TripPlan(int start, int dest) {
            super(_map, start, dest);
            _finalLocation = _map.getLabel(dest);
        }

        @Override
        protected double getWeight(int u, int v) {
            if (!_map.contains(u, v)) {
                return Double.POSITIVE_INFINITY;
            }
            return _map.getLabel(u, v).length();
        }

        @Override
        protected double estimatedDistance(int v) {
            if (_map.contains(v)) {
                Location loc = _map.getLabel(v);
                return loc.dist(_finalLocation);
            }
            return 0.0;
        }

        /**
         * Location of the destination.
         */
        private final Location _finalLocation;

    }

}
