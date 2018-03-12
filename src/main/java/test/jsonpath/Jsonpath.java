package test.jsonpath;

import com.alibaba.fastjson.JSONPath;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.sun.tools.javadoc.Start;

import java.util.List;

/**
 * Created by zhouxw on 18/1/2.
 */

public class Jsonpath {
    private static String jsonString = "{\n" +
            "    \"store\": {\n" +
            "        \"book\": [\n" +
            "            {\n" +
            "                \"category\": \"reference\",\n" +
            "                \"author\": \"Nigel Rees\",\n" +
            "                \"title\": \"Sayings of the Century\",\n" +
            "                \"price\": 8.95\n" +
            "            },\n" +
            "            {\n" +
            "                \"category\": \"fiction\",\n" +
            "                \"author\": \"Evelyn Waugh\",\n" +
            "                \"title\": \"Sword of Honour\",\n" +
            "                \"price\": 12.99\n" +
            "            },\n" +
            "            {\n" +
            "                \"category\": \"fiction\",\n" +
            "                \"author\": \"Herman Melville\",\n" +
            "                \"title\": \"Moby Dick\",\n" +
            "                \"isbn\": \"0-553-21311-3\",\n" +
            "                \"price\": 8.99\n" +
            "            },\n" +
            "            {\n" +
            "                \"category\": \"fiction\",\n" +
            "                \"author\": \"J. R. R. Tolkien\",\n" +
            "                \"title\": \"The Lord of the Rings\",\n" +
            "                \"isbn\": \"0-395-19395-8\",\n" +
            "                \"price\": 22.99\n" +
            "            }\n" +
            "        ],\n" +
            "        \"bicycle\": {\n" +
            "            \"color\": \"red\",\n" +
            "            \"price\": 19.95\n" +
            "        }\n" +
            "    },\n" +
            "    \"expensive\": 10\n" +
            "}";

    /**
     *
     * @param jsonString
     * @param path
     * @return  aa
     */



    private static List<String> read(String jsonString, String path) {
        List<String> result = JsonPath.read(jsonString,path);

        return result;
    }


    private static String read2(String jsonString, String path) {
       String result = JsonPath.read(jsonString,path);
        return result;
    }

    /**
     *
     *
     * @author chenmc
     * @date 18/3/2 下午2:42
     * @param [document, path]
     * @return java.util.List<java.lang.String>
     */

    private static List<String> read3(Object document,String path) {
        List<String> result = JsonPath.read(document,path);
        return result;
    }

    public static void main(String[] args) {
        List<String> list = read(jsonString,"$.store.book[?(@.isbn)].author");
        String author = read2(jsonString,"$.store.book[0].author");
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(jsonString);
        System.out.println(list);
        ReadContext readContext = JsonPath.parse(jsonString);






//        long start = System.currentTimeMillis();
//        for (int i = 0; i < 10000; i++) {
////            read(jsonString,"$.store.book[*].author");
//
//            read3(document,"$.store.book[*].author");
//        }
//        long end = System.currentTimeMillis();
//        System.out.println((end - start) / 10000.0);

    }





}
