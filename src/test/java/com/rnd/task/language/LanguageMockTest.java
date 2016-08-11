package com.rnd.task.language;

import com.rnd.task.language.exception.FileNotValidException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by srini on 09/08/2016.
 */
public class LanguageMockTest {
    private Language language;
    @Mock private FileReader fileReader;
    @Mock private com.rnd.task.language.Dictionary dictionary;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        language = new Language(fileReader, dictionary, "mock text file name", "mock text file folder", "mock dictionary folder");
    }

    @Test
    public void should_handle_not_valid_exception_in_file_reader() throws IOException, FileNotValidException {
        when(fileReader.readAllLinesWithCharacterCheck(anyString())).thenThrow(new FileNotValidException("mock file not valid exception"));
        String languageStr = language.determineLanguage();
        assertThat(languageStr, is("UNKNOWN"));
    }

    @Test
    public void should_handle_filereader_read_directory_io_exception() throws IOException {
        when(fileReader.readDirectory(anyString())).thenThrow(new IOException());
        String languageStr = language.determineLanguage();
        assertThat(languageStr, is("UNKNOWN"));
    }

    @Test
    public void should_handle_filereader_read_all_lines_io_exception() throws IOException, FileNotValidException {
        when(fileReader.readAllLinesWithCharacterCheck(anyString())).thenThrow(new IOException());
        String languageStr = language.determineLanguage();
        assertThat(languageStr, is("UNKNOWN"));
    }

    @Test
    public void should_handle_null_input_file() throws IOException, FileNotValidException {
        when(fileReader.readAllLinesWithCharacterCheck(anyString())).thenReturn(null);
        String languageStr = language.determineLanguage();
        assertThat(languageStr, is("UNKNOWN"));
    }

    @Test
    public void should_set_field_variables_properly() {
        language.setDictionaryFolder("mock dictionary folder");
        language.setTextFileName("mock text file name");
        language.setTextFileFolder("mock text file folder");
        assertThat(language.getDictionaryFolder(), is("mock dictionary folder"));
        assertThat(language.getTextFileName(), is("mock text file name"));
        assertThat(language.getTextFileFolder(), is("mock text file folder"));
        assertThat(language.getDictionary(), is(dictionary));
    }

    @Test
    public void should_calculate_correct_scores_given_no_duplicates_in_text_file() {
        List<String> spanishLines = Arrays.asList("ola", "como", "esta");
        List<String> englishLines = Arrays.asList("hi", "how", "are", "you");
        List<String> frenchLines = Arrays.asList("bonjour", "cava");

        List<File> dictionaryFiles = new ArrayList<>();
        File spanishFile = new File(spanishLines, "mock parent dictionary", "spanish");
        File englishFile = new File(englishLines, "mock parent dictionary", "eNglIsh");
        File frenchFile = new File(frenchLines, "mock parent dictionary", "French");
        dictionaryFiles.add(spanishFile);
        dictionaryFiles.add(englishFile);
        dictionaryFiles.add(frenchFile);

        Map<String, Set<String>> myDictionary = new HashMap<>();
        myDictionary.put("SPANISH", new HashSet<>(spanishLines));
        myDictionary.put("ENGLISH", new HashSet<>(englishLines));
        myDictionary.put("FRENCH", new HashSet<>(frenchLines));

        when(language.getDictionary().getDictionary()).thenReturn(myDictionary);

        File inputFile = new File(Arrays.asList("como", "esta"), "mock parent", "TEXT");
        Map<String, Integer> score = language.calculateScore(dictionaryFiles, inputFile);
        assertThat(score.get("SPANISH"), is(2));
        assertThat(score.get("ENGLISH"), is(0));
        assertThat(score.get("FRENCH"), is(0));
    }


    @Test
    public void should_calculate_correct_scores_given_duplicates_in_text_file() {
        List<String> spanishLines = Arrays.asList("ola", "como", "esta");
        List<String> englishLines = Arrays.asList("hi", "how", "are", "you");
        List<String> frenchLines = Arrays.asList("bonjour", "cava");

        List<File> dictionaryFiles = new ArrayList<>();
        File spanishFile = new File(spanishLines, "mock parent dictionary", "spanish");
        File englishFile = new File(englishLines, "mock parent dictionary", "eNglIsh");
        File frenchFile = new File(frenchLines, "mock parent dictionary", "French");
        dictionaryFiles.add(spanishFile);
        dictionaryFiles.add(englishFile);
        dictionaryFiles.add(frenchFile);

        Map<String, Set<String>> myDictionary = new HashMap<>();
        myDictionary.put("SPANISH", new HashSet<>(spanishLines));
        myDictionary.put("ENGLISH", new HashSet<>(englishLines));
        myDictionary.put("FRENCH", new HashSet<>(frenchLines));

        when(language.getDictionary().getDictionary()).thenReturn(myDictionary);

        File inputFile = new File(Arrays.asList("como", "esta", "como", "esta"), "mock parent", "TEXT");
        Map<String, Integer> score = language.calculateScore(dictionaryFiles, inputFile);
        assertThat(score.get("SPANISH"), is(2));
        assertThat(score.get("ENGLISH"), is(0));
        assertThat(score.get("FRENCH"), is(0));
    }

    @Test
    public void should_ignore_hyphen() {
        List<String> spanishLines = Arrays.asList("ola", "como", "esta");
        List<String> englishLines = Arrays.asList("hi", "how", "are", "you");
        List<String> frenchLines = Arrays.asList("bonjour", "cava");

        List<File> dictionaryFiles = new ArrayList<>();
        File spanishFile = new File(spanishLines, "mock parent dictionary", "spanish");
        File englishFile = new File(englishLines, "mock parent dictionary", "eNglIsh");
        File frenchFile = new File(frenchLines, "mock parent dictionary", "French");
        dictionaryFiles.add(spanishFile);
        dictionaryFiles.add(englishFile);
        dictionaryFiles.add(frenchFile);

        Map<String, Set<String>> myDictionary = new HashMap<>();
        myDictionary.put("SPANISH", new HashSet<>(spanishLines));
        myDictionary.put("ENGLISH", new HashSet<>(englishLines));
        myDictionary.put("FRENCH", new HashSet<>(frenchLines));

        when(language.getDictionary().getDictionary()).thenReturn(myDictionary);

        File inputFile = new File(Arrays.asList("hello-world hi"), "mock parent", "TEXT");
        Map<String, Integer> score = language.calculateScore(dictionaryFiles, inputFile);
        assertThat(score.get("SPANISH"), is(0));
        assertThat(score.get("ENGLISH"), is(1));
        assertThat(score.get("FRENCH"), is(0));
    }

}
