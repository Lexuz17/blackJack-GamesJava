import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;

public class Main {

    ArrayList<String> namaList = new ArrayList<>();
    ArrayList<String> passList = new ArrayList<>();
    ArrayList<Integer> scoreList = new ArrayList<>();

    public Main() {
        Scanner in = new Scanner(System.in);
        readFile();
        int menu;
        do {
            clearScreen();
            menu = menuPrint();

            switch (menu){
                case 1:{
                    String name, pass;
                    int loginFlag = 0, idx = 0;
                    clearScreen();
                    System.out.print("Input username : ");
                    name = in.nextLine();
                    System.out.print("Input password : ");
                    pass = in.nextLine();
                    for (int i = 0; i < namaList.size(); i++){
                        if(namaList.get(i).equals(name) && passList.get(i).equals(pass)){
                            System.out.println("[*] Successfully logged in");
                            loginFlag = 1;
                            idx = i;
                            break;
                        }
                    }
                    if(loginFlag == 0){
                        System.out.println("[!] Invalid Username and Password");
                    }
                    System.out.print("Press enter to continue");
                    in.nextLine();

                    if(loginFlag == 1){
                        int menu2;
                        clearScreen();
                        do {
                            menu2 = mainMenu2(namaList.get(idx), scoreList.get(idx));
                            switch (menu2){
                                case 1:{
                                    clearScreen();
                                    int bet;
                                    do {
                                        System.out.print("Input your bet [max "+scoreList.get(idx)+"]: ");
                                        bet = in.nextInt(); in.nextLine();
                                        if(scoreList.get(idx) == 0){
                                            break;
                                        }
                                        if(bet <= scoreList.get(idx) && bet >= 1){
                                            break;
                                        }
                                        else{
                                            System.out.println("[!] Input must be between 1 and "+scoreList.get(idx));
                                        }
                                    }while (true);
                                    if(scoreList.get(idx) == 0){
                                        System.out.println("=====================================");
                                        System.out.println("|           !!! ERROR !!!           |");
                                        System.out.println("=====================================");
                                        System.out.println("| Your account has reached 0 point  |");
                                        System.out.println("| and been banned by system         |");
                                        System.out.println("=====================================");
                                    }
                                    else {
                                        gamePlay(idx, bet);
                                    }
                                    System.out.print("Press enter to continue...");
                                    in.nextLine();
                                    break;
                                }

                                case 2:{
                                    String[] nameTemp = new String[100];
                                    int[] scoreTemp = new int[100];
                                    clearScreen();
                                    System.out.println("===========================");
                                    System.out.println("|        HIGHSCORE        |");
                                    System.out.println("===========================");
                                    System.out.println("| Username   | Point      |");
                                    System.out.println("===========================");
                                    for (int i = 0; i < namaList.size(); i++){
                                        nameTemp[i] = namaList.get(i);
                                        scoreTemp[i] = scoreList.get(i);
                                    }
                                    divide(nameTemp, scoreTemp, 0, namaList.size()-1);
                                    for (int i = 0; i < namaList.size(); i++){
                                        System.out.printf("| %-11s| %-11s|\n", nameTemp[i], scoreTemp[i]);
                                    }
                                    System.out.println("===========================");
                                    System.out.print("Press enter to continue...");
                                    in.nextLine();
                                    break;
                                }

                                case 3:{
                                    int newScore = scoreList.get(idx);
                                    scoreList.set(idx, newScore);
                                    break;
                                }
                            }
                        }while (menu2 != 3);
                    }
                    break;
                }

                case 2:{
                    String name, pass;
                    int flag = 0;
                    int score;
                    clearScreen();
                    do {
                        System.out.print("Input username : ");
                        name = in.nextLine();
                        if(name.length() >= 4 && name.length() <= 10){
                            for (String s : namaList) {
                                if (name.equals(s)) {
                                    System.out.println("[!] Username must be unique");
                                    break;
                                } else {
                                    flag++;
                                }
                            }
                            if(flag == namaList.size()){
                                break;
                            }
                        }
                        else {
                            System.out.println("[!] Username must be between 4 and 10 characters");
                        }
                    }while (true);

                    do {
                        System.out.print("Input password : ");
                        pass = in.nextLine();
                        if(pass.length() >= 8 && pass.length() <= 16){
                            if(pass.matches("^(?=.*[0-9])(?=.*[a-zA-Z])[a-zA-Z0-9]+$")){
                                break;
                            }
                            else {
                                System.out.println("[!] Password must be alphanumeric");
                            }
                        }
                        else {
                            System.out.println("[!] Password must be between 8 and 16 characters");
                        }
                    }while (true);
                    score = 100;
                    System.out.println("[*] Successfully registered an account");
                    System.out.print("Press enter to continue...");
                    namaList.add(name);
                    passList.add(pass);
                    scoreList.add(score);

                    in.nextLine();
                    break;
                }

                case 3:{
                    for(int i = 0; i < namaList.size(); i++){
                        String encrypt;
                        encrypt= encrypt(namaList.get(i) + "#" + passList.get(i) +"#" + scoreList.get(i), "Us3rS3CR3TD4T4SS");
                        createWriteFile(encrypt);
                    }
                    break;
                }
            }
        }while (menu != 3);
    }

