package grok;

import io.thekraken.grok.api.Grok;
import io.thekraken.grok.api.exception.GrokException;

/**
 * Created by zhouxw on 18/3/5.
 */
public class GrokInstance {

    private static Grok grok;
    private GrokInstance() {

    }
    public static Grok getGrokInstance(String grokPatternPath) {
        if (grok == null) {
            try {
                grok = Grok.create(grokPatternPath);
            } catch (GrokException e) {
                e.printStackTrace();
            }
        }
        return grok;
    }
}
