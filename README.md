# Language Problem

You are given a number of files from a number of different human languages. All languages use the 
Latin alphabet A to Z; case can be ignored and the only punctuation characters are . , ; : and single 
space characters separating words. For example, you might have the files FRENCH.1, GAELIC.2 to 
GAELIC.9, ENGLISH.1 to ENGLISH.6 and so on. 

Develop a program to identify its as a Given a file TEXT.txt in the same format but of unknown origin, 
language.

# Assumptions
- Known language files have the language name as the filename and numbers as the extension eg. ENGLISH.1 ENGLISH.2 FRENCH.1 FRENCH.2 would have 2 languages: ENGLISH and FRENCH
- The files provided are small enough that a computer with a reasonable memory should be able to store the data in memory instead of using a database
- Dictionary is fixed once the program runs, but TEXT.txt can change so the the program can detect the language in the text file
- If characters other than allowed ones exist, program will show error message saying illegal character exists, and will continue to the next line and If there is not enough data to determine the language, then UNKNOWN will be returned
- If the program determines that text could be in either language, for example "ENGLISH" or "FRENCH", then it will pick either one with no guaranteed order


# Software Needed
- JDK 8
- Maven 3.3.3

# Usage
## Spring Boot
- mvn clean spring-boot:run
- Enter input: text file name, text file folder and dictionary folder. Press ENTER to leave as default values.
- Modify text file and Language Indentifier program will detect language based on files in dictionary folder

## JAR Package
- mvn clean package
- java -jar target/language-1.0.0.jar
- Enter input: text file name, text file folder and dictionary folder. Press ENTER to leave as default values.
- Modify text file and Language Indentifier program will indentify the language based on files in dictionary folder
