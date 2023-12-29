import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Wordle {
    static String GREEN = "\u001B[32m";
    static String YELLOW = "\u001B[33m";
    static String RESET = "\u001B[0m";

public static void main(String[] args){

    String currentGuess = "";
    int Guesses = 0;
    boolean continues = false;
    String Mode = "";
    Scanner Guess = new Scanner(System.in);

    while(continues == false){
    System.out.println("Do you want to guess a 4 letter word or a 5 letter word? And type 'easy' or 'hard' for the game mode!"  + GREEN + " GREEN = Correctly Placed," + YELLOW + " YELLOW = Incorrect Placed" + RESET);
    Guesses = Guess.nextInt();
    Mode = Guess.nextLine();
    if (Guesses == 4 || Guesses == 5 && (Mode.toLowerCase().equals(" easy")|| Mode.toLowerCase().equals(" hard"))) {continues = true;}
    else {System.out.println("Invalid input. The word must either be 4 or 5 letters long and the game mode must be easy or hard!");}
    }
    
    boolean mode = Mode.toLowerCase().equals(" hard");

    ArrayList<Character> correctPlacedLetters = new ArrayList<>();
    ArrayList<Integer> correctPlacedIndex = new ArrayList<>();

    
    ArrayList<String> megaWordList = Methods.theword_list("allWords.txt", Guesses);
    Object[] ArrayML = megaWordList.toArray();

    ArrayList<String> theWordList = Methods.theword_list("guessWords.txt", Guesses);
    int wordIndex = (int)(Math.random()*theWordList.size());
    String theWord = theWordList.get(wordIndex).toUpperCase();
 

    ArrayList<String> colorWordsList = new ArrayList<String>();
    ArrayList<String> normalWordsList = new ArrayList<String>();
    ArrayList<ArrayList<Integer>> colorWordsIndex = new ArrayList<ArrayList<Integer>>();
    ArrayList<Character> allLettersList = new ArrayList<Character>(Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q','R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'));
    ArrayList<Character> usedLettersList = new ArrayList<Character>();
    ArrayList<Set<Character>> megaSet = new ArrayList<Set<Character>>();

    for(int x = 0; x < Guesses; x++) {
        boolean Valid = false;

        if (theWord.equals(currentGuess.toUpperCase())) {
            System.out.println("Congratulations! You correctly guessed the word: " + currentGuess);
            break;
        }
        else if (x < Guesses){
            boolean cont = true;
            boolean length = false;
            while (Valid == false) {
            do {
            System.out.print("Guess " + (x+1) + ":");
            String Ans = Guess.nextLine();
            currentGuess = Ans.toUpperCase();
            length = Methods.lengthCheck(currentGuess, Guesses);
            if (length == false) {System.out.println("Invalid word length! Try again.");}
        } while (length == false);

            String color = Methods.Checker(theWord, currentGuess, colorWordsIndex);

            if(x > 0 && mode == true)
             {cont = Methods.validGuess(currentGuess, normalWordsList, colorWordsIndex, correctPlacedLetters, correctPlacedIndex);}

            if (Arrays.asList(ArrayML).contains(currentGuess) && cont == true) {
                if (x == 0) {normalWordsList.add(currentGuess);}
                colorWordsList.add(color);
                Valid = true;
                currentGuess = currentGuess.toUpperCase();
                for(int j = 0; j < currentGuess.length(); j++) {usedLettersList.add(currentGuess.charAt(j));}
            }
            else {System.out.println("Invalid Word! Try again.");}
            }

            Set<Character> allSetLetters = new HashSet<>(allLettersList);
            allSetLetters.removeAll(usedLettersList);
            megaSet.add(allSetLetters);

            Methods.Display(colorWordsList, megaSet);

            if (x == Guesses - 1 && !theWord.equals(currentGuess.toUpperCase())) {System.out.println("You ran out of attempts! The word was '" + theWord + "'");}
            else if (x == Guesses - 1 && theWord.equals(currentGuess.toUpperCase())) {System.out.println("Congratulations! You correctly guessed the word: " + currentGuess);}
        }
    }
    Guess.close();

}
}

class Methods extends Wordle{

public static boolean lengthCheck(String currentGuess, int GuessLength){
    boolean valid = false;
    if(currentGuess.length() == GuessLength) {valid = true;}
    return valid;
}

static String Checker(String word, String theGuess, ArrayList<ArrayList<Integer>> colorIndexes){
    theGuess = theGuess.toUpperCase();
    String varWord = "";
    ArrayList<Integer> allWordLetterPosition = new ArrayList<Integer>();
    ArrayList<Integer> newAllWordLetterPosition = new ArrayList<Integer>();
    String newColors = "";
    String mainColor = "";
    // Creates an ArrayList matching the index of each letter of the current guess to the actual word
    for(int j = 0; j < theGuess.length(); j++) {
        char letter = theGuess.charAt(j);
        int value = word.substring(j).indexOf(letter);
        if (value != -1) {value += j;}
        allWordLetterPosition.add(value);
    }
    // Creates new words of the original word and the guess with matching letters replaced with '1' 
    for(int j = 0; j < theGuess.length(); j++) {
        if (allWordLetterPosition.get(j) == j) {
            newColors += '1';
            varWord += '1';
        }
        else {newColors += theGuess.charAt(j);
              varWord += word.charAt(j);  }
    }
    // Creates a new ArrayList matching the index of each letter of newColors with the original word
    for(int j = 0; j < theGuess.length(); j++) {
        char letter = newColors.charAt(j);
        int value = varWord.indexOf(letter);
        if('1' != varWord.charAt(j) && value != -1) {newAllWordLetterPosition.add(-2);}
        else {newAllWordLetterPosition.add(value);}
        if (value != -1) {
            varWord = varWord.substring(0, value) + '0' + varWord.substring(value + 1);
            }
        
    }
    colorIndexes.add(newAllWordLetterPosition);
    // Assigns colors to each letter of the guess based on the previous variables
    for(int j = 0; j < theGuess.length(); j++) {
        if (newColors.charAt(j) == '1') {
            mainColor += GREEN + theGuess.charAt(j) + RESET;
        }
        else if (newAllWordLetterPosition.get(j) == -1) {    
            mainColor += theGuess.charAt(j);
        }
        else {mainColor += YELLOW + theGuess.charAt(j) + RESET;}
    }
    return mainColor;
}

public static boolean validGuess(String currentGuess, ArrayList<String> normalWordsList, 
                                ArrayList<ArrayList<Integer>> colorWordsIndex, ArrayList<Character> correctPlacedLetters, ArrayList<Integer> correctPlacedIndex){
    
    boolean contain = false;
    normalWordsList.add(currentGuess);
    for(int k = 0; k < normalWordsList.get(0).length(); k++) {
        if(colorWordsIndex.size() == 0) {break;}
        boolean correctPlaced = false;
        for(int j = 0; j < colorWordsIndex.size(); j++){

            if(colorWordsIndex.get(j).get(k) == -1) {continue;}

            else if(colorWordsIndex.get(j).get(k) >= 0 && correctPlaced == false) {
                correctPlacedIndex.add(colorWordsIndex.get(j).get(k));
                correctPlacedLetters.add(normalWordsList.get(j).charAt(k));
                correctPlaced = true;
                    }
                }
            }
    for (int n = 0; n < normalWordsList.get(0).length(); n++) {
        if (correctPlacedIndex.contains(n) == true) {
            int indexOfNumber = correctPlacedIndex.indexOf(n);
            if (currentGuess.charAt(n) != correctPlacedLetters.get(indexOfNumber)) {
                contain = false;
                        break;
                    }
                }
                contain = true;
            }
    
    correctPlacedIndex.clear();
    correctPlacedLetters.clear();
    if (contain == false) {
        normalWordsList.remove(normalWordsList.size() - 1);
        colorWordsIndex.remove(colorWordsIndex.size() - 1);}
    return contain;
}


// Returns all words of the same length as the inputted word length from a text file of words.
public static ArrayList<String> theword_list(String filename, int wordLength){
    try {
        String[] words = new String(Files.readAllBytes(Paths.get(filename))).split(" ");
        ArrayList<String> actualWords = new ArrayList<String>();
        for(int j = 0; j < words.length; j++){
            if (words[j].length() == wordLength) {actualWords.add(words[j]);}
            else {continue;}
        }
        return actualWords;
    }
    catch(Exception e){
        System.out.println("Error 404: File \"words.txt\" not found");
        return null;
    }
}



// Displays the word with hints using colors, and also shows the unused letters in an ArrayList
public static void Display(ArrayList<String> arrayHints, ArrayList<Set<Character>> allArray){
    System.out.print("\033[H\033[2J");  
    System.out.flush();  
    for(int j = 0; j < arrayHints.size(); j++) {
        System.out.print("Guess " + (j+1) + " " + arrayHints.get(j));
        System.out.println(" Unused Letters: "+ allArray.get(j));
    }
}

}