import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class TableArcEnCiel {
    private static List<Integer> indexDejaPresent;
    private static Map<Integer, Integer> table;
    private Config config;
    private boolean isMD5hash;

    public TableArcEnCiel(Config config) {
        indexDejaPresent = new ArrayList<>();
        this.config = config;
    }

    public List<Integer> getIndexDejaPresent() {
        return indexDejaPresent;
    }


    public int newAleaIndex(int max) {
        Random rand = new Random();
        int indAlea = rand.nextInt(max + 1);
        while (indexDejaPresent.contains(indAlea)) {
            indAlea = rand.nextInt(max + 1);
        }
        return indAlea;
    }

    public void creerTable(int largeur, int hauteur) {
        int ind = newAleaIndex(Config.getN());
        table = new HashMap<>();
        for (int i = 0; i < hauteur; i++) {
            while (indexDejaPresent.contains(ind)) {
                ind = newAleaIndex(Config.getN());
            }
            int indFinal = config.nouvelleChaine(ind, largeur);
            indexDejaPresent.add(ind);
            table.put(ind, indFinal);
        }
        table = Utils.mapSortByValue(table);

        System.out.println("Table creer");

        sauve_table(table, "saveTable.txt", "tables");
    }

    public void sauve_table(Map tableArcEnCiel, String nomFichier, String directory) {
        new File(directory).mkdirs(); // création des dossiers en pour le fichier du path
        File fichier = new File(directory + "\\" + nomFichier);

        String newLine = System.getProperty("line.separator");

        try {
            if (!fichier.exists()) {
                System.out.println("nom fichier ? " + nomFichier);
                boolean succes = fichier.createNewFile();
                System.out.println("succes ? " + succes);
            }

            FileWriter fw = new FileWriter(fichier);
            BufferedWriter bw = new BufferedWriter(fw);
            gestionEnteteEcriture(bw); // ecriture de l'entete

            for (Object o : tableArcEnCiel.entrySet()) {
                Map.Entry mapentry = (Map.Entry) o;
                bw.write(mapentry.getKey() + " " + mapentry.getValue() + newLine);
            }

            bw.close();

        } catch (Exception e) {
            System.out.println("Impossible de creer le fichier");
        }
    }

    public void ouvre_table(String pathFichier) throws IOException {
        table = new HashMap<>();

        InputStream flux = new FileInputStream(pathFichier);
        InputStreamReader lecture = new InputStreamReader(flux);
        BufferedReader buff = new BufferedReader(lecture);

        //gestion de l'entête
        gestionEnteteLecture(buff);

        buff.readLine(); // saute la ligne de d'explication

        String ligne;
        String[] contenu;
        while ((ligne = buff.readLine()) != null) { // lecture du contenu
            contenu = ligne.split(" ");
            table.put(Integer.parseInt(contenu[0]), Integer.parseInt(contenu[1]));
        }

        table = Utils.mapSortByValue(table); // car le Map.put met dans l'orde des clés
        sauve_table(table, "ouvrirTable.txt", "tables");
        System.out.println("Table ouverte");
    }

    private String getInformationFromFile(String phrase) {
        return phrase.split(" : ")[1];
    }

    private void gestionEnteteEcriture(BufferedWriter bw) {
        String newLine = System.getProperty("line.separator");
        try {
            bw.write("méthode de hashage : " + (isMD5hash ? "md5" : "sha1") + newLine);
            bw.write("alphabet : " + Config.getAlphabet() + newLine);
            bw.write("taille de l'alphabet : " + Config.getAlphabet().length() + newLine);
            bw.write("taille min : " + Config.getTailleMin() + newLine);
            bw.write("taille max : " + Config.getTailleMax() + newLine);
            bw.write("nombre texte claire : " + Config.getN() + newLine);
            bw.write("largeur : " + Config.getlargeur() + newLine);
            bw.write("hauteur : " + Config.gethauteur() + newLine);
            bw.write("indice indiceProfondeur" + newLine);
        } catch (Exception e) {
            System.out.println("Impossible de creer l'entete du fichier");
        }
    }

    private void gestionEnteteLecture(BufferedReader buff) {
        String alphabet;
        int taille_min, taille_max, largeur, hauteur;
        //gestion de l'entête
        try{
            isMD5hash = getInformationFromFile(buff.readLine()).equals("md5");
            alphabet = getInformationFromFile(buff.readLine());
            buff.readLine(); // lecture de la taille de l'alphabet
            taille_min = Integer.parseInt(getInformationFromFile(buff.readLine()));
            taille_max = Integer.parseInt(getInformationFromFile(buff.readLine()));
            buff.readLine(); // lecture du nombre de mot claire
            largeur = Integer.parseInt(getInformationFromFile(buff.readLine()));
            hauteur = Integer.parseInt(getInformationFromFile(buff.readLine()));
            Config config = new Config();
            config.initConfig(alphabet, taille_min, taille_max, largeur, hauteur);
        }
        catch (Exception e){

        }

    }
}
