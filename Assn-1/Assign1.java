import java.util.Scanner;
import java.math.BigInteger; 
import java.math.BigDecimal; 
import java.math.RoundingMode;

public class Assign1{
	
	public static void main(String[] args){
		if(args.length == 0){
			System.out.println("--- Assign 1 Help ---\n-fib [n] : Compute the Fibonacci of [n]; valid range [0, 40]\n-fac [n] : Compute the factorial of [n]; valid range, [0, 2147483647]\n-e [n] : Compute the value of 'e' using [n] iterations; valid range [1, 2147483647]");
		}
		else{
			for(int i = 0; i < args.length; i++){
				if(args[i].equals("fib") ){
					int n = Integer.parseInt(args[i+1]);
					fib(n);
					i++;
				}
				else if(args[i].equals("fac")){
					int n = Integer.parseInt(args[i+1]);
					System.out.printf("Factorial of %d is ", n);
					System.out.println(fac(n));
					i++;
				}
				else if(args[i].equals("e")){
					int n = Integer.parseInt(args[i+1]);
					System.out.printf("Value of e using %d iterations is ", n);
					System.out.println(e(n));
					i++;
				}
				else{
					System.out.print("Unknown command line argument ");
					System.out.println(args[i]);
					i++;
				}	
			}
		}
	}
	
	public static void fib(int n){
		int preNumber = 1;
		int nextNumber = 1;
		int sum = 0;
		if(n < 0 || n > 40){
			System.out.println("--- Assign 1 Help ---\n-fib [n] : Compute the Fibonacci of [n]; valid range [0, 40]\n-fac [n] : Compute the factorial of [n]; valid range, [0, 2147483647]\n-e [n] : Compute the value of 'e' using [n] iterations; valid range [1, 2147483647]");
		}
		else{
			for (int i = 1; i < n; ++i){
				sum = preNumber + nextNumber;
				preNumber = nextNumber;
				nextNumber = sum;
			}
		System.out.printf("Fibonacci of %d is %d\n", n, sum);
		}
	}
	
	public static BigInteger fac(int n){
		BigInteger f = new BigInteger("1");
			for(int i = 1;i <= n; i++){    
				f = f.multiply(BigInteger.valueOf(i));    
			}
		return f;
	}
	
	public static BigDecimal e(int n){
		BigDecimal one = new BigDecimal(1);
        BigDecimal e = new BigDecimal(1.0);
        BigDecimal divisor = new BigDecimal(1.0);
		for (int i =  1; i < n; i++) {
            divisor = divisor.multiply(new BigDecimal(i));
            e = e.add(one.divide(divisor, 16, RoundingMode.HALF_EVEN));
        }
		return e;
	}
	
}
