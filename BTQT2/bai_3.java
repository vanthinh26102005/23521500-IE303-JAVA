import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class bai_3 {
    private static int[] parseArray(String text) {
        List<Integer> list = new ArrayList<>();
        Matcher matcher = Pattern.compile("-?\\d+").matcher(text);
        while (matcher.find()) {
            list.add(Integer.parseInt(matcher.group()));
        }

        int[] arr = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

    private static String findLongestConsecutiveSubarray(int[] arr) {
        if (arr.length == 0) {
            return "Khong ton tai.";
        }

        Set<Integer> set = new HashSet<>();
        for (int value : arr) {
            set.add(value);
        }

        int bestStart = 0;
        int bestLen = 0;

        for (int value : set) {
            if (!set.contains(value - 1)) {
                int current = value;
                int len = 1;
                while (set.contains(current + 1)) {
                    current++;
                    len++;
                }

                if (len > bestLen || (len == bestLen && value < bestStart)) {
                    bestLen = len;
                    bestStart = value;
                }
            }
        }

        if (bestLen < 2) {
            return "Khong ton tai.";
        }

        StringBuilder result = new StringBuilder();
        result.append("[");
        for (int i = 0; i < bestLen; i++) {
            if (i > 0) {
                result.append(", ");
            }
            result.append(bestStart + i);
        }
        result.append("]");
        return result.toString();
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Nhap mang so nguyen: ");
        String input = br.readLine();

        if (input == null) {
            System.out.println("Khong ton tai.");
            return;
        }

        int[] arr = parseArray(input);
        System.out.println(findLongestConsecutiveSubarray(arr));
    }
}
