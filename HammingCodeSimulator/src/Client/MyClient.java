package Client;

import Engine.*;
import java.util.Scanner;

public class MyClient {
	
	Scanner input = new Scanner(System.in);
	
	public void menu() {
		//creates menu for user to select whether to create hamming code or check hamming code
		while(true) {
			System.out.println("________________________________________________________________________");
			System.out.println("*** Hamming Code Simulator ***\n\n");
			System.out.print(String.format("%-25s %s\n","Create Hamming Code:", "A"));
			System.out.print(String.format("%-25s %s\n","Detect 1 bit Error:", "B"));
			System.out.print(String.format("%-25s %s","Enter Selection:", ""));
			switch (input.nextLine().toLowerCase()) {
			case "a":
				createHamming();
				break;
			case "b":
				detectError();
				break;
			default:	//if the input matched none of the cases then
				System.out.println("\n__________________________________________________________");	//prints error to the console to select one of the values and repeats the loop
				System.out.println("\n\nInvalid input, please enter one of the options below\n");
			}
		}
	}
	//creates hamming code from input number
	public void createHamming() {
		Calculations engine = new Calculations();
		System.out.println("\n__________________________________________________________");
		
		//enters the desired number to turn into hamming code
		System.out.print(String.format("%-10s %s","Enter number:", ""));
		try{
			engine.setNumber(engine.changeToArray(input.nextLine()));
		}
		catch(NumberFormatException E) {
			System.out.println("Error - Invalid input. Enter only digits");
			return;
		}
		
		//converts the number input into a binary number
		engine.setBinary(engine.convertBinary(engine.getNumber()));
		System.out.print(String.format("%-10s %s\n","ID in Binary:", engine.getBinary()));
		
		//sets the binary into the codeword
		engine.setCodeword(engine.getBinary());
		
		//this codeword is then altered to become hamming code
		engine.calcHamming();
		
		System.out.print(String.format("%-10s %s\n","Hamming Code:", engine.getCodeword()));
	}
	//detects and displays the fixed hamming code
	public void detectError() {
		Calculations engine = new Calculations();
		System.out.println("\n__________________________________________________________");
		
		//enters the desired code to check for 1 bit error
		System.out.print(String.format("%-10s %s","Enter number:", ""));
		try {
			engine.setBinary(engine.changeToArray(input.nextLine()));
		}
		catch(NumberFormatException E){
			System.out.println("Error - Invalid input. Enter only digits");
			return;
		}
		//sets the code into the codeword
		engine.setCodeword(engine.getBinary());
		//finds which bit is in error
		int errorBit = engine.getErrorBit(engine.detectError(engine.getBinary()));
		System.out.println(String.format("%-10s %s", "Error Bit: ", errorBit));
		//error prevention by checking whether there is an error
		if(errorBit != 0) {
			//fixes the error
			engine.fixErrorBit(engine.getErrorBit(engine.detectError(engine.getBinary())));
		}
		System.out.print(String.format("%-10s %s\n","Fixed Code:", engine.getCodeword()));
	}
}
