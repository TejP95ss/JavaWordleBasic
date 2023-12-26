import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Wordle {
    static String GREEN = "\u001B[32m";
    static String YELLOW = "\u001B[33m";
    static String RESET = "\u001B[0m";

public static void main(String[] args){
    int Guesses = 5;
    String CurrentGuess = "";
    System.out.println("Guess the 5 letter word in 5 tries!" + GREEN + " GREEN = Correctly Placed," + YELLOW + " YELLOW = Incorrect Placed" + RESET);
    Scanner Guess = new Scanner(System.in);
    String[] Mega_Word_List = Wordle.theword_list("words.txt");
    String[] The_Word_List = Wordle.theword_list("words2.txt");
    int wordIndex = (int)(Math.random()*The_Word_List.length);
    String The_Word = The_Word_List[wordIndex].toUpperCase();
    ArrayList<String> Color_Words = new ArrayList<String>();
    for(int x = 0; x < Guesses; x++) {
        boolean Valid = false;
        if (The_Word.equals(CurrentGuess.toUpperCase())) {
            System.out.println("Congratulations! You correctly guessed the word: " + CurrentGuess);
            break;
        }
        else if (x < Guesses){
            while (Valid == false) {
            System.out.print("Guess " + (x+1) + " :");
            String Ans = Guess.nextLine();
            CurrentGuess = Ans;
            if (Arrays.asList(Mega_Word_List).contains(CurrentGuess.toUpperCase()) && CurrentGuess.length() == 5) {
                Valid = true;
            }
            else {System.out.println("Invalid Word! Try again.");}
            }
            
            String CurrentColors = Checker(The_Word, CurrentGuess);
            Color_Words.add(CurrentColors);
            Hints(Color_Words);
            if (x == Guesses - 1 && !The_Word.equals(CurrentGuess.toUpperCase())) {System.out.println("You ran out of attempts! The word was '" + The_Word + "'");}
            else if (x == Guesses - 1 && The_Word.equals(CurrentGuess.toUpperCase())) {System.out.println("Congratulations! You correctly guessed the word: " + CurrentGuess);}
        }

    }

    Guess.close();

}

static String Checker(String word, String the_guess){
    the_guess = the_guess.toUpperCase();
    String var_word = "";
    ArrayList<Integer> AllWordLetterPosition = new ArrayList<Integer>();
    ArrayList<Integer> NewAllWordLetterPosition = new ArrayList<Integer>();
    for(int j = 0; j < the_guess.length(); j++) {
        char letter = the_guess.charAt(j);
        int value = word.substring(j).indexOf(letter);
        if (value != -1) {value += j;}
        AllWordLetterPosition.add(value);
    }
    String NewColors = "";
    String MainColor = "";
    for(int j = 0; j < AllWordLetterPosition.size(); j++) {
        if (AllWordLetterPosition.get(j) == j) 
             {NewColors += '1';}
        else {NewColors += the_guess.charAt(j);}
    }

    for(int j = 0; j < AllWordLetterPosition.size(); j++) {
        if (NewColors.charAt(j) == '1') {var_word += '1';}
        else {var_word += word.charAt(j);}
    }

    for(int j = 0; j < the_guess.length(); j++) {
        char letter = NewColors.charAt(j);
        int value = var_word.indexOf(letter);
        if (value != -1) {
            var_word = var_word.substring(0, value) + '1' + var_word.substring(value + 1);}
        NewAllWordLetterPosition.add(value);
    }

    for(int j = 0; j < the_guess.length(); j++) {
        if (NewColors.charAt(j) == '1') {
            MainColor += GREEN + the_guess.charAt(j) + RESET;
        }
        else if (NewAllWordLetterPosition.get(j) == -1) {    
            MainColor += the_guess.charAt(j);
        }
        else {MainColor += YELLOW + the_guess.charAt(j) + RESET;}
    }
    return MainColor;
}   

public static String[] theword_list(String filename){
    try {
        String[] words = new String(Files.readAllBytes(Paths.get(filename))).split(" ");
        return words; }
    catch(Exception e){
        System.out.println("Error 404: File \"words.txt\" not found");
        return null;
    }

}

public static void Hints(ArrayList<String> Array_Hints){
    System.out.print("\033[H\033[2J");  
    System.out.flush();  
    for(int j = 0; j < Array_Hints.size(); j++) {
        System.out.println("Guess " + (j+1) + " " + Array_Hints.get(j));
    }
}


}
