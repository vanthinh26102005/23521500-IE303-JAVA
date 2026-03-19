import java.util.Random;

public class bai_2 {
    public static void main(String[] args) {
        int totalPoints = 1_000_000; 
        int inside = 0; 
        Random random = new Random();

        for (int i = 0; i < totalPoints; i++) {
            double x = -1 + 2 * random.nextDouble();
            double y = -1 + 2 * random.nextDouble();

            if (x * x + y * y <= 1) inside++;
        }
        
        double pi = 4.0 * inside / totalPoints;
        System.out.printf("%.6f%n", pi);
    }
}
