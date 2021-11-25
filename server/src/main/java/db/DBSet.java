package db;

import java.util.LinkedList;

public
interface DBSet<T> {
    T get(int id);
    LinkedList <T> all();
    void add(T t);
    void remove(T t);
    void update(T t);
}
