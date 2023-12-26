import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Wordle{
    static String GREEN = "\u001B[32m";
    static String YELLOW = "\u001B[33m";
    static String RESET = "\u001B[0m";
public static void main(String[] args){
    WordList words = new WordList();
    String Words[] = words.Words;
    Random generator = new Random();
    int randomIndex = generator.nextInt(Words.length);
    String The_Word = Words[randomIndex];
    int Guesses = 5;
    String CurrentGuess = "";
    System.out.println("Guess the 5 letter word in 5 tries!" + GREEN + " GREEN = Correctly Placed," + YELLOW + " YELLOW = Incorrect Placed" + RESET);
    Scanner Guess = new Scanner(System.in);

    for(int x = 0; x < Guesses; x++) {
        if (The_Word.equals(CurrentGuess.toUpperCase())) {
            System.out.println("Congratulations! You correctly guessed the word: " + CurrentGuess);
            break;
        }
        else if (x < Guesses){
            System.out.print("Guess Number " + (x+1) + " :");
            String Ans = Guess.nextLine();
            CurrentGuess = Ans;
            String CurrentColors = Checker(The_Word, CurrentGuess);
            System.out.println(CurrentColors);
            if (x == Guesses - 1) {System.out.println("You ran out of attempts! The word was '" + The_Word + "'");}
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
            var_word = var_word.substring(0, value) + '0' + var_word.substring(value + 1);}
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


}

