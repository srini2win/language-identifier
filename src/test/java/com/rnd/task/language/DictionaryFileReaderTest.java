package com.rnd.task.language;

import com.rnd.task.language.File;
import com.rnd.task.language.FileReader;
import com.rnd.task.language.exception.FileNotValidException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Created by srini on 09/08/2016.
 */
public class DictionaryFileReaderTest extends AbstractJUnitSpringTest {
    @Autowired private FileReader fileReader;

    @Before
    public void setUp() {
    }

    @Test
    public void should_not_throw_exception_given_file_with_illegal_characters() throws IOException, FileNotValidException {
        fileReader.readAllLinesWithCharacterCheck("./src/test/resources/dictionaryfiles/ENGLISH.1");
    }

    @Test
    public void should_return_valid_lines_given_valid_file() throws IOException, FileNotValidException {
        File dictionaryFile = fileReader.readAllLinesWithCharacterCheck("./src/test/resources/dictionaryfiles/ENGLISH.2");
        List<String> lines = dictionaryFile.getLines();
        assertThat(lines.contains("tomorrow, hmm...."), is(true));
        assertThat(lines.contains("I wonder what will happen"), is(true));

        assertThat(dictionaryFile.getFileName(), is("ENGLISH.2"));
    }

    @Test
    public void should_return_list_of_valid_files_in_a_directory() throws IOException {
        List<File> dictionaryFiles = fileReader.readDirectory("./src/test/resources/dictionaryfiles");
        assertThat(dictionaryFiles, notNullValue());
        assertThat(dictionaryFiles.size(), is(5));
    }
}