package com.rnd.task.language;

import com.rnd.task.language.Dictionary;
import com.rnd.task.language.File;
import com.rnd.task.language.FileReader;
import com.rnd.task.language.exception.FileNotValidException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by srini on 09/08/2016.
 */
public class DictionaryTest extends AbstractJUnitSpringTest {
    @Autowired private FileReader fileReader;
    @Autowired private Dictionary dictionary;

    public static final String ENGLISH = "ENGLISH";
    public static final String INDONESIAN = "INDONESIAN";

    @Before
    public void setUp() {
        dictionary.getDictionary().clear();
    }

    @Test(expected = IllegalStateException.class)
    public void should_throw_exception_given_null_word() {
        dictionary.storeLineInDictionary(null, ENGLISH);
    }

    @Test(expected = IllegalStateException.class)
    public void should_throw_exception_given_blank_word() {
        dictionary.storeLineInDictionary("", ENGLISH);
    }

    @Test
    public void should_remove_duplicate_word_given_input() {
        dictionary.storeLineInDictionary("Hello, world. hello: hello ", ENGLISH);
        Map<String, Set<String>> dictionaryMap = dictionary.getDictionary();
        assertThat(dictionaryMap, notNullValue());
        assertThat(dictionaryMap.size(), is(1));
        assertThat(dictionaryMap.get(ENGLISH), notNullValue());
        assertThat(dictionaryMap.get(ENGLISH).size(), is(2));
        assertThat(dictionaryMap.get(ENGLISH).contains("hello"), is(true));
        assertThat(dictionaryMap.get(ENGLISH).contains("world"), is(true));
    }

    @Test
    public void should_read_and_store_one_file() throws IOException, FileNotValidException {
        dictionary.readAndStore(fileReader.readAllLinesWithCharacterCheck("./src/test/resources/dictionaryfiles/ENGLISH.2"));
        Map<String, Set<String>> dictionaryMap = dictionary.getDictionary();

        assertThat(dictionaryMap, notNullValue());
        assertThat(dictionaryMap.size(), is(1));

        Set<String> words = dictionaryMap.get(ENGLISH);
        assertThat(words, notNullValue());
        assertThat(words.size(), is(7));
        assertThat(words.contains("i"), is(true));
        assertThat(words.contains("wonder"), is(true));
        assertThat(words.contains("what"), is(true));
        assertThat(words.contains("will"), is(true));
        assertThat(words.contains("happen"), is(true));
        assertThat(words.contains("tomorrow"), is(true));
        assertThat(words.contains("hmm"), is(true));
    }

    @Test
    public void should_read_and_store_multiple_files_one_language() throws IOException, FileNotValidException {
        dictionary.readAndStore(fileReader.readAllLinesWithCharacterCheck("./src/test/resources/dictionaryfiles/ENGLISH.2"));
        dictionary.readAndStore(fileReader.readAllLinesWithCharacterCheck("./src/test/resources/dictionaryfiles/ENGLISH.3"));

        Map<String, Set<String>> dictionaryMap = dictionary.getDictionary();
        assertThat(dictionaryMap, notNullValue());
        assertThat(dictionaryMap.size(), is(1));

        Set<String> words = dictionaryMap.get(ENGLISH);
        assertThat(words, notNullValue());
        assertThat(words.size(), is(92));
        assertThat(words.contains("i"), is(true));
        assertThat(words.contains("wonder"), is(true));
        assertThat(words.contains("what"), is(true));
        assertThat(words.contains("will"), is(true));
        assertThat(words.contains("happen"), is(true));
        assertThat(words.contains("tomorrow"), is(true));
        assertThat(words.contains("hmm"), is(true));
        assertThat(words.contains("saint"), is(true));
        assertThat(words.contains("seiya"), is(true));
    }

    @Test
    public void should_read_and_store_multiple_files_multiple_languages() throws IOException, FileNotValidException {
        List<File> dictionaryFiles = fileReader.readDirectory("./src/test/resources/dictionaryfiles");
        for (File dictionaryFile : dictionaryFiles) {
            dictionary.readAndStore(dictionaryFile);
        }
        Map<String, Set<String>> dictionaryMap = dictionary.getDictionary();
        assertThat(dictionaryMap, notNullValue());
        assertThat(dictionaryMap.size(), is(3));

        Set<String> englishWords = dictionaryMap.get(ENGLISH);
        assertThat(englishWords, notNullValue());
        assertThat(englishWords.size(), is(93));
        assertThat(englishWords.contains("i"), is(true));
        assertThat(englishWords.contains("wonder"), is(true));
        assertThat(englishWords.contains("what"), is(true));
        assertThat(englishWords.contains("will"), is(true));
        assertThat(englishWords.contains("happen"), is(true));
        assertThat(englishWords.contains("tomorrow"), is(true));
        assertThat(englishWords.contains("hmm"), is(true));
        assertThat(englishWords.contains("saint"), is(true));
        assertThat(englishWords.contains("seiya"), is(true));

        Set<String> indonesianWords = dictionaryMap.get(INDONESIAN);
        assertThat(indonesianWords, notNullValue());
        assertThat(indonesianWords.size(), is(89));
        assertThat(indonesianWords.contains("bali"), is(true));
        assertThat(indonesianWords.contains("tanah"), is(true));
        assertThat(indonesianWords.contains("rot"), is(true));
        assertThat(indonesianWords.contains("lot"), is(true));
        assertThat(indonesianWords.contains("kembari"), is(true));
    }

    @Test
    public void should_not_return_exception_given_file_with_illegal_character() throws IOException, FileNotValidException {
        dictionary.readAndStore(fileReader.readAllLinesWithCharacterCheck("./src/test/resources/dictionaryfiles/ENGLISH.1"));
    }
}
