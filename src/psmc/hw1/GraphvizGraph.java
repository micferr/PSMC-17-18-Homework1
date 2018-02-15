package psmc.hw1;

import java.io.*;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class GraphvizGraph {
    public static class Edge {
        String from, to;

        Edge(String i, String j) {
            from = i;
            to = j;
        }
    }

    private AtomicInteger id = new AtomicInteger(0);
    private Set<Edge> edges = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public int getNewId() {
        return id.getAndAdd(1);
    }

    public void clearId() {
        id.set(0);
    }

    public void addEdge(String from, String to) { edges.add(new Edge(from, to)); }

    public Set<Edge> getEdges() { return Collections.unmodifiableSet(edges); }

    public void clear() { edges.clear(); }

    public void saveToFile(String filename) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(filename), "utf-8"))) {
            writer.write("digraph G {\n");
            for (Edge edge : edges) {
                writer.write("\t\"" + edge.from + "\" -> \"" + edge.to + "\"\n");
            }
            writer.write("}\n");
        } catch (IOException e) {
            System.err.println("Couldn't open " + filename + " :(");
        }
    }
}
