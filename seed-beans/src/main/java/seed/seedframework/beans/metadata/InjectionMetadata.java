package seed.seedframework.beans.metadata;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.Collection;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class InjectionMetadata {

    private static final Log logger = LogFactory.getLog(InjectionElement.class);

    private final Class target;

    private final Collection<InjectionElement> elements;


    public InjectionMetadata(Class target, Collection<InjectionElement> elements) {
        this.target = target;
        this.elements = elements;
    }

    public void inject(Object bean,String beanName,PropertyValues pvs) throws Throwable {
        Collection<InjectionElement> elements = this.elements;
        for(InjectionElement element : elements){
            if(logger.isDebugEnabled()){
                logger.debug("process inject element of bean :" + beanName + ":" + element);
            }
            element.inject(bean,beanName,pvs);
        }
    }

    public static abstract class InjectionElement{

        protected final Member member;

        protected final boolean isField;

        protected volatile boolean skip;

        public InjectionElement(Member member) {
            this.member = member;
            this.isField = (member instanceof Field);
        }

        /**
         * bean属性进行处理
         */
        protected abstract void inject(Object bean, String beanName, PropertyValues pvs) throws Throwable;

        public Member getMember() {
            return member;
        }

        public boolean isSkip() {
            return skip;
        }
    }

    public Class getTarget() {
        return target;
    }

    public Collection<InjectionElement> getElements() {
        return elements;
    }
}