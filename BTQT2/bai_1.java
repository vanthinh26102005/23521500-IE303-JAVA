import java.math.BigInteger;
import java.util.Scanner;

public class bai_1 {
    public static int firstDigit(long n) {
        while (n >= 10) {
            n /= 10;
        }
        return (int) n;
    }

    public static boolean hasAllOddDigits(long n) {
        if (n == 0) {
            return false;
        }

        while (n > 0) {
            long digit = n % 10;
            if (digit % 2 == 0) {
                return false;
            }
            n /= 10;
        }
        return true;
    }

    public static BigInteger productOfDivisors(long n) {
        BigInteger product = BigInteger.ONE;

        for (long i = 1; i * i <= n; i++) {
            if (n % i == 0) {
                long other = n / i;
                product = product.multiply(BigInteger.valueOf(i));
                if (other != i) {
                    product = product.multiply(BigInteger.valueOf(other));
                }
            }
        }
        return product;
    }

    public static boolean isPerfectNumber(long n) {
        if (n < 2) {
            return false;
        }

        long sum = 1;
        for (long i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                long other = n / i;
                sum += i;
                if (other != i) {
                    sum += other;
                }
            }
        }
        return sum == n;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Nhap so nguyen duong n: ");
        long n = sc.nextLong();

        if (n <= 0) {
            System.out.println("n phai la so nguyen duong.");
            sc.close();
            return;
        }

        System.out.println("a) Chu so dau tien cua n: " + firstDigit(n));
        System.out.println("b) n co toan chu so le khong: " + (hasAllOddDigits(n) ? "Co" : "Khong"));
        System.out.println("c) Tich tat ca uoc so cua n: " + productOfDivisors(n));
        System.out.println("d) n co phai so hoan thien khong: " + (isPerfectNumber(n) ? "Co" : "Khong"));

        sc.close();
    }
}
