package com.example.elasticsearchtest;

import com.example.elasticsearchtest.domain.LibraryEs;
import com.example.elasticsearchtest.repository.LibraryEsRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SpringBootApplication
public class ElasticsearchtestApplication {

    @Qualifier("elasticsearchTemplate")
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;


    public static void main(String[] args) {
        SpringApplication.run(ElasticsearchtestApplication.class, args);
    }
//    @Bean
//    public CommandLineRunner demo(LibraryEsRepository libraryEsRepository) {
//
//
//        return (args) -> {
//            long beforeTime = System.currentTimeMillis(); //코드 실행 전에 시간 받아오기
//
//
//
//            try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get("C:\\Users\\tlseh\\Downloads\\도서관 엑셀"))) {
//                for (Path file: stream) {
//
//
//                    List<IndexQuery> queries = new ArrayList<>();
//                    String[] tmp2 = file.getFileName().toString().split(" ");
//                    BufferedReader br = null;
//                    String line = "";
//                    String libraryName = null;
//                    FileInputStream fis = new FileInputStream("C:\\Users\\tlseh\\Downloads\\도서관 엑셀\\" + file.getFileName());
//
//                    XSSFWorkbook workbook = new XSSFWorkbook(fis);
//                    if (tmp2.length == 1) {
//                        libraryName = tmp2[0].split("\\.")[0];
//                    } else {
//                        StringBuilder sb = new StringBuilder();
//
//                        for (int i = 0; i < tmp2.length - 4; i++) {
//
//                            sb.append(tmp2[i]);
//
//                        }
//                        libraryName = sb.toString();
//                    }
//
//                    int sheets = workbook.getNumberOfSheets();
//                    for (int i = 0; i < sheets; i++) {
//                        Sheet sheet = workbook.getSheetAt(i);
//
//
//	/* 1. row 얻기 : getPhysicalNumberOfRows();
//	int rows = sheet.getPhysicalNumberOfRows();
//	for(int r = 0; r < rows; r++){
//		..
//	}
//	*/
//
//                        // 2. row 얻기 : iterator();
//                        Iterator<Row> rowIterator = sheet.iterator();
//                        rowIterator.next();
//                        while (rowIterator.hasNext()) {
//                            Row row = rowIterator.next();
//
//
//                            List<String> aLine = new ArrayList<>();
//                            // 2. cell 얻기 : cellIterator();
//                            Iterator<Cell> cellIterator = row.cellIterator();
//                            while (cellIterator.hasNext()) {
//                                Cell cell = cellIterator.next();
//                                switch (cell.getCellType()) {
//                                    case BOOLEAN:
//
//                                        aLine.add(String.valueOf(cell.getBooleanCellValue()));
//                                        break;
//                                    case NUMERIC:
//                                        aLine.add(String.valueOf(cell.getNumericCellValue()));
//                                        break;
//                                    case STRING:
//                                        aLine.add(cell.getStringCellValue());
//                                        break;
//                                    case FORMULA:
//
//                                        break;
//                                }// switch
//                            }// while
//                            if (aLine.size() == 0) continue;
//
//                            IndexQuery query = new IndexQueryBuilder()
//
//                                    .withObject(new LibraryEs(aLine.get(1), libraryName, aLine.get(3), aLine.get(4), aLine.get(2), aLine.get(9), aLine.get(5), aLine.get(8)))
//                                    .build();
//
//                            queries.add(query);
//                            if (queries.size() == 1000) {
//                                elasticsearchOperations.bulkIndex(queries, LibraryEs.class);
//                                queries.clear();
//                            }
//                        }// while
//                        elasticsearchOperations.bulkIndex(queries, LibraryEs.class);
//                    }// for
//                    fis.close();
//                }
//
//                   /*
//                    try {
//                        br = new BufferedReader(reader);
//
//                        while ((line = br.readLine()) != null) { // readLine()은 파일에서 개행된 한 줄의 데이터를 읽어온다.
//                            List<String> aLine;
//                            String[] lineArr = line.split("\\|"); // 파일의 한 줄을 |로 나누어 배열에 저장 후 리스트로 변환한다.
//                            if(lineArr.length==0) continue;
//
//                            //한줄 밀리는 현상에 대해서 픽스하기위한 코드
//                            if(lineArr.length!=13){
//                                StringBuilder sb = new StringBuilder();
//                                sb.append(line);
//                                line = br.readLine();
//                                sb.append(line);
//                                lineArr = sb.toString().split("\\|");
//                            }
//
//                            aLine = Arrays.asList(lineArr);
//
//                            IndexQuery query = new IndexQueryBuilder()
//
//                                    .withObject(new LibraryEs(aLine.get(1),tmp[0],aLine.get(3),aLine.get(4),aLine.get(2),aLine.get(9),aLine.get(5),aLine.get(8)))
//                                    .build();
//
//                            queries.add(query);
//                            if(queries.size()==1000){
//                                elasticsearchOperations.bulkIndex(queries, LibraryEs.class);
//                                queries.clear();
//                            }
//                        }
//                        elasticsearchOperations.bulkIndex(queries, LibraryEs.class);
//                        input.close();
//                        reader.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } finally {
//                        try {
//                            if (br != null) {
//                                br.close(); // 사용 후 BufferedReader를 닫아준다.
//
//                            }
//                        } catch(IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//
//                }*/
//            } catch (IOException | DirectoryIteratorException ex) {
//                System.err.println(ex);
//            }
//
//
//            long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
//            long secDiffTime = (afterTime - beforeTime)/1000; //두 시간에 차 계산
//            System.out.println("시간차이(m) : "+secDiffTime);
//
//        };
//    }
}
