package algorithm.sort;

/**
 * 快速排序
 * 设定一个基准， 使用第一个元素作为基准
 * 从后往前找数据，与基准比较，找到最靠前且小于基准的数据，替换首位元素，不过原首位已经缓存这基准中，不用担心丢失
 * 从首位开始，从前往后找比基准大的最靠后的数据，将该数据放入末位元素，不用担心丢失末位元素，末位元素已经存在首位中
 *
 */
public class QuickSort {

    public static void main(String[] args) {
        int[] arr = {5,7,6};
        quickSort(arr);
        for (int i : arr) {
            System.out.println(i);
        }
    }

    public static void quickSort(int[] arr){
        qsort(arr, 0, arr.length-1);
    }
    private static void qsort(int[] arr, int low, int high){
        System.out.println(String.format("qsort low %d high %d ",low,high));
        if (low < high){
            //将数组分为两部分
            int pivot=partition(arr, low, high);
            //递归排序左子数组
            qsort(arr, low, pivot-1);
            //递归排序右子数组
            qsort(arr, pivot+1, high);
        }
    }
    private static int partition(int[] arr, int low, int high){
        //枢轴记录
        int pivot = arr[low];
        while (low<high){
            while (low<high && arr[high]>=pivot) {
                System.out.println(String.format("--hign low %d high %d pivot %d",low,high,pivot));
                --high;
            }
            //交换比枢轴小的记录到左端
            arr[low]=arr[high];
            System.out.println(String.format("arr[low]=arr[high] low %d high %d pivot %d",low,high,pivot));
            while (low<high && arr[low]<=pivot) {
                System.out.println(String.format("++low low %d high %d pivot %d",low,high,pivot));
                ++low;
            }
            //交换比枢轴小的记录到右端
            System.out.println(String.format("arr[high] = arr[low] low %d high %d pivot %d",low,high,pivot));

            arr[high] = arr[low];
        }
        //扫描完成，枢轴到位
        arr[low] = pivot;
        System.out.println(String.format("arr[low] = pivot:arr[%d] = %d",low,pivot));
        //返回的是枢轴的位置
        return low;
    }
}
