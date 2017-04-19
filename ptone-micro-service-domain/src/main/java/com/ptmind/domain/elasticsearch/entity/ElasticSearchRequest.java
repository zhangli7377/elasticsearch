package com.ptmind.domain.elasticsearch.entity;

import java.io.Serializable;

/**
 * Created by ptmind on 2015/9/9.
 */
public class ElasticSearchRequest implements Serializable{
    
    private static final long serialVersionUID = 1080352250114025854L;
    
    private String index;
    private String type;
    private String id;

    public ElasticSearchRequest(String index, String type)
    {
        this(index,type,"");
    }

    public ElasticSearchRequest(String index, String type, String id) {
        this.index = index;
        this.type = type;
        this.id = id;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
