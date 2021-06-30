package algorithm.sort;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 归并排序
 * 分而治之
 * https://blog.51cto.com/flyingcat2013/1281026?xiangguantuijian&08
 */
public class MergeSort {

    public static void main(String[] args) {
//        int []arr = {9,8,7,6,5,4,3,2,1};
//        int []temp = new int[arr.length];//在排序前，先建好一个长度等于原数组长度的临时数组，避免递归中频繁开辟空间
        // sort(arr,0,arr.length-1,temp);

        //
        int[] arr = {3,3,4,5};
        int[] arr2 = {5,6,8,9,10};
        int[] result = new int[arr.length+arr2.length];
        mergeArr(arr,arr2,result);

        for (int i : result) {
            System.out.println(i);
        }
    }

    /**
     * 两个有序数组的有序合并
     * @param arr
     * @param arr2
     * @param result
     */
    public static  void mergeArr(int[] arr ,int[] arr2,int[] result){
        int i=0,j=0,k=0;

        while (i<arr.length&&j<arr2.length){
            if(arr[i]<arr2[j]){
                result[k++] =arr[i++];
            }else{
                result[k++] =arr2[j++];
            }
        }
        while(i<arr.length){
            result[k++] = arr[i++];
        }
        while(j<arr2.length){
            result[k++]=arr2[j++];
        }
    }
}
