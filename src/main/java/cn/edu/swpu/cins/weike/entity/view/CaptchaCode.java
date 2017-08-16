package cn.edu.swpu.cins.weike.entity.view;

import lombok.Data;

@Data
public class CaptchaCode {

    private String Cpatcha;
    private byte[] img;

    public CaptchaCode(String Captcha, byte[] img) {
        this.Cpatcha = Captcha;
        this.img = img;
    }

    public CaptchaCode() {
    }

}
