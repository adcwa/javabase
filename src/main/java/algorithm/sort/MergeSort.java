package algorithm.sort;

/**
 * 归并排序
 * 分而治之
 * 后置递归
 * 学习参考：https://blog.51cto.com/flyingcat2013/1281026?xiangguantuijian&08
 * 图例：https://blog.51cto.com/u_15241496/2878520?xiangguantuijian&01
 * @author adcwa
 */
public class MergeSort {

    public static void main(String[] args) {

        // 理解归并前先理解如果快速的将两个有序数组排序后放到一个新的数组中，对理解归并排序非常有帮助，如下
        int[] arr1 = {3,3,4,5};
        int[] arr2 = {5,6,8,9,10};
        int[] result = new int[arr1.length+arr2.length];
        mergeArr(arr1,arr2,result);
        for (int i : result) {
            System.out.println(i);
        }

        // 合并对逻辑比较简单
        // 归并排序对理解：归并其实是递归、合并对简称
        // 递归：将数组递归拆分为两个子数组，直到不能再拆，进行最小单元数组对进行排序

        System.out.println("--------------");
        int[] arr = {6,5,4,3,2,1,11,13,12,7,9};
        int[] tmp = new int[arr.length];
        // arr.length-1 全用下标进行统一
        interSort(arr,tmp,0, arr.length-1);
        for (int i : arr) {
            System.out.println(i);
        }
    }

    public static void interSort(int[] arr,int[] tmp ,int left ,int right){
        // 递归条件，意思是还能拆，拆到一个或者两个为止
        if(left<right){
            // 此处没有真的再定义两个物理数组， 而是这一个数组中通过left、mid、right三个下标分成逻辑的两个数组区域
            int mid =(left+right)/2;

            interSort(arr,tmp,left,mid);
            interSort(arr,tmp,mid+1,right);
            mergeSort(arr,tmp,left,mid,right);
        }
    }

    public static void mergeSort(int[] arr ,int tmp[],int left ,int mid ,int right ){
        // 可以参考mergeArr方法
        // 数组arr1的区域  : left -> mid
        // 数组arr2的区域  : mid+1 -> right
        // i for arr1 ，
        // j for arr2
        // k for tmp
        int i=left,j=mid+1,k=left;
        // j<=right 判断可以保证单数个的时候，最后一个值也能被访问
        while(i<=mid&&j<=right){
            if(arr[i]<arr[j]){
                tmp[k++] =arr[i++];
            }else{
                tmp[k++] = arr[j++];
            }
        }

        while(i<=mid){
            tmp[k++] =arr[i++];
        }

        while(j<=right){
            tmp[k++] =arr[j++];
        }

        for(int m =left;m<=right;m++){
            arr[m]= tmp[m];
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
