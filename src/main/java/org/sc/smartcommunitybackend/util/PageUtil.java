package org.sc.smartcommunitybackend.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * 分页工具类
 */
public class PageUtil {

    /**
     * 创建分页对象
     *
     * @param page 页码（从0开始）
     * @param size 每页大小
     * @return Pageable对象
     */
    public static Pageable createPageable(Integer page, Integer size) {
        return PageRequest.of(page, size);
    }

    /**
     * 创建分页对象（带排序）
     *
     * @param page 页码（从0开始）
     * @param size 每页大小
     * @param sort 排序对象
     * @return Pageable对象
     */
    public static Pageable createPageable(Integer page, Integer size, Sort sort) {
        return PageRequest.of(page, size, sort);
    }

    /**
     * 创建分页对象（按字段排序）
     *
     * @param page 页码（从0开始）
     * @param size 每页大小
     * @param direction 排序方向
     * @param properties 排序字段
     * @return Pageable对象
     */
    public static Pageable createPageable(Integer page, Integer size, Sort.Direction direction, String... properties) {
        return PageRequest.of(page, size, Sort.by(direction, properties));
    }

    /**
     * 将Page对象转换为分页结果
     *
     * @param page Page对象
     * @param <T> 数据类型
     * @return 分页结果
     */
    public static <T> PageResult<T> toPageResult(Page<T> page) {
        PageResult<T> result = new PageResult<>();
        result.setContent(page.getContent());
        result.setTotalElements(page.getTotalElements());
        result.setTotalPages(page.getTotalPages());
        result.setPage(page.getNumber());
        result.setSize(page.getSize());
        result.setNumberOfElements(page.getNumberOfElements());
        return result;
    }

    /**
     * 分页结果封装类
     */
    public static class PageResult<T> {
        private List<T> content;
        private long totalElements;
        private int totalPages;
        private int page;
        private int size;
        private int numberOfElements;

        // Getters and Setters
        public List<T> getContent() {
            return content;
        }

        public void setContent(List<T> content) {
            this.content = content;
        }

        public long getTotalElements() {
            return totalElements;
        }

        public void setTotalElements(long totalElements) {
            this.totalElements = totalElements;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getNumberOfElements() {
            return numberOfElements;
        }

        public void setNumberOfElements(int numberOfElements) {
            this.numberOfElements = numberOfElements;
        }
    }
}

