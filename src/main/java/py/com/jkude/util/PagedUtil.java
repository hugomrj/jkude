package py.com.jkude.util;

import java.util.List;

public class PagedUtil<T> {
    private List<T> content;
    private long total;
    private int size;
    private int page;

    public PagedUtil(List<T> content, long total, int size, int page) {
        this.content = content;
        this.total = total;
        this.size = size;
        this.page = page;
    }

    // Getters y Setters
    public List<T> getContent() { return content; }
    public void setContent(List<T> content) { this.content = content; }

    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getTotalPages() {
        return size == 0 ? 1 : (int) Math.ceil((double) total / size);
    }
}