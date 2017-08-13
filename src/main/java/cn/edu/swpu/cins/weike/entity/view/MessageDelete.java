package cn.edu.swpu.cins.weike.entity.view;

import lombok.Data;

/**
 * Created by muyi on 17-7-16.
 */
@Data
public class MessageDelete {

    private int id;

    public MessageDelete(int id) {
        this.id = id;
    }

    public MessageDelete() {
    }
}
