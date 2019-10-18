import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main (String[] args) throws IOException {

        Config config = new Config();
        config.initConfig("abcdefghijklmnopqrstuvwxyz", 5, 5, 200, 100);
        TableArcEnCiel tableArcEnCiel = new TableArcEnCiel(config);
        tableArcEnCiel.creerTable(200,100);
        tableArcEnCiel.ouvreTable("tables\\saveTable.txt");
        //Menu();
        /*
         * ATTENTION SI LA TAILLE MIN ET MAX SONT TROP GROSSE N PREND EN VALEUR -4 ! à VOIR AVEC LE PROF
         *
         * ############## QUESTION 6 ####################
         * Avec l'ajout de la profondeur, nous pouvons obtenir deux hash different pour une même suite d'octet.
         * de cette manière, si par malchance, nous obtenons la même suite d'octet au fur et à mesure que la profondeur augmente,
         * nous ne pouvons jamais rentrer dans un cycle. (qui sans l'ajout de la profondeur ce repetterai et nous donnerai plus de hash différent)
         */
    }


    private static void Menu(){
        boolean initializedConfig = false;
        Config config = new Config();
        Scanner scanner = new Scanner(System.in);  // Create a Scanner object
        String choix;
        boolean exit = false;
        do{
            System.out.println("=====================================================");
            System.out.print(BLUE);
            System.out.println("Veuillez choisir une action:");
            System.out.println("0  - Modifier (ou creer) la configuration");
            System.out.println("1  - Afficher la configuration");
            System.out.println("2  - Testez les fonctions de hashage ");
            System.out.println("3  - Question h2i ");
            System.out.println("4  - Question i2c ");
            System.out.println("5  - Tester une nouvelle chaine");
            System.out.println("-1 - Fin du programme" + RESET);
            choix = scanner.nextLine();  // Read user input
            if (!choix.equals("0") && !choix.equals("-1") && !choix.equals("2") && !initializedConfig){ //permet d'initialiser la config si le programme choisi par le menu le neccesite
                modifConfig(config);
                initializedConfig = true;
            }
            switch (choix){
                case "0":
                    modifConfig(config);
                    initializedConfig = true;
                    break;
                case "1":
                    showConfig(config);
                    break;
                case "2":
                    testFonctionHash();
                    break;
                case "3":
                    testH2i(config);
                    break;
                case "4":
                    testI2c(config);
                    break;
                case "5":
                    testNouvelleChaine(config);
                    break;
                case "-1":
                    exit = true;
                    break;
                default:
                    badInputMenu();
                    break;
            }
        }while(!exit);
    }

    private static void showConfig(Config config) {
        System.out.println("Vous avez pour alphabet : " + config.getAlphabet());
        System.out.println("Vous avez pour tailleMin : " + config.getTailleMin());
        System.out.println("Vous avez pour tailleMax : " + config.getTailleMax());
        System.out.println("Le N ainsi généré est de : " + config.getN());
    }

    private static void modifConfig(Config config){
        Scanner scanner = new Scanner(System.in);  // Create a Scanner object
        System.out.println(BLUE + "Veuillez renseigner l'alphabet" +RESET);
        String alphabet = scanner.nextLine();
        int tailleMin = loopUntilGoodInt("Veuillez renseigner la taille min", 1);
        int tailleMax = loopUntilGoodInt("Veuillez renseigner la taille max", 1);
        config.initConfig(alphabet, tailleMin, tailleMax);
        System.out.println("La configuration à bien était créé/modifié");
    }

    private static int loopUntilGoodInt(String strToDisplay, int minValue){
        Scanner scanner = new Scanner(System.in);
        int res = 0;
        boolean choixCorrect = false;
        while(!choixCorrect){
            System.out.println(BLUE + strToDisplay + RESET);
            try {
                res = scanner.nextInt();
                if (res < minValue){
                    System.out.println(RED + "ERROR : Votre choix est incorrect, il nombre doit être supérieur à " + (minValue - 1) + RESET);
                }
                else{
                    choixCorrect = true;
                }
            } catch (InputMismatchException e) {
                System.out.println(RED + "ERROR : Votre choix est incorrect, vous ne pouvez pas entrer de string ici" + RESET);
                scanner.nextLine();
            }
        }
        return res;
    }

    private static void badInputMenu(){
        System.out.println(RED + "ERROR : Veuillez saisir un champ valide du menu");
    }

    private static void testFonctionHash(){
        Scanner scanner = new Scanner(System.in);  // Create a Scanner object
        String choix, str;
        boolean exit = false;
        do{
            System.out.println("=====================================================");
            System.out.println(BLUE + "Veuillez choisir une votre fonction de hachage :");
            System.out.println("1 - md5");
            System.out.println("2 - sha1");
            System.out.println("0 - Retour vers le Menu" + RESET);
            choix = scanner.nextLine();  // Read user input
            switch (choix){
                case "1":
                    System.out.println(BLUE + "Saisissez votre string pour le hashage :" + RESET);
                    str = scanner.nextLine();
                    System.out.println(GREEN + "md5 avec '" + str + "' en hexa -> " + Utils.md5Hexa(str));
                    System.out.println("md5 avec '" + str + "' en byte -> " + Utils.md5Bytes(str) + RESET);
                    break;
                case "2":
                    System.out.println( BLUE + "Saisissez votre string pour le hashage :" +RESET);
                    str = scanner.nextLine();
                    System.out.println(GREEN + "sha1 avec '" + str + "' en hexa -> " + Utils.sha1Hexa(str));
                    System.out.println("sha1 avec '" + str + "' en byte -> " + Utils.sha1Bytes(str).toString() + RESET);
                    break;
                case "0":
                    exit = true;
                    break;
                default:
                    break;
            }
        }while(!exit);
    }

    public static void testNouvelleChaine(Config config){
        int indiceDepart = loopUntilGoodInt("Veuillez choisir un indice de départ", 0);
        int largeur = loopUntilGoodInt("Veuillez choisir une largeur", 1);
        System.out.println(GREEN + "Votre indice final pour " + indiceDepart + " avec une largeur de " + largeur + " est : " + config.nouvelleChaine(indiceDepart, largeur) + RESET);
    }

    private static void testI2c(Config config) {
        int number = loopUntilGoodInt("Veuillez saisir un nombre pour tester i2c", 0);
        showConfig(config);
        System.out.println(GREEN + "i2c : " + number + " -> " + config.i2c(number) + RESET);
    }

    private static void testH2i(Config config) {
        Scanner scanner = new Scanner(System.in);  // Create a Scanner object
        String choix, str;
        System.out.println(BLUE + "Saisissez votre string pour le hashage :" + RESET);
        str = scanner.nextLine();
        int column = loopUntilGoodInt("Saisissez le nombre de colonne 't' :", 1);
        boolean exit = false;
        do{
            System.out.println(BLUE + "Veuillez choisir une votre fonction de hachage :");
            System.out.println("1 - md5");
            System.out.println("2 - sha1" + RESET);
            choix = scanner.nextLine();  // Read user input
            switch (choix){
                case "1":
                    System.out.println("Fonction de hash : md5");
                    showConfig(config);
                    System.out.println(GREEN + "md5(\"" + str + "\") = " + Utils.md5Hexa(str));
                    System.out.println("h2i(md5(\"" + str + "\"), " + column + ") = " + config.h2i(Utils.md5Bytes(str), column) + RESET);
                    return;
                case "2":
                    System.out.println("Fonction de hash : sha1");
                    showConfig(config);
                    System.out.println(GREEN +"sha1(\"" + str + "\") = " + Utils.sha1Hexa(str));
                    System.out.println("h2i(sha1(\"" + str + "\"), " + column + ") = " + config.h2i(Utils.sha1Bytes(str), column) + RESET);
                    return;
                case "0":
                    exit = true;
                    break;
                default:
                    badInputMenu();
                    break;
            }
        }while(!exit);

    }

    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

}
