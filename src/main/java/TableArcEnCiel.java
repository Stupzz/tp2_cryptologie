import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class TableArcEnCiel {
    private List<Integer> indexDejaPresent;
    private Map<Integer, Integer> table;
    private int profondeur;
    private Config config;

    public TableArcEnCiel() {
        indexDejaPresent = new ArrayList<>();
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

    public int newAleaIndex(int max){
        Random rand = new Random();
        int indAlea = rand.nextInt(max + 1);
        while(indexDejaPresent.contains(indAlea)){
            indAlea = rand.nextInt(max + 1);
        }
        return indAlea;
    }

    public void creerTable(int largeur, int hauteur){
        int ind = newAleaIndex(hauteur * 100);
        int indFinal;
        table = new HashMap<>();
        for (int i = 0; i < hauteur; i++){
            while (indexDejaPresent.contains(ind)) {
                ind = newAleaIndex(hauteur * 100);
            }
            indFinal = config.nouvelleChaine(ind, largeur);
            indexDejaPresent.add(ind);
            table.put(ind, indFinal);
        }
        System.out.println("Table creer");
    }
}
