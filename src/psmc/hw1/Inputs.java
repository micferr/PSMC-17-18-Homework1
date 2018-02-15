package psmc.hw1;

public class Inputs {
    static int[] decreasing(int n) {
        int arr[] = new int[n];
        for (int i = 0; i < n; ++i) {
            arr[i] = n-i;
        }
        return arr;
    }

    static int[] random(int n, int min, int max) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = (int)(Math.random()*(max-min)) + min;
        }
        return arr;
    }
}
