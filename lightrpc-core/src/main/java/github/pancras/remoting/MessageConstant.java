package github.pancras.remoting;

/**
 * @author PancrasL
 */
public interface MessageConstant {
    byte REQUEST_TYPE = 1;
    byte RESPONSE_TYPE = 2;
    int SUCCESS_FLAG = 1;
    int FAIL_FLAG = -1;
    String SUCESS = "success";
    String FAIL = "fail";
}
