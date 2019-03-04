package hiyouka.seedframework.context;

import hiyouka.seedframework.beans.annotation.ComponentScan;
import hiyouka.seedframework.context.annotation.Configuration;
import hiyouka.seedframework.context.annotation.Import;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
@Configuration
@ComponentScan("hiyouka.seedframework.context")
@Import({ImportClass.class,ImportClass2.class})
public class ConfigClass {

}