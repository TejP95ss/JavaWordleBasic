import java.util.*;

public class Wordle {
    static String GREEN = "\u001B[32m";
    static String YELLOW = "\u001B[33m";
    static String RESET = "\u001B[0m";
// laid soul meth thor
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
    String theWord = theWordList.get(wordIndex).toUpperCase();;

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
            boolean inList = Arrays.asList(ArrayML).contains(currentGuess);
            length = Methods.lengthCheck(currentGuess, Guesses);
            if (inList == false) {length = false;}
            if (length == false) {System.out.println("Invalid word length or word not in dictionary! Try again.");}
        } while (length == false);

            String color = Methods.Checker(theWord, currentGuess, colorWordsIndex);
            normalWordsList.add(currentGuess);
            if(x > 0 && mode == true)
             {cont = Methods.validGuess(currentGuess, normalWordsList, colorWordsIndex, correctPlacedLetters, correctPlacedIndex);}
            if (cont == true) {
                colorWordsList.add(color);
                Valid = true;
                currentGuess = currentGuess.toUpperCase();
                for(int j = 0; j < currentGuess.length(); j++) {usedLettersList.add(currentGuess.charAt(j));}
            }
            else {System.out.println("Word must contain the correctly guessed letters! Try again.");}
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
