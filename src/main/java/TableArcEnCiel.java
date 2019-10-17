import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class TableArcEnCiel {
    private static List<Integer> indexDejaPresent;
    private static Map<Integer, Integer> table;
    private int profondeur;
    private static Config config;

    public TableArcEnCiel(Config config) {
        indexDejaPresent = new ArrayList<>();
        this.config = config;
    }

    public List<Integer> getIndexDejaPresent() {
        return indexDejaPresent;
    }


    public void sortByValue(){// a test
        /*
        trie le dictionnaire en fonction de ses valeurs
         */
        table = table.entrySet().stream().sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public static int newAleaIndex(int max){
        Random rand = new Random();
        int indAlea = rand.nextInt(max + 1);
        while(indexDejaPresent.contains(indAlea)){
            indAlea = rand.nextInt(max + 1);
        }
        return indAlea;
    }

    static void creerTable(int largeur, int hauteur){
        int ind = newAleaIndex(Config.getN());
        table = new HashMap<>();
        for (int i = 0; i < hauteur; i++){
            while (indexDejaPresent.contains(ind)) {
                ind = newAleaIndex(Config.getN());
            }
            int indFinal = config.nouvelleChaine(ind, largeur);
            indexDejaPresent.add(ind);
            table.put(ind, indFinal);
        }
        System.out.println("Table creer");

        sauve_table(table,"SaveTable.txt");
        System.out.println("Table sauvegarder");
    }

    public static void sauve_table(Map TableArcEnCiel, String NomFichier) {
        String path = NomFichier;
        File fichier = new File(path);
        String newLine = System.getProperty("line.separator");

        try{

            if (!fichier.exists()) {
                fichier.createNewFile();
            }

            FileWriter fw = new FileWriter(path);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(Config.getAlphabet() + newLine);
            bw.write(Config.getTailleMin() + newLine);
            bw.write(Config.getTailleMax() + newLine);
            bw.write(Config.getlargeur() + newLine);
            bw.write(Config.gethauteur() + newLine);
            bw.write("indice indiceHash" + newLine);

            Iterator iterator = TableArcEnCiel.entrySet().iterator();
            while (iterator.hasNext()) {

                Map.Entry mapentry = (Map.Entry) iterator.next();

                bw.write(mapentry.getKey() + " " + mapentry.getValue() +newLine);
            }

            bw.close();

        }catch (Exception e) {
            System.out.println("Impossible de creer le fichier");
        }
    }

    public static void ouvre_table(String NomFichier) throws IOException {

        String alphabet;
        int taille_min, taille_max, largeur, hauteur;
        table = new HashMap<>();

        InputStream flux= new FileInputStream(NomFichier);
        InputStreamReader lecture=new InputStreamReader(flux);
        BufferedReader buff = new BufferedReader(lecture);

        //gestion de l'entête
        alphabet = buff.readLine();
        taille_min = Integer.parseInt(buff.readLine());
        taille_max = Integer.parseInt(buff.readLine());
        largeur = Integer.parseInt(buff.readLine());
        hauteur = Integer.parseInt(buff.readLine());
        buff.readLine(); // saute la ligne de definition
        Config config = new Config();
        config.initConfig(alphabet, taille_min, taille_max, largeur, hauteur);

        String ligne;
        String[] contenu;

        while((ligne = buff.readLine()) !=null) {
            contenu = ligne.split(" ");
            table.put(Integer.parseInt(contenu[0]),Integer.parseInt(contenu[1]));
        }

        sauve_table(table, "OuvrirTable.txt");
        System.out.println("Table ouverte");
    }
}
