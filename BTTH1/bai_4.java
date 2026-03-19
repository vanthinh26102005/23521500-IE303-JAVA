import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class bai_4 {
    private static List<Integer> extractIntegers(String text) {
        List<Integer> numbers = new ArrayList<>();
        Matcher matcher = Pattern.compile("-?\\d+").matcher(text);
        while (matcher.find()) {
            numbers.add(Integer.parseInt(matcher.group()));
        }
        return numbers;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder allInput = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            allInput.append(line).append('\n');
        }

        List<Integer> nums = extractIntegers(allInput.toString());
        if (nums.size() < 2) return;

        int n = nums.get(0);
        int k = nums.get(1);
        if (n <= 0 || nums.size() < n + 2) return;

        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = nums.get(i + 2);
        }

        final int NEG = -1_000_000_000;
        int[] bestLen = new int[k + 1];
        int[] prevSum = new int[k + 1];
        int[] pickIndex = new int[k + 1];

        for (int s = 1; s <= k; s++) {
            bestLen[s] = NEG;
            prevSum[s] = -1;
            pickIndex[s] = -1;
        }
        bestLen[0] = 0;

        for (int i = 0; i < n; i++) {
            int val = a[i];
            if (val > k) continue;
            for (int s = k; s >= val; s--) {
                if (bestLen[s - val] == NEG) continue;
                int candidateLen = bestLen[s - val] + 1;
                if (candidateLen > bestLen[s]) {
                    bestLen[s] = candidateLen;
                    prevSum[s] = s - val;
                    pickIndex[s] = i;
                }
            }
        }

        if (bestLen[k] < 0) return;

        List<Integer> result = new ArrayList<>();
        int curSum = k;
        while (curSum > 0) {
            int idx = pickIndex[curSum];
            if (idx < 0) break;
            result.add(a[idx]);
            curSum = prevSum[curSum];
        }
        Collections.reverse(result);

        StringBuilder out = new StringBuilder();
        for (int i = 0; i < result.size(); i++) {
            if (i > 0) out.append(' ');
            out.append(result.get(i));
        }
        System.out.println(out);
    }
}
