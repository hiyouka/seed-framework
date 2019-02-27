package hiyouka.seedframework.constant;

import hiyouka.seedframework.exception.FileSizeOutException;
import hiyouka.seedframework.util.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author hiyouka
 * Date: 2019/2/12
 * @since JDK 1.8
 */
public class SeedConstant implements LogoConstant , EncodeConstant{

    private static final int MAX_LOGO_SIZE = 10240;

    @Override
    public String getLogo() {
        boolean isConfig = true;
        File file = null;
        try {
            file = FileUtils.getFile("classpath:" + LogoConstant.LOGO_FILE);
            if(!file.exists())
                isConfig = false;
            long length = file.length();
            if(length > MAX_LOGO_SIZE) {
                throw new FileSizeOutException();
            }

        } catch (FileNotFoundException e) {
            isConfig = false;
        }
        if (isConfig){
            return FileUtils.getFileContent(file).toString();
        }
        return LogoConstant.LOGO;
    }

}