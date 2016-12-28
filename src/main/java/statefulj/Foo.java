package statefulj;

import org.statefulj.persistence.annotations.State;

/**
 * Created by hslamba on 12/23/16.
 */
public class Foo {

    @State
    String state;       //Memory Persister requires a string

    boolean bar;

    public String getState() {
        return state;
    }

    /*
    Note : There is no setter for state field as the value is set by StatefulJ.
     */

    public boolean isBar() {
        return bar;
    }

    public void setBar(boolean bar) {
        this.bar = bar;
    }
}
