import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class bai_2 {
    public static int[] countCharacterTypes(String s) {
        int letters = 0;
        int spaces = 0;
        int digits = 0;
        int others = 0;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isLetter(c)) {
                letters++;
            } else if (Character.isWhitespace(c)) {
                spaces++;
            } else if (Character.isDigit(c)) {
                digits++;
            } else {
                others++;
            }
        }

        return new int[] { letters, spaces, digits, others };
    }

    public static Character secondMostFrequentChar(String s) {
        if (s.isEmpty()) {
            return null;
        }

        Map<Character, Integer> freq = new LinkedHashMap<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }

        int maxFreq = -1;
        int secondFreq = -1;

        for (int count : freq.values()) {
            if (count > maxFreq) {
                secondFreq = maxFreq;
                maxFreq = count;
            } else if (count < maxFreq && count > secondFreq) {
                secondFreq = count;
            }
        }

        if (secondFreq == -1) {
            return null;
        }

        for (Map.Entry<Character, Integer> entry : freq.entrySet()) {
            if (entry.getValue() == secondFreq) {
                return entry.getKey();
            }
        }

        return null;
    }

    public static BigInteger sumNumbersInString(String s) {
        BigInteger sum = BigInteger.ZERO;
        StringBuilder currentNumber = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c)) {
                currentNumber.append(c);
            } else if (currentNumber.length() > 0) {
                sum = sum.add(new BigInteger(currentNumber.toString()));
                currentNumber.setLength(0);
            }
        }

        if (currentNumber.length() > 0) {
            sum = sum.add(new BigInteger(currentNumber.toString()));
        }

        return sum;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Nhap chuoi: ");
        String input = sc.nextLine();

        int[] counts = countCharacterTypes(input);
        System.out.println("a) Chu cai: " + counts[0] + ", Khoang trang: " + counts[1]);
        System.out.println("   So: " + counts[2] + ", Cac ky tu khac: " + counts[3]);

        Character secondChar = secondMostFrequentChar(input);
        if (secondChar == null) {
            System.out.println("b) Khong ton tai ky tu xuat hien nhieu thu hai.");
        } else {
            System.out.println("b) Ky tu xuat hien nhieu thu hai: '" + secondChar + "'");
        }

        System.out.println("c) Tong cac so trong chuoi: " + sumNumbersInString(input));

        sc.close();
    }
}
