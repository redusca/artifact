package resource.artifact.domains;

import java.util.ArrayList;
import java.util.List;

public class ConexComponents<T> {
    private final List<List<T>> CC;

    public ConexComponents(){
        CC = new ArrayList<>();
    }

    /**
     * Create a conex component with its first value
     * @param value :first value of the conex component
     */
    public void createConex(T value){
        List<T> conex = new ArrayList<>();
        conex.add(value);
        CC.add(conex);
    }

    /**
     * If the value exist in any CC
     * @param value : the searched value
     * @return in which CC it is or null if it isn't in any
     */
    public Integer findValue(T value){
        for (List<T> conex : CC) {
            if(conex.contains(value))
                return CC.indexOf(conex);
        }
        return null;
    }

    /**
     * Add the value to the container that have the existing Value. If the value existing in another component , those two conexComponents will merge
     * If the existing Value isn't contained , a new conex comp will be made
     */
    public void addToConexCompThatHave(T value,T existingValue){
        Integer conexIndex = findValue(existingValue);
        if(conexIndex == null) {
            createConex(existingValue);
            conexIndex = CC.size()-1;
        }

        addToConexCompThatHave(value,conexIndex);
    }

    /**
     * Add the value to an exist conex component
     * @throws IllegalArgumentException if the conexIndex doesn't exit in CC
     */
    public void addToConexCompThatHave(T value,Integer conexIndex){
        if(CC.get(conexIndex) == null)
            throw new IllegalArgumentException("this conex comp index doesn't exit");

        Integer foundConexIndex = findValue(value);
        if(foundConexIndex != null){
            if(!foundConexIndex.equals(conexIndex)) {
                CC.get(foundConexIndex).addAll(CC.get(conexIndex));
                CC.remove(CC.get(conexIndex));
            }
        }
        else{
            CC.get(conexIndex).add(value);
        }
    }

    public Integer size(){
        return CC.size();
    }

    public void printComp(){
        CC.forEach(System.out::println);
    }
}
