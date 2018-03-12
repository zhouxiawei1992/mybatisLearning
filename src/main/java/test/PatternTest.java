package test;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhouxw on 17/12/4.
 */
public class PatternTest {
    @Test
    public void patternTest() {
        String str = "閑人到人间 中國 六個人";
        Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]+");
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            System.out.println(matcher.group());
        }
    }
    @Test
    public void strTest() {
        String str = "閑人到人间 中國 六個人";
        System.out.println((int) str.charAt(0));
        System.out.println(str.codePointAt(0));

        for (int i = 0 ; i < str.length(); i++) {
            if (str.codePointAt(i) >= 19968 && str.codePointAt(i) <= 40869) {
                System.out.println(str.charAt(i));
            }
        }

    }
    @Test
    public void chineseTest() {
//        String testTxt = "333";
////  注意[\u4E00-\u9FA5]里面的斜杠字符，千万不可省略，不区分大小写
//        Pattern pat = Pattern.compile("^[^\u4E00-\u9FA5]{3}$");
//        Matcher mat = pat.matcher(testTxt);
//        if(mat.matches()) {
//            System.out.println(mat.group());
//        }

    }
    @Test
    public void patternTest2() {
        String str = "閑人到人间 中國 六個人";
        Pattern pattern = Pattern.compile(".+");
        Matcher matcher = pattern.matcher(str);
        System.out.println(str.matches(str));
//        while (matcher.find()) {
//            System.out.println(matcher.group());
//        }
    }


}
