import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class bai_4 {
    private static int[] parsePositiveArray(String text) {
        List<Integer> list = new ArrayList<>();
        Matcher matcher = Pattern.compile("-?\\d+").matcher(text);

        while (matcher.find()) {
            int value = Integer.parseInt(matcher.group());
            if (value > 0) {
                list.add(value);
            }
        }

        int[] arr = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

    private static long countPossibleTriangles(int[] arr) {
        if (arr.length < 3) {
            return 0;
        }

        Arrays.sort(arr);
        long count = 0;

        for (int k = arr.length - 1; k >= 2; k--) {
            int i = 0;
            int j = k - 1;

            while (i < j) {
                if (arr[i] + arr[j] > arr[k]) {
                    count += (j - i);
                    j--;
                } else {
                    i++;
                }
            }
        }

        return count;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Nhap mang so nguyen duong: ");
        String input = br.readLine();

        if (input == null) {
            System.out.println("So tam giac co the co: 0");
            return;
        }

        int[] arr = parsePositiveArray(input);
        long result = countPossibleTriangles(arr);
        System.out.println("So tam giac co the co: " + result);
    }
}
