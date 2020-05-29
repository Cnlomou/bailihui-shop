package com.bailihui.shop.util;

import java.util.List;

/**
 * @param <T>
 * @author sjh
 */
public class Page<T> {

    private static final int PAGE_SIZE=10;
    //总数
    private long total;
    //当前页的数据
    private List<T> contents;
    //最前页 不是第一页
    private long ppage;
    //当前页
    private long currentPage;
    //最后页 不是最后一页
    private long lpage;
    //总页数
    private long pageCount;
    //页大小
    private int pageSize=PAGE_SIZE;
    //偏移量
    private int offset = 2;

    //上一页
    private long uperPage;
    //下一页
    private long nextPage;

    public Page(long total, long currentPage, List<T> ls) {
        this(total, currentPage,PAGE_SIZE, ls);
        this.contents = ls;
    }

    public Page(long total, long currentPage, int pageSize,List<T> ls) {
        this.total = total;
        this.pageSize = pageSize;
        this.contents=ls;
        initData(currentPage);

    }

    private void initData(long currentPage) {

        //计算出总页数
        this.pageCount = (total + pageSize - 1) / pageSize;

        //带当前页赋值
        if (currentPage > pageCount)
            this.currentPage = pageCount;
        else
            this.currentPage = currentPage < 1 ? 1 : currentPage;

        //算出显示的第一页
        this.ppage = Math.max(currentPage - offset, 1);
        //算出显示的最后一页
        this.lpage = Math.min(ppage + 2 * offset, pageCount);

        //调整显示的第一页
        if (lpage - ppage < 2 * offset && (lpage - 2 * offset > 0))
            ppage = lpage - offset * 2;

        //算出上一页、下一页
        this.uperPage = Math.max(currentPage - 1, 1);
        this.nextPage = Math.min(currentPage + 1, pageCount);
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getContents() {
        return contents;
    }

    public void setContents(List<T> ls) {
        this.contents = ls;
    }

    public long getPpage() {
        return ppage;
    }

    public void setPpage(long ppage) {
        this.ppage = ppage;
    }

    public long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(long currentPage) {
        this.currentPage = currentPage;
    }

    public long getLpage() {
        return lpage;
    }

    public void setLpage(long lpage) {
        this.lpage = lpage;
    }

    public long getPageCount() {
        return pageCount;
    }

    public void setPageCount(long pageCount) {
        this.pageCount = pageCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        if (offset < 1)
            return;
        this.offset = (int) Math.min(total / 2, offset);
    }

    public long getUperPage() {
        return uperPage;
    }

    public void setUperPage(long uperPage) {
        this.uperPage = uperPage;
    }

    public long getNextPage() {
        return nextPage;
    }

    public void setNextPage(long nextPage) {
        this.nextPage = nextPage;
    }
//    public static void main(String[] args) {
//        Page page = new Page(70,2,20);
//        System.out.println("最前页_"+page.getPpage()+"当前页_"+page.getCurrentPage()+"最后页_"+page.getLpage());
//    }
}
