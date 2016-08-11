package com.rnd.task.language;

import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * Created by srini on 09/08/2016.
 */
public final class File {
    private final List<String> lines;
    private final String parent;
    private final String fileName;

    public File(final List<String> lines, final String parent, final String fileName) {
        this.parent = parent;
        this.lines = lines;
        this.fileName = fileName;
    }

    public final String getParent() {
        return parent;
    }

    public final List<String> getLines() {
        return lines;
    }

    public final String getFileName() {
        return fileName;
    }

    public final String getLanguage() {
        return StringUtils.upperCase(StringUtils.substringBefore(fileName, "."));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        File that = (File) o;
        return Objects.equals(parent, that.parent) &&
                Objects.equals(fileName, that.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, fileName);
    }
}
