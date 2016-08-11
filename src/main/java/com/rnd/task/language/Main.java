package com.rnd.task.language;

import com.rnd.task.language.Language;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.util.List;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

/**
 * Created by srini on 09/08/2016.
 */
@SpringBootApplication
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Main.class, args);
        Language language = (Language) ctx.getBean("language");
        run(language);
    }

    static void run(Language language) {
        String defaultTextFileName = language.getTextFileName();
        String defaultTextFileFolder = language.getTextFileFolder();
        String defaultDictionaryFolder = language.getDictionaryFolder();

        boolean validUserInput = false;
        while (!validUserInput) {
            try {
                userInput(language, defaultTextFileName, defaultTextFileFolder, defaultDictionaryFolder, System.in);

                LOGGER.info("############### WELCOME TO LANGUAGE IDENTIFIER ###############");
                LOGGER.info("You can modify {} in {} manually", language.getTextFileName(), language.getTextFileFolder());
                LOGGER.info("and Language Identifier will identify the com.rnd.task.language");
                LOGGER.info("based on existing dictionary files in {}", language.getDictionaryFolder());
                LOGGER.info("##############################################################");

                String languageStr = language.determineLanguage();
                LOGGER.info("Language is {}", languageStr);
                Path myDir = Paths.get(language.getTextFileFolder());

                LOGGER.info("Watching folder {}", language.getTextFileFolder());

                while (true) {
                    WatchService watcher = myDir.getFileSystem().newWatchService();
                    myDir.register(watcher, ENTRY_MODIFY);
                    validUserInput = true;

                    WatchKey watchKey = watcher.take();

                    List<WatchEvent<?>> events = watchKey.pollEvents();
                    for (WatchEvent event : events) {
                        if (event.kind() == ENTRY_MODIFY) {
                            LOGGER.info("File modified: {}", event.context().toString());
                            String lang = language.determineLanguage();
                            LOGGER.info("Language is {}", lang);
                        }
                    }
                }
            } catch (NoSuchFileException e) {
                LOGGER.error("Invalid folder", e);
            } catch (InterruptedException | IOException e) {
                LOGGER.error("Error: ", e);
            }
        }
    }

    static void userInput(Language language, String defaultTextFileName, String defaultTextFileFolder, String defaultDictionaryFolder, InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        System.out.println("######################### USER INPUT #########################");
        System.out.print("Enter text file name (default is " + defaultTextFileName + "):");
        String textFileName = br.readLine();

        System.out.print("Enter text file folder (default is " + defaultTextFileFolder + "):");
        String textFileFolder = br.readLine();

        System.out.print("Enter dictionary folder (default is " + defaultDictionaryFolder + "):");
        String dictionaryFolder = br.readLine();

        language.setTextFileName(StringUtils.isBlank(textFileName) ? defaultTextFileName : textFileName);
        language.setTextFileFolder(StringUtils.isBlank(textFileFolder) ? defaultTextFileFolder : textFileFolder);
        language.setDictionaryFolder(StringUtils.isBlank(dictionaryFolder) ? defaultDictionaryFolder : dictionaryFolder);
        System.out.println("##############################################################");
    }
}
