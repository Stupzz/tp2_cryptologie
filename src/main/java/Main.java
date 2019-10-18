import com.sun.prism.impl.shape.ShapeUtil;

import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Menu();
    }


    private static void Menu() throws IOException {
        boolean initializedConfig = false;
        Config config = new Config();
        TableArcEnCiel tableArcEnCiel = null;
        Scanner scanner = new Scanner(System.in);  // Create a Scanner object
        String choix;
        boolean exit = false;
        do {
            System.out.println("=====================================================");
            System.out.print(BLUE);
            System.out.println("Veuillez choisir une action:");
            System.out.println("0  - Modifier (ou creer) la configuration");
            System.out.println("1  - Afficher la configuration");
            System.out.println("2  - Testez les fonctions de hashage ");
            System.out.println("3  - Question h2i ");
            System.out.println("4  - Question i2c ");
            System.out.println("5  - Tester une nouvelle chaine");
            System.out.println("6  - Création d'un fichier de sauvegarde");
            System.out.println("7  - Lecture d'un fichier de sauvegarde");
            System.out.println("8  - Recherche dichotomique ");
            System.out.println("9  - Taille de la table en octets ");
            System.out.println("10  - Couverture de la table ");
            System.out.println("11  - Craquer ");
            System.out.println("-1 - Fin du programme" + RESET);
            choix = scanner.nextLine();  // Read user input
            if (!choix.equals("0") && !choix.equals("-1") && !choix.equals("2") && !choix.equals("7") && !choix.equals("8") && !initializedConfig) { //permet d'initialiser la config si le programme choisi par le menu le neccesite
                modifConfig(config);
                initializedConfig = true;
            }
            switch (choix) {
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
                case "6":
                    tableArcEnCiel = creationFichierSauvegarde(config);
                    break;
                case "7":
                    tableArcEnCiel = ouvertureFichierSauvegarde();
                    config = tableArcEnCiel.getConfig();
                    break;
                case "8":
                    if (tableArcEnCiel == null){
                        System.out.println("Veuillez en premier lieu de créer une table arc en ciel (fonctionnalité 6 ou7 du menu)");
                    }else{
                        testRechercheDicho(tableArcEnCiel);
                    }
                    break;
                case "9":
                    if(tableArcEnCiel != null){
                        System.out.println(GREEN + "la taille en octet de notre table est de " + tableArcEnCiel.getTailleOctect() + RESET);tableArcEnCiel.getTailleOctect();
                    }
                    else {
                        System.out.println(RED + "Veuillez initialiser la table arc en Ciel " + RESET);
                    }
                    break;
                case "10":
                    if(tableArcEnCiel != null){
                        System.out.println(GREEN + "la couverture  de notre table est " + tableArcEnCiel.getCouverture() + RESET);tableArcEnCiel.getTailleOctect();
                    }
                    else {
                        System.out.println(RED + "Veuillez initialiser la table arc en Ciel " + RESET);
                    }
                    break;
                case "11":
                    if(tableArcEnCiel != null){
                      craquer(tableArcEnCiel);
                    }
                    else {
                        System.out.println(RED + "Veuillez initialiser la table arc en Ciel " + RESET);
                    }
                    break;
                case "-1":
                    exit = true;
                    break;
                default:
                    badInputMenu();
                    break;
            }
        } while (!exit);
    }

    private static void craquer(TableArcEnCiel tableArcEnCiel){
        Scanner sc = new Scanner(System.in);
        System.out.println("Veuillez choisir le mot à hasher. Veuillez respecter la taille du mot avec tailleMin " + tableArcEnCiel.getConfig().getTailleMin() + " et de taille max " + tableArcEnCiel.getConfig().getTailleMax());
        String choix = sc.nextLine();
        byte[] hash = tableArcEnCiel.isMD5hash()? Utils.md5Bytes(choix) : Utils.sha1Bytes(choix);
        long debut = System.currentTimeMillis();
        String res = tableArcEnCiel.inverse(hash);
        if (res != null){
            System.out.println(GREEN + " Vous avez trouver ! le mot clair est : " + res + RESET);
        }
        System.out.println("Temps de calcul de la fonction inverse : " + (System.currentTimeMillis() - debut) * 0.001);
    }

    private static void testRechercheDicho(TableArcEnCiel tableArcEnCiel){
        int numberToFind = loopUntilGoodInt("Quel nombre souhaitez vous chercher dans la table ?", 0);
        int[] indexEgaux = tableArcEnCiel.rechercheDichotomique((long) numberToFind);
        if (indexEgaux[0] == -1){
            System.out.println(YELLOW + "Vous n'avez pas trouver d'idx correspondant à votre choix");
        }else {
            System.out.println(GREEN + "Vous avez trouver 1 ou plusieur indice pouvant correspondre à notre recherche (tranche):");
            System.out.println("indice départ : "  + indexEgaux[0] + " / index d'arrivé " + indexEgaux[1] + RESET);
        }
    }

    private static TableArcEnCiel ouvertureFichierSauvegarde() throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Veuillez saisir le path du fichier à ouvrir. Veuillez bien donner uniquement un fichier généré par l'option d'écriture dans un fichier du menu.");
        String path = sc.nextLine();
        File fichier = new File(path);
        while(!fichier.exists()){
            System.out.println("Veuillez saisir le path du fichier à ouvrir. Veuillez bien donner uniquement un fichier généré par l'option d'écriture dans un fichier du menu.");
            path = sc.nextLine();
            fichier = new File(path);
        }
        return new TableArcEnCiel().ouvreTable(path);
    }

    private static TableArcEnCiel creationFichierSauvegarde(Config config) {
        TableArcEnCiel tableArcEnCiel = new TableArcEnCiel(config, choixHashage());
        int largeur = loopUntilGoodInt("Veulliez choisir votre largeur", 1);
        int hauteur = loopUntilGoodInt("Veulliez choisir votre hauteur", 1);
        tableArcEnCiel.creerTable(largeur, hauteur);
        Scanner sc = new Scanner(System.in);
        System.out.println("Veuillez choisir un nom pour votre fichier");
        String pathName = sc.nextLine();
        tableArcEnCiel.sauveTable(pathName);
        return tableArcEnCiel;
    }

    private static void showConfig(Config config) {
        System.out.println("Vous avez pour alphabet : " + config.getAlphabet());
        System.out.println("Vous avez pour tailleMin : " + config.getTailleMin());
        System.out.println("Vous avez pour tailleMax : " + config.getTailleMax());
        System.out.println("Le N ainsi généré est de : " + config.getN());
    }

    private static void modifConfig(Config config) {
        Scanner scanner = new Scanner(System.in);  // Create a Scanner object
        System.out.println(BLUE + "Veuillez renseigner l'alphabet" + RESET);
        String alphabet = scanner.nextLine();
        int tailleMin = loopUntilGoodInt("Veuillez renseigner la taille min", 1);
        int tailleMax = loopUntilGoodInt("Veuillez renseigner la taille max", 1);
        config.initConfig(alphabet, tailleMin, tailleMax);
        System.out.println("La configuration à bien était créé/modifié");
    }

    private static boolean choixHashage() {
        /*
        return true si la fonction de hash est MD5
        return false pour sha1
         */
        Scanner sc = new Scanner(System.in);
        String choix = "";
        do {
            System.out.print(BLUE);
            if (!choix.equals("")) {
                System.out.println(RED + "ERROR : veuillez choisir un chiffre entre 0 et 1" + RESET);
            }
            System.out.println("Veuillez choisir votre fonction de hashage:");
            System.out.println("1  - md5");
            System.out.println("2  - sha1" + RESET);
            choix = sc.nextLine();
        } while (!choix.equals("1") && !choix.equals("2"));
        if (choix == "1") {
            return true;
        } else return false;
    }

    private static int loopUntilGoodInt(String strToDisplay, int minValue) {
        Scanner scanner = new Scanner(System.in);
        int res = 0;
        boolean choixCorrect = false;
        while (!choixCorrect) {
            System.out.println(BLUE + strToDisplay + RESET);
            try {
                res = scanner.nextInt();
                if (res < minValue) {
                    System.out.println(RED + "ERROR : Votre choix est incorrect, il nombre doit être supérieur à " + (minValue - 1) + RESET);
                } else {
                    choixCorrect = true;
                }
            } catch (InputMismatchException e) {
                System.out.println(RED + "ERROR : Votre choix est incorrect, vous ne pouvez pas entrer de string ici" + RESET);
                scanner.nextLine();
            }
        }
        return res;
    }

    private static void badInputMenu() {
        System.out.println(RED + "ERROR : Veuillez saisir un champ valide du menu");
    }

    private static void testFonctionHash() {
        Scanner scanner = new Scanner(System.in);  // Create a Scanner object
        String choix, str;
        boolean exit = false;
        do {
            System.out.println("=====================================================");
            System.out.println(BLUE + "Veuillez choisir une votre fonction de hachage :");
            System.out.println("1 - md5");
            System.out.println("2 - sha1");
            System.out.println("0 - Retour vers le Menu" + RESET);
            choix = scanner.nextLine();  // Read user input
            switch (choix) {
                case "1":
                    System.out.println(BLUE + "Saisissez votre string pour le hashage :" + RESET);
                    str = scanner.nextLine();
                    System.out.println(GREEN + "md5 avec '" + str + "' en hexa -> " + Utils.md5Hexa(str));
                    System.out.println("md5 avec '" + str + "' en byte -> " + Utils.md5Bytes(str) + RESET);
                    break;
                case "2":
                    System.out.println(BLUE + "Saisissez votre string pour le hashage :" + RESET);
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
        } while (!exit);
    }

    public static void testNouvelleChaine(Config config) {
        int indiceDepart = loopUntilGoodInt("Veuillez choisir un indice de départ", 0);
        int largeur = loopUntilGoodInt("Veuillez choisir une largeur", 1);
        boolean choixHashage = choixHashage();
        System.out.println(GREEN + "Votre indice final pour " + indiceDepart + " avec une largeur de " + largeur + " est : " + config.nouvelleChaine(indiceDepart, largeur, choixHashage()) + ". Hash realisé avec " + (choixHashage? "md5" : "sh1") + RESET);
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
        do {
            System.out.println(BLUE + "Veuillez choisir une votre fonction de hachage :");
            System.out.println("1 - md5");
            System.out.println("2 - sha1" + RESET);
            choix = scanner.nextLine();  // Read user input
            switch (choix) {
                case "1":
                    System.out.println("Fonction de hash : md5");
                    showConfig(config);
                    System.out.println(GREEN + "md5(\"" + str + "\") = " + Utils.md5Hexa(str));
                    System.out.println("h2i(md5(\"" + str + "\"), " + column + ") = " + config.h2i(Utils.md5Bytes(str), column) + RESET);
                    return;
                case "2":
                    System.out.println("Fonction de hash : sha1");
                    showConfig(config);
                    System.out.println(GREEN + "sha1(\"" + str + "\") = " + Utils.sha1Hexa(str));
                    System.out.println("h2i(sha1(\"" + str + "\"), " + column + ") = " + config.h2i(Utils.sha1Bytes(str), column) + RESET);
                    return;
                case "0":
                    exit = true;
                    break;
                default:
                    badInputMenu();
                    break;
            }
        } while (!exit);

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
