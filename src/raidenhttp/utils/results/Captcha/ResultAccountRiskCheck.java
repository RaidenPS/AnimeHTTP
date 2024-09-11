package raidenhttp.utils.results.Captcha;

public class ResultAccountRiskCheck {
    public class GeeTestInfo {
        public String challenge;
        public String gt;
        public int new_captcha; // bool
        public int success;
    }

    public class DataInfo {
        public String id;
        public String action;
        public GeeTestInfo geetest = new GeeTestInfo();
    }

    public int retcode;
    public String message;
    public DataInfo data = new DataInfo();
}