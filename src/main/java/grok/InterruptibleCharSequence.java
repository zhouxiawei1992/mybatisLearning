package grok;

/**
 * Created by zhouxw on 18/3/16.
 */
public class InterruptibleCharSequence implements CharSequence {
    private CharSequence inner;
    public InterruptibleCharSequence(CharSequence inner) {
        super();
        this.inner = inner;
    }

    public char charAt(int index) {
        if (Thread.currentThread().isInterrupted()) {
            throw new RuntimeException("Interrupted!");
        }
        return inner.charAt(index);
    }

    public int length() {
        return inner.length();
    }


    public CharSequence subSequence(int start, int end) {
        return new InterruptibleCharSequence(inner.subSequence(start, end));
    }

    public String toString() {
        return inner.toString();
    }
}



