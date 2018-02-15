package psmc.hw1;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Integer.max;
import static java.util.Arrays.binarySearch;

public class Parallel {
    enum MergeMode {
        SEQUENTIAL,
        PARALLEL
    }

    private static int cutoff = 5000;
    private static MergeMode mergeMode = MergeMode.SEQUENTIAL;
    private static ForkJoinPool fjp = new ForkJoinPool();
    private static GraphvizGraph compGraph = new GraphvizGraph();
    private static boolean doForkGraph = false;
    private static String graphFile = "graph.csv";
    private static boolean doCountFork = false;
    private static AtomicInteger forks = new AtomicInteger(0);

    static void setCutoff(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be greather than 0.");
        }
        cutoff = max(2,n);
    }

    static void setMergeMode(MergeMode mode) {
        mergeMode = mode;
    }

    static void setDoForkGraph(boolean doGraph) {
        doForkGraph = doGraph;
    }

    static void setGraphPath(String path) {
        graphFile = path;
    }

    static void setDoCountFork(boolean doCount) {
        doCountFork = doCount;
    }

    static int getForkCount() {
        if (!doCountFork) throw new IllegalStateException();
        else return forks.get();
    }

    private static void merge(int[] arr, int ai, int aj, int bi, int bj, int id) {
        int an = aj-ai;
        int bn = bj-bi;
        if (an <= 0 || bn <= 0) {
            return;
        }
        if (an+bn <= cutoff) {
            int[] workArr = new int[an+bn];
            System.arraycopy(arr,ai,workArr,0,an);
            System.arraycopy(arr,bi,workArr,an,bn);
            Sequential.merge(workArr,0,an,an+bn);
            System.arraycopy(workArr,0,arr,ai,an);
            System.arraycopy(workArr, an, arr, bi, bn);
            return;
        }

        int am,bm;
        if (an >= bn) {
            am = (ai+aj)/2;
            bm = binarySearch(arr,bi,bj,arr[am]);
            if (bm < 0) {
                bm = -(bm+1);
            }
        } else {
            bm = (bi+bj)/2;
            am = binarySearch(arr,ai,aj,arr[bm]);
            if (am < 0) {
                am = -(am+1);
            }
        }
        final int childId = doForkGraph ? compGraph.getNewId() : 0;
        final int final_am = am, final_bm = bm;
        ForkJoinTask task = fjp.submit(()->merge(arr,ai,final_am,bi,final_bm, childId));
        merge(arr,am,aj,bm,bj,id);
        task.join();

        if (doCountFork) forks.addAndGet(1);
        if (doForkGraph) {
            compGraph.addEdge(
                    "Thr. " +      id + " [" + ai + "-" + aj + "|" + bi + "-" + bj + ")",
                    "Thr. " +      id + " [" + am + "-" + aj + "|" + bm + "-" + bj + ")"
            );
            compGraph.addEdge(
                    "Thr. " +      id + " [" + ai + "-" + aj + "|" + bi + "-" + bj + ")",
                    "Thr. " + childId + " [" + ai + "-" + am + "|" + bi + "-" + bm + ")"
            );
        }

        // At this point, we have four segments, in order 1-3-2-4.
        // We have to swap 3 and 2

        int[] res = new int[aj-am + bm-bi];
        System.arraycopy(arr, bi, res, 0, bm-bi);
        System.arraycopy(arr, am, res, bm-bi, aj-am);
        System.arraycopy(res, 0, arr, am, aj-am);
        System.arraycopy(res, aj-am, arr, bi, bm-bi);
    }

    private static void merge(int[] arr, int i, int j, int id) {
        int m = (i+j)/2;
        if (j-i <= cutoff) {
            Sequential.merge(arr, i, m, j);
        } else {
            compGraph.addEdge(
                    "Thr. " + id + " [" + i + "-" + j + ")",
                    "Thr. " + id + " [" + i + "-" + m + "|" + m + "-" + j + ")"
            );
            merge(arr, i, m, m, j, id);
        }
    }

    private static int mergeSort(int[] arr, int i, int j, int id) {
        if ((j-i) <= cutoff) {
            Sequential.mergeSort(arr,i,j);
            return id;
        }

        int m = (i+j)/2;
        Integer childId = doForkGraph ? compGraph.getNewId() : 0;

        ForkJoinTask<Integer> task = fjp.submit(() -> mergeSort(arr, i, m, childId));
        mergeSort(arr, m, j, id);
        task.join();

        if (doCountFork) {
            forks.addAndGet(1);
        }
        if (doForkGraph) {
            String thisId =  "Thr. " + id + " [" + i + "-" + j + ")";
            String leftId =  "Thr. " + childId + " [" + i + "-" + m + ")";
            String rightId = "Thr. " + id + " [" + m + "-" + j + ")";
            compGraph.addEdge(thisId, leftId);
            compGraph.addEdge(thisId, rightId);
        }

        if (mergeMode == MergeMode.SEQUENTIAL) {
            Sequential.merge(arr,i,m,j);
        } else {
            Parallel.merge(arr,i,j,id);
        }
        return id;
    }

    public static void mergeSort(int[] arr) {
        if (doCountFork) forks.set(0);
        mergeSort(arr,0,arr.length,compGraph.getNewId());
        if (doForkGraph) {
            compGraph.saveToFile(graphFile);
            compGraph.clear();
        }
    }
}
