package test.AutoGenTest;

import grok.AutoGenTemplate;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhouxw on 18/3/15.
 */
public class AutoGenExampleTest {
    static String sss = "10.1.0.105 - - 24/Jan/2016:22:21:28 +0800 \"GET .xx#/ HgdTP/1.1\" 304 0 \"-\" \"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36\" \"-\"";
    static String log = "  10.1.0.105\t|24/Jan/2016:22:20:21 +0800|GET / HTTP/1.1|304|0|-|Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36|-|-|-|0.000|-";
    static String log1 = "20107-30 01:06:43 192.168.0.102 - W3SVC1 MGL 192.168.0.102 80 GET /css/rss.xslt - 304 0 140 358 0 HTTP/1.1 192.168.0.102 Mozilla/4.0+(compatible;+MSIE+7.0;+Windows+NT+5.1;+Trident/4.0;+InfoPath.2;+360SE) ASPSESSIONIDACRRDABA=IDDHCBBBHBMBODAGCIDKAGLM -";
    static String apaLog = "100 100 192.168.1.2 - - C 100 \"GET /favicon.ico HTTP/1.1\" 404 209 1000 \"http://localhost/x1.html\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.97 Safari/537.36\"";
    static String nodeLog = "[2016-01-31 12:02:25.84] [INFO] access - 42.120.73.203 - - \"GET /user/projects/ali_sls_log?ignoreError=true HTTP/1.1\" 304 - \"http://\n" +
            "aliyun.com/\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.97 Safari/537.36\"";
    static String tomCatLog = "127.0.0.1 192.168.254.108 - -1 127.0.0.1 HTTP/1.1 - GET 80&<60; GET /rightmainima/leftbott4.swf HTTP/1.1 304 5563A67708646B6AA299C33D59BE132A [22/Sep/2007:10:08:52 +0800] - /rightmainima/leftbott4.swf localhost 0 0.000";
    static String sub = "POST /api HTTP/1.1";
    static String tmp = "https://www.baidu.com/s?wd=%E5%8F%AF%E8%83%BD%E7%9A%84%E6%97%B6%E9%97%B4%E6%A0%BC%E5%BC%8F&rsv_spt=1&rsv_iqid=0xd503fc06000500f3&issp=1&f=8&rsv_bp=1&rsv_idx=2&ie=utf-8&rqlang=cn&tn=baiduhome_pg&rsv_enter=1&oq=%25E6%2597%25B6%25E5%2588%2586%25E7%25A7%2592%25E6%2597%25B6%25E9%2597%25B4%25E6%25A0%25BC%25E5%25BC%258F&rsv_t=4b79tpN5ab1PwFp5kdPLo%2B58cJeaFb5Gn6KT%2BGqxi%2FOwFlDzGSDiNapKJF5lf1KMvJ8h&rsv_pq=93cb9e7f0000b57e&inputT=9621&rsv_sug3=106&rsv_sug1=81&rsv_sug7=100&rsv_sug2=0&rsv_sug4=9621";

    static String access01 = "116.62.158.18 - - [13/Mar/2018:19:48:30 +0800] \"GET /interface/gateway/account.ashx?balance_change_type=3&client_ip=192.168.42.29&create_internal_site_id=fd83d76224034cdeb86ba5815a771588&extend_id=STP1712280000173_T2366&input_charset=utf-8&is_need_original_trade_no=1&is_need_time=1&out_bill_id=&out_trade_no=&platform_lang=java&req_user_id=US14072362683001-03AD&service=query_account_detail_status&sign_type=MD5&version=1.6&sign=d421a9dcc0f3b5f5da5f90ae69e83941 HTTP/1.1\" 200 79 \"-\" \"Apache-HttpClient/4.4.1 (Java/1.8.0_144)\"";
    static  String access02 = "61.130.151.18 - - [13/Mar/2018:19:49:26 +0800] \"GET /static/e4d79c44/images/24x24/notepad.png HTTP/1.1\" 200 955 \"http://stm-jenkins.5173.com/view/stm-dev-pubg171/\" \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.146 Safari/537.36\"";
    static  String access03 = "192.168.181.222 - - [13/Mar/2018:17:48:56 +0800] \"GET /resources/images/img2.5.3/back2.jpg HTTP/2.0\" 200 689472 \"https://192.168.180.2/resources/css/login.css\" \"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36\" \"-\" \"0.006\"";
    static  String access04 = "10.158.247.150 - - [13/Mar/2018:09:58:56 +0800] \"GET /upload/a7/f4/b1/f0/f0/ac1434f1ca9c13460cb834.png HTTP/1.0\" 200 -";
    static   String access05 = "10.158.231.140 - - [13/Mar/2            Matcher matcher = pattern.matcher(arrayList.get(i));\n018:17:36:40 +0800] \"GET /goods/goods/detail?goods_id=1 HTTP/1.0\" 200 21646";
    static   String iis01 = "2018-03-13 12:15:41 192.168.0.231 GET / recommenduserid=US12013047573191-0024 80 - 10.1.1.254 Mozilla/5.0+(Windows+NT+6.1;+WOW64)+AppleWebKit/537.36+(KHTML,+like+Gecko)+Chrome/55.0.2883.87+Safari/537.36 200 0 0 124";
    static   String iis02 = "2018-03-13 12:01:47 W3SVC1 iZ2316fojp9Z 192.168.251.238 GET /Article/Details-2739 - 80 - 157.55.39.223 HTTP/1.1 Mozilla/5.0+(compatible;+bingbot/2.0;++http://www.bing.com/bingbot.htm) - - www.hhsurong.com 200 0 0 20755 291 577";
    static  String iis03 = "2018-03-12 04:30:58 172.16.100.7 GET /public/images/index2/coo-10.jpg - 80 - 211.95.57.242 Mozilla/5.0+(Windows+NT+10.0;+Win64;+x64)+AppleWebKit/537.36+(KHTML,+like+Gecko)+Chrome/58.0.3029.81+Safari/537.36 200 0 0 6";
    static  AutoGenTemplate autoGenTemplate = new AutoGenTemplate();

