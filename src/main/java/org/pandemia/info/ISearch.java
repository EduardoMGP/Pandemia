package org.pandemia.info;

import java.util.List;

public interface ISearch<T> {
    List<T> find(String search);
    String text(T t);
}
