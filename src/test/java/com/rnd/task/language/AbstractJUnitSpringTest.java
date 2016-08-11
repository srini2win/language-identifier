package com.rnd.task.language;

import com.rnd.task.language.Dictionary;
import com.rnd.task.language.FileReader;
import com.rnd.task.language.Language;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by srini on 09/08/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Language.class, Dictionary.class, FileReader.class})
public abstract class AbstractJUnitSpringTest {
}
