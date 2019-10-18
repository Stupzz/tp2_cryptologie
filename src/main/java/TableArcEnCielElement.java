public class TableArcEnCielElement implements Comparable<TableArcEnCielElement> {
    private int indexInitial;
    private Long indexFinal;

    public TableArcEnCielElement(int indexInitial, Long indexFinal) {
        this.indexInitial = indexInitial;
        this.indexFinal = indexFinal;
    }

    public int getIndexInitial() {
        return indexInitial;
    }

    public void setIndexInitial(int indexInitial) {
        this.indexInitial = indexInitial;
    }

    public Long getIndexFinal() {
        return indexFinal;
    }

    public void setIndexFinal(Long indexFinal) {
        this.indexFinal = indexFinal;
    }

    public TableArcEnCielElement() {
    }

    @Override
    public int compareTo(TableArcEnCielElement o) {
        return this.getIndexFinal().compareTo(o.getIndexFinal());
    }

    public String toString(){
        return ""+ indexInitial + "/" + indexFinal;
    }
}
