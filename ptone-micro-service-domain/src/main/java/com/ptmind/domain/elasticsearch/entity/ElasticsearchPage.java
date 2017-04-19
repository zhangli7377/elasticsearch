package com.ptmind.domain.elasticsearch.entity;

import java.io.Serializable;

/**
 * Created by ptmind on 2015/9/9.
 */
public class ElasticsearchPage implements Serializable {
    
    private static final long serialVersionUID = -8497314389957363390L;
    
    private Integer start;
    private Integer size;

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }


}