    public static void main(String[] args) {
        new Main();
    }

    int menuPrint(){
        Scanner in = new Scanner(System.in);
        System.out.println("=======================");
        System.out.println("| ♥   BlueJack    ♠ |");
        System.out.println("| ♦   Card Game   ♣ |");
        System.out.println("=======================");
        System.out.println("| 1. Login            |");
        System.out.println("| 2. Register         |");
        System.out.println("| 3. Exit             |");
        System.out.println("=======================");
        return menuScan(in);
    }

    int mainMenu2(String name, int score){
        Scanner in = new Scanner(System.in);
        clearScreen();
        System.out.println("=====================");
        System.out.printf("| Hello, %-11s|\n", name);
        System.out.printf("| point : %-10d|\n", score);
        System.out.println("=====================");
        System.out.println("| 1. Play           |");
        System.out.println("| 2. Highscore      |");
        System.out.println("| 3. Save & Logout  |");
        System.out.println("=====================");
        return menuScan(in);
    }

    private int menuScan(Scanner in) {
        int menu;
        do {
            System.out.print("Choose[1 - 3] >> ");
            menu = in.nextInt(); in.nextLine();
            if(menu < 1 || menu > 3){
                System.out.print("[!] Input must be between 1 and 3\n" +
                        "Press enter to continue...");
                in.nextLine();
            }
        }while (menu < 1 || menu > 3);
        return  menu;
    }

    void clearScreen(){
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }

    private SecretKeySpec secretKey;

