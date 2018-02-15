package psmc.hw1;

public class Main {
    enum Mode {
        SEQUENTIAL,
        PARALLEL,
    }

    enum InputType {
        DECREASING,
        RANDOM;
    }

    public static void main(String[] args) {
        Mode mode = Mode.SEQUENTIAL;
        InputType inputType = InputType.RANDOM;
        int inputSize = 100;
        Utils.Printer P = new Utils.Printer();
        boolean countForks = false;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--parallel") || args[i].equals("-p")) {
                mode = Mode.PARALLEL;
                Parallel.setMergeMode(Parallel.MergeMode.SEQUENTIAL);
            }
            else if (args[i].equals("--psmc.hw1.Parallel") || args[i].equals("-P")) {
                mode = Mode.PARALLEL;
                Parallel.setMergeMode(Parallel.MergeMode.PARALLEL);
            }
            else if (args[i].equals("--sequential") || args[i].equals("-s")) {
                mode = Mode.SEQUENTIAL;
            }
            else if (args[i].equals("--decreasing") || args[i].equals("-d")) {
                inputType = InputType.DECREASING;
            }
            else if (args[i].equals("--random") || args[i].equals("-r")) {
                inputType = InputType.RANDOM;
            }
            else if (args[i].equals("--verbose") || args[i].equals("-v")) {
                P.verbose = true;
            }
            else if (args[i].equals("-n")) {
                if (i != args.length-1) {
                    inputSize = Integer.valueOf(args[i+1]);
                    i++; // Skip next argument
                } else {
                    throw new IllegalArgumentException();
                }
            }
            else if (args[i].equals("-c")) {
                if (i != args.length-1) {
                    int c = Integer.valueOf(args[i+1]);
                    Parallel.setCutoff(c);
                    i++; // Skip next argument
                } else {
                    throw new IllegalArgumentException();
                }
            }
            else if (args[i].equals("-g") || args[i].equals("--graph")) {
                if (i != args.length-1) {
                    Parallel.setDoForkGraph(true);
                    Parallel.setGraphPath(args[i+1]);
                    i++;
                } else {
                    throw new IllegalArgumentException();
                }
            }
            else if (args[i].equals("-f") || args[i].equals("--forks")) {
                Parallel.setDoCountFork(true);
                countForks = true;
            }
        }

        P.println("Generating " + inputSize + " elements to sort...");
        int[] n = inputType == InputType.RANDOM ?
                Inputs.random(inputSize, 0, inputSize) :
                Inputs.decreasing(inputSize);
        P.println("Sorting...");
        long startTime = System.currentTimeMillis();
        if (mode == Mode.SEQUENTIAL) {
            Sequential.mergeSort(n);
        } else {
            Parallel.mergeSort(n);
        }
        long endTime = System.currentTimeMillis();
        System.out.print("Execution time: " + (endTime-startTime) + " ms");
        if (countForks) System.out.print("   Forks: " + Parallel.getForkCount());
        System.out.println();
        P.println(Check.ok(n) ? "n is correctly ordered!" : "n is not ordered!");
    }
}
