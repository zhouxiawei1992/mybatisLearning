package regex;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhouxw on 17/12/20.
 */
public class regTest {
    public static void f() {
        String pattern = "(A)??A|a";
//        String message = "我的QQ是: \n456456 我的电话是:\n0532  2  2  22214:我的邮箱是:aaa\n123:@aaa.com\n11\n";
        String message = "aAAAAA";
        Pattern p = Pattern.compile(pattern,Pattern.MULTILINE);
        Matcher matcher = p.matcher(message);
//        System.out.println(text);
        while (matcher.find()) {
            System.out.println(matcher.start() + "..." + matcher.end() + "..." + matcher.group());

        }
//        System.out.println(message);


    }

    public static void main(String[] args) {
        Map map = new HashMap();
        map.put("a",1);
        map.put("b",2);
        map.remove("a1111");
        System.out.println(map);
    }

}
