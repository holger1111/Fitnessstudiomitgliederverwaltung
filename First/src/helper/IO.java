package helper;

import java.util.Scanner;

public class IO {
	
	// Attribute

	// Konstruktoren

	// Methoden
	public static int readInt(String prompt) {
		Scanner scanner = new Scanner(System.in);
		System.out.println(prompt);
		return scanner.nextInt();
	}
	public static double readDouble(String prompt) {
		Scanner scanner = new Scanner(System.in);
		System.out.println(prompt);
		return scanner.nextDouble();
	}
	public static String readString(String prompt) {
		Scanner scanner = new Scanner(System.in);
		System.out.println(prompt);
		return scanner.nextLine();
	}
	public static char readChar(String prompt) {
		Scanner scanner = new Scanner(System.in);
		System.out.println(prompt);
		return scanner.next().charAt(0);
	}
	public static byte readByte(String prompt) {
		Scanner scanner = new Scanner(System.in);
		System.out.println(prompt);
		return scanner.nextByte();
	}
	public static short readShort(String prompt) {
		Scanner scanner = new Scanner(System.in);
		System.out.println(prompt);
		return scanner.nextShort();
	}
	public static long readLong(String prompt) {
		Scanner scanner = new Scanner(System.in);
		System.out.println(prompt);
		return scanner.nextLong();
	}
	public static float readFloat(String prompt) {
		Scanner scanner = new Scanner(System.in);
		System.out.println(prompt);
		scanner.close();
		return scanner.nextFloat();
	}
	public static boolean readBoolean(String prompt) {
		Scanner scanner = new Scanner(System.in);
		System.out.println(prompt);
		return scanner.nextBoolean();
	}
	public static String readLine(String prompt) {
		Scanner scanner = new Scanner(System.in);
		System.out.println(prompt);
		return scanner.nextLine();
	}
	public static String readToken(String prompt) {
		Scanner scanner = new Scanner(System.in);
		System.out.println(prompt);
		return scanner.next();
	}
}
