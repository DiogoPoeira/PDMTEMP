package pt.isel.g11.hagreve;

import java.util.Iterator;
import java.util.Set;

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
