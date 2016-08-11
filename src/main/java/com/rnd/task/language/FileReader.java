package com.rnd.task.language;

import com.rnd.task.language.exception.FileNotValidException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by srini on 09/08/2016.
 */
@Component
public class FileReader {
    private static final Logger LOG = LoggerFactory.getLogger(FileReader.class);

    public static final String LEGAL_CHARACTERS_REGEX = "^[a-zA-Z \\.\\,\\;\\:]+$";

    public FileReader() {
    }

    public List<File> readDirectory(String directory) throws IOException {
        List<String> fileNames = new ArrayList<>();
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(directory))) {
            directoryStream.forEach(path -> fileNames.add(path.toString()));
        }

        LOG.trace("fileNames: {}", fileNames);
        List<File> dictionaryFiles = new ArrayList<>();

        fileNames.forEach(fileName -> {
            try {
                File file = readAllLinesWithCharacterCheck(fileName);
                dictionaryFiles.add(file);
            } catch (FileNotValidException | IOException e) {
                // keep going
            }
        });

        return dictionaryFiles;
    }

    public File readAllLinesWithCharacterCheck(String pathStr) throws IOException, FileNotValidException {
        Path path = Paths.get(pathStr);
        List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
        LOG.debug("Reading file {}", path.getFileName().toString());

        String parent = path.getParent().toString();
        String fileName = path.getFileName().toString();
        Pattern pattern = Pattern.compile(LEGAL_CHARACTERS_REGEX);

        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            LOG.trace("line = {}", line);
            LOG.trace("matches {}", matcher.matches());

            if (StringUtils.isNotBlank(line) && !matcher.matches()) {
                LOG.error("File {} has illegal character(s)", path.getFileName().toString());
                //throw new FileNotValidException("File " + pathStr + " contains illegal characters");
            }
        }
        return new File(lines, parent, fileName);
    }
}
