package org.gtkim.example;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Properties;

public class PropertiesReadTest {
    @Test
    public void properties를_읽는다() throws Exception{
        // Given
        String workspace  = System.getProperty("user.dir");
        System.out.println("workspace = " + workspace);

        Properties prop = new Properties();
        BufferedReader br = new BufferedReader(new FileReader(workspace + File.separator +
                "src/test/resources/application.properties"));

        // When
        prop.load(br);
        int serverPort = Integer.parseInt(prop.getProperty("serverPort"));
        String tgrmContent = prop.getProperty("tgrmContent");
        String tgrmEncoding = prop.getProperty("tgrmEncoding");

        // Then
        assertThat(serverPort).isEqualTo(15151);
        assertThat(tgrmContent).isEqualTo("myResponse");
        assertThat(tgrmEncoding).isEqualTo("EUC-KR");
    }
}
