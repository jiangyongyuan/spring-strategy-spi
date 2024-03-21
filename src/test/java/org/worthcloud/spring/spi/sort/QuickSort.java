package org.worthcloud.spring.spi.sort;

import org.springframework.stereotype.Service;
import org.worthcloud.spring.spi.SPIKey;

import java.util.Arrays;
import java.util.function.IntConsumer;

@Service
@SPIKey(value = "Quick")
public class QuickSort implements Sort{

    @Override
    public int[] sort(int[] list) {
        System.out.println("当前使用快排算法 : ");
        Arrays.stream(list).forEach(new IntConsumer() {
            @Override
            public void accept(int value) {
                System.out.print( value );
            }
        });
        quickSort( list , 0 , list.length -1);
        System.out.println("\r\n排序结果 : " );
        Arrays.stream(list).forEach(new IntConsumer() {
            @Override
            public void accept(int value) {
                System.out.print( value );
            }
        });
        return list;
    }

    public void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            // Partitioning index is where the array is partitioned into two halves
            int partitionIndex = partition(arr, low, high);

            // Recursively sort the elements before and after the partition index
            quickSort(arr, low, partitionIndex - 1);
            quickSort(arr, partitionIndex + 1, high);
        }
    }

    public int partition(int[] arr, int low, int high) {
        // Choosing the rightmost element as the pivot
        int pivot = arr[high];

        // Index of smaller element
        int i = (low - 1);

        // Traverse through all elements
        for (int j = low; j < high; j++) {
            // If the current element is smaller than or equal to the pivot
            if (arr[j] <= pivot) {
                i++;

                // Swap arr[i] and arr[j]
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        // Swap arr[i+1] and arr[high] (pivot)
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        // Return the partition index
        return i + 1;
    }
}
