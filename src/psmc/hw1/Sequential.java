package psmc.hw1;

import java.util.Arrays;

class Sequential {
    static void merge(int arr[], int i, int m, int j) {
        int ii = 0, jj = 0, tot = i;
        int[] L = Arrays.copyOfRange(arr,i,m);
        int[] R = Arrays.copyOfRange(arr,m,j);
        while (ii < L.length && jj < R.length) {
            if (L[ii] <= R[jj]) {
                arr[tot] = L[ii];
                ii++;
            } else {
                arr[tot] = R[jj];
                jj++;
            }
            tot++;
        }
        while (ii < L.length) {
            arr[tot] = L[ii];
            ii++;
            tot++;
        }
        while (jj < R.length) {
            arr[tot] = R[jj];
            jj++;
            tot++;
        }
    }

    static void mergeSort(int[] arr, int i, int j) {
        int n = j-i+1;
        if (n <= 1) {
            return;
        } else if (n == 2) {
            if (arr[0] > arr[1]) {
                int tmp = arr[0];
                arr[0] = arr[1];
                arr[1] = tmp;
            }
            return;
        }

        int m = (i + j) / 2;
        mergeSort(arr, i, m);
        mergeSort(arr, m, j);
        merge(arr, i, m, j);
    }

    static void mergeSort(int[] arr) {
        mergeSort(arr,0,arr.length);
    }
}