    static  ArrayList<String> unparsedLogList = new ArrayList<String>();
    static Map<String, String> patternMap = new HashMap<String, String>();
    static String ll = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36";

    @Before
    public void setup() throws Exception {
        autoGenTemplate.init();
        unparsedLogList =  autoGenTemplate.getUnparsedLogList(access03);
    }

    @Test
    public void testTranslate() {
        String translate = "GET /&wd=&eqid=f7358f5700000ddb000000055aa77f5f HTTP/1.1";
        ArrayList arrayList = autoGenTemplate.translate(translate);
        System.out.println(arrayList);
    }

    @Test
    public void TestGetRegEx() throws Exception{
        ArrayList<ArrayList<String>> arrayLists = autoGenTemplate.translate(unparsedLogList);
        String result = autoGenTemplate.concatenate(arrayLists);
        System.out.println(arrayLists);

    }
    @Test
    public void testChrome() {
        String ll = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36";
        ArrayList<String> arrayList = autoGenTemplate.translate(ll);
        String patternStr = arrayList.get(1);
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(ll);
        System.out.println(matcher.matches());
        String replacement = "[[[${chromeOrEdgeAgent}]]]]";
        String newChrome = matcher.replaceAll(replacement);
        System.out.println(newChrome);
        System.out.println(matcher.groupCount());
        System.out.println();

    }
    @Test
    public void testMatch() {
        String ll = "AA";
        Pattern pattern = Pattern.compile("(?<aa>AA)(b)(c)(d)d");
        Matcher matcher = pattern.matcher(ll);
        System.out.println(matcher.matches());
        System.out.println(matcher.groupCount());
        while (matcher.find()) {
            System.out.println(matcher.group(0));
        }
    }
    @SuppressWarnings("all")
    @Test
    public void testGroup() throws  Exception{
        ArrayList<ArrayList<String>> arrayLists = autoGenTemplate.translate(unparsedLogList);
        String result = autoGenTemplate.concatenate(arrayLists);
        ArrayList<String> arrayList = autoGenTemplate.getCapturedGroupNames(result);

        Map<String,String> map = autoGenTemplate.getCapturedResult(result,apaLog);
        System.out.println(map.size());
        System.out.println(map);
    }

    @Test
    public void testRelacement() throws Exception {
        ArrayList<ArrayList<String>> arrayLists = autoGenTemplate.translate(unparsedLogList);
        String result = autoGenTemplate.concatenate(arrayLists);
        ArrayList<Map<String,String>> replaceBeans = new ArrayList<Map<String, String>>();
        Map<String,String> map1 = new HashMap<String, String>() {
            {
                put("old","NUM");
                put("new","XXXX");
            }
        };
        Map<String,String> map2 = new HashMap<String, String>() {
            {
                put("old","NUM1");
                put("new","YYYY");
            }
        };
        replaceBeans.add(map1);
        replaceBeans.add(map2);
        String s = autoGenTemplate.updateRegEx(result,replaceBeans);
        System.out.println(s);

    }
    @Test
    public void testGetStringFromFile() {
        String s = autoGenTemplate.convertFileToString("/Users/zhouxw/Desktop/webAccess/access_5.log","utf-8");
        String[] strings = s.split("\\n");
        ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(strings));
        System.out.println(s);
    }
    @Test
    public void testUpdateRegExOnNum() throws Exception {
        ArrayList<ArrayList<String>> arrayLists = autoGenTemplate.translate(unparsedLogList);
        String result = autoGenTemplate.concatenate(arrayLists);
        String s = autoGenTemplate.convertFileToString("/Users/zhouxw/Desktop/webAccess/access_5.log","utf-8");
        String[] strings = s.split("\\n");
        ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(strings));
        String newRegEx = autoGenTemplate.updateRegExOnNum(result,arrayList);
        System.out.println(newRegEx);

    }
    @Test
    public void testMatchRatio() throws Exception {
        ArrayList<ArrayList<String>> arrayLists = autoGenTemplate.translate(unparsedLogList);
        String result = autoGenTemplate.concatenate(arrayLists);
        Pattern pattern = Pattern.compile(result);

        String s = autoGenTemplate.convertFileToString("/Users/zhouxw/Desktop/webAccess/access_3.log","utf-8");
        String[] strings = s.split("\\n");
        ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(strings));


        int total = arrayList.size();
        int matchNum = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            Matcher matcher = pattern.matcher(arrayList.get(i));
            if (matcher.matches()) {
                matchNum++;
                System.out.println("true  ----" + arrayList.get(i));
            }else {
                System.out.println("false ----" + arrayList.get(i));
            }
        }
        System.out.println(total);
        System.out.println(matchNum * 1.0 / total);
    }

    @Test
    public void testNestedPattern() {
        String s = "(?<dd>\\d{6})?(xx)";
        String r = "?(dd)+|-";
        s = s.replace("xx",r);
        System.out.println(s);
        String ss = "11111-";
////        s = s.replace("xx)","(xx)");
//        System.out.println(s);
//        String ss = "123456-7890";
        Matcher m = Pattern.compile(s).matcher(ss);
        System.out.println(m.matches());

    }




}
