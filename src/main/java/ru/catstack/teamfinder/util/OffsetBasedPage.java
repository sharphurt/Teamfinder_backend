package ru.catstack.teamfinder.util;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class OffsetBasedPage implements Pageable {
    private int limit;
    private int offset;
    private Sort sort;

    public OffsetBasedPage(int offset, int limit, Sort sort) {
        this.sort = sort;
        if (limit < 1) {
            throw new IllegalArgumentException("Limit must not be less than one!");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("Offset index must not be less than zero!");
        }
        this.limit = limit;
        this.offset = offset;
    }

    @Override
    public int getPageNumber() {
        return offset / limit;
    }

    @Override
    public int getPageSize() {
        return limit;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return new OffsetBasedPage(getPageSize(), (int) (getOffset() + getPageSize()), sort);
    }

    private Pageable previous() {
        return hasPrevious() ? new OffsetBasedPage(getPageSize(), (int) (getOffset() - getPageSize()), sort) : this;
    }

    @Override
    public Pageable previousOrFirst() {
        return hasPrevious() ? previous() : first();
    }

    @Override
    public Pageable first() {
        return new OffsetBasedPage(getPageSize(), 0, sort);
    }

    @Override
    public boolean hasPrevious() {
        return offset > limit;
    }
}