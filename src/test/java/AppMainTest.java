import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.io.File;

class AppMainTest {

    @Before
    void init(){

    }

    @Test
    void processFile_test_does_not_contain_header() {

        AppMain appMain = new AppMain();

        File inputFile = new File("Resources/testfile_without_header.xml");

//        try{
//
//        }
    }
}