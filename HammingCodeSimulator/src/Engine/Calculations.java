package Engine;

import java.util.ArrayList;

public class Calculations {
	
	private ArrayList<Integer> number;	//input number to turn into hamming code
	private ArrayList<Integer> binary;	//binary number input to check hamming code OR binary of the number arraylist
	private ArrayList<Integer> codeword;	//final output
	
	//main code to call to do create hamming code
	public void calcHamming() {
		organiseNumberArray();
		inputParityBits();
	}
	
	//to change the number input into an array for convenience
	public ArrayList<Integer> changeToArray(String number) {
		ArrayList<Integer> numberArray = new ArrayList<Integer>();
		
		//As binary numbers are accepted a string must be used to accept 0s which is then split
		String[] splitNumber = number.split("(?!^)");
		
		//for each split digit it is placed into a separate element in the ArrayList
		for(String digit : splitNumber) {
			numberArray.add(Integer.parseInt(digit));
		}
		
		return numberArray;
	}
	
	//converts the passed ArrayList of of integers into binary with odds being 1 and evens being 0
	public ArrayList<Integer> convertBinary(ArrayList<Integer> number) {
		ArrayList<Integer> binaryConversion = new ArrayList<Integer>();
		
		//for each element in number, it is determined whether its odd or even and an appropriate binary bit is added to the binary Arraylist
		for(Integer numbers : number) {
			binaryConversion.add(numbers%2);
		}
		
		return binaryConversion;
	}
	
	//the passed in parity bit number returns which bits the parity bit checks
	public ArrayList<Integer> bitsChecked(int parityBit){
		int bit = (int) Math.pow(2, parityBit - 1);	//initialises the bit to the first bit checked by the parity bit
		ArrayList<Integer> checkedBits = new ArrayList<Integer>();
		
		//while loop that goes through the entire length of the final hamming code and adds to the Arraylist bits that are checked bits for the passed in parity bit
		int amount = 0;
		boolean currentlyOne = true;
		while(bit <= codeword.size()) {
			//if the bit is checked by the passed in parity then its added
			if(currentlyOne == true) {
				checkedBits.add(bit);
			}
			
			//way of alternating whether as it scrolls through each bit to see if its checked by the parity bit or not
			amount++;
			if(amount >= (Math.pow(2, parityBit - 1))){
				amount = 0;
				if(currentlyOne == true) {
					currentlyOne = false;
				}
				else {
					currentlyOne = true;
				}
			}
			bit++;
			
		}
		
		return checkedBits;
	}
	
	//for hamming code, places empty values into the array to place parity bits
	public void organiseNumberArray() {
		//adds null values in the codeword arraylist where parity bits will go
		for(int x = 0; x < parityAmount(); x++) {
			codeword.add((int) (codeword.size() - (Math.pow(2, x) - 1)), null);;
		}
	}
	
	//the passed values are which bits to check for parity. If even parity return 0 else return 1
	public int determineParity(ArrayList<Integer> number) {
		int numberOnes = 0;
		//goes through entire arraylist passed in and counts number of ones
		for(Integer digit : number) {
			if(digit == 1) {
				numberOnes++;
			}
		}
		
		//if the counted amount of ones is odd then it returns a 1 else return 0
		if(numberOnes%2 == 1) {
			return 1;
		}
		else {
			return 0;
		}
	}
	
	//method to call in order to call all the methods in order to place parity bits into the input number and create the codeword
	public void inputParityBits() {
		//goes through each of the needed parity bits to be placed
		for(int x = 1; x <= parityAmount(); x++) {
			ArrayList<Integer> binaryBitsChecking = new ArrayList<Integer>();
			
			for(Integer area : bitsChecked(x)) {
				binaryBitsChecking.add(codeword.get(codeword.size() - area));
			}
			binaryBitsChecking.remove(0);
			
			codeword.set(codeword.size() - bitsChecked(x).get(0), determineParity(binaryBitsChecking));
		}
	}

