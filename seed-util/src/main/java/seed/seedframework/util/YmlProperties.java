package seed.seedframework.util;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author hiyouka
 * @since JDK 1.8
 * @see Properties
 */
public class YmlProperties extends Hashtable<String,String> {

    private static final String KEY_VALUE_DELIMITER = ": ";

    private static final String DELIMITER = ":";

    private static final String KEY_DELIMITER = ".";

    private static final String NOTES_START = "##";

    private static final String RANK_SYMBOL = "  ";

    private static final int TOP_RANK = 0;

    public YmlProperties() {}

    public YmlProperties(InputStream inputStream){
        load(inputStream);
    }

    public YmlProperties(Reader reader){
        load(reader);
    }

    public synchronized String setProperty(String key,String value){
        return super.put(key,value);
    }

    public String getProperty(String key){
        return super.get(key);
    }

    public synchronized void load(InputStream inputStream){
        this.load(new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)));
    }

    public synchronized void load(Reader reader){
        this.load(new BufferedReader(reader));
    }

    private void load(BufferedReader lineReader){

        String line;
        LineWrapper previousLine = null;
        try{
            while((line = lineReader.readLine()) != null){
                line = filterLine(line);
                if(!needSkip(line)){
                    LineWrapper lineWrapper = new LineWrapper(line);
                    if(previousLine != null){
                        if(previousLine.isSameRank(lineWrapper)){
                            lineWrapper.setSuperiorLine(previousLine.getSuperiorLine());
                        }
                        else if(previousLine.higher(lineWrapper)){
                            lineWrapper.setSuperiorLine(previousLine);
                        }
                        else if(previousLine.lower(lineWrapper)){
                            LineWrapper supper = previousLine;
                            for(int i=0; i<(previousLine.getRank() - lineWrapper.getRank() + 1); i++){
                                supper = supper.getSuperiorLine();
                            }
                            lineWrapper.setSuperiorLine(supper);
                        }
                    }
                    if(lineWrapper.hasValue()){
                        this.setProperty(lineWrapper.getPropertiesKey(),lineWrapper.getValue());
                    }
                    previousLine = lineWrapper;
                }

            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 过滤注释
     */
    private String filterLine(String line){
        int index = line.indexOf(NOTES_START);
        if(index != -1){
            line = line.substring(0,index);
        }
        return line;
    }

    private boolean needSkip(String line){
        return !StringUtils.hasText(line);
    }

    private String getKey(String line){
        line = line.trim();
        return line.split(DELIMITER)[0];
    }


    static class IntegerWrapper{

        private volatile int value;

        IntegerWrapper(int value) {
            this.value = value;
        }

        public synchronized void set(int value) {
            this.value = value;
        }

        public int get() {
            return value;
        }
    }

    static class LineWrapper{

        private final String key;

        private final String value;

        private final int rank;

        private final String line;

        private LineWrapper superiorLine;

        private String propertiesKey;

        private LineWrapper(String line) {
            Assert.hasText(line,"line must not empty");
            this.line = line;
            this.key = line.trim().split(DELIMITER)[0];
            int index = this.line.indexOf(KEY_VALUE_DELIMITER);
            if(index != -1 && index != 0){
                this.value = line.split(KEY_VALUE_DELIMITER)[1];
            }else {
                value = null;
            }
            IntegerWrapper rankWrapper = new IntegerWrapper(TOP_RANK);
            rank(line,rankWrapper);
            this.rank = rankWrapper.get();
            if(isTop()){
                this.propertiesKey = this.key;
            }
        }

        boolean higher(LineWrapper lineWrapper){
            return this.rank < lineWrapper.getRank();
        }

        boolean lower(LineWrapper lineWrapper){
            return this.rank > lineWrapper.getRank();
        }

        boolean isSameRank(LineWrapper lineWrapper){
            return this.rank == lineWrapper.getRank();
        }

        boolean isTop(){
            return this.rank == TOP_RANK;
        }

        int getRank(){
            return this.rank;
        }

        public String getLine() {
            return line;
        }

        String getKey(){
            return this.key;
        }

        String getValue(){
            return this.value;
        }

        boolean hasValue(){
            return StringUtils.hasText(this.value);
        }

        String getPropertiesKey(){
            return this.propertiesKey;
        }

        private void rank(String line, IntegerWrapper rank){
            int index = line.indexOf(RANK_SYMBOL,rank.get());
            if(index != -1){
                rank.set(index + RANK_SYMBOL.length());
                rank(line,rank);
            }else {
                rank.set(rank.get()/RANK_SYMBOL.length());
            }
        }

        void setSuperiorLine(LineWrapper lineWrapper){
            if(isTop()){
                return;
            }
            this.superiorLine = lineWrapper;
            this.propertiesKey = lineWrapper.getPropertiesKey() + KEY_DELIMITER + this.key;
        }

        LineWrapper getSuperiorLine(){
            return this.superiorLine;
        }

        boolean isSuperLine(LineWrapper lineWrapper){
            if(isTop()){
                return false;
            }
            if(this.superiorLine != null){
                String propertiesKey = this.superiorLine.getPropertiesKey();
                if(StringUtils.hasText(propertiesKey)){
                    return propertiesKey.equals(lineWrapper.getPropertiesKey());
                }
            }
            return false;
        }


    }

}