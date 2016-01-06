package mytools;

/**
 * Created by CBD_O9 on 2014-12-17.
 */
public class ZuiDaShiRanGuji {
    public static int k[] = {0, 5, 10};
    public static int l[] = {200, 145, 345};

    /*
    public boolean read(String filename){
    try{
 BufferedReader br = new BufferedReader(new FileReader( fileName ));

    }catch(IOException e){
  System.out.println(e);
  return false;
 }
    return true;
  }
  */
    public static double Factorial(int n) {
        if (n == 0)
            return 1;
        else
            return n * Factorial(n - 1);
    }

    public static double Prob(double a, double b, int m) {
        double P = 0;
        double deta = 0, thta = 0;
        for (int i = 0; i <= k[m]; i++) {
            deta = l[m] * b;
            thta = l[m] * a;
            P = P + Math.pow(deta, i) * Math.pow(thta / (1 + thta), k[m] - i) / Factorial(i);
        }
        return P = P * Math.pow(Math.E, -deta) / (1 + thta);
    }

    public static double compute(double e, double d) {
        double L = 0;
        for (int i = 0; i < k.length; i++) {
            L = L + Math.log(Prob(e, d, i));
        }
        return L;
    }

    public static void main(String[] args) {
      /*if(read("1.txt")) { */

        double Ln, max = -10000000;
        double theta = 0, delta = 0;

        for (double i = 1; i < 1000; i = i + 1)
            for (double j = 1; j < 1000; j = j + 1) {
                double double_i = i / 10000;
                double double_j = j / 10000;
                Ln = compute(double_i, double_j);
                System.out.println("Ln=" + Ln);
                if (Ln > max) {
                    max = Ln;
                    theta = double_i;
                    delta = double_j;
                }
            }
        System.out.println("maxLn=" + max);
        System.out.println("theta=" + theta);
        System.out.println("delta=" + delta);

    }
}
