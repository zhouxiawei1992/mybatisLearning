package grok;

import com.sun.org.apache.regexp.internal.RE;
import io.thekraken.grok.api.Grok;
import io.thekraken.grok.api.Match;
import io.thekraken.grok.api.exception.GrokException;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhouxw on 18/3/5.
 */
public class GrokUtils {

    private static final String GROK_PATTERN_PATH = "/Users/zhouxw/Documents/workspace/mybatisLearning/src/main/resources/WebPatterns";

    private static Grok grok = GrokInstance.getGrokInstance(GROK_PATTERN_PATH);

    public static Map<String, Object> toMap(String pattern, String message) {
        Match match = getMatch(pattern, message);
        if (match != null) {
            return match.toMap();
        }
        return null;
    }
    public Grok getGrok() {
        return grok;
    }

    public static String toJson(String pattern, String message) {
        Match match = getMatch(pattern, message);
        if (match != null) {
            return match.toJson();
        }
        return null;
    }

    public static Match getMatch(String pattern, String message) {
        Match match = null;
        try {
            grok.compile(pattern);
            match = grok.match(message);
            match.captures();
        } catch (GrokException e) {
            e.printStackTrace();
            match = null;
        }
        return match;
    }

    public static void main(String[] args) {
        Match match = getMatch("%{IP}","1.1.1.1");


        System.out.println(match.getMatch().matches());
    }
}
