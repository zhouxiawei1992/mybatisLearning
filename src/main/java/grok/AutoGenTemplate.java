package grok;



import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhouxw on 18/3/5.
 */

public class AutoGenTemplate {
    public static String userAgentRegex = "(?<USERAGENT>(?:\\s{0,5}Mozilla/\\d{1,2}\\.\\d{1,2}\\s{0,5}\\([^)]+\\)\\s{0,5}AppleWebKit/\\d{2,4}\\.\\d{1,3}\\s{0,5}\\([^)]+\\)(?<ubantu>(?:\\s{0,5}Ubuntu/\\d{1,3}\\.\\d{1,3}\\s{0,3}Chromium/\\d{1,4}\\.\\d{1,4}\\.\\d{1,4}\\.\\d{1,4})?)\\s{0,5}Chrome/\\d{1,4}\\.\\d{1,4}\\.\\d{1,4}\\.\\d{1,4}\\s{0,5}Safari/[\\d\\w.\\s/\\n]+))|(?<name1>(?:\\s{0,5}Mozilla/\\d{1,2}\\.\\d{1,2}\\s{0,5}\\([^)]+\\)\\s{0,5}Gecko/[\\d\\w.\\s/]+))|(?<name2>(?:\\s{0,5}Mozilla/\\d{1,2}\\.\\d{1,2}\\s{0,5}\\([^)]+\\)\\s{0,5}(?:\\s{0,3}like\\s{0,3}Gecko)?))|(?<name3>(?:\\s{0,5}Mozilla/\\d{1,2}\\.\\d{1,2}\\s{0,5}\\([^)]+\\)\\s{0,5}AppleWebKit/\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\s{0,5}\\([^\\)]+\\)\\s{0,5}Version/\\d{1,2}\\.\\d{1,2}\\.\\d{1,2}\\s{0,5}Safari/[\\d\\w.\\s/\\n]+))";
    public static String quotationRegex = "\"[^\"]*\"";
    public static String squareBracketRegex = "\\[[^]]*\\]";
    public static String parenthesisRegex = "\\([^)]*\\)";
    public static Pattern patternName = Pattern.compile("<[^>]+>");
    public static Pattern filterPattern = Pattern.compile("%\\{[^}]+\\}");
    private static final String GROK_PATTERN_PATH = "/Users/zhouxw/Documents/workspace/mybatisLearning/src/main/resources/WebPatterns";
    public static Map<String, String> originalPatternMap = new HashMap<String, String>();
    public static Map<String, String> trimedPatternMap = new HashMap<String, String>();
    public static String spliter = " ";
    public void init() throws Exception{
        originalPatternMap = createPatternMap(GROK_PATTERN_PATH);
        trimOriginalPatternMap();
    }

