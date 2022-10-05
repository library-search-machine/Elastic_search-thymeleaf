package com.example.elasticsearchtest;

import com.example.elasticsearchtest.domain.LibraryEs;
import com.example.elasticsearchtest.repository.LibraryEsRepository;
import feign.Response;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;

@EnableJpaAuditing
@SpringBootApplication
@EnableScheduling
public class ElasticsearchtestApplication {


    @Qualifier("elasticsearchTemplate")
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;


        @Scheduled(cron = "* * 23 * * *")
        public void cronJobSch() throws IOException, ParseException {
            System.out.println("시작되었음");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            String strDate = sdf.format(now);
            System.out.println("Java cron job expression:: " + strDate);

            Calendar cal = new GregorianCalendar(Locale.KOREA);
            cal.setTime(now);
            cal.add(Calendar.DATE, -1);
            String time =sdf.format(cal.getTime());
            long beforeTime = System.currentTimeMillis(); //코드 실행 전에 시간 받아오기


            //먼저 도서관 코드 모음
            //도서관 url https://data4library.kr/api/libSrch?authKey=6bd363e870bb744d2e52c35f15cfef0aa929faba70bc2d66961aae91e101901f&pageSize=325&region=11&format=json

            URL urlLib = new URL("https://data4library.kr/api/libSrch?authKey=6bd363e870bb744d2e52c35f15cfef0aa929faba70bc2d66961aae91e101901f&pageSize=325&region=11&format=json");
            BufferedReader bfLibrary;
            bfLibrary = new BufferedReader(new InputStreamReader(urlLib.openStream(), StandardCharsets.UTF_8));
            String library = bfLibrary.readLine();
            ArrayList<String> libCode = new ArrayList<>();
            JSONParser jP = new JSONParser();
            JSONObject jO = (JSONObject) jP.parse(library);
            JSONObject response1 = (JSONObject) jO.get("response");
            JSONArray infoArr1 = (JSONArray) response1.get("libs");

            for (int i = 0; i < infoArr1.size(); i++) {
                JSONObject tmp = (JSONObject) infoArr1.get(i);
                JSONObject tmp2 = (JSONObject) tmp.get("lib");
                libCode.add((String) tmp2.get("libCode"));
            }
            System.out.println("라이브러리 코드 완성");
            for(int i=0;i<libCode.size();i++) {
                List<IndexQuery> queries = new ArrayList<>();
                int total = -1, size = 500, page = 1;
                Long resultInt = 0l;
                do {
                    total = size * page;
                    String result = "";
                    URL url = new URL("http://data4library.kr/api/itemSrch?authKey=d39c7f3d9d7c547994399f4d90e13c5529b752ba9f9345f4cf8a884c2e7e63fa&libCode=" + libCode.get(i) + "&pageNo=" + page + "&pageSize=" + size+"&startDt="+time+"&endDt="+time+"&format=json");
                    BufferedReader bf;
                    bf = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
                    result = bf.readLine();

                    JSONParser jsonParser = new JSONParser();
                    JSONObject jsonObject = (JSONObject) jsonParser.parse(result);


                    JSONObject response = (JSONObject) jsonObject.get("response");
                    resultInt = (Long) response.get("numFound");
                    System.out.println(response.get("libNm"));
                    if (resultInt == 0L) {
                        break;
                    }
                    JSONArray infoArr = (JSONArray) response.get("docs");

                    for (int j = 0; j < infoArr.size(); j++) {
                        JSONObject tmp = (JSONObject) infoArr.get(j);
                        JSONObject tmp2 = (JSONObject) tmp.get("doc");
                        IndexQuery query = null;
                        String equalDelate = (String) tmp2.get("bookname");
                        equalDelate=equalDelate.replaceAll("="," ");
                        try {

                            query = new IndexQueryBuilder()
                                    .withObject(new LibraryEs( equalDelate, (String) response.get("libNm"), (String) tmp2.get("publisher"), (String) tmp2.get("publication_year"), (String) tmp2.get("authors"), (String) tmp2.get("class_no"), (String) tmp2.get("isbn13"), (String) tmp2.get("vol")))
                                    .build();


                        } catch (ClassCastException e) {
                            JSONArray k = (JSONArray)tmp2.get("publication_year");
                            System.out.println(k.toJSONString());

                            query = new IndexQueryBuilder()
                                    .withObject(new LibraryEs(equalDelate, (String) response.get("libNm"), (String) tmp2.get("publisher"),  k.toJSONString(), (String) tmp2.get("authors"), (String) tmp2.get("class_no"), (String) tmp2.get("isbn13"), (String) tmp2.get("vol")))
                                    .build();
                        } finally {
                            queries.add(query);
                        }

                    }


                    elasticsearchOperations.bulkIndex(queries, LibraryEs.class);
                    System.out.println(time+" "+resultInt);
                    page++;
                } while (resultInt > total);


            }

            long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
            long secDiffTime = (afterTime - beforeTime)/1000; //두 시간에 차 계산
            System.out.println("시간차이(m) : "+secDiffTime);

        };


    public static void main(String[] args) {
        SpringApplication.run(ElasticsearchtestApplication.class, args);
    }
    /*
    public CommandLineRunner demo(LibraryEsRepository libraryEsRepository) {


        return (args) -> {
            long beforeTime = System.currentTimeMillis(); //코드 실행 전에 시간 받아오기


            int total=-1,size=100,page=1;
            Long resultInt=0l;
            do {
                String result = "";
                URL url = new URL("http://data4library.kr/api/itemSrch?authKey=d39c7f3d9d7c547994399f4d90e13c5529b752ba9f9345f4cf8a884c2e7e63fa&libCode=127058&type=ALL&pageNo="+page+"&pageSize="+size+"&format=json");
                BufferedReader bf;
                bf = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
                result = bf.readLine();

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(result);


                JSONObject response = (JSONObject) jsonObject.get("response");
                resultInt=(Long) response.get("numFound");
                JSONArray infoArr = (JSONArray) response.get("docs");
                List<LibraryEs> libraries = new ArrayList<>();
                for (int i = 0; i < infoArr.size(); i++) {
                    JSONObject tmp = (JSONObject) infoArr.get(i);
                    JSONObject tmp2 = (JSONObject) tmp.get("doc");
                    libraries.add(new LibraryEs((String) tmp2.get("bookname"), (String) response.get("libNm")));
                }

                page++;
            }while(resultInt>total);

            long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
            long secDiffTime = (afterTime - beforeTime)/1000; //두 시간에 차 계산
            System.out.println("시간차이(m) : "+secDiffTime);

        };
    }*/
}
