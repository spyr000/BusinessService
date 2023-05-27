package cs.vsu.businessservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.UnsupportedEncodingException;

@SpringBootApplication
public class BusinessServiceApplication {

    public static void main(String[] args) throws UnsupportedEncodingException {
//        File currentClass = new File(URLDecoder.decode(BusinessServiceApplication.class
//                .getProtectionDomain()
//                .getCodeSource()
//                .getLocation()
//                .getPath(), "UTF-8"));
//        String classDirectory = currentClass.getParent();
//        System.out.println("DIRECTORY! " + classDirectory);
        SpringApplication.run(BusinessServiceApplication.class, args);
    }

}
