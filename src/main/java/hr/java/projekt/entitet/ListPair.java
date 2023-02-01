package hr.java.projekt.entitet;

import java.util.List;

public class ListPair<T1, T2> {
    private List<T1> firstList;
    private List<T2> secondList;

    public ListPair(List<T1> firstList, List<T2> secondList) {
        this.firstList = firstList;
        this.secondList = secondList;
    }

    public List<T1> getFirstList() {
        return firstList;
    }

    public void setFirstList(List<T1> firstList) {
        this.firstList = firstList;
    }

    public List<T2> getSecondList() {
        return secondList;
    }

    public void setSecondList(List<T2> secondList) {
        this.secondList = secondList;
    }

    public void add(T1 t1, T2 t2){
        firstList.add(t1);
        secondList.add(t2);
    }
    public T1 fromFirstGet(int i){
        return firstList.get(i);
    }
    public T2 fromSecondGet(int i){
        return secondList.get(i);
    }
}
