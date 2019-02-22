package hiyouka.seedframework.constant;

/**
 * @author hiyouka
 * Date: 2019/2/12
 * @since JDK 1.8
 */
public interface LogoConstant {

    String LOGO_FILE = "banner.txt";

    String LOGO = "\n" +
            "         ███████╗    ███████╗    ███████╗    ██████╗ \n" +
            "         ██╔════╝    ██╔════╝    ██╔════╝    ██╔══██╗\n" +
            "         ███████╗    █████╗      █████╗      ██║  ██║\n" +
            "         ╚════██║    ██╔══╝      ██╔══╝      ██║  ██║\n" +
            "         ███████║    ███████╗    ███████╗    ██████╔╝\n" +
            "         ╚══════╝    ╚══════╝    ╚══════╝    ╚═════╝ \n" ;

    default String getLogo(){
        return LOGO;
    }

}