    public void setKey(final String myKey) {
        MessageDigest sha;
        try {
            byte[] key = myKey.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String encrypt(final String strToEncrypt, final String secret) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder()
                    .encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e);
        }
        return null;
    }

    public String decrypt(final String strToDecrypt, final String secret) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder()
                    .decode(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e);
        }
        return null;
    }

    void createWriteFile(String write) {
        File writeData = new File("SuperS3cr3tFile.dat");
        try {
            FileWriter file = new FileWriter(writeData);
            file.write(write);
            file.write(System.getProperty("line.separator"));
            file.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    void readFile(){
        File dataFile = new File("SuperS3cr3tFile.dat");
        Scanner readFile = null;
        try {
            readFile = new Scanner(dataFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while(true) {
            assert readFile != null;
            if (!readFile.hasNextLine()) break;
            String fileData = readFile.nextLine();
            String dataDecrypted = decrypt(fileData,"Us3rS3CR3TD4T4SS");
            String[] data = dataDecrypted.split("#");
            namaList.add(data[0]);
            passList.add(data[1]);
            scoreList.add(Integer.parseInt(data[2]));
        }
        readFile.close();
    }

    void mergeSort(String[] nameTemp, int[] scoreTemp, int l, int m, int r){
        int kiri = m - l + 1;
        int kanan = r - m;

        String[] nameLeft = new String[kiri];
        String[] nameRight = new String[kanan];

        int[] scoreLeft = new int[kiri];
        int[] scoreRight = new int[kanan];

        for (int i=0; i<kiri; ++i) {
            nameLeft[i] = nameTemp[l + i];
            scoreLeft[i] = scoreTemp[l + i];
        }
        for (int j=0; j<kanan; ++j) {
            nameRight[j] = nameTemp[m + 1+ j];
            scoreRight[j] = scoreTemp[m + 1+ j];
        }

        int i = 0, j = 0;

        int k = l;
        while (i < kiri && j < kanan){
            if (scoreLeft[i] > scoreRight[j]){
                nameTemp[k] = nameLeft[i];
                scoreTemp[k] = scoreLeft[i];
                i++;
            }
            else{
                nameTemp[k] = nameRight[j];
                scoreTemp[k] = scoreRight[j];
                j++;
            }
            k++;
        }

        while (i < kiri){
            nameTemp[k] = nameLeft[i];
            scoreTemp[k] = scoreLeft[i];
            i++;
            k++;
        }

        while (j < kanan){
            nameTemp[k] = nameRight[j];
            scoreTemp[k] = scoreRight[j];
            j++;
            k++;
        }
    }

    void divide(String[] nameTemp, int[] scoreTemp, int l, int r){
        if (l < r){
            int m = (l+r)/2;

            divide(nameTemp, scoreTemp,l, m);
            divide(nameTemp, scoreTemp , m+1, r);
            mergeSort(nameTemp, scoreTemp, l, m, r);
        }
    }

    int random(int max) {
        return (int)Math.floor(Math.random()*(max- 1 +1)+ 1);
    }

    void gamePlay(int index, int bet){
        int look = 0, done = 0;
        int[] cardDealerDepan= new int[10];
        int[] cardDealerBlkg= new int[10];
        int[] cardPlayerDepan= new int[10];
        int[] cardPlayerBlkg= new int[10];
        Scanner in =new Scanner(System.in);
        clearScreen();
        int dealerHand = 2, playerHand = 2, dealerPoint, playerPoint, playerWin = 0, dealerWin = 0;
        for(int i = 0; i < dealerHand; i++){
            int depan = random(13);
            cardDealerDepan[i] = depan;
            int belakang = random(4);
            cardDealerBlkg[i] = belakang;
        }

        for(int i = 0; i < playerHand; i++) {
            int depan = random(13);
            cardPlayerDepan[i] = depan;
            int belakang = random(4);
            cardPlayerBlkg[i] = belakang;
        }

        do {
            clearScreen();
            dealerPoint = 0;
            playerPoint = 0;
            System.out.println("Dealer Card : ");
            for(int i = 0; i < dealerHand; i++){
                if(look == 0 && i == 1){
                    System.out.print("??");
                }
                else{
                    if(cardDealerDepan[i] == 11){
                        System.out.print("K");
                        dealerPoint+=10;
                    }
                    else if(cardDealerDepan[i] == 12){
                        System.out.print("Q");
                        dealerPoint+=10;
                    }
                    else if(cardDealerDepan[i] == 13){
                        System.out.print("A");
                        dealerPoint+=11;
                    }
                    else {
                        System.out.print(cardDealerDepan[i]);
                        dealerPoint+=cardDealerDepan[i];
                    }

                    if(cardDealerBlkg[i] == 1){
                        System.out.print("♠");
                    }
                    else if(cardDealerBlkg[i] == 2){
                        System.out.print("♦");
                    }
                    else if(cardDealerBlkg[i] == 3){
                        System.out.print("♣");
                    }
                    else if(cardDealerBlkg[i] == 4){
                        System.out.print("♥");
                    }
                    if(i != dealerHand-1){
                        System.out.print(" | ");
                    }
                }
            }

            System.out.println("\nPlayer Card : ");
            for(int i = 0; i < playerHand; i++){
                if(cardPlayerDepan[i] == 11){
                    System.out.print("K");
                    playerPoint+=10;
                }
                else if(cardPlayerDepan[i] == 12){
                    System.out.print("Q");
                    playerPoint+=10;
                }
                else if(cardPlayerDepan[i] == 13){
                    System.out.print("A");
                    playerPoint+=11;
                }
                else {
                    System.out.print(cardPlayerDepan[i]);
                    playerPoint+= cardPlayerDepan[i];
                }

                if(cardPlayerBlkg[i] == 1){
                    System.out.print("♠");
                }
                else if(cardPlayerBlkg[i] == 2){
                    System.out.print("♦");
                }
                else if(cardPlayerBlkg[i] == 3){
                    System.out.print("♣");
                }
                else if(cardPlayerBlkg[i] == 4){
                    System.out.print("♥");
                }
                if(i != playerHand-1){
                    System.out.print(" | ");
                }
            }
            if(dealerWin == 0 && playerWin == 0 && done == 0){
                System.out.println("\n====================\n" +
                        "| Choose your move |\n" +
                        "===================="
                );
                System.out.println("| 1. Hit           |\n" +
                        "| 2. Stand         |\n" +
                        "===================="
                );
                int moveMenu;
                do {
                    System.out.print("Choose[1-2] >> ");
                    moveMenu = in.nextInt(); in.nextLine();
                    if(moveMenu >= 1 && moveMenu <= 2){
                        break;
                    }
                    else{
                        System.out.print("[!] Input must be between 1 and 2\n");
                    }
                }while (true);
                if(moveMenu == 1){
                    int depan = random(13);
                    int belakang = random(4);
                    cardPlayerDepan[playerHand] = depan;
                    cardPlayerBlkg[playerHand] = belakang;
                    playerHand++;
                    if(depan > 10){
                        playerPoint+=10;
                    }
                    else {
                        playerPoint += depan;
                    }
                }
                else {
                    done = 1;
                    look = 1;
                    int moveDealer;
                    moveDealer = random(2);
                    while (moveDealer == 1){
                        int depan = random(13);
                        int belakang = random(4);
                        cardDealerDepan[dealerHand] = depan;
                        cardDealerBlkg[dealerHand] = belakang;
                        dealerPoint+=depan;
                        dealerHand++;
                        if(dealerPoint > 21){
                            playerWin = 1;
                            break;
                        }
                        moveDealer = random(2);
                    }
                    if(dealerPoint < playerPoint && dealerPoint <= 21){
                        playerWin = 1;
                    }
                    else if(playerPoint < dealerPoint && dealerPoint <= 21){
                        dealerWin = 1;
                    }
                }
                if(playerPoint > 21){
                    look = 1;
                    dealerWin = 1;
                }
            }
            else if(dealerWin == 1 && playerWin == 1){
                System.out.println("\n==================================");
                System.out.println("[!] It's tie, you got nothing");
                break;
            }
            else if(dealerWin == 1){
                System.out.println("\n==================================");
                System.out.println("[!] " +namaList.get(index) +" Busted, You lost "+ bet +" point(s)");
                int scoreNew = scoreList.get(index)-bet;
                scoreList.set(index, scoreNew);
                break;
            }
            else if(playerWin == 1){
                System.out.println("\n==================================");
                System.out.println("[!] The dealer busted, You won "+ bet*2 +" point(s)");
                int scoreNew = scoreList.get(index)-bet*2;
                scoreList.set(index, scoreNew);
                break;
            }
        }while (true);
    }
}