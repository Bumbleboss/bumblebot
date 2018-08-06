package utility.audio.queue;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnusedReturnValue")
public class FairQueue<T extends Queueable> {
    private final List<T> list = new ArrayList<>();
    
    public int add(T item){
    	int lastIndex = list.size();
    	list.add(lastIndex, item);
        return lastIndex;
    }
    
    public int size(){
        return list.size();
    }
    
    public T pull(){
        return list.remove(0);
    }
    
    public boolean isEmpty(){
        return list.isEmpty();
    }
    
    public List<T> getList(){
        return list;
    }
    
    public T get(int index){
        return list.get(index);
    }
    
    public T remove(int index){
        return list.remove(index);
    }
    
    public int removeAll(long identifier){
        int count = 0;
        for(int i=list.size()-1; i>=0; i--){
        	if(list.get(i).getIdentifier()==identifier) {
        		list.remove(i);
        		count++;
        	}
        }
        return count;
    }
    
    public void clear(){
        list.clear();
    }
    
    public int shuffle(long identifier){
        List<Integer> iset = new ArrayList<>();
        for(int i=0; i<list.size(); i++){
            if(list.get(i).getIdentifier()==identifier)
                iset.add(i);
        }
        for(int j=0; j<iset.size(); j++){
            int first = iset.get(j);
            int second = iset.get((int)(Math.random()*iset.size()));
            T temp = list.get(first);
            list.set(first, list.get(second));
            list.set(second, temp);
        }
        return iset.size();
    }
    
    public void skip(int number){
        for(int i=0; i<number; i++)
            list.remove(0);
    }
}