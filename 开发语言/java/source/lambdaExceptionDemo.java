package javaDemo;

class EmptyArrayException extends Exception {
	EmptyArrayException() {
		super("Array Empty");
	}
}

interface DoubleNumericArrayFunc {
	double func(double[] n) throws EmptyArrayException;
}

public class lambdaExceptionDemo {
	
	public static void main(String args[]) throws EmptyArrayException {
		
		double[] values = { 1.0, 2.0, 3.0, 4.0 };
		//注意传入的是数组，但这里的参数还是n
		DoubleNumericArrayFunc average = (n) -> {
			double sum = 0;
			
			if(n.length == 0)
				throw new EmptyArrayException();
			
			for(int i = 0; i < n.length; i++)
				sum += n[i];
			
			return sum / n.length;
		};
		
		System.out.println("The average is "+ average.func(values));
		
		System.out.println("The average is " + average.func(new double[0]));
	}
}
