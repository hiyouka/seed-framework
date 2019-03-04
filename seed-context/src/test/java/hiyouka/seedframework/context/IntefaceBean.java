package hiyouka.seedframework.context;

import hiyouka.seedframework.beans.annotation.Bean;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface IntefaceBean {

    @Bean
    default InteTestBean inteTestBean(){
        return new InteTestBean();
    }

}