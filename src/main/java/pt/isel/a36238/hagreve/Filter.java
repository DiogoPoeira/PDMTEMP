package pt.isel.a36238.hagreve;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Diogo on 20-03-2015.
 */
public class Filter implements Iterable<String> {
    private Set<String> companies;

    public Filter(Set<String> c){
        companies = c;
    }

    @Override
    public Iterator<String> iterator() {
        return companies.iterator();
    }

    public Set<String> getStringSet() {
        return companies;
    }
}
