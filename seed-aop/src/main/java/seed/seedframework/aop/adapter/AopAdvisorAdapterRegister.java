package seed.seedframework.aop.adapter;

import seed.seedframework.core.intercept.Advice;
import seed.seedframework.core.intercept.Advisor;
import seed.seedframework.core.intercept.MethodInterceptor;
import seed.seedframework.util.Assert;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class AopAdvisorAdapterRegister implements AdvisorAdapterRegister {

    private final List<AdvisorAdapter> adapters = new CopyOnWriteArrayList<>();

    public AopAdvisorAdapterRegister() {
        this.adapters.add(new MethodBeforeAdviceAdapter());
        this.adapters.add(new MethodAfterAdviceAdapter());
        this.adapters.add(new MethodAroundAdviceAdapter());
        this.adapters.add(new ThrowsAdviceAdapter());
    }

    @Override
    public void addAdapter(AdvisorAdapter adapter) {
        Assert.notNull(adapter,"adapter must not null");

    }

    @Override
    public MethodInterceptor[] getInterceptors(Advisor advisor) {
        List<MethodInterceptor> interceptors = new LinkedList<>();
        Advice advice = advisor.getAdvice();
        for(AdvisorAdapter adapter : this.adapters){
            if(adapter.supportAdvisor(advice)){
                interceptors.add(adapter.getInterceptor(advisor));
            }
        }
        return interceptors.toArray(new MethodInterceptor[0]);
    }
}