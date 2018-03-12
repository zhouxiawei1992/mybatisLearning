package grok;



import io.thekraken.grok.api.Match;
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

    public Map<String, String> createPatternMap(String patternPath) throws Exception {
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

    public static void main(String[] args) throws Exception{
        AutoGenTemplate autoGenTemplate = new AutoGenTemplate();

        Pattern pattern = Pattern.compile("(?<chromeOrEdgeAgent>(?:\\s{0,5}Mozilla/\\d{1,2}\\.\\d{1,2}\\s{0,5}\\([^)]+\\)\\s{0,5}AppleWebKit/\\d{2,4}\\.\\d{1,3}\\s{0,5}\\([^)]+\\)(?<ubantu>(?:\\s{0,5}Ubuntu/\\d{1,3}\\.\\d{1,3}\\s{0,3}Chromium/\\d{1,4}\\.\\d{1,4}\\.\\d{1,4}\\.\\d{1,4})?)\\s{0,5}Chrome/\\d{1,4}\\.\\d{1,4}\\.\\d{1,4}\\.\\d{1,4}\\s{0,5}Safari/[\\d\\w.\\s/\\n]+))");
        String sss = "10.1.0.105 - - 24/Jan/2016:22:21:28 +0800 \"GET / HTTP/1.1\" 304 0 \"-\" \"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36\" \"-\"";
        String log = "10.1.0.105|24/Jan/2016:22:20:21 +0800|GET / HTTP/1.1|304|0|-|Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36|-|-|-|0.000|-";
        String log1 = "2010-07-30 01:06:43 192.168.0.102 - W3SVC1 MGL 192.168.0.102 80 GET /css/rss.xslt - 304 0 140 358 0 HTTP/1.1 192.168.0.102 Mozilla/4.0+(compatible;+MSIE+7.0;+Windows+NT+5.1;+Trident/4.0;+InfoPath.2;+360SE) ASPSESSIONIDACRRDABA=IDDHCBBBHBMBODAGCIDKAGLM -";
        String apaLog = "192.168.1.2 - - [02/Feb/2016:17:44:13 +0800] \"GET /favicon.ico HTTP/1.1\" 404 209 \"http://localhost/x1.html\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.97 Safari/537.36\"";
        String nodeLog = "[2016-01-31 12:02:25.844] [INFO] access - 42.120.73.203 - - \"GET /user/projects/ali_sls_log?ignoreError=true HTTP/1.1\" 304 - \"http://\n" +
                "aliyun.com/\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.97 Safari/537.36\"";
        String tomCatLog = "127.0.0.1 192.168.254.108 - -1 127.0.0.1 HTTP/1.1 - GET 80&<60; GET /rightmainima/leftbott4.swf HTTP/1.1 304 5563A67708646B6AA299C33D59BE132A [22/Sep/2007:10:08:52 +0800] - /rightmainima/leftbott4.swf localhost 0 0.000";
        String ll = "a $b $c $ d$e";
        String tmp = "Thu Jan 01 1970 00:00:00 GMT+0800";
        ArrayList<String> unparsedLogList = autoGenTemplate.getUnparsedLogList(tmp);
        Map<String, String> patternMap = autoGenTemplate.createPatternMap("/Users/zhouxw/Documents/workspace/mybatisLearning/src/main/resources/WebPatterns");
        ArrayList<ArrayList<String>> arrayLists = autoGenTemplate.translate(unparsedLogList,patternMap);
        ArrayList<String> arrayList = autoGenTemplate.translate(tmp,patternMap);
        System.out.println(arrayLists);
    }



    public ArrayList<String> getUnparsedLogList(String s) {
        String spliter = getSpliterChar(s);
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
                    c == ':' || c == '/' || c == '\\' || c == '=') {
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

    public ArrayList<ArrayList<String>> translate(ArrayList<String> unparsedList, Map<String,String> patternMap) {
        ArrayList<ArrayList<String>> arrayLists = new ArrayList();
        for (String unparsedString : unparsedList) {

            arrayLists.add(translate(unparsedString,patternMap));
        }
        return arrayLists;
    }

    public ArrayList<String> translate(String log, Map<String,String> map) {
        ArrayList<String> arrayList = new ArrayList<String>();
        for (String pattern : map.keySet()) {
            String resultStr = "";
            GrokUtils grokUtils = new GrokUtils();
            String search;
            if (map.get(pattern).contains("%{")) {
                search = map.get(pattern);
            }else {
                search = "%{" + pattern + "}";
            }
            Match match = GrokUtils.getMatch(search,log);
            if (match != null && match.getMatch() != null && match.getMatch().matches()) {
                resultStr = grokUtils.getGrok().getNamedRegex();
                resultStr = "(?<xx>" + resultStr + ")";
                resultStr = resultStr.replace("xx",pattern);
                arrayList.add(resultStr);
                System.out.println(resultStr);
            }
        }
        return arrayList;
    }


}
