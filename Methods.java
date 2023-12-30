import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Methods extends Wordle{

    public static boolean lengthCheck(String currentGuess, int GuessLength){
        boolean valid = false;
        if(currentGuess.length() == GuessLength) {valid = true;}
        return valid;
    }
    // Returns the colored word based on location of the letters in the guess and the actual word
    static String Checker(String word, String currentGuess, ArrayList<ArrayList<Integer>> colorIndexes){
        currentGuess = currentGuess.toUpperCase();
        String varWord = "";
        ArrayList<Integer> allWordLetterPosition = new ArrayList<Integer>();
        ArrayList<Integer> newAllWordLetterPosition = new ArrayList<Integer>();
        String newColors = "";
        String mainColor = "";
        // Creates an ArrayList matching the index of each letter of the current guess to the actual word
        for(int j = 0; j < currentGuess.length(); j++) {
            char letter = currentGuess.charAt(j);
            int value = word.substring(j).indexOf(letter);
            if (value != -1) {value += j;}
            allWordLetterPosition.add(value);
        }
        // Creates new words of the original word and the guess with matching letters replaced with '1' 
        for(int j = 0; j < currentGuess.length(); j++) {
            if (allWordLetterPosition.get(j) == j) {
                newColors += '1';
                varWord += '1';
            }
            else {newColors += currentGuess.charAt(j);
                  varWord += word.charAt(j);  }
        }
        // Creates a new ArrayList matching the index of each letter of newColors with the original word
        for(int j = 0; j < currentGuess.length(); j++) {
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
        for(int j = 0; j < currentGuess.length(); j++) {
            if (newColors.charAt(j) == '1') {
                mainColor += GREEN + currentGuess.charAt(j) + RESET;
            }
            else if (newAllWordLetterPosition.get(j) == -1) {    
                mainColor += RED + currentGuess.charAt(j) + RESET;
            }
            else {mainColor += YELLOW + currentGuess.charAt(j) + RESET;}
        }
        return mainColor;
    }
    
    public static boolean validGuess(String currentGuess, ArrayList<String> normalWordsList, 
                                    ArrayList<ArrayList<Integer>> colorWordsIndex, ArrayList<Character> correctPlacedLetters, ArrayList<Integer> correctPlacedIndex){
        
        boolean contain = false;
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