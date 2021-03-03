package net.herospvp.splitter.utils;

import java.util.Comparator;

public class Sorting {

    public static <K> void quickSort(K[] array, Comparator<K> comparator, int a, int b) {

        if (a >= b) return;

        int left = a;
        int right = b - 1;

        K pivot = array[b];
        K temp;

        while (left <= right) {

            while (left <= right && comparator.compare(array[left], pivot) < 0) left++;
            while (left <= right && comparator.compare(array[right], pivot) > 0) right--;

            if (left <= right) {
                temp = array[left]; array[left] = array[right]; array[right] = temp;
                left++; right--;
            }

            temp = array[left]; array[left] = array[b]; array[b] = temp;

            quickSort(array, comparator, a, left - 1);
            quickSort(array, comparator, left + 1, b);
        }

    }

}
