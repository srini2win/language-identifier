package com.rnd.task.language;

import com.rnd.task.language.exception.FileNotValidException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

/**
 * Created by srini on 09/08/2016.
 */
@Component
public class Language {
    private static final Logger LOGGER = LoggerFactory.getLogger(Language.class);

    private static final String UNKNOWN = "UNKNOWN";
    private FileReader fileReader;
    private Dictionary dictionary;
    private String textFileName;
    private String textFileFolder;
    private String dictionaryFolder;

    public Language() {
    }

    @Autowired
    public Language(FileReader fileReader, Dictionary dictionary,
                    @Value("${text.file.name}") String textFileName,
                    @Value("${text.file.folder}") String textFileFolder,
                    @Value("${dictionary.folder}") String dictionaryFolder) {
        this.fileReader = fileReader;
        this.dictionary = dictionary;
        this.textFileName = textFileName;
        this.textFileFolder = textFileFolder;
        this.dictionaryFolder = dictionaryFolder;
    }

    public String determineLanguage() {
        String pathStr = textFileFolder + "/" + textFileName;
        String pathDictionaryStr = dictionaryFolder;
        try {
            List<File> dictionaryFiles = fileReader.readDirectory(pathDictionaryStr);
            dictionaryFiles.forEach(dictionaryFile -> dictionary.readAndStore(dictionaryFile));

            String language = determineLanguage(pathStr, dictionaryFiles);
            LOGGER.debug("Language is: {}", language);
            return language;
        } catch (IOException ex) {
            return UNKNOWN;
        }
    }

    private String determineLanguage(String pathStr, List<File> dictionaryFiles) throws IOException {
        File inputFile;
        try {
            inputFile = fileReader.readAllLinesWithCharacterCheck(pathStr);
            if (inputFile == null) return UNKNOWN;
        } catch (FileNotValidException e) {
            return UNKNOWN;
        }

        Map<String, Integer> languageScore = calculateScore(dictionaryFiles, inputFile);

        Map.Entry<String, Integer> maxEntry = null;
        int totalScore = 0;
        for (Map.Entry<String, Integer> scoreEntry : languageScore.entrySet()) {
            if (maxEntry == null || scoreEntry.getValue() > maxEntry.getValue()) {
                maxEntry = scoreEntry;
            }
            totalScore += scoreEntry.getValue();
        }

        if (maxEntry == null || totalScore == 0) {
            return UNKNOWN;
        }
        return maxEntry.getKey();
    }

    Map<String, Integer> calculateScore(List<File> dictionaryFiles, File inputFile) {
        Map<String, Integer> languageScore = new HashMap<>();
        dictionaryFiles.forEach(dictionaryFile -> languageScore.put(dictionaryFile.getLanguage(), 0));

        Set<String> allWords = new HashSet<>();
        inputFile.getLines().forEach(line -> {
            LOGGER.debug("line = {}", line);
            line = StringUtils.lowerCase(line.replaceAll(Dictionary.NON_ALPHABET_AND_SPACE_REGEX, ""));
            String[] wordArray = StringUtils.split(line);
            allWords.addAll(Arrays.asList(wordArray));
        });

        dictionary.getDictionary().forEach((language, dictionaryWords) -> {
            LOGGER.debug("Checking {}", language);

            allWords.forEach(word -> {
                if (dictionaryWords.contains(word)) {
                    LOGGER.debug("dictionary contains word {} for com.rnd.task.language {}", word, language);
                    languageScore.put(language, languageScore.get(language) + 1);
                }
            });
        });

        LOGGER.info("Language Scores: {}", languageScore);
        return languageScore;
    }

    public String getTextFileName() {
        return textFileName;
    }

    public String getTextFileFolder() {
        return textFileFolder;
    }

    public String getDictionaryFolder() {
        return dictionaryFolder;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public void setTextFileName(String textFileName) {
        this.textFileName = textFileName;
    }

    public void setTextFileFolder(String textFileFolder) {
        this.textFileFolder = textFileFolder;
    }

    public void setDictionaryFolder(String dictionaryFolder) {
        this.dictionaryFolder = dictionaryFolder;
    }
}
