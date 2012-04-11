

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Peter Hoek
 */
public  class TimeLinkIterator {
    TimeLink start, end;
    
    public TimeLinkIterator(TimeLink start, TimeLink end) {
        this.start = start;
        this.end = end;
    }
    
    public boolean hasNext() {
        if(start == null || end == null) {
            return false;
        }
        
        return start != end.getNext();
    }

    public TimeLink next() {
        TimeLink next = start;
        start = start.getNext();        
        
        return next;
    }    
}
