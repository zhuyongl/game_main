package com.tencent.wxcloudrun.model.dto;

import lombok.Data;

@Data
public class PageRequest {

    private Integer page = 1;
    
    private Integer size = 10;
    
    public void setPage(Integer page) {
        if (page == null || page < 1) {
            this.page = 1;
        } else {
            this.page = page;
        }
    }
    
    public void setSize(Integer size) {
        if (size == null || size < 1) {
            this.size = 10;
        } else if (size > 100) {
            this.size = 100;
        } else {
            this.size = size;
        }
    }
}