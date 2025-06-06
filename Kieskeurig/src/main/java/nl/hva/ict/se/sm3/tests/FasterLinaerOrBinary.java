package nl.hva.ict.se.sm3.tests;

public class FasterLinaerOrBinary {
    public static int linearSearch(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) return i;
        }
        return -1;
    }

    public static int binarySearch(int[] arr, int target) {
        int left = 0, right = arr.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] == target) return mid;
            else if (arr[mid] < target) left = mid + 1;
            else right = mid - 1;
        }
        return -1;
    }

    public static void main(String[] args) {
        System.out.printf("%-10s %-10s %-15s %-15s %-10s\n",
                "Size", "Index(%)", "Linear (ms)", "Binary (ms)", "Faster");

        for (int size = 10; size <= 1_000_000; size *= 10) {
            int[] array = new int[size];
            for (int i = 0; i < size; i++) array[i] = i * 2;

            for (int pct = 0; pct <= 99; pct += 10) {
                int index = (int)((pct / 100.0) * size);
                if (index >= size) index = size - 1;

                int target = array[index];
                int runs = 50;
                long totalLinear = 0, totalBinary = 0;

                for (int r = 0; r < runs; r++) {
                    long startL = System.nanoTime();
                    linearSearch(array, target);
                    totalLinear += System.nanoTime() - startL;

                    long startB = System.nanoTime();
                    binarySearch(array, target);
                    totalBinary += System.nanoTime() - startB;
                }

                double avgLinear = totalLinear / runs / 1_000_000.0;
                double avgBinary = totalBinary / runs / 1_000_000.0;

                String winner = avgLinear < avgBinary ? "Linear" :
                        avgBinary < avgLinear ? "Binary" : "Equal";

                System.out.printf("%-10d %-10d %-15.5f %-15.5f %-10s\n",
                        size, pct, avgLinear, avgBinary, winner);
            }

            System.out.println();
        }
    }
}
