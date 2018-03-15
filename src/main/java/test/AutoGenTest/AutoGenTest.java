package test.AutoGenTest;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhouxw on 18/3/15.
 */
public class AutoGenTest {
    public static String s1 = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36";
    @Test
    public void test01() {
        String chrome = "(?<chromeOrEdgeAgent>\\s{0,5}Mozilla/\\d{1,2}\\.\\d{1,2}\\s{0,5}\\([^)]+\\)\\s{0,5}AppleWebKit/\\d{2,4}\\.\\d{1,3}\\s{0,5}\\([^)]+\\)(?:\\s{0,5}Ubuntu/\\d{1,3}\\.\\d{1,3}\\s{0,3}Chromium/\\d{1,4}\\.\\d{1,4}\\.\\d{1,4}\\.\\d{1,4})?\\s{0,5}Chrome/\\d{1,4}\\.\\d{1,4}\\.\\d{1,4}\\.\\d{1,4}\\s{0,5}Safari/[\\d\\w.\\s/\\n]+)";
        Pattern pattern = Pattern.compile(chrome);


    }
}
