package hiyouka.seedframework.core.prop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class StandardPropertySources implements PropertySources {

    private final List<PropertySource<?>> propertySources = new ArrayList<>();

    private final Map<String,Integer> indexNameLink = new ConcurrentHashMap<>();


    public StandardPropertySources(){}


//    @Override
//    public Map<String, Object> getProperties() {
//        Map<String,Object> result = new HashMap<>();
//        for(Map.Entry<String,Integer> entry : this.indexNameLink.entrySet()){
//            PropertySource propertySource = this.propertySources.get(entry.getValue());
//            result.put(propertySource.getName(),propertySource.getValue());
//        }
//        return result;
//    }

    @Override
    public PropertySource getProperty(String name) {
        Integer index = this.indexNameLink.get(name);
        if(index == null){
            return null;
        }
        return this.propertySources.get(index);
    }

    @Override
    public void addProperty(PropertySource propertySource) {
        this.propertySources.add(propertySource);
        this.indexNameLink.put(propertySource.getName(),this.propertySources.size());

    }

    @Override
    public void removeProperty(PropertySource propertySource) {
        boolean remove = this.propertySources.remove(propertySource);
        if(remove){
            this.indexNameLink.remove(propertySource.getName());
            resetIndexNameLink(this.indexNameLink.get(propertySource.getName()));
        }
    }

    @Override
    public boolean containProperty(String name) {
        return this.indexNameLink.containsKey(name);
    }


//    @Override
//    public void loadResource(Resource resource) {
//        Assert.notNull(resource,"resource must not be null");
//        try {
//            if(resource.isOpen()){
//                Properties properties = new Properties();
//                properties.load(resource.getInputStream());
//                Enumeration<?> enumeration = properties.propertyNames();
//                while (enumeration.hasMoreElements()){
//                    String name = (String) enumeration.nextElement();
//                    PropertySource propertySource = new MapPropertySource(name,properties.getProperty(name));
//                    addProperty(propertySource);
//                }
//            }
//        } catch (IOException e) {
//            throw new IllegalStateException("IO error to read source : " + resource.getFilename() );
//        }
//    }


    private void resetIndexNameLink(Integer removeIndex) {
        for(Map.Entry<String,Integer> entry : this.indexNameLink.entrySet()){
            Integer index = entry.getValue();
            if(index > removeIndex){
                index = index - 1;
                this.indexNameLink.put(entry.getKey(),index);
            }
        }
    }

    @Override
    public Iterator<PropertySource<?>> iterator() {
        return this.propertySources.iterator();
    }


}