    public static Map<String, String> createPatternMap(String patternPath) throws Exception {
        Map<String, String> map = new TreeMap<String, String>();
        if (StringUtils.isBlank(patternPath)) {
            throw new Exception("patternPath should not be empty or null");
        }else {
            File file = new File(patternPath);
            if (!file.exists()) {
                throw new Exception("pattern file does not exist!");
            }else if (!file.canRead()) {
                throw new Exception("pattern file can not read!");
            }
            Pattern pattern = Pattern.compile("^([A-z0-9_]+)\\s+(.*)$");
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                Matcher e = pattern.matcher(line);
                if (e.matches()) {
                    map.put(e.group(1),e.group(2));
                }
            }
            br.close();
        }
        return map;
    }
    public String trim(String field) throws Exception{
        String value = originalPatternMap.get(field);
        if (value == null) {
            System.out.println(field + "---" + value);
            throw new Exception("field can not find value");
        }
        if (!value.contains("%{")) {
            return  value;
        }else {
            Matcher matcher = filterPattern.matcher(value);
            while (matcher.find()) {
                String group = matcher.group();
                String subKey = group.substring(2,group.length() - 1);
                String replacement = trim(subKey);
                value = value.replace(group,replacement);
            }
            return value;
        }
    }
    public void trimOriginalPatternMap() throws Exception{
        for (String key : originalPatternMap.keySet()) {
            trimedPatternMap.put(key,trim(key));
        }
    }

    public static void main(String[] args) throws Exception{

        AutoGenTemplate autoGenTemplate = new AutoGenTemplate();
        autoGenTemplate.init();
        Pattern pattern = Pattern.compile("(?<chromeOrEdgeAgent>(?:\\s{0,5}Mozilla/\\d{1,2}\\.\\d{1,2}\\s{0,5}\\([^)]+\\)\\s{0,5}AppleWebKit/\\d{2,4}\\.\\d{1,3}\\s{0,5}\\([^)]+\\)(?<ubantu>(?:\\s{0,5}Ubuntu/\\d{1,3}\\.\\d{1,3}\\s{0,3}Chromium/\\d{1,4}\\.\\d{1,4}\\.\\d{1,4}\\.\\d{1,4})?)\\s{0,5}Chrome/\\d{1,4}\\.\\d{1,4}\\.\\d{1,4}\\.\\d{1,4}\\s{0,5}Safari/[\\d\\w.\\s/\\n]+))");
        String sss = "10.1.0.105 - - 24/Jan/2016:22:21:28 +0800 \"GET .xx#/ HgdTP/1.1\" 304 0 \"-\" \"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36\" \"-\"";
        String log = "  10.1.0.105\t|24/Jan/2016:22:20:21 +0800|GET / HTTP/1.1|304|0|-|Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36|-|-|-|0.000|-";
        String log1 = "20107-30 01:06:43 192.168.0.102 - W3SVC1 MGL 192.168.0.102 80 GET /css/rss.xslt - 304 0 140 358 0 HTTP/1.1 192.168.0.102 Mozilla/4.0+(compatible;+MSIE+7.0;+Windows+NT+5.1;+Trident/4.0;+InfoPath.2;+360SE) ASPSESSIONIDACRRDABA=IDDHCBBBHBMBODAGCIDKAGLM -";
        String apaLog = "100 100 192.168.1.2 - - [02/Feb/2016:17:44:13 +0800] 100 \"GET /favicon.ico HTTP/1.1\" 404 209 1000 \"http://localhost/x1.html\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.97 Safari/537.36\"";
        String nodeLog = "[2016-01-31 12:02:25.84] [INFO] access - 42.120.73.203 - - \"GET /user/projects/ali_sls_log?ignoreError=true HTTP/1.1\" 304 - \"http://\n" +
                "aliyun.com/\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.97 Safari/537.36\"";
        String tomCatLog = "127.0.0.1 192.168.254.108 - -1 127.0.0.1 HTTP/1.1 - GET 80&<60; GET /rightmainima/leftbott4.swf HTTP/1.1 304 5563A67708646B6AA299C33D59BE132A [22/Sep/2007:10:08:52 +0800] - /rightmainima/leftbott4.swf localhost 0 0.000";
        String sub = "POST /api HTTP/1.1";
        String tmp = "https://www.baidu.com/s?wd=%E5%8F%AF%E8%83%BD%E7%9A%84%E6%97%B6%E9%97%B4%E6%A0%BC%E5%BC%8F&rsv_spt=1&rsv_iqid=0xd503fc06000500f3&issp=1&f=8&rsv_bp=1&rsv_idx=2&ie=utf-8&rqlang=cn&tn=baiduhome_pg&rsv_enter=1&oq=%25E6%2597%25B6%25E5%2588%2586%25E7%25A7%2592%25E6%2597%25B6%25E9%2597%25B4%25E6%25A0%25BC%25E5%25BC%258F&rsv_t=4b79tpN5ab1PwFp5kdPLo%2B58cJeaFb5Gn6KT%2BGqxi%2FOwFlDzGSDiNapKJF5lf1KMvJ8h&rsv_pq=93cb9e7f0000b57e&inputT=9621&rsv_sug3=106&rsv_sug1=81&rsv_sug7=100&rsv_sug2=0&rsv_sug4=9621";

        String access01 = "116.62.158.18 - - [13/Mar/2018:19:48:30 +0800] \"GET /interface/gateway/account.ashx?balance_change_type=3&client_ip=192.168.42.29&create_internal_site_id=fd83d76224034cdeb86ba5815a771588&extend_id=STP1712280000173_T2366&input_charset=utf-8&is_need_original_trade_no=1&is_need_time=1&out_bill_id=&out_trade_no=&platform_lang=java&req_user_id=US14072362683001-03AD&service=query_account_detail_status&sign_type=MD5&version=1.6&sign=d421a9dcc0f3b5f5da5f90ae69e83941 HTTP/1.1\" 200 79 \"-\" \"Apache-HttpClient/4.4.1 (Java/1.8.0_144)\"";
        String access02 = "61.130.151.18 - - [13/Mar/2018:19:49:26 +0800] \"GET /static/e4d79c44/images/24x24/notepad.png HTTP/1.1\" 200 955 \"http://stm-jenkins.5173.com/view/stm-dev-pubg171/\" \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.146 Safari/537.36\"";
        String access03 = "192.168.181.222 - - [13/Mar/2018:17:48:56 +0800] \"GET /resources/images/img2.5.3/back2.jpg HTTP/2.0\" 200 689472 \"https://192.168.180.2/resources/css/login.css\" \"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36\" \"-\" \"0.006\"";
        String access04 = "10.158.247.150 - - [13/Mar/2018:09:58:56 +0800] \"GET /upload/a7/f4/b1/f0/f0/ac1434f1ca9c13460cb834.png HTTP/1.0\" 200 -";
        String access05 = "10.158.231.140 - - [13/Mar/2018:17:36:40 +0800] \"GET /goods/goods/detail?goods_id=1 HTTP/1.0\" 200 21646";
        String iis01 = "2018-03-13 12:15:41 192.168.0.231 GET / recommenduserid=US12013047573191-0024 80 - 10.1.1.254 Mozilla/5.0+(Windows+NT+6.1;+WOW64)+AppleWebKit/537.36+(KHTML,+like+Gecko)+Chrome/55.0.2883.87+Safari/537.36 200 0 0 124";
        String iis02 = "2018-03-13 12:01:47 W3SVC1 iZ2316fojp9Z 192.168.251.238 GET /Article/Details-2739 - 80 - 157.55.39.223 HTTP/1.1 Mozilla/5.0+(compatible;+bingbot/2.0;++http://www.bing.com/bingbot.htm) - - www.hhsurong.com 200 0 0 20755 291 577";
        String iis03 = "2018-03-12 04:30:58 172.16.100.7 GET /public/images/index2/coo-10.jpg - 80 - 211.95.57.242 Mozilla/5.0+(Windows+NT+10.0;+Win64;+x64)+AppleWebKit/537.36+(KHTML,+like+Gecko)+Chrome/58.0.3029.81+Safari/537.36 200 0 0 6";
        ArrayList<String> unparsedLogList = autoGenTemplate.getUnparsedLogList(access05);
        Map<String, String> patternMap = autoGenTemplate.createPatternMap("/Users/zhouxw/Documents/workspace/mybatisLearning/src/main/resources/WebPatterns");
        ArrayList<ArrayList<String>> arrayLists = autoGenTemplate.translate(unparsedLogList);
        String result = autoGenTemplate.concatenate(arrayLists);
        String ll = "GET /interface/gateway/account.ashx?balance_change_type=3&client_ip=192.168.42.29&create_internal_site_id=fd83d76224034cdeb86ba5815a771588&extend_id=STP1712280000173_T2366&input_charset=utf-8&is_need_original_trade_no=1&is_need_time=1&out_bill_id=&out_trade_no=&platform_lang=java&req_user_id=US14072362683001-03AD&service=query_account_detail_status&sign_type=MD5&version=1.6&sign=d421a9dcc0f3b5f5da5f90ae69e83941 HTTP/1.1";
        ArrayList<String> arrayList = autoGenTemplate.translate(ll);
        System.out.println(arrayLists);
        System.out.println(apaLog);
        System.out.println("ss");



    }

    public void performance(String regex, String log) {
        long start = System.currentTimeMillis();

        for (int i = 0; i < 1000; i++) {
            String myStr = new String(log);
            Pattern pattern1 = Pattern.compile(regex);
            Matcher matcher1 = pattern1.matcher(myStr);
            System.out.println(matcher1.matches());
        }
        long end = System.currentTimeMillis();
        System.out.println((end - start) / 1000.0);
    }

    public String modifyPatternName(String patternString,String name) {
        Matcher matcher = patternName.matcher(patternString);
        String result = patternString;
        while (matcher.find()) {
            result = result.replace(matcher.group(),"<" + name + ">");
            break;
        }
        return result;
    }

    public ArrayList<String> getUnparsedLogList(String s) {
        spliter = getSpliterChar(s);
        ArrayList  arrayList = new ArrayList<String>();
        int numOfSpace = 0;
        int from  = 0;
        int numOfDQ = 0;
        boolean stop = false;
        if (!StringUtils.isBlank(s)) {
            if (!" ".equalsIgnoreCase(spliter)) {
                arrayList = new ArrayList(Arrays.asList(s.split(spliter)));
                return arrayList;
            }
           for (int i = 0; i < s.length(); i++) {
               if (s.charAt(i) == ' ' && !stop) {
                   numOfSpace++;
                   if (i + 1 < s.length() && s.charAt(i + 1) != ' ') {
                       arrayList.add(s.substring(from,i).trim());
                       arrayList.add(numOfSpace + "###");
                       numOfSpace = 0;
                       from = i + 1;
                   }
               }else if (s.charAt(i) == '[') {
                   stop = true;
               }else if (s.charAt(i) == ']'){
                   stop = false;
               }else if (s.charAt(i) == '\"') {
                    if (numOfDQ == 0) {
                        numOfDQ++;
                        stop = true;
                    }else {
                        numOfDQ = 0;
                        stop = false;
                    }
               }
           }
           arrayList.add(s.substring(from,s.length()).trim());
           arrayList.add(numOfSpace + "###");
        }
        return arrayList;
    }

    public String getSpliterChar(String log) {
        log = log.replaceAll(userAgentRegex,"");
        log = log.replaceAll(quotationRegex,"");
        log = log.replaceAll(squareBracketRegex,"");
        log = log.replaceAll(parenthesisRegex,"");
        ArrayList<Character> arrayList = new ArrayList<Character>();
        for (int i = 0; i < log.length(); i++) {
            char c = log.charAt(i);
            if (c <= '9' && c >= '0' || c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c == '.' || c == '/' || c == '+' || c == '-' ||
                    c == ':' || c == '/' || c == '\\' || c == '=' || c == '^') {
            }else {
                arrayList.add(c);
            }
        }

        int most = 0;
        int second = 0;
        char c1 = ' ';
        char c2 = ' ';
        HashMap<Character,Integer> hashMap = new HashMap();
        for (int j = 0; j < arrayList.size(); j++) {
            Integer i = hashMap.get(arrayList.get(j));
            hashMap.put(arrayList.get(j),i == null ? 0 : ++i);
        }
        for (Character character : hashMap.keySet()) {
            if (hashMap.get(character) >= most) {
                second = most;
                most = hashMap.get(character);
                c1 = character;
                c2 = c1;
            } else if (hashMap.get(character) > second) {
                second = hashMap.get(character);
                c2 = character;
            }
        }
        char result = ' ';
        if (c1 != ' ') {
            result  = c1;
        }else {
            if (second / 1.0 * most >= 0.6) {
                result = c2;
            }
        }
        if (result == ' ') {
            return result + "";
        }
        String patternString = "\\|{2,5}";


        if (needsTransfer(result)) {
            patternString = patternString.replace("|",result + "");
        }else {
            patternString = result + "{2,5}";
        }

        Pattern multiSpliterPattern = Pattern.compile(patternString);
        Matcher matcher = multiSpliterPattern.matcher(log);
        int multiNum = 0;
        String spliter = result + "";
        String expectedSpliter = "";
        while (matcher.find()) {
            String s = matcher.group();
            if (!s.equalsIgnoreCase("")) {
                expectedSpliter = matcher.group();
                System.out.println(matcher.group()+ " " + matcher.start() + " " + matcher.end());
                multiNum++;
            }
        }
        if (multiNum > 3) {
            spliter = patternString.replaceAll("2,5","" + expectedSpliter.length());
        }else {
            spliter = patternString.replaceAll("2,5",1 + "");
        }
       return spliter;
    }

    public boolean needsTransfer(char c) {
        if (c == '.' || c == '|' || c == '$' || c == '￥' || c == '?' || c == '+' || c == '-'
                || c == '*' || c == '\t' || c == ' ') {
            return true;
        }else {
            return false;
        }
    }

    public String concatenate(ArrayList<ArrayList<String>> arrayLists) {
        ArrayList<String> collect = new ArrayList<String>();
        StringBuffer stringBuffer = new StringBuffer("");
        int index = 0;
        for (ArrayList<String> arrayList : arrayLists) {
            if (arrayList.size() == 1) {
                collect.add(arrayList.get(0));
            }else {
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).length() < arrayList.get(index).length()) {
                        index = i;
                    }
                }
                collect.add(arrayList.get(index));
                index = 0;
            }
        }

        for (int i = 0; i < collect.size(); i++) {
            String tmp = collect.get(i);
            String group = "";
            int repeat = 1;
            if (tmp.contains("(?<")) {
                Matcher matcher = patternName.matcher(tmp);
                while (matcher.find()) {
                    group = matcher.group();
                    break;
                }
                for (int j = i + 1; j < collect.size();j++) {
                    if (collect.get(j).contains(group)) {
                        String replace = group.substring(1,group.length() - 1);
                        replace = replace + repeat;
                        ++repeat;
                        String s = modifyPatternName(collect.get(j),replace);
                        collect.set(j,s);
                    }
                }
            }
        }
        for (int i = 0; i < collect.size() - 1; i++) {
            String str = collect.get(i);
            if (!spliter.equalsIgnoreCase(" ")) {
                str = collect.get(i) + spliter;
            }
            stringBuffer.append(str);
        }
        stringBuffer.append(collect.get(collect.size() - 1));
        return stringBuffer.toString();
    }

    public ArrayList<ArrayList<String>> translate(ArrayList<String> unparsedList) throws Exception{
        ArrayList<ArrayList<String>> arrayLists = new ArrayList();
        for (String unparsedString : unparsedList) {
            ArrayList<String> arrayList = translate(unparsedString);
            if (arrayList.size() == 0) {
                String result = "";
                if (unparsedString.contains("###")) {
                    unparsedString = "" + Integer.valueOf(unparsedString.substring(0,unparsedString.length() - 3));
                    result =  "[\\S\\s]{x}".replace("x",unparsedString);
                }else {

                    String token = " ".equalsIgnoreCase(spliter) ? " " : spliter.substring(1,spliter.length() - 3);
                    String tmp = "[^xxxx]+".replace("xxxx",token);
                    if (!unparsedString.contains(token)) {
                        result = tmp;
                    }else {
                        Matcher matcher = Pattern.compile("[xxxx]".replace("xxxx",token)).matcher(unparsedString);
                        for (int i = 0; i < unparsedString.split("[xxxx]".replace("xxxx",token)).length;i++) {
                            result = result + tmp + spliter;
                        }
                        result = result.substring(0,result.length() - spliter.length());

                    }
                }
                arrayList.add(result);
            }
            arrayLists.add(arrayList);
        }
        return arrayLists;
    }

    public ArrayList<String> translate(String log) {
        int frontSpace = 0;
        int endSpace = 0;
        String frontSpacePattern = "\\s{1,x}";
        String endSpacePattern = "\\s{1,x}";
        String quotation = "\"";
        String leftSquareBracket = "\\[";
        String rightSquareBracket = "\\]";
        String frontAppendix = "";
        String endAppendix = "";

        int i = 0,j = log.length() - 1;
        while (i <= log.length() - 1) {
            if (log.charAt(i) == ' ' || log.charAt(i) == '\t') {
                frontSpace++;
                i++;
            }else {
                break;
            }
        }
        while (j >= 0) {
            if (log.charAt(j) == ' ' || log.charAt(j) == '\t') {
                endSpace++;
                --j;
            }else {
                break;
            }
        }
        if (frontSpace > 0 || endSpace > 0) {
            log = log.substring(frontSpace,log.length() - endSpace);
            if (frontSpace != 0) {
                frontSpacePattern = frontSpacePattern.replace("x",frontSpace + "");
                frontAppendix = frontSpacePattern;
            }
            if (endSpace != 0) {
                endSpacePattern = endSpacePattern.replace("x",endSpace + "");
                endAppendix = endSpacePattern;
            }
        }

        frontSpacePattern = frontSpacePattern.contains("x") ? "" : frontSpacePattern;
        endSpacePattern = endSpacePattern.contains("x") ? "" : endSpacePattern;
        if (log.charAt(0) == '[' && log.charAt(log.length() -1) == ']') {
            frontAppendix  = frontSpacePattern + leftSquareBracket;
            endAppendix = rightSquareBracket + endSpacePattern;
            log = log.substring(1,log.length() - 1);
        }
        if (log.charAt(0) == '\"' && log.charAt(log.length() - 1) == '\"') {
            frontAppendix = frontSpacePattern + quotation;
            endAppendix = quotation + endSpacePattern;
            log = log.substring(1,log.length() - 1);
        }

        ArrayList<String> arrayList = new ArrayList<String>();
        for (String key : trimedPatternMap.keySet()) {
            String resultStr = "";
            String pattern = trimedPatternMap.get(key);
            if (log.matches(pattern)) {
                resultStr = "(?<xx>" + pattern + ")";
                resultStr = resultStr.replace("xx",key);
                if (frontAppendix.length() > 0) {
                    resultStr = frontAppendix + resultStr;
                }
                if (endAppendix.length() > 0) {
                    resultStr = resultStr + endAppendix;
                }
                arrayList.add(resultStr);
                System.out.println(resultStr);
            }
        }
        return arrayList;
    }


}
