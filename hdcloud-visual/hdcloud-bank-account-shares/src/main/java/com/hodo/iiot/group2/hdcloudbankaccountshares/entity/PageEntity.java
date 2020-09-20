package com.hodo.iiot.group2.hdcloudbankaccountshares.entity;

//分页处理的参数
public class PageEntity {
    private static final long serialVersionUID = 1L;
    Integer page;
    Integer limit;

    public PageEntity(Integer page, Integer limit) {
        this.page = page;
        this.limit = limit;

    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
