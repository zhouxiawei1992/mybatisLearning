package test.jsonpath;

import com.jayway.jsonpath.*;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.*;

import static com.jayway.jsonpath.JsonPath.parse;
import static com.jayway.jsonpath.Criteria.where;
import static com.jayway.jsonpath.Filter.filter;
import static com.jayway.jsonpath.JsonPath.read;
import static com.jayway.jsonpath.JsonPath.using;

/**
 * Created by zhouxw on 18/1/2.
 */
public class JsonpathTest {

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
    private static Object document = Configuration.defaultConfiguration().jsonProvider().parse(jsonString);
    private static ReadContext readContext = JsonPath.parse(jsonString);

    @Before
    public void init() {
        Configuration.setDefaults(new Configuration.Defaults() {

            private final JsonProvider jsonProvider = new JacksonJsonProvider();
            private final MappingProvider mappingProvider = new JacksonMappingProvider();

            public JsonProvider jsonProvider() {
                return jsonProvider;
            }

            public MappingProvider mappingProvider() {
                return mappingProvider;
            }

            public Set<Option> options() {
                return EnumSet.noneOf(Option.class);
            }
        });
    }
    @Test
    public void test1() {
        String json = "{\"date_as_long\" : 1411455611975}";
        Date date = JsonPath.parse(json).read("$['date_as_long']", Date.class);
        System.out.println(date);

    }
    @Test
    public void test2() {
        TypeRef<List<String>> typeRef = new TypeRef<List<String>>() {};

        List<String> titles = JsonPath.parse(jsonString).read("$.store.book[*].title",typeRef);
        System.out.println(titles);
    }
    @Test
    public void test3() {
        List<Map<String, Object>> books =  JsonPath.parse(jsonString)
                .read("$.store.book[?( @.price <  10)]");
        System.out.println(books);
    }
    @Test
    public void tt() {
        Object o = JsonPath.parse(jsonString).read("$.store.book[1]['price']");
        System.out.println(o);

    }
    @Test
    public void gg() {
        List list = new ArrayList();
        list.add(1);
        System.out.println(list);
        System.out.println(list.toString());
    }

    @Test
    public void test4() {
        Filter filter = filter(where("category").is("fiction").and("price").lte(10d));
        List<Map<String, Object>> books = parse(jsonString).read("$.store.book[?]",filter);
        System.out.println(books);
    }

    @Test
    public void test5() {
        Filter filter = filter(where("category").is("fiction").and("price").lte(10d));
        Filter fooOrBar =  filter(where("foo").exists(true)).or(where("bar").exists(true));
        Filter fooAndBar = filter(where("foo").exists(true)).and(where("bar").exists(true));
        List<Map<String, Object>> books = parse(jsonString).read("$.store.book[?,?]",filter,fooOrBar);
        System.out.println(books);

    }
    @Test
    public void test6() {
       Predicate predicate = new Predicate() {
           public boolean apply(PredicateContext ctx) {
               return ctx.item(Map.class).containsKey("isbn");
           }
       };
        List<Map<String, Object>> books = read(document,"$.store.book[?].isbn",predicate);
        System.out.println(books);
    }
    @Test
    public void test7() {
        Configuration configuration = Configuration.builder().options(Option.AS_PATH_LIST).build();
        List<String> list = using(configuration).parse(jsonString).read("$..author");
        System.out.println(list);
    }

    @Test
    public void test8() {
            String s = "[\n" +
                    "   {\n" +
                    "      \"name\" : \"john\",\n" +
                    "      \"gender\" : \"male\"\n" +
                    "   },\n" +
                    "   {\n" +
                    "      \"name\" : \"ben\"\n" +
                    "   }\n" +
                    "]";
        Configuration conf = Configuration.defaultConfiguration();


        conf.addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);
        

        Configuration conf2 = conf.addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);
        String gender1 = JsonPath.using(conf2).parse(s).read("$[1]['gender']");
        System.out.println(gender1);



        Configuration conf3 = conf.addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);
        String render3 = JsonPath.using(conf3).parse(s).read("$[1]['gender']");
        System.out.println(render3);

    }
    @Test

    public void text9() {
        int j = 0;
        try {
            int a = 1/0;
        }catch (Exception e) {

        }

       for (int i = 0  ; i < 100; i++) {
           System.out.println(i);
           j++;
           System.out.println(i);
           System.out.println(i);
           System.out.println(i);
           System.out.println(i);

           System.out.println(i);
           System.out.println(i);

       }
    }
    @Test
    public void text10() {
        String message = "{\\\"color\\\":\\\"red\\\",\\\"price\\\":19.95}";
    }


}
