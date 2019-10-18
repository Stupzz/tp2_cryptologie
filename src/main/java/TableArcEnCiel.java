import java.io.*;
import java.util.*;

public class TableArcEnCiel {
    private static List<Integer> indexDejaPresent;
    private static TableArcEnCielElement[] tableArcEnCielElements;
  //  private static Map<Integer, Integer> table;
    private Config config;
    int hauteur, largeur;
    private boolean isMD5hash;

    public Config getConfig() {
        return config;
    }

    public TableArcEnCiel() {
    }

    public TableArcEnCiel(Config config, boolean isMD5hash) {
        indexDejaPresent = new ArrayList<>();
        hauteur = 0;
        largeur = 0;
        this.isMD5hash = isMD5hash;
        this.config = config;
    }

    public List<Integer> getIndexDejaPresent() {
        return indexDejaPresent;
    }

    public boolean isMD5hash() {
        return isMD5hash;
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
        this.largeur = largeur;
        this.hauteur = hauteur;

        List<TableArcEnCielElement> tableElements = new ArrayList<>();
        int ind = newAleaIndex(config.getN());
        for (int i = 0; i < hauteur; i++) {
            while (indexDejaPresent.contains(ind)) {
                ind = newAleaIndex(config.getN());
            }
            int indFinal = config.nouvelleChaine(ind, largeur, isMD5hash);
            indexDejaPresent.add(ind);
            tableElements.add(new TableArcEnCielElement(ind, (long) indFinal));
        }
        Collections.sort(tableElements);
        tableArcEnCielElements = tableElements.toArray(new TableArcEnCielElement[]{});
    }

    public void sauveTable(String fileName) {
        String path = fileName + ".txt";
        File fichier = new File(path);

        String newLine = System.getProperty("line.separator");

        try {
            if (!fichier.exists()) {
                fichier.createNewFile();
            }
            FileWriter fw = new FileWriter(fichier);
            BufferedWriter bw = new BufferedWriter(fw);
            gestionEnteteEcriture(bw); // ecriture de l'entete

            for (TableArcEnCielElement tableArcEnCielElement : tableArcEnCielElements){
                bw.write(tableArcEnCielElement.getIndexInitial() + " " + tableArcEnCielElement.getIndexFinal() + newLine);
            }
            bw.close();

        } catch (Exception e) {
            System.out.println("Impossible de creer le fichier");
        }
        System.out.println("Saved at the root of the project : " + path);
    }

    public TableArcEnCiel ouvreTable(String pathFichier) throws IOException {
        List<TableArcEnCielElement> tableElements = new ArrayList<>();

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
            tableElements.add(new TableArcEnCielElement(Integer.parseInt(contenu[0]), (long) Integer.parseInt(contenu[1])));
        }
        Collections.sort(tableElements);
        tableArcEnCielElements = tableElements.toArray(new TableArcEnCielElement[]{});
        sauveTable("OpenedTable");
        return this;
    }

    public int[] rechercheDichotomique(long idx){

        int bas = 1;
        int haut = tableArcEnCielElements.length - 1;
        int milieu;

        int indexCourant = -1;

        do {
            milieu = (bas + haut) / 2;

            if (idx == tableArcEnCielElements[milieu].getIndexFinal()){
                indexCourant = milieu;
            }
            else if (tableArcEnCielElements[milieu].getIndexFinal() < idx){
                bas = milieu + 1;
            }
            else{
                haut = milieu - 1;
            }
        } while ((idx != tableArcEnCielElements[milieu].getIndexFinal()) && (bas <= haut));

        if (indexCourant == -1){
            return new int[]{-1, -1};
        }

        // Obtention de la plage des valeur egale a idx
        int start_rang = indexCourant;
        int end_rang = indexCourant;

        while (start_rang >= 1 && tableArcEnCielElements[start_rang-1].getIndexFinal() == idx){
            start_rang--;
        }

        while (end_rang < tableArcEnCielElements.length-1 && tableArcEnCielElements[end_rang + 1].getIndexFinal() == idx){
            end_rang++;
        }
        return new int[]{start_rang, end_rang};
    }

    // essaie d'inverser l'empreinte h
//   - table : table arc-en-ciel
//   - hauteur : nombre de chaines dans la table
//   - largeur : longueur des chaines
//   - h : empreinte à inverser
//   - clair : (résultat) texte clair dont l'empreinte est h
    public String inverse(byte[] h) {
        int nb_candidats = 0;
        long idx;
        for (int t = largeur - 1; t > 0; t--) {
            idx = config.h2i(h, t);
            for (int i = t + 1; i < largeur; i++) {
                idx = isMD5hash? config.i2iMd5((int)idx, i) : config.i2iSha1((int)idx, i);
            }
            int[] resRechercheDicho = rechercheDichotomique(idx);
            if (resRechercheDicho[0] >= 0) { //resRechercheDicho[0] = -1 si pas trouvé
                for (int i = resRechercheDicho[0]; i <= resRechercheDicho[1]; i++) {
                    String msgClair = verifie_candidat(h, t, tableArcEnCielElements[i].getIndexInitial());
                    if (msgClair != null){
                        return msgClair;
                    } else {
                        nb_candidats++;
                    }
                }
            }
        }
        System.out.println("Vous n'avez pas trouver le bon mais vous avez potentiellement : " + nb_candidats);
        return null;
    }

    public String verifie_candidat(byte[] h, int t, int idx) {
        int tmp = idx;
        for (int i = 1; i < t; i++) {
            tmp = isMD5hash ? config.i2iMd5(tmp, i) : config.i2iSha1(tmp, i);
        }
        String msg = config.i2c(tmp);
        byte[] hashMsg = isMD5hash ? Utils.md5Bytes(msg): Utils.sha1Bytes(msg);
        if (hashMsg.equals(h)){
            return msg;
        }
        return null;
    }

    public long getTailleOctect(){

        File fichier = new File("SaveTable.txt");
        long taille = fichier.length();
        return taille;
    }

    public int getCouverture(){
        int n = config.getN();
        int m = hauteur;
        double v = 1.0;
        for (int i = 0; i < largeur; i++){
            v = v * (1 - m / n);
            m = (int) (n * (1 - Math.exp( (-m) / n)));
        }
        int couverture = (int) (100 * (1 - v));
        System.out.println("La couverture de la table est : " + (int) (100 * (1 - v)));
        return couverture;
    }


    private String getInformationFromFile(String phrase) {
        return phrase.split(" : ")[1];
    }

    private void gestionEnteteEcriture(BufferedWriter bw) {
        String newLine = System.getProperty("line.separator");
        try {
            bw.write("méthode de hashage : " + (isMD5hash ? "md5" : "sha1") + newLine);
            bw.write("alphabet : " + config.getAlphabet() + newLine);
            bw.write("taille de l'alphabet : " + config.getAlphabet().length() + newLine);
            bw.write("taille min : " + config.getTailleMin() + newLine);
            bw.write("taille max : " + config.getTailleMax() + newLine);
            bw.write("nombre texte claire : " + config.getN() + newLine);
            bw.write("largeur : " + largeur + newLine);
            bw.write("hauteur : " + hauteur + newLine);
            bw.write("indice indiceProfondeur" + newLine);
        } catch (Exception e) {
            System.out.println("Impossible de creer l'entete du fichier");
        }
    }

    private void gestionEnteteLecture(BufferedReader buff) {
        String alphabet;
        int taille_min, taille_max;
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
            config.initConfig(alphabet, taille_min, taille_max);

        }
        catch (Exception e){

        }

    }
}
