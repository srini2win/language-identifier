package com.rnd.task.language;

import com.rnd.task.language.Language;
import com.rnd.task.language.Main;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by srini on 09/08/2016.
 */
public class MainTest {
    private Language language;

    @Before
    public void setUp() {
        language = new Language();
    }

    @Test
    public void testUserInput() throws Exception {
        try (ByteArrayInputStream in = new ByteArrayInputStream("My text file name\nMy text file folder\nMy dictionary folder".getBytes())) {
            Main.userInput(language, "default1", "default2", "default3", in);

            assertThat(language.getTextFileName(), is("My text file name"));
            assertThat(language.getTextFileFolder(), is("My text file folder"));
            assertThat(language.getDictionaryFolder(), is("My dictionary folder"));
        }
    }
}