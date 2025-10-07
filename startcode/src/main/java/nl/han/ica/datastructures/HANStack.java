package nl.han.ica.datastructures;

import java.util.ArrayList;
import java.util.List;

public class HANStack<T> implements IHANStack<T>{

    private final List<T> list;

    public HANStack() {
        this.list = new ArrayList<>();
    }
    @Override
    public void push(T value) {
     list.add(value);
    }

    public T pop() {
        if (list.isEmpty()) {
            throw new IllegalStateException("Stack leeg");
        }
        return list.remove(list.size() - 1);
    }

    @Override
    public T peek() {
        if (list.isEmpty()) {
            throw new IllegalStateException("Stack leeg");
        }
        return list.get(list.size() - 1);
    }
}