	//returns the amount of parity bits
	public int parityAmount() {
		int amountParityBits = 0;
		//goes through the number to determine how many parity bits needed to be added along with the data bits
		while((Math.pow(2, amountParityBits)) < (number.size() + amountParityBits + 1)) {
			amountParityBits++;
		}
		return amountParityBits;
	}
	
	//returns which bit in the input binary is in error
	public int getErrorBit(ArrayList<Integer> errorParityBits) {
		ArrayList<Integer> errorBit = new ArrayList<Integer>();
		//checks whether theres an error, if theres non return 0
		if(errorParityBits.isEmpty()) {
			return 0;
		}
		else{
			errorBit = bitsChecked(errorParityBits.get(0));
		}
		
		//goes through each list of parity bits passed in which should all be in error and finds the one error bit
		for(int x = 1; x <= amountCheckBits(); x++) {
			//determines whether the current parity bit being checked is in error by comparing it to all the error parity bits passed in
			boolean inError = false;
			for(Integer bits : errorParityBits) {
				if (x == bits) {
					inError = true;
				}
			}
			
			//checks whether the current parity bit is in error to find whether it needs to remove all not similar or remove all the exists
			if(inError == true) {
				//if in error then it compares all the bits in the list which contains the error and this list being checked and removes all that dont exist in both
				for(int y = errorBit.size() - 1; y >= 0; y--) {
					boolean found = false;
					int check1 = errorBit.get(y);
					for(Integer check2 : bitsChecked(x)) {
						if(check1 == check2) {
							found = true;
						}
					}
					if(found == false) {
						errorBit.remove(y);
					}
				}
			}
			else {
				//if the parity bit is not in error it goes through and compares the current obtained list and this list and removes all in the obtained list thats checking list
				for(int y = errorBit.size() - 1; y >= 0; y--) {
					int check1 = errorBit.get(y);
					for(Integer check2 : bitsChecked(x)) {
						if(check1 == check2) {
							errorBit.remove(y);
						}
					}
				}
			}
		}
		//in the end there should only be one in the arraylist and hence return the one value
		return errorBit.get(0);
	}
	
	//returns how many check bits are within the binary number
	public int amountCheckBits() {
		int amountCheckBits = 0;
		//raises the amount of check bits until it is enough that it is more than or equal to the current size of the binary number
		while(Math.pow(2, amountCheckBits) < binary.size()) {
			amountCheckBits++;
		}
		
		return amountCheckBits;
	}
	
	//flips the binary number at the point of the codeword specified by the passed in binary bit
	public void fixErrorBit(int errorBit) {
		//flips the bit specified by the passed in variable in error
		if(codeword.get(codeword.size() - errorBit) == 1) {
			codeword.set(codeword.size() - errorBit, 0);
		}
		else {
			codeword.set(codeword.size() - errorBit, 1);
		}
	}
	
	//returns which parity bits have detected an error
	public ArrayList<Integer> detectError(ArrayList<Integer> number) {
		ArrayList<Integer> errors = new ArrayList<Integer>();
		int amountCheckBits = amountCheckBits();
		//loops through all parity bits and checks whether they are in error
		for(int x = 1; x <= amountCheckBits ; x++) {
			int oneAmount = 0;
			//goes through the bits checked by the parity bit and counts the amount of 1s
			for(Integer digits : bitsChecked(x)) {
				if(binary.get(binary.size() - digits) == 1) {
					oneAmount++;
				}
			}
			//if the amount of ones is odd then the parity bit is in error and is added to the the arraylist
			if(oneAmount%2 == 1) {
				errors.add(x);
			}
		}
		
		return errors;
	}
	
	//sets input number
	public void setNumber(ArrayList<Integer> number) {
		this.number = number;
	}
	
	//sets binary version of number
	public void setBinary(ArrayList<Integer> binary) {
		this.binary = binary;
	}
	
	//sets final output
	public void setCodeword(ArrayList<Integer> codeword) {
		this.codeword = codeword;
	}
	
	//returns input number
	public ArrayList<Integer> getNumber(){
		return number;
	}
	
	//returns binary number
	public ArrayList<Integer> getBinary(){

		return binary;
	}
	
	//returns final output
	public ArrayList<Integer> getCodeword(){
		return codeword;
	}
}
