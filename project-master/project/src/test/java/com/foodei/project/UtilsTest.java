package com.foodei.project;

import static org.assertj.core.api.Assertions.*;

import com.foodei.project.utils.Utils;
import org.junit.jupiter.api.Test;

public class UtilsTest {
    @Test
    void getExtension() {
        String extension = Utils.getExtensionFile("pic1.JPG");
        assertThat(extension).isEqualTo("JPG");
        assertThat(extension).isNotEqualTo("jpg");
    }
}
