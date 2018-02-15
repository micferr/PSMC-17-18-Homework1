package psmc.hw1;

class Utils {
    static void print(int[] arr) {
        for (int l : arr) {
            System.out.print(l+" ");
        }
        System.out.println();
    }

    static class Printer {
        boolean verbose = false;

        void println(String s) {
            if (verbose) {
                System.out.println(s);
            }
        }

        void printArray(int[] arr) {
            if (verbose) {
                Utils.print(arr);
            }
        }
    }
}
