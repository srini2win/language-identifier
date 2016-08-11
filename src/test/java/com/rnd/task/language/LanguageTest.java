package com.rnd.task.language;

import com.rnd.task.language.Language;
import com.rnd.task.language.exception.FileNotValidException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by srini on 09/08/2016.
 */
public class LanguageTest extends AbstractJUnitSpringTest {
    @Autowired private Language language;

    @Before
    public void setUp() throws IOException, FileNotValidException {
        language.getDictionary().getDictionary().clear();
        language.setTextFileFolder("./src/test/resources/textfile");
    }

    @Test
    public void should_return_indonesian_given_text() throws IOException, FileNotValidException {
        language.setTextFileName("TEXT.txt");
        language.setDictionaryFolder("./src/test/resources/dictionaryfiles");
        String languageStr = language.determineLanguage();
        assertThat(languageStr, is("INDONESIAN"));
    }

    @Test
    public void should_return_unknown_given_text_with_all_zero_occurence() throws IOException, FileNotValidException {
        language.setTextFileName("TEXT2.txt");
        language.setDictionaryFolder("./src/test/resources/dictionaryfiles2");
        String languageStr = language.determineLanguage();
        assertThat(languageStr, is("UNKNOWN"));
    }

    @Test
    public void should_return_either_given_same_words() throws IOException, FileNotValidException {
        language.setTextFileName("TEXT3.txt");
        language.setDictionaryFolder("./src/test/resources/dictionaryfiles3");
        String languageStr = language.determineLanguage();
        assertThat(languageStr, is("ENGLISH"));
    }

    @Test
    public void should_return_spanish() throws IOException, FileNotValidException {
        language.setTextFileName("TEXT4.txt");
        language.setDictionaryFolder("./src/test/resources/dictionaryfiles4");
        String languageStr = language.determineLanguage();
        assertThat(languageStr, is("SPANISH"));
    }

    @Test
    public void should_return_english_ignoring_hyphen() throws IOException, FileNotValidException {
        language.setTextFileName("TEXT5.txt");
        language.setDictionaryFolder("./src/test/resources/dictionaryfiles5");
        String languageStr = language.determineLanguage();
        assertThat(languageStr, is("ENGLISH"));
    }
}
