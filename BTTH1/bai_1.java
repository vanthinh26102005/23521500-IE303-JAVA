import java.util.Random;
import java.util.Scanner;

public class bai_1 { 
    public static void main(String[] args) { 
        Scanner sc = new Scanner(System.in); 
        double r = sc.nextDouble(); 
        
        int totalPoints = 1_000_000; 

        int inside = 0; 

        Random random = new Random(); 

        for (int i = 0; i < totalPoints; i++) { 
            double x = -r + 2 * r * random.nextDouble();
            double y = -r + 2 * r * random.nextDouble();
            if (x * x + y * y <= r * r) inside++; 
        }

        double area = ((double) inside / totalPoints) * (2 * r) * (2 * r); 
        System.out.printf("%.6f%n", area); 
        sc.close();
    }
}
