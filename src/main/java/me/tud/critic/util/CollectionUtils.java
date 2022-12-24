package me.tud.critic.util;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtils {

    @SafeVarargs
    public static <E> List<E> listOfNonNullables(E... elements) {
        List<E> list = new ArrayList<>(elements.length);
        for (E element : elements) {
            if (element != null)
                list.add(element);
        }
        return list;
    }

}
