import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Wordle {
    static String GREEN = "\u001B[32m";
    static String YELLOW = "\u001B[33m";
    static String RESET = "\u001B[0m";
public static void main(String[] args){
    String CurrentGuess = "";
    int Guesses = 0;
    boolean continues = false;
    Scanner Guess = new Scanner(System.in);
    while(continues == false){
    System.out.println("Do you want to guess a 4 letter word or a 5 letter word?" + GREEN + " GREEN = Correctly Placed," + YELLOW + " YELLOW = Incorrect Placed" + RESET);
    Guesses = Guess.nextInt();
    if (Guesses == 4 || Guesses == 5) {continues = true;}
    else {System.out.println("Invalid input. The word must either be 4 or 5 letters long!");}
    }
    Guess.nextLine();
    
    ArrayList<String> Mega_Word_List = Methods.theword_list("words.txt", Guesses);
    Object[] ArrayML = Mega_Word_List.toArray();

    ArrayList<String> The_Word_List = Methods.theword_list("words2.txt", Guesses);
    int wordIndex = (int)(Math.random()*The_Word_List.size());
    String The_Word = The_Word_List.get(wordIndex).toUpperCase();
    ArrayList<String> Color_Words = new ArrayList<String>();
    ArrayList<Character> allLetters = new ArrayList<Character>(Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q','R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'));
    ArrayList<Character> usedLetters = new ArrayList<Character>();
    ArrayList<Set<Character>> megaList = new ArrayList<Set<Character>>();
    for(int x = 0; x < Guesses; x++) {
        boolean Valid = false;
        if (The_Word.equals(CurrentGuess.toUpperCase())) {
            System.out.println("Congratulations! You correctly guessed the word: " + CurrentGuess);
            break;
        }
        else if (x < Guesses){
            while (Valid == false) {
            System.out.print("Guess " + (x+1) + ":");
            String Ans = Guess.nextLine();
            CurrentGuess = Ans;
            if (Arrays.asList(ArrayML).contains(CurrentGuess.toUpperCase()) && CurrentGuess.length() == Guesses) {
                Valid = true;
                CurrentGuess = CurrentGuess.toUpperCase();
                for(int j = 0; j < CurrentGuess.length(); j++) {usedLetters.add(CurrentGuess.charAt(j));}
            }
            else {System.out.println("Invalid Word! Try again.");}
            }
            Set<Character> AllSetLetters = new HashSet<>(allLetters);
            AllSetLetters.removeAll(usedLetters);
            megaList.add(AllSetLetters);
            String CurrentColors = Methods.Checker(The_Word, CurrentGuess);
            Color_Words.add(CurrentColors);
            Methods.Display(Color_Words, megaList);
            if (x == Guesses - 1 && !The_Word.equals(CurrentGuess.toUpperCase())) {System.out.println("You ran out of attempts! The word was '" + The_Word + "'");}
            else if (x == Guesses - 1 && The_Word.equals(CurrentGuess.toUpperCase())) {System.out.println("Congratulations! You correctly guessed the word: " + CurrentGuess);}
        }

    }
    Guess.close();
}
}

class Methods{
    static String GREEN = "\u001B[32m";
    static String YELLOW = "\u001B[33m";
    static String RESET = "\u001B[0m";

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
        if (AllWordLetterPosition.get(j) == j) {
            NewColors += '1';
            var_word += '1';
        }
        else {NewColors += the_guess.charAt(j);
              var_word += word.charAt(j);  }
    }
    /* 
    for(int j = 0; j < AllWordLetterPosition.size(); j++) {
        if (NewColors.charAt(j) == '1') {var_word += '1';}
        else {var_word += word.charAt(j);}
    }
    */
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

public static ArrayList<String> theword_list(String filename, int wordLength){
    try {
        String[] words = new String(Files.readAllBytes(Paths.get(filename))).split(" ");
        ArrayList<String> actualWords = new ArrayList<String>();
        for(int j = 0; j < words.length; j++){
            if (words[j].length() == wordLength) {actualWords.add(words[j]);}
            else {continue;}
        }
        return actualWords;}
    catch(Exception e){
        System.out.println("Error 404: File \"words.txt\" not found");
        return null;
    }

}

public static void Display(ArrayList<String> Array_Hints, ArrayList<Set<Character>> AllArray){
    System.out.print("\033[H\033[2J");  
    System.out.flush();  

    for(int j = 0; j < Array_Hints.size(); j++) {
        System.out.print("Guess " + (j+1) + " " + Array_Hints.get(j));
        System.out.println(" Unused Letters: "+ AllArray.get(j));
    }
}

}